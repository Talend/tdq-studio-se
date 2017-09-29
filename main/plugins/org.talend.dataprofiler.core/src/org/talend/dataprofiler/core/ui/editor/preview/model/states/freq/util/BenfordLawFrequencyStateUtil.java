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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util;

import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dq.analysis.explore.BenfordLawFrequencyExplorer;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class BenfordLawFrequencyStateUtil {

    public static void sortIndicator(FrequencyExt[] frequencyExt) {
        ComparatorsFactory.sort(frequencyExt, ComparatorsFactory.BENFORDLAW_FREQUENCY_COMPARATOR_ID);
        recomputerForBenfordLaw(frequencyExt);
    }

    public static FrequencyExt[] recomputerForBenfordLaw(FrequencyExt[] frequencyExt) {
        FrequencyExt[] tempFreq = frequencyExt;
        // get the sum
        double sum = 0d;
        for (FrequencyExt ext : frequencyExt) {
            sum += ext.getValue();
        }
        // set the values from count to percentage
        for (FrequencyExt ext : tempFreq) {
            ext.setFrequency(ext.getValue() / sum);
        }
        return tempFreq;
    }

    public static DataExplorer getDataExplorer() {
        return new BenfordLawFrequencyExplorer();
    }

}
