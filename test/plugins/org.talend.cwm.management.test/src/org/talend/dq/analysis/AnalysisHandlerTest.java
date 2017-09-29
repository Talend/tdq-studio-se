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

import org.junit.Assert;
import org.junit.Test;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionInformations;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class AnalysisHandlerTest {

    /**
     * Test method for {@link org.talend.dq.analysis.AnalysisHandler#getDefaultLoadedRowCount()}.
     */
    @Test
    public void testGetDefaultLoadedRowCount() {
        int rowCountValue = 100;
        Analysis createAna = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        createAna.setResults(createAnalysisResult);
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        AnalysisHandler createHandler = AnalysisHandler.createHandler(createAna);

        // default case the value should be 50
        String defaultLoadedRowCount = createHandler.getDefaultLoadedRowCount();
        Assert.assertEquals(Integer.valueOf(50), Integer.valueOf(defaultLoadedRowCount));

        // after setting the value the result should be changed
        TaggedValueHelper.setTaggedValue(createAna, TaggedValueHelper.PREVIEW_ROW_NUMBER, String.valueOf(rowCountValue));
        defaultLoadedRowCount = createHandler.getDefaultLoadedRowCount();
        Assert.assertEquals(Integer.valueOf(rowCountValue), Integer.valueOf(defaultLoadedRowCount));
    }

    /**
     * Test method for {@link org.talend.dq.analysis.AnalysisHandler#changeDefaultRowLoaded(java.lang.String)}.
     */
    @Test
    public void testChangeDefaultRowLoaded() {
        int rowCountValue = 100;
        Analysis createAna = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        createAna.setResults(createAnalysisResult);
        ExecutionInformations createExecutionInformations = AnalysisFactory.eINSTANCE.createExecutionInformations();
        createAnalysisResult.setResultMetadata(createExecutionInformations);
        AnalysisHandler createHandler = AnalysisHandler.createHandler(createAna);

        createHandler.changeDefaultRowLoaded(String.valueOf(rowCountValue));
        String defaultLoadedRowCount = createHandler.getDefaultLoadedRowCount();
        Assert.assertEquals(Integer.valueOf(rowCountValue), Integer.valueOf(defaultLoadedRowCount));
    }

}
