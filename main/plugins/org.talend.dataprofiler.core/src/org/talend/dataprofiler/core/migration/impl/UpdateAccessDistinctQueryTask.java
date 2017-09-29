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
 * @author yyi
 * 
 * To update access distince query
 */
public class UpdateAccessDistinctQueryTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        // MOD msjian 2011-6-7 22021 fixed: this task return default value should be true.
        boolean rc = true;
        String categorie = "categories=\"_ccKHEBF2Ed2PKb6nEJEvhw\" label=\"Distinct Count\">"; //$NON-NLS-1$

        String accessQuery = "    <sqlGenericExpression xmi:id=\"_ybqP0jh8Ed2XmO7pl5Yuyl\" body=\"SELECT COUNT (&lt;%=__COLUMN_NAMES__%>) FROM (SELECT DISTINCT (&lt;%=__COLUMN_NAMES__%>) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>)\" language=\"Access\"/>"; //$NON-NLS-1$

        TalendDefinitionFileUpdate talendDefinitionFileUpdate = new TalendDefinitionFileUpdate();
        if (-1 == talendDefinitionFileUpdate.indexOf(accessQuery)) {
            talendDefinitionFileUpdate.add(categorie, categorie + System.getProperty("line.separator") + accessQuery); //$NON-NLS-1$
            rc = talendDefinitionFileUpdate.replace(this.getClass().getName());
        }

        return rc;
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
        return createDate(2010, 05, 04);
    }

}
