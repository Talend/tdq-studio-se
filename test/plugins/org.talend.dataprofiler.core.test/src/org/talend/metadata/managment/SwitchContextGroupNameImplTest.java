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
package org.talend.metadata.managment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.DBConnectionContextUtils;
import org.talend.metadata.managment.ui.utils.SwitchContextGroupNameImpl;
import org.talend.repository.ProjectManager;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by talend on Oct 30, 2012 Detailled comment
 * 
 */
public class SwitchContextGroupNameImplTest {

    final static String dqTestProjectName = "testForContextGroupTDQ"; //$NON-NLS-1$

    DatabaseConnectionItem createDatabaseConnectionItem = null;

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    String catalogOld = "oldCatalog"; //$NON-NLS-1$

    String catalogNew = "newCatalog"; //$NON-NLS-1$

    String schemaOld = "oldSchema"; //$NON-NLS-1$

    String schemaNew = "newSchema"; //$NON-NLS-1$

    String ContextTpeyNameOld = "default"; //$NON-NLS-1$

    String ContextTpeyNameNew = "test"; //$NON-NLS-1$

    String connectionName = "conn1"; //$NON-NLS-1$

    String Connectionlabel = "";// ConnectionContextHelper.convertContextLabel(connectionName);// should be label of
                                // connection property when
                                // use

    static int index = 0;

    String prefixName = Connectionlabel + ConnectionContextHelper.LINE;

    String paramNameCatalog = prefixName + DBConnectionContextUtils.EDBParamName.Database;

    String contextCatalogName = ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX + paramNameCatalog;

    String paramNameSchema = prefixName + DBConnectionContextUtils.EDBParamName.Schema;

    String contextSchemaName = ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX + paramNameSchema;

    private static Project currentProject;

    /**
     * DOC talend Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure(dqTestProjectName); //$NON-NLS-1$
        createContextItem("contentName" + index++); //$NON-NLS-1$
        Connectionlabel = ConnectionContextHelper.convertContextLabel(connectionName);
    }

    @BeforeClass
    public static void recordCurrentProject() {
        currentProject = ProjectManager.getInstance().getCurrentProject();
    }

    @AfterClass
    public static void backToCurrentProject() throws Exception {
        if (currentProject != null) {
            ProxyRepositoryFactory.getInstance().logOffProject();
            ProxyRepositoryFactory.getInstance().logOnProject(currentProject, null);
        }
        // clean test project
        IProject testProject = ResourcesPlugin.getWorkspace().getRoot().getProject(dqTestProjectName.toUpperCase());
        if (testProject.exists()) {
            testProject.delete(true, null);
        }
    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.utils.SwitchContextGroupNameImpl#updateContextGroup(org.talend.core.model.properties.ConnectionItem)}
     * . mysql case
     */
    @Test
    public void testUpdateContextGroupSuccessMysql() {
        boolean updateContextGroup = SwitchContextGroupNameImpl.getInstance().updateContextGroup(createDatabaseConnectionItem,
                ContextTpeyNameNew);
        Connection connection = createDatabaseConnectionItem.getConnection();
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        assertTrue(updateContextGroup);
        assertTrue(catalogs.size() == 1);
        assertTrue(catalogs.get(0).getName().equalsIgnoreCase(catalogNew));
    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.utils.SwitchContextGroupNameImpl#updateContextGroup(org.talend.core.model.properties.ConnectionItem)}
     * . sqlserver case
     */
    @Test
    public void testUpdateContextGroupSuccessMssql() {
        createCatalogSchema(schemaOld);
        boolean updateContextGroup = SwitchContextGroupNameImpl.getInstance().updateContextGroup(createDatabaseConnectionItem,
                ContextTpeyNameNew);
        Connection connection = createDatabaseConnectionItem.getConnection();
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        List<Schema> schemas = CatalogHelper.getSchemas(catalogs.get(0));
        assertTrue(updateContextGroup);
        assertTrue(schemas.size() == 1);
        assertTrue(schemas.get(0).getName().equalsIgnoreCase(schemaNew));
        assertTrue(catalogs.size() == 1);
        assertTrue(catalogs.get(0).getName().equalsIgnoreCase(catalogNew));
    }

    /**
     * DOC talend Comment method "createSchema".
     * 
     * @param schemaOld2
     */
    private void createCatalogSchema(String schemaOld2) {
        Connection connection = createDatabaseConnectionItem.getConnection();
        ((DatabaseConnection) connection).setUiSchema(this.contextSchemaName);
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        Catalog catalog = catalogs.get(0);
        Schema createSchema = SchemaHelper.createSchema(schemaOld);
        List<Schema> schemas = new ArrayList<Schema>();
        schemas.add(createSchema);
        CatalogHelper.addSchemas(schemas, catalog);

    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.utils.SwitchContextGroupNameImpl#updateContextGroup(org.talend.core.model.properties.ConnectionItem)}
     * . oracle case
     */
    @Test
    public void testUpdateContextGroupSuccessOracle() {
        createSchema();
        boolean updateContextGroup = SwitchContextGroupNameImpl.getInstance().updateContextGroup(createDatabaseConnectionItem,
                ContextTpeyNameNew);
        Connection connection = createDatabaseConnectionItem.getConnection();
        List<Schema> schemas = ConnectionHelper.getSchema(connection);
        assertTrue(updateContextGroup);
        assertTrue(schemas.size() == 1);
        assertTrue(schemas.get(0).getName().equalsIgnoreCase(schemaNew));
    }

    /**
     * DOC talend Comment method "createSchema".
     */
    private void createSchema() {
        Connection connection = createDatabaseConnectionItem.getConnection();
        ((DatabaseConnection) connection).setUiSchema(this.contextSchemaName);
        ((DatabaseConnection) connection).setSID(null);
        Schema createSchema = SchemaHelper.createSchema(schemaOld);
        ConnectionHelper.addSchema(createSchema, connection);
        ConnectionHelper.removeCatalogs(ConnectionHelper.getCatalogs(connection), connection);
    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.utils.SwitchContextGroupNameImpl#updateContextGroup(org.talend.core.model.properties.ConnectionItem)}
     * . fail1: createDatabaseConnectionItem is null will nothing to do and return false
     */
    @Test
    public void testUpdateContextGroupfail1() {
        boolean updateContextGroup = SwitchContextGroupNameImpl.getInstance().updateContextGroup(null, ContextTpeyNameNew);
        Connection connection = createDatabaseConnectionItem.getConnection();
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        assertFalse(updateContextGroup);
        assertTrue(catalogs.size() == 1);
        assertTrue(catalogs.get(0).getName().equalsIgnoreCase(catalogOld));
    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.utils.SwitchContextGroupNameImpl#updateContextGroup(org.talend.core.model.properties.ConnectionItem)}
     * . fail2: connection is null return false
     */
    @Test
    public void testUpdateContextGroupfail2() {
        createDatabaseConnectionItem.setConnection(null);
        boolean updateContextGroup = SwitchContextGroupNameImpl.getInstance().updateContextGroup(createDatabaseConnectionItem,
                ContextTpeyNameNew);
        assertFalse(updateContextGroup);

    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.utils.SwitchContextGroupNameImpl#updateContextGroup(org.talend.core.model.properties.ConnectionItem)}
     * . fail3: the Connection type is ODBC case
     */
    @Test
    public void testUpdateContextGroupSuccessODBC() {
        ((DatabaseConnection) createDatabaseConnectionItem.getConnection()).setDatabaseType(EDatabaseTypeName.GODBC.getXmlName());
        boolean updateContextGroup = SwitchContextGroupNameImpl.getInstance().updateContextGroup(createDatabaseConnectionItem,
                ContextTpeyNameNew);
        Connection connection = createDatabaseConnectionItem.getConnection();
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        assertTrue(updateContextGroup);
        assertTrue(catalogs.size() == 1);
        assertTrue(catalogs.get(0).getName().equalsIgnoreCase(catalogNew));

    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.utils.SwitchContextGroupNameImpl#updateContextGroup(org.talend.core.model.properties.ConnectionItem)}
     * . fail4: the Connection type is GENERAL_JDBC case
     */
    @Test
    public void testUpdateContextGroupSuccessGeneralJdbc() {
        ((DatabaseConnection) createDatabaseConnectionItem.getConnection()).setDatabaseType(EDatabaseTypeName.GENERAL_JDBC
                .getXmlName());
        boolean updateContextGroup = SwitchContextGroupNameImpl.getInstance().updateContextGroup(createDatabaseConnectionItem,
                ContextTpeyNameNew);
        Connection connection = createDatabaseConnectionItem.getConnection();
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(connection);
        assertTrue(updateContextGroup);
        assertTrue(catalogs.size() == 1);
        assertTrue(catalogs.get(0).getName().equalsIgnoreCase(catalogNew));

    }

    private void createConnectionItem(String name, IFolder folder, Boolean isDelete) {
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
        createConnection.setContextMode(true);
        createConnection.setContextName(ContextTpeyNameOld);
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
            Assert.fail(e.getMessage());
        }
    }

    public void createContextItem(String name) throws PersistenceException {

        // ContextType--default
        ContextType createContextDefault = TalendFileFactory.eINSTANCE.createContextType();
        createContextDefault.setName(ContextTpeyNameOld);
        ContextParameterType createContextParameterTypeForDefault = TalendFileFactory.eINSTANCE.createContextParameterType();
        createContextParameterTypeForDefault.setName(paramNameCatalog);
        createContextParameterTypeForDefault.setValue(catalogOld);
        createContextDefault.getContextParameter().add(createContextParameterTypeForDefault);
        createContextParameterTypeForDefault = TalendFileFactory.eINSTANCE.createContextParameterType();
        createContextParameterTypeForDefault.setName(paramNameSchema);
        createContextParameterTypeForDefault.setValue(schemaOld);
        createContextDefault.getContextParameter().add(createContextParameterTypeForDefault);
        // ~ContextType--default
        // ContextType--test
        ContextType createContextTest = TalendFileFactory.eINSTANCE.createContextType();
        createContextTest.setName(ContextTpeyNameNew);
        createContextParameterTypeForDefault = TalendFileFactory.eINSTANCE.createContextParameterType();
        createContextParameterTypeForDefault.setName(paramNameCatalog);
        createContextParameterTypeForDefault.setValue(catalogNew);
        createContextTest.getContextParameter().add(createContextParameterTypeForDefault);
        createContextParameterTypeForDefault = TalendFileFactory.eINSTANCE.createContextParameterType();
        createContextParameterTypeForDefault.setName(paramNameSchema);
        createContextParameterTypeForDefault.setValue(schemaNew);
        createContextTest.getContextParameter().add(createContextParameterTypeForDefault);
        // ~ContextType--test
        // ConntextItem
        ContextItem contextItem = PropertiesFactory.eINSTANCE.createContextItem();
        Property contextProperty = PropertiesFactory.eINSTANCE.createProperty();
        contextProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        contextProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        contextProperty.setStatusCode(""); //$NON-NLS-1$
        contextProperty.setLabel(name);
        contextItem.setProperty(contextProperty);
        JobContextManager contextManager = new JobContextManager();
        String nextId = factory.getNextId();
        contextProperty.setId(nextId);
        contextProperty.setLabel(contextProperty.getDisplayName());
        contextManager.saveToEmf(contextItem.getContext());
        contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
        contextItem.getContext().clear();
        contextItem.getContext().add(createContextDefault);
        contextItem.getContext().add(createContextTest);
        factory.create(contextItem, Path.EMPTY);
        // ContextItem

        // connection
        createConnectionItem(connectionName, null, false);
        Connection connection = createDatabaseConnectionItem.getConnection();
        connection.setContextId(contextProperty.getId());
        createCatalog(catalogOld);
        // ~connection data
    }

    /**
     * DOC talend Comment method "createCatalog".
     * 
     * @param catalogName
     */
    private void createCatalog(String catalogName) {
        Connection connection = createDatabaseConnectionItem.getConnection();
        Catalog createCatalog = CatalogHelper.createCatalog(catalogOld);
        ConnectionHelper.addCatalog(createCatalog, connection);
        ((DatabaseConnection) connection).setSID(contextCatalogName);
    }

}
