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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnTreeViewer;
import org.talend.dataprofiler.core.ui.editor.preview.CompositeIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.SummaryStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticsState;
import org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.IEventReceiver;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataprofiler.core.ui.utils.pagination.UIPagination;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.preview.EIndicatorChartType;

/**
 * DOC mzhao UIPagination class global comment. Detailled comment
 */
public class MasterPaginationInfo extends IndicatorPaginationInfo {

    private List<ExpandableComposite> previewChartList;

    private AnalysisColumnTreeViewer treeViewer;

    // Added TDQ-9272 20140806, use the Dynamic model for SQL mode only, Java mode will not use it.
    private boolean isSQLMode = true;

    public MasterPaginationInfo(ScrolledForm form, List<ExpandableComposite> previewChartList,
            List<? extends ModelElementIndicator> modelElementIndicators, UIPagination uiPagination) {
        this(form, previewChartList, modelElementIndicators, uiPagination, null);
    }

    public MasterPaginationInfo(ScrolledForm form, List<ExpandableComposite> previewChartList,
            List<? extends ModelElementIndicator> modelElementIndicators, UIPagination uiPagination,
            AnalysisColumnTreeViewer treeViewer) {
        super(form, modelElementIndicators, uiPagination);
        this.previewChartList = previewChartList;
        if (treeViewer != null) {
            this.treeViewer = treeViewer;
            if (ExecutionLanguage.JAVA.equals(treeViewer.getLanguage())) {
                isSQLMode = false;
            }
        }
    }

    @Override
    protected void render() {
        // refresh analysis tree
        if (treeViewer != null) {
            treeViewer.setElements(modelElementIndicators.toArray(new ModelElementIndicator[modelElementIndicators.size()]),
                    false);
        }
        // chart composite don't display So need't consider it.
        if (previewChartList == null || uiPagination.getChartComposite() == null) {
            return;
        }

        previewChartList.clear();
        clearDynamicList();

        if (EditorPreferencePage.isHideGraphicsSectionForSettingsPage() || !TOPChartUtils.getInstance().isTOPChartInstalled()) {
            return;
        }
        for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {
            final ExpandableComposite exComp = uiPagination.getToolkit().createExpandableComposite(
                    uiPagination.getChartComposite(), ExpandableComposite.TREE_NODE | ExpandableComposite.CLIENT_INDENT);

            needDispostWidgets.add(exComp);
            exComp.setText(DefaultMessagesImpl
                    .getString("ColumnMasterDetailsPage.column", modelElementIndicator.getElementName())); //$NON-NLS-1$
            exComp.setLayout(new GridLayout());
            exComp.setData(modelElementIndicator);
            previewChartList.add(exComp);

            Composite comp = uiPagination.getToolkit().createComposite(exComp);
            comp.setLayout(new GridLayout());
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));
            Map<EIndicatorChartType, List<IndicatorUnit>> indicatorComposite = CompositeIndicator.getInstance()
                    .getIndicatorComposite(modelElementIndicator);
            for (EIndicatorChartType chartType : indicatorComposite.keySet()) {
                List<IndicatorUnit> units = indicatorComposite.get(chartType);
                if (!units.isEmpty()) {
                    if (chartType == EIndicatorChartType.UDI_FREQUENCY) {
                        for (IndicatorUnit unit : units) {
                            List<IndicatorUnit> specialUnit = new ArrayList<IndicatorUnit>();
                            specialUnit.add(unit);
                            createChart(comp, chartType, specialUnit);
                        }
                    } else {
                        createChart(comp, chartType, units);
                    }
                }
            }

            exComp.addExpansionListener(new ExpansionAdapter() {

                @Override
                public void expansionStateChanged(ExpansionEvent e) {
                    uiPagination.getChartComposite().layout();
                    form.reflow(true);
                    if (e.getState()) {
                        exComp.setExpanded(e.getState());
                        exComp.getParent().pack();
                    }
                }

            });
            exComp.setExpanded(true);
            exComp.setClient(comp);
            uiPagination.getChartComposite().layout();
        }
    }

    /**
     * DOC bZhou Comment method "createChart".
     * 
     * @param comp
     * @param chartType
     * @param units
     */
    private void createChart(Composite comp, EIndicatorChartType chartType, List<IndicatorUnit> units) {
        final IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartState(chartType, units);
        boolean isPattern = chartTypeState instanceof PatternStatisticsState;
        Object chart = null;
        try {
            // MOD TDQ-8787 20140722 yyin:(when first switch from master to result) if there is some dynamic event for
            // the
            // current indicator, use its dataset directly (TDQ-9241)
            IEventReceiver event = EventManager.getInstance().findRegisteredEvent(units.get(0).getIndicator(),
                    EventEnum.DQ_DYMANIC_CHART, 0);
            if (event == null) {
                chart = chartTypeState.getChart();
            } else {
                chart = chartTypeState.getChart(((DynamicChartEventReceiver) event).getDataset());
            }// ~

            if (chart == null) {
                return;
            }
            if (!isPattern) { // need not to decorate the chart of Pattern(Regex/Sql/UdiMatch)
                TOPChartUtils.getInstance().decorateChart(chart, false);
            } else {
                TOPChartUtils.getInstance().decoratePatternMatching(chart);
            }
            Object chartComp = TOPChartUtils.getInstance().createChartComposite(comp, SWT.NONE, chart, true);
            addListenerToChartComp(chartComp, chartTypeState);

            List<Indicator> indicators = getIndicators(units);
            if (isSQLMode) {// use the dynamic model for SQL mode only.
                DynamicIndicatorModel dyModel = AnalysisUtils.createDynamicModel(chartType, indicators, chart);
                if (EIndicatorChartType.SUMMARY_STATISTICS.equals(chartType)) {
                    if (units.size() == SummaryStatisticsState.FULL_FLAG) {
                        indicators = getIndicatorsForTable(units, false);
                    }
                    dyModel.setSummaryIndicators(indicators);
                }
                this.dynamicList.add(dyModel);
                if (EIndicatorChartType.SUMMARY_STATISTICS.equals(chartType)) {
                    // for summary indicators: need to record the chart composite, which is used for create BAW chart
                    dyModel.setBawParentChartComp(chartComp);
                }
            }
        } catch (Error e) {
            log.error(DefaultMessagesImpl.getString("IndicatorPaginationInfo.FailToCreateChart"), e); //$NON-NLS-1$
        } catch (Exception exp) {
            log.error(DefaultMessagesImpl.getString("IndicatorPaginationInfo.FailToCreateChart"), exp); //$NON-NLS-1$
        }

    }

    /**
     * Getter for previewChartList. not used any more
     * 
     * @return the previewChartList
     */
    @Deprecated
    public List<ExpandableComposite> getPreviewChartList() {
        return previewChartList;
    }
}
