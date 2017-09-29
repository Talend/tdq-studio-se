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
package org.talend.dq.nodes;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class RuleRepNode extends DQRepositoryNode {

    /**
     * RuleRepNode constructor.
     *
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public RuleRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    public IndicatorDefinition getRule() {
        if (getObject() != null && getObject().getProperty() != null) {
            Item item = getObject().getProperty().getItem();
            if (item != null && item instanceof TDQBusinessRuleItem) {
                return ((TDQBusinessRuleItem) item).getDqrule();
            } else if (item != null && item instanceof TDQMatchRuleItem) {
                return ((TDQMatchRuleItem) item).getMatchRule();
            }
        }
        return null;
    }

    @Override
    public String getLabel() {
        if (this.getRule() != null && this.getRule().getName() != null) {
            return this.getRule().getName();
        }
        return super.getLabel();
    }

    @Override
    public boolean canExpandForDoubleClick() {
        return false;
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        // MOD klliu 2011-06-28 bug 22669
        return new ArrayList<IRepositoryNode>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getLabel() + " " + getObject().getVersion(); //$NON-NLS-1$
    }
}
