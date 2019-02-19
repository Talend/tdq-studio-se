// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.composite.utils;

import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.constant.TokenizedResolutionMethod;
import org.talend.dataquality.record.linkage.ui.composite.table.ISortComparator;
import org.talend.dataquality.record.linkage.ui.composite.table.SortComparator;
import org.talend.dataquality.record.linkage.ui.composite.table.SortState;
import org.talend.dataquality.record.linkage.utils.DefaultSurvivorShipDataTypeEnum;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.KeyDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dq.analysis.AnalysisRecordGroupingUtils;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by zshen on Aug 6, 2013 Detailled comment
 * 
 */
public class MatchRuleAnlaysisUtils {

    public static List<String> getColumnFromRuleMatcher(MatchRule ruleMatcher) {
        List<String> returnList = new ArrayList<String>();
        return returnList;
    }

    public static List<String> getColumnFromBlockKey(BlockKeyDefinition blockKeyDefinition) {
        List<String> returnList = new ArrayList<String>();
        return returnList;
    }

    /**
     * DOC zshen Comment method "createDefaultRow".
     * 
     * @param columnName
     * @return
     */
    public static MatchKeyDefinition createDefaultMatchRow(String columnName) {

        MatchKeyDefinition createMatchKeyDefinition1 = RulesFactory.eINSTANCE.createMatchKeyDefinition();
        AlgorithmDefinition createAlgorithmDefinition1 = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        // by default the name of the match attribute rule is the name of the selected column
        createMatchKeyDefinition1.setName(columnName);
        createMatchKeyDefinition1.setColumn(columnName);
        createMatchKeyDefinition1.setConfidenceWeight(1);
        createMatchKeyDefinition1.setHandleNull(HandleNullEnum.NULL_MATCH_NULL.getValue());
        createMatchKeyDefinition1.setTokenizationType(TokenizedResolutionMethod.NO.getComponentValue());
        createAlgorithmDefinition1.setAlgorithmParameters(StringUtils.EMPTY);
        createAlgorithmDefinition1.setAlgorithmType(AttributeMatcherType.values()[0].name());
        createMatchKeyDefinition1.setAlgorithm(createAlgorithmDefinition1);
        createMatchKeyDefinition1.setThreshold(1.0);
        return createMatchKeyDefinition1;
    }

    public static List<MatchRule> convertDataMapToRuleMatcher(Map<String, String> columnMap) {
        List<MatchRule> matcherList = new ArrayList<MatchRule>();
        if (columnMap == null) {
            return matcherList;
        }

        MatchRule createRuleMatcher = RulesFactory.eINSTANCE.createMatchRule();
        for (String columnName : columnMap.keySet()) {
            MatchKeyDefinition createDefaultMatchRow = createDefaultMatchRow(columnName);
            createRuleMatcher.getMatchKeys().add(createDefaultMatchRow);
        }
        matcherList.add(createRuleMatcher);
        return matcherList;
    }

    /**
     * DOC yyin Comment method "ruleMatcherConvert".
     * 
     * @param blockKeyDef
     * @param columnMap
     * @return
     */
    public static List<Map<String, String>> blockingKeyDataConvert(List<KeyDefinition> blockKeyDefList) {
        List<Map<String, String>> resultListData = new ArrayList<Map<String, String>>();
        for (KeyDefinition keyDef : blockKeyDefList) {
            BlockKeyDefinition blockKeydef = (BlockKeyDefinition) keyDef;
            String column = blockKeydef.getColumn();
            String preAlgo = blockKeydef.getPreAlgorithm().getAlgorithmType();
            String preAlgoValue = blockKeydef.getPreAlgorithm().getAlgorithmParameters();
            String algorithm = blockKeydef.getAlgorithm().getAlgorithmType();
            String algorithmValue = blockKeydef.getAlgorithm().getAlgorithmParameters();
            String postAlgo = blockKeydef.getPostAlgorithm().getAlgorithmType();
            String postAlgValue = blockKeydef.getPostAlgorithm().getAlgorithmParameters();
            Map<String, String> blockKeyDefMap =
                    AnalysisRecordGroupingUtils.getBlockingKeyMap(column, preAlgo, preAlgoValue, algorithm,
                            algorithmValue, postAlgo, postAlgValue);
            resultListData.add(blockKeyDefMap);
        }

        return resultListData;
    }

    /**
     * Get recording matching indicator from analysis
     * 
     * @param analysis
     * @return
     */
    public static RecordMatchingIndicator getRecordMatchIndicatorFromAna(Analysis analysis) {
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator ind : indicators) {
            if (ind instanceof RecordMatchingIndicator) {
                return (RecordMatchingIndicator) ind;
            }
        }
        return null;
    }

    /**
     * Get recording matching indicator and Blocking Indicator from analysis
     * 
     * @param analysis
     * @return the index 0 will be RecordMatchingIndicator and index 1 will be BlockKeyIndicator
     */
    public static Object[] getNeedIndicatorFromAna(Analysis analysis) {
        Object[] returnList = new Object[2];
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator ind : indicators) {
            if (ind instanceof RecordMatchingIndicator) {
                returnList[0] = ind;
            } else if (ind instanceof BlockKeyIndicator) {
                returnList[1] = ind;
            }
        }
        // If match rule definition is null, create a default.
        if (returnList[0] == null) {
            returnList[0] = ColumnsetPackage.eINSTANCE.getColumnsetFactory().createRecordMatchingIndicator();
        }
        // If blocking key indicator is nul, create a default.
        if (returnList[1] == null) {
            returnList[1] = ColumnsetPackage.eINSTANCE.getColumnsetFactory().createBlockKeyIndicator();
        }
        return returnList;
    }

    /**
     * check if the column name equals to these additional special columns
     * 
     * @param columnName
     * @return
     */
    public static boolean isEqualsToAdditionalColumn(String columnName) {
        if (MatchAnalysisConstant.GID.equals(columnName) || MatchAnalysisConstant.GRP_QUALITY.equals(columnName)
                || MatchAnalysisConstant.GROUP_SIZE.equals(columnName)
                || MatchAnalysisConstant.SCORE.equals(columnName)
                || MatchAnalysisConstant.ATTRIBUTE_SCORES.equals(columnName)
                || MatchAnalysisConstant.BLOCK_KEY.equals(columnName)) {
            return true;
        }
        return false;
    }

    /**
     * refresh data Table.
     * 
     * @param analysis
     * 
     * @param matchResultConsumer
     */
    public static void refreshDataTable(ModelElement analysis, List<Object[]> resultData) {
        ITDQRepositoryService tdqRepService = null;

        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
            tdqRepService =
                    (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(ITDQRepositoryService.class);
        }
        if (tdqRepService != null) {
            tdqRepService.refreshTableWithResult(analysis, resultData);
        }
    }

    /**
     * sorting the result data by GID,master
     * 
     * @param allColumns
     * @param resultData
     * @return
     */
    public static List<Object[]> sortResultByGID(String[] allColumns, List<Object[]> resultData) {
        int gidIndex = -1;
        int masterIndex = -1;
        for (int i = 0; i < allColumns.length; i++) {
            if (StringUtils.endsWithIgnoreCase(allColumns[i], MatchAnalysisConstant.GID)) {
                gidIndex = i;
            } else if (StringUtils.endsWithIgnoreCase(allColumns[i], MatchAnalysisConstant.MASTER)) {
                masterIndex = i;
            }
        }
        // Sort by master first
        final int masterIdx = masterIndex;
        Comparator<Object[]> comparator = new Comparator<Object[]>() {

            @Override
            public int compare(Object[] row1, Object[] row2) {
                return ((String) row2[masterIdx]).compareTo((String) row1[masterIdx]);
            }

        };
        java.util.Collections.sort(resultData, comparator);

        insertionSort(resultData, gidIndex);
        return resultData;
    }

    public static void insertionSort(List<Object[]> data, int gidIdx) {
        int in, out;

        for (out = 1; out < data.size(); out++) {
            Object[] temp = data.get(out);
            in = out;

            while (in > 0 && !isSameGroup(data.get(in - 1)[gidIdx].toString(), (temp[gidIdx]).toString())) {
                data.set(in, data.get(in - 1));
                --in;
            }
            data.set(in, temp);
        }
    }

    /**
     * 
     * @param group ID one
     * @param group ID two.
     * @return true if they are the same group considering the two merged groups (groupID contains two more UUID).
     */
    public static boolean isSameGroup(String groupID1, String groupID2) {
        if (groupID1 == null || groupID1.trim().equals(StringUtils.EMPTY) || groupID2 == null
                || groupID2.trim().equals(StringUtils.EMPTY)) {
            return false;
        }
        String[] ids1 = StringUtils.splitByWholeSeparatorPreserveAllTokens(groupID1, PluginConstant.COMMA_STRING);
        String[] ids2 = StringUtils.splitByWholeSeparatorPreserveAllTokens(groupID2, PluginConstant.COMMA_STRING);
        for (String id1 : ids1) {
            for (String id2 : ids2) {
                if (id1.equalsIgnoreCase(id2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Object[]> sortDataByColumn(final SortState sortState, List<Object[]> resultData,
            final List<ModelElement> columns) {

        Comparator<Object[]> comparator = new Comparator<Object[]>() {

            ISortComparator sortComparator = null;

            @Override
            public int compare(Object[] row1, Object[] row2) {
                // when the user select the special column which has no result yet
                if ((row1.length - 1) < sortState.getSelectedColumnIndex()) {
                    return 0;
                }
                Object value1 = row1[sortState.getSelectedColumnIndex()];
                Object value2 = row2[sortState.getSelectedColumnIndex()];

                // get the sort comparator according to its type.
                try {
                    if (sortComparator == null) {
                        sortComparator = getSortComparator(value1, sortState.getSelectedColumnIndex());
                    }
                } catch (ParseException e) {
                    return 0;
                }
                if (sortComparator == null) {
                    return 0;
                }
                switch (sortState.getCurrentSortDirection()) {
                case ASC:
                    return sortComparator.compareTwo(value1, value2);
                case DESC:
                    return sortComparator.compareTwo(value2, value1);
                default:
                    return 0;
                }
            }

            // when the columns is TdColumn, use its sqlType to compare
            private ISortComparator getSortComparator(Object object, int index) throws ParseException {
                if (!(columns.get(0) instanceof TdColumn)) {// if not a db column
                    return SortComparator.getSortComparator(Types.VARCHAR);
                }
                if (columns.size() <= index) {// special columns
                    if (sortState.isGroupSizeColumn()) {// group size is integer type
                        return SortComparator.getSortComparator(Types.INTEGER);
                    } else {
                        return SortComparator.getSortComparator(Types.VARCHAR);
                    }
                }
                TdColumn element = (TdColumn) columns.get(index);
                return SortComparator.getSortComparator(element.getSqlDataType().getJavaDataType());
            }

        };

        java.util.Collections.sort(resultData, comparator);
        return resultData;
    }

    public static boolean isSurvivorShipFunctionConsistentWithType(String algorithmType, String dataType) {
        SurvivorShipAlgorithmEnum survivorShipAlgorithm = SurvivorShipAlgorithmEnum.getTypeBySavedValue(algorithmType);
        switch (survivorShipAlgorithm) {
        case LARGEST:
        case SMALLEST:
            // compare column's talend type || compare DefaultSurvivorShipDataTypeEnum own type
            return JavaTypesManager.isNumber(dataType)
                    || DefaultSurvivorShipDataTypeEnum.NUMBER.getValue().equals(dataType);
        case LONGEST:
        case SHORTEST:
            return JavaTypesManager.isString(dataType)
                    || DefaultSurvivorShipDataTypeEnum.STRING.getValue().equals(dataType);
        case PREFER_TRUE:
        case PREFER_FALSE:
            return JavaTypesManager.isBoolean(dataType)
                    || DefaultSurvivorShipDataTypeEnum.BOOLEAN.getValue().equals(dataType);
        default:
            return true;
        }
    }

    /**
     * duplicate with AnalysisMatchParameterAdapter.isMatchMetadataType()
     */
    public static boolean isMatchMetadataType(String dataTypeName, DefaultSurvivorshipDefinition defSurvDef) {
        return isMatchMetadataType(dataTypeName, defSurvDef.getDataType());
    }

    /**
     * duplicate with AnalysisMatchParameterAdapter.isMatchMetadataType()
     */
    public static boolean isMatchMetadataType(String talendDataTypeName, String defDataType) {
        if (StringUtils.equals(talendDataTypeName, "id_" + defDataType)) { //$NON-NLS-1$
            return true;
        } else if (StringUtils.equals(defDataType, DefaultSurvivorShipDataTypeEnum.STRING.getValue())) {
            return JavaTypesManager.isString(talendDataTypeName);
        } else if (StringUtils.equals(defDataType, DefaultSurvivorShipDataTypeEnum.NUMBER.getValue())) {
            return JavaTypesManager.isNumber(talendDataTypeName);
        } else {
            return false;
        }
    }
}
