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
package org.talend.dq.analysis;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.dataquality.helpers.AnalysisBaseBuilder;
import org.talend.dq.writer.impl.AnalysisWriter;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.ReturnCode;

/**
 * @author scorreia
 * 
 * Creates an Analysis and its attribute from the given informations. Use one AnalysisBuilder per Analysis to create.
 */
public class AnalysisBuilder extends AnalysisBaseBuilder {

    private static Logger log = Logger.getLogger(AnalysisBuilder.class);

    /**
     * Method "saveAnalysis". This method should be called when the creation of the analysis definition (analysis
     * context + parameters) is created. It is not safe to use it for saving the analysis after the evaluation of the
     * analysis result. The domain should be saved before calling this method.
     * 
     * @param folder the folder where the analysis is saved.
     * @return true if saved without problem
     */
    public boolean saveAnalysis(IFolder folder) {
        assert analysis != null;
        AnalysisWriter writer = ElementWriterFactory.getInstance().createAnalysisWrite();
        String filename = DqRepositoryViewService.createFilename(this.analysis.getName(), FactoriesUtil.ANA);
        IFile file = folder.getFile(filename);
        ReturnCode saved = writer.create(analysis, folder);
        if (saved.isOk()) {
            if (log.isDebugEnabled()) {
                log.debug("Saved in  " + file.getFullPath());//$NON-NLS-1$
            }
        }
        return saved.isOk();
    }
}
