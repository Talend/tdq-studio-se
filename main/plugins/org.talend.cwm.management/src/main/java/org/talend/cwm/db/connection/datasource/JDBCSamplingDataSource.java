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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * created by zhao Sampling data source regarding jdbc connection. <br>
 * the parameter ResultSet should be closed by the caller who set it.
 *
 */
public class JDBCSamplingDataSource extends AbstractSamplingDataSource<ResultSet> {

    private static Logger log = Logger.getLogger(JDBCSamplingDataSource.class);

    private ResultSet jdbcResultSet = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.datascience.SamplingDataSource#setDataSource(java.lang.Object)
     */
    public void setDataSource(ResultSet rs) {
        jdbcResultSet = rs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.datascience.SamplingDataSource#getDatasize()
     */
    public boolean hasNext() throws Exception {
        try {
            if (jdbcResultSet == null) {
                return false;
            }
            return jdbcResultSet.next();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.datascience.SamplingDataSource#getRecord()
     */
    public Object[] getRecord() throws Exception {
        try {
            Object[] oneRow = new Object[columnSize];
            // --- for each column
            for (int i = 0; i < columnSize; i++) {
                // --- get content of column
                try {
                    oneRow[i] = jdbcResultSet.getObject(i + 1);
                } catch (SQLException e) {
                    if (NULLDATE.equals(jdbcResultSet.getString(i + 1))) {
                        oneRow[i] = null;
                    } else {
                        throw new Exception(e);
                    }
                }
            }
            return oneRow;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private static final String NULLDATE = "0000-00-00 00:00:00"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.sampling.SamplingDataSource#finalizeDataSampling()
     */
    public boolean finalizeDataSampling() throws Exception {
        if (jdbcResultSet != null) {
            Statement statement = jdbcResultSet.getStatement();
            Connection connection = statement.getConnection();

            jdbcResultSet.close();
            statement.close();
            connection.close();
        }
        return true;
    }

}
