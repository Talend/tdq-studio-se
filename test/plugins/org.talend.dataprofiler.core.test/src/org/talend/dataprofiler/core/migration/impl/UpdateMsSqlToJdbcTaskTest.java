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
package org.talend.dataprofiler.core.migration.impl;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;

/**
 * DOC qiongli Test case for split system indicators.
 */
public class UpdateMsSqlToJdbcTaskTest {

    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
    }

    @Test
    public void testDoExecute() throws Exception {
        DatabaseConnection dbConn = createConnectionItem("conn1", null); //$NON-NLS-1$
        UpdateMsSqlToJdbcTask updateMsSqlToJdbcTask = new UpdateMsSqlToJdbcTask();
        updateMsSqlToJdbcTask.doExecute();
        assertTrue("General JDBC".equals(dbConn.getDatabaseType())); //$NON-NLS-1$
        assertTrue(dbConn.getDriverJarPath() == null);
        assertTrue("".equals(dbConn.getDriverClass())); //$NON-NLS-1$
    }

    private DatabaseConnection createConnectionItem(String name, IFolder folder) {
        IPath createPath = Path.EMPTY;
        if (folder != null) {
            createPath = new Path(folder.getFullPath().lastSegment());
        }
        // connection
        DatabaseConnection createConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createConnection.setName(name);
        createConnection.setUsername("UserName"); //$NON-NLS-1$
        createConnection.setRawPassword("Password"); //$NON-NLS-1$
        createConnection.setURL("URL"); //$NON-NLS-1$
        createConnection.setDatabaseType("Microsoft SQL Server 2005/2008"); //$NON-NLS-1$
        createConnection.setContextMode(true);
        // ~connection
        DatabaseConnectionItem createDatabaseConnectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();

        org.talend.core.model.properties.Property createDatabaseConnectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        createDatabaseConnectionProperty.setId(EcoreUtil.generateUUID());
        createDatabaseConnectionProperty.setItem(createDatabaseConnectionItem);
        createDatabaseConnectionProperty.setLabel(createConnection.getName());
        createDatabaseConnectionItem.setProperty(createDatabaseConnectionProperty);
        createDatabaseConnectionItem.setConnection(createConnection);
        try {
            ProxyRepositoryFactory.getInstance().create(createDatabaseConnectionItem, createPath, false);
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
        return createConnection;
    }

}
