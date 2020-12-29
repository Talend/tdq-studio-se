// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
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

import org.talend.core.model.general.Project;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.RepositoryNode;

public class DBCalculationViewRepNode extends DBTableRepNode {

    /**
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public DBCalculationViewRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    protected RepositoryNode createParentNode() {
        DBCalculationViewFolderRepNode dbCalculationViewFolderRepNode = new DBCalculationViewFolderRepNode(
                getParentViewObject(), null, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        dbCalculationViewFolderRepNode.setId(NO_ID);
        return dbCalculationViewFolderRepNode;
    }

}
