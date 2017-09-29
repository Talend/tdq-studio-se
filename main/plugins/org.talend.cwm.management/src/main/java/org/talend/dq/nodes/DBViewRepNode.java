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
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdViewRepositoryObject;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.relational.TdView;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC klliu Database view repository node displayed on repository view (UI).
 */
public class DBViewRepNode extends ColumnSetRepNode {

    private TdViewRepositoryObject tdViewRepositoryObject;

    public TdViewRepositoryObject getTdViewRepositoryObject() {
        return this.tdViewRepositoryObject;
    }

    public TdView getTdView() {
        return this.tdViewRepositoryObject.getTdView();
    }

    /**
     * DOC klliu DBViewRepNode constructor comment.
     * 
     * @param object
     * @param parent if parent is null will try to create new one to insert of old parent.
     * @param type
     */
    public DBViewRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        if (object instanceof TdViewRepositoryObject) {
            this.tdViewRepositoryObject = (TdViewRepositoryObject) object;
            if (parent == null) {
                RepositoryNode createParentNode = createParentNode();
                this.setParent(createParentNode);
            }
        }
    }

    /**
     * create the node of parent.
     * 
     * @param object
     * @return
     */
    private RepositoryNode createParentNode() {
        DBViewFolderRepNode dbViewFolderRepNode = new DBViewFolderRepNode(getParentViewObject(), null,
                ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        dbViewFolderRepNode.setId(NO_ID);
        return dbViewFolderRepNode;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DQRepositoryNode#getParentViewObject()
     */
    @Override
    protected IRepositoryViewObject getParentViewObject() {
        IRepositoryViewObject packageViewObject = null;
        Package parentPackage = PackageHelper.getParentPackage(getTdView());
        if (parentPackage instanceof Catalog) {
            packageViewObject = new MetadataCatalogRepositoryObject(tdViewRepositoryObject.getViewObject(),
                    (Catalog) parentPackage);
        } else if (parentPackage instanceof Schema) {
            packageViewObject = new MetadataSchemaRepositoryObject(tdViewRepositoryObject.getViewObject(), (Schema) parentPackage);
        }
        return packageViewObject;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        // MOD gdbu 2011-7-1 bug : 22204
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        DBColumnFolderRepNode columnFolderNode = new DBColumnFolderRepNode(getObject(), this, ENodeType.TDQ_REPOSITORY_ELEMENT,
                getProject());
        nodes.add(columnFolderNode);
        return filterResultsIfAny(nodes);
        // ~22204
    }

    @Override
    public String getLabel() {
        return this.getTdView().getName();
    }
}
