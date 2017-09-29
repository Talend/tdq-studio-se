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
package org.talend.dataprofiler.core.ui.editor.report;

import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class ReportItemEditorInput extends AbstractItemEditorInput {

    private ReportRepNode reportRepNode;

    /**
     * ReportItemEditorInput constructor.
     * 
     * @param reportRepNode
     */
    public ReportItemEditorInput(IRepositoryNode reportRepNode) {
        super(reportRepNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getRepNode()
     */
    @Override
    public ReportRepNode getRepNode() {
        return reportRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getModelOfRepNode()
     */
    @Override
    public ModelElement getModel() {
        return reportRepNode.getReport();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#setRepNode(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public void setRepNode(IRepositoryNode node) {
        this.reportRepNode = (ReportRepNode) node;
    }
}
