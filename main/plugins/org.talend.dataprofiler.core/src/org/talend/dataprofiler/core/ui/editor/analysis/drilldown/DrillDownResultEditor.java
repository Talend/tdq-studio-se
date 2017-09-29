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
package org.talend.dataprofiler.core.ui.editor.analysis.drilldown;

import java.text.Collator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.pagination.IPageLoader;
import org.eclipse.nebula.widgets.pagination.PageChangedAdapter;
import org.eclipse.nebula.widgets.pagination.PageLoaderStrategyHelper;
import org.eclipse.nebula.widgets.pagination.PageableController;
import org.eclipse.nebula.widgets.pagination.collections.PageResult;
import org.eclipse.nebula.widgets.pagination.collections.PageResultContentProvider;
import org.eclipse.nebula.widgets.pagination.renderers.navigation.ResultAndNavigationPageGraphicsRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.talend.cwm.indicator.ColumnFilter;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.common.ui.pagination.pageloder.MapDBPageConstant;
import org.talend.dataprofiler.common.ui.pagination.pageloder.MapDBPageLoader;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.MapDBManager;
import org.talend.dataquality.indicators.mapdb.MapDBUtils;
import org.talend.dataquality.indicators.validation.IDataValidationFactory;
import org.talend.dq.helper.SqlExplorerUtils;

/**
 * 
 * DOC zshen class global comment. Detailled comment
 * 
 * Display result for drill down operation
 */
public class DrillDownResultEditor extends EditorPart {

    private static Logger log = Logger.getLogger(DrillDownResultEditor.class);

    private TableViewer tableView;

    private Action exportAction;

    private Hashtable<String, Action> actionList;

    public static final String[] ACTION_NAME = { "export" };//$NON-NLS-1$

    public static final String NULL_VALUE_DISPLAY_TEXT = "(null)"; //$NON-NLS-1$

    public DrillDownResultEditor() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doSaveAs() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        this.setSite(site);
        this.setInput(input);
        DrillDownEditorInput ddEditorInput = (DrillDownEditorInput) input;
        this.setPartName(ddEditorInput.getMenuType());
        this.setTitleToolTip(ddEditorInput.getToolTipText());

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));
        // layout.marginTop = 10;
        parent.setLayout(layout);
        // createPopupMenu();
        exportAction = SqlExplorerUtils.getDefault().createExportCSVAction();
        createCoolBar(parent);
        tableView = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
        Table table = tableView.getTable();
        SqlExplorerUtils.getDefault().setExportCSVActionTable(exportAction, table);
        MenuManager popupMenu = new MenuManager();
        // IAction newRowAction = new NewRowAction();
        exportAction.setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.EXPORT_WIZARD));
        popupMenu.add(exportAction);
        Menu menu = popupMenu.createContextMenu(table);
        table.setMenu(menu);

        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(table);
        if (this.getEditorInput() instanceof DrillDownEditorInput) {
            DrillDownEditorInput ddEditorInput = (DrillDownEditorInput) this.getEditorInput();
            if (ddEditorInput.getCurrIndicator().isUsedMapDBMode()) {
                initTableViewerForMapDB(parent, table, ddEditorInput);
            } else {
                initTableViewerForJava(table, ddEditorInput);
            }
            // 3) Create Table columns with sort of paginated list.

            for (TableColumn packColumn : table.getColumns()) {
                packColumn.pack();
            }

        }

    }

    /**
     * DOC talend Comment method "initTableViewerForJava".
     * 
     * @param table
     * @param ddEditorInput
     */
    private void initTableViewerForJava(Table table, DrillDownEditorInput ddEditorInput) {
        addTableColumn(ddEditorInput, table);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setData(ddEditorInput.getDataSet());
        tableView.setLabelProvider(new DrillDownResultLabelProvider());
        tableView.setContentProvider(new DrillDownResultContentProvider());
        tableView.setInput(this.getEditorInput());
    }

    /**
     * DOC talend Comment method "initTableViewerForMapDB".
     * 
     * @param parent
     * @param table
     * @param ddEditorInput
     */
    @SuppressWarnings("unchecked")
    private void initTableViewerForMapDB(final Composite parent, final Table table, DrillDownEditorInput ddEditorInput) {
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        tableView.setLabelProvider(new DrillDownResultLabelProvider());
        tableView.setContentProvider(new DrillDownResultContentProvider());
        // set page size
        final PageableController controller = new PageableController(MapDBPageConstant.NUMBER_PER_PAGE);
        Object dataSetForMapDB = ddEditorInput.getDataSetForMapDB(controller.getPageSize());
        if (dataSetForMapDB == null) {
            log.error(DefaultMessagesImpl.getString("DrillDownResultEditor.drillDownError"), new RuntimeException(
                    DefaultMessagesImpl.getString("DrillDownResultEditor.drillDownErrorMessage")));
        } else {
            table.setData(dataSetForMapDB);
        }
        // for columnSet analysis here only have one db file need to support drill down and data section
        Analysis analysis = ddEditorInput.getAnalysis();
        AnalysisType analysisType = analysis.getParameters().getAnalysisType();
        IPageLoader<PageResult<Object[]>> pageLoader = null;
        AbstractDB<Object> mapDB = ddEditorInput.getMapDB();
        Indicator generateMapDBIndicator = ddEditorInput.getGenerateMapDBIndicator();
        MapDBManager.getInstance().addDBRef(MapDBUtils.getMapDBFile(generateMapDBIndicator));
        Long itemsSize = ddEditorInput.getItemSize(mapDB);
        if (AnalysisType.COLUMN_SET == analysisType) {
            pageLoader = new MapDBPageLoader<Object>(mapDB, IDataValidationFactory.INSTANCE.createValidation(ddEditorInput
                    .getCurrIndicator()), itemsSize);
        } else {
            // ~
            ColumnFilter filter = ddEditorInput.getColumnFilter();
            pageLoader = new MapDBPageLoader<Object>(mapDB, null, itemsSize, filter);
        }
        controller.addPageChangedListener(PageLoaderStrategyHelper.createLoadPageAndReplaceItemsListener(controller, tableView,
                pageLoader, PageResultContentProvider.getInstance(), null));
        controller.addPageChangedListener(new PageChangedAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.nebula.widgets.pagination.PageChangedAdapter#pageIndexChanged(int, int,
             * org.eclipse.nebula.widgets.pagination.PageableController)
             */
            @Override
            public void pageIndexChanged(int oldPageIndex, int newPageIndex, PageableController controller) {
                Object data = table.getData();
                if (data != null && SqlExplorerUtils.getDefault().isInstanceofTalendDataSet(data)) {
                    long totalSize = controller.getTotalElements();
                    long pageSize = controller.getPageSize();
                    long pageIndex = controller.getPageOffset();

                    long fromIndex = pageIndex;
                    long toIndex = pageIndex + pageSize;
                    if (toIndex > totalSize) {
                        toIndex = totalSize;
                    }

                    SqlExplorerUtils.getDefault().resetTalendDataSetIndex(data, fromIndex, toIndex);
                    parent.layout();
                }
            }

        });

        // Create navigation page links
        ResultAndNavigationPageGraphicsRenderer resultAndNavigationPageGraphicsRenderer = new ResultAndNavigationPageGraphicsRenderer(
                parent, SWT.NONE, controller);
        resultAndNavigationPageGraphicsRenderer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createColumns(tableView, controller, ((DrillDownEditorInput) this.getEditorInput()));
        // Set current page to 0 to refresh the table
        controller.setCurrentPage(0);
    }

    /**
     * DOC talend Comment method "createColumns".
     * 
     * @param tableView2
     * @param controller
     * @param currIndicator
     */
    private void createColumns(TableViewer tableView2, PageableController controller, DrillDownEditorInput ddEditorInput) {
        List<String> tableColumnNames = ddEditorInput.filterAdaptColumnHeader();
        for (String tableColumnName : tableColumnNames) {
            // First column is for the first name
            TableViewerColumn col = createTableViewerColumn(tableView2, tableColumnName);
            TableSectionViewerProvider provider = new TableSectionViewerProvider();
            col.setLabelProvider(provider);
            // TDQ-11356 msjian 2015-12-4: make the sort work well in the drilldown editor
            addSorter(tableView2.getTable(), col.getColumn());
            // TDQ-11356~
        }

    }

    /**
     * DOC zshen ColumnSetResultPage class global comment. Detailled comment
     */
    class TableSectionViewerProvider extends CellLabelProvider implements IStructuredContentProvider, ITableLabelProvider,
            ITableColorProvider {

        @SuppressWarnings("unchecked")
        public Object[] getElements(Object inputElement) {
            List<Object> columnDataSet = (List<Object>) inputElement;
            return columnDataSet.toArray();
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // nothing need to do at here
        }

        public Image getImage(Object element) {
            if (element instanceof TdColumn) {
                return ImageLib.getImage(ImageLib.TD_COLUMN);
            }
            return null;
        }

        @Override
        public void dispose() {
            // TODO Auto-generated method stub

        }

        public Image getColumnImage(Object element, int columnIndex) {
            // TODO Auto-generated method stub
            return null;
        }

        @SuppressWarnings("unchecked")
        public String getColumnText(Object element, int columnIndex) {
            if (List.class.isInstance(element)) {
                List<Object> listElement = (List<Object>) element;
                int listSize = listElement.size();
                String[] keyArray = new String[listSize];
                for (int index = 0; index < listElement.size(); index++) {
                    Object tempData = listElement.get(index);
                    keyArray[index] = tempData == null ? StringUtils.EMPTY : tempData.toString();
                }
                if (columnIndex < keyArray.length) {
                    if (keyArray[columnIndex] == null) {
                        return NULL_VALUE_DISPLAY_TEXT;
                    }
                    return keyArray[columnIndex];
                }
            } else {
                for (int i = 0; i < ((Object[]) element).length; i++) {
                    if (columnIndex == i) {
                        return String.valueOf(((Object[]) element)[i]);
                    }
                }
            }
            return null;
        }

        @Override
        public void addListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isLabelProperty(Object element, String property) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void removeListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        public Color getBackground(Object element, int columnIndex) {

            return null;
        }

        public Color getForeground(Object element, int columnIndex) {
            // MOD by zshen for feature 14000
            return null;
            // ~14000
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
         */
        @Override
        public void update(ViewerCell cell) {
            Object element = cell.getElement();
            int index = cell.getColumnIndex();
            cell.setText(getColumnText(element, index));
        }
    }

    /**
     * DOC talend Comment method "createTableViewerColumn".
     * 
     * @param tableView2
     * @param tableColumnName
     * @return
     */
    private TableViewerColumn createTableViewerColumn(TableViewer tableView2, String tableColumnName) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(tableView2, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(tableColumnName);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    private void createCoolBar(Composite parent) {
        CoolBar coolBar = new CoolBar(parent, SWT.HORIZONTAL | SWT.HORIZONTAL);
        GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(coolBar);
        actionList = new Hashtable<String, Action>();

        actionList.put(ACTION_NAME[0], exportAction);
        ToolBar toolBar = new ToolBar(coolBar, SWT.RIGHT | SWT.FLAT);
        toolBar.pack();

        for (String element : ACTION_NAME) {
            ToolItem item = new ToolItem(toolBar, SWT.PUSH);
            item.setImage(actionList.get(element).getImageDescriptor().createImage());

            item.setToolTipText(actionList.get(element).getText());
            item.addSelectionListener(new ListenToTheAction(actionList.get(element)));

        }

        CoolItem coolEdit = new CoolItem(coolBar, SWT.HORIZONTAL | SWT.DROP_DOWN);
        coolEdit.setControl(toolBar);

        CoolItem[] coolItems = coolBar.getItems();
        for (CoolItem coolItem : coolItems) {
            Control control = coolItem.getControl();
            Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            Point coolSize = coolItem.computeSize(size.x, size.y);
            if (control instanceof ToolBar) {
                ToolBar bar = (ToolBar) control;
                if (bar.getItemCount() > 0) {

                    size.x = bar.getItem(0).getWidth();

                }
            }
            coolItem.setMinimumSize(size);
            coolItem.setPreferredSize(coolSize);
            coolItem.setSize(coolSize);
        }
        Point p = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        toolBar.setSize(p);
        Point p2 = coolEdit.computeSize(p.x, p.y);
        coolEdit.setControl(toolBar);
        coolEdit.setSize(p2);

        coolBar.setLocked(true);
    }

    private void addTableColumn(DrillDownEditorInput ddEditorInput, Table table) {

        List<String> columnElementList = ddEditorInput.filterAdaptColumnHeader();
        for (String columnElement : columnElementList) {
            TableColumn column = new TableColumn(table, SWT.CENTER);
            column.setText(columnElement);
            addSorter(table, column);
        }
    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

    private void addSorter(final Table table, final TableColumn column) {
        column.addListener(SWT.Selection, new Listener() {

            boolean isAscend = true;

            Collator comparator = Collator.getInstance(Locale.getDefault());

            public void handleEvent(Event event) {
                int columnIndex = getColumnIndex(table, column);
                TableItem[] items = table.getItems();
                int length = "...".equals(items[items.length - 1].getText(columnIndex)) ? items.length - 1 : items.length;//$NON-NLS-1$
                for (int i = 1; i < length; i++) {
                    String value2 = items[i].getText(columnIndex);
                    for (int j = 0; j < i; j++) {
                        String value1 = items[j].getText(columnIndex);
                        boolean isLessThan = true;

                        if (StringUtils.isNumeric(value2) && StringUtils.isNotEmpty(value2) && StringUtils.isNumeric(value1)
                                && StringUtils.isNotEmpty(value1)) {
                            isLessThan = Long.valueOf(value2).compareTo(Long.valueOf(value1)) < 0;
                        } else {
                            isLessThan = comparator.compare(value2, value1) < 0;
                        }
                        if ((isAscend && isLessThan) || (!isAscend && !isLessThan)) {
                            String[] values = getTableItemText(table, items[i]);
                            Object obj = items[i].getData();
                            items[i].dispose();
                            TableItem item = new TableItem(table, SWT.NONE, j);
                            item.setText(values);
                            item.setData(obj);
                            items = table.getItems();
                            break;
                        }
                    }
                }

                table.setSortColumn(column);
                table.setSortDirection((isAscend ? SWT.UP : SWT.DOWN));
                isAscend = !isAscend;

            }
        });
    }

    private int getColumnIndex(Table table, TableColumn column) {
        TableColumn[] columns = table.getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equals(column)) {
                return i;
            }
        }
        return -1;
    }

    private String[] getTableItemText(Table table, TableItem item) {
        int count = table.getColumnCount();
        String[] strs = new String[count];
        for (int i = 0; i < count; i++) {
            strs[i] = item.getText(i);
        }
        return strs;
    }

    /**
     * 
     * DOC zshen DrillDownResultEditor class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     * 
     */
    private class DrillDownResultLabelProvider implements ITableLabelProvider {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {

            // MOD qiongli 2011-3-31,bug 20033,avoid ArrayIndexOutOfBoundsException
            if (columnIndex >= ((Object[]) element).length) {
                return PluginConstant.EMPTY_STRING;
            }
            Object object = ((Object[]) element)[columnIndex];
            if (object != null) {
                return object.toString();
            }
            return NULL_VALUE_DISPLAY_TEXT;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void addListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
         */
        public void dispose() {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
         */
        public boolean isLabelProperty(Object element, String property) {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
         */
        public void removeListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 
     * DOC zshen DrillDownResultEditor class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class DrillDownResultContentProvider implements IStructuredContentProvider {

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof DrillDownEditorInput) {
                DrillDownEditorInput ddEditorInput = ((DrillDownEditorInput) inputElement);
                List<Object[]> newColumnElementList = ddEditorInput.filterAdaptDataList();

                if (ddEditorInput.isDataSpill()) {
                    Object[] leaveOutData = new Object[newColumnElementList.get(0).length];
                    for (int i = 0; i < newColumnElementList.get(0).length; i++) {
                        leaveOutData[i] = "...";//$NON-NLS-1$
                    }
                    newColumnElementList.add(leaveOutData);
                }
                return newColumnElementList.toArray();
            } else if (List.class.isInstance(inputElement)) {
                List<?> columnDataSet = (List<?>) inputElement;
                return columnDataSet.toArray();
            }
            return new Object[0];
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        public void dispose() {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // TODO Auto-generated method stub
            viewer.getControl().pack();
        }

    }

    /**
     * 
     * DOC zshen DrillDownResultEditor class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    private class ListenToTheAction extends SelectionAdapter {

        Action theAction;

        public ListenToTheAction(Action action) {
            super();
            this.theAction = action;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            this.theAction.run();
            // changeCoolBarState();

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        DrillDownEditorInput ddEditorInput = (DrillDownEditorInput) this.getEditorInput();
        Indicator generateMapDBIndicator = ddEditorInput.getGenerateMapDBIndicator();
        MapDBManager.getInstance().removeDBRef(MapDBUtils.getMapDBFile(generateMapDBIndicator));
    }

}
