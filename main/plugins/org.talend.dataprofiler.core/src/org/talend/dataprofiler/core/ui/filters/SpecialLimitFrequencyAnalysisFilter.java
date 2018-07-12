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
package org.talend.dataprofiler.core.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.BenfordLawFrequencyIndicator;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.LowFrequencyIndicator;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class SpecialLimitFrequencyAnalysisFilter extends ViewerFilter {

    private int freResultLimit = 10;

    private int lowFreResultLimit = 10;

    public SpecialLimitFrequencyAnalysisFilter(int freResultLimit, int lowFreResultLimit) {
        this.freResultLimit = freResultLimit;
        this.lowFreResultLimit = lowFreResultLimit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof AnalysisRepNode) {
            if (ERepositoryStatus.isLock(((AnalysisRepNode) element).getObject().getRepositoryStatus())) {
                return false;
            }
            return isNotDefaultLimitAnaNode((AnalysisRepNode) element);
        }

        return true;
    }

    private boolean isNotDefaultLimitAnaNode(IRepositoryNode repositoryNode) {
        if (repositoryNode instanceof AnalysisRepNode) {
            Analysis analysis = ((AnalysisRepNode) repositoryNode).getAnalysis();
            for (Indicator indicator : analysis.getResults().getIndicators()) {
                // BenfordLawFrequencyIndicator can not special the number of shown
                if (indicator instanceof BenfordLawFrequencyIndicator) {
                    continue;
                }
                if (indicator instanceof LowFrequencyIndicator
                        && indicator.getParameters().getTopN() != lowFreResultLimit && lowFreResultLimit > 0) {
                    return true;
                }
                // too many frequency type
                if (isNotLowFrequencyIndicator(indicator) && indicator.getParameters().getTopN() != freResultLimit
                        && freResultLimit > 0) {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean isNotLowFrequencyIndicator(Indicator indicator) {
        return indicator instanceof FrequencyIndicator && !(indicator instanceof LowFrequencyIndicator);
    }
}
