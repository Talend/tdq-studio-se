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
package org.talend.dataprofiler.ecos.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.talend.dataprofiler.ecos.jobs.ComponentSearcherTest;
import org.talend.dataprofiler.ecos.service.EcosystemServiceTest;

/**
 * DOC yyin class global comment. Detailled comment
 */
@RunWith(Suite.class)
@SuiteClasses({ ComponentSearcherTest.class, EcosystemServiceTest.class })
public class AllEcosTests {

}
