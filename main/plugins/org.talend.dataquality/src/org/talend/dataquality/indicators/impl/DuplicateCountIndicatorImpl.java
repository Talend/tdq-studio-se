/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.DBMap;
import org.talend.dataquality.indicators.mapdb.DBSet;
import org.talend.dataquality.indicators.mapdb.StandardDBName;
import org.talend.resource.ResourceManager;
import org.talend.utils.sql.ResultSetUtils;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Duplicate Count Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.dataquality.indicators.impl.DuplicateCountIndicatorImpl#getDuplicateValueCount <em>Duplicate Value Count
 * </em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DuplicateCountIndicatorImpl extends IndicatorImpl implements DuplicateCountIndicator {

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
     * The cached value of the '{@link #getDuplicateValueCount() <em>Duplicate Value Count</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDuplicateValueCount()
     * @generated
     * @ordered
     */
    protected Long duplicateValueCount = DUPLICATE_VALUE_COUNT_EDEFAULT;

    private Set<Object> duplicateObjects = null;

    // store all distinct rows by one key, in its list.
    private Map<Object, List<Object>> distinctMap = null;

    // ~

    /**
     * Create a new DBSet
     * 
     * @return
     */
    private Set<Object> initValueForSet(String dbName) {
        if (isUsedMapDBMode()) {
            return new DBSet<Object>(ResourceManager.getMapDBFilePath(), ResourceManager.getMapDBFileName(this),
                    ResourceManager.getMapDBCatalogName(this, dbName));
        } else {
            return new HashSet<Object>();
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DuplicateCountIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.DUPLICATE_COUNT_INDICATOR;
    }

    /**
     * MOD yyin 20120608 TDQ-3589 use this set for "view values", not all dup rows, but only different values <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Set<Object> getDuplicateValues() {
        return this.duplicateObjects;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case IndicatorsPackage.DUPLICATE_COUNT_INDICATOR__DUPLICATE_VALUE_COUNT:
            return getDuplicateValueCount();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case IndicatorsPackage.DUPLICATE_COUNT_INDICATOR__DUPLICATE_VALUE_COUNT:
            setDuplicateValueCount((Long) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case IndicatorsPackage.DUPLICATE_COUNT_INDICATOR__DUPLICATE_VALUE_COUNT:
            setDuplicateValueCount(DUPLICATE_VALUE_COUNT_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case IndicatorsPackage.DUPLICATE_COUNT_INDICATOR__DUPLICATE_VALUE_COUNT:
            return DUPLICATE_VALUE_COUNT_EDEFAULT == null ? duplicateValueCount != null : !DUPLICATE_VALUE_COUNT_EDEFAULT
                    .equals(duplicateValueCount);
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
        StringBuffer result = new StringBuffer(this.getName());
        result.append("= ");
        result.append(duplicateValueCount);
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Long getDuplicateValueCount() {
        return duplicateValueCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setDuplicateValueCount(Long newDuplicateValueCount) {
        Long oldDuplicateValueCount = duplicateValueCount;
        duplicateValueCount = newDuplicateValueCount;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET,
                    IndicatorsPackage.DUPLICATE_COUNT_INDICATOR__DUPLICATE_VALUE_COUNT, oldDuplicateValueCount,
                    duplicateValueCount));
        }
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
        if (!checkResults(objects, 1)) {
            return false;
        }
        // MOD gdbu 2011-4-14 bug : 18975
        this.setDuplicateValueCount(IndicatorHelper.getLongFromObject(objects.get(0)[0]));
        // ~18975
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
        return this.getDuplicateValueCount();
    }

    @Override
    public boolean finalizeComputation() {
        // Mod yyin 20120608 TDQ-3589
        // at the end: remove the list.size()=1 , only remain the list.size()>1
        Iterator<Object> iterator = duplicateObjects.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();

            if (needStoreDrillDownData()) {
                List<Object> valueArray = distinctMap.get(key);
                handleDrillDownData(key, valueArray);
            } else {
                break;
            }
        }
        this.setDuplicateValueCount(Long.valueOf(duplicateObjects.size()));
        return super.finalizeComputation();
    }

    /**
     * Judge whether we should store current data. It should be mapDB mode and keyIndex is not more than limit
     * 
     * @param keyIndex
     * @return
     */
    private boolean needStoreDrillDownData() {
        return isUsedMapDBMode() && this.checkMustStoreCurrentRow() && this.checkAllowDrillDown();
    }

    @Override
    public boolean reset() {
        if (this.isUsedMapDBMode()) {
            if (needReconnect((AbstractDB<?>) distinctMap)) {
                distinctMap = initValueForDBMap(StandardDBName.computeProcess.name());
            }
            distinctMap.clear();
            if (needReconnect((AbstractDB<?>) duplicateObjects)) {
                duplicateObjects = initValueForSet(StandardDBName.computeProcessSet.name());
            }
            duplicateObjects.clear();
            drillDownValueCount = 0l;
            // java normal mode
        } else {
            distinctMap = new HashMap<>();
            duplicateObjects = new HashSet<>();
            distinctMap.clear();
            duplicateObjects.clear();
        }
        return super.reset();
    }

    /*
     * store the colValue as key, get the whole row from resultset as value. when duplicate, also get the first row from
     * uMap into dMap Added yyin 20120608 TDQ-3589
     * 
     * @see org.talend.dataquality.indicators.DuplicateCountIndicator#handle(java.lang.Object, java.sql.ResultSet, int)
     */
    @Override
    public void handle(Object colValue, ResultSet resultSet, int columnSize) throws SQLException {
        super.handle(colValue);
        if (distinctMap.containsKey(colValue)) {
            if (!duplicateObjects.contains(colValue)) {
                duplicateObjects.add(colValue);
            }
            if (checkMustStoreCurrentRow() || checkMustStoreCurrentRow(drillDownValueCount)) {
                this.mustStoreRow = true;
            }
        } else {
            // first get the whole row from resultset
            List<Object> valueObject = new ArrayList<Object>();

            for (int i = 0; i < columnSize; i++) {
                Object object = ResultSetUtils.getBigObject(resultSet, i + 1);

                // TDQ-9455 msjian: if the value is null, we show it "<null>" in the drill down editor
                valueObject.add(object == null ? PluginConstant.NULL_STRING : object);
            }
            distinctMap.put(colValue, valueObject);
        }

    }

    /*
     * (non-Javadoc) Added yyin 20120608 TDQ-3589
     * 
     * @see org.talend.dataquality.indicators.DuplicateCountIndicator#getDuplicateMap()
     */
    @Override
    public Map<Object, List<Object>> getDuplicateMap() {
        return this.distinctMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.DuplicateCountIndicator#handle(java.lang.Object, java.lang.String[])
     */
    @Override
    public void handle(Object object, String[] rowValues) {
        super.handle(object);
        if (distinctMap.containsKey(object)) {
            if (!duplicateObjects.contains(object)) {
                duplicateObjects.add(object);
            }
            if (checkMustStoreCurrentRow() || checkMustStoreCurrentRow(drillDownValueCount)) {
                this.mustStoreRow = true;
            }
        } else {
            List<Object> list = new ArrayList<Object>();
            list.addAll(Arrays.asList(rowValues));
            distinctMap.put(object, list);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#getMapDB(java.lang.String)
     */
    @Override
    public AbstractDB<Object> getMapDB(String dbName) {
        if (isUsedMapDBMode()) {
            // is get computeProcess map
            if (StandardDBName.computeProcess.name().equals(dbName)) {
                // current set is valid
                if (distinctMap != null && !((DBMap<Object, List<Object>>) distinctMap).isClosed()) {
                    return (DBMap<Object, List<Object>>) distinctMap;
                } else {
                    // create new DBSet
                    return initValueForDBMap(StandardDBName.computeProcess.name());
                }
            } else if (StandardDBName.computeProcessSet.name().equals(dbName)) {
                // current set is valid
                if (duplicateObjects != null && !((DBSet<Object>) duplicateObjects).isClosed()) {
                    return (DBSet<Object>) duplicateObjects;
                } else {
                    // create new DBSet
                    return ((DBSet<Object>) initValueForSet(StandardDBName.computeProcessSet.name()));
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
    @Override
    public void handleDrillDownData(Object masterObject, List<Object> inputRowList) {
        if (checkMustStoreCurrentRow()) {
            super.handleDrillDownData(masterObject, inputRowList);
        }
        // store drill dwon data for view values
        if (this.checkMustStoreCurrentRow(drillDownValueCount)) {
            if (!drillDownValuesSet.contains(masterObject)) {
                drillDownValueCount++;
                drillDownValuesSet.add(masterObject);
            }
        }
    }

} // DuplicateCountIndicatorImpl
