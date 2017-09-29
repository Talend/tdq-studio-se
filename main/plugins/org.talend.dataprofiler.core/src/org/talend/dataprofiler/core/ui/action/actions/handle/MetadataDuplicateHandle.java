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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * Duplicate a metadata
 */
public class MetadataDuplicateHandle extends ModelElementDuplicateHandle {

    private Logger log = Logger.getLogger(MetadataDuplicateHandle.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.actions.handle.IDuplicateHandle#isExistedLabel(java.lang.String)
     */
    public boolean isExistedLabel(String label) {
        List<ConnectionItem> allMetadataConnectionsItem = new ArrayList<ConnectionItem>();
        try {
            allMetadataConnectionsItem = ProxyRepositoryFactory.getInstance().getMetadataConnectionsItem();
        } catch (PersistenceException e) {
            log.error(e, e);
        }
        for (ConnectionItem connectionItem : allMetadataConnectionsItem) {
            if (StringUtils.equals(label, connectionItem.getConnection().getName())) {
                return true;
            }
        }

        return false;
    }

}
