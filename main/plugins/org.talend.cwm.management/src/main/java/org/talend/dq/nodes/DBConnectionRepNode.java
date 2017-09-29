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

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.factory.DQRepNodeCreateFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC klliu Database connection repository node displayed on repository view (UI).
 */
public class DBConnectionRepNode extends ConnectionRepNode {

    List<IRepositoryNode> afterGlobalFilter;

    /**
     * DBConnectionRepNode constructor.
     * 
     * @param object
     * @param parent
     * @param type
     * @param inWhichProject
     */
    public DBConnectionRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    public DatabaseConnection getDatabaseConnection() {
        Property property = this.getObject().getProperty();
        if (property != null && property.getItem() != null
                && ((ConnectionItem) property.getItem()).getConnection() instanceof DatabaseConnection) {
            return (DatabaseConnection) ((ConnectionItem) property.getItem()).getConnection();
        }
        return null;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        afterGlobalFilter = new ArrayList<IRepositoryNode>();
        // Retrieve catalogs/schemes.
        DatabaseConnection databaseConnection = getDatabaseConnection();
        if (databaseConnection != null) {
            EList<Package> dataPackage = databaseConnection.getDataPackage();
            if (dataPackage != null && dataPackage.size() > 0) {
                Package pack = dataPackage.get(0);
                String filterCharater = ConnectionHelper.getPackageFilter(databaseConnection);
                List<IRepositoryNode> afterPackageFilter = null;
                if (pack instanceof Schema) {
                    // MOD gdbu 2011-6-29 bug : 22204
                    afterGlobalFilter = filterResultsIfAny(createRepositoryNodeSchema(dataPackage));
                    afterPackageFilter = filterPackages(filterCharater, afterGlobalFilter);
                    return afterPackageFilter == null ? afterGlobalFilter : afterPackageFilter;
                } else if (pack instanceof Catalog) {
                    afterGlobalFilter = filterResultsIfAny(createRepositoryNodeCatalog(dataPackage));
                    afterPackageFilter = filterPackages(filterCharater, afterGlobalFilter);
                    return afterPackageFilter == null ? afterGlobalFilter : afterPackageFilter;
                    // ~22204
                }
            }
        }
        return new ArrayList<IRepositoryNode>();

    }

    /**
     * 
     * Filter package node for connection.
     * 
     * @param filterCharater
     * @param afterGlobalFilter
     * @return
     */
    private List<IRepositoryNode> filterPackages(String filterCharater, List<IRepositoryNode> afterGlobalFilter) {
        if (filterCharater == null || filterCharater.trim().equalsIgnoreCase("")) {//$NON-NLS-1$
            return afterGlobalFilter;
        }

        if (isReturnAllNodesWhenFiltering()) {
            return afterGlobalFilter;
        }

        // MOD yyin TDQ-5077 20121213
        return RepositoryNodeHelper.filterPackages(afterGlobalFilter, filterCharater);
    }

    /**
     * DOC klliu Comment method "createRepositoryNodeSchema".
     * 
     * @param node
     * @param viewObject
     * @param schema
     */
    private List<IRepositoryNode> createRepositoryNodeSchema(EList<Package> dataPackage) {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        for (Package pack : dataPackage) {
            initializedSchemaRepNode(nodes, pack);
        }
        return nodes;
    }

    private void initializedSchemaRepNode(List<IRepositoryNode> nodes, Package pack) {
        MetadataSchemaRepositoryObject metadataSchema = new MetadataSchemaRepositoryObject(getObject(), (Schema) pack);
        RepositoryNode schemaNode = new DBSchemaRepNode(metadataSchema, this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        schemaNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_SCHEMA);
        schemaNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_SCHEMA);
        metadataSchema.setRepositoryNode(schemaNode);
        nodes.add(schemaNode);
    }

    /**
     * DOC klliu Comment method "createRepositoryNodeCatalog".
     * 
     * @param node
     * @param viewObject
     * @param catalog
     */
    private List<IRepositoryNode> createRepositoryNodeCatalog(EList<Package> dataPackage) {
        List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
        for (Package pack : dataPackage) {
            initializedCatalogRepNode(nodes, pack);
        }
        return nodes;
    }

    private void initializedCatalogRepNode(List<IRepositoryNode> nodes, Package pack) {
        if (pack instanceof Catalog) {
            MetadataCatalogRepositoryObject metadataCatalog = new MetadataCatalogRepositoryObject(getObject(), (Catalog) pack);
            RepositoryNode catalogNode = DQRepNodeCreateFactory.createDBCatalogRepNode(metadataCatalog, this,
                    ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
            catalogNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_CATALOG);
            catalogNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_CATALOG);
            metadataCatalog.setRepositoryNode(catalogNode);
            nodes.add(catalogNode);
        }
    }

    @Override
    public String getLabel() {
        if (getObject() == null) {
            return this.getProperties(EProperties.LABEL).toString();
        }
        return this.getObject().getLabel();
    }

    @Override
    public boolean canExpandForDoubleClick() {
        return false;
    }

    public List<IRepositoryNode> getAfterGlobalFilter() {
        return this.afterGlobalFilter;
    }
}
