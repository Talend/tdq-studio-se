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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.ReportFileHelper;
import org.talend.dq.helper.ReportFileHelper.ReportListParameters;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * use relative path to record the report generated docs.
 */
public class UpdateReportGeneratedDocPathTask extends AbstractWorksapceUpdateTask {

    public Date getOrder() {
        return createDate(2012, 11, 26);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        List<? extends ModelElement> allElement = RepResourceFileHelper.getInstance().getAllElement();
        for (ModelElement me : allElement) {
            if (me instanceof Report) {
                IFile reportFile = ModelElementHelper.getIFile(me);
                if (reportFile != null) {
                    File reportListFile = ReportFileHelper.reportListFile(reportFile);
                    if (reportListFile.exists() && reportListFile.isFile()) {
                        // get the report output folder
                        File parentFile = reportListFile.getParentFile();
                        // get the ReportListParameters from the .report.list file
                        List<ReportListParameters> reportListParameters = ReportFileHelper.getReportListParameters(reportListFile);
                        // update old path
                        if (!reportListParameters.isEmpty()) {
                            String parentPath = parentFile.getAbsolutePath();
                            for (ReportListParameters repParam : reportListParameters) {
                                String oldPath = repParam.getPath();
                                if (oldPath.startsWith(parentPath)) {
                                    repParam.setPath(".." + oldPath.substring(parentPath.length(), oldPath.length())); //$NON-NLS-1$
                                }
                            }
                        }
                        // check the report folder, if there have generated docs, add them to the .report.list file
                        // also(need to remove the duplicated items)
                        File[] listFiles = parentFile.listFiles();
                        List<ReportListParameters> localParameters = new ArrayList<ReportListParameters>();
                        for (File file : listFiles) {
                            String fileName = file.getName();
                            if (file.isFile() && !fileName.startsWith(".")) { //$NON-NLS-1$
                                String name = fileName.substring(0, fileName.lastIndexOf(".")); //$NON-NLS-1$
                                localParameters.add(ReportFileHelper.buildRepListParams(name, ".." + fileName, //$NON-NLS-1$
                                        String.valueOf(System.currentTimeMillis())));
                            }
                        }
                        // the list contains all the ReportListParameters
                        List<ReportListParameters> allReportListParameters = new ArrayList<ReportListParameters>();
                        for (ReportListParameters param : reportListParameters) {
                            boolean saveMe = true;
                            String path = param.getPath();
                            for (ReportListParameters param2 : localParameters) {
                                if (path.endsWith(param2.getPath().substring(2))) {
                                    saveMe = false;
                                    break;
                                }
                            }
                            if (saveMe) {
                                allReportListParameters.add(param);
                            }
                        }
                        allReportListParameters.addAll(localParameters);
                        // save new path
                        ReportFileHelper.saveReportListFile(reportListFile, allReportListParameters);
                    }
                }
            }
        }
        return true;
    }
}
