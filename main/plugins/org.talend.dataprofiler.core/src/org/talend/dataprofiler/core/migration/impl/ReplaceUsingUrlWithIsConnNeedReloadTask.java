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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;
import java.util.List;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import orgomg.cwm.objectmodel.core.TaggedValue;


/**
 * DOC yyin  class global comment. Detailled comment
 */
public class ReplaceUsingUrlWithIsConnNeedReloadTask extends AbstractWorksapceUpdateTask {

    private static final String USING_URL = "Using URL";//$NON-NLS-1$
    /* (non-Javadoc)
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2012, 12, 3);
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /* (non-Javadoc)
     * @see org.talend.dataprofiler.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        List<IRepositoryViewObject> allConnectionObject = ProxyRepositoryFactory.getInstance().getAll(
                ERepositoryObjectType.METADATA_CONNECTIONS);

        for (IRepositoryViewObject object : allConnectionObject) {
            ConnectionItem item = (ConnectionItem) object.getProperty().getItem();
            Connection connection = item.getConnection();
            // check if it has "USING_URL, if not, set "IS_CONN_NEED_RELOAD"=false
            TaggedValue usingUrl = TaggedValueHelper.getTaggedValue(USING_URL, connection.getTaggedValue());
            if (usingUrl == null || usingUrl.getValue() == null) {
                ConnectionHelper.setIsConnNeedReload(connection, Boolean.FALSE);
            } else if (connection instanceof DatabaseConnection) {
                // else: compare its value with connection's url, if equals set IS_CONN_NEED_RELOAD=false
                if (!usingUrl.getValue().equalsIgnoreCase(((DatabaseConnection) connection).getURL())) {
                    ConnectionHelper.setIsConnNeedReload(connection, Boolean.TRUE);
                } else {
                // else set IS_CONN_NEED_RELOAD=true
                    ConnectionHelper.setIsConnNeedReload(connection, Boolean.FALSE);
                }
            }
            ProxyRepositoryFactory.getInstance().save(item);
        }
        return true;
    }

}
