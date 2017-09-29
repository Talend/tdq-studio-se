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
package org.talend.dataprofiler.core.ui.editor.matchrule;

import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dq.helper.EObjectHelper;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * TDQ-8212 msjian note: this class ONLY used for MDM team to open matchrule editor, because they only can get item, didn't have
 * IRepositoryNode.
 *
 */
public class MatchRuleItemEditorInput extends AbstractItemEditorInput {

    /**
     * MatchRuleItemEditorInput constructor.
     *
     * @param tdqItem
     */
    public MatchRuleItemEditorInput(Item tdqItem) {
        super(tdqItem);
    }

    public ModelElement getMatchRule() {
        return ((TDQMatchRuleItem) this.getItem()).getMatchRule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getRepNode()
     */
    @Override
    public IRepositoryNode getRepNode() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#setRepNode(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public void setRepNode(IRepositoryNode node) {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getModel()
     */
    @Override
    public ModelElement getModel() {
        return null;
    }

    @Override
    public String getName() {
        return item.getProperty().getLabel();
    }

    @Override
    public String getToolTipText() {
        return item.getProperty().getLabel();
    }

    @Override
    public String getPath() {
        return item.getState().getPath() + "/";//$NON-NLS-1$ 
    }

    @Override
    public Item getItem() {
        return (Item) EObjectHelper.resolveObject(item);
    }
}
