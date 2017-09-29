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
package org.talend.core.service;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.DBConnectionContextUtils;
import org.talend.repository.ProjectManager;

/**
 * created by talend on Oct 9, 2012 Detailled comment
 * 
 */

public class TalendCWMServiceTest {

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private static Project currentProject;

    final static String dqTestProjectName = "testForSoftWareTDQ"; //$NON-NLS-1$

    /**
     * DOC talend Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure(dqTestProjectName);
    }

    /**
     * DOC zshen Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        //
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

    @Test
    public void testGetNameConnectionIsNull() throws PersistenceException {

        TalendCWMService talendCWMService = new TalendCWMService();
        // Assert DataManager is null.
        String passedNamed = "name1"; //$NON-NLS-1$
        String actualName = talendCWMService.getReadableName(null, passedNamed);
        Assert.assertEquals(passedNamed, actualName);

    }

    @Test
    public void testGetNameContextFalse() throws PersistenceException {

        TalendCWMService talendCWMService = new TalendCWMService();
        // DatabaseConnection
        DatabaseConnection createDatabaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setContextMode(false);
        // ~DatabaseConnection
        // Assert DataManager is null.
        String passedNamed = "name1"; //$NON-NLS-1$
        String actualName = talendCWMService.getReadableName(createDatabaseConnection, passedNamed);
        Assert.assertEquals(passedNamed, actualName);

    }

    @Test
    public void testGetNameNullContextualName() throws PersistenceException {

        TalendCWMService talendCWMService = new TalendCWMService();
        // DatabaseConnection
        DatabaseConnection createDatabaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setContextMode(true);
        // ~DatabaseConnection
        // Assert DataManager is null.
        String actualName = talendCWMService.getReadableName(createDatabaseConnection, null);
        Assert.assertNull(actualName);

    }

    /**
     * Test method for
     * {@link org.talend.core.service.TalendCWMService#getReadableName(orgomg.cwm.foundation.softwaredeployment.DataManager, java.lang.String)}
     * .
     * 
     * @throws PersistenceException
     */
    @Test
    public void testGetNameContexTrueForCatalog() throws PersistenceException {

        String connectionName = "conn1"; //$NON-NLS-1$
        String catalogName = "catalog1"; //$NON-NLS-1$

        String ContextTpeyName = "default"; //$NON-NLS-1$
        String Connectionlabel = ConnectionContextHelper.convertContextLabel(connectionName);// should be label of
                                                                                             // connection property when
                                                                                             // use
        String prefixName = Connectionlabel + ConnectionContextHelper.LINE;
        String paramName = prefixName + DBConnectionContextUtils.EDBParamName.Database;
        String contextCatalogName = ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX + paramName;

        // ContextType--default
        ContextType createContextDefault = TalendFileFactory.eINSTANCE.createContextType();
        createContextDefault.setName(ContextTpeyName);
        ContextParameterType createContextParameterTypeForDefault = TalendFileFactory.eINSTANCE.createContextParameterType();
        createContextParameterTypeForDefault.setName(paramName);
        createContextParameterTypeForDefault.setValue(catalogName);
        createContextDefault.getContextParameter().add(createContextParameterTypeForDefault);
        // ~ContextType--default

        // ConntextItem
        ContextItem contextItem = PropertiesFactory.eINSTANCE.createContextItem();
        Property contextProperty = PropertiesFactory.eINSTANCE.createProperty();
        contextProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        contextProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        contextProperty.setStatusCode(""); //$NON-NLS-1$
        contextProperty.setLabel("context1"); //$NON-NLS-1$
        contextItem.setProperty(contextProperty);
        JobContextManager contextManager = new JobContextManager();
        String nextId = factory.getNextId();
        contextProperty.setId(nextId);
        contextProperty.setLabel(contextProperty.getDisplayName());
        contextManager.saveToEmf(contextItem.getContext());
        contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
        contextItem.getContext().add(createContextDefault);
        factory.create(contextItem, Path.EMPTY);
        // ContextItem

        // DatabaseConnection
        DatabaseConnection createDatabaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setName(connectionName);
        createDatabaseConnection.setContextMode(true);
        createDatabaseConnection.setContextId(contextProperty.getId());
        createDatabaseConnection.setContextName(ContextTpeyName);
        // ~DatabaseConnection

        String readableName = null;
        readableName = TalendCWMService.getReadableName(createDatabaseConnection, contextCatalogName);
        assert (catalogName.equals(readableName));
    }

    /**
     * Test method for
     * {@link org.talend.core.service.TalendCWMService#getReadableName(orgomg.cwm.foundation.softwaredeployment.DataManager, java.lang.String)}
     * .
     * 
     * @throws PersistenceException
     */
    @Test
    public void testGetNameContexTrueForSchema() throws PersistenceException {

        String connectionName = "conn1"; //$NON-NLS-1$
        String catalogName = "catalog1"; //$NON-NLS-1$

        String ContextTpeyName = "default"; //$NON-NLS-1$
        String Connectionlabel = ConnectionContextHelper.convertContextLabel(connectionName);// should be label of
        // connection property when
        // use
        String prefixName = Connectionlabel + ConnectionContextHelper.LINE;
        String paramName = prefixName + DBConnectionContextUtils.EDBParamName.Schema;
        String contextCatalogName = ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX + paramName;

        // ContextType--default
        ContextType createContextDefault = TalendFileFactory.eINSTANCE.createContextType();
        createContextDefault.setName(ContextTpeyName);
        ContextParameterType createContextParameterTypeForDefault = TalendFileFactory.eINSTANCE.createContextParameterType();
        createContextParameterTypeForDefault.setName(paramName);
        createContextParameterTypeForDefault.setValue(catalogName);
        createContextDefault.getContextParameter().add(createContextParameterTypeForDefault);
        // ~ContextType--default

        // ConntextItem
        ContextItem contextItem = PropertiesFactory.eINSTANCE.createContextItem();
        Property contextProperty = PropertiesFactory.eINSTANCE.createProperty();
        contextProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        contextProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        contextProperty.setStatusCode(""); //$NON-NLS-1$
        contextProperty.setLabel("context2"); //$NON-NLS-1$
        contextItem.setProperty(contextProperty);
        JobContextManager contextManager = new JobContextManager();
        String nextId = factory.getNextId();
        contextProperty.setId(nextId);
        contextProperty.setLabel(contextProperty.getDisplayName());
        contextManager.saveToEmf(contextItem.getContext());
        contextItem.setDefaultContext(contextManager.getDefaultContext().getName());
        contextItem.getContext().add(createContextDefault);
        factory.create(contextItem, Path.EMPTY);
        // ContextItem

        // DatabaseConnection
        DatabaseConnection createDatabaseConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setName(connectionName);
        createDatabaseConnection.setContextMode(true);
        createDatabaseConnection.setContextId(contextProperty.getId());
        createDatabaseConnection.setContextName(ContextTpeyName);
        // ~DatabaseConnection

        String readableName = null;
        readableName = TalendCWMService.getReadableName(createDatabaseConnection, contextCatalogName);
        assert (catalogName.equals(readableName));
    }

}
