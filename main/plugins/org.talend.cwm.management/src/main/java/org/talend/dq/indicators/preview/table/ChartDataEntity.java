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
package org.talend.dq.indicators.preview.table;

import java.sql.Types;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.utils.format.StringFormatUtil;
import org.talend.utils.sql.Java2SqlType;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class ChartDataEntity {

    private static Logger log = Logger.getLogger(ChartDataEntity.class);

    private String label;

    private String value;

    protected Indicator indicator;

    private Double percent;

    private Boolean outOfRange = null;

    protected String range;

    private boolean labelNull;

    private boolean isPercent;

    // MOD mzhao 2009-03-24, feature 6307 Show soundex distinct count and count
    // label.
    private Object key = null;

    public ChartDataEntity() {

    }

    public ChartDataEntity(Indicator indicator, String label, String value) {
        this(indicator, label, value, null, false);
    }

    public ChartDataEntity(Indicator indicator, String label, String value, Double percent, boolean labelNull) {
        this.label = label;
        this.value = value;
        this.percent = percent;
        this.labelNull = labelNull;
        this.indicator = indicator;
    }

    /**
     * Getter for labelNull.
     * 
     * @return true if the given label represents a null value
     */
    public boolean isLabelNull() {
        return this.labelNull;
    }

    /**
     * Sets the labelNull.
     * 
     * @param labelNull set to true if the label represents the null value
     */
    public void setLabelNull(boolean labelNull) {
        this.labelNull = labelNull;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // MOD mzhao 2009-03-24, feature 6307 Show soundex distinct count and count
    // label.
    public Object getKey() {
        return key;
    }

    // MOD mzhao 2009-03-24, feature 6307 Show soundex distinct count and count
    // label.
    public void setKey(Object key) {
        this.key = key;
    }

    public String getPersent() {
        if (percent != null) {
            // MOD qiongli 2011-4-25 bug 20670:if it is infinite,return N/A.
            if (Double.isNaN(percent) || Double.isInfinite(percent)) {
                return PluginConstant.NA_STRING;
            } else {
                return StringFormatUtil.format(percent, StringFormatUtil.PERCENT).toString();
            }
        } else {
            return null;
        }
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    /**
     * Getter for isPercent.
     * 
     * @return the isPercent
     */
    public boolean isPercent() {
        return this.isPercent;
    }

    /**
     * check the inString is a validate value.
     * 
     * @param inString
     * @return
     */
    public boolean isOutOfValue(String inString) {

        if (inString.equals(PluginConstant.NA_STRING)) { // $NON-NLS-1$
            return true;
        }

        boolean isOutOfValue = false;

        // the default value check
        isPercent = inString.indexOf('%') > 0; // $NON-NLS-1$
        if (isPercent) {
            NumberFormat nFromat = NumberFormat.getPercentInstance();
            try {
                Number number = nFromat.parse(inString);

                if (number instanceof Double) {
                    Double doubleString = (Double) number;
                    if (doubleString < 0 || doubleString > 1) {
                        isOutOfValue = true;
                    }
                } else if (number instanceof Long) {
                    Long longString = (Long) number;
                    if (longString < 0 || longString > 1) {
                        isOutOfValue = true;
                    }
                }
            } catch (ParseException e) {
                isOutOfValue = false;
            }
        } else {
            Double douString = Double.valueOf(inString);
            if (douString < 0) {
                isOutOfValue = true;
            }
        }
        return isOutOfValue;
    }

    /**
     * DOC Zqin Comment method "isOutOfRange".
     * 
     * @return
     */
    public boolean isOutOfRange(String inputValue) {
        outOfRange = false;

        if (inputValue == null || indicator == null) {
            return false;
        }

        IndicatorEnum indicatorEnum = IndicatorEnum.findIndicatorEnum(indicator.eClass());

        if (indicatorEnum != null) {
            switch (indicatorEnum) {
            case ModeIndicatorEnum:
                String expectedValue = IndicatorHelper.getExpectedValue(indicator);
                if (expectedValue != null) {

                    Boolean ignoreCaseOption = IndicatorHelper.ignoreCaseOption(indicator);
                    // MOD sgandon 01/04/2010 bug 12086 : ignoreCaseOption was not checked for null value
                    if (ignoreCaseOption != null && ignoreCaseOption) {
                        outOfRange = !(ignoreCaseOption && StringUtils.equalsIgnoreCase(value, expectedValue));
                    } else {
                        outOfRange = !StringUtils.equals(value, expectedValue);
                    }
                }
                break;
            default:

                outOfRange = checkRange(inputValue);
            }
        }

        return outOfRange;
    }

    /**
     * Get threshold by the input string.
     * 
     * @param inString
     * @return
     */
    protected String[] getDefinedRange(String inString) {
        isPercent = inString.indexOf('%') > 0;
        // MOD qiongli 2011-11-25 TDQ-4033,don't need to get the DataThreshold.
        String[] threshold = isPercent ? IndicatorHelper.getIndicatorThresholdInPercent(indicator) : IndicatorHelper
                .getIndicatorThreshold(indicator);
        return threshold;
    }

    private boolean checkRange(String inString) {

        String[] definedRange = getDefinedRange(inString);
        if (definedRange != null && definedRange.length >= 2) {

            range = "[" + definedRange[0] + "," + definedRange[1] + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            ModelElement temp = indicator.getAnalyzedElement();
            int sqltype = Types.INTEGER;
            if (null != temp) {
                int tempType = sqltype;
                if (temp instanceof TdColumn) {
                    // MOD qiongli 2011-11-15,TDQ-3690.it should be set value to 'tempType' not 'sqltype' at here.
                    tempType = ((TdColumn) temp).getSqlDataType().getJavaDataType();
                }
                sqltype = temp instanceof TdTable ? Types.INTEGER : tempType;
            }
            boolean isChildOfRange = IndicatorsPackage.eINSTANCE.getValueIndicator().isSuperTypeOf(indicator.eClass());
            if (Java2SqlType.isDateInSQL(sqltype) && isChildOfRange) {

                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
                    Date dValue = format.parse(value);

                    if ("".equals(definedRange[0])) { //$NON-NLS-1$
                        Date max = format.parse(definedRange[1]);
                        range = "[*, " + definedRange[1] + "]"; //$NON-NLS-1$ //$NON-NLS-2$
                        return dValue.after(max);
                    } else if ("".equals(definedRange[1])) { //$NON-NLS-1$
                        Date min = format.parse(definedRange[0]);
                        range = "[" + definedRange[0] + ", *]"; //$NON-NLS-1$ //$NON-NLS-2$
                        return dValue.before(min);
                    } else {
                        Date min = format.parse(definedRange[0]);
                        Date max = format.parse(definedRange[1]);
                        return dValue.after(max) || dValue.before(min);
                    }
                } catch (Exception e) {
                    log.error(e, e);
                    return false;
                }

            } else {
                Double min = StringFormatUtil.formatFourDecimalDouble(definedRange[0]);
                Double max = StringFormatUtil.formatFourDecimalDouble(definedRange[1]);

                // handle min and max
                Double dValue = inString != null ? StringFormatUtil.parseDouble(inString) : Double.NaN;
                if (min == null || Double.isNaN(min)) {
                    min = Double.NEGATIVE_INFINITY;
                }

                if (max == null || Double.isNaN(max)) {
                    max = Double.POSITIVE_INFINITY;
                }

                if (isPercent) {
                    return dValue < min * 100 || dValue > max * 100;
                }
                return dValue < min || dValue > max;
            }
        }

        return false;
    }

    /**
     * DOC Zqin Comment method "getRangeAsString".
     * 
     * @return retrun the message when indicator value out the defined range.
     */
    public String getRangeAsString() {

        StringBuilder msg = new StringBuilder();

        if (indicator != null) {
            IndicatorEnum indicatorEnum = IndicatorEnum.findIndicatorEnum(indicator.eClass());

            // TDQ-10785: when the data is too long, add a tooltip to show the first 200 characters.
            if (indicatorEnum == IndicatorEnum.PatternFreqIndicatorEnum
                    || indicatorEnum == IndicatorEnum.PatternLowFreqIndicatorEnum
                    || indicatorEnum == IndicatorEnum.FrequencyIndicatorEnum
                    || indicatorEnum == IndicatorEnum.LowFrequencyIndicatorEnum
                    || indicatorEnum == IndicatorEnum.SoundexIndicatorEnum
                    || indicatorEnum == IndicatorEnum.SoundexLowIndicatorEnum
                    || indicatorEnum == IndicatorEnum.EastAsiaPatternLowFreqIndicatorEnum
                    || indicatorEnum == IndicatorEnum.EastAsiaPatternFreqIndicatorEnum
                    || indicatorEnum == IndicatorEnum.CSWordPatternFreqIndicatorEnum
                    || indicatorEnum == IndicatorEnum.CSWordPatternLowFreqIndicatorEnum
                    || indicatorEnum == IndicatorEnum.CIWordPatternFreqIndicatorEnum
                    || indicatorEnum == IndicatorEnum.CIWordPatternLowFreqIndicatorEnum) {
                if (key != null) {
                    String keyLabel = key.toString();
                    long keyLabelCPCount = keyLabel.codePoints().count();
                    if (keyLabelCPCount > 30) {
                        msg.append(keyLabelCPCount > 200 ? keyLabel.substring(0,
                                keyLabel.offsetByCodePoints(0, 200))
                                + "...(" + keyLabelCPCount + " characters)" : keyLabel); //$NON-NLS-1$ //$NON-NLS-2$
                    }

                }
            } else if (indicatorEnum == IndicatorEnum.ModeIndicatorEnum) {
                msg.append("This value differs from the expected value: \"" + IndicatorHelper.getExpectedValue(indicator) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            } else if (indicatorEnum == IndicatorEnum.BoxIIndicatorEnum) {
                if (isOutOfRange(getValue())) {
                    msg.append("This value is outside the expected data's thresholds: " + range); //$NON-NLS-1$
                }
            } else {
                if (isOutOfRange(getValue())) {
                    msg.append("This value is outside the expected indicator's thresholds: " + range); //$NON-NLS-1$
                    msg.append("\n"); //$NON-NLS-1$
                }
                if (isOutOfRange(getPersent())) {
                    msg.append("This value is outside the expected indicator's thresholds in percent: " + range); //$NON-NLS-1$
                }
            }
        }

        return msg.length() == 0 ? null : msg.toString();
    }

}
