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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
@PrepareForTest({ RepositoryNodeHelper.class, ResourcesPlugin.class })
public class RepositoryNodeHelperTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Test
    public void testFindNearestSystemFolderNode() {
        RepositoryNode node = mock(RepositoryNode.class);
        RepositoryNode parent1 = mock(RepositoryNode.class);
        when(parent1.getType()).thenReturn(ENodeType.SIMPLE_FOLDER);
        when(node.getParent()).thenReturn(parent1);
        RepositoryNode parent2 = mock(RepositoryNode.class);
        when(parent2.getType()).thenReturn(ENodeType.SYSTEM_FOLDER);
        when(parent1.getParent()).thenReturn(parent2);
        RepositoryNode parent3 = mock(RepositoryNode.class);
        when(parent3.getType()).thenReturn(ENodeType.SYSTEM_FOLDER);
        when(parent2.getParent()).thenReturn(parent3);
        RepositoryNode findNearestSysNode = RepositoryNodeHelper.findNearestSystemFolderNode(node);
        assertTrue(findNearestSysNode.equals(parent2));
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#getIFile(org.talend.repository.model.IRepositoryNode)}.
     */
    @Test
    public void testGetIFile() {
        // test non-ModelElement node, such as Jrxml File and Source File
        JrxmlTempleteRepNode jrxmlNodeMock = mock(JrxmlTempleteRepNode.class);

        ModelElement meNull = null; // Jrxml File and Source File don't have ModelElement
        stub(method(RepositoryNodeHelper.class, "getModelElementFromRepositoryNode", JrxmlTempleteRepNode.class)) //$NON-NLS-1$
                .toReturn(meNull);

        PowerMockito.mockStatic(ResourcesPlugin.class);
        IWorkspace workspaceMock = mock(IWorkspace.class);
        when(ResourcesPlugin.getWorkspace()).thenReturn(workspaceMock);

        IWorkspaceRoot workspaceRootMock = mock(IWorkspaceRoot.class);
        when(workspaceMock.getRoot()).thenReturn(workspaceRootMock);

        IRepositoryViewObject repoViewObjMock = mock(IRepositoryViewObject.class);
        when(jrxmlNodeMock.getObject()).thenReturn(repoViewObjMock);

        Property propertyMock = mock(Property.class);
        when(repoViewObjMock.getProperty()).thenReturn(propertyMock);

        Item itemMock = mock(Item.class);
        when(propertyMock.getItem()).thenReturn(itemMock);

        Resource resourceMock = mock(Resource.class);
        when(itemMock.eResource()).thenReturn(resourceMock);

        String propPathStr = "/Talend/Talend_All_trunk/runtime/TDQEE_runtime_50/PP/TDQ_Libraries/JRXML Template/column/b01_column_basic_0.1.properties"; //$NON-NLS-1$
        URI uriReal = URI.createFileURI(propPathStr);
        when(resourceMock.getURI()).thenReturn(uriReal);

        IFile fileMock = mock(IFile.class);
        when(workspaceRootMock.getFile((Path) any())).thenReturn(fileMock);

        IFile iFile = RepositoryNodeHelper.getIFile(jrxmlNodeMock);
        assertEquals(fileMock, iFile);
    }

    /**
     * Test method for
     * {@link org.talend.dq.helper.RepositoryNodeHelper#getIFile(org.talend.repository.model.IRepositoryNode)}.
     */
    @Test
    public void testGetIFile2() {
        // test for ModelElement node, such as Analysis, Report, etc.
        AnalysisRepNode analysisNodeMock = mock(AnalysisRepNode.class);

        ModelElement meMock = mock(ModelElement.class);
        stub(method(RepositoryNodeHelper.class, "getModelElementFromRepositoryNode", AnalysisRepNode.class)) //$NON-NLS-1$
                .toReturn(meMock);

        PowerMockito.mockStatic(ResourcesPlugin.class);
        IWorkspace workspaceMock = mock(IWorkspace.class);
        when(ResourcesPlugin.getWorkspace()).thenReturn(workspaceMock);

        IWorkspaceRoot workspaceRootMock = mock(IWorkspaceRoot.class);
        when(workspaceMock.getRoot()).thenReturn(workspaceRootMock);

        Resource resourceMock = mock(Resource.class);
        when(meMock.eResource()).thenReturn(resourceMock);

        String itemPathStr = "/Talend/Talend_All_trunk/runtime/TDQEE_runtime/P0518/TDQ_Data Profiling/Analyses/a1_0.1.ana";
        URI uri = URI.createFileURI(itemPathStr);
        when(resourceMock.getURI()).thenReturn(uri);

        IFile fileMock = mock(IFile.class);
        when(workspaceRootMock.getFile((Path) any())).thenReturn(fileMock);

        IFile iFile = RepositoryNodeHelper.getIFile(analysisNodeMock);
        assertEquals(fileMock, iFile);
    }
}
