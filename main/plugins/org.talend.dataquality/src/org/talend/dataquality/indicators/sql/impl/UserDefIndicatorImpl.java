/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.sql.impl;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.IndicatorValueType;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.impl.IndicatorImpl;
import org.talend.dataquality.indicators.sql.IndicatorSqlPackage;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.i18n.Messages;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>User Def Indicator</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl#getUserCount <em>User Count</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class UserDefIndicatorImpl extends IndicatorImpl implements UserDefIndicator {

    private static Logger log = Logger.getLogger(UserDefIndicatorImpl.class);

    private static final DecimalFormat F4_DIGIT = new DecimalFormat("0000");

    private static final DecimalFormat F2_DIGIT = new DecimalFormat("00");

    private IndicatorCategory indicatorCategory = null;

    private IndicatorCategory getIndicatorCategory() {
        try {
            EList<IndicatorCategory> categories = this.getIndicatorDefinition().getCategories();
            if (categories != null && categories.size() > 0) {
                indicatorCategory = categories.get(0);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return indicatorCategory;
    }

    /**
     * Field for getting frequency for remaining values when the row count is set.
     */
    public static final String OTHER = "Other";

    /**
     * true is distinct value count is computed
     */
    private boolean distinctComputed = false;

    private Set<Object> distinctValues = null;

    /**
     * ADDED scorreia Method "computeDistinctValues" updates the distinctValues field and set distinctComputed field to
     * true.
     */
    private void computeDistinctValues() {
        // Added yyin TDQ-5890, 20130104, when the user select wrong category for UDI.
        if (valueToFreq == null) {
            log.error(Messages.getString("UserDefIndicator.categoryError", this.name));
            return;
        }// ~
        this.distinctValues = this.valueToFreq.keySet();
        this.setDistinctValueCount(Long.valueOf(distinctValues.size()));
        distinctComputed = true;
    }

    /**
     * The default value of the '{@link #getUserCount() <em>User Count</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUserCount()
     * @generated
     * @ordered
     */
    protected static final Long USER_COUNT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUserCount() <em>User Count</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getUserCount()
     * @generated
     * @ordered
     */
    protected Long userCount = USER_COUNT_EDEFAULT;

    /**
     * The default value of the '{@link #getMatchingValueCount() <em>Matching Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getMatchingValueCount()
     * @generated
     * @ordered
     */
    protected static final Long MATCHING_VALUE_COUNT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMatchingValueCount() <em>Matching Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getMatchingValueCount()
     * @generated
     * @ordered
     */
    protected Long matchingValueCount = MATCHING_VALUE_COUNT_EDEFAULT;

    /**
     * The default value of the '{@link #getNotMatchingValueCount() <em>Not Matching Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getNotMatchingValueCount()
     * @generated
     * @ordered
     */
    protected static final Long NOT_MATCHING_VALUE_COUNT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNotMatchingValueCount() <em>Not Matching Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getNotMatchingValueCount()
     * @generated
     * @ordered
     */
    protected Long notMatchingValueCount = NOT_MATCHING_VALUE_COUNT_EDEFAULT;

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
     * The default value of the '{@link #getValueToFreq() <em>Value To Freq</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueToFreq()
     * @generated
     * @ordered
     */
    protected static final HashMap<Object, Long> VALUE_TO_FREQ_EDEFAULT = null;

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
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getDatatype() <em>Datatype</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDatatype()
     * @generated
     * @ordered
     */
    protected static final int DATATYPE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getDatatype() <em>Datatype</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getDatatype()
     * @generated
     * @ordered
     */
    protected int datatype = DATATYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected UserDefIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorSqlPackage.Literals.USER_DEF_INDICATOR;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getUserCount() {
        return userCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setUserCount(Long newUserCount) {
        Long oldUserCount = userCount;
        userCount = newUserCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__USER_COUNT, oldUserCount, userCount));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getMatchingValueCount() {
        return matchingValueCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMatchingValueCount(Long newMatchingValueCount) {
        Long oldMatchingValueCount = matchingValueCount;
        matchingValueCount = newMatchingValueCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__MATCHING_VALUE_COUNT, oldMatchingValueCount, matchingValueCount));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getNotMatchingValueCount() {
        return notMatchingValueCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setNotMatchingValueCount(Long newNotMatchingValueCount) {
        Long oldNotMatchingValueCount = notMatchingValueCount;
        notMatchingValueCount = newNotMatchingValueCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__NOT_MATCHING_VALUE_COUNT, oldNotMatchingValueCount, notMatchingValueCount));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Object> getUniqueValues() {
        if (uniqueValues == null) {
            uniqueValues = new EDataTypeUniqueEList<Object>(Object.class, this, IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUES);
        }
        return uniqueValues;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getDistinctValueCount() {
        return distinctValueCount;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__DISTINCT_VALUE_COUNT, oldDistinctValueCount, distinctValueCount));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUE_COUNT, oldUniqueValueCount, uniqueValueCount));
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
    @Override
    public HashMap<Object, Long> getValueToFreq() {
        return valueToFreq;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public void setValueToFreq(HashMap<Object, Long> newValueToFreq) {
        HashMap<Object, Long> oldValueToFreq = valueToFreq;
        valueToFreq = newValueToFreq;
        distinctComputed = false;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE_TO_FREQ,
                    oldValueToFreq, valueToFreq));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getDatatype() {
        return datatype;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDatatype(int newDatatype) {
        int oldDatatype = datatype;
        datatype = newDatatype;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorSqlPackage.USER_DEF_INDICATOR__DATATYPE, oldDatatype, datatype));
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Long getCount(Object dataValue) {
        if (dataValue == OTHER) {
            long counted = 0L;
            for (Object val : valueToFreq.keySet()) {
                Long freq = this.valueToFreq.get(val);
                counted = (freq == null) ? counted : counted + freq;
            }
            return (count != null && count > 0) ? count - counted : 0L;
        }
        Long freq = this.valueToFreq.get(dataValue);
        return (freq == null) ? 0L : freq;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorSqlPackage.USER_DEF_INDICATOR__USER_COUNT:
                return getUserCount();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__MATCHING_VALUE_COUNT:
                return getMatchingValueCount();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__NOT_MATCHING_VALUE_COUNT:
                return getNotMatchingValueCount();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUES:
                return getUniqueValues();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DISTINCT_VALUE_COUNT:
                return getDistinctValueCount();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUE_COUNT:
                return getUniqueValueCount();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DUPLICATE_VALUE_COUNT:
                return getDuplicateValueCount();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE_TO_FREQ:
                return getValueToFreq();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE:
                return getValue();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DATATYPE:
                return getDatatype();
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
            case IndicatorSqlPackage.USER_DEF_INDICATOR__USER_COUNT:
                setUserCount((Long)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__MATCHING_VALUE_COUNT:
                setMatchingValueCount((Long)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__NOT_MATCHING_VALUE_COUNT:
                setNotMatchingValueCount((Long)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUES:
                getUniqueValues().clear();
                getUniqueValues().addAll((Collection<? extends Object>)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DISTINCT_VALUE_COUNT:
                setDistinctValueCount((Long)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUE_COUNT:
                setUniqueValueCount((Long)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE_TO_FREQ:
                setValueToFreq((HashMap<Object, Long>)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE:
                setValue((String)newValue);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DATATYPE:
                setDatatype((Integer)newValue);
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
            case IndicatorSqlPackage.USER_DEF_INDICATOR__USER_COUNT:
                setUserCount(USER_COUNT_EDEFAULT);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__MATCHING_VALUE_COUNT:
                setMatchingValueCount(MATCHING_VALUE_COUNT_EDEFAULT);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__NOT_MATCHING_VALUE_COUNT:
                setNotMatchingValueCount(NOT_MATCHING_VALUE_COUNT_EDEFAULT);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUES:
                getUniqueValues().clear();
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DISTINCT_VALUE_COUNT:
                setDistinctValueCount(DISTINCT_VALUE_COUNT_EDEFAULT);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUE_COUNT:
                setUniqueValueCount(UNIQUE_VALUE_COUNT_EDEFAULT);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE_TO_FREQ:
                setValueToFreq(VALUE_TO_FREQ_EDEFAULT);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DATATYPE:
                setDatatype(DATATYPE_EDEFAULT);
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
            case IndicatorSqlPackage.USER_DEF_INDICATOR__USER_COUNT:
                return USER_COUNT_EDEFAULT == null ? userCount != null : !USER_COUNT_EDEFAULT.equals(userCount);
            case IndicatorSqlPackage.USER_DEF_INDICATOR__MATCHING_VALUE_COUNT:
                return MATCHING_VALUE_COUNT_EDEFAULT == null ? matchingValueCount != null : !MATCHING_VALUE_COUNT_EDEFAULT.equals(matchingValueCount);
            case IndicatorSqlPackage.USER_DEF_INDICATOR__NOT_MATCHING_VALUE_COUNT:
                return NOT_MATCHING_VALUE_COUNT_EDEFAULT == null ? notMatchingValueCount != null : !NOT_MATCHING_VALUE_COUNT_EDEFAULT.equals(notMatchingValueCount);
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUES:
                return uniqueValues != null && !uniqueValues.isEmpty();
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DISTINCT_VALUE_COUNT:
                return DISTINCT_VALUE_COUNT_EDEFAULT == null ? distinctValueCount != null : !DISTINCT_VALUE_COUNT_EDEFAULT.equals(distinctValueCount);
            case IndicatorSqlPackage.USER_DEF_INDICATOR__UNIQUE_VALUE_COUNT:
                return UNIQUE_VALUE_COUNT_EDEFAULT == null ? uniqueValueCount != null : !UNIQUE_VALUE_COUNT_EDEFAULT.equals(uniqueValueCount);
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DUPLICATE_VALUE_COUNT:
                return DUPLICATE_VALUE_COUNT_EDEFAULT == null ? getDuplicateValueCount() != null : !DUPLICATE_VALUE_COUNT_EDEFAULT.equals(getDuplicateValueCount());
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE_TO_FREQ:
                return VALUE_TO_FREQ_EDEFAULT == null ? valueToFreq != null : !VALUE_TO_FREQ_EDEFAULT.equals(valueToFreq);
            case IndicatorSqlPackage.USER_DEF_INDICATOR__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case IndicatorSqlPackage.USER_DEF_INDICATOR__DATATYPE:
                return datatype != DATATYPE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (userCount: ");
        result.append(userCount);
        result.append(", matchingValueCount: ");
        result.append(matchingValueCount);
        result.append(", notMatchingValueCount: ");
        result.append(notMatchingValueCount);
        result.append(", uniqueValues: ");
        result.append(uniqueValues);
        result.append(", distinctValueCount: ");
        result.append(distinctValueCount);
        result.append(", uniqueValueCount: ");
        result.append(uniqueValueCount);
        result.append(", valueToFreq: ");
        result.append(valueToFreq);
        result.append(", value: ");
        result.append(value);
        result.append(", datatype: ");
        result.append(datatype);
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getIntegerValue()
     * 
     * @generated NOT getIntegerValue() returns the user count value
     */
    @Override
    public Long getIntegerValue() {
        if (IndicatorCategoryHelper.isUserDefMatching(getIndicatorCategory())) {
            return this.getMatchingValueCount();
        }
        return this.getUserCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#storeSqlResults(java.util.List)
     * 
     * ADDED xqliu 2009-07-28 storeSqlResults(List<Object[]> objects)
     */
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        IndicatorCategory indicatorCategoryTmp = getIndicatorCategory();
        if (IndicatorCategoryHelper.isUserDefCount(indicatorCategoryTmp)) {
            return storeSqlResultsRowCount(objects);
        } else if (IndicatorCategoryHelper.isUserDefFrequency(indicatorCategoryTmp)) {
            return storeSqlResultsFrequency(objects);
        } else if (IndicatorCategoryHelper.isUserDefMatching(indicatorCategoryTmp)) {
            return storeSqlResultsMatching(objects);
        } else if (IndicatorCategoryHelper.isUserDefRealValue(indicatorCategoryTmp)) {
            return storeSqlResultsRealValue(objects);
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "storeSqlResultsRowCount".
     * 
     * @param objects
     * @return
     */
    private boolean storeSqlResultsRowCount(List<Object[]> objects) {
        // store row count in userCount attribute
        if (!checkResults(objects, 1)) {
            return false;
        }
        Long c = Long.valueOf(String.valueOf(objects.get(0)[0]));
        this.setUserCount(c);
        return true;
    }

    /**
     * DOC xqliu Comment method "storeSqlResultsFrequency".
     * 
     * @param objects
     * @return
     */
    @SuppressWarnings("fallthrough")
    private boolean storeSqlResultsFrequency(List<Object[]> objects) {
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
            Long freq = Long.valueOf(String.valueOf(value2freq[nbColumns - 1]));
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

    /**
     * DOC xqliu Comment method "storeSqlResultsMatching".
     * 
     * @param objects
     * @return
     */
    private boolean storeSqlResultsMatching(List<Object[]> objects) {
        if (!checkResults(objects, 2)) {
            return false;
        }
        Long match = Long.valueOf(String.valueOf(objects.get(0)[0]));
        Long total = Long.valueOf(String.valueOf(objects.get(0)[1]));
        if (match != null) {
            this.setMatchingValueCount(match);
        }
        if (total != null) {
            this.setCount(total);
        }
        if (total != null && match != null) {
            this.setNotMatchingValueCount(total - match);
        }
        return true;
    }

    /**
     * DOC xqliu Comment method "storeSqlResultsRealValue".
     * 
     * @param objects
     * @return
     */
    private boolean storeSqlResultsRealValue(List<Object[]> objects) {
        if (!checkResults(objects, 1)) {
            return false;
        }

        if (objects.size() == 1) {
            String med = String.valueOf(objects.get(0)[0]);
            if (med == null) {
                log.error("Value is null of " + this.getName() + " !!");
                return false;
            }
            this.setValue(med);
            this.setDatatype(this.getColumnType());
            return true;
        }
        return false;
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
     * 
     * FIXME avoid copy&paste
     */
    private Object getValueFields(Object[] value2freq) {
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
            buf.append(format4digit(year));
            Object quarter = value2freq[1];
            buf.append(String.valueOf(quarter));
            return buf.toString();
        }

        if (nbFields == 4) { // year, quarter and month
            buf.append(format4digit(value2freq[0]));
            buf.append(format2digit(value2freq[2]));
            return buf.toString();
        }

        if (nbFields == 5) { // year, quarter, month, week
            Object year = value2freq[0];
            buf.append(format4digit(year));
            Object month = String.valueOf(value2freq[2]);
            buf.append(format2digit(month));
            String week = String.valueOf(value2freq[3]);
            buf.append(format2digit(week));
            return buf.toString();
        }

        if (nbFields == 6) { // year, quarter, month, week and day
            Object year = value2freq[0];
            buf.append(format4digit(year));
            Object month = String.valueOf(value2freq[2]);
            buf.append(format2digit(month));
            String day = String.valueOf(value2freq[4]);
            buf.append(format2digit(day));
            return buf.toString();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getRealValue()
     */
    @Override
    public Double getRealValue() {
        if (IndicatorValueType.REAL_VALUE.equals(this.getValueType())) {
            return value == null || "null".equals(value) ? null : Double.valueOf(value); //$NON-NLS-1$
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getValueType()
     */
    @Override
    public IndicatorValueType getValueType() {
        if (IndicatorCategoryHelper.isUserDefRealValue(getIndicatorCategory())) {
            return IndicatorValueType.REAL_VALUE;
        }
        // else if count, frequency table or matching count
        return IndicatorValueType.INTEGER_VALUE;
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
        return F4_DIGIT.format(Integer.valueOf(str));
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
        return F2_DIGIT.format(Integer.valueOf(str));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.Indicator#reset()
     */
    @Override
    public boolean reset() {
        super.reset();
        this.valueToFreq = new HashMap<Object, Long>();
        distinctComputed = false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#isUsedMapDBMode()
     */
    @Override
    public boolean isUsedMapDBMode() {
        return false;
    }
}
