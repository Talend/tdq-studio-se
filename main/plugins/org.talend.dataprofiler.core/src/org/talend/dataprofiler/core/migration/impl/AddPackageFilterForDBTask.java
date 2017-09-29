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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.resource.EResourceConstant;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class AddPackageFilterForDBTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2012, 2, 8);
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
        updateConnectionFile();
        return true;
    }

    private void updateConnectionFile() {
        File rawDir = getWorkspacePath().append(EResourceConstant.METADATA.getPath()).toFile();
        ArrayList<File> fileList = new ArrayList<File>();

        if (rawDir.exists()) {
            getAllFilesFromFolder(rawDir, fileList, new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(FactoriesUtil.ITEM_EXTENSION);
                }
            });
        }

        for (File file : fileList) {
            IFile iFile = WorkspaceUtils.fileToIFile(file);

            Property property = PropertyHelper.getProperty(iFile);
            if (property != null) {
                Item item = property.getItem();
                EResourceConstant type = EResourceConstant.getTypedConstant(item);
                if (type == EResourceConstant.DB_CONNECTIONS) {
                    DatabaseConnectionItem dbItem = (DatabaseConnectionItem) item;
                    DatabaseConnection connection = (DatabaseConnection) dbItem.getConnection();
                    // set package filter to old connection file
                    ConnectionHelper.setPackageFilter(connection, "");//$NON-NLS-1$
                    EMFSharedResources.getInstance().saveResource(connection.eResource());
                }
            }
        }

    }
}
