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
package org.talend.dataprofiler.core.ui.utils;

import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.*;
import static org.powermock.api.support.membermodification.MemberModifier.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.ReportsFactory;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.ColumnRepNode;
import org.talend.dq.nodes.ConnectionRepNode;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.dq.nodes.DQDBFolderRepositoryNode;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

/**
 * DOC yyin class global comment. Detailled comment
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProjectManager.class, ResourceManager.class, RepositoryNodeHelper.class, CorePlugin.class,
        ProxyRepositoryFactory.class, ModelElementHelper.class })
public class RepNodeUtilsTest {

    TdReport realReport;

    String oldJrxmlPath = "../../../TDQ_Libraries/JRXML Template/column/column_basic_0.1.jrxml"; //$NON-NLS-1$

    String newJrxmlPath = "TDQ_Libraries/JRXML Template/column/new_column_basic_new_0.1.jrxml"; //$NON-NLS-1$

    String moveJrxmlPath = "TDQ_Libraries/JRXML Template/column_move/new_column_basic_new_0.1.jrxml"; //$NON-NLS-1$

    String newJrxmlResource = "../../../TDQ_Libraries/JRXML Template/column/new_column_basic_new_0.1.jrxml"; //$NON-NLS-1$

    /**
     * can not use the real project to test, because the method RepositoryNodeHelper.getDataProfilingFolderNode will use
     * the UI view to fetch the node. so , can only mock for this test. The report and its analysismap with jrxml is
     * real created, not mocked object.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        // create a report with user defined jrxml
        realReport = ReportHelper.createReport("report20130124"); //$NON-NLS-1$
        AnalysisMap analysisMap = ReportsFactory.eINSTANCE.createAnalysisMap();
        analysisMap.setReportType(ReportHelper.ReportType.USER_MADE.getLabel());
        analysisMap.setAnalysis(AnalysisHelper.createAnalysis("analysis20130124")); //$NON-NLS-1$
        analysisMap.setJrxmlSource(oldJrxmlPath);
        realReport.getAnalysisMap().add(analysisMap);

        List<ReportRepNode> repNodes = new ArrayList<ReportRepNode>();
        ReportRepNode repNode = mock(ReportRepNode.class);
        repNodes.add(repNode);
        IRepositoryViewObject view = mock(IRepositoryViewObject.class);
        Property prop = mock(Property.class);
        Item item = mock(Item.class);

        when(prop.getItem()).thenReturn(item);
        when(view.getProperty()).thenReturn(prop);
        when(repNode.getObject()).thenReturn(view);
        when(repNode.getReport()).thenReturn(realReport);

        ProjectManager manager = mock(ProjectManager.class);
        stub(method(ProjectManager.class, "getInstance")).toReturn(manager); //$NON-NLS-1$

        Project project = mock(Project.class);
        when(manager.getCurrentProject()).thenReturn(project);

        PowerMockito.mock(ResourceManager.class);
        IProject iproject = mock(IProject.class);
        when(iproject.getLocation()).thenReturn(new Path("/opt/runtime/tdqee/a1/")); //$NON-NLS-1$
        stub(method(ResourceManager.class, "getRootProject")).toReturn(iproject); //$NON-NLS-1$

        IFile ifile = mock(IFile.class);
        when(iproject.getFile(newJrxmlPath)).thenReturn(ifile);
        when(ifile.getLocation()).thenReturn(new Path("/opt/runtime/tdqee/a1/" + newJrxmlPath)); //$NON-NLS-1$

        PowerMockito.mockStatic(ModelElementHelper.class);
        IFile ifile2 = mock(IFile.class);
        when(ModelElementHelper.getIFile(realReport)).thenReturn(ifile2);
        when(ifile2.getLocation())
                .thenReturn(new Path("/opt/runtime/tdqee/a1/TDQ_Data Profiling/Reports/report20130124_0.1.rep")); //$NON-NLS-1$

        IFile ifile3 = mock(IFile.class);
        when(iproject.getFile(moveJrxmlPath)).thenReturn(ifile3);
        when(ifile3.getLocation()).thenReturn(new Path("/opt/runtime/tdqee/a1/" + moveJrxmlPath)); //$NON-NLS-1$

        PowerMockito.mock(RepositoryNodeHelper.class);
        IRepositoryNode ReportRootFolderNode = mock(IRepositoryNode.class);
        stub(method(RepositoryNodeHelper.class, "getDataProfilingFolderNode", EResourceConstant.class)).toReturn( //$NON-NLS-1$
                ReportRootFolderNode);
        stub(method(RepositoryNodeHelper.class, "getReportRepNodes", IRepositoryNode.class, boolean.class, boolean.class)) //$NON-NLS-1$
                .toReturn(repNodes);

        ProxyRepositoryFactory repFactory = mock(ProxyRepositoryFactory.class);
        PowerMockito.mockStatic(ProxyRepositoryFactory.class);
        when(ProxyRepositoryFactory.getInstance()).thenReturn(repFactory);
        stub(method(ProxyRepositoryFactory.class, "save", Project.class, Item.class, boolean.class)); //$NON-NLS-1$
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // do nothing until now
    }

    /**
     * Test for normal case: change the jrxml source in the report which use it. Test method for
     * {@link org.talend.dataprofiler.core.ui.utils.RepNodeUtils#updateJrxmlRelatedReport(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)}
     * .
     */
    @Test
    public void testUpdateJrxmlRelatedReportIPath() {

        // before update, the jrxml in the report is old.
        for (AnalysisMap anaMap : realReport.getAnalysisMap()) {
            Assert.assertTrue(anaMap.getJrxmlSource().equals(oldJrxmlPath));
        }
        RepNodeUtils.updateJrxmlRelatedReport(new Path(oldJrxmlPath), new Path(newJrxmlPath));
        // after update, the jrxml in the report should be changed to new
        for (AnalysisMap anaMap : realReport.getAnalysisMap()) {
            Assert.assertTrue(anaMap.getJrxmlSource().equals(this.newJrxmlResource));
        }
    }

    /**
     * Test for rename case: not change the jrxml source in the report which NOT use it. Test method for
     * {@link org.talend.dataprofiler.core.ui.utils.RepNodeUtils#updateJrxmlRelatedReport(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)}
     * .
     */
    @Test
    public void testUpdateJrxmlRelatedReportIPath_2() {
        String jrxmlPath = "TDQ_Libraries/JRXML Template/column/column_0.1.jrxml"; //$NON-NLS-1$
        // before update, the jrxml in the report
        for (AnalysisMap anaMap : realReport.getAnalysisMap()) {
            Assert.assertTrue(anaMap.getJrxmlSource().equals(oldJrxmlPath));
        }
        RepNodeUtils.updateJrxmlRelatedReport(new Path(jrxmlPath), new Path(newJrxmlPath));
        // after update, the jrxml in the report should NOT be changed, because this report didnot user the renamed
        // jrxml.
        for (AnalysisMap anaMap : realReport.getAnalysisMap()) {
            Assert.assertTrue(anaMap.getJrxmlSource().equals(oldJrxmlPath));
        }
    }

    /**
     * Test for move jrxml file case: move the jrxml source, the in the report which use it will also be updated. Test
     * method for
     * {@link org.talend.dataprofiler.core.ui.utils.RepNodeUtils#updateJrxmlRelatedReport(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)}
     * .
     */
    @Test
    public void testUpdateJrxmlRelatedReportIPath_move() {

        // before update, the jrxml in the report is old.
        for (AnalysisMap anaMap : realReport.getAnalysisMap()) {
            Assert.assertTrue(anaMap.getJrxmlSource().equals(oldJrxmlPath));
        }
        RepNodeUtils.updateJrxmlRelatedReport(new Path(oldJrxmlPath), new Path(this.moveJrxmlPath));
        // after update, the jrxml in the report should be changed to new
        for (AnalysisMap anaMap : realReport.getAnalysisMap()) {
            Assert.assertTrue(anaMap.getJrxmlSource().equals("../../../" + this.moveJrxmlPath)); //$NON-NLS-1$
        }
    }

    /**
     * Test: 1) when the selected node is: connection, catalog,schema, folder, will not be valid; 2) when the selected
     * nodes are: multiple table/views, multiple columns from different table/view, will not be valid; 3) when the
     * selected node is: one single table/view/file table,will be valid; 4) when the selected nodes are: multiple
     * columns from one same table/view, will be valid.
     */
    @Test
    public void testIsValidSelectionForMatchAnalysis() {
        // 1) when the selected node is: connection, catalog,schema, folder, will not be valid;
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        ConnectionRepNode cNode = mock(ConnectionRepNode.class);
        nodes.add(cNode);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        DBConnectionRepNode dbNode = mock(DBConnectionRepNode.class);
        nodes.clear();
        nodes.add(dbNode);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        DBConnectionFolderRepNode dbfNode = mock(DBConnectionFolderRepNode.class);
        nodes.clear();
        nodes.add(dbfNode);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        DQDBFolderRepositoryNode folderNode = mock(DQDBFolderRepositoryNode.class);
        nodes.clear();
        nodes.add(folderNode);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        DBCatalogRepNode catalogNode = mock(DBCatalogRepNode.class);
        nodes.clear();
        nodes.add(catalogNode);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        DBSchemaRepNode schemaNode = mock(DBSchemaRepNode.class);
        nodes.clear();
        nodes.add(schemaNode);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));
    }

    /**
     * Test: 2) when the selected nodes are: multiple table/views, multiple columns from different table/view, will not
     * be valid;
     */
    @Test
    public void testIsValidSelectionForMatchAnalysis_2() {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        DBTableRepNode table1 = mock(DBTableRepNode.class);
        DBTableRepNode table2 = mock(DBTableRepNode.class);
        nodes.clear();
        nodes.add(table1);
        nodes.add(table2);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        DBViewRepNode view1 = mock(DBViewRepNode.class);
        DBViewRepNode view2 = mock(DBViewRepNode.class);
        nodes.clear();
        nodes.add(view1);
        nodes.add(view2);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        ColumnRepNode col1 = mock(ColumnRepNode.class);
        ColumnRepNode col2 = mock(ColumnRepNode.class);
        when(col1.getParent()).thenReturn(table1);
        when(col2.getParent()).thenReturn(table2);
        nodes.clear();
        nodes.add(col1);
        nodes.add(col2);
        Assert.assertFalse(RepNodeUtils.isValidSelectionFromSameTable(nodes));
    }

    /**
     * Test: 3) when the selected node is: one single table/view/file table,will be valid;
     */
    @Test
    public void testIsValidSelectionForMatchAnalysis_3() {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        DBTableRepNode table1 = mock(DBTableRepNode.class);
        DBViewRepNode view1 = mock(DBViewRepNode.class);

        // 3) when the selected node is: one single table/view/file table,will be valid;
        nodes.clear();
        nodes.add(table1);
        Assert.assertTrue(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        nodes.clear();
        nodes.add(view1);
        Assert.assertTrue(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        DFTableRepNode dfTable = mock(DFTableRepNode.class);
        nodes.clear();
        nodes.add(dfTable);
        Assert.assertTrue(RepNodeUtils.isValidSelectionFromSameTable(nodes));
    }

    /**
     * Test:4) when the selected nodes are: multiple columns from one same table/view, will be valid.
     */
    @Test
    public void testIsValidSelectionForMatchAnalysis_4() {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        DBTableRepNode table1 = mock(DBTableRepNode.class);
        DBViewRepNode view1 = mock(DBViewRepNode.class);
        DFTableRepNode dfTable = mock(DFTableRepNode.class);

        ColumnRepNode col1 = mock(ColumnRepNode.class);
        ColumnRepNode col2 = mock(ColumnRepNode.class);
        when(col1.getParent()).thenReturn(table1);
        when(col2.getParent()).thenReturn(table1);
        nodes.clear();
        nodes.add(col1);
        nodes.add(col2);
        Assert.assertTrue(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        when(col1.getParent()).thenReturn(view1);
        when(col2.getParent()).thenReturn(view1);
        nodes.clear();
        nodes.add(col1);
        nodes.add(col2);
        Assert.assertTrue(RepNodeUtils.isValidSelectionFromSameTable(nodes));

        when(col1.getParent()).thenReturn(dfTable);
        when(col2.getParent()).thenReturn(dfTable);
        nodes.clear();
        nodes.add(col1);
        nodes.add(col2);
        Assert.assertTrue(RepNodeUtils.isValidSelectionFromSameTable(nodes));
    }
}
