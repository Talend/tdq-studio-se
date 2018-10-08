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
package org.talend.dq.dbms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class MySQLDbmsLanguage extends DbmsLanguage {

    /**
     * 
     */
    private static final String MYSQL_IDENTIFIER_QUOTE = "`"; //$NON-NLS-1$

    /**
     * DOC scorreia MySQLDbmsLanguage constructor comment.
     */
    MySQLDbmsLanguage() {
        super(DbmsLanguage.MYSQL);
    }

    /**
     * DOC bzhou MySQLDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     * @param dbVersion
     */
    MySQLDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
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
     * @see org.talend.cwm.management.api.DbmsLanguage#replaceNullsWithString(java.lang.String, java.lang.String)
     */
    @Override
    public String replaceNullsWithString(String colName, String replacement) {
        return " IFNULL(" + colName + "," + replacement + ")"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getRegularExpressionFunction()
     */
    @Override
    public String getRegularExpressionFunction() {
        return "REGEXP BINARY"; //$NON-NLS-1$
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
     * @see org.talend.cwm.management.api.DbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain,
     * java.lang.String)
     */
    @Override
    protected String extract(DateGrain dateGrain, String colName) {
        return dateGrain.getName() + surroundWith('(', colName, ')');
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
        ProductVersion dbVersion = getDbVersion();
        // 'RegexP' doesn't work well for MySQL8
        if (dbVersion != null && dbVersion.getMajor() >= 8) {
            return surroundWithSpaces("REGEXP_LIKE(" + element + "," + regex + ",'c')");
        }
        return surroundWithSpaces(element + surroundWithSpaces(getRegularExpressionFunction()) + regex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexNotLike(String element, String regex) {
        ProductVersion dbVersion = getDbVersion();
        if (dbVersion != null && dbVersion.getMajor() >= 8) {
            return surroundWithSpaces("NOT REGEXP_LIKE(" + element + "," + regex + ",'c')");
        }
        return surroundWithSpaces(element + surroundWithSpaces(this.not() + getRegularExpressionFunction()) + regex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getQuoteIdentifier()
     */
    @Override
    public String getHardCodedQuoteIdentifier() {
        return MYSQL_IDENTIFIER_QUOTE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#supportAliasesInGroupBy()
     */
    @Override
    public boolean supportAliasesInGroupBy() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getSelectRemarkOnTable(java.lang.String)
     */
    @Override
    public String getSelectRemarkOnTable(String tableName) {
        return "SELECT TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_NAME='" + tableName + "'"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getBackSlashForRegex()
     */
    @Override
    public String getBackSlashForRegex() {
        return "\\\\"; //$NON-NLS-1$
    }

    @Override
    public boolean supportRegexp() {

        ProductVersion dbVersion = getDbVersion();
        if (dbVersion != null) {

            return dbVersion.getMajor() >= 5;
        }

        return false;
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
     * @see org.talend.dq.dbms.DbmsLanguage#createStatementForBigdata(java.sql.Connection)
     */
    @Override
    public Statement createStatement(Connection connection, int fetchSize) throws SQLException {
        // TDQ-10991,TDQ-11124 we use stream result to enhance performance on big data.
        // comment form Mysql API "createStreamingResultSet()":We only stream result sets when they are forward-only,
        // read-only, and the fetch size has been set to Integer.MIN_VALUE.
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.setFetchSize(Integer.MIN_VALUE);
        return statement;
    }
}
