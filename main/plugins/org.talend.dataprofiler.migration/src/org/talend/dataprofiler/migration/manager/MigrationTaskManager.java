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
package org.talend.dataprofiler.migration.manager;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.dataprofiler.migration.IMigrationTask;
import org.talend.dataprofiler.migration.IWorkspaceMigrationTask.MigrationTaskType;
import org.talend.utils.ProductVersion;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class MigrationTaskManager extends MigrationTaskManagerWithoutUI {

    private static Logger log = Logger.getLogger(MigrationTaskManager.class);

    public MigrationTaskManager(ProductVersion workspaceVersion) {
        super(null, workspaceVersion, null, null);
    }

    public MigrationTaskManager(ProductVersion workspaceVersion, MigrationTaskType taskType) {
        super(null, workspaceVersion, null, taskType);
    }

    public MigrationTaskManager(ProductVersion workspaceVersion, ProductVersion currentVersion, MigrationTaskType taskType) {
        super(null, workspaceVersion, currentVersion, taskType);
    }

    public MigrationTaskManager(IMigrationTaskProvider taskProvider, ProductVersion workspaceVersion,
            ProductVersion currentVersion, MigrationTaskType taskType) {
        super(taskProvider, workspaceVersion, currentVersion, taskType);
    }

    /**
     * DOC bZhou Comment method "doMigrationTask".
     * 
     * @param monitor
     */
    public void doMigrationTask(IProgressMonitor monitor) {
        doMigrationTask(getValidTasks(), monitor);
    }

    /**
     * DOC bZhou Comment method "doMigrationTask".
     * 
     * @param tasks
     * @param monitor
     */
    public static void doMigrationTask(List<IMigrationTask> tasks, IProgressMonitor monitor) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        monitor.beginTask("Migration...", tasks.size()); //$NON-NLS-1$

        for (IMigrationTask task : tasks) {

            if (monitor.isCanceled()) {
                break;
            }

            monitor.subTask(task.getName());

            if (task.valid()) {
                if (!task.execute()) {
                    log.error("Migration Task failed: " + task.getName()); //$NON-NLS-1$
                } else {
                    if (log.isInfoEnabled()) {
                        log.info("Migration Task success: " + task.getId()); //$NON-NLS-1$
                    }
                }
            }

            monitor.worked(1);
        }

        monitor.done();
    }
}
