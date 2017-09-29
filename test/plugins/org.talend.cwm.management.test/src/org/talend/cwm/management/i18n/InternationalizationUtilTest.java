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
package org.talend.cwm.management.i18n;

import org.eclipse.core.runtime.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * created by xqliu on 2016年6月24日
 * Detailled comment
 *
 */
public class InternationalizationUtilTest {

    /**
     * DOC xqliu Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * DOC xqliu Comment method "tearDownAfterClass".
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC xqliu Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC xqliu Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.cwm.management.i18n.InternationalizationUtil#getDefinitionInternationalizationLabel(java.lang.String)}.
     */
    @Test
    public void testGetDefinitionInternationalizationLabelString() {
        // parameter is null
        String originalName = null;
        String label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isTrue("".equals(label));
        // parameter is empty
        originalName = "";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
        // parameter is blank
        originalName = " ";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
        // parameter is a normal string
        originalName = "someString";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
    }

    /**
     * Test method for
     * {@link org.talend.cwm.management.i18n.InternationalizationUtil#getCategoryInternationalizationLabel(java.lang.String)}.
     */
    @Test
    public void testGetCategoryInternationalizationLabel() {
        // parameter is null
        String originalName = null;
        String label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isTrue("".equals(label));
        // parameter is empty
        originalName = "";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
        // parameter is blank
        originalName = " ";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
        // parameter is a normal string
        originalName = "someString";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
    }

    /**
     * Test method for
     * {@link org.talend.cwm.management.i18n.InternationalizationUtil#getDefinitionInternationalizationLabel(org.talend.core.model.properties.Property)}
     * .
     */
    @Test
    public void testGetDefinitionInternationalizationLabelProperty() {
        // parameter is null
        String originalName = null;
        String label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isTrue("".equals(label));
        // parameter is empty
        originalName = "";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
        // parameter is blank
        originalName = " ";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
        // parameter is a normal string
        originalName = "someString";
        label = InternationalizationUtil.getCategoryInternationalizationLabel(originalName);
        Assert.isNotNull(label);
    }

}
