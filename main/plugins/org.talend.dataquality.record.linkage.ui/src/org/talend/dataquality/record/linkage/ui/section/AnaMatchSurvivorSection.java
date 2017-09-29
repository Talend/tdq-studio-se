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
package org.talend.dataquality.record.linkage.ui.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.grouping.swoosh.SurvivorshipUtils;
import org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.MatchKeyAndSurvivorTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorDefinition;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.sorter.KeyDefinitionTableViewerSorter;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * 
 * Match survivorship key table section specific to match analysis editor.
 * 
 */
public class AnaMatchSurvivorSection extends MatchingKeySection {

    protected MatchRuleDefinition matchRuleDef = null;

    protected Map<MatchRule, List<MatchKeyAndSurvivorDefinition>> matchRuleWithSurvMap = new HashMap<MatchRule, List<MatchKeyAndSurvivorDefinition>>();

    /**
     * DOC zhao AnaMatchSurvivorSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public AnaMatchSurvivorSection(ScrolledForm form, Composite parent, int style, FormToolkit toolkit, Analysis analysis) {
        super(form, parent, style, toolkit, analysis);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#getSectionName()
     */
    @Override
    protected String getSectionName() {
        return MatchAnalysisConstant.MATCHING_KEY_AND_SURVIVOR_DEFINITION_SECTION_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#createTableComposite(org.eclipse.swt.widgets
     * .Composite, org.talend.dataquality.rules.MatchRule)
     */
    @Override
    protected AbsMatchAnalysisTableComposite<?> createTableComposite(Composite ruleComp, MatchRule matchRule) {
        MatchKeyAndSurvivorTableComposite tableComp = new MatchKeyAndSurvivorTableComposite(ruleComp, SWT.NO_FOCUS, matchRule);
        // In case of the section in analysis editor, need to show the input column in matching key table.
        tableComp.setShowInputColumn(true);
        return tableComp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#setInput(org.talend.dataquality.rules.MatchRule
     * , org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite)
     */
    @Override
    protected void setInput(MatchRule matchRule, AbsMatchAnalysisTableComposite<?> matchRuleComposite) {
        List<MatchKeyAndSurvivorDefinition> generatedSurvivorKeyList = generateSurvivorKeyByMatchKey(matchRule, Boolean.FALSE);
        ((MatchKeyAndSurvivorTableComposite) matchRuleComposite).setInput(generatedSurvivorKeyList);
    }

    protected List<MatchKeyAndSurvivorDefinition> generateSurvivorKeyByMatchKey(MatchRule matchRule, boolean isMustCreateSurvivor) {

        List<MatchKeyAndSurvivorDefinition> matchAndSurvivorKeyList = matchRuleWithSurvMap.get(matchRule);
        if (matchAndSurvivorKeyList == null) {
            matchAndSurvivorKeyList = new ArrayList<MatchKeyAndSurvivorDefinition>();
            matchRuleWithSurvMap.put(matchRule, matchAndSurvivorKeyList);
        }
        EList<MatchKeyDefinition> matchKeys = matchRule.getMatchKeys();
        int index = 0;
        for (MatchKeyDefinition matchKey : matchKeys) {
            // first, find the current matchKey in MatchAndSurvivorKeyList
            if (matchAndSurvivorKeyList.size() > index) {
                MatchKeyAndSurvivorDefinition definition = matchAndSurvivorKeyList.get(index);
                // check if the position of the match key moved or not
                if (StringUtils.equals(matchKey.getName(), definition.getMatchKey().getName())) {
                    // update the current match key
                    definition.setMatchKey(matchKey);
                    updateSurvivorKey(isMustCreateSurvivor, matchKey.getName(), definition);
                } else {
                    // the position of the current match key moved, need to find its related mAndS key in list,
                    MatchKeyAndSurvivorDefinition oldDefinition = findPositionOfCurrentMatchkey(matchKey, matchAndSurvivorKeyList);
                    // if can't find, means that it is a new one
                    if (oldDefinition == null) {
                        createMatchAndSurvivorKey(matchKey, isMustCreateSurvivor, matchAndSurvivorKeyList);
                    } else {
                        // delete the old definition in current list
                        matchAndSurvivorKeyList.remove(oldDefinition);
                        // set new match key to it
                        oldDefinition.setMatchKey(matchKey);
                        updateSurvivorKey(isMustCreateSurvivor, matchKey.getName(), oldDefinition);
                        // insert it in the new position
                        matchAndSurvivorKeyList.add(index, oldDefinition);
                    }
                }

            } else {
                // need to create a MatchAndSurvivorKey
                createMatchAndSurvivorKey(matchKey, isMustCreateSurvivor, matchAndSurvivorKeyList);
            }
            index++;
        }
        return matchAndSurvivorKeyList;
    }

    @Override
    public void createMatchKeyFromCurrentMatchRule(String column) {
        MatchKeyAndSurvivorTableComposite matchRuleTableComp = (MatchKeyAndSurvivorTableComposite) getCurrentMatchRuleTableComposite();
        List<MatchKeyAndSurvivorDefinition> matchAndSurvKeyList = matchRuleWithSurvMap.get(matchRuleTableComp.getMatchRule());
        matchRuleTableComp.addKeyDefinition(column, matchAndSurvKeyList);
    }

    private void updateSurvivorKey(boolean isCreateNewOne, String name, MatchKeyAndSurvivorDefinition definition) {
        EList<SurvivorshipKeyDefinition> survivorshipKeys = matchRuleDef.getSurvivorshipKeys();

        // set the survivor key: if clear, use a new one , if not clear, use the one in model
        boolean isSurvKeyFound = false;
        if (!isCreateNewOne) {
            // Use the key name to find the appropriate survivor key definition.
            for (SurvivorshipKeyDefinition survKey : survivorshipKeys) {
                if (survKey.getName().equals(name)) {
                    definition.setSurvivorShipKey(survKey);
                    isSurvKeyFound = true;
                    break;
                }
            }

        }

        if (isCreateNewOne || !isSurvKeyFound) {
            SurvivorshipKeyDefinition survivorshipKeyDefinition = createNewSurvivorshipKeyDefinition(name);
            definition.setSurvivorShipKey(survivorshipKeyDefinition);
            survivorshipKeys.add(survivorshipKeyDefinition);
        }

    }

    private SurvivorshipKeyDefinition createNewSurvivorshipKeyDefinition(String matchKeyName) {
        SurvivorshipKeyDefinition skd = RulesFactory.eINSTANCE.createSurvivorshipKeyDefinition();
        skd.setName(matchKeyName);
        AlgorithmDefinition createAlgorithmDefinition = RulesFactory.eINSTANCE.createAlgorithmDefinition();
        createAlgorithmDefinition.setAlgorithmType(SurvivorShipAlgorithmEnum.getTypeByIndex(0).getValue());
        // MOD TDQ-11774 set a default value for parameter
        createAlgorithmDefinition.setAlgorithmParameters(SurvivorshipUtils.DEFAULT_CONCATENATE_PARAMETER);
        skd.setFunction(createAlgorithmDefinition);
        skd.setAllowManualResolution(true);
        return skd;
    }

    /**
     * DOC yyin Comment method "findPositionOfCurrentMatchkey".
     * 
     * @param matchKey
     * @return
     */
    private MatchKeyAndSurvivorDefinition findPositionOfCurrentMatchkey(MatchKeyDefinition matchKey,
            List<MatchKeyAndSurvivorDefinition> matchAndSurvivorKeyList) {
        // find the definition by match key's name
        for (MatchKeyAndSurvivorDefinition definition : matchAndSurvivorKeyList) {
            if (StringUtils.equals(matchKey.getName(), definition.getMatchKey().getName())) {
                return definition;
            }
        }
        return null;
    }

    /**
     * DOC yyin Comment method "createMatchAndSurvivorKey".
     * 
     * @param matchKey
     * @param isClearSurvivor
     * @param isClearSurvivor
     * @param index
     */
    private void createMatchAndSurvivorKey(MatchKeyDefinition matchKey, boolean isClearSurvivor,
            List<MatchKeyAndSurvivorDefinition> matchAndSurvivorKeyList) {
        MatchKeyAndSurvivorDefinition mAnds = new MatchKeyAndSurvivorDefinition();
        mAnds.setMatchKey(matchKey);
        // create a new survivor key
        updateSurvivorKey(isClearSurvivor, matchKey.getName(), mAnds);
        matchAndSurvivorKeyList.add(mAnds);
    }

    public void removeAllSurvivorship() {
        matchRuleDef.getSurvivorshipKeys().clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#removeTableItem()
     */
    @Override
    public void removeTableItem() {
        boolean success = false;
        ISelection selectItems = getCurrentMatchRuleTableComposite().getSelectItems();
        if (selectItems instanceof StructuredSelection) {
            Iterator<MatchKeyAndSurvivorDefinition> iterator = ((StructuredSelection) selectItems).iterator();
            while (iterator.hasNext()) {
                MatchKeyAndSurvivorDefinition next = iterator.next();
                removeMatchKeyFromCurrentMatchRule(next);
                success = true;
            }
            if (success) {
                listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, true, false);
                listeners.firePropertyChange(MatchAnalysisConstant.MATCH_RULE_TAB_SWITCH, true, false);
            }
        }
    }

    public void removeMatchKeyFromCurrentMatchRule(MatchKeyAndSurvivorDefinition mAndSDefition) {
        MatchKeyAndSurvivorTableComposite matchRuleTableComp = (MatchKeyAndSurvivorTableComposite) getCurrentMatchRuleTableComposite();
        matchRuleTableComp.removeKeyDefinition(mAndSDefition, matchRuleWithSurvMap.get(matchRuleTableComp.getMatchRule()));
    }

    public void setMatchRuleDef(MatchRuleDefinition matchRuleDef) {
        this.matchRuleDef = matchRuleDef;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#deleteMatchRuleTab(org.eclipse.swt.custom
     * .CTabItem)
     */
    @Override
    protected void deleteMatchRuleTab(CTabItem tabItem) {
        MatchRule matchRule = getMatchRule(tabItem);
        List<MatchKeyAndSurvivorDefinition> matchAndSurvDefList = matchRuleWithSurvMap.get(matchRule);
        MatchRuleDefinition matchRuleDefinition = getMatchRuleDefinition();
        for (MatchKeyAndSurvivorDefinition matchAndSurvDef : matchAndSurvDefList) {
            matchRuleDefinition.getSurvivorshipKeys().remove(matchAndSurvDef.getSurvivorShipKey());
        }
        matchRuleWithSurvMap.remove(matchRule);
        super.deleteMatchRuleTab(tabItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#remoteKeyDefinition(java.lang.String,
     * org.eclipse.swt.custom.CTabItem)
     */
    @Override
    protected void remoteKeyDefinition(String column, CTabItem oneTab) {
        MatchKeyAndSurvivorTableComposite matchRuleTableComp = (MatchKeyAndSurvivorTableComposite) getMatchRuleComposite(oneTab);
        List<MatchKeyAndSurvivorDefinition> matchAndSurvDefList = matchRuleWithSurvMap.get(getMatchRule(oneTab));
        matchRuleTableComp.removeKeyDefinition(column, matchAndSurvDefList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#checkAndRemoveEmptyMatchRule(org.eclipse.
     * swt.custom.CTabItem)
     */
    @Override
    protected void checkAndRemoveEmptyMatchRule(CTabItem theTab) {
        MatchRule matchRule = getMatchRule(theTab);
        List<MatchKeyAndSurvivorDefinition> matchAndSurvDefList = matchRuleWithSurvMap.get(matchRule);
        if (matchAndSurvDefList.size() <= 0) {
            MatchRuleDefinition matchRuleDefinition = getMatchRuleDefinition();
            matchRuleDefinition.getMatchRules().remove(matchRule);
            for (MatchKeyAndSurvivorDefinition matchAndSurvDef : matchAndSurvDefList) {
                matchRuleDefinition.getSurvivorshipKeys().remove(matchAndSurvDef.getSurvivorShipKey());
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#getCurrentTabDefinitions()
     */
    @Override
    protected List<?> getCurrentTabDefinitions() {
        MatchKeyAndSurvivorTableComposite matchRuleTableComp = (MatchKeyAndSurvivorTableComposite) getCurrentMatchRuleTableComposite();
        MatchRule matchRule = matchRuleTableComp.getMatchRule();
        List<MatchKeyAndSurvivorDefinition> matchAndSurvDefList = matchRuleWithSurvMap.get(matchRule);
        return matchAndSurvDefList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#getMatchRule(org.eclipse.swt.custom.CTabItem)
     */
    @Override
    protected MatchRule getMatchRule(CTabItem tabItem) {
        MatchKeyAndSurvivorTableComposite matchRuleTableComp = (MatchKeyAndSurvivorTableComposite) getMatchRuleComposite(tabItem);
        MatchRule matchRule = matchRuleTableComp.getMatchRule();
        return matchRule;
    }

    @Override
    protected MatchRule getCurrentMatchRule() throws Exception {
        CTabItem currentTabItem = ruleFolder.getSelection();
        if (currentTabItem == null) {
            throw new Exception(DefaultMessagesImpl.getString("MatchingKeySection.ONE_MATCH_RULE_REQUIRED")); //$NON-NLS-1$
        }
        return getMatchRule(currentTabItem);
    }

    @Override
    public void removeMatchKeyFromCurrentMatchRule(String column) {
        MatchKeyAndSurvivorTableComposite matchRuleTableComp = (MatchKeyAndSurvivorTableComposite) getCurrentMatchRuleTableComposite();
        MatchRule matchRule = matchRuleTableComp.getMatchRule();
        List<MatchKeyAndSurvivorDefinition> matchAndSurvDefList = matchRuleWithSurvMap.get(matchRule);
        matchRuleTableComp.removeKeyDefinition(column, matchAndSurvDefList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#getTableViewerSorter(org.talend.dataquality
     * .rules.MatchRule)
     */
    @Override
    protected KeyDefinitionTableViewerSorter<?> getTableViewerSorter(MatchRule matchRule) {
        List<MatchKeyAndSurvivorDefinition> matchAndSurvDefList = matchRuleWithSurvMap.get(matchRule);
        return new KeyDefinitionTableViewerSorter<MatchKeyAndSurvivorDefinition>(matchAndSurvDefList);
    }

}
