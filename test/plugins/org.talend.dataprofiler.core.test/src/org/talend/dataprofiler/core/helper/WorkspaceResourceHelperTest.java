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
package org.talend.dataprofiler.core.helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.*;
import static org.powermock.api.support.membermodification.MemberModifier.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
@PrepareForTest({ MessageUI.class, DefaultMessagesImpl.class, CorePlugin.class, WorkspaceResourceHelper.class,
        RepositoryNodeHelper.class })
public class WorkspaceResourceHelperTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.WorkspaceResourceHelper#sourceFileHasBeenOpened(org.talend.repository.model.IRepositoryNode)}
     * .
     */
    @Test
    public void testSourceFileHasBeenOpened() {
        SourceFileRepNode fileNode = mock(SourceFileRepNode.class);

        CorePlugin cpMock = mock(CorePlugin.class);
        PowerMockito.mockStatic(CorePlugin.class);
        when(CorePlugin.getDefault()).thenReturn(cpMock);

        IWorkbench workbenchMock = mock(IWorkbench.class);
        when(cpMock.getWorkbench()).thenReturn(workbenchMock);

        IWorkbenchWindow workbenchWindowMock = mock(IWorkbenchWindow.class);
        when(workbenchMock.getActiveWorkbenchWindow()).thenReturn(workbenchWindowMock);

        IWorkbenchPage workbenchPageMock = mock(IWorkbenchPage.class);
        when(workbenchWindowMock.getActivePage()).thenReturn(workbenchPageMock);

        IEditorReference editorRefMock = mock(IEditorReference.class);
        IEditorReference[] editorRefMocks = new IEditorReference[] { editorRefMock };
        when(workbenchPageMock.getEditorReferences()).thenReturn(editorRefMocks);

        FileEditorInput fileEditorInputMock = mock(FileEditorInput.class);
        try {
            when(editorRefMock.getEditorInput()).thenReturn(fileEditorInputMock);
        } catch (PartInitException e) {
            fail(e.getMessage());
        }

        IFile nodeFileMock = mock(IFile.class);
        stub(method(RepositoryNodeHelper.class, "getIFile", SourceFileRepNode.class)).toReturn(nodeFileMock); //$NON-NLS-1$

        IPath nodeFilePathMock = mock(IPath.class);
        when(nodeFileMock.getFullPath()).thenReturn(nodeFilePathMock);

        String path = "/abc"; //$NON-NLS-1$
        when(nodeFilePathMock.toString()).thenReturn(path);

        IFile inputFileMock = mock(IFile.class);
        when(fileEditorInputMock.getFile()).thenReturn(inputFileMock);

        IPath inputFilePathMock = mock(IPath.class);
        when(inputFileMock.getFullPath()).thenReturn(inputFilePathMock);

        when(inputFilePathMock.toString()).thenReturn(path);

        assertTrue(WorkspaceResourceHelper.sourceFileHasBeenOpened(fileNode));
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.WorkspaceResourceHelper#checkSourceFileNodeOpening(org.talend.dq.nodes.SourceFileRepNode)}
     * .
     */
    @Test
    public void testCheckSourceFileNodeOpening() {
        SourceFileRepNode fileNodeMock = mock(SourceFileRepNode.class);
        String nodeLabel = "nodeLabel"; //$NON-NLS-1$
        when(fileNodeMock.getLabel()).thenReturn(nodeLabel);

        stub(method(WorkspaceResourceHelper.class, "sourceFileHasBeenOpened", SourceFileRepNode.class)).toReturn(Boolean.TRUE); //$NON-NLS-1$

        ReturnCode rc = WorkspaceResourceHelper.checkSourceFileNodeOpening(fileNodeMock);

        assertTrue(rc.isOk());
        assertTrue(rc.getMessage().startsWith(nodeLabel));
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.helper.WorkspaceResourceHelper#checkSourceFileSubFolderNodeOpening(org.talend.dq.nodes.SourceFileSubFolderNode)}
     * .
     */
    @Test
    public void testCheckSourceFileSubFolderNodeOpening() {
        SourceFileSubFolderNode folderNodeMock = mock(SourceFileSubFolderNode.class);

        SourceFileRepNode nodeMock = mock(SourceFileRepNode.class);
        List<IRepositoryNode> nodeList = new ArrayList<IRepositoryNode>();
        nodeList.add(nodeMock);

        when(folderNodeMock.getChildren()).thenReturn(nodeList);

        boolean ok = Boolean.TRUE;
        String msg = "msg"; //$NON-NLS-1$
        ReturnCode rc = new ReturnCode(msg, ok);

        stub(method(WorkspaceResourceHelper.class, "checkSourceFileNodeOpening", SourceFileRepNode.class)).toReturn(rc); //$NON-NLS-1$

        ReturnCode rc2 = WorkspaceResourceHelper.checkSourceFileSubFolderNodeOpening(folderNodeMock);

        assertEquals(rc.isOk(), rc2.isOk());
        assertEquals(rc.getMessage(), rc2.getMessage());
    }
}
