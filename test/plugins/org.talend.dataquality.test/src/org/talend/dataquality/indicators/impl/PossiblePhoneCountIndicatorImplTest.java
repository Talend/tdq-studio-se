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
package org.talend.dataquality.indicators.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.TextParameters;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class PossiblePhoneCountIndicatorImplTest {

    PossiblePhoneCountIndicatorImpl possiblePhoneCountIndicatorImpl = null;

    private Object data_impossible[] = { "01012345678912", "086 18611281175", "08618611281175", "+08618611281175",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
            "123", "", null }; //$NON-NLS-1$ //$NON-NLS-2$ 

    private Object data_possible[] = {
            "18611281175", "8618611281175", "12345", "+86 18611281175", "+8618611281175", "01062153217", "010621532178" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ 

    @Before
    public void setUp() throws Exception {
        possiblePhoneCountIndicatorImpl = new PossiblePhoneCountIndicatorImpl();
        IndicatorParameters createIndicatorParameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
        TextParameters textParameters = IndicatorsFactory.eINSTANCE.createTextParameters();
        textParameters.setCountryCode(java.util.Locale.CHINA.getCountry());
        createIndicatorParameters.setTextParameter(textParameters);
        possiblePhoneCountIndicatorImpl.setParameters(createIndicatorParameters);
    }

    @Test
    public void testHandle_1() {
        for (Object obj : data_possible) {
            this.possiblePhoneCountIndicatorImpl.handle(obj);

        }
        assertEquals(7, possiblePhoneCountIndicatorImpl.getPossiblePhoneCount().longValue());
    }

    @Test
    public void testHandle_2() {
        for (Object obj : data_impossible) {
            this.possiblePhoneCountIndicatorImpl.handle(obj);

        }
        assertEquals(0, possiblePhoneCountIndicatorImpl.getPossiblePhoneCount().longValue());
    }
}
