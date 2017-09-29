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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.talend.dataprofiler.migration.IMigrationTask;
import org.talend.dataprofiler.migration.IMigrationTask.MigrationTaskCategory;
import org.talend.dataprofiler.migration.IWorkspaceMigrationTask;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class DefaultMigrationTaskProvider implements IMigrationTaskProvider {

    private static Logger log = Logger.getLogger(DefaultMigrationTaskProvider.class);

    static List<IMigrationTask> allTaskList = null;

    static {

        allTaskList = new ArrayList<IMigrationTask>();

        IConfigurationElement[] elems = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
        for (IConfigurationElement elem : elems) {
            try {
                String taskName = elem.getAttribute(ATTR_NAME);
                String taskID = elem.getAttribute(ATTR_PID);

                IMigrationTask task = (IMigrationTask) elem.createExecutableExtension(ATTR_CLASS);
                task.setName(taskName);
                task.setId(taskID);

                if (task.getTaskCategory() == MigrationTaskCategory.WORKSPACE) {
                    String version = elem.getAttribute(ATTR_VERSION);
                    ((IWorkspaceMigrationTask) task).setVersion(version);
                }

                allTaskList.add(task);
            } catch (CoreException e) {
                log.error(e, e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.manager.IMigrationTaskProvider#getMigrationTasks()
     */
    public IMigrationTask[] getMigrationTasks() {
        return allTaskList.toArray(new IMigrationTask[allTaskList.size()]);
    }

}
