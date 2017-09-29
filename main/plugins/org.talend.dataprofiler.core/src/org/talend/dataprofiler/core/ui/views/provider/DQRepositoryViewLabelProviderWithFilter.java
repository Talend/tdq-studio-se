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
package org.talend.dataprofiler.core.ui.views.provider;

import org.apache.commons.lang.StringUtils;
import org.talend.dataprofiler.core.ui.filters.SpecialLimitFrequencyAnalysisFilter;
import org.talend.dq.nodes.AnalysisFolderRepNode;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class DQRepositoryViewLabelProviderWithFilter extends DQRepositoryViewLabelProvider {

    SpecialLimitFrequencyAnalysisFilter filter = null;

    public DQRepositoryViewLabelProviderWithFilter(SpecialLimitFrequencyAnalysisFilter filter) {
        this.filter = filter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {

        if (element instanceof AnalysisFolderRepNode) {
            AnalysisFolderRepNode subFolderNode = (AnalysisFolderRepNode) element;
            String label = StringUtils.EMPTY;
            if (subFolderNode.getObject() != null) {
                label = subFolderNode.getObject().getLabel();
            } else {
                label = subFolderNode.getLabel();
            }
            return label + "(" + subFolderNode.getChildrenAll(false, filter).size() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return super.getText(element);
    }

}
