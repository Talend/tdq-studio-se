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
import org.talend.cwm.helper.PackageHelper;
import org.talend.dq.nodes.factory.DQRepNodeCreateFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC klliu Database schema repository node displayed on repository view (UI).
 */
public class DBSchemaRepNode extends DQRepositoryNode {

    private MetadataSchemaRepositoryObject metadataSchemaObject = null;

    public Schema getSchema() {
        return this.metadataSchemaObject.getSchema();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getObject()
     */
    @Override
    public IRepositoryViewObject getObject() {
        return this.metadataSchemaObject;
    }

    /**
     * DOC klliu DBSchemaRepNode constructor comment.
     * 
     * @param object
     * @param parent if parent is null will try to create new one to insert of old parent.
     * @param type
     */
    public DBSchemaRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        if (object instanceof MetadataSchemaRepositoryObject) {
            metadataSchemaObject = (MetadataSchemaRepositoryObject) object;
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
        RepositoryNode dbParentRepNode = null;
        Package parentPackage = PackageHelper.getParentPackage(getSchema());
        if (parentPackage == null) {
            dbParentRepNode = new DBConnectionRepNode(getParentViewObject(), null, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        } else if (parentPackage instanceof Catalog) {
            dbParentRepNode = DQRepNodeCreateFactory.createDBCatalogRepNode(getParentViewObject(), null,
                    ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        }
        return dbParentRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DQRepositoryNode#getParentViewObject()
     */
    @Override
    protected IRepositoryViewObject getParentViewObject() {
        IRepositoryViewObject packageViewObject = null;
        Package parentPackage = PackageHelper.getParentPackage(getSchema());
        if (parentPackage == null) {
            return metadataSchemaObject.getViewObject();
        } else if (parentPackage instanceof Catalog) {
            packageViewObject = new MetadataCatalogRepositoryObject(metadataSchemaObject.getViewObject(), (Catalog) parentPackage);
        }
        return packageViewObject;
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        IRepositoryViewObject object = getObject();
        // MOD msjian 2011-7-13 feature 22206 : fix note 0091973 issue1
        if (DQRepositoryNode.isUntilSchema()) {
            return createTableViewFolder((MetadataSchemaRepositoryObject) object);
        }
        // MOD gdbu 2011-6-28 bug : 22204
        return filterResultsIfAny(createTableViewFolder((MetadataSchemaRepositoryObject) object));
        // ~22204
    }

    private List<IRepositoryNode> createTableViewFolder(MetadataSchemaRepositoryObject metadataSchema) {
        // IRepositoryViewObject viewObject = metadataSchema.getViewObject();
        List<IRepositoryNode> repsNodes = new ArrayList<IRepositoryNode>();
        // table folder node under catalog
        DBTableFolderRepNode tableFloderNode = new DBTableFolderRepNode(null, this, ENodeType.TDQ_REPOSITORY_ELEMENT,
                getProject());
        repsNodes.add(tableFloderNode);
        // view folder node under catalog
        DBViewFolderRepNode viewFolderNode = new DBViewFolderRepNode(null, this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        repsNodes.add(viewFolderNode);
        return repsNodes;
    }

    @Override
    public String getLabel() {
        if (getObject() == null) {
            return this.getProperties(EProperties.LABEL).toString();
        }
        return this.getObject().getLabel();
    }
}
