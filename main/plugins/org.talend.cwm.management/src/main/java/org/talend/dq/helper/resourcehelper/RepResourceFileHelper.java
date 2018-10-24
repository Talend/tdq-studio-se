// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.helper.resourcehelper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dataquality.reports.util.ReportsSwitch;
import org.talend.resource.ResourceManager;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC rli class global comment. Detailled comment
 */
public final class RepResourceFileHelper extends ResourceFileMap<TdReport> {

    private static RepResourceFileHelper instance;

    ReportsSwitch<TdReport> reportSwitch = new ReportsSwitch<TdReport>() {

        @Override
        public TdReport caseTdReport(TdReport object) {
            return object;
        }
    };

    private RepResourceFileHelper() {
        super();
    }

    public static RepResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new RepResourceFileHelper();
        }
        return instance;
    }

    public TdReport findReport(IFile file) {
        if (checkFile(file)) {
            return getModelElement(file);
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "getAnalysisFilesFromReport".
     * 
     * Get all analysis files used by a Report.
     * 
     * @param report
     * @return
     */
    public List<IFile> getAnalysisFilesFromReport(Report report) {
        List<IFile> analysisFiles = new ArrayList<IFile>();

        // MOD yyin 20120530 TDQ-5050
        for (AnalysisMap anaMap : ((TdReport) report).getAnalysisMap()) {
            Analysis analysis = anaMap.getAnalysis();
            analysisFiles.add(ResourceFileMap.findCorrespondingFile(analysis));
        }

        return analysisFiles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#checkFile(org.eclipse.core.resources.IFile)
     */
    @Override
    protected boolean checkFile(IFile file) {
        return file != null && FactoriesUtil.REP.equalsIgnoreCase(file.getFileExtension());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#getTypedFolder()
     */
    @Override
    public IFolder getTypedFolder() {
        return ResourceManager.getReportsFolder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#doSwitch(org.eclipse.emf.ecore.EObject)
     */
    @Override
    public TdReport doSwitch(EObject object) {
        return reportSwitch.doSwitch(object);
    }

}
