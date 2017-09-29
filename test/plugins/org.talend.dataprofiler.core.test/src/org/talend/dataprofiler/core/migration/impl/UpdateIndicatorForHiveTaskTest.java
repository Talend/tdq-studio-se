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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * created by qiongli on 2012-9-6 Detailled comment
 *
 */
public class UpdateIndicatorForHiveTaskTest {



    private UpdateIndicatorForHiveTask updateHiveTask = null;

    /**
     * DOC qiongli Comment method "setUp".
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        updateHiveTask = new UpdateIndicatorForHiveTask();

    }

    /**
     * DOC qiongli Comment method "tearDown".
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test method update sql expression for hive connection in definition file.
     *
     * @throws Exception
     */
    @Test
    public void testDoExecute() throws Exception {
        boolean isOk = updateHiveTask.doExecute();
        assertTrue(isOk);
    }

}
