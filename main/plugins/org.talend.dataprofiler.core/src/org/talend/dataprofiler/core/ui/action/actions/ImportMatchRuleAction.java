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
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.ui.dialog.MatchRuleElementTreeSelectionDialog;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dq.nodes.RuleRepNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class ImportMatchRuleAction extends Action {

    protected static Logger log = Logger.getLogger(ImportMatchRuleAction.class);

    private AbstractAnalysisMetadataPage masterPage;

    public ImportMatchRuleAction(AbstractAnalysisMetadataPage masterPage) {
        this.masterPage = masterPage;
        ImageDescriptor imageDescriptor = ImageLib.getImageDescriptor(ImageLib.IMPORT_MATCH_RULE_ICON);

        this.setImageDescriptor(imageDescriptor);
    }

    @Override
    public void run() {
        MatchRuleElementTreeSelectionDialog dialog = new MatchRuleElementTreeSelectionDialog(null,
                new DQRepositoryViewLabelProvider(), new ResourceViewContentProvider(),
                MatchRuleElementTreeSelectionDialog.MATCH_ANALYSIS_TYPE);

        List<String> inputColumnNames = new ArrayList<String>();
        Map<String, String> columnName2Type = new HashMap<String, String>();
        Analysis analysis = masterPage.getCurrentModelElement();
        EList<ModelElement> elements = analysis.getContext().getAnalysedElements();
        for (ModelElement me : elements) {
            inputColumnNames.add(me.getName());
            if (me instanceof MetadataColumn) {
                columnName2Type.put(me.getName(), ((MetadataColumn) me).getTalendType());

            }
        }
        dialog.setInputColumnNames(inputColumnNames);
        dialog.setColumnName2Type(columnName2Type);

        AnalysisResult anaResults = analysis.getResults();
        if (anaResults != null) {
            for (Indicator ind : anaResults.getIndicators()) {
                if (ind != null && ind instanceof RecordMatchingIndicator) {
                    RecordMatchingIndicator rmInd = (RecordMatchingIndicator) ind;
                    MatchRuleDefinition builtInMatchRuleDefinition = rmInd.getBuiltInMatchRuleDefinition();
                    if (builtInMatchRuleDefinition != null) {
                        if (builtInMatchRuleDefinition.getBlockKeys() != null
                                && builtInMatchRuleDefinition.getBlockKeys().size() > 0) {
                            List<String> blockKeyName = new ArrayList<String>();
                            for (BlockKeyDefinition blockKey : builtInMatchRuleDefinition.getBlockKeys()) {
                                blockKeyName.add(blockKey.getName());
                            }
                            dialog.setCurrentAnaBlockKeys(blockKeyName);
                        }
                        List<String> matchKeysName = new ArrayList<String>();
                        for (MatchRule matchRule : builtInMatchRuleDefinition.getMatchRules()) {
                            EList<MatchKeyDefinition> matchKeys = matchRule.getMatchKeys();
                            for (MatchKeyDefinition mkd : matchKeys) {
                                // only need to add different names of the match keys, for the import to compare if any
                                // same
                                if (!matchKeysName.contains(mkd.getName())) {
                                    matchKeysName.add(mkd.getName());
                                }
                            }
                        }
                        dialog.setAnalysisCurrentMatchKeys(matchKeysName);
                    }
                }
            }
        }
        dialog.create();

        // dialog.setExpandedElements(getAllMatchRules());

        if (dialog.open() == Window.OK) {
            Object[] results = dialog.getResult();
            for (Object obj : results) {
                if (obj instanceof RuleRepNode) {
                    RuleRepNode node = (RuleRepNode) obj;
                    MatchRuleDefinition matchRule = (MatchRuleDefinition) node.getRule();
                    if (matchRule != null) {
                        updateMatchRule(matchRule, dialog.isOverwrite());
                    }
                }
            }

        }
    }

    /**
     * if is overwrite: clear all block/match keys. add keys from matchRule to the master page
     * 
     * @param matchRule
     * @param overwrite
     */
    private void updateMatchRule(MatchRuleDefinition matchRule, boolean overwrite) {
        ((MatchAnalysisDetailsPage) masterPage).importMatchRule(matchRule, overwrite);
    }

}
