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
package org.talend.cwm.db.connection.datasource;

import org.talend.dataquality.sampling.SamplingDataSource;

/**
 * DOC talend class global comment. Detailled comment
 */
public abstract class AbstractSamplingDataSource<DataSource> implements SamplingDataSource<DataSource> {

    protected int columnSize = 0;

    /**
     * 
     * DOC zhao Set column size .
     * 
     * @param columnSize the size of the columns in a record.
     */
    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }
}
