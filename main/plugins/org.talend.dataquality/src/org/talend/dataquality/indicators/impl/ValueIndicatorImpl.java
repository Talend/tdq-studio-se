/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.indicators.IndicatorValueType;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.ValueIndicator;
import org.talend.utils.sql.Java2SqlType;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Value Indicator</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.dataquality.indicators.impl.ValueIndicatorImpl#getValue <em>Value</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.impl.ValueIndicatorImpl#getDatatype <em>Datatype</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ValueIndicatorImpl extends IndicatorImpl implements ValueIndicator {

    private static Logger log = Logger.getLogger(ValueIndicatorImpl.class);

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
     * ADD by qiongli 2011-11-21 TDQ-4033. Get the original Object,not toString,.eg. Date type will get the Date Object.
     * then, it is better to direct compare for date type,don't need to parse from String.
     */
    protected Object objValue = null;

    /**
     * it is only for a Date type in DelimetedFile connection
     */
    protected String datePattern = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected ValueIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.VALUE_INDICATOR;
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
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.VALUE_INDICATOR__VALUE, oldValue, value));
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
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.VALUE_INDICATOR__DATATYPE, oldDatatype, datatype));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorsPackage.VALUE_INDICATOR__VALUE:
                return getValue();
            case IndicatorsPackage.VALUE_INDICATOR__DATATYPE:
                return getDatatype();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case IndicatorsPackage.VALUE_INDICATOR__VALUE:
                setValue((String)newValue);
                return;
            case IndicatorsPackage.VALUE_INDICATOR__DATATYPE:
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
            case IndicatorsPackage.VALUE_INDICATOR__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case IndicatorsPackage.VALUE_INDICATOR__DATATYPE:
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
            case IndicatorsPackage.VALUE_INDICATOR__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case IndicatorsPackage.VALUE_INDICATOR__DATATYPE:
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
        result.append(" (value: ");
        result.append(value);
        result.append(", datatype: ");
        result.append(datatype);
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#storeSqlResults(java.util.List)
     * 
     * ADDED scorreia 2008-05-06 storeSqlResults(List<Object[]> objects)
     */
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        if (!checkResults(objects, 1)) {
            return false;
        }

        if (objects.size() == 1) {
            String str = String.valueOf(objects.get(0)[0]);
            if (str == null || StringUtils.equalsIgnoreCase("null", str)) {
                log.warn("Value is null of " + this.getName() + " !!");
            }
            str = getCorrectStringValue(objects.get(0)[0], str);
            this.setValue(str);
            // set datatype here
            this.setDatatype(this.getColumnType());
            return true;
        }
        return false;
    }

    /**
     * format the object data to correct string value.
     * 
     * @param obj: the object data
     * @param str: the object string value
     * @return the object correct string value
     */
    private String getCorrectStringValue(Object obj, String str) {
        String result = str;
        // ADD msjian TDQ-5673 2013-12-27: when the data type oracle.sql.TIMESTAMP, format it to display
        if (obj != null && "oracle.sql.TIMESTAMP".equals(obj.getClass().getName())) { //$NON-NLS-1$
            try {
                Class<? extends Object> clz = obj.getClass();
                Method method = clz.getMethod("timestampValue"); //$NON-NLS-1$
                Timestamp objTimestamp = (Timestamp) method.invoke(obj);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); //$NON-NLS-1$
                result = df.format(objTimestamp);
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getRealValue()
     */
    @Override
    public Double getRealValue() {
        if (IndicatorValueType.REAL_VALUE.equals(this.getValueType())) {
            // MOD xqliu 2009-06-29 bug 7068
            try {
                return value == null || "null".equalsIgnoreCase(value) ? null : Double.valueOf(value);
            } catch (NumberFormatException e) {
                log.error(e, e);
            }
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
        return IndicatorValueType.INSTANCE_VALUE;
    }

    /**
     * Method "isDateValue".
     * 
     * @return true if the analyzed column is of date type.
     */
    protected boolean isDateValue() {
        // MOD scorreia handle date: bug 5938
        final ModelElement elt = getAnalyzedElement();
        if (elt != null) {
            final TdColumn col = SwitchHelpers.COLUMN_SWITCH.doSwitch(elt);
            if (col != null
                    && (Java2SqlType.isDateInSQL(col.getSqlDataType().getJavaDataType()) || Java2SqlType.isDateTimeSQL(col
                            .getSqlDataType().getJavaDataType()))) {
                return true;
            }
            MetadataColumn metadataColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(elt);
            if (metadataColumn != null && metadataColumn.getTalendType().equals("id_Date")) {
                // get date pattern from the column
                String pattern = metadataColumn.getPattern();
                if (StringUtils.isEmpty(pattern)) {
                    pattern = "yyyy-MM-dd";
                } else {
                    pattern = StringUtils.replace(pattern, "\"", StringUtils.EMPTY);
                }
                // the datePattern only for DelimitedFile connection in current or sub-class indicators.
                this.datePattern = pattern;
                return true;

            }
        }
        return false;
    }

    /**
     * 
     * Format the Date type 'ObjValue' based on a date pattern in DelimitedFile. format value like as 2017-10-20.
     * 
     * @return
     */
    protected String formatObjValue() {
        if (!StringUtils.isEmpty(datePattern) && this.objValue != null) {
            SimpleDateFormat df = new SimpleDateFormat(datePattern);
            return df.format(objValue);
        }
        return objValue == null ? "" : objValue.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#reset()
     */
    @Override
    public boolean reset() {
        this.computed = COMPUTED_EDEFAULT; // tells that quartile should be recomputed.
        this.setValue(VALUE_EDEFAULT);
        this.setDatatype(DATATYPE_EDEFAULT);
        this.objValue = null;
        return super.reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#finalizeComputation()
     */
    @Override
    public boolean finalizeComputation() {
        this.setDatatype(this.getColumnType());
        return super.finalizeComputation();
    }

    public Object getObjValue() {
        return this.objValue;
    }

	public String getDatePattern() {
		return datePattern;
	}

} // ValueIndicatorImpl
