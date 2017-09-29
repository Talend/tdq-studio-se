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

import java.sql.Types;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.actions.ActionContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.dataprofiler.core.ui.action.actions.predefined.SemanticDiscoveryAction;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;


/**
 * DOC zshen  class global comment. Detailled comment
 */
public class SemanticDiscoveryActionProviderTest {

    /**
     * DOC zshen Comment method "setUpBeforeClass".
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * DOC zshen Comment method "tearDownAfterClass".
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * DOC zshen Comment method "setUp".
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC zshen Comment method "tearDown".
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.action.provider.SemanticDiscoveryActionProvider#fillContextMenu(org.eclipse.jface.action.IMenuManager)}.
     */
    @Test
    public void testFillContextMenuIMenuManager() {
        SemanticDiscoveryActionProvider semanticDiscoveryActionProvider = new SemanticDiscoveryActionProvider();
        IRepositoryNode createNewColumnRepNode = createNewColumnRepNode();
        TreePath treePath = new TreePath(new IRepositoryNode[]{createNewColumnRepNode});
        TreeSelection treeSelection = new TreeSelection(new TreePath[] { treePath });
        semanticDiscoveryActionProvider.setContext(new ActionContext(treeSelection));
        MenuManager menuManager = new MenuManager();
        semanticDiscoveryActionProvider.fillContextMenu(menuManager);
        Assert.assertEquals(1, menuManager.getSize());
        Assert.assertTrue("Current action must be SemanticDiscoveryAction", //$NON-NLS-1$
                ((ActionContributionItem) menuManager.getItems()[0]).getAction() instanceof SemanticDiscoveryAction);
    }

    /**
     * DOC zshen Comment method "createNewColumnRepNode".
     */
    private IRepositoryNode createNewColumnRepNode() {
        // TODO　create real connection with eresource
        TdColumn tdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdTable createTdTable = RelationalFactory.eINSTANCE.createTdTable();
        tdColumn.setOwner(createTdTable);
        TdSqlDataType dataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        dataType.setJavaDataType(Types.VARCHAR);
        tdColumn.setSqlDataType(dataType);
        MetadataColumnRepositoryObject columnObject = new MetadataColumnRepositoryObject(null, tdColumn);
        IRepositoryNode columnRepNode = new DBColumnRepNode(columnObject, new RepositoryNode(null, null, null),
                ENodeType.REPOSITORY_ELEMENT, null);
        return columnRepNode;
    }

}
