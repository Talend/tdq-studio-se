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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;

import org.talend.dataquality.PluginConstant;
import org.talend.utils.ProductVersion;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class SybaseASEDbmsLanguage extends DbmsLanguage {

    /**
     * DOC scorreia SybaseASEDbmsLanguage constructor comment.
     */
    SybaseASEDbmsLanguage() {
        super(DbmsLanguage.SYBASE);
    }

    /**
     * DOC scorreia SybaseASEDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     * @param majorVersion
     * @param minorVersion
     */
    SybaseASEDbmsLanguage(ProductVersion dbVersion) {
        super(DbmsLanguage.SYBASE, dbVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#toQualifiedName(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String toQualifiedName(String catalog, String schema, String table) {

        return super.toQualifiedName(catalog, schema, table);
    }

    /**
     * DOC yyi 2011-08-10 22246:view rows for aveagge length
     * 
     * @return average length sql statement
     */
    @Override
    public String getAverageLengthRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE CHAR_LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEILING(SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#trim(java.lang.String)
     */
    @Override
    public String trim(String colName) {
        return " LTRIM(RTRIM(" + colName + ")) "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithBlankRows()
     */
    @Override
    public String getAverageLengthWithBlankRows() {
        String whereExpression = "WHERE <%=__COLUMN_NAMES__%> IS NOT NULL ";
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE " + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + " BETWEEN (SELECT FLOOR(SUM(" + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + ") / COUNT(*)) FROM <%=__TABLE_NAME__%> " + whereExpression + ") AND (SELECT CEILING(SUM(" + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + " ) / COUNT(* )) FROM <%=__TABLE_NAME__%> " + whereExpression + ")"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullBlankRows()
     */
    @Override
    public String getAverageLengthWithNullBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE " + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + " BETWEEN (SELECT FLOOR(SUM(" + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + ") / COUNT(*)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEILING(SUM(" + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + " ) / COUNT(* )) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullRows()
     */
    @Override
    public String getAverageLengthWithNullRows() {
        String whereExpression = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")";
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression + "AND " + charLength("<%=__COLUMN_NAMES__%>") + " BETWEEN (SELECT FLOOR(SUM(" + charLength("<%=__COLUMN_NAMES__%>") + ") / COUNT( * )) FROM <%=__TABLE_NAME__%> " + whereExpression + ") AND (SELECT CEILING(SUM(" + charLength("<%=__COLUMN_NAMES__%>") + ") / COUNT(*)) FROM <%=__TABLE_NAME__%>  " + whereExpression + ")"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getTopNQuery(java.lang.String, int)
     */
    @Override
    public String getTopNQuery(String query, int n) {

        Matcher m = SELECT_PATTERN.matcher(query);
        return m.replaceFirst("SELECT TOP " + n + PluginConstant.SPACE_STRING); //$NON-NLS-1$ 

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or left(convert(char(15)," + columnName + "),1) not " + this.like() + "'%[0-9]%'";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getColumnNameInQueryClause(java.lang.String)
     */
    @Override
    public String castColumnNameToChar(String columnName) {
        return "convert(char(15)," + columnName + ")";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getCatalog(orgomg.cwm.objectmodel.core.ModelElement)
     */
    @Override
    protected Catalog getCatalog(ModelElement columnSetOwner) {
        // get the schema first
        Schema schema = getSchema(columnSetOwner);
        // get the catalog according to the schema
        Catalog catalog = super.getCatalog(schema);
        return catalog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getQueryColumnSetWithPrefix(orgomg.cwm.resource.relational.ColumnSet)
     */
    @Override
    public String getQueryColumnSetWithPrefix(ColumnSet columnset) {
        String catalogName = getCatalog(columnset).getName();
        String schemaName = getSchema(columnset).getName();
        return getQualifiedColumnSetName(columnset, catalogName, schemaName);
    }

    @Override
    public Statement createStatement(Connection connection, int fetchSize) throws SQLException {
        // TDQ-12520 when using fetchSize on SybConnection,should specify these 2 default parameters "ResultSet.TYPE_FORWARD_ONLY,
        // ResultSet.CONCUR_READ_ONLY"
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.setFetchSize(fetchSize);
        return statement;
    }

}
