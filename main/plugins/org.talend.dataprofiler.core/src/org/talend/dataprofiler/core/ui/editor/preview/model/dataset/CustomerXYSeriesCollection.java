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
package org.talend.dataprofiler.core.ui.editor.preview.model.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class CustomerXYSeriesCollection implements ICustomerDataset {

    // XYSeriesCollection
    private Object xySeries;

    private List<ChartDataEntity> dataEnities;

    public CustomerXYSeriesCollection(Map<Integer, Double> valueMap) {
        dataEnities = new ArrayList<ChartDataEntity>();
        xySeries = TOPChartUtils.getInstance().createXYDataset(valueMap);
    }

    public Object getDataset() {
        return xySeries;
    }

    public void addDataEntity(ChartDataEntity dataEntity) {
        dataEnities.add(dataEntity);
    }

    public void addDataEntity(ChartDataEntity[] dataEntity) {
        for (ChartDataEntity data : dataEntity) {
            dataEnities.add(data);
        }
    }

    public ChartDataEntity[] getDataEntities() {
        return dataEnities.toArray(new ChartDataEntity[dataEnities.size()]);
    }

}
