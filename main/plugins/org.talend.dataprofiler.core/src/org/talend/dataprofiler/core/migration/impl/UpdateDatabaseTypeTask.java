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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class UpdateDatabaseTypeTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(UpdateDatabaseTypeTask.class);

    private static final String RAW_JDBC_TYPE = "Generic JDBC"; //$NON-NLS-1$

    private static final String RAW_SYBASE_TYPE = "Sybase"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2011, 2, 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        List<IRepositoryViewObject> allConnectionObject = ProxyRepositoryFactory.getInstance().getAll(
                ERepositoryObjectType.METADATA_CONNECTIONS);

        for (IRepositoryViewObject object : allConnectionObject) {
            ConnectionItem item = (ConnectionItem) object.getProperty().getItem();
            if (item.getConnection() instanceof DatabaseConnection) {
                DatabaseConnection connection = (DatabaseConnection) item.getConnection();
                String rawType = connection.getDatabaseType();
                if (StringUtils.equalsIgnoreCase(rawType, RAW_JDBC_TYPE)) {
                    connection.setDatabaseType(SupportDBUrlType.GENERICJDBCDEFAULTURL.getDBKey());
                } else if (StringUtils.equalsIgnoreCase(rawType, RAW_SYBASE_TYPE)) {
                    connection.setDatabaseType(SupportDBUrlType.SYBASEDEFAULTURL.getDBKey());
                }

                ProxyRepositoryFactory.getInstance().save(item);
            } else {
                log.warn("Database " + object.getProperty().getLabel() + " Ignored: It's not a Database Connection!"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        return true;
    }

}
