/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.indicators;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Phone Numb Statistics Indicator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormNationalPhoneCountIndicator <em>Well Form National Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormIntePhoneCountIndicator <em>Well Form Inte Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormE164PhoneCountIndicator <em>Well Form E164 Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getInvalidRegCodeCountIndicator <em>Invalid Reg Code Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getPossiblePhoneCountIndicator <em>Possible Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidRegCodeCountIndicator <em>Valid Reg Code Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidPhoneCountIndicator <em>Valid Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidPhoneForRegionCountIndicator <em>Valid Phone For Region Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getFormatFreqPieIndicator <em>Format Freq Pie Indicator</em>}</li>
 * </ul>
 *
 * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator()
 * @model
 * @generated
 */
public interface PhoneNumbStatisticsIndicator extends CompositeIndicator {

    /**
     * Returns the value of the '<em><b>Well Form National Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Well Form National Phone Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Well Form National Phone Count Indicator</em>' containment reference.
     * @see #setWellFormNationalPhoneCountIndicator(WellFormNationalPhoneCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_WellFormNationalPhoneCountIndicator()
     * @model containment="true"
     * @generated
     */
    WellFormNationalPhoneCountIndicator getWellFormNationalPhoneCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormNationalPhoneCountIndicator <em>Well Form National Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Well Form National Phone Count Indicator</em>' containment reference.
     * @see #getWellFormNationalPhoneCountIndicator()
     * @generated
     */
    void setWellFormNationalPhoneCountIndicator(WellFormNationalPhoneCountIndicator value);

    /**
     * Returns the value of the '<em><b>Well Form Inte Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Well Form Inte Phone Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Well Form Inte Phone Count Indicator</em>' containment reference.
     * @see #setWellFormIntePhoneCountIndicator(WellFormIntePhoneCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_WellFormIntePhoneCountIndicator()
     * @model containment="true"
     * @generated
     */
    WellFormIntePhoneCountIndicator getWellFormIntePhoneCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormIntePhoneCountIndicator <em>Well Form Inte Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Well Form Inte Phone Count Indicator</em>' containment reference.
     * @see #getWellFormIntePhoneCountIndicator()
     * @generated
     */
    void setWellFormIntePhoneCountIndicator(WellFormIntePhoneCountIndicator value);

    /**
     * Returns the value of the '<em><b>Well Form E164 Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Well Form E164 Phone Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Well Form E164 Phone Count Indicator</em>' containment reference.
     * @see #setWellFormE164PhoneCountIndicator(WellFormE164PhoneCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_WellFormE164PhoneCountIndicator()
     * @model containment="true"
     * @generated
     */
    WellFormE164PhoneCountIndicator getWellFormE164PhoneCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getWellFormE164PhoneCountIndicator <em>Well Form E164 Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Well Form E164 Phone Count Indicator</em>' containment reference.
     * @see #getWellFormE164PhoneCountIndicator()
     * @generated
     */
    void setWellFormE164PhoneCountIndicator(WellFormE164PhoneCountIndicator value);

    /**
     * Returns the value of the '<em><b>Invalid Reg Code Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Invalid Reg Code Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Invalid Reg Code Count Indicator</em>' containment reference.
     * @see #setInvalidRegCodeCountIndicator(InvalidRegCodeCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_InvalidRegCodeCountIndicator()
     * @model containment="true"
     * @generated
     */
    InvalidRegCodeCountIndicator getInvalidRegCodeCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getInvalidRegCodeCountIndicator <em>Invalid Reg Code Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Invalid Reg Code Count Indicator</em>' containment reference.
     * @see #getInvalidRegCodeCountIndicator()
     * @generated
     */
    void setInvalidRegCodeCountIndicator(InvalidRegCodeCountIndicator value);

    /**
     * Returns the value of the '<em><b>Possible Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Possible Phone Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Possible Phone Count Indicator</em>' containment reference.
     * @see #setPossiblePhoneCountIndicator(PossiblePhoneCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_PossiblePhoneCountIndicator()
     * @model containment="true"
     * @generated
     */
    PossiblePhoneCountIndicator getPossiblePhoneCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getPossiblePhoneCountIndicator <em>Possible Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Possible Phone Count Indicator</em>' containment reference.
     * @see #getPossiblePhoneCountIndicator()
     * @generated
     */
    void setPossiblePhoneCountIndicator(PossiblePhoneCountIndicator value);

    /**
     * Returns the value of the '<em><b>Valid Reg Code Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Reg Code Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Reg Code Count Indicator</em>' containment reference.
     * @see #setValidRegCodeCountIndicator(ValidRegCodeCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_ValidRegCodeCountIndicator()
     * @model containment="true"
     * @generated
     */
    ValidRegCodeCountIndicator getValidRegCodeCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidRegCodeCountIndicator <em>Valid Reg Code Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Reg Code Count Indicator</em>' containment reference.
     * @see #getValidRegCodeCountIndicator()
     * @generated
     */
    void setValidRegCodeCountIndicator(ValidRegCodeCountIndicator value);

    /**
     * Returns the value of the '<em><b>Valid Phone Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Phone Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Phone Count Indicator</em>' containment reference.
     * @see #setValidPhoneCountIndicator(ValidPhoneCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_ValidPhoneCountIndicator()
     * @model containment="true"
     * @generated
     */
    ValidPhoneCountIndicator getValidPhoneCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidPhoneCountIndicator <em>Valid Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Phone Count Indicator</em>' containment reference.
     * @see #getValidPhoneCountIndicator()
     * @generated
     */
    void setValidPhoneCountIndicator(ValidPhoneCountIndicator value);

    /**
     * Returns the value of the '<em><b>Valid Phone For Region Count Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Phone For Region Count Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Phone For Region Count Indicator</em>' containment reference.
     * @see #setValidPhoneForRegionCountIndicator(ValidPhoneForRegionCountIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_ValidPhoneForRegionCountIndicator()
     * @model containment="true"
     * @generated
     */
    ValidPhoneForRegionCountIndicator getValidPhoneForRegionCountIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getValidPhoneForRegionCountIndicator <em>Valid Phone For Region Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Phone For Region Count Indicator</em>' containment reference.
     * @see #getValidPhoneForRegionCountIndicator()
     * @generated
     */
    void setValidPhoneForRegionCountIndicator(ValidPhoneForRegionCountIndicator value);

    /**
     * Returns the value of the '<em><b>Format Freq Pie Indicator</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Format Freq Pie Indicator</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Format Freq Pie Indicator</em>' containment reference.
     * @see #setFormatFreqPieIndicator(FormatFreqPieIndicator)
     * @see org.talend.dataquality.indicators.IndicatorsPackage#getPhoneNumbStatisticsIndicator_FormatFreqPieIndicator()
     * @model containment="true"
     * @generated
     */
    FormatFreqPieIndicator getFormatFreqPieIndicator();

    /**
     * Sets the value of the '{@link org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator#getFormatFreqPieIndicator <em>Format Freq Pie Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format Freq Pie Indicator</em>' containment reference.
     * @see #getFormatFreqPieIndicator()
     * @generated
     */
    void setFormatFreqPieIndicator(FormatFreqPieIndicator value);
} // PhoneNumbStatisticsIndicator
