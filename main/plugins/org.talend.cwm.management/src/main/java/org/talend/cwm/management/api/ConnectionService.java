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
package org.talend.cwm.management.api;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.dq.analysis.parameters.DBConnectionParameter;
import org.talend.dq.helper.ParameterUtil;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * @author zshen
 * 
 * @deprecated don't should use this class to test please use MetadataFillFactory.getDBInstance().fillUIConnParams()
 * instead of it.
 * 
 */
@Deprecated
public class ConnectionService {

    private static Logger log = Logger.getLogger(ConnectionService.class);

    public static TypedReturnCode<Connection> createConnection(DBConnectionParameter parameter) {
        TypedReturnCode<Connection> tReturnCode = new TypedReturnCode<Connection>(false);
        MetadataFillFactory instance = null;
        instance = MetadataFillFactory.getDBInstance();

        IMetadataConnection metaConnection = instance.fillUIParams(ParameterUtil.toMap(parameter));
        ReturnCode rc = instance.createConnection(metaConnection);
        if (rc.isOk()) {
            Connection dbConn = instance.fillUIConnParams(metaConnection, null);
            DatabaseMetaData dbMetadata = null;
            List<String> packageFilter = ConnectionUtils.getPackageFilter(parameter);
            java.sql.Connection sqlConn = null;
            try {
                if (rc instanceof TypedReturnCode) {
                    @SuppressWarnings("rawtypes")
                    Object sqlConnObject = ((TypedReturnCode) rc).getObject();
                    if (sqlConnObject instanceof java.sql.Connection) {
                        sqlConn = (java.sql.Connection) sqlConnObject;
                        dbMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(sqlConn);
                    }
                }

                instance.fillCatalogs(dbConn, dbMetadata, packageFilter);
                instance.fillSchemas(dbConn, dbMetadata, packageFilter);

                tReturnCode.setObject(dbConn);
            } catch (SQLException e) {
                log.error(e, e);
                // Need to add a dialog for report the reson of error
            } finally {
                if (sqlConn != null) {
                    ConnectionUtils.closeConnection(sqlConn);
                }

            }
        } else {
            tReturnCode.setMessage(rc.getMessage());
            tReturnCode.setOk(false);
        }
        return tReturnCode;
    }
}
