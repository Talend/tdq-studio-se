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

import java.util.Calendar;
import java.util.Date;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public abstract class AProjectTask extends AMigrationTask implements IProjectMigrationTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#valid()
     */
    public boolean valid() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#persist()
     */
    public boolean persist() throws Exception {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#clear()
     */
    public boolean clear() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getTaskCategory()
     */
    public MigrationTaskCategory getTaskCategory() {
        return MigrationTaskCategory.PROJECT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        Calendar calender = Calendar.getInstance();
        // month - the value used to set the MONTH calendar field. Month value is 0-based. e.g., 0 for January.
        calender.set(2999, 11, 12);
        return calender.getTime();
    }
}
