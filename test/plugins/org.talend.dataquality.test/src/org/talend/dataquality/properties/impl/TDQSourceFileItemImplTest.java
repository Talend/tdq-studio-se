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
package org.talend.dataquality.properties.impl;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.ByteArray;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.properties.PropertiesFactory;
import org.talend.dataquality.properties.TDQSourceFileItem;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class TDQSourceFileItemImplTest {

    TDQSourceFileItem sqlItem;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        sqlItem = PropertiesFactory.eINSTANCE.createTDQSourceFileItem();
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
     * Test method for {@link org.talend.dataquality.properties.impl.TDQSourceFileItemImpl#getContent()}.
     */
    @Test
    public void testGetContent() {
        ByteArray ba = sqlItem.getContent();
        Assert.assertNull(ba);

        ByteArray byteArray = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContent(PluginConstant.EMPTY_STRING.getBytes());

        sqlItem.setContent(byteArray);
        ba = sqlItem.getContent();
        Assert.assertNotNull(ba);
    }

    @Test
    public void testSetName() {
        sqlItem.setName("sql_test"); //$NON-NLS-1$
        Assert.assertEquals("sql_test", sqlItem.getName());//$NON-NLS-1$
    }

    @Test
    public void testGetName() {
        sqlItem.setName("sql_testcase");//$NON-NLS-1$
        Assert.assertEquals("sql_testcase", sqlItem.getName());//$NON-NLS-1$
    }

    @Test
    public void testSetExtension() {
        sqlItem.setExtension("sql");//$NON-NLS-1$
        Assert.assertEquals("sql", sqlItem.getExtension());//$NON-NLS-1$
    }

    @Test
    public void testGetExtension() {
        sqlItem.setExtension("sql2");//$NON-NLS-1$
        Assert.assertEquals("sql2", sqlItem.getExtension());//$NON-NLS-1$
    }

    @Test
    public void testSetFileExtension() {
        sqlItem.setFileExtension("sql");//$NON-NLS-1$
        Assert.assertEquals("sql", sqlItem.getFileExtension());//$NON-NLS-1$
        sqlItem.setFileExtension("notsql");//$NON-NLS-1$
        Assert.assertNotSame("not_sql", sqlItem.getFileExtension());//$NON-NLS-1$
    }

    @Test
    public void testGetFileExtension() {
        Assert.assertEquals(WorkspaceUtils.SQL_EXTENSION, sqlItem.getFileExtension());
    }
}
