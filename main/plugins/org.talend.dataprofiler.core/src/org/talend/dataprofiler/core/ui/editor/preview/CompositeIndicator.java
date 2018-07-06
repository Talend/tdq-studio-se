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
package org.talend.dataprofiler.core.ui.editor.preview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public final class CompositeIndicator {

    private IndicatorUnit[] indicatorUnits;

    private TableIndicatorUnit[] tableIndicatorUnits;

    private Map<EIndicatorChartType, List<IndicatorUnit>> separatedMap;

    private Map<EIndicatorChartType, List<TableIndicatorUnit>> tableSeparatedMap;

    private List<IndicatorUnit> simpleList, textList, frequencyList, lowFrequencyList, soundexFrequencyList,
            soundexLowFrequencyList, patternFrequencylist, patternLowFrequencyList, eastAsiaPatternFrequencylist,
            eastAsiaPatternLowFrequencyList, datePatternFrequencyList, summaryList, patternList, sqlPatternList,
            modelIndicatorList, udiCountList, udiFrequencyList, udiMatchingList, udiRealValueList, dateFrequencyList,
            dateLowFrequencyList, simpleTextList, phoneNumbList, formatFreqList;

    private List<TableIndicatorUnit> tableSimpleList, tableWhereRuleList;

    private List<IndicatorUnit> binFrequencyList, fraudList;

    private List<IndicatorUnit> binLowFrequencyList;

    // MOD yyi 2010-10-08 16081
    private List<IndicatorUnit> weekFrequencyList, weekLowFrequencyList, monthFrequencyList, monthLowFrequencyList,
            quarterFrequencyList, quarterLowFrequencyList, yearFrequencyList, yearLowFrequencyList;

    private static CompositeIndicator instance;

    private CompositeIndicator() {
        init();

        // this.separatedMap = new HashMap<EIndicatorChartType, List<IndicatorUnit>>();
        // this.indicatorUnits = initIndicatorUnits(columnIndicator.getIndicatorUnits());
    }

    public static CompositeIndicator getInstance() {
        if (instance == null) {
            instance = new CompositeIndicator();
        }
        return instance;
    }

    private void init() {
        simpleList = new ArrayList<IndicatorUnit>();
        textList = new ArrayList<IndicatorUnit>();
        frequencyList = new ArrayList<IndicatorUnit>();
        lowFrequencyList = new ArrayList<IndicatorUnit>();
        binFrequencyList = new ArrayList<IndicatorUnit>();
        binLowFrequencyList = new ArrayList<IndicatorUnit>();
        soundexFrequencyList = new ArrayList<IndicatorUnit>();
        soundexLowFrequencyList = new ArrayList<IndicatorUnit>();
        patternFrequencylist = new ArrayList<IndicatorUnit>();
        patternLowFrequencyList = new ArrayList<IndicatorUnit>();
        eastAsiaPatternLowFrequencyList = new ArrayList<IndicatorUnit>();
        eastAsiaPatternFrequencylist = new ArrayList<IndicatorUnit>();
        datePatternFrequencyList = new ArrayList<IndicatorUnit>();
        summaryList = new ArrayList<IndicatorUnit>();
        patternList = new ArrayList<IndicatorUnit>();
        sqlPatternList = new ArrayList<IndicatorUnit>();
        modelIndicatorList = new ArrayList<IndicatorUnit>();
        separatedMap = new LinkedHashMap<EIndicatorChartType, List<IndicatorUnit>>();
        // MOD xqliu 2009-02-25 feature 6015
        tableSimpleList = new ArrayList<TableIndicatorUnit>();
        tableWhereRuleList = new ArrayList<TableIndicatorUnit>();
        tableSeparatedMap = new HashMap<EIndicatorChartType, List<TableIndicatorUnit>>();
        // ~
        udiCountList = new ArrayList<IndicatorUnit>();
        udiFrequencyList = new ArrayList<IndicatorUnit>();
        udiMatchingList = new ArrayList<IndicatorUnit>();
        udiRealValueList = new ArrayList<IndicatorUnit>();

        dateFrequencyList = new ArrayList<IndicatorUnit>();
        dateLowFrequencyList = new ArrayList<IndicatorUnit>();
        simpleTextList = new ArrayList<IndicatorUnit>();

        // MOD yyi 2010-10-08 16081
        weekFrequencyList = new ArrayList<IndicatorUnit>();
        weekLowFrequencyList = new ArrayList<IndicatorUnit>();
        monthFrequencyList = new ArrayList<IndicatorUnit>();
        monthLowFrequencyList = new ArrayList<IndicatorUnit>();
        quarterFrequencyList = new ArrayList<IndicatorUnit>();
        quarterLowFrequencyList = new ArrayList<IndicatorUnit>();
        yearFrequencyList = new ArrayList<IndicatorUnit>();
        yearLowFrequencyList = new ArrayList<IndicatorUnit>();
        // ~
        phoneNumbList = new ArrayList<IndicatorUnit>();
        formatFreqList = new ArrayList<IndicatorUnit>();
        fraudList = new ArrayList<IndicatorUnit>();
    }

    private void clear() {
        simpleList.clear();
        textList.clear();
        frequencyList.clear();
        lowFrequencyList.clear();
        binFrequencyList.clear();
        binLowFrequencyList.clear();
        soundexFrequencyList.clear();
        soundexLowFrequencyList.clear();
        patternFrequencylist.clear();
        patternLowFrequencyList.clear();
        eastAsiaPatternFrequencylist.clear();
        eastAsiaPatternLowFrequencyList.clear();
        datePatternFrequencyList.clear();
        summaryList.clear();
        patternList.clear();
        sqlPatternList.clear();
        modelIndicatorList.clear();
        separatedMap.clear();
        // MOD xqliu 2009-02-25 feature 6015
        tableSimpleList.clear();
        tableWhereRuleList.clear();
        tableSeparatedMap.clear();
        // ~
        udiCountList.clear();
        udiFrequencyList.clear();
        udiMatchingList.clear();
        udiRealValueList.clear();

        dateFrequencyList.clear();
        dateLowFrequencyList.clear();
        simpleTextList.clear();

        weekFrequencyList.clear();
        weekLowFrequencyList.clear();
        monthFrequencyList.clear();
        monthLowFrequencyList.clear();
        quarterFrequencyList.clear();
        quarterLowFrequencyList.clear();
        yearFrequencyList.clear();
        yearLowFrequencyList.clear();
        phoneNumbList.clear();
        formatFreqList.clear();
        fraudList.clear();

    }

    private IndicatorUnit[] initChildIndicatorUnits(List<IndicatorUnit> tempList, IndicatorUnit[] indicatorUnits) {
        for (IndicatorUnit unit : indicatorUnits) {
            // MOD gdbu TDQ-4261 2011-12-21 : Here we should only add the indicator,needn't to add the indicator's
            // classification.

            if (unit.getChildren() != null) {
                // MOD qiongli TDQ-6211 2012-11-27 IQRIndicator and RangeIndicator should add itself.
                IndicatorEnum type = unit.getType();
                if (type != null && (type == IndicatorEnum.IQRIndicatorEnum || type == IndicatorEnum.RangeIndicatorEnum)) {
                    tempList.add(unit);
                }
                initChildIndicatorUnits(tempList, unit.getChildren());
            } else {
                tempList.add(unit);
            }
            // ~TDQ-4261
        }

        return tempList.toArray(new IndicatorUnit[tempList.size()]);
    }

    public Map<EIndicatorChartType, List<IndicatorUnit>> getIndicatorComposite(ModelElementIndicator modelElementIndicator) {
        this.clear();
        List<IndicatorUnit> tempList = new ArrayList<IndicatorUnit>();
        this.indicatorUnits = initChildIndicatorUnits(tempList, modelElementIndicator.getIndicatorUnits());
        for (IndicatorUnit one : indicatorUnits) {

            switch (one.getType()) {
            case RowCountIndicatorEnum:
            case NullCountIndicatorEnum:
            case DistinctCountIndicatorEnum:
            case UniqueIndicatorEnum:
            case DuplicateCountIndicatorEnum:
            case BlankCountIndicatorEnum:
            case DefValueCountIndicatorEnum:
                simpleList.add(one);
                break;
            case MinLengthIndicatorEnum:
            case MinLengthWithNullIndicatorEnum:
            case MinLengthWithBlankIndicatorEnum:
            case MinLengthWithBlankNullIndicatorEnum:
            case MaxLengthIndicatorEnum:
            case MaxLengthWithNullIndicatorEnum:
            case MaxLengthWithBlankIndicatorEnum:
            case MaxLengthWithBlankNullIndicatorEnum:
            case AverageLengthIndicatorEnum:
            case AverageLengthWithNullIndicatorEnum:
            case AverageLengthWithBlankIndicatorEnum:
            case AverageLengthWithNullBlankIndicatorEnum:
                textList.add(one);
                break;
            case FrequencyIndicatorEnum:
                frequencyList.add(one);
                break;
            // MOD yyi 2010-10-08 16081
            case WeekFrequencyIndicatorEnum:
                weekFrequencyList.add(one);
                break;
            case MonthFrequencyIndicatorEnum:
                monthFrequencyList.add(one);
                break;
            case QuarterFrequencyIndicatorEnum:
                quarterFrequencyList.add(one);
                break;
            case YearFrequencyIndicatorEnum:
                yearFrequencyList.add(one);
                break;
            case DateFrequencyIndicatorEnum:
                dateFrequencyList.add(one);
                break;
            case BinFrequencyIndicatorEnum:
                binFrequencyList.add(one);
                break;
            case LowFrequencyIndicatorEnum:
                lowFrequencyList.add(one);
                break;
            case WeekLowFrequencyIndicatorEnum:
                weekLowFrequencyList.add(one);
                break;
            case MonthLowFrequencyIndicatorEnum:
                monthLowFrequencyList.add(one);
                break;
            case QuarterLowFrequencyIndicatorEnum:
                quarterLowFrequencyList.add(one);
                break;
            case YearLowFrequencyIndicatorEnum:
                yearLowFrequencyList.add(one);
                break;
            case DateLowFrequencyIndicatorEnum:
                dateLowFrequencyList.add(one);
                break;
            // ~
            case BinLowFrequencyIndicatorEnum:
                binLowFrequencyList.add(one);
                break;
            // ~ MOD mzhao 2009-03-23 Feature 6307
            case SoundexIndicatorEnum:
                soundexFrequencyList.add(one);
                break;
            case SoundexLowIndicatorEnum:
                soundexLowFrequencyList.add(one);
                break;
            // ~
            case PatternFreqIndicatorEnum:
                patternFrequencylist.add(one);
                break;
            case PatternLowFreqIndicatorEnum:
                patternLowFrequencyList.add(one);
                break;
            case EastAsiaPatternFreqIndicatorEnum:
                eastAsiaPatternFrequencylist.add(one);
                break;
            case EastAsiaPatternLowFreqIndicatorEnum:
                eastAsiaPatternLowFrequencyList.add(one);
                break;
            case DatePatternFreqIndicatorEnum:
                datePatternFrequencyList.add(one);
                break;
            case IQRIndicatorEnum:
            case RangeIndicatorEnum:
            case MeanIndicatorEnum:
            case MinValueIndicatorEnum:
            case MaxValueIndicatorEnum:
            case MedianIndicatorEnum:
            case LowerQuartileIndicatorEnum:
            case UpperQuartileIndicatorEnum:
                summaryList.add(one);
                break;
            case RegexpMatchingIndicatorEnum:
                patternList.add(one);
                break;

            case SqlPatternMatchingIndicatorEnum:
                sqlPatternList.add(one);
                break;

            case ModeIndicatorEnum:
                modelIndicatorList.add(one);
                break;

            case UserDefinedIndicatorEnum:
                IndicatorCategory ic = UDIUtils.getUDICategory(one);
                if (DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory().equals(ic)) {
                    udiCountList.add(one);
                } else if (DefinitionHandler.getInstance().getUserDefinedMatchIndicatorCategory().equals(ic)) {
                    udiMatchingList.add(one);
                } else if (DefinitionHandler.getInstance().getUserDefinedFrequencyIndicatorCategory().equals(ic)) {
                    udiFrequencyList.add(one);
                } else if (DefinitionHandler.getInstance().getUserDefinedRealValueIndicatorCategory().equals(ic)) {
                    udiRealValueList.add(one);
                }
                break;
            case ValidPhoneCountIndicatorEnum:
            case PossiblePhoneCountIndicatorEnum:
            case ValidRegCodeCountIndicatorEnum:
            case InvalidRegCodeCountIndicatorEnum:
            case WellFormE164PhoneCountIndicatorEnum:
            case WellFormIntePhoneCountIndicatorEnum:
            case WellFormNationalPhoneCountIndicatorEnum:
                phoneNumbList.add(one);
                break;
            case FormatFreqPieIndictorEnum:
                formatFreqList.add(one);
                break;
            case BenfordLawFrequencyIndicatorEnum:
                fraudList.add(one);
                break;
            default:
            }
        }

        separatedMap.put(EIndicatorChartType.SIMPLE_STATISTICS, simpleList);
        separatedMap.put(EIndicatorChartType.TEXT_STATISTICS, textList);
        separatedMap.put(EIndicatorChartType.FREQUENCE_STATISTICS, frequencyList);
        separatedMap.put(EIndicatorChartType.LOW_FREQUENCE_STATISTICS, lowFrequencyList);
        // MOD klliu 2010-08-06 bug 14695
        separatedMap.put(EIndicatorChartType.BIN_FREQUENCE_STATISTICS, binFrequencyList);
        separatedMap.put(EIndicatorChartType.BIN_LOW_FREQUENCE_STATISTICS, binLowFrequencyList);

        // MOD yyi 2010-10-08 16081
        separatedMap.put(EIndicatorChartType.DATE_FREQUENCE_STATISTICS, dateFrequencyList);
        separatedMap.put(EIndicatorChartType.DATE_LOW_FREQUENCE_STATISTICS, dateLowFrequencyList);
        separatedMap.put(EIndicatorChartType.WEEK_FREQUENCE_STATISTICS, weekFrequencyList);
        separatedMap.put(EIndicatorChartType.WEEK_LOW_FREQUENCE_STATISTICS, weekLowFrequencyList);
        separatedMap.put(EIndicatorChartType.MONTH_FREQUENCE_STATISTICS, monthFrequencyList);
        separatedMap.put(EIndicatorChartType.MONTH_LOW_FREQUENCE_STATISTICS, monthLowFrequencyList);
        separatedMap.put(EIndicatorChartType.QUARTER_FREQUENCE_STATISTICS, quarterFrequencyList);
        separatedMap.put(EIndicatorChartType.QUARTER_LOW_FREQUENCE_STATISTICS, quarterLowFrequencyList);
        separatedMap.put(EIndicatorChartType.YEAR_FREQUENCE_STATISTICS, yearFrequencyList);
        separatedMap.put(EIndicatorChartType.YEAR_LOW_FREQUENCE_STATISTICS, yearLowFrequencyList);
        // ~

        separatedMap.put(EIndicatorChartType.SOUNDEX_FREQUENCY_TABLE, soundexFrequencyList);
        separatedMap.put(EIndicatorChartType.SOUNDEX_LOW_FREQUENCY_TABLE, soundexLowFrequencyList);
        separatedMap.put(EIndicatorChartType.PATTERN_FREQUENCE_STATISTICS, patternFrequencylist);
        separatedMap.put(EIndicatorChartType.PATTERN_LOW_FREQUENCE_STATISTICS, patternLowFrequencyList);
        separatedMap.put(EIndicatorChartType.EAST_ASIA_PATTERN_FREQUENCE_STATISTICS, eastAsiaPatternFrequencylist);
        separatedMap.put(EIndicatorChartType.EAST_ASIA_PATTERN_LOW_FREQUENCE_STATISTICS, eastAsiaPatternLowFrequencyList);
        separatedMap.put(EIndicatorChartType.DATE_PATTERN_FREQUENCE_STATISTICS, datePatternFrequencyList);
        separatedMap.put(EIndicatorChartType.SUMMARY_STATISTICS, summaryList);
        separatedMap.put(EIndicatorChartType.PATTERN_MATCHING, patternList);
        separatedMap.put(EIndicatorChartType.SQL_PATTERN_MATCHING, sqlPatternList);
        separatedMap.put(EIndicatorChartType.MODE_INDICATOR, modelIndicatorList);
        separatedMap.put(EIndicatorChartType.UDI_COUNT, udiCountList);
        separatedMap.put(EIndicatorChartType.UDI_FREQUENCY, udiFrequencyList);
        separatedMap.put(EIndicatorChartType.UDI_MATCHING, udiMatchingList);
        separatedMap.put(EIndicatorChartType.UDI_REALVALUE, udiRealValueList);
        separatedMap.put(EIndicatorChartType.PHONE_NUMBER_STATISTICS, phoneNumbList);
        separatedMap.put(EIndicatorChartType.FORMAT_FREQ_PIE_STATISTICS, formatFreqList);
        separatedMap.put(EIndicatorChartType.BENFORD_LAW_STATISTICS, fraudList);

        return separatedMap;
    }

    public Map<EIndicatorChartType, List<TableIndicatorUnit>> getTableIndicatorComposite(TableIndicator tableIndicator) {
        this.clear();
        List<TableIndicatorUnit> tempList = new ArrayList<TableIndicatorUnit>();
        this.tableIndicatorUnits = initChildTableIndicatorUnits(tempList, tableIndicator.getIndicatorUnits());
        for (TableIndicatorUnit one : tableIndicatorUnits) {
            switch (one.getType()) {
            case WhereRuleIndicatorEnum:
            case RowCountIndicatorEnum:
                tableWhereRuleList.add(one);
                break;
            default:
            }
        }

        tableSeparatedMap.put(EIndicatorChartType.SIMPLE_STATISTICS, tableSimpleList);
        tableSeparatedMap.put(EIndicatorChartType.WHERERULE_INDICATOR, tableWhereRuleList);

        return tableSeparatedMap;
    }

    private TableIndicatorUnit[] initChildTableIndicatorUnits(List<TableIndicatorUnit> tempList,
            TableIndicatorUnit[] indicatorUnits) {
        for (TableIndicatorUnit unit : indicatorUnits) {
            if (unit.getChildren() != null) {
                initChildTableIndicatorUnits(tempList, unit.getChildren());
            } else {
                tempList.add(unit);
            }
        }
        return tempList.toArray(new TableIndicatorUnit[tempList.size()]);
    }

}
