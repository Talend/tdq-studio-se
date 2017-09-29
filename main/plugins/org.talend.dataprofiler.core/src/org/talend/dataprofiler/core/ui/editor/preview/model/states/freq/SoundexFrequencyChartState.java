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
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util.SoundexFrequencyStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;

/**
 * 
 * DOC mzhao 2009-03-23 Soundex Fequency state.
 */
public class SoundexFrequencyChartState extends FrequencyTypeStates {

    public SoundexFrequencyChartState(List<IndicatorUnit> units) {
        super(units);
    }

    public DataExplorer getDataExplorer() {
        return SoundexFrequencyStateUtil.getDataExplorer();
    }

    @Override
    protected void sortIndicator(FrequencyExt[] frequencyExt) {
        ComparatorsFactory.sort(frequencyExt, ComparatorsFactory.SOUNDEX_FREQUENCY_COMPARATOR_ID);
    }

    @Override
    protected String getTitle() {
        return DefaultMessagesImpl.getString("FrequencyTypeStates.SoundexFreqyebctStatistics"); //$NON-NLS-1$
    }

    @Override
    public Object getChart() {
        return getChart(getDataset());
    }

    @Override
    public Object getChart(Object dataset) {
        return TOPChartUtils.getInstance().createBarChartByKCD(DefaultMessagesImpl.getString("TopChartFactory.count"), dataset); //$NON-NLS-1$
    }

}
