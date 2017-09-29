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
package org.talend.dq.analysis.connpool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import junit.framework.Assert;

import org.eclipse.emf.ecore.EClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * DOC yyin class global comment. Detailled comment
 */
@PrepareForTest({ AnalysisHandler.class, SwitchHelpers.class, JavaSqlFactory.class })
public class TdqAnalysisConnectionPoolTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    TdqAnalysisConnectionPool connPool;

    Analysis analysis;

    org.talend.core.model.metadata.builder.connection.Connection dataManager;

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        analysis = mock(Analysis.class);
        AnalysisContext context = mock(AnalysisContext.class);
        AnalysisResult result = mock(AnalysisResult.class);
        dataManager = mock(org.talend.core.model.metadata.builder.connection.Connection.class);
        ExecutionInformations resultMetadata = mock(ExecutionInformations.class);

        PowerMockito.mockStatic(AnalysisHandler.class);
        AnalysisHandler mockHandler = mock(AnalysisHandler.class);
        when(AnalysisHandler.createHandler(analysis)).thenReturn(mockHandler);
        when(mockHandler.getNumberOfConnectionsPerAnalysis()).thenReturn(5);

        when(context.getConnection()).thenReturn(dataManager);
        when(analysis.getContext()).thenReturn(context);
        when(analysis.getResults()).thenReturn(result);
        when(result.getResultMetadata()).thenReturn(resultMetadata);

        connPool = new TdqAnalysisConnectionPool(analysis, 5);
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
     * {@link org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool#getConnectionPool(org.talend.dataquality.analysis.Analysis)}
     * .
     */
    @Test
    public void testGetConnectionPool() {
        TdqAnalysisConnectionPool pool = TdqAnalysisConnectionPool.getConnectionPool(analysis);
        assertNotNull(pool);
    }

    /**
     * Test method for {@link org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool#getConnection()}.
     * 
     * @throws SQLException
     */
    @Test
    public void testGetConnection() throws SQLException {
        TdqAnalysisConnectionPool pool = TdqAnalysisConnectionPool.getConnectionPool(analysis);
        assertNotNull(pool);

        // PowerMockito.mockStatic(SwitchHelpers.class);
        // ConnectionSwitch<org.talend.core.model.metadata.builder.connection.Connection> conSwitch =
        // mock(ConnectionSwitch.class);
        // when(SwitchHelpers.CONNECTION_SWITCH).thenReturn(conSwitch);
        EClass theEClass = mock(EClass.class);
        when(dataManager.eClass()).thenReturn(theEClass);
        when(theEClass.getClassifierID()).thenReturn(ConnectionPackage.CONNECTION);
        when(theEClass.eContainer()).thenReturn(ConnectionPackage.eINSTANCE);

        // org.talend.core.model.metadata.builder.connection.Connection dataprovider =
        // mock(org.talend.core.model.metadata.builder.connection.Connection.class);
        // when(conSwitch.doSwitch(dataManager)).thenReturn(dataprovider);

        PowerMockito.mockStatic(JavaSqlFactory.class);
        TypedReturnCode<Connection> trcConn = mock(TypedReturnCode.class);
        when(JavaSqlFactory.createConnection(dataManager)).thenReturn(trcConn);
        when(trcConn.isOk()).thenReturn(true);
        Connection conn = mock(Connection.class);
        when(trcConn.getObject()).thenReturn(conn);
        DatabaseMetaData metaData = mock(DatabaseMetaData.class);
        when(conn.getMetaData()).thenReturn(metaData);
        when(metaData.getMaxConnections()).thenReturn(100);

        Connection con = pool.getConnection();
        assertNotNull(con);
    }

    /**
     * Test method for {@link org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool#closeConnectionPool()}.
     */
    @Test
    public void testCloseConnectionPool() {
        TdqAnalysisConnectionPool pool = TdqAnalysisConnectionPool.getConnectionPool(analysis);
        assertNotNull(pool);
        pool.closeConnectionPool();
        Assert.assertTrue(pool.getPConnections().size() == 0);
    }

}
