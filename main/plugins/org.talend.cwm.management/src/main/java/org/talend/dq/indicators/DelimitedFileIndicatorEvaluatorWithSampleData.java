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
package org.talend.dq.indicators;

import java.io.IOException;

import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.helper.AnalysisExecutorHelper;
import org.talend.fileprocess.FileInputDelimited;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class DelimitedFileIndicatorEvaluatorWithSampleData extends DelimitedFileIndicatorEvaluator {

    /**
     * DOC zshen DelimitedFileIndicatorEvaluatorWithSampleData constructor comment.
     * 
     * @param analysis
     */
    public DelimitedFileIndicatorEvaluatorWithSampleData(Analysis analysis) {
        super(analysis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.DelimitedFileIndicatorEvaluator#createFileInputDelimited()
     */
    @Override
    protected FileInputDelimited createFileInputDelimited() throws IOException {
        return AnalysisExecutorHelper.createFileInputDelimited(delimitedFileconnection, getLimitNumber(this.analysis));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.indicators.DelimitedFileIndicatorEvaluator#getCsvReaderLimit()
     */
    @Override
    protected int getCsvReaderLimit() {
        int connLimit = super.getCsvReaderLimit();
        int analysisLimit = getLimitNumber(this.analysis);
        if (connLimit <= 0) {
            return analysisLimit;
        }
        return analysisLimit < connLimit ? analysisLimit : connLimit;
    }

    /**
     * DOC zshen Comment method "getLimitNumber".
     * 
     * @param findAnalysis
     * @return
     */
    private int getLimitNumber(Analysis findAnalysis) {
        String valueString = TaggedValueHelper.getValueString(TaggedValueHelper.PREVIEW_ROW_NUMBER, findAnalysis);
        Integer limitNumber = -1;
        try {
            limitNumber = Integer.valueOf(valueString);
        } catch (NumberFormatException e) {
            // there keep limitNumber is zero because of when this value is less than or same with zero then mean that no limit
            // here
        }
        return limitNumber;
    }

}
