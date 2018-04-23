/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.LengthIndicator;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.DBMap;
import org.talend.dataquality.indicators.mapdb.StandardDBName;
import org.talend.resource.ResourceManager;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Length Indicator</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.dataquality.indicators.impl.LengthIndicatorImpl#getLength <em>Length</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class LengthIndicatorImpl extends IndicatorImpl implements LengthIndicator {

    /**
     * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected static final Long LENGTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLength() <em>Length</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getLength()
     * @generated
     * @ordered
     */
    protected Long length = LENGTH_EDEFAULT;

    protected Set<String> dbNameSet = new HashSet<String>();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected LengthIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.LENGTH_INDICATOR;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getLength() {
        return length;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLength(Long newLength) {
        Long oldLength = length;
        length = newLength;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.LENGTH_INDICATOR__LENGTH, oldLength, length));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorsPackage.LENGTH_INDICATOR__LENGTH:
                return getLength();
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
            case IndicatorsPackage.LENGTH_INDICATOR__LENGTH:
                setLength((Long)newValue);
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
            case IndicatorsPackage.LENGTH_INDICATOR__LENGTH:
                setLength(LENGTH_EDEFAULT);
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
            case IndicatorsPackage.LENGTH_INDICATOR__LENGTH:
                return LENGTH_EDEFAULT == null ? length != null : !LENGTH_EDEFAULT.equals(length);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(this.getName());
        buf.append("= ");
        buf.append(this.getLength());
        return buf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#storeSqlResults(java.lang.Object[])
     * 
     * ADDED scorreia 2008-04-30 storeSqlResults(List<Object[]> objects)
     */
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        final int expectedSize = 1;
        if (!checkResults(objects, expectedSize)) {
            return false;
        }
        Object lLength = objects.get(0)[0];
        if (lLength == null) {
            // http://www.talendforge.org/bugs/view.php?id=4783
            // Oracle treats empty strings as null values
            this.setLength(0L);
        } else {
            // MOD gdbu 2011-4-14 bug : 18975
            this.setLength(IndicatorHelper.getLongFromObject(lLength));
            // ~18975
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getIntegerValue()
     * 
     * ADDED scorreia 2008-05-12 getIntegerValue()
     */
    @Override
    public Long getIntegerValue() {
        return this.getLength();
    }

    @Override
    public boolean reset() {
        this.length = LENGTH_EDEFAULT;
        return super.reset();
    }

    /**
     * Change length attribute
     * 
     * @param strLength
     */
    protected void changeLength(final int strLength) {
        this.resetDrillDownRowCount();
        length = Long.valueOf(strLength);
    }

    /**
     * DOC talend Comment method "clearDrillDownMaps".
     */
    @Override
    protected void clearDrillDownMap() {
        if (isUsedMapDBMode() && checkAllowDrillDown()) {
            AbstractDB<?> mapDB = getMapDB(StandardDBName.drillDown.name());
            if (mapDB != null) {
                mapDB.clearDB(ResourceManager.getMapDBCatalogName(this));
            }
            resetDrillDownRowCount();
        }
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
     * DOC talend Comment method "getDBName".
     * 
     * @param masterObject
     * @return
     */
    private String getDBName(Object inputData) {
        int inputDataLength = 0;
        if (null == inputData) {
            inputDataLength = 0;
        } else if (StringUtils.EMPTY.equals(inputData)) {
            inputDataLength = 0;
        } else {
            String inputStr = inputData.toString();
            inputDataLength = inputStr.codePointCount(0, inputStr.length());
        }
        return this.getName() + inputDataLength;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getParameters()
     */
    @Override
    public IndicatorParameters getParameters() {
        if (null == super.getParameters()) {
            setParameters(IndicatorsFactory.eINSTANCE.createIndicatorParameters());
        }
        if (null == parameters.getTextParameter()) {
            parameters.setTextParameter(IndicatorsFactory.eINSTANCE.createTextParameters());
        }
        return parameters;
    }

} // LengthIndicatorImpl
