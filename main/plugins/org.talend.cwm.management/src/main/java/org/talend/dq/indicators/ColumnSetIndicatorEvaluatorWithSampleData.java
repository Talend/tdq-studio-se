// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.indicators;

import java.sql.SQLException;
import java.sql.Statement;

import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.helper.SampleDataUtils;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class ColumnSetIndicatorEvaluatorWithSampleData extends ColumnSetIndicatorEvaluator {

    /**
     * DOC zshen ColumnSetIndicatorEvaluatorWithSampleData constructor comment.
     *
     * @param analysis
     */
    public ColumnSetIndicatorEvaluatorWithSampleData(Analysis analysis) {
        super(analysis);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dq.indicators.Evaluator#createStatement()
     */
    @Override
    protected Statement createStatement() throws SQLException {
        return SampleDataUtils.getInstance().getSampleDataStatement(this.analysis);
    }

}
