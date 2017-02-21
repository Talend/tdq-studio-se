/**
 * <copyright> </copyright>
 * 
 * $Id$
 */
package org.talend.dataquality.indicators.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.emf.ecore.EClass;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.dataquality.indicators.DateParameters;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.QuarterFrequencyIndicator;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Quarter Frequency Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class QuarterFrequencyIndicatorImpl extends FrequencyIndicatorImpl implements QuarterFrequencyIndicator {

    private final String monthSign = "MM"; //$NON-NLS-1$ 

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected QuarterFrequencyIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.QUARTER_FREQUENCY_INDICATOR;
    }

    @Override
    public IndicatorParameters getParameters() {
        IndicatorParameters parameters = super.getParameters();
        if (parameters == null) {
            parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        }
        DateParameters dateParameters = parameters.getDateParameters();
        if (dateParameters == null) {
            dateParameters = IndicatorsFactory.eINSTANCE.createDateParameters();
        }
        dateParameters.setDateAggregationType(DateGrain.QUARTER);
        parameters.setDateParameters(dateParameters);
        this.setParameters(parameters);
        return parameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        if (data == null) {
            return super.handle(data);
        }
        if (data instanceof Date) {
            String format = getFormatName(data);
            return super.handle(format);
        }
        return super.handle(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.impl.FrequencyIndicatorImpl#getSpecialName(java.lang.Object)
     */
    @Override
    protected String getFormatName(Object data) {
        // add the quater pattern for each data.
        String monthStr = DateFormatUtils.format((Date) data, monthSign);
        int month = Integer.parseInt(monthStr);
        int quotient = month / 3;
        int remainder = month % 3;
        int quarter = remainder == 0 ? quotient : quotient + 1;
        return DateFormatUtils.format((Date) data, datePattern + quarter);
    }

    /**
     * set initail datePatten to 'yyyy'
     */
    @Override
    public boolean reset() {
        boolean flag = super.reset();
        super.datePattern = "yyyy";
        return flag;
    }

} // QuarterFrequencyIndicatorImpl
