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

import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC Zqin class global comment. Detailled comment
 * 
 * this interface supply one ability, which is, the dataset of chart can bring the specified entity, use these entities
 * to separate everyone of dataset.
 */
public interface ICustomerDataset {

    /**
     * DOC Zqin Comment method "addDataEntity".
     * 
     * @param dataEntity
     */
    public void addDataEntity(ChartDataEntity dataEntity);

    /**
     * DOC Zqin Comment method "addDataEntity".
     * 
     * @param dataEntity
     */
    public void addDataEntity(ChartDataEntity[] dataEntity);

    /**
     * DOC Zqin Comment method "getDataEntities".
     * 
     * @return all data entities.
     */
    public ChartDataEntity[] getDataEntities();
}
