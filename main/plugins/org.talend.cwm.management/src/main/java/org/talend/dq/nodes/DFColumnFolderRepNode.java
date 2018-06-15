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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.cwm.management.i18n.Messages;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DFColumnFolderRepNode extends DQRepositoryNode {

    private IRepositoryViewObject object;

    private EList<MetadataColumn> columns;

    private MetadataTable mTable;

    /**
     * DOC qiongli DFColumnFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DFColumnFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        List<IRepositoryNode> repsNodes = new ArrayList<IRepositoryNode>();
        List<MetadataColumn> mdColumns = new ArrayList<MetadataColumn>();
        IRepositoryViewObject meataColumnSetObject = this.getObject();
        if (meataColumnSetObject instanceof MetadataTableRepositoryObject) {
            MetadataTableRepositoryObject mTableRepositoryObject = (MetadataTableRepositoryObject) meataColumnSetObject;
            object = mTableRepositoryObject;
            mTable = mTableRepositoryObject.getTable();
            columns = mTable.getColumns();
        }
        if (columns != null && columns.size() > 0) {
            for (MetadataColumn mec : columns) {
                mdColumns.add(mec);
            }
        }
        createMdcolumnsNode(mdColumns, repsNodes);

        // MOD msjian 2011-7-13 feature 22206 : fix note 0091973 issue3
        return filterResultsIfAny(repsNodes);
    }

    /**
     * 
     * DOC qiongli Comment method "createTdcolumnsNode".
     * 
     * @param tdcolumns
     * @param repsNodes
     */
    private void createMdcolumnsNode(List<MetadataColumn> mdColumns, List<IRepositoryNode> repsNodes) {
        for (MetadataColumn mdColumn : mdColumns) {
            MetadataColumnRepositoryObject metadataColumn = new MetadataColumnRepositoryObject(object, mdColumn);
            metadataColumn.setId(mdColumn.getLabel());
            metadataColumn.setLabel(mdColumn.getLabel());
            DFColumnRepNode columnNode = new DFColumnRepNode(metadataColumn, this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
            columnNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_COLUMN);
            columnNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_COLUMN);
            metadataColumn.setRepositoryNode(columnNode);
            repsNodes.add(columnNode);
        }
    }

    public MetadataTable getmTable() {
        return this.mTable;
    }

    public String getNodeName() {
        return Messages.getString("DFColumnFolderRepNode.Columns", this.getChildrenCount()); //$NON-NLS-1$ 
    }

    public int getChildrenCount() {
        List<IRepositoryNode> children2 = this.getChildren();
        if (children2 != null) {
            return children2.size();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getLabel()
     */
    @Override
    public String getLabel() {
        if (getObject() == null) {
            return this.getProperties(EProperties.LABEL).toString();
        }
        return this.getObject().getLabel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getDisplayText()
     */
    @Override
    public String getDisplayText() {
        return getNodeName();
    }

    @Override
    public IImage getIcon() {
        return ECoreImage.FOLDER_CLOSE_ICON;
    }

}
