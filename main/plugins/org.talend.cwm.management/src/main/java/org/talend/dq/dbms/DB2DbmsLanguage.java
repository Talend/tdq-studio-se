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

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class DB2DbmsLanguage extends DbmsLanguage {

    /**
     * DOC scorreia DB2DbmsLanguage constructor comment.
     */
    DB2DbmsLanguage() {
        super(DbmsLanguage.DB2);
    }

    /**
     * DOC scorreia DB2DbmsLanguage constructor comment.
     *
     * @param dbmsType
     * @param majorVersion
     * @param minorVersion
     */
    DB2DbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)
     */
    @Override
    public String getPatternFinderDefaultFunction(String expression) {
        return "TRANSLATE(CHAR(" + expression + ") ,VARCHAR(REPEAT('9',10) || REPEAT('A',25)||REPEAT('a',25)), " //$NON-NLS-1$ //$NON-NLS-2$
                + " '1234567890BCDEFGHIJKLMNOPQRSTUVWXYZbcdefghijklmnopqrstuvwxyz')"; // cannot put accents //$NON-NLS-1$
    }

    @Override
    protected String getPatternFinderFunction(String expression, String charsToReplace, String replacementChars) {
        return "TRANSLATE(" + expression + " ,VARCHAR('" + replacementChars + "'),'" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + charsToReplace + "')"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.cwm.management.api.DbmsLanguage#getTopNQuery(java.lang.String, int)
     */
    @Override
    public String getTopNQuery(String query, int n) {
        return query + " FETCH FIRST " + n + " ROWS ONLY "; //$NON-NLS-1$ //$NON-NLS-2$
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
     * @see org.talend.dq.dbms.DbmsLanguage#charLength(java.lang.String)
     */
    @Override
    public String charLength(String columnName) {
        return " LENGTH(" + columnName + ") "; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#trim(java.lang.String)
     */
    @Override
    public String trim(String colName) {
        return " LTRIM(RTRIM(" + colName + ")) ";//$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public String isNotBlank(String colName) {
        // ADD qiongli 2012-6-7 TDQ-5559
        return " LENGTH(" + trim(colName) + ")!=0";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or LEFT(" + columnName + ",1)" + " not in ('0','1','2','3','4','5','6','7','8','9')";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

    @Override
    public String getAverageLengthRows() {
        String whereExpression =
                "WHERE <%=__COLUMN_NAMES__%> IS NOT NULL AND (LENGTH(TRIM(<%=__COLUMN_NAMES__%>)) > 0 )"; //$NON-NLS-1$
        return "SELECT * FROM <%=__TABLE_NAME__%> " + whereExpression + " AND LENGTH(<%=__COLUMN_NAMES__%>"
                + ") BETWEEN (SELECT FLOOR(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(*)) FROM <%=__TABLE_NAME__%> "
                + whereExpression
                + ") AND (SELECT CEIL(SUM(LENGTH(<%=__COLUMN_NAMES__%>))*1.0 / COUNT(* )) FROM <%=__TABLE_NAME__%> "
                + whereExpression + ")"; //$NON-NLS-1$
    }

}
