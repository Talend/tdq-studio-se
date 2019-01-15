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
package org.talend.dataprofiler.core.ui.action.actions;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.DeleteAction;
import org.talend.core.repository.ui.actions.DeleteActionCache;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.message.DeleteModelElementConfirmDialog;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dataprofiler.core.ui.views.resources.RemoteRepositoryObjectCRUD;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ReportFileHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.JrxmlTempSubFolderNode;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.dq.nodes.hadoopcluster.HDFSOfHCConnectionNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DQDeleteAction extends DeleteAction {

    private IRepositoryNode currentNode = null;

    private static Logger log = Logger.getLogger(DQDeleteAction.class);

    private List<IRepositoryNode> selectedNodes;

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    // key in nodeWithDependsMap is the repostiory node and value is the dependencies of this node.
    private Map<IRepositoryNode, List<ModelElement>> nodeWithDependsMap = new HashMap<IRepositoryNode, List<ModelElement>>();

    private Object[] deleteElements = null;

    private boolean showFilteredOutWarning = true;

    private boolean needRefreshHadoopCluster = false;

    /**
     * Getter for showFilteredOutWarning.
     * 
     * @return the showFilteredOutWarning
     */
    public boolean isShowFilteredOutWarning() {
        return this.showFilteredOutWarning;
    }

    /**
     * Sets the showFilteredOutWarning.
     * 
     * @param showFilteredOutWarning the showFilteredOutWarning to set
     */
    public void setShowFilteredOutWarning(boolean showFilteredOutWarning) {
        this.showFilteredOutWarning = showFilteredOutWarning;
    }

    public DQDeleteAction() {
        super();
        setText(DefaultMessagesImpl.getString("DQDeleteAction.delete"));//$NON-NLS-1$
        setId(ActionFactory.DELETE.getId());
        this.selectedNodes = new ArrayList<IRepositoryNode>();
        this.deleteElements = ((IStructuredSelection) this.getSelection()).toArray();

    }

    public DQDeleteAction(Object[] delElements) {
        super();
        setText(DefaultMessagesImpl.getString("DQDeleteAction.delete"));//$NON-NLS-1$
        setId(ActionFactory.DELETE.getId());
        this.selectedNodes = new ArrayList<IRepositoryNode>();
        this.deleteElements = delElements;
    }

    @Override
    public ISelection getSelection() {
        ISelection selection = null;
        if (currentNode == null) {
            if (deleteElements != null) {
                selection = super.getSelection();
            } else {
                // select by UI(tree)
                DQRespositoryView findView = CorePlugin.getDefault().getRepositoryView();
                selection = findView.getCommonViewer().getSelection();
            }
        } else {
            // new instance of selection for dependency modeleEelemnt.
            selection = new StructuredSelection(currentNode);
        }
        return selection;
    }

    @Override
    protected ISelection getRepositorySelection() {
        return getSelection();
    }

    ISelectionProvider deletionSelProv = new ISelectionProvider() {

        public void setSelection(ISelection arg0) {
            // no implementation
        }

        public void removeSelectionChangedListener(ISelectionChangedListener arg0) {
            // no implementation
        }

        public ISelection getSelection() {
            IStructuredSelection structruedSelection = null;
            if (deleteElements != null) {
                structruedSelection = new StructuredSelection(deleteElements);
            }
            return structruedSelection;
        }

        public void addSelectionChangedListener(ISelectionChangedListener arg0) {
            // no implementation
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.ui.actions.DeleteAction#doRun()
     */
    @Override
    public void doRun() {
        repositoryObjectCRUD.refreshDQViewForRemoteProject();

        // ADD msjian TDQ-7006 2013-7-24: after refresh get the selection to check.
        if (!repositoryObjectCRUD.isSelectionAvailable()) {
            repositoryObjectCRUD.showWarningDialog();
            return;
        }
        // TDQ-7006~

        // MOD qiongli 2012-4-1 TDQ-4926.fill selectedNodes,and delete opration will base on the List.
        if (deleteElements.length == 0) {
            return;
        }
        setSpecialSelection(deletionSelProv);
        // remove the source file nodes which have been opened the editor
        deleteElements = checkSourceFilesEditorOpening(deleteElements);
        // ~ TDQ-4831

        for (Object obj : deleteElements) {
            RepositoryNode node = (RepositoryNode) obj;
            selectedNodes.add(node);

            // TDQ-12034: reload the delete node to make sure when check the dependency correct.
            if (repositoryObjectCRUD instanceof RemoteRepositoryObjectCRUD) {
                // TDQ-13357: fix NPE, because for ReportFileRepNode, repNode.getObject() == null
                // TDQ-14283: fix NPE for the folder node in remote project
                if (node.getObject() != null && node.getObject().getProperty().eResource() != null) {
                    try {
                        ProxyRepositoryFactory.getInstance().reload(node.getObject().getProperty());
                        IFile objFile = PropertyHelper.getItemFile(node.getObject().getProperty());
                        objFile.refreshLocal(IResource.DEPTH_INFINITE, null);
                    } catch (Exception e1) {
                        log.error(e1, e1);
                    }
                }
            }
            // TDQ-12034~
        }
        if (DQRepositoryNode.isOnFilterring()) {
            setPreviousFilteredNode(selectedNodes.get(0));
        }

        if (selectedNodes.size() > 0) {
            IRepositoryNode firstNode = selectedNodes.get(0);
            boolean isStateDeleted = RepositoryNodeHelper.isStateDeleted(firstNode);
            // logical delete
            if (!isStateDeleted) {
                logicDelete();
            } else {
                Collection<IRepositoryNode> shownNodes = null;
                Collection<IRepositoryNode> allDeleteNodes = null;
                if (DQRepositoryNode.isOnFilterring()) {
                    shownNodes = RepositoryNodeHelper.findAllChildrenNodes(selectedNodes);
                    selectedNodes = rebuildNodes(selectedNodes);
                    allDeleteNodes = RepositoryNodeHelper.findAllChildrenNodes(selectedNodes);
                }
                // if the repository node is on filtering, show warning dialog when the nodes going to be deleted are
                // different with the nodes showing
                if (DQRepositoryNode.isOnFilterring() && isShowFilteredOutWarning()) {
                    setShowFilteredOutWarning(false);
                    if (!RepositoryNodeHelper.canDeleteWhenFiltering(allDeleteNodes, shownNodes)) {
                        return;
                    }
                }
                // show a confirm dialog to make sure the user want to proceed
                if (showConfirmDialog()) {
                    // sort the selected nodes with special order: first report type, then (jrxml, analysis) type,
                    // finally (connection, DQ Rule, Pattern) type.
                    sortNodesBeforePhysicalDelete();
                    physicalDelete();
                }
            }
        }

        if (DQRepositoryNode.isOnFilterring()) {
            RepositoryNodeHelper.regainRecycleBinFilteredNode();
        }
        // the deleteReportFile() mothed have refresh the workspace and dqview
        refreshWorkspaceAndRecycleBinNodes();
    }

    /**
     * get the nodes with filtered children from the RecycleBinNode.
     * 
     * @param nodes
     * @return
     */
    private List<IRepositoryNode> rebuildNodes(List<IRepositoryNode> nodes) {
        List<IRepositoryNode> tempNodes = new ArrayList<IRepositoryNode>();

        List<IRepositoryNode> recycleBinNodeFirstLevelChildren = ((RepositoryNode) RepositoryNodeHelper.getRecycleBinRepNode())
                .getChildren();
        Collection<IRepositoryNode> allRecycleBinNodes = RepositoryNodeHelper
                .findAllChildrenNodes(recycleBinNodeFirstLevelChildren);
        Iterator<IRepositoryNode> iterator = allRecycleBinNodes.iterator();
        while (iterator.hasNext()) {
            IRepositoryNode next = iterator.next();
            for (IRepositoryNode node : nodes) {
                if (next.equals(node)) {
                    tempNodes.add(next);
                }
            }
        }

        return tempNodes;
    }

    /**
     * DOC msjian Comment method "refreshWorkspaceAndRecycleBinNodes".
     */
    protected void refreshWorkspaceAndRecycleBinNodes() {
        CorePlugin.getDefault().refreshWorkSpace();
        CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getRecycleBinRepNode());

        if (this.needRefreshHadoopCluster) {
            WorkbenchUtils.refreshMetadataNode();
        }

    }

    /**
     * Sort the selected nodes with special order: - first report type, - then (jrxml, analysis) type, - then
     * (connection, DQ Rule, Pattern) type. - finally : the type which has no dependency,
     */
    private void sortNodesBeforePhysicalDelete() {
        List<IRepositoryNode> reportNodes = new ArrayList<IRepositoryNode>();
        List<IRepositoryNode> anaOrJrxml = new ArrayList<IRepositoryNode>();
        // except report analysis and jrxml
        List<IRepositoryNode> files = new ArrayList<IRepositoryNode>();
        // the final level is folder
        List<IRepositoryNode> folders = new ArrayList<IRepositoryNode>();

        for (IRepositoryNode node : selectedNodes) {
            if (isReport(node)) {
                reportNodes.add(node);
            } else if (isAna(node) || isJrxml(node)) {
                anaOrJrxml.add(node);
            } else if (node.getType() == ENodeType.SIMPLE_FOLDER) {
                folders.add(node);
            } else {
                checkIsHiveConnectionUnderHC(node);
                files.add(node);
            }
        }
        selectedNodes.clear();
        selectedNodes.addAll(folders);
        selectedNodes.addAll(files);
        selectedNodes.addAll(anaOrJrxml);
        selectedNodes.addAll(reportNodes);
    }

    /**
     * Judge if the node is an analysis or NOT
     * 
     * @param node
     * @return true when the node is an analysis
     */
    private boolean isAna(IRepositoryNode node) {
        if (node.getObject() != null) {
            return ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT.equals(node.getObject().getRepositoryObjectType());
        }
        return false;
    }

    /**
     * Judge if the node is a report or NOT
     * 
     * @param node
     * @return true when the node is a report
     */
    private boolean isReport(IRepositoryNode node) {
        if (node.getObject() != null) {
            return ERepositoryObjectType.TDQ_REPORT_ELEMENT.equals(node.getObject().getRepositoryObjectType());
        }
        return false;
    }

    /**
     * judge if the deleted hive connection is created from a Hadoop Cluster
     * 
     * @param node
     * @return true- if is a hive and created from a Hadoop cluster
     */
    private void checkIsHiveConnectionUnderHC(IRepositoryNode node) {
        if (node.getObject() != null) {
            boolean isHive = ERepositoryObjectType.METADATA_CONNECTIONS.equals(node.getObject().getRepositoryObjectType());
            if (isHive) {
                String hcId = ConnectionUtils.getHadoopClusterIDOfHive(node.getObject());
                if (!StringUtils.isBlank(hcId)) {
                    needRefreshHadoopCluster = true;
                }
            } else if (node instanceof HDFSOfHCConnectionNode) {
                needRefreshHadoopCluster = true;
            }
        }
    }

    /**
     * physical Delete all selected nodes, if the node has dependency, will popup a confirm dialog with lists of
     * dependencies
     */
    private void physicalDelete() {
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("Delete items") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                List<IRepositoryNode> folderNodeWhichChildHadDepend = null;

                // use this selectedNodes directly, the order also important,when the depended nodes had been deleted,
                // no need
                // to check them any more
                for (int i = selectedNodes.size() - 1; i >= 0; i--) {
                    IRepositoryNode node = selectedNodes.get(i);
                    IRepositoryNode parent = node.getParent();
                    // -- When the node has no depends, delete it directly
                    // -- when the node has depends, add it with depends list to the nodeWithDependsMap
                    if (node.getType() == ENodeType.SIMPLE_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER) {
                        List<IRepositoryNode> newLs = RepositoryNodeHelper.getRepositoryElementFromFolder(node, true);
                        // if the folder have sub node(s) not be deleted, the folder should not be deleted also
                        boolean haveSubNode = false;
                        for (IRepositoryNode subNode : newLs) {
                            if (!hasDependencyClients(subNode)) {
                                excuteSuperRun(subNode, node);
                            } else {
                                // if the folder has some child with depends, can not delete the folder itself here
                                haveSubNode = true;
                                if (folderNodeWhichChildHadDepend == null) {
                                    folderNodeWhichChildHadDepend = new ArrayList<IRepositoryNode>();
                                }
                                if (!folderNodeWhichChildHadDepend.contains(node)) {
                                    folderNodeWhichChildHadDepend.add(node);
                                }
                            }

                        }
                        if (!haveSubNode) {
                            excuteSuperRun(node, parent);
                        }
                    } else {
                        if (!hasDependencyClients(node)) {
                            excuteSuperRun(node, parent);
                        }
                    }
                }
                // show all nodes with its depends in one dialog
                boolean forceDelete = false;
                if (nodeWithDependsMap.size() > 0) {
                    // show all nodes in one dialog.
                    forceDelete = DeleteModelElementConfirmDialog.showDialog(nodeWithDependsMap,
                            DefaultMessagesImpl.getString("DQDeleteAction.dependencyByOther"), true);//$NON-NLS-1$
                    if (forceDelete) {
                        Iterator<Entry<IRepositoryNode, List<ModelElement>>> iter = nodeWithDependsMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<IRepositoryNode, List<ModelElement>> entry = iter.next();
                            IRepositoryNode node = entry.getKey();
                            List<ModelElement> dependencies = entry.getValue();

                            // only when its dependencies deleted successfully, then delete itself
                            if (physicalDeleteDependencies(dependencies)) {
                                excuteSuperRun(node, node.getParent());
                            }
                        }
                    }
                }
                nodeWithDependsMap.clear();
                // if the folder has the child who has depends, can only proceeding after its child be handled
                if (folderNodeWhichChildHadDepend != null && folderNodeWhichChildHadDepend.size() > 0) {
                    if (forceDelete) {
                        for (IRepositoryNode folder : folderNodeWhichChildHadDepend) {
                            excuteSuperRun(folder, folder.getParent());
                        }
                    }
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
    }

    /**
     * Judge if the node has dependencies. and if the node has depends, add the depends with this node into map
     * 
     * Because find the depends should only be executed once in this delete action, so if the node has some depends,
     * will store it in a map here.
     * 
     * @param subNode
     * @return
     */
    private boolean hasDependencyClients(IRepositoryNode node) {
        List<ModelElement> dependencies = EObjectHelper.getDependencyClients(node);
        if (dependencies == null || dependencies.isEmpty()) {
            return false;
        }

        // ADD msjian TDQ-13165: check the dependency is paired or not
        List<ModelElement> validDependenciesList = new ArrayList<ModelElement>();
        ModelElement deleteModelElement = RepositoryNodeHelper.getModelElementFromRepositoryNode(node);
        for (ModelElement mod : dependencies) {
            if (DependenciesHandler.getInstance().checkClientDependencyExist(mod, deleteModelElement)) {
                validDependenciesList.add(mod);
            } else {
                // will come to here ONLY when there have not paired dependency.
                if (DependenciesHandler.getInstance().removeDependenciesBetweenModel(deleteModelElement, mod)) {
                    EMFUtil.saveSingleResource(deleteModelElement.eResource());
                }
            }
        }
        if (validDependenciesList == null || validDependenciesList.isEmpty()) {
            return false;
        }
        // TDQ-13165~

        nodeWithDependsMap.put(node, validDependenciesList);
        return true;
    }

    /**
     * logical delete the selected nodes
     * 
     */
    private void logicDelete() {
        for (int i = selectedNodes.size() - 1; i >= 0; i--) {
            IRepositoryNode node = selectedNodes.get(i);
            // handle generating report file.bug 18805 .
            if (node instanceof ReportFileRepNode) {
                try {
                    deleteReportFile((ReportFileRepNode) node);
                } catch (PersistenceException e) {
                    log.error(e, e);
                }
                continue;
            }
            this.checkIsHiveConnectionUnderHC(node);
        }

        RepositoryNode parent = selectedNodes.get(0).getParent();
        // only need to run one time, because in the super.run() can handle all selected node.
        excuteSuperRun(null, parent);

        // TDQ-7298: refresh all the selected node' parent node when delete different items.
        for (IRepositoryNode node : selectedNodes) {
            refreshParentNode(node.getParent());
        }
    }

    /**
     * DOC yyin Comment method "isJrxml".
     * 
     * @param node
     * @return
     */
    private boolean isJrxml(IRepositoryNode node) {
        if (node.getObject() != null) {
            return ERepositoryObjectType.TDQ_JRAXML_ELEMENT.equals(node.getObject().getRepositoryObjectType());
        }
        return false;
    }

    /**
     * DOC gdbu Comment method "setNextFilteredNode".
     * 
     * @param node
     */
    private void setPreviousFilteredNode(IRepositoryNode node) {
        if (DQRepositoryNode.isOnFilterring()) {

            List<IRepositoryNode> allFilteredNodeList = RepositoryNodeHelper.getAllFilteredNodeList();
            for (int i = 0; i < allFilteredNodeList.size(); i++) {
                if (allFilteredNodeList.get(i).equals(node)) {

                    if (i <= 1) {
                        RepositoryNodeHelper.setFilteredNode(allFilteredNodeList.get(0));
                    } else {
                        RepositoryNodeHelper.setFilteredNode(allFilteredNodeList.get(i - 1));
                    }
                    break;
                }
            }
        }
    }

    /**
     * DOC qiongli Comment method "physicalDeleteDependencies".
     * 
     * @param dependences
     * @return
     */
    private boolean physicalDeleteDependencies(List<ModelElement> dependences) {
        boolean isSucceed = true;
        try {
            for (ModelElement mod : dependences) {
                List<ModelElement> subDependences = EObjectHelper.getDependencyClients(mod);
                if (subDependences != null && !subDependences.isEmpty()) {
                    if (!physicalDeleteDependencies(subDependences)) {
                        isSucceed = false;
                        continue;
                    }
                }
                RepositoryNode tempNode = RepositoryNodeHelper.recursiveFind(mod);
                if (tempNode == null) {
                    tempNode = RepositoryNodeHelper.recursiveFindRecycleBin(mod);
                    // TDQ-7337 if the tempNode is physical deleted by other supplier dependency,it will be null at
                    // here,should continue the loop.
                    if (tempNode == null) {
                        continue;
                    }

                }

                // before physical delete, we must do logicDelete first, because in DI side, they didn't have dependency
                // and didn't have force delete
                boolean isLogicDeletedd = logicDeleteDependeny(tempNode);
                tempNode = RepositoryNodeHelper.recursiveFindRecycleBin(mod);
                // we must get IFile before delete
                IFile propertyFile = RepositoryNodeHelper.getIFile(tempNode);
                // if logical delete failed,set 'isSucceed' to false and continue to logical delete other dependeces.
                if (!isLogicDeletedd || tempNode == null) {
                    isSucceed = false;
                    log.error(DefaultMessagesImpl.getString("DQDeleteAction.failToLogicalDelete", mod.getName())); //$NON-NLS-1$
                    continue;
                }
                excuteSuperRun(tempNode, tempNode.getParent());
                // when physical delete failed, we continue to delete others
                if (propertyFile != null && propertyFile.exists()) {
                    isSucceed = false;
                    log.error(DefaultMessagesImpl.getString("DQDeleteAction.getErrorWhenDelete", propertyFile.getFullPath() //$NON-NLS-1$
                            .removeFileExtension()));
                    continue;
                }
            }
        } catch (Exception exc) {
            log.error(exc, exc);
            return false;
        }
        return isSucceed;
    }

    /**
     * logical delete the element,return boolean to indcate if delete successfully.
     * 
     * @param tempNode
     * @return
     */
    private boolean logicDeleteDependeny(RepositoryNode tempNode) {
        if (tempNode.getObject().getProperty() == null) {
            return false;
        }
        boolean isStateDel = RepositoryNodeHelper.isStateDeleted(tempNode);
        if (isStateDel) {
            return isStateDel;
        }
        if (tempNode != null) {
            // logcial delete dependcy element.
            if (tempNode.getObject() != null) {
                // TDQ-13184 msjian: because when item is opening, when close it we will do unlock, so we make sure we do unlock
                // only one time here(else for mode: ask user will popup twice unlock confirm when select no)
                if (CorePlugin.getDefault().itemIsOpening(tempNode.getObject().getProperty().getItem())) {
                    CorePlugin.getDefault().closeEditorIfOpened(tempNode.getObject().getProperty().getItem());
                } else {
                    // TDQ-12207 msjian: unlock the object which is locked by user only, then we will unify the logic to the same
                    // as above closeEditorIfOpened: alway will do unlock.
                    if (ProxyRepositoryFactory.getInstance().getStatus(tempNode.getObject()) == ERepositoryStatus.LOCK_BY_USER) {
                        try {
                            ProxyRepositoryFactory.getInstance().unlock(tempNode.getObject());
                        } catch (PersistenceException e) {
                            log.error(e, e);
                        } catch (LoginException e) {
                            log.error(e, e);
                        }
                    }
                    // TDQ-12207~
                }
            }

            // need to pass the tempNode parameter at here for logical delete dependce.
            excuteSuperRun(tempNode, tempNode.getParent());
            CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getRecycleBinRepNode());
        }
        return RepositoryNodeHelper.isStateDeleted(tempNode);
    }

    /**
     * DOC qiongli :excute super method run().
     * 
     * @param currentNode:null for logical delete a selected element by UI.none-null for physical delete or logical
     * delete dependecy.
     */
    private void excuteSuperRun(IRepositoryNode repoNode, IRepositoryNode parent) {
        this.currentNode = repoNode;
        Item item = null;
        if (repoNode != null) {
            Property property = repoNode.getObject().getProperty();
            if (property != null) {
                item = property.getItem();
            }
        }

        // MOD qiongli 2011-5-9 bug 21035,avoid to unload resource.
        super.setAvoidUnloadResources(true);
        super.doRun();
        // because reuse tos codes.remove current node from its parent(simple folder) for phisical delete or logical
        // delete dependency.
        if (repoNode != null) {
            // MOD qiongli 2012-4-1 TDQ-4926,after physical delete this node,should remove it from that
            // selection List.avoid to delete twice.
            this.selectedNodes.remove(repoNode);
            if (parent != null
                    && (parent.getType() == ENodeType.SIMPLE_FOLDER || parent.getLabel().equalsIgnoreCase(
                            ERepositoryObjectType.RECYCLE_BIN.name().replaceAll("_", PluginConstant.SPACE_STRING)))) {//$NON-NLS-1$
                parent.getChildren(true).remove(repoNode);
            }
            // is TDQReportItem or not
            if (item != null && item instanceof TDQReportItem) {
                deleteRelatedFolder(repoNode, item);
            }
        }

        // refresh parent node
        refreshParentNode(parent);
    }

    private void deleteRelatedFolder(IRepositoryNode repoNode, Item item) {
        List<IFile> repDocLinkFiles = ReportFileHelper.getRepDocLinkFiles(RepositoryNodeHelper.getIFile(repoNode));
        // delete related output folder after physical delete a report.
        IFile itemFile = PropertyHelper.getItemFile(item.getProperty());
        try {
            ReportFileHelper.deleteRepOutputFolder(itemFile);
        } catch (PersistenceException e) {
            log.error(e, e);
        }
        // delete the link files which links to the Report Generated Doc File
        if (!repDocLinkFiles.isEmpty()) {
            ReportFileHelper.removeRepDocLinkFiles(repDocLinkFiles);
        }
    }

    /**
     * refresh Parent Node.
     * 
     * @param parent
     */
    private void refreshParentNode(IRepositoryNode parent) {
        if (parent != null) {
            if (parent instanceof AnalysisSubFolderRepNode || parent instanceof ReportSubFolderRepNode) {
                CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.findNearestSystemFolderNode((RepositoryNode) parent));
            } else {
                CorePlugin.getDefault().refreshDQView(parent);
            }
        }
    }

    /**
     * physical delete generating report file.
     * 
     * @param repFileNode
     * @throws PersistenceException
     */
    private void deleteReportFile(final ReportFileRepNode repFileNode) throws PersistenceException {
        @SuppressWarnings("rawtypes")
        RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                "deleteReportFile") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                    public void run(IProgressMonitor monitor) {
                        try {
                            IPath location = Path.fromOSString(repFileNode.getResource().getProjectRelativePath().toOSString());
                            IFile latestRepIFile = ResourceManager.getRootProject().getFile(location);
                            if (latestRepIFile.isLinked()) {
                                File file = new File(latestRepIFile.getRawLocation().toOSString());
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                            latestRepIFile.delete(true, null);
                            IContainer parent = latestRepIFile.getParent();
                            if (parent != null) {
                                parent.refreshLocal(IResource.DEPTH_INFINITE, monitor);
                            }
                        } catch (CoreException e) {
                            log.error(e, e);
                        }
                    }

                };

                IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        IWorkspace workspace = ResourcesPlugin.getWorkspace();
                        try {
                            ISchedulingRule schedulingRule = workspace.getRoot();
                            workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                        } catch (CoreException e) {
                            throw new InvocationTargetException(e);
                        }
                    }
                };

                try {
                    PlatformUI.getWorkbench().getProgressService().run(false, false, iRunnableWithProgress);
                } catch (InterruptedException e) {
                    ExceptionHandler.process(e);
                } catch (InvocationTargetException e) {
                    ExceptionHandler.process(e);
                }
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
        repositoryWorkUnit.throwPersistenceExceptionIfAny();

        // refresh the parent
        RepositoryNode parent = repFileNode.getParent();
        if (parent != null) {
            CorePlugin.getDefault().refreshDQView(parent);
        }
    }

    protected boolean showConfirmDialog() {
        return MessageDialog.openConfirm(null, DefaultMessagesImpl.getString("DQDeleteAction.deleteForeverTitle"), //$NON-NLS-1$
                PluginConstant.SPACE_STRING + DefaultMessagesImpl.getString("DQDeleteAction.areYouDeleteForever"));//$NON-NLS-1$
    }

    public IRepositoryNode getCurrentNode() {
        return this.currentNode;
    }

    public void setCurrentNode(RepositoryNode node) {
        this.currentNode = node;
    }

    /**
     * DOC xqliu Comment method "checkSourceFilesEditorOpening".
     * 
     * @param deleteElements
     * @return
     */
    @SuppressWarnings({ "hiding", "rawtypes", "unchecked" })
    private Object[] checkSourceFilesEditorOpening(Object[] deleteElements) {
        List list = new ArrayList();
        boolean opened = false;
        String openSourceFileNames = ""; //$NON-NLS-1$
        for (Object obj : deleteElements) {
            if (obj instanceof SourceFileRepNode || obj instanceof JrxmlTempleteRepNode) {
                ReturnCode rc = WorkspaceResourceHelper.checkSourceFileNodeOpening((RepositoryNode) obj);
                if (rc.isOk()) {
                    opened = rc.isOk();
                    openSourceFileNames += rc.getMessage();
                } else {
                    list.add(obj);
                }
            } else if (obj instanceof SourceFileSubFolderNode || obj instanceof JrxmlTempSubFolderNode) {
                ReturnCode rc = WorkspaceResourceHelper.checkSourceFileSubFolderNodeOpening((RepositoryNode) obj);
                if (rc.isOk()) {
                    opened = rc.isOk();
                    openSourceFileNames += rc.getMessage();
                } else {
                    list.add(obj);
                }
            } else {
                list.add(obj);
            }
        }
        if (opened) {
            WorkspaceResourceHelper.showSourceFilesOpeningWarnMessages(openSourceFileNames);
        }
        return list.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.DeleteAction#synchUI(org.talend.repository.ui.actions.DeleteActionCache)
     */
    @Override
    protected void synchUI(DeleteActionCache deleteActionCache) {
        super.synchUI(deleteActionCache);
    }

    /**
     * make the confirm dialog not popup.
     */
    @Override
    protected boolean deleteElements(IProxyRepositoryFactory factory, DeleteActionCache deleteActionCache,
            RepositoryNode currentJobNode, List<IRepositoryViewObject> batchDeleteObjectList)
            throws PersistenceException, BusinessException {
        return deleteElements(factory, deleteActionCache, currentJobNode, true, batchDeleteObjectList);
    }

}
