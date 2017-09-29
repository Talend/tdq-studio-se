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
package org.talend.cwm.compare.ui.service;

import org.talend.core.ITDQCompareService;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.compare.ui.actions.ReloadDatabaseAction;
import org.talend.cwm.compare.ui.actions.provider.ReloadDatabaseProvider;
import org.talend.utils.sugars.ReturnCode;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class TDQCompareService implements ITDQCompareService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ITDQCompareService#reloadDatabase(org.talend.core.model.properties.ConnectionItem)
     */
    /**
     * 
     * Comment method "reloadDatabase".
     * 
     * @param connectionItem
     * 
     */
    public ReturnCode reloadDatabase(ConnectionItem connectionItem) {
        ReturnCode retCode = new ReturnCode(Boolean.TRUE);
        Connection conn = connectionItem.getConnection();
        if (conn instanceof DatabaseConnection) {
            // MOD TDQ-7528 20130627 yyin: no need to popup select compare dialog
            ReloadDatabaseAction reloadDatabaseAction = new ReloadDatabaseAction(conn,
                    ReloadDatabaseProvider.RELOADDATABASE_MENUTEXT, false);
            reloadDatabaseAction.run();
            retCode = reloadDatabaseAction.getReturnCode();

        }
        return retCode;
    }

}
