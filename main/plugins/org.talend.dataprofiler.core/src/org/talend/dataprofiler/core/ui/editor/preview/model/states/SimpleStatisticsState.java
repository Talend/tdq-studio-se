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
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.SimpleStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class SimpleStatisticsState extends AbstractChartTypeStates {

    public SimpleStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    public Object getChart() {
        return TOPChartUtils.getInstance().createBarChart(
                DefaultMessagesImpl.getString("SimpleStatisticsState.SimpleStatistics"), getDataset(), false); //$NON-NLS-1$
    }

    @Override
    public Object getChart(Object dataset) {
        return TOPChartUtils.getInstance().createBarChartWithDefaultDataset(
                DefaultMessagesImpl.getString("SimpleStatisticsState.SimpleStatistics"), dataset, false); //$NON-NLS-1$
    }

    public ICustomerDataset getCustomerDataset() {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (IndicatorUnit unit : units) {
            String value = CommonStateUtil.getUnitValue(unit.getValue(), 0);
            String label = unit.getIndicatorName();
            customerdataset.addValue(Double.parseDouble(value), label, label);
            ChartDataEntity entity = CommonStateUtil.createDataEntity(unit, value, label);
            customerdataset.addDataEntity(entity);
        }
        return customerdataset;
    }

    public Object getExampleChart() {
        return null;
    }

    public String getReferenceLink() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getDataExplorer()
     */
    public DataExplorer getDataExplorer() {
        return SimpleStatisticsStateUtil.getDataExplorer();
    }
}
