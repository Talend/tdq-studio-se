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

import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;
import org.talend.utils.properties.PropertiesLoader;

import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class PostgresqlDbmsLanguage extends DbmsLanguage {

    private static final Class<PostgresqlDbmsLanguage> THAT = PostgresqlDbmsLanguage.class;

    private static final String NUM = getProperties("POSTGRES_NUM", "1234567890"); //$NON-NLS-1$ //$NON-NLS-2$

    private static final String LOWER = getProperties("POSTGRES_LOWER", "abcdefghijklmnopqrstuvwxyz"); //$NON-NLS-1$ //$NON-NLS-2$

    private static final String UPPER = getProperties("POSTGRES_UPPER", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"); //$NON-NLS-1$ //$NON-NLS-2$

    private static String getProperties(String key, String defaultString) {
        return PropertiesLoader.getProperties(THAT, "characters.properties").getProperty(key, defaultString); //$NON-NLS-1$
    }

    /**
     * DOC scorreia PostgresqlDbmsLanguage constructor comment.
     */
    PostgresqlDbmsLanguage() {
        super(DbmsLanguage.POSTGRESQL);
    }

    PostgresqlDbmsLanguage(String dbmsType) {
        super(dbmsType);
    }

    /**
     * DOC scorreia PostgresqlDbmsLanguage constructor comment.
     *
     * @param dbmsType
     * @param majorVersion
     * @param minorVersion
     */
    PostgresqlDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#toQualifiedName(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String toQualifiedName(String catalog, String schema, String table) {
        // do not use Catalog (ZQL Parser does not know how to handle qualified names such as 'c.s.t'
        return super.toQualifiedName(null, schema, table);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)
     */
    @Override
    public String getPatternFinderDefaultFunction(String expression) {
        return "TRANSLATE(" + expression + " ,'" + NUM + UPPER + LOWER + "' " + ",RPAD('9'," + NUM.length() //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + ",'9') || RPAD('A'," + UPPER.length() + ",'A')||RPAD('a'," + LOWER.length() + ",'a'))"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    @Override
    protected String getPatternFinderFunction(String expression, String charsToReplace, String replacementChars) {
        assert charsToReplace != null && replacementChars != null && charsToReplace.length() == replacementChars.length();
        return translateUsingPattern(expression, charsToReplace, replacementChars);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain,
     * java.lang.String)
     */
    @Override
    protected String extract(DateGrain dateGrain, String colName) {
        return " CAST( EXTRACT(" + dateGrain + from() + colName + ") AS INTEGER )"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getSelectRegexp(java.lang.String)
     */
    @Override
    protected String getSelectRegexp(String regexLikeExpression) {
        return "SELECT " + regexLikeExpression + " AS OK" + EOS; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#regexLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexLike(String element, String regex) {
        return surroundWithSpaces(element + surroundWithSpaces(getRegularExpressionFunction()) + regex);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexNotLike(String element, String regex) {
        return surroundWithSpaces(element + surroundWithSpaces("!" + getRegularExpressionFunction()) + regex); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getRegularExpressionFunction()
     */
    @Override
    public String getRegularExpressionFunction() {
        return "~"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getRegularExpressionFunction()
     */
    @Override
    public String extractRegularExpressionFunction(Expression expression, String regex) {
        return getRegularExpressionFunction();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getQuoteIdentifier()
     */
    @Override
    public String getHardCodedQuoteIdentifier() {
        return "\""; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#supportNonIntegerConstantInGroupBy()
     */
    @Override
    public boolean supportNonIntegerConstantInGroupBy() {
        return false;
    }

    @Override
    public boolean supportRegexp() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or SUBSTRING(" + columnName + ", 1,1)  ~ '[^0-9]'";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getColumnNameInQueryClause(java.lang.String)
     */
    @Override
    public String castColumnNameToChar(String columnName) {
        return "cast(" + columnName + " as char)";//$NON-NLS-1$ //$NON-NLS-2$
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
     * @see org.talend.dq.dbms.DbmsLanguage#getRandomQuery(java.lang.String)
     */
    @Override
    public String getRandomQuery(String query) {
        return query + orderBy() + "RANDOM() "; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#createStatementForBigdata(java.sql.Connection)
     */
    @Override
    public Statement createStatement(Connection connection, int fetchSize) throws SQLException {
        // fetchSize is effective only when not auto commit mode
        connection.setAutoCommit(false);
        return super.createStatement(connection, fetchSize);
    }

    @Override
    public String getAverageLengthWithBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    @Override
    public String getAverageLengthRows() {
        String whereExpression = "WHERE (TRIM(<%=__COLUMN_NAMES__%>) <> '' ) "; //$NON-NLS-1$
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression //$NON-NLS-1$
                + " AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>)" //$NON-NLS-1$
                + ")*1.0 / COUNT(*)) FROM <%=__TABLE_NAME__%> " + whereExpression
                + ") AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%> ))*1.0 / COUNT(* )) FROM <%=__TABLE_NAME__%> "
                + whereExpression + ")";
    }

    @Override
    public String getAverageLengthWithNullBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(*)) FROM <%=__TABLE_NAME__%>)"
                + " AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(* )) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    @Override
    public String getAverageLengthWithNullRows() {
        String whereExpression = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression
                + "AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%> ))*1.0 / COUNT( * )) FROM <%=__TABLE_NAME__%> "
                + whereExpression
                + ") AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%> ))*1.0 / COUNT(*)) FROM <%=__TABLE_NAME__%>  " //$NON-NLS-1$
                + whereExpression + ")"; //$NON-NLS-1$ 
    }

}
