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
package org.talend.dq.analysis.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.RecordMatcherType;
import org.talend.dataquality.record.linkage.genkey.BlockingKeyHandler;
import org.talend.dataquality.record.linkage.grouping.AnalysisMatchRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.MatchGroupResultConsumer;
import org.talend.dataquality.record.linkage.grouping.swoosh.AnalysisSwooshMatchRecordGrouping;
import org.talend.dq.analysis.AnalysisRecordGroupingUtils;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * created by zshen on Sep 16, 2013 Detailled comment
 * Used for the "chart" button in the analysis.
 * 
 */
public class ExecuteMatchRuleHandler {

    public TypedReturnCode<MatchGroupResultConsumer> execute(Map<MetadataColumn, String> columnMap,
            RecordMatchingIndicator recordMatchingIndicator, List<Object[]> matchRows, BlockKeyIndicator blockKeyIndicator,
            MatchGroupResultConsumer matchResultConsumer) {

        TypedReturnCode<MatchGroupResultConsumer> returnCode = new TypedReturnCode<MatchGroupResultConsumer>(false);
        returnCode.setObject(matchResultConsumer);

        // By default for analysis, the applied blocking key will be the key
        // from key generation definition. This
        // will be refined when there is a need to define the applied blocking
        // key manually by user later.
        AnalysisRecordGroupingUtils.createAppliedBlockKeyByGenKey(recordMatchingIndicator);
        ReturnCode computeMatchGroupReturnCode = null;
        // Blocking key specified.
        computeMatchGroupReturnCode = computeMatchGroupWithBlockKey(recordMatchingIndicator, blockKeyIndicator, columnMap,
                matchResultConsumer, matchRows);
        returnCode.setOk(computeMatchGroupReturnCode.isOk());
        returnCode.setMessage(computeMatchGroupReturnCode.getMessage());
        return returnCode;
    }

    /**
     * DOC zhao Comment method "computeMatchGroupWithBlockKey".
     * 
     * @param recordMatchingIndicator
     * @param blockKeyIndicator
     * @param columnMap
     * @param matchResultConsumer
     * @param matchRows
     */
    private ReturnCode computeMatchGroupWithBlockKey(RecordMatchingIndicator recordMatchingIndicator,
            BlockKeyIndicator blockKeyIndicator, Map<MetadataColumn, String> columnMap,
            MatchGroupResultConsumer matchResultConsumer, List<Object[]> matchRows) {
        ReturnCode rc = new ReturnCode(Boolean.TRUE);

        Map<String, List<String[]>> resultWithBlockKey = computeBlockingKey(columnMap, matchRows, recordMatchingIndicator);
        Iterator<String> keyIterator = resultWithBlockKey.keySet().iterator();

        TreeMap<Object, Long> blockSize2Freq = new TreeMap<Object, Long>();
        while (keyIterator.hasNext()) {
            // Match group with in each block
            List<String[]> matchRowsInBlock = resultWithBlockKey.get(keyIterator.next());
            List<Object[]> objList = new ArrayList<Object[]>();
            objList.addAll(matchRowsInBlock);
            // Add check match key
            try {
                computeMatchGroupResult(columnMap, matchResultConsumer, objList, recordMatchingIndicator);
            } catch (BusinessException e) {
                rc.setOk(Boolean.FALSE);
                rc.setMessage(e.getAdditonalMessage());
                return rc;
            }

            // Store indicator
            Integer blockSize = matchRowsInBlock.size();
            if (blockSize == null) { // should not happen
                blockSize = 0;
            }
            Long freq = blockSize2Freq.get(Long.valueOf(blockSize));
            if (freq == null) {
                freq = 0l;
            }
            blockSize2Freq.put(Long.valueOf(blockSize), freq + 1);
        }
        blockKeyIndicator.setBlockSize2frequency(blockSize2Freq);
        return rc;
    }

    private Map<String, List<String[]>> computeBlockingKey(Map<MetadataColumn, String> columnMap, List<Object[]> matchRows,
            RecordMatchingIndicator recordMatchingIndicator) {
        List<Map<String, String>> blockKeySchema = AnalysisRecordGroupingUtils.getBlockKeySchema(recordMatchingIndicator);
        Map<String, String> colName2IndexMap = new HashMap<String, String>();
        for (MetadataColumn metaCol : columnMap.keySet()) {
            if(metaCol.getName()==null){
                colName2IndexMap.put(metaCol.getLabel(), columnMap.get(metaCol));
            }else{
                colName2IndexMap.put(metaCol.getName(), columnMap.get(metaCol));
            }
        }
        BlockingKeyHandler blockKeyHandler = new BlockingKeyHandler(blockKeySchema, colName2IndexMap);
        blockKeyHandler.setInputData(matchRows);
        blockKeyHandler.run();
        Map<String, List<String[]>> resultData = blockKeyHandler.getResultDatas();

        return resultData;

    }

    /**
     * DOC zhao Comment method "computeMatchGroupResult".
     * 
     * @param columnMap
     * @param matchResultConsumer
     * @param matchRows
     * @return
     */
    private void computeMatchGroupResult(Map<MetadataColumn, String> columnMap, MatchGroupResultConsumer matchResultConsumer,
            List<Object[]> matchRows, RecordMatchingIndicator recordMatchingIndicator) throws BusinessException {
        boolean isOpenWarningDialog = false;
        AnalysisMatchRecordGrouping analysisMatchRecordGrouping = null;
        if (recordMatchingIndicator.getBuiltInMatchRuleDefinition().getRecordLinkageAlgorithm()
                .equals(RecordMatcherType.T_SwooshAlgorithm.name())) {
            analysisMatchRecordGrouping = new AnalysisSwooshMatchRecordGrouping(matchResultConsumer);
        } else {
            analysisMatchRecordGrouping = new AnalysisMatchRecordGrouping(matchResultConsumer);
        }
        AnalysisRecordGroupingUtils.setRuleMatcher(columnMap, recordMatchingIndicator, analysisMatchRecordGrouping);
        analysisMatchRecordGrouping.setMatchRows(matchRows);

        try {

            AnalysisRecordGroupingUtils.initialMatchGrouping(columnMap, recordMatchingIndicator, analysisMatchRecordGrouping);

            analysisMatchRecordGrouping.run();
        } catch (InstantiationException e1) {
            isOpenWarningDialog = true;
        } catch (IllegalAccessException e1) {
            isOpenWarningDialog = true;
        } catch (ClassNotFoundException e1) {
            isOpenWarningDialog = true;
        } finally {
            if (isOpenWarningDialog) {
                BusinessException businessException = new BusinessException();
                throw businessException;

            }
        }

    }

}
