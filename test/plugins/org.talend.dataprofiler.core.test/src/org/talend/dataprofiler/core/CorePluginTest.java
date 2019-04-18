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
package org.talend.dataprofiler.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.helper.PropertyHelper;

/**
 * DOC xqliu class global comment. Detailled comment
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DefaultMessagesImpl.class, CorePlugin.class, PropertyHelper.class })
public class CorePluginTest {


    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.CorePlugin#itemIsOpening(org.talend.core.model.properties.Item, boolean)}.
     */
    @Test
    public void testItemIsOpeningItemBoolean() {
        try {
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
            when(editorRefMock.getEditorInput()).thenReturn(fileEditorInputMock);

            String path1 = "/abc1"; //$NON-NLS-1$
            String path2 = "/abc2"; //$NON-NLS-1$

            IFile inputFileMock = mock(IFile.class);
            when(fileEditorInputMock.getFile()).thenReturn(inputFileMock);

            IPath inputFilePathMock = mock(IPath.class);
            when(inputFileMock.getFullPath()).thenReturn(inputFilePathMock);

            when(inputFilePathMock.toString()).thenReturn(path1);

            Item itemMock = mock(Item.class);
            Property propertyMock = mock(Property.class);
            when(itemMock.getProperty()).thenReturn(propertyMock);

            Resource resourceMock = mock(Resource.class);
            when(propertyMock.eResource()).thenReturn(resourceMock);

            IPath ipathMock = mock(IPath.class);
            PowerMockito.mockStatic(PropertyHelper.class);
            when(PropertyHelper.getItemPath(propertyMock)).thenReturn(ipathMock);
            when(ipathMock.toString()).thenReturn(path2);

            CorePlugin cp = new CorePlugin();
            assertFalse(cp.itemIsOpening(itemMock, false));
        } catch (PartInitException e) {
            fail(e.getMessage());
        }
    }
}
