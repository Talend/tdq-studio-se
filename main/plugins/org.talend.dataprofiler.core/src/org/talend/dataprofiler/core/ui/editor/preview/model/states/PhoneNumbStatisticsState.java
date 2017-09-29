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
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.PhoneNumbStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.utils.format.StringFormatUtil;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class PhoneNumbStatisticsState extends AbstractChartTypeStates {

    /**
     * DOC qiongli PhoneNumbStatisticsState constructor comment.
     * 
     * @param units
     */
    public PhoneNumbStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getCustomerDataset()
     */
    public ICustomerDataset getCustomerDataset() {

        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (IndicatorUnit unit : units) {
            String value = CommonStateUtil.getUnitValue(unit.getValue(), StringFormatUtil.DOUBLE_NUMBER);
            String label = unit.getIndicatorName();

            customerdataset.addValue(Double.parseDouble(value), label, label);

            ChartDataEntity entity = PhoneNumbStatisticsStateUtil.createDataEntity(unit.getIndicator(), value, label);

            customerdataset.addDataEntity(entity);
        }
        return customerdataset;

    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChart()
     */
    public Object getChart() {
        return TOPChartUtils.getInstance().createBarChart(
                DefaultMessagesImpl.getString("PhoneNumbStatisticsState.PhoneNumbStatistics"), getDataset(), false); //$NON-NLS-1$
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getExampleChart()
     */
    public Object getExampleChart() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getDataExplorer()
     */
    public DataExplorer getDataExplorer() {
        return PhoneNumbStatisticsStateUtil.getDataExplorer();
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getReferenceLink()
     */
    public String getReferenceLink() {
        // TODO Auto-generated method stub
        return null;
    }

}
