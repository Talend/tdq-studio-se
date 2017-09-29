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
package org.talend.dataprofiler.core.model.dynamic;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.preview.EIndicatorChartType;

/**
 * created by yyin on 2014-7-3 Detailled comment
 * 
 */
public class DynamicIndicatorModel {

    // the list of indicators which belongs to same category
    private List<Indicator> indicatorList = null;

    private List<Indicator> summaryIndicators = null;

    private EIndicatorChartType chartType = null;

    // the dataset of the chart
    private Object dataset = null;

    // for benford law indicator
    private Object secondDataset = null;

    // the table of this category of indicators in the result page only
    private TableViewer tableViewer = null;

    // the parent composite for the chart of the summary indicators: BAW chart
    private Object bawParentChartComp = null; // TalendChartComposite

    /**
     * Getter for indicatorList.
     * 
     * @return the indicatorList
     */
    public List<Indicator> getIndicatorList() {
        return this.indicatorList;
    }

    /**
     * Sets the indicatorList.
     * 
     * @param indicatorList the indicatorList to set
     */
    public void setIndicatorList(List<Indicator> indicatorList) {
        this.indicatorList = indicatorList;
    }

    /**
     * Getter for summaryIndicators.
     * 
     * @return the summaryIndicators
     */
    public List<Indicator> getSummaryIndicators() {
        return this.summaryIndicators;
    }

    /**
     * Sets the summaryIndicators.
     * 
     * @param summaryIndicators the summaryIndicators to set
     */
    public void setSummaryIndicators(List<Indicator> summaryIndicators) {
        this.summaryIndicators = summaryIndicators;
    }

    /**
     * Getter for chartType.
     * 
     * @return the chartType
     */
    public EIndicatorChartType getChartType() {
        return this.chartType;
    }

    /**
     * Sets the chartType.
     * 
     * @param chartType the chartType to set
     */
    public void setChartType(EIndicatorChartType chartType) {
        this.chartType = chartType;
    }

    /**
     * Getter for dataset.
     * 
     * @return the dataset
     */
    public Object getDataset() {
        return this.dataset;
    }

    /**
     * Sets the dataset.
     * 
     * @param dataset the dataset to set
     */
    public void setDataset(Object dataset) {
        this.dataset = dataset;
    }

    /**
     * Getter for tableViewer.
     * 
     * @return the tableViewer
     */
    public TableViewer getTableViewer() {
        return this.tableViewer;
    }

    /**
     * Sets the tableViewer.
     * 
     * @param tableViewer the tableViewer to set
     */
    public void setTableViewer(TableViewer tableViewer) {
        this.tableViewer = tableViewer;
    }

    /**
     * Getter for bawParentChartComp.
     * 
     * @return the bawParentChartComp
     */
    public Object getBawParentChartComp() {
        return this.bawParentChartComp;
    }

    /**
     * Sets the bawParentChartComp.
     * 
     * @param bawParentChartComp the bawParentChartComp to set
     */
    public void setBawParentChartComp(Object bawParentChartComp) {
        this.bawParentChartComp = bawParentChartComp;
    }

    public void clear() {
        this.dataset = null;
        this.indicatorList = null;
        this.tableViewer = null;
        this.bawParentChartComp = null;
        this.summaryIndicators = null;
        this.secondDataset = null;

    }

    /**
     * Getter for secondDataset.
     * 
     * @return the secondDataset
     */
    public Object getSecondDataset() {
        return secondDataset;
    }

    /**
     * Sets the secondDataset.
     * 
     * @param secondDataset the secondDataset to set
     */
    public void setSecondDataset(Object secondDataset) {
        this.secondDataset = secondDataset;
    }
}
