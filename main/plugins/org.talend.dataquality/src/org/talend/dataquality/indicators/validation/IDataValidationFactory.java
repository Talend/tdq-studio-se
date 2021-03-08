// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import org.talend.dataquality.indicators.Indicator;

/**
 * created by talend on Dec 10, 2014 Detailled comment
 *
 */
public interface IDataValidationFactory {

    final IDataValidationFactory INSTANCE = new DataValidationFactory();

    /**
     *
     * Create Validation instance
     *
     * @param indicator
     * @return
     */
    public DataValidation createValidation(Indicator indicator);
}
