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

import org.eclipse.emf.common.util.EList;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.record.RecordFile;

/**
 * update file connection to add connection name.
 * 
 * @author msjian
 */
public class UpdateFileConnectionTask extends AbstractWorksapceUpdateTask {

    @Override
    protected boolean doExecute() throws Exception {

        RootContainer<String, IRepositoryViewObject> rc = ProxyRepositoryFactory.getInstance().getMetadata(
                ERepositoryObjectType.METADATA_FILE_DELIMITED);
        for (IRepositoryViewObject repViewObj : rc.getMembers()) {
            ConnectionItem item = (ConnectionItem) repViewObj.getProperty().getItem();
            Connection connection = item.getConnection();

            String conName = connection.getName();
            if (conName == null || "".equals(conName)) { //$NON-NLS-1$
                // set connection name.
                connection.setName(item.getProperty().getDisplayName());
                // set RecordFile name.
                EList<Package> dataPackage = connection.getDataPackage();
                for (Package data : dataPackage) {
                    if (data instanceof RecordFile) {
                        RecordFile rf = (RecordFile) data;
                        if (rf != null) {
                            rf.setName("default"); //$NON-NLS-1$
                        }
                    }
                }
                EMFUtil.saveResource(connection.eResource());
            }

        }

        return true;
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    public Date getOrder() {
        return createDate(2013, 1, 8);
    }

}
