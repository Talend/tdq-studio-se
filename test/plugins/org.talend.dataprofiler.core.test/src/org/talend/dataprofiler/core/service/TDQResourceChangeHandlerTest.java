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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.repository.model.IRepositoryFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC qiongli class global comment. Detailled comment
 */
@PrepareForTest({ RepositoryNodeHelper.class, EObjectHelper.class, ProxyRepositoryFactory.class })
public class TDQResourceChangeHandlerTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private TDQResourceChangeHandler tdqResourceChangeHandler = null;

    @Before
    public void setUp() throws Exception {
        if (tdqResourceChangeHandler == null) {
            ProxyRepositoryFactory proxFactory = mock(ProxyRepositoryFactory.class);
            stub(method(ProxyRepositoryFactory.class, "getInstance")).toReturn(proxFactory);//$NON-NLS-1$
            IRepositoryFactory repFactory = mock(IRepositoryFactory.class);
            when(proxFactory.getRepositoryFactoryFromProvider()).thenReturn(repFactory);
            XmiResourceManager resManager = mock(XmiResourceManager.class);
            when(repFactory.getResourceManager()).thenReturn(resManager);
            tdqResourceChangeHandler = new TDQResourceChangeHandler();
        }
    }

    /**
     * 
     * test for a folde node and its children has depnedences.
     */
    @Test
    public void testGetDependentNodes_folderNode() {

        IRepositoryNode currentNode = mock(IRepositoryNode.class);
        IRepositoryNode child1 = mock(IRepositoryNode.class);
        IRepositoryNode child2 = mock(IRepositoryNode.class);
        List<IRepositoryNode> children = new ArrayList<IRepositoryNode>();
        children.add(child1);
        children.add(child2);
        when(currentNode.getChildren()).thenReturn(children);
        PowerMockito.mockStatic(RepositoryNodeHelper.class);
        ModelElement con1 = mock(ModelElement.class);
        ModelElement con2 = mock(ModelElement.class);
        when(RepositoryNodeHelper.getModelElementFromRepositoryNode(child1)).thenReturn(con1);
        when(RepositoryNodeHelper.getModelElementFromRepositoryNode(child2)).thenReturn(con2);
        ModelElement clientDenMod = mock(ModelElement.class);
        ModelElement clientDenMod2 = mock(ModelElement.class);
        List<ModelElement> clientLs = new ArrayList<ModelElement>();
        List<ModelElement> clientLs2 = new ArrayList<ModelElement>();
        clientLs.add(clientDenMod);
        clientLs2.add(clientDenMod2);
        PowerMockito.mockStatic(EObjectHelper.class);
        when(EObjectHelper.getDependencyClients(child1)).thenReturn(clientLs);
        when(EObjectHelper.getDependencyClients(child2)).thenReturn(clientLs2);
        when(currentNode.getType()).thenReturn(ENodeType.SIMPLE_FOLDER);
        List<IRepositoryNode> dependentNodes = tdqResourceChangeHandler.getDependentNodes(currentNode);
        assertFalse(dependentNodes.isEmpty());
        assertTrue(dependentNodes.size() == 2);

    }

    /**
     * 
     * test for a node(not folder) with client dependences.
     */
    @Test
    public void testGetDependentNodes_node1() {

        IRepositoryNode currentNode = mock(IRepositoryNode.class);
        PowerMockito.mockStatic(RepositoryNodeHelper.class);

        ModelElement mod = mock(ModelElement.class);
        when(RepositoryNodeHelper.getModelElementFromRepositoryNode(currentNode)).thenReturn(mod);
        ModelElement clientDenMod = mock(ModelElement.class);
        List<ModelElement> clientLs = new ArrayList<ModelElement>();
        clientLs.add(clientDenMod);
        PowerMockito.mockStatic(EObjectHelper.class);
        when(EObjectHelper.getDependencyClients(currentNode)).thenReturn(clientLs);

        List<IRepositoryNode> dependentNodes = tdqResourceChangeHandler.getDependentNodes(currentNode);
        assertFalse(dependentNodes.isEmpty());

    }

    /**
     * 
     * test for a node(not folder) without client dependences..
     */
    @Test
    public void testGetDependentNodes_node2() {

        IRepositoryNode currentNode = mock(IRepositoryNode.class);
        PowerMockito.mockStatic(RepositoryNodeHelper.class);
        ModelElement mod = mock(ModelElement.class);
        List<ModelElement> clientLs = new ArrayList<ModelElement>();
        PowerMockito.mockStatic(EObjectHelper.class);
        when(EObjectHelper.getDependencyClients(mod)).thenReturn(clientLs);

        List<IRepositoryNode> dependentNodes = tdqResourceChangeHandler.getDependentNodes(currentNode);
        assertTrue(dependentNodes.isEmpty());

    }

}
