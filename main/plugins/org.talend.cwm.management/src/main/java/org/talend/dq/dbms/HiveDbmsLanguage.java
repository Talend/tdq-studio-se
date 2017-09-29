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

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.utils.ProductVersion;
import org.talend.utils.sql.Java2SqlType;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class HiveDbmsLanguage extends DbmsLanguage {

    private static final String HIVE_IDENTIFIER_QUOTE = "`"; //$NON-NLS-1$

    /**
     * DOC qiongli HiveDbmsLanguage constructor comment.
     */
    public HiveDbmsLanguage() {
        super(DbmsLanguage.HIVE);
    }

    /**
     * DOC qiongli HiveDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     */
    public HiveDbmsLanguage(String dbmsType) {
        super(dbmsType);
    }

    /**
     * DOC qiongli HiveDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     * @param dbVersion
     */
    public HiveDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
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
     * @see org.talend.dq.dbms.DbmsLanguage#regexNotLike(java.lang.String, java.lang.String)
     */
    @Override
    public String regexNotLike(String element, String regex) {
        return surroundWithSpaces(element + surroundWithSpaces(this.not() + getRegularExpressionFunction()) + regex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getRegularExpressionFunction()
     */
    @Override
    public String getRegularExpressionFunction() {
        return "REGEXP"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#extractRegularExpressionFunction(orgomg.cwm.objectmodel.core.Expression)
     */
    @Override
    public String extractRegularExpressionFunction(Expression expression, String regex) {
        return getRegularExpressionFunction();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#isPkIndexSupported()
     */
    @Override
    public boolean isPkIndexSupported() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#supportCatalogSelection()
     */
    @Override
    public boolean supportCatalogSelection() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#createStatement(java.sql.Connection)
     */
    @Override
    public Statement createStatement(java.sql.Connection connection, int fetchSize) throws SQLException {
        // hive don't need to set fetch size
        return createStatement(connection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getQueryColumnsWithPrefix(org.talend.cwm.relational.TdColumn[])
     */
    @Override
    public String getQueryColumnsWithPrefix(TdColumn[] columns) {
        return getQueryColumns(columns);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dq.dbms.DbmsLanguage#castColumn4ColumnAnalysisSqlExecutor(org.talend.dataquality.indicators.Indicator,
     * org.talend.cwm.relational.TdColumn, java.lang.String)
     */
    @Override
    public String castColumn4ColumnAnalysisSqlExecutor(Indicator indicator, TdColumn tdColumn, String colName) {
        if (indicator instanceof BenfordLawFrequencyIndicator) {
            int javaType = tdColumn.getSqlDataType().getJavaDataType();
            if (Java2SqlType.isNumbericInSQL(javaType)) {
                return castColumnNameToChar(colName);
            }
        }
        return super.castColumn4ColumnAnalysisSqlExecutor(indicator, tdColumn, colName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#castColumnNameToChar(java.lang.String)
     */
    @Override
    public String castColumnNameToChar(String columnName) {
        // for impala, int type can not be used for string method,
        return "CAST(" + columnName + " AS String)";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        // for impala, int type can not be used for REGEXP method
        return super.getInvalidClauseBenFord(castColumnNameToChar(columnName));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dq.dbms.DbmsLanguage#getQueryColumnSetWithPrefixFromContext(org.talend.core.model.metadata.builder
     * .connection.DatabaseConnection, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected String getQueryColumnSetWithPrefixFromContext(DatabaseConnection dbConn, String catalogName, String schemaName,
            String tableName) {
        String catalogNameFromContext = getCatalogNameFromContext(dbConn);
        String schemaNameFromContext = getSchemaNameFromContext(dbConn);
        if (StringUtils.isBlank(catalogNameFromContext) && StringUtils.isBlank(schemaNameFromContext)) {
            catalogNameFromContext = catalogName;
            schemaNameFromContext = schemaName;
        }
        return toQualifiedName(catalogNameFromContext, schemaNameFromContext, tableName);
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
     * @see org.talend.dq.dbms.DbmsLanguage#getPatternFinderFunction(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    protected String getPatternFinderFunction(String expression, String charsToReplace, String replacementChars) {
        // TDQ-12042: fix hive can not run well for indicator "Pattern Low Frequency".
        assert charsToReplace != null && replacementChars != null && charsToReplace.length() == replacementChars.length();
        return translateUsingPattern(expression, charsToReplace, replacementChars);
    }

    @Override
    public String getHardCodedQuoteIdentifier() {
        return HIVE_IDENTIFIER_QUOTE;
    }
}
