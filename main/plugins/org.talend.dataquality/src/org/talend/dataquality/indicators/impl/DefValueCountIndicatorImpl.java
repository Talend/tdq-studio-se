/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.DefValueCountIndicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.utils.sql.Java2SqlType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Def Value Count Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.dataquality.indicators.impl.DefValueCountIndicatorImpl#getDefaultValCount <em>Default Val Count</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DefValueCountIndicatorImpl extends IndicatorImpl implements DefValueCountIndicator {

    /**
     * The default value of the '{@link #getDefaultValCount() <em>Default Val Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDefaultValCount()
     * @generated
     * @ordered
     */
    protected static final Long DEFAULT_VAL_COUNT_EDEFAULT = new Long(0L);

    /**
     * The cached value of the '{@link #getDefaultValCount() <em>Default Val Count</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getDefaultValCount()
     * @generated
     * @ordered
     */
    protected Long defaultValCount = DEFAULT_VAL_COUNT_EDEFAULT;

    protected Object defValue = null;

    protected boolean isOracle = false;

    private static Logger log = Logger.getLogger(DefValueCountIndicatorImpl.class);

    private String pointZeroStr = ".0"; //$NON-NLS-1$

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected DefValueCountIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.DEF_VALUE_COUNT_INDICATOR;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getDefaultValCount() {
        return defaultValCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDefaultValCount(Long newDefaultValCount) {
        Long oldDefaultValCount = defaultValCount;
        defaultValCount = newDefaultValCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR__DEFAULT_VAL_COUNT, oldDefaultValCount, defaultValCount));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR__DEFAULT_VAL_COUNT:
                return getDefaultValCount();
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
            case IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR__DEFAULT_VAL_COUNT:
                setDefaultValCount((Long)newValue);
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
            case IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR__DEFAULT_VAL_COUNT:
                setDefaultValCount(DEFAULT_VAL_COUNT_EDEFAULT);
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
            case IndicatorsPackage.DEF_VALUE_COUNT_INDICATOR__DEFAULT_VAL_COUNT:
                return DEFAULT_VAL_COUNT_EDEFAULT == null ? defaultValCount != null : !DEFAULT_VAL_COUNT_EDEFAULT.equals(defaultValCount);
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
        result.append(" (defaultValCount: ");
        result.append(defaultValCount);
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#storeSqlResults(java.util.List)
     * 
     * ADDED scorreia 2009-01-28 storeSqlResults(List<Object[]> objects)
     */
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        if (!super.checkResults(objects, 1)) {
            return false;
        }
        // MOD gdbu 2011-4-14 bug : 18975
        this.setDefaultValCount(IndicatorHelper.getLongFromObject(objects.get(0)[0]));
        // ~18975

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getIntegerValue()
     * 
     * ADDED scorreia 2009-04-28 getIntegerValue()
     */
    @Override
    public Long getIntegerValue() {
        return this.getDefaultValCount();
    }

    @Override
    public boolean reset() {
        this.defaultValCount = DEFAULT_VAL_COUNT_EDEFAULT;
        return super.reset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handle(java.lang.Object)
     * 
     * ADD yyi 2009-09-02 prepare()
     */
    @Override
    public boolean handle(Object data) {
        boolean ok = super.handle(data);
        if (data == null || defValue == null) {
            return ok;
        }

        String str = data.toString();
        // MOD qiongli 2011-5-31 bug 21655,handle oracle date type.
        boolean isMatch = false;
        if (data instanceof Date && defValue instanceof Date) {
            if (((Date) data).compareTo((Date) defValue) == 0) {
                isMatch = true;
            }
        } else {
            // considering that Timestamp parse fail then defValue is a String and with equals,if data is end with
            // ".0",we should remove the end string ".0".
            if (str.endsWith(pointZeroStr)) {
                str = StringUtils.removeEnd(str, pointZeroStr);
            }
            if (StringUtils.equals(str, defValue.toString())) {
                isMatch = true;
            }
        }
        if (isMatch) {
            if (this.checkMustStoreCurrentRow()) {
                mustStoreRow = true;
            }
            this.defaultValCount++;
        }

        return ok;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#prepare()
     * 
     * ADD yyi 2009-09-02 prepare()
     */
    @Override
    public boolean prepare() {
        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(this.getAnalyzedElement());
        this.defValue = ColumnHelper.getDefaultValue(tdColumn);
        // MOD qiongli 2011-5-31 bug 21655,if it is oracle db,need to handel the default value.
        Connection connection = ConnectionHelper.getConnection(tdColumn);
        if (SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection) != null) {
            String databaseType = ((DatabaseConnection) connection).getDatabaseType();
            if (databaseType != null && databaseType.contains("Oracle")) { //$NON-NLS-1$
                isOracle = true;
            }
        }

        // MOD qiongli 2011-11-7 TDQ-1554.if tdColumn is Date type,parse the default value to Date type when initialize
        // this indicator.
        if (defValue != null) {
            if (isOracle) {
                getDefValueForOracle(tdColumn);
            } else if (Java2SqlType.isDateInSQL(tdColumn.getSqlDataType().getJavaDataType())) {
                String defTmp = defValue.toString();
                try {
                    if (StringUtils.contains(defTmp, ":")) { //$NON-NLS-1$
                        defValue = DateFormat.getDateTimeInstance().parse(defTmp);
                    } else {
                        defValue = DateFormat.getDateInstance().parse(defTmp);
                    }
                } catch (ParseException exc) {
                    // if parse fail,reset defValue to String
                    defValue = defTmp;
                    log.warn("Parse default value failure!");
                }
            }
        }
        return super.prepare();
    }

    /**
     * 
     * DOC qiongli:handle default value and get the right value.
     * 
     * @param tdColumn
     */
    protected void getDefValueForOracle(TdColumn tdColumn) {
        // MOD qiongli 2011-5-31 bug 21655,remove the redundant "\n" or "\'" and handle the date type for default value
        // on oracle db.
        String defTemp = defValue.toString();
        defTemp = StringUtils.removeEnd(defTemp, PluginConstant.ENTER_STRING);
        try {
            int defTempCount = defTemp.codePointCount(0, defTemp.length());
            if (Java2SqlType.isTextInSQL(tdColumn.getSqlDataType().getJavaDataType()) && defTempCount > 1
                    && (defTemp.startsWith(PluginConstant.SINGLE_QUOTE) && defTemp.endsWith(PluginConstant.SINGLE_QUOTE))) {
                defValue = defTemp.substring(1, defTempCount - 1);
            } else if (Java2SqlType.isDateInSQL(tdColumn.getSqlDataType().getJavaDataType()) && defTemp.startsWith("to_date(")) {//$NON-NLS-1$
                String[] array = StringUtils.split(defTemp, PluginConstant.SINGLE_QUOTE);
                if (array.length > 3) {
                    if (array[3] != null && !PluginConstant.EMPTY_STRING.equals(array[3].trim())) {
                        String pattern = StringUtils.replace(array[3], "mi", "mm"); //$NON-NLS-1$ //$NON-NLS-2$
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        defValue = simpleDateFormat.parse(array[1]);
                    } else {
                        defValue = StringUtils.removeEnd(array[1], PluginConstant.ENTER_STRING);
                    }
                }
            } else {
                defValue = defTemp;
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

} // DefValueCountIndicatorImpl

