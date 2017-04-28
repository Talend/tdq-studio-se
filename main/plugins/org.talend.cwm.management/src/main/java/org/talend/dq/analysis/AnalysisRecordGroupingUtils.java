// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.constant.RecordMatcherType;
import org.talend.dataquality.record.linkage.grouping.AnalysisMatchRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.IRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorShipAlgorithmParams;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorShipAlgorithmParams.SurvivorshipFunction;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorshipUtils;
import org.talend.dataquality.record.linkage.record.CombinedRecordMatcher;
import org.talend.dataquality.record.linkage.record.IRecordMatcher;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AppliedBlockKey;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.KeyDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;
import org.talend.dq.helper.CustomAttributeMatcherHelper;

/**
 * used for some utility functions
 */
public class AnalysisRecordGroupingUtils {

    public static final String ESCAPE_CHARACTER = "\\"; //$NON-NLS-1$

    /**
     * get Complete Column Schema.
     * 
     * @return
     */
    public static MetadataColumn[] getCompleteColumnSchema(Map<MetadataColumn, String> columnMap) {
        List<MetadataColumn> columnNameList = new ArrayList<MetadataColumn>();
        if (columnMap == null) {
            return new MetadataColumn[0];
        }
        for (MetadataColumn columnName : columnMap.keySet()) {
            columnNameList.add(columnName);
        }
        MetadataColumn dummyBlockKeyColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyBlockKeyColumn.setName(MatchAnalysisConstant.BLOCK_KEY);
        columnNameList.add(dummyBlockKeyColumn);
        MetadataColumn dummyGIDColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyGIDColumn.setName(MatchAnalysisConstant.GID);
        columnNameList.add(dummyGIDColumn);
        MetadataColumn dummyGSizeColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyGSizeColumn.setName(MatchAnalysisConstant.GRP_SIZE);
        columnNameList.add(dummyGSizeColumn);
        MetadataColumn dummyMasterColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyMasterColumn.setName(MatchAnalysisConstant.MASTER);
        columnNameList.add(dummyMasterColumn);
        MetadataColumn dummyScoreColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyScoreColumn.setName(MatchAnalysisConstant.SCORE);
        columnNameList.add(dummyScoreColumn);
        MetadataColumn dummyGRPQualityColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyGRPQualityColumn.setName(MatchAnalysisConstant.GRP_QUALITY);
        columnNameList.add(dummyGRPQualityColumn);
        MetadataColumn dummyAtrrScoresColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyAtrrScoresColumn.setName(MatchAnalysisConstant.ATTRIBUTE_SCORES);
        columnNameList.add(dummyAtrrScoresColumn);
        return columnNameList.toArray(new MetadataColumn[columnNameList.size()]);
    }

    /**
     * getCompleteColumnSchema: when refresh match chart by click refresh button
     * 
     * @return
     */
    public static MetadataColumn[] getCompleteColumnSchemaWithoutBlockKey(Map<MetadataColumn, String> columnMap) {
        List<MetadataColumn> columnNameList = new ArrayList<MetadataColumn>();
        if (columnMap == null) {
            return new MetadataColumn[0];
        }
        for (MetadataColumn columnName : columnMap.keySet()) {
            columnNameList.add(columnName);
        }
        MetadataColumn dummyGIDColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyGIDColumn.setName(MatchAnalysisConstant.GID);
        columnNameList.add(dummyGIDColumn);
        MetadataColumn dummyGSizeColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyGSizeColumn.setName(MatchAnalysisConstant.GRP_SIZE);
        columnNameList.add(dummyGSizeColumn);
        MetadataColumn dummyMasterColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyMasterColumn.setName(MatchAnalysisConstant.MASTER);
        columnNameList.add(dummyMasterColumn);
        MetadataColumn dummyScoreColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyScoreColumn.setName(MatchAnalysisConstant.SCORE);
        columnNameList.add(dummyScoreColumn);
        MetadataColumn dummyGRPQualityColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyGRPQualityColumn.setName(MatchAnalysisConstant.GRP_QUALITY);
        columnNameList.add(dummyGRPQualityColumn);
        MetadataColumn dummyAtrrScoresColumn = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        dummyAtrrScoresColumn.setName(MatchAnalysisConstant.ATTRIBUTE_SCORES);
        columnNameList.add(dummyAtrrScoresColumn);
        return columnNameList.toArray(new MetadataColumn[columnNameList.size()]);
    }

    /**
     * get the key map of the match table's columns(<column, index>)
     * 
     * @param column
     * @param algoType
     * @param algoParameter
     * @param confidentWeight
     * @param columnIndexMap
     * @param matchInterval
     * @param attributeName
     * @return
     */
    public static Map<String, String> getMatchKeyMap(String column, String algoType, String algoParameter, int confidentWeight,
            double attrThreshold, Map<MetadataColumn, String> columnIndexMap, double matchInterval, String attributeName,
            String handleNull) {
        return getMatchKeyMap(column, algoType, algoParameter, confidentWeight, attrThreshold, columnIndexMap, matchInterval,
                attributeName, handleNull, null);// The jar path is null when the matcher's algorithm is not a type of
                                                 // "custom"
    }

    /**
     * get the key map of the match table's columns(<column, index>)
     * 
     * @param column
     * @param algoType
     * @param algoParameter
     * @param confidentWeight
     * @param columnIndexMap
     * @param matchInterval
     * @param attributeName
     * @param handleNull
     * @param jarPath
     * @return
     */
    public static Map<String, String> getMatchKeyMap(String column, String algoType, String algoParameter, int confidentWeight,
            double attrThreshold, Map<MetadataColumn, String> columnIndexMap, double matchInterval, String attributeName,
            String handleNull, String jarPath) {
        Map<String, String> matchKeyMap = getMatchKeyMap(column, algoType, algoParameter, confidentWeight, attrThreshold,
                columnIndexMap, matchInterval, attributeName, null, handleNull, jarPath, null);// the last one parameter need to
                                                                                               // be check by junit
        return matchKeyMap;
    }

    /**
     * 
     * Create match key map plus an additional "match key name" .
     * 
     * @param column
     * @param algoType
     * @param algoParameter
     * @param confidentWeight
     * @param columnIndexMap
     * @param matchInterval
     * @param attributeName
     * @param matchKeyName
     * @param handleNull
     * @param jarPath
     * @return
     */
    public static Map<String, String> getMatchKeyMap(String column, String algoType, String algoParameter, int confidentWeight,
            double attrThreshold, Map<MetadataColumn, String> columnIndexMap, double matchInterval, String attributeName,
            String matchKeyName, String handleNull, String jarPath, String tokenizationType) {
        Map<String, String> matchKeyMap = new HashMap<String, String>();
        for (MetadataColumn metaCol : columnIndexMap.keySet()) {
            if (metaCol.getName().equals(column)) {
                matchKeyMap.put(IRecordGrouping.COLUMN_IDX, columnIndexMap.get(metaCol));
                break;
            }
        }
        matchKeyMap.put(IRecordGrouping.MATCHING_TYPE, AttributeMatcherType.valueOf(algoType).name());
        matchKeyMap.put(IRecordGrouping.CUSTOMER_MATCH_CLASS, algoParameter);
        matchKeyMap.put(IRecordGrouping.CONFIDENCE_WEIGHT, String.valueOf(confidentWeight));
        matchKeyMap.put(IRecordGrouping.ATTRIBUTE_THRESHOLD, String.valueOf(attrThreshold));
        matchKeyMap.put(IRecordGrouping.RECORD_MATCH_THRESHOLD, String.valueOf(matchInterval));
        matchKeyMap.put(IRecordGrouping.ATTRIBUTE_NAME, attributeName);
        matchKeyMap.put(IRecordGrouping.MATCH_KEY_NAME, matchKeyName);
        matchKeyMap.put(IRecordGrouping.HANDLE_NULL, handleNull);
        matchKeyMap.put(IRecordGrouping.TOKENIZATION_TYPE, tokenizationType);
        matchKeyMap.put(IRecordGrouping.JAR_PATH, jarPath);
        return matchKeyMap;
    }

    /**
     * Get blocking key map
     * 
     * @return
     */
    public static Map<String, String> getBlockingKeyMap(String column, String preAlgo, String preAlgValue, String algorithm,
            String algorithmValue, String postAlgo, String postAlgoValue) {
        Map<String, String> blockKeyDefMap = new HashMap<String, String>();
        blockKeyDefMap.put(MatchAnalysisConstant.PRECOLUMN, column);
        blockKeyDefMap.put(MatchAnalysisConstant.PRE_ALGO, preAlgo);
        blockKeyDefMap.put(MatchAnalysisConstant.PRE_VALUE, preAlgValue);
        blockKeyDefMap.put(MatchAnalysisConstant.KEY_ALGO, algorithm);
        blockKeyDefMap.put(MatchAnalysisConstant.KEY_VALUE, algorithmValue);
        blockKeyDefMap.put(MatchAnalysisConstant.POST_ALGO, postAlgo);
        blockKeyDefMap.put(MatchAnalysisConstant.POST_VALUE, postAlgoValue);
        return blockKeyDefMap;
    }

    /**
     * join the string array to a single string, use escapeCharacter to escape the separator. MUST call
     * {@link #split(String, String, String)} to split the joined string. (if the string end with escapeCharacter, there
     * will join to the next column!!!)
     * 
     * @param array
     * @param separator recommend to use |
     * @param escapeCharacter recommend to use \
     * @return
     */
    public static String join(String[] array, String separator, String escapeCharacter) {
        String doubleEscapeCharacter = escapeCharacter + escapeCharacter;
        String escapeCharacterSeparator = escapeCharacter + separator;
        StringBuilder sr = new StringBuilder();
        for (String str : array) {
            String temp = StringUtils.replace(str, escapeCharacter, doubleEscapeCharacter);
            temp = StringUtils.replace(temp, separator, escapeCharacterSeparator);
            sr.append(temp + separator);
        }
        return StringUtils.removeEnd(sr.toString(), separator);
    }

    /**
     * split the string into a string array, use escapeCharacter to escape the separator. the string MUST be generated
     * by {@link #join(String[], String, String)}.(if the string end with escapeCharacter, there will join to the next
     * column!!!)
     * 
     * @param string
     * @param separator recommend to use |
     * @param escapeCharacter recommend to use \
     * @return
     */
    public static String[] split(String string, String separator, String escapeCharacter) {
        String doubleEscapeCharacter = escapeCharacter + escapeCharacter;
        String escapeCharacterSeparator = escapeCharacter + separator;
        String regex = "(?<!" + Pattern.quote(escapeCharacter) + ")" + Pattern.quote(separator); //$NON-NLS-1$ //$NON-NLS-2$
        ArrayList<String> strs = new ArrayList<String>();
        for (String s : string.split(regex)) {
            String temp = StringUtils.replace(s, escapeCharacterSeparator, separator);
            temp = StringUtils.replace(temp, doubleEscapeCharacter, escapeCharacter);
            strs.add(temp);
        }
        return strs.toArray(new String[strs.size()]);
    }

    /**
     * By default for analysis, the applied blocking key will be the key from key generation definition. This will be
     * refined when there is a need to define the applied blocking key manually by user later.
     * 
     * @param recordMatchingIndicator
     */
    public static void createAppliedBlockKeyByGenKey(RecordMatchingIndicator recordMatchingIndicator) {
        List<AppliedBlockKey> appliedBlockKeys = recordMatchingIndicator.getBuiltInMatchRuleDefinition().getAppliedBlockKeys();
        appliedBlockKeys.clear();
        List<BlockKeyDefinition> blockKeyDefs = recordMatchingIndicator.getBuiltInMatchRuleDefinition().getBlockKeys();
        if (blockKeyDefs != null && blockKeyDefs.size() > 0) {
            AppliedBlockKey appliedBlockKey = RulesPackage.eINSTANCE.getRulesFactory().createAppliedBlockKey();
            appliedBlockKey.setColumn(PluginConstant.BLOCK_KEY);
            appliedBlockKey.setName(PluginConstant.BLOCK_KEY);
            appliedBlockKeys.add(appliedBlockKey);
        }
    }

    /**
     * mzhao Get block key schema given the record matching indicator.
     * 
     * @param recordMatchingIndicator
     * @return
     */
    public static List<Map<String, String>> getBlockKeySchema(RecordMatchingIndicator recordMatchingIndicator) {
        List<AppliedBlockKey> appliedBlockKeys = recordMatchingIndicator.getBuiltInMatchRuleDefinition().getAppliedBlockKeys();

        List<Map<String, String>> blockKeySchema = new ArrayList<Map<String, String>>();
        for (KeyDefinition keyDef : appliedBlockKeys) {
            AppliedBlockKey appliedKeyDefinition = (AppliedBlockKey) keyDef;
            String column = appliedKeyDefinition.getColumn();

            if (StringUtils.equals(PluginConstant.BLOCK_KEY, column)) {
                // If there exist customized block key defined, get the key
                // parameters.
                List<BlockKeyDefinition> blockKeyDefs = recordMatchingIndicator.getBuiltInMatchRuleDefinition().getBlockKeys();
                for (BlockKeyDefinition blockKeyDef : blockKeyDefs) {
                    Map<String, String> blockKeyDefMap = new HashMap<String, String>();
                    blockKeyDefMap.putAll(getCustomizedBlockKeyParameter(blockKeyDef, blockKeyDef.getColumn()));
                    blockKeySchema.add(blockKeyDefMap);
                }
            } else {
                Map<String, String> blockKeyDefMap = new HashMap<String, String>();
                blockKeyDefMap.put(MatchAnalysisConstant.PRECOLUMN, column);
                blockKeySchema.add(blockKeyDefMap);
            }
        }
        return blockKeySchema;
    }

    /**
     * DOC zhao Comment method "getCustomizedBlockKeyParameter".
     * 
     * @param appliedKeyDefinition
     * @param column
     * @return
     */
    private static Map<String, String> getCustomizedBlockKeyParameter(BlockKeyDefinition blockKeydef, String column) {
        String preAlgo = blockKeydef.getPreAlgorithm().getAlgorithmType();
        String preAlgoValue = blockKeydef.getPreAlgorithm().getAlgorithmParameters();
        String algorithm = blockKeydef.getAlgorithm().getAlgorithmType();
        String algorithmValue = blockKeydef.getAlgorithm().getAlgorithmParameters();
        String postAlgo = blockKeydef.getPostAlgorithm().getAlgorithmType();
        String postAlgValue = blockKeydef.getPostAlgorithm().getAlgorithmParameters();
        Map<String, String> blockKeyDefMap = AnalysisRecordGroupingUtils.getBlockingKeyMap(column, preAlgo, preAlgoValue,
                algorithm, algorithmValue, postAlgo, postAlgValue);
        return blockKeyDefMap;
    }

    /**
     * DOC zhao Comment method "setRuleMatcher".
     * 
     * @param columnMap
     * @param recordMatchingIndicator
     * @param analysisMatchRecordGrouping
     * @throws BusinessException
     */
    public static void setRuleMatcher(Map<MetadataColumn, String> columnMap, RecordMatchingIndicator recordMatchingIndicator,
            AnalysisMatchRecordGrouping analysisMatchRecordGrouping) throws BusinessException {
        List<MatchRule> matchRules = recordMatchingIndicator.getBuiltInMatchRuleDefinition().getMatchRules();

        // Column index list store all the indices.
        List<String> allColumnIndice = new ArrayList<String>();
        for (MatchRule matcher : matchRules) {
            if (matcher == null) {
                continue;
            }
            List<Map<String, String>> currentRuleMatcher = new ArrayList<Map<String, String>>();
            List<String> currentColumnIndice = new ArrayList<String>();
            for (MatchKeyDefinition matchDef : matcher.getMatchKeys()) {
                // check if the current match key does not contain any
                // column,throw exception, do not continue
                if (matchDef.getColumn() == null || StringUtils.EMPTY.equals(matchDef.getColumn())) {
                    BusinessException businessException = new BusinessException();
                    businessException.setAdditonalMessage(Messages.getString("MatchAnalysisExecutor.NoColumnInMatchKey", //$NON-NLS-1$
                            matchDef.getName()));
                    throw businessException;
                }
                String algorithmType = matchDef.getAlgorithm().getAlgorithmType();
                Map<String, String> matchKeyMap = getMatchKeyMap(columnMap, matcher, matchDef, algorithmType);
                addMatchKeyOrderbyColumnIdx(currentRuleMatcher, matchKeyMap);
                currentColumnIndice.add(matchKeyMap.get(IRecordGrouping.COLUMN_IDX));
            }

            if (allColumnIndice.isEmpty()) {
                for (Map<String, String> matchKey : currentRuleMatcher) {
                    String colIdx = matchKey.get(IRecordGrouping.COLUMN_IDX);
                    allColumnIndice.add(colIdx);
                }

            } else {
                refineMatcherWithDummy(analysisMatchRecordGrouping, allColumnIndice, currentColumnIndice, currentRuleMatcher);
            }
            analysisMatchRecordGrouping.addRuleMatcher(currentRuleMatcher);
        }
    }

    /**
     * DOC yyin Comment method "initialMatchGrouping".
     * 
     * @param columnMap
     * @param recordMatchingIndicator
     * @param analysisMatchRecordGrouping
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static void initialMatchGrouping(Map<MetadataColumn, String> columnMap,
            RecordMatchingIndicator recordMatchingIndicator, AnalysisMatchRecordGrouping analysisMatchRecordGrouping)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (recordMatchingIndicator.getBuiltInMatchRuleDefinition().getRecordLinkageAlgorithm()
                .equals(RecordMatcherType.simpleVSRMatcher.name())) {
            analysisMatchRecordGrouping.setRecordLinkAlgorithm(RecordMatcherType.simpleVSRMatcher);
            analysisMatchRecordGrouping.initialize();
        } else {
            analysisMatchRecordGrouping.setRecordLinkAlgorithm(RecordMatcherType.T_SwooshAlgorithm);
            analysisMatchRecordGrouping.setOrginalInputColumnSize(columnMap.size() + 1);
            analysisMatchRecordGrouping.initialize();
            SurvivorShipAlgorithmParams survivorShipAlgorithmParams = createSurvivorShipAlgorithmParams(
                    analysisMatchRecordGrouping, recordMatchingIndicator, columnMap);
            analysisMatchRecordGrouping.setSurvivorShipAlgorithmParams(survivorShipAlgorithmParams);
        }
    }

    /**
     * 
     * DOC zshen Comment method "createSurvivorShipAlgorithmParams".
     * Same with {@link SurvivorshipUtils#createSurvivorShipAlgorithmParams(AnalysisMatchRecordGrouping, List, List, Map, Map)} so
     * that any modify need to synchronization them with same time
     * 
     * @param analysisMatchRecordGrouping
     * @param recordMatchingIndicator
     * @param columnMap
     * @return
     */
    public static SurvivorShipAlgorithmParams createSurvivorShipAlgorithmParams(
            AnalysisMatchRecordGrouping analysisMatchRecordGrouping, RecordMatchingIndicator recordMatchingIndicator,
            Map<MetadataColumn, String> columnMap) {
        SurvivorShipAlgorithmParams survivorShipAlgorithmParams = new SurvivorShipAlgorithmParams();

        // Survivorship functions.
        List<SurvivorshipKeyDefinition> survivorshipKeyDefs = recordMatchingIndicator.getBuiltInMatchRuleDefinition()
                .getSurvivorshipKeys();
        List<SurvivorshipFunction> survFunctions = new ArrayList<SurvivorshipFunction>();
        for (SurvivorshipKeyDefinition survDef : survivorshipKeyDefs) {
            SurvivorshipFunction func = survivorShipAlgorithmParams.new SurvivorshipFunction();
            func.setSurvivorShipKey(survDef.getName());
            func.setParameter(survDef.getFunction().getAlgorithmParameters());
            func.setSurvivorShipAlgoEnum(SurvivorShipAlgorithmEnum.getTypeBySavedValue(survDef.getFunction().getAlgorithmType()));
            survFunctions.add(func);
        }
        survivorShipAlgorithmParams
                .setSurviorShipAlgos(survFunctions.toArray(new SurvivorshipFunction[survivorshipKeyDefs.size()]));

        // Set default survivorship functions.
        List<DefaultSurvivorshipDefinition> defSurvDefs = recordMatchingIndicator.getBuiltInMatchRuleDefinition()
                .getDefaultSurvivorshipDefinitions();
        Map<Integer, SurvivorshipFunction> defaultSurvRules = new HashMap<Integer, SurvivorshipFunction>();

        for (MetadataColumn metaColumn : columnMap.keySet()) {
            String dataTypeName = metaColumn.getTalendType();
            for (DefaultSurvivorshipDefinition defSurvDef : defSurvDefs) {
                // the column's data type start with id_, so need to add id_ ahead of the default survivorship's data
                // type before judging if they are equal
                if (StringUtils.equals(dataTypeName, "id_" + defSurvDef.getDataType()) || StringUtils.equals(defSurvDef.getDataType(), "Number") && JavaTypesManager.isNumber(dataTypeName)) { //$NON-NLS-1$
                    putNewSurvFunc(columnMap, survivorShipAlgorithmParams, defaultSurvRules, metaColumn, defSurvDef);
                    break;
                }
            }// End for: if no func defined, then the value will be taken from one of the records in a group (1st
             // one ).
        }

        survivorShipAlgorithmParams.setDefaultSurviorshipRules(defaultSurvRules);

        // Set the record matcher
        CombinedRecordMatcher combinedRecordMatcher = analysisMatchRecordGrouping.getCombinedRecordMatcher();
        survivorShipAlgorithmParams.setRecordMatcher(combinedRecordMatcher);
        Map<IRecordMatcher, SurvivorshipFunction[]> survAlgos = new HashMap<IRecordMatcher, SurvivorshipFunction[]>();
        SurvivorshipFunction[] survFuncs = survivorShipAlgorithmParams.getSurviorShipAlgos();
        Map<Integer, SurvivorshipFunction> colIdx2DefaultSurvFunc = survivorShipAlgorithmParams.getDefaultSurviorshipRules();
        int matchRuleIdx = -1;
        List<List<Map<String, String>>> multiRules = analysisMatchRecordGrouping.getMultiMatchRules();
        for (List<Map<String, String>> matchrule : multiRules) {
            matchRuleIdx++;
            if (matchrule == null) {
                continue;
            }

            SurvivorshipFunction[] surFuncsInMatcher = new SurvivorshipFunction[matchrule.size()];
            int idx = 0;
            for (Map<String, String> mkDef : matchrule) {
                String matcherType = mkDef.get(IRecordGrouping.MATCHING_TYPE);
                if (AttributeMatcherType.DUMMY.name().equalsIgnoreCase(matcherType)) {
                    // Find the func from default survivorship rule.
                    surFuncsInMatcher[idx] = colIdx2DefaultSurvFunc.get(Integer.valueOf(mkDef.get(IRecordGrouping.COLUMN_IDX)));
                    if (surFuncsInMatcher[idx] == null) {
                        // Use CONCATENATE by default if not specified .
                        surFuncsInMatcher[idx] = survivorShipAlgorithmParams.new SurvivorshipFunction();
                        surFuncsInMatcher[idx].setSurvivorShipAlgoEnum(SurvivorShipAlgorithmEnum.CONCATENATE);
                        // MOD TDQ-11774 set a default parameter
                        surFuncsInMatcher[idx].setParameter(SurvivorshipUtils.DEFAULT_CONCATENATE_PARAMETER);
                    }
                } else {
                    // Find the func from existing survivorship rule list.
                    for (SurvivorshipFunction survFunc : survFuncs) {
                        String keyName = mkDef.get(IRecordGrouping.MATCH_KEY_NAME);
                        if (keyName.equals(survFunc.getSurvivorShipKey())) {
                            surFuncsInMatcher[idx] = survFunc;
                            break;
                        }
                    }

                }
                idx++;
            }

            // Add the funcs to a specific record matcher. NOTE that the index of matcher must be coincidence to the
            // index of match rule.
            survAlgos.put(combinedRecordMatcher.getMatchers().get(matchRuleIdx), surFuncsInMatcher);

        }

        survivorShipAlgorithmParams.setSurvivorshipAlgosMap(survAlgos);

        return survivorShipAlgorithmParams;
    }

    /**
     * Create a new surv function and put it into map given column index as the key.
     * 
     * @param columnMap
     * @param survivorShipAlgorithmParams
     * @param defaultSurvRules
     * @param metaColumn
     * @param defSurvDef
     */
    private static void putNewSurvFunc(Map<MetadataColumn, String> columnMap,
            SurvivorShipAlgorithmParams survivorShipAlgorithmParams, Map<Integer, SurvivorshipFunction> defaultSurvRules,
            MetadataColumn metaColumn, DefaultSurvivorshipDefinition defSurvDef) {
        SurvivorshipFunction survFunc = survivorShipAlgorithmParams.new SurvivorshipFunction();
        survFunc.setParameter(defSurvDef.getFunction().getAlgorithmParameters());
        survFunc.setSurvivorShipAlgoEnum(SurvivorShipAlgorithmEnum.getTypeBySavedValue(defSurvDef.getFunction()
                .getAlgorithmType()));
        defaultSurvRules.put(Integer.valueOf(columnMap.get(metaColumn)), survFunc);
    }

    private static void refineMatcherWithDummy(AnalysisMatchRecordGrouping analysisMatchRecordGrouping,
            List<String> allColumnIndice, List<String> currentColumnIndice, List<Map<String, String>> currentRuleMatcher) {
        List<List<Map<String, String>>> multiMatchRules = analysisMatchRecordGrouping.getMultiMatchRules();
        // Refine the other matchers with dummy matcher
        for (Map<String, String> matchKey : currentRuleMatcher) {
            String colIdx = matchKey.get(IRecordGrouping.COLUMN_IDX);
            if (!allColumnIndice.contains(colIdx)) {
                allColumnIndice.add(colIdx);
                // Create dummy matcher
                Map<String, String> dummyMatcherMap = new HashMap<String, String>();
                dummyMatcherMap.put(IRecordGrouping.COLUMN_IDX, colIdx);
                dummyMatcherMap.put(IRecordGrouping.MATCHING_TYPE, AttributeMatcherType.DUMMY.name());

                // Refine the multi match rules with dummy matcher.
                for (List<Map<String, String>> matchRule : multiMatchRules) {
                    addMatchKeyOrderbyColumnIdx(matchRule, dummyMatcherMap);
                }
            }
        }

        // Refine current matcher with dummy matcher by the knowledge of the
        // previous matcher.
        if (multiMatchRules != null && multiMatchRules.size() > 0) {
            // Here the 0 index is safe because all matchers before are
            // same record size with same match key.
            List<Map<String, String>> preMatcher = multiMatchRules.get(0);
            for (Map<String, String> preMatchKey : preMatcher) {
                String colIdx = preMatchKey.get(IRecordGrouping.COLUMN_IDX);
                if (!currentColumnIndice.contains(colIdx)) {
                    // Create dummy matcher
                    Map<String, String> dummyMatcherMap = new HashMap<String, String>();
                    dummyMatcherMap.put(IRecordGrouping.COLUMN_IDX, colIdx);
                    dummyMatcherMap.put(IRecordGrouping.MATCHING_TYPE, AttributeMatcherType.DUMMY.name());
                    addMatchKeyOrderbyColumnIdx(currentRuleMatcher, dummyMatcherMap);
                }
            }
        }
    }

    private static Map<String, String> getMatchKeyMap(Map<MetadataColumn, String> columnMap, MatchRule matcher,
            MatchKeyDefinition matchDef, String algorithmType) {
        Map<String, String> matchKeyMap = null;
        if (AttributeMatcherType.get(algorithmType) == AttributeMatcherType.CUSTOM) {
            matchKeyMap = AnalysisRecordGroupingUtils.getMatchKeyMap(matchDef.getColumn(), algorithmType, matchDef.getAlgorithm()
                    .getAlgorithmParameters(), matchDef.getConfidenceWeight(), matchDef.getThreshold(), columnMap, matcher
                    .getMatchInterval(), matchDef.getColumn(), matchDef.getName(), matchDef.getHandleNull(),
                    CustomAttributeMatcherHelper.getFullJarPath(matchDef.getAlgorithm().getAlgorithmParameters()), null);
        } else {
            matchKeyMap = AnalysisRecordGroupingUtils.getMatchKeyMap(matchDef.getColumn(), algorithmType, matchDef.getAlgorithm()
                    .getAlgorithmParameters(), matchDef.getConfidenceWeight(), matchDef.getThreshold(), columnMap, matcher
                    .getMatchInterval(), matchDef.getColumn(), matchDef.getName(), matchDef.getHandleNull(), null, matchDef
                    .getTokenizationType());
        }
        return matchKeyMap;
    }

    private static void addMatchKeyOrderbyColumnIdx(List<Map<String, String>> currentRuleMatcher, Map<String, String> matchKeyMap) {
        int index = 0;
        for (Map<String, String> currentMatchKey : currentRuleMatcher) {
            int currColIdx = Integer.valueOf(currentMatchKey.get(IRecordGrouping.COLUMN_IDX));
            int toBeInsertColIdx = Integer.valueOf(matchKeyMap.get(IRecordGrouping.COLUMN_IDX));
            if (currColIdx > toBeInsertColIdx) {
                currentRuleMatcher.add(index, matchKeyMap);
                return;
            }
            index++;
        }
        currentRuleMatcher.add(matchKeyMap);
    }

}
