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
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.FrequencyTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.FrequencyTypeStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class LowFrequencyStatisticsTableState extends FrequencyTableState {

    /**
     * DOC yyin LowFrequencyStatisticsTableState constructor comment.
     * 
     * @param units
     */
    public LowFrequencyStatisticsTableState(List<IndicatorUnit> units) {
        super(units);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.preview.model.states.table.FrequencyTableState#sortIndicator(org.talend
     * .dq.indicators.ext.FrequencyExt[])
     */
    @Override
    protected void sortIndicator(FrequencyExt[] frequencyExt) {
        ComparatorsFactory.sort(frequencyExt, ComparatorsFactory.LOW_FREQUENCY_COMPARATOR_ID);
    }

    @Override
    public DataExplorer getDataExplorer() {
        return FrequencyTypeStateUtil.getDataExplorer();
    }
}
