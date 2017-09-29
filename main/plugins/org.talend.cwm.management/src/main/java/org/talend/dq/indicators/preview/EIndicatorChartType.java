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
package org.talend.dq.indicators.preview;

import org.talend.cwm.management.i18n.Messages;
import org.talend.dq.analysis.explore.BenfordLawFrequencyExplorer;
import org.talend.dq.analysis.explore.FrequencyStatisticsExplorer;
import org.talend.dq.analysis.explore.FunctionFrequencyStatExplorer;
import org.talend.dq.analysis.explore.IDataExplorer;
import org.talend.dq.analysis.explore.RegexPatternExplorer;
import org.talend.dq.analysis.explore.SQLPatternExplorer;
import org.talend.dq.analysis.explore.SimpleStatisticsExplorer;
import org.talend.dq.analysis.explore.SoundexFrequencyExplorer;
import org.talend.dq.analysis.explore.SummaryStastictisExplorer;
import org.talend.dq.analysis.explore.TextStatisticsExplorer;

/**
 * DOC zqin class global comment. Detailled comment
 */
public enum EIndicatorChartType {

    SIMPLE_STATISTICS(Messages.getString("EIndicatorChartType.SimpleStatistics"), new SimpleStatisticsExplorer()), //$NON-NLS-1$
    TEXT_STATISTICS(Messages.getString("EIndicatorChartType.TextStatistics"), new TextStatisticsExplorer()), //$NON-NLS-1$
    FREQUENCE_STATISTICS(Messages.getString("EIndicatorChartType.FrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    BIN_FREQUENCE_STATISTICS(Messages.getString("EIndicatorChartType.BinFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    LOW_FREQUENCE_STATISTICS(Messages.getString("EIndicatorChartType.LowFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    BIN_LOW_FREQUENCE_STATISTICS(
                                 Messages.getString("EIndicatorChartType.BinLowFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    PATTERN_FREQUENCE_STATISTICS(
                                 Messages.getString("EIndicatorChartType.PatternFrequencyStatistics"), new FunctionFrequencyStatExplorer()), //$NON-NLS-1$
    PATTERN_LOW_FREQUENCE_STATISTICS(
                                     Messages.getString("EIndicatorChartType.PatternLowFrequencyStatistics"), new FunctionFrequencyStatExplorer()), //$NON-NLS-1$
    EAST_ASIA_PATTERN_FREQUENCE_STATISTICS(
                                           Messages.getString("EIndicatorChartType.EastAsiaPatternFrequencyStatistics"), new FunctionFrequencyStatExplorer()), //$NON-NLS-1$
    EAST_ASIA_PATTERN_LOW_FREQUENCE_STATISTICS(
                                               Messages.getString("EIndicatorChartType.EastAsiaPatternLowFrequencyStatistics"), new FunctionFrequencyStatExplorer()), //$NON-NLS-1$
    DATE_PATTERN_FREQUENCE_STATISTICS(
                                      Messages.getString("EIndicatorChartType.DatePatternFrequencyStatistics"), new FunctionFrequencyStatExplorer()), //$NON-NLS-1$
    SUMMARY_STATISTICS(Messages.getString("EIndicatorChartType.SummaryStatistics"), new SummaryStastictisExplorer()), //$NON-NLS-1$
    PATTERN_MATCHING(Messages.getString("EIndicatorChartType.PatternMatching"), new RegexPatternExplorer()), //$NON-NLS-1$
    SQL_PATTERN_MATCHING(Messages.getString("EIndicatorChartType.SQLPatternMatching"), new SQLPatternExplorer()), //$NON-NLS-1$
    MODE_INDICATOR(Messages.getString("EIndicatorChartType.ModeIndicator"), null), //$NON-NLS-1$
    WHERERULE_INDICATOR(Messages.getString("EIndicatorChartType.WhereRuleIndicator"), null), //$NON-NLS-1$
    // MOD mzhao 2009-03-23,Soundex frequency.
    SOUNDEX_FREQUENCY_TABLE(Messages.getString("EIndicatorChartType.SoundexFrequencyTable"), new SoundexFrequencyExplorer()), //$NON-NLS-1$
    SOUNDEX_LOW_FREQUENCY_TABLE(
                                Messages.getString("EIndicatorChartType.SoundexLowFrequencyTable"), new SoundexFrequencyExplorer()), //$NON-NLS-1$
    UDI_COUNT(Messages.getString("EIndicatorChartType.UserDefinedCount"), new SimpleStatisticsExplorer()), //$NON-NLS-1$
    UDI_FREQUENCY(Messages.getString("EIndicatorChartType.UserDefinedFrequency"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    UDI_MATCHING(Messages.getString("EIndicatorChartType.UserDefinedMatching"), new RegexPatternExplorer()), //$NON-NLS-1$
    UDI_REALVALUE(Messages.getString("EIndicatorChartType.UserDefinedRealValue"), new SimpleStatisticsExplorer()), //$NON-NLS-1$

    DATE_FREQUENCE_STATISTICS(
                              Messages.getString("EIndicatorChartType.DateFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    DATE_LOW_FREQUENCE_STATISTICS(
                                  Messages.getString("EIndicatorChartType.DateLowFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    // MOD yyi 2010-10-08 16081
    WEEK_FREQUENCE_STATISTICS(
                              Messages.getString("EIndicatorChartType.WeekFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    WEEK_LOW_FREQUENCE_STATISTICS(
                                  Messages.getString("EIndicatorChartType.WeekLowFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    MONTH_FREQUENCE_STATISTICS(
                               Messages.getString("EIndicatorChartType.MonthFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    MONTH_LOW_FREQUENCE_STATISTICS(
                                   Messages.getString("EIndicatorChartType.MonthLowFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    QUARTER_FREQUENCE_STATISTICS(
                                 Messages.getString("EIndicatorChartType.QuarterFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    QUARTER_LOW_FREQUENCE_STATISTICS(
                                     Messages.getString("EIndicatorChartType.QuarterLowFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    YEAR_FREQUENCE_STATISTICS(
                              Messages.getString("EIndicatorChartType.YearFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    YEAR_LOW_FREQUENCE_STATISTICS(
                                  Messages.getString("EIndicatorChartType.YearLowFrequencyStatistics"), new FrequencyStatisticsExplorer()), //$NON-NLS-1$
    // ~
    SIMPLE_TEXT_STATISTICS(Messages.getString("EIndicatorChartType.SimpleTextStatistics"), new TextStatisticsExplorer()), //$NON-NLS-1$

    PHONE_NUMBER_STATISTICS(Messages.getString("EIndicatorChartType.PhoneNumbStatistics"), null), //$NON-NLS-1$

    FORMAT_FREQ_PIE_STATISTICS(Messages.getString("EIndicatorChartType.FormatFreqPieStatistics"), null), //$NON-NLS-1$

    BENFORD_LAW_STATISTICS(Messages.getString("EIndicatorChartType.BenfordLawStatistics"), new BenfordLawFrequencyExplorer()); //$NON-NLS-1$

    private String literal;

    private IDataExplorer explorer;

    public IDataExplorer getExplorer() {
        return explorer;
    }

    public String getLiteral() {
        return literal;
    }

    EIndicatorChartType(String literal, IDataExplorer explorer) {
        this.literal = literal;
        this.explorer = explorer;
    }
}
