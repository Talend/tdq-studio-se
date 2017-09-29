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
package org.talend.dataprofiler.core.ui.editor.preview.model.states;

import java.util.List;

import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class SimpleTextStatisticsState extends TextStatisticsState {

    /**
     * DOC yyi SimpleTextStatisticsState constructor comment.
     * 
     * @param units
     */
    public SimpleTextStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    @Override
    public Object getChart() {
        return getChart(getDataset());
    }

    @Override
    public Object getChart(Object dataset) {
        Object chart = TOPChartUtils.getInstance().createBarChart(
                DefaultMessagesImpl.getString("SimpleTextStatisticsState.SimpleTextStatistics"), dataset, false);//$NON-NLS-1$
        TOPChartUtils.getInstance().setOrientation(chart, true);
        return chart;
    }

    @Override
    public ICustomerDataset getCustomerDataset() {

        // sort these indicators.
        ComparatorsFactory.sort(units, ComparatorsFactory.TEXT_STATISTICS_COMPARATOR_ID);

        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (IndicatorUnit unit : units) {
            double value = unit.getValue() != null ? Double.parseDouble(unit.getValue().toString()) : Double.NaN;
            String label = unit.getIndicatorName();
            String type = "";//$NON-NLS-1$
            switch (unit.getType()) {
            case MinLengthWithNullIndicatorEnum:
            case MinLengthWithBlankIndicatorEnum:
            case MinLengthWithBlankNullIndicatorEnum:
                type = "Minimal";//$NON-NLS-1$
                break;
            case MaxLengthWithBlankIndicatorEnum:
            case MaxLengthWithBlankNullIndicatorEnum:
            case MaxLengthWithNullIndicatorEnum:
                type = "Maximal";//$NON-NLS-1$
                break;
            case AverageLengthWithNullIndicatorEnum:
            case AverageLengthWithBlankIndicatorEnum:
            case AverageLengthWithNullBlankIndicatorEnum:
                type = "Average";//$NON-NLS-1$
                break;
            default:
                type = label;
                break;
            }

            customerdataset.addValue(value, type, label);

            ChartDataEntity entity = new ChartDataEntity();
            entity.setIndicator(unit.getIndicator());
            entity.setLabel(label);
            entity.setValue(String.valueOf(value));
            entity.setPercent(value / unit.getIndicator().getCount());

            customerdataset.addDataEntity(entity);
        }
        return customerdataset;
    }
}
