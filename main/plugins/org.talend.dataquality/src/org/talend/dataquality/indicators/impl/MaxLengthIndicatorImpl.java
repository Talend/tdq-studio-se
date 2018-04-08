/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.MaxLengthIndicator;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Max Length Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class MaxLengthIndicatorImpl extends LengthIndicatorImpl implements MaxLengthIndicator {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected MaxLengthIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.MAX_LENGTH_INDICATOR;
    }

    /*
     * (non-Javadoc) ADDED scorreia 2008-04-08 compute max length
     * 
     * @see org.talend.dataquality.indicators.impl.IndicatorImpl#handle(java.lang.Object)
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
                } else if (length.intValue() < strLength) {
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

    protected void zeroIsMax() {
        // first data is zero
        if (length == LENGTH_EDEFAULT) {
            length = 0L;
        }
        if (length == 0 && this.checkMustStoreCurrentRow()) {
            mustStoreRow = true;
        }
    }

} // MaxLengthIndicatorImpl
