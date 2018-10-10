/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.emf.ecore.EClass;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.PatternFreqIndicator;
import org.talend.dataquality.indicators.TextParameters;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Pattern Freq Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class PatternFreqIndicatorImpl extends FrequencyIndicatorImpl implements PatternFreqIndicator {

    /**
     * Note that these two constant string are identical to whom in {
     * {@link org.talend.dataquality.statistics.frequency.recognition.AsciiCharPatternRecognition} in project
     * "org.talend.dataquality.statistics". But the project can not be used currently due to the JRE version differences
     * so here we keep another copy.
     */
    public static final String CHARS_TO_REPLACE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿßÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞ0123456789";

    public static final String REPLACEMENT_CHARS = "aaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999";

    private String charsToReplace = CHARS_TO_REPLACE;

    private String replacementChars = REPLACEMENT_CHARS;

    // Mark replace parameter whether has been setting by the user.
    private boolean hasBeanCustomized = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected PatternFreqIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.PATTERN_FREQ_INDICATOR;
    }

    /**
     * add by zshen for feature 12919 to convertCharacter when save data into analysisDataSet with java engin
     */
    @Override
    public String convertCharacters(String data) {
        return replaceCharacters(String.valueOf(data), this.charsToReplace, this.replacementChars);
    }

    @Override
    public boolean prepare() {
        // MOD xqliu 2009-06-29 bug 7068
        final TextParameters textParameter = this.getParameters() == null ? null : this.getParameters().getTextParameter();
        // ~
        if (textParameter != null) {

            // TDQ-10044: fix when the user didn't set the replace and charactersToReplace, use the default value(only
            // for jave engine)
            String replacementCharacters = textParameter.getReplacementCharacters();
            if (!StringUtils.isBlank(replacementCharacters)) {
                this.replacementChars = replacementCharacters;
                hasBeanCustomized = true;
            } else {
                replacementChars = REPLACEMENT_CHARS;
                hasBeanCustomized = false;
            }

            String charactersToReplace = textParameter.getCharactersToReplace();
            if (!StringUtils.isBlank(charactersToReplace)) {
                this.charsToReplace = charactersToReplace;
            } else {
                charsToReplace = CHARS_TO_REPLACE;
            }
            // TDQ-10044~

        }
        return super.prepare();
    }

    @Override
    public boolean reset() {
        boolean flag = super.reset();
        // set date pattern only for delimited file
        MetadataColumn mdColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(this.getAnalyzedElement());
        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(this.getAnalyzedElement());
        if (tdColumn == null && mdColumn != null && "id_Date".equals(mdColumn.getTalendType())) {
            // get date pattern from the column
            String pattern = mdColumn.getPattern();
            if (StringUtils.isEmpty(pattern)) {
                pattern = "yyyy-MM-dd";
            } else {
                pattern = StringUtils.replace(pattern, "\"", StringUtils.EMPTY);
            }
            // the datePattern only for DelimitedFile connection in PatternFreqIndicator.
            this.datePattern = pattern;
        }
        return flag;
    }

    @Override
    public boolean handle(Object data) {
        if (data == null) {
            return super.handle(data);
        } else {
            // format the date for file connection.
            if (data instanceof Date && !StringUtils.isEmpty(this.datePattern)) {
                data = DateFormatUtils.format((Date) data, datePattern);
            }
            String parsedData = convertCharacters(String.valueOf(data));
            return super.handle(parsedData);
        }
    }

    /**
     * it is uesed to get mapDB name. it will be like "9999-99-99"
     * 
     * @param name
     * @return
     */
    @Override
    protected String getFormatName(Object name) {
        String formatDate = DateFormatUtils.format((Date) name, datePattern);
        return convertCharacters(formatDate);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#specialName(java.lang.Object)
     */
    @Override
    protected String getFrequencyLabel(Object name) {
        return convertCharacters(name.toString());
    }

    /**
     * Getter for hasBeanCustomized.
     * 
     * @return the hasBeanCustomized
     */
    protected boolean isBeanCustomized() {
        return hasBeanCustomized;
    }

} // PatternFreqIndicatorImpl
