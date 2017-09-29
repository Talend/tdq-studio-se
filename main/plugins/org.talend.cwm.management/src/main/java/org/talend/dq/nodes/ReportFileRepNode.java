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

import org.eclipse.core.resources.IResource;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * Report generated document file RepositoryNode.
 */
public class ReportFileRepNode extends DQRepositoryNode {

    private IResource resource;

    public IResource getResource() {
        return this.resource;
    }

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    /**
     * DOC xqliu ReportFileRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public ReportFileRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    public String getLabel() {
        if (this.getResource() != null) {
            return this.getResource().getName();
        }
        return super.getLabel();
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        return new ArrayList<IRepositoryNode>();
    }
}
