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
package org.talend.dq.helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdViewRepositoryObject;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * junit test case for class RepositoryNodeHelper, use real objects to test it.
 * 
 */
public class RepositoryNodeHelperRealTest {

    Logger log = Logger.getLogger(RepositoryNodeHelperRealTest.class);

    private UnitTestBuildHelper unitTestBuildHelper;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // do something here
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // do something here
    }

    @Before
    public void setUp() throws Exception {
        // do something here
        UnitTestBuildHelper.initProjectStructure();
    }

    @After
    public void tearDown() throws Exception {
        // do something here
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#getIFolder(org.talend.repository.model.IRepositoryNode)}.
     */
    @Ignore
    @Test
    public void testGetIFolder() {
        if (this.unitTestBuildHelper != null && this.unitTestBuildHelper.isInit()) {
            // test DataProfilingNode
            RepositoryNode dataProfilingNode = this.unitTestBuildHelper.getDataProfilingNode();
            IFolder dataProfilingFolder = RepositoryNodeHelper.getIFolder(dataProfilingNode);

            IPath pathA1 = dataProfilingFolder.getFullPath();
            IPath pathA2 = Path.EMPTY.append(this.unitTestBuildHelper.gettProject().getEmfProject().getTechnicalLabel()).append(
                    ERepositoryObjectType.TDQ_DATA_PROFILING.getFolder());
            assertTrue(pathA1.makeRelativeTo(pathA2) == Path.EMPTY);

            String filePathA1 = WorkspaceUtils.ifolderToFile(dataProfilingFolder).toString();
            String filePathA2 = this.unitTestBuildHelper.getiProject().getWorkspace().getRoot().getLocation().toOSString()
                    + File.separator + this.unitTestBuildHelper.gettProject().getEmfProject().getTechnicalLabel()
                    + File.separator + ERepositoryObjectType.TDQ_DATA_PROFILING.getFolder();
            assertTrue(filePathA1.equals(filePathA2));

            // test LibrariesNode
            RepositoryNode librariesNode = this.unitTestBuildHelper.getLibrariesNode();
            IFolder librariesFolder = RepositoryNodeHelper.getIFolder(librariesNode);

            IPath pathB1 = librariesFolder.getFullPath();
            IPath pathB2 = Path.EMPTY.append(this.unitTestBuildHelper.gettProject().getEmfProject().getTechnicalLabel()).append(
                    ERepositoryObjectType.TDQ_LIBRARIES.getFolder());
            assertTrue(pathB1.makeRelativeTo(pathB2) == Path.EMPTY);

            String filePathB1 = WorkspaceUtils.ifolderToFile(librariesFolder).toString();
            String filePathB2 = this.unitTestBuildHelper.getiProject().getWorkspace().getRoot().getLocation().toOSString()
                    + File.separator + this.unitTestBuildHelper.gettProject().getEmfProject().getTechnicalLabel()
                    + File.separator + ERepositoryObjectType.TDQ_LIBRARIES.getFolder();
            assertTrue(filePathB1.equals(filePathB2));

            // test for MetadataNode
            RepositoryNode metadataNode = this.unitTestBuildHelper.getMetadataNode();
            IFolder metadataFolder = RepositoryNodeHelper.getIFolder(metadataNode);

            IPath pathC1 = metadataFolder.getFullPath();
            IPath pathC2 = Path.EMPTY.append(this.unitTestBuildHelper.gettProject().getEmfProject().getTechnicalLabel()).append(
                    ERepositoryObjectType.METADATA.getFolder());
            assertTrue(pathC1.makeRelativeTo(pathC2) == Path.EMPTY);

            String filePathC1 = WorkspaceUtils.ifolderToFile(metadataFolder).toString();
            String filePathC2 = this.unitTestBuildHelper.getiProject().getWorkspace().getRoot().getLocation().toOSString()
                    + File.separator + this.unitTestBuildHelper.gettProject().getEmfProject().getTechnicalLabel()
                    + File.separator + ERepositoryObjectType.METADATA.getFolder();
            assertTrue(filePathC1.equals(filePathC2));
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#createRepositoryNode(orgomg.cwm.objectmodel.core.ModelElement)}.
     * parameter is null
     */
    @Test
    public void testCreateMysqlColumnRepositoryNodeParameterNull() {
        RepositoryNode createRepositoryNode = RepositoryNodeHelper.createRepositoryNode(null);
        Assert.assertTrue(createRepositoryNode == null);
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#createRepositoryNode(orgomg.cwm.objectmodel.core.ModelElement)}.
     * mysql case find column node
     */
    @Test
    public void testCreateMysqlColumnRepositoryNode() {
        DatabaseConnectionItem createConnectionItem = createDataBaseConnection("conn1", null, false); //$NON-NLS-1$
        Catalog addCatalog = this.addCatalog(createConnectionItem.getConnection(), "catalog1"); //$NON-NLS-1$
        TdTable addTable = this.addTable(addCatalog, "table1"); //$NON-NLS-1$
        TdColumn addColumn = this.addColumn(addTable, "column1"); //$NON-NLS-1$
        try {
            ProxyRepositoryFactory.getInstance().save(createConnectionItem, null);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        RepositoryNode createRepositoryNode = RepositoryNodeHelper.createRepositoryNode(addColumn);
        if (createRepositoryNode != null) {
            IRepositoryViewObject object = createRepositoryNode.getObject();
            Assert.assertTrue(createRepositoryNode instanceof DBColumnRepNode);
            Assert.assertTrue(object != null);
            Assert.assertTrue(object instanceof MetadataColumnRepositoryObject);
            Assert.assertTrue(object.getId().equals(addColumn.getName()));
            Assert.assertTrue(object.getLabel().equals(addColumn.getName()));
            Assert.assertTrue(object.getRepositoryNode() != null);
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.LABEL).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.CONTENT_TYPE).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getParent().getParent().getParent().getParent().getParent() != null);
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#createRepositoryNode(orgomg.cwm.objectmodel.core.ModelElement)}.
     * mysql case find table node
     */
    @Test
    public void testCreateMysqlTableRepositoryNode() {
        DatabaseConnectionItem createConnectionItem = createDataBaseConnection("conn1", null, false); //$NON-NLS-1$
        Catalog addCatalog = this.addCatalog(createConnectionItem.getConnection(), "catalog1"); //$NON-NLS-1$
        TdTable addTable = this.addTable(addCatalog, "table1"); //$NON-NLS-1$
        try {
            ProxyRepositoryFactory.getInstance().save(createConnectionItem, null);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        RepositoryNode createRepositoryNode = RepositoryNodeHelper.createRepositoryNode(addTable);
        if (createRepositoryNode != null) {
            IRepositoryViewObject object = createRepositoryNode.getObject();
            Assert.assertTrue(createRepositoryNode instanceof DBTableRepNode);
            Assert.assertTrue(object != null);
            Assert.assertTrue(object instanceof TdTableRepositoryObject);
            Assert.assertTrue(object.getId().equals(addTable.getName()));
            Assert.assertTrue(object.getLabel().equals(addTable.getName()));
            Assert.assertTrue(object.getRepositoryNode() != null);
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.LABEL).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.CONTENT_TYPE).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getParent().getParent().getParent() != null);
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#createRepositoryNode(orgomg.cwm.objectmodel.core.ModelElement)}.
     * mysql case find View node
     */
    @Test
    public void testCreateMysqlViewRepositoryNode() {
        DatabaseConnectionItem createConnectionItem = createDataBaseConnection("conn1", null, false); //$NON-NLS-1$
        Catalog addCatalog = this.addCatalog(createConnectionItem.getConnection(), "catalog1"); //$NON-NLS-1$
        TdView addView = this.addView(addCatalog, "view1"); //$NON-NLS-1$
        try {
            ProxyRepositoryFactory.getInstance().save(createConnectionItem, null);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        RepositoryNode createRepositoryNode = RepositoryNodeHelper.createRepositoryNode(addView);
        if (createRepositoryNode != null) {
            IRepositoryViewObject object = createRepositoryNode.getObject();
            Assert.assertTrue(createRepositoryNode instanceof DBViewRepNode);
            Assert.assertTrue(object != null);
            Assert.assertTrue(object instanceof TdViewRepositoryObject);
            Assert.assertTrue(object.getId().equals(addView.getName()));
            Assert.assertTrue(object.getLabel().equals(addView.getName()));
            Assert.assertTrue(object.getRepositoryNode() != null);
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.LABEL).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.CONTENT_TYPE).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getParent().getParent().getParent() != null);
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#createRepositoryNode(orgomg.cwm.objectmodel.core.ModelElement)}.
     * mssql case
     */
    @Test
    public void testCreateMssqlRepositoryNode() {
        DatabaseConnectionItem createConnectionItem = createDataBaseConnection("conn1", null, false); //$NON-NLS-1$
        Catalog addCatalog = this.addCatalog(createConnectionItem.getConnection(), "catalog1"); //$NON-NLS-1$
        Schema addSchema = this.addSchema(addCatalog, "schema1"); //$NON-NLS-1$
        TdTable addTable = this.addTable(addSchema, "table1"); //$NON-NLS-1$
        TdColumn addColumn = this.addColumn(addTable, "column1"); //$NON-NLS-1$
        try {
            ProxyRepositoryFactory.getInstance().save(createConnectionItem, null);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        RepositoryNode createRepositoryNode = RepositoryNodeHelper.createRepositoryNode(addColumn);
        if (createRepositoryNode != null) {
            IRepositoryViewObject object = createRepositoryNode.getObject();
            Assert.assertTrue(createRepositoryNode instanceof DBColumnRepNode);
            Assert.assertTrue(object != null);
            Assert.assertTrue(object instanceof MetadataColumnRepositoryObject);
            Assert.assertTrue(object.getId().equals(addColumn.getName()));
            Assert.assertTrue(object.getLabel().equals(addColumn.getName()));
            Assert.assertTrue(object.getRepositoryNode() != null);
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.LABEL).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.CONTENT_TYPE).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getParent().getParent().getParent().getParent().getParent().getParent() != null);
        }
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#createRepositoryNode(orgomg.cwm.objectmodel.core.ModelElement)}.
     * oracle case
     */
    @Test
    public void testCreateOracleRepositoryNode() {
        DatabaseConnectionItem createConnectionItem = createDataBaseConnection("conn1", null, false); //$NON-NLS-1$
        Schema addSchema = this.addSchema(createConnectionItem.getConnection(), "catalog1"); //$NON-NLS-1$
        TdTable addTable = this.addTable(addSchema, "table1"); //$NON-NLS-1$
        TdColumn addColumn = this.addColumn(addTable, "column1"); //$NON-NLS-1$
        try {
            ProxyRepositoryFactory.getInstance().save(createConnectionItem, null);
        } catch (PersistenceException e) {
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        RepositoryNode createRepositoryNode = RepositoryNodeHelper.createRepositoryNode(addColumn);
        if (createRepositoryNode != null) {
            IRepositoryViewObject object = createRepositoryNode.getObject();
            Assert.assertTrue(createRepositoryNode instanceof DBColumnRepNode);
            Assert.assertTrue(object != null);
            Assert.assertTrue(object instanceof MetadataColumnRepositoryObject);
            Assert.assertTrue(object.getId().equals(addColumn.getName()));
            Assert.assertTrue(object.getLabel().equals(addColumn.getName()));
            Assert.assertTrue(object.getRepositoryNode() != null);
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.LABEL).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getProperties(EProperties.CONTENT_TYPE).equals(
                    ERepositoryObjectType.METADATA_CON_COLUMN));
            Assert.assertTrue(createRepositoryNode.getParent().getParent().getParent().getParent().getParent() != null);
        }
    }

    private DatabaseConnectionItem createDataBaseConnection(String name, IFolder folder, Boolean isDelete) {
        DatabaseConnection createConnection = null;
        IPath createPath = Path.EMPTY;
        if (folder != null) {
            createPath = new Path(folder.getFullPath().lastSegment());
        }
        // connection
        createConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createConnection.setName(name);
        createConnection.setUsername("UserName"); //$NON-NLS-1$
        createConnection.setRawPassword("Password"); //$NON-NLS-1$
        createConnection.setURL("URL"); //$NON-NLS-1$
        createConnection.setDatabaseType(SupportDBUrlType.MYSQLDEFAULTURL.getDBKey());
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
            log.error(e, e);
            Assert.fail(e.getMessage());
        }
        return createDatabaseConnectionItem;
    }

    public Catalog addCatalog(Connection conn, String catalogName) {
        Catalog createCatalog = CatalogHelper.createCatalog(catalogName);
        ConnectionHelper.addCatalog(createCatalog, conn);
        return createCatalog;
    }

    public Schema addSchema(Catalog catalog, String schemaName) {
        Schema createSchema = SchemaHelper.createSchema(schemaName);
        List<Schema> schemaList = new ArrayList<Schema>();
        schemaList.add(createSchema);
        CatalogHelper.addSchemas(schemaList, catalog);
        return createSchema;
    }

    public Schema addSchema(Connection conn, String schemaName) {
        Schema createSchema = SchemaHelper.createSchema(schemaName);
        ConnectionHelper.addSchema(createSchema, conn);
        return createSchema;
    }

    public TdTable addTable(Catalog catalog, String tableName) {
        TdTable createTdTable = RelationalFactory.eINSTANCE.createTdTable();
        createTdTable.setName(tableName);
        List<TdTable> tables = new ArrayList<TdTable>();
        tables.add(createTdTable);
        CatalogHelper.addTables(tables, catalog);
        return createTdTable;
    }

    public TdTable addTable(Schema schema, String tableName) {
        TdTable createTdTable = RelationalFactory.eINSTANCE.createTdTable();
        createTdTable.setName(tableName);
        List<TdTable> tables = new ArrayList<TdTable>();
        tables.add(createTdTable);
        SchemaHelper.addTables(tables, schema);
        return createTdTable;
    }

    public TdView addView(Catalog catalog, String tableName) {
        TdView createTdView = RelationalFactory.eINSTANCE.createTdView();
        createTdView.setName(tableName);
        List<TdView> tables = new ArrayList<TdView>();
        tables.add(createTdView);
        CatalogHelper.addViews(tables, catalog);
        return createTdView;
    }

    public TdColumn addColumn(TdTable table, String columnName) {
        TdColumn createTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        createTdColumn.setName(columnName);
        TableHelper.addColumn(table, createTdColumn);
        return createTdColumn;
    }

    // test for match part
    @Test
    public void testFilterPackages_1() {
        RepositoryNode catalogNode1 = mock(RepositoryNode.class);
        when(catalogNode1.getLabel()).thenReturn("catalog type 1"); //$NON-NLS-1$
        RepositoryNode catalogNode2 = mock(RepositoryNode.class);
        when(catalogNode2.getLabel()).thenReturn("catalog type 2"); //$NON-NLS-1$

        RepositoryNode catalogNode3 = mock(RepositoryNode.class);
        when(catalogNode3.getLabel()).thenReturn("catalog sub 1"); //$NON-NLS-1$
        RepositoryNode catalogNode4 = mock(RepositoryNode.class);
        when(catalogNode4.getLabel()).thenReturn("catalog sub 2"); //$NON-NLS-1$

        List<IRepositoryNode> filterPackages = new ArrayList<IRepositoryNode>();
        filterPackages.add(catalogNode1);
        filterPackages.add(catalogNode2);
        filterPackages.add(catalogNode3);
        filterPackages.add(catalogNode4);

        List<IRepositoryNode> filterResult = RepositoryNodeHelper.filterPackages(filterPackages, "type"); //$NON-NLS-1$
        Assert.assertEquals(2, filterResult.size());
        for (IRepositoryNode node : filterResult) {
            Assert.assertTrue(node.getLabel().contains("catalog type")); //$NON-NLS-1$
        }
    }

    // test for no match
    @Test
    public void testFilterPackages_2() {
        RepositoryNode catalogNode1 = mock(RepositoryNode.class);
        when(catalogNode1.getLabel()).thenReturn("catalog type 1"); //$NON-NLS-1$
        RepositoryNode catalogNode2 = mock(RepositoryNode.class);
        when(catalogNode2.getLabel()).thenReturn("catalog type 2"); //$NON-NLS-1$

        RepositoryNode catalogNode3 = mock(RepositoryNode.class);
        when(catalogNode3.getLabel()).thenReturn("catalog sub 1"); //$NON-NLS-1$
        RepositoryNode catalogNode4 = mock(RepositoryNode.class);
        when(catalogNode4.getLabel()).thenReturn("catalog sub 2"); //$NON-NLS-1$

        List<IRepositoryNode> filterPackages = new ArrayList<IRepositoryNode>();
        filterPackages.add(catalogNode1);
        filterPackages.add(catalogNode2);
        filterPackages.add(catalogNode3);
        filterPackages.add(catalogNode4);

        List<IRepositoryNode> filterResult = RepositoryNodeHelper.filterPackages(filterPackages, "no one"); //$NON-NLS-1$
        Assert.assertEquals(0, filterResult.size());

    }

    // test for add comma in the filter string
    @Test
    public void testFilterPackages_3() {
        RepositoryNode catalogNode1 = mock(RepositoryNode.class);
        when(catalogNode1.getLabel()).thenReturn("catalog type 1"); //$NON-NLS-1$
        RepositoryNode catalogNode2 = mock(RepositoryNode.class);
        when(catalogNode2.getLabel()).thenReturn("catalog type 2"); //$NON-NLS-1$

        RepositoryNode catalogNode3 = mock(RepositoryNode.class);
        when(catalogNode3.getLabel()).thenReturn("catalog sub 1"); //$NON-NLS-1$
        RepositoryNode catalogNode4 = mock(RepositoryNode.class);
        when(catalogNode4.getLabel()).thenReturn("catalog sub 2"); //$NON-NLS-1$

        List<IRepositoryNode> filterPackages = new ArrayList<IRepositoryNode>();
        filterPackages.add(catalogNode1);
        filterPackages.add(catalogNode2);
        filterPackages.add(catalogNode3);
        filterPackages.add(catalogNode4);

        List<IRepositoryNode> filterResult = RepositoryNodeHelper.filterPackages(filterPackages, "type,sub"); //$NON-NLS-1$
        Assert.assertEquals(4, filterResult.size());
        for (IRepositoryNode node : filterResult) {
            Assert.assertTrue(node.getLabel().contains("catalog")); //$NON-NLS-1$
        }
    }
}
