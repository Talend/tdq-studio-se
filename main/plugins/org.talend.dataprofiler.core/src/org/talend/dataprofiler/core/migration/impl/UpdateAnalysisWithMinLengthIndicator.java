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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.StringUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.resource.EResourceConstant;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class UpdateAnalysisWithMinLengthIndicator extends AbstractWorksapceUpdateTask {

    private FilenameFilter anaFileFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(FactoriesUtil.ANA);
        }
    };

    /**
     * DOC yyi UpdateAnalysisWithMinLengthIndicator constructor comment.
     */
    public UpdateAnalysisWithMinLengthIndicator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {

        File anaFolder = getWorkspacePath().append(EResourceConstant.ANALYSIS.getPath()).toFile();

        List<File> fileList = new ArrayList<File>();

        FilesUtils.getAllFilesFromFolder(anaFolder, fileList, anaFileFilter);

        for (File file : fileList) {
            if (file.exists()) {
                try { // only migrate feature that was renamed, not the type
                    String content = FileUtils.readFileToString(file, EMFUtil.ENCODING);
                    content = StringUtils.replace(content, "<MinLengthIndicator", "<minLengthIndicator"); //$NON-NLS-1$ //$NON-NLS-2$
                    content = StringUtils.replace(content, "</MinLengthIndicator>", "</minLengthIndicator>"); //$NON-NLS-1$ //$NON-NLS-2$
                    FileUtils.writeStringToFile(file, content, EMFUtil.ENCODING);
                } catch (IOException e) {
                    return false;
                }
            }

        }
        return true;
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
        return createDate(2010, 06, 20);
    }

}
