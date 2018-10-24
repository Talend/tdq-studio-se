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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.util.AnalysisSwitch;
import org.talend.resource.ResourceManager;

/**
 * DOC rli class global comment. Detailled comment
 */
public final class AnaResourceFileHelper extends ResourceFileMap<Analysis> {

    private static AnaResourceFileHelper instance;

    AnalysisSwitch<Analysis> analysisSwitch = new AnalysisSwitch<Analysis>() {

        @Override
        public Analysis caseAnalysis(Analysis object) {
            return object;
        }
    };

    private AnaResourceFileHelper() {
        super();
    }

    public static AnaResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new AnaResourceFileHelper();
        }
        return instance;
    }

    /**
     * DOC bZhou Comment method "findAnalysis".
     * 
     * @param file
     * @return
     */
    public Analysis findAnalysis(IFile file) {
        if (checkFile(file)) {
            return getModelElement(file);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#checkFile(org.eclipse.core.resources.IFile)
     */
    @Override
    protected boolean checkFile(IFile file) {
        return file != null && FactoriesUtil.ANA.equalsIgnoreCase(file.getFileExtension());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#getTypedFolder()
     */
    @Override
    public IFolder getTypedFolder() {
        return ResourceManager.getAnalysisFolder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#doSwitch(org.eclipse.emf.ecore.EObject)
     */
    @Override
    public Analysis doSwitch(EObject object) {
        return analysisSwitch.doSwitch(object);
    }

}
