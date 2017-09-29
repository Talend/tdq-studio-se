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

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataquality.record.linkage.ui.composite.BlockingKeyTableComposite;
import org.talend.dataquality.record.linkage.ui.composite.definition.BlockingKeyTableDefinitionComposite;
import org.talend.dataquality.record.linkage.ui.section.BlockingKeySection;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.MatchRuleDefinition;

/**
 * created by zshen on Aug 20, 2013 Detailled comment
 * 
 */
public class BlockingKeyDefinitionSection extends BlockingKeySection {

    private MatchRuleDefinition matchRuleDef = null;

    /**
     * DOC zshen BlockingKeyDefinitionSection constructor comment.
     * 
     * @param form
     * @param parent
     * @param style
     * @param toolkit
     * @param analysis
     */
    public BlockingKeyDefinitionSection(ScrolledForm form, Composite parent, FormToolkit toolkit) {
        super(form, parent, Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED, toolkit, null);
        super.setIsNeedSubChart(false);
    }

    /**
     * Sets the matchRuleDef.
     * 
     * @param matchRuleDef the matchRuleDef to set
     */
    public void setMatchRuleDef(MatchRuleDefinition matchRuleDef) {
        this.matchRuleDef = matchRuleDef;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.BlockingKeySection#getBlockKeyDefinitionList()
     */
    @Override
    protected List<BlockKeyDefinition> getBlockKeyDefinitionList() {
        return this.matchRuleDef.getBlockKeys();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.BlockingKeySection#getSectionName()
     */
    @Override
    protected String getSectionName() {
        return MatchAnalysisConstant.BlOCKING_KEY_DEFINITION_SECTION_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.BlockingKeySection#createSubChart(org.eclipse.swt.widgets.Composite
     * )
     */
    @Override
    protected void createSubChart(Composite sectionClient) {
        // don't need do anything
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.BlockingKeySection#removeBlockingKey(org.talend.dataquality.
     * rules.BlockKeyDefinition)
     */
    @Override
    public void removeBlockingKey(BlockKeyDefinition blockKeyDef) {
        tableComposite.removeKeyDefinition(blockKeyDef, matchRuleDef.getBlockKeys());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.section.BlockingKeySection#getMatchRuleDefinition()
     */
    @Override
    protected MatchRuleDefinition getMatchRuleDefinition() {
        return matchRuleDef;
    }

    /**
     * Getter for blockKeyList.
     * 
     * @return the blockKeyList
     */
    public List<BlockKeyDefinition> getBlockKeyList() {
        List<BlockKeyDefinition> blockKeys = new ArrayList<BlockKeyDefinition>();
        blockKeys.addAll(EcoreUtil.copyAll(this.matchRuleDef.getBlockKeys()));
        return blockKeys;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.BlockingKeySection#createTableComposite(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    protected BlockingKeyTableComposite createTableComposite(Composite parent) {
        return new BlockingKeyTableDefinitionComposite(parent, SWT.NO_FOCUS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.section.BlockingKeySection#checkColumnNameIsEmpty(org.talend.dataquality
     * .rules.BlockKeyDefinition)
     */
    @Override
    protected boolean checkColumnNameIsEmpty(BlockKeyDefinition bdk) {
        return false;
    }

}
