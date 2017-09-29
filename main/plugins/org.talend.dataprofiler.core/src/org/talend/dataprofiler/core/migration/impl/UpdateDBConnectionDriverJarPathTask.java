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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;

/**
 * ADD TDQ-11676 msjian: this task will change the connection's DriverJarPath from
 * "C:\Users\ZhangShaona\Desktop\exajdbc.jar" to "exajdbc.jar" to avoid the bug: the import jdbc connection can check
 * connection but when run analysis get error. the same function this class is the same with
 * RenameDriverJarPathForDBConnectionMigrationTask which will run from DI side.
 */
public class UpdateDBConnectionDriverJarPathTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2016, 5, 3);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        List<IRepositoryViewObject> allConnectionObject = ProxyRepositoryFactory.getInstance().getAll(
                ERepositoryObjectType.METADATA_CONNECTIONS);

        for (IRepositoryViewObject object : allConnectionObject) {
            ConnectionItem item = (ConnectionItem) object.getProperty().getItem();
            Connection connection = item.getConnection();
            if (connection instanceof DatabaseConnection) {
                DatabaseConnection dbConn = (DatabaseConnection) connection;
                String oldJarPath = dbConn.getDriverJarPath();
                String newJarPath = null;
                try {
                    if (oldJarPath != null && !"".equals(oldJarPath.trim())) { //$NON-NLS-1$
                        IPath path = Path.fromOSString(oldJarPath);
                        if (path.isAbsolute()) {
                            newJarPath = path.lastSegment();
                        }
                    }
                    if (newJarPath != null) {
                        dbConn.setDriverJarPath(newJarPath);
                        ProxyRepositoryFactory.getInstance().save(item, true);
                    }
                } catch (Exception e) {
                    return false;
                }
            }

        }
        return true;
    }

}
