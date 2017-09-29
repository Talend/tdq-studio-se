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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.utils;

import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.PieStatisticsExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class PieStatisticsStateUtil {

    public static DataExplorer getDataExplorer() {
        return new PieStatisticsExplorer();
    }

    /**
     * DOC yyin Comment method "getNumberOfShown".
     * 
     * @param unit
     * @param frequencyExt
     * @return
     */
    public static int getNumberOfShown(IndicatorUnit unit, FrequencyExt[] frequencyExt) {
        int numOfShown = frequencyExt.length;
        IndicatorParameters parameters = unit.getIndicator().getParameters();
        if (parameters != null) {
            if (parameters.getTopN() < numOfShown) {
                numOfShown = parameters.getTopN();
            }
        }
        return numOfShown;
    }

    /**
     * DOC yyin Comment method "getkeyLabel".
     * 
     * @param freqExt
     * @return
     */
    public static String getkeyLabel(FrequencyExt freqExt) {
        String keyLabel = String.valueOf(freqExt.getKey());
        if ("null".equals(keyLabel)) { //$NON-NLS-1$
            keyLabel = SpecialValueDisplay.NULL_FIELD;
        }
        if (PluginConstant.EMPTY_STRING.equals(keyLabel)) {
            keyLabel = SpecialValueDisplay.EMPTY_FIELD;
        }
        return keyLabel;
    }

    /**
     * DOC yyin Comment method "createDataEntity".
     * 
     * @param unit
     * @param freqExt
     * @param keyLabel
     * @return
     */
    public static ChartDataEntity createDataEntity(IndicatorUnit unit, FrequencyExt freqExt, String keyLabel) {
        ChartDataEntity entity = new ChartDataEntity();
        entity.setIndicator(unit.getIndicator());
        entity.setKey(freqExt.getKey());
        entity.setLabelNull(freqExt.getKey() == null);
        entity.setLabel(keyLabel);
        entity.setValue(String.valueOf(freqExt.getValue()));

        Double percent = freqExt.getFrequency();
        entity.setPercent(percent);
        return entity;
    }

}
