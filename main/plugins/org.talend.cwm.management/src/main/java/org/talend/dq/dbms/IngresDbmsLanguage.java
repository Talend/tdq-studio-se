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

    /**
     * DOC yyi 2011-07-07 22246:view rows for average length for Oracle
     * 
     * @return average length sql statement
     */
    @Override
    public String getAverageLengthRows() {
        return "SELECT t.* FROM(" + "SELECT "
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(<%=__COLUMN_NAMES__%>)*1.00)+0.99 as int) c, "
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(<%=__COLUMN_NAMES__%>)*1.00) as int) f "
                + "FROM <%=__TABLE_NAME__%>) e, <%=__TABLE_NAME__%> t " + "where LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN f AND c";
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithBlankRows()
     */
    @Override
    public String getAverageLengthWithBlankRows() {
        String sql = "SELECT t.* FROM(SELECT CAST(SUM(LENGTH(" + trimIfBlank("<%=__COLUMN_NAMES__%>")
                + ")) / (COUNT(<%=__COLUMN_NAMES__%> )*1.00)+0.99 as int) c," + "CAST(SUM(LENGTH("
                + trimIfBlank("<%=__COLUMN_NAMES__%>") + ")) / (COUNT(<%=__COLUMN_NAMES__%>)*1.00) as int) f "
                + "FROM <%=__TABLE_NAME__%> WHERE(<%=__COLUMN_NAMES__%> IS NOT NULL)) e, <%=__TABLE_NAME__%> t "
                + "WHERE LENGTH(" + trimIfBlank("<%=__COLUMN_NAMES__%>") + ") BETWEEN f AND c";
        return sql;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullRows()
     */
    @Override
    public String getAverageLengthWithNullRows() {
        String whereExp = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")";
        String sql = "SELECT t.* FROM(SELECT "
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(<%=__COLUMN_NAMES__%> )*1.00)+0.99 as int) c,"
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(<%=__COLUMN_NAMES__%>)*1.00) as int) f "
                + "FROM <%=__TABLE_NAME__%> " + whereExp + ") e, <%=__TABLE_NAME__%> t " + whereExp
                + "AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN f AND c";
        return sql;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullBlankRows()
     */
    @Override
    public String getAverageLengthWithNullBlankRows() {
        String sql = "SELECT t.* FROM(SELECT CAST(SUM(LENGTH(" + trimIfBlank("<%=__COLUMN_NAMES__%>")
                + ")) / (COUNT(*)*1.00)+0.99 as int) c," + "CAST(SUM(LENGTH(" + trimIfBlank("<%=__COLUMN_NAMES__%>")
                + ")) / (COUNT(*)*1.00) as int) f " + "FROM <%=__TABLE_NAME__%> ) e, <%=__TABLE_NAME__%> t " + "WHERE LENGTH("
                + trimIfBlank("<%=__COLUMN_NAMES__%>") + ") BETWEEN f AND c";
        return sql;
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
