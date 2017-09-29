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

import org.talend.utils.ProductVersion;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DelimitedFileLanguage extends DbmsLanguage {

    public DelimitedFileLanguage() {
        super(DbmsLanguage.DELIMITEDFILE);
    }

    public DelimitedFileLanguage(String dbmsType, ProductVersion dbVersion) {
        super(dbmsType, dbVersion);
    }

}
