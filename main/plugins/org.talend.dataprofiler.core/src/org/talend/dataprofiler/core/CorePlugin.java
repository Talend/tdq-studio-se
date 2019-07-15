// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.help.internal.base.BaseHelpSystem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.helper.StatusHelper;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.IRepositoryFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.RepositoryFactoryProvider;
import org.talend.core.repository.utils.ProjectHelper;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.services.IMavenUIService;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.CommonFormEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.PatternTestView;
import org.talend.dataprofiler.core.ui.views.RespositoryDetailView;
import org.talend.dataprofiler.help.BookMarkEnum;
import org.talend.dataprofiler.migration.MigrationPlugin;
import org.talend.dataprofiler.service.ISemanticStudioService;
import org.talend.dataquality.analysis.impl.AnalysisImpl;
import org.talend.dataquality.reports.impl.TdReportImpl;
import org.talend.dq.CWMPlugin;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryConstants;
import org.talend.resource.ResourceManager;
import org.talend.utils.ProductVersion;
import org.talend.utils.sugars.ReturnCode;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * The activator class controls the plug-in life cycle.
 */
public class CorePlugin extends AbstractUIPlugin {

    protected static Logger log = Logger.getLogger(CorePlugin.class);

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.dataprofiler.core"; //$NON-NLS-1$

    // The shared instance
    private static CorePlugin plugin;

    private RefreshAction refreshAction;

    private boolean repositoryInitialized = false;

    private BundleContext bundleContext;

    private ISemanticStudioService service;

    /**
     * Getter for context.
     *
     * @return the context
     */
    public BundleContext getBundleContext() {
        return this.bundleContext;
    }

    /**
     * Getter for repositoryInitialized.
     *
     * @return the repositoryInitialized
     */
    public boolean isRepositoryInitialized() {
        return this.repositoryInitialized;
    }

    /**
     * The constructor.
     */
    public CorePlugin() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
     */
    @SuppressWarnings("restriction")
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.bundleContext = context;
        plugin = this;
        getPreferenceStore().setDefault(PluginConstant.CHEAT_SHEET_VIEW, true);
        getPreferenceStore().setValue(ITalendCorePrefConstants.PREVIEW_LIMIT, "50");//$NON-NLS-1$
        getPreferenceStore().setValue(ITalendCorePrefConstants.LANGUAGE_SELECTOR, Locale.getDefault().getLanguage());
        getPreferenceStore().setDefault(PluginConstant.MAX_NB_ROWS_ANALYSIS_EDITOR, 10000);
        try {
            for (BookMarkEnum bookMark : BookMarkEnum.VALUES) {
                BaseHelpSystem.getBookmarkManager().addBookmark(bookMark.getHref(), bookMark.getLabel());
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
    public static CorePlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
     * DOC Zqin Comment method "getCurrentActiveEditor".
     *
     * @return the current active editor;
     */
    public IEditorPart getCurrentActiveEditor() {
        return getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    }

    /**
     * DOC msjian Comment method "getAllDataProviders".
     *
     * @return
     */
    public Collection<Connection> getAllDataProviders() {
        Collection<Connection> allDataProviders = new ArrayList<Connection>();

        try {
            for (ConnectionItem connItem : ProxyRepositoryFactory.getInstance().getMetadataConnectionsItem()) {
                allDataProviders.add(connItem.getConnection());
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }
        return allDataProviders;
    }

    /**
     * DOC bZhou Comment method "openEditor".
     *
     * @param file
     * @param editorId
     * @return
     * @deprecated
     */
    @Deprecated
    public IEditorPart openEditor(IFile file, String editorId) {
        FileEditorInput input = new FileEditorInput(file);
        try {

            return this.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
        } catch (PartInitException e) {
            ExceptionHandler.process(e);
            return null;
        }
    }

    /**
     * DOC mzhao open editor with editor input.
     *
     * @param editorInput
     * @param editorId
     * @return
     */
    public IEditorPart openEditor(IEditorInput editorInput, String editorId) {
        try {
            return this.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, editorId);
        } catch (PartInitException e) {
            ExceptionHandler.process(e);
            return null;
        }
    }

    /**
     * get DQ repository view if the view is opening else return null
     *
     * @return
     */
    public DQRespositoryView getRepositoryView() {
        IViewPart view = WorkbenchUtils.getView(DQRespositoryView.ID, false);
        return view != null ? (DQRespositoryView) view : null;
    }

    /**
     * get DQ repository view without bring it to top.
     *
     * @return
     */
    public DQRespositoryView getRepositoryViewWithoutBringToTop() {
        IViewPart view = WorkbenchUtils.getView(DQRespositoryView.ID, false, false);
        return view != null ? (DQRespositoryView) view : null;
    }

    /**
     * get DQ repository view if the view is opening else will open it firstly
     *
     * @return
     */
    public DQRespositoryView findAndOpenRepositoryView() {
        IViewPart view = WorkbenchUtils.getView(DQRespositoryView.ID, true);
        return view != null ? (DQRespositoryView) view : null;
    }

    /**
     * DOC bzhou Comment method "getPatternTestView".
     *
     * @return
     */
    public PatternTestView getPatternTestView() {
        IViewPart view = WorkbenchUtils.getAndOpenView(PatternTestView.ID);
        return view != null ? (PatternTestView) view : null;
    }

    public RespositoryDetailView getRespositoryDetailView() {
        IViewPart view = WorkbenchUtils.getAndOpenView(RespositoryDetailView.ID);
        return view != null ? (RespositoryDetailView) view : null;
    }

    public void refreshWorkSpace() {
        if (refreshAction == null) {
            refreshAction = new RefreshAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        }
        refreshAction.run();
    }

    /**
     * refresh the whole DQReposirotyView.
     */
    public void refreshDQView() {
        DQRespositoryView repositoryView = getRepositoryView();
        if (repositoryView != null && repositoryView.getCommonViewer() != null) {
            repositoryView.getCommonViewer().refresh();
        }
    }

    /**
     * get Current the first selected node from DQRespositoryView.
     *
     * @return
     */
    public IRepositoryNode getCurrentSelectionNode() {
        DQRespositoryView repositoryView = getRepositoryView();
        if (repositoryView == null) {
            return null;
        }
        TreeItem[] selectionTreeItem = repositoryView.getCommonViewer().getTree().getSelection();
        if (null != selectionTreeItem && selectionTreeItem.length > 0 && null != selectionTreeItem[0]) {
            IRepositoryNode repoNode = (IRepositoryNode) selectionTreeItem[0].getData();
            return repoNode;
        } else {
            return null;
        }

    }

    /**
     * get Current all selected nodes from DQRespositoryView.
     *
     * @return
     */
    public IRepositoryNode[] getCurrentSelectionNodes() {
        DQRespositoryView repositoryView = getRepositoryView();
        if (repositoryView == null) {
            return null;
        }
        TreeItem[] selectionTreeItem = repositoryView.getCommonViewer().getTree().getSelection();

        if (null == selectionTreeItem || selectionTreeItem.length == 0) {
            return null;
        }

        IRepositoryNode[] currentSelectionNodes = new IRepositoryNode[selectionTreeItem.length];
        for (int i = 0; i < selectionTreeItem.length; i++) {
            currentSelectionNodes[i] = (IRepositoryNode) selectionTreeItem[i].getData();
        }
        return currentSelectionNodes;
    }

    /**
     * refresh the object's status in the DQReposirotyView(if this object is null, then refresh all the
     * DQReposirotyView).
     *
     * @param object
     */
    public void refreshDQView(Object object) {
        if (object == null) {
            refreshDQView();
        } else {
            DQRespositoryView repositoryView = getRepositoryView();
            if (repositoryView != null && repositoryView.getCommonViewer() != null) {
                repositoryView.getCommonViewer().refresh(object);
            }
        }
    }

    /**
     * after create analysis, do refresh
     */
    public void refresh(ModelElement modelElement) {
        if (modelElement instanceof AnalysisImpl || modelElement instanceof TdReportImpl) {
            // MOD by zshen refresh the folder which contain the modelElement but not select it
            CorePlugin.getDefault().refreshDQView(
                    RepositoryNodeHelper.findNearestSystemFolderNode(RepositoryNodeHelper.recursiveFind(modelElement)));
        } else {
            IRepositoryNode currentSelectionNode = CorePlugin.getDefault().getCurrentSelectionNode();
            // if DqRepositoryView is not opened currentSelectionNode will be null and refreshDQView method will
            // get one error log.
            CorePlugin.getDefault().refreshDQView(currentSelectionNode);
        }
        CorePlugin.getDefault().refreshWorkSpace();
        // MOD gdbu 2011-11-18 TDQ-3969 : after create items re-filter the tree , to create a new list .
        if (DQRepositoryNode.isOnFilterring()) {
            RepositoryNodeHelper.fillTreeList(null);
            RepositoryNodeHelper
                    .setFilteredNode(RepositoryNodeHelper.getRootNode(ERepositoryObjectType.TDQ_DATA_PROFILING, true));
        }
    }

    /**
     * DOC bzhou Comment method "getProductVersion".
     *
     * @return
     * @deprecated use MigrationPlugin.getDefault().getProductVersion()
     */
    @Deprecated
    public ProductVersion getProductVersion() {
        return MigrationPlugin.getDefault().getProductVersion();
    }

    /**
     * DOC qiongli close editor by file.
     *
     * @param fileRes
     */
    public void closeEditorIfOpened(Item item) {
        itemIsOpening(item, true);
    }

    /**
     * check the item's editor is opening or not.
     *
     * @param item
     * @return
     */
    public boolean itemIsOpening(Item item) {
        return itemIsOpening(item, false);
    }

    /**
     * check the item's editor is opening or not.
     *
     * @param item
     * @param closeEditor close the editor if it is opening
     * @return
     */
    public boolean itemIsOpening(Item item, boolean closeEditor) {
        boolean opening = false;
        IWorkbenchPage activePage = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editorReferences = activePage.getEditorReferences();
        IEditorInput editorInput = null;
        Property property = item.getProperty();
        for (IEditorReference reference : editorReferences) {
            try {
                editorInput = reference.getEditorInput();
                if (editorInput instanceof FileEditorInput) {
                    FileEditorInput fileInput = (FileEditorInput) editorInput;

                    if (property.eResource() != null) {
                        IPath itemPath = PropertyHelper.getItemPath(property);
                        if (itemPath != null && itemPath.equals(fileInput.getFile().getFullPath())) {
                            opening = true;
                            if (closeEditor) {
                                activePage.closeEditor(reference.getEditor(false), false);
                            }
                            break;
                        }
                    }
                } else if (editorInput instanceof AbstractItemEditorInput) {
                    AbstractItemEditorInput input = (AbstractItemEditorInput) editorInput;
                    Item it = input.getItem();
                    if (it != null && property != null && it.getProperty().getId().equals(property.getId())) {
                        opening = true;
                        if (closeEditor) {
                            activePage.closeEditor(reference.getEditor(false), false);
                        }
                        break;
                    }
                }
            } catch (PartInitException e) {
                log.error(e);
                continue;
            }
        }
        return opening;
    }

    /**
     *
     * refresh the related connection which is opened in DQ side.
     *
     * @param item
     */
    public void refreshOpenedEditor(Item item) {
        if (item == null) {
            return;
        }
        IWorkbenchPage activePage = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editorReferences = activePage.getEditorReferences();
        Property property = item.getProperty();
        for (IEditorReference reference : editorReferences) {
            try {
                IEditorInput input = reference.getEditorInput();
                if (input instanceof AbstractItemEditorInput) {
                    AbstractItemEditorInput itemInput = (AbstractItemEditorInput) input;
                    Item it = itemInput.getItem();
                    if (it != null && property != null && it.getProperty().getId().equals(property.getId())) {
                        // make sure the item in editorInput is latest.
                        if (!item.equals(it)) {
                            itemInput.setRepNode(RepositoryNodeHelper.recursiveFind(property));
                        }
                        CommonFormEditor editor = (CommonFormEditor) reference.getEditor(false);
                        editor.refreshEditor();
                        break;
                    }
                }
            } catch (PartInitException e) {
                log.error(e);
                continue;
            }
        }
    }

    public List<AnalysisEditor> getCurrentOpenAnalysisEditor() {
        IWorkbenchPage activePage = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editorReferences = activePage.getEditorReferences();
        List<AnalysisEditor> result = new ArrayList<AnalysisEditor>();

        for (IEditorReference iEditorReference : editorReferences) {
            IEditorPart editor = iEditorReference.getEditor(false);
            if (editor instanceof AnalysisEditor) {
                result.add((AnalysisEditor) editor);
            }
        }
        return result;
    }

    /**
     * DOC zshen Comment method "initProxyRepository".
     */
    public ReturnCode initProxyRepository() {
        ReturnCode rc = new ReturnCode();
        Project project = null;
        RepositoryContext repositoryContext = (RepositoryContext) org.talend.core.runtime.CoreRuntimePlugin.getInstance()
                .getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (repositoryContext != null) {
            project = repositoryContext.getProject();
            User user = repositoryContext.getUser();

            ReponsitoryContextBridge.initialized(project.getEmfProject(), user);
        } else { // else project is null, then we are in TOP only
            Location instanceLoc = Platform.getInstanceLocation();
            try {
                if (instanceLoc.isLocked()) {
                    rc.setMessage(DefaultMessagesImpl.getString("CorePlugin.workspaceInUse"));//$NON-NLS-1$
                    rc.setOk(false);
                    return rc;
                } else {
                    instanceLoc.lock();
                }
            } catch (IOException e) {
                log.error(e, e);
            }
            ProxyRepositoryFactory proxyRepository = ProxyRepositoryFactory.getInstance();
            IRepositoryFactory repository = RepositoryFactoryProvider.getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID);
            if (repository == null) {
                log.fatal(DefaultMessagesImpl.getString("CorePlugin.noLocalRepositoryFound")); //$NON-NLS-1$
                rc.setMessage(DefaultMessagesImpl.getString("CorePlugin.noLocalRepositoryFound"));//$NON-NLS-1$
                rc.setOk(false);
                return rc;
            }
            proxyRepository.setRepositoryFactoryFromProvider(repository);
            try {
                proxyRepository.checkAvailability();
                proxyRepository.initialize();

                XmiResourceManager xmiResourceManager = proxyRepository.getRepositoryFactoryFromProvider().getResourceManager();
                IProject rootProject = ResourceManager.getRootProject();

                if (rootProject.getFile(FileConstants.LOCAL_PROJECT_FILENAME).exists()) {
                    // Initialize TDQ EMF model packages.
                    new EMFUtil();
                    project = new Project(xmiResourceManager.loadProject(rootProject));
                } else {
                    String username = "talend@talend.com"; //$NON-NLS-1$
                    String password = "talend@talend.com"; //$NON-NLS-1$

                    User user = PropertiesFactoryImpl.eINSTANCE.createUser();
                    user.setLogin(username);
                    user.setPassword(password.getBytes());
                    String projectName = ResourceManager.getRootProjectName();
                    String projectDesc = ResourcesPlugin.getWorkspace().newProjectDescription(projectName).getComment();
                    Project projectInfor = ProjectHelper.createProject(projectName, projectDesc, ECodeLanguage.JAVA.getName(),
                            user);

                    // MOD zshen create project by proxyRepository
                    checkFileName(projectInfor.getLabel(), RepositoryConstants.PROJECT_PATTERN);

                    project = proxyRepository.getRepositoryFactoryFromProvider().createProject(user, password, projectInfor);
                }

                if (project != null) {
                    initRepositoryContext(project);

                    // add status
                    String defaultTechnicalStatusList = "DEV development;TEST testing;PROD production"; //$NON-NLS-1$
                    List<Status> statusList = StatusHelper.parse(defaultTechnicalStatusList);
                    proxyRepository.setTechnicalStatus(statusList);

                    // TDQ-11125:setup MavenResolver properties for TOP(like as generate
                    // 'maven_user_settings.xml').before set, must check user setting first.
                    if (org.talend.commons.utils.platform.PluginChecker.isOnlyTopLoaded()) {
                        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMavenUIService.class)) {
                            IMavenUIService mavenUIService = (IMavenUIService) GlobalServiceRegister.getDefault().getService(
                                    IMavenUIService.class);
                            if (mavenUIService != null) {
                                mavenUIService.checkUserSettings(new NullProgressMonitor());
                                mavenUIService.updateMavenResolver(false);
                                mavenUIService.addMavenConfigurationChangeListener();
                            }
                        }
                        // deploy libraries and maven index here
                        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                            ILibrariesService librariesService = (ILibrariesService) GlobalServiceRegister.getDefault()
                                    .getService(ILibrariesService.class);
                            if (librariesService != null) {
                                librariesService.syncLibraries();
                                // TDQ-9529 check libararies install status at here,so that
                                // "Optional third-party libraries" is displayed in the "Additional Talend Package"
                                // dialog.
                                librariesService.checkLibraries();

                            }
                        }
                    }
                    CWMPlugin.getDefault().createLibFolderIfNotExist();
                    // TDQ-11348 the readOnlyUser property is false for TOP.
                    System.getProperties().put("ReadOnlyUser", new Boolean(false).toString()); //$NON-NLS-1$
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                rc.setMessage(e.getMessage());
                rc.setOk(false);
                return rc;
            }
        }
        return rc;
    }

    private void initRepositoryContext(Project project) {
        RepositoryContext repositoryContext = new RepositoryContext();
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        ctx.putProperty(Context.REPOSITORY_CONTEXT_KEY, repositoryContext);

        repositoryContext.setUser(project.getAuthor());
        repositoryContext.setClearPassword(project.getLabel());
        repositoryContext.setProject(project);
        repositoryContext.setFields(new HashMap<String, String>());
        //        repositoryContext.getFields().put(IProxyRepositoryFactory.BRANCH_SELECTION + "_" + project.getTechnicalLabel(), ""); //$NON-NLS-1$ //$NON-NLS-2$
        ProjectManager.getInstance().setMainProjectBranch(project, null);

        ReponsitoryContextBridge.initialized(project.getEmfProject(), project.getAuthor());
        // MOD zshen for bug tdq-4757 remove this init from corePlugin.start() to here because the initLocal command of
        // commandLine
        // TODO TDQ-9378
        // SQLExplorerPlugin.getDefault().setRootProject(ReponsitoryContextBridge.getRootProject());
        // SqlExplorerUtils.getDefault().initSqlExplorerRootProject();
    }

    /**
     * copy the method from ProxyRepositoryFactory to avoid tos migeration.
     *
     * @param fileName
     * @param pattern
     */
    private void checkFileName(String fileName, String pattern) {
        if (!Pattern.matches(pattern, fileName)) {
            throw new IllegalArgumentException(DefaultMessagesImpl.getString(
                    "ProxyRepositoryFactory.illegalArgumentException.labelNotMatchPattern", new Object[] { fileName, pattern })); //$NON-NLS-1$
        }
    }

    public ISemanticStudioService getSemanticStudioService() {
        if (service == null) {
            ServiceReference<?> serviceReference = bundleContext.getServiceReference(ISemanticStudioService.class.getName());
            if (serviceReference != null) {
                service = (ISemanticStudioService) bundleContext.getService(serviceReference);
            }
        }
        return service;
    }

    public synchronized void setSemanticStudioService(ISemanticStudioService service) {
        System.out.println("Service was set. Thank you DS!");
        this.service = service;
    }

    public synchronized void unsetSemanticStudioService(ISemanticStudioService service) {
        System.out.println("Service was unset. Why did you do this to me?");
        if (this.service == service) {
            this.service = null;
        }
    }

    /**
     * 
     * When the user is read only for current project then setting composite is disable
     * 
     * @param mainComposite
     */
    public void handleUserReadOnlyStatus(Composite mainComposite) {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            mainComposite.setEnabled(false);
        }
    }
}
