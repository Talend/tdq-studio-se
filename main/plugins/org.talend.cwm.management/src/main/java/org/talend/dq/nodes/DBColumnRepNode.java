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

import java.util.List;

import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdViewRepositoryObject;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC klliu Database column repository node displayed on repository view (UI).
 */
public class DBColumnRepNode extends ColumnRepNode {

    /**
     * DOC klliu DBColumnRepNode constructor comment.
     * 
     * @param object
     * @param parent if parent is null will try to create new one to insert of old parent.
     * @param type
     */
    public DBColumnRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        if (object instanceof MetadataColumnRepositoryObject) {
            metadataColumnRepositoryObject = (MetadataColumnRepositoryObject) object;
            if (parent == null) {
                RepositoryNode createParentNode = createParentNode();
                this.setParent(createParentNode);
            }
        }
    }

    /**
     * create node of parent
     * 
     * @param metadataColumnRepositoryObject2
     */
    private RepositoryNode createParentNode() {
        DBColumnFolderRepNode dbColumnFolderRepNode = new DBColumnFolderRepNode(getParentViewObject(), null,
                ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        dbColumnFolderRepNode.setId(NO_ID);
        return dbColumnFolderRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dq.nodes.DQRepositoryNode#getParentViewObject(org.talend.core.model.repository.IRepositoryViewObject)
     */
    @Override
    protected IRepositoryViewObject getParentViewObject() {
        IRepositoryViewObject returnViewObject = null;
        MetadataTable columnOwnerAsMetadataTable = ColumnHelper.getColumnOwnerAsMetadataTable(getTdColumn());
        if (columnOwnerAsMetadataTable instanceof TdTable) {
            returnViewObject = new TdTableRepositoryObject(metadataColumnRepositoryObject.getViewObject(),
                    (TdTable) columnOwnerAsMetadataTable);
        } else if (columnOwnerAsMetadataTable instanceof TdView) {
            returnViewObject = new TdViewRepositoryObject(metadataColumnRepositoryObject.getViewObject(),
                    (TdView) columnOwnerAsMetadataTable);
        }
        return returnViewObject;
    }

    public TdColumn getTdColumn() {
        TdColumn tdColumn = (TdColumn) metadataColumnRepositoryObject.getTdColumn();
        return tdColumn;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        // MOD gdbu 2011-6-28 bug : 22204
        return filterResultsIfAny(super.getChildren());
        // ~22204
    }

    @Override
    public String getLabel() {
        if (this.getTdColumn() != null) {
            return this.getTdColumn().getName();
        }
        return super.getLabel();
    }

    public String getNodeDataType() {
        TdSqlDataType sqlDataType = this.getTdColumn().getSqlDataType();
        return sqlDataType.getName();

    }

    public boolean isKey() {
        return ColumnHelper.isPrimaryKey(this.getTdColumn());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getLabel() + "(" + getNodeDataType() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
