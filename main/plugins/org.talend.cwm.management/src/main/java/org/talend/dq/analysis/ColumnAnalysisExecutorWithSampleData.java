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

import org.apache.commons.lang.StringUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.indicators.IndicatorEvaluator;
import org.talend.dq.indicators.IndicatorEvaluatorWithSampleData;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class ColumnAnalysisExecutorWithSampleData extends ColumnAnalysisExecutor {

    /**
     * DOC zshen ColumnAnalysisExecutorWithSampleData constructor comment.
     */
    public ColumnAnalysisExecutorWithSampleData() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.ColumnAnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    public String createSqlStatement(Analysis analysis) {
        return StringUtils.EMPTY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.ColumnAnalysisExecutor#CreateIndicatorEvaluator(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    protected IndicatorEvaluator CreateIndicatorEvaluator(Analysis analysis) {
        return new IndicatorEvaluatorWithSampleData(analysis);
    }

}
