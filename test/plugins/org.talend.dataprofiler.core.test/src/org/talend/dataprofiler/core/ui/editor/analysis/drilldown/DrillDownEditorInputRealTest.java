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
package org.talend.dataprofiler.core.ui.editor.analysis.drilldown;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.ViewHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;

/**
 * DOC talend class global comment. Detailled comment
 */
public class DrillDownEditorInputRealTest {

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnsByTdColumn(org.talend.cwm.relational.TdColumn)}
     * . case1 :for table case
     */
    @Test
    public void testGetColumnsByTdColumnCase1() {
        TdColumn currentTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdColumn secondTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdTable createTdTable = RelationalFactory.eINSTANCE.createTdTable();
        TableHelper.addColumn(createTdTable, currentTdColumn);
        TableHelper.addColumn(createTdTable, secondTdColumn);
        DrillDownEditorInput drillDownEditorInput = new DrillDownEditorInput();
        List<TdColumn> allofTdColumn = drillDownEditorInput.getColumnsByTdColumn(currentTdColumn);
        Assert.assertEquals(createTdTable.getColumns().size(), allofTdColumn.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnsByTdColumn(org.talend.cwm.relational.TdColumn)}
     * . case2 :for view case
     */
    @Test
    public void testGetColumnsByTdColumnCase2() {
        TdColumn currentTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdColumn secondTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdView createTdView = RelationalFactory.eINSTANCE.createTdView();
        List<TdColumn> tdColumns = new ArrayList<TdColumn>();
        tdColumns.add(currentTdColumn);
        tdColumns.add(secondTdColumn);
        ViewHelper.addColumns(createTdView, tdColumns);
        DrillDownEditorInput drillDownEditorInput = new DrillDownEditorInput();
        List<TdColumn> allofTdColumn = drillDownEditorInput.getColumnsByTdColumn(currentTdColumn);
        Assert.assertEquals(createTdView.getColumns().size(), allofTdColumn.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnsByTdColumn(org.talend.cwm.relational.TdColumn)}
     * . case3 :table is null
     */
    @Test
    public void testGetColumnsByTdColumnCase3() {
        TdColumn currentTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdColumn secondTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdView createTdView = RelationalFactory.eINSTANCE.createTdView();
        List<TdColumn> tdColumns = new ArrayList<TdColumn>();
        tdColumns.add(currentTdColumn);
        tdColumns.add(secondTdColumn);
        DrillDownEditorInput drillDownEditorInput = new DrillDownEditorInput();
        List<TdColumn> allofTdColumn = drillDownEditorInput.getColumnsByTdColumn(currentTdColumn);
        Assert.assertEquals(createTdView.getColumns().size(), allofTdColumn.size());
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.analysis.drilldown.DrillDownEditorInput#getColumnsByTdColumn(org.talend.cwm.relational.TdColumn)}
     * . case4 :the parent of column is not belong to columnSet(for example MetadataTable)
     */
    @Test
    public void testGetColumnsByTdColumnCase4() {
        TdColumn currentTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        TdColumn secondTdColumn = RelationalFactory.eINSTANCE.createTdColumn();
        List<TdColumn> tdColumns = new ArrayList<TdColumn>();
        tdColumns.add(currentTdColumn);
        tdColumns.add(secondTdColumn);
        MetadataTable createMetadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
        createMetadataTable.getFeature().addAll(tdColumns);
        DrillDownEditorInput drillDownEditorInput = new DrillDownEditorInput();
        List<TdColumn> allofTdColumn = drillDownEditorInput.getColumnsByTdColumn(currentTdColumn);
        Assert.assertEquals(0, allofTdColumn.size());
    }

}
