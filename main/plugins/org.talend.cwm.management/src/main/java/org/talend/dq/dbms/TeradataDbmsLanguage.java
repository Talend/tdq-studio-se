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

import java.util.regex.Matcher;

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class TeradataDbmsLanguage extends DbmsLanguage {

    /**
     * DOC scorreia TeradataDbmsLanguage constructor comment.
     */
    TeradataDbmsLanguage() {
        super(DbmsLanguage.TERADATA);
    }

    /**
     * DOC scorreia TeradataDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     * @param majorVersion
     * @param minorVersion
     */
    TeradataDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /**
     * DOC yyi 2011-07-07 22246:view rows for average length for Oracle
     * 
     * @return average length sql statement
     */
    @Override
    public String getAverageLengthRows() {
        return "SELECT t.* FROM(" + "SELECT " //$NON-NLS-1$//$NON-NLS-2$
                + "CAST(SUM(CHARACTER_LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(<%=__COLUMN_NAMES__%>)*1.00)+0.99 as int) c, " //$NON-NLS-1$
                + "CAST(SUM(CHARACTER_LENGTH(<%=__COLUMN_NAMES__%>)) / (COUNT(<%=__COLUMN_NAMES__%>)*1.00) as int) f " //$NON-NLS-1$
                + "FROM <%=__TABLE_NAME__%>) e, <%=__TABLE_NAME__%> t " //$NON-NLS-1$
                + "where character_length(<%=__COLUMN_NAMES__%>) between f and c"; //$NON-NLS-1$
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithBlankRows()
     */
    @Override
    public String getAverageLengthWithBlankRows() {
        String sql = "SELECT t.* FROM(SELECT CAST(SUM(" + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) //$NON-NLS-1$ //$NON-NLS-2$
                + ") / (COUNT(<%=__COLUMN_NAMES__%> )*1.00)+0.99 as int) c," + "CAST(SUM(" //$NON-NLS-1$ //$NON-NLS-2$
                + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + ") / (COUNT(<%=__COLUMN_NAMES__%>)*1.00) as int) f " //$NON-NLS-1$ //$NON-NLS-2$
                + "FROM <%=__TABLE_NAME__%> WHERE(<%=__COLUMN_NAMES__%> IS NOT NULL)) e, <%=__TABLE_NAME__%> t " + "WHERE " //$NON-NLS-1$ //$NON-NLS-2$
                + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + " BETWEEN f AND c"; //$NON-NLS-1$ //$NON-NLS-2$
        return sql;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullBlankRows()
     */
    @Override
    public String getAverageLengthWithNullBlankRows() {
        String sql = "SELECT t.* FROM(SELECT CAST(SUM(" + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) //$NON-NLS-1$ //$NON-NLS-2$
                + ") / (COUNT(*)*1.00)+0.99 as int) c," + "CAST(SUM(" + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ") / (COUNT(*)*1.00) as int) f " + "FROM <%=__TABLE_NAME__%> ) e, <%=__TABLE_NAME__%> t " + "WHERE " //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                + charLength(trimIfBlank("<%=__COLUMN_NAMES__%>")) + " BETWEEN f AND c"; //$NON-NLS-1$ //$NON-NLS-2$
        return sql;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getAverageLengthWithNullRows()
     */
    @Override
    public String getAverageLengthWithNullRows() {
        String whereExp = "WHERE(<%=__COLUMN_NAMES__%> IS NULL OR " + isNotBlank("<%=__COLUMN_NAMES__%>") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String sql = "SELECT t.* FROM(SELECT " + "CAST(SUM(" + charLength("<%=__COLUMN_NAMES__%>") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ") / (COUNT(<%=__COLUMN_NAMES__%> )*1.00)+0.99 as int) c," + "CAST(SUM(" + charLength("<%=__COLUMN_NAMES__%>") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + ") / (COUNT(<%=__COLUMN_NAMES__%>)*1.00) as int) f " + "FROM <%=__TABLE_NAME__%> " + whereExp //$NON-NLS-1$ //$NON-NLS-2$
                + ") e, <%=__TABLE_NAME__%> t " + whereExp + "AND " + charLength("<%=__COLUMN_NAMES__%>") + " BETWEEN f AND c"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        return sql;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or cast(" + columnName + " as char(1)) not in ('0','1','2','3','4','5','6','7','8','9')";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getColumnNameInQueryClause(java.lang. String)
     */
    @Override
    public String castColumnNameToChar(String columnName) {
        return "cast(" + columnName + " as char)";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getTopNQuery(java.lang.String, int)
     */
    @Override
    public String getTopNQuery(String query, int n) {
        // when do getRandomQuery first
        if (query.endsWith(" SAMPLE ")) { //$NON-NLS-1$
            return query + n;
        }
        Matcher m = SELECT_PATTERN.matcher(query);
        return m.replaceFirst("SELECT TOP " + n + PluginConstant.SPACE_STRING); //$NON-NLS-1$ 
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getCatalogNameFromContext(org.talend.
     * core.model.metadata.builder.connection. DatabaseConnection)
     */
    @Override
    public String getCatalogNameFromContext(DatabaseConnection dbConn) {
        return null;
    }

    @Override
    public String extractWeek(String colName) {
        return "((CAST(" + colName + " AS DATE) - ((" + extract(DateGrain.YEAR, colName) //$NON-NLS-1$ //$NON-NLS-2$
                + "- 1900) * 10000 + 0101 (DATE))) - ((CAST(" + colName + " AS DATE) - DATE '0001-01-07') MOD 7)  + 13) / 7"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public String extractQuarter(String colName) {
        return "(((CAST(" + extract(DateGrain.MONTH, colName) + "AS BYTEINT)-1)/3)+1)"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getRandomQuery(java.lang.String)
     */
    @Override
    public String getRandomQuery(String query) {
        // the SAMPLE is the random method, when use this method need to add a number after this
        return query + " SAMPLE "; //$NON-NLS-1$
    }
}
