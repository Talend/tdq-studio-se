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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.DuplicateStatisticsRow;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;

/**
 * created by zhao on Aug 19, 2013 Detailled comment
 * 
 */
public class DuplicateRecordPieChart {

    private Composite parent = null;

    private Object chartComposite = null;

    /**
     * DOC zhao DuplicateRecordPieChart constructor comment.
     * 
     * @param parent
     * @param style
     */
    public DuplicateRecordPieChart(Composite parent) {
        this.parent = parent;
    }

    public void createPieChart(List<DuplicateStatisticsRow> dupStats) {
        Object pieChart = TOPChartUtil.getInstance().createDuplicateRecordPieChart(
                DefaultMessagesImpl.getString("DuplicateRecordPieChart.PieChart.Title"), getPieDataset(dupStats)); //$NON-NLS-1$
        chartComposite = TOPChartUtil.getInstance().createChartComposite(parent, SWT.NONE, pieChart, true);

    }

    private Object getPieDataset(List<DuplicateStatisticsRow> dupStats) {
        Map<String, Long> dupMap = new HashMap<String, Long>();

        for (DuplicateStatisticsRow dupStatRow : dupStats) {
            if (!dupStatRow.getIsRowCount()) {
                String label = dupStatRow.getLabel();
                dupMap.put(label, dupStatRow.getCount());
            }
        }
        return TOPChartUtil.getInstance().createDatasetForDuplicateRecord(dupMap);
    }

    public void refreshChart(List<DuplicateStatisticsRow> dupStatistics) {
        if (chartComposite == null) {
            return;
        }
        ((Composite) chartComposite).dispose();
        createPieChart(dupStatistics);
        ((Composite) chartComposite).getParent().layout();

    }

    public static final int CHART_STANDARD_WIDHT = 600;

    public static final int CHART_STANDARD_HEIGHT = 275;
}
