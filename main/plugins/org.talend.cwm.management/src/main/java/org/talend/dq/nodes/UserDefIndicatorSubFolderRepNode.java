// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment: user defined indicator sub folder repository node
 */
public class UserDefIndicatorSubFolderRepNode extends UserDefIndicatorFolderRepNode {

    /**
     * DOC klliu UserDefIndicatorSubFolderRepNode constructor comment.
     *
     * @param object
     * @param parent
     * @param type
     */
    public UserDefIndicatorSubFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }
}
