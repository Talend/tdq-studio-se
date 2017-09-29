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

import org.junit.Test;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class TeradataDbmsLanguageTest {

	@Test
	public void testExtractQuarter() {
		String expectedResult = "(((CAST( EXTRACT(month FROM name) AS BYTEINT)-1)/3)+1)"; //$NON-NLS-1$
		TeradataDbmsLanguage teradataDbmsLanguage = (TeradataDbmsLanguage) DbmsLanguageFactory
				.createDbmsLanguage(SupportDBUrlType.TERADATADEFAULTURL);
		String query = teradataDbmsLanguage.extractQuarter("name"); //$NON-NLS-1$
		Assert.assertEquals(expectedResult, query);

	}

	@Test
	public void testExtractWeek() {
		String expectedResult = "((CAST(name AS DATE) - (( EXTRACT(year FROM name) - 1900) * 10000" //$NON-NLS-1$
				+ " + 0101 (DATE))) - ((CAST(name AS DATE) - DATE '0001-01-07') MOD 7)  + 13) / 7"; //$NON-NLS-1$
		TeradataDbmsLanguage teradataDbmsLanguage = (TeradataDbmsLanguage) DbmsLanguageFactory
				.createDbmsLanguage(SupportDBUrlType.TERADATADEFAULTURL);
		String query = teradataDbmsLanguage.extractWeek("name"); //$NON-NLS-1$
		Assert.assertEquals(expectedResult, query);
	}

}
