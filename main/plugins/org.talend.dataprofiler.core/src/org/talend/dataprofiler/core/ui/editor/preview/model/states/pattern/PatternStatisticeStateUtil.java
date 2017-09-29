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

import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.RegexPatternExplorer;
import org.talend.dq.indicators.IndicatorCommonUtil;
import org.talend.dq.indicators.ext.PatternMatchingExt;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;

/**
 * created by yyin on 2014-12-3 Detailled comment
 * 
 */
public class PatternStatisticeStateUtil {

    /**
     * DOC yyin Comment method "createDataEntity".
     * 
     * @param unit
     * @param label
     * @param notMathCount
     * @param machCount
     * @return
     */
    public static PatternChartDataEntity createDataEntity(IndicatorUnit unit, String label, String notMathCount, String machCount) {
        PatternChartDataEntity patternEntity = new PatternChartDataEntity();
        patternEntity.setIndicator(unit.getIndicator());
        patternEntity.setLabel(label);
        patternEntity.setNumMatch(machCount);
        patternEntity.setNumNoMatch(notMathCount);
        return patternEntity;
    }

    /**
     * DOC yyin Comment method "getMatchCount".
     * 
     * @param patternExt
     * @return
     */
    public static String getMatchCount(PatternMatchingExt patternExt) {
        return CommonStateUtil.getUnitValue(patternExt.getMatchingValueCount(), 0);
    }

    /**
     * DOC yyin Comment method "getNotMatchCount".
     * 
     * @param patternExt
     * @return
     */
    public static String getNotMatchCount(PatternMatchingExt patternExt) {
        return CommonStateUtil.getUnitValue(patternExt.getNotMatchingValueCount(), 0);
    }

    public static DataExplorer getDataExplorer() {
        return new RegexPatternExplorer();
    }

    /**
     * 
     * Convert unitValue when indicator is not computed
     * 
     * @param indicator
     * @param unitValue
     * @return Instance of PatternMatchingExt which contain the result of currnt matching indicator
     */
    public static PatternMatchingExt getUnitValue(Indicator indicator, Object unitValue) {
        if (indicator.isComputed() && unitValue != null && unitValue instanceof PatternMatchingExt) {
            return (PatternMatchingExt) unitValue;
        }
        return IndicatorCommonUtil.handleMatchingValue(indicator);
    }
}
