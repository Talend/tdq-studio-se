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

import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class CustomerDefaultBAWDataset implements ICustomerDataset {

    private Object bawDataset;

    private Object defaultCategoryDataset;

    private static final long serialVersionUID = 1L;

    private List<ChartDataEntity> dataEnities;

    public CustomerDefaultBAWDataset(Double mean, Double median, Double q1, Double q3, Double minRegularValue,
            Double maxRegularValue) {
        dataEnities = new ArrayList<ChartDataEntity>();
        bawDataset = TOPChartUtils.getInstance().createDefaultBoxAndWhiskerCategoryDataset(mean, median, q1, q3, minRegularValue,
                maxRegularValue);
    }

    /**
     * 
     * DOC zshen CustomerDefaultBAWDataset constructor comment.
     * 
     * @param mean
     * @param median
     * @param q1
     * @param q3
     * @param minRegularValue
     * @param maxRegularValue
     * @param defaultCategoryDataset should be DefaultCategoryDataset type
     */
    public CustomerDefaultBAWDataset(Double mean, Double median, Double q1, Double q3, Double minRegularValue,
            Double maxRegularValue, Object defaultCategoryDataset) {
        this(mean, median, q1, q3, minRegularValue, maxRegularValue);
        this.defaultCategoryDataset = defaultCategoryDataset;
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

    public int getRowCount() {
        return TOPChartUtils.getInstance().getRowCount(bawDataset);
    }

    public Object getDataset() {
        return this.bawDataset;
    }

    /**
     * Getter for defaultCategoryDataset.
     * 
     * @return the defaultCategoryDataset
     */
    public Object getDefaultCategoryDataset() {
        return this.defaultCategoryDataset;
    }

    public void clear() {
        dataEnities.clear();
        TOPChartUtils.getInstance().clearDefaultBoxAndWhiskerCategoryDataset(bawDataset);
    }
}
