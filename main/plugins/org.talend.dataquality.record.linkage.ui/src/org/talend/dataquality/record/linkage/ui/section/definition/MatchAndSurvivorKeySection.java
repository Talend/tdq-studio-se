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
package org.talend.dataquality.record.linkage.ui.section.definition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.record.linkage.ui.composite.AbsMatchAnalysisTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.MatchKeyAndSurvivorTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorDefinition;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.ui.section.AnaMatchSurvivorSection;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;

/**
 * The section used in match rule editor, show Match and Survivor keys for t-swoosh algorithm
 */
public class MatchAndSurvivorKeySection extends AnaMatchSurvivorSection {

    private MatchKeyAndSurvivorTableComposite tableComposite = null;

    List<MatchKeyAndSurvivorDefinition> matchAndSurvivorKeyList = new ArrayList<MatchKeyAndSurvivorDefinition>();

    private Text groupQualityThresholdText = null;

    /**
     * DOC yyin MatchAndSurvivorKeySection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public MatchAndSurvivorKeySection(ScrolledForm form, Composite parent, int style, FormToolkit toolkit, Analysis analysis) {
        super(form, parent, style, toolkit, analysis);
        super.setIsNeedSubChart(false);
    }

    /**
     * DOC zshen BlockingKeyDefinitionSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public MatchAndSurvivorKeySection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED, toolkit, null);
        super.setIsNeedSubChart(false);
    }

    @Override
    protected MatchRuleDefinition getMatchRuleDefinition() {
        return this.matchRuleDef;
    }

    /**
     * DOC yyin Comment method "createTableComposite".
     * 
     * @param ruleComp
     * @param data
     * @param gridLayout
     */
    @Override
    protected AbsMatchAnalysisTableComposite<?> createTableComposite(Composite ruleComp, MatchRule matchRule) {
        tableComposite = new MatchKeyAndSurvivorTableComposite(ruleComp, SWT.NO_FOCUS, matchRule);
        return tableComposite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.MatchingKeySection#getMatchRuleComposite(org.eclipse.swt.custom
     * .CTabItem)
     */
    @Override
    public AbsMatchAnalysisTableComposite<?> getMatchRuleComposite(CTabItem currentTabItem) {
        return (MatchKeyAndSurvivorTableComposite) currentTabItem.getData(MatchAnalysisConstant.MATCH_RULE_TABLE_COMPOSITE);
    }

    /**
     * DOC zhao Comment method "createGroupQualityThreshold".
     * 
     * @param parent
     */
    @Override
    protected void createGroupQualityThreshold(Composite parent) {

        Composite groupQualityThresholdComposite = new Composite(parent, SWT.NONE);
        groupQualityThresholdComposite.setLayout(new GridLayout(2, Boolean.TRUE));
        Label groupQualityTresholdLabel = new Label(groupQualityThresholdComposite, SWT.NONE);
        groupQualityTresholdLabel.setText(DefaultMessagesImpl.getString("MatchRuleTableComposite.GROUP_QUALITY_THRESHOLD")); //$NON-NLS-1$
        groupQualityThresholdText = new Text(groupQualityThresholdComposite, SWT.BORDER);
        GridData layoutData = new GridData();
        layoutData.widthHint = 80;
        groupQualityThresholdText.setLayoutData(layoutData);
        groupQualityThresholdText.setText(String.valueOf(this.matchRuleDef.getMatchGroupQualityThreshold()));
        groupQualityThresholdText.addModifyListener(new ModifyListener() {

            Double oldValue = matchRuleDef.getMatchGroupQualityThreshold();

            @Override
            public void modifyText(ModifyEvent e) {
                try {
                    String newValue = groupQualityThresholdText.getText();
                    Double value = Double.valueOf(newValue);
                    if (value != oldValue) {
                        matchRuleDef.setMatchGroupQualityThreshold(value);
                        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, oldValue, value);
                        oldValue = value;
                    }
                } catch (Exception exc) {
                    // Invalid input
                }
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#getSectionName()
     */
    @Override
    protected String getSectionName() {
        return MatchAnalysisConstant.MATCHING_KEY_AND_SURVIVOR_DEFINITION_SECTION_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.AbstractMatchAnaysisTableSection#isKeyDefinitionAdded(java.lang
     * .String)
     */
    @Override
    public Boolean isKeyDefinitionAdded(String columnName) throws Exception {
        return Boolean.FALSE;

    }

    // when switch to t_swoosh, call this method, to recompute the input
    public void initTableInput(boolean isClearSurvivor) {
        List<MatchKeyAndSurvivorDefinition> generatedSurvivorKeyList = new ArrayList<MatchKeyAndSurvivorDefinition>();
        EList<MatchRule> matchRules = matchRuleDef.getMatchRules();
        if (!matchRules.isEmpty()) {
            generatedSurvivorKeyList = generateSurvivorKeyByMatchKey(matchRules.get(0), isClearSurvivor);
            matchAndSurvivorKeyList = matchRuleWithSurvMap.get(matchRules.get(0));
        }
        tableComposite.setInput(generatedSurvivorKeyList);
        if (!matchRules.isEmpty()) {
            tableComposite.setMatchIntervalText(String.valueOf(matchRules.get(0).getMatchInterval()));
        }
        groupQualityThresholdText.setText(String.valueOf(this.matchRuleDef.getMatchGroupQualityThreshold()));
    }

    /**
     * For the rule editor, no column is needed.
     */
    @Override
    protected boolean checkColumnNameIsEmpty(MatchKeyDefinition mdk) {
        return false;
    }

    /**
     * When the user has selected t-swoosh and clicks on the "plus" button, there should be a warning pop-up Added
     * TDQ-9318
     */
    @Override
    protected void addNewMatchRule() {
        super.addNewMatchRule();
    }
}
