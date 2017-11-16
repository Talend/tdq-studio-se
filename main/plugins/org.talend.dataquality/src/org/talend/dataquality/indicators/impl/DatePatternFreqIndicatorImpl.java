/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.osgi.framework.Bundle;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.indicators.DatePatternFreqIndicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.mapdb.DBMap;
import org.talend.dataquality.matching.date.pattern.DatePatternRetriever;
import org.talend.dataquality.matching.date.pattern.ModelMatcher;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Date Pattern Freq Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DatePatternFreqIndicatorImpl extends FrequencyIndicatorImpl implements DatePatternFreqIndicator {

    private static final String PATTERNS_FILENAME = "PatternsNameAndRegularExpressions.txt";

    private DatePatternRetriever dateRetriever;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DatePatternFreqIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.DATE_PATTERN_FREQ_INDICATOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.PatternFreqIndicatorImpl#prepare()
     */
    @Override
    public boolean prepare() {
        initDateRetriever();
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
            // the datePattern only for DelimitedFile connection in DatePatternFreqIndicator.
            this.datePattern = pattern;
        }
        return flag;
    }

    /**
     * Extract it from 'prepare()'.initialize DatePatternRetriever.
     */
    private void initDateRetriever() {
        dateRetriever = new DatePatternRetriever();
        URL url = null;
        if (Platform.isRunning()) {
            Bundle bundle = Platform.getBundle("org.talend.dataquality.matching"); //$NON-NLS-1$
            url = bundle.getResource(PATTERNS_FILENAME);
            String filepath = null;
            try {
                filepath = FileLocator.toFileURL(url).getFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file = new File(filepath);
            dateRetriever.initModel2Regex(file);
        } else {
            InputStream inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PATTERNS_FILENAME);
            dateRetriever.initModel2Regex(inStream);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.PatternFreqIndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        mustStoreRow = false;
        if (data != null) {
            // MOD qiongli 2011-11-11 TDQ-3864,format the date for file connection.
            if (data instanceof Date && !StringUtils.isEmpty(this.datePattern)) {
                data = DateFormatUtils.format((Date) data, datePattern);
            }
            List<ModelMatcher> findMatchers = dateRetriever.findMatchers(String.valueOf(data));
            for (ModelMatcher matcher : findMatchers) {
                if (matcher != null) {
                    data = matcher.getModel();
                    matcher.increment();
                    mustStoreRow = mustStoreRow || this.checkMustStoreCurrentRow(Long.valueOf(matcher.getScore() - 1));

                }
            }
        } else {
            nullCount++;
        }
        count++;

        return true;
    }

    @Override
    public Double getFrequency(Object dataValue) {
        if (this.count.compareTo(0L) == 0) {
            return Double.NaN;
        }
        ModelMatcher matcher = null;
        if (dataValue instanceof ModelMatcher) {
            matcher = (ModelMatcher) dataValue;
            return ((double) matcher.getScore()) / this.getCount().longValue();
        } else {
            return super.getFrequency(dataValue);
        }

    }

    @Override
    public boolean finalizeComputation() {
        List<ModelMatcher> modelMatchers = dateRetriever.getModelMatchers();
        HashMap<Object, Long> map = new HashMap<Object, Long>();
        for (ModelMatcher matcher : modelMatchers) {
            map.put(matcher.getModel(), (long) matcher.getScore());
        }
        // this clear is necessary, because in the map contains the parent'result.
        getMapForFreq().clear();
        getMapForFreq().putAll(map);
        return super.finalizeComputation();
    }

    @Override
    public List<ModelMatcher> getModelMatcherList() {
        return dateRetriever.getModelMatchers();
    }

    /**
     * return List for ModelMatcher which Score more than 1.
     * 
     * @deprecated use {@link #getResult()} instead
     */
    @Deprecated
    @Override
    public List<Object> getRealModelMatcherList() {
        List<Object> realModelMatcherList = new ArrayList<Object>();
        for (ModelMatcher matcher : dateRetriever.getModelMatchers()) {
            if (matcher.getScore() > 0) {
                realModelMatcherList.add(matcher);
            }
        }
        return realModelMatcherList;
    }

    @Override
    public String getModel(Object matcher) {
        if (matcher instanceof ModelMatcher) {
            return ((ModelMatcher) matcher).getModel();
        } else {
            return null;
        }
    }

    @Override
    public int getScore(Object matcher) {
        if (matcher instanceof ModelMatcher) {
            return ((ModelMatcher) matcher).getScore();
        } else {
            return -1;
        }
    }

    /**
     * 
     * DOC zshen Comment method "getRegex".
     * 
     * @param model the model of matcher.
     * @return if can find corresponding to matcher return it's the Regex of matcher else return null;
     */
    @Override
    public String getRegex(String model) {
        // TDQ-9779. Avoid NPE.
        if (dateRetriever == null) {
            initDateRetriever();
        }
        return this.dateRetriever.getRegex(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.DatePatternFreqIndicator#getResult()
     */
    @Override
    public Map<String, Long> getResult() {
        Map<String, Long> result = new TreeMap<String, Long>();
        HashMap<Object, Long> values = this.getValueToFreq();
        // add the value which greater than zero into the result
        Iterator<Object> iterator = values.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Long value = values.get(key);
            if (value > 0) {
                result.put(key.toString(), value);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#handleDrillDownData(java.lang.Object,
     * java.util.List)
     */
    @Override
    public void handleDrillDownData(Object masterObject, List<Object> inputRowList) {
        // Only for DelimitedFile,format a date from 'Thu Jan 01 00:00:00 CST 1970' to datePattern like as '1970-01-01'
        Object data = masterObject;
        if (data instanceof Date && !StringUtils.isEmpty(this.datePattern)) {
            data = DateFormatUtils.format((Date) data, datePattern);
        }
        List<ModelMatcher> matchers = dateRetriever.findMatchers(String.valueOf(data));
        for (ModelMatcher matcher : matchers) {
            drillDownMap = (DBMap<Object, List<Object>>) getMapDB(matcher.getModel());
            // check the size of limite
            if (this.checkMustStoreCurrentRow(Long.valueOf(matcher.getScore() - 1))) {
                drillDownMap.put(matcher.getScore(), inputRowList);
            }
        }
    }

} // DatePatternFreqIndicatorImpl
