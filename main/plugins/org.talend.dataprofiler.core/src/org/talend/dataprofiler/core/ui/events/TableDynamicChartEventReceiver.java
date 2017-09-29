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

import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.WhereRuleStatisticsStateTable;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.WhereRuleStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.WhereRuleChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * created by yyin on 2014-7-9 Detailled comment
 * 
 */
public class TableDynamicChartEventReceiver extends DynamicChartEventReceiver {

    @Override
    public boolean handle(Object value) {
        if (value == null) {
            return false;
        }
        if (IndicatorEnum.RowCountIndicatorEnum.equals(this.getIndicatorType())) {
            super.handle(value);
        } else {
            Long count = getIndicator().getCount();
            double valueMatch = WhereRuleStatisticsStateUtil.getMatchValue(value);
            double valueNotmatch = WhereRuleStatisticsStateUtil.getNotMatchValue(value, valueMatch, count);
            if (dataset != null) {
                TOPChartUtils.getInstance().setValue(dataset, valueNotmatch, WhereRuleStatisticsStateTable.ROW_KEY_NOT_PASS,
                        indicatorName);
                TOPChartUtils.getInstance().setValue(dataset, valueMatch, WhereRuleStatisticsStateTable.ROW_KEY_PASS,
                        indicatorName);
            }
            if (tableViewer != null) {
                TableWithData input = (TableWithData) tableViewer.getInput();
                if (input != null) {
                    ChartDataEntity[] dataEntities = input.getEnity();

                    ((WhereRuleChartDataEntity) dataEntities[this.getEntityIndex()]).setNumMatch(String.valueOf(valueMatch));
                    ((WhereRuleChartDataEntity) dataEntities[this.getEntityIndex()]).setNumNoMatch(String.valueOf(valueNotmatch));

                    tableViewer.getTable().clearAll();
                    tableViewer.setInput(input);
                }
            }
        }
        return true;
    }

    @Override
    public void clearValue() {
        if (IndicatorEnum.RowCountIndicatorEnum.equals(this.getIndicatorType())) {
            super.clearValue();
        } else {// clear the data before running.
            if (dataset != null) {
                TOPChartUtils.getInstance().setValue(dataset, 0.0, WhereRuleStatisticsStateTable.ROW_KEY_NOT_PASS, indicatorName);
                TOPChartUtils.getInstance().setValue(dataset, 0.0, WhereRuleStatisticsStateTable.ROW_KEY_PASS, indicatorName);
            }
            if (tableViewer != null) {
                TableWithData input = (TableWithData) tableViewer.getInput();
                if (input != null) {
                    ChartDataEntity[] dataEntities = input.getEnity();

                    ((WhereRuleChartDataEntity) dataEntities[this.getEntityIndex()]).setNumMatch(NAN_STRING);
                    ((WhereRuleChartDataEntity) dataEntities[this.getEntityIndex()]).setNumNoMatch(NAN_STRING);

                    tableViewer.getTable().clearAll();
                    tableViewer.setInput(input);
                }
            }
        }
    }

    // every indicator need to be remembered in table analysis
    @Override
    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }
}
