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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern;

import java.util.List;

import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.AbstractChartTypeStates;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.PatternMatchingExt;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class PatternStatisticsState extends AbstractChartTypeStates {

    public PatternStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    public Object getChart() {
        return getChart(getDataset());
    }

    @Override
    public Object getChart(Object dataset) {
        return TOPChartUtils.getInstance().createStackedBarChart(
                DefaultMessagesImpl.getString("PatternStatisticsState.PatternStatistics"), dataset, false, true); //$NON-NLS-1$
    }

    public ICustomerDataset getCustomerDataset() {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (IndicatorUnit unit : units) {
            String label = unit.getIndicator().getName();
            PatternMatchingExt patternExt = PatternStatisticeStateUtil.getUnitValue(unit.getIndicator(), unit.getValue());
            String notMathCount = PatternStatisticeStateUtil.getNotMatchCount(patternExt);
            String machCount = PatternStatisticeStateUtil.getMatchCount(patternExt);

            customerdataset.addValue(Double.parseDouble(notMathCount),
                    DefaultMessagesImpl.getString("PatternStatisticsState.NotMatching"), label); //$NON-NLS-1$
            customerdataset.addValue(Double.parseDouble(machCount),
                    DefaultMessagesImpl.getString("PatternStatisticsState.Matching"), label); //$NON-NLS-1$

            PatternChartDataEntity patternEntity = PatternStatisticeStateUtil.createDataEntity(unit, label, notMathCount,
                    machCount);

            customerdataset.addDataEntity(patternEntity);
        }

        return customerdataset;
    }

    public DataExplorer getDataExplorer() {
        return PatternStatisticeStateUtil.getDataExplorer();
    }

    public Object getExampleChart() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getReferenceLink() {
        return null;
    }
}
