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
package org.talend.cwm.db.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dq.helper.StoreOnDiskUtils;

/**
 * created by zhao on Oct 20, 2013 Detailled comment
 * 
 */
public abstract class SQLExecutor implements ISQLExecutor {

    private static Logger log = Logger.getLogger(SQLExecutor.class);

    private Boolean isStoreOnDisk = Boolean.FALSE;

    protected Object storeOnDiskHandler = null;

    public static final String STORE_ON_DISK_KEY = "STORE_ON_DISK"; //$NON-NLS-1$

    public static final String MAX_BUFFER_SIZE = "MAX_BUFFER_SIZE";//$NON-NLS-1$

    public static final String TEMP_DATA_DIR = "TEMP_DATA_DIR";//$NON-NLS-1$

    /**
     * the limit count number of rows.
     */
    private int limit = 0;

    private Boolean isShowRandomData = Boolean.FALSE;

    /**
     * List of rows obtained from data source.
     */
    private List<Object[]> dataFromTable = new ArrayList<Object[]>();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.ISQLExecutor#getLimit()
     */
    public int getLimit() {
        return this.limit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.ISQLExecutor#setLimit(int)
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.ISQLExecutor#isShowRandomData()
     */
    public Boolean isShowRandomData() {
        return this.isShowRandomData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.ISQLExecutor#setShowRandomData(java.lang.Boolean)
     */
    public void setShowRandomData(Boolean isShowRandomData) {
        this.isShowRandomData = isShowRandomData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.ISQLExecutor#initStoreOnDiskHandler(org.talend.dataquality.analysis.Analysis,
     * org.talend.dataquality.indicators.columnset.RecordMatchingIndicator, java.util.Map)
     */
    public void initStoreOnDiskHandler(Analysis analysis, RecordMatchingIndicator recordMatchingIndicator,
            Map<MetadataColumn, String> columnMap) {
        if (isStoreOnDisk()) {
            try {
                String tempDataPath = TaggedValueHelper.getValueString(TEMP_DATA_DIR, analysis);
                int bufferSize = Integer.valueOf(TaggedValueHelper.getValueString(MAX_BUFFER_SIZE, analysis));
                storeOnDiskHandler = StoreOnDiskUtils.getDefault().createStoreOnDiskHandler(tempDataPath, bufferSize,
                        recordMatchingIndicator, columnMap);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * The client can override the method to prepare an environment before query.
     */
    protected void beginQuery() throws Exception {
        if (isStoreOnDisk()) {
            StoreOnDiskUtils.getDefault().beginQuery(storeOnDiskHandler);
        }
    }

    /**
     * mzhao finalize the query, the client can override this method.
     */
    protected void endQuery() throws Exception {
        if (isStoreOnDisk()) {
            StoreOnDiskUtils.getDefault().endQuery(storeOnDiskHandler);
        }
    }

    protected void handleRow(Object[] oneRow) throws Exception {
        if (isStoreOnDisk()) {
            StoreOnDiskUtils.getDefault().handleRow(oneRow, storeOnDiskHandler);
        } else {
            dataFromTable.add(oneRow);
        }
    }

    /**
     * Sets the storeOnDisk.
     * 
     * @param storeOnDisk the storeOnDisk to set
     */
    public void setStoreOnDisk(Boolean storeOnDisk) {
        this.isStoreOnDisk = storeOnDisk;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.db.connection.ISQLExecutor#isStoreOnDisk()
     */
    public Boolean isStoreOnDisk() {
        return isStoreOnDisk;
    }

    /**
     * Getter for storeOnDiskHandler.
     * 
     * @return the storeOnDiskHandler
     */
    public Object getStoreOnDiskHandler() {
        return this.storeOnDiskHandler;
    }

    public List<Object[]> getDataFromTable() {
        return this.dataFromTable;
    }
}
