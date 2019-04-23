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

import org.talend.utils.ProductVersion;

public class MariaDBDbmsLanguage extends MySQLDbmsLanguage {

    @Override
    public String getRegularExpressionFunction() {
        return "REGEXP"; //$NON-NLS-1$
    }

    /**
     * 
     */
    public MariaDBDbmsLanguage() {
        super();
    }

    /**
     * @param dbmsType
     * @param dbVersion
     */
    public MariaDBDbmsLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

    @Override
    protected boolean isMappingMutilVersion(ProductVersion dbVersion) {
        return false;
    }

}
