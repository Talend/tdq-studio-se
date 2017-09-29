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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dq.helper.ReportFileHelper.ReportListParameters;
import org.talend.resource.ResourceManager;
import org.talend.utils.string.StringUtilities;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ReportUtilsRealTest {

    private IProject realProject = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // do something here
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // do something here
    }

    @Before
    public void setUp() throws Exception {
        realProject = ResourceManager.getRootProject();
    }

    @After
    public void tearDown() throws Exception {
        // do something here
    }

    /**
     * Test method for {@link org.talend.dq.helper.ReportFileHelper#getTheLatestReport(org.eclipse.core.resources.IFile)}.
     * 
     * @throws Exception
     */
    @Test
    public void testGetTheLatestReport() throws Exception {
        if (this.realProject != null) {
            String folderName1 = ERepositoryObjectType.TDQ_DATA_PROFILING.getFolder();
            String folderName2 = ERepositoryObjectType.TDQ_REPORT_ELEMENT.getFolder();
            String reportName = "B" + StringUtilities.getRandomString(7); //$NON-NLS-1$

            UnitTestBuildHelper.createRealFolder(this.realProject, folderName1);
            IFolder folder2 = UnitTestBuildHelper.createRealFolder(this.realProject, folderName2);
            IFile iFile = UnitTestBuildHelper.createRealFile(folder2, reportName + "_0.1.rep"); //$NON-NLS-1$
            assertTrue(iFile.exists());
            assertTrue(WorkspaceUtils.ifileToFile(iFile).exists());

            ReportFileHelper.recordReportFiles(iFile, "s-20141011-1801-00013", "..s-20141011-1801-00013.pdf", //$NON-NLS-1$ //$NON-NLS-2$
                    1);
            ReportFileHelper.recordReportFiles(iFile, "s-20141011-1802-00026", "..s-20141011-1802-00026.pdf", //$NON-NLS-1$ //$NON-NLS-2$
                    2);
            ReportFileHelper.recordReportFiles(iFile, "s-20141011-1809-00004", "..s-20141011-1809-00004.pdf", //$NON-NLS-1$ //$NON-NLS-2$
                    3);

            ReportListParameters lastest = ReportFileHelper.getTheLatestReport(iFile);
            assertNotNull(lastest);
            Assert.assertEquals("s-20141011-1809-00004", lastest.getName()); //$NON-NLS-1$
        } else {
            fail("project is null!"); //$NON-NLS-1$
        }
    }

    /**
     * Test method for {@link org.talend.dq.helper.ReportFileHelper#getReportListFile(org.eclipse.core.resources.IFile)}.
     */
    @Test
    public void testGetReportListFile() {
        if (this.realProject != null) {
            String folderName1 = ERepositoryObjectType.TDQ_DATA_PROFILING.getFolder();
            String folderName2 = ERepositoryObjectType.TDQ_REPORT_ELEMENT.getFolder();
            String reportName = "B" + StringUtilities.getRandomString(7); //$NON-NLS-1$

            UnitTestBuildHelper.createRealFolder(this.realProject, folderName1);
            IFolder folder2 = UnitTestBuildHelper.createRealFolder(this.realProject, folderName2);
            IFile iFile = UnitTestBuildHelper.createRealFile(folder2, reportName + "_0.1.rep"); //$NON-NLS-1$
            assertTrue(iFile.exists());
            assertTrue(WorkspaceUtils.ifileToFile(iFile).exists());

            File reportListFile = ReportFileHelper.getReportListFile(iFile);
            assertTrue(reportListFile.exists());
        } else {
            fail("project is null!"); //$NON-NLS-1$
        }
    }

    /**
     * Test method for {@link org.talend.dq.helper.ReportFileHelper#getSimpleName(java.lang.String)}.
     */
    @Test
    public void testGetSimpleName() {
        String reportName = "B" + StringUtilities.getRandomString(7) + "_0.1"; //$NON-NLS-1$ //$NON-NLS-2$
        String reportFileName = reportName + ".rep"; //$NON-NLS-1$
        String simpleName = ReportFileHelper.getSimpleName(reportFileName);
        assertTrue(reportName.equals(simpleName));

        reportName = "B" + StringUtilities.getRandomString(7); //$NON-NLS-1$
        simpleName = ReportFileHelper.getSimpleName(reportName);
        assertNull(simpleName);
    }

    /**
     * Test method for {@link org.talend.dq.helper.ReportFileHelper#getSimpleName(org.talend.core.model.properties.Property)} .
     */
    @Test
    public void testGetSimpleName2() {
        Property prop = PropertiesFactory.eINSTANCE.createProperty();
        String label = "label"; //$NON-NLS-1$
        String version = "0.1"; //$NON-NLS-1$
        prop.setLabel(label);
        prop.setVersion(version);
        String simpleName = ReportFileHelper.getSimpleName(prop);
        assertTrue(simpleName.equals(label + "_" + version)); //$NON-NLS-1$
    }

    /**
     * Test method for {@link org.talend.dq.helper.ReportFileHelper#initRepListFile(org.eclipse.core.resources.IFile)} .
     */
    @Test
    public void testInitRepListFile() {
        if (this.realProject != null) {
            String folderName1 = ERepositoryObjectType.TDQ_DATA_PROFILING.getFolder();
            String folderName2 = ERepositoryObjectType.TDQ_REPORT_ELEMENT.getFolder();
            String reportName = "B" + StringUtilities.getRandomString(7); //$NON-NLS-1$

            UnitTestBuildHelper.createRealFolder(this.realProject, folderName1);
            IFolder folder2 = UnitTestBuildHelper.createRealFolder(this.realProject, folderName2);
            IFile iFile = UnitTestBuildHelper.createRealFile(folder2, reportName + "_0.1.rep"); //$NON-NLS-1$
            assertTrue(iFile.exists());
            assertTrue(WorkspaceUtils.ifileToFile(iFile).exists());
            IFolder iFolder = UnitTestBuildHelper.createRealFolder(folder2, ReportFileHelper.getOutputFolder(iFile).getFullPath()
                    .lastSegment());
            assertTrue(iFolder.exists());
            assertTrue(WorkspaceUtils.ifolderToFile(iFolder).exists());

            try {
                File repListFile = ReportFileHelper.getReportListFile(iFile);
                assertTrue(repListFile.exists());

                ReportFileHelper.initRepListFile(iFile);
                assertTrue(repListFile.length() > 0);
            } catch (Exception e) {
                fail(e.getMessage());
            }
        } else {
            fail("project is null!"); //$NON-NLS-1$
        }
    }

    /**
     * Test method for {@link org.talend.dq.helper.ReportFileHelper#deleteRepOutputFolder(org.eclipse.core.resources.IFile)} .
     */
    @Test
    public void testDeleteRepOutputFolder() {
        if (this.realProject != null) {
            String folderName1 = ERepositoryObjectType.TDQ_DATA_PROFILING.getFolder();
            String folderName2 = ERepositoryObjectType.TDQ_REPORT_ELEMENT.getFolder();
            String reportName = "B" + StringUtilities.getRandomString(7); //$NON-NLS-1$

            UnitTestBuildHelper.createRealFolder(this.realProject, folderName1);
            IFolder folder2 = UnitTestBuildHelper.createRealFolder(this.realProject, folderName2);
            IFile iFile = UnitTestBuildHelper.createRealFile(folder2, reportName + "_0.1.rep"); //$NON-NLS-1$
            assertTrue(iFile.exists());
            assertTrue(WorkspaceUtils.ifileToFile(iFile).exists());
            IFolder iFolder = UnitTestBuildHelper.createRealFolder(folder2, ReportFileHelper.getOutputFolder(iFile).getFullPath()
                    .lastSegment());
            assertTrue(iFolder.exists());
            assertTrue(WorkspaceUtils.ifolderToFile(iFolder).exists());

            try {
                ReportFileHelper.getReportListFile(iFile);

                File outputFolder = WorkspaceUtils.ifolderToFile(ReportFileHelper.getOutputFolder(iFile));
                assertTrue(outputFolder.exists() && outputFolder.isDirectory());
                ReturnCode rc = ReportFileHelper.deleteRepOutputFolder(iFile);
                assertTrue(rc.isOk());
                assertFalse(outputFolder.exists());
            } catch (Exception e) {
                fail(e.getMessage());
            }
        } else {
            fail("project is null!"); //$NON-NLS-1$
        }
    }

    /**
     * Test method for {@link org.talend.dq.helper.ReportFileHelper#getReportListParameters(java.io.File)} .
     */
    @Test
    public void testGetReportListParameters() {
        try {
            File file = new File("./file.txt");
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                out.println("\"Name\"\t\"Path\"\t\"CreateTime\"");
                out.println("\"a1-20160624-1847-00059\"\t\"..a1-20160624-1847-00059.pdf\"\t\"1466765283837\"");
                out.println("\"a2-20160624-1947-00095\"\t\"..a2-20160624-1947-00095.pdf\"\t\"1466795093537\"");
                out.close();

                List<ReportListParameters> reportListParameters = ReportFileHelper.getReportListParameters(file);
                String params = "";
                int i = 0;
                for (ReportListParameters p : reportListParameters) {
                    i++;
                    params += i + ":";
                    params += "[" + p.getName() + "," + p.getPath() + "," + p.getCreateTime() + "]";
                }
                Assert.assertEquals(
                        "1:[a1-20160624-1847-00059,..a1-20160624-1847-00059.pdf,1466765283837]2:[a2-20160624-1947-00095,..a2-20160624-1947-00095.pdf,1466795093537]",
                        params);
            }
            if (file.exists()) {
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
