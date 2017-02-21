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
import org.talend.dataquality.indicators.YearFrequencyIndicator;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Year Frequency Indicator</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class YearFrequencyIndicatorImpl extends FrequencyIndicatorImpl implements YearFrequencyIndicator {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected YearFrequencyIndicatorImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return IndicatorsPackage.Literals.YEAR_FREQUENCY_INDICATOR;
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
        dateParameters.setDateAggregationType(DateGrain.YEAR);
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
            String format = DateFormatUtils.format((Date) data, datePattern);
            return super.handle(format);
        }
        return super.handle(data);
    }

    /**
     * set the date pattern to format
     */
    @Override
    public boolean reset() {
        boolean flag = super.reset();
        super.datePattern = "yyyy"; //$NON-NLS-1$ 
        return flag;
    }

} // YearFrequencyIndicatorImpl
