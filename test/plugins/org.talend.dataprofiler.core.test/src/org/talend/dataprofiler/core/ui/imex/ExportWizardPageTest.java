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
package org.talend.dataprofiler.core.ui.imex;

import static org.junit.Assert.*;

import org.eclipse.swt.widgets.Text;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 * DOC zshen class global comment. Detailled comment need to run it with a top application
 */

public class ExportWizardPageTest {


    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.imex.ExportWizardPage#updateBasePath()}.
     */
    @Test
    public void testUpdateBasePath() {
        // mock mockExportWizardPage.isDirState()
        String return1 = "dirTxt";//$NON-NLS-1$
        String return2 = "archTxt";//$NON-NLS-1$
        ExportWizardPage exportWizardPage = new ExportWizardPage(null);
        ExportWizardPage mockExportWizardPage = Mockito.spy(exportWizardPage);

        // Shell shell = new Shell();
        // mockExportWizardPage.createSelectComposite(shell);
        // mockExportWizardPage.createRepositoryTree(shell);

        PowerMockito.doReturn(true).when(mockExportWizardPage).isDirState();
        PowerMockito.doReturn(return1).when(mockExportWizardPage).getTextContent((Text) Mockito.any());
        PowerMockito.doNothing().when(mockExportWizardPage).textModified(Mockito.anyString());
        
        String updateBasePath1 = mockExportWizardPage.updateBasePath();

        PowerMockito.doReturn(false).when(mockExportWizardPage).isDirState();
        PowerMockito.doReturn(return2).when(mockExportWizardPage).getTextContent((Text) Mockito.any());
        String updateBasePath2 = mockExportWizardPage.updateBasePath();
        Mockito.verify(mockExportWizardPage, Mockito.times(2)).isDirState();
        Mockito.verify(mockExportWizardPage, Mockito.times(2)).getTextContent((Text) Mockito.any());
        Mockito.verify(mockExportWizardPage, Mockito.times(2)).textModified(Mockito.anyString());
        // Mockito.verify(mockExportWizardPage).createSelectComposite(shell);
        // Mockito.verify(mockExportWizardPage).createRepositoryTree(shell);

        assertEquals(updateBasePath1, return1);
        assertEquals(updateBasePath2, return2);
        // shell.dispose();
    }

    public void textModified(String pathStr) {

    }
}
