// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TreeEditor;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ColumnIndicator;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.impl.DelimitedFileIndicatorImpl;
import org.talend.dataprofiler.core.ui.dialog.composite.TooltipTree;
import org.talend.dataprofiler.core.ui.editor.AbstractAnalysisActionHandler;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnSetAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.views.ColumnViewerDND;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.ColumnSetRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sql.TalendTypeConvert;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * 
 * @author yyi 2009-12-16
 */
public class AnalysisColumnSetTreeViewer extends AbstractColumnDropTree {

    protected static Logger log = Logger.getLogger(AnalysisColumnSetTreeViewer.class);

    public static final String VIEWER_KEY = "org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnSetTreeViewer"; //$NON-NLS-1$

    private Composite parentComp;

    private Map<String, Button> buttons = new HashMap<String, Button>();

    private List<IRepositoryNode> columnSetMultiValueList;

    private ColumnSetAnalysisDetailsPage masterPage;

    private ExecutionLanguage language;

    private AnalysisColumnSetTreeViewer setTreeViewer;

    public AnalysisColumnSetTreeViewer(Composite parent, ColumnSetAnalysisDetailsPage masterPage) {
        absMasterPage = masterPage;
        viewKey = VIEWER_KEY;
        parentComp = parent;
        tree = createTree(parent);
        tree.setData(this);
        columnSetMultiValueList = new ArrayList<IRepositoryNode>();
        this.masterPage = masterPage;
        this.createButtonSection(parent.getParent());
        this.setElements(masterPage.getCurrentModelElementIndicators());
        this.setDirty(false);

        // MOD yyi 2011-02-25 18868
        AbstractColumnDropTree treeViewer = null == masterPage.getTreeViewer() ? this : masterPage.getTreeViewer();
        if (treeViewer != null && treeViewer instanceof AnalysisColumnSetTreeViewer) {
            setTreeViewer = (AnalysisColumnSetTreeViewer) treeViewer;
        }
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
        final Tree newTree = new TooltipTree(parent, SWT.MULTI | SWT.BORDER) {

            @Override
            protected String getItemTooltipText(TreeItem item) {
                ModelElementIndicator meIndicator = (ModelElementIndicator) item.getData(MODELELEMENT_INDICATOR_KEY);
                IRepositoryNode parentNodeForColumnNode = RepositoryNodeHelper.getParentNodeForColumnNode(meIndicator
                        .getModelElementRepositoryNode());
                return DefaultMessagesImpl.getString(
                        "AnalysisColumnTreeViewer.columnParent", parentNodeForColumnNode.getObject().getLabel()); //$NON-NLS-1$ //;
            }
        };
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(newTree);

        newTree.setHeaderVisible(true);
        createTreeItem(newTree, 300, "AnalysisColumnTreeViewer.analyzedColumns"); //$NON-NLS-1$
        createTreeDataminingItem(newTree);
        createTreeItem(newTree, 80, "AnalysisColumnTreeViewer.pattern"); //$NON-NLS-1$
        createTreeItem(newTree, 80, "AnalysisColumnTreeViewer.operation"); //$NON-NLS-1$

        parent.layout();

        AbstractAnalysisActionHandler actionHandler = new AbstractAnalysisActionHandler(parent) {

            @Override
            protected void handleRemove() {
                removeSelectedElements(newTree);
                // MOD mzhao 2005-05-05 bug 6587.
                // MOD mzhao 2009-06-8, bug 5887.
                updateBindConnection(masterPage, tree);
            }

        };

        parent.setData(AbstractMetadataFormPage.ACTION_HANDLER, actionHandler);

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
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedElements();
                notifyObservers();
            }
        });
        final Button moveUpButton = new Button(buttonsComp, SWT.NULL);
        moveUpButton.setText(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.moveUp")); //$NON-NLS-1$
        moveUpButton.setLayoutData(buttonGridData);
        moveUpButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveElement(setTreeViewer, -1);
                notifyObservers();
            }

        });
        final Button moveDownButton = new Button(buttonsComp, SWT.NULL);
        moveDownButton.setText(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.moveDown")); //$NON-NLS-1$
        moveDownButton.setLayoutData(buttonGridData);
        moveDownButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveElement(setTreeViewer, 1);
                notifyObservers();
            }

        });

        buttons.put("delButton", delButton);//$NON-NLS-1$
        buttons.put("moveUpButton", moveUpButton);//$NON-NLS-1$
        buttons.put("moveDownButton", moveDownButton);//$NON-NLS-1$
        enabledButtons(false);

    }

    /**
     * 
     * DOC zshen Comment method "enabledButtons".
     * 
     * @param enabled the state of buttons .
     * 
     * change the state of buttons.
     */
    public void enabledButtons(boolean enabled) {

        boolean moveEnabled = enabled;
        TreeItem[] selectItems = getTree().getSelection();
        if (!isSelectedColumn(selectItems)) {
            moveEnabled = false;
        }
        buttons.get("delButton").setEnabled(enabled);//$NON-NLS-1$
        buttons.get("moveUpButton").setEnabled(moveEnabled);//$NON-NLS-1$
        buttons.get("moveDownButton").setEnabled(moveEnabled);//$NON-NLS-1$
    }

    /**
     * 
     * DOC zshen Comment method "moveElement".
     * 
     * @param columnsElementViewer
     * @param step
     * 
     * move the element of the columnList to up or down.
     */
    private void moveElement(AnalysisColumnSetTreeViewer columnsElementViewer, int step) {
        Tree currentTree = columnsElementViewer.getTree();
        Object[] selectItem = currentTree.getSelection();
        List<IRepositoryNode> columnList = columnsElementViewer.getColumnSetMultiValueList();

        // get all the selected index list first which is used to check how to move
        List<Object> allSelectedItemList = new ArrayList<Object>();
        for (Object element : selectItem) {
            allSelectedItemList.add(element);
        }

        int index = 0;
        RepositoryNode moveElement = null;
        List<Integer> indexArray = new ArrayList<Integer>();
        for (Object element : selectItem) {
            index = currentTree.indexOf((TreeItem) element);

            int changeIndex = index + step;

            if (changeIndex > -1 && changeIndex < columnList.size()) {
                Object treeElement = ((TreeItem) element).getData(AnalysisColumnNominalIntervalTreeViewer.COLUMN_INDICATOR_KEY);
                // MOD by zshen for bug 15750 TODO 39 columnset analysis move up/down one column will get exception.
                if (treeElement instanceof ModelElement) {
                    moveElement = RepositoryNodeHelper.recursiveFind((ModelElement) treeElement);
                } else if (treeElement instanceof RepositoryNode) {
                    moveElement = (RepositoryNode) treeElement;
                }
                columnList.remove(moveElement);
                // when the changed one is the selected one too, get next one
                while (allSelectedItemList.contains(currentTree.getItem(changeIndex))) {
                    ++changeIndex;
                }
                columnList.add(changeIndex, moveElement);
                indexArray.add(changeIndex);

                ModelElementIndicator tmpElement = modelElementIndicators[changeIndex];
                modelElementIndicators[changeIndex] = modelElementIndicators[index];
                modelElementIndicators[index] = tmpElement;

                allSelectedItemList.remove(element);

            } else {
                return;
            }
        }

        // columnsElementViewer.setInput(columnList.toArray());
        setElements(modelElementIndicators);
        currentTree = columnsElementViewer.getTree();
        for (int i = 0; i < indexArray.size(); i++) {
            currentTree.select(currentTree.getItem(indexArray.get(i)));
        }
    }

    /**
     * DOC bZhou Comment method "addTargetDND".
     * 
     * @param newTree
     */
    private void addTargetDND(final Tree newTree) {
        ColumnViewerDND.installDND(newTree);
    }

    @Override
    public void setInput(Object[] objs) {
        if (objs != null && objs.length != 0) {
            List<DBColumnRepNode> columnList = new ArrayList<DBColumnRepNode>();
            for (Object obj : objs) {
                // TDQ-11926: create column set analysis by right click "Analyses" in wizard select db table node,
                // default is should be sql engine.
                if (obj instanceof DBTableRepNode || obj instanceof DBViewRepNode) {
                    columnList.addAll((Collection<? extends DBColumnRepNode>) ((ColumnSetRepNode) obj).getAllColumns());
                } else if (obj instanceof DBColumnRepNode) {
                    columnList.add((DBColumnRepNode) obj);
                }
            }
            // MOD yyi 2010-05-13 12828
            Collections.reverse(columnList);
            // MOD qiongli 2011-3-15 set DataFilterText disabled except TdColumn.
            if (masterPage.getDataFilterComp() != null) {
                masterPage.getDataFilterComp().getDataFilterText().setEnabled(!columnList.isEmpty());
                if (columnList.isEmpty()) {// when the selected column is not DB type,will disable the execute engine
                                           // combobox.
                    masterPage.changeExecuteLanguageToJava(true);
                } else {// when the selected column is back to DB type, should enable the execute engine combobox again.
                    masterPage.enableExecuteLanguage();
                }
            }

            ModelElementIndicator[] newSelects = translateSelectedNodeIntoIndicator(objs);
            List<ModelElementIndicator> indicatorList = new ArrayList<ModelElementIndicator>();
            if (newSelects != null) {
                // do not replace the original one(which may contains some indicator units), only add the new column
                // indicator;
                for (ModelElementIndicator column : newSelects) {
                    // if the modelElementIndicators contains selected column, add the column in modelElementIndicators
                    // to ColumnIndicatorList
                    boolean isOld = false;
                    for (ModelElementIndicator oldColumn : modelElementIndicators) {
                        if (oldColumn.getModelElementRepositoryNode().equals(column.getModelElementRepositoryNode())) {
                            indicatorList.add(oldColumn);
                            isOld = true;
                            break;
                        }
                    }
                    // else add this column in filterInputData to ColumnIndicatorList
                    if (!isOld) {
                        indicatorList.add(column);
                    }
                }
                this.modelElementIndicators = indicatorList.toArray(new ModelElementIndicator[indicatorList.size()]);
                this.setElements(modelElementIndicators);
            }
        } else {
            TreeItem[] items = this.tree.getItems();
            for (TreeItem item : items) {
                this.removeItemBranch(item);
            }
            super.setInput(objs);
        }
    }

    @Override
    public void setElements(ModelElementIndicator[] elements) {
        this.tree.dispose();
        this.tree = createTree(this.parentComp);
        tree.setData(this);
        // MOD mzhao bug 8282 2009-7-31 Clear column cache.
        columnSetMultiValueList.clear();
        this.modelElementIndicators = elements;
        addItemElements(elements);
        // MOD mzhao 2009-05-05 bug 6587.
        updateBindConnection(masterPage, tree);
    }

    private void addItemElements(final ModelElementIndicator[] elements) {
        // MOD qiongli 2011-1-27,change TdColumn to MetadataColumn for supporting delimited file.
        for (ModelElementIndicator element2 : elements) {
            final ModelElementIndicator meIndicator = element2;

            final ModelElement modelElement = RepositoryNodeHelper
                    .getSubModelElement(meIndicator.getModelElementRepositoryNode());
            final TreeItem treeItem = new TreeItem(tree, SWT.NONE);

            MetadataHelper.setDefaultDataminingType(modelElement);
            columnSetMultiValueList.add(meIndicator.getModelElementRepositoryNode());
            treeItem.setImage(ImageLib.getImage(ImageLib.TD_COLUMN));

            treeItem.setText(0, getModelElemetnDisplayName(meIndicator));
            treeItem.setData(COLUMN_INDICATOR_KEY, modelElement);
            treeItem.setData(MODELELEMENT_INDICATOR_KEY, meIndicator);

            // MOD mzhao feature 13040 , 2010-05-21

            TreeEditor comboEditor = new TreeEditor(tree);
            final CCombo combo = new CCombo(tree, SWT.BORDER);
            for (DataminingType type : DataminingType.values()) {
                combo.add(type.getLiteral()); // MODSCA 2008-04-10 use literal
                // for presentation
            }
            DataminingType dataminingType = MetadataHelper.getDataminingType(modelElement);

            if (dataminingType == null) {
                combo.select(0);
            } else {
                combo.setText(dataminingType.getLiteral());
            }
            combo.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    MetadataHelper.setDataminingType(DataminingType.get(combo.getText()), modelElement);
                    setDirty(true);
                }

            });
            combo.setEditable(false);

            comboEditor.minimumWidth = WIDTH1_CELL;
            comboEditor.setEditor(combo, treeItem, 1);

            TreeEditor addPatternEditor = new TreeEditor(tree);
            Label addPatternLabl = createTreeItemLabel(tree, ImageLib.ADD_PATTERN, "AnalysisColumnTreeViewer.addPattern"); //$NON-NLS-1$ 

            PatternMouseAdapter mouseAdapter = new PatternMouseAdapter(this, masterPage, meIndicator, treeItem);
            mouseAdapter.addFilter(new ViewerFilter() {

                @Override
                public boolean select(Viewer viewer, Object parentElement, Object element) {
                    if (element instanceof IFolder) {
                        IFolder folder = (IFolder) element;
                        return !folder.getName().endsWith("SQL");//$NON-NLS-1$
                    }
                    return true;
                }
            });
            addPatternLabl.addMouseListener(mouseAdapter);
            addPatternEditor.minimumWidth = addPatternLabl.getImage().getBounds().width;
            addPatternEditor.setEditor(addPatternLabl, treeItem, 2);

            TreeEditor delLabelEditor = new TreeEditor(tree);
            Label delLabel = createTreeItemLabel(tree, ImageLib.DELETE_ACTION, "AnalysisColumnTreeViewer.delete"); //$NON-NLS-1$
            delLabel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    deleteColumnItems(meIndicator.getModelElementRepositoryNode());
                    if (treeItem.getParentItem() != null && treeItem.getParentItem().getData(INDICATOR_UNIT_KEY) != null) {
                        setElements(modelElementIndicators);
                    } else {
                        deleteIndicatorItems(meIndicator);
                        removeItemBranch(treeItem);
                        enabledButtons(false);
                    }
                    notifyObservers();
                }

            });

            delLabelEditor.minimumWidth = delLabel.getImage().getBounds().width;
            delLabelEditor.horizontalAlignment = SWT.CENTER;
            delLabelEditor.setEditor(delLabel, treeItem, 3);
            treeItem.setData(ITEM_EDITOR_KEY, new TreeEditor[] { comboEditor, addPatternEditor, delLabelEditor });
            if (meIndicator.hasIndicators()) {
                createIndicatorItems(treeItem, meIndicator.getIndicatorUnits());
            }
            delLabelEditor.layout();
            treeItem.setExpanded(false);
        }

        tree.layout();
        tree.redraw();
        this.setDirty(true);
        tree.forceFocus();
    }

    private boolean isSelectedColumn(TreeItem[] items) {
        for (TreeItem item : items) {
            if (item.getData(AbstractColumnDropTree.INDICATOR_UNIT_KEY) != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void addElements(final ModelElementIndicator[] elements) {
        RepositoryNode[] columns = new RepositoryNode[elements.length];
        for (int i = 0; i < elements.length; i++) {
            columns[i] = (RepositoryNode) elements[i].getModelElementRepositoryNode();
        }
        List<IRepositoryNode> oriColumns = getColumnSetMultiValueList();
        for (RepositoryNode column : columns) {
            if (!RepositoryNodeHelper.containsModelElementNode(oriColumns, column)) {
                oriColumns.add(column);
            }
        }
        setInput(oriColumns.toArray());
        // MOD qiongli 2010-6-4,bug 0012766,after drag and drop a column from left view,update the connection state
        updateBindConnection(masterPage, tree);

        // ADD msjian TDQ-11606: refresh the datapreview part after add a new element
        ModelElementIndicator[] newsArray = new ModelElementIndicator[getAllTheElementIndicator().length + elements.length];
        System.arraycopy(getAllTheElementIndicator(), 0, newsArray, 0, getAllTheElementIndicator().length);
        for (int i = 0; i < elements.length; i++) {
            newsArray[getAllTheElementIndicator().length + i] = elements[i];
        }
        masterPage.refreshPreviewTable(newsArray, true);
        // ADD msjian TDQ-11606~
    }

    /**
     * DOC xzhao Comment method "deleteTreeElements".
     * 
     * @param columnIndicators
     * @param deleteColumnIndiciators
     */
    private void deleteColumnItems(IRepositoryNode deleteColumn) {
        List<IRepositoryNode> remainColumns = columnSetMultiValueList;
        for (int j = 0; j < columnSetMultiValueList.size(); j++) {
            RepositoryNode column = (RepositoryNode) columnSetMultiValueList.get(j);
            if (deleteColumn.getObject().getId() == column.getObject().getId()) {
                remainColumns.remove(j);
            }
        }
        this.columnSetMultiValueList = remainColumns;
    }

    public List<IRepositoryNode> getColumnSetMultiValueList() {
        return this.columnSetMultiValueList;
    }

    /**
     * Remove the selected elements(eg:TdColumn or Indicator) from tree.
     * 
     * @param newTree
     */
    private void removeSelectedElements(final Tree newTree) {
        removeSelectedElements();
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
                if (e.item instanceof TreeItem) {
                    enabledButtons(true);
                    AnalysisColumnSetMenuProvider analysisColumnSetMenuProvider = new AnalysisColumnSetMenuProvider(tree);
                    analysisColumnSetMenuProvider.createTreeMenu(Boolean.TRUE);
                    analysisColumnSetMenuProvider.showDetailView(tree);
                }
            }
        });

        tree.addTreeListener(treeAdapter);
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
        return this.masterPage.getAnalysisHandler().getAnalysis();
    }

    /**
     * Getter for analysis.
     * 
     * @return the analysis
     */
    public IRepositoryNode getAnalysisNode() {
        return this.masterPage.getCurrentRepNode();
    }

    public Tree getTree() {
        return tree;
    }

    @Override
    public void updateModelViewer() {
        // TDQ-11927 msjian: fix for column set analysis, when the connection is changed, refresh the data preview part
        masterPage.recomputeIndicators();
        columnSetMultiValueList.clear();
        modelElementIndicators = masterPage.getCurrentModelElementIndicators();
        masterPage.refreshPreviewTable(modelElementIndicators, true);
        masterPage.refreshTheTree(modelElementIndicators);
    }

    @Override
    public boolean canDrop(IRepositoryNode reposNode) {
        // MOD klliu bug 19991 note 0077019 2011-03-29
        Set<EObject> nodeTypeName = new HashSet<EObject>();
        for (IRepositoryNode rd : columnSetMultiValueList) {
            ModelElement modelElementFromRepositoryNode = RepositoryNodeHelper.getModelElementFromRepositoryNode(rd);
            EObject eContainer = modelElementFromRepositoryNode.eContainer();
            nodeTypeName.add(eContainer);
        }
        ModelElement modelElement = RepositoryNodeHelper.getModelElementFromRepositoryNode(reposNode);
        nodeTypeName.add(modelElement.eContainer());
        if (nodeTypeName.size() > 1) {
            return false;
        }
        // ~
        List<IRepositoryNode> existColumns = new ArrayList<IRepositoryNode>();
        for (IRepositoryNode columnFromMultiValueList : this.getColumnSetMultiValueList()) {
            existColumns.add(columnFromMultiValueList);
        }
        if (existColumns.contains(reposNode)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#openIndicatorOptionDialog(org.eclipse
     * .swt.widgets.Shell, org.eclipse.swt.widgets.TreeItem)
     */
    @Override
    public boolean openIndicatorOptionDialog(Shell shell, TreeItem indicatorItem) {
        // MOD msjian TDQ-8551 2014-4-15: columnset analysis can not set options for pattern types
        openNoIndicatorOptionsMessageDialog(shell);
        // TDQ-8551~
        return false;
    }

    /**
     * 
     * DOC qiongli Comment method "getModelElemetnDisplayName".
     * 
     * @param meIndicator
     * @return
     */
    private String getModelElemetnDisplayName(ModelElementIndicator meIndicator) {
        if (meIndicator.getModelElementRepositoryNode() == null) {
            return "null"; //$NON-NLS-1$
        }
        String meName = meIndicator.getElementName();
        String typeName = "";//$NON-NLS-1$
        if (meIndicator instanceof ColumnIndicator) {
            // MOD scorreia 2010-10-20 bug 16403 avoid NPE here
            TdSqlDataType sqlDataType = ((ColumnIndicator) meIndicator).getTdColumn().getSqlDataType();
            typeName = sqlDataType != null ? sqlDataType.getName() : "unknown";//$NON-NLS-1$
        } else if (meIndicator instanceof DelimitedFileIndicatorImpl) {
            MetadataColumn mColumn = ((DelimitedFileIndicatorImpl) meIndicator).getMetadataColumn();
            typeName = TalendTypeConvert.convertToJavaType(mColumn.getTalendType());
        }
        return meName != null ? meName + PluginConstant.SPACE_STRING + PluginConstant.PARENTHESIS_LEFT + typeName
                + PluginConstant.PARENTHESIS_RIGHT : "null";//$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#removeItemBranch(org.eclipse.swt.widgets
     * .TreeItem)
     */
    @Override
    protected void removeItemBranch(TreeItem item) {
        // MOD yyi 2011-03-22 19460: The "remove elements" option on the contextual menu does not work
        IndicatorUnit unit = (IndicatorUnit) item.getData(INDICATOR_UNIT_KEY);
        ModelElementIndicator meIndicator = (ModelElementIndicator) item.getData(MODELELEMENT_INDICATOR_KEY);
        // MOD klliu check the item is column or pattern
        if (item.getData(COLUMN_INDICATOR_KEY) != null) {
            deleteColumnItems(meIndicator.getModelElementRepositoryNode());
            deleteModelElementItems(meIndicator);
            masterPage.setCurrentModelElementIndicators(modelElementIndicators);
        }
        if (null != unit) {
            meIndicator.removeIndicatorUnit(unit);
        }
        super.removeItemBranch(item);

        // add msjian TDQ-8860 2014-4-30:check whether show allmatchindicator in the indicators section.
        EventManager.getInstance().publish(this.getAnalysis(), EventEnum.DQ_COLUMNSET_SHOW_MATCH_INDICATORS, null);
        // TDQ-8860~
        // MOD mzhao 2009-06-8, bug 5887.
        updateBindConnection(masterPage, tree);

        enabledButtons(false);
        tree.setFocus();
    }

    public void removeItemByUnit(IndicatorUnit unit) {
        for (TreeItem item : this.tree.getItems()) {
            if (unit.getIndicatorName().equals(item.getText())) {
                this.removeItemBranch(item);
                break;
            }
        }
    }

    /**
     * DOC yyi AnalysisColumnSetTreeViewer class global comment. Detailled comment
     */
    class AnalysisColumnSetMenuProvider extends ModelElementTreeMenuProvider {

        /**
         * DOC yyi AnalysisColumnSetMenuProvider constructor comment.
         * 
         * @param tree
         */
        public AnalysisColumnSetMenuProvider(Tree tree) {
            super(tree);
        }

        @Override
        protected void removeSelectedElements2(Tree theTree) {
            removeSelectedElements(theTree);
            notifyObservers();
        }

        @Override
        protected IRepositoryNode getAnalysis2() {
            return getAnalysisNode();
        }

    }

    public ExecutionLanguage getLanguage() {
        return this.language;
    }

    public void setLanguage(ExecutionLanguage language) {
        this.language = language;
    }

    /**
     * DOC yyi 2011-03-14 19460:remove selected element form the tree
     */
    private void removeSelectedElements() {
        TreeItem[] selection = tree.getSelection();
        for (TreeItem treeItem : selection) {
            removeItemBranch(treeItem);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.grid.utils.TDQObserver#update(java.lang.Object)
     */
    @Override
    public void update(Map<String, Integer> columnIndexMap) {
        ModelElementIndicator[] reorderModelElement = reorderModelElement(masterPage.getCurrentModelElementIndicators(),
                columnIndexMap);
        masterPage.refreshTheTree(reorderModelElement);
        masterPage.setDirty(true);
    }

    @Override
    protected ModelElementIndicator[] getAllTheElementIndicator() {
        return masterPage.getCurrentModelElementIndicators();
    }

}
