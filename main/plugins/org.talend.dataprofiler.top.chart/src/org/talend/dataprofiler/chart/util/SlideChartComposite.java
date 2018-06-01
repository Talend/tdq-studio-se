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
package org.talend.dataprofiler.chart.util;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;


/**
 * There is a vertical scroll bar in the chart for Frequency indicator.
 */
public class SlideChartComposite extends TalendChartComposite {

    private SlidingCategoryDataset slidDataSet = null;

    private JFreeChart chart = null;
    /**
     * @param comp
     * @param style
     * @param chart
     * @param useBuffer
     */
    public SlideChartComposite(Composite comp, int style, JFreeChart chart, boolean useBuffer) {
        super(comp, style, chart, useBuffer);
        this.chart = chart;
        setBarProperty();

    }

    private void setBarProperty() {
        ScrollBar verticalBar = this.getVerticalBar();
        CategoryDataset dataset = chart.getCategoryPlot().getDataset();
        if (dataset instanceof SlidingCategoryDataset) {
            slidDataSet = (SlidingCategoryDataset) dataset;
            int underlingCount = slidDataSet.getUnderlyingDataset().getColumnCount();
            verticalBar.setMaximum(underlingCount);
            if (underlingCount <= slidDataSet.getMaximumCategoryCount()) {
                verticalBar.setVisible(false);
            }
            verticalBar.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    int selection = verticalBar.getSelection();
                    if (selection < underlingCount) {
                        slidDataSet.setFirstCategoryIndex(selection);
                    }
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    widgetSelected(e);
                }

            });
        }
    }

}
