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

import org.apache.commons.lang.StringUtils;
import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class NetezzaDbmsLanguage extends DbmsLanguage {

    /**
     * 
     */
    private static final String MYSQL_IDENTIFIER_QUOTE = "`"; //$NON-NLS-1$

    private final String NYSIIS_PREFIX = "NYSIIS";//$NON-NLS-1$

    /**
     * DOC klliu NetezzaDbmsLanguage constructor comment.
     */
    NetezzaDbmsLanguage() {
        super(DbmsLanguage.NETEZZA);
    }

    /**
     * DOC klliu NetezzaDbmsLanguage constructor comment.
     * 
     * @param dbmsType
     * @param dbVersion
     */
    NetezzaDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#getPatternFinderDefaultFunction(java.lang.String)
     */
    @Override
    public String getPatternFinderDefaultFunction(String expression) {
        return StringUtils.repeat("TRANSLATE(", 59) + expression //$NON-NLS-1$
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
        return translateUsingPattern(expression, charsToReplace, replacementChars);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#replaceNullsWithString(java.lang.String, java.lang.String)
     */
    @Override
    public String replaceNullsWithString(String colName, String replacement) {
        return " ISNULL(" + colName + "," + replacement + ")"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.management.api.DbmsLanguage#extract(org.talend.dataquality.indicators.DateGrain,
     * java.lang.String)
     */
    @Override
    protected String extract(DateGrain dateGrain, String colName) {
        return "DATE_PART" + surroundWith('(', surroundWith('\'', dateGrain.getLiteral(), '\'') + "," + colName, ')'); //$NON-NLS-1$ //$NON-NLS-2$
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
     * @see org.talend.dq.dbms.DbmsLanguage#getSoundexPrefix()
     */
    @Override
    public String getSoundexPrefix() {
        return this.NYSIIS_PREFIX;
    }

    /**
     * 
     * Get invalid clause for Benford indicator.
     * 
     * @param columnName
     * @return
     */
    @Override
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or Substring(" + columnName + ",1,1) not in ('0','1','2','3','4','5','6','7','8','9')";//$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.DbmsLanguage#toQualifiedName(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String toQualifiedName(String catalog, String schema, String table) {
        String c = catalog;
        String s = schema;
        // TDQ-9543: the correct structure of Netezza include both catalog and schema, if the catalog is not blank but
        // schema is blank, should set catalog with empty by force, otherwise the generate sql should be like
        // "select * from catalog.table", this will cause error; another point is that catalog is blank but schema is
        // not blank, don't need to do anything for this because the sql like "select * from schema.table" is ok
        if (!StringUtils.isBlank(c) && StringUtils.isBlank(s)) {
            c = StringUtils.EMPTY;
        }
        // the catalog and schema of Netezza must be UpperCase
        c = StringUtils.upperCase(c);
        s = StringUtils.upperCase(s);
        return super.toQualifiedName(c, s, table);
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
}
