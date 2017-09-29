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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.grouping.AnalysisMatchRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.IRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.MatchGroupResultConsumer;
import org.talend.dataquality.record.linkage.grouping.swoosh.DQMFBRecordMatcher;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorShipAlgorithmParams;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorShipAlgorithmParams.SurvivorshipFunction;
import org.talend.dataquality.record.linkage.record.CombinedRecordMatcher;
import org.talend.dataquality.record.linkage.record.IRecordMatcher;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * created by zshen on Nov 19, 2013 Detailled comment
 * 
 */
public class AnalysisRecordGroupingUtilsTest {

    /**
     * Test method for
     * {@link org.talend.dq.analysis.AnalysisRecordGroupingUtils#getMatchKeyMap(java.lang.String, java.lang.String, java.lang.String, int, java.util.Map, double, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetMatchKeyMap() {
        String column = "col1"; //$NON-NLS-1$
        String algoType = AttributeMatcherType.CUSTOM.name();
        String algoParameter = "myMatcher.jar||com.matcher.MyMatcher"; //$NON-NLS-1$
        int confidentWeight = 5;
        Map<MetadataColumn, String> columnIndexMap = new HashMap<MetadataColumn, String>();
        MetadataColumn metaColumn = ConnectionFactory.eINSTANCE.createMetadataColumn();
        metaColumn.setName(column);
        columnIndexMap.put(metaColumn, "0"); //$NON-NLS-1$
        double matchInterval = 0.5;
        double attrThreshold = 1.0d;
        String attributeName = "attribute1"; //$NON-NLS-1$
        String handleNull = "nullMatchNone"; //$NON-NLS-1$

        Map<String, String> matchKeyMap = AnalysisRecordGroupingUtils.getMatchKeyMap(column, algoType, algoParameter,
                confidentWeight, attrThreshold, columnIndexMap, matchInterval, attributeName, handleNull);
        judgeMatchMapResult(matchKeyMap);
    }

    /**
     * DOC zshen Comment method "judgeMatchMapResult".
     * 
     * @param matchKeyMap
     */
    private void judgeMatchMapResult(Map<String, String> matchKeyMap) {
        // this line will check the size of return map, any change will cause fail
        assertNotNull(matchKeyMap.size() == 8);
        assertNotNull(matchKeyMap.get(IRecordGrouping.COLUMN_IDX));
        assertNotNull(matchKeyMap.get(IRecordGrouping.MATCHING_TYPE));
        assertNotNull(matchKeyMap.get(IRecordGrouping.CUSTOMER_MATCH_CLASS));
        assertNotNull(matchKeyMap.get(IRecordGrouping.CONFIDENCE_WEIGHT));
        assertNotNull(matchKeyMap.get(IRecordGrouping.RECORD_MATCH_THRESHOLD));
        assertNotNull(matchKeyMap.get(IRecordGrouping.ATTRIBUTE_NAME));
        assertNotNull(matchKeyMap.get(IRecordGrouping.HANDLE_NULL));
        assertNull(matchKeyMap.get(IRecordGrouping.JAR_PATH));
    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.AnalysisRecordGroupingUtils#createSurvivorShipAlgorithmParams(org.talend.dataquality.record.linkage.grouping.AnalysisMatchRecordGrouping, org.talend.dataquality.indicators.columnset.RecordMatchingIndicator, Map)
     * .
     */
    @Test
    public void testCreateSurvivorShipAlgorithmParams() {
        // Survivorshipkey
        RecordMatchingIndicator recordMatchingIndicator = ColumnsetFactory.eINSTANCE.createRecordMatchingIndicator();
        MatchRuleDefinition createMatchRuleDefinition = RulesFactory.eINSTANCE.createMatchRuleDefinition();
        recordMatchingIndicator.setBuiltInMatchRuleDefinition(createMatchRuleDefinition);
        EList<SurvivorshipKeyDefinition> survivorshipKeys = createMatchRuleDefinition.getSurvivorshipKeys();
        AlgorithmDefinition createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmParameters(""); //$NON-NLS-1$
        createAlgorithmDefinition.setAlgorithmType("Longest"); //$NON-NLS-1$
        survivorshipKeys.add(createKeyDefinition("a1", createAlgorithmDefinition)); //$NON-NLS-1$
        createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmParameters(""); //$NON-NLS-1$
        createAlgorithmDefinition.setAlgorithmType("Longest"); //$NON-NLS-1$
        survivorshipKeys.add(createKeyDefinition("a2", createAlgorithmDefinition)); //$NON-NLS-1$

        // DefaultSurvivorship
        EList<DefaultSurvivorshipDefinition> defaultSurvivorshipDefinitions = createMatchRuleDefinition
                .getDefaultSurvivorshipDefinitions();
        createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmParameters(""); //$NON-NLS-1$
        createAlgorithmDefinition.setAlgorithmType("MostCommon"); //$NON-NLS-1$
        defaultSurvivorshipDefinitions.add(createDefaultsurvivShip("String", createAlgorithmDefinition)); //$NON-NLS-1$

        // DefaultSurvivorship
        EList<ParticularDefaultSurvivorshipDefinitions> particularDefaultSurvivorshipDefinitions = createMatchRuleDefinition
                .getParticularDefaultSurvivorshipDefinitions();
        createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmParameters(""); //$NON-NLS-1$
        createAlgorithmDefinition.setAlgorithmType("Concatenate"); //$NON-NLS-1$
        particularDefaultSurvivorshipDefinitions.add(createParticularDefaultSurvivorshipDefinitions(
                "a2", createAlgorithmDefinition)); //$NON-NLS-1$ 

        // init columnMap
        Map<MetadataColumn, String> columnMap = new HashMap<MetadataColumn, String>();
        MetadataColumn col0 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col0.setName("a1"); //$NON-NLS-1$
        col0.setTalendType("id_String"); //$NON-NLS-1$
        columnMap.put(col0, "0"); //$NON-NLS-1$
        MetadataColumn col1 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col1.setTalendType("id_String"); //$NON-NLS-1$
        col1.setName("a2"); //$NON-NLS-1$
        columnMap.put(col1, "1"); //$NON-NLS-1$
        MetadataColumn col2 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col2.setTalendType("id_String"); //$NON-NLS-1$
        col2.setName("a3"); //$NON-NLS-1$
        columnMap.put(col2, "2"); //$NON-NLS-1$
        MatchGroupResultConsumer matchGroupResultConsumer = new MatchGroupResultConsumer(true) {

            @Override
            public void handle(Object row) {
                // no need to implement
            }

        };
        AnalysisMatchRecordGrouping analysisMatchRecordGrouping = new AnalysisMatchRecordGrouping(matchGroupResultConsumer);
        CombinedRecordMatcher combinedRecordMatcher = analysisMatchRecordGrouping.getCombinedRecordMatcher();
        DQMFBRecordMatcher dqmfbRecordMatcher = new DQMFBRecordMatcher(0.9);
        combinedRecordMatcher.getMatchers().add(dqmfbRecordMatcher);
        List<List<Map<String, String>>> multiMatchRules = analysisMatchRecordGrouping.getMultiMatchRules();
        List<Map<String, String>> matchRuleList = new ArrayList<Map<String, String>>();
        Map<String, String> matchKeyMap1 = new HashMap<String, String>();
        Map<String, String> matchKeyMap2 = new HashMap<String, String>();
        Map<String, String> matchKeyMap3 = new HashMap<String, String>();
        matchKeyMap1.put(IRecordGrouping.MATCHING_TYPE, AttributeMatcherType.EXACT.name());
        matchKeyMap2.put(IRecordGrouping.MATCHING_TYPE, AttributeMatcherType.DUMMY.name());// change by
                                                                                           // DefaultSurvivorshipDefinitions
        matchKeyMap3.put(IRecordGrouping.MATCHING_TYPE, AttributeMatcherType.DUMMY.name());// change by
                                                                                           // ParticularDefaultSurvivorshipDefinitions
        matchRuleList.add(matchKeyMap1);
        matchRuleList.add(matchKeyMap2);
        matchRuleList.add(matchKeyMap3);

        matchKeyMap1.put(IRecordGrouping.MATCH_KEY_NAME, "a1"); //$NON-NLS-1$
        matchKeyMap2.put(IRecordGrouping.MATCH_KEY_NAME, "a2"); //$NON-NLS-1$
        matchKeyMap3.put(IRecordGrouping.MATCH_KEY_NAME, "a3"); //$NON-NLS-1$

        matchKeyMap1.put(IRecordGrouping.COLUMN_IDX, "0"); //$NON-NLS-1$
        matchKeyMap2.put(IRecordGrouping.COLUMN_IDX, "1"); //$NON-NLS-1$
        matchKeyMap3.put(IRecordGrouping.COLUMN_IDX, "2"); //$NON-NLS-1$

        multiMatchRules.add(matchRuleList);

        SurvivorShipAlgorithmParams createSurvivorShipAlgorithmParams = AnalysisRecordGroupingUtils
                .createSurvivorShipAlgorithmParams(analysisMatchRecordGrouping, recordMatchingIndicator, columnMap);
        Assert.assertEquals("The size of SurvivorShipAlgos should be 2", 2, //$NON-NLS-1$
                createSurvivorShipAlgorithmParams.getSurviorShipAlgos().length);
        // the size of default survivorshipRules is come from by (column size * default item size)
        Assert.assertEquals("The size of DefaultSurviorshipRules should be 3", 3, createSurvivorShipAlgorithmParams //$NON-NLS-1$
                .getDefaultSurviorshipRules().size());
        Map<IRecordMatcher, SurvivorshipFunction[]> survivorshipAlgosMap = createSurvivorShipAlgorithmParams
                .getSurvivorshipAlgosMap();
        Assert.assertEquals("The size of survivorshipAlgosMap should be 1", 1, survivorshipAlgosMap.size()); //$NON-NLS-1$
        SurvivorshipFunction[] survivorshipFunctions = survivorshipAlgosMap.get(dqmfbRecordMatcher);
        Assert.assertEquals("The size of survivorshipFunctions should be 3", 3, survivorshipFunctions.length); //$NON-NLS-1$
        Assert.assertEquals("The Algorithm of a1 function should be LONGEST", SurvivorShipAlgorithmEnum.LONGEST, //$NON-NLS-1$
                survivorshipFunctions[0].getSurvivorShipAlgoEnum());
        Assert.assertEquals("The Algorithm of a2 function should be Concatenate", SurvivorShipAlgorithmEnum.CONCATENATE, //$NON-NLS-1$
                survivorshipFunctions[1].getSurvivorShipAlgoEnum());
        Assert.assertEquals("The Algorithm of a3 function should be MostCommon", SurvivorShipAlgorithmEnum.MOST_COMMON, //$NON-NLS-1$
                survivorshipFunctions[2].getSurvivorShipAlgoEnum());

    }

    /**
     * Create DefaultsurvivShip
     * 
     * @return
     */
    private DefaultSurvivorshipDefinition createDefaultsurvivShip(String dataType, AlgorithmDefinition algorDef) {
        DefaultSurvivorshipDefinition createDefaultSurvivorshipDefinition = RulesFactory.eINSTANCE
                .createDefaultSurvivorshipDefinition();
        createDefaultSurvivorshipDefinition.setDataType(dataType);
        createDefaultSurvivorshipDefinition.setFunction(algorDef);
        return createDefaultSurvivorshipDefinition;
    }

    /**
     * Create ParticularDefaultSurvivorshipDefinitions
     * 
     * @return
     */
    private ParticularDefaultSurvivorshipDefinitions createParticularDefaultSurvivorshipDefinitions(String colName,
            AlgorithmDefinition algorDef) {
        ParticularDefaultSurvivorshipDefinitions createParticularDefaultSurvivorshipDefinition = RulesFactory.eINSTANCE
                .createParticularDefaultSurvivorshipDefinitions();
        createParticularDefaultSurvivorshipDefinition.setFunction(algorDef);
        createParticularDefaultSurvivorshipDefinition.setColumn(colName);
        return createParticularDefaultSurvivorshipDefinition;
    }

    /**
     * Create KeyDefinition
     * 
     * @return
     */
    private SurvivorshipKeyDefinition createKeyDefinition(String name, AlgorithmDefinition algorDef) {
        SurvivorshipKeyDefinition createSurvivorshipKeyDefinition = RulesFactory.eINSTANCE.createSurvivorshipKeyDefinition();
        createSurvivorshipKeyDefinition.setName(name);
        createSurvivorshipKeyDefinition.setFunction(algorDef);
        return createSurvivorshipKeyDefinition;
    }

}
