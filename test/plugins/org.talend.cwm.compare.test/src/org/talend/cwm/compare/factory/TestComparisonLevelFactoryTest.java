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
package org.talend.cwm.compare.factory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.cwm.compare.UnitTestBuildHelper;
import org.talend.cwm.compare.factory.comparisonlevel.CatalogSchemaComparisonLevel;
import org.talend.cwm.compare.factory.comparisonlevel.DataProviderComparisonLevel;
import org.talend.cwm.compare.factory.comparisonlevel.RepositoryObjectComparisonLevel;
import org.talend.cwm.compare.factory.comparisonlevel.TableViewComparisonLevel;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.nodes.AnalysisFolderRepNode;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.factory.DQRepNodeCreateFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.resource.relational.Catalog;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class TestComparisonLevelFactoryTest {

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    DatabaseConnectionItem databaseConnectionItem = null;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        UnitTestBuildHelper.initProjectStructure();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test method for
     * {@link org.talend.cwm.compare.factory.ComparisonLevelFactory#creatComparisonLevel(java.lang.Object)}. test for
     * the type of : db connectin rep node
     */
    @Test
    public void testCreatComparisonLevelObject_1() {
        Property property = PropertyHelper.createTDQItemProperty();
        IRepositoryViewObject viewObj = new RepositoryViewObject(property);
        DBConnectionRepNode node = new DBConnectionRepNode(viewObj, null, null, null);

        IComparisonLevel level = ComparisonLevelFactory.creatComparisonLevel(node);
        Assert.assertTrue(level instanceof RepositoryObjectComparisonLevel);
    }

    /**
     * Test method for
     * {@link org.talend.cwm.compare.factory.ComparisonLevelFactory#creatComparisonLevel(java.lang.Object)}. test for
     * the type of : table folder rep node
     */
    @Test
    public void testCreatComparisonLevelObject_2() {
        RepositoryNode dbCatalogRepNode = createCatalogRepNode();
        DBTableFolderRepNode dbTableFolderRepNode = new DBTableFolderRepNode(null, dbCatalogRepNode, null, null);
        IComparisonLevel level = ComparisonLevelFactory.creatComparisonLevel(dbTableFolderRepNode);
        Assert.assertTrue(level instanceof CatalogSchemaComparisonLevel);
    }

    /**
     * Test method for
     * {@link org.talend.cwm.compare.factory.ComparisonLevelFactory#creatComparisonLevel(java.lang.Object)}. test for
     * the type of : db view rep node
     */
    @Test
    public void testCreatComparisonLevelObject_3() {
        //
        RepositoryNode dbCatalogRepNode = createCatalogRepNode();
        DBViewFolderRepNode node = new DBViewFolderRepNode(null, dbCatalogRepNode, null, null);
        IComparisonLevel level = ComparisonLevelFactory.creatComparisonLevel(node);
        Assert.assertTrue(level instanceof CatalogSchemaComparisonLevel);
    }

    /**
     * Test method for
     * {@link org.talend.cwm.compare.factory.ComparisonLevelFactory#creatComparisonLevel(java.lang.Object)}. test for
     * the type of : rep node
     */
    @Test
    public void testCreatComparisonLevelObject_5() {
        RepositoryNode dbCatalogRepNode = createCatalogRepNode();
        DBTableRepNode dbTableRepNode = new DBTableRepNode(null, dbCatalogRepNode, ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        DBColumnFolderRepNode node = new DBColumnFolderRepNode(null, dbTableRepNode, null, null);
        IComparisonLevel level = ComparisonLevelFactory.creatComparisonLevel(node);
        Assert.assertTrue(level instanceof TableViewComparisonLevel);
    }

    /**
     * Test method for
     * {@link org.talend.cwm.compare.factory.ComparisonLevelFactory#creatComparisonLevel(java.lang.Object)}. test for
     * the type of : rep node
     */
    @Test
    public void testCreatComparisonLevelObject_6() {
        Connection connection = ConnectionFactory.eINSTANCE.createConnection();
        IComparisonLevel level = ComparisonLevelFactory.creatComparisonLevel(connection);
        Assert.assertTrue(level instanceof DataProviderComparisonLevel);
    }

    /**
     * Test method for
     * {@link org.talend.cwm.compare.factory.ComparisonLevelFactory#creatComparisonLevel(java.lang.Object)}. test for
     * the type of : null
     */
    @Test
    public void testCreatComparisonLevelObject_7() {
        IComparisonLevel level = ComparisonLevelFactory.creatComparisonLevel(null);
        Assert.assertNull(level);
    }

    // other type
    @Test
    public void testCreatComparisonLevelObject_8() {
        Project tProject = ProjectManager.getInstance().getCurrentProject();
        if (tProject != null && tProject.getEmfProject() != null && tProject.getAuthor() != null) {

            IRepositoryViewObject viewObject = UnitTestBuildHelper.buildRepositoryViewObjectSystemFolder(
                    tProject.getEmfProject(), tProject.getAuthor(), ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);

            RepositoryNode node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
            viewObject.setRepositoryNode(node);
            AnalysisFolderRepNode AnalysisFolderRepNode = new AnalysisFolderRepNode(viewObject, null, ENodeType.SYSTEM_FOLDER,
                    tProject);
            IComparisonLevel level = ComparisonLevelFactory.creatComparisonLevel(AnalysisFolderRepNode);
            Assert.assertNull(level);
        }
    }

    private RepositoryNode createCatalogRepNode() {
        IRepositoryViewObject lastVersion = null;
        databaseConnectionItem = UnitTestBuildHelper.createDatabaseConnectionItem("testCompareLevelConnection", null, false); //$NON-NLS-1$
        Assert.assertNotNull(databaseConnectionItem);
        Assert.assertNotNull(databaseConnectionItem.getProperty());
        String propertyID = databaseConnectionItem.getProperty().getId();
        Catalog createCatalog = createCatalog("catalog1"); //$NON-NLS-1$
        try {
            lastVersion = factory.getLastVersion(propertyID);
            lastVersion = new MetadataCatalogRepositoryObject(lastVersion, createCatalog);
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
        // ~connection data
        // ~FileConnection
        // create DFColumnFolderRepNode
        Assert.assertFalse(lastVersion == null);
        RepositoryNode dbCatalogRepNode = DQRepNodeCreateFactory.createDBCatalogRepNode(lastVersion, null,
                ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        return dbCatalogRepNode;
    }

    private Catalog createCatalog(String catalogName) {
        Connection connection = databaseConnectionItem.getConnection();
        Catalog createCatalog = CatalogHelper.createCatalog(catalogName);
        ConnectionHelper.addCatalog(createCatalog, connection);
        return createCatalog;
    }

}
