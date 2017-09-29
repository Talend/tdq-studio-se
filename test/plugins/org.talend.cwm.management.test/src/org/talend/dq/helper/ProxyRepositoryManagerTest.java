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
package org.talend.dq.helper;

import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.*;
import static org.powermock.api.support.membermodification.MemberModifier.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * DOC yyin class global comment. Detailled comment
 */
@PrepareForTest({ ProxyRepositoryFactory.class })
public class ProxyRepositoryManagerTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private ProxyRepositoryFactory proxFactory;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        proxFactory = mock(ProxyRepositoryFactory.class);
        stub(method(ProxyRepositoryFactory.class, "getInstance")).toReturn(proxFactory);//$NON-NLS-1$

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
     * Test method for
     * {@link org.talend.dq.helper.ProxyRepositoryManager#isLocked(org.talend.core.model.properties.Item)}.
     */
    @Test
    public void testIsLocked() {
        Item item = mock(Item.class);
        when(proxFactory.getStatus(item)).thenReturn(ERepositoryStatus.LOCK_BY_USER);
        Assert.assertTrue(ProxyRepositoryManager.getInstance().isLocked(item));

        when(proxFactory.getStatus(item)).thenReturn(ERepositoryStatus.LOCK_BY_OTHER);
        Assert.assertTrue(ProxyRepositoryManager.getInstance().isLocked(item));

        when(proxFactory.getStatus(item)).thenReturn(ERepositoryStatus.DEFAULT);
        Assert.assertFalse(ProxyRepositoryManager.getInstance().isLocked(item));

        when(proxFactory.getStatus(item)).thenReturn(ERepositoryStatus.READ_ONLY);
        Assert.assertFalse(ProxyRepositoryManager.getInstance().isLocked(item));
    }

}
