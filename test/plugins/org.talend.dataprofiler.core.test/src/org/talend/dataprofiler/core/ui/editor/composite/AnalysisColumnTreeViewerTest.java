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
package org.talend.dataprofiler.core.ui.editor.composite;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.relational.TdColumn;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

@RunWith(MockitoJUnitRunner.class)
/**
 * DOC yyin  class global comment. Detailled comment
 */
public class AnalysisColumnTreeViewerTest {

    // @Mock
    // private ColumnMasterDetailsPage masterPage;
    //
    // @Mock
    // private Composite parent;
    //
    // @Mock
    // private DQRuleMasterDetailsPage dqPage;

    private AnalysisColumnTreeViewer analysisColumnTreeViewer;

    private JoinConditionTableViewer joinViewer;

    @Before
    public void createAnalysisColumnTreeViewer() throws Exception {
        // analysisColumnTreeViewer = new AnalysisColumnTreeViewer(parent, masterPage);
        // joinViewer = new JoinConditionTableViewer(parent, dqPage);
    }

    /**
     * DOC yyin Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // createAnalysisColumnTreeViewer();
    }

    /**
     * DOC yyin Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree#filterInputData(java.lang.Object[])}
     * . This test can NOT initialize the test class, so just copy the modified part into this test class, and test the
     * modified part.
     */
    @Test
    public void testFilterInputData_1() {
        MetadataColumn col = mock(MetadataColumn.class);
        Object[] params = { col };

        Object[] beforeModify = this.filterInputData_before(params);
        assertNull(beforeModify);

        Object[] result = filterInputData_after(params);
        assertNotNull(result);
        assertEquals(result.length, 1);

    }

    // Due to TDQ-9553 : not consider MDM related any more.
    Object[] filterInputData_after(Object[] objs) {
        List<IRepositoryNode> reposList = new ArrayList<IRepositoryNode>();
        for (Object obj : objs) {
            if (obj instanceof DBColumnRepNode || obj instanceof DFColumnRepNode) {
                reposList.add((RepositoryNode) obj);
            }
            if (obj instanceof DBTableRepNode || obj instanceof DBViewRepNode || obj instanceof DFTableRepNode) {
                List<IRepositoryNode> children = ((IRepositoryNode) obj).getChildren().get(0).getChildren();
                reposList.addAll(children);

            } else if (obj instanceof TdColumn) {
                // MOD yyi 2012-02-29 TDQ-3605 For column set column list.
                reposList.add(RepositoryNodeHelper.recursiveFind((TdColumn) obj));
            } else if (obj instanceof MetadataColumn) {
                // MOD yyin 2012-03-31 TDQ-4994 reopen column set analysis about fileDelimited file all items gone.
                // because here did not check this kind of obj so it is not be added in the list
                reposList.add(RepositoryNodeHelper.recursiveFind((MetadataColumn) obj));
            }
        }
        if (reposList.size() == 0) {
            // MOD yyi 2012-02-29 TDQ-3605 Empty column table.
            return null;
        }
        return reposList.toArray();
    }

    Object[] filterInputData_before(Object[] objs) {
        List<IRepositoryNode> reposList = new ArrayList<IRepositoryNode>();
        for (Object obj : objs) {
            if (obj instanceof DBColumnRepNode || obj instanceof DFColumnRepNode) {
                reposList.add((RepositoryNode) obj);
            }
            if (obj instanceof DBTableRepNode || obj instanceof DBViewRepNode || obj instanceof DFTableRepNode) {
                List<IRepositoryNode> children = ((IRepositoryNode) obj).getChildren().get(0).getChildren();
                reposList.addAll(children);

            } else if (obj instanceof TdColumn) {
                // MOD yyi 2012-02-29 TDQ-3605 For column set column list.
                reposList.add(RepositoryNodeHelper.recursiveFind((TdColumn) obj));
            }
        }
        if (reposList.size() == 0) {
            // MOD yyi 2012-02-29 TDQ-3605 Empty column table.
            return null;
        }
        return reposList.toArray();
    }

}
