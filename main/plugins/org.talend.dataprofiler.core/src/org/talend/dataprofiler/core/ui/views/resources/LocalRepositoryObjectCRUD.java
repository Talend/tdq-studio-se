// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.views.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.MoveObjectAction;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.helpers.ReportHelper.ReportType;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.ReportFileHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.dq.nodes.ConnectionRepNode;
import org.talend.dq.nodes.ContextRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.JrxmlTempSubFolderNode;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

/**
 * Local Repository Object CRUD. only when the project is local use this.
 * 
 */
public class LocalRepositoryObjectCRUD extends AbstractRepObjectCRUDAction {

    public LocalRepositoryObjectCRUD() {
    }

    private static final IPath PROJECT_FULL_PATH = ResourceManager.getRootProject().getFullPath();

    protected static Logger log = Logger.getLogger(LocalRepositoryObjectCRUD.class);

    private static final IRepositoryNode[] NO_RESOURCES = new IRepositoryNode[0];

    private IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    @Override
    public Boolean validateDrop(IRepositoryNode targetNode) {
        Boolean retStatus = Boolean.FALSE;
        for (IRepositoryNode res : getSelectedRepositoryNodes()) {
            switch (targetNode.getType()) {
            case SYSTEM_FOLDER:
            case SIMPLE_FOLDER:
                if (allowDND(res, targetNode)) {
                    return Boolean.TRUE;
                }
                break;
            case REPOSITORY_ELEMENT:
                if (res instanceof AnalysisRepNode && targetNode instanceof ReportRepNode) {
                    return Boolean.TRUE;
                }
                break;
            default:
                break;
            }
        }
        return retStatus;
    }

    /**
     * check whether DND is allowed.
     * 
     * @param sourceNode
     * @param targetNode
     * @return
     */
    private boolean allowDND(IRepositoryNode sourceNode, IRepositoryNode targetNode) {
        // TDQ-10579: fix The NPE when drag something to reference project item.
        if (EResourceConstant.REFERENCED_PROJECT.getName().equals(targetNode.getProperties(EProperties.LABEL))) {
            return false;
        }
        // TDQ-10579~

        // MOD klliu Bug TDQ-4330 if targetCount's length is 1,that means targetNode is the root and system node.
        // so there is not any operations on it,then the operation of DND is not allowed.
        IPath sourcePath = RepositoryNodeHelper.getPath(sourceNode);
        IPath targetPath = RepositoryNodeHelper.getPath(targetNode);
        int sourceCount = sourcePath.segmentCount();
        int targetCount = targetPath.segmentCount();
        ERepositoryObjectType sourceType = sourceNode.getContentType();
        ERepositoryObjectType targetentType = targetNode.getContentType();
        // there is a special case if target and source both are context then it is ok
        if ((sourceCount == 1 || targetCount == 1) && !allBeContextNode(sourceType, targetentType)) {
            return false;
        }

        if (sourceNode.equals(targetNode)) {
            return false;
        }

        // MOD qiongli 2012-4-24 TDQ-5127
        if (isForbidNode(sourceNode)) {
            return false;
        }
        // MOD klliu Bug TDQ-4444 2012-01-09
        // This need check the object type of source node is sub type of targetNode's
        // if it is,that is not allowed to drop.
        if (isSubTypeOfTargetNode(sourceType, targetentType)) {
            return false;
        }
        // ~

        // when the source and target is not the same type, can not drag
        if (!isSameType(sourceNode, targetNode)) {
            return false;
        }

        // can't drag an item in recycle bin
        if (ProxyRepositoryFactory.getInstance().getStatus(sourceNode.getObject()) == ERepositoryStatus.DELETED) {
            return false;
        }

        // when the sourceNode parent is targetNode, no need to do drag
        if (sourceNode.getParent().equals(targetNode)) {
            return false;
        }

        // when move the parent to child node,can not do drag
        RepositoryNode parentNode = targetNode.getParent();
        if (parentNode != null && parentNode.equals(sourceNode)) {
            return false;
        }

        // the specail check for folder
        if (sourceNode.getType() == ENodeType.SIMPLE_FOLDER) {
            if (!canMoveFolderNode(sourceNode, targetNode)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Judge whether both source and target nodes are context type
     * 
     * @param sourceNode
     * @param targetNode
     * @return
     */
    private boolean allBeContextNode(ERepositoryObjectType sourceType, ERepositoryObjectType targetentType) {
        if (ERepositoryObjectType.CONTEXT == sourceType && ERepositoryObjectType.CONTEXT == targetentType) {
            return true;
        }
        return false;
    }

    /**
     * get Selected Repository Nodes.
     * 
     * @return
     */
    protected IRepositoryNode[] getSelectedRepositoryNodes() {
        ISelection selection = getUISelection();
        if (selection == null) {
            return NO_RESOURCES;
        }
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj == null) {
            return NO_RESOURCES;
        }

        if (selection instanceof IStructuredSelection) {
            return getSelectedRepositoryNodes((IStructuredSelection) selection);
        }
        return NO_RESOURCES;
    }

    /**
     * get Selected Repository Nodes from the selection.
     * 
     * @param selection
     * @return
     */
    private IRepositoryNode[] getSelectedRepositoryNodes(IStructuredSelection selection) {
        ArrayList<IRepositoryNode> selectedRepositoryNodes = new ArrayList<IRepositoryNode>();
        List<?> list = selection.toList();
        for (Object o : list) {
            if (o instanceof IRepositoryNode) {
                selectedRepositoryNodes.add((IRepositoryNode) o);
            }
        }
        return selectedRepositoryNodes.toArray(new IRepositoryNode[selectedRepositoryNodes.size()]);
    }

    /**
     * check sourceNode whether has Locked Items.
     * 
     * @param sourceNode
     * @return boolean
     */
    private boolean haveLockedItems(IRepositoryNode sourceNode) {
        IRepositoryViewObject object = sourceNode.getObject();
        if (object != null) {
            // when it is locked, can not move.
            if (object instanceof Folder) {
                List<IRepositoryNode> children = object.getRepositoryNode().getChildren();
                if (children != null) {
                    for (IRepositoryNode childrenNode : children) {
                        if (haveLockedItems(childrenNode)) {
                            return true;
                        }
                    }
                }
            } else {
                if (MoveObjectAction.getInstance().isLock((RepositoryNode) sourceNode)) {
                    MessageDialog
                            .openWarning(
                                    PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                                    DefaultMessagesImpl.getString("RepositoyNodeDropAdapterAssistant.error.moveError"), DefaultMessagesImpl.getString("RepositoyNodeDropAdapterAssistant.error.moveIsLocked")); //$NON-NLS-1$ //$NON-NLS-2$
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * check whether the source node is the subtype of target node.
     * 
     * @param sourceNode
     * @param targetNode
     * @return
     */
    private boolean
            isSubTypeOfTargetNode(ERepositoryObjectType sourceObjectType, ERepositoryObjectType targetObjectType) {
        List<ERepositoryObjectType> targetNodeChildTypes = getTargetNodeChildTypes(targetObjectType);
        return targetNodeChildTypes.contains(sourceObjectType);
    }

    /**
     * get Target Node's all Child Types.
     * 
     * @param targetObjectType
     * @return
     */
    private List<ERepositoryObjectType> getTargetNodeChildTypes(ERepositoryObjectType targetObjectType) {
        List<ERepositoryObjectType> childObjectType = new ArrayList<ERepositoryObjectType>();
        // get Target node child types
        if (targetObjectType.equals(ERepositoryObjectType.TDQ_DATA_PROFILING)) {
            childObjectType.add(ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
            childObjectType.add(ERepositoryObjectType.TDQ_REPORT_ELEMENT);
        } else if (targetObjectType.equals(ERepositoryObjectType.TDQ_LIBRARIES)) {
            childObjectType.add(ERepositoryObjectType.TDQ_EXCHANGE);
            childObjectType.add(ERepositoryObjectType.TDQ_INDICATOR_ELEMENT);
            childObjectType.add(ERepositoryObjectType.TDQ_JRAXML_ELEMENT);
            childObjectType.add(ERepositoryObjectType.TDQ_PATTERN_ELEMENT);
            childObjectType.add(ERepositoryObjectType.TDQ_RULES);
            childObjectType.add(ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT);
        } else if (targetObjectType.equals(ERepositoryObjectType.TDQ_INDICATOR_ELEMENT)) {
            childObjectType.add(ERepositoryObjectType.TDQ_SYSTEM_INDICATORS);
            childObjectType.add(ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS);
        } else if (targetObjectType.equals(ERepositoryObjectType.TDQ_SYSTEM_INDICATORS)) {
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_ADVANCED_STATISTICS);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_BUSINESS_RULES);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_CORRELATION);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_FRAUDDETECTION);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_PATTERN_MATCHING);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_ROW_COMPARISON);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_SIMPLE_STATISTICS);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_SOUNDEX);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_SUMMARY_STATISTICS);
            childObjectType.add(ERepositoryObjectType.SYSTEM_INDICATORS_TEXT_STATISTICS);
        } else if (targetObjectType.equals(ERepositoryObjectType.TDQ_PATTERN_ELEMENT)) {
            childObjectType.add(ERepositoryObjectType.TDQ_PATTERN_REGEX);
            childObjectType.add(ERepositoryObjectType.TDQ_PATTERN_SQL);
        } else if (targetObjectType.equals(ERepositoryObjectType.TDQ_RULES)) {
            childObjectType.add(ERepositoryObjectType.TDQ_RULES_PARSER);
            childObjectType.add(ERepositoryObjectType.TDQ_RULES_SQL);
        } else if (targetObjectType.equals(ERepositoryObjectType.METADATA)) {
            childObjectType.add(ERepositoryObjectType.METADATA_CONNECTIONS);
            childObjectType.add(ERepositoryObjectType.METADATA_FILE_DELIMITED);
        }
        return childObjectType;
    }

    /**
     * check whether the node is forbidden to drag.
     * 
     * @param sourceNode
     * @return
     */
    private boolean isForbidNode(IRepositoryNode sourceNode) {
        ENodeType type = sourceNode.getType();
        // MOD xqliu 2012-05-22 TDQ-4831 allow user to drag Jrxml file
        boolean flag =
                (type != null && type == ENodeType.SYSTEM_FOLDER) || sourceNode instanceof ReportFileRepNode
                        || sourceNode instanceof ReportAnalysisRepNode;
        // ~ TDQ-4831
        if (!flag) {
            RepositoryNode parent = sourceNode.getParent();
            if (parent != null) {
                flag =
                        parent instanceof AnalysisRepNode
                                || parent instanceof ReportRepNode
                                || (parent instanceof AnalysisSubFolderRepNode && ((AnalysisSubFolderRepNode) parent)
                                        .getObject() == null);
            }
        }
        return flag;
    }

    @Override
    public Boolean handleDrop(IRepositoryNode targetNode) {

        Boolean isHandleOK = Boolean.FALSE;

        try {
            IRepositoryNode[] selectedRepositoryNodes = getSelectedRepositoryNodes();

            // do some checks before moving.
            for (IRepositoryNode sourceNode : selectedRepositoryNodes) {
                // MOD msjian 2012-10-23 TDQ-5614: when the node is locked, tell user can not move.
                if (haveLockedItems(sourceNode)) {
                    return isHandleOK;
                }
                // TDQ-5614 ~
            }

            // do move.
            moveRepositoryNodes(selectedRepositoryNodes, targetNode);

            // MOD gdbu 2011-11-17 TDQ-3969 : after move folder or items re-filter the tree , to create a new list .
            if (DQRepositoryNode.isOnFilterring()) {
                RepositoryNodeHelper.fillTreeList(null);
                RepositoryNodeHelper.setFilteredNode(RepositoryNodeHelper.getRootNode(
                        ERepositoryObjectType.TDQ_DATA_PROFILING, true));
            }
            isHandleOK = Boolean.TRUE;

        } catch (PersistenceException e) {
            if (log.isInfoEnabled()) {
                log.info(e.toString());
            }
            MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                    DefaultMessagesImpl.getString("RepositoyNodeDropAdapterAssistant.error.moveError"), e.getMessage()); //$NON-NLS-1$
            isHandleOK = Boolean.FALSE;

        }
        // MOD gdbu TDQ-3546 unload resource after move item.
        ProxyRepositoryManager.getInstance().refresh();
        return isHandleOK;
    }

    /**
     * move RepositoryNodes to the target RepositoryNode.
     * 
     * @param repositoryNodes
     * @param targetNode
     * @throws PersistenceException
     */
    private void moveRepositoryNodes(IRepositoryNode[] repositoryNodes, IRepositoryNode targetNode)
            throws PersistenceException {
        if (repositoryNodes != null) {
            for (IRepositoryNode sourceNode : repositoryNodes) {
                if (sourceNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
                    if (sourceNode instanceof AnalysisRepNode || sourceNode instanceof ConnectionRepNode) {
                        moveAnaConNode(sourceNode, targetNode);
                    } else if (sourceNode instanceof ReportRepNode) {
                        moveReportRepNode(sourceNode, targetNode);
                    } else if (sourceNode instanceof ContextRepNode) {
                        moveContextNode(sourceNode, targetNode);
                    } else {
                        IPath makeRelativeTo = getMakeRelativeTo(sourceNode);
                        IPath removeLastSegments = makeRelativeTo.removeLastSegments(1);
                        if (sourceNode instanceof SysIndicatorDefinitionRepNode) {
                            // SystemIndicatorDefinition can't be moved
                            if (!((SysIndicatorDefinitionRepNode) sourceNode).isSystemIndicator()) {
                                moveOthersNode(sourceNode, targetNode, removeLastSegments);
                            }
                        } else if (sourceNode instanceof PatternRepNode) {
                            moveOthersNode(sourceNode, targetNode, removeLastSegments);
                        } else if (sourceNode instanceof JrxmlTempleteRepNode) {
                            moveJrxmlFileRepNode(sourceNode, targetNode, makeRelativeTo);
                        } else if (sourceNode instanceof SourceFileRepNode || sourceNode instanceof RuleRepNode) {
                            moveOthersNode(sourceNode, targetNode, makeRelativeTo);
                        } else {
                            moveOthersNode(sourceNode, targetNode, removeLastSegments);
                        }
                    }
                } else if (sourceNode.getType() == ENodeType.SIMPLE_FOLDER) {
                    moveFolderRepNode(sourceNode, targetNode);
                }
                // refresh the dq repository tree view
                CorePlugin.getDefault().refreshDQView(targetNode.getParent());
                // MOD qiongli 2012-4-23,only refresh the parent of source node at here.
                CorePlugin.getDefault().refreshDQView(sourceNode.getParent());
            }
        }
    }

    /**
     * move Jrxml File. MOD yyin 20130123 TDQ-5392
     * 
     * @param sourceNode
     * @param targetNode
     * @param makeRelativeTo
     * @throws PersistenceException
     */
    private void moveJrxmlFileRepNode(IRepositoryNode sourceNode, IRepositoryNode targetNode, IPath makeRelativeTo)
            throws PersistenceException {
        //MessageUI.openWarning(DefaultMessagesImpl.getString("JrxmlFileAction.forbiddenOperation")); //$NON-NLS-1$
        // remeber the old path
        String filename = RepNodeUtils.getSeparator() + RepositoryNodeHelper.getFileNameOfTheNode(sourceNode);
        IPath oldPath = RepositoryNodeHelper.getPath(sourceNode).append(filename);

        // move the jrxml file
        this.moveObject(sourceNode, targetNode, makeRelativeTo);

        // update the depended reports
        RepNodeUtils.updateJrxmlRelatedReport(oldPath,
                RepositoryNodeHelper.getPath(targetNode).append(RepNodeUtils.getSeparator()).append(filename));
    }

    /**
     * when move Analysis, Connection node, use this method.
     * 
     * @param sourceNode
     * @param targetNode
     */
    private void moveAnaConNode(IRepositoryNode sourceNode, IRepositoryNode targetNode) {
        moveDQNode(sourceNode, targetNode);
    }

    /**
     * when move Analysis, Connection, Context node, use this method.
     * 
     * @param sourceNode
     * @param targetNode
     */
    protected void moveDQNode(IRepositoryNode sourceNode, IRepositoryNode targetNode) {
        // MOD yyin 20121127, TDQ-6302, when back from DI's expanded Metadata, can not move the connections.
        final IRepositoryNode finalSourceNode = sourceNode;
        final IRepositoryNode finalTargetNode = targetNode;
        if (finalTargetNode.getType() == ENodeType.SIMPLE_FOLDER) {
            moveObject(finalSourceNode, finalTargetNode, getMakeRelativeTo(finalSourceNode));
        } else if (finalTargetNode.getType() == ENodeType.SYSTEM_FOLDER) {
            moveObject(finalSourceNode, finalTargetNode, Path.EMPTY);
        }
    }

    /**
     * when move Context node, use this method.
     * 
     * @param sourceNode
     * @param targetNode
     */
    private void moveContextNode(IRepositoryNode sourceNode, IRepositoryNode targetNode) {
        moveDQNode(sourceNode, targetNode);
    }

    /**
     * get Source Node Full path Relative To project full path.
     * 
     * @param sourceNode
     * @return
     */
    private IPath getMakeRelativeTo(IRepositoryNode sourceNode) {
        IPath fullPath = getFullPathFormObjectType(sourceNode.getContentType());
        IPath makeRelativeTo = fullPath.makeRelativeTo(PROJECT_FULL_PATH);
        return makeRelativeTo;
    }

    /**
     * move the report node (move the report generate doc folder also).
     * 
     * @param sourceNode
     * @param targetNode
     * @throws PersistenceException
     */
    private void moveReportRepNode(IRepositoryNode sourceNode, IRepositoryNode targetNode) throws PersistenceException {
        // MOD yyi 2012-02-22:TDQ-4545 Update user define jrxml template path.
        relocateJrxmlTemplates(sourceNode, targetNode);

        // get the report generated doc folder
        IFolder outputFolder = ReportFileHelper.getOutputFolder((ReportRepNode) sourceNode);

        moveAnaConNode(sourceNode, targetNode);

        // move the report generated doc folder
        File srcFolder = WorkspaceUtils.ifolderToFile(outputFolder);
        IFolder targetFolder = RepositoryNodeHelper.getIFolder(targetNode);
        File tarFolder = WorkspaceUtils.ifolderToFile(targetFolder);
        ReportFileHelper.moveReportGeneratedDocFolder(srcFolder, tarFolder);
    }

    /**
     * update user define jrxml template path.
     * 
     * @param sourceNode
     * @param targetNode
     * @link {@link #moveReportRepNode(IRepositoryNode, IRepositoryNode)}
     */
    private void relocateJrxmlTemplates(IRepositoryNode sourceNode, IRepositoryNode targetNode) {
        IPath targetPath = RepositoryNodeHelper.getPath(targetNode);
        IFolder targetFolder = ResourceManager.getRootProject().getFolder(targetPath);

        TdReport rep = (TdReport) RepositoryNodeHelper.getModelElementFromRepositoryNode(sourceNode);
        IPath jrxmlPath = null;
        IPath relativePath = null;
        ReportType reportType = null;
        for (AnalysisMap anaMap : rep.getAnalysisMap()) {
            reportType = ReportHelper.ReportType.getReportType(anaMap.getAnalysis(), anaMap.getReportType());
            // Relocate the jrxml template path for user define type.
            if (ReportHelper.ReportType.USER_MADE.equals(reportType)) {
                jrxmlPath =
                        RepResourceFileHelper
                                .findCorrespondingFile(rep)
                                .getParent()
                                .getLocation()
                                .append(anaMap.getJrxmlSource());
                relativePath = jrxmlPath.makeRelativeTo(targetFolder.getLocation().append("/")); //$NON-NLS-1$
                anaMap.setJrxmlSource(relativePath.toString());
            }
        }
    }

    /**
     * when move Common, Source file, Pattern, UDI, Rule, use this method.
     * 
     * @param sourceNode
     * @param targetNode
     * @param removeLastSegments
     */
    private void moveOthersNode(IRepositoryNode sourceNode, IRepositoryNode targetNode, IPath ipath) {
        if (targetNode.getType() == ENodeType.SIMPLE_FOLDER || targetNode.getType() == ENodeType.SYSTEM_FOLDER) {
            moveObject(sourceNode, targetNode, ipath);
        }
    }

    /**
     * check can move folder node or not.
     * 
     * @param sourceNode
     * @param targetNode
     * @return
     */
    private boolean canMoveFolderNode(IRepositoryNode sourceNode, IRepositoryNode targetNode) {
        String sourceNodeRelPath = RepositoryNodeHelper.getPath(sourceNode).toString().trim() + IPath.SEPARATOR;
        String targetNodeRelPath = RepositoryNodeHelper.getPath(targetNode).toString().trim() + IPath.SEPARATOR;
        if (sourceNodeRelPath.length() > targetNodeRelPath.length()) {
            // Move a child node to parent node or child node to move to other branches, allowing moving.
            return true;
        }
        if (sourceNodeRelPath.equals(targetNodeRelPath.substring(0, sourceNodeRelPath.length()))) {
            // Move a parent node to child node is not allowed.
            return false;
        }
        return true;
    }

    /**
     * move Folder Node.
     * 
     * @param sourceNode
     * @param targetNode
     * @throws PersistenceException
     */
    private void moveFolderRepNode(IRepositoryNode sourceNode, IRepositoryNode targetNode) throws PersistenceException {
        // MOD yyin 20130125 TDQ-5392
        IPath oldPath = null;
        List<String> jrxmlFileNames = null;
        List<JrxmlTempleteRepNode> jrxmlFileRepNodes = new ArrayList<JrxmlTempleteRepNode>();

        if (sourceNode instanceof JrxmlTempSubFolderNode) {
            // remeber the old path
            oldPath = RepositoryNodeHelper.getPath(sourceNode);

            // find all jrxml files in this folder
            jrxmlFileRepNodes = RepositoryNodeHelper.getJrxmlFileRepNodes(sourceNode, true);
            // remember the files name
            jrxmlFileNames = RepNodeUtils.getListOfJrxmlNameWithPath(oldPath, jrxmlFileRepNodes);
        }
        // ~

        moveFolder(sourceNode, targetNode);

        // ADD yyin 20130125 TDQ-5392
        if (sourceNode instanceof JrxmlTempSubFolderNode) {
            // use two array :old file names and new file names, to call the method.
            IPath newPath = RepositoryNodeHelper.getPath(targetNode);
            List<String> jrxmlFileNamesAfterMove = new ArrayList<String>();
            for (JrxmlTempleteRepNode jrxml : jrxmlFileRepNodes) {
                // check it there some sub folder under the source node
                IPath relativeTo = RepositoryNodeHelper.getPath(jrxml.getParent()).makeRelativeTo(oldPath);
                IPath tempPath =
                        newPath.append(RepNodeUtils.getSeparator())
                                .append(sourceNode.getLabel())
                                .append(RepNodeUtils.getSeparator());
                if (relativeTo.segmentCount() > 0) {// if there are some sub folder under the source node
                    tempPath = tempPath.append(relativeTo).append(RepNodeUtils.getSeparator());
                }
                jrxmlFileNamesAfterMove.add(tempPath
                        .append(RepositoryNodeHelper.getFileNameOfTheNode(jrxml))
                        .toOSString());
            }
            // update the depended reports
            RepNodeUtils.updateJrxmlRelatedReport(jrxmlFileNames, jrxmlFileNamesAfterMove);
        }// ~
    }

    /**
     * check whether sourceNode and targetNode is the same Type.
     * 
     * @param sourceNode
     * @param targetNode
     * @return
     */
    private boolean isSameType(IRepositoryNode sourceNode, IRepositoryNode targetNode) {
        // MOD klliu Bug TDQ-4444 2012-01-09
        // if sourceNode and targetNode have a same root node, also need to check the node's object type is same.
        // MOD qiongli TDQ-5127 avoid NPE
        ERepositoryObjectType sourceType = sourceNode.getContentType();
        ERepositoryObjectType targetentType = targetNode.getContentType();
        if (sourceType == null || targetentType == null || !sourceType.equals(targetentType)) {
            return false;
        }
        // ~

        // check root node
        IPath sourcePath = RepositoryNodeHelper.getFirstSystemPath(sourceNode);
        IPath targetPath = RepositoryNodeHelper.getFirstSystemPath(targetNode);
        String sourceString = sourcePath.toOSString();
        String targetString = targetPath.toOSString();
        return sourceString.equals(targetString);
    }

    /**
     * get FullPath from objectType.
     * 
     * @param objectType
     * @return
     */
    private IPath getFullPathFormObjectType(ERepositoryObjectType objectType) {
        IPath fullPath = null;
        if (objectType == ERepositoryObjectType.TDQ_JRAXML_ELEMENT) {
            fullPath = ResourceManager.getJRXMLFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_PATTERN_REGEX) {
            fullPath = ResourceManager.getPatternRegexFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_PATTERN_SQL) {
            fullPath = ResourceManager.getPatternSQLFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT) {
            fullPath = ResourceManager.getSourceFileFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_RULES_SQL) {
            fullPath = ResourceManager.getRulesSQLFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS) {
            fullPath = ResourceManager.getUDIFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.METADATA_CONNECTIONS) {
            fullPath = ResourceManager.getConnectionFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT) { // gdbu 2011-3-16 bug 19537
            fullPath = ResourceManager.getAnalysisFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_REPORT_ELEMENT) {
            fullPath = ResourceManager.getReportsFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.METADATA_FILE_DELIMITED) {
            fullPath = ResourceManager.getFileDelimitedFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_RULES_PARSER) {
            fullPath = ResourceManager.getRulesParserFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.TDQ_RULES_MATCHER) {
            fullPath = ResourceManager.getRulesMatcherFolder().getFullPath();
        } else if (objectType == ERepositoryObjectType.CONTEXT) {
            fullPath = ResourceManager.getContextFolder().getFullPath();
        }
        return fullPath;
    }

    /**
     * move folder.
     * 
     * @param sourceNode
     * @param targetNode
     * @throws PersistenceException
     */
    private void moveFolder(final IRepositoryNode sourceNode, final IRepositoryNode targetNode)
            throws PersistenceException {
        IPath sourcePath = RepositoryNodeHelper.getPath(sourceNode);
        IPath targetPath = RepositoryNodeHelper.getPath(targetNode);
        IPath makeRelativeTo = getMakeRelativeTo(targetNode);
        final IPath sourceMakeRelativeTo = sourcePath.makeRelativeTo(makeRelativeTo);
        final IPath targetMakeRelativeTo = targetPath.makeRelativeTo(makeRelativeTo);
        RepositoryWorkUnit<Object> workUnit =
                new RepositoryWorkUnit<Object>(DefaultMessagesImpl.getString("LocalRepositoryObjectCRUD.MOVE_FOLDER") //$NON-NLS-1$
                        + sourceNode.getObject().getProperty().getDisplayName()) {

                    @Override
                    protected void run() {

                        IWorkspace workspace = ResourcesPlugin.getWorkspace();
                        IWorkspaceRunnable operation = new IWorkspaceRunnable() {

                            public void run(IProgressMonitor monitor) throws CoreException {
                                try {
                                    factory.moveFolder(targetNode.getContentType(), sourceMakeRelativeTo,
                                            targetMakeRelativeTo);
                                } catch (PersistenceException e) {
                                    log.error(e.getMessage(), e);
                                }
                            }
                        };
                        try {
                            workspace.run(operation, new NullProgressMonitor());
                        } catch (CoreException e) {
                            log.error(e, e);
                        }
                    }
                };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }

    /**
     * move the IRepositoryViewObject from the sourceNode to targetNode, don't refresh the source and target node, user
     * need to refresh the dq repository view tree by hand.
     * 
     * @param sourceNode
     * @param targetNode
     * @param basePath
     */
    private void moveObject(final IRepositoryNode sourceNode, final IRepositoryNode targetNode, final IPath basePath) {
        final IPath targetPath = RepositoryNodeHelper.getPath(targetNode);
        RepositoryWorkUnit<Object> workUnit =
                new RepositoryWorkUnit<Object>(DefaultMessagesImpl.getString("LocalRepositoryObjectCRUD.MOVE_ITEM") //$NON-NLS-1$
                        + sourceNode.getObject().getProperty().getDisplayName()) {

                    @Override
                    protected void run() {

                        IWorkspace workspace = ResourcesPlugin.getWorkspace();
                        IWorkspaceRunnable operation = new IWorkspaceRunnable() {

                            public void run(IProgressMonitor monitor) throws CoreException {
                                IPath makeRelativeTo = Path.EMPTY;
                                if (!basePath.isEmpty()) {
                                    makeRelativeTo = targetPath.makeRelativeTo(basePath);
                                }
                                try {
                                    factory.moveObject(sourceNode.getObject(), makeRelativeTo, Path.EMPTY);
                                } catch (PersistenceException e) {
                                    log.error(sourceNode, e);
                                } catch (BusinessException e) {
                                    log.error(sourceNode, e);
                                }
                            }
                        };
                        try {
                            workspace.run(operation, new NullProgressMonitor());
                        } catch (CoreException e) {
                            log.error(e, e);
                        }
                    }
                };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);

    }

    @Override
    public Boolean handleRenameFolder(IRepositoryNode repositoryNode) {
        // ~ TDQ-4831
        // Added yyin 20120712 TDQ-5721 when rename the sql file folder with file opening, should inform
        if (repositoryNode instanceof SourceFileSubFolderNode) {
            ReturnCode rc =
                    WorkspaceResourceHelper
                            .checkSourceFileSubFolderNodeOpening((SourceFileSubFolderNode) repositoryNode);
            if (rc.isOk()) {
                WorkspaceResourceHelper.showSourceFilesOpeningWarnMessages(rc.getMessage());
                return Boolean.TRUE;
            }
        }// ~

        return Boolean.TRUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUD#getUISelection()
     */
    @Override
    public ISelection getUISelection() {
        ISelection sel = null;
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow != null) {
            IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
            if (activePage != null) {
                IWorkbenchPart activePart = activePage.getActivePart();
                if (activePart != null) {
                    if (activePart instanceof DQRespositoryView) {
                        sel = ((DQRespositoryView) activePart).getCommonViewer().getSelection();
                    }
                }
            }
        }
        return sel;
    }

    /**
     * refresh Workspace and DQView.
     */
    protected void refreshWorkspaceDQView() {
        IWorkbenchPart activePart =
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
        if (activePart instanceof DQRespositoryView) {
            ((DQRespositoryView) activePart).refresh();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction#showWarningDialog()
     */
    public void showWarningDialog() {
        MessageDialog
                .openWarning(
                        PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                        DefaultMessagesImpl.getString("LocalRepositoryObjectCRUD.Title"), DefaultMessagesImpl.getString("LocalRepositoryObjectCRUD.Content")); //$NON-NLS-1$ //$NON-NLS-2$  
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUD#refreshDQViewForRemoteProject()
     */
    @Override
    public void refreshDQViewForRemoteProject() {
        // in local project, needn't to refresh
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction#isSelectionAvailable()
     */
    public Boolean isSelectionAvailable() {
        return true;
    }
}
