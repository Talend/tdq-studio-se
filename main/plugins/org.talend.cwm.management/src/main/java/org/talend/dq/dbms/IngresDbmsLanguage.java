// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.dbms;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.utils.ProductVersion;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class IngresDbmsLanguage extends DbmsLanguage {

    /**
     * DOC xqliu IngresDbmsLanguage constructor comment.
     */
    IngresDbmsLanguage() {
        super(DbmsLanguage.INGRES);
    }

    /**
     * DOC xqliu IngresDbmsLanguage constructor comment.
     *
     * @param dbmsType
     * @param dbVersion
     */
    IngresDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    @Override
    public String toQualifiedName(String catalog, String schema, String table) {
        return super.toQualifiedName(null, null, table);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#charLength(java.lang.String)
     */
    @Override
    public String charLength(String columnName) {
        return " LENGTH(" + columnName + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * Added yyin 20121214 TDQ-6571
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getTopNQuery(java.lang.String, int)
     */
    @Override
    public String getTopNQuery(String query, int n) {

        return query.replaceFirst("SELECT", "SELECT FIRST " + n); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getCatalogNameFromContext(org.talend.core.model.metadata.builder.connection.
     * DatabaseConnection)
     */
    @Override
    public String getCatalogNameFromContext(DatabaseConnection dbConn) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#createStatement(java.sql.Connection, int)
     */
    @Override
    public Statement createStatement(Connection connection, int fetchSize) throws SQLException {
        return createStatement(connection);
    }

}
