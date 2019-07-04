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

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;

import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class MSSqlDbmsLanguage extends DbmsLanguage {

    private static final String MSSQL_IDENTIFIER_QUOTE = "\""; //$NON-NLS-1$

    Logger log = Logger.getLogger(MSSqlDbmsLanguage.class);

    /**
     * DOC scorreia MSSqlDbmsLanguage constructor comment.
     */
    MSSqlDbmsLanguage() {
        super(DbmsLanguage.MSSQL);
    }

    /**
     * DOC scorreia MSSqlDbmsLanguage constructor comment.
     *
     * @param dbmsType
     * @param majorVersion
     * @param minorVersion
     */
    MSSqlDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
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
        if (schema == null) { // use default (backward compatibility with TOP 1.1.0
            schema = quote("dbo"); //$NON-NLS-1$
        }

        return super.toQualifiedName(catalog, schema, table);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)
     */
    @Override
    public String getPatternFinderDefaultFunction(String expression) {
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

    @Override
    protected String getPatternFinderFunction(String expression, String charsToReplace, String replacementChars) {
        assert charsToReplace != null && replacementChars != null && charsToReplace.length() == replacementChars.length();
        for (int i = 0; i < charsToReplace.length(); i++) {
            final char charToReplace = charsToReplace.charAt(i);
            final char replacement = replacementChars.charAt(i);
            expression = replaceOneChar(expression, charToReplace, replacement);
        }
        return expression;
    }

    /*
     * (non-Javadoc)
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
     * @see org.talend.cwm.management.api.DbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain,
     * java.lang.String)
     */
    @Override
    protected String extract(DateGrain dateGrain, String colName) {
        return "DATEPART(" + dateGrain.getName() + " , " + colName + ") "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
     * @see org.talend.dq.dbms.DbmsLanguage#charLength(java.lang.String)
     */
    @Override
    public String charLength(String columnName) {
        // MOD klliu bug TDQ-4724 2012-03-13
        return " LEN(" + columnName + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#createGenericSqlWithRegexFunction(java.lang.String)
     */
    @Override
    public String createGenericSqlWithRegexFunction(String function) {

        return "SELECT COUNT(CASE WHEN " + function + "(" + GenericSQLHandler.COLUMN_NAMES + "," + GenericSQLHandler.PATTERN_EXPRESSION //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ") = 1 THEN 1 END), COUNT(*) FROM " + GenericSQLHandler.TABLE_NAME + " " + GenericSQLHandler.WHERE_CLAUSE; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * DOC yyi 2011-07-07 22246:view rows for average length for Oracle
     *
     * @return average length sql statement
     */
    @Override
    public String getAverageLengthRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LEN(LTRIM(RTRIM(<%=__COLUMN_NAMES__%>)))>0 AND DATALENGTH(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(DATALENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEILING(SUM(DATALENGTH(<%=__COLUMN_NAMES__%>)) / COUNT(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    @Override
    public String trimIfBlank(String colName) {
        return " CASE WHEN  LEN(" + trim(colName) + ")=0  THEN '' ELSE  " + colName + " END"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithBlankRows()
     */
    @Override
    public String getAverageLengthWithBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LEN(" + trimIfBlank("<%=__COLUMN_NAMES__%>") + ") BETWEEN (SELECT FLOOR(SUM(LEN(" + trimIfBlank("<%=__COLUMN_NAMES__%>") + ")) / COUNT(*)) FROM <%=__TABLE_NAME__%> WHERE <%=__COLUMN_NAMES__%> IS NOT NULL) AND (SELECT CEILING(CAST(SUM(LEN(" + trimIfBlank("<%=__COLUMN_NAMES__%>") + " ))*1.00 AS FLOAT) / COUNT(*)) FROM <%=__TABLE_NAME__%>  WHERE <%=__COLUMN_NAMES__%> IS NOT NULL)"; //$NON-NLS-1$
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullRows()
     */
    @Override
    public String getAverageLengthWithNullRows() {
        String whereExpression = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")";
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression + "AND LEN(<%=__COLUMN_NAMES__%>) BETWEEN (SELECT FLOOR(SUM(LEN(<%=__COLUMN_NAMES__%> )) / COUNT(*)) FROM <%=__TABLE_NAME__%> " + whereExpression + ") AND (SELECT CEILING(CAST(SUM(LEN(<%=__COLUMN_NAMES__%>))*1.00 AS FLOAT) / COUNT(*)) FROM <%=__TABLE_NAME__%> " + whereExpression + ")"; //$NON-NLS-1$
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullBlankRows()
     */
    @Override
    public String getAverageLengthWithNullBlankRows() {
        return "SELECT * FROM <%=__TABLE_NAME__%> WHERE LEN(" + trimIfBlank("<%=__COLUMN_NAMES__%>") + ") BETWEEN (SELECT FLOOR(SUM(LEN(" + trimIfBlank("<%=__COLUMN_NAMES__%>") + ")) / COUNT(*)) FROM <%=__TABLE_NAME__%>) AND (SELECT CEILING(CAST(SUM(LEN(" + trimIfBlank("<%=__COLUMN_NAMES__%>") + " ))*1.00 AS FLOAT) / COUNT(*)) FROM <%=__TABLE_NAME__%>)"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or LEFT(" + columnName + ",1) not" + this.like() + "'%[0-9]%'";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
        return query + orderBy() + "NEWID() "; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getQuoteIdentifier()
     */
    @Override
    public String getHardCodedQuoteIdentifier() {
        return MSSQL_IDENTIFIER_QUOTE; //$NON-NLS-1$
    }

}
