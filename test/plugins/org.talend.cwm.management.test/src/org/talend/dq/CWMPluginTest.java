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
package org.talend.dq;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;

/**
 * created by qiongli on 2013-11-26 Detailled comment
 * 
 */
public class CWMPluginTest extends TestCase {

    private DatabaseConnection dbConn = ConnectionFactory.eINSTANCE.createDatabaseConnection();

    private String dbName = "conn1"; //$NON-NLS-1$

    @Override
    @Before
    public void setUp() throws Exception {
        dbConn.setURL(""); //$NON-NLS-1$
        dbConn.setName(dbName);
        dbConn.setUsername("root"); //$NON-NLS-1$
        dbConn.setRawPassword("root"); //$NON-NLS-1$
        dbConn.setContextMode(false);
    }

    /**
     * Test method "addConnetionAliasToSQLPlugin" for all not supported database type on DQ side.
     * {@link org.talend.dq.CWMPlugin#addConnetionAliasToSQLPlugin(orgomg.cwm.objectmodel.core.ModelElement[])}.
     */
    @Test
    public void testAddConnetionAliasToSQLPlugin_notSupportDB() {
        String allNoSupportedType[] = new String[] { EDatabaseTypeName.FIREBIRD.getXmlName(),
                EDatabaseTypeName.JAVADB.getXmlName(), EDatabaseTypeName.JAVADB_DERBYCLIENT.getXmlName(),
                EDatabaseTypeName.JAVADB_EMBEDED.getXmlName(), EDatabaseTypeName.JAVADB_JCCJDBC.getXmlName(),
                EDatabaseTypeName.GREENPLUM.getXmlName(), EDatabaseTypeName.HBASE.getXmlName(),
                EDatabaseTypeName.H2.getXmlName(), EDatabaseTypeName.INTERBASE.getXmlName(),
                EDatabaseTypeName.MAXDB.getXmlName(), EDatabaseTypeName.PARACCEL.getXmlName(),
                EDatabaseTypeName.SAPHana.getXmlName(), EDatabaseTypeName.SAS.getXmlName(),
                EDatabaseTypeName.VECTORWISE.getXmlName() };
        for (String dbType : allNoSupportedType) {
            dbConn.setDatabaseType(dbType);
            runAddConnetionAliasToSQLPlugin(false);
        }
    }

    /**
     * 
     * test method "addConnetionAliasToSQLPlugin" for all supported database type on DQ side.
     */
    @Test
    public void testAddConnetionAliasToSQLPlugin_supportDB() {
        List<String> tdqSupportDBType = MetadataConnectionUtils.getTDQSupportDBTemplate();
        for (String dbType : tdqSupportDBType) {
            dbConn.setDatabaseType(dbType);
            runAddConnetionAliasToSQLPlugin(true);
        }
    }

    /**
     * DOC qiongli Comment method "runAddConnetionAliasToSQLPlugin".
     */
    private void runAddConnetionAliasToSQLPlugin(boolean isSupportedDB) {
        CWMPlugin.getDefault().addConnetionAliasToSQLPlugin(dbConn);
        boolean aliasExist = SqlExplorerUtils.getDefault().aliasExist(dbConn.getName());
        if (isSupportedDB) {
            assertTrue(aliasExist);
        } else {
            assertFalse(aliasExist);
        }
    }
}
