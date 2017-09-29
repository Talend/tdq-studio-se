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

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.TalendDefinitionFileUpdate;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class SoundexIndicatorQueryTask extends AbstractWorksapceUpdateTask {

    private final String oldSoundexQuery = "ORDER BY d,c DESC"; //$NON-NLS-1$

    private final String newSoundexQuery = "ORDER BY d DESC,c DESC"; //$NON-NLS-1$

    private final String oldPSoundexQuery = "ORDER BY COUNT(DISTINCT &lt;%=__COLUMN_NAMES__%>) , COUNT(*) DESC"; //$NON-NLS-1$

    private final String newPSoundexQuery = "ORDER BY COUNT(DISTINCT &lt;%=__COLUMN_NAMES__%>) DESC , COUNT(*) DESC"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        TalendDefinitionFileUpdate talendDefinitionFileUpdate = new TalendDefinitionFileUpdate();
        talendDefinitionFileUpdate.add(this.oldSoundexQuery, this.newSoundexQuery);
        talendDefinitionFileUpdate.add(this.oldPSoundexQuery, this.newPSoundexQuery);
        return talendDefinitionFileUpdate.replace(this.getClass().getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2009, 10, 23);
    }

}
