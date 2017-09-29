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
package org.talend.dataprofiler.core.ui.editor.pattern;

import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC klliu Pattern item editor input.
 */
public class PatternItemEditorInput extends AbstractItemEditorInput {

    private PatternRepNode patternRepNode;

    /**
     * PatternItemEditorInput constructor.
     * 
     * @param patternRepNode
     */
    public PatternItemEditorInput(IRepositoryNode patternRepNode) {
        super(patternRepNode);
    }

    @Override
    public String getModelElementUuid() {
        if (this.patternRepNode != null) {
            return ResourceHelper.getUUID(getItem().getParent());
        }
        return super.getModelElementUuid();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getRepNode()
     */
    @Override
    public PatternRepNode getRepNode() {
        return patternRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getModelOfRepNode()
     */
    @Override
    public ModelElement getModel() {
        return patternRepNode.getPattern();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#setRepNode(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public void setRepNode(IRepositoryNode node) {
        this.patternRepNode = (PatternRepNode) node;
    }
}
