/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.dataquality.indicators.FormatFreqPieIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.InvalidRegCodeCountIndicator;
import org.talend.dataquality.indicators.PhoneNumbStatisticsIndicator;
import org.talend.dataquality.indicators.PossiblePhoneCountIndicator;
import org.talend.dataquality.indicators.ValidPhoneCountIndicator;
import org.talend.dataquality.indicators.ValidPhoneForRegionCountIndicator;
import org.talend.dataquality.indicators.ValidRegCodeCountIndicator;
import org.talend.dataquality.indicators.WellFormE164PhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormIntePhoneCountIndicator;
import org.talend.dataquality.indicators.WellFormNationalPhoneCountIndicator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Phone Numb Statistics Indicator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getWellFormNationalPhoneCountIndicator <em>Well Form National Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getWellFormIntePhoneCountIndicator <em>Well Form Inte Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getWellFormE164PhoneCountIndicator <em>Well Form E164 Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getInvalidRegCodeCountIndicator <em>Invalid Reg Code Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getPossiblePhoneCountIndicator <em>Possible Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getValidRegCodeCountIndicator <em>Valid Reg Code Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getValidPhoneCountIndicator <em>Valid Phone Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getValidPhoneForRegionCountIndicator <em>Valid Phone For Region Count Indicator</em>}</li>
 *   <li>{@link org.talend.dataquality.indicators.impl.PhoneNumbStatisticsIndicatorImpl#getFormatFreqPieIndicator <em>Format Freq Pie Indicator</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PhoneNumbStatisticsIndicatorImpl extends CompositeIndicatorImpl implements PhoneNumbStatisticsIndicator {
    /**
     * The cached value of the '{@link #getWellFormNationalPhoneCountIndicator() <em>Well Form National Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWellFormNationalPhoneCountIndicator()
     * @generated
     * @ordered
     */
    protected WellFormNationalPhoneCountIndicator wellFormNationalPhoneCountIndicator;
    /**
     * The cached value of the '{@link #getWellFormIntePhoneCountIndicator() <em>Well Form Inte Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWellFormIntePhoneCountIndicator()
     * @generated
     * @ordered
     */
    protected WellFormIntePhoneCountIndicator wellFormIntePhoneCountIndicator;
    /**
     * The cached value of the '{@link #getWellFormE164PhoneCountIndicator() <em>Well Form E164 Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWellFormE164PhoneCountIndicator()
     * @generated
     * @ordered
     */
    protected WellFormE164PhoneCountIndicator wellFormE164PhoneCountIndicator;
    /**
     * The cached value of the '{@link #getInvalidRegCodeCountIndicator() <em>Invalid Reg Code Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInvalidRegCodeCountIndicator()
     * @generated
     * @ordered
     */
    protected InvalidRegCodeCountIndicator invalidRegCodeCountIndicator;
    /**
     * The cached value of the '{@link #getPossiblePhoneCountIndicator() <em>Possible Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPossiblePhoneCountIndicator()
     * @generated
     * @ordered
     */
    protected PossiblePhoneCountIndicator possiblePhoneCountIndicator;
    /**
     * The cached value of the '{@link #getValidRegCodeCountIndicator() <em>Valid Reg Code Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValidRegCodeCountIndicator()
     * @generated
     * @ordered
     */
    protected ValidRegCodeCountIndicator validRegCodeCountIndicator;
    /**
     * The cached value of the '{@link #getValidPhoneCountIndicator() <em>Valid Phone Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValidPhoneCountIndicator()
     * @generated
     * @ordered
     */
    protected ValidPhoneCountIndicator validPhoneCountIndicator;

    /**
     * The cached value of the '{@link #getValidPhoneForRegionCountIndicator() <em>Valid Phone For Region Count Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValidPhoneForRegionCountIndicator()
     * @generated
     * @ordered
     */
    protected ValidPhoneForRegionCountIndicator validPhoneForRegionCountIndicator;
    /**
     * The cached value of the '{@link #getFormatFreqPieIndicator() <em>Format Freq Pie Indicator</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormatFreqPieIndicator()
     * @generated
     * @ordered
     */
    protected FormatFreqPieIndicator formatFreqPieIndicator;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PhoneNumbStatisticsIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.PHONE_NUMB_STATISTICS_INDICATOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WellFormNationalPhoneCountIndicator getWellFormNationalPhoneCountIndicator() {
        return wellFormNationalPhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWellFormNationalPhoneCountIndicator(WellFormNationalPhoneCountIndicator newWellFormNationalPhoneCountIndicator, NotificationChain msgs) {
        WellFormNationalPhoneCountIndicator oldWellFormNationalPhoneCountIndicator = wellFormNationalPhoneCountIndicator;
        wellFormNationalPhoneCountIndicator = newWellFormNationalPhoneCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR, oldWellFormNationalPhoneCountIndicator, newWellFormNationalPhoneCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWellFormNationalPhoneCountIndicator(WellFormNationalPhoneCountIndicator newWellFormNationalPhoneCountIndicator) {
        if (newWellFormNationalPhoneCountIndicator != wellFormNationalPhoneCountIndicator) {
            NotificationChain msgs = null;
            if (wellFormNationalPhoneCountIndicator != null)
                msgs = ((InternalEObject)wellFormNationalPhoneCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR, null, msgs);
            if (newWellFormNationalPhoneCountIndicator != null)
                msgs = ((InternalEObject)newWellFormNationalPhoneCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR, null, msgs);
            msgs = basicSetWellFormNationalPhoneCountIndicator(newWellFormNationalPhoneCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR, newWellFormNationalPhoneCountIndicator, newWellFormNationalPhoneCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WellFormIntePhoneCountIndicator getWellFormIntePhoneCountIndicator() {
        return wellFormIntePhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWellFormIntePhoneCountIndicator(WellFormIntePhoneCountIndicator newWellFormIntePhoneCountIndicator, NotificationChain msgs) {
        WellFormIntePhoneCountIndicator oldWellFormIntePhoneCountIndicator = wellFormIntePhoneCountIndicator;
        wellFormIntePhoneCountIndicator = newWellFormIntePhoneCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR, oldWellFormIntePhoneCountIndicator, newWellFormIntePhoneCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWellFormIntePhoneCountIndicator(WellFormIntePhoneCountIndicator newWellFormIntePhoneCountIndicator) {
        if (newWellFormIntePhoneCountIndicator != wellFormIntePhoneCountIndicator) {
            NotificationChain msgs = null;
            if (wellFormIntePhoneCountIndicator != null)
                msgs = ((InternalEObject)wellFormIntePhoneCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR, null, msgs);
            if (newWellFormIntePhoneCountIndicator != null)
                msgs = ((InternalEObject)newWellFormIntePhoneCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR, null, msgs);
            msgs = basicSetWellFormIntePhoneCountIndicator(newWellFormIntePhoneCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR, newWellFormIntePhoneCountIndicator, newWellFormIntePhoneCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WellFormE164PhoneCountIndicator getWellFormE164PhoneCountIndicator() {
        return wellFormE164PhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWellFormE164PhoneCountIndicator(WellFormE164PhoneCountIndicator newWellFormE164PhoneCountIndicator, NotificationChain msgs) {
        WellFormE164PhoneCountIndicator oldWellFormE164PhoneCountIndicator = wellFormE164PhoneCountIndicator;
        wellFormE164PhoneCountIndicator = newWellFormE164PhoneCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR, oldWellFormE164PhoneCountIndicator, newWellFormE164PhoneCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWellFormE164PhoneCountIndicator(WellFormE164PhoneCountIndicator newWellFormE164PhoneCountIndicator) {
        if (newWellFormE164PhoneCountIndicator != wellFormE164PhoneCountIndicator) {
            NotificationChain msgs = null;
            if (wellFormE164PhoneCountIndicator != null)
                msgs = ((InternalEObject)wellFormE164PhoneCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR, null, msgs);
            if (newWellFormE164PhoneCountIndicator != null)
                msgs = ((InternalEObject)newWellFormE164PhoneCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR, null, msgs);
            msgs = basicSetWellFormE164PhoneCountIndicator(newWellFormE164PhoneCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR, newWellFormE164PhoneCountIndicator, newWellFormE164PhoneCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InvalidRegCodeCountIndicator getInvalidRegCodeCountIndicator() {
        return invalidRegCodeCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInvalidRegCodeCountIndicator(InvalidRegCodeCountIndicator newInvalidRegCodeCountIndicator, NotificationChain msgs) {
        InvalidRegCodeCountIndicator oldInvalidRegCodeCountIndicator = invalidRegCodeCountIndicator;
        invalidRegCodeCountIndicator = newInvalidRegCodeCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR, oldInvalidRegCodeCountIndicator, newInvalidRegCodeCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInvalidRegCodeCountIndicator(InvalidRegCodeCountIndicator newInvalidRegCodeCountIndicator) {
        if (newInvalidRegCodeCountIndicator != invalidRegCodeCountIndicator) {
            NotificationChain msgs = null;
            if (invalidRegCodeCountIndicator != null)
                msgs = ((InternalEObject)invalidRegCodeCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR, null, msgs);
            if (newInvalidRegCodeCountIndicator != null)
                msgs = ((InternalEObject)newInvalidRegCodeCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR, null, msgs);
            msgs = basicSetInvalidRegCodeCountIndicator(newInvalidRegCodeCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR, newInvalidRegCodeCountIndicator, newInvalidRegCodeCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PossiblePhoneCountIndicator getPossiblePhoneCountIndicator() {
        return possiblePhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPossiblePhoneCountIndicator(PossiblePhoneCountIndicator newPossiblePhoneCountIndicator, NotificationChain msgs) {
        PossiblePhoneCountIndicator oldPossiblePhoneCountIndicator = possiblePhoneCountIndicator;
        possiblePhoneCountIndicator = newPossiblePhoneCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR, oldPossiblePhoneCountIndicator, newPossiblePhoneCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPossiblePhoneCountIndicator(PossiblePhoneCountIndicator newPossiblePhoneCountIndicator) {
        if (newPossiblePhoneCountIndicator != possiblePhoneCountIndicator) {
            NotificationChain msgs = null;
            if (possiblePhoneCountIndicator != null)
                msgs = ((InternalEObject)possiblePhoneCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR, null, msgs);
            if (newPossiblePhoneCountIndicator != null)
                msgs = ((InternalEObject)newPossiblePhoneCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR, null, msgs);
            msgs = basicSetPossiblePhoneCountIndicator(newPossiblePhoneCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR, newPossiblePhoneCountIndicator, newPossiblePhoneCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValidRegCodeCountIndicator getValidRegCodeCountIndicator() {
        return validRegCodeCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValidRegCodeCountIndicator(ValidRegCodeCountIndicator newValidRegCodeCountIndicator, NotificationChain msgs) {
        ValidRegCodeCountIndicator oldValidRegCodeCountIndicator = validRegCodeCountIndicator;
        validRegCodeCountIndicator = newValidRegCodeCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR, oldValidRegCodeCountIndicator, newValidRegCodeCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValidRegCodeCountIndicator(ValidRegCodeCountIndicator newValidRegCodeCountIndicator) {
        if (newValidRegCodeCountIndicator != validRegCodeCountIndicator) {
            NotificationChain msgs = null;
            if (validRegCodeCountIndicator != null)
                msgs = ((InternalEObject)validRegCodeCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR, null, msgs);
            if (newValidRegCodeCountIndicator != null)
                msgs = ((InternalEObject)newValidRegCodeCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR, null, msgs);
            msgs = basicSetValidRegCodeCountIndicator(newValidRegCodeCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR, newValidRegCodeCountIndicator, newValidRegCodeCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValidPhoneCountIndicator getValidPhoneCountIndicator() {
        return validPhoneCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValidPhoneCountIndicator(ValidPhoneCountIndicator newValidPhoneCountIndicator, NotificationChain msgs) {
        ValidPhoneCountIndicator oldValidPhoneCountIndicator = validPhoneCountIndicator;
        validPhoneCountIndicator = newValidPhoneCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR, oldValidPhoneCountIndicator, newValidPhoneCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValidPhoneCountIndicator(ValidPhoneCountIndicator newValidPhoneCountIndicator) {
        if (newValidPhoneCountIndicator != validPhoneCountIndicator) {
            NotificationChain msgs = null;
            if (validPhoneCountIndicator != null)
                msgs = ((InternalEObject)validPhoneCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR, null, msgs);
            if (newValidPhoneCountIndicator != null)
                msgs = ((InternalEObject)newValidPhoneCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR, null, msgs);
            msgs = basicSetValidPhoneCountIndicator(newValidPhoneCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR, newValidPhoneCountIndicator, newValidPhoneCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValidPhoneForRegionCountIndicator getValidPhoneForRegionCountIndicator() {
        return validPhoneForRegionCountIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValidPhoneForRegionCountIndicator(ValidPhoneForRegionCountIndicator newValidPhoneForRegionCountIndicator, NotificationChain msgs) {
        ValidPhoneForRegionCountIndicator oldValidPhoneForRegionCountIndicator = validPhoneForRegionCountIndicator;
        validPhoneForRegionCountIndicator = newValidPhoneForRegionCountIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR, oldValidPhoneForRegionCountIndicator, newValidPhoneForRegionCountIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValidPhoneForRegionCountIndicator(ValidPhoneForRegionCountIndicator newValidPhoneForRegionCountIndicator) {
        if (newValidPhoneForRegionCountIndicator != validPhoneForRegionCountIndicator) {
            NotificationChain msgs = null;
            if (validPhoneForRegionCountIndicator != null)
                msgs = ((InternalEObject)validPhoneForRegionCountIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR, null, msgs);
            if (newValidPhoneForRegionCountIndicator != null)
                msgs = ((InternalEObject)newValidPhoneForRegionCountIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR, null, msgs);
            msgs = basicSetValidPhoneForRegionCountIndicator(newValidPhoneForRegionCountIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR, newValidPhoneForRegionCountIndicator, newValidPhoneForRegionCountIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FormatFreqPieIndicator getFormatFreqPieIndicator() {
        return formatFreqPieIndicator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFormatFreqPieIndicator(FormatFreqPieIndicator newFormatFreqPieIndicator, NotificationChain msgs) {
        FormatFreqPieIndicator oldFormatFreqPieIndicator = formatFreqPieIndicator;
        formatFreqPieIndicator = newFormatFreqPieIndicator;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR, oldFormatFreqPieIndicator, newFormatFreqPieIndicator);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormatFreqPieIndicator(FormatFreqPieIndicator newFormatFreqPieIndicator) {
        if (newFormatFreqPieIndicator != formatFreqPieIndicator) {
            NotificationChain msgs = null;
            if (formatFreqPieIndicator != null)
                msgs = ((InternalEObject)formatFreqPieIndicator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR, null, msgs);
            if (newFormatFreqPieIndicator != null)
                msgs = ((InternalEObject)newFormatFreqPieIndicator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR, null, msgs);
            msgs = basicSetFormatFreqPieIndicator(newFormatFreqPieIndicator, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR, newFormatFreqPieIndicator, newFormatFreqPieIndicator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR:
                return basicSetWellFormNationalPhoneCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR:
                return basicSetWellFormIntePhoneCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR:
                return basicSetWellFormE164PhoneCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR:
                return basicSetInvalidRegCodeCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR:
                return basicSetPossiblePhoneCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR:
                return basicSetValidRegCodeCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR:
                return basicSetValidPhoneCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR:
                return basicSetValidPhoneForRegionCountIndicator(null, msgs);
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR:
                return basicSetFormatFreqPieIndicator(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR:
                return getWellFormNationalPhoneCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR:
                return getWellFormIntePhoneCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR:
                return getWellFormE164PhoneCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR:
                return getInvalidRegCodeCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR:
                return getPossiblePhoneCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR:
                return getValidRegCodeCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR:
                return getValidPhoneCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR:
                return getValidPhoneForRegionCountIndicator();
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR:
                return getFormatFreqPieIndicator();
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
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR:
                setWellFormNationalPhoneCountIndicator((WellFormNationalPhoneCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR:
                setWellFormIntePhoneCountIndicator((WellFormIntePhoneCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR:
                setWellFormE164PhoneCountIndicator((WellFormE164PhoneCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR:
                setInvalidRegCodeCountIndicator((InvalidRegCodeCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR:
                setPossiblePhoneCountIndicator((PossiblePhoneCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR:
                setValidRegCodeCountIndicator((ValidRegCodeCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR:
                setValidPhoneCountIndicator((ValidPhoneCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR:
                setValidPhoneForRegionCountIndicator((ValidPhoneForRegionCountIndicator)newValue);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR:
                setFormatFreqPieIndicator((FormatFreqPieIndicator)newValue);
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
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR:
                setWellFormNationalPhoneCountIndicator((WellFormNationalPhoneCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR:
                setWellFormIntePhoneCountIndicator((WellFormIntePhoneCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR:
                setWellFormE164PhoneCountIndicator((WellFormE164PhoneCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR:
                setInvalidRegCodeCountIndicator((InvalidRegCodeCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR:
                setPossiblePhoneCountIndicator((PossiblePhoneCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR:
                setValidRegCodeCountIndicator((ValidRegCodeCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR:
                setValidPhoneCountIndicator((ValidPhoneCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR:
                setValidPhoneForRegionCountIndicator((ValidPhoneForRegionCountIndicator)null);
                return;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR:
                setFormatFreqPieIndicator((FormatFreqPieIndicator)null);
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
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_NATIONAL_PHONE_COUNT_INDICATOR:
                return wellFormNationalPhoneCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_INTE_PHONE_COUNT_INDICATOR:
                return wellFormIntePhoneCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__WELL_FORM_E164_PHONE_COUNT_INDICATOR:
                return wellFormE164PhoneCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__INVALID_REG_CODE_COUNT_INDICATOR:
                return invalidRegCodeCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__POSSIBLE_PHONE_COUNT_INDICATOR:
                return possiblePhoneCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_REG_CODE_COUNT_INDICATOR:
                return validRegCodeCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_COUNT_INDICATOR:
                return validPhoneCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__VALID_PHONE_FOR_REGION_COUNT_INDICATOR:
                return validPhoneForRegionCountIndicator != null;
            case IndicatorsPackage.PHONE_NUMB_STATISTICS_INDICATOR__FORMAT_FREQ_PIE_INDICATOR:
                return formatFreqPieIndicator != null;
        }
        return super.eIsSet(featureID);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataquality.indicators.impl.CompositeIndicatorImpl#getChildIndicators() ADDED scorreia 2011-7-22
     */
    @Override
    public EList<Indicator> getChildIndicators() {
        EList<Indicator> children = new BasicEList<Indicator>();
        addChildToList(this.getValidPhoneCountIndicator(), children);
        addChildToList(this.getValidPhoneForRegionCountIndicator(), children);
        addChildToList(this.getPossiblePhoneCountIndicator(), children);
        addChildToList(this.getValidRegCodeCountIndicator(), children);
        addChildToList(this.getInvalidRegCodeCountIndicator(), children);
        addChildToList(this.getWellFormE164PhoneCountIndicator(), children);
        addChildToList(this.getWellFormIntePhoneCountIndicator(), children);
        addChildToList(this.getWellFormNationalPhoneCountIndicator(), children);
        addChildToList(this.getFormatFreqPieIndicator(), children);
        return children;
    }

} //PhoneNumbStatisticsIndicatorImpl
