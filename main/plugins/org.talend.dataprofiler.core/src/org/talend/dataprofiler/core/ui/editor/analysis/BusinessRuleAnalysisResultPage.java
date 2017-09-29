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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.editor.composite.AbstractPagePart;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisTableTreeViewer;
import org.talend.dataprofiler.core.ui.editor.preview.CompositeIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.WhereRuleStatisticsStateTable;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.ITableTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.WhereRuleStatisticsTableState;
import org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.EventReceiver;
import org.talend.dataprofiler.core.ui.events.IEventReceiver;
import org.talend.dataprofiler.core.ui.events.TableDynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class BusinessRuleAnalysisResultPage extends AbstractAnalysisResultPageWithChart implements PropertyChangeListener {

    protected static Logger log = Logger.getLogger(BusinessRuleAnalysisResultPage.class);

    private Composite resultComp;

    BusinessRuleAnalysisDetailsPage masterPage;

    AnalysisTableTreeViewer tableTreeViewer;

    private Section resultSection = null;

    // Added TDQ-8787 20140617 yyin : store the temp indicator and its related dataset between one running
    private List<DynamicIndicatorModel> dynamicList = new ArrayList<DynamicIndicatorModel>();

    private Map<Indicator, EventReceiver> eventReceivers = new IdentityHashMap<Indicator, EventReceiver>();

    private EventReceiver registerDynamicRefreshEvent;

    private Composite sectionClient;

    // Added TDQ-9241
    private EventReceiver switchBetweenPageEvent;

    /**
     * DOC xqliu TableAnalysisResultPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public BusinessRuleAnalysisResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (BusinessRuleAnalysisDetailsPage) analysisEditor.getMasterPage();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);

        if (topComposite != null && !topComposite.isDisposed()) {
            resultComp = toolkit.createComposite(topComposite);
            resultComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            resultComp.setLayout(new GridLayout());
            createResultSection(resultComp);
            form.reflow(true);
        }
    }

    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getAnalysisHandler();
    }

    @Override
    protected void createResultSection(Composite parent) {
        // ADD gdbu 2011-3-4 bug 19242
        AbstractPagePart treeViewer = masterPage.getTreeViewer();
        if (treeViewer != null && treeViewer instanceof AnalysisTableTreeViewer) {
            tableTreeViewer = (AnalysisTableTreeViewer) treeViewer;
        }
        // ~

        resultSection = createSection(form, parent, DefaultMessagesImpl.getString("TableAnalysisResultPage.analysisResult"), null); //$NON-NLS-1$
        sectionClient = toolkit.createComposite(resultSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        dynamicList.clear();
        for (final TableIndicator tableIndicator : tableTreeViewer.getTableIndicator()) {

            ExpandableComposite exComp = toolkit.createExpandableComposite(sectionClient, ExpandableComposite.TWISTIE
                    | ExpandableComposite.CLIENT_INDENT | ExpandableComposite.EXPANDED);
            // bug 10541 fix by zshen,Change some character set to be proper to add view in the table anasys
            if (tableIndicator.isTable()) {
                exComp.setText(DefaultMessagesImpl.getString(
                        "TableAnalysisResultPage.table", tableIndicator.getColumnSet().getName())); //$NON-NLS-1$
            } else {
                exComp.setText(DefaultMessagesImpl.getString(
                        "TableAnalysisResultPage.view", tableIndicator.getColumnSet().getName())); //$NON-NLS-1$
            }
            exComp.setLayout(new GridLayout());
            exComp.setLayoutData(new GridData(GridData.FILL_BOTH));

            // MOD xqliu 2009-06-23 bug 7481
            exComp.setExpanded(EditorPreferencePage.isUnfoldingAnalyzedEelementsResultPage());
            // ~

            final Composite comp = toolkit.createComposite(exComp);
            comp.setLayout(new GridLayout());
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));
            exComp.setClient(comp);

            createResultDataComposite(comp, tableIndicator);

            exComp.addExpansionListener(new ExpansionAdapter() {

                @Override
                public void expansionStateChanged(ExpansionEvent e) {

                    form.reflow(true);
                }

            });
        }

        resultSection.setClient(sectionClient);
    }

    private void createResultDataComposite(final Composite comp, final TableIndicator tableIndicator) {
        if (tableIndicator.getIndicators().length != 0) {

            final NamedColumnSet set = tableIndicator.getColumnSet();
            IRunnableWithProgress rwp = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                    monitor.beginTask(
                            DefaultMessagesImpl.getString("TableAnalysisResultPage.createPreview", set.getName()), IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {

                            Map<EIndicatorChartType, List<TableIndicatorUnit>> indicatorComposite = CompositeIndicator
                                    .getInstance().getTableIndicatorComposite(tableIndicator);
                            for (EIndicatorChartType chartType : indicatorComposite.keySet()) {
                                List<TableIndicatorUnit> units = indicatorComposite.get(chartType);
                                if (!units.isEmpty()) {
                                    // create UI
                                    ExpandableComposite subComp = createSubWholeComposite(comp, chartType);

                                    final Composite composite = createCompositeForTableAndChart(subComp);

                                    Composite tableTopComp = createTableComposite(composite);

                                    Analysis analysis = masterPage.getAnalysisHandler().getAnalysis();
                                    ITableTypeStates tableTypeState = TableTypeStatesFactory.getInstance().getTableStateForRule(
                                            chartType, units, tableIndicator);

                                    // create table for RownCountIndicator
                                    createTableViewerForRowCount(chartType, units, tableTopComp, analysis, tableTypeState);

                                    // create table for WhereRuleIndicator
                                    createTableForWhereRule(chartType, tableTopComp, analysis, tableTypeState, units);

                                    Composite chartTopComp = createTableComposite(composite);

                                    if (canShowChartForResultPage()) {
                                        createChartsForRules(tableIndicator, chartType, units, analysis, chartTopComp);
                                    }

                                    subComp.setClient(composite);
                                    subComp.addExpansionListener(new ExpansionAdapter() {

                                        @Override
                                        public void expansionStateChanged(ExpansionEvent e) {
                                            form.reflow(true);
                                        }

                                    });
                                }
                            }
                        }

                        /**
                         * DOC yyin Comment method "createChartsForRules".
                         * 
                         * @param tableIndicator
                         * @param chartType
                         * @param units
                         * @param analysis
                         * @param chartTopComp
                         */
                        private void createChartsForRules(final TableIndicator tableIndicator, EIndicatorChartType chartType,
                                List<TableIndicatorUnit> units, Analysis analysis, Composite chartTopComp) {
                            IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartStateOfTableAna(chartType, units,
                                    tableIndicator);

                            // get all indicator lists separated by chart, and only
                            // WhereRuleStatisticsStateTable can get not-null charts
                            List<List<Indicator>> pagedIndicators = ((WhereRuleStatisticsStateTable) chartTypeState)
                                    .getPagedIndicators();
                            // Added TDQ-9241: for each list(for each chart), check if the current
                            // list has been registered dynamic event
                            List<Object> datasets = new ArrayList<Object>();
                            for (List<Indicator> oneChart : pagedIndicators) {
                                IEventReceiver event = EventManager.getInstance().findRegisteredEvent(oneChart.get(0),
                                        EventEnum.DQ_DYMANIC_CHART, 0);
                                if (event != null) {
                                    // get the dataset from the event
                                    Object dataset = ((TableDynamicChartEventReceiver) event).getDataset();
                                    // if there has the dataset for the current rule, use it to replace,
                                    // (only happen when first switch from master to result page, during
                                    // one running)
                                    if (dataset != null) {
                                        datasets.add(dataset);
                                    }

                                }// ~
                            }
                            // create chart
                            List<Object> charts = null;
                            if (datasets.size() > 0) {
                                charts = chartTypeState.getChartList(datasets);
                            } else {
                                charts = chartTypeState.getChartList();
                                datasets = ((WhereRuleStatisticsStateTable) chartTypeState).getTempDatasetList();
                            }
                            if (charts != null) {

                                int index = 0;
                                for (int i = 0; i < charts.size(); i++) {
                                    Object chart2 = charts.get(i);
                                    Object chartComp = TOPChartUtils.getInstance().createChartCompositeWithSpecialSize(
                                            chartTopComp, SWT.NONE, chart2, true, 250, 550);
                                    // Added TDQ-8787 20140707 yyin: create and store the dynamic model for
                                    // each chart
                                    DynamicIndicatorModel dyModel = AnalysisUtils.createDynamicModel(chartType,
                                            pagedIndicators.get(index++), chart2);
                                    dynamicList.add(dyModel);
                                    // ~

                                    // one dataset <--> one chart
                                    addMenuToChartComp(chartComp, chartTypeState.getDataExplorer(), analysis,
                                            ((ICustomerDataset) datasets.get(i)).getDataEntities());
                                }
                            }
                        }

                        /**
                         * DOC yyin Comment method "createTableForWhereRule".
                         * 
                         * @param chartType
                         * @param tableTopComp
                         * @param analysis
                         * @param tableTypeState
                         * @param units
                         * @return
                         */
                        private void createTableForWhereRule(EIndicatorChartType chartType, Composite tableTopComp,
                                Analysis analysis, ITableTypeStates tableTypeState, List<TableIndicatorUnit> units) {
                            TableWithData chartData = new TableWithData(chartType, tableTypeState.getDataEntity());
                            TableViewer tableviewer = tableTypeState.getTableForm(tableTopComp);
                            tableviewer.setInput(chartData);
                            ChartTableFactory.addMenuAndTip(tableviewer, tableTypeState.getDataExplorer(), analysis);

                            // Added TDQ-8787 20140707 yyin: create and store the dynamic model
                            List<Indicator> allRules = new ArrayList<Indicator>();
                            List<TableIndicatorUnit> removeRowCountUnit = ((WhereRuleStatisticsTableState) tableTypeState)
                                    .removeRowCountUnit(units);
                            for (TableIndicatorUnit indUnit : removeRowCountUnit) {
                                allRules.add(indUnit.getIndicator());
                            }
                            DynamicIndicatorModel dyModel = AnalysisUtils.createDynamicModel(chartType, allRules, null);
                            dyModel.setTableViewer(tableviewer);

                            dynamicList.add(dyModel);
                        }

                        /**
                         * DOC yyin Comment method "createTableViewerForRowCount".
                         * 
                         * @param chartType
                         * @param units
                         * @param tableTopComp
                         * @param analysis
                         * @param tableTypeState
                         */
                        private void createTableViewerForRowCount(EIndicatorChartType chartType, List<TableIndicatorUnit> units,
                                Composite tableTopComp, Analysis analysis, ITableTypeStates tableTypeState) {
                            WhereRuleStatisticsTableState tableWhereRule = (WhereRuleStatisticsTableState) tableTypeState;
                            TableWithData chartDataRowCount = new TableWithData(chartType, tableWhereRule
                                    .getDataEntityOfRowCount());

                            TableViewer tableviewerRowCount = tableWhereRule.getTableFormRowCount(tableTopComp);
                            tableviewerRowCount.setInput(chartDataRowCount);
                            ChartTableFactory.addMenuAndTip(tableviewerRowCount, tableTypeState.getDataExplorer(), analysis);

                            // Added TDQ-8787 20140707 yyin: create and store the dynamic model for row
                            // count's table
                            List<Indicator> rowCount = new ArrayList<Indicator>();
                            rowCount.add(tableWhereRule.getRownCountUnit(units).getIndicator());
                            DynamicIndicatorModel dyModel = AnalysisUtils.createDynamicModel(chartType, rowCount, null);
                            dyModel.setTableViewer(tableviewerRowCount);

                            dynamicList.add(dyModel);
                        }

                        /**
                         * DOC yyin Comment method "createTableComposite".
                         * 
                         * @param composite
                         * @return
                         */
                        private Composite createTableComposite(final Composite composite) {
                            Composite tableTopComp = toolkit.createComposite(composite, SWT.NULL);
                            tableTopComp.setLayout(new GridLayout(1, false));
                            tableTopComp.setLayoutData(new GridData(GridData.FILL_BOTH));
                            return tableTopComp;
                        }

                        /**
                         * DOC yyin Comment method "createCompositeForTableAndChart".
                         * 
                         * @param subComp
                         * @return
                         */
                        private Composite createCompositeForTableAndChart(ExpandableComposite subComp) {
                            final Composite composite = toolkit.createComposite(subComp, SWT.NULL);
                            composite.setLayout(new GridLayout(2, false));
                            composite.setLayoutData(new GridData(GridData.FILL_BOTH));
                            return composite;
                        }

                        /**
                         * DOC yyin Comment method "createSubWholeComposite".
                         * 
                         * @param comp
                         * @param chartType
                         * @return
                         */
                        private ExpandableComposite createSubWholeComposite(final Composite comp, EIndicatorChartType chartType) {
                            ExpandableComposite subComp = toolkit.createExpandableComposite(comp, ExpandableComposite.TWISTIE
                                    | ExpandableComposite.CLIENT_INDENT | ExpandableComposite.EXPANDED);
                            subComp.setText(chartType.getLiteral());
                            subComp.setLayoutData(new GridData(GridData.FILL_BOTH));
                            subComp.setExpanded(EditorPreferencePage.isUnfoldingIndicatorsResultPage());
                            return subComp;
                        }

                    });

                    monitor.done();
                }

            };

            try {
                new ProgressMonitorDialog(this.getEditorSite().getShell()).run(true, false, rwp);
            } catch (Exception ex) {
                log.error(ex, ex);
            }
        }
    }

    @Override
    public void setDirty(boolean isDirty) {
        // no implementation
    }

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
    public void refresh(AbstractAnalysisMetadataPage masterPage1) {
        this.masterPage = (BusinessRuleAnalysisDetailsPage) masterPage1;

        disposeComposite();

        createFormContent(getManagedForm());

    }

    /**
     * DOC yyin Comment method "disposeComposite".
     */
    private void disposeComposite() {
        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }

        if (resultComp != null && !resultComp.isDisposed()) {
            resultComp.dispose();
        }
    }

    /**
     * Added TDQ-8787 20140613 yyin: create all charts before running, register each chart with its related indicator.
     */
    public void registerDynamicEvent() {
        // register dynamic event,for the indicator (for each column)
        for (DynamicIndicatorModel oneCategoryIndicatorModel : dynamicList) {
            Object categoryDataset = oneCategoryIndicatorModel.getDataset();
            TableViewer tableViewer = oneCategoryIndicatorModel.getTableViewer();
            int index = 0;
            for (Indicator oneIndicator : oneCategoryIndicatorModel.getIndicatorList()) {
                TableDynamicChartEventReceiver eReceiver = new TableDynamicChartEventReceiver();
                eReceiver.setDataset(categoryDataset);
                eReceiver.setIndexInDataset(index++);
                eReceiver.setIndicatorName(oneIndicator.getName());
                eReceiver.setIndicator(oneIndicator);
                eReceiver.setIndicatorType(IndicatorEnum.findIndicatorEnum(oneIndicator.eClass()));
                eReceiver.setChartComposite(sectionClient);
                eReceiver.setTableViewer(tableViewer);
                // clear data
                eReceiver.clearValue();

                registerIndicatorEvent(oneIndicator, eReceiver);
            }
        }
        reLayoutChartComposite();

        registerRefreshDynamicChartEvent();
    }

    private void registerIndicatorEvent(Indicator oneIndicator, DynamicChartEventReceiver eReceiver) {
        eventReceivers.put(oneIndicator, eReceiver);
        EventManager.getInstance().register(oneIndicator, EventEnum.DQ_DYMANIC_CHART, eReceiver);
    }

    public void reLayoutChartComposite() {
        sectionClient.getParent().layout();
        sectionClient.layout();
    }

    /**
     * refresh the composite of the chart, to show the changes on the chart.
     */
    private void registerRefreshDynamicChartEvent() {
        registerDynamicRefreshEvent = new EventReceiver() {

            @Override
            public boolean handle(Object data) {
                reLayoutChartComposite();
                return true;
            }
        };
        EventManager.getInstance().register(sectionClient, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART,
                registerDynamicRefreshEvent);

        // register a event to handle switch between master and result page
        switchBetweenPageEvent = new EventReceiver() {

            int times = 0;

            @Override
            public boolean handle(Object data) {
                if (times == 0) {
                    times++;
                    masterPage.refreshGraphicsInSettingsPage();
                }
                return true;
            }
        };
        EventManager.getInstance().register(masterPage.getCurrentModelElement(), EventEnum.DQ_DYNAMIC_SWITCH_MASTER_RESULT_PAGE,
                switchBetweenPageEvent);
    }

    /**
     * unregister every dynamic events which registered before executing analysis
     * 
     * @param eventReceivers
     */
    public void unRegisterDynamicEvent() {
        // Added TDQ-9241
        EventManager.getInstance()
                .clearEvent(masterPage.getCurrentModelElement(), EventEnum.DQ_DYNAMIC_SWITCH_MASTER_RESULT_PAGE);

        for (Indicator oneIndicator : eventReceivers.keySet()) {
            DynamicChartEventReceiver eventReceiver = (DynamicChartEventReceiver) eventReceivers.get(oneIndicator);
            eventReceiver.clear();
            EventManager.getInstance().clearEvent(oneIndicator, EventEnum.DQ_DYMANIC_CHART);
        }
        eventReceivers.clear();
        EventManager.getInstance().clearEvent(sectionClient, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART);

        for (DynamicIndicatorModel dyModel : dynamicList) {
            dyModel.clear();
        }
        dynamicList.clear();

        masterPage.clearDynamicDatasets();
    }

}
