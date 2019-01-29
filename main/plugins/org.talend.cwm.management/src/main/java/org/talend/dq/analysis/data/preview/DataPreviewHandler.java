// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis.data.preview;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.commons.i18n.internal.DefaultMessagesImpl;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.db.connection.DatabaseSQLExecutor;
import org.talend.cwm.db.connection.DelimitedFileSQLExecutor;
import org.talend.cwm.db.connection.ISQLExecutor;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdColumn;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class DataPreviewHandler {

    private DataManager connection = null;

    final static Logger log = Logger.getLogger(DataPreviewHandler.class);

    private String dataFilter = StringUtils.EMPTY;

    public List<Object[]> createPreviewData(ModelElement[] columns, int limit, boolean isRandomData) throws SQLException {
        // no columns be selected so that no data can be read
        if (columns == null || columns.length == 0) {
            return new ArrayList<Object[]>();
        }

        // use ModelElement instead of node to get the data source type directly.
        // get connection from column[0]

        boolean isDelimitedFile = false;
        ModelElement modelElement = columns[0];
        if (modelElement instanceof MetadataColumn && !(modelElement instanceof TdColumn)) {
            isDelimitedFile = true;
            connection = ConnectionHelper.getTdDataProvider((MetadataColumn) modelElement);
        } else if (modelElement instanceof TdColumn) {
            connection = ConnectionHelper.getTdDataProvider((TdColumn) modelElement);
        } else {// other case it is not support by now
            log.warn(DefaultMessagesImpl.getString("DataPreviewHandler.UnSupportType")); //$NON-NLS-1$
            return new ArrayList<Object[]>();
        }

        ISQLExecutor sqlExecutor = null;
        if (isDelimitedFile) {
            sqlExecutor = new DelimitedFileSQLExecutor();
        } else {// is database
            sqlExecutor = new DatabaseSQLExecutor();
        }
        // set limit
        sqlExecutor.setLimit(limit);
        sqlExecutor.setShowRandomData(isRandomData);
        return sqlExecutor.executeQuery(connection, Arrays.asList(columns), dataFilter);
    }

    /**
     * Sets the dataFilter.
     * 
     * @param dataFilter the dataFilter to set
     */
    public void setDataFilter(String dataFilter) {
        this.dataFilter = dataFilter;
    }

    public DataManager getConnection() {
        return connection;
    }

}
