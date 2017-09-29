// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicators.validation;

import org.talend.cwm.indicator.DataValidation;
import org.talend.dataquality.indicators.DuplicateCountIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.UniqueCountIndicator;

/**
 * created by talend on Dec 10, 2014 Detailled comment
 * 
 */
public class DataValidationFactory implements IDataValidationFactory {

    protected DataValidationFactory() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.validation.IDataValidationFactory#createValidation(org.talend.dataquality.
     * indicators.Indicator)
     */
    @Override
    public DataValidation createValidation(Indicator indicator) {
        if (indicator == null) {
            return null;
        }
        if (DuplicateCountIndicator.class.isInstance(indicator)) {
            return new DuplicateDataValidation();
        } else if (UniqueCountIndicator.class.isInstance(indicator)) {
            return new UniqueDataValidation();
        } else {
            return new DataValidationImpl();
        }
    }
}
