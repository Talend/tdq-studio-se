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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnTreeViewer;
import org.talend.dataprofiler.core.ui.editor.preview.CompositeIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.FrequencyTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.PatternStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.ITableTypeStates;
import org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.IEventReceiver;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.utils.pagination.UIPagination;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.repository.model.IRepositoryNode;

/**
 * 
 * DOC mzhao UIPagination.MasterPaginationInfo class global comment. Detailled comment
 */
public class ResultPaginationInfo extends IndicatorPaginationInfo {

    private ColumnAnalysisDetailsPage masterPage;

    // Added TDQ-9272 20140806, only use the Dynamic model for SQL mode.
    private boolean isSQLMode = true;

    public ResultPaginationInfo(ScrolledForm form, List<? extends ModelElementIndicator> modelElementIndicators,
            ColumnAnalysisDetailsPage masterPage, UIPagination uiPagination) {
        super(form, modelElementIndicators, uiPagination);
        this.masterPage = masterPage;
        if (masterPage.getTreeViewer() != null) {
            ExecutionLanguage language = ((AnalysisColumnTreeViewer) masterPage.getTreeViewer()).getLanguage();
            if (ExecutionLanguage.JAVA.equals(language)) {
                isSQLMode = false;
            }
        }
    }

    /**
     * create CollapseAll Link for column name.
     * 
     * @param composite
     * @param label: column name
     */
    private void createCollapseAllLink(Composite composite, final String label) {
        ImageHyperlink collapseAllImageLink = uiPagination.getToolkit().createImageHyperlink(composite, SWT.NONE);
        collapseAllImageLink.setToolTipText(DefaultMessagesImpl.getString("CollapseColumn")); //$NON-NLS-1$
        WorkbenchUtils.setHyperlinkImage(collapseAllImageLink, ImageLib.getImage(ImageLib.COLLAPSE_ALL));
        collapseAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                Control[] expandableComposite = getIndicatorExpandableCompositeList(label);
                for (Control control : expandableComposite) {
                    if (control instanceof ExpandableComposite) {
                        ((ExpandableComposite) control).setExpanded(false);
                        ((ExpandableComposite) control).getParent().pack();
                    }
                }
                form.reflow(true);
            }
        });
    }

    /**
     * create ExpandAll Link for column name.
     * 
     * @param composite
     * @param label: column name
     */
    private void createExpandAllLink(Composite composite, final String label) {
        ImageHyperlink expandAllImageLink = uiPagination.getToolkit().createImageHyperlink(composite, SWT.NONE);
        expandAllImageLink.setToolTipText(DefaultMessagesImpl.getString("ExpandColumn")); //$NON-NLS-1$
        WorkbenchUtils.setHyperlinkImage(expandAllImageLink, ImageLib.getImage(ImageLib.EXPAND_ALL));
        expandAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                Composite compositePar = columnCompositeMap.get(label);
                if (compositePar.getParent() instanceof ExpandableComposite) {
                    ((ExpandableComposite) compositePar.getParent()).setExpanded(true);
                }

                Control[] expandableComposite = getIndicatorExpandableCompositeList(label);
                for (Control control : expandableComposite) {
                    if (control instanceof ExpandableComposite) {
                        ((ExpandableComposite) control).setExpanded(true);
                        ((ExpandableComposite) control).getParent().pack();
                    }
                }
                form.reflow(true);
            }
        });
    }

    /**
     * get all indicator subsection under the column name.
     * 
     * @param label: column name
     * @return
     */
    protected Control[] getIndicatorExpandableCompositeList(String label) {
        Composite composite = columnCompositeMap.get(label);
        Control[] children = composite.getChildren();
        return children;
    }

    @Override
    protected void render() {
        clearDynamicList();
        allExpandableCompositeList.clear();
        columnCompositeMap.clear();

        for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {

            ExpandableComposite exComp = uiPagination.getToolkit().createExpandableComposite(
                    uiPagination.getChartComposite(),
                    ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT | ExpandableComposite.EXPANDED
                            | ExpandableComposite.LEFT_TEXT_CLIENT_ALIGNMENT);
            needDispostWidgets.add(exComp);
            allExpandableCompositeList.add(exComp);

            // MOD klliu add more information about the column belongs to which table/view.
            IRepositoryNode modelElementRepositoryNode = modelElementIndicator.getModelElementRepositoryNode();
            IRepositoryNode parentNodeForColumnNode = RepositoryNodeHelper.getParentNodeForColumnNode(modelElementRepositoryNode);
            String label = parentNodeForColumnNode.getObject().getLabel();
            if (label != null && !label.equals("")) { //$NON-NLS-1$
                label = label.concat(".").concat(modelElementIndicator.getElementName());//$NON-NLS-1$
            } else {
                label = modelElementIndicator.getElementName();
            }
            // ~
            exComp.setText(DefaultMessagesImpl.getString("ColumnAnalysisResultPage.Column", label)); //$NON-NLS-1$
            exComp.setLayout(new GridLayout());
            exComp.setLayoutData(new GridData(GridData.FILL_BOTH));
            // MOD xqliu 2009-06-23 bug 7481
            exComp.setExpanded(EditorPreferencePage.isUnfoldingAnalyzedEelementsResultPage());
            // ~

            // TDQ-11525 msjian : Add "expand all" and "fold all" icon buttons in the "Analysis Results" section
            Composite collapseExpandComposite = uiPagination.getToolkit().createComposite(exComp);
            GridLayout gdLayout = new GridLayout();
            gdLayout.numColumns = 2;
            collapseExpandComposite.setLayout(gdLayout);
            createCollapseAllLink(collapseExpandComposite, label);
            createExpandAllLink(collapseExpandComposite, label);
            exComp.setTextClient(collapseExpandComposite);
            // TDQ-11525~

            Composite comp = uiPagination.getToolkit().createComposite(exComp);
            comp.setLayout(new GridLayout());
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));
            exComp.setClient(comp);
            createResultDataComposite(comp, modelElementIndicator);
            columnCompositeMap.put(label, comp);

            exComp.addExpansionListener(new ExpansionAdapter() {

                @Override
                public void expansionStateChanged(ExpansionEvent e) {
                    uiPagination.getChartComposite().layout();
                    form.reflow(true);
                }

            });
            uiPagination.getChartComposite().layout();
            masterPage.registerSection(exComp);
        }
    }

    private void createResultDataComposite(Composite comp, ModelElementIndicator modelElementIndicator) {
        if (modelElementIndicator.getIndicators().length != 0) {
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
        DynamicIndicatorModel dyModel = new DynamicIndicatorModel();

        // MOD TDQ-8787 20140618 yyin: to let the chart and table use the same dataset
        Object chart = null;
        Object dataset = null;
        // Added TDQ-8787 20140722 yyin:(when first switch from master to result) if there is some dynamic event for the
        // current indicator, use its dataset directly (TDQ-9241)
        IEventReceiver event = EventManager.getInstance().findRegisteredEvent(units.get(0).getIndicator(),
                EventEnum.DQ_DYMANIC_CHART, 0);
        // get the dataset from the event
        if (event != null) {
            dataset = ((DynamicChartEventReceiver) event).getDataset();
        }// ~

        // Added TDQ-8787 2014-06-18 yyin: add the current units and dataset into the list
        List<Indicator> indicators = null;
        dyModel.setChartType(chartType);
        this.dynamicList.add(dyModel);

        if (EIndicatorChartType.SUMMARY_STATISTICS.equals(chartType)) {
            // for the summary indicators, the table show 2 more than the bar chart
            dyModel.setSummaryIndicators(getIndicatorsForTable(units, true));
        }

        // create UI
        ExpandableComposite subComp = uiPagination.getToolkit().createExpandableComposite(comp,
                ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT | ExpandableComposite.EXPANDED);
        subComp.setText(chartType.getLiteral());
        subComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        // MOD xqliu 2009-06-23 bug 7481
        subComp.setExpanded(EditorPreferencePage.isUnfoldingIndicatorsResultPage());
        // ~

        final Composite composite = uiPagination.getToolkit().createComposite(subComp, SWT.NULL);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Analysis analysis = masterPage.getAnalysisHandler().getAnalysis();

        // create table viewer firstly
        ITableTypeStates tableTypeState = TableTypeStatesFactory.getInstance().getTableState(chartType, units);
        ChartDataEntity[] dataEntities = tableTypeState.getDataEntity();
        TableWithData chartData = new TableWithData(chartType, dataEntities);
        TableViewer tableviewer = tableTypeState.getTableForm(composite);
        tableviewer.setInput(chartData);
        tableviewer.getTable().pack();
        dyModel.setTableViewer(tableviewer);

        DataExplorer dataExplorer = tableTypeState.getDataExplorer();
        ChartTableFactory.addMenuAndTip(tableviewer, dataExplorer, analysis);

        if (EIndicatorChartType.TEXT_STATISTICS.equals(chartType) && dataEntities != null && dataEntities.length > 0) {
            // only text indicator need
            indicators = getIndicators(dataEntities);
        } else {
            indicators = getIndicators(units);
        }
        dyModel.setIndicatorList(indicators);

        // create chart
        try {
            if (!EditorPreferencePage.isHideGraphicsForResultPage() && TOPChartUtils.getInstance().isTOPChartInstalled()) {
                IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartState(chartType, units);
                boolean isPattern = chartTypeState instanceof PatternStatisticsState;
                if (event == null) {
                    chart = chartTypeState.getChart();
                    if (chart != null && isSQLMode) {// chart is null for MODE. Get the dataset by this way for SQL mode
                        if (EIndicatorChartType.BENFORD_LAW_STATISTICS.equals(chartType)) {
                            dataset = TOPChartUtils.getInstance().getDatasetFromChart(chart, 2);
                            if (dataset == null) {
                                dataset = TOPChartUtils.getInstance().getDatasetFromChart(chart, 1);
                            }
                            dyModel.setSecondDataset(TOPChartUtils.getInstance().getDatasetFromChart(chart, 0));
                        } else {
                            dataset = TOPChartUtils.getInstance().getDatasetFromChart(chart, 1);
                            if (dataset == null) {
                                dataset = TOPChartUtils.getInstance().getDatasetFromChart(chart, -1);
                            }
                        }
                    }
                } else {
                    chart = chartTypeState.getChart(dataset);
                }

                dyModel.setDataset(dataset);
                if (chart != null) {
                    if (!isPattern) { // need not to decorate the chart of Pattern(Regex/Sql/UdiMatch)
                        TOPChartUtils.getInstance().decorateChart(chart, false);
                    } else {
                        TOPChartUtils.getInstance().decoratePatternMatching(chart);
                    }
                    // TDQ-13378 vertical scroll bar for Frequency indicators
                    int swtStyle = (chartTypeState instanceof FrequencyTypeStates) ? SWT.VERTICAL : SWT.NONE;
                    Object chartComposite =
                            TOPChartUtils.getInstance().createTalendChartComposite(composite, swtStyle, chart, true);
                    dyModel.setBawParentChartComp(chartComposite);
                    Map<String, Object> menuMap = createMenuForAllDataEntity((Composite) chartComposite, dataExplorer, analysis,
                            ((ICustomerDataset) chartTypeState.getDataset()).getDataEntities());
                    // call chart service to create related mouse listener
                    if (EIndicatorChartType.BENFORD_LAW_STATISTICS.equals(chartType)
                            || EIndicatorChartType.FREQUENCE_STATISTICS.equals(chartType)) {
                        TOPChartUtils.getInstance().addMouseListenerForChart(chartComposite, menuMap, false);
                    } else {
                        TOPChartUtils.getInstance().addMouseListenerForChart(chartComposite, menuMap, true);
                    }
                }
            }
            // TDQ-11886 add these 2 catches.make it continue to work even if encounter some problems.
        } catch (Error e) {
            log.error(DefaultMessagesImpl.getString("IndicatorPaginationInfo.FailToCreateChart"), e); //$NON-NLS-1$
        } catch (Exception exp) {
            log.error(DefaultMessagesImpl.getString("IndicatorPaginationInfo.FailToCreateChart"), exp); //$NON-NLS-1$
        }

        subComp.setClient(composite);
        subComp.addExpansionListener(new ExpansionAdapter() {

            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }

        });

        masterPage.registerSection(subComp);
    }

    /**
     * get the indicators from the data entities, which maybe sorted, and the order is changed.
     * 
     * @param dataEntities
     * @return
     */
    private List<Indicator> getIndicators(ChartDataEntity[] dataEntities) {
        List<Indicator> indicators = new ArrayList<Indicator>();

        for (ChartDataEntity entity : dataEntities) {
            indicators.add(entity.getIndicator());
        }
        return indicators;

    }

}
