/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.rules.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.RulesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Algorithm Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.talend.dataquality.rules.impl.AlgorithmDefinitionImpl#getAlgorithmType <em>Algorithm Type</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.AlgorithmDefinitionImpl#getAlgorithmParameters <em>Algorithm Parameters</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.impl.AlgorithmDefinitionImpl#getReferenceColumn <em>Reference Column</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AlgorithmDefinitionImpl extends EObjectImpl implements AlgorithmDefinition {
    /**
     * The default value of the '{@link #getAlgorithmType() <em>Algorithm Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlgorithmType()
     * @generated
     * @ordered
     */
    protected static final String ALGORITHM_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAlgorithmType() <em>Algorithm Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlgorithmType()
     * @generated
     * @ordered
     */
    protected String algorithmType = ALGORITHM_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getAlgorithmParameters() <em>Algorithm Parameters</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlgorithmParameters()
     * @generated
     * @ordered
     */
    protected static final String ALGORITHM_PARAMETERS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAlgorithmParameters() <em>Algorithm Parameters</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAlgorithmParameters()
     * @generated
     * @ordered
     */
    protected String algorithmParameters = ALGORITHM_PARAMETERS_EDEFAULT;

    /**
     * The default value of the '{@link #getReferenceColumn() <em>Reference Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceColumn()
     * @generated
     * @ordered
     */
    protected static final String REFERENCE_COLUMN_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReferenceColumn() <em>Reference Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceColumn()
     * @generated
     * @ordered
     */
    protected String referenceColumn = REFERENCE_COLUMN_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AlgorithmDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RulesPackage.Literals.ALGORITHM_DEFINITION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAlgorithmType() {
        return algorithmType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAlgorithmType(String newAlgorithmType) {
        String oldAlgorithmType = algorithmType;
        algorithmType = newAlgorithmType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_TYPE, oldAlgorithmType, algorithmType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAlgorithmParameters() {
        return algorithmParameters;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAlgorithmParameters(String newAlgorithmParameters) {
        String oldAlgorithmParameters = algorithmParameters;
        algorithmParameters = newAlgorithmParameters;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS, oldAlgorithmParameters, algorithmParameters));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getReferenceColumn() {
        return referenceColumn;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceColumn(String newReferenceColumn) {
        String oldReferenceColumn = referenceColumn;
        referenceColumn = newReferenceColumn;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RulesPackage.ALGORITHM_DEFINITION__REFERENCE_COLUMN, oldReferenceColumn, referenceColumn));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_TYPE:
                return getAlgorithmType();
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS:
                return getAlgorithmParameters();
            case RulesPackage.ALGORITHM_DEFINITION__REFERENCE_COLUMN:
                return getReferenceColumn();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_TYPE:
                setAlgorithmType((String)newValue);
                return;
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS:
                setAlgorithmParameters((String)newValue);
                return;
            case RulesPackage.ALGORITHM_DEFINITION__REFERENCE_COLUMN:
                setReferenceColumn((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_TYPE:
                setAlgorithmType(ALGORITHM_TYPE_EDEFAULT);
                return;
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS:
                setAlgorithmParameters(ALGORITHM_PARAMETERS_EDEFAULT);
                return;
            case RulesPackage.ALGORITHM_DEFINITION__REFERENCE_COLUMN:
                setReferenceColumn(REFERENCE_COLUMN_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_TYPE:
                return ALGORITHM_TYPE_EDEFAULT == null ? algorithmType != null : !ALGORITHM_TYPE_EDEFAULT.equals(algorithmType);
            case RulesPackage.ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS:
                return ALGORITHM_PARAMETERS_EDEFAULT == null ? algorithmParameters != null : !ALGORITHM_PARAMETERS_EDEFAULT.equals(algorithmParameters);
            case RulesPackage.ALGORITHM_DEFINITION__REFERENCE_COLUMN:
                return REFERENCE_COLUMN_EDEFAULT == null ? referenceColumn != null : !REFERENCE_COLUMN_EDEFAULT.equals(referenceColumn);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (algorithmType: ");
        result.append(algorithmType);
        result.append(", algorithmParameters: ");
        result.append(algorithmParameters);
        result.append(", referenceColumn: ");
        result.append(referenceColumn);
        result.append(')');
        return result.toString();
    }

} //AlgorithmDefinitionImpl
