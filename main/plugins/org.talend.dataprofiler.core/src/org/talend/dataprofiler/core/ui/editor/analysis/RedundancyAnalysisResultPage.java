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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.model.MenuItemEntity;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.RowMatchingIndicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.RowMatchExplorer;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;
import org.talend.utils.format.StringFormatUtil;

import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC rli class global comment. Detailled comment
 */
public class RedundancyAnalysisResultPage extends AbstractAnalysisResultPageWithChart {

    private RedundancyAnalysisDetailsPage masterPage;

    private RowMatchingIndicator rowMatchingIndicatorA;

    private RowMatchingIndicator rowMatchingIndicatorB;

    private String executeData;

    private Composite analyzedColumnSetsComp;

    private Composite analysisResultsComp;

    private boolean isHasDeactivatedIndicator;

    private String setAMatchPercent;

    private String setBMatchPercent;

    private Section resultSection = null;

    private Section columnSetSection = null;

    /**
     * DOC rli ColumnsComparisonAnalysisResultPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public RedundancyAnalysisResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (RedundancyAnalysisDetailsPage) analysisEditor.getMasterPage();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);
        if (topComposite != null && !topComposite.isDisposed()) {
            analyzedColumnSetsComp = toolkit.createComposite(topComposite);
            analyzedColumnSetsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            analyzedColumnSetsComp.setLayout(new GridLayout());
            createAnalyzedColumnSetsSection(analyzedColumnSetsComp);
            analysisResultsComp = toolkit.createComposite(topComposite);
            analysisResultsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            analysisResultsComp.setLayout(new GridLayout());
            createResultSection(analysisResultsComp);

            // resultComp = toolkit.createComposite(topComposite);
            // resultComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            // resultComp.setLayout(new GridLayout());
            // createResultSection(resultComp);
            form.reflow(true);
        }
    }

    @Override
    protected void createSummarySection(ScrolledForm form, Composite parent, AnalysisHandler analysisHandler) {
        super.createSummarySection(form, parent, analysisHandler);
        executeData = analysisHandler.getExecuteData();
    }

    private void createAnalyzedColumnSetsSection(Composite parent) {
        columnSetSection = createSection(form, parent,
                DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.analyzedColumnSets"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(columnSetSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        TableViewer elementsTableViewer = new TableViewer(sectionClient, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        Table table = elementsTableViewer.getTable();
        GridDataFactory.fillDefaults().applyTo(table);
        ((GridData) table.getLayoutData()).heightHint = 240;
        ((GridData) table.getLayoutData()).widthHint = 600;
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setDragDetect(true);
        table.setToolTipText(DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.dragAndDropToolTip")); //$NON-NLS-1$
        final TableColumn columnHeader1 = new TableColumn(table, SWT.LEAD);
        columnHeader1.setWidth(300);
        final TableColumn columnHeader2 = new TableColumn(table, SWT.LEAD);
        columnHeader2.setWidth(300);
        Analysis analysis = this.masterPage.getAnalysisHandler().getAnalysis();
        isHasDeactivatedIndicator = analysis.getParameters().getDeactivatedIndicators().size() != 0;
        EList<Indicator> indicators = analysis.getResults().getIndicators();

        if (indicators.size() != 0) {
            rowMatchingIndicatorA = (RowMatchingIndicator) indicators.get(0);
            rowMatchingIndicatorB = (RowMatchingIndicator) indicators.get(1);
            TdColumn columnA = null;
            if (rowMatchingIndicatorA.getColumnSetA().size() > 0) {
                columnA = rowMatchingIndicatorA.getColumnSetA().get(0);
                if (columnA.eIsProxy()) {
                    columnA = (TdColumn) EObjectHelper.resolveObject(columnA);
                }
            }
            String columnName = rowMatchingIndicatorA.getColumnSetA().size() > 0 ? ColumnHelper
                    .getColumnOwnerAsColumnSet(columnA).getName() : PluginConstant.EMPTY_STRING;
            columnHeader1.setText(columnName.equals(PluginConstant.EMPTY_STRING) ? columnName : DefaultMessagesImpl.getString(
                    "ColumnsComparisonAnalysisResultPage.elementsFrom", columnName)); //$NON-NLS-1$
            TdColumn columnB = null;
            if (rowMatchingIndicatorA.getColumnSetB().size() > 0) {
                columnB = rowMatchingIndicatorA.getColumnSetB().get(0);
                if (columnB.eIsProxy()) {
                    columnB = (TdColumn) EObjectHelper.resolveObject(columnB);
                }
            }
            columnName = rowMatchingIndicatorA.getColumnSetA().size() > 0 ? ColumnHelper.getColumnOwnerAsColumnSet(columnB)
                    .getName() : PluginConstant.EMPTY_STRING;
            columnHeader2.setText(columnName.equals(PluginConstant.EMPTY_STRING) ? columnName : DefaultMessagesImpl.getString(
                    "ColumnsComparisonAnalysisResultPage.elementsFrom", columnName)); //$NON-NLS-1$
        }
        ColumnPairsViewerProvider provider = new ColumnPairsViewerProvider();
        elementsTableViewer.setContentProvider(provider);
        elementsTableViewer.setLabelProvider(provider);
        elementsTableViewer.setInput(rowMatchingIndicatorA);
        columnSetSection.setClient(sectionClient);
    }

    @Override
    protected void createResultSection(Composite parent) {
        resultSection = createSection(form, parent,
                DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.analysisResults"), ""); //$NON-NLS-1$ //$NON-NLS-2$
        Composite sectionClient = toolkit.createComposite(resultSection);
        sectionClient.setLayout(new GridLayout(2, false));
        sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        resultSection.setClient(sectionClient);
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        }
        Table resultTable = new Table(sectionClient, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        resultTable.setLinesVisible(true);
        resultTable.setHeaderVisible(true);
        final TableColumn columnHeader0 = new TableColumn(resultTable, SWT.LEAD);
        columnHeader0.setWidth(100);
        final TableColumn columnHeader1 = new TableColumn(resultTable, SWT.LEAD);
        columnHeader1.setWidth(150);
        // add by hcheng for 6503:change the "set A" and "set B" labels
        Analysis analysis = this.masterPage.getAnalysisHandler().getAnalysis();
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        rowMatchingIndicatorA = (RowMatchingIndicator) indicators.get(0);
        rowMatchingIndicatorB = (RowMatchingIndicator) indicators.get(1);

        // MOD yyi 2009-10-27 9100: not save when remove element in comparison analysis
        int sizeA = rowMatchingIndicatorA.getColumnSetA().size();
        int sizeB = rowMatchingIndicatorA.getColumnSetB().size();
        if (sizeA > 0 && sizeB > 0) {

            TdColumn columnA = rowMatchingIndicatorA.getColumnSetA().get(0);
            if (columnA.eIsProxy()) {
                columnA = (TdColumn) EObjectHelper.resolveObject(columnA);
            }
            String tableNameA = ColumnHelper.getColumnOwnerAsColumnSet(columnA).getName();
            TdColumn columnB = rowMatchingIndicatorA.getColumnSetB().get(0);
            if (columnB.eIsProxy()) {
                columnB = (TdColumn) EObjectHelper.resolveObject(columnB);
            }
            String tableNameB = ColumnHelper.getColumnOwnerAsColumnSet(columnB).getName();
            // ~
            // MOD zshen 11136: the chart of result lose one table
            if (tableNameA.equals(tableNameB)) {
                tableNameB = tableNameB + "(*)"; //$NON-NLS-1$
            }
            // ~11136
            columnHeader1.setText(tableNameA);
            if (!isHasDeactivatedIndicator) {
                final TableColumn columnHeader2 = new TableColumn(resultTable, SWT.LEAD);
                columnHeader2.setWidth(150);
                columnHeader2.setText(tableNameB);
            }
            createTableItems(resultTable);

            if (canShowChartForResultPage()) {
                creatChart(sectionClient, tableNameA, tableNameB);
            }

            StringBuilder description = new StringBuilder();
            description.append(DefaultMessagesImpl.getString(
                    "ColumnsComparisonAnalysisResultPage.ASetFoundInB", setAMatchPercent, tableNameA, tableNameB)); //$NON-NLS-1$
            if (!isHasDeactivatedIndicator) {
                description.append("\n"); //$NON-NLS-1$
                description.append(DefaultMessagesImpl.getString(
                        "ColumnsComparisonAnalysisResultPage.BSetFoundInA", setBMatchPercent, tableNameB, tableNameA)); //$NON-NLS-1$
            }
            resultSection.setDescription(description.toString());
        }
        resultSection.layout();
    }

    private void createTableItems(final Table resultTable) {
        Long columnSetARows = rowMatchingIndicatorA.getMatchingValueCount() + rowMatchingIndicatorA.getNotMatchingValueCount();

        TableItem item1 = new TableItem(resultTable, SWT.NULL);
        item1.setText(0, DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.%Match")); //$NON-NLS-1$
        // TDQ-8695 display "N/A" if it is infinite or NaN
        double matchPerA = (rowMatchingIndicatorA.getMatchingValueCount().doubleValue()) / columnSetARows.doubleValue();
        if (Double.isNaN(matchPerA) || Double.isInfinite(matchPerA)) {
            setAMatchPercent = org.talend.dataquality.PluginConstant.NA_STRING;
        } else {
            setAMatchPercent = StringFormatUtil.format(matchPerA, StringFormatUtil.PERCENT).toString();
        }
        item1.setText(1, setAMatchPercent);
        TableItem item2 = new TableItem(resultTable, SWT.NULL);
        item2.setText(0, DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.%NotMatch")); //$NON-NLS-1$
        double notMatchPerA = (rowMatchingIndicatorA.getNotMatchingValueCount().doubleValue()) / columnSetARows.doubleValue();
        if (Double.isNaN(notMatchPerA) || Double.isInfinite(notMatchPerA)) {
            item2.setText(1, org.talend.dataquality.PluginConstant.NA_STRING);
        } else {
            item2.setText(1, StringFormatUtil.format(notMatchPerA, StringFormatUtil.PERCENT).toString());
        }
        TableItem item3 = new TableItem(resultTable, SWT.NULL);
        item3.setText(0, DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.Match")); //$NON-NLS-1$
        item3.setText(1, rowMatchingIndicatorA.getMatchingValueCount().toString());
        TableItem item4 = new TableItem(resultTable, SWT.NULL);
        item4.setText(0, DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.NotMatch")); //$NON-NLS-1$
        item4.setText(1, rowMatchingIndicatorA.getNotMatchingValueCount().toString());
        TableItem item5 = new TableItem(resultTable, SWT.NULL);
        item5.setText(0, DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.rows")); //$NON-NLS-1$
        item5.setText(1, columnSetARows.toString());

        if (!isHasDeactivatedIndicator) {
            Long columnSetBRows = rowMatchingIndicatorB.getMatchingValueCount()
                    + rowMatchingIndicatorB.getNotMatchingValueCount();
            double matchPerB = rowMatchingIndicatorB.getMatchingValueCount().doubleValue() / columnSetBRows.doubleValue();
            if (Double.isNaN(matchPerB) || Double.isInfinite(matchPerB)) {
                setBMatchPercent = org.talend.dataquality.PluginConstant.NA_STRING;
            } else {
                setBMatchPercent = StringFormatUtil.format(matchPerB, StringFormatUtil.PERCENT).toString();
            }
            item1.setText(2, setBMatchPercent);
            double notMatchPerB = (rowMatchingIndicatorB.getNotMatchingValueCount().doubleValue()) / columnSetBRows.doubleValue();
            if (Double.isNaN(notMatchPerB) || Double.isInfinite(notMatchPerB)) {
                item2.setText(2, org.talend.dataquality.PluginConstant.NA_STRING);
            } else {
                item2.setText(2, StringFormatUtil.format(notMatchPerB, StringFormatUtil.PERCENT).toString());
            }
            item3.setText(2, rowMatchingIndicatorB.getMatchingValueCount().toString());
            item4.setText(2, rowMatchingIndicatorB.getNotMatchingValueCount().toString());
            item5.setText(2, columnSetBRows.toString());
        }
        // add by hcheng for 6530 (add menus for table)
        final TableCursor cursor = new TableCursor(resultTable, SWT.NONE);

        cursor.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int column = cursor.getColumn();
                if (column == 1) {
                    resultTable.setMenu(createMenu(resultTable, rowMatchingIndicatorA));
                } else if (column == 2) {
                    resultTable.setMenu(createMenu(resultTable, rowMatchingIndicatorB));
                } else {
                    resultTable.setMenu(null);
                }

                // ADD by msjian 2011-4-29 20602: click the data, its color changed
                if (e.widget instanceof TableCursor) {
                    ((TableCursor) e.widget).setForeground(new Color(null, 0, 0, 0));
                }
                // ~
            }

        });

        // TDQ-10416 msjian: show the menu when right click after a left click
        cursor.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                // do nothing
            }

            public void mouseDown(MouseEvent e) {
                if (resultTable.getMenu() != null) {
                    int column = cursor.getColumn();
                    if (e.button == 3 && (column == 1 || column == 2)) {
                        resultTable.getMenu().setVisible(true);
                    } else {
                        resultTable.getMenu().setVisible(false);
                    }
                }
            }

            public void mouseUp(MouseEvent e) {
                // do nothing
            }

        });
    }

    /**
     * 
     * DOC hcheng Comment method "createMenu".
     * 
     * @param resultTable
     * @param indicator
     * @return
     */
    private Menu createMenu(final Table resultTable, final RowMatchingIndicator indicator) {

        if (indicator == null) {
            return null;
        }

        final ColumnSet columnSet = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(indicator.getAnalyzedElement());

        getAnalysisHandler().getAnalyzedColumns();

        if (columnSet == null) {
            return null;
        }
        Menu menu = new Menu(resultTable);

        MenuItem itemNotMatch = new MenuItem(menu, SWT.PUSH);
        MenuItem itemRow = new MenuItem(menu, SWT.PUSH);

        // MOD qiongli 2012-8-30 TDQ-5907 hide 'view match row' menue for hive connection.
        final Connection provider = ConnectionHelper.getDataProvider(columnSet);
        if (!ConnectionHelper.isHive(provider)) {
            MenuItem itemMatch = new MenuItem(menu, SWT.PUSH);
            itemMatch.setText(DataExplorer.MENU_VIEW_MATCH_ROWS);
            itemMatch.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));
            itemMatch.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event event) {

                    RowMatchExplorer rowMatchExplorer = new RowMatchExplorer();
                    rowMatchExplorer.setAnalysis(masterPage.getCurrentModelElement());
                    rowMatchExplorer.setEnitty(new ChartDataEntity(indicator, "", "")); //$NON-NLS-1$ //$NON-NLS-2$

                    String query = rowMatchExplorer.getRowsMatchStatement();
                    if (provider != null) {
                        SqlExplorerUtils.getDefault().runInDQViewer(provider, query, columnSet.getName());
                    }
                }

            });
        }

        itemNotMatch.setText(DataExplorer.MENU_VIEW_NOT_MATCH_ROWS);
        itemNotMatch.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));

        itemRow.setText(DataExplorer.MENU_VIEW_ROWS);
        itemRow.setImage(ImageLib.getImage(ImageLib.EXPLORE_IMAGE));

        itemNotMatch.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {

                RowMatchExplorer rowMatchExplorer = new RowMatchExplorer();
                rowMatchExplorer.setAnalysis(masterPage.getCurrentModelElement());
                rowMatchExplorer.setEnitty(new ChartDataEntity(indicator, "", "")); //$NON-NLS-1$ //$NON-NLS-2$

                String query = rowMatchExplorer.getRowsNotMatchStatement();
                if (provider != null) {
                    SqlExplorerUtils.getDefault().runInDQViewer(provider, query, columnSet.getName());
                }
            }
        });

        itemRow.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {

                // MOD 10913 zshen:unify the method that get sql query
                RowMatchExplorer rowMatchExplorer = new RowMatchExplorer();
                rowMatchExplorer.setAnalysis(masterPage.getCurrentModelElement());
                rowMatchExplorer.setEnitty(new ChartDataEntity(indicator, "", "")); //$NON-NLS-1$ //$NON-NLS-2$

                String query = rowMatchExplorer.getAllRowsStatement();
                if (provider != null) {
                    SqlExplorerUtils.getDefault().runInDQViewer(provider, query, columnSet.getName());
                }
                // ~10913
            }

        });
        return menu;
    }

    private void creatChart(Composite parent, String tableA, String tableB) {
        String notMatching = DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.Not_matching"); //$NON-NLS-1$
        String matching = DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.Matching"); //$NON-NLS-1$

        CustomerDefaultCategoryDataset dataset = new CustomerDefaultCategoryDataset();
        // DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(rowMatchingIndicatorA.getNotMatchingValueCount(), notMatching, tableA);
        dataset.addValue(rowMatchingIndicatorA.getMatchingValueCount(), matching, tableA);
        // add by hcheng
        PatternChartDataEntity dataEntityA = new PatternChartDataEntity();
        dataEntityA.setLabel(tableA);
        dataEntityA.setIndicator(rowMatchingIndicatorA);
        dataEntityA.setNumMatch(rowMatchingIndicatorA.getMatchingValueCount().toString());
        dataEntityA.setNumNoMatch(rowMatchingIndicatorA.getNotMatchingValueCount().toString());

        dataset.addDataEntity(dataEntityA);

        if (!isHasDeactivatedIndicator) {
            dataset.addValue(rowMatchingIndicatorB.getNotMatchingValueCount(), notMatching, tableB);
            dataset.addValue(rowMatchingIndicatorB.getMatchingValueCount(), matching, tableB);

            PatternChartDataEntity dataEntityB = new PatternChartDataEntity();
            dataEntityB.setLabel(tableB);
            dataEntityB.setIndicator(rowMatchingIndicatorB);
            dataEntityB.setNumMatch(rowMatchingIndicatorB.getMatchingValueCount().toString());
            dataEntityB.setNumNoMatch(rowMatchingIndicatorB.getNotMatchingValueCount().toString());

            dataset.addDataEntity(dataEntityB);
        }
        Object chart = TOPChartUtils.getInstance().createStackedBarChart(
                DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.ColumnsComparison"), dataset, //$NON-NLS-1$
                true, true);

        final Object chartComp = TOPChartUtils.getInstance().createChartComposite(parent, SWT.NONE, chart, true);

        // add by hcheng for 6530(add menu to "View query result" for chart )
        addMenuToChartComp(chartComp, new RowMatchExplorer(), getAnalysisHandler().getAnalysis(),
                ((ICustomerDataset) dataset).getDataEntities());
    }

    @Override
    protected String getEditorName(Indicator indicator) {
        return indicator.getAnalyzedElement().getName();
    }

    @Override
    protected Image getItemImage(MenuItemEntity menuItem) {
        return ImageLib.getImage(ImageLib.EXPLORE_IMAGE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisResultPage#refresh(org.talend.dataprofiler.core
     * .ui.editor.analysis.AbstractAnalysisMetadataPage)
     */
    @Override
    public void refresh(AbstractAnalysisMetadataPage masterPage) {
        this.masterPage = (RedundancyAnalysisDetailsPage) masterPage;
        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }

        if (analyzedColumnSetsComp != null && !analyzedColumnSetsComp.isDisposed()) {
            analyzedColumnSetsComp.dispose();
        }

        if (analysisResultsComp != null && !analysisResultsComp.isDisposed()) {
            analysisResultsComp.dispose();
        }
        createFormContent(getManagedForm());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
        // until now, no action here
    }

    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getAnalysisHandler();
    }

    /**
     * The provider for displaying the pair of <Code>Column</Code>.
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class ColumnPairsViewerProvider extends LabelProvider implements ITableLabelProvider, IStructuredContentProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            if (element instanceof ColumnPair) {
                return ImageLib.getImage(ImageLib.TD_COLUMN);
            }
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            String text = PluginConstant.EMPTY_STRING;
            if (element instanceof ColumnPair) {
                ColumnPair columnPair = (ColumnPair) element;
                switch (columnIndex) {
                case 0:
                    text = columnPair.getAOfPair().getName();
                    return text;
                case 1:
                    text = columnPair.getBOfPair().getName();
                    return text;
                default:
                    break;
                }
                return text;
            }

            return text;
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof RowMatchingIndicator) {
                RowMatchingIndicator rowMatchingIndicator = (RowMatchingIndicator) inputElement;
                return ColumnPair.createColumnPairs(rowMatchingIndicator.getColumnSetA(), rowMatchingIndicator.getColumnSetB());
            }
            return null;
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // until now, no action here
        }

    }

    /**
     * The pair of columnA and columnB.
     */
    static class ColumnPair {

        private TdColumn columnA;

        private TdColumn columnB;

        public ColumnPair(TdColumn columnA, TdColumn columnB) {
            this.columnA = columnA;
            this.columnB = columnB;

        }

        public TdColumn getAOfPair() {
            return columnA;
        }

        public TdColumn getBOfPair() {
            return columnB;
        }

        public static ColumnPair[] createColumnPairs(List<TdColumn> columnListA, List<TdColumn> columnListB) {
            if (columnListA.size() != columnListB.size()) {
                return null;
            }
            ColumnPair[] columnPairs = new ColumnPair[columnListA.size()];
            for (int i = 0; i < columnListA.size(); i++) {
                columnPairs[i] = new ColumnPair(columnListA.get(i), columnListB.get(i));
            }
            return columnPairs;
        }
    }
}
