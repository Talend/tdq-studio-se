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

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * The parent for: DBTableRepNode,DFTableRepNode,DBViewRepNode,and other table/view level node in the future. Which can
 * provide a easy way to judge them , and handle them together.
 * 
 */
public class ColumnSetRepNode extends DQRepositoryNode {

    /**
     * DOC talend ColumnSetRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public ColumnSetRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    /**
     * get all columns of the current column set.
     * 
     * @return
     */
    public List<IRepositoryNode> getAllColumns() {
        List<IRepositoryNode> folderList = this.getChildren();
        // first getChildren will only return the related Folder node; second getChildren will get related columns
        // under it
        if (folderList != null && folderList.size() > 0) {
            return folderList.get(0).getChildren();
        }
        return new ArrayList<IRepositoryNode>();
    }

}
