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

import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.utils.ProductVersion;

/**
 * DOC msjian class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class RedshiftDbmsLanguage extends PostgresqlDbmsLanguage {

    public RedshiftDbmsLanguage() {
        super(SupportDBUrlType.REDSHIFT.getDBKey());
    }

    public RedshiftDbmsLanguage(String dbmsType) {
        super(dbmsType);
    }

    public RedshiftDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.dbms.PostgresqlDbmsLanguage#getInvalidClauseBenFord(java.lang.String)
     */
    public String getInvalidClauseBenFord(String columnName) {
        return columnName + " is null or " + columnName + "='' or SUBSTRING(" + columnName + ", 1,1)  ~ '[^0-9]'";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
