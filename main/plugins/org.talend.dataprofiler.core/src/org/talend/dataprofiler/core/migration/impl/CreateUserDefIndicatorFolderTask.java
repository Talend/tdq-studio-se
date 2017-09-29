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

import org.eclipse.core.resources.IFolder;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class CreateUserDefIndicatorFolderTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AWorkspaceTask#valid()
     */
    @Override
    public boolean valid() {
        return !ResourceManager.getUDIFolder().exists() && super.valid();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        DQStructureManager manager = DQStructureManager.getInstance();

        // creator Indicators
        IFolder folder = manager.createNewFolder(ResourceManager.getLibrariesFolder(), EResourceConstant.INDICATORS.getName());
        ResourceService.setNoSubFolderProperty(folder);

        // create User Defined Indicators
        folder = manager.createNewFolder(folder, EResourceConstant.USER_DEFINED_INDICATORS.getName());

        return folder != null && folder.exists();
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.STUCTRUE;
    }

    public Date getOrder() {
        return createDate(2009, 8, 13);
    }

}
