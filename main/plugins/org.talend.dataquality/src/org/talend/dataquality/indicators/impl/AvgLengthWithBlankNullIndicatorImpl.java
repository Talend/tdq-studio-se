/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.AvgLengthWithBlankNullIndicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Avg Length With Blank Null Indicator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class AvgLengthWithBlankNullIndicatorImpl extends AverageLengthIndicatorImpl implements AvgLengthWithBlankNullIndicator {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected AvgLengthWithBlankNullIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.AVG_LENGTH_WITH_BLANK_NULL_INDICATOR;
    }

    @Override
    public IndicatorParameters getParameters() {
        parameters = super.getParameters();
        parameters.getTextParameter().setUseNulls(true);
        parameters.getTextParameter().setUseBlank(true);
        return parameters;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.AvgLengthWithBlankIndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        // override super.handle(data);
        this.mustStoreRow = false;
        count++; // count all rows
        if (data == null) {
            nullCount++;
        } else {
            // blank strings count as zero length strings
            if (((String) data).trim().length() > 0) {
                String str = (String) data;
                sumLength += str.codePointCount(0, str.length());
            }
        }
        return true;
    }

} // AvgLengthWithBlankNullIndicatorImpl
