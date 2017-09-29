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
package org.talend.dataprofiler.core.ui.events;

import org.apache.log4j.Logger;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.cwm.management.api.SoftwareSystemManager;

/**
 * created by zhao on Aug 4, 2013 Detailled comment
 * 
 */
public class SoftwareSystemUpdateEventReceiver extends EventReceiver {

    private static Logger log = Logger.getLogger(SoftwareSystemUpdateEventReceiver.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.events.EventReceiver#handle(java.lang.Object)
     */
    @Override
    public boolean handle(Object data) {
        Boolean isSuccess = Boolean.FALSE;
        try {
            if (data instanceof DatabaseConnection) {
                DatabaseConnection databaseConnection = (DatabaseConnection) data;
                SoftwareSystemManager.getInstance().updateSoftwareSystem(databaseConnection);
                isSuccess = Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            isSuccess = Boolean.FALSE;
        }
        return isSuccess;
    }
}
