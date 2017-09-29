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
package org.talend.dq.nodes.hadoopcluster;

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.RepositoryNode;

/**
 * created by yyin on 2015年4月23日 Detailled comment
 *
 */
public class HadoopClusterSubFolderRepNode extends HadoopClusterFolderRepNode {

    /**
     * DOC yyin HadoopClusterSubFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public HadoopClusterSubFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    public String getDisplayText() {
        return getObject().getLabel();
    }

}
