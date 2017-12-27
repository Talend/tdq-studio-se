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

import java.io.IOException;

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.dataquality.sampling.exception.DQException;
import org.talend.dq.helper.FileUtils;

import com.talend.csv.CSVReader;

/**
 * File delimited data.
 *
 */
public class FileSamplingDataSource extends AbstractSamplingDataSource<DelimitedFileConnection> {

    private static Logger log = Logger.getLogger(FileSamplingDataSource.class);

    private CSVReader csvReader = null;

    private DelimitedFileConnection fileConnection = null;

    private int[] positions;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.datascience.SamplingDataSource#setDataSource(java.lang.Object)
     */
    public void setDataSource(DelimitedFileConnection conn) {
        fileConnection = conn;
        if (fileConnection != null) {
            try {
                int headValue = JavaSqlFactory.getHeadValue(fileConnection);
                csvReader = FileUtils.createCsvReader(fileConnection);
                FileUtils.initializeCsvReader(fileConnection, csvReader);
                if (headValue != 0) {
                    csvReader.readHeaders();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.datascience.SamplingDataSource#getDatasize()
     */
    public boolean hasNext() throws DQException {
        boolean hasNext = false;
        try {
            if (csvReader == null) {
                return false;
            }
            hasNext = csvReader.readNext();
            if (!hasNext) {
                csvReader.close();
                csvReader = null;
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new DQException(e);
        }
        return hasNext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.datascience.SamplingDataSource#getRecord()
     */
    public Object[] getRecord() {
        Object[] oneRow = new Object[positions.length];

        // --- for each column
        for (int i = 0; i < positions.length; i++) {
            // --- get content of column
            oneRow[i] = csvReader.get(positions[i]);
        }
        return oneRow;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.sampling.SamplingDataSource#finalizeDataSampling()
     */
    public boolean finalizeDataSampling() throws DQException {
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException e) {
                throw new DQException(e);
            }
        }
        return true;
    }

    public void setColumnPositions(int[] positions) {
        this.positions = positions;
    }

}
