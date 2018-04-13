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

import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.BenfordLawFrequencyTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.BinFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.BinLowFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.DateFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.DateLowFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.DatePatternFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.EastAsiaPatternFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.EastAsiaPatternLowFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.FrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.LowFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.PatternFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.PatternLowFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.SoundexFrequencyTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.SoundexLowFrequencyTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.table.UDIFrequencyStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.REGEXPatternStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.pattern.SQLPatternStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.ITableTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.ModeStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.PhoneNumbStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.PieStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.SimpleRuleStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.SimpleStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.SimpleTextStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.SummaryStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.TextStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.UDIMatchStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.UDISimpleStatisticsTableState;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.WhereRuleStatisticsTableState;
import org.talend.dq.indicators.preview.EIndicatorChartType;

/**
 * created by yyin on 2014-12-4 Detailled comment
 * 
 */
public class TableTypeStatesFactory {

    private static TableTypeStatesFactory instance;

    private TableTypeStatesFactory() {

    }

    public static TableTypeStatesFactory getInstance() {
        if (instance == null) {
            instance = new TableTypeStatesFactory();
        }
        return instance;
    }

    public ITableTypeStates getTableState(EIndicatorChartType type, List<IndicatorUnit> units) {
        switch (type) {
        case SIMPLE_STATISTICS:
            return new SimpleStatisticsTableState(units);

        case UDI_COUNT:
        case UDI_REALVALUE:
            return new UDISimpleStatisticsTableState(units);

        case TEXT_STATISTICS:
            return new TextStatisticsTableState(units);

        case MODE_INDICATOR:
            return new ModeStatisticsTableState(units);

        case FREQUENCE_STATISTICS:
        case CS_WORD_PATTERN_FREQUENCY_TABLE:
        case CI_WORD_PATTERN_FREQUENCY_TABLE:
            return new FrequencyStatisticsTableState(units);

        case UDI_FREQUENCY:
            return new UDIFrequencyStatisticsTableState(units);

        case LOW_FREQUENCE_STATISTICS:
        case CS_WORD_PATTERN_LOW_FREQUENCY_TABLE:
        case CI_WORD_PATTERN_LOW_FREQUENCY_TABLE:
            return new LowFrequencyStatisticsTableState(units);

        case BIN_FREQUENCE_STATISTICS:

            return new BinFrequencyStatisticsTableState(units);
        case BIN_LOW_FREQUENCE_STATISTICS:
            return new BinLowFrequencyStatisticsTableState(units);

        case PATTERN_FREQUENCE_STATISTICS:
            return new PatternFrequencyStatisticsTableState(units);

        case PATTERN_LOW_FREQUENCE_STATISTICS:
            return new PatternLowFrequencyStatisticsTableState(units);

        case EAST_ASIA_PATTERN_FREQUENCE_STATISTICS:
            return new EastAsiaPatternFrequencyStatisticsTableState(units);

        case EAST_ASIA_PATTERN_LOW_FREQUENCE_STATISTICS:
            return new EastAsiaPatternLowFrequencyStatisticsTableState(units);

        case DATE_PATTERN_FREQUENCE_STATISTICS:
            return new DatePatternFrequencyStatisticsTableState(units);

        case PATTERN_MATCHING:
            return new REGEXPatternStatisticsTableState(units);

        case UDI_MATCHING:
            return new UDIMatchStatisticsTableState(units);

        case SQL_PATTERN_MATCHING:
            return new SQLPatternStatisticsTableState(units);

        case SUMMARY_STATISTICS:
            return new SummaryStatisticsTableState(units);
            // MOD mzhao 2009-03-23, Soundex frequency.
        case SOUNDEX_FREQUENCY_TABLE:
            return new SoundexFrequencyTableState(units);
        case SOUNDEX_LOW_FREQUENCY_TABLE:
            return new SoundexLowFrequencyTableState(units);

        case DATE_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsTableState(units);
        case WEEK_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsTableState(units);
        case MONTH_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsTableState(units);
        case QUARTER_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsTableState(units);
        case YEAR_FREQUENCE_STATISTICS:
            return new DateFrequencyStatisticsTableState(units);
        case DATE_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsTableState(units);
        case WEEK_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsTableState(units);
        case MONTH_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsTableState(units);
        case QUARTER_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsTableState(units);
        case YEAR_LOW_FREQUENCE_STATISTICS:
            return new DateLowFrequencyStatisticsTableState(units);
        case SIMPLE_TEXT_STATISTICS:
            return new SimpleTextStatisticsTableState(units);
        case PHONE_NUMBER_STATISTICS:
            return new PhoneNumbStatisticsTableState(units);
        case FORMAT_FREQ_PIE_STATISTICS:
            return new PieStatisticsTableState(units);
        case BENFORD_LAW_STATISTICS:
            return new BenfordLawFrequencyTableState(units);
        default:
            return null;
        }
    }

    public ITableTypeStates getTableStateForRule(EIndicatorChartType type, List<TableIndicatorUnit> units,
            TableIndicator tableIndicator) {
        switch (type) {
        case WHERERULE_INDICATOR:
            return new WhereRuleStatisticsTableState(units, tableIndicator);
        case SIMPLE_STATISTICS:
            return new SimpleRuleStatisticsTableState(units);

        default:
            return null;
        }
    }
}
