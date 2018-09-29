/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.talend.commons.utils.SpecialValueDisplay;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.DBMap;
import org.talend.dataquality.indicators.mapdb.StandardDBName;
import org.talend.dataquality.indicators.mapdb.TalendFormatDate;
import org.talend.dataquality.indicators.mapdb.TalendFormatTime;
import org.talend.resource.ResourceManager;
import org.talend.utils.collections.MapValueSorter;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Frequency Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getUniqueValues <em>Unique Values</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDistinctValueCount <em>Distinct Value Count</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getUniqueValueCount <em>Unique Value Count</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getDuplicateValueCount <em>Duplicate Value Count</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getValueToFreq <em>Value To Freq</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FrequencyIndicatorImpl extends IndicatorImpl implements FrequencyIndicator {

    private static Logger log = Logger.getLogger(FrequencyIndicatorImpl.class);

    private static final DecimalFormat F4_DIGIT = new DecimalFormat("0000");

    private static final DecimalFormat F2_DIGIT = new DecimalFormat("00");

    protected String datePattern = null;

    private final String FREQUENCYMAPNAME = StandardDBName.computeProcess.name() + "frequency";

    /**
     * The cached value of the '{@link #getUniqueValues() <em>Unique Values</em>}' attribute list.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getUniqueValues()
     * @generated
     * @ordered
     */
    protected EList<Object> uniqueValues;

    /**
     * The default value of the '{@link #getDistinctValueCount() <em>Distinct Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDistinctValueCount()
     * @generated
     * @ordered
     */
    protected static final Long DISTINCT_VALUE_COUNT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDistinctValueCount() <em>Distinct Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDistinctValueCount()
     * @generated
     * @ordered
     */
    protected Long distinctValueCount = DISTINCT_VALUE_COUNT_EDEFAULT;

    /**
     * The default value of the '{@link #getUniqueValueCount() <em>Unique Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getUniqueValueCount()
     * @generated
     * @ordered
     */
    protected static final Long UNIQUE_VALUE_COUNT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUniqueValueCount() <em>Unique Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getUniqueValueCount()
     * @generated
     * @ordered
     */
    protected Long uniqueValueCount = UNIQUE_VALUE_COUNT_EDEFAULT;

    /**
     * The default value of the '{@link #getDuplicateValueCount() <em>Duplicate Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDuplicateValueCount()
     * @generated
     * @ordered
     */
    protected static final Long DUPLICATE_VALUE_COUNT_EDEFAULT = null;

    /**
     * The default value of the '{@link #getValueToFreq() <em>Value To Freq</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getValueToFreq()
     * @generated NOT
     * @ordered
     */
    protected final HashMap<Object, Long> VALUE_TO_FREQ_EDEFAULT = new HashMap<Object, Long>();

    /**
     * The cached value of the '{@link #getValueToFreq() <em>Value To Freq</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueToFreq()
     * @generated
     * @ordered
     */
    protected HashMap<Object, Long> valueToFreq = VALUE_TO_FREQ_EDEFAULT;

    /**
     * store the value of group when use MapDB.
     */

    protected Map<Object, Long> valueToFreqForMapDB = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected FrequencyIndicatorImpl() {
        super();
    }

    /**
     * init the ValueByGroupMap.
     * 
     * @return
     */
    private Map<Object, Long> initValueForFrequencyDBMap(String dbName) {
        if (isUsedMapDBMode()) {
            return new DBMap<Object, Long>(ResourceManager.getMapDBFilePath(), ResourceManager.getMapDBFileName(this),
                    ResourceManager.getMapDBCatalogName(this, dbName));
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.FREQUENCY_INDICATOR;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Object> getUniqueValues() {
        if (uniqueValues == null) {
            uniqueValues = new EDataTypeUniqueEList<Object>(Object.class, this, IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUES);
        }
        return uniqueValues;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Long getCount(Object dataValue) {
        if (dataValue == OTHER) {
            long counted = 0L;
            for (Object val : getValueToFreq().keySet()) {
                Long freq = getValueToFreq().get(val);
                counted = (freq == null) ? counted : counted + freq;
            }
            return (count != null && count > 0) ? count - counted : 0L;
        }
        Long freq = getValueToFreq().get(dataValue);
        return (freq == null) ? 0L : freq;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Double getFrequency(Object dataValue) {
        if (this.count.compareTo(0L) == 0) {
            return Double.NaN;
        }
        return ((double) getCount(dataValue)) / this.count.longValue();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Set<Object> getDistinctValues() {
        if (!distinctComputed) {
            computeDistinctValues();
        }
        return distinctValues;
    }

    /**
     * ADDED scorreia Method "computeDistinctValues" updates the distinctValues field and set distinctComputed field to
     * true.
     */
    private void computeDistinctValues() {
        this.distinctValues = getValueToFreq().keySet();
        this.setDistinctValueCount(Long.valueOf(distinctValues.size()));
        distinctComputed = true;
    }

    private Set<Object> distinctValues = null;

    /**
     * @generated
     */
    protected Long getDistinctValueCountGen() {
        return distinctValueCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Long getDistinctValueCount() {
        if (!distinctComputed) {
            computeDistinctValues();
        }
        return getDistinctValueCountGen();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDistinctValueCount(Long newDistinctValueCount) {
        Long oldDistinctValueCount = distinctValueCount;
        distinctValueCount = newDistinctValueCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT, oldDistinctValueCount, distinctValueCount));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getUniqueValueCount() {
        return uniqueValueCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setUniqueValueCount(Long newUniqueValueCount) {
        Long oldUniqueValueCount = uniqueValueCount;
        uniqueValueCount = newUniqueValueCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT, oldUniqueValueCount, uniqueValueCount));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Long getDuplicateValueCount() {
        return count - getUniqueValueCount();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public HashMap<Object, Long> getValueToFreqGen() {
        return valueToFreq;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.FrequencyIndicator#getValueToFreq()
     * 
     * @generated NOT
     */
    @Override
    public HashMap<Object, Long> getValueToFreq() {
        return getValueToFreqGen();
    }

    /**
     * 
     * When MapDB is valid will return BtreeMap else will return HashMap(valueToFreq)
     * 
     * @return
     */
    public Map<Object, Long> getMapForFreq() {
        if (isUsedMapDBMode()) {
            return valueToFreqForMapDB;
        }
        return getValueToFreq();
    }

    @Override
    public void setValueToFreq(HashMap<Object, Long> newValueToFreq) {
        this.distinctComputed = false;
        this.setValueToFreqGen(newValueToFreq);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setValueToFreqGen(HashMap<Object, Long> newValueToFreq) {
        HashMap<Object, Long> oldValueToFreq = valueToFreq;
        valueToFreq = newValueToFreq;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.FREQUENCY_INDICATOR__VALUE_TO_FREQ, oldValueToFreq, valueToFreq));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUES:
                return getUniqueValues();
            case IndicatorsPackage.FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT:
                return getDistinctValueCount();
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT:
                return getUniqueValueCount();
            case IndicatorsPackage.FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT:
                return getDuplicateValueCount();
            case IndicatorsPackage.FREQUENCY_INDICATOR__VALUE_TO_FREQ:
                return getValueToFreq();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUES:
                getUniqueValues().clear();
                getUniqueValues().addAll((Collection<? extends Object>)newValue);
                return;
            case IndicatorsPackage.FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT:
                setDistinctValueCount((Long)newValue);
                return;
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT:
                setUniqueValueCount((Long)newValue);
                return;
            case IndicatorsPackage.FREQUENCY_INDICATOR__VALUE_TO_FREQ:
                setValueToFreq((HashMap<Object, Long>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUES:
                getUniqueValues().clear();
                return;
            case IndicatorsPackage.FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT:
                setDistinctValueCount(DISTINCT_VALUE_COUNT_EDEFAULT);
                return;
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT:
                setUniqueValueCount(UNIQUE_VALUE_COUNT_EDEFAULT);
                return;
            case IndicatorsPackage.FREQUENCY_INDICATOR__VALUE_TO_FREQ:
                setValueToFreq(VALUE_TO_FREQ_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUES:
                return uniqueValues != null && !uniqueValues.isEmpty();
            case IndicatorsPackage.FREQUENCY_INDICATOR__DISTINCT_VALUE_COUNT:
                return DISTINCT_VALUE_COUNT_EDEFAULT == null ? distinctValueCount != null : !DISTINCT_VALUE_COUNT_EDEFAULT.equals(distinctValueCount);
            case IndicatorsPackage.FREQUENCY_INDICATOR__UNIQUE_VALUE_COUNT:
                return UNIQUE_VALUE_COUNT_EDEFAULT == null ? uniqueValueCount != null : !UNIQUE_VALUE_COUNT_EDEFAULT.equals(uniqueValueCount);
            case IndicatorsPackage.FREQUENCY_INDICATOR__DUPLICATE_VALUE_COUNT:
                return DUPLICATE_VALUE_COUNT_EDEFAULT == null ? getDuplicateValueCount() != null : !DUPLICATE_VALUE_COUNT_EDEFAULT.equals(getDuplicateValueCount());
            case IndicatorsPackage.FREQUENCY_INDICATOR__VALUE_TO_FREQ:
                return VALUE_TO_FREQ_EDEFAULT == null ? valueToFreq != null : !VALUE_TO_FREQ_EDEFAULT.equals(valueToFreq);
        }
        return super.eIsSet(featureID);
    }

    /**
     * true is distinct value count is computed
     */
    private boolean distinctComputed = false;

    @Override
    public boolean handle(Object data) {
        super.handle(data);
        Long freq = getMapForFreq().get(data);
        if (freq == null) { // new data
            freq = 0L;
            if (!isUsedMapDBMode()) {
                this.getUniqueValues().add(data);
            }
            this.uniqueValueCount++;
        } else { // data not new
            if (!isUsedMapDBMode()) {
                this.getUniqueValues().remove(data);
            }
            if (freq.compareTo(1L) == 0) { // decrement when data is seen twice
                this.uniqueValueCount--;
            }
        }
        if (this.checkMustStoreCurrentRow(freq)) {
            mustStoreRow = true;
        }
        freq++;
        // TODO scorreia compute distinct values ?
        // TODO scorreia handle options (for numeric values and date values)
        getMapForFreq().put(data, freq);
        return freq > 0;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#finalizeComputation()
     */
    @Override
    public boolean finalizeComputation() {
        final int topN = (parameters != null) ? parameters.getTopN() : PluginConstant.DEFAULT_TOP_N;
        List<Object> mostFrequent = getReducedValues(topN);
        HashMap<Object, Long> map = new HashMap<Object, Long>();
        for (Object object : mostFrequent) {
            map.put(object, getMapForFreq().get(object));
        }
        this.valueToFreq.clear();
        this.setValueToFreq(map);
        // this.distinctComputed = true;
        return super.finalizeComputation();
    }

    /**
     * get Reduced Values after reduce until n(the topN value).
     * 
     * @param n
     * @return
     */
    protected List<Object> getReducedValues(int n) {
        return new MapValueSorter().getMostFrequent(getMapForFreq(), n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#reset()
     */
    @Override
    public boolean reset() {
        this.uniqueValueCount = 0L;
        this.distinctValueCount = 0L;
        this.distinctComputed = false;
        this.datePattern = null;
        if (isUsedMapDBMode()) {
            if (checkAllowDrillDown()) {
                clearDrillDownMaps();
            }
            valueToFreqForMapDB = initValueForFrequencyDBMap(FREQUENCYMAPNAME);
            if (valueToFreqForMapDB != null && !valueToFreqForMapDB.isEmpty()) {
                valueToFreqForMapDB.clear();
            }
        } else {
            this.getValueToFreq().clear();
        }
        return super.reset();
    }

    /**
     * clear DrillDown Maps.
     * 
     * @param valueToFreqForMapDB2
     */
    @SuppressWarnings("unchecked")
    protected void clearDrillDownMaps() {
        AbstractDB<?> mapDB = getMapDB(StandardDBName.drillDown.name());
        if (mapDB != null) {
            mapDB.clearDB(ResourceManager.getMapDBCatalogName(this));
        }
    }

    /**
     * get Map DB Name.
     * 
     * @param name
     * 
     * @return String
     */
    protected String getDBName(Object name) {
        if (null == name) {
            return SpecialValueDisplay.NULL_FIELD;
        }
        if (StringUtils.EMPTY.equals(name)) {
            return SpecialValueDisplay.EMPTY_FIELD;
        } else {
            if (datePattern != null && name instanceof Date) {
                return getFormatName(name);
            } else {
                return getFrequencyLabel(name);
            }
        }
    }

    /**
     * Get the label of frequency item
     * 
     * @param name
     * @return
     */
    protected String getFrequencyLabel(Object name) {
        // TDQ-10833 format Date .like as :Date and Timsatamp(yyyy-MM-dd HH:mm:ss:SSS),Time(HH:MM:ss)
        if (name instanceof Date) {
            if (name instanceof Time) {
                return new TalendFormatTime(((Time) name)).toString();
            }
            return new TalendFormatDate(((Date) name)).toString();
        }
        return name.toString();
    }

    /**
     * Year/Month/Quarter indicator will need this method format input data
     * 
     * @param name
     * @return
     */
    protected String getFormatName(Object name) {
        return DateFormatUtils.format((Date) name, datePattern);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#toString()
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(this.getName());
        Set<Object> keySet = this.valueToFreq.keySet();
        for (Object object : keySet) {
            buf.append(object);
            buf.append(": ");
            buf.append(this.valueToFreq.get(object));
            buf.append("\n");
        }
        // TODO scorreia could compute average frequency (sum values/# keys)
        return buf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#storeSqlResults(java.lang.Object[])
     * 
     * ADDED scorreia 2008-04-30 storeSqlResults(List<Object[]> objects)
     */
    @SuppressWarnings("fallthrough")
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        // handle case when frequencies are computed on dates.
        int nbColumns = 2;
        if (hasDateGrainParameter()) {
            DateGrain dategrain = getParameters().getDateParameters().getDateAggregationType();
            switch (dategrain) {
            case DAY:
                nbColumns++;
                // no break
            case WEEK:
                nbColumns++;
                // no break
            case MONTH:
                nbColumns++;
                // no break
            case QUARTER:
                nbColumns++;
                // no break
            case YEAR:
                break;
            default:
                break;
            }
        }
        // handle case when no row is returned because there is no value.
        if (objects.isEmpty()) {

            if (log.isInfoEnabled()) {
                log.info("Query for frequency table did not return any result. "
                        + "Check the options of this indicator. Bins must contains some data.");
            }
            this.setValueToFreq(new HashMap<Object, Long>());
            return true;
        } // else we got some values
        if (!checkResults(objects, nbColumns)) {
            return false;
        }
        HashMap<Object, Long> mapVal2Freq = new HashMap<Object, Long>();
        boolean debug = log.isDebugEnabled();
        StringBuffer matrix = debug ? new StringBuffer() : null;
        for (Object[] value2freq : objects) {
            if (value2freq.length != nbColumns) {
                log.error("Problem with result for Frequency indicator");
                return false;
            }
            Object value = getValueFields(value2freq);

            // MOD gdbu 2011-4-14 bug : 18975
            // Long freq = Long.valueOf(String.valueOf(value2freq[nbColumns - 1]));
            Long freq = IndicatorHelper.getLongFromObject(value2freq[nbColumns - 1]);
            // ~18975

            mapVal2Freq.put(value, freq);
            if (debug) {
                matrix.append("\n").append("\"").append(value).append("\"").append(",").append(freq);
            }
        }
        if (debug) {
            log.debug(matrix);
        }

        this.setValueToFreq(mapVal2Freq);
        return true;
    }

    private boolean hasDateGrainParameter() {
        return getParameters() != null && getParameters().getDateParameters() != null
                && getParameters().getDateParameters().getDateAggregationType() != null;
    }

    /**
     * DOC scorreia Comment method "getValueFields".
     * 
     * @param value2freq
     * @return
     */
    protected Object getValueFields(Object[] value2freq) {
        int nbFields = value2freq.length;
        if (nbFields == 2) {
            return value2freq[0];
        }
        // else
        // several columns exists (for date fields)
        // y, q , m, w, d
        StringBuffer buf = new StringBuffer();

        if (nbFields == 3) { // year and quarter
            Object year = value2freq[0];
            Object quarter = value2freq[1];
            if (year == null && quarter == null) {
                return null;
            }
            // else at least one is not null
            buf.append(format4digit(year));
            buf.append(String.valueOf(quarter));
            return buf.toString();
        }

        if (nbFields == 4) { // year, quarter and month
            Object year = value2freq[0];
            Object month = value2freq[2];
            if (year == null && month == null) {
                return null;
            }
            // else at least one is not null
            buf.append(format4digit(year));
            buf.append(format2digit(month));
            return buf.toString();
        }

        if (nbFields == 5) { // year, quarter, month, week
            Object year = value2freq[0];
            Object month = value2freq[2];
            Object week = value2freq[3];
            if (year == null && month == null && week == null) {
                return null;
            }
            // else at least one is not null
            buf.append(format4digit(year));
            buf.append(format2digit(month));
            buf.append(format2digit(week));
            return buf.toString();
        }

        if (nbFields == 6) { // year, quarter, month, week and day
            Object year = value2freq[0];
            Object month = value2freq[2];
            Object day = value2freq[4];
            if (year == null && month == null && day == null) {
                return null;
            }
            // else at least one is not null
            buf.append(format4digit(year));
            buf.append(format2digit(month));
            buf.append(format2digit(day));
            return buf.toString();
        }

        return null;
    }

    /**
     * DOC scorreia Comment method "format2digit".
     * 
     * @param month
     * @return
     */
    private String format2digit(Object month) {
        if (month == null) {
            return "00";
        }
        // else
        String str = String.valueOf(month);
        // MOD msjian for exasol, the str value can be "1.0"
        return F2_DIGIT.format(Double.valueOf(str));
    }

    /**
     * DOC scorreia Comment method "format4digit".
     * 
     * @param year
     * @return
     */
    private String format4digit(Object year) {
        if (year == null) {
            return "0000";
        }
        // else
        String str = String.valueOf(year);
        // MOD msjian for exasol, the str value can be "2008.0"
        return F4_DIGIT.format(Double.valueOf(str));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getMapDB(java.lang.String)
     */
    @Override
    public AbstractDB getMapDB(String dbName) {
        if (isUsedMapDBMode()) {
            // is get computeProcess map
            if (FREQUENCYMAPNAME.equals(dbName)) {
                // current set is valid
                if (valueToFreqForMapDB != null && !((DBMap<Object, Long>) valueToFreqForMapDB).isClosed()) {
                    return (DBMap<Object, Long>) valueToFreqForMapDB;
                } else {
                    // create new DBSet
                    return ((DBMap<Object, Long>) initValueForFrequencyDBMap(FREQUENCYMAPNAME));
                }
            }
        }
        return super.getMapDB(dbName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handleDrillDownData(java.lang.Object, java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleDrillDownData(Object masterObject, List<Object> inputRowList) {
        String dbName = getDBName(masterObject);
        drillDownMap = (DBMap<Object, List<Object>>) getMapDB(dbName);
        super.handleDrillDownData(masterObject, inputRowList);

    }

    /**
     * Replace characters from a string by other characters.
     * 
     * @param s the string to convert
     * @param charsToReplace the list of characters to replace
     * @param replacementChars the replacement characters
     * @return the new string
     * 
     * @generated NOT
     */
    protected static String replaceCharacters(final String s, final String charsToReplace, final String replacementChars) {
        StringBuffer sb = new StringBuffer();
        int c;
        for (int i = 0; i < s.length(); i += Character.charCount(c)) {
            c = s.codePointAt(i);
            int pos = charsToReplace.indexOf(c);
            if (pos > -1) {
                sb.append(replacementChars.charAt(pos));
            } else {
                sb.appendCodePoint(c);
            }
        }
        return sb.toString();
    }

} // FrequencyIndicatorImpl
