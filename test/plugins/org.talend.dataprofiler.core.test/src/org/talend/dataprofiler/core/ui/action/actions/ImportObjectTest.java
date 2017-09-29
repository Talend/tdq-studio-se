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
package org.talend.dataprofiler.core.ui.action.actions;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.dataprofiler.ecos.jobs.ComponentInstaller;
import org.talend.dataprofiler.ecos.model.IEcosComponent;
import org.talend.utils.sugars.ReturnCode;

/**
 * created by xqliu on Sep 26, 2012 Detailled comment
 */
@PrepareForTest({ ComponentInstaller.class, FilesUtils.class })
public class ImportObjectTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.actions.ImportObject#extractImportObject(org.talend.dataprofiler.ecos.model.IEcosComponent, java.util.List)}
     * .
     */
    @Test
    public void testExtractImportObject() {
        try {
            IEcosComponent ecosMock = mock(IEcosComponent.class);

            PowerMockito.mockStatic(ComponentInstaller.class);
            File fileMock = mock(File.class);
            when(ComponentInstaller.unzip(anyString(), anyString())).thenReturn(fileMock);

            PowerMockito.mockStatic(FilesUtils.class);

            List<File> objFiles = new ArrayList<File>();
            File objFileMock = mock(File.class);
            objFiles.add(objFileMock);

            List<File> jarFiles = new ArrayList<File>();
            File jarFileMock = mock(File.class);
            jarFiles.add(jarFileMock);

            when(FilesUtils.getAllFilesFromFolder((File) anyObject(), (FilenameFilter) anyObject())).thenReturn(objFiles)
                    .thenReturn(jarFiles);

            List<ReturnCode> information = new ArrayList<ReturnCode>();
            List<ImportObject> importObjects = ImportObject.extractImportObject(ecosMock, information);

            assertEquals(importObjects.size(), 1);
            assertEquals(information.size(), 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.action.actions.ImportObject#extractImportObject(org.talend.dataprofiler.ecos.model.IEcosComponent, java.util.List)}
     * .
     */
    @Test
    public void testExtractImportObject2() {
        try {
            IEcosComponent ecosMock = mock(IEcosComponent.class);

            PowerMockito.mockStatic(ComponentInstaller.class);
            File fileMock = mock(File.class);
            when(ComponentInstaller.unzip(anyString(), anyString())).thenReturn(fileMock);

            PowerMockito.mockStatic(FilesUtils.class);

            List<File> objFiles = new ArrayList<File>();

            when(FilesUtils.getAllFilesFromFolder((File) anyObject(), (FilenameFilter) anyObject())).thenReturn(objFiles);

            List<ReturnCode> information = new ArrayList<ReturnCode>();
            List<ImportObject> importObjects = ImportObject.extractImportObject(ecosMock, information);

            assertEquals(importObjects.size(), 0);
            assertEquals(information.size(), 1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
