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
package org.talend.dq.nodes.indicator.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.sql.IndicatorSqlPackage;

/**
 * @author rli
 */
public enum IndicatorEnum {
    SqlPatternMatchingIndicatorEnum(
            IndicatorsPackage.SQL_PATTERN_MATCHING_INDICATOR,
            IndicatorsPackage.Literals.SQL_PATTERN_MATCHING_INDICATOR,
            "SQL Pattern Matching", //$NON-NLS-1$
            true,
            null),
    RegexpMatchingIndicatorEnum(
            IndicatorsPackage.REGEXP_MATCHING_INDICATOR,
            IndicatorsPackage.Literals.REGEXP_MATCHING_INDICATOR,
            "regexp matching", //$NON-NLS-1$
            true,
            null),
    RowCountIndicatorEnum(
            IndicatorsPackage.ROW_COUNT_INDICATOR,
            IndicatorsPackage.Literals.ROW_COUNT_INDICATOR,
            "Row Count", true, //$NON-NLS-1$
            null),
    NullCountIndicatorEnum(
            IndicatorsPackage.NULL_COUNT_INDICATOR,
            IndicatorsPackage.Literals.NULL_COUNT_INDICATOR,
            "Null Count", true, //$NON-NLS-1$
            null),
    DistinctCountIndicatorEnum(
            IndicatorsPackage.DISTINCT_COUNT_INDICATOR,
            IndicatorsPackage.Literals.DISTINCT_COUNT_INDICATOR,
            "Distinct Count", //$NON-NLS-1$
            true,
            null),
    UniqueIndicatorEnum(
            IndicatorsPackage.UNIQUE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.UNIQUE_COUNT_INDICATOR,
            "Unique Count", //$NON-NLS-1$
            true,
            null),
    DuplicateCountIndicatorEnum(
            IndicatorsPackage.DUPLICATE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.DUPLICATE_COUNT_INDICATOR,
            "Duplicate Count", //$NON-NLS-1$
            true,
            null),
    BlankCountIndicatorEnum(
            IndicatorsPackage.BLANK_COUNT_INDICATOR,
            IndicatorsPackage.Literals.BLANK_COUNT_INDICATOR,
            "Blank Count", //$NON-NLS-1$
            true,
            null),
    DefValueCountIndicatorEnum(
            IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.DEF_VALUE_COUNT_INDICATOR,
            "Default Value Count", //$NON-NLS-1$
            true,
            null),

    MinLengthIndicatorEnum(
            IndicatorsPackage.MIN_LENGTH_INDICATOR,
            IndicatorsPackage.Literals.MIN_LENGTH_INDICATOR,
            "Minimal Length", //$NON-NLS-1$
            false,
            null),
    MinLengthWithNullIndicatorEnum(
            IndicatorsPackage.MIN_LENGTH_WITH_NULL_INDICATOR,
            IndicatorsPackage.Literals.MIN_LENGTH_WITH_NULL_INDICATOR,
            "Minimal Length With Null", //$NON-NLS-1$
            false,
            null),

    MinLengthWithBlankIndicatorEnum(
            IndicatorsPackage.MIN_LENGTH_WITH_BLANK_INDICATOR,
            IndicatorsPackage.Literals.MIN_LENGTH_WITH_BLANK_INDICATOR,
            "Minimal Length With Blank", //$NON-NLS-1$
            false,
            null),

    MinLengthWithBlankNullIndicatorEnum(
            IndicatorsPackage.MIN_LENGTH_WITH_BLANK_NULL_INDICATOR,
            IndicatorsPackage.Literals.MIN_LENGTH_WITH_BLANK_NULL_INDICATOR,
            "Minimal Length With Blank and Null", //$NON-NLS-1$
            false,
            null),

    MaxLengthIndicatorEnum(
            IndicatorsPackage.MAX_LENGTH_INDICATOR,
            IndicatorsPackage.Literals.MAX_LENGTH_INDICATOR,
            "Maximal Length", //$NON-NLS-1$
            false,
            null),
    MaxLengthWithNullIndicatorEnum(
            IndicatorsPackage.MAX_LENGTH_WITH_NULL_INDICATOR,
            IndicatorsPackage.Literals.MAX_LENGTH_WITH_NULL_INDICATOR,
            "Maximal Length With Null", //$NON-NLS-1$
            false,
            null),
    MaxLengthWithBlankIndicatorEnum(
            IndicatorsPackage.MAX_LENGTH_WITH_BLANK_INDICATOR,
            IndicatorsPackage.Literals.MAX_LENGTH_WITH_BLANK_INDICATOR,
            "Maximal Length With Blank", //$NON-NLS-1$
            false,
            null),
    MaxLengthWithBlankNullIndicatorEnum(
            IndicatorsPackage.MAX_LENGTH_WITH_BLANK_NULL_INDICATOR,
            IndicatorsPackage.Literals.MAX_LENGTH_WITH_BLANK_NULL_INDICATOR,
            "Maximal Length With Blank and Null", //$NON-NLS-1$
            false,
            null),

    AverageLengthIndicatorEnum(
            IndicatorsPackage.AVERAGE_LENGTH_INDICATOR,
            IndicatorsPackage.Literals.AVERAGE_LENGTH_INDICATOR,
            "Average Length", //$NON-NLS-1$
            false,
            null),
    AverageLengthWithNullIndicatorEnum(
            IndicatorsPackage.AVG_LENGTH_WITH_NULL_INDICATOR,
            IndicatorsPackage.Literals.AVG_LENGTH_WITH_NULL_INDICATOR,
            "Average Length With Null", //$NON-NLS-1$
            false,
            null),
    AverageLengthWithBlankIndicatorEnum(
            IndicatorsPackage.AVG_LENGTH_WITH_BLANK_INDICATOR,
            IndicatorsPackage.Literals.AVG_LENGTH_WITH_BLANK_INDICATOR,
            "Average Length With Blank", //$NON-NLS-1$
            false,
            null),
    AverageLengthWithNullBlankIndicatorEnum(
            IndicatorsPackage.AVG_LENGTH_WITH_BLANK_NULL_INDICATOR,
            IndicatorsPackage.Literals.AVG_LENGTH_WITH_BLANK_NULL_INDICATOR,
            "Average Length With Blank and Null", //$NON-NLS-1$
            false,
            null),

    ModeIndicatorEnum(IndicatorsPackage.MODE_INDICATOR, IndicatorsPackage.Literals.MODE_INDICATOR, "Mode", false, null), //$NON-NLS-1$

    FrequencyIndicatorEnum(
            IndicatorsPackage.FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.FREQUENCY_INDICATOR,
            "Frequency Table", //$NON-NLS-1$
            true,
            null),

    DateFrequencyIndicatorEnum(
            IndicatorsPackage.DATE_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.DATE_FREQUENCY_INDICATOR,
            "Date Frequency Table", //$NON-NLS-1$
            true,
            null),
    WeekFrequencyIndicatorEnum(
            IndicatorsPackage.WEEK_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.WEEK_FREQUENCY_INDICATOR,
            "Week Frequency Table", //$NON-NLS-1$
            true,
            null),
    MonthFrequencyIndicatorEnum(
            IndicatorsPackage.MONTH_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.MONTH_FREQUENCY_INDICATOR,
            "Month Frequency Table", //$NON-NLS-1$
            true,
            null),
    QuarterFrequencyIndicatorEnum(
            IndicatorsPackage.QUARTER_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.QUARTER_FREQUENCY_INDICATOR,
            "Quarter Frequency Table", //$NON-NLS-1$
            true,
            null),
    YearFrequencyIndicatorEnum(
            IndicatorsPackage.YEAR_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.YEAR_FREQUENCY_INDICATOR,
            "Year Frequency Table", //$NON-NLS-1$
            true,
            null),
    BinFrequencyIndicatorEnum(
            IndicatorsPackage.BIN_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.BIN_FREQUENCY_INDICATOR,
            "Bin Frequency Table", //$NON-NLS-1$
            true,
            null),

    LowFrequencyIndicatorEnum(
            IndicatorsPackage.LOW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.LOW_FREQUENCY_INDICATOR,
            "Low Frequency Table", //$NON-NLS-1$
            true,
            null),

    DateLowFrequencyIndicatorEnum(
            IndicatorsPackage.DATE_LOW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.DATE_LOW_FREQUENCY_INDICATOR,
            "Date Low Frequency Table", //$NON-NLS-1$
            true,
            null),
    WeekLowFrequencyIndicatorEnum(
            IndicatorsPackage.WEEK_LOW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.WEEK_LOW_FREQUENCY_INDICATOR,
            "Week Low Frequency Table", //$NON-NLS-1$
            true,
            null),
    MonthLowFrequencyIndicatorEnum(
            IndicatorsPackage.MONTH_LOW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.MONTH_LOW_FREQUENCY_INDICATOR,
            "Month Low Frequency Table", //$NON-NLS-1$
            true,
            null),
    QuarterLowFrequencyIndicatorEnum(
            IndicatorsPackage.QUARTER_LOW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.QUARTER_LOW_FREQUENCY_INDICATOR,
            "Quarter Low Frequency Table", //$NON-NLS-1$
            true,
            null),
    YearLowFrequencyIndicatorEnum(
            IndicatorsPackage.YEAR_LOW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.YEAR_LOW_FREQUENCY_INDICATOR,
            "Year Low Frequency Table", //$NON-NLS-1$
            true,
            null),
    BinLowFrequencyIndicatorEnum(
            IndicatorsPackage.BIN_LOW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.BIN_LOW_FREQUENCY_INDICATOR,
            "Bin Low Frequency Table", //$NON-NLS-1$
            true,
            null),

    // MOD mzhao 2009-03-05 Soundex frequency indicator.
    SoundexIndicatorEnum(
            IndicatorsPackage.SOUNDEX_FREQ_INDICATOR,
            IndicatorsPackage.Literals.SOUNDEX_FREQ_INDICATOR,
            "Soundex Frequency Table", //$NON-NLS-1$
            true,
            null),
    SoundexLowIndicatorEnum(
            IndicatorsPackage.SOUNDEX_LOW_FREQ_INDICATOR,
            IndicatorsPackage.Literals.SOUNDEX_LOW_FREQ_INDICATOR,
            "Soundex Low Frequency Table", //$NON-NLS-1$
            true,
            null),

    PatternFreqIndicatorEnum(
            IndicatorsPackage.PATTERN_FREQ_INDICATOR,
            IndicatorsPackage.Literals.PATTERN_FREQ_INDICATOR,
            "Pattern Frequency Table", //$NON-NLS-1$
            true,
            null),
    PatternLowFreqIndicatorEnum(
            IndicatorsPackage.PATTERN_LOW_FREQ_INDICATOR,
            IndicatorsPackage.Literals.PATTERN_LOW_FREQ_INDICATOR,
            "Pattern Low Frequency Table", //$NON-NLS-1$
            true,
            null),
    EastAsiaPatternFreqIndicatorEnum(
            IndicatorsPackage.EAST_ASIA_PATTERN_FREQ_INDICATOR,
            IndicatorsPackage.Literals.EAST_ASIA_PATTERN_FREQ_INDICATOR,
            "East Asia Pattern Frequency Table", //$NON-NLS-1$
            true,
            null),
    EastAsiaPatternLowFreqIndicatorEnum(
            IndicatorsPackage.EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR,
            IndicatorsPackage.Literals.EAST_ASIA_PATTERN_LOW_FREQ_INDICATOR,
            "East Asia Pattern Low Frequency Table", //$NON-NLS-1$
            true,
            null),
    DatePatternFreqIndicatorEnum(
            IndicatorsPackage.DATE_PATTERN_FREQ_INDICATOR,
            IndicatorsPackage.Literals.DATE_PATTERN_FREQ_INDICATOR,
            "Date Pattern Frequency Table", //$NON-NLS-1$
            true,
            null),

    MeanIndicatorEnum(IndicatorsPackage.MEAN_INDICATOR, IndicatorsPackage.Literals.MEAN_INDICATOR, "Mean", false, null), //$NON-NLS-1$
    MedianIndicatorEnum(IndicatorsPackage.MEDIAN_INDICATOR, IndicatorsPackage.Literals.MEDIAN_INDICATOR, "Median", false, null), //$NON-NLS-1$
    MinValueIndicatorEnum(
            IndicatorsPackage.MIN_VALUE_INDICATOR,
            IndicatorsPackage.Literals.MIN_VALUE_INDICATOR,
            "Minimum", false, //$NON-NLS-1$
            null),
    MaxValueIndicatorEnum(
            IndicatorsPackage.MAX_VALUE_INDICATOR,
            IndicatorsPackage.Literals.MAX_VALUE_INDICATOR,
            "Maximum", false, //$NON-NLS-1$
            null),
    LowerQuartileIndicatorEnum(
            IndicatorsPackage.LOWER_QUARTILE_INDICATOR,
            IndicatorsPackage.Literals.LOWER_QUARTILE_INDICATOR,
            "Lower Quartile", //$NON-NLS-1$
            false,
            null),
    UpperQuartileIndicatorEnum(
            IndicatorsPackage.UPPER_QUARTILE_INDICATOR,
            IndicatorsPackage.Literals.UPPER_QUARTILE_INDICATOR,
            "Upper Quartile", //$NON-NLS-1$
            false,
            null),
    CountsIndicatorEnum(
            IndicatorsPackage.COUNTS_INDICATOR,
            IndicatorsPackage.Literals.COUNTS_INDICATOR,
            "Simple Statistics", true,//$NON-NLS-1$
            new IndicatorEnum[] { RowCountIndicatorEnum, NullCountIndicatorEnum, DistinctCountIndicatorEnum, UniqueIndicatorEnum,
                    DuplicateCountIndicatorEnum, BlankCountIndicatorEnum, DefValueCountIndicatorEnum }),
    TextIndicatorEnum(IndicatorsPackage.TEXT_INDICATOR, IndicatorsPackage.Literals.TEXT_INDICATOR, "Text Statistics", false,//$NON-NLS-1$
            new IndicatorEnum[] { MinLengthIndicatorEnum, MinLengthWithNullIndicatorEnum, MinLengthWithBlankIndicatorEnum,
                    MinLengthWithBlankNullIndicatorEnum, MaxLengthIndicatorEnum, MaxLengthWithNullIndicatorEnum,
                    MaxLengthWithBlankIndicatorEnum, MaxLengthWithBlankNullIndicatorEnum, AverageLengthIndicatorEnum,
                    AverageLengthWithNullIndicatorEnum, AverageLengthWithBlankIndicatorEnum,
                    AverageLengthWithNullBlankIndicatorEnum }),
    IQRIndicatorEnum(IndicatorsPackage.IQR_INDICATOR, IndicatorsPackage.Literals.IQR_INDICATOR, "Inter Quartile Range", false,//$NON-NLS-1$
            new IndicatorEnum[] { LowerQuartileIndicatorEnum, UpperQuartileIndicatorEnum }),
    RangeIndicatorEnum(IndicatorsPackage.RANGE_INDICATOR, IndicatorsPackage.Literals.RANGE_INDICATOR, "Range", false,//$NON-NLS-1$
            new IndicatorEnum[] { MinValueIndicatorEnum, MaxValueIndicatorEnum }),
    BoxIIndicatorEnum(IndicatorsPackage.BOX_INDICATOR, IndicatorsPackage.Literals.BOX_INDICATOR, "Summary Statistics", false,//$NON-NLS-1$
            new IndicatorEnum[] { MeanIndicatorEnum, MedianIndicatorEnum, IQRIndicatorEnum, RangeIndicatorEnum }),
    WhereRuleIndicatorEnum(
            IndicatorSqlPackage.WHERE_RULE_INDICATOR,
            IndicatorSqlPackage.Literals.WHERE_RULE_INDICATOR,
            "where rule", //$NON-NLS-1$
            true,
            null),
    UserDefinedIndicatorEnum(
            IndicatorSqlPackage.USER_DEF_INDICATOR,
            IndicatorSqlPackage.Literals.USER_DEF_INDICATOR,
            "User Defined Indicator", //$NON-NLS-1$
            true,
            null),
    ColumnDependencyIndicatorEnum(
            ColumnsetPackage.COLUMN_DEPENDENCY_INDICATOR,
            ColumnsetPackage.Literals.COLUMN_DEPENDENCY_INDICATOR,
            // MOD msjian 2011-06-07 22021: fixed running the Function dependency analysis
            // imported from 4.0 works well.
            "Functional Dependency Indicator", //$NON-NLS-1$
            true,
            null),
    ColumnSetMultiValueIndicatorEnum(
            ColumnsetPackage.COLUMN_SET_MULTI_VALUE_INDICATOR,
            ColumnsetPackage.Literals.COLUMN_SET_MULTI_VALUE_INDICATOR,
            "Column Set Multi Value Indicator", //$NON-NLS-1$
            true,
            null),
    SimpleStatIndicatorEnum(
            ColumnsetPackage.SIMPLE_STAT_INDICATOR,
            ColumnsetPackage.Literals.SIMPLE_STAT_INDICATOR,
            "Simple Stat Indicator", //$NON-NLS-1$
            true,
            null),

    AllMatchIndicatorEnum(
            ColumnsetPackage.ALL_MATCH_INDICATOR,
            ColumnsetPackage.Literals.ALL_MATCH_INDICATOR,
            "All Match Indicator", //$NON-NLS-1$
            true,
            null),
    ValidPhoneCountIndicatorEnum(
            IndicatorsPackage.VALID_PHONE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.VALID_PHONE_COUNT_INDICATOR,
            "Valid Phone Number Count", //$NON-NLS-1$
            true,
            null),
    PossiblePhoneCountIndicatorEnum(
            IndicatorsPackage.POSSIBLE_PHONE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.POSSIBLE_PHONE_COUNT_INDICATOR,
            "Possible Phone Number Count", //$NON-NLS-1$
            true,
            null),
    ValidRegCodeCountIndicatorEnum(
            IndicatorsPackage.VALID_REG_CODE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.VALID_REG_CODE_COUNT_INDICATOR,
            "Valid Region Code Count", //$NON-NLS-1$
            true,
            null),
    InvalidRegCodeCountIndicatorEnum(
            IndicatorsPackage.INVALID_REG_CODE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.INVALID_REG_CODE_COUNT_INDICATOR,
            "Invalid Region Code Count", //$NON-NLS-1$
            true,
            null),
    WellFormIntePhoneCountIndicatorEnum(
            IndicatorsPackage.WELL_FORM_INTE_PHONE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.WELL_FORM_INTE_PHONE_COUNT_INDICATOR,
            "Well Formed International Phone Number Count", //$NON-NLS-1$
            true,
            null),
    WellFormNationalPhoneCountIndicatorEnum(
            IndicatorsPackage.WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR,
            "Well Formed National Phone Number Count", //$NON-NLS-1$
            true,
            null),
    WellFormE164PhoneCountIndicatorEnum(
            IndicatorsPackage.WELL_FORM_E164_PHONE_COUNT_INDICATOR,
            IndicatorsPackage.Literals.WELL_FORM_E164_PHONE_COUNT_INDICATOR,
            "Well Formed E164 Phone Number Count", //$NON-NLS-1$
            true,
            null),
    FormatFreqPieIndictorEnum(
            IndicatorsPackage.FORMAT_FREQ_PIE_INDICATOR,
            IndicatorsPackage.Literals.FORMAT_FREQ_PIE_INDICATOR,
            "Format Phone Number Frequency", //$NON-NLS-1$
            false,
            null),
    PhoneNumbStatisticsIndicatorEnum(
            IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR,
            IndicatorsPackage.Literals.PHONE_NUMB_STATISTICS_INDICATOR,
            "Phone Number Statistics", //$NON-NLS-1$
            true,
            new IndicatorEnum[] { ValidPhoneCountIndicatorEnum, ValidRegCodeCountIndicatorEnum, InvalidRegCodeCountIndicatorEnum,
                    PossiblePhoneCountIndicatorEnum, WellFormIntePhoneCountIndicatorEnum,
                    WellFormNationalPhoneCountIndicatorEnum, WellFormE164PhoneCountIndicatorEnum, FormatFreqPieIndictorEnum }),
    BenfordLawFrequencyIndicatorEnum(
            IndicatorsPackage.BENFORD_LAW_FREQUENCY_INDICATOR,
            IndicatorsPackage.Literals.BENFORD_LAW_FREQUENCY_INDICATOR,
            "Benford Law Frequency", //$NON-NLS-1$
            true,
            null),
    CSWordPatternFreqIndicatorEnum(
            IndicatorsPackage.CS_WORD_PATTERN_FREQ_INDICATOR,
            IndicatorsPackage.Literals.CS_WORD_PATTERN_FREQ_INDICATOR,
            "CS Word Pattern Frequency", //$NON-NLS-1$
            true,
            null),
    CSWordPatternLowFreqIndicatorEnum(
            IndicatorsPackage.CS_WORD_PATTERN_LOW_FREQ_INDICATOR,
            IndicatorsPackage.Literals.CS_WORD_PATTERN_LOW_FREQ_INDICATOR,
            "CS Word Pattern Low Frequency", //$NON-NLS-1$
            true,
            null),
    CIWordPatternFreqIndicatorEnum(
            IndicatorsPackage.CI_WORD_PATTERN_FREQ_INDICATOR,
            IndicatorsPackage.Literals.CI_WORD_PATTERN_FREQ_INDICATOR,
            "CI Word Pattern Frequency", //$NON-NLS-1$
            true,
            null),
    CIWordPatternLowFreqIndicatorEnum(
            IndicatorsPackage.CI_WORD_PATTERN_LOW_FREQ_INDICATOR,
            IndicatorsPackage.Literals.CI_WORD_PATTERN_LOW_FREQ_INDICATOR,
            "CI Word Pattern Low Frequency", //$NON-NLS-1$
            true,
            null),

    PatternIndicatorEnum(
            IndicatorsPackage.PATTERN_MATCHING_INDICATOR,
            IndicatorsPackage.Literals.PATTERN_MATCHING_INDICATOR,
            "Patterns", //$NON-NLS-1$
            true,
            null);

    private EClass indicatorType;

    private String label;

    private IndicatorEnum[] children;

    private IndicatorEnum parent;

    private final int indicatorClassifierId;

    private static Map<String, IndicatorEnum> labelMap;

    // whether this indicator compute count like as Simple statistic and Frequency indicator; no compute count like as Text
    // Indicators and Summary Indicators.
    private boolean isCountRow;

    IndicatorEnum(int indicatorClassifierId, EClass indicatorType, String label, boolean isCountRow, IndicatorEnum[] children) {
        this.indicatorClassifierId = indicatorClassifierId;
        this.indicatorType = indicatorType;
        this.label = label;
        this.isCountRow = isCountRow;
        setChildren(children);
    }

    /**
     * DOC rli Comment method "setChildren".
     * 
     * @param children
     */
    private void setChildren(IndicatorEnum[] children) {
        this.children = children;
        if (this.children == null) {
            return;
        }
        for (IndicatorEnum indicatorEnum : children) {
            indicatorEnum.setParent(this);
        }
    }

    /**
     * @return the indicator
     */
    public EClass getIndicatorType() {
        return indicatorType;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the children
     */
    public IndicatorEnum[] getChildren() {
        return children;
    }

    /**
     * Getter for indicatorClassifierId.
     * 
     * @return the indicatorClassifierId
     */
    public int getIndicatorClassifierId() {
        return indicatorClassifierId;
    }

    public boolean hasChildren() {
        return children != null;
    }

    public boolean hasParent() {
        return parent != null;
    }

    private void setParent(IndicatorEnum parent) {
        this.parent = parent;
    }

    /**
     * Getter for parent.
     * 
     * @return the parent
     */
    public IndicatorEnum getParent() {
        return parent;
    }

    public static IndicatorEnum findIndicatorEnumByLabel(String label) {
        if (labelMap == null) {
            labelMap = new HashMap<String, IndicatorEnum>();
            for (IndicatorEnum indiEnum : IndicatorEnum.values()) {
                labelMap.put(indiEnum.getLabel(), indiEnum);
            }
        }
        return labelMap.get(label);
    }

    public static IndicatorEnum findIndicatorEnum(EClass indicatorType) {
        IndicatorEnum returnEnum = null;

        if (indicatorType == RowCountIndicatorEnum.getIndicatorType()) {
            returnEnum = RowCountIndicatorEnum;
        } else if (indicatorType == NullCountIndicatorEnum.getIndicatorType()) {
            returnEnum = NullCountIndicatorEnum;
        } else if (indicatorType == DistinctCountIndicatorEnum.getIndicatorType()) {
            returnEnum = DistinctCountIndicatorEnum;
        } else if (indicatorType == UniqueIndicatorEnum.getIndicatorType()) {
            returnEnum = UniqueIndicatorEnum;
        } else if (indicatorType == DuplicateCountIndicatorEnum.getIndicatorType()) {
            returnEnum = DuplicateCountIndicatorEnum;
        } else if (indicatorType == BlankCountIndicatorEnum.getIndicatorType()) {
            returnEnum = BlankCountIndicatorEnum;
        } else if (indicatorType == MinLengthIndicatorEnum.getIndicatorType()) {
            returnEnum = MinLengthIndicatorEnum;
        } else if (indicatorType == MinLengthWithNullIndicatorEnum.getIndicatorType()) {
            returnEnum = MinLengthWithNullIndicatorEnum;
        } else if (indicatorType == MinLengthWithBlankIndicatorEnum.getIndicatorType()) {
            returnEnum = MinLengthWithBlankIndicatorEnum;
        } else if (indicatorType == MinLengthWithBlankNullIndicatorEnum.getIndicatorType()) {
            returnEnum = MinLengthWithBlankNullIndicatorEnum;
        } else if (indicatorType == MaxLengthWithNullIndicatorEnum.getIndicatorType()) {
            returnEnum = MaxLengthWithNullIndicatorEnum;
        } else if (indicatorType == MaxLengthIndicatorEnum.getIndicatorType()) {
            returnEnum = MaxLengthIndicatorEnum;
        } else if (indicatorType == MaxLengthWithBlankIndicatorEnum.getIndicatorType()) {
            returnEnum = MaxLengthWithBlankIndicatorEnum;
        } else if (indicatorType == MaxLengthWithBlankNullIndicatorEnum.getIndicatorType()) {
            returnEnum = MaxLengthWithBlankNullIndicatorEnum;
        } else if (indicatorType == AverageLengthIndicatorEnum.getIndicatorType()) {
            returnEnum = AverageLengthIndicatorEnum;
        } else if (indicatorType == AverageLengthWithBlankIndicatorEnum.getIndicatorType()) {
            returnEnum = AverageLengthWithBlankIndicatorEnum;
        } else if (indicatorType == AverageLengthWithNullIndicatorEnum.getIndicatorType()) {
            returnEnum = AverageLengthWithNullIndicatorEnum;
        } else if (indicatorType == AverageLengthWithNullBlankIndicatorEnum.getIndicatorType()) {
            returnEnum = AverageLengthWithNullBlankIndicatorEnum;
        } else if (indicatorType == ModeIndicatorEnum.getIndicatorType()) {
            returnEnum = ModeIndicatorEnum;
        } else if (indicatorType == MeanIndicatorEnum.getIndicatorType()) {
            returnEnum = MeanIndicatorEnum;
        } else if (indicatorType == MedianIndicatorEnum.getIndicatorType()) {
            returnEnum = MedianIndicatorEnum;
        } else if (indicatorType == MinValueIndicatorEnum.getIndicatorType()) {
            returnEnum = MinValueIndicatorEnum;
        } else if (indicatorType == MaxValueIndicatorEnum.getIndicatorType()) {
            returnEnum = MaxValueIndicatorEnum;
        } else if (indicatorType == LowerQuartileIndicatorEnum.getIndicatorType()) {
            returnEnum = LowerQuartileIndicatorEnum;
        } else if (indicatorType == UpperQuartileIndicatorEnum.getIndicatorType()) {
            returnEnum = UpperQuartileIndicatorEnum;
        } else if (indicatorType == IQRIndicatorEnum.getIndicatorType()) {
            returnEnum = IQRIndicatorEnum;
        } else if (indicatorType == RangeIndicatorEnum.getIndicatorType()) {
            returnEnum = RangeIndicatorEnum;
        } else if (indicatorType == SoundexIndicatorEnum.getIndicatorType()) {
            returnEnum = SoundexIndicatorEnum;
        } else if (indicatorType == SoundexLowIndicatorEnum.getIndicatorType()) {
            returnEnum = SoundexLowIndicatorEnum;
        } else if (indicatorType == FrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = FrequencyIndicatorEnum;
        } else if (indicatorType == DateFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = DateFrequencyIndicatorEnum;
        } else if (indicatorType == WeekFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = WeekFrequencyIndicatorEnum;
        } else if (indicatorType == MonthFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = MonthFrequencyIndicatorEnum;
        } else if (indicatorType == QuarterFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = QuarterFrequencyIndicatorEnum;
        } else if (indicatorType == YearFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = YearFrequencyIndicatorEnum;
        } else if (indicatorType == BinFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = BinFrequencyIndicatorEnum;
        } else if (indicatorType == LowFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = LowFrequencyIndicatorEnum;
        } else if (indicatorType == DateLowFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = DateLowFrequencyIndicatorEnum;
        } else if (indicatorType == WeekLowFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = WeekLowFrequencyIndicatorEnum;
        } else if (indicatorType == MonthLowFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = MonthLowFrequencyIndicatorEnum;
        } else if (indicatorType == QuarterLowFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = QuarterLowFrequencyIndicatorEnum;
        } else if (indicatorType == YearLowFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = YearLowFrequencyIndicatorEnum;
        } else if (indicatorType == BinLowFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = BinLowFrequencyIndicatorEnum;
        } else if (indicatorType == PatternFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = PatternFreqIndicatorEnum;
        } else if (indicatorType == PatternLowFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = PatternLowFreqIndicatorEnum;
        } else if (indicatorType == EastAsiaPatternFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = EastAsiaPatternFreqIndicatorEnum;
        } else if (indicatorType == EastAsiaPatternLowFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = EastAsiaPatternLowFreqIndicatorEnum;
        } else if (indicatorType == DatePatternFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = DatePatternFreqIndicatorEnum;
        } else if (indicatorType == TextIndicatorEnum.getIndicatorType()) {
            returnEnum = TextIndicatorEnum;
        } else if (indicatorType == BoxIIndicatorEnum.getIndicatorType()) {
            returnEnum = BoxIIndicatorEnum;
        } else if (indicatorType == CountsIndicatorEnum.getIndicatorType()) {
            returnEnum = CountsIndicatorEnum;
        } else if (indicatorType == RegexpMatchingIndicatorEnum.getIndicatorType()) {
            returnEnum = RegexpMatchingIndicatorEnum;
        } else if (indicatorType == SqlPatternMatchingIndicatorEnum.getIndicatorType()) {
            returnEnum = SqlPatternMatchingIndicatorEnum;
        } else if (indicatorType == WhereRuleIndicatorEnum.getIndicatorType()) {
            returnEnum = WhereRuleIndicatorEnum;
        } else if (indicatorType == DefValueCountIndicatorEnum.getIndicatorType()) {
            returnEnum = DefValueCountIndicatorEnum;
        } else if (indicatorType == UserDefinedIndicatorEnum.getIndicatorType()) {
            returnEnum = UserDefinedIndicatorEnum;
        } else if (indicatorType == ColumnDependencyIndicatorEnum.getIndicatorType()) {
            returnEnum = ColumnDependencyIndicatorEnum;
        } else if (indicatorType == AllMatchIndicatorEnum.getIndicatorType()) {
            returnEnum = AllMatchIndicatorEnum;
        } else if (indicatorType == ValidPhoneCountIndicatorEnum.getIndicatorType()) {
            returnEnum = ValidPhoneCountIndicatorEnum;
        } else if (indicatorType == ValidRegCodeCountIndicatorEnum.getIndicatorType()) {
            returnEnum = ValidRegCodeCountIndicatorEnum;
        } else if (indicatorType == PossiblePhoneCountIndicatorEnum.getIndicatorType()) {
            returnEnum = PossiblePhoneCountIndicatorEnum;
        } else if (indicatorType == InvalidRegCodeCountIndicatorEnum.getIndicatorType()) {
            returnEnum = InvalidRegCodeCountIndicatorEnum;
        } else if (indicatorType == WellFormE164PhoneCountIndicatorEnum.getIndicatorType()) {
            returnEnum = WellFormE164PhoneCountIndicatorEnum;
        } else if (indicatorType == WellFormIntePhoneCountIndicatorEnum.getIndicatorType()) {
            returnEnum = WellFormIntePhoneCountIndicatorEnum;
        } else if (indicatorType == WellFormNationalPhoneCountIndicatorEnum.getIndicatorType()) {
            returnEnum = WellFormNationalPhoneCountIndicatorEnum;
        } else if (indicatorType == PhoneNumbStatisticsIndicatorEnum.getIndicatorType()) {
            returnEnum = PhoneNumbStatisticsIndicatorEnum;
        } else if (indicatorType == FormatFreqPieIndictorEnum.getIndicatorType()) {
            returnEnum = FormatFreqPieIndictorEnum;
        } else if (indicatorType == BenfordLawFrequencyIndicatorEnum.getIndicatorType()) {
            returnEnum = BenfordLawFrequencyIndicatorEnum;
        } else if (indicatorType == CSWordPatternFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = CSWordPatternFreqIndicatorEnum;
        } else if (indicatorType == CSWordPatternLowFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = CSWordPatternLowFreqIndicatorEnum;
        } else if (indicatorType == CIWordPatternFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = CIWordPatternFreqIndicatorEnum;
        } else if (indicatorType == CIWordPatternLowFreqIndicatorEnum.getIndicatorType()) {
            returnEnum = CIWordPatternLowFreqIndicatorEnum;
        }

        return returnEnum;

    }

    /**
     * 
     * Judge the enum is plainIndicatorEnum
     * 
     * @param indicatorEnumn
     * @return True if indicatorEnumn is plainIndicatorEnum
     */
    public static boolean isPlainIndicatorEnum(IndicatorEnum indicatorEnumn) {
        return indicatorEnumn != RegexpMatchingIndicatorEnum && indicatorEnumn != SqlPatternMatchingIndicatorEnum
                && indicatorEnumn != WhereRuleIndicatorEnum && indicatorEnumn != UserDefinedIndicatorEnum;
    }

    /**
     * 
     * Judge the enum is SpecialIndicatorEnum
     * 
     * @param indicatorEnumn
     * @return True if indicatorEnumn is SpecialIndicatorEnum
     */
    public static boolean isSpecialIndicatorEnum(IndicatorEnum indicatorEnumn) {
        return indicatorEnumn == RegexpMatchingIndicatorEnum || indicatorEnumn == SqlPatternMatchingIndicatorEnum
                || indicatorEnumn == UserDefinedIndicatorEnum;
    }

    public boolean isAChildOf(IndicatorEnum parent) {
        if (parent.getChildren() != null) {
            for (IndicatorEnum child : parent.getChildren()) {
                if (child == this) {
                    return true;
                }
            }
        }

        return false;
    }

    public static List<IndicatorEnum> getJavaIndicatorsEnum() {
        List<IndicatorEnum> returnList = new ArrayList<IndicatorEnum>();
        returnList.add(IndicatorEnum.DatePatternFreqIndicatorEnum);
        returnList.add(IndicatorEnum.EastAsiaPatternFreqIndicatorEnum);
        returnList.add(IndicatorEnum.EastAsiaPatternLowFreqIndicatorEnum);
        returnList.addAll(Arrays.asList(IndicatorEnum.PhoneNumbStatisticsIndicatorEnum.getChildren()));
        return returnList;
    }

    public boolean isCountRow() {
        return isCountRow;
    }
}
