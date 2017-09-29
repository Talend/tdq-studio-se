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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.resource.relational.Catalog;

/**
 * created by zshen on Apr 18, 2013 Detailled comment
 * 
 */
public class DBConnectionDuplicateHandleTest {

    /**
     * DOC zshen Comment method "setUpBeforeClass".
     * 
     * @throws java.lang.Exception
     */

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private static final String oldConnectionName = "EMFResourceHandleTest_conn1"; //$NON-NLS-1$

    private static final String newConnectionName = "copy_of_EMFResourceHandleTest_conn1"; //$NON-NLS-1$

    /**
     * DOC zshen Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        //        UnitTestBuildHelper.initProjectStructure("testForEMFSharedResourcesDuplicate"); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.actions.handle.ModelElementDuplicateHandle#duplicate(java.lang.String)}
     * .
     * 
     * @throws BusinessException
     * @throws PersistenceException
     * @throws InterruptedException
     */
    @Test
    public void testDuplicate() throws BusinessException, PersistenceException, InterruptedException {
        // connectionNode
        DatabaseConnectionItem oldConnectionItem = createConnectionItem(oldConnectionName, null, false);
        addDatapackage("catalog1", oldConnectionItem); //$NON-NLS-1$
        addDatapackage("catalog2", oldConnectionItem); //$NON-NLS-1$
        RepositoryNode dbParentRepNode = createConnectionNode(oldConnectionItem);

        // ~connectionNode

        IDuplicateHandle createDuplicateHandle = ActionHandleFactory.getInstance().createDuplicateHandle(dbParentRepNode);

        Item duplicate = createDuplicateHandle.duplicateItem(oldConnectionItem, newConnectionName);
        Property newProperty = duplicate.getProperty();
        IRepositoryViewObject newViewObject = factory.getLastVersion(newProperty.getId());
        DatabaseConnectionItem newConnectionItem = (DatabaseConnectionItem) newViewObject.getProperty().getItem();
        compareResult(newConnectionItem, oldConnectionItem);

    }

    /**
     * DOC zshen Comment method "compareResult".
     * 
     * @param newConnectionItem
     * @param oldConnectionItem
     */
    private void compareResult(DatabaseConnectionItem newConnectionItem, DatabaseConnectionItem oldConnectionItem) {
        Connection newConnection = newConnectionItem.getConnection();
        Connection oldConnection = oldConnectionItem.getConnection();

        // check connection has been persistenced
        Assert.assertTrue(newConnection != null);
        Assert.assertTrue(newConnection.eResource() != null);
        Assert.assertTrue(!newConnection.eIsProxy());

        Assert.assertTrue(oldConnection != null);
        Assert.assertTrue(oldConnection.eResource() != null);
        Assert.assertTrue(!oldConnection.eIsProxy());

        // check name and label has been set
        Assert.assertEquals(oldConnection.getName(), oldConnectionName);
        Assert.assertEquals(oldConnection.getLabel(), oldConnectionName);
        Assert.assertEquals(newConnection.getName(), newConnectionName);
        Assert.assertEquals(newConnection.getLabel(), newConnectionName);

        // check catalog size is same
        List<Catalog> newCatalogs = ConnectionHelper.getCatalogs(newConnection);
        List<Catalog> oldCatalogs = ConnectionHelper.getCatalogs(oldConnection);
        Assert.assertTrue(newCatalogs.size() == 2);
        Assert.assertTrue(oldCatalogs.size() == 2);

        // check the structor of connection is valid
        Catalog oldCatalog1 = CatalogHelper.getCatalog(oldConnection, "catalog1"); //$NON-NLS-1$
        Catalog newCatalog1 = CatalogHelper.getCatalog(newConnection, "catalog1"); //$NON-NLS-1$
        Catalog oldCatalog2 = CatalogHelper.getCatalog(oldConnection, "catalog2"); //$NON-NLS-1$
        Catalog newCatalog2 = CatalogHelper.getCatalog(newConnection, "catalog2"); //$NON-NLS-1$

        Assert.assertTrue(oldCatalog1 != null);
        Assert.assertTrue(newCatalog1 != null);
        Assert.assertTrue(oldCatalog2 != null);
        Assert.assertTrue(newCatalog2 != null);

    }

    /**
     * DOC zshen Comment method "createConnectionNode".
     * 
     * @return
     */
    private RepositoryNode createConnectionNode(DatabaseConnectionItem createConnectionItem) {
        IRepositoryViewObject connectionViewObject = null;
        try {
            connectionViewObject = factory.getLastVersion(createConnectionItem.getProperty().getId());
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }

        RepositoryNode dbParentRepNode = new DBConnectionRepNode(connectionViewObject, null, ENodeType.REPOSITORY_ELEMENT, null);
        return dbParentRepNode;
    }

    private DatabaseConnectionItem createConnectionItem(String name, IFolder folder, Boolean isDelete) {
        IPath createPath = Path.EMPTY;
        if (folder != null) {
            createPath = new Path(folder.getFullPath().lastSegment());
        }
        // connection
        DatabaseConnection createConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createConnection.setName(name);
        createConnection.setLabel(name);
        createConnection.setUsername("UserName"); //$NON-NLS-1$
        createConnection.setRawPassword("Password"); //$NON-NLS-1$
        createConnection.setURL("URL"); //$NON-NLS-1$
        createConnection.setDatabaseType(EDatabaseTypeName.MYSQL.getXmlName());
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
            factory.create(createDatabaseConnectionItem, createPath, false);
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
        return createDatabaseConnectionItem;
    }

    private Catalog addDatapackage(String packageName, DatabaseConnectionItem connItem) throws PersistenceException {
        Connection connection = connItem.getConnection();
        Catalog createCatalog = CatalogHelper.createCatalog(packageName);
        ConnectionHelper.addCatalog(createCatalog, connection);
        factory.save(connItem, null);
        return createCatalog;
    }
}
