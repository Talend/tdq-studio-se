/**
 */
package org.talend.dataquality.indicators;

import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Valid Phone For Region Count Indicator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.dataquality.indicators.ValidPhoneForRegionCountIndicator#getValidPhoneNumCount <em>Valid Phone Num Count</em>}</li>
 * </ul>
 *
 * @see org.talend.dataquality.indicators.IndicatorsPackage#getValidPhoneForRegionCountIndicator()
 * @model
 * @generated
 */
public interface ValidPhoneForRegionCountIndicator extends Indicator {
    /**
     * Returns the value of the '<em><b>Valid Phone Num Count</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Phone Num Count</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Phone Num Count</em>' attribute.
     * @see #setValidPhoneNumCount(Long)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getValidPhoneForRegionCountIndicator_ValidPhoneNumCount()
     * @model default="0"
     * @generated
     */
    Long getValidPhoneNumCount();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.ValidPhoneForRegionCountIndicator#getValidPhoneNumCount <em>Valid Phone Num Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Phone Num Count</em>' attribute.
     * @see #getValidPhoneNumCount()
     * @generated
     */
    void setValidPhoneNumCount(Long value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model kind="operation" dataType="org.talend.dataquality.indicators.JavaSet"
     * @generated
     */
    Set<Object> getValidPhoneValues();

} // ValidPhoneForRegionCountIndicator
