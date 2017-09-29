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
package org.talend.dataprofiler.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

/**
 * Test kinds of database Help Link. using HttpClient components, see examples
 * http://hc.apache.org/httpcomponents-client-4.5.x/examples.html
 */
public class RegexExpeHelpUrlTest {

    private final String mysqlUrl = "http://dev.mysql.com/doc/refman/5.0/en/regexp.html"; //$NON-NLS-1$

    //    private final String oldSqlserverlUrl = "http://msdn.microsoft.com/en-us/magazine/cc163473.aspx"; //$NON-NLS-1$

    private final String sqlserverlUrl = "https://blogs.msdn.microsoft.com/sqlclr/2005/06/29/working-with-regular-expressions/"; //$NON-NLS-1$

    private final String oracleUrl = "http://docs.oracle.com/cd/E11882_01/appdev.112/e41502/adfns_regexp.htm"; //$NON-NLS-1$

    private final String postgreSqlUrl = "http://www.postgresql.org/docs/current/static/functions-matching.html"; //$NON-NLS-1$

    private final String db2Url = "http://www.ibm.com/developerworks/data/library/techarticle/0301stolze/0301stolze.html"; //$NON-NLS-1$

    private final String informixUrl = "http://www.ibm.com/developerworks/data/zones/informix/library/techarticle/db_regexp.html"; //$NON-NLS-1$

    private final String sqlLiteUrl = "http://www.sqlite.org/lang_expr.html"; //$NON-NLS-1$

    @Test
    public void testMysqlHelpUrl() throws ClientProtocolException, IOException {
        assertSatus(mysqlUrl);
    }

    @Test
    public void testOracleHelpUrl() throws ClientProtocolException, IOException {
        assertSatus(oracleUrl);

    }

    @Test
    public void testPostgreSqlHelpUrl() throws ClientProtocolException, IOException {
        assertSatus(postgreSqlUrl);
    }

    @Test
    public void testDB2HelpUrl() throws ClientProtocolException, IOException {
        assertSatus(db2Url);
    }

    @Test
    public void testInformixHelpUrl() throws ClientProtocolException, IOException {
        assertSatus(informixUrl);
    }

    @Test
    public void testSQLServerHelpUrl() throws ClientProtocolException, IOException {
        assertSatus(sqlserverlUrl);
        // StatusLine statusLine = execute(oldSqlserverlUrl);
        // assertNotNull(statusLine);
        // assertFalse(statusLine.getStatusCode() == 200);
    }

    @Test
    public void testSQLLiteelpUrl() throws ClientProtocolException, IOException {
        assertSatus(sqlLiteUrl);
    }

    private void assertSatus(String helpUrl) throws ClientProtocolException, IOException {
        StatusLine statusLine = execute(helpUrl);
        assertNotNull(helpUrl + "status is null!" + statusLine); //$NON-NLS-1$
        assertEquals(helpUrl + "has an unexpected http status code: statusLine.getStatusCode()" + 200, 200, //$NON-NLS-1$
                statusLine.getStatusCode());
    }

    private StatusLine execute(String helpUrl) throws ClientProtocolException, IOException {
        StatusLine statusLine = null;
        CloseableHttpClient httpclient = HttpClients.createMinimal();
        try {
            HttpGet httpget = new HttpGet(helpUrl);
            CloseableHttpResponse execute = httpclient.execute(httpget);
            statusLine = execute.getStatusLine();

        } finally {
            httpclient.close();
        }
        return statusLine;
    }
}
