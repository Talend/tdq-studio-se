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
package org.talend.dataquality.record.linkage.ui.section;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.record.linkage.constant.RecordMatcherType;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.record.linkage.ui.section.definition.DefaultSurvivorshipDefinitionSection;
import org.talend.dataquality.record.linkage.ui.section.definition.MatchAndSurvivorKeySection;
import org.talend.dataquality.record.linkage.ui.section.definition.ParticularDefSurshipDefinitionSection;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.MatchRuleDefinition;

/**
 * created by zshen on Aug 20, 2013 Detailled comment
 * 
 */
public class SelectAlgorithmSection extends AbstractSectionComposite {

    private Button vsrButton = null;

    private Button tSwooshButton = null;

    protected boolean isVSRMode = true;

    private BlockingKeySection blockKeySection = null;

    private MatchingKeySection matchKeySection = null;

    // private SurvivorshipDefinitionSection survivorshipDefinitionSection = null;

    protected MatchAndSurvivorKeySection matchAndSurvivorKeySection = null;

    private DefaultSurvivorshipDefinitionSection defaultSurvivorshipDefinitionSection = null;

    private ParticularDefSurshipDefinitionSection particularDefaultSurvivorshipSection = null;

    private String algorithmName = null;

    private MatchRuleDefinition matchRuleDef = null;

    public SelectAlgorithmSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        this(form, parent, Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED, toolkit);
    }

    /**
     * DOC zshen SelectAlgorithmSection constructor comment.
     * 
     * @param parent
     * @param style
     * @param toolkit
     */
    public SelectAlgorithmSection(ScrolledForm form, Composite parent, int style, FormToolkit toolkit) {
        super(form, parent, style, toolkit);
        section.setText(DefaultMessagesImpl.getString("SelectAlgorithmSection.record.linkage.algorithm")); //$NON-NLS-1$
        section.setDescription(DefaultMessagesImpl.getString("SelectAlgorithmSection.choose.algorithm")); //$NON-NLS-1$
    }

    /**
     * DOC zshen Comment method "createChooseAlgorithmCom".
     * 
     * @param dqRuleDefinitionSection
     */
    public void createChooseAlgorithmCom() {
        Composite mainComp = toolkit.createComposite(section);
        mainComp.setLayout(new GridLayout());

        Composite container = toolkit.createComposite(mainComp, SWT.NONE);
        GridLayout gdLayout = new GridLayout(1, false);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);
        container.setLayout(gdLayout);

        tSwooshButton = toolkit.createButton(container, RecordMatcherType.T_SwooshAlgorithm.getLabel(), SWT.RADIO);
        if (this.algorithmName == null) {
            this.algorithmName = RecordMatcherType.simpleVSRMatcher.name();
        }
        tSwooshButton.setSelection(!isVSRMode());
        tSwooshButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleTSwooshButtonSelection();
            }

        });
        vsrButton = toolkit.createButton(container, RecordMatcherType.simpleVSRMatcher.getLabel(), SWT.RADIO);
        vsrButton.setSelection(isVSRMode());
        vsrButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleVSRButtonSelection();
            }

        });
        section.setClient(mainComp);

    }

    /**
     * DOC zhao Comment method "handleVSRButtonSelection".
     */
    private void handleVSRButtonSelection() {
        isVSRMode = true;
        matchKeySection.setAddColumn(false);
        notifyOtherSections();
        algorithmName = RecordMatcherType.simpleVSRMatcher.name();
        matchRuleDef.setRecordLinkageAlgorithm(algorithmName);
        removeAllSurvivorship();
        // survivorshipDefinitionSection.removeAllSurvivorship();
        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, RecordMatcherType.T_SwooshAlgorithm.name(),
                RecordMatcherType.simpleVSRMatcher.name());
    }

    /**
     * DOC zhao Comment method "handleTSwooshButtonSelection".
     */
    private void handleTSwooshButtonSelection() {
        isVSRMode = false;
        matchKeySection.setAddColumn(true);
        notifyOtherSections();
        algorithmName = RecordMatcherType.T_SwooshAlgorithm.name();
        matchRuleDef.setRecordLinkageAlgorithm(algorithmName);
        listeners.firePropertyChange(MatchAnalysisConstant.ISDIRTY_PROPERTY, RecordMatcherType.simpleVSRMatcher.name(),
                RecordMatcherType.T_SwooshAlgorithm.name());
    }

    public void setSelection(boolean isVSR) {
        if (isVSR) {
            vsrButton.setSelection(true);
            tSwooshButton.setSelection(false);
            handleVSRButtonSelection();
        } else {
            vsrButton.setSelection(false);
            tSwooshButton.setSelection(true);
            handleTSwooshButtonSelection();
        }
    }

    /**
     * DOC zhao Comment method "removeAllSurvivorship".
     * 
     * @param redrawn whether needed redrawn the section after remove All of survivorship
     */
    protected void removeAllSurvivorship() {
        matchAndSurvivorKeySection.removeAllSurvivorship();
    }

    /**
     * DOC zshen Comment method "noticeOtherSection".
     */
    private void notifyOtherSections() {
        matchKeySection.changeSectionDisStatus(isVSRMode);
        // MOD 20180731, TDQ-15002 yyin
        if (!CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected()) {
            blockKeySection.getSection().setExpanded(false);
        }
        defaultSurvivorshipDefinitionSection.changeSectionDisStatus(!isVSRMode);
        particularDefaultSurvivorshipSection.changeSectionDisStatus(!isVSRMode);
        changeDisplayStatus();
        if (!isVSRMode) {
            updateMatchAndSurvivorSection();
        } else {
            matchKeySection.redrawnContent();
        }
        // TDQ-9757
        if (matchKeySection.hasMatchKey(true)) {
            listeners.firePropertyChange(MatchAnalysisConstant.HIDE_GROUPS, 1, PluginConstant.HIDDEN_GROUP_LESS_THAN_DEFAULT);
        }
    }

    /**
     * DOC zhao Comment method "notifyMatchSurvSection".
     */
    protected void changeDisplayStatus() {
        matchAndSurvivorKeySection.changeSectionDisStatus(!isVSRMode);
    }

    /**
     * DOC zhao Update the match and survivorship section.
     */
    protected void updateMatchAndSurvivorSection() {
        // In case of match rule definition editor.
        matchAndSurvivorKeySection.initTableInput(Boolean.TRUE);
    }

    public boolean isVSRMode() {
        return RecordMatcherType.simpleVSRMatcher.name().equals(algorithmName);
    }

    protected boolean isShowBlockingKeySection(boolean isVSRAlgo) {
        if (isVSRAlgo) {
            return true;
        }
        return false;
    }

    /**
     * Sets the blockKeySection.
     * 
     * @param blockKeySection the blockKeySection to set
     */
    public void setBlockkeySection(BlockingKeySection blockKeySection) {
        this.blockKeySection = blockKeySection;
    }

    /**
     * Sets the matchKeySection.
     * 
     * @param matchKeySection the matchKeySection to set
     */
    public void setMatchKeySection(MatchingKeySection matchKeySection) {
        this.matchKeySection = matchKeySection;
    }

    public void setMatchAndSurvivorKeySection(MatchAndSurvivorKeySection matchAndSurvivorKeySection) {
        this.matchAndSurvivorKeySection = matchAndSurvivorKeySection;
    }

    /**
     * Getter for algorithmName.
     * 
     * @return the algorithmName
     */
    public String getAlgorithmName() {
        return this.algorithmName;
    }

    /**
     * Sets the matchRuleDef.
     * 
     * @param matchRuleDef the matchRuleDef to set
     */
    public void setMatchRuleDef(MatchRuleDefinition matchRuleDef) {
        this.matchRuleDef = matchRuleDef;
        this.algorithmName = this.matchRuleDef.getRecordLinkageAlgorithm();
    }

    // /**
    // * Sets the survivorshipDefinitionSection.
    // *
    // * @param survivorshipDefinitionSection the survivorshipDefinitionSection to set
    // */
    // public void setSurvivorshipDefinitionSection(SurvivorshipDefinitionSection survivorshipDefinitionSection) {
    // this.survivorshipDefinitionSection = survivorshipDefinitionSection;
    // }

    /**
     * Sets the defaultSurvivorshipDefinitionSection.
     * 
     * @param defaultSurvivorshipDefinitionSection the defaultSurvivorshipDefinitionSection to set
     */
    public void setDefaultSurvivorshipDefinitionSection(DefaultSurvivorshipDefinitionSection defaultSurvivorshipDefinitionSection) {
        this.defaultSurvivorshipDefinitionSection = defaultSurvivorshipDefinitionSection;
    }

    /**
     * Sets the particularDefaultSurvivorshipSection.
     * 
     * @param particularDefaultSurvivorshipSection the particularDefaultSurvivorshipSection to set
     */
    public void setParticularDefaultSurvivorshipSection(ParticularDefSurshipDefinitionSection particularDefaultSurvivorshipSection) {
        this.particularDefaultSurvivorshipSection = particularDefaultSurvivorshipSection;
    }

}
