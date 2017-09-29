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
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.TextStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.utils.format.StringFormatUtil;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class TextStatisticsState extends AbstractChartTypeStates {

    public TextStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    public Object getChart() {
        return getChart(getDataset());
    }

    @Override
    public Object getChart(Object dataset) {
        Object chart = TOPChartUtils.getInstance().createBarChart(
                DefaultMessagesImpl.getString("TextStatisticsState.TextStatistics"), dataset, false); //$NON-NLS-1$ 
        TOPChartUtils.getInstance().setDisplayDecimalFormatOfChart(chart);
        return chart;
    }

    public ICustomerDataset getCustomerDataset() {
        // sort these indicators.
        ComparatorsFactory.sort(units, ComparatorsFactory.TEXT_STATISTICS_COMPARATOR_ID);

        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (IndicatorUnit unit : units) {
            String value = CommonStateUtil.getUnitValue(unit.getValue(), StringFormatUtil.DOUBLE_NUMBER);
            String label = unit.getIndicatorName();
            customerdataset.addValue(Double.parseDouble(value), label, label);
            ChartDataEntity entity = CommonStateUtil.createDataEntity(unit, value, label);
            customerdataset.addDataEntity(entity);
        }
        return customerdataset;
    }

    public DataExplorer getDataExplorer() {
        return TextStatisticsStateUtil.getDataExplorer();
    }

    public Object getExampleChart() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getReferenceLink() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChart(org.talend.dataprofiler
     * .common.ui.editor.preview.ICustomerDataset)
     */
    public Object getChart(ICustomerDataset dataset) {
        // TODO Auto-generated method stub
        return null;
    }
}
