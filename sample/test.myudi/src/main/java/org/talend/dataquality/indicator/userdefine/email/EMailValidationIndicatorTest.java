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
package org.talend.dataquality.indicator.userdefine.email;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * created by zhao on 2012-8-27 Detailled comment
 * 
 */
public class EMailValidationIndicatorTest {

    private EMailValidationIndicator emailValidationIndicator = null;

    private final static String[] emails = new String[] {
            "mzhao@talend.cn", "xxxmzhao@talend.com", "minglee.zhao@gmail.com", "paslor@126.com", "paslor@hotmail.com", "mzhao@Talxxx.com", null, "", " ", "a@", "@b", "@hotmail.com" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ 

    private final static boolean[] valid = { false, true, true, true, false, false, false, false, false, false, false, false };

    /**
     * DOC zhao Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        emailValidationIndicator = new EMailValidationIndicator();
        emailValidationIndicator.setName(this.getClass().getName());
        emailValidationIndicator.setEmailAddress("mzhao@talend.com"); //$NON-NLS-1$
        Assert.assertEquals(emails.length, valid.length);
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicator.userdefine.email.EMailValidationIndicator#handle(java.lang.Object)}.
     */
    @Test
    public void testHandle() {
        emailValidationIndicator.reset();

        for (String email : emails) {
            emailValidationIndicator.handle(email);
        }
        // The total count
        Assert.assertEquals(emails.length, emailValidationIndicator.getCount().longValue());
        // The matching count
        Assert.assertEquals(3, emailValidationIndicator.getMatchingValueCount().longValue());
    }

    /**
     * Test method for
     * {@link org.talend.dataquality.indicator.userdefine.email.EMailValidationIndicator#isAddressValid(java.lang.String)}
     */
    @Test
    public void testIsAddressValid() {
        emailValidationIndicator.reset();
        for (int j = 0; j < emails.length; j++) {
            Assert.assertEquals(valid[j], emailValidationIndicator.isAddressValid(emails[j]));
        }
    }

    // TODO create different parameters and test the initparameter method.
}
