/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.io.IOError;
import java.sql.Types;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorValueType;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.DBMap;
import org.talend.dataquality.indicators.mapdb.DBSet;
import org.talend.dataquality.indicators.mapdb.StandardDBName;
import org.talend.dataquality.rules.JoinElement;
import org.talend.resource.ResourceManager;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.TalendTypeConvert;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.impl.ModelElementImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Indicator</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getCount <em>Count</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getNullCount <em>Null Count</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getAnalyzedElement <em>Analyzed Element</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getDataminingType <em>Datamining Type</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getIndicatorDefinition <em>Indicator Definition</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getInstantiatedExpressions <em>Instantiated Expressions</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#isComputed <em>Computed</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getJoinConditions <em>Join Conditions</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getMaxNumberRows <em>Max Number Rows</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#isValidRow <em>Valid Row</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#isInValidRow <em>In Valid Row</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#isStoreData <em>Store Data</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.IndicatorImpl#getBuiltInIndicatorDefinition <em>Built In Indicator Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndicatorImpl extends ModelElementImpl implements Indicator {

    private static Logger log = Logger.getLogger(IndicatorImpl.class);

    /**
     * Decide whether save temp data to file
     */
    protected boolean usedMapDBMode = true;

    /**
     * The limit size of the items which will be store by drillDown
     */
    protected Long drillDownLimitSize = 0l;

    /**
     * The count which how many rows have been store.
     */
    protected Long drillDownRowCount = 0l;

    /**
     * store view values count
     */
    protected Long drillDownValueCount = 0l;

    /**
     * store drill down rows.
     */
    protected DBMap<Object, List<Object>> drillDownMap = null;

    /**
     * store drill down value.
     */
    protected DBSet<Object> drillDownValuesSet = null;

    /**
     * The default value of the '{@link #getCount() <em>Count</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getCount()
     * @generated
     * @ordered
     */
    protected static final Long COUNT_EDEFAULT = new Long(0L);

    /**
     * The cached value of the '{@link #getCount() <em>Count</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getCount()
     * @generated
     * @ordered
     */
    protected Long count = COUNT_EDEFAULT;

    /**
     * The default value of the '{@link #getNullCount() <em>Null Count</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getNullCount()
     * @generated
     * @ordered
     */
    protected static final Long NULL_COUNT_EDEFAULT = new Long(0L);

    /**
     * The cached value of the '{@link #getNullCount() <em>Null Count</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #getNullCount()
     * @generated
     * @ordered
     */
    protected Long nullCount = NULL_COUNT_EDEFAULT;

    // MOD mzhao feature 12919
    /**
     * This field should only be used by java engin. When this field is true mean that currnt data should be store for
     * drill down action {@link #handleDrillDownData(Object, List)} method will do that. Else it should be false. And
     * default it is false. It should be decided in {@link #handle(Object)} method.
     */
    protected boolean mustStoreRow = false;

    /**
     * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getParameters()
     * @generated
     * @ordered
     */
    protected IndicatorParameters parameters;

    /**
     * The cached value of the '{@link #getAnalyzedElement() <em>Analyzed Element</em>}' reference.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getAnalyzedElement()
     * @generated
     * @ordered
     */
    protected ModelElement analyzedElement;

    /**
     * The default value of the '{@link #getDataminingType() <em>Datamining Type</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getDataminingType()
     * @generated
     * @ordered
     */
    protected static final DataminingType DATAMINING_TYPE_EDEFAULT = DataminingType.NOMINAL;

    /**
     * The cached value of the '{@link #getDataminingType() <em>Datamining Type</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getDataminingType()
     * @generated
     * @ordered
     */
    protected DataminingType dataminingType = DATAMINING_TYPE_EDEFAULT;

    /**
     * The cached value of the '{@link #getIndicatorDefinition() <em>Indicator Definition</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getIndicatorDefinition()
     * @generated
     * @ordered
     */
    protected IndicatorDefinition indicatorDefinition;

    /**
     * The cached value of the '{@link #getInstantiatedExpressions() <em>Instantiated Expressions</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getInstantiatedExpressions()
     * @generated
     * @ordered
     */
    protected EList<Expression> instantiatedExpressions;

    /**
     * The default value of the '{@link #isComputed() <em>Computed</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isComputed()
     * @generated
     * @ordered
     */
    protected static final boolean COMPUTED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isComputed() <em>Computed</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isComputed()
     * @generated
     * @ordered
     */
    protected boolean computed = COMPUTED_EDEFAULT;

    /**
     * The cached value of the '{@link #getJoinConditions() <em>Join Conditions</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getJoinConditions()
     * @generated
     * @ordered
     */
    protected EList<JoinElement> joinConditions;

    /**
     * The default value of the '{@link #getMaxNumberRows() <em>Max Number Rows</em>}' attribute.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @see #getMaxNumberRows()
     * @generated
     * @ordered
     */
    protected static final int MAX_NUMBER_ROWS_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getMaxNumberRows() <em>Max Number Rows</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxNumberRows()
     * @generated
     * @ordered
     */
    protected int maxNumberRows = MAX_NUMBER_ROWS_EDEFAULT;

    /**
     * The default value of the '{@link #isValidRow() <em>Valid Row</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isValidRow()
     * @generated
     * @ordered
     */
    protected static final boolean VALID_ROW_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isValidRow() <em>Valid Row</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isValidRow()
     * @generated
     * @ordered
     */
    protected boolean validRow = VALID_ROW_EDEFAULT;

    /**
     * The default value of the '{@link #isInValidRow() <em>In Valid Row</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isInValidRow()
     * @generated
     * @ordered
     */
    protected static final boolean IN_VALID_ROW_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isInValidRow() <em>In Valid Row</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isInValidRow()
     * @generated
     * @ordered
     */
    protected boolean inValidRow = IN_VALID_ROW_EDEFAULT;

    /**
     * The default value of the '{@link #isStoreData() <em>Store Data</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isStoreData()
     * @generated
     * @ordered
     */
    protected static final boolean STORE_DATA_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStoreData() <em>Store Data</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @see #isStoreData()
     * @generated
     * @ordered
     */
    protected boolean storeData = STORE_DATA_EDEFAULT;

    /**
     * The cached value of the '{@link #getBuiltInIndicatorDefinition() <em>Built In Indicator Definition</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #getBuiltInIndicatorDefinition()
     * @generated
     * @ordered
     */
    protected IndicatorDefinition builtInIndicatorDefinition;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected IndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.INDICATOR;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getCount() {
        return count;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setCount(Long newCount) {
        Long oldCount = count;
        count = newCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__COUNT, oldCount, count));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Long getNullCount() {
        return nullCount;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setNullCount(Long newNullCount) {
        Long oldNullCount = nullCount;
        nullCount = newNullCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__NULL_COUNT, oldNullCount, nullCount));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IndicatorParameters getParameters() {
        return parameters;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetParameters(IndicatorParameters newParameters, NotificationChain msgs) {
        IndicatorParameters oldParameters = parameters;
        parameters = newParameters;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__PARAMETERS, oldParameters, newParameters);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setParameters(IndicatorParameters newParameters) {
        if (newParameters != parameters) {
            NotificationChain msgs = null;
            if (parameters != null)
                msgs = ((InternalEObject)parameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.INDICATOR__PARAMETERS, null, msgs);
            if (newParameters != null)
                msgs = ((InternalEObject)newParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.INDICATOR__PARAMETERS, null, msgs);
            msgs = basicSetParameters(newParameters, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__PARAMETERS, newParameters, newParameters));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ModelElement getAnalyzedElement() {
        if (analyzedElement != null && analyzedElement.eIsProxy()) {
            InternalEObject oldAnalyzedElement = (InternalEObject)analyzedElement;
            analyzedElement = (ModelElement)eResolveProxy(oldAnalyzedElement);
            if (analyzedElement != oldAnalyzedElement) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, IndicatorsPackage.INDICATOR__ANALYZED_ELEMENT, oldAnalyzedElement, analyzedElement));
            }
        }
        return analyzedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ModelElement basicGetAnalyzedElement() {
        return analyzedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAnalyzedElement(ModelElement newAnalyzedElement) {
        ModelElement oldAnalyzedElement = analyzedElement;
        analyzedElement = newAnalyzedElement;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__ANALYZED_ELEMENT, oldAnalyzedElement, analyzedElement));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public DataminingType getDataminingType() {
        ModelElement elt = getAnalyzedElement();
        if (elt == null) {
            return getDataminingTypeGen();
        }
        TdColumn col = SwitchHelpers.COLUMN_SWITCH.doSwitch(elt);
        if (col == null) {
            return getDataminingTypeGen();
        }
        DataminingType type = MetadataHelper.getDataminingType(col);
        if (type == null) {
            // try the default code
            return getDataminingTypeGen();
        }
        return type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected DataminingType getDataminingTypeGen() {
        return dataminingType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDataminingType(DataminingType newDataminingType) {
        DataminingType oldDataminingType = dataminingType;
        dataminingType = newDataminingType == null ? DATAMINING_TYPE_EDEFAULT : newDataminingType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__DATAMINING_TYPE, oldDataminingType, dataminingType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public IndicatorDefinition getIndicatorDefinition() {
        if (indicatorDefinition != null && indicatorDefinition.eIsProxy()) {
            InternalEObject oldIndicatorDefinition = (InternalEObject) indicatorDefinition;
            indicatorDefinition = (IndicatorDefinition) eResolveProxy(oldIndicatorDefinition);
            if (indicatorDefinition != oldIndicatorDefinition) {
                if (eNotificationRequired()) {
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, IndicatorsPackage.INDICATOR__INDICATOR_DEFINITION,
                            oldIndicatorDefinition, indicatorDefinition));
                }
            }
        }
        if ((indicatorDefinition == null || indicatorDefinition.eIsProxy()) && builtInIndicatorDefinition != null) {
            return builtInIndicatorDefinition;
        }
        return indicatorDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public IndicatorDefinition basicGetIndicatorDefinition() {
        return indicatorDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIndicatorDefinition(IndicatorDefinition newIndicatorDefinition) {
        IndicatorDefinition oldIndicatorDefinition = indicatorDefinition;
        indicatorDefinition = newIndicatorDefinition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__INDICATOR_DEFINITION, oldIndicatorDefinition, indicatorDefinition));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getInstantiatedExpressions() {
        if (instantiatedExpressions == null) {
            instantiatedExpressions = new EObjectContainmentEList<Expression>(Expression.class, this, IndicatorsPackage.INDICATOR__INSTANTIATED_EXPRESSIONS);
        }
        return instantiatedExpressions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isComputed() {
        return computed;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setComputed(boolean newComputed) {
        boolean oldComputed = computed;
        computed = newComputed;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__COMPUTED, oldComputed, computed));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<JoinElement> getJoinConditions() {
        if (joinConditions == null) {
            joinConditions = new EObjectContainmentEList<JoinElement>(JoinElement.class, this, IndicatorsPackage.INDICATOR__JOIN_CONDITIONS);
        }
        return joinConditions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getMaxNumberRows() {
        return maxNumberRows;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMaxNumberRows(int newMaxNumberRows) {
        int oldMaxNumberRows = maxNumberRows;
        maxNumberRows = newMaxNumberRows;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__MAX_NUMBER_ROWS, oldMaxNumberRows, maxNumberRows));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isValidRow() {
        return validRow;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setValidRow(boolean newValidRow) {
        boolean oldValidRow = validRow;
        validRow = newValidRow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__VALID_ROW, oldValidRow, validRow));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isInValidRow() {
        return inValidRow;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setInValidRow(boolean newInValidRow) {
        boolean oldInValidRow = inValidRow;
        inValidRow = newInValidRow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__IN_VALID_ROW, oldInValidRow, inValidRow));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isStoreData() {
        return storeData;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setStoreData(boolean newStoreData) {
        boolean oldStoreData = storeData;
        storeData = newStoreData;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__STORE_DATA, oldStoreData, storeData));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IndicatorDefinition getBuiltInIndicatorDefinition() {
        return builtInIndicatorDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBuiltInIndicatorDefinition(IndicatorDefinition newBuiltInIndicatorDefinition,
            NotificationChain msgs) {
        IndicatorDefinition oldBuiltInIndicatorDefinition = builtInIndicatorDefinition;
        builtInIndicatorDefinition = newBuiltInIndicatorDefinition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION, oldBuiltInIndicatorDefinition, newBuiltInIndicatorDefinition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setBuiltInIndicatorDefinition(IndicatorDefinition newBuiltInIndicatorDefinition) {
        if (newBuiltInIndicatorDefinition != builtInIndicatorDefinition) {
            NotificationChain msgs = null;
            if (builtInIndicatorDefinition != null)
                msgs = ((InternalEObject)builtInIndicatorDefinition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION, null, msgs);
            if (newBuiltInIndicatorDefinition != null)
                msgs = ((InternalEObject)newBuiltInIndicatorDefinition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION, null, msgs);
            msgs = basicSetBuiltInIndicatorDefinition(newBuiltInIndicatorDefinition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION, newBuiltInIndicatorDefinition, newBuiltInIndicatorDefinition));
    }

    /**
     * <!-- begin-user-doc --> Increments counts for each given data. <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean handle(Object data) {
        mustStoreRow = false;
        if (data == null) {
            nullCount++;
        }
        count++;
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean reset() {
        count = COUNT_EDEFAULT;
        nullCount = NULL_COUNT_EDEFAULT;
        // for MapDB init
        clearDrillDownMap();
        clearDrillDownSet();
        return true;
    }

    /**
     * DOC talend Comment method "clearDrillDownMap".
     */
    protected void clearDrillDownMap() {
        if (this.isUsedMapDBMode() && checkAllowDrillDown()) {
            if (needReconnect(drillDownMap)) {
                drillDownMap = initValueForDBMap(StandardDBName.drillDown.name());
            }
            if (!drillDownMap.isEmpty()) {
                drillDownMap.clear();
            }
            resetDrillDownRowCount();
        }
    }

    /**
     * DOC talend Comment method "clearDrillDownMap".
     */
    protected void clearDrillDownSet() {
        if (this.isUsedMapDBMode() && checkAllowDrillDown()) {
            if (needReconnect(drillDownValuesSet)) {
                drillDownValuesSet = initValueForDBSet(StandardDBName.drillDownValues.name());
            }
            if (!drillDownValuesSet.isEmpty()) {
                drillDownValuesSet.clear();
            }
            drillDownValueCount = 0l;
        }
    }

    /**
     * Whether the map is not created or has been closed
     * 
     * @return true if map should be reconnection else false
     */
    protected boolean needReconnect(AbstractDB<?> map) {
        return map == null || map.isClosed();
    }

    /**
     * Create a new DBMap
     * 
     * @return
     */
    protected DBMap<Object, List<Object>> initValueForDBMap(String dbName) {
        return new DBMap<Object, List<Object>>(ResourceManager.getMapDBFilePath(), ResourceManager.getMapDBFileName(this),
                ResourceManager.getMapDBCatalogName(this, dbName));
    }

    /**
     * Create a new DBSet
     * 
     * @return
     */
    protected DBSet<Object> initValueForDBSet(String dbName) {
        return new DBSet<Object>(ResourceManager.getMapDBFilePath(), ResourceManager.getMapDBFileName(this),
                ResourceManager.getMapDBCatalogName(this, dbName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public String getPurpose() {
        IndicatorDefinition def = this.getIndicatorDefinition();
        return (def != null) ? MetadataHelper.getPurpose(def) : "?? no purpose found for " + this.getName() + " ??";
        // return IndicatorDocumentationHandler.getPurpose(this.eClass().getClassifierID());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public String getLongDescription() {
        IndicatorDefinition def = this.getIndicatorDefinition();
        return (def != null) ? MetadataHelper.getDescription(def) : "?? no description found for " + this.getName() + " ??";
        // return IndicatorDocumentationHandler.getLongDescription(this.eClass().getClassifierID());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean prepare() {
        return this.reset();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean finalizeComputation() {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean storeSqlResults(List<Object[]> objects) {
        // nothing to implement here, a generic indicator does not know how to handle a result.
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT scorreia 2008-04-30
     */
    @Override
    public Expression getInstantiatedExpressions(String language) {
        if (language == null) {
            return null;
        }
        EList<Expression> expressions = this.getInstantiatedExpressions();
        for (Expression expression : expressions) {
            if (expression == null) {
                continue;
            }
            if (language.toUpperCase().compareTo(expression.getLanguage().toUpperCase()) == 0) {
                return expression;
            }
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT scorreia 2008-04-30
     */
    @Override
    public boolean setInstantiatedExpression(Expression expression) {
        if (expression == null) {
            return false;
        }
        String language = expression.getLanguage();
        if (language == null) {
            return false;
        }
        Expression found = getInstantiatedExpressions(language);
        if (found != null) {
            found.setBody(expression.getBody());
        } else {
            getInstantiatedExpressions().add(expression);
        }
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Long getIntegerValue() {
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public Double getRealValue() {
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public IndicatorValueType getValueType() {
        return IndicatorValueType.INTEGER_VALUE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getInstanceValue() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean mustStoreRow() {
        return mustStoreRow;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public void setMustStoreRow(boolean mustStoreRow) {
        this.mustStoreRow = mustStoreRow;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean handle(EList<Object> datas) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see orgomg.cwm.objectmodel.core.impl.ModelElementImpl#getName()
     * 
     * ADDED scorreia 2008-05-02 return a default name if none has been set.
     */
    @Override
    public String getName() {
        String n = super.getName();
        if (n != null) {
            return n;
        }
        // else
        IndicatorDefinition def = getIndicatorDefinition();
        if (def != null) {
            return def.getName();
        }
        // else
        return this.eClass().getName();
    }

    /**
     * Method "checkResults" checks whether the result has the right number of elements (but some elements could be
     * null).
     * 
     * @param objects the results of the query
     * @param expectedSize the expected number of elements in the resulting array "objects"
     * @return true if ok
     */
    protected boolean checkResults(List<Object[]> objects, final int expectedSize) {
        if (objects == null || objects.isEmpty()) {
            log.error("<" + getName() + "> Unexpected result: Result set is null or empty for the query.");
            return false;
        }
        for (Object[] array : objects) {
            if (array == null || expectedSize != array.length) {
                log.error("<" + getName() + "> Unexpected result: " + array + ". Expected " + expectedSize
                        + " columns as a result of the query.");
                return false;
            }
            if (log.isDebugEnabled()) {
                log.debug("<" + getName() + "> Result of query: " + ArrayUtils.toString(array));
            }
            // for (int i = 0; i < array.length; i++) {
            // Object object = array[i];
            // // assume last column is not null (for example in frequency table result)
            // if (object == null && i == array.length - 1) {
            // log.error("Unexpected result: " + object + ". One of the column returned by the query is null!");
            // return false;
            // }
            // }
        }

        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IndicatorsPackage.INDICATOR__PARAMETERS:
                return basicSetParameters(null, msgs);
            case IndicatorsPackage.INDICATOR__INSTANTIATED_EXPRESSIONS:
                return ((InternalEList<?>)getInstantiatedExpressions()).basicRemove(otherEnd, msgs);
            case IndicatorsPackage.INDICATOR__JOIN_CONDITIONS:
                return ((InternalEList<?>)getJoinConditions()).basicRemove(otherEnd, msgs);
            case IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION:
                return basicSetBuiltInIndicatorDefinition(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorsPackage.INDICATOR__COUNT:
                return getCount();
            case IndicatorsPackage.INDICATOR__NULL_COUNT:
                return getNullCount();
            case IndicatorsPackage.INDICATOR__PARAMETERS:
                return getParameters();
            case IndicatorsPackage.INDICATOR__ANALYZED_ELEMENT:
                if (resolve) return getAnalyzedElement();
                return basicGetAnalyzedElement();
            case IndicatorsPackage.INDICATOR__DATAMINING_TYPE:
                return getDataminingType();
            case IndicatorsPackage.INDICATOR__INDICATOR_DEFINITION:
                if (resolve) return getIndicatorDefinition();
                return basicGetIndicatorDefinition();
            case IndicatorsPackage.INDICATOR__INSTANTIATED_EXPRESSIONS:
                return getInstantiatedExpressions();
            case IndicatorsPackage.INDICATOR__COMPUTED:
                return isComputed();
            case IndicatorsPackage.INDICATOR__JOIN_CONDITIONS:
                return getJoinConditions();
            case IndicatorsPackage.INDICATOR__MAX_NUMBER_ROWS:
                return getMaxNumberRows();
            case IndicatorsPackage.INDICATOR__VALID_ROW:
                return isValidRow();
            case IndicatorsPackage.INDICATOR__IN_VALID_ROW:
                return isInValidRow();
            case IndicatorsPackage.INDICATOR__STORE_DATA:
                return isStoreData();
            case IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION:
                return getBuiltInIndicatorDefinition();
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
            case IndicatorsPackage.INDICATOR__COUNT:
                setCount((Long)newValue);
                return;
            case IndicatorsPackage.INDICATOR__NULL_COUNT:
                setNullCount((Long)newValue);
                return;
            case IndicatorsPackage.INDICATOR__PARAMETERS:
                setParameters((IndicatorParameters)newValue);
                return;
            case IndicatorsPackage.INDICATOR__ANALYZED_ELEMENT:
                setAnalyzedElement((ModelElement)newValue);
                return;
            case IndicatorsPackage.INDICATOR__DATAMINING_TYPE:
                setDataminingType((DataminingType)newValue);
                return;
            case IndicatorsPackage.INDICATOR__INDICATOR_DEFINITION:
                setIndicatorDefinition((IndicatorDefinition)newValue);
                return;
            case IndicatorsPackage.INDICATOR__INSTANTIATED_EXPRESSIONS:
                getInstantiatedExpressions().clear();
                getInstantiatedExpressions().addAll((Collection<? extends Expression>)newValue);
                return;
            case IndicatorsPackage.INDICATOR__COMPUTED:
                setComputed((Boolean)newValue);
                return;
            case IndicatorsPackage.INDICATOR__JOIN_CONDITIONS:
                getJoinConditions().clear();
                getJoinConditions().addAll((Collection<? extends JoinElement>)newValue);
                return;
            case IndicatorsPackage.INDICATOR__MAX_NUMBER_ROWS:
                setMaxNumberRows((Integer)newValue);
                return;
            case IndicatorsPackage.INDICATOR__VALID_ROW:
                setValidRow((Boolean)newValue);
                return;
            case IndicatorsPackage.INDICATOR__IN_VALID_ROW:
                setInValidRow((Boolean)newValue);
                return;
            case IndicatorsPackage.INDICATOR__STORE_DATA:
                setStoreData((Boolean)newValue);
                return;
            case IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION:
                setBuiltInIndicatorDefinition((IndicatorDefinition)newValue);
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
            case IndicatorsPackage.INDICATOR__COUNT:
                setCount(COUNT_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__NULL_COUNT:
                setNullCount(NULL_COUNT_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__PARAMETERS:
                setParameters((IndicatorParameters)null);
                return;
            case IndicatorsPackage.INDICATOR__ANALYZED_ELEMENT:
                setAnalyzedElement((ModelElement)null);
                return;
            case IndicatorsPackage.INDICATOR__DATAMINING_TYPE:
                setDataminingType(DATAMINING_TYPE_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__INDICATOR_DEFINITION:
                setIndicatorDefinition((IndicatorDefinition)null);
                return;
            case IndicatorsPackage.INDICATOR__INSTANTIATED_EXPRESSIONS:
                getInstantiatedExpressions().clear();
                return;
            case IndicatorsPackage.INDICATOR__COMPUTED:
                setComputed(COMPUTED_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__JOIN_CONDITIONS:
                getJoinConditions().clear();
                return;
            case IndicatorsPackage.INDICATOR__MAX_NUMBER_ROWS:
                setMaxNumberRows(MAX_NUMBER_ROWS_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__VALID_ROW:
                setValidRow(VALID_ROW_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__IN_VALID_ROW:
                setInValidRow(IN_VALID_ROW_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__STORE_DATA:
                setStoreData(STORE_DATA_EDEFAULT);
                return;
            case IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION:
                setBuiltInIndicatorDefinition((IndicatorDefinition)null);
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
            case IndicatorsPackage.INDICATOR__COUNT:
                return COUNT_EDEFAULT == null ? count != null : !COUNT_EDEFAULT.equals(count);
            case IndicatorsPackage.INDICATOR__NULL_COUNT:
                return NULL_COUNT_EDEFAULT == null ? nullCount != null : !NULL_COUNT_EDEFAULT.equals(nullCount);
            case IndicatorsPackage.INDICATOR__PARAMETERS:
                return parameters != null;
            case IndicatorsPackage.INDICATOR__ANALYZED_ELEMENT:
                return analyzedElement != null;
            case IndicatorsPackage.INDICATOR__DATAMINING_TYPE:
                return dataminingType != DATAMINING_TYPE_EDEFAULT;
            case IndicatorsPackage.INDICATOR__INDICATOR_DEFINITION:
                return indicatorDefinition != null;
            case IndicatorsPackage.INDICATOR__INSTANTIATED_EXPRESSIONS:
                return instantiatedExpressions != null && !instantiatedExpressions.isEmpty();
            case IndicatorsPackage.INDICATOR__COMPUTED:
                return computed != COMPUTED_EDEFAULT;
            case IndicatorsPackage.INDICATOR__JOIN_CONDITIONS:
                return joinConditions != null && !joinConditions.isEmpty();
            case IndicatorsPackage.INDICATOR__MAX_NUMBER_ROWS:
                return maxNumberRows != MAX_NUMBER_ROWS_EDEFAULT;
            case IndicatorsPackage.INDICATOR__VALID_ROW:
                return validRow != VALID_ROW_EDEFAULT;
            case IndicatorsPackage.INDICATOR__IN_VALID_ROW:
                return inValidRow != IN_VALID_ROW_EDEFAULT;
            case IndicatorsPackage.INDICATOR__STORE_DATA:
                return storeData != STORE_DATA_EDEFAULT;
            case IndicatorsPackage.INDICATOR__BUILT_IN_INDICATOR_DEFINITION:
                return builtInIndicatorDefinition != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * Method "getColumnType".
     * 
     * @return the column type of the analyzed object (when the analyzed object is a column). Otherwise, it returns
     * Types.JAVA_OBJECT.
     */
    protected int getColumnType() {
        int javaType = Types.JAVA_OBJECT; // default type
        ModelElement elt = this.getAnalyzedElement();
        if (elt != null) {
            TdColumn col = SwitchHelpers.COLUMN_SWITCH.doSwitch(elt);
            if (col != null) {
                javaType = col.getSqlDataType().getJavaDataType();
                return javaType;
            }
            MetadataColumn mdCol = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(elt);
            if (mdCol != null) {
                javaType = Java2SqlType.getJavaTypeBySqlType(TalendTypeConvert.convertToJavaType(mdCol.getTalendType()));
            }
        }
        return javaType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (count: ");
        result.append(count);
        result.append(", nullCount: ");
        result.append(nullCount);
        result.append(", dataminingType: ");
        result.append(dataminingType);
        result.append(", computed: ");
        result.append(computed);
        result.append(", maxNumberRows: ");
        result.append(maxNumberRows);
        result.append(", validRow: ");
        result.append(validRow);
        result.append(", inValidRow: ");
        result.append(inValidRow);
        result.append(", storeData: ");
        result.append(storeData);
        result.append(')');
        return result.toString();
    }

    /**
     * Getter for saveTempDataToFile.
     * 
     * @return the saveTempDataToFile
     */
    @Override
    public boolean isUsedMapDBMode() {
        return this.usedMapDBMode;
    }

    /**
     * 
     * Get MapDB by dbName
     * 
     * @param dbName The name of DB(note that it is not the name of db file, one db file can contains many dbs and every
     * db have a name)
     * 
     * @return null when MapDB can not be used by current indicator
     * @exception when the DB colsed by abnormal way in last exit, then call this method will throws IOError
     */
    public AbstractDB<Object> getMapDB(String dbName) throws IOError {
        if (isUsedMapDBMode() && checkAllowDrillDown()) {
            if (StandardDBName.drillDown.name().equals(dbName)) {
                if (drillDownMap != null && !drillDownMap.isClosed()) {
                    return drillDownMap;
                }
            } else if (StandardDBName.drillDownValues.name().equals(dbName)) {
                if (drillDownValuesSet != null && !drillDownValuesSet.isClosed()) {
                    return drillDownValuesSet;
                }
                return initValueForDBSet(StandardDBName.drillDownValues.name());
            }
            return initValueForDBMap(dbName);
        }
        return null;
    }

    /**
     * Store drill down data into file
     * 
     * @param masterObject
     * @param inputRowList
     */
    public void handleDrillDownData(Object masterObject, List<Object> inputRowList) {
        drillDownRowCount++;
        drillDownMap.put(drillDownRowCount, inputRowList);
    }

    /**
     * 
     * Set current indicator is belong to MapDB mode
     * 
     * @param usedMapDBMode
     */
    public void setUsedMapDBMode(boolean usedMapDBMode) {
        this.usedMapDBMode = usedMapDBMode;
    }

    /**
     * Getter for drillDownLimitSize.
     * 
     * @return the drillDownLimitSize
     */
    protected Long getDrillDownLimitSize() {
        Analysis analysis = AnalysisHelper.getAnalysis(this);
        if (analysis != null) {
            this.drillDownLimitSize = Long.valueOf(analysis.getParameters().getMaxNumberRows());
        }
        return this.drillDownLimitSize;
    }

    /**
     * 
     * Check whether drill down action is allow
     * 
     * @return true is allowed else false
     */
    protected boolean checkAllowDrillDown() {
        Analysis analysis = AnalysisHelper.getAnalysis(this);
        boolean isStoreData = false;
        boolean isJavaEngine = false;
        if (analysis != null) {
            isStoreData = AnalysisHelper.isStoreData(analysis);
            isJavaEngine = AnalysisHelper.isJavaExecutionEngine(analysis);
        }
        return isStoreData && isJavaEngine;
    }

    /**
     * Sets the drillDownLimitSize.
     * 
     * @param drillDownLimitSize the drillDownLimitSize to set
     */
    protected void setDrillDownLimitSize(Long drillDownLimitSize) {
        this.drillDownLimitSize = drillDownLimitSize;
    }

    /**
     * Getter for dirllDownRowCount.
     * 
     * @return the dirllDownRowCount
     */
    protected Long getDrillDownRowCount() {
        return this.drillDownRowCount;
    }

    /**
     * Sets the dirllDownRowCount.
     * 
     * @param dirllDownRowCount the dirllDownRowCount to set
     */
    protected void setDrillDownRowCount(Long drillDownRowCount) {
        this.drillDownRowCount = drillDownRowCount;
    }

    /**
     * 
     * Reset drillDownRowCount
     */
    protected void resetDrillDownRowCount() {
        this.drillDownRowCount = 0l;
    }

    /**
     * check the DrillDown if From the current DrillDownRowCount.
     */
    protected boolean checkMustStoreCurrentRow() {
        return checkMustStoreCurrentRow(getDrillDownRowCount());

    }

    /**
     * check the DrillDown if From the beginning of 0.
     */
    protected boolean checkMustStoreCurrentRow(Long rowCount) {
        Long currentDrillDownLimit = getDrillDownLimitSize();
        if (currentDrillDownLimit == null || currentDrillDownLimit == 0l) {
            return true;
        }
        if (rowCount < currentDrillDownLimit) {
            return true;
        }
        return false;
    }

    /**
     * Get drill down key word. normally it is the name of indicatorDefinition.
     * But for frequency indicator it is the value of column. So that override in the frequency indicator
     */
    public String getDrillDownKeyWord(String displayValue) {
        return this.getName();
    }

} // IndicatorImpl
