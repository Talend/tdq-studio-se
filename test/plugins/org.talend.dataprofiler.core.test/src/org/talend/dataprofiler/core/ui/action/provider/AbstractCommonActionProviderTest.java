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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import junit.framework.Assert;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.actions.ActionContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataprofiler.core.helper.UnitTestBuildHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.indicators.CountsIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.repository.localprovider.model.LocalRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * created by qiongli on 2012-9-18 Detailled comment
 *
 */
public class AbstractCommonActionProviderTest {

    private AbstractCommonActionProvider absCommonActionProvider = null;

    TreeSelection treeSel = null;

    private static LocalRepositoryFactory repositoryFactory;

    ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private static Project originalProject;

    /**
     * DOC qiongli Comment method "setUp".
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        absCommonActionProvider = new AbstractCommonActionProvider();
        ActionContext context = mock(ActionContext.class);
        treeSel = mock(TreeSelection.class);
        when(context.getSelection()).thenReturn(treeSel);
        absCommonActionProvider.setContext(context);
        UnitTestBuildHelper.initProjectStructure("testForDeleteActionTDQ"); //$NON-NLS-1$
    }

    @BeforeClass
    public static void beforeAllTests() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (repositoryContext != null) {
            originalProject = repositoryContext.getProject();
        }
        repositoryFactory = new LocalRepositoryFactory();
    }

    @AfterClass
    public static void afterAllTests() {
        repositoryFactory = null;
        if (originalProject != null) {
            Context ctx = CoreRuntimePlugin.getInstance().getContext();
            RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
            repositoryContext.setProject(originalProject);
        }
    }

    /**
     * Test method for different type nodes(analysis node and report node) .
     */
    @Test
    public void testisSelectionSameType_1() {

        RepositoryNode anaNode = createAnalysisNode("ana", null, false); //$NON-NLS-1$
        setParentNode(anaNode, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        RepositoryNode connectionNode = createConnectionNode("conn", null, false); //$NON-NLS-1$
        setParentNode(connectionNode, ERepositoryObjectType.METADATA_CONNECTIONS);
        Object objs[] = new Object[2];
        objs[0] = anaNode;
        objs[1] = connectionNode;
        when(treeSel.toArray()).thenReturn(objs);
        boolean flag = absCommonActionProvider.isSelectionSameType();
        assertFalse(flag);
    }

    /**
     * Test method for the same type nodes(analysis nodes) .
     */
    @Test
    public void testisSelectionSameType_2() {

        RepositoryNode anaNode = createAnalysisNode("ana", null, false); //$NON-NLS-1$
        RepositoryNode anaNode2 = createAnalysisNode("ana2", null, false); //$NON-NLS-1$
        setParentNode(anaNode, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        setParentNode(anaNode2, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        Object objs[] = new Object[2];
        objs[0] = anaNode;
        objs[1] = anaNode2;
        when(treeSel.toArray()).thenReturn(objs);
        boolean flag = absCommonActionProvider.isSelectionSameType();
        assertTrue(flag);
    }

    /**
     * Test method for a node is in recycle bin,another one is not in recycle bin .
     */
    @Test
    public void testisSelectionSameType_3() {

        RepositoryNode anaNode = createAnalysisNode("ana", null, false); //$NON-NLS-1$
        RepositoryNode anaNode_deleted = createAnalysisNode("ana2", null, true); //$NON-NLS-1$
        setParentNode(anaNode, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        setParentNode(anaNode, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        Object objs[] = new Object[2];
        objs[0] = anaNode;
        objs[1] = anaNode_deleted;
        when(treeSel.toArray()).thenReturn(objs);
        boolean flag = absCommonActionProvider.isSelectionSameType();
        assertFalse(flag);
    }

    private RepositoryNode createConnectionNode(String name, IFolder folder, boolean isDelete) {
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
        // ~connection
        DatabaseConnectionItem createDatabaseConnectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();

        org.talend.core.model.properties.Property createDatabaseConnectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        createDatabaseConnectionProperty.setId(EcoreUtil.generateUUID());
        createDatabaseConnectionProperty.setItem(createDatabaseConnectionItem);
        createDatabaseConnectionProperty.setLabel(createConnection.getName());
        createDatabaseConnectionItem.setProperty(createDatabaseConnectionProperty);
        createDatabaseConnectionItem.setConnection(createConnection);
        ItemState createItemState = PropertiesFactory.eINSTANCE.createItemState();
        createItemState.setDeleted(isDelete);
        createDatabaseConnectionItem.setState(createItemState);
        IRepositoryViewObject repViewObject = null;
        try {
            factory.create(createDatabaseConnectionItem, createPath, false);
            repViewObject = factory.getLastVersion(createDatabaseConnectionProperty.getId());
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }

        RepositoryNode dbConnRepNode = new DBConnectionRepNode(repViewObject, null, ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        return dbConnRepNode;
    }

    private RepositoryNode createAnalysisNode(String name, IFolder folder, boolean isDelete) {
        IPath createPath = Path.EMPTY;
        if (folder != null) {
            createPath = new Path(folder.getFullPath().lastSegment());
        }
        // connection
        Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        createAnalysis.setName(name);

        // ~connection
        TDQAnalysisItem createAnalysisItem = org.talend.dataquality.properties.PropertiesFactory.eINSTANCE
                .createTDQAnalysisItem();

        org.talend.core.model.properties.Property createAnaProperty = PropertiesFactory.eINSTANCE.createProperty();
        createAnaProperty.setId(EcoreUtil.generateUUID());
        createAnaProperty.setItem(createAnalysisItem);
        createAnaProperty.setLabel(createAnalysis.getName());
        createAnalysisItem.setProperty(createAnaProperty);
        createAnalysisItem.setAnalysis(createAnalysis);
        ItemState createItemState = PropertiesFactory.eINSTANCE.createItemState();
        createItemState.setDeleted(isDelete);
        createAnalysisItem.setState(createItemState);

        // Indicator
        CountsIndicator createCountsIndicator = IndicatorsFactory.eINSTANCE.createCountsIndicator();
        List<IRepositoryViewObject> all = null;
        IndicatorDefinition createIndicatorDefinition = null;
        try {
            all = factory.getAll(ERepositoryObjectType.TDQ_SYSTEM_INDICATORS);
            for (IRepositoryViewObject indicatorViewObject : all) {
                if (indicatorViewObject.getLabel().equalsIgnoreCase("ROW COUNT")) { //$NON-NLS-1$
                    createIndicatorDefinition = (IndicatorDefinition) PropertyHelper.getModelElement(indicatorViewObject
                            .getProperty());
                    break;
                }
            }
        } catch (PersistenceException e1) {
            e1.printStackTrace();
            Assert.fail(e1.getMessage());
        }
        createCountsIndicator.setIndicatorDefinition(createIndicatorDefinition);

        AnalysisParameters createAnalysisParameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        AnalysisContext createAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        createAnalysis.setContext(createAnalysisContext);
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        createAnalysisResult.getIndicators().add(createCountsIndicator);
        createAnalysis.setResults(createAnalysisResult);
        createAnalysis.setParameters(createAnalysisParameters);

        IRepositoryViewObject repViewObject = null;
        try {
            factory.create(createAnalysisItem, createPath, false);
            repViewObject = factory.getLastVersion(createAnaProperty.getId());
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
        RepositoryNode anaRepNode = new AnalysisRepNode(repViewObject, null, ENodeType.TDQ_REPOSITORY_ELEMENT, null);
        // set the contentType for anaRepNode from its parentNode
        setParentNode(anaRepNode, ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        // ~
        return anaRepNode;
    }

    private void setParentNode(RepositoryNode repNode, ERepositoryObjectType objType) {
        RepositoryNode parentNode = mock(RepositoryNode.class);
        when(parentNode.getContentType()).thenReturn(objType);
        repNode.setParent(parentNode);
    }

}
