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

/**
 * 
 * Validation the data is unique
 * 
 */
public class UniqueDataValidation extends DataValidationImpl {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.indicators.validation.AbstractDataValidation#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid(Object inputData) {
        if (Long.class.isInstance(inputData)) {
            Long dataFrequency = Long.valueOf(inputData.toString());
            if (dataFrequency == 1) {
                return true;
            }
        }
        return false;
    }

}
