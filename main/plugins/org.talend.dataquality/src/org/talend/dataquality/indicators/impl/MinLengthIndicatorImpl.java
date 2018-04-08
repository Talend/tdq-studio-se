/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.MinLengthIndicator;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Min Length Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class MinLengthIndicatorImpl extends LengthIndicatorImpl implements MinLengthIndicator {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected MinLengthIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.MIN_LENGTH_INDICATOR;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean handle(Object data) {
        boolean ok = super.handle(data);
        if (data != null) {
            String str = (String) data;
            final int strLength = str.codePointCount(0, str.length());
            if (strLength > 0) {
                if ((length == LENGTH_EDEFAULT || length.intValue() == strLength)) {
                    length = Long.valueOf(strLength);
                    if (this.checkMustStoreCurrentRow()) {
                        mustStoreRow = true;
                    }
                } else if (length.intValue() > strLength) {
                    changeLength(strLength);
                    if (this.checkMustStoreCurrentRow()) {
                        mustStoreRow = true;
                    }
                }
            }
        }
        return ok;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.LengthIndicatorImpl#getParameters()
     */
    @Override
    public IndicatorParameters getParameters() {
        parameters = super.getParameters();
        parameters.getTextParameter().setUseNulls(false);
        parameters.getTextParameter().setUseBlank(false);
        return parameters;
    }

} // MinLengthIndicatorImpl
