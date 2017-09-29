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
package org.talend.dataprofiler.core.ui.editor.connection;

import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC mzhao Connection item editor input.
 */
public class ConnectionItemEditorInput extends AbstractItemEditorInput {

    private DBConnectionRepNode dBConnectionRepNode;

    /**
     * ConnectionItemEditorInput constructor.
     * 
     * @param dBConnectionRepNode
     */
    public ConnectionItemEditorInput(IRepositoryNode dBConnectionRepNode) {
        super(dBConnectionRepNode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getRepNode()
     */
    @Override
    public DBConnectionRepNode getRepNode() {
        return dBConnectionRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#getModelOfRepNode()
     */
    @Override
    public ModelElement getModel() {
        return dBConnectionRepNode.getDatabaseConnection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput#setRepNode(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public void setRepNode(IRepositoryNode node) {
        this.dBConnectionRepNode = (DBConnectionRepNode) node;
    }
}
