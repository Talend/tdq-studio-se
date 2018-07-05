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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.nebula.widgets.pagination.AbstractPageControllerComposite;
import org.eclipse.nebula.widgets.pagination.IPageChangedListener;
import org.eclipse.nebula.widgets.pagination.IPageLoader;
import org.eclipse.nebula.widgets.pagination.PageLoaderStrategyHelper;
import org.eclipse.nebula.widgets.pagination.PageableController;
import org.eclipse.nebula.widgets.pagination.collections.PageResult;
import org.eclipse.nebula.widgets.pagination.collections.PageResultContentProvider;
import org.eclipse.nebula.widgets.pagination.table.SortTableColumnSelectionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.common.ui.pagination.controller.PageableWithIndexController;
import org.talend.dataprofiler.common.ui.pagination.pageloder.MapDBPageConstant;
import org.talend.dataprofiler.common.ui.pagination.pageloder.MapDBPageLoader;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.ColumnSortListener;
import org.talend.dataprofiler.core.ui.editor.analysis.renderer.ColumnSetNavigationPageGraphicsRenderer;
import org.talend.dataprofiler.core.ui.editor.pattern.PatternsDataValidation;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnSetIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.ITableTypeStates;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dataprofiler.core.ui.wizard.patterns.DataFilterType;
import org.talend.dataprofiler.core.ui.wizard.patterns.SelectPatternsWizard;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.indicators.columnset.impl.AllMatchIndicatorImpl;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.MapDBManager;
import org.talend.dataquality.indicators.mapdb.MapDBUtils;
import org.talend.dataquality.indicators.mapdb.StandardDBName;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * @author yyi 2009-12-16
 */
public class ColumnSetAnalysisResultPage extends AbstractAnalysisResultPageWithChart implements PropertyChangeListener {

    protected static Logger log = Logger.getLogger(ColumnSetAnalysisResultPage.class);

    private Composite graphicsAndTableComp;

    private ColumnSetAnalysisDetailsPage masterPage;

    private SimpleStatIndicator simpleStaticIndicator;

    private AllMatchIndicator allMatchIndicator;

    private String executeData;

    private Section graphicsAndTableSection = null;

    private TableViewer columnsElementViewer;

    private TableFilterResult tableFilterResult = new TableFilterResult(null);

    private DataFilterType filterType = DataFilterType.ALL_DATA;

    private PageableController controller = null;

    private IPageChangedListener createLoadPageAndReplaceItemsListener = null;

    private Composite buttonComposite = null;

    private AbstractDB<Object> mapDB = null;

    private AbstractPageControllerComposite resultAndPageButtonsDecorator = null;

    /**
     * @param editor
     * @param id
     * @param title
     */
    public ColumnSetAnalysisResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (ColumnSetAnalysisDetailsPage) analysisEditor.getMasterPage();
        simpleStaticIndicator = masterPage.getSimpleStatIndicator();
        allMatchIndicator = masterPage.getAllMatchIndicator();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);

        if (topComposite != null && !topComposite.isDisposed()) {
            graphicsAndTableComp = toolkit.createComposite(topComposite);
            graphicsAndTableComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                    | GridData.VERTICAL_ALIGN_BEGINNING));
            graphicsAndTableComp.setLayout(new GridLayout());
            createResultSection(graphicsAndTableComp);
            form.reflow(true);
        }
    }

    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getAnalysisHandler();
    }

    @Override
    protected void createResultSection(Composite parent) {
        executeData = getAnalysisHandler().getExecuteData();
        graphicsAndTableSection =
                this.createSection(form, parent,
                        DefaultMessagesImpl.getString("ColumnSetResultPage.AnalysisResult"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(graphicsAndTableSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite graphicsComp = toolkit.createComposite(sectionClient);
        graphicsComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        graphicsComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        }

        Composite simpleSatisticsComp = toolkit.createComposite(sectionClient);
        simpleSatisticsComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        simpleSatisticsComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            this.createSimpleStatisticsPart(sectionClient,
                    DefaultMessagesImpl.getString("ColumnSetResultPage.SimpleStatistics"), simpleStaticIndicator); //$NON-NLS-1$

            // match
            if (0 < allMatchIndicator.getCompositeRegexMatchingIndicators().size()) {
                this.createAllMatchPart(sectionClient,
                        DefaultMessagesImpl.getString("ColumnSetAnalysisResultPage.AllMatch"), allMatchIndicator);//$NON-NLS-1$
            }
        }

        Composite tableComp = toolkit.createComposite(sectionClient);
        tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        tableComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            if (simpleStaticIndicator.isUsedMapDBMode()
                    && AnalysisHelper.isJavaExecutionEngine(masterPage.getCurrentModelElement())) {
                this.createTableSectionPartForMapDB(sectionClient,
                        DefaultMessagesImpl.getString("ColumnSetResultPage.Data"), simpleStaticIndicator); //$NON-NLS-1$
            } else {
                this.createTableSectionPart(sectionClient,
                        DefaultMessagesImpl.getString("ColumnSetResultPage.Data"), simpleStaticIndicator); //$NON-NLS-1$
            }
        }
        graphicsAndTableSection.setExpanded(true);
        graphicsAndTableSection.setClient(sectionClient);
    }

    private Section createAllMatchPart(Composite parentComp, String title, AllMatchIndicator matchIndicator) {
        Section section = createSection(form, parentComp, title, null);
        section.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite sectionClient = toolkit.createComposite(section);
        sectionClient.setLayout(new GridLayout(2, false));
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.AllMatchIndicatorEnum, allMatchIndicator));

        EIndicatorChartType matchingType = EIndicatorChartType.PATTERN_MATCHING;
        ITableTypeStates tableTypeState = TableTypeStatesFactory.getInstance().getTableState(matchingType, units);
        TableWithData chartData = new TableWithData(matchingType, tableTypeState.getDataEntity());

        TableViewer tableviewer = tableTypeState.getTableForm(sectionClient);
        tableviewer.setInput(chartData);

        // MOD qiongli feature 19192.
        if (masterPage.getCurrentModelElement().getParameters().isStoreData()) {
            ChartTableFactory.addMenuAndTip(tableviewer, tableTypeState.getDataExplorer(),
                    masterPage.getCurrentModelElement());
        } else {
            TableUtils.addTooltipForTable(tableviewer.getTable());
            TableUtils.addActionTooltip(tableviewer.getTable());
        }

        if (canShowChartForResultPage()) {
            IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartState(matchingType, units);
            Object chart = chartTypeState.getChart();
            TOPChartUtils.getInstance().decorateChart(chart, false);
            if (chart != null) {
                TOPChartUtils.getInstance().createChartComposite(sectionClient, SWT.NONE, chart, true);
            }
        }
        section.setClient(sectionClient);
        return section;
    }

    private Section createSimpleStatisticsPart(Composite parentComp, String title,
            SimpleStatIndicator simpleStatIndicator) {
        // MOD sgandon 15/03/2010 bug 11769 : made descriotion null to remove empty space.
        Section section = createSection(form, parentComp, title, null);
        section.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite sectionClient = toolkit.createComposite(section);
        sectionClient.setLayout(new GridLayout(2, false));
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        createSimpleTable2(form, sectionClient, simpleStatIndicator);
        section.setClient(sectionClient);
        return section;
    }

    private void createSimpleTable2(final ScrolledForm form, final Composite composite,
            final SimpleStatIndicator simpleStatIndicator) {

        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.RowCountIndicatorEnum, masterPage
                .getSimpleStatIndicator()
                .getRowCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.DistinctCountIndicatorEnum, masterPage
                .getSimpleStatIndicator()
                .getDistinctCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.DuplicateCountIndicatorEnum, masterPage
                .getSimpleStatIndicator()
                .getDuplicateCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.UniqueIndicatorEnum, masterPage
                .getSimpleStatIndicator()
                .getUniqueCountIndicator()));

        EIndicatorChartType simpleStatType = EIndicatorChartType.SIMPLE_STATISTICS;
        // create table firstly
        ITableTypeStates tableTypeState = TableTypeStatesFactory.getInstance().getTableState(simpleStatType, units);
        TableWithData chartData = new TableWithData(simpleStatType, tableTypeState.getDataEntity());

        TableViewer tableviewer = tableTypeState.getTableForm(composite);
        tableviewer.setInput(chartData);
        // MOD qiongli feature 19192.
        DataExplorer dataExplorer = tableTypeState.getDataExplorer();
        Analysis analysis = this.getAnalysisHandler().getAnalysis();
        ChartTableFactory.addMenuAndTip(tableviewer, dataExplorer, analysis);

        // create chart
        if (canShowChartForResultPage()) {
            IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartState(simpleStatType, units);
            Object chart = chartTypeState.getChart();
            TOPChartUtils.getInstance().decorateChart(chart, false);
            if (chart != null) {
                Object chartComposite2 =
                        TOPChartUtils.getInstance().createChartComposite(composite, SWT.NONE, chart, true);

                addMenuToChartComp(chartComposite2, dataExplorer, analysis,
                        ((ICustomerDataset) chartTypeState.getDataset()).getDataEntities());

            }
        }
    }

    private Section createTableSectionPart(Composite parentComp, String title, SimpleStatIndicator ssIndicator) {
        Section columnSetElementSection = this.createSection(form, parentComp, title, null);
        Composite sectionTableComp = toolkit.createComposite(columnSetElementSection);
        // MOD yyi 2010-12-07 17282:create parameter section for storing data control
        if (ssIndicator.isStoreData()) {
            columnSetElementSection.setExpanded(true);
            columnSetElementSection.setEnabled(true);

            sectionTableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
            sectionTableComp.setLayout(new GridLayout());
            // MOD zshen for feature 14000
            Button filterDataBt = new Button(sectionTableComp, SWT.NONE);
            filterDataBt.setText(DefaultMessagesImpl.getString("ColumnSetResultPage.filterData"));//$NON-NLS-1$
            filterDataBt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
            filterDataBt.setEnabled(containAllMatchIndicator());
            filterDataBt.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                public void mouseDown(MouseEvent e) {
                    List<Indicator> indicatorsList = masterPage.getCurrentModelElement().getResults().getIndicators();
                    SelectPatternsWizard wizard = new SelectPatternsWizard(indicatorsList);
                    wizard.setFilterType(filterType);
                    wizard.setOldTableInputList(ColumnSetAnalysisResultPage.this.tableFilterResult
                            .getTableFilterResult());
                    WizardDialog dialog = new WizardDialog(null, wizard);
                    dialog.setPageSize(300, 400);
                    wizard.setContainer(dialog);
                    wizard.setWindowTitle(DefaultMessagesImpl.getString("SelectPatternsWizard.title"));//$NON-NLS-1$
                    if (WizardDialog.OK == dialog.open()) {
                        ColumnSetAnalysisResultPage.this.tableFilterResult =
                                new TableFilterResult(wizard.getPatternSelectPage().getTableInputList());
                        filterType = wizard.getPatternSelectPage().getFilterType();
                        columnsElementViewer.refresh();
                    }
                }

                public void mouseUp(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

            });

            columnsElementViewer = new TableViewer(sectionTableComp, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);

            Table table = columnsElementViewer.getTable();
            table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            List<String> tableColumnNames = ssIndicator.getColumnHeaders();
            for (String tableColumnName : tableColumnNames) {
                final TableColumn columnHeader = new TableColumn(table, SWT.NONE);
                columnHeader.setText(tableColumnName);
            }
            table.setLinesVisible(true);
            table.setHeaderVisible(true);
            TableSectionViewerProvider provider = new TableSectionViewerProvider();
            List<Object[]> tableRows = ssIndicator.getListRows();
            columnsElementViewer.setContentProvider(provider);
            columnsElementViewer.setLabelProvider(provider);
            columnsElementViewer.setInput(tableRows);
            for (int i = 0; i < tableColumnNames.size(); i++) {
                table.getColumn(i).pack();
            }
            columnSetElementSection.setClient(sectionTableComp);
            columnSetElementSection.setExpanded(false);
            // ADDED sgandon 15/03/2010 bug 11769 : setup the size of the table to avoid crash and add consistency.
            setupTableGridDataLimitedSize(table, tableRows.size());

            addColumnSorters(columnsElementViewer, table.getColumns(), this.buildSorter(tableRows));
        } else {
            columnSetElementSection.setExpanded(false);
            columnSetElementSection.setEnabled(false);
        }
        return columnSetElementSection;
    }

    private Section createTableSectionPartForMapDB(Composite parentComp, String title, SimpleStatIndicator ssIndicator) {
        Section columnSetElementSection = this.createSection(form, parentComp, title, null);
        Composite sectionTableComp = toolkit.createComposite(columnSetElementSection);
        // MOD yyi 2010-12-07 17282:create parameter section for storing data control
        if (ssIndicator.isStoreData()) {
            columnSetElementSection.setExpanded(true);
            columnSetElementSection.setEnabled(true);

            sectionTableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
            sectionTableComp.setLayout(new GridLayout());
            // MOD zshen for feature 14000

            try {
                mapDB = MapDBUtils.getMapDB(StandardDBName.dataSection.name(), ssIndicator);
            } catch (IOError error) {
                log.warn(error.getMessage(), error);
            }
            Button filterDataBt = new Button(sectionTableComp, SWT.NONE);
            filterDataBt.setText(DefaultMessagesImpl.getString("ColumnSetResultPage.filterData"));//$NON-NLS-1$
            filterDataBt.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
            filterDataBt.setEnabled(containAllMatchIndicator() && mapDB != null);
            filterDataBt.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent e) {
                    // do nothing until now
                }

                public void mouseDown(MouseEvent e) {
                    List<Indicator> indicatorsList = masterPage.getCurrentModelElement().getResults().getIndicators();
                    SelectPatternsWizard wizard = new SelectPatternsWizard(indicatorsList);
                    wizard.setFilterType(filterType);
                    wizard.setOldTableInputList(ColumnSetAnalysisResultPage.this.tableFilterResult
                            .getTableFilterResult());
                    WizardDialog dialog = new WizardDialog(null, wizard);
                    dialog.setPageSize(300, 400);
                    wizard.setContainer(dialog);
                    wizard.setWindowTitle(DefaultMessagesImpl.getString("SelectPatternsWizard.title"));//$NON-NLS-1$
                    if (WizardDialog.OK == dialog.open()) {
                        ColumnSetAnalysisResultPage.this.tableFilterResult =
                                new TableFilterResult(wizard.getPatternSelectPage().getTableInputList());
                        filterType = wizard.getPatternSelectPage().getFilterType();
                        columnsElementViewer.refresh();
                        redrawController();
                    }
                }

                public void mouseUp(MouseEvent e) {
                    // do nothing until now

                }

            });

            columnsElementViewer = new TableViewer(sectionTableComp, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);

            Table table = columnsElementViewer.getTable();
            table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            table.setLinesVisible(true);
            table.setHeaderVisible(true);
            TableSectionViewerProvider provider = new TableSectionViewerProvider();
            columnsElementViewer.setContentProvider(provider);
            columnSetElementSection.setClient(sectionTableComp);
            columnSetElementSection.setExpanded(false);
            int pageSize = 100;
            // // ADDED sgandon 15/03/2010 bug 11769 : setup the size of the table to avoid crash and add consistency.
            setupTableGridDataLimitedSize(table, pageSize);

            // add pagation control
            controller = new PageableWithIndexController(MapDBPageConstant.NUMBER_PER_PAGE);

            if (mapDB != null) {
                redrawPagationComposite(sectionTableComp, null, true);
            }
            createColumns(controller, ssIndicator);

            // Set current page to 0 to refresh the table
            controller.reset();
            for (TableColumn column : table.getColumns()) {
                column.pack();
            }
        } else {
            columnSetElementSection.setExpanded(false);
            columnSetElementSection.setEnabled(false);
        }
        return columnSetElementSection;
    }

    private void redrawController() {
        Composite parent = buttonComposite.getParent();
        if (ColumnSetAnalysisResultPage.this.tableFilterResult.isEmptyResult() || DataFilterType.ALL_DATA == filterType) {
            redrawPagationComposite(parent, null, true);
        } else {
            redrawPagationComposite(parent, ColumnSetAnalysisResultPage.this.tableFilterResult.getTableFilterResult(),
                    false);
        }
    }

    /**
     * DOC zshen Comment method "redrawPagationComposite".
     * 
     * @param parent
     */
    private void redrawPagationComposite(Composite parent, List<Map<Integer, RegexpMatchingIndicator>> patternList,
            boolean withPageNumberButton) {
        PatternsDataValidation patternsDataValidation = new PatternsDataValidation(patternList, filterType);
        patternsDataValidation.setProperties(((PageableWithIndexController) controller).getPropertiesStr());
        final IPageLoader<PageResult<Object[]>> pageLoader =
                new MapDBPageLoader<Object>(mapDB, patternsDataValidation, mapDB.size());
        controller.removePageChangedListener(createLoadPageAndReplaceItemsListener);
        controller.removePageChangedListener(resultAndPageButtonsDecorator);
        controller.setCurrentPage(-1);
        createLoadPageAndReplaceItemsListener =
                PageLoaderStrategyHelper.createLoadPageAndReplaceItemsListener(controller, columnsElementViewer,
                        pageLoader, PageResultContentProvider.getInstance(), null);
        controller.addPageChangedListener(createLoadPageAndReplaceItemsListener);
        if (buttonComposite != null) {
            buttonComposite.dispose();
        }
        buttonComposite = new Composite(parent, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        buttonComposite.setLayout(new GridLayout());

        resultAndPageButtonsDecorator =
                new ColumnSetNavigationPageGraphicsRenderer(buttonComposite, SWT.NONE, controller, withPageNumberButton);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        resultAndPageButtonsDecorator.setLayoutData(gridData);
        controller.reset();
        parent.layout();
    }

    /**
     * DOC talend Comment method "createColumns".
     * 
     * @param columnsElementViewer2
     * @param controller
     */
    private void createColumns(PageableController controller, SimpleStatIndicator ssIndicator) {
        List<String> tableColumnNames = ssIndicator.getColumnHeaders();
        for (String tableColumnName : tableColumnNames) {
            // First column is for the first name
            TableViewerColumn col = createTableViewerColumn(tableColumnName);
            TableSectionViewerProvider provider = new TableSectionViewerProvider();
            col.setLabelProvider(provider);

            // ignore last one column 'count(*)' because it is the value of map
            if (!tableColumnName.equalsIgnoreCase(ColumnsetFactory.eINSTANCE
                    .createColumnSetMultiValueIndicator()
                    .getCountAll())) {
                col.getColumn().addSelectionListener(new SortTableColumnSelectionListener(tableColumnName, controller));
            }

        }
        String[] properties = tableColumnNames.toArray(new String[] {});
        columnsElementViewer.setColumnProperties(properties);
        ((PageableWithIndexController) controller).setPropertiesStr(properties);
    }

    private TableViewerColumn createTableViewerColumn(String title) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(columnsElementViewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    /**
     * 
     * DOC hcheng Comment method "addColumnSorters".For 8267.
     * 
     * @param tableViewer
     * @param tableColumns
     * @param sorters
     */
    protected void addColumnSorters(TableViewer tableViewer, TableColumn[] tableColumns, ViewerSorter[][] sorters) {
        for (int i = 0; i < tableColumns.length; ++i) {
            tableColumns[i].addSelectionListener(new ColumnSortListener(tableColumns, i, tableViewer, sorters));
        }
    }

    private Color bg = new Color(null, 249, 139, 121);

    /**
     * DOC zshen ColumnSetResultPage class global comment. Detailled comment
     */
    class TableSectionViewerProvider extends CellLabelProvider implements IStructuredContentProvider,
            ITableLabelProvider, ITableColorProvider {

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

        public String getColumnText(Object element, int columnIndex) {
            if (List.class.isInstance(element)) {
                String[] keyArray = ((List<?>) element).toArray(new String[((List<?>) element).size()]);
                if (columnIndex < keyArray.length) {
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
            Object[] elements = (Object[]) element;

            for (Object elem : elements) {
                if (elem == null || "".equals(elem)) { //$NON-NLS-1$
                    return bg;
                }
            }
            return null;
        }

        public Color getForeground(Object element, int columnIndex) {
            // MOD by zshen for feature 14000
            return getMatchColor(element, columnIndex);
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
            cell.setForeground(getForeground(element, index));
            cell.setBackground(getBackground(element, index));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
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
        this.masterPage = (ColumnSetAnalysisDetailsPage) masterPage;
        simpleStaticIndicator = this.masterPage.getSimpleStatIndicator();

        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }

        if (graphicsAndTableComp != null && !graphicsAndTableComp.isDisposed()) {
            graphicsAndTableComp.dispose();
        }
        createFormContent(getManagedForm());
    }

    private boolean containAllMatchIndicator() {
        List<Indicator> indicatorsList = masterPage.getCurrentModelElement().getResults().getIndicators();
        for (Indicator theIndicator : indicatorsList) {
            if (theIndicator instanceof AllMatchIndicatorImpl) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void dispose() {
        if (bg != null) {
            bg.dispose();
        }
        if (AnalysisHelper.isJavaExecutionEngine(masterPage.getCurrentModelElement())) {
            MapDBManager.getInstance().closeDB(masterPage.getCurrentModelElement());
        }
        super.dispose();
    }

    private CorrelationDataSorter[][] buildSorter(List<Object[]> tableRows) {
        CorrelationDataSorter[][] result = null;
        if (tableRows != null && tableRows.size() > 0) {
            Object[] objs = tableRows.get(0);
            int length = objs.length;
            result = new CorrelationDataSorter[length][2];
            for (int i = 0; i < length; ++i) {
                result[i][0] = new CorrelationDataSorter(i + 1);
                result[i][1] = new CorrelationDataSorter(-(i + 1));
            }
        }
        return result;
    }

    /**
     * 
     * zshen Comment method "getMatchColor".
     * 
     * @param element
     * @param columnIndex
     * @return get the color of the element.
     */
    private Color getMatchColor(Object element, int columnIndex) {
        if (tableFilterResult.getTableFilterResult() != null) {
            for (Map<Integer, RegexpMatchingIndicator> tableItem : this.tableFilterResult.getTableFilterResult()) {
                RegexpMatchingIndicator regMatIndicator = tableItem.get(columnIndex);
                if (regMatIndicator == null) {
                    continue;
                }
                String regex = regMatIndicator.getRegex();
                Pattern p = java.util.regex.Pattern.compile(regex);
                if (element instanceof Object[]) {
                    Object theElement = ((Object[]) element)[columnIndex];
                    if (theElement == null) {
                        theElement = "null";//$NON-NLS-1$
                    }
                    Matcher m = p.matcher(String.valueOf(theElement));
                    if (!m.find()) {
                        return new Color(null, 255, 0, 0);
                    }

                }
            }
        }
        return new Color(null, 0, 0, 0);
    }

    public List<Map<Integer, RegexpMatchingIndicator>> getTableFilterResult() {
        return tableFilterResult.getTableFilterResult();
    }

    public void setTableFilterResult(List<Map<Integer, RegexpMatchingIndicator>> tableFilterResult) {
        this.tableFilterResult = new TableFilterResult(tableFilterResult);
    }

}
