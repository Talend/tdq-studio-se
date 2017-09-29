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
package org.talend.dataprofiler.core.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dq.nodes.AnalysisFolderRepNode;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.repository.model.IRepositoryNode.EProperties;

/**
 * this filter is used in the select analysis dialog.
 */
public class SelectAnalysisDialogNodeFilter extends ViewerFilter {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof AnalysisSubFolderRepNode) {
            if (((AnalysisSubFolderRepNode) element).getProperties(EProperties.LABEL).equals(
                    ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT)) {
                return true;
            } else {
                // filter "analyzed elements" folders
                return false;
            }
        }

        if (element instanceof AnalysisFolderRepNode || element instanceof AnalysisRepNode) {
            return true;
        }

        return false;
    }
}
