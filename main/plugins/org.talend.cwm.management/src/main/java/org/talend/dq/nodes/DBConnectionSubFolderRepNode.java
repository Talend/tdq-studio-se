// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu DBConnectionSubFolderRepNode type comment.
 */
public class DBConnectionSubFolderRepNode extends DBConnectionFolderRepNode {

    /**
     * DOC klliu DBConnectionSubFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DBConnectionSubFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getObject().getLabel();
    }

    @Override
    public IImage getIcon() {
        return getIconWithState(this.getObject());
    }

}
