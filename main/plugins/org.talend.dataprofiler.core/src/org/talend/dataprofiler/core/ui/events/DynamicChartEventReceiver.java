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
package org.talend.dataprofiler.core.ui.events;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.ModeIndicator;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.utils.format.StringFormatUtil;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class DynamicChartEventReceiver extends EventReceiver {

    protected static final String NAN_STRING = String.valueOf(Double.NaN);

    protected Object dataset;

    protected Indicator indicator;

    private int entityIndex;

    protected Object registerChart;

    protected String indicatorName;

    // mainly used for the summary indicators
    private IndicatorEnum indicatorType = null;

    protected TableViewer tableViewer = null;

    protected Composite chartComposite;

    protected Composite parentChartComposite;

    public int getIndexInDataset() {
        return this.entityIndex;
    }

    public void setIndexInDataset(int row) {
        this.entityIndex = row;
    }

    // only Frequency indicator need to remember itself
    public void setIndicator(Indicator indicator) {
        if (indicator instanceof FrequencyIndicator || UDIHelper.isFrequency(indicator)) {
            this.indicator = indicator;
        }
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
     * Getter for entityIndex.
     * 
     * @return the entityIndex
     */
    public int getEntityIndex() {
        return this.entityIndex;
    }

    /**
     * Sets the entityIndex.
     * 
     * @param entityIndex the entityIndex to set
     */
    public void setEntityIndex(int entityIndex) {
        this.entityIndex = entityIndex;
    }

    /**
     * Getter for indicator.
     * 
     * @return the indicator
     */
    public Indicator getIndicator() {
        return this.indicator;
    }

    @Override
    public boolean handle(Object value) {
        Object indValue = value;
        if (value == null) {
            indValue = 0;
        }
        if (dataset != null) {
            settingDatasetValue(indValue);
        }
        if (tableViewer != null) {
            refreshTable(value == null ? NAN_STRING : String.valueOf(indValue));
        }
        // need to refresh the parent composite of the chart to show the changes
        EventManager.getInstance().publish(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART, null);

        return true;
    }

    /**
     * Set dataset value by input parameter.
     */
    protected void settingDatasetValue(Object indValue) {
        if (indValue instanceof Number) {
            TOPChartUtils.getInstance().setValue(dataset, (Number) indValue, getRowKey(), getRowKey());
        } else if (indValue instanceof String) {
            if (!(indicator instanceof ModeIndicator)) {
                TOPChartUtils.getInstance()
                        .setValue(dataset, Double.parseDouble((String) indValue), getRowKey(), getRowKey());
            }
        } else {
            TOPChartUtils.getInstance().setValue(dataset,
                    (Number) StringFormatUtil.format(indValue, StringFormatUtil.DOUBLE_NUMBER), getRowKey(), getRowKey());
        }

    }

    public void clearValue() {
        if (dataset != null) {
            clearDataset();
        }
        if (tableViewer != null) {
            refreshTable(NAN_STRING);
        }
    }

    /**
     * Reset value of special rowKey and columnKey 
     */
    protected void clearDataset() {
        TOPChartUtils.getInstance().setValue(dataset, 0.0, getRowKey(), getColumnKey());
    }

    /**
     * Get RowKey current it is the name of indicator sub class maybe override it.
     * 
     * @return
     */
    protected String getRowKey() {
        return getIndicatorName();
    }

    /**
     * Get ColumnKey current it is the name of indicator sub class maybe override it.
     * 
     * @return
     */
    protected String getColumnKey() {
        return getIndicatorName();
    }

    // frequency and summary need this method
    public void refreshChart() {
        // no need to implements
    }

    public void refreshTable(String value) {
        TableWithData input = (TableWithData) tableViewer.getInput();
        if (input != null) {
            ChartDataEntity[] dataEntities = input.getEnity();
            if (dataEntities != null && dataEntities.length > entityIndex) {
                dataEntities[entityIndex].setValue(value);
                dataEntities[entityIndex].setPercent(Double.NaN);
                tableViewer.getTable().clearAll();
                tableViewer.setInput(input);
            }
        }
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public Composite getChartComposite() {
        return chartComposite;
    }

    public void setChartComposite(Composite chartComposite) {
        this.chartComposite = chartComposite;
    }

    public IndicatorEnum getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorEnum indicatorType) {
        this.indicatorType = indicatorType;
    }

    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public void setTableViewer(TableViewer tableViewer) {
        this.tableViewer = tableViewer;
    }

    public void clear() {
        this.chartComposite = null;
        this.dataset = null;
        this.tableViewer = null;
        this.indicatorType = null;
        this.indicator = null;
    }

    /**
     * Sets the parentChartComposite.
     * 
     * @param parentChartComposite the parentChartComposite to set
     */
    public void setParentChartComposite(Composite parentChartComposite) {
        this.parentChartComposite = parentChartComposite;
        registerChart = TOPChartUtils.getInstance().getChartFromChartComposite(parentChartComposite);
    }

}
