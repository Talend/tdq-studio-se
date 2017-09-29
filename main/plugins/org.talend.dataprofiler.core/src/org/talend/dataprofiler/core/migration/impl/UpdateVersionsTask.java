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

import java.util.Iterator;

import org.eclipse.emf.ecore.resource.Resource;
import org.talend.dataprofiler.core.migration.helper.WorkspaceVersionHelper;
import org.talend.dataprofiler.migration.AProjectTask;
import org.talend.dataprofiler.migration.IWorkspaceMigrationTask.MigrationTaskType;
import org.talend.dq.writer.EMFSharedResources;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class UpdateVersionsTask extends AProjectTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        // update workspace version.
        WorkspaceVersionHelper.storeVersion();

        // clear useless resource handel.
        clearUnloadResources();

        return true;
    }

    /**
     * DOC bZhou Comment method "clearUnloadResources".
     */
    private void clearUnloadResources() {
        Iterator<Resource> it = EMFSharedResources.getInstance().getResourceSet().getResources().iterator();
        while (it.hasNext()) {
            Resource resource = it.next();

            if (resource.getContents().isEmpty() || !resource.isLoaded()) {
                it.remove();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }
}
