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

import org.talend.dataquality.indicators.DateGrain;
import org.talend.utils.ProductVersion;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC mzhao, AS/400 database language. bug 14464: Column Analysis with Pattern Frequency Statistics against AS400 fails
 * 2010-08-02
 */
public class AS400DbmsLanguage extends DbmsLanguage {

    AS400DbmsLanguage() {
        super(DbmsLanguage.AS400);
    }

    AS400DbmsLanguage(String dbmsType, ProductVersion dbVersion) {
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
        return "TRANSLATE(CHAR(" + expression + ") ,'" + replacementChars + "','" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
}
