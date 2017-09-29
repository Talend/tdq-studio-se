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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.talend.commons.exception.BusinessException;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.indicators.columnset.BlockKeyIndicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.matchmerge.Attribute;
import org.talend.dataquality.matchmerge.Record;
import org.talend.dataquality.record.linkage.constant.RecordMatcherType;
import org.talend.dataquality.record.linkage.genkey.AbstractGenerateKey;
import org.talend.dataquality.record.linkage.grouping.AnalysisMatchRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.MatchGroupResultConsumer;
import org.talend.dataquality.record.linkage.grouping.swoosh.AnalysisSwooshMatchRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.swoosh.RichRecord;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dq.analysis.AnalysisRecordGroupingUtils;

/**
 * For each record: find its related block, and use this block's matching to do the match.
 * Used for running the match analysis
 * 
 */
public class BlockAndMatchManager {

    private Map<String, OneBlockMatching> blockMatchMap = new HashMap<String, OneBlockMatching>();

    // Used to read each record from the data source
    private Iterator<Record> resultIterator;

    private MatchGroupResultConsumer matchResultConsumer;

    private RecordMatchingIndicator recordMatchingIndicator;

    private BlockKeyIndicator blockKeyIndicator;

    private Map<MetadataColumn, String> columnMap;

    private AbstractGenerateKey blockKeyGenerator = new AbstractGenerateKey();

    private Map<String, String> colName2IndexMap = new HashMap<String, String>();

    private List<Map<String, String>> blockKeyDefinition;

    public BlockAndMatchManager(Iterator<Record> resultIterator, MatchGroupResultConsumer matchResultConsumer,
            Map<MetadataColumn, String> columnMap, RecordMatchingIndicator recordMatchingIndicator,
            BlockKeyIndicator blockKeyIndicator) {
        this.resultIterator = resultIterator;
        this.matchResultConsumer = matchResultConsumer;
        this.recordMatchingIndicator = recordMatchingIndicator;
        this.blockKeyIndicator = blockKeyIndicator;
        this.columnMap = columnMap;
        for (MetadataColumn metaCol : columnMap.keySet()) {
            colName2IndexMap.put(metaCol.getName(), columnMap.get(metaCol));
        }
        initBlockKeyDefinitions();
    }

    public void initBlockKeyDefinitions() {
        // By default for analysis, the applied blocking key will be the key from key generation definition. This
        // will be refined when there is a need to define the applied blocking key manually by user later.
        AnalysisRecordGroupingUtils.createAppliedBlockKeyByGenKey(recordMatchingIndicator);

        blockKeyDefinition = AnalysisRecordGroupingUtils.getBlockKeySchema(recordMatchingIndicator);
    }

    public void run() throws BusinessException {
        while (this.resultIterator.hasNext()) {
            RichRecord record = (RichRecord) this.resultIterator.next();
            if (record == null) {
                continue;
            }
            // get the block key of this record(which block this record belongs)
            String blockKey = getBlockKey(record);
            OneBlockMatching matchInOneBlock = this.blockMatchMap.get(blockKey);
            if (matchInOneBlock == null) {// create a new matching for this new block to do the matching
                matchInOneBlock = createBlockKeyManager(blockKey);
            }
            // do group in the same block
            matchInOneBlock.run(record);
        }

        // end all
        endAll();
    }

    /**
     * end all blocks matching, and set each block size into the block indicator
     * 
     * @throws BusinessException
     */
    private void endAll() throws BusinessException {
        TreeMap<Object, Long> blockSize2Freq = new TreeMap<Object, Long>();
        for (String key : this.blockMatchMap.keySet()) {
            OneBlockMatching oneBlockMatching = blockMatchMap.get(key);
            oneBlockMatching.end();
            long blockSize = oneBlockMatching.getBlockSize();
            Long freq = blockSize2Freq.get(blockSize);
            if (freq == null) {
                freq = 0l;
            }
            blockSize2Freq.put(blockSize, freq + 1);
        }
        blockKeyIndicator.setBlockSize2frequency(blockSize2Freq);
    }

    /**
     * create the processing manager of one block
     * 
     * @param blockKey
     * @return
     * @throws BusinessException
     */
    private OneBlockMatching createBlockKeyManager(String blockKey) throws BusinessException {
        OneBlockMatching matchInOneBlock = new OneBlockMatching();
        this.blockMatchMap.put(blockKey, matchInOneBlock);
        return matchInOneBlock;
    }

    /**
     * get the block key of one record
     * 
     * @param record
     * @return
     */
    private String getBlockKey(RichRecord record) {
        Map<String, String> columnValueMap = new HashMap<String, String>();
        for (String columnName : colName2IndexMap.keySet()) {
            int index = Integer.parseInt(colName2IndexMap.get(columnName));
            columnValueMap.put(columnName, record.getAttributes().get(index).getValue());
        }

        String blockkey = blockKeyGenerator.getGenKey(blockKeyDefinition, columnValueMap);
        // add the block key into the record
        Attribute attribute = new Attribute(MatchAnalysisConstant.BLOCK_KEY,record.getAttributes().size());
        attribute.setValue(blockkey);
        record.getAttributes().add(attribute);

        return blockkey;
    }

    /**
     * the match grouping within one block.
     */
    class OneBlockMatching {

        private AnalysisMatchRecordGrouping matchRecordGrouping;

        private long blockSize = 0;

        public OneBlockMatching() throws BusinessException {
            if (recordMatchingIndicator.getBuiltInMatchRuleDefinition().getRecordLinkageAlgorithm()
                    .equals(RecordMatcherType.T_SwooshAlgorithm.name())) {
                matchRecordGrouping = new AnalysisSwooshMatchRecordGrouping(matchResultConsumer);
            } else {
                matchRecordGrouping = new AnalysisMatchRecordGrouping(matchResultConsumer);
            }
            initGrouping(matchRecordGrouping);
        }

        /**
         * Init the algorithm
         * 
         * @throws BusinessException
         */
        private void initGrouping(AnalysisMatchRecordGrouping recordGrouping) throws BusinessException {
            AnalysisRecordGroupingUtils.setRuleMatcher(columnMap, recordMatchingIndicator, recordGrouping);
            try {
                AnalysisRecordGroupingUtils.initialMatchGrouping(columnMap, recordMatchingIndicator, recordGrouping);
            } catch (InstantiationException e1) {
                throw new BusinessException();
            } catch (IllegalAccessException e1) {
                throw new BusinessException();
            } catch (ClassNotFoundException e1) {
                throw new BusinessException();
            }
        }

        /*
         * Group on one record
         * 
         * @see java.lang.Thread#run()
         */
        public void run(RichRecord currentRecord) throws BusinessException {
            try {
                matchRecordGrouping.doGroup(currentRecord);
                blockSize++;
            } catch (IOException e) {
                throw new BusinessException();
            } catch (InterruptedException e) {
                throw new BusinessException();
            }
        }

        public void end() throws BusinessException {
            try {
                matchRecordGrouping.end();
            } catch (IOException e) {
                throw new BusinessException();
            } catch (InterruptedException e) {
                throw new BusinessException();
            }
        }

        public long getBlockSize() {
            return blockSize;
        }
    }
}
