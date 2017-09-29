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

import static org.junit.Assert.*;

import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisPackage;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPreAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dq.helper.UnitTestBuildHelper;

/**
 * created by zhao on Aug 28, 2013 Detailled comment
 * 
 */
public class MatchAnalysisExecutorTest {

    private DelimitedFileConnection delimitedFileconnection = null;

    private MetadataTable metadataTable = null;

    private MetadataColumn name = null;

    @Before
    public void setUp() throws Exception {
        delimitedFileconnection = ConnectionPackage.eINSTANCE.getConnectionFactory().createDelimitedFileConnection();

    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.MatchAnalysisExecutor#execute(org.talend.dataquality.analysis.Analysis)}.
     */
    @SuppressWarnings("nls")
    @Test
    public void testExecute() {
        MatchAnalysisExecutor matchAnalysisExecutor = new MatchAnalysisExecutor();
        Analysis analysis = AnalysisPackage.eINSTANCE.getAnalysisFactory().createAnalysis();
        AnalysisContext context = AnalysisPackage.eINSTANCE.getAnalysisFactory().createAnalysisContext();
        analysis.setContext(context);

        AnalysisParameters params = AnalysisPackage.eINSTANCE.getAnalysisFactory().createAnalysisParameters();
        analysis.setParameters(params);
        TaggedValueHelper.setTaggedValue(analysis, TaggedValueHelper.PREVIEW_ROW_NUMBER, String.valueOf(100));

        // analysisResult.setAnalysis(analysis);

        context.setConnection(delimitedFileconnection);
        URL fileUrl = this.getClass().getResource("match_test_data"); //$NON-NLS-1$
        metadataTable = UnitTestBuildHelper.getDefault().initFileConnection(fileUrl, delimitedFileconnection);

        this.name = UnitTestBuildHelper.getDefault().initColumns(context, this.metadataTable);

        // Scenario 1
        // - Match key: name, no block key, levenshtein attribute algorithm. groupQualityThreshold = 0.9d, matchInterval
        // = 0.95d .
        double groupQualityThreshold = 0.9d;
        double matchInterval = 0.95d;
        assertScenario1(matchAnalysisExecutor, analysis, name, "name", groupQualityThreshold, matchInterval);
        // Scenario 2
        // - Same to scenario 1, EXCEPT matchInterval = 0.8d .
        matchInterval = 0.8d;
        assertScenario2(matchAnalysisExecutor, analysis, name, "name", groupQualityThreshold, matchInterval);
        // Scenario 3
        // - Same to scenario 2, EXCEPT groupQualityThreshold = 0.95d.
        groupQualityThreshold = 0.95d;
        assertScenario3(matchAnalysisExecutor, analysis, name, "name", groupQualityThreshold, matchInterval);
        // Scenario 4
        // - Same to scenario 3, EXCEPT a new blocking key = country.

        assertScenario4(matchAnalysisExecutor, analysis, name, "name", groupQualityThreshold, matchInterval);

    }

    /**
     * DOC zhao Comment method "assertScenario1".
     * 
     * @param matchAnalysisExecutor
     * @param analysis
     * @param name
     * @param nameVar
     */
    private void assertScenario1(MatchAnalysisExecutor matchAnalysisExecutor, Analysis analysis, MetadataColumn name,
            String nameVar, double groupQualityThreshold, double matchInterval) {
        // Set indicators into analysis result.
        RecordMatchingIndicator matchIndicator = ColumnsetPackage.eINSTANCE.getColumnsetFactory().createRecordMatchingIndicator();
        // Match key: name, no block key, levenshtein attribute algorithm.
        matchIndicator.setAnalyzedElement(name);

        createMatchIndicatorWithOneMathRule(nameVar, matchIndicator, groupQualityThreshold, matchInterval);

        executeAnalysis(matchAnalysisExecutor, analysis, matchIndicator);

        // Assert group size and frequency.
        Map<Object, Long> size2Frequency = matchIndicator.getGroupSize2groupFrequency();
        assertTrue(size2Frequency.get(String.valueOf(4)) == 1l);// For 4 -> "seb"
        assertTrue(size2Frequency.get(String.valueOf(1)) == 4l);// For 1 -> "Sebastião","babass","nico","nicola"
        assertTrue(size2Frequency.get(String.valueOf(2)) == 3l);// For 2 -> "sebas","nicolas","nigula"

        // Assert row count, unique records, matched records and suspect records.
        assertTrue(matchIndicator.getCount() == 14);
        assertTrue(matchIndicator.getMatchedRecordCount() == 10);
        assertTrue(matchIndicator.getSuspectRecordCount() == 0);
    }

    /**
     * DOC zhao Comment method "assertScenario2".
     * 
     * @param matchAnalysisExecutor
     * @param analysis
     * @param name
     * @param nameVar
     */
    private void assertScenario2(MatchAnalysisExecutor matchAnalysisExecutor, Analysis analysis, MetadataColumn name,
            String nameVar, double groupQualityThreshold, double matchInterval) {
        // Set indicators into analysis result.
        RecordMatchingIndicator matchIndicator = ColumnsetPackage.eINSTANCE.getColumnsetFactory().createRecordMatchingIndicator();
        // Match key: name, no block key, levenshtein attribute algorithm.
        matchIndicator.setAnalyzedElement(name);

        createMatchIndicatorWithOneMathRule(nameVar, matchIndicator, groupQualityThreshold, matchInterval);

        executeAnalysis(matchAnalysisExecutor, analysis, matchIndicator);

        // Assert group size and frequency.
        Map<Object, Long> size2Frequency = matchIndicator.getGroupSize2groupFrequency();
        assertTrue(size2Frequency.get(String.valueOf(4)) == 1l);// For 4 -> "seb"
        assertTrue(size2Frequency.get(String.valueOf(1)) == 3l);// For 1 -> "Sebastião","babass","nico"
        assertTrue(size2Frequency.get(String.valueOf(3)) == 1l);// For 3 -> "nicolas"("nicola")
        assertTrue(size2Frequency.get(String.valueOf(2)) == 2l);// For 2 -> "sebas","nigula"

        // Assert row count, unique records, matched records and suspect records.
        assertTrue(matchIndicator.getCount() == 14);
        assertTrue(matchIndicator.getMatchedRecordCount() == 11);
        assertTrue(matchIndicator.getSuspectRecordCount() == 0);
    }

    /**
     * DOC zhao Comment method "assertScenario3".
     * 
     * @param matchAnalysisExecutor
     * @param analysis
     * @param name
     * @param nameVar
     */
    private void assertScenario3(MatchAnalysisExecutor matchAnalysisExecutor, Analysis analysis, MetadataColumn name,
            String nameVar, double groupQualityThreshold, double matchInterval) {
        // Set indicators into analysis result.
        RecordMatchingIndicator matchIndicator = ColumnsetPackage.eINSTANCE.getColumnsetFactory().createRecordMatchingIndicator();
        // Match key: name, no block key, levenshtein attribute algorithm.
        matchIndicator.setAnalyzedElement(name);

        createMatchIndicatorWithOneMathRule(nameVar, matchIndicator, groupQualityThreshold, matchInterval);

        executeAnalysis(matchAnalysisExecutor, analysis, matchIndicator);

        // Assert group size and frequency.
        Map<Object, Long> size2Frequency = matchIndicator.getGroupSize2groupFrequency();
        assertTrue(size2Frequency.get(String.valueOf(4)) == 1l);// For 4 -> "seb"
        assertTrue(size2Frequency.get(String.valueOf(1)) == 3l);// For 1 -> "Sebastião","babass","nico"
        assertTrue(size2Frequency.get(String.valueOf(3)) == 1l);// For 3 -> "nicolas"("nicola")
        assertTrue(size2Frequency.get(String.valueOf(2)) == 2l);// For 2 -> "sebas","nigula"

        // Assert row count, unique records, matched records and suspect records.
        assertTrue(matchIndicator.getCount() == 14);
        assertTrue(matchIndicator.getMatchedRecordCount() == 8);
        assertTrue(matchIndicator.getSuspectRecordCount() == 3); // For 3 -> "nicolas"("nicola"), group score: 0.9 <
                                                                 // 0.95
    }

    /**
     * DOC zhao Comment method "assertScenario3".
     * 
     * @param matchAnalysisExecutor
     * @param analysis
     * @param name
     * @param nameVar
     */
    @SuppressWarnings("nls")
    private void assertScenario4(MatchAnalysisExecutor matchAnalysisExecutor, Analysis analysis, MetadataColumn name,
            String nameVar, double groupQualityThreshold, double matchInterval) {
        // Set indicators into analysis result.
        RecordMatchingIndicator matchIndicator = ColumnsetPackage.eINSTANCE.getColumnsetFactory().createRecordMatchingIndicator();
        // Match key: name, no block key, levenshtein attribute algorithm.
        matchIndicator.setAnalyzedElement(name);

        createMatchIndicatorWithOneMathRule(nameVar, matchIndicator, groupQualityThreshold, matchInterval);
        // Add a blocking key: country
        BlockKeyDefinition blockKeyDef = RulesPackage.eINSTANCE.getRulesFactory().createBlockKeyDefinition();
        AlgorithmDefinition algoDef = RulesPackage.eINSTANCE.getRulesFactory().createAlgorithmDefinition();
        algoDef.setAlgorithmType(AttributeMatcherType.EXACT.name());
        blockKeyDef.setAlgorithm(algoDef);
        blockKeyDef.setColumn("country");
        blockKeyDef.setName("country");

        AlgorithmDefinition dummyAlgoPre = RulesPackage.eINSTANCE.getRulesFactory().createAlgorithmDefinition();
        dummyAlgoPre.setAlgorithmType(BlockingKeyPreAlgorithmEnum.NON_ALGO.getComponentValueName());
        blockKeyDef.setPreAlgorithm(dummyAlgoPre);
        AlgorithmDefinition dummyAlgoPost = RulesPackage.eINSTANCE.getRulesFactory().createAlgorithmDefinition();
        dummyAlgoPost.setAlgorithmType(BlockingKeyPreAlgorithmEnum.NON_ALGO.getComponentValueName());
        blockKeyDef.setPostAlgorithm(dummyAlgoPost);

        matchIndicator.getBuiltInMatchRuleDefinition().getBlockKeys().add(blockKeyDef);

        executeAnalysis(matchAnalysisExecutor, analysis, matchIndicator);

        // Assert group size and frequency.
        Map<Object, Long> size2Frequency = matchIndicator.getGroupSize2groupFrequency();
        assertTrue(size2Frequency.get(String.valueOf(1)) == 6l);// For 1 -> FR(4)"babass","Sebastião","nicolas","nigula"
                                                                // CN(2)"nigula","nico"
        assertTrue(size2Frequency.get(String.valueOf(2)) == 2l);// For 2 -> FR(1)"sebas", CN(1)"nicolas"("nicola")

        assertTrue(size2Frequency.get(String.valueOf(4)) == 1l);// For 4 -> FR(4)"seb"

        // Assert row count, unique records, matched records and suspect records.
        assertTrue(matchIndicator.getCount() == 14);
        assertTrue(matchIndicator.getMatchedRecordCount() == 6); // For 6 -> FR 4*"seb", FR 2 *"sebas"
        assertTrue(matchIndicator.getSuspectRecordCount() == 2); // For 2 -> CN "nicolas"("nicola"), group score: 0.9 <
                                                                 // 0.95
    }

    /**
     * DOC zhao Comment method "executeAnalysis".
     * 
     * @param matchAnalysisExecutor
     * @param analysis
     * @param matchIndicator
     */
    private void executeAnalysis(MatchAnalysisExecutor matchAnalysisExecutor, Analysis analysis,
            RecordMatchingIndicator matchIndicator) {
        BlockKeyIndicator blockKeyIndicator = ColumnsetPackage.eINSTANCE.getColumnsetFactory().createBlockKeyIndicator();

        AnalysisResult anaResult = AnalysisPackage.eINSTANCE.getAnalysisFactory().createAnalysisResult();
        anaResult.setResultMetadata(AnalysisPackage.eINSTANCE.getAnalysisFactory().createExecutionInformations());

        analysis.setResults(anaResult);
        analysis.getResults().getIndicators().add(matchIndicator);
        analysis.getResults().getIndicators().add(blockKeyIndicator);

        matchAnalysisExecutor.setMonitor(new NullProgressMonitor());
        matchAnalysisExecutor.execute(analysis);
    }

    /**
     * DOC zhao Comment method "createMatchIndicatorWithOneMathRule".
     * 
     * @param nameVar
     * @param matchIndicator
     * @param groupQualityThreshold
     * @param matchInterval
     */
    private void createMatchIndicatorWithOneMathRule(String nameVar, RecordMatchingIndicator matchIndicator,
            double groupQualityThreshold, double matchInterval) {
        MatchRuleDefinition matchRuleDefinition = RulesPackage.eINSTANCE.getRulesFactory().createMatchRuleDefinition();
        matchRuleDefinition.setMatchGroupQualityThreshold(groupQualityThreshold);
        MatchRule matchRule = RulesPackage.eINSTANCE.getRulesFactory().createMatchRule();
        matchRule.setMatchInterval(matchInterval);
        matchRule.setName("match rule 1");
        MatchKeyDefinition matchkeyDef = RulesPackage.eINSTANCE.getRulesFactory().createMatchKeyDefinition();
        matchkeyDef.setName(nameVar);
        matchkeyDef.setColumn(nameVar);

        AlgorithmDefinition algoDef = RulesPackage.eINSTANCE.getRulesFactory().createAlgorithmDefinition();
        algoDef.setAlgorithmType(AttributeMatcherType.LEVENSHTEIN.name());
        matchkeyDef.setAlgorithm(algoDef);
        matchkeyDef.setConfidenceWeight(1);
        matchRule.getMatchKeys().add(matchkeyDef);

        matchRuleDefinition.getMatchRules().add(matchRule);
        matchIndicator.setBuiltInMatchRuleDefinition(matchRuleDefinition);
    }
}
