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
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.ModeStatisticsStateUtil;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class ModeStatisticsState extends AbstractChartTypeStates {

    public ModeStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    public Object getChart() {
        return null;
    }

    public ICustomerDataset getCustomerDataset() {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (IndicatorUnit unit : units) {
            ChartDataEntity entity = ModeStatisticsStateUtil.createDataEntity(unit, unit.getIndicatorName());
            customerdataset.addDataEntity(entity);
        }
        return customerdataset;
    }

    public DataExplorer getDataExplorer() {
        return null;
    }

    public Object getExampleChart() {
        return null;
    }

    public String getReferenceLink() {
        return null;
    }
}
