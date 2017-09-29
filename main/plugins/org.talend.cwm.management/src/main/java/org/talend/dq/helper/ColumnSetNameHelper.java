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
package org.talend.dq.helper;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public final class ColumnSetNameHelper {

    private ColumnSetNameHelper() {

    }

    /**
     * DOC zqin Comment method "getQualifiedName".
     * 
     * @param tdDataProvider
     * @return
     */
    public static String getColumnSetQualifiedName(Connection tdDataProvider, ColumnSet columnset) {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(tdDataProvider);
        return dbmsLanguage.getQueryColumnSetWithPrefix(columnset);
    }
}
