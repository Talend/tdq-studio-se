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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table;

import java.util.List;

import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util.DateFrequencyStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class DateFrequencyStatisticsTableState extends FrequencyStatisticsTableState {

    public DateFrequencyStatisticsTableState(List<IndicatorUnit> units) {
        super(units);
    }

    @Override
    public ChartDataEntity[] getDataEntity() {
        return DateFrequencyStateUtil.getDataEntity(units, ComparatorsFactory.FREQUENCY_COMPARATOR_ID);
    }
}
