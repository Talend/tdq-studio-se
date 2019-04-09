// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.dbms;

import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.utils.ProductVersion;

import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC msjian class global comment. Detailled comment <br/>
 * 
 */
public class BigQueryDbmsLanguage extends DbmsLanguage {

    private static final String BIGQUERY_IDENTIFIER_QUOTE = "`"; //$NON-NLS-1$

    public BigQueryDbmsLanguage() {
        super(BIGQUERY);
    }

    public BigQueryDbmsLanguage(String dbmsType) {
        super(dbmsType);
    }

    public BigQueryDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getQuoteIdentifier()
     */
    @Override
    public String getHardCodedQuoteIdentifier() {
        return BIGQUERY_IDENTIFIER_QUOTE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getQueryColumnsWithPrefix(org.talend.cwm.relational.TdColumn[])
     */
    @Override
    public String getQueryColumnsWithPrefix(TdColumn[] columns) {
        String columnClause = PluginConstant.EMPTY_STRING;
        if (columns.length == 0) {
            return columnClause;
        }
        ColumnSet columnSet = ColumnHelper.getColumnOwnerAsColumnSet(columns[0]);
        // TDQ-15039: support bigQuery can generate correct query
        // remove the first schema. from the select
        String tableName = handleContextModeOrAddQuotes(columnSet.getName());
        for (TdColumn column : columns) {
            columnClause += tableName + DOT + quote(column.getName()) + getSeparatedCharacter();
        }
        columnClause = columnClause.substring(0, columnClause.length() - 1);
        return columnClause;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#toQualifiedName(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String toQualifiedName(String catalog, String schema, String table) {
        StringBuffer qualName = new StringBuffer();
        // not append catalog here
        if (schema != null && schema.trim().length() > 0) {
            qualName.append(this.handleContextModeOrAddQuotes(schema));
            qualName.append(getDelimiter());
        }

        qualName.append(this.handleContextModeOrAddQuotes(table));
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s.%s.%s -> %s", catalog, schema, table, qualName)); //$NON-NLS-1$
        }
        return qualName.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or not REGEXP_CONTAINS(" + columnName + ", '^[0-9]')"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#castColumnNameToChar(java.lang.String)
     */
    @Override
    public String castColumnNameToChar(String columnName) {
        return "CAST(" + columnName + " AS String)";//$NON-NLS-1$ //$NON-NLS-2$
    }
}
