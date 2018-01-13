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
package org.talend.dataprofiler.core.ui.views;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.progress.UIJob;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.helper.WorkspaceVersionHelper;
import org.talend.dataprofiler.core.model.nodes.foldernode.ColumnFolderNode;
import org.talend.dataprofiler.core.model.nodes.foldernode.TableFolderNode;
import org.talend.dataprofiler.core.model.nodes.foldernode.ViewFolderNode;
import org.talend.dataprofiler.core.service.GlobalServiceRegister;
import org.talend.dataprofiler.core.service.IService;
import org.talend.dataprofiler.core.service.IViewerFilterService;
import org.talend.dataprofiler.core.ui.action.actions.DQEditContextAction;
import org.talend.dataprofiler.core.ui.action.actions.EditDFTableAction;
import org.talend.dataprofiler.core.ui.action.actions.EditFileDelimitedAction;
import org.talend.dataprofiler.core.ui.action.actions.EditHDFSConnectionAction;
import org.talend.dataprofiler.core.ui.action.actions.EditHadoopClusterAction;
import org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction;
import org.talend.dataprofiler.core.ui.editor.indicator.IndicatorEditor;
import org.talend.dataprofiler.core.ui.editor.indicator.IndicatorEditorInput;
import org.talend.dataprofiler.core.ui.filters.AbstractViewerFilter;
import org.talend.dataprofiler.core.ui.filters.EMFObjFilter;
import org.talend.dataprofiler.core.ui.filters.FolderObjFilter;
import org.talend.dataprofiler.core.ui.filters.ReportingFilter;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.layout.BorderLayout;
import org.talend.dataprofiler.migration.manager.MigrationTaskManager;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ContextRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFConnectionRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.dq.nodes.foldernode.AbstractFolderNode;
import org.talend.dq.nodes.hadoopcluster.HDFSOfHCConnectionNode;
import org.talend.dq.nodes.hadoopcluster.HadoopClusterConnectionRepNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceService;
import org.talend.utils.ProductVersion;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author rli
 */
public class DQRespositoryView extends CommonNavigator {

    protected static Logger log = Logger.getLogger(DQRespositoryView.class);

    public static final String ID = "org.talend.dataprofiler.core.ui.views.DQRespositoryView"; //$NON-NLS-1$

    private Map<String, AbstractViewerFilter> filterMap = new HashMap<String, AbstractViewerFilter>();

    private static final String VIEW_CONTEXT_ID = "org.talend.dataprofiler.core.ui.views.DQRespositoryView.viewScope"; //$NON-NLS-1$

    private ITreeContentProvider contentProvider = null;

    DQStructureManager manager;

    public DQRespositoryView() {
        super();

        // MOD qiongli 2010-9-7,bug 14698,add 'try...catch'
        try {

            RepositoryWorkUnit<Object> dQRepositoryWorkUnit = new RepositoryWorkUnit<Object>("Create DQ Repository structure") { //$NON-NLS-1$

                @Override
                protected void run() {
                    final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                        public void run(IProgressMonitor monitor) throws CoreException {
                            manager = DQStructureManager.getInstance();
                            if (manager.isNeedCreateStructure()) {
                                manager.createDQStructure();
                            }
                        }
                    };
                    IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                        public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            IWorkspace workspace = ResourcesPlugin.getWorkspace();
                            try {
                                ISchedulingRule schedulingRule = workspace.getRoot();
                                // the update the project files need to be done in the workspace runnable to
                                // avoid all notification of changes before the end of the modifications.
                                workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                            } catch (CoreException e) {
                                throw new InvocationTargetException(e);
                            }

                        }
                    };

                    try {
                        // do not use the UI related
                        iRunnableWithProgress.run(new NullProgressMonitor());
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }

                }
            };
            // TDQ-11267 by zshen ForceTransaction attribute make sure TDQ folder can be commit on the server
            dQRepositoryWorkUnit.setAvoidUnloadResources(true);
            dQRepositoryWorkUnit.setUnloadResourcesAfterRun(true);
            dQRepositoryWorkUnit.setFilesModifiedOutsideOfRWU(true);
            dQRepositoryWorkUnit.setForceTransaction(true);
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(dQRepositoryWorkUnit);

            if (manager.isNeedMigration()) {

                // TDQ-12154 commit all once after all migrations done.
                RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("Migration for DQ Repository") { //$NON-NLS-1$

                    @Override
                    protected void run() {
                        final IWorkspaceRunnable op = new IWorkspaceRunnable() {

                            public void run(IProgressMonitor monitor) throws CoreException {
                                ProductVersion wVersion = WorkspaceVersionHelper.getVesion();
                                new MigrationTaskManager(wVersion).doMigrationTask(monitor);
                            }
                        };
                        IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                            public void run(final IProgressMonitor monitor) throws InvocationTargetException,
                                    InterruptedException {
                                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                                try {
                                    ISchedulingRule schedulingRule = workspace.getRoot();
                                    // the update the project files need to be done in the workspace runnable to
                                    // avoid all notification of changes before the end of the modifications.
                                    workspace.run(op, schedulingRule, IWorkspace.AVOID_UPDATE, monitor);
                                } catch (CoreException e) {
                                    throw new InvocationTargetException(e);
                                }

                            }
                        };

                        try {
                            // need to display the progress on UI
                            ProgressUI.popProgressDialog(iRunnableWithProgress);
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }

                    }
                };

                workUnit.setAvoidUnloadResources(true);
                ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);

            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonNavigator#init(org.eclipse.ui.IViewSite, org.eclipse.ui.IMemento)
     */
    @Override
    public void init(IViewSite site, IMemento mem) throws PartInitException {
        super.init(site, mem);

        // MOD msjian TDQ-7840 2013-9-9: set the "Link with Editor" default value is "linked"
        if (mem == null) {
            setLinkingEnabled(true);
        }
        // TDQ-7840~

        // MOD qiongli 2010-9-7,bug 14698,add 'try...catch'
        try {
            addPostWindowCloseListener();

            initToolBar();

            initWorkspace();

            // TDQ-9711 init all ManagedDrivers and alias.
            if (SqlExplorerUtils.getDefault().isServiceInstalled()) {
                SqlExplorerUtils.getDefault().initAllConnectionsToSQLExplorer();
            }

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * DOC bZhou Comment method "initWorkspace".
     */
    private void initWorkspace() {

        // initialized resource persistence property.
        ResourceService.initResourcePersistence();

        // MOD qiongli 2011-3-2 feature 17588.initilize all folder.
        initAllFolders();
    }

    /**
     * DOC bZhou Comment method "initToolBar".
     */
    private void initToolBar() {
        IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
        toolBarManager.add(new RefreshDQReponsitoryViewAction());
    }

    /**
     * MOD mzhao bug 8581 Add pre window close listener.
     */
    private void addPostWindowCloseListener() {

        PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {

            public void postShutdown(IWorkbench workbench) {
                // do nothing here until now
            }

            public boolean preShutdown(IWorkbench workbench, boolean forced) {
                // MOD zshen it is duplicate with initWorkspace()
                return true;
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonNavigator#createPartControl(org.eclipse .swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent) {

        parent.setLayout(new BorderLayout());

        Composite topComp = new Composite(parent, SWT.NONE);
        topComp.setFont(parent.getFont());
        topComp.setLayoutData(BorderLayout.NORTH);

        Composite bottomComp = new Composite(parent, SWT.NONE);
        bottomComp.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        bottomComp.setFont(parent.getFont());
        bottomComp.setLayoutData(BorderLayout.CENTER);

        topComp.setLayout(new BorderLayout());

        FillLayout bottomLayout = new FillLayout(SWT.VERTICAL);
        bottomComp.setLayout(bottomLayout);

        createTreeFilter(topComp);
        super.createPartControl(bottomComp);

        // For removing the popup menu of DQRepositoryView.
        MenuManager menuMgr = new MenuManager("org.talend.dataprofiler.core.ui.views.DQRespositoryView"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager menuManager) {
                ISelection selection = getCommonViewer().getSelection();

                getNavigatorActionService().setContext(new ActionContext(selection));
                getNavigatorActionService().fillContextMenu(menuManager);
            }

        });

        Menu menu = menuMgr.createContextMenu(getCommonViewer().getTree());
        getCommonViewer().getTree().setMenu(menu);

        this.addViewerFilter(EMFObjFilter.FILTER_ID);
        this.addViewerFilter(ReportingFilter.FILTER_ID);
        this.addViewerFilter(FolderObjFilter.FILTER_ID);
        adjustFilter();
        activateContext();

        getCommonViewer().setSorter(null);
        getCommonViewer().getTree().addTreeListener(new TreeAdapter() {

            @Override
            public void treeExpanded(TreeEvent e) {
                TreeItem item = (TreeItem) e.item;
                if (!item.getText().endsWith(")")) { //$NON-NLS-1$
                    Object obj = item.getData();

                    if (obj instanceof TableFolderNode || obj instanceof ViewFolderNode || obj instanceof ColumnFolderNode) {
                        item.setText(item.getText() + "(" + item.getItemCount() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                super.treeExpanded(e);
            }

        });

        getCommonViewer().getTree().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {

                boolean superDoubleClick = true;
                Tree tree = (Tree) e.getSource();
                Point point = new Point(e.x, e.y);
                final TreeItem item = tree.getItem(point);
                if (item != null) {
                    Object obj = item.getData();

                    if (obj instanceof AbstractFolderNode) {
                        AbstractFolderNode node = (AbstractFolderNode) obj;
                        node.loadChildren();
                        Object[] children = node.getChildren();
                        if (children != null) {
                            if (item.getText().indexOf("(") > 0) { //$NON-NLS-1$
                                item.setText(item.getText().substring(0, item.getText().indexOf("(")) + "(" + children.length //$NON-NLS-1$ //$NON-NLS-2$
                                        + ")"); //$NON-NLS-1$
                            } else {
                                item.setText(item.getText() + "(" + children.length + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                            }

                        }

                    }

                    if (obj instanceof IndicatorDefinition) {
                        IndicatorDefinition indicatorDefinition = (IndicatorDefinition) obj;
                        // reload object
                        indicatorDefinition = DefinitionHandler.getInstance().getIndicatorDefinition(
                                indicatorDefinition.getLabel());
                        IndicatorEditorInput input = new IndicatorEditorInput(indicatorDefinition);
                        try {
                            CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
                                    .openEditor(input, IndicatorEditor.class.getName());
                        } catch (PartInitException e1) {
                            log.error(e1, e1);
                        }
                    }

                    if (obj instanceof DQRepositoryNode) {
                        if (obj instanceof ReportFileRepNode) {
                            new OpenItemEditorAction(new IRepositoryNode[] { (IRepositoryNode) obj }).run();
                        } else if (obj instanceof DFConnectionRepNode) {
                            new EditFileDelimitedAction((IRepositoryNode) obj).run();
                        } else if (obj instanceof HadoopClusterConnectionRepNode
                                && HadoopClusterUtils.getDefault().isServiceInstalled()) {
                            new EditHadoopClusterAction((IRepositoryNode) obj).run();
                        } else if (obj instanceof HDFSOfHCConnectionNode && HadoopClusterUtils.getDefault().isServiceInstalled()) {
                            new EditHDFSConnectionAction((IRepositoryNode) obj).run();
                        } else if (obj instanceof DFTableRepNode) {
                            new EditDFTableAction((IRepositoryNode) obj).run();
                        } else if (obj instanceof ContextRepNode) {
                            new DQEditContextAction((ContextRepNode) obj, false).run();
                        } else {
                            DQRepositoryNode repoNode = (DQRepositoryNode) obj;
                            if (RepositoryNodeHelper.canOpenEditor(repoNode)) {
                                OpenItemEditorAction openItemEditorAction = new OpenItemEditorAction(
                                        new IRepositoryNode[] { repoNode });
                                openItemEditorAction.run();

                            }
                            if (repoNode instanceof AnalysisRepNode || repoNode instanceof ReportRepNode
                                    || repoNode instanceof SysIndicatorDefinitionRepNode || repoNode instanceof PatternRepNode
                                    || repoNode instanceof RuleRepNode) {
                                superDoubleClick = false;
                            }
                        }
                    }
                }
                if (superDoubleClick) {
                    super.mouseDoubleClick(e);
                }
            }

        });
        getCommonViewer().getTree().addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
                    Tree tree = (Tree) e.getSource();
                    TreeItem[] selection = tree.getSelection();
                    for (TreeItem item : selection) {
                        Object data = item.getData();
                        DQRepositoryNode repoNode = (DQRepositoryNode) data;
                        if (RepositoryNodeHelper.canOpenEditor(repoNode)) {
                            OpenItemEditorAction openItemEditorAction = new OpenItemEditorAction(
                                    new IRepositoryNode[] { repoNode });
                            openItemEditorAction.run();
                        }

                    }
                }
            }

            public void keyReleased(KeyEvent e) {
                // do nothing here until now
            }
        });

        // ~ADD mzhao for feature 6233 Load columns when selecting a table (or
        // view) in DQ Repository view
        getCommonViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                TreeSelection selection = (TreeSelection) event.getSelection();
                Iterator<?> iterator = selection.iterator();
                while (iterator.hasNext()) {
                    Object selectedElement = iterator.next();
                    if (selectedElement instanceof DBTableRepNode || selectedElement instanceof DBViewRepNode) {
                        ((RepositoryNode) selectedElement).getChildren().get(0).getChildren();
                    }
                }
            }

        });

        // MOD mzhao 2011-03-10 bug 19147: filter MDM entries.
        getCommonViewer().addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IProject) {
                    return false;
                }
                return true;
            }

        });
        // ~
    }

    /**
     * DOC gdbu Comment method "createTreeFilter".
     * 
     * @param parent
     * @param _catalogToolBarMgr
     */
    protected void createTreeFilter(final Composite parent) {

        Composite centerComp = new Composite(parent, SWT.NONE);
        centerComp.setFont(parent.getFont());
        centerComp.setLayoutData(BorderLayout.CENTER);
        FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
        fillLayout.marginHeight = 2;
        centerComp.setLayout(fillLayout);

        final Text filterText = new Text(centerComp, SWT.BORDER);
        filterText.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        Composite eastComp = new Composite(parent, SWT.NONE);
        eastComp.setFont(parent.getFont());
        eastComp.setLayoutData(BorderLayout.EAST);
        eastComp.setLayout(new FillLayout(SWT.HORIZONTAL));

        final CoolBar coolBar = new CoolBar(eastComp, SWT.FLAT);
        CoolItem coolItem1 = new CoolItem(coolBar, SWT.FLAT);
        ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);

        ToolItem runFilterItem = new ToolItem(toolBar, SWT.FLAT);
        runFilterItem.setImage(ImageLib.getImage(ImageLib.FILTER_RUN));
        runFilterItem.setToolTipText(DefaultMessagesImpl.getString("DQRespositoryView.RunFilter"));//$NON-NLS-1$

        ToolItem previousMatchItem = new ToolItem(toolBar, SWT.FLAT);
        previousMatchItem.setImage(ImageLib.getImage(ImageLib.FILTER_UP));
        previousMatchItem.setToolTipText(DefaultMessagesImpl.getString("DQRespositoryView.ShowPreviousMatch"));//$NON-NLS-1$

        ToolItem nextMatchItem = new ToolItem(toolBar, SWT.FLAT);
        nextMatchItem.setImage(ImageLib.getImage(ImageLib.FILTER_DOWN));
        nextMatchItem.setToolTipText(DefaultMessagesImpl.getString("DQRespositoryView.ShowNextMatch"));//$NON-NLS-1$

        ToolItem closeFilterItem = new ToolItem(toolBar, SWT.FLAT);
        closeFilterItem.setImage(ImageLib.getImage(ImageLib.FILTER_CLOSE));
        closeFilterItem.setToolTipText(DefaultMessagesImpl.getString("DQRespositoryView.CloseFilter"));//$NON-NLS-1$

        toolBar.pack();
        Point size = toolBar.getSize();
        coolItem1.setControl(toolBar);
        coolItem1.setSize(coolItem1.computeSize(size.x, size.y));
        coolItem1.setMinimumSize(size);

        coolBar.setLocked(true);
        runFilterItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                String filterStr = filterText.getText() + PluginConstant.EMPTY_STRING;
                Shell shell = filterText.getShell();
                runFilter(filterStr, shell);
            }
        });

        previousMatchItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (DQRepositoryNode.isOnFilterring()) {
                    TreeItem[] selectionNode = getCommonViewer().getTree().getSelection();
                    if (0 == selectionNode.length) {
                        IRepositoryNode filteredNode = RepositoryNodeHelper.getFilteredNode();
                        IRepositoryNode previousFilteredNode = RepositoryNodeHelper.getPreviouFilteredNode(filteredNode);
                        if (null != previousFilteredNode) {
                            RepositoryNodeHelper.setFilteredNode(previousFilteredNode);
                            showSelectedElements(previousFilteredNode);
                        }
                    } else {
                        TreeItem selectionTreeItem = selectionNode[(selectionNode.length - 1)];
                        IRepositoryNode repoNode = (IRepositoryNode) selectionTreeItem.getData();
                        IRepositoryNode previousFilteredNode = RepositoryNodeHelper.getPreviouFilteredNode(repoNode);
                        if (null != previousFilteredNode) {
                            RepositoryNodeHelper.setFilteredNode(previousFilteredNode);
                            // showSelectedElements((RepositoryNode) previousFilteredNode);
                            DQRepositoryNode.setOnDisplayNextOrPreviousNode(true);
                            StructuredSelection structSel = new StructuredSelection(previousFilteredNode);
                            getCommonViewer().setSelection(structSel);
                            DQRepositoryNode.setOnDisplayNextOrPreviousNode(false);
                        }
                    }
                }
            }
        });

        nextMatchItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (DQRepositoryNode.isOnFilterring()) {
                    TreeItem[] selectionNode = getCommonViewer().getTree().getSelection();
                    if (0 == selectionNode.length) {
                        IRepositoryNode filteredNode = RepositoryNodeHelper.getFilteredNode();
                        IRepositoryNode nextFilteredNode = RepositoryNodeHelper.getNextFilteredNode(filteredNode);
                        if (null != nextFilteredNode) {
                            RepositoryNodeHelper.setFilteredNode(nextFilteredNode);
                            showSelectedElements(nextFilteredNode);
                        }
                    } else {
                        TreeItem selectionTreeItem = selectionNode[(selectionNode.length - 1)];
                        IRepositoryNode repoNode = (IRepositoryNode) selectionTreeItem.getData();
                        IRepositoryNode nextFilteredNode = RepositoryNodeHelper.getNextFilteredNode(repoNode);
                        if (null != nextFilteredNode) {
                            RepositoryNodeHelper.setFilteredNode(nextFilteredNode);
                            // showSelectedElements((RepositoryNode) nextFilteredNode);
                            DQRepositoryNode.setOnDisplayNextOrPreviousNode(true);
                            getCommonViewer().expandToLevel(nextFilteredNode, 1);
                            StructuredSelection structSel = new StructuredSelection(nextFilteredNode);
                            getCommonViewer().setSelection(structSel);
                            DQRepositoryNode.setOnDisplayNextOrPreviousNode(false);
                        }
                    }
                }
            }
        });

        closeFilterItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (DQRepositoryNode.isOnFilterring()) {
                    closeFilterStatus(filterText);
                }
            }
        });

        filterText.addKeyListener(new KeyAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyPressed(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
                    String filterStr = filterText.getText() + PluginConstant.EMPTY_STRING;
                    Shell shell = filterText.getShell();
                    runFilter(filterStr, shell);
                }
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if (DQRepositoryNode.isOnFilterring()) {
                    if (e.keyCode == SWT.BS || e.keyCode == SWT.DEL) {
                        if (isFilterTextEmpty(filterText)) {
                            new UIJob(PluginConstant.EMPTY_STRING) {

                                @Override
                                public IStatus runInUIThread(IProgressMonitor monitor) {
                                    if (isFilterTextEmpty(filterText)) {
                                        closeFilterStatus(filterText);
                                    }
                                    return Status.OK_STATUS;
                                }
                            }.schedule(2000);
                        }
                    }
                }
            }
        });
    }

    private void closeFilterStatus(Text filterText) {
        RepositoryNodeHelper.setFilteredNode(null);
        DQRepositoryNode.setFilterStr(PluginConstant.EMPTY_STRING);
        DQRepositoryNode.setFiltering(false);
        filterText.setText(PluginConstant.EMPTY_STRING);
        getCommonViewer().collapseAll();
        refresh();
    }

    private boolean isFilterTextEmpty(Text filterText) {
        String filterString = filterText.getText().trim() + PluginConstant.EMPTY_STRING;
        if (filterString.equals(PluginConstant.EMPTY_STRING)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * DOC gdbu DQRespositoryView class global comment. Detailled comment
     */
    class RunFilterThread extends Thread {

        private Shell shell = null;

        private String filterStr = null;

        public RunFilterThread(String filterStr, Shell shell) {
            this.shell = shell;
            this.filterStr = filterStr;
        }

        @Override
        public void run() {

            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    ProgressDialog progressDialog = new ProgressDialog(shell) {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                            monitor.beginTask("Filtering...", 11); //$NON-NLS-1$

                            getCommonViewer().collapseAll();
                            monitor.worked(1);
                            try {
                                DQRepositoryNode.setFilterStr(filterStr);
                                if (filterStr.trim().equals(PluginConstant.EMPTY_STRING)) {
                                    DQRepositoryNode.setFiltering(false);
                                } else {
                                    DQRepositoryNode.setFiltering(true);
                                    RepositoryNodeHelper.fillTreeList(monitor);
                                }
                            } catch (Exception exception) {
                                log.error(exception, exception);
                            } finally {
                                final IRepositoryNode firstFilteredNode = RepositoryNodeHelper.getFirstFilteredNode();
                                if (null != firstFilteredNode) {
                                    RepositoryNodeHelper.setFilteredNode(firstFilteredNode);
                                    monitor.worked(1);
                                    showSelectedElements(firstFilteredNode);
                                    monitor.worked(1);
                                }
                                monitor.done();
                            }
                        }
                    };
                    try {
                        progressDialog.executeProcess();
                    } catch (InvocationTargetException e) {
                        log.error(e, e);
                    } catch (InterruptedException e) {
                        log.error(e, e);
                    }
                }
            });
        }
    }

    private void runFilter(final String filterStr, final Shell shell) {
        RunFilterThread runFilterThread = new RunFilterThread(filterStr, shell);
        runFilterThread.run();
    }

    static class ViewColumnViewerToolTipSupport extends ColumnViewerToolTipSupport {

        protected ViewColumnViewerToolTipSupport(ColumnViewer viewer, int style, boolean manualActivation) {
            super(viewer, style, manualActivation);
        }

        @Override
        protected Composite createViewerToolTipContentArea(Event event, ViewerCell cell, Composite parent) {
            final Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(new RowLayout(SWT.VERTICAL));
            Text text = new Text(composite, SWT.SINGLE);
            text.setText(getText(event));
            text.setSize(100, 60);
            DateTime calendar = new DateTime(composite, SWT.CALENDAR);
            calendar.setEnabled(false);
            calendar.setSize(100, 100);
            composite.pack();
            return composite;
        }

        public static final void enableFor(final ColumnViewer viewer) {
            new ViewColumnViewerToolTipSupport(viewer, ToolTip.NO_RECREATE, false);
        }

    }

    /**
     * Activate a context that this view uses. It will be tied to this view activation events and will be removed when
     * the view is disposed.
     */
    private void activateContext() {
        IContextService contextService = (IContextService) getSite().getService(IContextService.class);
        contextService.activateContext(VIEW_CONTEXT_ID);
    }

    private void adjustFilter() {
        List<IService> filterList = GlobalServiceRegister.getDefault().getServiceGroup(IViewerFilterService.class);
        for (IService service : filterList) {
            if (service instanceof IViewerFilterService) {
                IViewerFilterService filterService = (IViewerFilterService) service;
                if (filterService.isAddOrDel()) {
                    this.addViewerFilter(filterService.getViwerFilterId());
                } else {
                    this.removeViewerFilter(filterService.getViwerFilterId());
                }
            }
        }
    }

    public void addViewerFilter(int viewerFilterId) {
        if (filterMap.containsKey(String.valueOf(viewerFilterId))) {
            return;
        }
        AbstractViewerFilter filter = null;
        switch (viewerFilterId) {
        case EMFObjFilter.FILTER_ID:
            filter = new EMFObjFilter();
            filterMap.put(String.valueOf(EMFObjFilter.FILTER_ID), filter);
            break;
        case ReportingFilter.FILTER_ID:
            filter = new ReportingFilter();
            filterMap.put(String.valueOf(ReportingFilter.FILTER_ID), filter);
            break;
        case FolderObjFilter.FILTER_ID:
            filter = new FolderObjFilter();
            filterMap.put(String.valueOf(FolderObjFilter.FILTER_ID), filter);
            break;
        default:
            filter = new ReportingFilter();
            filterMap.put(String.valueOf(ReportingFilter.FILTER_ID), filter);
        }
        getCommonViewer().addFilter(filter);
    }

    public void removeViewerFilter(int viewerFilterId) {
        String filterKey = String.valueOf(viewerFilterId);
        if (filterMap.containsKey(filterKey)) {
            this.getCommonViewer().removeFilter(filterMap.get(filterKey));
            this.filterMap.remove(filterKey);
        }
    }

    /**
     * DOC Zqin Comment method "showSelectedElements".
     * 
     * MOD 2009-01-07 mzhao for feature:0005664
     * 
     * @param newTree
     */
    public void showSelectedElements(Object selectedElement) {
        try {
            // MOD by zshen for bug 12940 refresh the viewer to collapse all the element.
            StructuredSelection structSel = new StructuredSelection(selectedElement);
            getCommonViewer().setSelection(structSel);

            // If not select,unfold tree structure to this column.
            StructuredSelection selectionTarge = (StructuredSelection) getCommonViewer().getSelection();
            if (!selectionTarge.equals(structSel)) {
                getCommonViewer().refresh();
                StructuredSelection recursiveExpandTree = recursiveExpandTree(selectedElement);
                if (recursiveExpandTree != null) {
                    structSel = recursiveExpandTree;
                }
                getCommonViewer().setSelection(structSel);
            }
            this.setFocus();
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * 
     * DOC mzhao Comment method "recursiveExpandTree".
     * 
     * @param commonViewer
     * @param provider
     * @param item
     */
    private StructuredSelection recursiveExpandTree(Object item) {
        if (contentProvider == null) {
            contentProvider = (ITreeContentProvider) getCommonViewer().getContentProvider();
        }
        // MOD xqliu 2011-01-14 bug 15750: show in DQ Repository View
        if (item instanceof RepositoryNode) {
            // RepositoryNode node = ((RepositoryNode) item).getParent();
            // if (node == null) {
            RepositoryNode recursiveFind = findRealNode(item);
            if (recursiveFind == null) {
                return null;
            }
            RepositoryNode node = recursiveFind.getParent();
            // }
            recursiveExpandTree(node);
            getCommonViewer().expandToLevel(node, 1);
            return new StructuredSelection(recursiveFind);
        }
        return null;
    }

    /**
     * Because the exist for filter on the repositoryView we maybe use a temp node to display so we need to find out
     * real one at here.
     * 
     * @param item
     * @return
     */
    private RepositoryNode findRealNode(Object item) {
        ModelElement modelElementFromRepositoryNode = RepositoryNodeHelper
                .getModelElementFromRepositoryNode((RepositoryNode) item);
        if (modelElementFromRepositoryNode == null) {// for root node case
            return null;
        }
        RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(modelElementFromRepositoryNode);
        if (recursiveFind == null) {
            log.warn(DefaultMessagesImpl.getString("DQRepositoryView.nodeNotExistWarring", //$NON-NLS-1$
                    modelElementFromRepositoryNode.getName()));
            MessageDialog.openWarning(null, DefaultMessagesImpl.getString("DQRepositoryView.nodeNotExistWarring.Title"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("DQRepositoryView.nodeNotExistWarring", //$NON-NLS-1$
                            modelElementFromRepositoryNode.getName()));
            return null;
        }
        return recursiveFind;
    }

    /**
     * DOC bZhou DQRespositoryView class global comment. Detailled comment
     */
    class RefreshDQReponsitoryViewAction extends RefreshAction {

        public RefreshDQReponsitoryViewAction() {
            super(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
            setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.REFRESH_SPACE));
        }

        @Override
        public void run() {
            RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(ProjectManager.getInstance().getCurrentProject(),
                    "refresh DQ Reponsitory View") { //$NON-NLS-1$

                @Override
                protected void run() throws LoginException, PersistenceException {
                    // equals super.run()
                    // new RefreshAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow()).run();
                    CorePlugin.getDefault().refreshWorkSpace();
                    ProxyRepositoryManager.getInstance().refresh();
                    getCommonViewer().refresh();
                }

            };
            // TDQ-12262: set to false, because for git remote project, when the item file is updated, need to unload.
            repositoryWorkUnit.setAvoidUnloadResources(false);
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
            try {
                repositoryWorkUnit.throwPersistenceExceptionIfAny();
            } catch (PersistenceException e) {
                log.error(e);
            }
        }
    }

    public void refresh() {
        new RefreshDQReponsitoryViewAction().run();
    }

    @Override
    protected void handleDoubleClick(DoubleClickEvent anEvent) {
        IStructuredSelection selection = (IStructuredSelection) anEvent.getSelection();
        Object element = selection.getFirstElement();
        RepositoryNode repoNode = (RepositoryNode) element;
        if (repoNode != null && repoNode.canExpandForDoubleClick()) {
            super.handleDoubleClick(anEvent);
        }
    }

    /**
     * 
     * DOC qiongli Comment method "initAllFolder".
     */
    private void initAllFolders() {
        Project newProject = ProjectManager.getInstance().getCurrentProject();
        List<FolderItem> folderItems = ProjectManager.getInstance().getFolders(newProject.getEmfProject());
        try {
            for (FolderItem folder : new ArrayList<FolderItem>(folderItems)) {
                if (WorkbenchUtils.isTDQOrMetadataRootFolder(folder, ProjectManager.getInstance().getCurrentProject()
                        .getEmfProject())) {
                    ERepositoryObjectType type = WorkbenchUtils.getFolderContentType(folder);
                    // MOD by zshen to avoid NullPointerException.
                    if (type != null) {
                        ProxyRepositoryFactory.getInstance().getAll(type, true);
                    }
                }
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }
    }

}
