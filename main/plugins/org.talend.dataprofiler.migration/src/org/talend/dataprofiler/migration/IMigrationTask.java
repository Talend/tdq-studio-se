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
package org.talend.dataprofiler.migration;

import java.util.Date;

import org.talend.dataprofiler.migration.IWorkspaceMigrationTask.MigrationTaskType;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IMigrationTask {

    /**
     * DOC bZhou IWorkspaceMigrationTask class global comment. Detailled comment
     */
    public enum MigrationTaskCategory {
        WORKSPACE,
        PROJECT,
    }

    /**
     * DOC bzhou Comment method "getId".
     * 
     * @return
     */
    public String getId();

    /**
     * DOC bzhou Comment method "setId".
     * 
     * @param id
     */
    public void setId(String id);

    /**
     * DOC bzhou Comment method "getName".
     * 
     * @return
     */
    public String getName();

    /**
     * DOC bzhou Comment method "setName".
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * This method is used to check the condition for executing the task.
     * 
     * @return true if it's proper to execute the task.
     */
    public boolean valid();

    /**
     * DOC bzhou Comment method "execute".
     * 
     * @return true if execute successfully.
     */
    public boolean execute();

    /**
     * DOC bZhou Comment method "persist".
     * 
     * @return true if persist successfully.
     * @throws Exception TODO
     */
    public boolean persist() throws Exception;

    /**
     * DOC bZhou Comment method "clear".
     * 
     * @return true if clear successfully.
     */
    public boolean clear();

    /**
     * DOC bzhou Comment method "getOrder".
     * 
     * Use to manage task order. Tasks are sorted (ASC) by this date, then executed following this order.
     * 
     * @return
     */
    public Date getOrder();

    /**
     * DOC bZhou Comment method "getTaskCategory".
     * 
     * @return
     */
    public MigrationTaskCategory getTaskCategory();

    /**
     * DOC bZhou Comment method "getMigrationTaskType".
     * 
     * @return
     */
    public MigrationTaskType getMigrationTaskType();

    /**
     * 
     * DOC mzhao Verify if the task is a model (CWM) task by comparing the task id.
     * 
     * @param taskId
     * @return true if the task id is match.
     */
    public Boolean isModelTask();
}
