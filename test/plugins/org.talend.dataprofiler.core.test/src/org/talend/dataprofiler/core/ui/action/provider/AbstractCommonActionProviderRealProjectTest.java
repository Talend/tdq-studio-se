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
package org.talend.dataprofiler.core.ui.action.provider;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.impl.RelationalFactoryImpl;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DFColumnFolderRepNode;
import org.talend.dq.nodes.factory.DQRepNodeCreateFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by talend on Dec 19, 2012 Detailled comment
 * 
 */
public class AbstractCommonActionProviderRealProjectTest {

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    String connectionName = "conn1"; //$NON-NLS-1$

    DatabaseConnectionItem createDatabaseConnectionItem = null;

    Logger log = Logger.getLogger(AbstractCommonActionProviderRealProjectTest.class);

    /**
     * DOC talend Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure("testForActionProviderTDQ"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case1 :parameter is null
     */
    @Test
    public void testGetConnectionCase1() {
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(null);
        Assert.assertTrue(connection == null);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case2 :viewObject is null but parameter of input is not a extend from IConnectionElementSubFolder(for example
     * DFColumnFolderRepNode)
     */
    @Test
    public void testGetConnectionCase2() {
        // create FileConnection
        // connection
        IRepositoryViewObject lastVersion = null;
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        DFColumnFolderRepNode dfcolFolderNode = new DFColumnFolderRepNode(lastVersion, null, null, null);
        // ~DFColumnFolderRepNode
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(dfcolFolderNode);
        Assert.assertTrue(connection == null);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case3 :input parameter is DBConnectionRepNode
     */
    @Test
    public void testGetConnectionCase3() {
        // create FileConnection
        // connection
        IRepositoryViewObject lastVersion = null;
        String propertyID = createConnectionItem(connectionName, null, null);
        try {
            lastVersion = factory.getLastVersion(propertyID);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        Assert.assertFalse(lastVersion == null);
        DBConnectionRepNode dbConnRepNode = new DBConnectionRepNode(lastVersion, null, null, null);
        // ~DFColumnFolderRepNode
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(dbConnRepNode);
        Assert.assertTrue(connection != null);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case4 :input parameter is DBCatalogRepNode
     */
    @Test
    public void testGetConnectionCase4() {
        // create FileConnection
        // connection
        IRepositoryViewObject lastVersion = null;
        String propertyID = createConnectionItem(connectionName, null, null);
        Catalog createCatalog = createCatalog("catalog1"); //$NON-NLS-1$
        try {
            lastVersion = factory.getLastVersion(propertyID);
            lastVersion = new MetadataCatalogRepositoryObject(lastVersion, createCatalog);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        Assert.assertFalse(lastVersion == null);

        RepositoryNode dbCatalogRepNode = DQRepNodeCreateFactory.createDBCatalogRepNode(lastVersion, null, null, null);
        // ~DFColumnFolderRepNode
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(dbCatalogRepNode);
        Assert.assertTrue(connection != null);
    }

    /**
     * DOC talend Comment method "createCatalog".
     * 
     * @param catalogName
     */
    private Catalog createCatalog(String catalogName) {
        Connection connection = createDatabaseConnectionItem.getConnection();
        Catalog createCatalog = CatalogHelper.createCatalog(catalogName);
        ConnectionHelper.addCatalog(createCatalog, connection);
        return createCatalog;
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case5 :input parameter is DBSchemaRepNode
     */
    @Test
    public void testGetConnectionCase5() {
        // create FileConnection
        // connection
        IRepositoryViewObject lastVersion = null;
        String propertyID = createConnectionItem(connectionName, null, null);
        Schema createSchema = createSchema("Schema1"); //$NON-NLS-1$
        try {
            lastVersion = factory.getLastVersion(propertyID);
            lastVersion = new MetadataSchemaRepositoryObject(lastVersion, createSchema);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        Assert.assertFalse(lastVersion == null);

        DBSchemaRepNode dbSchemaRepNode = new DBSchemaRepNode(lastVersion, null, null, null);
        // ~DFColumnFolderRepNode
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(dbSchemaRepNode);
        Assert.assertTrue(connection != null);
    }

    /**
     * DOC talend Comment method "createSchema".
     */
    private Schema createSchema(String catalogName) {
        Connection connection = createDatabaseConnectionItem.getConnection();
        Schema createSchema = SchemaHelper.createSchema(catalogName);
        ConnectionHelper.addSchema(createSchema, connection);
        return createSchema;
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case6 :input parameter is DBTableFolderRepNode
     */
    @Test
    public void testGetConnectionCase6() {
        // create FileConnection
        // connection
        IRepositoryViewObject lastVersion = null;
        String propertyID = createConnectionItem(connectionName, null, null);
        Catalog createCatalog = createCatalog("catalog1"); //$NON-NLS-1$
        try {
            lastVersion = factory.getLastVersion(propertyID);
            lastVersion = new MetadataCatalogRepositoryObject(lastVersion, createCatalog);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        Assert.assertFalse(lastVersion == null);
        RepositoryNode dbCatalogRepNode = DQRepNodeCreateFactory.createDBCatalogRepNode(lastVersion, null,
                ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        DBTableFolderRepNode dbTableFolderRepNode = new DBTableFolderRepNode(null, dbCatalogRepNode, null, null);
        // ~DFColumnFolderRepNode
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(dbTableFolderRepNode);
        Assert.assertTrue(connection != null);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case7 :input parameter is DBViewFolderRepNode
     */
    @Test
    public void testGetConnectionCase7() {
        // create FileConnection
        // connection
        IRepositoryViewObject lastVersion = null;
        String propertyID = createConnectionItem(connectionName, null, null);
        Catalog createCatalog = createCatalog("catalog1"); //$NON-NLS-1$
        try {
            lastVersion = factory.getLastVersion(propertyID);
            lastVersion = new MetadataCatalogRepositoryObject(lastVersion, createCatalog);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        Assert.assertFalse(lastVersion == null);
        RepositoryNode dbCatalogRepNode = DQRepNodeCreateFactory.createDBCatalogRepNode(lastVersion, null,
                ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        DBViewFolderRepNode dbViewFolderRepNode = new DBViewFolderRepNode(null, dbCatalogRepNode, null, null);
        // ~DFColumnFolderRepNode
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(dbViewFolderRepNode);
        Assert.assertTrue(connection != null);
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider#getConnection(java.lang.Object)}
     * . case8 :input parameter is DBColumnFolderRepNode
     */
    @Test
    public void testGetConnectionCase8() {
        // create FileConnection
        // connection
        IRepositoryViewObject lastVersion = null;
        String propertyID = createConnectionItem(connectionName, null, null);
        TdTable createTdTable = RelationalFactoryImpl.eINSTANCE.createTdTable();
        RepositoryNode dbCatalogRepNode = null;
        createTdTable.setName("table1"); //$NON-NLS-1$
        try {
            lastVersion = factory.getLastVersion(propertyID);
            dbCatalogRepNode = DQRepNodeCreateFactory.createDBCatalogRepNode(lastVersion, null, ENodeType.TDQ_REPOSITORY_ELEMENT,
                    null);
            lastVersion = new TdTableRepositoryObject(lastVersion, createTdTable);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        Assert.assertFalse(lastVersion == null);

        DBTableRepNode dbTableRepNode = new DBTableRepNode(lastVersion, dbCatalogRepNode, ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        DBColumnFolderRepNode dbColumnFolderRepNode = new DBColumnFolderRepNode(null, dbTableRepNode, null, null);
        // ~DFColumnFolderRepNode
        AbstractCommonActionProvider provider = new AbstractCommonActionProvider();
        Connection connection = provider.getConnection(dbColumnFolderRepNode);
        Assert.assertTrue(connection != null);
    }

    private String createConnectionItem(String name, IFolder folder, Boolean isDelete) {
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
        createConnection.setDatabaseType(EDatabaseTypeName.MYSQL.getXmlName());
        // ~connection
        createDatabaseConnectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();

        org.talend.core.model.properties.Property createDatabaseConnectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        createDatabaseConnectionProperty.setId(EcoreUtil.generateUUID());
        createDatabaseConnectionProperty.setItem(createDatabaseConnectionItem);
        createDatabaseConnectionProperty.setLabel(createConnection.getName());
        createDatabaseConnectionItem.setProperty(createDatabaseConnectionProperty);
        createDatabaseConnectionItem.setConnection(createConnection);
        try {
            factory.create(createDatabaseConnectionItem, createPath, false);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        return createDatabaseConnectionProperty.getId();
    }

}
