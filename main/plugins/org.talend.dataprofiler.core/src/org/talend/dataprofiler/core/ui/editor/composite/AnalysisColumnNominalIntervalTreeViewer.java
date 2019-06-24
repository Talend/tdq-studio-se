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
package org.talend.dataprofiler.core.ui.editor.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ColumnIndicator;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.editor.AbstractAnalysisActionHandler;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.analysis.CorrelationAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.views.ColumnViewerDND;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.RespositoryDetailView;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 *
 * DOC zhaoxinyi class global comment. Detailled comment
 */
public class AnalysisColumnNominalIntervalTreeViewer extends AbstractColumnDropTree {

    protected static Logger log = Logger.getLogger(AnalysisColumnNominalIntervalTreeViewer.class);

    public static final String VIEWER_KEY = "org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnNominalIntervalTreeViewer"; //$NON-NLS-1$

    private Composite parentComp;

    private Button[] buttons;

    private List<RepositoryNode> columnSetMultiValueList;

    private CorrelationAnalysisDetailsPage masterPage;

    private Menu menu;

    private MenuItem editPatternMenuItem;

    public AnalysisColumnNominalIntervalTreeViewer(Composite parent) {
        parentComp = parent;
        tree = createTree(parent);
        tree.setData(this);
        columnSetMultiValueList = new ArrayList<RepositoryNode>();
    }

    public AnalysisColumnNominalIntervalTreeViewer(Composite parent, CorrelationAnalysisDetailsPage masterPage) {
        this(parent);
        this.masterPage = masterPage;
        createButtonSection(parent.getParent());
        // this.setElements(masterPage.getColumnSetMultiValueIndicator().
        // getAnalyzedColumns());
        this.setDirty(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#getMasterPage()
     */
    @Override
    public AbstractAnalysisMetadataPage getMasterPage() {
        return masterPage;
    }

    /**
     * @param parent
     */
    private Tree createTree(Composite parent) {
        final Tree newTree = new Tree(parent, SWT.MULTI | SWT.BORDER);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(newTree);

        newTree.setHeaderVisible(true);
        createTreeItem(newTree, 300, "AnalysisColumnTreeViewer.analyzedColumns"); //$NON-NLS-1$
        createTreeDataminingItem(newTree);
        createTreeItem(newTree, 80, "AnalysisColumnTreeViewer.operation"); //$NON-NLS-1$

        parent.layout();
        createTreeMenu(newTree, false);

        AbstractAnalysisActionHandler actionHandler = new AbstractAnalysisActionHandler(parent) {

            @Override
            protected void handleRemove() {
                removeSelectedElements(newTree);
                // MOD mzhao 2005-05-05 bug 6587.
                // MOD mzhao 2009-06-8, bug 5887.
                // updateBindConnection(masterPage, tree);
            }

        };

        parent.setData(AbstractMetadataFormPage.ACTION_HANDLER, actionHandler);

        addSourceDND(newTree);
        addTargetDND(newTree);

        addTreeListener(newTree);
        return newTree;
    }

    /**
     *
     * DOC zshen Comment method "createButtonSection".
     *
     * @param topComp create the button with delButton,moveUpButton,moveDownButton
     */
    private void createButtonSection(Composite topComp) {
        Composite buttonsComp = masterPage.getEditor().getToolkit().createComposite(topComp, SWT.NONE);
        buttonsComp.setLayout(new GridLayout(3, true));
        buttonsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        GridData buttonGridData = new GridData();
        buttonGridData.heightHint = 25;
        buttonGridData.horizontalAlignment = GridData.CENTER;
        buttonGridData.verticalAlignment = GridData.FILL;
        buttonGridData.grabExcessHorizontalSpace = true;
        buttonGridData.grabExcessVerticalSpace = true;

        final Button delButton = new Button(buttonsComp, SWT.NULL);
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setLayoutData(buttonGridData);
        final Button moveUpButton = new Button(buttonsComp, SWT.NULL);
        moveUpButton.setText(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.moveUp")); //$NON-NLS-1$
        moveUpButton.setLayoutData(buttonGridData);
        final Button moveDownButton = new Button(buttonsComp, SWT.NULL);
        moveDownButton.setText(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.moveDown")); //$NON-NLS-1$
        moveDownButton.setLayoutData(buttonGridData);
        buttons = new Button[] { delButton, moveUpButton, moveDownButton };
        enabledButtons(false);
        moveUpButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveElement(masterPage.getTreeViewer(), false);
            }
        });
        moveDownButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveElement(masterPage.getTreeViewer(), true);
            }

        });
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Tree currentTree = tree;
                Object[] selectItem = currentTree.getSelection();
                List<RepositoryNode> columnList = masterPage.getTreeViewer().getColumnSetMultiValueList();
                for (Object element : selectItem) {
                    Object removeElement = ((TreeItem) element)
                            .getData(AnalysisColumnNominalIntervalTreeViewer.COLUMN_INDICATOR_KEY);
                    columnList.remove(removeElement);
                }
                masterPage.getTreeViewer().setInput(columnList.toArray());
                enabledButtons(false);
            }

        });

    }

    /**
     *
     * DOC zshen Comment method "enabledButtons".
     *
     * @param enabled the state of buttons.
     *
     * change the state of buttons.
     */
    public void enabledButtons(boolean enabled) {
        for (Button button : buttons) {
            button.setEnabled(enabled);
        }
    }

    /**
     *
     * DOC zshen Comment method "moveElement".
     *
     * @param columnsElementViewer
     * @param isDown
     *
     * move the element of the columnList to up or down.
     */

    private void moveElement(AnalysisColumnNominalIntervalTreeViewer columnsElementViewer, boolean isDown) {
        Tree currentTree = columnsElementViewer.getTree();
        Object[] selectItem = currentTree.getSelection();
        List<RepositoryNode> columnList = columnsElementViewer.getColumnSetMultiValueList();
        int index = 0;
        // boolean moveFlag = false;
        List<Integer> indexArray = new ArrayList<Integer>();
        if (isDown) {
            for (int i = selectItem.length - 1; i >= 0; i--) {
                index = currentTree.indexOf((TreeItem) selectItem[i]);
                if ((index + 1) >= columnList.size()) {
                    return;
                } else {
                    RepositoryNode moveElement = (RepositoryNode) ((TreeItem) selectItem[i])
                            .getData(AnalysisColumnNominalIntervalTreeViewer.COLUMN_INDICATOR_KEY);
                    columnList.remove(moveElement);
                    columnList.add((index + 1), moveElement);
                    indexArray.add(index + 1);
                }
            }
        } else {
            for (Object element : selectItem) {
                index = currentTree.indexOf((TreeItem) element);
                if ((index - 1) < 0) {
                    return;
                } else {
                    RepositoryNode moveElement = (RepositoryNode) ((TreeItem) element)
                            .getData(AnalysisColumnNominalIntervalTreeViewer.COLUMN_INDICATOR_KEY);
                    columnList.remove(moveElement);
                    columnList.add((index - 1), moveElement);
                    indexArray.add(index - 1);
                }
            }
        }
        columnsElementViewer.setInput(convertList(columnList).toArray());
        currentTree = columnsElementViewer.getTree();
        for (int i = 0; i < indexArray.size(); i++) {
            currentTree.select(currentTree.getItem(indexArray.get(i)));
        }
    }

    private List<RepositoryNode> convertList(List<RepositoryNode> columnList) {
        List<RepositoryNode> resultList = new ArrayList<RepositoryNode>();
        for (int i = columnList.size() - 1; i >= 0; i--) {
            resultList.add(columnList.get(i));
        }
        return resultList;
    }

    /**
     * DOC bZhou Comment method "addTargetDND".
     *
     * @param newTree
     */
    private void addTargetDND(final Tree newTree) {
        ColumnViewerDND.installDND(newTree);
    }

    /**
     * DOC bZhou Comment method "addSourceDND".
     *
     * @param newTree
     */
    private void addSourceDND(final Tree newTree) {
        final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
        Transfer[] types = new Transfer[] { transfer };
        int operations = DND.DROP_COPY | DND.DROP_MOVE;
        final DragSource source = new DragSource(newTree, operations);
        source.setTransfer(types);
        final TreeItem[] dragSourceItem = new TreeItem[1];
        source.addDragListener(new DragSourceListener() {

            public void dragStart(DragSourceEvent event) {
                TreeItem[] selection = newTree.getSelection();
                dragSourceItem[0] = selection[0];
                if (selection.length > 0) {
                    event.doit = true;
                    transfer.setSelection(new StructuredSelection(selection[0].getData(COLUMN_INDICATOR_KEY)));
                    getColumnSetMultiValueList().remove(selection[0].getData(COLUMN_INDICATOR_KEY));
                } else {
                    event.doit = false;
                }
            };

            public void dragSetData(DragSourceEvent event) {
                event.data = dragSourceItem[0];
            }

            public void dragFinished(DragSourceEvent event) {
                if (event.detail == DND.DROP_MOVE) {
                    removeItemBranch(dragSourceItem[0]);
                    tree.forceFocus();
                }
                dragSourceItem[0] = null;

            }
        });
    }

    /**
     * DOC xzhao Comment method "createTreeMenu".
     *
     * @param newTree
     * @param containEdit
     */
    private void createTreeMenu(final Tree newTree, boolean containEdit) {
        Menu oldMenu = newTree.getMenu();
        if (oldMenu != null && !oldMenu.isDisposed()) {
            oldMenu.dispose();
        }
        menu = new Menu(newTree);
        MenuItem deleteMenuItem = new MenuItem(menu, SWT.CASCADE);
        deleteMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.removeElement")); //$NON-NLS-1$
        deleteMenuItem.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        deleteMenuItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             *
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse .swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedElements(newTree);
                // MOD mzhao 2005-05-05 bug 6587.
                // MOD mzhao 2009-06-8, bug 5887.
                // updateBindConnection(masterPage, tree);
            }

        });

        // ADD 2009-01-07 mzhao for feature:0005664
        MenuItem showMenuItem = new MenuItem(menu, SWT.CASCADE);
        showMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.showDQElement")); //$NON-NLS-1$
        showMenuItem.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
        showMenuItem.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             *
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse .swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem[] selection = newTree.getSelection();

                if (selection.length > 0) {
                    // if DqRepository view is not openning we will not do anything
                    DQRespositoryView dqview = CorePlugin.getDefault().findAndOpenRepositoryView();
                    if (dqview == null) {
                        return;
                    }
                    // TdColumn tdColumn = (TdColumn) selection[0].getData(COLUMN_INDICATOR_KEY);
                    dqview.showSelectedElements(selection[0].getData(COLUMN_INDICATOR_KEY));
                }
            }

        });

        if (containEdit) {
            editPatternMenuItem = new MenuItem(menu, SWT.CASCADE);
            editPatternMenuItem.setText(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.editPattern")); //$NON-NLS-1$
            editPatternMenuItem.setImage(ImageLib.getImage(ImageLib.PATTERN_REG));
            editPatternMenuItem.addSelectionListener(new SelectionAdapter() {

                /*
                 * (non-Javadoc)
                 *
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org .eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {
                    TreeItem[] selection = tree.getSelection();
                    if (selection.length > 0) {
                        TreeItem treeItem = selection[0];
                        IndicatorUnit indicatorUnit = (IndicatorUnit) treeItem.getData(INDICATOR_UNIT_KEY);
                        PatternMatchingIndicator indicator = (PatternMatchingIndicator) indicatorUnit.getIndicator();
                        Pattern pattern = indicator.getParameters().getDataValidDomain().getPatterns().get(0);
                        // MOD mzhao 2009-03-13 Feature 6066 Move all folders
                        // into one project.
                        IFile file = ResourceFileMap.findCorrespondingFile(pattern);
                        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        try {
                            activePage.openEditor(new FileEditorInput(file),
                                    "org.talend.dataprofiler.core.ui.editor.pattern.PatternEditor"); //$NON-NLS-1$
                        } catch (PartInitException e1) {
                            log.error(e1, e1);
                        }
                    }
                }

            });
        }
        newTree.setMenu(menu);
    }

    @Override
    public void setInput(Object[] objs) {
        // MOD xqliu 2011-01-11 bug 15750
        // if (!RepositoryNodeHelper.hasColumnNode(objs) && !RepositoryNodeHelper.hasTdColumn(objs)) {
        // return;
        // }
        // List<DBColumnRepNode> columnNodeList = RepositoryNodeHelper.getColumnNodeList(objs);
        List<IRepositoryNode> columnNodes = this.getColumnNodes(objs);
        if (columnNodes.size() == 0) {
            TreeItem[] items = this.tree.getItems();
            this.removeSelectedElements(items);
            return;
        }
        this.setElements(columnNodes);
    }

    public void setElements(final Object columnNodes) {
        this.tree.dispose();
        this.tree = createTree(this.parentComp);
        tree.setData(this);
        // MOD mzhao bug 8282 2009-7-31 Clear column cache.
        columnSetMultiValueList.clear();
        addItemElements(getColumnNodes(columnNodes), 0);
        // addItemElements(columns);
        // MOD mzhao 2009-05-05 bug 6587.
        updateBindConnection(masterPage, tree);
    }

    /**
     * DOC xqliu Comment method "getColumnNodes".
     *
     * @param columnNodes
     * @return
     */
    private List<IRepositoryNode> getColumnNodes(Object columnNodes) {
        List<IRepositoryNode> result = new ArrayList<IRepositoryNode>();
        List<DBColumnRepNode> columnNodeList = null;
        List<TdColumn> columnList = null;
        if (columnNodes instanceof Object[]) {
            columnNodeList = RepositoryNodeHelper.getColumnNodeList((Object[]) columnNodes);
            columnList = RepositoryNodeHelper.getTdColumnList((Object[]) columnNodes);
        } else if (columnNodes instanceof List) {
            columnNodeList = RepositoryNodeHelper.getColumnNodeList(((List) columnNodes).toArray());
            columnList = RepositoryNodeHelper.getTdColumnList(((List) columnNodes).toArray());
        }
        if (columnNodeList != null) {
            result.addAll(columnNodeList);
        }
        if (columnList != null) {
            result.addAll(columns2Nodes(columnList));
        }
        return result;
    }

    private void addItemElements(final List<IRepositoryNode> columns, int index) {
        for (int i = 0; i < columns.size(); i++) {
            final RepositoryNode columnNode = (RepositoryNode) columns.get(i);
            final TdColumn column = (TdColumn) ((MetadataColumnRepositoryObject) columnNode.getObject()).getTdColumn();
            final TreeItem treeItem = new TreeItem(tree, SWT.NONE, index);

            columnSetMultiValueList.add(index, columnNode);

            String columnName = column.getName();
            treeItem.setImage(ImageLib.getImage(ImageLib.TD_COLUMN));

            treeItem.setText(0, columnName != null ? columnName + PluginConstant.SPACE_STRING + PluginConstant.PARENTHESIS_LEFT
                    + column.getSqlDataType().getName() + PluginConstant.PARENTHESIS_RIGHT : "null"); //$NON-NLS-1$
            treeItem.setData(COLUMN_INDICATOR_KEY, columnNode);

            TreeEditor comboEditor = new TreeEditor(tree);
            tree.setData(DefaultMessagesImpl.getString("AnalysisColumnNominalIntervalTreeViewer.TreeEditor"), comboEditor); //$NON-NLS-1$

            final CCombo combo = new CCombo(tree, SWT.BORDER);
            for (DataminingType type : DataminingType.values()) {
                combo.add(type.getLiteral()); // MODSCA 2008-04-10 use literal
                // for presentation
            }
            DataminingType dataminingType = MetadataHelper.getDataminingType(column);
            if (dataminingType == null) {
                dataminingType = MetadataHelper.getDefaultDataminingType(column.getSqlDataType().getJavaDataType());
            }

            if (dataminingType == null) {
                combo.select(0);
            } else {
                combo.setText(dataminingType.getLiteral());
            }
            MetadataHelper.setDataminingType(DataminingType.get(combo.getText()), column);
            /**
             * DOC zhaoxinyi AnalysisColumnNominalIntervalTreeViewer class global comment. Detailled comment
             */
            class Selection extends SelectionAdapter {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    MetadataHelper.setDataminingType(DataminingType.get(combo.getText()), column);
                    setDirty(true);
                }
            }
            combo.addSelectionListener(new Selection());
            // comboTextList.add(combo.getText().trim());
            combo.setEditable(false);
            comboEditor.minimumWidth = WIDTH1_CELL;
            comboEditor.setEditor(combo, treeItem, 1);

            TreeEditor delLabelEditor = new TreeEditor(tree);
            Label delLabel = createTreeItemLabel(tree, ImageLib.DELETE_ACTION, "AnalysisColumnTreeViewer.delete"); //$NON-NLS-1$
            delLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    deleteColumnItems(columnNode);
                    removeItemBranch(treeItem);
                    enabledButtons(false);
                    // MOD mzhao 2005-05-05 bug 6587.
                    // MOD mzhao 2009-06-8, bug 5887.
                    // updateBindConnection(masterPage, tree);
                }

            });

            delLabelEditor.minimumWidth = delLabel.getImage().getBounds().width;
            delLabelEditor.horizontalAlignment = SWT.CENTER;
            delLabelEditor.setEditor(delLabel, treeItem, 2);
            treeItem.setData(ITEM_EDITOR_KEY, new TreeEditor[] { comboEditor, delLabelEditor });
            /*
             * if (columnIndicator.hasIndicators()) { createIndicatorItems(treeItem,
             * columnIndicator.getIndicatorUnits()); }
             */
            delLabelEditor.layout();
            treeItem.setExpanded(true);
        }

        tree.layout();
        tree.redraw();
        this.setDirty(true);
        tree.forceFocus();
    }

    public void addElements(final List<IRepositoryNode> columnNode, int index) {
        this.addItemElements(columnNode, index);
        updateBindConnection(masterPage, tree);
    }

    /**
     * DOC xzhao Comment method "deleteTreeElements".
     *
     * @param columnIndicators
     * @param deleteColumnIndiciators
     */
    private void deleteColumnItems(RepositoryNode delRepNodeItem) {
        List<RepositoryNode> remainColumns = columnSetMultiValueList;
        for (int j = 0; j < columnSetMultiValueList.size(); j++) {
            // User id identify the equality. the object instance might not equals.
            if (delRepNodeItem.getObject().getId().equals(columnSetMultiValueList.get(j).getObject().getId())) {
                remainColumns.remove(j);
            }
        }
        this.columnSetMultiValueList = remainColumns;
        // setElements(columnSetMultiValueList);
    }

    public List<RepositoryNode> getColumnSetMultiValueList() {
        return this.columnSetMultiValueList;
    }

    /**
     * Remove the selected elements(eg:TdColumn or Indicator) from tree.
     *
     * @param newTree
     */
    private void removeSelectedElements(final Tree newTree) {
        TreeItem[] selection = newTree.getSelection();
        for (TreeItem item : selection) {
            RepositoryNode tdColumnReposNode = (RepositoryNode) item.getData(COLUMN_INDICATOR_KEY);
            deleteColumnItems(tdColumnReposNode);
            removeItemBranch(item);
        }
    }

    private void removeSelectedElements(final TreeItem[] selection) {
        for (TreeItem item : selection) {
            RepositoryNode tdColumnReposNode = (RepositoryNode) item.getData(COLUMN_INDICATOR_KEY);
            deleteColumnItems(tdColumnReposNode);
            removeItemBranch(item);
        }
    }

    private void addTreeListener(final Tree tree) {
        tree.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (e.getSource() instanceof Tree) {
                    Tree currentTree = (Tree) e.getSource();

                    if (currentTree.getSelection().length > 0) {
                        enabledButtons(true);
                    }
                }

            }

            public void focusLost(FocusEvent e) {
                if (e.getSource() instanceof Tree) {
                    Tree currentTree = (Tree) e.getSource();

                    if (currentTree.getSelection().length <= 0) {
                        enabledButtons(false);
                    }
                }
            }

        });

        tree.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean con = false;

                if (e.item instanceof TreeItem) {
                    enabledButtons(true);
                    TreeItem item = (TreeItem) e.item;
                    if (item.getData(INDICATOR_UNIT_KEY) != null) {
                        IndicatorUnit indicatorUnit = (IndicatorUnit) item.getData(INDICATOR_UNIT_KEY);
                        IndicatorEnum type = indicatorUnit.getType();
                        con = IndicatorEnum.RegexpMatchingIndicatorEnum.compareTo(type) == 0
                                || IndicatorEnum.SqlPatternMatchingIndicatorEnum.compareTo(type) == 0;
                    }
                }
                createTreeMenu(tree, con);
                showDetailView(tree);
            }

        });

        tree.addTreeListener(treeAdapter);

    }

    private void showDetailView(Tree newTree) {
        TreeItem[] selection = newTree.getSelection();

        if (selection.length > 0) {
            RespositoryDetailView detailView = CorePlugin.getDefault().getRespositoryDetailView();
            if (detailView == null) {
                return;
            }

            DQRespositoryView dqview = CorePlugin.getDefault().getRepositoryView();
            detailView.selectionChanged(dqview, new StructuredSelection(selection[0].getData(COLUMN_INDICATOR_KEY)));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#getTheSuitedComposite(org.eclipse.swt
     * .events.SelectionEvent)
     */
    @Override
    public ExpandableComposite getTheSuitedComposite(SelectionEvent e) {
        Composite[] previewChartCompsites = masterPage.getPreviewChartCompsites();
        if (previewChartCompsites == null) {
            return null;
        }

        Object obj = e.item.getData(COLUMN_INDICATOR_KEY);
        if (obj instanceof ColumnIndicator) {
            ColumnIndicator columnIndicator = (ColumnIndicator) obj;
            for (Composite comp : previewChartCompsites) {
                if (comp.getData() == columnIndicator) {
                    return (ExpandableComposite) comp;
                }
            }
        }

        return null;
    }

    /**
     * Getter for analysis.
     *
     * @return the analysis
     */
    public Analysis getAnalysis() {
        return this.masterPage.getCurrentModelElement();
    }

    public Tree getTree() {
        return tree;
    }

    @Override
    public void updateModelViewer() {
        masterPage.recomputeIndicators();
        columnSetMultiValueList.clear();
        this.setElements(masterPage.getColumnSetMultiValueIndicator().getAnalyzedColumns());
    }

    @Override
    public boolean canDrop(IRepositoryNode modelElement) {
        // TDQ-3984: column correlation analysis is not available on file connection.
        if (modelElement instanceof DFColumnRepNode) {
            return false;
        }
        // TDQ-3984~
        List<TdColumn> existColumns = new ArrayList<TdColumn>();
        for (RepositoryNode columnFromMultiValueList : this.getColumnSetMultiValueList()) {
            IRepositoryViewObject repObject = columnFromMultiValueList.getObject();
            existColumns.add((TdColumn) ((MetadataColumnRepositoryObject) repObject).getTdColumn());
        }

        if (existColumns.contains(modelElement)) {
            return false;
        }
        // MOD qiongli 2010-8-19,bug 14436:if come from diffrent table,can not drop
        IRepositoryViewObject repObject = modelElement.getObject();
        existColumns.add((TdColumn) ((MetadataColumnRepositoryObject) repObject).getTdColumn());
        if (!existColumns.isEmpty() && !ColumnHelper.isFromSameTable(existColumns)) {
            return false;
        }
        return true;
    }

    @Override
    public void dropModelElements(List<? extends IRepositoryNode> reposObjects, int index) {
        List<IRepositoryNode> columns = new ArrayList<IRepositoryNode>();
        for (IRepositoryNode element : reposObjects) {
            if (element.getObject() instanceof MetadataColumnRepositoryObject) {
                columns.add(element);
            }
        }
        this.addElements(columns, index);
    }

    @Override
    public void addElements(ModelElementIndicator[] elements) {

    }

    @Override
    protected void setElements(ModelElementIndicator[] modelElementIndicator) {

    }

    public static List<DBColumnRepNode> columns2Nodes(List<TdColumn> tdColumns) {
        List<DBColumnRepNode> nodes = new ArrayList<DBColumnRepNode>();
        for (TdColumn tdColumn : tdColumns) {
            RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(tdColumn);
            if (recursiveFind == null) {
                recursiveFind = RepositoryNodeHelper.createRepositoryNode(tdColumn);
            }
            RepositoryNode repNode = recursiveFind;
            if (repNode != null && repNode instanceof DBColumnRepNode) {
                nodes.add((DBColumnRepNode) repNode);
            }
        }
        return nodes;
    }
}
