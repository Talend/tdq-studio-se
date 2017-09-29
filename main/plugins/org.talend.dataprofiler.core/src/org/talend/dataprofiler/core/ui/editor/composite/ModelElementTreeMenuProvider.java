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
package org.talend.dataprofiler.core.ui.editor.composite;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.DelimitedFileIndicator;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.service.GlobalServiceRegister;
import org.talend.dataprofiler.core.service.IDatabaseJobService;
import org.talend.dataprofiler.core.service.IJobService;
import org.talend.dataprofiler.core.ui.action.actions.TdAddTaskAction;
import org.talend.dataprofiler.core.ui.action.actions.predefined.CreateColumnAnalysisAction;
import org.talend.dataprofiler.core.ui.action.actions.predefined.PreviewColumnAction;
import org.talend.dataprofiler.core.ui.editor.pattern.PatternEditor;
import org.talend.dataprofiler.core.ui.editor.pattern.PatternItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.RespositoryDetailView;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyi class global comment. Detailled comment
 */
public abstract class ModelElementTreeMenuProvider {

    private static Logger log = Logger.getLogger(ModelElementTreeMenuProvider.class);

    protected Tree tree;

    public ModelElementTreeMenuProvider(Tree tree) {
        this.tree = tree;
    }

    /**
     * DOC qzhang Comment method "createTreeMenu". MOD qiongli bug 16252.Add a param of 'isColumnSetMenu'
     * 
     * @param newTree
     * @param containEdit
     */
    public void createTreeMenu(boolean isColumnSetMenu) {
        Menu oldMenu = tree.getMenu();
        if (oldMenu != null && !oldMenu.isDisposed()) {
            oldMenu.dispose();
        }
        Menu menu = new Menu(tree);

        if (isSelectedColumn(tree.getSelection())) {
            if (!isMetadataColumnSelected(tree.getSelection())) {
                MenuItem previewMenuItem = new MenuItem(menu, SWT.CASCADE);
                previewMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.previewDQElement")); //$NON-NLS-1$
                previewMenuItem.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
                previewMenuItem.addSelectionListener(new SelectionAdapter() {

                    /*
                     * (non-Javadoc)
                     * 
                     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected (org.eclipse
                     * .swt.events.SelectionEvent)
                     */
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        previewSelectedElements(tree);
                    }

                });
            }

            if (isColumnSetMenu) {
                MenuItem createColumnAnalysisMenuItem = new MenuItem(menu, SWT.CASCADE);
                createColumnAnalysisMenuItem.setText(DefaultMessagesImpl.getString("CreateColumnAnalysisAction.columnAnalysis")); //$NON-NLS-1$
                createColumnAnalysisMenuItem.setImage(ImageLib.getImage(ImageLib.ACTION_NEW_ANALYSIS));
                createColumnAnalysisMenuItem.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        createColumnAnalysis(tree);
                    }
                });
            }

            MenuItem showLocationMenuItem = new MenuItem(menu, SWT.CASCADE);
            showLocationMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.showDQElement")); //$NON-NLS-1$
            showLocationMenuItem.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
            showLocationMenuItem.addSelectionListener(new SelectionAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @seeorg.eclipse.swt.events.SelectionAdapter# widgetSelected(org.eclipse .swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {
                    showSelectedElements(tree);
                }

            });
        }

        if (isSelectedIndicator(tree.getSelection()) && !isMetadataColumnIndicatorSelected(tree.getSelection())) {
            // MOD 2009-01-04 mzhao
            MenuItem showQueryMenuItem = new MenuItem(menu, SWT.CASCADE);
            showQueryMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.viewQuery")); //$NON-NLS-1$
            showQueryMenuItem.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
            showQueryMenuItem.addSelectionListener(new SelectionAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected (org.eclipse .swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {
                    viewQueryForSelectedElement(tree);

                }
            });

        }

        if (isSelectedPatternIndicator(tree.getSelection())) {
            MenuItem editPatternMenuItem = new MenuItem(menu, SWT.CASCADE);
            editPatternMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.editPattern")); //$NON-NLS-1$
            editPatternMenuItem.setImage(ImageLib.getImage(ImageLib.PATTERN_REG));
            editPatternMenuItem.addSelectionListener(new SelectionAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @seeorg.eclipse.swt.events.SelectionAdapter# widgetSelected(org .eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {
                    editPattern(tree);
                }

            });
        }
        // MOD zshen 2010-06-02 featrue 12919
        // judge to indicator whether is frequency
        if (isSelectedFrequencyIndicator(tree.getSelection()) && PluginChecker.isTDCPLoaded()) {
            MenuItem editPatternMenuItem = new MenuItem(menu, SWT.CASCADE);
            editPatternMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.generateJob")); //$NON-NLS-1$
            editPatternMenuItem.setImage(ImageLib.getImage(ImageLib.ICON_PROCESS));
            editPatternMenuItem.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    final IDatabaseJobService service = (IDatabaseJobService) GlobalServiceRegister.getDefault().getService(
                            IJobService.class);
                    if (service != null) {
                        service.setIndicator(getSelectedIndicator(tree.getSelection()));
                        IRepositoryNode anaNode = getAnalysis2();
                        Analysis ana;
                        if (anaNode instanceof ReportAnalysisRepNode) {
                            ana = ((ReportAnalysisRepNode) anaNode).getAnalysis();
                        } else {
                            ana = ((AnalysisRepNode) anaNode).getAnalysis();
                        }
                        service.setAnalysis(ana);
                        service.executeJob();
                    }
                }
            });
        }
        if (isSelectedUDIndicator(tree.getSelection())) {
            MenuItem editPatternMenuItem = new MenuItem(menu, SWT.CASCADE);
            editPatternMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.editUDIndicator")); //$NON-NLS-1$
            editPatternMenuItem.setImage(ImageLib.getImage(ImageLib.IND_DEFINITION));
            editPatternMenuItem.addSelectionListener(new SelectionAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @seeorg.eclipse.swt.events.SelectionAdapter# widgetSelected(org .eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {
                    editUDIndicator(tree);
                }

            });
        }
        // add common menu to the tree
        MenuItem addTaskItem = new MenuItem(menu, SWT.CASCADE);
        addTaskItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.AddTask")); //$NON-NLS-1$
        addTaskItem.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addTaskItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org .eclipse .swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem[] selection = tree.getSelection();
                if (selection.length > 0) {
                    IRepositoryNode ana = getAnalysis2();
                    // MOD qiongli 2011-1-12 feature 16796 :handle the case of ModelElement name is null
                    (new TdAddTaskAction(tree.getShell(), ana)).run();
                }

            }
        });

        MenuItem deleteMenuItem = new MenuItem(menu, SWT.CASCADE);
        deleteMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.removeElement")); //$NON-NLS-1$
        deleteMenuItem.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        deleteMenuItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org .eclipse .swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedElements2(tree);
            }

        });

        tree.setMenu(menu);
    }

    /**
     * DOC yyi Comment method "removeSelectedElements".
     * 
     * @param tree
     */
    protected abstract void removeSelectedElements2(Tree tree);

    protected abstract IRepositoryNode getAnalysis2();

    private void editPattern(Tree tree) {
        TreeItem[] selection = tree.getSelection();
        if (selection.length > 0) {
            TreeItem treeItem = selection[0];
            IndicatorUnit indicatorUnit = (IndicatorUnit) treeItem.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY);
            PatternMatchingIndicator indicator = (PatternMatchingIndicator) indicatorUnit.getIndicator();
            Pattern pattern = indicator.getParameters().getDataValidDomain().getPatterns().get(0);
            RepositoryNode patternRecursiveFind = RepositoryNodeHelper.recursiveFind(pattern);
            PatternItemEditorInput patternItemEditorInput = new PatternItemEditorInput(patternRecursiveFind);
            CorePlugin.getDefault().openEditor(patternItemEditorInput, PatternEditor.class.getName());
        }
    }

    /**
     * DOC yyi Comment method "editUDIndicator" 2009-09-04.
     * 
     * @param tree
     */
    private void editUDIndicator(Tree tree) {
        TreeItem[] selection = tree.getSelection();
        if (selection.length > 0) {
            TreeItem treeItem = selection[0];
            IndicatorUnit indicatorUnit = (IndicatorUnit) treeItem.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY);
            UserDefIndicator indicator = (UserDefIndicator) indicatorUnit.getIndicator();
            IFile file = ResourceFileMap.findCorrespondingFile(indicator.getIndicatorDefinition());
            IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            try {
                activePage.openEditor(new FileEditorInput(file),
                        "org.talend.dataprofiler.core.ui.editor.indicator.IndicatorEditor"); //$NON-NLS-1$
            } catch (PartInitException e1) {
                log.error(e1, e1);
            }
        }
    }

    /**
     * DOC Zqin Comment method "previewSelectedElements".
     * 
     * @param newTree
     */
    private void previewSelectedElements(Tree newTree) {
        TreeItem[] items = newTree.getSelection();
        ModelElement[] mes = new ModelElement[items.length];

        for (int i = 0; i < items.length; i++) {
            ModelElementIndicator meIndicator = (ModelElementIndicator) items[i]
                    .getData(AbstractColumnDropTree.MODELELEMENT_INDICATOR_KEY);
            ModelElement me = ((MetadataColumnRepositoryObject) meIndicator.getModelElementRepositoryNode().getObject())
                    .getTdColumn();
            mes[i] = me;
        }

        new PreviewColumnAction(mes).run();
    }

    /**
     * 
     * DOC mzhao Comment method "viewQueryForSelectedElement".
     * 
     * @param newTree
     */
    private void viewQueryForSelectedElement(Tree newTree) {
        TreeItem[] selection = newTree.getSelection();
        for (TreeItem item : selection) {
            ModelElementIndicator meIndicator = (ModelElementIndicator) item
                    .getData(AbstractColumnDropTree.MODELELEMENT_INDICATOR_KEY);
            ConnectionItem connItem = (ConnectionItem) meIndicator.getModelElementRepositoryNode().getObject().getProperty()
                    .getItem();
            Connection dataprovider = connItem.getConnection();
            IndicatorUnit indicatorUnit = (IndicatorUnit) item.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY);
            DbmsLanguage dbmsLang = DbmsLanguageFactory.createDbmsLanguage(dataprovider);
            Expression expression = dbmsLang.getInstantiatedExpression(indicatorUnit.getIndicator());
            if (expression == null) {
                MessageDialogWithToggle
                        .openWarning(
                                null,
                                DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.Warn"), DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.NoQueryDefined")); //$NON-NLS-1$ //$NON-NLS-2$
                return;
            }

            SqlExplorerUtils.getDefault().runInDQViewer(dataprovider, expression.getBody(),
                    meIndicator.getModelElementRepositoryNode().getObject().getProperty().getLabel());
        }
    }

    /**
     * DOC Zqin Comment method "showSelectedElements".MOD 2009-01-07 mzhao.
     * 
     * @param newTree
     */
    public void showDetailView(Tree newTree) {
        TreeItem[] selection = newTree.getSelection();
        if (selection.length > 0) {
            RespositoryDetailView detailView = CorePlugin.getDefault().getRespositoryDetailView();
            if (detailView == null) {
                return;
            }

            DQRespositoryView dqview = CorePlugin.getDefault().getRepositoryView();
            RepositoryNode node = getSelectedNode(selection);
            detailView.selectionChanged(dqview, new StructuredSelection(node));
        }
    }

    /**
     * DOC Zqin Comment method "showSelectedElements".MOD 2009-01-07 mzhao.
     * 
     * @param newTree
     */
    private void showSelectedElements(Tree newTree) {
        TreeItem[] selection = newTree.getSelection();
        if (selection.length > 0) {
            DQRespositoryView dqview = CorePlugin.getDefault().findAndOpenRepositoryView();
            // if DqRepository view is not openning we will not do anything
            if (dqview == null) {
                return;
            }
            RepositoryNode node = getSelectedNode(selection);
            dqview.showSelectedElements(node);
        }
    }

    /**
     * DOC msjian Comment method "getSelectedNode".
     * 
     * @param selection
     * @return
     */
    protected RepositoryNode getSelectedNode(TreeItem[] selection) {
        ModelElementIndicator meIndicator = (ModelElementIndicator) selection[0]
                .getData(AbstractColumnDropTree.MODELELEMENT_INDICATOR_KEY);
        ModelElement me = ((ISubRepositoryObject) meIndicator.getModelElementRepositoryNode().getObject()).getModelElement();

        RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(me);
        if (recursiveFind == null) {
            recursiveFind = RepositoryNodeHelper.createRepositoryNode(me);
        }
        return recursiveFind;
    }

    private boolean isSelectedColumn(TreeItem[] items) {
        for (TreeItem item : items) {
            if (item.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY) != null) {
                return false;
            }
        }

        return true;
    }

    private boolean isSelectedIndicator(TreeItem[] items) {
        if (isSelectedColumn(items)) {
            return false;
        }

        return true;
    }

    private boolean isSelectedPatternIndicator(TreeItem[] items) {
        if (!isSelectedIndicator(items)) {
            return false;
        }

        for (TreeItem item : items) {
            IndicatorUnit unit = (IndicatorUnit) item.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY);
            if (unit != null) {

                Indicator indicator = unit.getIndicator();
                if (!(indicator instanceof PatternMatchingIndicator)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 
     * DOC zshen Comment method "isSelectedFrequencyIndicator". judge to indicator whether is frequency
     * 
     * @param items
     * @return
     */
    private boolean isSelectedFrequencyIndicator(TreeItem[] items) {
        if (!isSelectedIndicator(items)) {
            return false;
        }

        for (TreeItem item : items) {
            IndicatorUnit unit = (IndicatorUnit) item.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY);
            if (unit != null) {

                Indicator indicator = unit.getIndicator();
                if (!(indicator instanceof FrequencyIndicator)) {
                    return false;
                }
            }
        }

        return true;
    }

    private Indicator getSelectedIndicator(TreeItem[] items) {
        assert items.length == 1;
        if (!isSelectedIndicator(items)) {
            return null;
        }
        for (TreeItem item : items) {
            IndicatorUnit unit = (IndicatorUnit) item.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY);
            if (unit != null) {
                return unit.getIndicator();
            }
        }
        return null;
    }

    /**
     * 
     * DOC qiongli:if the selected item is MetadaColumn.
     * 
     * @param items
     * @return
     */
    private boolean isMetadataColumnSelected(TreeItem[] items) {
        for (TreeItem item : items) {
            Object data = item.getData(AnalysisColumnTreeViewer.MODELELEMENT_INDICATOR_KEY);
            if (data != null && data instanceof DelimitedFileIndicator) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * DOC qiongli Comment method "isMetadataColumnIndicatorSelected".
     * 
     * @param items
     * @return
     */
    private boolean isMetadataColumnIndicatorSelected(TreeItem[] items) {
        for (TreeItem item : items) {
            Object dataIndicator = item.getData(AnalysisColumnTreeViewer.INDICATOR_UNIT_KEY);
            if (dataIndicator != null && dataIndicator instanceof ColumnIndicatorUnit) {
                ColumnIndicatorUnit iu = (ColumnIndicatorUnit) dataIndicator;
                return iu.isMetadataColumn();
            }

        }
        return false;
    }

    /**
     * DOC yyi Comment method "isSelectedUDIndicator" 2009-09-04.
     * 
     * @param items
     * @return
     */
    private boolean isSelectedUDIndicator(TreeItem[] items) {
        if (!isSelectedIndicator(items)) {
            return false;
        }

        for (TreeItem item : items) {
            IndicatorUnit unit = (IndicatorUnit) item.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY);
            if (unit != null) {

                Indicator indicator = unit.getIndicator();
                if (!(indicator instanceof UserDefIndicator)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 
     * DOC qiongli Comment method "createColumnAnalysis".bug 16252.
     * 
     * @param newTree
     */
    private void createColumnAnalysis(Tree newTree) {
        TreeItem[] items = newTree.getSelection();
        if (items.length > 0) {
            TreePath[] paths = new TreePath[items.length];

            for (int i = 0; i < items.length; i++) {
                // MOD klliu bug 20820 change TDcolum to IRepositoryNode, then solve NPE 2011-04-29
                ModelElementIndicator data = (ModelElementIndicator) items[i].getData("MODELELEMENT_INDICATOR_KEY");//$NON-NLS-1$
                IRepositoryNode modelElementRepositoryNode = data.getModelElementRepositoryNode();
                paths[i] = new TreePath(new Object[] { modelElementRepositoryNode });
            }
            CreateColumnAnalysisAction analysisAction = new CreateColumnAnalysisAction();
            analysisAction.setSelection(new TreeSelection(paths));
            analysisAction.run();
        }
    }

}
