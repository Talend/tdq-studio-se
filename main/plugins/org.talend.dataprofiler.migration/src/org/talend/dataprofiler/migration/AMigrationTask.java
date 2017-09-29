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

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public abstract class AMigrationTask implements IMigrationTask {

    private static Logger log = Logger.getLogger(AMigrationTask.class);

    private String id;

    private String name;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#execute()
     */
    @Override
    public final boolean execute() {
        try {
            return doExecute() && persist();
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            clear();
        }

        return false;
    }

    /**
     * DOC bZhou Comment method "createDate".
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected Date createDate(int year, int month, int day) {
        Calendar calender = Calendar.getInstance();
        calender.set(year, month, day);
        return calender.getTime();
    }

    /**
     * DOC bZhou Comment method "doExecute".
     * 
     * @return TODO
     * 
     * @throws Exception TODO
     * @throws SQLException
     */
    protected abstract boolean doExecute() throws Exception;

    @Override
    public Boolean isModelTask() {
        return "org.talend.dataprofiler.core.migration.impl.MergeMetadataTask".equals(getId())//$NON-NLS-1$
                || "org.talend.dataprofiler.core.migration.impl.ExchangeFileNameToReferenceTask".equals(getId())//$NON-NLS-1$
                || "org.talend.dataprofiler.core.migration.impl.UpdatePropertiesFileTask".equals(getId())//$NON-NLS-1$
                || "org.talend.dataprofiler.core.migration.impl.UpdateAnalysisWithMinLengthIndicator".equals(getId())//$NON-NLS-1$
                || "org.talend.dataprofiler.core.migration.impl.RefactMdmMetadataTask".equals(getId())//$NON-NLS-1$
                || "org.talend.dataprofiler.core.migration.impl.UpdateFileAfterMergeConnectionTask".equals(getId())//$NON-NLS-1$
                || "update the value of path on the property files".equals(getId())//$NON-NLS-1$
                || "org.talend.dataprofiler.core.changeBinFrequanceUUID".equals(getId()) //$NON-NLS-1$
                || "org.talend.dataprofiler.core.tdq.migration.UpdateReportUserDefinedJrxmlFilePathTask".equals(getId()) //$NON-NLS-1$
                || "org.talend.dataprofiler.core.migration.impl.UpdateAggrDefinitionTask".equals(getId()) //$NON-NLS-1$
                || "org.talend.dataprofiler.core.migration.impl.UpdateMeanAggrDefinitionTask".equals(getId()); //$NON-NLS-1$
    }
}
