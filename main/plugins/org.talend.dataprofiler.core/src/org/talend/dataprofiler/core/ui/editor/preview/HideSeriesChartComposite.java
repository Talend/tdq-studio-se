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
package org.talend.dataprofiler.core.ui.editor.preview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.DatasetUtils.DateValueAggregate;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataprofiler.service.utils.ValueAggregator;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class HideSeriesChartComposite {

    private ColumnSetMultiValueIndicator indicator;

    private ModelElement column;

    private Object chart;

    private Object chartComposite;

    private static final String SERIES_KEY_ID = "SERIES_KEY"; //$NON-NLS-1$

    // used for the bubble chart to add mouse listeners
    private Map<Integer, Object> bubbleQueryMap;

    private Map<Integer, Object> ganttQueryMap;

    private Analysis analysis = null;

    boolean isCoungAvg = false;

    boolean isMinMax = false;

    public HideSeriesChartComposite(Composite comp, Analysis ana, ColumnSetMultiValueIndicator indicator, ModelElement column,
            boolean isNeedUtility) {

        this.analysis = ana;
        this.indicator = indicator;
        this.column = column;

        isCoungAvg = ColumnsetPackage.eINSTANCE.getCountAvgNullIndicator().equals(indicator.eClass());
        isMinMax = ColumnsetPackage.eINSTANCE.getMinMaxDateIndicator().equals(indicator.eClass());

        this.chart = createChart();

        chartComposite = TOPChartUtils.getInstance().createChartCompositeForCorrelationAna(comp, chart,
                indicator.getAnalyzedColumns().size() * 30 < 230 ? 230 : indicator.getAnalyzedColumns().size() * 30);

        if (chart != null && isNeedUtility) {
            createUtilityControl(comp);
        }

        addSpecifiedListeners(isCoungAvg, isMinMax);
    }

    private void addSpecifiedListeners(final boolean isAvg, final boolean isDate) {
        if (isAvg) {
            TOPChartUtils.getInstance().addSpecifiedListenersForCorrelationChart(chartComposite, chart, isAvg, isDate,
                    bubbleQueryMap);
        } else if (isDate) {
            TOPChartUtils.getInstance().addSpecifiedListenersForCorrelationChart(chartComposite, chart, isAvg, isDate,
                    ganttQueryMap);
        }

    }

    /**
     * DOC bzhou Comment method "createChart".
     * 
     * @return
     */
    private Object createChart() {
        Object jchart = null;

        if (isCoungAvg) {
            jchart = createBubbleChart();
            TOPChartUtils.getInstance().decorateChart(jchart, false);
        } else {
            if (isMinMax) {
                final int indexOfDateCol = indicator.getDateColumns().indexOf(column);
                assert indexOfDateCol != -1;

                jchart = createGanttChart();

                TOPChartUtils.getInstance().decorateChart(jchart, false);
            }
        }

        return jchart;
    }

    /**
     * DOC yyin Comment method "createGanttChart".
     * 
     * @return
     */
    private Object createGanttChart() {
        final Map<String, DateValueAggregate> createGannttDatasets = DatasetUtils.createGanttDatasets(indicator, column);

        Object ganttDataset = TOPChartUtils.getInstance().createTaskSeriesCollection();
        final Iterator<String> iterator = createGannttDatasets.keySet().iterator();
        while (iterator.hasNext()) {
            final String next = iterator.next();
            createGannttDatasets.get(next).addSeriesToGanttDataset(ganttDataset, next);
        }

        ganttQueryMap = DatasetUtils.getGanttQueryMap(createGannttDatasets, indicator, column, analysis);

        String chartAxies = DefaultMessagesImpl.getString("TopChartFactory.chartAxies", column.getName()); //$NON-NLS-1$

        Object jchart = TOPChartUtils.getInstance().createGanttChart(chartAxies, ganttDataset);

        final int nbNominalColumns = indicator.getNominalColumns().size();
        final int nbDateFunctions = indicator.getDateFunctions().size();
        final int indexOfDateCol = indicator.getDateColumns().indexOf(column);

        List<Object[]> shownRows = new ArrayList<Object[]>();
        if (createGannttDatasets.size() != indicator.getListRows().size()) {// TDQ-6450 some null values
            final int firstDateColumnIdx = nbNominalColumns + nbDateFunctions * indexOfDateCol;
            for (Object[] row : indicator.getListRows()) {
                final Object minObj = row[firstDateColumnIdx];
                final Object maxobj = row[firstDateColumnIdx + 1];
                if (minObj == null || maxobj == null) {
                    continue;
                } else {
                    shownRows.add(row);
                }
            }
        } else {
            shownRows = indicator.getListRows();
        }// ~end TDQ-6450

        TOPChartUtils.getInstance().createAnnotOnGantt(jchart, shownRows,
                nbNominalColumns + nbDateFunctions * indexOfDateCol + 3, nbNominalColumns);
        return jchart;
    }

    /**
     * DOC yyin Comment method "createBubbleChart".
     * 
     * @return
     */
    private Object createBubbleChart() {
        final Map<String, ValueAggregator> createXYZDatasets = DatasetUtils.createXYZDatasets(indicator, column);

        Object dataset = TOPChartUtils.getInstance().createDefaultXYZDataset();
        final Iterator<String> iterator = createXYZDatasets.keySet().iterator();
        while (iterator.hasNext()) {
            final String next = iterator.next();
            createXYZDatasets.get(next).addSeriesToXYZDataset(dataset, next, TOPChartUtils.getInstance().getChartService());
        }

        bubbleQueryMap = DatasetUtils.getQueryMap(createXYZDatasets, indicator, column, analysis);

        String chartName = DefaultMessagesImpl.getString("TopChartFactory.ChartName", column.getName()); //$NON-NLS-1$

        return TOPChartUtils.getInstance().createBubbleChart(chartName, dataset, createXYZDatasets);
    }

    private void createUtilityControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.BORDER);
        comp.setLayout(new GridLayout());
        comp.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_END));
        comp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));

        SelectionAdapter listener = (SelectionAdapter) TOPChartUtils.getInstance().createSelectionAdapterForButton(chart,
                isCoungAvg, isMinMax);

        if (this.isCoungAvg) {
            int count = TOPChartUtils.getInstance().getSeriesCount(chart);

            for (int i = 0; i < count; i++) {

                Button checkBtn = new Button(comp, SWT.CHECK);
                checkBtn.setText(TOPChartUtils.getInstance().getSeriesKeyOfBubbleChart(chart, i));
                checkBtn.setSelection(true);
                checkBtn.addSelectionListener(listener);
                checkBtn.setData(SERIES_KEY_ID, i);
            }
        }

        if (isMinMax) {
            int count = TOPChartUtils.getInstance().getSeriesRowCount(chart);

            for (int i = 0; i < count; i++) {

                Button checkBtn = new Button(comp, SWT.CHECK);
                checkBtn.setText(TOPChartUtils.getInstance().getSeriestKeyOfGanttChart(chart, i));
                checkBtn.setSelection(true);
                checkBtn.addSelectionListener(listener);
                checkBtn.setData(SERIES_KEY_ID, i);
            }
        }

    }

    /**
     * DOC yyin Comment method "setLayoutData".
     * 
     * @param gd
     */
    public void setLayoutData(GridData gd) {
        ((Composite) this.chartComposite).setLayoutData(gd);

    }

}
