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
package org.talend.dataquality.record.linkage.ui.composite.chart;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.DuplicateStatisticsRow;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DuplicateRecordPieChartTest {

    private DuplicateRecordPieChart dupRecPieChart = null;

    /**
     * Test method for
     * {@link org.talend.dataquality.record.linkage.ui.composite.chart.DuplicateRecordPieChart#refreshChart(java.util.List)}
     * .
     */
    @Test
    public void testRefreshChartWithNullChartComposite() {
        dupRecPieChart = new DuplicateRecordPieChart(null);
        List<DuplicateStatisticsRow> dupStatistics = new ArrayList<DuplicateStatisticsRow>();
        dupRecPieChart.refreshChart(dupStatistics);
    }

}
