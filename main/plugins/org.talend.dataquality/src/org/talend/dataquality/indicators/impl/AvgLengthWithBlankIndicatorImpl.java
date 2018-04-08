/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.AvgLengthWithBlankIndicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Avg Length With Blank Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class AvgLengthWithBlankIndicatorImpl extends AverageLengthIndicatorImpl implements AvgLengthWithBlankIndicator {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected AvgLengthWithBlankIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.AVG_LENGTH_WITH_BLANK_INDICATOR;
    }

    @Override
    public IndicatorParameters getParameters() {
        parameters = super.getParameters();
        parameters.getTextParameter().setUseNulls(false);
        parameters.getTextParameter().setUseBlank(true);
        return parameters;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public boolean handle(Object data) {
        // override super.handle(data);
        this.mustStoreRow = false;
        if (data == null) {
            nullCount++;
        } else {
            count++;

            // blank strings count as zero length strings
            if (((String) data).trim().length() > 0) {
                String str = (String) data;
                sumLength += str.codePointCount(0, str.length());
            }
        }
        return true;
    }

} // AvgLengthWithBlankIndicatorImpl
