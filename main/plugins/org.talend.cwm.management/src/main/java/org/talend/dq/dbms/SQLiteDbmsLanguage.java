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

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.relational.TdColumn;
import org.talend.utils.ProductVersion;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class SQLiteDbmsLanguage extends DbmsLanguage {

    /**
     * DOC scorreia SQLiteDbmsLanguage constructor comment.
     */
    SQLiteDbmsLanguage() {
        super(DbmsLanguage.SQLITE3);
    }

    /**
     * DOC scorreia SQLiteDbmsLanguage constructor comment.
     *
     * @param dbmsType
     * @param majorVersion
     * @param minorVersion
     */
    SQLiteDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)
     */
    @Override
    public String getPatternFinderDefaultFunction(String expression) {
        // stack overflow issue in SQLite with this implementation: see
        // http://www.mail-archive.com/sqlite-users@sqlite.org/msg22286.html for a possible workaround
        return StringUtils.repeat("REPLACE(", 59) + expression //$NON-NLS-1$
                + ",'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A')" //$NON-NLS-1$
                + ",'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A')" //$NON-NLS-1$
                + ",'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A')" //$NON-NLS-1$
                + ",'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a')" //$NON-NLS-1$
                + ",'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a')" //$NON-NLS-1$
                + ",'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a')" //$NON-NLS-1$
                + ",'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a')" //$NON-NLS-1$
                + ",'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9')" + ",'7','9'),'8','9'),'0','9')"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    // ADD by msjian 2011-7-20 22517: no such function: CHAR_LENGTH for SQLite
    /**
     * Method "charLength".
     *
     * @param columnName
     * @return CHAR_LENGTH(columnName)
     */
    @Override
    public String charLength(String columnName) {
        return " LENGTH(" + columnName + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Count function will ignore null value if special name so that use * instead of the column name
     */
    @Override
    public String getAverageLengthRows() {
        String whereExpress =
                "WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL AND " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")";
        return "SELECT t.* FROM(SELECT "
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(* )*1.00)+0.99 as int) c,"
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(*)*1.00) as int) f "
                + "FROM <%=__TABLE_NAME__%> " + whereExpress + ") e, <%=__TABLE_NAME__%> t " + whereExpress
                + " AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN f AND c";
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithBlankRows()
     */
    @Override
    public String getAverageLengthWithBlankRows() {
        String sql = "SELECT t.* FROM(SELECT CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(*)*1.00)+0.99 as int) c,"
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(*)*1.00) as int) f "
                + "FROM <%=__TABLE_NAME__%> WHERE(<%=__COLUMN_NAMES__%> IS NOT NULL)) e, <%=__TABLE_NAME__%> t "
                + "WHERE <%=__COLUMN_NAMES__%> IS NOT NULL AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN f AND c";
        return sql;
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullRows()
     */
    @Override
    public String getAverageLengthWithNullRows() {
        String whereExpress = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")";
        String sql = "SELECT t.* FROM(SELECT "
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(* )*1.00)+0.99 as int) c,"
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(*)*1.00) as int) f "
                + "FROM <%=__TABLE_NAME__%> " + whereExpress + ") e, <%=__TABLE_NAME__%> t " + whereExpress
                + " AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN f AND c";
        return sql;
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullBlankRows()
     */
    @Override
    public String getAverageLengthWithNullBlankRows() {
        String sql = "SELECT t.* FROM(SELECT CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(*)*1.00)+0.99 as int) c,"
                + "CAST(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(*)*1.00) as int) f "
                + "FROM <%=__TABLE_NAME__%> ) e, <%=__TABLE_NAME__%> t "
                + "WHERE LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN f AND c";
        return sql;
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
     * @see org.talend.dq.dbms.DbmsLanguage#getSchemaNameFromContext(org.talend.core.model.metadata.builder.connection.
     * DatabaseConnection)
     */
    @Override
    public String getSchemaNameFromContext(DatabaseConnection dbConn) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getCatalogOrSchemaName(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    public String getCatalogOrSchemaName(TdColumn analyzedColumn) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getCatalog(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected Catalog getCatalog(ModelElement columnSetOwner) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getSchema(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected Schema getSchema(ModelElement columnSetOwner) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getRandomQuery(java.lang.String)
     */
    @Override
    public String getRandomQuery(String query) {
        return query + orderBy() + "RANDOM() "; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#createStatement(java.sql.Connection, int)
     */
    @Override
    public Statement createStatement(Connection connection, int fetchSize) throws SQLException {
        // TDQ-11124 "fetchSize" not support very well.it looks fetchSize as limitRows.see detail with
        // "org.sqlite.RS.setFetchSize(int rows)" and "org.sqlite.RS.next()".
        return super.createStatement(connection);
    }
}
