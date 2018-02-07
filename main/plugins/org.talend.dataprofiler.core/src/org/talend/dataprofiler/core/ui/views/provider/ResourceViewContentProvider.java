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
package org.talend.dataprofiler.core.ui.views.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.TalendPropertiesUtil;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.exchange.ExchangeCategoryRepNode;
import org.talend.dataprofiler.core.ui.exchange.ExchangeComponentRepNode;
import org.talend.dataprofiler.core.ui.exchange.ExchangeFolderRepNode;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.ecos.model.IEcosCategory;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.ContextFolderRepNode;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.RecycleBinRepNode;
import org.talend.dq.nodes.SysIndicatorFolderRepNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.exceptions.MissingDriverException;

/**
 * DOC klliu Reconstructure the ResourceViewContentProvider for using DI's API.
 */
public class ResourceViewContentProvider extends WorkbenchContentProvider {

    private static Logger log = Logger.getLogger(ResourceViewContentProvider.class);

    private TreeViewer treeViewer = null;

    private IPropertyChangeListener mergeRefListener;

    /**
     * DOC sgandon Comment method "registerMergeRefListgener".
     */
    private void registerMergeRefListener() {
        if (mergeRefListener == null) {
            mergeRefListener = new IPropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent event) {
                    if (IRepositoryPrefConstants.MERGE_REFERENCE_PROJECT.equals(event.getProperty())) {
                        try {
                            ProxyRepositoryFactory.getInstance().initialize();
                        } catch (PersistenceException e) {
                            log.error(e, e);
                        }
                        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        CommonNavigator findView = (CommonNavigator) activePage.findView(DQRespositoryView.ID);
                        if (findView != null) {
                            CommonViewer commonViewer = findView.getCommonViewer();
                            if (commonViewer != null) {
                                commonViewer.refresh();
                            }
                        }
                    }

                }
            };
            // the merge only for DI repository,need to judge null for other product
            IRepositoryService repositoryService = CoreRuntimePlugin.getInstance().getRepositoryService();
            if (repositoryService != null) {
                IPreferenceStore preferenceStore = repositoryService.getRepositoryPreferenceStore();
                preferenceStore.addPropertyChangeListener(mergeRefListener);
            }

        }
    }

    /**
     * DOC rli ResourceViewContentProvider constructor comment.
     */
    public ResourceViewContentProvider() {
        super();
    }

    @Override
    public Object[] getChildren(Object element) {
        if (DQRepositoryNode.isOnDisplayNextOrPreviousNode() && element instanceof IRepositoryNode) {
            List<IRepositoryNode> children = findChildren(element);
            return sortRepositoryNode(children.toArray());
        }

        try {
            if (element instanceof IWorkspaceRoot) {
                return createWorkspaceRootChildren(element);
            } else if (element instanceof RepositoryNode) {
                return getRepositoryNodeChildren(element);
            }
        } catch (CoreException e) {
            log.error(e);
        } catch (PersistenceException e) {
            log.error(e);
        }
        return sortRepositoryNode(super.getChildren(element));
    }

    /**
     * DOC talend Comment method "getRepositoryNodeChildren".
     * 
     * @param element
     * @param instance
     * @return
     * @throws PersistenceException
     * @throws CoreException
     */
    private Object[] getRepositoryNodeChildren(Object element) throws PersistenceException, CoreException {
        final DQRepositoryNode node = (DQRepositoryNode) element;
        // MOD gdbu 2011-7-20 bug : 23220
        DQRepositoryNode.setIsReturnAllNodesWhenFiltering(false);
        List<IRepositoryNode> children = node.getChildren();
        DQRepositoryNode.setIsReturnAllNodesWhenFiltering(true);

        if ((node instanceof DBTableFolderRepNode || node instanceof DBViewFolderRepNode || node instanceof DBColumnFolderRepNode)
                && !DQRepositoryNode.isOnFilterring()) {
            if (0 < children.size()) {
                try {

                    StructuredSelection structSel = new StructuredSelection(node);

                    if (null != treeViewer) {
                        ISelection tempSelection = treeViewer.getSelection();
                        treeViewer.setSelection(structSel);
                        TreeItem[] selectionItems = treeViewer.getTree().getSelection();
                        if (0 != selectionItems.length) {
                            selectionItems[0].setText(node.getLabel());
                            treeViewer.setSelection(tempSelection);
                        }
                    } else {
                        ISelection dqTreeSelection = RepositoryNodeHelper.getDQCommonViewer().getSelection();

                        RepositoryNodeHelper.getDQCommonViewer().setSelection(structSel);
                        TreeItem[] selections = RepositoryNodeHelper.getDQCommonViewer().getTree().getSelection();
                        if (0 != selections.length) {
                            selections[0].setText(node.getLabel());
                        }
                        RepositoryNodeHelper.getDQCommonViewer().setSelection(dqTreeSelection);
                    }

                } catch (Exception e) {
                    log.error(e.toString());
                }
            }
        }

        if (children.size() <= 0) {
            // ~23220
            List<EResourceConstant> resContants = new ArrayList<EResourceConstant>();
            IRepositoryViewObject viewObject = node.getObject();
            String label = viewObject == null ? null : viewObject.getLabel();
            if (EResourceConstant.DATA_PROFILING.getName().equals(label)) {
                resContants.add(EResourceConstant.ANALYSIS);
                if (PluginChecker.isTDQLoaded()) {
                    resContants.add(EResourceConstant.REPORTS);
                }
            } else if (EResourceConstant.LIBRARIES.getName().equals(label)) {
                // MOD TDQ-10933 msjian 20150915: Hide the Exchange node
                if (!TalendPropertiesUtil.isHideExchange()) {
                    resContants.add(EResourceConstant.EXCHANGE);
                }
                // TDQ-10933~
                resContants.add(EResourceConstant.INDICATORS);
                if (PluginChecker.isTDQLoaded()) {
                    resContants.add(EResourceConstant.JRXML_TEMPLATE);
                }
                resContants.add(EResourceConstant.PATTERNS);
                resContants.add(EResourceConstant.RULES);
                resContants.add(EResourceConstant.SOURCE_FILES);
            } else if (EResourceConstant.METADATA.getName().equals(label)) {
                resContants.add(EResourceConstant.DB_CONNECTIONS);
                resContants.add(EResourceConstant.FILEDELIMITED);
                if (PluginChecker.isTDQLoaded() && HadoopClusterUtils.getDefault().isServiceInstalled()) {
                    resContants.add(EResourceConstant.HADOOP_CLUSTER);
                }
            }
            if (resContants.size() > 0) {
                RepositoryNodeBuilder.getInstance().createRepositoryNodeSystemFolders(node, resContants, node.getProject());
            }
        } else {
            // create the reference project nodes(metadata/library/dataprofiling), make them the same with main project,
            // to avoid can not get reference project nodes when open the select udi/pattern/rule dialog
            if (EResourceConstant.REFERENCED_PROJECT.getName().equals(node.getProperties(EProperties.LABEL))) {
                if (!ProxyRepositoryManager.getInstance().isMergeRefProject()) {
                    for (IRepositoryNode refProjectNode : node.getChildren()) {
                        for (IRepositoryNode refProjectItemNode : refProjectNode.getChildren()) {
                            getRepositoryNodeChildren(refProjectItemNode);
                        }
                    }
                }
            }
        }
        if (node instanceof ContextFolderRepNode) {
            children = ((ContextFolderRepNode) node).getChildren();
        }

        return sortRepositoryNode(children.toArray());
    }

    private Object[] createWorkspaceRootChildren(Object element) throws CoreException {
        return createWorkspaceRootChildren(element, ResourceManager.getRootProjectName());
    }

    /**
     * DOC talend Comment method "getWorkspaceRootChildren".
     * 
     * @param element
     * @return
     * @throws CoreException
     */
    private Object[] createWorkspaceRootChildren(Object element, String projectTechnicalLabel) throws CoreException {
        Project inWhichProject = ProxyRepositoryManager.getInstance().getProjectFromProjectTechnicalLabel(projectTechnicalLabel);

        Object currentOpenProject = null;
        for (Object child : super.getChildren(element)) {
            if (child instanceof IProject) {
                if (((IProject) child).getName().equals(projectTechnicalLabel)) {
                    currentOpenProject = child;
                    break;
                }
            }
        }
        List<Object> folders = new ArrayList<Object>();

        Object[] rootFolders = new Object[0];
        if (currentOpenProject != null) {
            rootFolders = ((IProject) currentOpenProject).members(false);

            for (Object folder : rootFolders) {
                IRepositoryNode node = null;
                // here we use RepositoryNodeHelper.PREFIX_TDQ is because of we expect it is useful when we add some
                // TDQ_XXX node on the DQRepository view
                if (folder instanceof IFolder && ((IFolder) folder).getName().startsWith(RepositoryNodeHelper.PREFIX_TDQ)) {
                    IFolder iFolder = (IFolder) folder;
                    if (((IFolder) folder).getName().trim().equals("TDQ_reporting_db")) { //$NON-NLS-1$
                        continue;
                    }
                    IPath relativePath = iFolder.getFullPath().makeRelativeTo(((IProject) currentOpenProject).getFullPath());
                    ERepositoryObjectType respositoryObjectType = RepositoryNodeBuilder.getInstance()
                            .retrieveRepObjectTypeByPath(relativePath.toOSString());
                    node = createNewRepNode(respositoryObjectType, inWhichProject);
                }
                // MOD mzhao for metadata folder
                if (folder instanceof IFolder) {
                    String folderName = ((IFolder) folder).getName();
                    if (EResourceConstant.METADATA.getName().equals(folderName)) {
                        node = createNewRepNode(ERepositoryObjectType.METADATA, inWhichProject);
                    } else if (PluginChecker.isTDQLoaded() && EResourceConstant.CONTEXT.getName().equals(folderName)) {
                        node = createNewRepNode(ERepositoryObjectType.CONTEXT, inWhichProject);
                    }
                }
                if (node != null) {
                    folders.add(node);
                }
            }
        }

        // add msjian TDQ-10386: Recycle Bin should remove form Referenced project
        if (inWhichProject.isMainProject()) {
            // add RecycleBinRepNode
            RecycleBinRepNode recycleBin = new RecycleBinRepNode(
                    DefaultMessagesImpl.getString("RecycleBin.resBinName"), inWhichProject); //$NON-NLS-1$
            folders.add(recycleBin);
        }

        // Reference Projects
        if (org.talend.core.PluginChecker.isRefProjectLoaded() && inWhichProject.getEmfProject() != null
                && inWhichProject.getEmfProject().getReferencedProjects().size() > 0) {

            if (!ProxyRepositoryManager.getInstance().isMergeRefProject()) {
                DQRepositoryNode refProjectNode = createNewRepNode(ERepositoryObjectType.REFERENCED_PROJECTS, inWhichProject);
                refProjectNode.setProperties(EProperties.LABEL, ERepositoryObjectType.REFERENCED_PROJECTS.getLabel());
                refProjectNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.REFERENCED_PROJECTS);
                folders.add(refProjectNode);

                // create children for referenced project node
                handleReferenced(refProjectNode);
            }
        }

        return folders.toArray();
    }

    @SuppressWarnings("unchecked")
    private void handleReferenced(RepositoryNode parent) {
        Project currentProject = ((DQRepositoryNode) parent).getProject();
        if (parent.getType().equals(ENodeType.SYSTEM_FOLDER)) {
            for (ProjectReference refProject : (List<ProjectReference>) currentProject.getEmfProject().getReferencedProjects()) {
                String parentBranch = ProjectManager.getInstance().getMainProjectBranch(currentProject);
                // if not a DB ref project, modified by nma, order 12519
                org.talend.core.model.properties.Project emfProject = refProject.getReferencedProject();
                if (emfProject.getUrl() != null && emfProject.getUrl().startsWith("teneo") //$NON-NLS-1$
                        || (refProject.getBranch() != null && refProject.getBranch().equals(parentBranch))) {

                    DQRepositoryNode referencedProjectNode = new DQRepositoryNode(null, parent, ENodeType.REFERENCED_PROJECT,
                            currentProject);
                    referencedProjectNode.setProperties(EProperties.LABEL, emfProject.getLabel());
                    referencedProjectNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.REFERENCED_PROJECTS);
                    parent.getChildren().add(referencedProjectNode);

                    try {
                        // org.talend.core.model.general.Project refGeneralProject = new
                        // org.talend.core.model.general.Project(
                        // emfProject, false);
                        Object[] createWorkspaceRootChildren = createWorkspaceRootChildren(ResourceManager.getRoot(),
                                emfProject.getTechnicalLabel());
                        referencedProjectNode.getChildren().addAll(
                                (Collection<? extends IRepositoryNode>) Arrays.asList(createWorkspaceRootChildren));
                    } catch (CoreException e) {
                        log.error(e, e);
                    }
                }
            }
        }
    }

    /**
     * DOC talend Comment method "findChildren".
     * 
     * @param element
     * @return
     */
    private List<IRepositoryNode> findChildren(Object element) {
        List<IRepositoryNode> children = new ArrayList<IRepositoryNode>();
        IRepositoryNode node = (IRepositoryNode) element;
        List<IRepositoryNode> allFilteredNodeList = RepositoryNodeHelper.getAllFilteredNodeList();
        for (IRepositoryNode iRepositoryNode : allFilteredNodeList) {
            if (null != iRepositoryNode.getParent() && iRepositoryNode.getParent().equals(node)) {
                children.add(iRepositoryNode);
            }
        }
        if (children.isEmpty()) {
            for (IRepositoryNode iRepositoryNode : allFilteredNodeList) {
                if (null != iRepositoryNode.getParent() && null != iRepositoryNode.getParent().getParent()
                        && iRepositoryNode.getParent().getParent().equals(node)) {
                    children.add(iRepositoryNode);
                }
            }
        }
        return children;
    }

    private DQRepositoryNode createNewRepNode(ERepositoryObjectType type, org.talend.core.model.general.Project inWhichProject) {
        IRepositoryViewObject viewObject = null;
        FolderItem folderItem = ProxyRepositoryFactory.getInstance().getFolderItem(inWhichProject, type, Path.EMPTY);
        if (folderItem == null) {
            String folderName = ERepositoryObjectType.getFolderName(type);
            viewObject = new Folder(folderName, folderName);
        } else {
            viewObject = new Folder(folderItem.getProperty(), type);
        }

        DQRepositoryNode node = new DQRepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER, inWhichProject);
        if (ERepositoryObjectType.CONTEXT == type) {
            node = new ContextFolderRepNode(viewObject, null, ENodeType.SYSTEM_FOLDER, inWhichProject);
        }
        viewObject.setRepositoryNode(node);
        return node;
    }

    /**
     * sort element on the tree.
     * 
     * @param array
     * @return
     */
    protected Object[] sortRepositoryNode(Object[] array) {
        if (array != null && array.length > 0) {
            List<IRepositoryNode> repositoryNodeList = RepositoryNodeHelper.getRepositoryNodeList(array);
            return ComparatorsFactory.sort(repositoryNodeList.toArray(), ComparatorsFactory.REPOSITORY_NODE_COMPARATOR_ID);
        }
        return new Object[] {};
    }

    @Override
    public boolean hasChildren(Object element) {
        try {
            if (element instanceof IRepositoryNode) {
                IRepositoryNode node = (IRepositoryNode) element;
                IRepositoryViewObject viewObject = node.getObject();
                if (viewObject instanceof MetadataColumnRepositoryObject) {
                    return false;
                } else if (node instanceof ExchangeFolderRepNode || node instanceof ExchangeCategoryRepNode
                        || node instanceof SysIndicatorFolderRepNode || element instanceof DBTableRepNode
                        || element instanceof DBViewRepNode || element instanceof DBCatalogRepNode
                        || element instanceof DBSchemaRepNode) {
                    // ExchangeFolderRepNode always have children
                    // ExchangeCategoryRepNode always have children
                    return true;
                } else if (node instanceof ExchangeComponentRepNode) {
                    // ExchangeComponentRepNode always don't have children
                    return false;
                } else if (element instanceof DBTableFolderRepNode) {
                    // MOD gdbu 2011-9-1 TDQ-3457
                    if (DQRepositoryNode.isOnFilterring()) {
                        return true;
                    }
                    DBTableFolderRepNode dbTableFolder = (DBTableFolderRepNode) element;
                    return dbTableFolder.hasChildren();
                } else if (element instanceof DBViewFolderRepNode) {
                    if (DQRepositoryNode.isOnFilterring()) {
                        return true;
                    }
                    DBViewFolderRepNode dbViewFolder = (DBViewFolderRepNode) element;
                    return dbViewFolder.hasChildren();
                    // ~TDQ-3457
                }
            } else if (element instanceof IEcosCategory) {
                return true;
            }
            // // MOD qiongli feature 9486
            // if (element instanceof IFolder) {
            // // MOD yyi 2010-09-30 15271: svn project can't load exchange nodes
            // if (ResourceManager.isExchangeFolder((IFolder) element)) {
            // return true;
            // }
            // // ~15271
            // List<Object> obsLs = Arrays.asList(super.getChildren(element));
            // if (obsLs.size() == 1) {
            // Object obj = (Object) obsLs.get(0);
            // if (obj instanceof IFolder && ((IFolder) obj).getName().equals(PluginConstant.SVN_SUFFIX))
            // return false;
            // }
            // } else if (element instanceof DQRecycleBinNode) {
            // DQRecycleBinNode rbn = (DQRecycleBinNode) element;
            // Object obj = rbn.getObject();
            // if (obj instanceof IFolder) {
            // try {
            // return ((IFolder) obj).members().length > 0;
            // } catch (CoreException e) {
            // log.error(e);
            // }
            // }
            // return false;
            // }

        } catch (MissingDriverException e) {
            if (PluginChecker.isOnlyTopLoaded()) {
                MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        DefaultMessagesImpl.getString("ResourceViewContentProvider.warining"), //$NON-NLS-1$
                        e.getErrorMessage());
            } else {
                log.error(e, e);
            }
            return false;
        }
        return super.hasChildren(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.model.BaseWorkbenchContentProvider#getParent(java.lang.Object)
     */
    @Override
    public Object getParent(Object element) {
        // MOD gdbu 2011-5-16 bug : 21188
        if (element instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) element;
            return node.getParent();
        }
        return super.getParent(element);
        // ~21188
    }

    public void setTreeViewer(TreeViewer fViewer) {
        this.treeViewer = fViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.model.WorkbenchContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
     * java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        super.inputChanged(viewer, oldInput, newInput);
        registerMergeRefListener();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.model.WorkbenchContentProvider#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (mergeRefListener != null) {
            IRepositoryService repositoryService = CoreRuntimePlugin.getInstance().getRepositoryService();
            if (repositoryService != null) {
                IPreferenceStore preferenceStore = repositoryService.getRepositoryPreferenceStore();
                preferenceStore.removePropertyChangeListener(mergeRefListener);
            }
            mergeRefListener = null;
        }
    }

}
