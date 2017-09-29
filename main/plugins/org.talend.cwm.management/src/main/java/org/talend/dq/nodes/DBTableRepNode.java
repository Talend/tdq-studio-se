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
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC klliu Database table repository node displayed on repository view (UI).
 */
public class DBTableRepNode extends ColumnSetRepNode {

    private TdTableRepositoryObject tdTableRepositoryObject;

    private List<IRepositoryNode> casheChildren;

    public TdTableRepositoryObject getTdTableRepositoryObject() {
        return this.tdTableRepositoryObject;
    }

    public TdTable getTdTable() {
        return this.tdTableRepositoryObject.getTdTable();
    }

    /**
     * DOC klliu DBTableRepNode constructor comment.
     * 
     * @param object
     * @param parent if parent is null will try to create new one to insert of old parent.
     * @param type
     */
    public DBTableRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        if (object instanceof TdTableRepositoryObject) {
            this.tdTableRepositoryObject = (TdTableRepositoryObject) object;
            if (parent == null) {
                RepositoryNode createParentNode = createParentNode();
                this.setParent(createParentNode);
            }
        }
        casheChildren = new ArrayList<IRepositoryNode>();
    }

    /**
     * create the node of parent.
     * 
     * @param object
     * @return
     */
    private RepositoryNode createParentNode() {
        DBTableFolderRepNode dbTableFolderRepNode = new DBTableFolderRepNode(getParentViewObject(), null,
                ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        dbTableFolderRepNode.setId(NO_ID);
        return dbTableFolderRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DQRepositoryNode#getParentViewObject()
     */
    @Override
    protected IRepositoryViewObject getParentViewObject() {
        IRepositoryViewObject packageViewObject = null;
        Package parentPackage = PackageHelper.getParentPackage(this.tdTableRepositoryObject.getTdTable());
        if (parentPackage instanceof Catalog) {
            packageViewObject = new MetadataCatalogRepositoryObject(tdTableRepositoryObject.getViewObject(),
                    (Catalog) parentPackage);
        } else if (parentPackage instanceof Schema) {
            packageViewObject = new MetadataSchemaRepositoryObject(tdTableRepositoryObject.getViewObject(),
                    (Schema) parentPackage);
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
        // MOD klliu TDQ-4332 only initlized sub node(column folder) once time 2012-02-07
        if (!casheChildren.isEmpty()) {
            return filterResultsIfAny(casheChildren);
        }
        // MOD gdbu 2011-7-1 bug : 22204
        // TdTableRepositoryObject viewObject = ((TdTableRepositoryObject) this.getObject());
        DBColumnFolderRepNode columnFolderNode = new DBColumnFolderRepNode(null, this, ENodeType.TDQ_REPOSITORY_ELEMENT,
                getProject());
        casheChildren.add(columnFolderNode);
        return filterResultsIfAny(casheChildren);
        // ~22204
    }

    /**
     * get the parent package node of this table.
     * 
     * @param node
     * @return
     */
    public static IRepositoryNode getParentPackageNode(IRepositoryNode node) {
        if (node == null) {
            return null;
        }
        if (node instanceof DBSchemaRepNode || node instanceof DBCatalogRepNode) {
            return node;
        } else {
            return getParentPackageNode(node.getParent());
        }
    }

    @Override
    public String getLabel() {
        return this.getTdTable().getName();
    }

    public List<IRepositoryNode> getCasheChildren() {
        return this.casheChildren;
    }

}
