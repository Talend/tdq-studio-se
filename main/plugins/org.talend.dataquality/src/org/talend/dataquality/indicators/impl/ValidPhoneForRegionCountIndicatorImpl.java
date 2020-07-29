/**
 */
package org.talend.dataquality.indicators.impl;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.TextParameters;
import org.talend.dataquality.indicators.ValidPhoneForRegionCountIndicator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Valid Phone For Region Count Indicator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.talend.dataquality.indicators.impl.ValidPhoneForRegionCountIndicatorImpl#getValidPhoneNumCount <em>Valid Phone Num Count</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ValidPhoneForRegionCountIndicatorImpl extends IndicatorImpl implements ValidPhoneForRegionCountIndicator {
    /**
     * The default value of the '{@link #getValidPhoneNumCount() <em>Valid Phone Num Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValidPhoneNumCount()
     * @generated
     * @ordered
     */
    protected static final Long VALID_PHONE_NUM_COUNT_EDEFAULT = new Long(0L);

    /**
     * The cached value of the '{@link #getValidPhoneNumCount() <em>Valid Phone Num Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValidPhoneNumCount()
     * @generated
     * @ordered
     */
    protected Long validPhoneNumCount = VALID_PHONE_NUM_COUNT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ValidPhoneForRegionCountIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.VALID_PHONE_FOR_REGION_COUNT_INDICATOR;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Long getValidPhoneNumCount() {
        return validPhoneNumCount;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValidPhoneNumCount(Long newValidPhoneNumCount) {
        Long oldValidPhoneNumCount = validPhoneNumCount;
        validPhoneNumCount = newValidPhoneNumCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, IndicatorsPackage.VALID_PHONE_FOR_REGION_COUNT_INDICATOR__VALID_PHONE_NUM_COUNT, oldValidPhoneNumCount, validPhoneNumCount));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Set<Object> getValidPhoneValues() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case IndicatorsPackage.VALID_PHONE_FOR_REGION_COUNT_INDICATOR__VALID_PHONE_NUM_COUNT:
                return getValidPhoneNumCount();
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
            case IndicatorsPackage.VALID_PHONE_FOR_REGION_COUNT_INDICATOR__VALID_PHONE_NUM_COUNT:
                setValidPhoneNumCount((Long)newValue);
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
            case IndicatorsPackage.VALID_PHONE_FOR_REGION_COUNT_INDICATOR__VALID_PHONE_NUM_COUNT:
                setValidPhoneNumCount(VALID_PHONE_NUM_COUNT_EDEFAULT);
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
            case IndicatorsPackage.VALID_PHONE_FOR_REGION_COUNT_INDICATOR__VALID_PHONE_NUM_COUNT:
                return VALID_PHONE_NUM_COUNT_EDEFAULT == null ? validPhoneNumCount != null : !VALID_PHONE_NUM_COUNT_EDEFAULT.equals(validPhoneNumCount);
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

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (validPhoneNumCount: ");
        result.append(validPhoneNumCount);
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        super.handle(data);
        if (data == null) {
            return false;
        }
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            IndicatorParameters indParameters = this.getParameters();
            TextParameters textParameters = indParameters == null ? null : indParameters.getTextParameter();
            String country = IndicatorHelper.getCountryCodeByParameter(textParameters);
            PhoneNumber phoneNumber = phoneUtil.parse(data.toString(), country);
            if (phoneUtil.isValidNumberForRegion(phoneNumber, country)) {
                // if (phoneUtil.isValidNumber(phoneNumber)) {
                validPhoneNumCount++;
                if (checkMustStoreCurrentRow() || checkMustStoreCurrentRow(drillDownValueCount)) {
                    this.mustStoreRow = true;
                }
            }

        } catch (NumberParseException e) {
            return false;
        }

        return true;
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

    @Override
    public boolean reset() {
        this.validPhoneNumCount = VALID_PHONE_NUM_COUNT_EDEFAULT;
        drillDownValueCount = 0l;
        return super.reset();
    }

    @Override
    public Long getIntegerValue() {
        return this.getValidPhoneNumCount();
    }

} //ValidPhoneForRegionCountIndicatorImpl
