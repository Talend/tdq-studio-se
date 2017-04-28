// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.editor.analysis;

import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC klliu Analysis editor input.
 */
public class AnalysisItemEditorInput extends AbstractItemEditorInput {

    private IRepositoryNode anaRepNode;

    private IRepositoryNode connectionNode;

    /**
     * AnalysisItemEditorInput constructor.
     * 
     * @param anaRepNode
     */
    public AnalysisItemEditorInput(IRepositoryNode anaRepNode) {
        super(anaRepNode);
    }

    public IRepositoryNode getConnectionNode() {
        // MOD mzhao bug 19244, when connection node wasn't set before, find it from analysis.
        if (connectionNode == null) {
            DataManager connection = ((Analysis) getModel()).getContext().getConnection();
            if (connection != null) {
                connectionNode = RepositoryNodeHelper.recursiveFind(connection);
            }
        }
        return this.connectionNode;
    }

    public void setConnectionNode(IRepositoryNode connectionNode) {
        this.connectionNode = connectionNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getRepNode()
     */
    @Override
    public IRepositoryNode getRepNode() {
        return anaRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getModelOfRepNode()
     */
    @Override
    public ModelElement getModel() {
        if (anaRepNode instanceof ReportAnalysisRepNode) {
            return ((ReportAnalysisRepNode) anaRepNode).getAnalysis();
        }
        return ((AnalysisRepNode) anaRepNode).getAnalysis();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#setRepNode(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public void setRepNode(IRepositoryNode node) {
        this.anaRepNode = node;
    }

}
