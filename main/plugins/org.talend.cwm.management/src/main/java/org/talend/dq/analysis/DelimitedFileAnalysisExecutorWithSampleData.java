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

import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.indicators.DelimitedFileIndicatorEvaluator;
import org.talend.dq.indicators.DelimitedFileIndicatorEvaluatorWithSampleData;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class DelimitedFileAnalysisExecutorWithSampleData extends DelimitedFileAnalysisExecutor {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dq.analysis.DelimitedFileAnalysisExecutor#createIndicatorEvaluator(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    protected DelimitedFileIndicatorEvaluator createIndicatorEvaluator(Analysis analysis) {
        return new DelimitedFileIndicatorEvaluatorWithSampleData(analysis);
    }

}
