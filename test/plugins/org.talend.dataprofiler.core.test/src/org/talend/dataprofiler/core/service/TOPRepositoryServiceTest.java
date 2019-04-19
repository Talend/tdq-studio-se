// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.*;
import static org.powermock.api.support.membermodification.MemberModifier.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
@PrepareForTest({ WorkspaceResourceHelper.class })
public class TOPRepositoryServiceTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private ITDQRepositoryService tdqRepService;

    /*
     * (non-Jsdoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        tdqRepService = new TOPRepositoryService();// (ITDQRepositoryService)
                                                   // GlobalServiceRegister.getDefault().getService(ITDQRepositoryService.class);
    }

    /**
     * DOC qiongli Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    // @After
    // public void tearDown() throws Exception {
    // }

    /**
     * Test method for {@link org.talend.dataprofiler.core.service.TOPRepositoryService#updateImpactOnAnalysis(org.talend.core.model.properties.ConnectionItem)}.
     */
    @Test
    public void testUpdateImpactOnAnalysis() {
        // fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.talend.dataprofiler.core.service.TOPRepositoryService#confirmUpdateAnalysis(org.talend.core.model.properties.ConnectionItem)}.
     */
    @Test
    public void testConfirmUpdateAnalysis() {
        // fail("Not yet implemented");
    }

    /**
     * Test method for {@link org.talend.dataprofiler.core.service.TOPRepositoryService#hasClientDependences(org.talend.core.model.properties.ConnectionItem)}.
     */
    @Test
    public void testHasClientDependences() {
        // ConnectionItem connectionItem = mock(ConnectionItem.class);
        // Connection connection = mock(Connection.class);
        // when(connectionItem.getConnection()).thenReturn(connection);
        // List<ModelElement> clientDependences = new ArrayList<ModelElement>();
        // ModelElement mod = mock(ModelElement.class);
        // clientDependences.add(mod);
        // PowerMockito.mockStatic(EObjectHelper.class);
        // EObjectHelper eobj = mock(EObjectHelper.class);
        // when(EObjectHelper.getDependencyClients(connection)).thenReturn(clientDependences);
        // TOPRepositoryService service = new TOPRepositoryService();
        // assertTrue(service.hasClientDependences(connectionItem));

    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.service.TOPRepositoryService#sourceFileOpening(org.talend.repository.model.RepositoryNode)}
     * .
     */
    @Test
    public void testSourceFileOpening() {
        // test for SourceFileRepNode
        SourceFileRepNode fileNodeMock = mock(SourceFileRepNode.class);
        String nodeLabel = "nodeLabel"; //$NON-NLS-1$
        when(fileNodeMock.getLabel()).thenReturn(nodeLabel);

        stub(method(WorkspaceResourceHelper.class, "sourceFileHasBeenOpened", SourceFileRepNode.class)).toReturn(Boolean.TRUE); //$NON-NLS-1$

        TOPRepositoryService service = new TOPRepositoryService();

        assertTrue(service.sourceFileOpening(fileNodeMock));
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.service.TOPRepositoryService#sourceFileOpening(org.talend.repository.model.RepositoryNode)}
     * .
     */
    @Test
    public void testSourceFileOpening2() {
        // test for SourceFileSubFolderNode
        SourceFileSubFolderNode folderNodeMock = mock(SourceFileSubFolderNode.class);

        SourceFileRepNode nodeMock = mock(SourceFileRepNode.class);
        List<IRepositoryNode> nodeList = new ArrayList<IRepositoryNode>();
        nodeList.add(nodeMock);

        when(folderNodeMock.getChildren()).thenReturn(nodeList);

        boolean ok = Boolean.TRUE;
        String msg = "msg"; //$NON-NLS-1$
        ReturnCode rc = new ReturnCode(msg, ok);

        stub(method(WorkspaceResourceHelper.class, "checkSourceFileNodeOpening", SourceFileRepNode.class)).toReturn(rc); //$NON-NLS-1$

        TOPRepositoryService service = new TOPRepositoryService();

        assertTrue(service.sourceFileOpening(folderNodeMock));
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.service.TOPRepositoryService#removeAliasInSQLExplorer(org.talend.repository.model.IRepositoryNode)}
     * .
     */
    public void testRemoveAliasInSQLExplorer() {
        IRepositoryNode child = mock(IRepositoryNode.class);
        IRepositoryViewObject obj = mock(IRepositoryViewObject.class);
        when(child.getObject()).thenReturn(obj);
        Property pro = mock(Property.class);
        when(obj.getProperty()).thenReturn(pro);
        ConnectionItem item = mock(ConnectionItem.class);
        when(pro.getItem()).thenReturn(item);

        MDMConnection mdm = mock(MDMConnection.class);
        DelimitedFileConnection dfile = mock(DelimitedFileConnection.class);
        DatabaseConnection dbc = mock(DatabaseConnection.class);
        when(item.getConnection()).thenReturn(dbc).thenReturn(dfile).thenReturn(mdm);

        List<ModelElement> dependencyClients = new ArrayList<ModelElement>();
        ModelElement m1 = mock(ModelElement.class);
        dependencyClients.add(m1);
        PowerMockito.mockStatic(EObjectHelper.class);
        when(EObjectHelper.getDependencyClients(dbc)).thenReturn(dependencyClients);
        when(EObjectHelper.getDependencyClients(mdm)).thenReturn(dependencyClients);
        when(EObjectHelper.getDependencyClients(dfile)).thenReturn(dependencyClients);

        Assert.assertFalse(tdqRepService.removeAliasInSQLExplorer(child));
        Assert.assertFalse(tdqRepService.removeAliasInSQLExplorer(child));
        Assert.assertFalse(tdqRepService.removeAliasInSQLExplorer(child));
    }
}
