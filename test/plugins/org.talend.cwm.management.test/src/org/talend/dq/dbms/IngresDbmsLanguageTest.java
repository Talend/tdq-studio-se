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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * DOC yyin  class global comment. Detailled comment
 */
public class IngresDbmsLanguageTest {

    IngresDbmsLanguage ingresDbms;
    /**
     * DOC yyin Comment method "setUp".
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.ingresDbms = new IngresDbmsLanguage();
    }

    /**
     * DOC yyin Comment method "tearDown".
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dq.dbms.IngresDbmsLanguage#getTopNQuery(java.lang.String, int)}.
     */
    @Test
    public void testGetTopNQuery_1() {
        String topNQuery = this.ingresDbms.getTopNQuery("SELECT * from table", 25);
        Assert.assertEquals("SELECT FIRST 25 * from table", topNQuery);
    }

    @Test
    public void testGetTopNQuery_2() {
        String topNQuery = this.ingresDbms.getTopNQuery("select * from table", 10);
        Assert.assertEquals("select * from table", topNQuery);
    }
}
