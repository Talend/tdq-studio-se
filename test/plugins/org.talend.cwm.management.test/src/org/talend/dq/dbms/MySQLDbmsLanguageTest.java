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
package org.talend.dq.dbms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * created by talend2 on 2012-12-17 Detailled comment
 * 
 */
public class MySQLDbmsLanguageTest {

    MySQLDbmsLanguage mySQLDbmsLanguage = null;

    String REGEXP_1 = "CHINA"; //$NON-NLS-1$

    String REGEXP_2 = "^[A-Z]*$"; //$NON-NLS-1$

    String SPACE = " "; //$NON-NLS-1$

    /**
     * DOC talend2 Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        if (mySQLDbmsLanguage == null) {
            mySQLDbmsLanguage = new MySQLDbmsLanguage();
        }

    }

    /**
     * Test method for {@link org.talend.dq.dbms.MySQLDbmsLanguage#regexLike(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testRegexLike() {
        String regexLike = mySQLDbmsLanguage.regexLike(REGEXP_1, REGEXP_2);
        Assert.assertNotNull(regexLike);
        Assert.assertEquals(SPACE + REGEXP_1 + " REGEXP BINARY " + REGEXP_2 + SPACE, regexLike); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.dbms.MySQLDbmsLanguage#regexNotLike(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testRegexNotLike() {
        String regexLike = mySQLDbmsLanguage.regexNotLike(REGEXP_1, REGEXP_2);
        Assert.assertNotNull(regexLike);
        Assert.assertEquals(SPACE + REGEXP_1 + "  NOT REGEXP BINARY " + REGEXP_2 + SPACE, regexLike); //$NON-NLS-1$
    }

}
