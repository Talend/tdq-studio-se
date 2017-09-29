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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class InfomixDbmsLanguageTest {

    InfomixDbmsLanguage infomix;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        infomix = new InfomixDbmsLanguage();
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dq.dbms.InfomixDbmsLanguage#getCatalogDelimiter()}.
     */
    @Test
    public void testGetCatalogDelimiter() {
        assertTrue(":".equals(infomix.getCatalogDelimiter()));
    }

    /**
     * Test method for {@link org.talend.dq.dbms.InfomixDbmsLanguage#getDelimiter()}.
     */
    @Test
    public void testGetDelimiter() {
        assertTrue(".".equals(infomix.getDelimiter()));
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.DbmsLanguage#toQualifiedName(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testToQualifiedName() {
        assertTrue("catalog:schema.table".equals(infomix.toQualifiedName("catalog", "schema", "table")));
    }

}
