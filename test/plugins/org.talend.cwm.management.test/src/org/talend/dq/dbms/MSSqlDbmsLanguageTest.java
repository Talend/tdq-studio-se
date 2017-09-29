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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class MSSqlDbmsLanguageTest {

    private String charsToReplace = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//$NON-NLS-1$ 

    private String replacementChars = "aaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999";//$NON-NLS-1$ 

    String[][] valuesNresults = { { "abc", "aaa" }, { "a1e", "a9a" }, { "dkmné", "aaaaa" } };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ 

    private String columnName = "navarchar";//$NON-NLS-1$ 

    /**
     * Test method for
     * {@link org.talend.dq.dbms.MSSqlDbmsLanguage#getPatternFinderFunction(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatternFinderFunction() {
        MSSqlDbmsLanguage dbms = new MSSqlDbmsLanguage();
        String value = "toto";//$NON-NLS-1$ 
        System.out.println(dbms.getPatternFinderFunction(value, charsToReplace, replacementChars));
    }

    /**
     * Test method for {@link org.talend.dq.dbms.MSSqlDbmsLanguage#CharLength(java.lang.String)}.
     */
    @Test
    public void testCharLength() {
        MSSqlDbmsLanguage dbms = new MSSqlDbmsLanguage();
        String charLength = dbms.charLength(columnName);
        String testCharLength = "LEN(" + columnName + ")";//$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(charLength.trim(), testCharLength);
    }
}
