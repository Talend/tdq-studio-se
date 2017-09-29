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

import org.junit.Assert;
import org.junit.Test;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.MaxValueIndicator;

/**
 * created by yyin on 2016年5月12日 Detailled comment
 *
 */
public class MaxValueIndicatorImplTest {

    /**
     * Test method for {@link org.talend.dataquality.indicators.impl.MaxValueIndicatorImpl#handle(java.lang.Object)}.
     */
    @Test
    public void testHandleObject() {
        MaxValueIndicator maxValueIndicator = IndicatorsFactory.eINSTANCE.createMaxValueIndicator();
        Object data = null;
        Assert.assertTrue(maxValueIndicator.handle(data));
    }

}
