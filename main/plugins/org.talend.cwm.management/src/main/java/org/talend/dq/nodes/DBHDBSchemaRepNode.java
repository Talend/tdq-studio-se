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

import java.util.List;

import org.talend.core.model.general.Project;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

public class DBHDBSchemaRepNode extends DBSchemaRepNode {

    /**
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public DBHDBSchemaRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    protected List<IRepositoryNode> createTableViewFolder(MetadataSchemaRepositoryObject metadataSchema) {
        List<IRepositoryNode> repsNodes = super.createTableViewFolder(metadataSchema);
        // only sys_bic may conatians calculation views
        if (!metadataSchema.getSchema().getName().toLowerCase().equals("_sys_bic")) {
            return repsNodes;
        }
        DBCalculationViewFolderRepNode calViewFloderNode =
                new DBCalculationViewFolderRepNode(null, this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        repsNodes.add(calViewFloderNode);
        return repsNodes;
    }

}
