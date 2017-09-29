// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.exception.ExceptionFactory;
import org.talend.dataprofiler.core.exception.ExceptionHandler;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.handle.ActionHandleFactory;
import org.talend.dataprofiler.core.ui.action.actions.handle.IDuplicateHandle;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dataquality.properties.TDQFileItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class DuplicateAction extends org.talend.core.repository.ui.actions.DuplicateAction {

    private IRepositoryNode[] nodeArray = new IRepositoryNode[0];

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    private Logger log = Logger.getLogger(DuplicateAction.class);

    public DuplicateAction(IRepositoryNode[] nodeArray) {
        super();
        this.nodeArray = nodeArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#doRun()
     */
    @Override
    protected void doRun() {
        repositoryObjectCRUD.refreshDQViewForRemoteProject();

        // ADD msjian TDQ-7006 2013-7-24: after refresh get the selection to check.
        if (!repositoryObjectCRUD.isSelectionAvailable()) {
            repositoryObjectCRUD.showWarningDialog();
            return;
        }
        // TDQ-7006~

        try {
            String newLabel = null;
            Item lastDuplicateItem = null;
            for (final IRepositoryNode node : nodeArray) {
                IRepositoryViewObject viewObject = node.getObject();

                validateOriginalObject(viewObject);

                final IDuplicateHandle handle = ActionHandleFactory.getInstance().createDuplicateHandle(node);

                if (handle != null) {
                    // MOD msjian TDQ-4672 2012-2-17: modified the check duplicate name method
                    String initLabel = generateInitialLabel(node);
                    InputDialog dialog = createInputNewNameDialog(node, initLabel);
                    // TDQ-4672~
                    if (dialog.open() == Window.OK) {
                        newLabel = dialog.getValue().trim();

                        lastDuplicateItem = handle.duplicateItem(viewObject.getProperty().getItem(), newLabel);
                    }
                } else {
                    // if can not find the related handler for the current node, log it and continue for others
                    log.error(DefaultMessagesImpl.getString("DuplicateAction.HandleNull", node.getLabel()));//$NON-NLS-1$
                }
            }
            // if the user select cancel, the item will be null, then no need to refresh.
            if (lastDuplicateItem != null) {
                CorePlugin.getDefault().refreshWorkSpace();
                // show the last new success duplicated one as selected on the repository view.
                selectAndReveal(newLabel, lastDuplicateItem);
            }
        } catch (BusinessException e) {
            ExceptionHandler.process(e, Level.FATAL);
        }
    }

    private InputDialog createInputNewNameDialog(final IRepositoryNode node, String initLabel) {
        return new InputDialog(
                CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
                DefaultMessagesImpl.getString("DuplicateAction.InputDialog"), DefaultMessagesImpl.getString("DuplicateAction.InpurtDesc"), initLabel, //$NON-NLS-1$ //$NON-NLS-2$
                new IInputValidator() {

                    public String isValid(String newText) {
                        // ADD msjian TDQ-7579: fix the name can not be empty
                        if (PluginConstant.EMPTY_STRING.equals(newText.trim())) {
                            return DefaultMessagesImpl.getString("DuplicateAction.LabelEmpty"); //$NON-NLS-1$
                        }
                        // TDQ-7579~

                        // MOD msjian TDQ-7218 2013-5-31: when dulicate a system indicator, should check
                        // whether exist in UDI.
                        ERepositoryObjectType contentType = node.getContentType();
                        if (node instanceof SysIndicatorDefinitionRepNode) {
                            contentType = ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS;
                        }
                        if (PropertyHelper.existDuplicateName(newText.trim(), null, contentType)) {
                            return DefaultMessagesImpl.getString("DuplicateAction.LabelExists"); //$NON-NLS-1$
                        }
                        // TDQ-7218~

                        return null;
                    }
                });
    }

    private void validateOriginalObject(IRepositoryViewObject viewObject) throws BusinessException {
        if (viewObject == null || viewObject.getProperty() == null || viewObject.getProperty().getItem() == null) {
            BusinessException createBusinessException = ExceptionFactory.getInstance().createBusinessException(
                    DefaultMessagesImpl.getString("DuplicateAction.ObjectNull")); //$NON-NLS-1$
            throw createBusinessException;
        }
    }

    /**
     * DOC msjian Comment method "generateInitialLabel".
     * 
     * check the duplicate name by node.
     * 
     * @param node
     * @return
     */
    private String generateInitialLabel(IRepositoryNode node) {
        String initNameValue = "Copy_of_" + node.getLabel(); //$NON-NLS-1$
        ERepositoryObjectType type = node.getContentType();
        if (!PropertyHelper.existDuplicateName(initNameValue, null, type)) {
            return initNameValue;
        } else {
            char j = 'a';
            String temp = initNameValue;

            while (PropertyHelper.existDuplicateName(temp, null, type)) {
                if (j <= 'z') {
                    temp = initNameValue + "_" + (j++) + ""; //$NON-NLS-1$ //$NON-NLS-2$
                }

            }
            return temp;
        }
    }

    /**
     * DOC bZhou Comment method "selectAndReveal".
     * 
     * Selects and reveals the newly added resource in all parts of the active workbench window's active page.
     * 
     * @param newLabel
     * 
     * @param duplicateObject
     * @throws BusinessException
     */
    private void selectAndReveal(String newLabel, Item duplicateItem) throws BusinessException {
        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = workbenchWindow.getActivePage();
        IWorkbenchPart activePart = page.getActivePart();

        RepositoryNode recursiveFind = null;
        recursiveFind = getSelctionNode(newLabel, duplicateItem.getProperty());
        if (recursiveFind != null) {
            if (recursiveFind instanceof AnalysisRepNode || recursiveFind instanceof AnalysisSubFolderRepNode
                    || recursiveFind instanceof ReportRepNode || recursiveFind instanceof ReportSubFolderRepNode) {

                CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.findNearestSystemFolderNode(recursiveFind));
            } else {
                CorePlugin.getDefault().refreshDQView(recursiveFind.getParent());
                refreshHiveConnectionParent(recursiveFind);
            }
            // MOD qiongli TDQ-5391 Avoid 'recursiveFind' to casue NPE .
            if (activePart instanceof ISetSelectionTarget) {
                ISelection selection = new StructuredSelection(recursiveFind);
                ((ISetSelectionTarget) activePart).selectReveal(selection);
            }
        }

    }

    /**
     * if the duplicate hive connection has its related hadoop cluster, need to refresh it.
     * 
     * @param recursiveFind
     */
    private void refreshHiveConnectionParent(RepositoryNode recursiveFind) {
        if (recursiveFind instanceof DBConnectionRepNode) {
            String hcId = ConnectionUtils.getHadoopClusterIDOfHive(recursiveFind.getObject());
            if (!StringUtils.isBlank(hcId)) {
                IRepositoryNode HClusterFolderNode = RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.HADOOP_CLUSTER);
                List<IRepositoryNode> children = HClusterFolderNode.getChildren();
                for (IRepositoryNode hcluster : children) {
                    if (StringUtils.equals(hcId, hcluster.getId())) {
                        CorePlugin.getDefault().refreshDQView(hcluster);
                        break;
                    }
                }
            }
        }
    }

    private RepositoryNode getSelctionNode(String newLabel, Property property) throws BusinessException {
        RepositoryNode recursiveFind = null;
        if (property != null) {
            if (property.getItem() instanceof TDQFileItem) {
                // if the model element is null, means that it is a file item.
                recursiveFind = findNodeForTDQFileItem(newLabel);
            } else {
                // find the related node by the model element
                recursiveFind = RepositoryNodeHelper.recursiveFind(property);
            }
        }
        if (recursiveFind == null) {
            BusinessException createBusinessException = ExceptionFactory.getInstance().createBusinessException(
                    DefaultMessagesImpl.getString("DuplicateAction.NodeNull", newLabel));//$NON-NLS-1$
            throw createBusinessException;
        }

        RepositoryNode parent = recursiveFind.getParent();
        CommonViewer dqCommonViewer = RepositoryNodeHelper.getDQCommonViewer();
        if (dqCommonViewer != null && !dqCommonViewer.getExpandedState(parent)) {
            dqCommonViewer.setExpandedState(parent, true);
        }

        return recursiveFind;
    }

    private RepositoryNode findNodeForTDQFileItem(String label) {
        IRepositoryNode librariesFolderNode = RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.SOURCE_FILES);
        List<? extends RepositoryNode> childrenList = RepositoryNodeHelper.getSourceFileRepNodes(librariesFolderNode, true);
        RepositoryNode node = findNodeInList(label, childrenList);
        if (node == null) {
            librariesFolderNode = RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.JRXML_TEMPLATE);
            List<? extends RepositoryNode> childrenList2 = RepositoryNodeHelper.getJrxmlFileRepNodes(librariesFolderNode, true);
            node = findNodeInList(label, childrenList2);
        }

        return node;
    }

    private RepositoryNode findNodeInList(String label, List<? extends RepositoryNode> childrenList) {
        // MOD msjian TDQ-4830 2012-5-25: fixed a NPE and should consider the subfolder
        if (childrenList != null && childrenList.size() > 0) {
            for (RepositoryNode node : childrenList) {
                if (label != null && label.equals(node.getLabel())) {
                    return node;
                }
            }
        }
        return null;
    }
}
