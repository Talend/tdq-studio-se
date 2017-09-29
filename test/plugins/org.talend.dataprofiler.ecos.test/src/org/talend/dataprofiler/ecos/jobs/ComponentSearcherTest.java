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
package org.talend.dataprofiler.ecos.jobs;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.talend.dataprofiler.ecos.model.IEcosCategory;

/**
 * test for ComponentSearcher class.
 * 
 */
public class ComponentSearcherTest {

    private String version = "5.4.0"; //$NON-NLS-1$

    /**
     * Test method for {@link org.talend.dataprofiler.ecos.jobs.ComponentSearcher#getAvailableCategory(String, boolean)
     * )} test when on dqRepository view has filter .
     * 
     * 
     */
    @Test
    public void testGetAvailableCategory_1() {
        try {
            // get Categories from the cache, this happens when on dqRepository view has filter.
            List<IEcosCategory> availableCategory = null;
            availableCategory = ComponentSearcher.getAvailableCategory(version, true);
            Assert.assertNull(availableCategory);

        } catch (Exception e) {
            // when there is no net to connect.
            if (e instanceof java.net.NoRouteToHostException) {
                Assert.assertEquals("No route to host: connect", e.getMessage()); //$NON-NLS-1$
            }
        }
    }

    /**
     * Test method for {@link org.talend.dataprofiler.ecos.jobs.ComponentSearcher#getAvailableCategory(String, boolean)
     * )} test when on dqRepository view has no filter.
     * 
     * 
     */
    @Test
    public void testGetAvailableCategory_2() {
        try {
            // get Categories from the internet, this happens when on dqRepository view has no filter.
            List<IEcosCategory> availableCategory2 = null;
            availableCategory2 = ComponentSearcher.getAvailableCategory(version, false);
            Assert.assertNotNull(availableCategory2);
        } catch (Exception e) {
            // when there is no net to connect.
            if (e instanceof java.net.NoRouteToHostException) {
                Assert.assertEquals("No route to host: connect", e.getMessage()); //$NON-NLS-1$
            }
        }
    }
}
