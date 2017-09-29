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
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.SimpleStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.utils.format.StringFormatUtil;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class SimpleRuleStatisticsChartState extends AbstractChartTypeStatesTable {

    public SimpleRuleStatisticsChartState(List<TableIndicatorUnit> units) {
        super(units);
    }

    @Override
    public Object getChart() {
        Object chart = TOPChartUtils.getInstance().createBarChart(
                DefaultMessagesImpl.getString("SimpleStatisticsStateTable.SimpleStatistics"), getDataset(), true); //$NON-NLS-1$
        TOPChartUtils.getInstance().decorateChart(chart, false);
        return chart;
    }

    public ICustomerDataset getCustomerDataset() {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (TableIndicatorUnit unit : units) {
            String value = CommonStateUtil.getUnitValue(unit.getValue(), StringFormatUtil.DOUBLE_NUMBER);
            String label = unit.getIndicatorName();
            customerdataset.addValue(Double.parseDouble(value), label, ""); //$NON-NLS-1$
            ChartDataEntity entity = CommonStateUtil.createDataEntity(unit, value, label);
            customerdataset.addDataEntity(entity);
        }
        return customerdataset;
    }

    public DataExplorer getDataExplorer() {
        return SimpleStatisticsStateUtil.getDataExplorer();
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
     * @see
     * org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChart(org.jfree.data.category
     * .CategoryDataset)
     */
    public Object getChart(Object dataset) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChartList(java.util.List)
     */
    public List<Object> getChartList(List<Object> datasets) {
        return null;
    }
}
