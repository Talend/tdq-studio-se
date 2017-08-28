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
package org.talend.dq.analysis.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.grouping.AnalysisMatchRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.IRecordGrouping;
import org.talend.dataquality.record.linkage.grouping.adapter.MatchParameterAdapter;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorShipAlgorithmParams;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorShipAlgorithmParams.SurvivorshipFunction;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorshipUtils;
import org.talend.dataquality.record.linkage.record.CombinedRecordMatcher;
import org.talend.dataquality.record.linkage.record.IRecordMatcher;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * Create a adapter for Match Parameter of Analysis
 */
public class AnalysisMatchParameterAdapter extends MatchParameterAdapter {

    private AnalysisMatchRecordGrouping analysisMatchRecordGrouping;

    private RecordMatchingIndicator recordMatchingIndicator;

    private Map<MetadataColumn, String> columnMap;

    public AnalysisMatchParameterAdapter(AnalysisMatchRecordGrouping analysisMatchRecordGrouping,
            RecordMatchingIndicator recordMatchingIndicator, Map<MetadataColumn, String> columnMap) {
        this.analysisMatchRecordGrouping = analysisMatchRecordGrouping;
        this.recordMatchingIndicator = recordMatchingIndicator;
        this.columnMap = columnMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.grouping.adapter.MatchParameterAdapter#getAllSurvivorshipFunctions()
     */
    @Override
    public List<SurvivorshipFunction> getAllSurvivorshipFunctions() {
        List<SurvivorshipFunction> survFunctions = new ArrayList<SurvivorshipFunction>();
        // Survivorship functions.
        List<SurvivorshipKeyDefinition> survivorshipKeyDefs = recordMatchingIndicator.getBuiltInMatchRuleDefinition()
                .getSurvivorshipKeys();
        for (SurvivorshipKeyDefinition survDef : survivorshipKeyDefs) {
            SurvivorshipFunction func = new SurvivorShipAlgorithmParams().new SurvivorshipFunction();
            func.setSurvivorShipKey(survDef.getName());
            func.setParameter(survDef.getFunction().getAlgorithmParameters());
            func.setSurvivorShipAlgoEnum(SurvivorShipAlgorithmEnum.getTypeBySavedValue(survDef.getFunction().getAlgorithmType()));
            survFunctions.add(func);
        }
        return survFunctions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.grouping.adapter.MatchParameterAdapter#getDefaultSurviorShipRules()
     */
    @Override
    public Map<Integer, SurvivorshipFunction> getDefaultSurviorShipRules() {
        Map<Integer, SurvivorshipFunction> defaultSurvRules = new HashMap<Integer, SurvivorshipFunction>();
        // Set default survivorship functions.
        List<DefaultSurvivorshipDefinition> defSurvDefs = recordMatchingIndicator.getBuiltInMatchRuleDefinition()
                .getDefaultSurvivorshipDefinitions();
        // consisder ParticularDefaultSurvivorshipDefinitions too
        EList<ParticularDefaultSurvivorshipDefinitions> particularDefaultSurvivorshipDefinitions = recordMatchingIndicator
                .getBuiltInMatchRuleDefinition().getParticularDefaultSurvivorshipDefinitions();
        for (MetadataColumn metaColumn : columnMap.keySet()) {
            String dataTypeName = metaColumn.getTalendType();
            for (ParticularDefaultSurvivorshipDefinitions pdefaultSurvivdef : particularDefaultSurvivorshipDefinitions) {
                if (pdefaultSurvivdef.getColumn().equals(metaColumn.getName())) {
                    putNewSurvFunc(columnMap, defaultSurvRules, metaColumn, pdefaultSurvivdef);
                    break;
                }
            }
            // default survivorship has been handle by Particular
            if (defaultSurvRules.get(Integer.valueOf(columnMap.get(metaColumn))) != null) {
                continue;
            }

            for (DefaultSurvivorshipDefinition defSurvDef : defSurvDefs) {
                // the column's data type start with id_, so need to add id_ ahead of the default survivorship's data
                // type before judging if they are equal
                if (StringUtils.equals(dataTypeName, "id_" + defSurvDef.getDataType()) || StringUtils.equals(defSurvDef.getDataType(), "Number") && JavaTypesManager.isNumber(dataTypeName)) { //$NON-NLS-1$
                    putNewSurvFunc(columnMap, defaultSurvRules, metaColumn, defSurvDef);
                    break;
                }
            }// End for: if no func defined, then the value will be taken from one of the records in a group (1st
             // one ).
        }
        return defaultSurvRules;
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
            Map<Integer, SurvivorshipFunction> defaultSurvRules, MetadataColumn metaColumn,
            DefaultSurvivorshipDefinition defSurvDef) {
        SurvivorshipFunction survFunc = new SurvivorShipAlgorithmParams().new SurvivorshipFunction();
        survFunc.setParameter(defSurvDef.getFunction().getAlgorithmParameters());
        survFunc.setSurvivorShipAlgoEnum(SurvivorShipAlgorithmEnum.getTypeBySavedValue(defSurvDef.getFunction()
                .getAlgorithmType()));
        defaultSurvRules.put(Integer.valueOf(columnMap.get(metaColumn)), survFunc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.grouping.adapter.MatchParameterAdapter#getSurvivorshipAlgosMap(java.util.Map)
     */
    @Override
    public Map<IRecordMatcher, SurvivorshipFunction[]> getSurvivorshipAlgosMap(
            Map<Integer, SurvivorshipFunction> colIdx2DefaultSurvFunc, List<SurvivorshipFunction> survFunctions) {
        Map<IRecordMatcher, SurvivorshipFunction[]> survAlgos = new HashMap<IRecordMatcher, SurvivorshipFunction[]>();
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
                        surFuncsInMatcher[idx] = new SurvivorShipAlgorithmParams().new SurvivorshipFunction();
                        surFuncsInMatcher[idx].setSurvivorShipAlgoEnum(SurvivorShipAlgorithmEnum.MOST_COMMON);
                        // MOD TDQ-11774 set a default parameter
                        surFuncsInMatcher[idx].setParameter(SurvivorshipUtils.DEFAULT_CONCATENATE_PARAMETER);
                    }
                } else {
                    // Find the func from existing survivorship rule list.
                    for (SurvivorshipFunction survFunc : survFunctions) {
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
            survAlgos.put(this.getCombinedRecordMatcher().getMatchers().get(matchRuleIdx), surFuncsInMatcher);

        }
        return survAlgos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.grouping.adapter.MatchParameterAdapter#getCombinedRecordMatcher()
     */
    @Override
    public CombinedRecordMatcher getCombinedRecordMatcher() {
        return this.analysisMatchRecordGrouping.getCombinedRecordMatcher();
    }

}
