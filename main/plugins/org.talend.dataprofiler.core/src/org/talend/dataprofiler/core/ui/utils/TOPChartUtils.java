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
package org.talend.dataprofiler.core.ui.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PartInitException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableMenuGenerator;
import org.talend.dataprofiler.core.ui.editor.preview.model.MenuItemEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.dataset.CustomerDefaultBAWDataset;
import org.talend.dataprofiler.service.ITOPChartService;
import org.talend.dataprofiler.service.utils.ValueAggregator;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.explore.IDataExplorer;
import org.talend.dq.helper.AbstractOSGIServiceUtils;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-8 Detailled comment
 * 
 */
public class TOPChartUtils extends AbstractOSGIServiceUtils {

    private static Logger log = Logger.getLogger(TOPChartUtils.class);

    private static TOPChartUtils instance;

    private ITOPChartService chartService;

    public static TOPChartUtils getInstance() {
        if (instance == null) {
            instance = new TOPChartUtils();
        }
        return instance;
    }

    @Override
    public boolean isServiceInstalled() {
        initService(true);
        return this.chartService != null;
    }

    @Override
    public String getPluginName() {
        return ITOPChartService.PLUGIN_NAME;
    }

    @Override
    public String getServiceName() {
        return ITOPChartService.class.getName();
    }

    @Override
    protected void setService(BundleContext context, ServiceReference serviceReference) {
        if (serviceReference != null) {
            Object obj = context.getService(serviceReference);
            if (obj != null) {
                this.chartService = (ITOPChartService) obj;
            }
        }
    }

    // No need to download,only for check
    public boolean isTOPChartInstalled() {
        initService(false);
        return this.chartService != null;
    }

    public Object createBarChart(String title, Object dataset, boolean showLegend) {
        if (isTOPChartInstalled()) {
            return chartService.createBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset(), showLegend);
        }
        return null;
    }

    public Object createBarChart(String title, Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.createBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset());
        }
        return null;
    }

    /**
     * 
     * zshen Create bar chart and keep customer dataset
     * 
     * @param title
     * @param dataset
     * @return
     */
    public Object createBarChartByKCD(String title, Object dataset) {
        if (isTOPChartInstalled()) {
            CustomerDefaultCategoryDataset customerDataset = ((CustomerDefaultCategoryDataset) dataset);
            return chartService.createBarChartByKCD(title, customerDataset.getDataset(), customerDataset);
        }
        return null;
    }

    public Object createBenfordChart(String axisXLabel, String categoryAxisLabel, Object dataset, List<String> dotChartLabels,
            double[] formalValues, String title) {
        if (isTOPChartInstalled()) {
            return chartService.createBenfordChart(axisXLabel, categoryAxisLabel,
                    ((CustomerDefaultCategoryDataset) dataset).getDataset(), dotChartLabels, formalValues, title);
        }
        return null;
    }

    /**
     * 
     * zshen Create Benford chart and keep customer dataset
     * 
     * @param axisXLabel
     * @param categoryAxisLabel
     * @param dataset
     * @param dotChartLabels
     * @param formalValues
     * @param title
     * @return
     */
    public Object createBenfordChartByKCD(String axisXLabel, String categoryAxisLabel, Object dataset,
            List<String> dotChartLabels, double[] formalValues, String title) {
        if (isTOPChartInstalled()) {
            CustomerDefaultCategoryDataset customerDataset = ((CustomerDefaultCategoryDataset) dataset);
            return chartService.createBenfordChartByKCD(axisXLabel, categoryAxisLabel, customerDataset.getDataset(),
                    customerDataset, dotChartLabels, formalValues, title);
        }
        return null;
    }

    public Object createTalendChartComposite(Object parentComponent, int style, Object chart, boolean useBuffer) {
        if (isTOPChartInstalled()) {
            return chartService.createTalendChartComposite(parentComponent, style, chart, useBuffer);
        }
        return null;
    }

    public Object createChartComposite(Object composite, int style, Object chart, boolean useBuffer) {
        if (isTOPChartInstalled()) {
            return chartService.createChartComposite(composite, style, chart, useBuffer);
        }
        return null;
    }

    public Object createChartCompositeWithSpecialSize(Object composite, int style, Object chart, boolean useBuffer, int height,
            int width) {
        if (isTOPChartInstalled()) {
            return chartService.createChartCompositeWithSpecialSize(composite, style, chart, useBuffer, height, width);
        }
        return null;
    }

    public Object createChartCompositeWithFull(Object composite, Object chart) {
        if (isTOPChartInstalled()) {
            return chartService.createChartCompositeWithFull(composite, chart);
        }
        return null;
    }

    public Object getChartFromChartComposite(Object chartComposite) {
        if (isTOPChartInstalled()) {
            return chartService.getChartFromChartComposite(chartComposite);
        }
        return null;
    }

    public Object getDatasetFromChart(Object chart, int datasetIndex) {
        if (isTOPChartInstalled()) {
            return chartService.getDatasetFromChart(chart, datasetIndex);
        }
        return null;
    }

    public void decorateChart(Object chart, boolean withPlot) {
        if (isTOPChartInstalled()) {
            chartService.decorateChart(chart, withPlot);
        }
    }

    public void decorateColumnDependency(Object chart) {
        if (isTOPChartInstalled()) {
            chartService.decorateColumnDependency(chart);
        }
    }

    public void decoratePatternMatching(Object chart) {
        if (isTOPChartInstalled()) {
            chartService.decoratePatternMatching(chart);
        }
    }

    public void setOrientation(Object chart, boolean isHorizontal) {
        if (isTOPChartInstalled()) {
            chartService.setOrientation(chart, isHorizontal);
        }
    }

    public void setDisplayDecimalFormatOfChart(Object chart) {
        if (isTOPChartInstalled()) {
            chartService.setDisplayDecimalFormatOfChart(chart);
        }
    }

    public void addMouseListenerForChart(Object chartComposite, final Map<String, Object> menuMap, boolean useRowFirst) {
        if (isTOPChartInstalled()) {
            chartService.addMouseListenerForChart(chartComposite, menuMap, useRowFirst);
        }
    }

    public void addMouseListenerForConceptChart(Object chartComposite, final Map<String, Object> menuMap) {
        if (isTOPChartInstalled()) {
            chartService.addMouseListenerForConceptChart(chartComposite, menuMap);
        }
    }

    public Object createPieChart(String title, Object dataset, boolean showLegend, boolean toolTips, boolean urls) {
        if (isTOPChartInstalled()) {
            return chartService.createPieChart(title, dataset, showLegend, toolTips, urls);
        }
        return null;
    }

    public Object createBoxAndWhiskerChart(String title, Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.createBoxAndWhiskerChart(title, ((CustomerDefaultBAWDataset) dataset).getDataset());
        }
        return null;
    }

    public Object createStackedBarChart(String title, Object dataset, boolean showLegend) {
        if (isTOPChartInstalled()) {
            return chartService.createStackedBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset(), showLegend);
        }
        return null;
    }

    public Object createStackedBarChart(String title, Object dataset, boolean isHorizatal, boolean showLegend) {
        if (isTOPChartInstalled()) {
            return chartService.createStackedBarChart(title, ((CustomerDefaultCategoryDataset) dataset).getDataset(),
                    isHorizatal, showLegend);
        }
        return null;
    }

    public void addListenerToChartComp(Object chartComposite, final String referenceLink, final String menuText) {
        if (isTOPChartInstalled()) {
            chartService.addListenerToChartComp(chartComposite, referenceLink, menuText);
        }
    }

    /**
     * create the menu used for the chart in the analysis result page.
     * 
     * @param composite NOTE:this parameter must be a composite which can be disposed, because we need its dispose listener to
     * dispose
     * the menu. BUT when the composite's shell is top shell, it is NOT allowed, becasue it will never dispose until close the
     * studio.
     * @param explorer
     * @param analysis
     * @param currentEngine
     * @param currentDataEntity
     * @param currentIndicator
     * @param checkSql : when it is true , use the check sql service as the judgement, when is false, come from the column ana,
     * use the input compute as the judgement
     * @return
     */
    public Menu createMenu(final Composite composite, final IDataExplorer explorer, final Analysis analysis,
            final ChartDataEntity currentDataEntity, final Indicator currentIndicator, final boolean checkSql) {
        final Menu menu = new Menu(composite.getShell(), SWT.POP_UP);
        // TDQ-12737: fix the no more handles error, because we create a potentially unreferenced menu, so we must dispose it
        composite.addListener(SWT.Dispose, new Listener() {

            public void handleEvent(Event event) {
                menu.dispose();
            }
        });

        int createPatternFlag = 0;
        MenuItemEntity[] itemEntities = ChartTableMenuGenerator.generate(explorer, analysis, currentDataEntity);
        for (MenuItemEntity itemEntity : itemEntities) {
            MenuItem item = new MenuItem(menu, SWT.PUSH);
            item.setText(itemEntity.geti18nLabel());
            item.setImage(itemEntity.getIcon());
            item.setEnabled(DrillDownUtils.isMenuItemEnable(currentDataEntity, itemEntity, analysis));
            item.addSelectionListener(createSelectionAdapter(analysis, currentDataEntity, currentIndicator.getName(), itemEntity,
                    checkSql));

            if (ChartTableFactory.isPatternFrequencyIndicator(currentIndicator)
                    && !ChartTableFactory.isEastAsiaPatternFrequencyIndicator(currentIndicator) && createPatternFlag == 0) {
                ChartTableFactory.createMenuOfGenerateRegularPattern(analysis, menu, currentDataEntity);
            }

            createPatternFlag++;
        }
        return menu;
    }

    public SelectionAdapter createSelectionAdapter(final Analysis analysis1, final ChartDataEntity currentDataEntity,
            final String editorName, final MenuItemEntity itemEntity, final boolean checkSql) {
        return new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (ExecutionLanguage.JAVA == analysis1.getParameters().getExecutionLanguage()) {
                    try {
                        DrillDownEditorInput input = new DrillDownEditorInput(analysis1, currentDataEntity, itemEntity);
                        Boolean check = checkSql ? SqlExplorerUtils.getDefault().getSqlexplorerService() != null : input
                                .computeColumnValueLength(input.filterAdaptDataList());

                        if (check) {
                            CorePlugin
                                    .getDefault()
                                    .getWorkbench()
                                    .getActiveWorkbenchWindow()
                                    .getActivePage()
                                    .openEditor(input,
                                            "org.talend.dataprofiler.core.ui.editor.analysis.drilldown.drillDownResultEditor");//$NON-NLS-1$
                        } else {
                            if (!checkSql) {
                                MessageDialog.openWarning(null,
                                        Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Title"),//$NON-NLS-1$
                                        Messages.getString("DelimitedFileIndicatorEvaluator.badlyForm.Message"));//$NON-NLS-1$
                            }
                        }

                    } catch (PartInitException e1) {
                        log.error(e1, e1);
                    }
                } else {
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            Connection tdDataProvider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(analysis1.getContext()
                                    .getConnection());
                            String query = itemEntity.getQuery();
                            SqlExplorerUtils.getDefault().runInDQViewer(tdDataProvider, query, editorName);
                        }

                    });
                }
            }
        };
    }

    public Object createDefaultCategoryDataset() {
        if (isTOPChartInstalled()) {
            return chartService.createDefaultCategoryDataset();
        }
        return null;
    }

    public void addValueToCategoryDataset(Object dataset, double value, String labelX, String labelY) {
        if (isTOPChartInstalled()) {
            chartService.addValueToCategoryDataset(dataset, value, labelX, labelY);
        }
    }

    public Object createPieDataset(Map<String, Double> valueMap) {
        if (isTOPChartInstalled()) {
            return chartService.createPieDataset(valueMap);
        }
        return null;
    }

    public Object createDefaultBoxAndWhiskerCategoryDataset(Double mean, Double median, Double q1, Double q3,
            Double minRegularValue, Double maxRegularValue) {
        if (isTOPChartInstalled()) {
            return chartService.createDefaultBoxAndWhiskerCategoryDataset(mean, median, q1, q3, minRegularValue, maxRegularValue);
        }
        return null;
    }

    public Object createXYDataset(Map<Integer, Double> valueMap) {
        if (isTOPChartInstalled()) {
            return chartService.createXYDataset(valueMap);
        }
        return null;
    }

    public int getColumnCount(Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.getColumnCount(dataset);
        }
        return Integer.MIN_VALUE;
    }

    public int getRowCount(Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.getRowCount(dataset);
        }
        return Integer.MIN_VALUE;
    }

    public Number getValue(Object dataset, int row, int column) {
        if (isTOPChartInstalled()) {
            return chartService.getValue(dataset, row, column);
        }
        return null;
    }

    public Comparable getColumnKey(Object dataset, int column) {
        if (isTOPChartInstalled()) {
            return chartService.getColumnKey(dataset, column);
        }
        return null;
    }

    public List getColumnKeys(Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.getColumnKeys(dataset);
        }
        return null;
    }

    public void setValue(Object dataset, Number value, Comparable rowKey, Comparable columnKey) {
        if (isTOPChartInstalled()) {
            chartService.setValue(dataset, value, rowKey, columnKey);
        }
    }

    public void clearDataset(Object dataset) {
        if (isTOPChartInstalled()) {
            chartService.clearDataset(dataset);
        }
    }

    public void refrechChart(Object chartComp, Object chart) {
        if (isTOPChartInstalled()) {
            chartService.refrechChart(chartComp, chart);
        }
    }

    public void clearDefaultBoxAndWhiskerCategoryDataset(Object dataset) {
        if (isTOPChartInstalled()) {
            chartService.clearDefaultBoxAndWhiskerCategoryDataset(dataset);
        }
    }

    public Object createTaskSeriesCollection() {
        if (isTOPChartInstalled()) {
            return chartService.createTaskSeriesCollection();
        }
        return null;
    }

    public Object createTaskSeries(String keyOfDataset) {
        if (isTOPChartInstalled()) {
            return chartService.createTaskSeries(keyOfDataset);
        }
        return null;
    }

    public void addTaskToTaskSeries(Object taskSeries, String key, Date[] date) {
        if (isTOPChartInstalled()) {
            chartService.addTaskToTaskSeries(taskSeries, key, date);
        }
    }

    public void addSeriesToCollection(Object taskSeriesCollection, Object taskSeries) {
        if (isTOPChartInstalled()) {
            chartService.addSeriesToCollection(taskSeriesCollection, taskSeries);
        }
    }

    public Object createGanttChart(String chartAxies, Object ganttDataset) {
        if (isTOPChartInstalled()) {
            return chartService.createGanttChart(chartAxies, ganttDataset);
        }
        return null;
    }

    public void addSeriesToDefaultXYZDataset(Object dataset, String keyOfDataset, double[][] data) {
        if (isTOPChartInstalled()) {
            chartService.addSeriesToDefaultXYZDataset(dataset, keyOfDataset, data);
        }
    }

    public Object createBubbleChart(String chartName, Object dataset, Map<String, ValueAggregator> createXYZDatasets) {
        if (isTOPChartInstalled()) {
            return chartService.createBubbleChart(chartName, dataset, createXYZDatasets);
        }
        return null;
    }

    public Object createDefaultXYZDataset() {
        if (isTOPChartInstalled()) {
            return chartService.createDefaultXYZDataset();
        }
        return null;
    }

    public void createAnnotOnGantt(Object chart, List<Object[]> rowList, int multiDateColumn, int nominal) {
        if (isTOPChartInstalled()) {
            chartService.createAnnotOnGantt(chart, rowList, multiDateColumn, nominal);
        }
    }

    public void showChartInFillScreen(Object chart, boolean isCountAvgNull, boolean isMinMaxDate) {
        if (isTOPChartInstalled()) {
            chartService.showChartInFillScreen(chart, isCountAvgNull, isMinMaxDate);
        }
    }

    public void addSpecifiedListenersForCorrelationChart(Object chartcomp, Object chart, final boolean isAvg,
            final boolean isDate, final Map<Integer, Object> keyWithAdapter) {
        if (isTOPChartInstalled()) {
            chartService.addSpecifiedListenersForCorrelationChart(chartcomp, chart, isAvg, isDate, keyWithAdapter);
        }
    }

    public int getSeriesCount(Object chart) {
        if (isTOPChartInstalled()) {
            return chartService.getSeriesCount(chart);
        }
        return Integer.MIN_VALUE;
    }

    public int getSeriesRowCount(Object chart) {
        if (isTOPChartInstalled()) {
            return chartService.getSeriesRowCount(chart);
        }
        return Integer.MIN_VALUE;
    }

    public Object createSelectionAdapterForButton(final Object chart, final boolean isCountAvg, final boolean isMinMax) {
        if (isTOPChartInstalled()) {
            return chartService.createSelectionAdapterForButton(chart, isCountAvg, isMinMax);
        }
        return null;
    }

    public String getSeriesKeyOfBubbleChart(Object chart, int index) {
        if (isTOPChartInstalled()) {
            return chartService.getSeriesKeyOfBubbleChart(chart, index);
        }
        return StringUtils.EMPTY;
    }

    public String getSeriestKeyOfGanttChart(Object chart, int index) {
        if (isTOPChartInstalled()) {
            return chartService.getSeriestKeyOfGanttChart(chart, index);
        }
        return StringUtils.EMPTY;
    }

    public Object createChartCompositeForCorrelationAna(Object parent, Object chart, int height) {
        if (isTOPChartInstalled()) {
            return chartService.createChartCompositeForCorrelationAna(parent, chart, height);
        }
        return null;
    }

    @Override
    protected String getMissingMessageName() {
        return "TOPChartUtils.missingTopChart"; //$NON-NLS-1$
    }

    @Override
    protected String getRestartMessageName() {
        return "TOPChartUtils.restartToLoadTopChart"; //$NON-NLS-1$
    }

    /**
     * use the dataset directly, mainly for dynamical chart.
     */
    public Object createBarChartWithDefaultDataset(String title, Object dataset, boolean showLegend) {
        if (isTOPChartInstalled()) {
            return chartService.createBarChart(title, dataset, showLegend);
        }
        return null;
    }

    public ITOPChartService getChartService() {
        return chartService;
    }

    /**
     * 
     * Get CustomerDataset from EncapsulationCumstomerDataset
     * 
     * @param dataset
     * @return Get CustomerDataset when dataset is EncapsulationCumstomerDataset else return null
     */
    public ICustomerDataset getCustomerDataset(Object dataset) {
        if (isTOPChartInstalled()) {
            Object customerDataset = chartService.getCustomerDataset(dataset);
            if (customerDataset != null && customerDataset instanceof ICustomerDataset) {
                return (ICustomerDataset) customerDataset;
            }
        }
        return null;
    }

}
