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
 * @author scorreia
 * 
 * This migration task removes the call to the Oracle TRIM function when computing the length indicators
 */
public class UpdateOracleTextIndicatorQueryTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        TalendDefinitionFileUpdate talendDefinitionFileUpdate = new TalendDefinitionFileUpdate();
        talendDefinitionFileUpdate
                .add(
                        "<sqlGenericExpression xmi:id=\"_ybtTIDh8Ed2XmO7pl5Yuyg\" body=\"SELECT MIN(LENGTH(TRIM('XX' || &lt;%=__COLUMN_NAMES__%>))) - LENGTH('XX') FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>\" language=\"Oracle\"/>", //$NON-NLS-1$
                        "<sqlGenericExpression xmi:id=\"_ybtTIDh8Ed2XmO7pl5Yuyg\" body=\"SELECT MIN(LENGTH('XX' || &lt;%=__COLUMN_NAMES__%>)) - LENGTH('XX') FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>\" language=\"Oracle\"/>"); //$NON-NLS-1$
        talendDefinitionFileUpdate
                .add(
                        "<sqlGenericExpression xmi:id=\"_ybt6Mjh8Ed2XmO7pl5Yuyg\" body=\"SELECT MAX(LENGTH(TRIM('XX' || &lt;%=__COLUMN_NAMES__%>))) - LENGTH('XX') FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>\" language=\"Oracle\"/>", //$NON-NLS-1$
                        "<sqlGenericExpression xmi:id=\"_ybt6Mjh8Ed2XmO7pl5Yuyg\" body=\"SELECT MAX(LENGTH('XX' || &lt;%=__COLUMN_NAMES__%>)) - LENGTH('XX') FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>\" language=\"Oracle\"/>"); //$NON-NLS-1$
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
        return createDate(2010, 04, 19);
    }

}
