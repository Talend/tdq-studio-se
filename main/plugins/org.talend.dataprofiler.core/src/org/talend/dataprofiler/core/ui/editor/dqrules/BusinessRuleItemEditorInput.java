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
package org.talend.dataprofiler.core.ui.editor.dqrules;

import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * TDQ-8212 msjian note: for DQ team, use this rule editor input for all types of rule: parse rule, match rule, business
 * rule, NOT use MatchRuleItemEditorInput class.
 */
public class BusinessRuleItemEditorInput extends AbstractItemEditorInput {

    private RuleRepNode ruleRepNode;

    /**
     * note: for DQ team, use this BusinessRuleItemEditorInput constructor.
     * 
     * @param ruleRepNode
     */
    public BusinessRuleItemEditorInput(IRepositoryNode ruleRepNode) {
        super(ruleRepNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getRepNode()
     */
    @Override
    public RuleRepNode getRepNode() {
        return ruleRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getModelOfRepNode()
     */
    @Override
    public ModelElement getModel() {
        return ruleRepNode.getRule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#setRepNode(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public void setRepNode(IRepositoryNode node) {
        this.ruleRepNode = (RuleRepNode) node;
    }
}
