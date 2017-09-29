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
package org.talend.dataprofiler.core.ui.editor.preview.model;

import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * TODO: delete the chart. DOC zqin class global comment. Detailled comment
 */
public class TableWithData {

    private ChartDataEntity[] entity;

    private EIndicatorChartType chartType;

    public TableWithData(EIndicatorChartType chartType, ChartDataEntity[] entity) {
        this.chartType = chartType;
        this.entity = entity;
    }

    public ChartDataEntity[] getEnity() {
        return entity;
    }

    public EIndicatorChartType getChartType() {
        return chartType;
    }

    public void setEntities(ChartDataEntity[] entity) {
        this.entity = entity;
    }
}
