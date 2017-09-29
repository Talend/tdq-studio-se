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
import org.talend.dataquality.indicators.DateGrain;

/**
 * created by talend on Feb 24, 2014 Detailled comment
 * 
 */
public class NetezzaDbmsLanguageTest {

    /**
     * Test method for {@link org.talend.dq.dbms.NetezzaDbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)}.
     */
    @Test
    public void testGetPatternFinderDefaultFunction() {
        String expectedResult = "TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(TRANSLATE(name,'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'0','9')"; //$NON-NLS-1$
        NetezzaDbmsLanguage netezzaDbmsLanguage = (NetezzaDbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);
        String patternFinderDefaultFunction = netezzaDbmsLanguage.getPatternFinderDefaultFunction("name"); //$NON-NLS-1$
        Assert.assertEquals(expectedResult, patternFinderDefaultFunction);

    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.NetezzaDbmsLanguage#replaceNullsWithString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplaceNullsWithString() {
        String expectedResult = " ISNULL(name,NULL TALEND)"; //$NON-NLS-1$
        NetezzaDbmsLanguage netezzaDbmsLanguage = (NetezzaDbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);
        String replaceNullsWithString = netezzaDbmsLanguage.replaceNullsWithString("name", "NULL TALEND"); //$NON-NLS-1$ //$NON-NLS-2$
        Assert.assertEquals(expectedResult, replaceNullsWithString);
    }

    /**
     * Test method for
     * {@link org.talend.dq.dbms.NetezzaDbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain, java.lang.String)}
     * .
     * 
     */
    @Test
    public void testExtract() {
        String expectedResult = "DATE_PART(\'" + DateGrain.DAY.getLiteral() + "\',name)"; //$NON-NLS-1$ //$NON-NLS-2$
        NetezzaDbmsLanguage netezzaDbmsLanguage = (NetezzaDbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);
        String extractString = netezzaDbmsLanguage.extract(DateGrain.DAY, "name"); //$NON-NLS-1$ 
        Assert.assertEquals(expectedResult, extractString);
    }

    /**
     * Test method for {@link org.talend.dq.dbms.NetezzaDbmsLanguage#getInvalidClauseBenFord(java.lang.String)} .
     * 
     * for task TDQ-8600
     * 
     * Before that we use "cast(colmnName as char(1))" to get the first character of data but there is one error say
     * that "Character width exceeded" when data type of input data is Number and it is null. So we use
     * "Substring(colmnName,1,1)" instead of it to fixed this error.By the way, if we have two conditions and link them
     * use "or" like that "condition1 or condition2", althougth condition1 is true the condition2 will execute too in
     * the netezza database.
     */
    @Test
    public void testGetInvalidClauseBenFord() {
        String colmnName = "name"; //$NON-NLS-1$
        String actualResult = colmnName
                + " is null or Substring(" + colmnName + ",1,1) not in ('0','1','2','3','4','5','6','7','8','9')";//$NON-NLS-1$ //$NON-NLS-2$
        NetezzaDbmsLanguage netezzaDbmsLanguage = (NetezzaDbmsLanguage) DbmsLanguageFactory
                .createDbmsLanguage(SupportDBUrlType.NETEZZADEFAULTURL);
        String resultString = netezzaDbmsLanguage.getInvalidClauseBenFord(colmnName);
        Assert.assertEquals(actualResult, resultString);
    }

}
