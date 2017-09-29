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
package org.talend.dataprofiler.core.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * this class is used to get IndicatorEnum when use the create analysis wizard(both from right click menu and normal
 * way) to create analysis.
 */
public final class IndicatorEnumUtils {

    public static IndicatorEnum[] getForDiscreteDataAnalysis() {
        IndicatorEnum[] allowedEnumes = new IndicatorEnum[2];
        allowedEnumes[0] = IndicatorEnum.CountsIndicatorEnum;
        allowedEnumes[1] = IndicatorEnum.BinFrequencyIndicatorEnum;
        return allowedEnumes;
    }

    public static IndicatorEnum[] getForSummaryAnalysis() {
        IndicatorEnum[] allowedEnumes = new IndicatorEnum[3];
        allowedEnumes[0] = IndicatorEnum.BoxIIndicatorEnum;
        allowedEnumes[1] = IndicatorEnum.RowCountIndicatorEnum;
        allowedEnumes[2] = IndicatorEnum.NullCountIndicatorEnum;
        return allowedEnumes;
    }

    public static IndicatorEnum[] getForPatternFrequencyAnalysis() {
        IndicatorEnum[] allowedEnumes = new IndicatorEnum[4];
        allowedEnumes[0] = IndicatorEnum.RowCountIndicatorEnum;
        allowedEnumes[1] = IndicatorEnum.NullCountIndicatorEnum;
        allowedEnumes[2] = IndicatorEnum.PatternFreqIndicatorEnum;
        allowedEnumes[3] = IndicatorEnum.PatternLowFreqIndicatorEnum;
        return allowedEnumes;
    }

    public static IndicatorEnum[] getForNominalValuesAnalysis(boolean addTextIndicator) {
        // MOD qiongli 2011-3-31,bug 19810.if contain none-nominal data,don't add TextIndicator.
        List<IndicatorEnum> allwedEnumeLs = new ArrayList<IndicatorEnum>();
        allwedEnumeLs.add(IndicatorEnum.CountsIndicatorEnum);
        allwedEnumeLs.add(IndicatorEnum.FrequencyIndicatorEnum);
        if (addTextIndicator) {
            allwedEnumeLs.add(IndicatorEnum.TextIndicatorEnum);
        }
        return allwedEnumeLs.toArray(new IndicatorEnum[allwedEnumeLs.size()]);
    }

    public static IndicatorEnum[] getForSimpleAnalysis() {
        IndicatorEnum[] allowedEnumes = new IndicatorEnum[1];
        allowedEnumes[0] = IndicatorEnum.CountsIndicatorEnum;
        return allowedEnumes;
    }

    public static IndicatorEnum[] getForColumnAnalysis() {
        IndicatorEnum[] allowedEnumes = new IndicatorEnum[1];
        allowedEnumes[0] = IndicatorEnum.CountsIndicatorEnum;
        return allowedEnumes;
    }

    public static IndicatorEnum[] getForTableAnalysis() {
        IndicatorEnum[] allowedEnumes = new IndicatorEnum[1];
        allowedEnumes[0] = IndicatorEnum.WhereRuleIndicatorEnum;
        return allowedEnumes;
    }

}
