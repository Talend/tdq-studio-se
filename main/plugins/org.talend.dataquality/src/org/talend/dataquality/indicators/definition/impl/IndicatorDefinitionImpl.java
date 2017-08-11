/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.definition.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.indicators.definition.CharactersMapping;
import org.talend.dataquality.indicators.definition.DefinitionPackage;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;
import orgomg.cwm.objectmodel.core.impl.ModelElementImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Indicator Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getCategories <em>Categories</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getAggregatedDefinitions <em>Aggregated
 * Definitions</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getLabel <em>Label</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getSubCategories <em>Sub Categories</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getSqlGenericExpression <em>Sql Generic
 * Expression</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getAggregate1argFunctions <em>
 * Aggregate1arg Functions</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getDate1argFunctions <em>Date1arg
 * Functions</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getCharactersMapping <em>Characters
 * Mapping</em>}</li>
 * <li>{@link org.talend.dataquality.indicators.definition.impl.IndicatorDefinitionImpl#getIndicatorDefinitionParameter <em>
 * Indicator Definition Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndicatorDefinitionImpl extends ModelElementImpl implements IndicatorDefinition {

    /**
     * The cached value of the '{@link #getCategories() <em>Categories</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getCategories()
     * @generated
     * @ordered
     */
    protected EList<IndicatorCategory> categories;

    /**
     * The cached value of the '{@link #getAggregatedDefinitions() <em>Aggregated Definitions</em>}' reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAggregatedDefinitions()
     * @generated
     * @ordered
     */
    protected EList<IndicatorDefinition> aggregatedDefinitions;

    /**
     * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected static final String LABEL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected String label = LABEL_EDEFAULT;

    /**
     * The cached value of the '{@link #getSubCategories() <em>Sub Categories</em>}' reference list.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getSubCategories()
     * @generated
     * @ordered
     */
    protected EList<IndicatorCategory> subCategories;

    /**
     * The cached value of the '{@link #getSqlGenericExpression() <em>Sql Generic Expression</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSqlGenericExpression()
     * @generated
     * @ordered
     */
    protected EList<TdExpression> sqlGenericExpression;

    /**
     * The cached value of the '{@link #getAggregate1argFunctions() <em>Aggregate1arg Functions</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAggregate1argFunctions()
     * @generated
     * @ordered
     */
    protected EList<TdExpression> aggregate1argFunctions;

    /**
     * The cached value of the '{@link #getDate1argFunctions() <em>Date1arg Functions</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDate1argFunctions()
     * @generated
     * @ordered
     */
    protected EList<TdExpression> date1argFunctions;

    /**
     * The cached value of the '{@link #getCharactersMapping() <em>Characters Mapping</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getCharactersMapping()
     * @generated
     * @ordered
     */
    protected EList<CharactersMapping> charactersMapping;

    /**
     * The cached value of the '{@link #getIndicatorDefinitionParameter() <em>Indicator Definition Parameter</em>}' containment
     * reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getIndicatorDefinitionParameter()
     * @generated
     * @ordered
     */
    protected EList<IndicatorDefinitionParameter> indicatorDefinitionParameter;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected IndicatorDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return DefinitionPackage.Literals.INDICATOR_DEFINITION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<IndicatorCategory> getCategories() {
        if (categories == null) {
            categories = new EObjectResolvingEList<IndicatorCategory>(IndicatorCategory.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__CATEGORIES);
        }
        return categories;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<IndicatorDefinition> getAggregatedDefinitions() {
        if (aggregatedDefinitions == null) {
            aggregatedDefinitions = new EObjectResolvingEList<IndicatorDefinition>(IndicatorDefinition.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__AGGREGATED_DEFINITIONS);
        }
        return aggregatedDefinitions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setLabel(String newLabel) {
        String oldLabel = label;
        label = newLabel;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, DefinitionPackage.INDICATOR_DEFINITION__LABEL, oldLabel, label));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<IndicatorCategory> getSubCategories() {
        if (subCategories == null) {
            subCategories = new EObjectResolvingEList<IndicatorCategory>(IndicatorCategory.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__SUB_CATEGORIES);
        }
        return subCategories;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<TdExpression> getSqlGenericExpression() {
        if (sqlGenericExpression == null) {
            sqlGenericExpression = new EObjectContainmentEList<TdExpression>(TdExpression.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION);
        }
        return sqlGenericExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<TdExpression> getAggregate1argFunctions() {
        if (aggregate1argFunctions == null) {
            aggregate1argFunctions = new EObjectContainmentEList<TdExpression>(TdExpression.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS);
        }
        return aggregate1argFunctions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<TdExpression> getDate1argFunctions() {
        if (date1argFunctions == null) {
            date1argFunctions = new EObjectContainmentEList<TdExpression>(TdExpression.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS);
        }
        return date1argFunctions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<CharactersMapping> getCharactersMapping() {
        if (charactersMapping == null) {
            charactersMapping = new EObjectContainmentEList<CharactersMapping>(CharactersMapping.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING);
        }
        return charactersMapping;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<IndicatorDefinitionParameter> getIndicatorDefinitionParameter() {
        if (indicatorDefinitionParameter == null) {
            indicatorDefinitionParameter = new EObjectContainmentEList<IndicatorDefinitionParameter>(
                    IndicatorDefinitionParameter.class, this,
                    DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER);
        }
        return indicatorDefinitionParameter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION:
            return ((InternalEList<?>) getSqlGenericExpression()).basicRemove(otherEnd, msgs);
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS:
            return ((InternalEList<?>) getAggregate1argFunctions()).basicRemove(otherEnd, msgs);
        case DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS:
            return ((InternalEList<?>) getDate1argFunctions()).basicRemove(otherEnd, msgs);
        case DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING:
            return ((InternalEList<?>) getCharactersMapping()).basicRemove(otherEnd, msgs);
        case DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER:
            return ((InternalEList<?>) getIndicatorDefinitionParameter()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case DefinitionPackage.INDICATOR_DEFINITION__CATEGORIES:
            return getCategories();
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATED_DEFINITIONS:
            return getAggregatedDefinitions();
        case DefinitionPackage.INDICATOR_DEFINITION__LABEL:
            return getLabel();
        case DefinitionPackage.INDICATOR_DEFINITION__SUB_CATEGORIES:
            return getSubCategories();
        case DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION:
            return getSqlGenericExpression();
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS:
            return getAggregate1argFunctions();
        case DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS:
            return getDate1argFunctions();
        case DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING:
            return getCharactersMapping();
        case DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER:
            return getIndicatorDefinitionParameter();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case DefinitionPackage.INDICATOR_DEFINITION__CATEGORIES:
            getCategories().clear();
            getCategories().addAll((Collection<? extends IndicatorCategory>) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATED_DEFINITIONS:
            getAggregatedDefinitions().clear();
            getAggregatedDefinitions().addAll((Collection<? extends IndicatorDefinition>) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__LABEL:
            setLabel((String) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__SUB_CATEGORIES:
            getSubCategories().clear();
            getSubCategories().addAll((Collection<? extends IndicatorCategory>) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION:
            getSqlGenericExpression().clear();
            getSqlGenericExpression().addAll((Collection<? extends TdExpression>) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS:
            getAggregate1argFunctions().clear();
            getAggregate1argFunctions().addAll((Collection<? extends TdExpression>) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS:
            getDate1argFunctions().clear();
            getDate1argFunctions().addAll((Collection<? extends TdExpression>) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING:
            getCharactersMapping().clear();
            getCharactersMapping().addAll((Collection<? extends CharactersMapping>) newValue);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER:
            getIndicatorDefinitionParameter().clear();
            getIndicatorDefinitionParameter().addAll((Collection<? extends IndicatorDefinitionParameter>) newValue);
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
        case DefinitionPackage.INDICATOR_DEFINITION__CATEGORIES:
            getCategories().clear();
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATED_DEFINITIONS:
            getAggregatedDefinitions().clear();
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__LABEL:
            setLabel(LABEL_EDEFAULT);
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__SUB_CATEGORIES:
            getSubCategories().clear();
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION:
            getSqlGenericExpression().clear();
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS:
            getAggregate1argFunctions().clear();
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS:
            getDate1argFunctions().clear();
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING:
            getCharactersMapping().clear();
            return;
        case DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER:
            getIndicatorDefinitionParameter().clear();
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
        case DefinitionPackage.INDICATOR_DEFINITION__CATEGORIES:
            return categories != null && !categories.isEmpty();
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATED_DEFINITIONS:
            return aggregatedDefinitions != null && !aggregatedDefinitions.isEmpty();
        case DefinitionPackage.INDICATOR_DEFINITION__LABEL:
            return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
        case DefinitionPackage.INDICATOR_DEFINITION__SUB_CATEGORIES:
            return subCategories != null && !subCategories.isEmpty();
        case DefinitionPackage.INDICATOR_DEFINITION__SQL_GENERIC_EXPRESSION:
            return sqlGenericExpression != null && !sqlGenericExpression.isEmpty();
        case DefinitionPackage.INDICATOR_DEFINITION__AGGREGATE1ARG_FUNCTIONS:
            return aggregate1argFunctions != null && !aggregate1argFunctions.isEmpty();
        case DefinitionPackage.INDICATOR_DEFINITION__DATE1ARG_FUNCTIONS:
            return date1argFunctions != null && !date1argFunctions.isEmpty();
        case DefinitionPackage.INDICATOR_DEFINITION__CHARACTERS_MAPPING:
            return charactersMapping != null && !charactersMapping.isEmpty();
        case DefinitionPackage.INDICATOR_DEFINITION__INDICATOR_DEFINITION_PARAMETER:
            return indicatorDefinitionParameter != null && !indicatorDefinitionParameter.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (label: ");
        result.append(label);
        result.append(')');
        return result.toString();
    }

} // IndicatorDefinitionImpl
