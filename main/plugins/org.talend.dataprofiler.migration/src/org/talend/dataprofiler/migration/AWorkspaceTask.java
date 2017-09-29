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

/**
 * DOC Administrator class global comment. Detailled comment
 */
public abstract class AWorkspaceTask extends AMigrationTask implements IWorkspaceMigrationTask {

    private String version;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getVersion()
     */
    public String getVersion() {
        return version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#setVersion(java.lang.String)
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getTaskCategory()
     */
    public MigrationTaskCategory getTaskCategory() {
        return MigrationTaskCategory.WORKSPACE;
    }
}
