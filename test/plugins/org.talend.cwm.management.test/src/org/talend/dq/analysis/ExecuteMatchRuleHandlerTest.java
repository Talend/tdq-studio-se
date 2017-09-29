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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.grouping.MatchGroupResultConsumer;
import org.talend.dataquality.record.linkage.utils.BlockingKeyAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPostAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPreAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dq.analysis.match.ExecuteMatchRuleHandler;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * created by zshen on Jan 2, 2014 Detailled comment
 * 
 */
public class ExecuteMatchRuleHandlerTest {

    private final String columnName0 = "id"; //$NON-NLS-1$

    private final String columnName1 = "name"; //$NON-NLS-1$

    private final String columnName2 = "number"; //$NON-NLS-1$

    private final String columnName3 = "date"; //$NON-NLS-1$

    /**
     * Test method for
     * {@link org.talend.dq.analysis.ExecuteMatchRuleHandler#execute(java.util.Map, org.talend.dataquality.indicators.columnset.RecordMatchingIndicator, java.util.List, org.talend.dataquality.indicators.columnset.BlockKeyIndicator)}
     * .
     * 
     * no block key one match key
     */
    @Test
    public void testExecute1() {
        Map<MetadataColumn, String> columnMap = new HashMap<MetadataColumn, String>();
        MetadataColumn col0 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col0.setName(columnName0);
        columnMap.put(col0, "0"); //$NON-NLS-1$
        MetadataColumn col1 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col1.setName(columnName1);
        columnMap.put(col1, "1"); //$NON-NLS-1$
        MetadataColumn col2 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col2.setName(columnName2);
        columnMap.put(col2, "2"); //$NON-NLS-1$
        MetadataColumn col3 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col3.setName(columnName3);
        columnMap.put(col3, "3"); //$NON-NLS-1$
        // create match key
        RecordMatchingIndicator recordMatchingIndicator = ColumnsetFactory.eINSTANCE.createRecordMatchingIndicator();
        MatchRuleDefinition matchRuleDef = RulesPackage.eINSTANCE.getRulesFactory().createMatchRuleDefinition();
        recordMatchingIndicator.setBuiltInMatchRuleDefinition(matchRuleDef);
        MatchRule createMatchRule1 = RulesFactory.eINSTANCE.createMatchRule();
        MatchKeyDefinition createMatchKeyDefinition1 = RulesFactory.eINSTANCE.createMatchKeyDefinition();
        createMatchRule1.getMatchKeys().add(createMatchKeyDefinition1);
        createMatchKeyDefinition1.setColumn(columnName0);
        createMatchKeyDefinition1.setConfidenceWeight(1);
        createMatchKeyDefinition1.setName("rule1.matchkey1"); //$NON-NLS-1$
        createMatchKeyDefinition1.setHandleNull(HandleNullEnum.NULL_MATCH_NULL.getValue());
        AlgorithmDefinition createAlgorithmDefinition1 = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition1.setAlgorithmType(AttributeMatcherType.EXACT.name());
        createMatchKeyDefinition1.setAlgorithm(createAlgorithmDefinition1);
        matchRuleDef.getMatchRules().add(createMatchRule1);
        // input data
        List<Object[]> matchRows = new ArrayList<Object[]>();
        matchRows.add(new String[] { "id1", "name1", "number1", "date1" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id2", "name2", "number2", "date2" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id3", "name1", "number3", "date3" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id4", "name4", "number2", "date1" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        BlockKeyIndicator blockKeyIndicator = ColumnsetFactory.eINSTANCE.createBlockKeyIndicator();
        ExecuteMatchRuleHandler execHandler = new ExecuteMatchRuleHandler();
        MatchGroupResultConsumer matchResultConsumer = createMatchGroupResultConsumer(columnMap, recordMatchingIndicator);
        TypedReturnCode<MatchGroupResultConsumer> executeResult = execHandler.execute(columnMap, recordMatchingIndicator,
                matchRows, blockKeyIndicator, matchResultConsumer);

        Assert.assertTrue(executeResult.isOk());
        Assert.assertTrue(executeResult.getMessage() == null);
        Assert.assertTrue(executeResult.getObject() != null);
        MatchGroupResultConsumer ResultConsumer = executeResult.getObject();
        List<Object[]> fullMatchResult = ResultConsumer.getFullMatchResult();
        Assert.assertTrue(fullMatchResult.size() == 4);
        // every input data is master data
        for (Object[] objectArray : fullMatchResult) {
            Object object = objectArray[7];
            Assert.assertTrue(Boolean.parseBoolean(object.toString()));
        }

    }

    private MatchGroupResultConsumer createMatchGroupResultConsumer(Map<MetadataColumn, String> columnMap,
            final RecordMatchingIndicator recordMatchingIndicator) {
        MetadataColumn[] completeColumnSchema = AnalysisRecordGroupingUtils.getCompleteColumnSchema(columnMap);
        String[] colSchemaString = new String[completeColumnSchema.length];
        int idx = 0;
        for (MetadataColumn metadataCol : completeColumnSchema) {
            colSchemaString[idx++] = metadataCol.getName();
        }
        recordMatchingIndicator.setMatchRowSchema(colSchemaString);
        recordMatchingIndicator.reset();

        MatchGroupResultConsumer matchResultConsumer = new MatchGroupResultConsumer(true) {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.dataquality.record.linkage.grouping. MatchGroupResultConsumer#handle(java.lang.Object)
             */
            @Override
            public void handle(Object row) {
                recordMatchingIndicator.handle(row);
                addOneRowOfResult(row);
            }
        };
        return matchResultConsumer;
    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.ExecuteMatchRuleHandler#execute(java.util.Map, org.talend.dataquality.indicators.columnset.RecordMatchingIndicator, java.util.List, org.talend.dataquality.indicators.columnset.BlockKeyIndicator)}
     * .
     * 
     * one block key one match key
     */
    @Test
    public void testExecute2() {
        Map<MetadataColumn, String> columnMap = new HashMap<MetadataColumn, String>();
        MetadataColumn col0 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col0.setName(columnName0);
        columnMap.put(col0, "0"); //$NON-NLS-1$
        MetadataColumn col1 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col1.setName(columnName1);
        columnMap.put(col1, "1"); //$NON-NLS-1$
        MetadataColumn col2 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col2.setName(columnName2);
        columnMap.put(col2, "2"); //$NON-NLS-1$
        MetadataColumn col3 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col3.setName(columnName3);
        columnMap.put(col3, "3"); //$NON-NLS-1$

        RecordMatchingIndicator recordMatchingIndicator = ColumnsetFactory.eINSTANCE.createRecordMatchingIndicator();
        MatchRuleDefinition matchRuleDef = RulesPackage.eINSTANCE.getRulesFactory().createMatchRuleDefinition();
        recordMatchingIndicator.setBuiltInMatchRuleDefinition(matchRuleDef);
        // create match key
        MatchRule createMatchRule1 = RulesFactory.eINSTANCE.createMatchRule();
        MatchKeyDefinition createMatchKeyDefinition1 = RulesFactory.eINSTANCE.createMatchKeyDefinition();
        createMatchRule1.getMatchKeys().add(createMatchKeyDefinition1);
        createMatchKeyDefinition1.setColumn(columnName2);
        createMatchKeyDefinition1.setConfidenceWeight(1);
        createMatchKeyDefinition1.setName("rule1.matchkey1"); //$NON-NLS-1$
        createMatchKeyDefinition1.setHandleNull(HandleNullEnum.NULL_MATCH_NULL.getValue());
        AlgorithmDefinition createAlgorithmDefinition1 = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition1.setAlgorithmType(AttributeMatcherType.EXACT.name());
        createMatchKeyDefinition1.setAlgorithm(createAlgorithmDefinition1);
        matchRuleDef.getMatchRules().add(createMatchRule1);

        // create block key
        BlockKeyDefinition createBlockKeyDefinition = RulesFactory.eINSTANCE.createBlockKeyDefinition();
        createBlockKeyDefinition.setColumn(columnName1);
        createBlockKeyDefinition.setName("blockKey1"); //$NON-NLS-1$
        // setPreAlgorithm
        AlgorithmDefinition blockPreAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockPreAlgorithm.setAlgorithmType(BlockingKeyPreAlgorithmEnum.NON_ALGO.getValue());
        createBlockKeyDefinition.setPreAlgorithm(blockPreAlgorithm);
        // setAlgorithm
        AlgorithmDefinition blockAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockAlgorithm.setAlgorithmType(BlockingKeyAlgorithmEnum.EXACT.getValue());
        createBlockKeyDefinition.setAlgorithm(blockAlgorithm);
        // setPostAlgorithm
        AlgorithmDefinition blockPostAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockPostAlgorithm.setAlgorithmType(BlockingKeyPostAlgorithmEnum.NON_ALGO.getValue());
        createBlockKeyDefinition.setPostAlgorithm(blockPostAlgorithm);

        matchRuleDef.getBlockKeys().add(createBlockKeyDefinition);

        List<Object[]> matchRows = new ArrayList<Object[]>();
        matchRows.add(new String[] { "id1", "name1", "number1", "date1" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id2", "name1", "number2", "date2" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id3", "name2", "number2", "date3" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id4", "name2", "number2", "date1" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        BlockKeyIndicator blockKeyIndicator = ColumnsetFactory.eINSTANCE.createBlockKeyIndicator();
        ExecuteMatchRuleHandler execHandler = new ExecuteMatchRuleHandler();
        MatchGroupResultConsumer matchResultConsumer = createMatchGroupResultConsumer(columnMap, recordMatchingIndicator);
        TypedReturnCode<MatchGroupResultConsumer> executeResult = execHandler.execute(columnMap, recordMatchingIndicator,
                matchRows, blockKeyIndicator, matchResultConsumer);
        Assert.assertTrue(executeResult.isOk());
        Assert.assertTrue(executeResult.getMessage() == null);
        Assert.assertTrue(executeResult.getObject() != null);
        MatchGroupResultConsumer ResultConsumer = executeResult.getObject();
        List<Object[]> fullMatchResult = ResultConsumer.getFullMatchResult();
        Assert.assertTrue(fullMatchResult.size() == 4);
        for (int i = 0; i < fullMatchResult.size(); i++) {
            Object[] objectArray = fullMatchResult.get(i);
            Object masterValue = objectArray[7];
            Object idValue = objectArray[0];
            // judge id1 is master id2 is master id3 is master and id4 is not master
            if ("id4".equals(idValue)) { //$NON-NLS-1$
                Assert.assertFalse(Boolean.parseBoolean(masterValue.toString()));
            } else {
                Assert.assertTrue(Boolean.parseBoolean(masterValue.toString()));
            }
        }

    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.ExecuteMatchRuleHandler#execute(java.util.Map, org.talend.dataquality.indicators.columnset.RecordMatchingIndicator, java.util.List, org.talend.dataquality.indicators.columnset.BlockKeyIndicator)}
     * .
     * 
     * one block key, two match rule
     */
    @Test
    public void testExecute3() {
        Map<MetadataColumn, String> columnMap = new HashMap<MetadataColumn, String>();
        MetadataColumn col0 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col0.setName(columnName0);
        columnMap.put(col0, "0"); //$NON-NLS-1$
        MetadataColumn col1 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col1.setName(columnName1);
        columnMap.put(col1, "1"); //$NON-NLS-1$
        MetadataColumn col2 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col2.setName(columnName2);
        columnMap.put(col2, "2"); //$NON-NLS-1$
        MetadataColumn col3 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col3.setName(columnName3);
        columnMap.put(col3, "3"); //$NON-NLS-1$
        RecordMatchingIndicator recordMatchingIndicator = ColumnsetFactory.eINSTANCE.createRecordMatchingIndicator();
        MatchRuleDefinition matchRuleDef = RulesPackage.eINSTANCE.getRulesFactory().createMatchRuleDefinition();
        recordMatchingIndicator.setBuiltInMatchRuleDefinition(matchRuleDef);
        // create match rule
        MatchRule matchRule1 = RulesFactory.eINSTANCE.createMatchRule();
        MatchKeyDefinition createMatchKeyDefinition1 = RulesFactory.eINSTANCE.createMatchKeyDefinition();
        matchRule1.getMatchKeys().add(createMatchKeyDefinition1);
        createMatchKeyDefinition1.setColumn(columnName2);
        createMatchKeyDefinition1.setConfidenceWeight(1);
        createMatchKeyDefinition1.setName("rule1.matchkey1"); //$NON-NLS-1$
        createMatchKeyDefinition1.setHandleNull(HandleNullEnum.NULL_MATCH_NULL.getValue());
        AlgorithmDefinition createAlgorithmDefinition1 = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition1.setAlgorithmType(AttributeMatcherType.EXACT.name());
        createMatchKeyDefinition1.setAlgorithm(createAlgorithmDefinition1);
        matchRuleDef.getMatchRules().add(matchRule1);
        // create match rule
        MatchRule matchRule2 = RulesFactory.eINSTANCE.createMatchRule();
        MatchKeyDefinition createMatchKeyDefinition2 = RulesFactory.eINSTANCE.createMatchKeyDefinition();
        matchRule2.getMatchKeys().add(createMatchKeyDefinition2);
        createMatchKeyDefinition2.setColumn(columnName3);
        createMatchKeyDefinition2.setConfidenceWeight(1);
        createMatchKeyDefinition2.setName("rule1.matchkey1"); //$NON-NLS-1$
        createMatchKeyDefinition2.setHandleNull(HandleNullEnum.NULL_MATCH_NULL.getValue());
        AlgorithmDefinition createAlgorithmDefinition2 = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition2.setAlgorithmType(AttributeMatcherType.EXACT.name());
        createMatchKeyDefinition2.setAlgorithm(createAlgorithmDefinition2);
        matchRuleDef.getMatchRules().add(matchRule2);

        // create block key
        BlockKeyDefinition createBlockKeyDefinition = RulesFactory.eINSTANCE.createBlockKeyDefinition();
        createBlockKeyDefinition.setColumn(columnName1);
        createBlockKeyDefinition.setName("blockKey1"); //$NON-NLS-1$
        // setPreAlgorithm
        AlgorithmDefinition blockPreAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockPreAlgorithm.setAlgorithmType(BlockingKeyPreAlgorithmEnum.NON_ALGO.getValue());
        createBlockKeyDefinition.setPreAlgorithm(blockPreAlgorithm);
        // setAlgorithm
        AlgorithmDefinition blockAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockAlgorithm.setAlgorithmType(BlockingKeyAlgorithmEnum.EXACT.getValue());
        createBlockKeyDefinition.setAlgorithm(blockAlgorithm);
        // setPostAlgorithm
        AlgorithmDefinition blockPostAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockPostAlgorithm.setAlgorithmType(BlockingKeyPostAlgorithmEnum.NON_ALGO.getValue());
        createBlockKeyDefinition.setPostAlgorithm(blockPostAlgorithm);

        matchRuleDef.getBlockKeys().add(createBlockKeyDefinition);

        List<Object[]> matchRows = new ArrayList<Object[]>();
        matchRows.add(new String[] { "id1", "name1", "number1", "date1" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id2", "name1", "number2", "date1" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id3", "name2", "number2", "date3" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id4", "name2", "number2", "date1" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        BlockKeyIndicator blockKeyIndicator = ColumnsetFactory.eINSTANCE.createBlockKeyIndicator();
        ExecuteMatchRuleHandler execHandler = new ExecuteMatchRuleHandler();

        MatchGroupResultConsumer matchResultConsumer = createMatchGroupResultConsumer(columnMap, recordMatchingIndicator);

        TypedReturnCode<MatchGroupResultConsumer> executeResult = execHandler.execute(columnMap, recordMatchingIndicator,
                matchRows, blockKeyIndicator, matchResultConsumer);
        Assert.assertTrue(executeResult.isOk());
        Assert.assertTrue(executeResult.getMessage() == null);
        Assert.assertTrue(executeResult.getObject() != null);
        MatchGroupResultConsumer ResultConsumer = executeResult.getObject();
        List<Object[]> fullMatchResult = ResultConsumer.getFullMatchResult();
        Assert.assertTrue(fullMatchResult.size() == 4);
        for (int i = 0; i < fullMatchResult.size(); i++) {
            Object[] objectArray = fullMatchResult.get(i);
            Object masterValue = objectArray[7];
            Object idValue = objectArray[0];
            // judge id1 is master id2 is not master id3 is master and id4 is not master
            // id2 is because of matchRule1 id4 is because of matchRule2
            if ("id2".equals(idValue) || "id4".equals(idValue)) { //$NON-NLS-1$ //$NON-NLS-2$
                Assert.assertFalse(Boolean.parseBoolean(masterValue.toString()));
            } else {
                Assert.assertTrue(Boolean.parseBoolean(masterValue.toString()));
            }
        }

    }

    /**
     * Test method for
     * {@link org.talend.dq.analysis.ExecuteMatchRuleHandler#execute(java.util.Map, org.talend.dataquality.indicators.columnset.RecordMatchingIndicator, java.util.List, org.talend.dataquality.indicators.columnset.BlockKeyIndicator)}
     * .
     * 
     * same to case 3 but the match rule order is exchange
     */
    @Test
    public void testExecute4() {
        Map<MetadataColumn, String> columnMap = new HashMap<MetadataColumn, String>();
        MetadataColumn col0 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col0.setName(columnName0);
        columnMap.put(col0, "0"); //$NON-NLS-1$
        MetadataColumn col1 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col1.setName(columnName1);
        columnMap.put(col1, "1"); //$NON-NLS-1$
        MetadataColumn col2 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col2.setName(columnName2);
        columnMap.put(col2, "2"); //$NON-NLS-1$
        MetadataColumn col3 = ConnectionFactory.eINSTANCE.createMetadataColumn();
        col3.setName(columnName3);
        columnMap.put(col3, "3"); //$NON-NLS-1$
        RecordMatchingIndicator recordMatchingIndicator = ColumnsetFactory.eINSTANCE.createRecordMatchingIndicator();
        MatchRuleDefinition matchRuleDef = RulesPackage.eINSTANCE.getRulesFactory().createMatchRuleDefinition();
        recordMatchingIndicator.setBuiltInMatchRuleDefinition(matchRuleDef);

        // create match rule
        MatchRule matchRule2 = RulesFactory.eINSTANCE.createMatchRule();
        MatchKeyDefinition createMatchKeyDefinition2 = RulesFactory.eINSTANCE.createMatchKeyDefinition();
        matchRule2.getMatchKeys().add(createMatchKeyDefinition2);
        createMatchKeyDefinition2.setColumn(columnName3);
        createMatchKeyDefinition2.setConfidenceWeight(1);
        createMatchKeyDefinition2.setName("rule1.matchkey1"); //$NON-NLS-1$
        createMatchKeyDefinition2.setHandleNull(HandleNullEnum.NULL_MATCH_NULL.getValue());
        AlgorithmDefinition createAlgorithmDefinition2 = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition2.setAlgorithmType(AttributeMatcherType.EXACT.name());
        createMatchKeyDefinition2.setAlgorithm(createAlgorithmDefinition2);
        matchRuleDef.getMatchRules().add(matchRule2);

        // create match rule
        MatchRule matchRule1 = RulesFactory.eINSTANCE.createMatchRule();
        MatchKeyDefinition createMatchKeyDefinition1 = RulesFactory.eINSTANCE.createMatchKeyDefinition();
        matchRule1.getMatchKeys().add(createMatchKeyDefinition1);
        createMatchKeyDefinition1.setColumn(columnName2);
        createMatchKeyDefinition1.setConfidenceWeight(1);
        createMatchKeyDefinition1.setName("rule1.matchkey1"); //$NON-NLS-1$
        createMatchKeyDefinition1.setHandleNull(HandleNullEnum.NULL_MATCH_NULL.getValue());
        AlgorithmDefinition createAlgorithmDefinition1 = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition1.setAlgorithmType(AttributeMatcherType.EXACT.name());
        createMatchKeyDefinition1.setAlgorithm(createAlgorithmDefinition1);
        matchRuleDef.getMatchRules().add(matchRule1);

        // create block key
        BlockKeyDefinition createBlockKeyDefinition = RulesFactory.eINSTANCE.createBlockKeyDefinition();
        createBlockKeyDefinition.setColumn(columnName1);
        createBlockKeyDefinition.setName("blockKey1"); //$NON-NLS-1$
        // setPreAlgorithm
        AlgorithmDefinition blockPreAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockPreAlgorithm.setAlgorithmType(BlockingKeyPreAlgorithmEnum.NON_ALGO.getValue());
        createBlockKeyDefinition.setPreAlgorithm(blockPreAlgorithm);
        // setAlgorithm
        AlgorithmDefinition blockAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockAlgorithm.setAlgorithmType(BlockingKeyAlgorithmEnum.EXACT.getValue());
        createBlockKeyDefinition.setAlgorithm(blockAlgorithm);
        // setPostAlgorithm
        AlgorithmDefinition blockPostAlgorithm = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        blockPostAlgorithm.setAlgorithmType(BlockingKeyPostAlgorithmEnum.NON_ALGO.getValue());
        createBlockKeyDefinition.setPostAlgorithm(blockPostAlgorithm);

        matchRuleDef.getBlockKeys().add(createBlockKeyDefinition);

        List<Object[]> matchRows = new ArrayList<Object[]>();
        matchRows.add(new String[] { "id1", "name1", "number1", "date1" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id2", "name1", "number2", "date1" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id3", "name2", "number2", "date3" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        matchRows.add(new String[] { "id4", "name2", "number2", "date1" });//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        BlockKeyIndicator blockKeyIndicator = ColumnsetFactory.eINSTANCE.createBlockKeyIndicator();
        ExecuteMatchRuleHandler execHandler = new ExecuteMatchRuleHandler();

        MatchGroupResultConsumer matchResultConsumer = createMatchGroupResultConsumer(columnMap, recordMatchingIndicator);
        TypedReturnCode<MatchGroupResultConsumer> executeResult = execHandler.execute(columnMap, recordMatchingIndicator,
                matchRows, blockKeyIndicator, matchResultConsumer);
        Assert.assertTrue(executeResult.isOk());
        Assert.assertTrue(executeResult.getMessage() == null);
        Assert.assertTrue(executeResult.getObject() != null);
        MatchGroupResultConsumer ResultConsumer = executeResult.getObject();
        List<Object[]> fullMatchResult = ResultConsumer.getFullMatchResult();
        Assert.assertTrue(fullMatchResult.size() == 4);
        for (int i = 0; i < fullMatchResult.size(); i++) {
            Object[] objectArray = fullMatchResult.get(i);
            Object masterValue = objectArray[7];
            Object idValue = objectArray[0];
            // judge id1 is master id2 is not master id3 is master and id4 is not master
            // id2 is because of matchRule1 id4 is because of matchRule2
            if ("id2".equals(idValue) || "id4".equals(idValue)) { //$NON-NLS-1$ //$NON-NLS-2$
                Assert.assertFalse(Boolean.parseBoolean(masterValue.toString()));
            } else {
                Assert.assertTrue(Boolean.parseBoolean(masterValue.toString()));
            }
        }

    }
}
