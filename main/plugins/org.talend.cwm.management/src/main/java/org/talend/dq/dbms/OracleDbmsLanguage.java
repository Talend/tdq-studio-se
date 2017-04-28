// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.dataquality.domain.sql.SqlPredicate;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;
import org.talend.utils.properties.PropertiesLoader;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class OracleDbmsLanguage extends DbmsLanguage {

    private static final Class<OracleDbmsLanguage> THAT = OracleDbmsLanguage.class;

    private static final String NUM = getProperties("ORACLE_NUM", "1234567890"); //$NON-NLS-1$ //$NON-NLS-2$

    private static final String LOWER = getProperties("ORACLE_LOWER", "abcdefghijklmnopqrstuvwxyz"); //$NON-NLS-1$ //$NON-NLS-2$

    private static final String UPPER = getProperties("ORACLE_UPPER", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"); //$NON-NLS-1$ //$NON-NLS-2$

    private static String getProperties(String key, String defaultString) {
        return PropertiesLoader.getProperties(THAT, "characters.properties").getProperty(key, defaultString); //$NON-NLS-1$
    }

    /**
     * DOC scorreia OracleDbmsLanguage constructor comment.
     */
    OracleDbmsLanguage() {
        super(DbmsLanguage.ORACLE);
    }

    /**
     * DOC scorreia OracleDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     * @param majorVersion
     * @param minorVersion
     */
    OracleDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#notEqual()
     */
    @Override
    public String notEqual() {
        // "!=" seem to be more performant on Oracle than "<>". See
        // http://www.freelists.org/archives/oracle-l/09-2006/msg01005.html
        return surroundWithSpaces(SqlPredicate.NOT_EQUAL2.getLiteral());
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
     * @see org.talend.cwm.management.api.DbmsLanguage#replaceNullsWithString(java.lang.String, java.lang.String)
     */
    @Override
    public String replaceNullsWithString(String colName, String replacement) {
        if ("''".equals(replacement)) { //$NON-NLS-1$
            // MOD qiongli 2011-8-8 TDQ-2474.
            return super.replaceNullsWithString(colName, replacement);
        }
        return " NVL(" + colName + "," + replacement + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#isNotBlank(java.lang.String)
     */
    @Override
    public String isNotBlank(String colName) {
        // oracle does not currently distinguish between blank and null
        return trim(colName) + isNotNull();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getTopNQuery(java.lang.String, int)
     */
    @Override
    public String getTopNQuery(String query, int n) {
        return "SELECT * FROM (" + query + ") WHERE ROWNUM <= " + n; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#countRowInSubquery(java.lang.String, java.lang.String)
     */
    @Override
    public String countRowInSubquery(String subquery, String alias) {
        // does not support "AS"
        return " SELECT COUNT(*) FROM (" + subquery + ") " + alias; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#sumRowInSubquery(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String sumRowInSubquery(String colToSum, String subquery, String alias) {
        // does not support "AS"
        return " SELECT SUM(" + colToSum + ") FROM (" + subquery + ") " + alias; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain,
     * java.lang.String)
     */
    @Override
    protected String extract(DateGrain dateGrain, String colName) {
        String toNumberToChar = "TO_NUMBER(TO_CHAR("; //$NON-NLS-1$
        switch (dateGrain.getValue()) {
        case DateGrain.DAY_VALUE:
            return toNumberToChar + colName + ", 'DD'))"; //$NON-NLS-1$
        case DateGrain.WEEK_VALUE:
            return toNumberToChar + colName + ", 'IW'))"; //$NON-NLS-1$
        case DateGrain.MONTH_VALUE:
            return toNumberToChar + colName + ",'MM'))"; //$NON-NLS-1$
        case DateGrain.QUARTER_VALUE:
            return toNumberToChar + colName + ",'Q'))"; //$NON-NLS-1$
        case DateGrain.YEAR_VALUE:
            return toNumberToChar + colName + ", 'YYYY'))"; //$NON-NLS-1$
        default:
            return super.extract(dateGrain, colName);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getRegularExpressionFunction()
     */
    @Override
    public String getRegularExpressionFunction() {
        return "REGEXP_LIKE"; //$NON-NLS-1$
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
     * @see org.talend.cwm.management.api.DbmsLanguage#regexLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexLike(String element, String regex) {
        return surroundWithSpaces(getRegularExpressionFunction() + "(" + element + " , " + regex + " )"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexNotLike(String element, String regex) {
        return surroundWithSpaces(this.not() + getRegularExpressionFunction() + "(" + element + " , " + regex + " )"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
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
     * @see org.talend.cwm.management.api.DbmsLanguage#getSelectRemarkOnTable(java.lang.String)
     */
    @Override
    public String getSelectRemarkOnTable(String tableName) {
        return "SELECT COMMENTS FROM USER_TAB_COMMENTS WHERE TABLE_NAME='" + tableName + "'"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getSelectRemarkOnColumn(java.lang.String)
     */
    @Override
    public String getSelectRemarkOnColumn(String columnName) {
        return "SELECT COMMENTS FROM USER_COL_COMMENTS WHERE COLUMN_NAME='" + columnName + "'"; //$NON-NLS-1$ //$NON-NLS-2$
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

    @Override
    public String getSelectRegexp(String regexLikeExpression) {
        return "SELECT '1' FROM dual WHERE " + regexLikeExpression; //$NON-NLS-1$ 
    }

    @Override
    public boolean supportRegexp() {
        ProductVersion dbVersion = getDbVersion();
        if (dbVersion != null) {
            return dbVersion.getMajor() >= 10;
        }

        return false;
    }

    /**
     * DOC yyi 2011-06-20 22246:view rows for average length for Oracle
     * 
     * @return average length sql statement
     */
    @Override
    public String getAverageLengthRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$ 
    }

    @Override
    public String trimIfBlank(String colName) {
        return " CASE WHEN " + colName + " IS NOT NULL AND  LENGTH(" + trim(colName) + ") IS NULL  THEN '' ELSE  " + colName + " END"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or " + "  regexp_like(SUBSTR(" + columnName + ",0,1),'^[^[:digit:]]+$')";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithBlankRows()
     */
    @Override
    public String getAverageLengthWithBlankRows() {
        String whereExpression = "WHERE <%=__COLUMN_NAMES__%> IS NOT NULL "; //$NON-NLS-1$
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE  " + lengthForSumColumn("<%=__COLUMN_NAMES__%>") + " BETWEEN (SELECT FLOOR(SUM(" + lengthForSumColumn("<%=__COLUMN_NAMES__%>") + ") / COUNT(*)) FROM <%=__TABLE_NAME__%> " + whereExpression + ") AND (SELECT CEIL(SUM(" + lengthForSumColumn("<%=__COLUMN_NAMES__%>") + " ) / COUNT(* )) FROM <%=__TABLE_NAME__%> " + whereExpression + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullBlankRows()
     */
    @Override
    public String getAverageLengthWithNullBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE " + lengthForSumColumn("<%=__COLUMN_NAMES__%>") + " BETWEEN (SELECT FLOOR(SUM(" + lengthForSumColumn("<%=__COLUMN_NAMES__%>") + ") / COUNT(*)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEIL(SUM(" + lengthForSumColumn("<%=__COLUMN_NAMES__%>") + " ) / COUNT(* )) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullRows()
     */
    @Override
    public String getAverageLengthWithNullRows() {
        String whereExpression = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression + "AND LENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%> )) / COUNT( * )) FROM <%=__TABLE_NAME__%> " + whereExpression + ") AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%> )) / COUNT(*)) FROM <%=__TABLE_NAME__%>  " + whereExpression + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    /**
     * 
     * when the column value is null or blank,the length should be 0.
     * 
     * @param columnName
     * @return
     */
    private String lengthForSumColumn(String columnName) {
        return "CASE WHEN " + trim(columnName) + " IS NULL THEN 0 ELSE LENGTH(" + columnName + ") END"; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getRandomQuery(java.lang.String)
     */
    @Override
    public String getRandomQuery(String query) {
        return query + orderBy() + "dbms_random.value "; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#quote(java.lang.String)
     */
    @Override
    public String quote(String sqlIdentifier) {
        return super.quote(sqlIdentifier).toUpperCase();
    }

}
