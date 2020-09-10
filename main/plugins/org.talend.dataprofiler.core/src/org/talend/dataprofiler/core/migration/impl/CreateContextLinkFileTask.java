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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;
import java.util.List;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.context.link.ContextLinkService;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;

/**
 * created by msjian on 2020年5月7日
 * TDQ-18173: create context link files migration, so that can auto propagation context when have changes
 *
 */
public class CreateContextLinkFileTask extends AbstractWorksapceUpdateTask {


    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        // all connections
        saveContextLinkFor(ERepositoryObjectType.METADATA_CONNECTIONS);
        // all anaslyses
        saveContextLinkFor(ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
        // all reports
        saveContextLinkFor(ERepositoryObjectType.TDQ_REPORT_ELEMENT);

        return true;
    }

    /**
     * DOC msjian Comment method "saveContextLinkFor".
     * 
     * @throws PersistenceException
     */
    private void saveContextLinkFor(ERepositoryObjectType type) throws PersistenceException {
        List<IRepositoryViewObject> allTypeObjects = ProxyRepositoryFactory.getInstance().getAll(type);

        for (IRepositoryViewObject object : allTypeObjects) {
            ContextLinkService.getInstance().saveContextLink(object.getProperty().getItem());
        }
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
        return createDate(2020, 9, 10);
    }

}
