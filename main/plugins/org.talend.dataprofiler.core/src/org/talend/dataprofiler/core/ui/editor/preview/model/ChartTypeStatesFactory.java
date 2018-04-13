// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.editor.preview.model;

import java.util.List;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.ModeStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.PhoneNumbStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.PieStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.SimpleRuleStatisticsChartState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.SimpleStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.SimpleTextStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.SummaryStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.TextStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.UDIFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.UDIMatchStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.UDISimpleStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.WhereRuleStatisticsStateTable;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.BenfordLawFrequencyState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.BinFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.BinLowFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.DateFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.DateLowFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.DatePatternFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.EastAsiaPatternFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.EastAsiaPatternLowFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.FrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.LowFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.PatternFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.PatternLowFrequencyStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.SoundexFrequencyChartState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.SoundexLowFrequencyChartState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.REGEXPatternStatisticsState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.SQLPatternStatisticsState;
import org.talend.dq.indicators.preview.EIndicatorChartType;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public final class ChartTypeStatesFactory {

    private ChartTypeStatesFactory() {

    }

    public static IChartTypeStates getChartState(EIndicatorChartType type, List<IndicatorUnit> units) {
        switch (type) {
        case SIMPLE_STATISTICS:
            return new SimpleStatisticsState(units);

        case UDI_COUNT:
        case UDI_REALVALUE:
            return new UDISimpleStatisticsState(units);

        case TEXT_STATISTICS:
            return new TextStatisticsState(units);

        case MODE_INDICATOR:
            return new ModeStatisticsState(units);

        case FREQUENCE_STATISTICS:
        case CS_WORD_PATTERN_FREQUENCY_TABLE:
        case CI_WORD_PATTERN_FREQUENCY_TABLE:
            return new FrequencyStatisticsState(units);

        case UDI_FREQUENCY:
            return new UDIFrequencyStatisticsState(units);

        case LOW_FREQUENCE_STATISTICS:
        case CS_WORD_PATTERN_LOW_FREQUENCY_TABLE:
        case CI_WORD_PATTERN_LOW_FREQUENCY_TABLE:
            return new LowFrequencyStatisticsState(units);
            // MOD klliu 2010-08-06 bug 14695
        case BIN_FREQUENCE_STATISTICS:
            // MOD gdbu 2011-7-12 bug : 21803
            return new BinFrequencyStatisticsState(units);
            // ~21803 ;
        case BIN_LOW_FREQUENCE_STATISTICS:
            // MOD xwang 2011-08-15 bug TDQ-2594
            return new BinLowFrequencyStatisticsState(units);

        case PATTERN_FREQUENCE_STATISTICS:
            return new PatternFrequencyStatisticsState(units);

        case PATTERN_LOW_FREQUENCE_STATISTICS:
            return new PatternLowFrequencyStatisticsState(units);

        case EAST_ASIA_PATTERN_FREQUENCE_STATISTICS:
            return new EastAsiaPatternFrequencyStatisticsState(units);

        case EAST_ASIA_PATTERN_LOW_FREQUENCE_STATISTICS:
            return new EastAsiaPatternLowFrequencyStatisticsState(units);

        case DATE_PATTERN_FREQUENCE_STATISTICS:
            return new DatePatternFrequencyStatisticsState(units);

        case PATTERN_MATCHING:
            return new REGEXPatternStatisticsState(units);

        case UDI_MATCHING:
            return new UDIMatchStatisticsState(units);

        case SQL_PATTERN_MATCHING:
            return new SQLPatternStatisticsState(units);

        case SUMMARY_STATISTICS:
            return new SummaryStatisticsState(units);
            // MOD mzhao 2009-03-23, Soundex frequency.
        case SOUNDEX_FREQUENCY_TABLE:
            return new SoundexFrequencyChartState(units);
        case SOUNDEX_LOW_FREQUENCY_TABLE:
            return new SoundexLowFrequencyChartState(units);

        case DATE_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsState(units, EIndicatorChartType.DATE_FREQUENCE_STATISTICS);
        case WEEK_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsState(units, EIndicatorChartType.WEEK_FREQUENCE_STATISTICS);
        case MONTH_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsState(units, EIndicatorChartType.MONTH_FREQUENCE_STATISTICS);
        case QUARTER_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsState(units, EIndicatorChartType.QUARTER_FREQUENCE_STATISTICS);
        case YEAR_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsState(units, EIndicatorChartType.YEAR_FREQUENCE_STATISTICS);
        case DATE_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsState(units, EIndicatorChartType.DATE_LOW_FREQUENCE_STATISTICS);
        case WEEK_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsState(units, EIndicatorChartType.WEEK_LOW_FREQUENCE_STATISTICS);
        case MONTH_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsState(units, EIndicatorChartType.MONTH_LOW_FREQUENCE_STATISTICS);
        case QUARTER_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsState(units, EIndicatorChartType.QUARTER_LOW_FREQUENCE_STATISTICS);
        case YEAR_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsState(units, EIndicatorChartType.YEAR_LOW_FREQUENCE_STATISTICS);
        case SIMPLE_TEXT_STATISTICS:
            return new SimpleTextStatisticsState(units);
        case PHONE_NUMBER_STATISTICS:
            return new PhoneNumbStatisticsState(units);
        case FORMAT_FREQ_PIE_STATISTICS:
            return new PieStatisticsState(units, DefaultMessagesImpl.getString("PieStatisticsState.Title"));
        case BENFORD_LAW_STATISTICS:
            return new BenfordLawFrequencyState(units);
        default:
            return null;
        }
    }

    public static IChartTypeStates getChartStateOfTableAna(EIndicatorChartType type, List<TableIndicatorUnit> units,
            TableIndicator tableIndicator) {
        switch (type) {
        case WHERERULE_INDICATOR:
            return new WhereRuleStatisticsStateTable(units, tableIndicator);
        case SIMPLE_STATISTICS:
            return new SimpleRuleStatisticsChartState(units);

        default:
            return null;
        }
    }
}
