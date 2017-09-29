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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq;

import java.util.List;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.FunctionFrequencyStatExplorer;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class DatePatternFrequencyStatisticsState extends FrequencyStatisticsState {

    /**
     * DOC zshen DatePatternFrequencyStatisticsState constructor comment.
     * 
     * @param units
     */
    public DatePatternFrequencyStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    @Override
    protected String getTitle() {
        return DefaultMessagesImpl.getString("FrequencyTypeStates.DatePatternFreqyebctStatistics"); //$NON-NLS-1$
    }

    @Override
    public DataExplorer getDataExplorer() {
        return new FunctionFrequencyStatExplorer();
    }

}
