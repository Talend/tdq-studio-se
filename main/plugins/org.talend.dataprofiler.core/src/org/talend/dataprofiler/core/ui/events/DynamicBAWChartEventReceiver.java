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
package org.talend.dataprofiler.core.ui.events;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.dataset.CustomerDefaultBAWDataset;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.SummaryStatisticsState;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.IndicatorCommonUtil;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * For Summary statistics indicators: when select all
 */
public class DynamicBAWChartEventReceiver extends DynamicChartEventReceiver {

    private CustomerDefaultBAWDataset bawDataset;

    private Object BAWparentComposite = null;

    private List<IndicatorUnit> indicators = new ArrayList<IndicatorUnit>();

    private DynamicChartEventReceiver IRQIndicatorEvent = null;

    private Indicator IRQIndicator = null;

    private DynamicChartEventReceiver rangeIndicatorEvent = null;

    private Indicator rangeIndicator = null;

    Map<IndicatorEnum, Double> summaryValues = new HashMap<IndicatorEnum, Double>();

    public DynamicChartEventReceiver createEventReceiver(IndicatorEnum type, Indicator oneIndicator) {
        // the receiver for each summary indicator, just need to put the indicator name and its related value into the
        // map, and call the method which judge the total
        DynamicChartEventReceiver eReceiver = new DynamicChartEventReceiver() {

            @Override
            public boolean handle(Object value) {
                super.handle(value);
                addToSummaryMap(getIndicatorType(), value);
                if (isIntact()) {
                    if (this.getTableViewer() != null) {
                        String str = value == null ? String.valueOf(Double.NaN) : String.valueOf(value);
                        this.refreshTable(str);
                        if (isLastIndicator()) {
                            // when current is last one indicator which Summary statistics indicators then update IQRIndicator and
                            // RangeIndicator
                            updateValueOfIRQAndRange();
                        }
                    }
                } else {
                    updateValueOfIRQAndRange();
                }
                return true;
            }
        };

        eReceiver.setIndicatorType(type);
        eReceiver.setIndicatorName(oneIndicator.getName());
        indicators.add(new ColumnIndicatorUnit(type, oneIndicator, null));
        
        if (IndicatorEnum.IQRIndicatorEnum.equals(type)) {
            this.IRQIndicator = oneIndicator;
            this.IRQIndicatorEvent = eReceiver;
        } else if (IndicatorEnum.RangeIndicatorEnum.equals(type)) {
            this.rangeIndicator = oneIndicator;
            this.rangeIndicatorEvent = eReceiver;
        }
        return eReceiver;
    }

    private void addToSummaryMap(IndicatorEnum indicatorType, Object value) {
        Object indValue = value;
        if (value == null || "null".equals(value)) { //$NON-NLS-1$
            indValue = 0;
        }
        try {
            summaryValues.put(indicatorType, Double.parseDouble(String.valueOf(indValue)));
        } catch (NumberFormatException e) {
            // nothing to do, when the value is a date, will throw NumberFormatException.
        }
        refreshChart();
    }

    private boolean isIntact() {
        return indicators.size() == SummaryStatisticsState.FULL_FLAG;
    }

    @Override
    public void refreshChart() {
        Map<IndicatorUnit, String> indicators2ValueMap = converIndicatorListToMap();
        SummaryStatisticsState state = new SummaryStatisticsState(indicators, indicators2ValueMap);
        if (!isLastIndicator()) {
            state.setSupportDynamicChart(true);
        }
        state.setSqltype(Types.DOUBLE);
        Object chart = state.getChart();
        TOPChartUtils.getInstance().decorateChart(chart, false);
        if (BAWparentComposite != null) {
            TOPChartUtils.getInstance().refrechChart(BAWparentComposite, chart);
        }
        EventManager.getInstance().publish(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART, null);
    }

    /**
     * DOC zshen Comment method "copyIndicatorUnit".
     * 
     * @param indicators2
     * @return
     */
    private Map<IndicatorUnit, String> converIndicatorListToMap() {
        Map<IndicatorUnit, String> returnIndicators = new HashMap<IndicatorUnit, String>();
        for (IndicatorUnit indUnit : indicators) {
            if (summaryValues.containsKey(indUnit.getType())) {
                returnIndicators.put(indUnit, indUnit.getValue() == null ? "0.0" : String.valueOf(indUnit.getValue())); //$NON-NLS-1$
            } else {
                returnIndicators.put(indUnit, "0.0"); //$NON-NLS-1$
            }
        }
        return returnIndicators;
    }

    /**
     * DOC yyin Comment method "updateValueOfIRQAndRange".
     */
    private void updateValueOfIRQAndRange() {
        if (this.getTableViewer() != null) {
            if (IRQIndicator != null) {
                Object indicatorValue = IndicatorCommonUtil.getIndicatorValue(IRQIndicator);
                String str = indicatorValue == null ? String.valueOf(Double.NaN) : String.valueOf(indicatorValue);
                IRQIndicatorEvent.refreshTable(str);
            }
            if (rangeIndicator != null) {
                Object indicatorValue = IndicatorCommonUtil.getIndicatorValue(rangeIndicator);
                String str = indicatorValue == null ? String.valueOf(Double.NaN) : String.valueOf(indicatorValue);
                rangeIndicatorEvent.refreshTable(str);
            }
        }
    }

    @Override
    public boolean handle(Object value) {
        return true;
    }

    @Override
    public void clearValue() {
        if (bawDataset != null && bawDataset.getRowCount() == 7) {
            bawDataset.clear();
        }
    }

    public CustomerDefaultBAWDataset getBawDataset() {
        return bawDataset;
    }

    public void setBawDataset(CustomerDefaultBAWDataset bawDataset) {
        this.bawDataset = bawDataset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver#clear()
     */
    @Override
    public void clear() {
        super.clear();
        // reset the values
        summaryValues.clear();
        indicators.clear();
        BAWparentComposite = null;
        bawDataset = null;
    }

    /**
     * Sets the bAWparentComposite.
     * 
     * @param bAWparentComposite the bAWparentComposite to set
     */
    public void setBAWparentComposite(Object bAWparentComposite) {
        this.BAWparentComposite = bAWparentComposite;
    }

    private boolean isLastIndicator() {
        return summaryValues.size() == SummaryStatisticsState.FULL_CHART;
    }


}
