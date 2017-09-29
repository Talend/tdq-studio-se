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

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.resource.EResourceConstant;

/**
 * DOC update prv and ana files because of the renameing of classes: TdXMLDocument --> TdXmlSchema , TdXMLElement -->
 * TdXmlElementType.
 */
public class RefactMdmMetadataTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(RefactMdmMetadataTask.class);

    private Map<String, String> replaceStringMapMdm;

    public Map<String, String> getReplaceStringMapMdm() {
        if (this.replaceStringMapMdm == null) {
            this.replaceStringMapMdm = initReplaceStringMapMdm();
        }
        return this.replaceStringMapMdm;
    }

    /**
     * DOC xqliu Comment method "initReplaceStringMapMdm".
     * 
     * @return
     */
    private Map<String, String> initReplaceStringMapMdm() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("TdXMLDocument", "TdXmlSchema"); //$NON-NLS-1$ //$NON-NLS-2$
        result.put("TdXMLElement", "TdXmlElementType"); //$NON-NLS-1$ //$NON-NLS-2$
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;

        File fileAnalysis = this.getWorkspacePath().append(EResourceConstant.ANALYSIS.getPath()).toFile();

        try {
            String[] anaFileExtentionNames = { ".ana" }; //$NON-NLS-1$
            result = FilesUtils.migrateFolder(fileAnalysis, anaFileExtentionNames, this.getReplaceStringMapMdm(), log);
            // AnaResourceFileHelper.getInstance().clear();
            // AnaResourceFileHelper.getInstance().getAllAnalysis();
        } catch (Exception e) {
            result = false;
            log.error(e, e);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        // MOD xqliu 2010-09-15 bug 13941: this task must be called before SplitSysIndicatorTask
        // return createDate(2010, 8, 9);
        return createDate(2010, 7, 6);
    }

}
