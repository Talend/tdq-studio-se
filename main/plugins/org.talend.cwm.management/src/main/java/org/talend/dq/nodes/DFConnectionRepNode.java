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
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.record.RecordFile;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DFConnectionRepNode extends ConnectionRepNode {

    public DelimitedFileConnection getDfConnection() {
        DelimitedFileConnection dfConnection = null;
        Property property = getObject().getProperty();
        Item item = property.getItem();
        if (item instanceof DelimitedFileConnectionItem) {
            dfConnection = (DelimitedFileConnection) ((DelimitedFileConnectionItem) item).getConnection();
        }
        return dfConnection;
    }

    /**
     * DOC qiongli DelimitedFileConnectionRepNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DFConnectionRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        DelimitedFileConnection dfConnection = getDfConnection();
        if (dfConnection != null) {
            EList<Package> dataPackage = dfConnection.getDataPackage();
            if (dataPackage != null && dataPackage.size() > 0) {
                Package pack = dataPackage.get(0);
                if (pack instanceof RecordFile) {
                    return filterResultsIfAny(createRepositoryNodeSchema());
                }
            }
        }

        return new ArrayList<IRepositoryNode>();
    }

    private List<IRepositoryNode> createRepositoryNodeSchema() {
        super.getChildren().clear();
        IRepositoryViewObject object = this.getObject();
        Set<MetadataTable> tables = ConnectionHelper.getTables(((ConnectionItem) getObject().getProperty().getItem())
                .getConnection());
        if (tables != null && tables.size() > 0) {
            for (MetadataTable mtable : tables) {
                MetadataTableRepositoryObject mtRepObject = new MetadataTableRepositoryObject(object, mtable);
                mtRepObject.setTableName(mtable.getLabel());
                mtRepObject.setLabel(mtable.getLabel());
                mtRepObject.setId(mtable.getLabel());
                DFTableRepNode tableNode = new DFTableRepNode(mtRepObject, this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                tableNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_TABLE);
                tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_TABLE);
                mtRepObject.setRepositoryNode(tableNode);
                super.getChildren().add(tableNode);
            }
        }
        return super.getChildren();
    }

    @Override
    public boolean canExpandForDoubleClick() {
        return false;
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
}
