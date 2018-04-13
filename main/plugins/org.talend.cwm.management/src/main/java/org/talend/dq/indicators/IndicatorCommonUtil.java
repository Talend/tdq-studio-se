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
package org.talend.dq.indicators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.indicators.AverageLengthIndicator;
import org.talend.dataquality.indicators.AvgLengthWithBlankIndicator;
import org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.AvgLengthWithNullIndicator;
import org.talend.dataquality.indicators.BlankCountIndicator;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.DefValueCountIndicator;
import org.talend.dataquality.indicators.DistinctCountIndicator;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.FormatFreqPieIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.InvalidRegCodeCountIndicator;
import org.talend.dataquality.indicators.LowerQuartileIndicator;
import org.talend.dataquality.indicators.MaxLengthIndicator;
import org.talend.dataquality.indicators.MaxLengthWithBlankIndicator;
import org.talend.dataquality.indicators.MaxLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.MaxLengthWithNullIndicator;
import org.talend.dataquality.indicators.MaxValueIndicator;
import org.talend.dataquality.indicators.MeanIndicator;
import org.talend.dataquality.indicators.MedianIndicator;
import org.talend.dataquality.indicators.MinLengthIndicator;
import org.talend.dataquality.indicators.MinLengthWithBlankIndicator;
import org.talend.dataquality.indicators.MinLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.MinLengthWithNullIndicator;
import org.talend.dataquality.indicators.MinValueIndicator;
import org.talend.dataquality.indicators.ModeIndicator;
import org.talend.dataquality.indicators.NullCountIndicator;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.PossiblePhoneCountIndicator;
import org.talend.dataquality.indicators.RangeIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.SoundexFreqIndicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;
import org.talend.dataquality.indicators.UpperQuartileIndicator;
import org.talend.dataquality.indicators.ValidPhoneCountIndicator;
import org.talend.dataquality.indicators.ValidRegCodeCountIndicator;
import org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.indicators.sql.WhereRuleIndicator;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.ext.PatternMatchingExt;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public final class IndicatorCommonUtil {

    private static Logger log = Logger.getLogger(IndicatorCommonUtil.class);

    private IndicatorCommonUtil() {

    }

    public static Object getIndicatorValue(Indicator indicator) {

        Object value = null;

        IndicatorEnum type = IndicatorEnum.findIndicatorEnum(indicator.eClass());

        if (type != null) {
            try {
                if (type == IndicatorEnum.RangeIndicatorEnum || type == IndicatorEnum.IQRIndicatorEnum) {
                    value = ((RangeIndicator) indicator).getRange();
                    ((RangeIndicator) indicator).setComputed(true);
                } else if (indicator.isComputed()) {

                    // log.warn("now getting the value of indicator [" + AnalysisExecutorHelper.getIndicatorName(indicator) +
                    // "]");

                    switch (type) {
                    case RowCountIndicatorEnum:
                        value = ((RowCountIndicator) indicator).getCount();
                        break;

                    case NullCountIndicatorEnum:
                        value = ((NullCountIndicator) indicator).getNullCount();
                        break;

                    case DistinctCountIndicatorEnum:
                        value = ((DistinctCountIndicator) indicator).getDistinctValueCount();
                        break;

                    case UniqueIndicatorEnum:
                        value = (((UniqueCountIndicator) indicator).getUniqueValueCount());
                        break;

                    case DuplicateCountIndicatorEnum:
                        value = ((DuplicateCountIndicator) indicator).getDuplicateValueCount();
                        break;

                    case BlankCountIndicatorEnum:
                        value = ((BlankCountIndicator) indicator).getBlankCount();
                        break;

                    case DefValueCountIndicatorEnum:
                        value = ((DefValueCountIndicator) indicator).getDefaultValCount();
                        break;

                    case MinLengthIndicatorEnum:
                        value = ((MinLengthIndicator) indicator).getLength();
                        break;

                    case MinLengthWithNullIndicatorEnum:
                        value = ((MinLengthWithNullIndicator) indicator).getLength();
                        break;

                    case MinLengthWithBlankIndicatorEnum:
                        value = ((MinLengthWithBlankIndicator) indicator).getLength();
                        break;

                    case MinLengthWithBlankNullIndicatorEnum:
                        value = ((MinLengthWithBlankNullIndicator) indicator).getLength();
                        break;

                    case MaxLengthIndicatorEnum:
                        value = ((MaxLengthIndicator) indicator).getLength();
                        break;

                    case MaxLengthWithNullIndicatorEnum:
                        value = ((MaxLengthWithNullIndicator) indicator).getLength();
                        break;

                    case MaxLengthWithBlankIndicatorEnum:
                        value = ((MaxLengthWithBlankIndicator) indicator).getLength();
                        break;

                    case MaxLengthWithBlankNullIndicatorEnum:
                        value = ((MaxLengthWithBlankNullIndicator) indicator).getLength();
                        break;

                    case AverageLengthIndicatorEnum:
                        value = ((AverageLengthIndicator) indicator).getAverageLength();
                        break;

                    case AverageLengthWithNullIndicatorEnum:
                        value = ((AvgLengthWithNullIndicator) indicator).getAverageLength();
                        break;

                    case AverageLengthWithBlankIndicatorEnum:
                        value = ((AvgLengthWithBlankIndicator) indicator).getAverageLength();
                        break;

                    case AverageLengthWithNullBlankIndicatorEnum:
                        value = ((AvgLengthWithBlankNullIndicator) indicator).getAverageLength();
                        break;

                    case FrequencyIndicatorEnum:
                    case DateFrequencyIndicatorEnum:
                    case WeekFrequencyIndicatorEnum:
                    case MonthFrequencyIndicatorEnum:
                    case QuarterFrequencyIndicatorEnum:
                    case YearFrequencyIndicatorEnum:
                    case BinFrequencyIndicatorEnum:
                    case LowFrequencyIndicatorEnum:
                    case DateLowFrequencyIndicatorEnum:
                    case WeekLowFrequencyIndicatorEnum:
                    case MonthLowFrequencyIndicatorEnum:
                    case QuarterLowFrequencyIndicatorEnum:
                    case YearLowFrequencyIndicatorEnum:
                    case BinLowFrequencyIndicatorEnum:
                    case PatternFreqIndicatorEnum:
                    case PatternLowFreqIndicatorEnum:
                    case EastAsiaPatternFreqIndicatorEnum:
                    case EastAsiaPatternLowFreqIndicatorEnum:
                    case DatePatternFreqIndicatorEnum:
                    case SoundexIndicatorEnum:
                    case SoundexLowIndicatorEnum:
                    case BenfordLawFrequencyIndicatorEnum:
                    case CSWordPatternFreqIndicatorEnum:
                    case CSWordPatternLowFreqIndicatorEnum:
                    case CIWordPatternFreqIndicatorEnum:
                    case CIWordPatternLowFreqIndicatorEnum:
                        value = handleFrequency(indicator);
                        break;

                    case MeanIndicatorEnum:
                        value = ((MeanIndicator) indicator).getMean();
                        break;

                    case MedianIndicatorEnum:
                        value = ((MedianIndicator) indicator).getMedian();
                        break;

                    case MinValueIndicatorEnum:
                        value = ((MinValueIndicator) indicator).getValue();
                        break;

                    case MaxValueIndicatorEnum:
                        value = ((MaxValueIndicator) indicator).getValue();
                        break;

                    case LowerQuartileIndicatorEnum:
                        value = ((LowerQuartileIndicator) indicator).getValue();
                        break;

                    case UpperQuartileIndicatorEnum:
                        value = ((UpperQuartileIndicator) indicator).getValue();
                        break;

                    case RegexpMatchingIndicatorEnum:
                    case SqlPatternMatchingIndicatorEnum:
                    case AllMatchIndicatorEnum:
                        value = handleMatchingValue(indicator);
                        break;

                    case ModeIndicatorEnum:
                        value = ((ModeIndicator) indicator).getMode();
                        break;

                    case UserDefinedIndicatorEnum:
                        value = handleUDIValue(indicator);
                        break;

                    case WhereRuleIndicatorEnum:
                        Long userCount = ((WhereRuleIndicator) indicator).getUserCount();
                        value = userCount == null ? 0 : userCount;
                        break;
                    // MOD qiongli 2011-7-21 feature 22362
                    case ValidPhoneCountIndicatorEnum:
                        value = ((ValidPhoneCountIndicator) indicator).getValidPhoneNumCount();
                        break;
                    case ValidRegCodeCountIndicatorEnum:
                        value = ((ValidRegCodeCountIndicator) indicator).getValidRegCount();
                        break;
                    case InvalidRegCodeCountIndicatorEnum:
                        value = ((InvalidRegCodeCountIndicator) indicator).getInvalidRegCount();
                        break;
                    case WellFormE164PhoneCountIndicatorEnum:
                        value = ((WellFormE164PhoneCountIndicator) indicator).getWellFormE164PhoneCount();
                        break;
                    case WellFormIntePhoneCountIndicatorEnum:
                        value = ((WellFormIntePhoneCountIndicator) indicator).getWellFormIntePhoneCount();
                        break;
                    case WellFormNationalPhoneCountIndicatorEnum:
                        value = ((WellFormNationalPhoneCountIndicator) indicator).getWellFormNatiPhoneCount();
                        break;
                    case PossiblePhoneCountIndicatorEnum:
                        value = ((PossiblePhoneCountIndicator) indicator).getPossiblePhoneCount();
                        break;
                    case FormatFreqPieIndictorEnum:
                        value = handleFreqPie(indicator);

                    default:

                    }

                    if (value == null || "null".equalsIgnoreCase(value.toString())) { //$NON-NLS-1$
                        indicator.setComputed(false);
                    }

                }
            } catch (Exception e) {
                log.error(Messages.getString("IndicatorCommonUtil.FailValue", e.getMessage()), e); //$NON-NLS-1$
            }
        }

        return value;
    }

    /**
     * DOC xqliu Comment method "handleFrequency".
     * 
     * @param indicator
     * @return
     */
    private static Object handleFrequency(Indicator indicator) {
        FrequencyExt[] frequencyExt = null;
        if (UDIHelper.isUDI(indicator)) {
            UserDefIndicator udi = (UserDefIndicator) indicator;
            Set<Object> valueSet = udi.getDistinctValues();
            if (valueSet == null) {
                return null;
            }

            frequencyExt = new FrequencyExt[valueSet.size()];

            int i = 0;
            for (Object o : valueSet) {
                frequencyExt[i] = new FrequencyExt();
                frequencyExt[i].setKey(o);
                frequencyExt[i].setValue(udi.getCount(o));
                frequencyExt[i].setFrequency(udi.getFrequency(o));
                i++;
            }
        } else if (IndicatorEnum.DatePatternFreqIndicatorEnum.getIndicatorType().isInstance(indicator)) {
            DatePatternFreqIndicator datePatternFrequency = (DatePatternFreqIndicator) indicator;
            Map<String, Long> results = datePatternFrequency.getResult();
            frequencyExt = new FrequencyExt[results.size()];
            int i = 0;
            for (String key : results.keySet()) {
                Long value = results.get(key);
                Double frequency = datePatternFrequency.getFrequency(key);
                frequencyExt[i] = new FrequencyExt();
                frequencyExt[i].setKey(key);
                frequencyExt[i].setValue(value);
                frequencyExt[i].setFrequency(frequency);
                i++;
            }
        } else {
            FrequencyIndicator frequency = (FrequencyIndicator) indicator;
            Set<Object> valueSet = frequency.getDistinctValues();
            if (valueSet == null) {
                return null;
            }

            frequencyExt = new FrequencyExt[valueSet.size()];

            int i = 0;
            for (Object o : valueSet) {
                frequencyExt[i] = new FrequencyExt();
                frequencyExt[i].setKey(o);
                if (IndicatorsPackage.eINSTANCE.getSoundexFreqIndicator().equals(frequency.eClass())
                        || IndicatorsPackage.eINSTANCE.getSoundexLowFreqIndicator().equals(frequency.eClass())) {
                    // MOD scorreia 2009-03-23 display distinct count when working with Soundex
                    frequencyExt[i].setValue(((SoundexFreqIndicator) frequency).getDistinctCount(o));
                } else {
                    frequencyExt[i].setValue(frequency.getCount(o));
                }
                frequencyExt[i].setFrequency(frequency.getFrequency(o));
                i++;
            }
        }

        return frequencyExt;
    }

    /**
     * DOC xqliu Comment method "handleMatchingValue".
     * 
     * @param indicator
     * @return
     */
    public static PatternMatchingExt handleMatchingValue(Indicator indicator) {
        PatternMatchingExt patternExt = new PatternMatchingExt();
        if (UDIHelper.isUDI(indicator)) {
            UserDefIndicator udi = (UserDefIndicator) indicator;
            if (udi.getMatchingValueCount() != null) {
                patternExt.setMatchingValueCount(udi.getMatchingValueCount());
            }
            if (udi.getNotMatchingValueCount() != null) {
                patternExt.setNotMatchingValueCount(udi.getNotMatchingValueCount());
            }
        } else {
            // TDQ-12282 msjian : fix a NPE
            Long matchingValueCount = ((PatternMatchingIndicator) indicator).getMatchingValueCount();
            patternExt.setMatchingValueCount(matchingValueCount == null ? 0l : matchingValueCount);
            Long notMatchingValueCount = ((PatternMatchingIndicator) indicator).getNotMatchingValueCount();
            patternExt.setNotMatchingValueCount(notMatchingValueCount == null ? 0l : notMatchingValueCount);
        }
        return patternExt;
    }

    /**
     * DOC xqliu Comment method "handleUDIValue".
     * 
     * @param indicator
     * @return
     */
    private static Object handleUDIValue(Indicator indicator) {
        Object object = null;
        if (UDIHelper.isCount(indicator)) {
            object = ((UserDefIndicator) indicator).getUserCount();
            // Added yyin 20130104, TDQ-5890, when the user count is null, use the count.
            if (object == null) {
                object = ((UserDefIndicator) indicator).getCount();
            }// ~
        } else if (UDIHelper.isFrequency(indicator)) {
            object = handleFrequency(indicator);
        } else if (UDIHelper.isMatching(indicator)) {
            object = handleMatchingValue(indicator);
        } else if (UDIHelper.isRealValue(indicator)) {
            object = ((UserDefIndicator) indicator).getRealValue();
        }
        return object;
    }

    /**
     * 
     * DOC qiongli Comment method "handleFreqPie".
     * 
     * @param indicator
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static Object handleFreqPie(Indicator indicator) {
        FrequencyExt[] frequencyExt = null;
        FormatFreqPieIndicator formatFreqIndicator = (FormatFreqPieIndicator) indicator;
        HashMap<Object, Long> valueToFreq = formatFreqIndicator.getValueToFreq();
        if (valueToFreq == null) {
            return null;
        }
        int i = 0;
        frequencyExt = new FrequencyExt[valueToFreq.size()];
        Iterator iter = valueToFreq.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            frequencyExt[i] = new FrequencyExt();
            Object key = entry.getKey();
            long value = Long.valueOf(entry.getValue().toString());
            frequencyExt[i].setKey(key.toString());
            frequencyExt[i].setValue(value);
            frequencyExt[i].setFrequency(formatFreqIndicator.getFrequency(key));
            i++;
        }

        return frequencyExt;
    }

}
