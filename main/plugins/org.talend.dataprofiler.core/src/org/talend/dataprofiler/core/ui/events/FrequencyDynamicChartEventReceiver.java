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
package org.talend.dataprofiler.core.ui.events;

import java.util.ArrayList;
import java.util.List;

import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.FrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.FrequencyTypeStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.ModeIndicator;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * created by yyin on 2014-7-11 Detailled comment
 * 
 */
public class FrequencyDynamicChartEventReceiver extends DynamicChartEventReceiver {

    @Override
    public boolean handle(Object value) {
        Object indValue = value;
        if (value == null) {
            indValue = 0;
        }
        if (dataset != null) {
            // no sort needed here
            if (indValue instanceof FrequencyExt[]) {
                FrequencyExt[] frequencyExt = (FrequencyExt[]) indValue;
                setFrequecyToDataset(dataset, frequencyExt, indicator);
            }
        }
        if (tableViewer != null && dataset != null) {
            TableWithData input = (TableWithData) tableViewer.getInput();
            if (input != null) {
                if (this.indicator instanceof ModeIndicator) {
                    ChartDataEntity entity = new ChartDataEntity();
                    entity.setIndicator(indicator);
                    entity.setLabel(this.indicatorName);
                    entity.setValue(String.valueOf(indValue));
                    // mode indicator has not a chart so that no dataset too
                    input.setEntities(new ChartDataEntity[] { entity });
                } else {
                    ICustomerDataset customerDataset = TOPChartUtils.getInstance().getCustomerDataset(dataset);
                    if (customerDataset != null) {
                        input.setEntities((customerDataset).getDataEntities());
                    } else {
                        input.setEntities(((ICustomerDataset) dataset).getDataEntities());
                    }
                }
            }
            if (!tableViewer.getTable().isDisposed()) {
                tableViewer.getTable().clearAll();
                tableViewer.setInput(input);
            }
        }

        if (registerChart != TOPChartUtils.getInstance().getChartFromChartComposite(parentChartComposite)) {
            restoreChart();
        }

        // need to refresh the parent composite of the chart to show the changes
        if (!(indicator instanceof ModeIndicator)) {
            EventManager.getInstance().publish(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART, null);
        }
        return true;
    }

    private void setFrequecyToDataset(Object customerdataset, FrequencyExt[] frequencyExt, Indicator indicator) {

        int numOfShown = frequencyExt.length;
        IndicatorParameters parameters = indicator.getParameters();
        if (parameters != null) {
            if (parameters.getTopN() < numOfShown) {
                numOfShown = parameters.getTopN();
            }
        }
        FrequencyExt[] tempFreq = handleFrequency(frequencyExt);
        clearDataEntity(customerdataset);

        // Added TDQ-12870
        if (tempFreq != null && tempFreq.length > 0) {
            FrequencyExt freqE = tempFreq[0];
            List columnKeys = TOPChartUtils.getInstance().getColumnKeys(customerdataset);
            if (!columnKeys.contains(String.valueOf(freqE.getKey()))) {
                // columnKeys.clear();
                TOPChartUtils.getInstance().clearDataset(customerdataset);
                ICustomerDataset customerDataset = TOPChartUtils.getInstance().getCustomerDataset(customerdataset);
                if (customerDataset != null && customerDataset instanceof CustomerDefaultCategoryDataset) {
                    ((CustomerDefaultCategoryDataset) customerDataset).clearAll();
                }
            }
            for (int i = 0; i < numOfShown; i++) {
                FrequencyExt freqExt = tempFreq[i];
                String keyLabel = String.valueOf(freqExt.getKey());
                if ("null".equals(keyLabel)) { //$NON-NLS-1$
                    keyLabel = SpecialValueDisplay.NULL_FIELD;
                } else if ("".equals(keyLabel)) { //$NON-NLS-1$
                    keyLabel = SpecialValueDisplay.EMPTY_FIELD;
                }

                ChartDataEntity entity = FrequencyTypeStateUtil.createChartEntity(indicator, freqExt, keyLabel, true);

                if (customerdataset instanceof CustomerDefaultCategoryDataset) {
                    ((CustomerDefaultCategoryDataset) customerdataset).addDataEntity(entity);
                    addValueToDataset(((CustomerDefaultCategoryDataset) customerdataset).getDataset(), freqExt,
                            keyLabel);
                } else {
                    ICustomerDataset customerDataset = TOPChartUtils.getInstance().getCustomerDataset(customerdataset);
                    if (customerDataset != null && customerDataset instanceof CustomerDefaultCategoryDataset) {
                        customerDataset.addDataEntity(entity);

                        addValueToDataset(((CustomerDefaultCategoryDataset) customerDataset).getDataset(), freqExt,
                                keyLabel);
                    }
                }
            }
        }

    }

    /**
     * Clear DataEntity becuase of there maybe have a empty string when init it.
     * 
     * @param customerdataset
     */
    private void clearDataEntity(Object customerdataset) {
        if (customerdataset instanceof CustomerDefaultCategoryDataset) {
            ((CustomerDefaultCategoryDataset) customerdataset).clearDataEnities();
        } else {
            ICustomerDataset customerDataset = TOPChartUtils.getInstance().getCustomerDataset(customerdataset);
            if (customerDataset != null && customerDataset instanceof CustomerDefaultCategoryDataset) {
                ((CustomerDefaultCategoryDataset) customerDataset).clearDataEnities();
            }
        }

    }

    private void restoreChart() {
        if (registerChart == null) {
            return;
        }

        if (this.parentChartComposite != null) {
            TOPChartUtils.getInstance().refrechChart(this.parentChartComposite, registerChart);
        }

        EventManager.getInstance().publish(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART, null);

    }

    @Override
    public void refreshChart() {
        List<IndicatorUnit> indicatorUnits = new ArrayList<IndicatorUnit>();
        indicatorUnits.add(new ColumnIndicatorUnit(IndicatorEnum.findIndicatorEnum(this.getIndicator().eClass()), this
                .getIndicator(), null));
        // indicators
        FrequencyStatisticsState state = new FrequencyStatisticsState(indicatorUnits);
        state.setSupportDynamicChart(true);
        Object chart = state.getChart();
        TOPChartUtils.getInstance().decorateChart(chart, false);
        if (this.parentChartComposite != null) {
            TOPChartUtils.getInstance().refrechChart(this.parentChartComposite, chart);
        }

        EventManager.getInstance().publish(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART, null);

    }

    @Override
    public void clearValue() {
        if (dataset != null) {// clear old data
            if (dataset instanceof CustomerDefaultCategoryDataset) {
                ((CustomerDefaultCategoryDataset) dataset).clearAll();
            } else {
                TOPChartUtils.getInstance().clearDataset(dataset);
            }
        }
        if (tableViewer != null) {
            TableWithData input = (TableWithData) tableViewer.getInput();
            input.setEntities(null);
            tableViewer.getTable().clearAll();
        }
    }

    /**
     * DOC yyin Comment method "handleFrequency".
     * 
     * @param frequencyExt
     * @param indicator
     * @return
     */
    protected FrequencyExt[] handleFrequency(FrequencyExt[] frequencyExt) {
        return frequencyExt;
    }

    /**
     * DOC yyin Comment method "addValueToDataset".
     * 
     * @param customerdataset
     * @param indicator
     * @param freqExt
     * @param keyLabel
     */
    protected void addValueToDataset(Object customerdataset, FrequencyExt freqExt, String keyLabel) {
        TOPChartUtils.getInstance().addValueToCategoryDataset(customerdataset, freqExt.getValue(), "1", keyLabel); //$NON-NLS-1$
    }
}
