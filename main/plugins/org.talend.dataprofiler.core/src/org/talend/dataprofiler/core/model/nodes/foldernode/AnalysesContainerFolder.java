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
package org.talend.dataprofiler.core.model.nodes.foldernode;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.nodes.foldernode.AbstractFolderNode;

/**
 * This folder container will store all the analyses related with special report.
 */
public class AnalysesContainerFolder extends AbstractFolderNode implements IWorkbenchAdapter {

    private static final String ANALYSES = DefaultMessagesImpl.getString("AnalysesContainerFolder.analyses"); //$NON-NLS-1$

    /**
     * DOC rli AnalysesFolder constructor comment.
     * 
     * @param name
     */
    public AnalysesContainerFolder(TdReport report) {
        super(ANALYSES);
        this.setParent(report);
        this.loadChildren();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.model.nodes.foldernode.AbstractFolderNode#loadChildren()
     */
    @Override
    public void loadChildren() {
        List<Analysis> analyses = ReportHelper.getAnalyses((TdReport) getParent());
        this.setChildren(analyses.toArray());
    }

    public Object[] getChildren(Object o) {
        loadChildren();
        return this.getChildren();
    }

    public ImageDescriptor getImageDescriptor(Object object) {
        return ImageLib.getImageDescriptor(ImageLib.FOLDERNODE_IMAGE);
    }

    public String getLabel(Object o) {
        return ANALYSES;
    }

    public Object getParent(Object o) {
        return null;
    }

}
