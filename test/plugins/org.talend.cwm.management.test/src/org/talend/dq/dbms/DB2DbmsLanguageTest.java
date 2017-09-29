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
public class DB2DbmsLanguageTest {

    @Test
    public void testGetPatternFinderFunction() {
        String expectedResult = "TRANSLATE(\"address\" ,VARCHAR('aaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999'),'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789')"; //$NON-NLS-1$
        DB2DbmsLanguage db2DbmsLanguage = (DB2DbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.DB2DEFAULTURL);
        String query = db2DbmsLanguage
                .getPatternFinderFunction(
                        "\"address\"", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", "aaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Assert.assertEquals(expectedResult, query);
    }

}
