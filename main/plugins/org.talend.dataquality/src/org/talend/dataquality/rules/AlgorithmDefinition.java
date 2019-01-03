/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.rules;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Algorithm Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmType <em>Algorithm Type</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmParameters <em>Algorithm Parameters</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.AlgorithmDefinition#getReferenceColumn <em>Reference Column</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.dataquality.rules.RulesPackage#getAlgorithmDefinition()
 * @model
 * @generated
 */
public interface AlgorithmDefinition extends EObject {
    /**
     * Returns the value of the '<em><b>Algorithm Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Algorithm Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Algorithm Type</em>' attribute.
     * @see #setAlgorithmType(String)
     * @see org.talend.dataquality.rules.RulesPackage#getAlgorithmDefinition_AlgorithmType()
     * @model
     * @generated
     */
    String getAlgorithmType();

    /**
     * Sets the value of the '{@link org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmType <em>Algorithm Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Algorithm Type</em>' attribute.
     * @see #getAlgorithmType()
     * @generated
     */
    void setAlgorithmType(String value);

    /**
     * Returns the value of the '<em><b>Algorithm Parameters</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Algorithm Parameters</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Algorithm Parameters</em>' attribute.
     * @see #setAlgorithmParameters(String)
     * @see org.talend.dataquality.rules.RulesPackage#getAlgorithmDefinition_AlgorithmParameters()
     * @model
     * @generated
     */
    String getAlgorithmParameters();

    /**
     * Sets the value of the '{@link org.talend.dataquality.rules.AlgorithmDefinition#getAlgorithmParameters <em>Algorithm Parameters</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Algorithm Parameters</em>' attribute.
     * @see #getAlgorithmParameters()
     * @generated
     */
    void setAlgorithmParameters(String value);

    /**
     * Returns the value of the '<em><b>Reference Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference Column</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference Column</em>' attribute.
     * @see #setReferenceColumn(String)
     * @see org.talend.dataquality.rules.RulesPackage#getAlgorithmDefinition_ReferenceColumn()
     * @model
     * @generated
     */
    String getReferenceColumn();

    /**
     * Sets the value of the '{@link org.talend.dataquality.rules.AlgorithmDefinition#getReferenceColumn <em>Reference Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference Column</em>' attribute.
     * @see #getReferenceColumn()
     * @generated
     */
    void setReferenceColumn(String value);

} // AlgorithmDefinition
