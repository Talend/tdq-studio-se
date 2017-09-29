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
package org.talend.dq.analysis.explore;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.indicators.IQRIndicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.LowerQuartileIndicator;
import org.talend.dataquality.indicators.MaxValueIndicator;
import org.talend.dataquality.indicators.MeanIndicator;
import org.talend.dataquality.indicators.MedianIndicator;
import org.talend.dataquality.indicators.MinValueIndicator;
import org.talend.dataquality.indicators.RangeIndicator;
import org.talend.dataquality.indicators.UpperQuartileIndicator;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentFactory;

/**
 * created by xqliu on Jul 16, 2013 Detailled comment
 * 
 */
public class SummaryStastictisExplorerRealTest {

    /**
     * Test method for {@link org.talend.dq.analysis.explore.SummaryStastictisExplorer#getQueryMap()}.
     */
    @Test
    public void testGetQueryMap() {
        // testing code for SQL Engine here
        SummaryStastictisExplorer summaryExplorerSql = new SummaryStastictisExplorer();

        Analysis analysisReal = AnalysisFactory.eINSTANCE.createAnalysis();

        AnalysisParameters analysisParametersReal = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        analysisParametersReal.setExecutionLanguage(ExecutionLanguage.SQL);
        analysisReal.setParameters(analysisParametersReal);

        AnalysisContext analysisContextReal = AnalysisFactory.eINSTANCE.createAnalysisContext();
        DataManager dataManagerReal = SoftwaredeploymentFactory.eINSTANCE.createDataManager();
        analysisContextReal.setConnection(dataManagerReal);
        analysisReal.setContext(analysisContextReal);
        summaryExplorerSql.setAnalysis(analysisReal);

        TdColumn tdColumnReal = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdColumn();
        TdTable tdTableReal = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdTable();
        tdTableReal.setName("tableName"); //$NON-NLS-1$
        tdColumnReal.setOwner(tdTableReal);
        tdColumnReal.setName("id");//$NON-NLS-1$

        TdSqlDataType tdsqlReal = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdSqlDataType();
        tdsqlReal.setName("Integer"); //$NON-NLS-1$
        tdColumnReal.setSqlDataType(tdsqlReal);

        // test for MeanIndicator
        MeanIndicator meanIndicator = IndicatorsFactory.eINSTANCE.createMeanIndicator();
        meanIndicator.setAnalyzedElement(tdColumnReal);
        IndicatorDefinition  meanIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        meanIndicatorDef.setName("Mean");
        meanIndicator.setIndicatorDefinition(meanIndicatorDef);

        ChartDataEntity entityReal = new ChartDataEntity();
        entityReal.setValue("0"); //$NON-NLS-1$
        entityReal.setIndicator(meanIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        Map<String, String> queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for MedianIndicator
        MedianIndicator medianIndicator = IndicatorsFactory.eINSTANCE.createMedianIndicator();
        medianIndicator.setAnalyzedElement(tdColumnReal);
        IndicatorDefinition  medianIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        medianIndicatorDef.setName("Median");
        medianIndicator.setIndicatorDefinition(medianIndicatorDef);

        entityReal.setIndicator(medianIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.size() == 1);

        // test for IQRIndicator
        MaxValueIndicator maxValueIndicator = IndicatorsFactory.eINSTANCE.createMaxValueIndicator();
        maxValueIndicator.setValue("80"); //$NON-NLS-1$
        IndicatorDefinition  maxValueIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        maxValueIndicatorDef.setName("Maximum");
        maxValueIndicator.setIndicatorDefinition(maxValueIndicatorDef);
        
        MinValueIndicator minValueIndicator = IndicatorsFactory.eINSTANCE.createMinValueIndicator();
        minValueIndicator.setValue("20"); //$NON-NLS-1$
        IndicatorDefinition  minValueIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        minValueIndicatorDef.setName("Minimum");
        minValueIndicator.setIndicatorDefinition(minValueIndicatorDef);

        IQRIndicator iqrIndicator = IndicatorsFactory.eINSTANCE.createIQRIndicator();
        iqrIndicator.setUpperValue(maxValueIndicator);
        iqrIndicator.setLowerValue(minValueIndicator);
        iqrIndicator.setAnalyzedElement(tdColumnReal);
        IndicatorDefinition  iqrIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        iqrIndicatorDef.setName("Inter Quartile Range");
        iqrIndicator.setIndicatorDefinition(iqrIndicatorDef);
        

        entityReal.setIndicator(iqrIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.size() == 2);

        // test for LowerQuartileIndicator
        LowerQuartileIndicator lowerQuartileIndicator = IndicatorsFactory.eINSTANCE.createLowerQuartileIndicator();
        lowerQuartileIndicator.setAnalyzedElement(tdColumnReal);
        IndicatorDefinition  lowerQuartileIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        lowerQuartileIndicatorDef.setName("Lower Quartile");
        lowerQuartileIndicator.setIndicatorDefinition(lowerQuartileIndicatorDef);

        entityReal.setIndicator(lowerQuartileIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.size() == 1);

        // test for UpperQuartileIndicator
        UpperQuartileIndicator upperQuartileIndicator = IndicatorsFactory.eINSTANCE.createUpperQuartileIndicator();
        upperQuartileIndicator.setAnalyzedElement(tdColumnReal);
        IndicatorDefinition  upperQuartileIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        upperQuartileIndicatorDef.setName("Upper Quartile");
        upperQuartileIndicator.setIndicatorDefinition(upperQuartileIndicatorDef);

        entityReal.setIndicator(upperQuartileIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.size() == 1);

        // test for RangeIndicator
        RangeIndicator rangeIndicator = IndicatorsFactory.eINSTANCE.createRangeIndicator();
        rangeIndicator.setUpperValue(maxValueIndicator);
        rangeIndicator.setLowerValue(minValueIndicator);
        rangeIndicator.setAnalyzedElement(tdColumnReal);
        IndicatorDefinition  rangeIndicatorDef = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        rangeIndicatorDef.setName("Range");
        rangeIndicator.setIndicatorDefinition(rangeIndicatorDef);

        entityReal.setIndicator(rangeIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.size() == 2);

        // test for MinValueIndicator
        minValueIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(minValueIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.size() == 1);

        // test for MaxValueIndicator
        maxValueIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(maxValueIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.size() == 1);

        // testing code for Java Engine here
        analysisParametersReal.setExecutionLanguage(ExecutionLanguage.JAVA);
        analysisReal.setParameters(analysisParametersReal);
        summaryExplorerSql.setAnalysis(analysisReal);

        // test for MeanIndicator
        meanIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(meanIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for MedianIndicator
        medianIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(medianIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for IQRIndicator
        iqrIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(iqrIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for LowerQuartileIndicator
        lowerQuartileIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(lowerQuartileIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for UpperQuartileIndicator
        upperQuartileIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(upperQuartileIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for RangeIndicator
        rangeIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(rangeIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for MinValueIndicator
        minValueIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(minValueIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());

        // test for MaxValueIndicator
        maxValueIndicator.setAnalyzedElement(tdColumnReal);

        entityReal.setIndicator(maxValueIndicator);
        summaryExplorerSql.setEnitty(entityReal);

        queryMap = summaryExplorerSql.getQueryMap();
        Assert.assertTrue(queryMap.isEmpty());
    }
}
