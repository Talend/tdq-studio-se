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

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.indicators.ColumnSetIndicatorEvaluator;
import org.talend.dq.indicators.ColumnSetIndicatorEvaluatorWithSampleData;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class ColumnSetAnalysisExecutorWithSampleData extends ColumnSetAnalysisExecutor {

    /**
     * DOC zshen ColumnSetAnalysisExecutorWithSampleData constructor comment.
     * 
     * @param isDelimitedFile
     * @param isMdm
     */
    public ColumnSetAnalysisExecutorWithSampleData(boolean isDelimitedFile, boolean isMdm) {
        super(isDelimitedFile, isMdm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.ColumnSetAnalysisExecutor#createSqlStatement(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    protected String createSqlStatement(Analysis analysis) {
        return StringUtils.EMPTY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.ColumnSetAnalysisExecutor#createIndicatorEvaluator(org.talend.dataquality.analysis.Analysis)
     */
    @Override
    protected ColumnSetIndicatorEvaluator createIndicatorEvaluator(Analysis analysis) {
        return new ColumnSetIndicatorEvaluatorWithSampleData(analysis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.analysis.ColumnSetAnalysisExecutor#initConnection(org.talend.dataquality.analysis.Analysis,
     * org.talend.dq.indicators.ColumnSetIndicatorEvaluator)
     */
    @Override
    protected TypedReturnCode<Connection> initConnection(Analysis analysis, ColumnSetIndicatorEvaluator eval) {
        return new TypedReturnCode<Connection>();
    }

}
