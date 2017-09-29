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
package org.talend.dataprofiler.core.migration.impl;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.resources.IProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class CreateBenforLawIndicatorTaskTest {

    private CreateBenforLawIndicatorTask benTask;

    private String projectName;

    private IProject realProject;

    private File projectFile;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        benTask = new CreateBenforLawIndicatorTask();
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
     * Test method for {@link org.talend.dataprofiler.core.migration.impl.CreateSystemIndicatorTask#doExecute()}. no
     * meaning to test it.
     */
    @Test
    public void testDoExecute() {
        try {
            Assert.assertTrue(benTask.doExecute());
        } catch (Exception e) {
            Assert.fail("Create BenfordLaw indicator migration task failed." + e.getMessage());
            e.printStackTrace();
        }

    }

}
