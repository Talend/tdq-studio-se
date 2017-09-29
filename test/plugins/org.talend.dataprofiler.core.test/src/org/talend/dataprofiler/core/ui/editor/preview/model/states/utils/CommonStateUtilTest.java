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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.utils;


import org.junit.Assert;
import org.junit.Test;


/**
 * DOC qiongli class global comment. Detailled comment
 */
public class CommonStateUtilTest {

    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil#getUnitValue(java.lang.Object, int)}.
     */
    @Test
    public void testGetUnitValue_1() {
        String value = CommonStateUtil.getUnitValue("1.2", 0); //$NON-NLS-1$
        Assert.assertEquals(value, "1"); //$NON-NLS-1$
        value = CommonStateUtil.getUnitValue("1.2", 1); //$NON-NLS-1$
        Assert.assertEquals(value, "1.2"); //$NON-NLS-1$
        value = CommonStateUtil.getUnitValue("1.2", 2); //$NON-NLS-1$
        Assert.assertEquals(value, "1.20"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil#getUnitValue(java.lang.Object, int)}
     * .
     */
    @Test
    public void testGetUnitValue_2() {
        String value = CommonStateUtil.getUnitValue("0.003", 0); //$NON-NLS-1$
        Assert.assertEquals(value, "0"); //$NON-NLS-1$
        value = CommonStateUtil.getUnitValue("0.003", 1); //$NON-NLS-1$
        Assert.assertEquals(value, "0.0"); //$NON-NLS-1$
        value = CommonStateUtil.getUnitValue("0.003", 2); //$NON-NLS-1$
        Assert.assertEquals(value, "0.00"); //$NON-NLS-1$
        value = CommonStateUtil.getUnitValue("0.006", 2); //$NON-NLS-1$
        Assert.assertEquals(value, "0.01"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil#getUnitValue(java.lang.Object, int)}
     * .
     */
    @Test
    public void testGetUnitValue_3() {
        String value = CommonStateUtil.getUnitValue("3", 0); //$NON-NLS-1$
        Assert.assertEquals(value, "3"); //$NON-NLS-1$
        value = CommonStateUtil.getUnitValue("3", 1); //$NON-NLS-1$
        Assert.assertEquals(value, "3.0"); //$NON-NLS-1$
        value = CommonStateUtil.getUnitValue("3", 2); //$NON-NLS-1$
        Assert.assertEquals(value, "3.00"); //$NON-NLS-1$
    }

}
