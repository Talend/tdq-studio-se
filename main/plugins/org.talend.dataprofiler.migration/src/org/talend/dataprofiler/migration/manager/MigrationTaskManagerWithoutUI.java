// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.dataprofiler.migration.IMigrationTask;
import org.talend.dataprofiler.migration.IMigrationTask.MigrationTaskCategory;
import org.talend.dataprofiler.migration.IWorkspaceMigrationTask;
import org.talend.dataprofiler.migration.IWorkspaceMigrationTask.MigrationTaskType;
import org.talend.dataprofiler.migration.MigrationPlugin;
import org.talend.utils.ProductVersion;

/**
 * created by xqliu on 2013-11-7 Detailled comment
 *
 */
public class MigrationTaskManagerWithoutUI {

    private static Logger log = Logger.getLogger(MigrationTaskManagerWithoutUI.class);

    protected List<IMigrationTask> allMigrationTask;

    protected ProductVersion workspaceVersion;

    protected ProductVersion currentVersion;

    protected MigrationTaskType taskType;

    public MigrationTaskManagerWithoutUI(ProductVersion workspaceVersion) {
        this(null, workspaceVersion, null, null);
    }

    public MigrationTaskManagerWithoutUI(ProductVersion workspaceVersion, MigrationTaskType taskType) {
        this(null, workspaceVersion, null, taskType);
    }

    public MigrationTaskManagerWithoutUI(ProductVersion workspaceVersion, ProductVersion currentVersion,
            MigrationTaskType taskType) {
        this(null, workspaceVersion, currentVersion, taskType);
    }

    public MigrationTaskManagerWithoutUI(IMigrationTaskProvider taskProvider, ProductVersion workspaceVersion,
            ProductVersion currentVersion, MigrationTaskType taskType) {
        if (taskProvider == null) {
            taskProvider = new DefaultMigrationTaskProvider();
        }

        if (currentVersion == null) {
            this.currentVersion = MigrationPlugin.getDefault().getProductDisplayVersionWithPatch();
        } else {
            this.currentVersion = currentVersion;
        }

        this.workspaceVersion = workspaceVersion;
        this.taskType = taskType;

        this.allMigrationTask = new ArrayList<IMigrationTask>();
        if (taskProvider != null) {
            allMigrationTask.addAll(Arrays.asList(taskProvider.getMigrationTasks()));
            sortTasks(allMigrationTask);
        }
    }

    /**
     * DOC bZhou Comment method "getValidTasks".
     *
     * @param workspaceVersion
     * @param currentVersion
     * @param tasks
     * @return
     */
    public static List<IMigrationTask> getValidTasks(ProductVersion workspaceVersion, ProductVersion currentVersion,
            List<IMigrationTask> tasks) {

        List<IMigrationTask> validTasks = new ArrayList<IMigrationTask>();
        // TDQ-18624: not output debug log because cause studio slowly
        boolean isDebugEnabled = log.isInfoEnabled();
        if (isDebugEnabled) {
            log.info("old workspaceVersion: " + workspaceVersion); //$NON-NLS-1$
        }
        // consider 7.3.1,7.2.1...old migration tasks work as before.
        if (workspaceVersion.toString().length() == 5) {
            // because our monthly release begin from 2020
            workspaceVersion = new ProductVersion(workspaceVersion.getMajor(), workspaceVersion.getMinor(),
                    workspaceVersion.getMicro(), "20200101"); //$NON-NLS-1$
        }
        if (isDebugEnabled) {
            log.info("new workspaceVersion: " + workspaceVersion); //$NON-NLS-1$
            log.info("currentVersion: " + currentVersion); //$NON-NLS-1$
        }
        for (IMigrationTask task : tasks) {
            if (task.getTaskCategory() == MigrationTaskCategory.WORKSPACE) {
                IWorkspaceMigrationTask wTask = (IWorkspaceMigrationTask) task;
                ProductVersion taskVersion = ProductVersion.fromString(wTask.getVersion());
                // migration task Display Version format like 7.3.1.20200910
                ProductVersion taskDisplayVersion = new ProductVersion(taskVersion, task.getOrder());
                if (isDebugEnabled) {
                    log.info("one new task's DisplayVersion is: " + taskDisplayVersion); //$NON-NLS-1$
                }
                if (taskDisplayVersion.compareTo(workspaceVersion) > 0
                        && taskDisplayVersion.compareTo(currentVersion) <= 0) {
                    if (isDebugEnabled) {
                        log
                                .info(taskDisplayVersion + " > " + workspaceVersion + "&&" + taskDisplayVersion + "<=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                        + currentVersion + ", so"); //$NON-NLS-1$
                        log.info(task.getId() + " is valid task"); //$NON-NLS-1$
                    }
                    validTasks.add(task);
                } else {
                    if (isDebugEnabled) {
                        log.info(task.getId() + " is NOT valid task"); //$NON-NLS-1$
                    }
                }
            }

            if (task.getTaskCategory() == MigrationTaskCategory.PROJECT) {
                if (isDebugEnabled) {
                    log.info(task.getId() + " is project valid task"); //$NON-NLS-1$
                }
                validTasks.add(task);
            }
        }

        return validTasks;
    }

    /**
     * DOC bZhou Comment method "getValidTasks".
     *
     * @return
     */
    public List<IMigrationTask> getValidTasks() {

        if (taskType != null) {
            return getTaskByType(taskType);
        } else {
            return getWorkspaceTasks();
        }
    }

    public List<IMigrationTask> getWorkspaceTasks() {
        List<IMigrationTask> validTasks = getValidTasks(workspaceVersion, currentVersion, allMigrationTask);

        Iterator<IMigrationTask> it = validTasks.iterator();

        while (it.hasNext()) {
            IMigrationTask task = it.next();
            if (task.getMigrationTaskType() == MigrationTaskType.DATABASE) {
                it.remove();
            }
        }

        List<IMigrationTask> resortList = new ArrayList<IMigrationTask>();
        for (IMigrationTask task : validTasks) {
            if (task.isModelTask()) {
                resortList.add(task);
            }
        }

        for (IMigrationTask task : validTasks) {
            if (!task.isModelTask()) {
                resortList.add(task);
            }
        }

        return resortList;
    }

    /**
     * DOC bZhou Comment method "getTaskByType".
     *
     * @param type
     * @param specifiedVersion
     * @return
     */
    public List<IMigrationTask> getTaskByType(MigrationTaskType type) {
        List<IMigrationTask> validTasks = new ArrayList<IMigrationTask>();

        for (IMigrationTask task : allMigrationTask) {
            if (task.getMigrationTaskType() == type) {
                validTasks.add(task);
            }
        }

        return getValidTasks(workspaceVersion, currentVersion, validTasks);
    }

    /**
     * DOC bZhou Comment method "sortTasks".
     *
     * @param tasks
     */
    private static void sortTasks(List<IMigrationTask> tasks) {
        Collections.sort(tasks, new Comparator<IMigrationTask>() {

            @Override
            public int compare(IMigrationTask o1, IMigrationTask o2) {
                if (o1.getOrder() == null || o2.getOrder() == null) {
                    return 0;
                }
                if (o1 instanceof IWorkspaceMigrationTask && o2 instanceof IWorkspaceMigrationTask) {
                    int compareResult = ((IWorkspaceMigrationTask) o1).getVersion().compareToIgnoreCase(
                            ((IWorkspaceMigrationTask) o2).getVersion());
                    if (compareResult != 0) {
                        return compareResult;
                    }

                }

                return o1.getOrder().compareTo(o2.getOrder());
            }

        });
    }

    /**
     * DOC bZhou Comment method "doMigrationTask".
     */
    public void doMigrationTaskWithoutUI() {
        doMigrationTaskWithoutUI(getValidTasks());
    }

    /**
     * DOC bZhou Comment method "doMigrationTask".
     *
     * @param tasks
     * @param monitor
     */
    public static void doMigrationTaskWithoutUI(List<IMigrationTask> tasks) {
        for (IMigrationTask task : tasks) {
            if (task.valid()) {
                if (!task.execute()) {
                    log.error("Migration Task failed: " + task.getName()); //$NON-NLS-1$
                } else {
                    if (log.isInfoEnabled()) {
                        log.info("Migration Task success: " + task.getId()); //$NON-NLS-1$
                    }
                }
            }
        }
    }
}
