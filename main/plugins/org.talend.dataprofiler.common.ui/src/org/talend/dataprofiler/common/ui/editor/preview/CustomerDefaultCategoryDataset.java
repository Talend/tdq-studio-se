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
package org.talend.dataprofiler.common.ui.editor.preview;

import java.util.ArrayList;
import java.util.List;

import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class CustomerDefaultCategoryDataset implements ICustomerDataset {

    private Object dataset;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<ChartDataEntity> dataEnities;

    public CustomerDefaultCategoryDataset() {
        dataEnities = new ArrayList<ChartDataEntity>();
        dataset = TOPChartUtil.getInstance().createDefaultCategoryDataset();
    }

    @Override
    public void addDataEntity(ChartDataEntity dataEntity) {
        dataEnities.add(dataEntity);
    }

    @Override
    public void addDataEntity(ChartDataEntity[] dataEntity) {
        for (ChartDataEntity data : dataEntity) {
            dataEnities.add(data);
        }
    }

    /**
     * add the value to the dataset
     * 
     * @param value
     * @param labelx
     * @param labely
     */
    public void addValue(double value, String labelx, String labely) {
        TOPChartUtil.getInstance().addValueToCategoryDataset(dataset, value, labelx, labely);
    }

    @Override
    public ChartDataEntity[] getDataEntities() {
        return dataEnities.toArray(new ChartDataEntity[dataEnities.size()]);
    }

    public Object getDataset() {
        return dataset;
    }

    // when clear the dataset, also need to clear data entities
    public void clearAll() {
        // super.clear();
        dataEnities.clear();
        // clear the dataset
        TOPChartUtil.getInstance().clearDataset(dataset);
    }

    public void clearDataEnities() {
        dataEnities.clear();
    }
}
