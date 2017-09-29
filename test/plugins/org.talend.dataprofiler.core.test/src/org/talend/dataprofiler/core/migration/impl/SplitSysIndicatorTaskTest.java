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

import org.junit.Test;

/**
 * DOC mzhao Test case for split system indicators.
 */
public class SplitSysIndicatorTaskTest {

    /**
     * Test method for {@link org.talend.dataprofiler.core.migration.AMigrationTask#execute()}.
     */
    @Test
    public void testExecute() {
        try {
            SplitSysIndicatorTask splitSysIndicatorTask = new SplitSysIndicatorTask();
            splitSysIndicatorTask.doExecute();
            // FIXME there is no assert here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
