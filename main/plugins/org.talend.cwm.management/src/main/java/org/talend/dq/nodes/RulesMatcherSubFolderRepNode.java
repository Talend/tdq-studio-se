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

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.RepositoryNode;

/**
 * created by zshen on Aug 19, 2013 Detailled comment
 *
 */
public class RulesMatcherSubFolderRepNode extends RulesMatcherFolderRepNode {

    /**
     * 
     * @param object
     * @param parent
     * @param type
     */
    public RulesMatcherSubFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

}
