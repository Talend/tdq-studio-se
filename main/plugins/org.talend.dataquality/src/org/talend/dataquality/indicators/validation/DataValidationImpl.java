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

/**
 * Validation data type
 * 
 */
public class DataValidationImpl implements DataValidation {

    private boolean checkKey = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.DataValidation#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid(Object inputData) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.DataValidation#isCheckKey()
     */
    @Override
    public boolean isCheckKey() {
        return checkKey;
    }

    /**
     * Sets the checkKey.
     * 
     * @param checkKey the checkKey to set
     */
    @Override
    public void setCheckKey(boolean checkKey) {
        this.checkKey = checkKey;
    }

}
