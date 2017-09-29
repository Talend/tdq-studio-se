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
package org.talend.dataprofiler.core.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ResultSetHelper {

    public static ResultSet getResultSet(MetadataTable metadataTable, String whereExpression) throws SQLException {
        return getResultSet(metadataTable, whereExpression, 0);
    }

    public static ResultSet getResultSet(MetadataTable metadataTable, String whereExpression, int maxRows) throws SQLException {
        return getResultSet(metadataTable, null, whereExpression, maxRows);
    }

    public static ResultSet getResultSet(MetadataTable metadataTable, java.sql.Connection sqlConn, String whereExpression,
            int maxRows) throws SQLException {
        Connection tdDataProvider = ConnectionHelper.getTdDataProvider(metadataTable);
        if (sqlConn == null) {
            IMetadataConnection metadataBean = ConvertionHelper.convert(tdDataProvider);
            TypedReturnCode<java.sql.Connection> createConnection = MetadataConnectionUtils.createConnection(metadataBean, false);
            if (!createConnection.isOk()) {
                return null;
            }
            sqlConn = createConnection.getObject();
        }

        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(tdDataProvider);
        Statement createStatement = null;
        if (maxRows != 0) {// TOPN algorithm, it has row limited,no need the fetch size.
            createStatement = dbmsLanguage.createStatement(sqlConn);
        } else {// Resevoir Sample algorithm
            createStatement = dbmsLanguage.createStatement(sqlConn, 1000);
        }
        createStatement.setMaxRows(maxRows);

        Expression columnQueryExpression = dbmsLanguage.getTableQueryExpression(metadataTable, whereExpression);
        return createStatement.executeQuery(columnQueryExpression.getBody());
    }
}
