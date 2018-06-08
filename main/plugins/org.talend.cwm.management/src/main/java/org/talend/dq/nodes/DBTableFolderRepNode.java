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

import org.apache.log4j.Logger;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataCatalogRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataSchemaRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.PluginConstant;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.factory.DQRepNodeCreateFactory;
import org.talend.dq.nodes.foldernode.IConnectionElementSubFolder;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC klliu Folder node node displayed on repository view (UI), knowing exact folder type by folder
 * object:TDQFolderObject.
 */
public class DBTableFolderRepNode extends DQDBFolderRepositoryNode implements IConnectionElementSubFolder {

    private static Logger log = Logger.getLogger(DBTableFolderRepNode.class);

    private IRepositoryViewObject viewObject;

    private Catalog catalog;

    private Schema schema;

    public Catalog getCatalog() {
        return this.catalog;
    }

    public Schema getSchema() {
        return this.schema;
    }

    /**
     * DBTableFolderRepNode constructor comment.
     * 
     * @param object
     * @param parent if parent is null will try to create new one to insert of old parent.
     * @param type
     */
    public DBTableFolderRepNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
        children = new ArrayList<IRepositoryNode>();
        this.viewObject = object;
        if (parent == null) {
            RepositoryNode createParentNode = createParentNode();
            this.setParent(createParentNode);
        }
    }

    /**
     * create the node of parent.
     * 
     * @return
     */
    private RepositoryNode createParentNode() {
        RepositoryNode dbParentRepNode = null;
        if (viewObject instanceof MetadataCatalogRepositoryObject) {
            dbParentRepNode = DQRepNodeCreateFactory.createDBCatalogRepNode(viewObject, null, ENodeType.TDQ_REPOSITORY_ELEMENT,
                    getProject());
        } else if (viewObject instanceof MetadataSchemaRepositoryObject) {
            dbParentRepNode = new DBSchemaRepNode(viewObject, null, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
        }
        viewObject.setRepositoryNode(dbParentRepNode);
        return dbParentRepNode;
    }

    @Override
    public List<IRepositoryNode> getChildren() {
        if (!this.isReload() && !children.isEmpty()) {
            // MOD gdbu 2011-6-29 bug : 22204
            return filterResultsIfAny(children);
        }
        children.clear();

        IRepositoryViewObject object = this.getParent().getObject();
        createRepositoryNodeTableFolderNode(object);
        // ADD msjian 2011-7-22 22206: fix the note 93101
        if (DQRepositoryNode.isUntilSchema()) {
            return children;
        }
        // ~22206
        this.setReload(false);
        return filterResultsIfAny(children);
        // ~22204
    }

    /**
     * Create TableFolderNodeRepositoryNode.
     * 
     * @param metadataObject parent CatalogViewObject or SchemaViewObject
     */
    private void createRepositoryNodeTableFolderNode(IRepositoryViewObject metadataObject) {
        List<TdTable> tables = new ArrayList<TdTable>();
        String filterCharacter = null;
        try {
            if (metadataObject instanceof MetadataCatalogRepositoryObject) {
                viewObject = ((MetadataCatalogRepositoryObject) metadataObject).getViewObject();
                setItem((ConnectionItem) viewObject.getProperty().getItem());
                setConnection(getItem().getConnection());
                if (((MetadataCatalogRepositoryObject) metadataObject).getCatalog().eIsProxy()) {
                    // reload the connection to make sure the connection(and all it's owned elements) is not proxy
                    reloadConnectionViewObject();
                }
                catalog = ((MetadataCatalogRepositoryObject) metadataObject).getCatalog();

                tables = PackageHelper.getTables(catalog);
                filterCharacter = RepositoryNodeHelper.getTableFilter(catalog, schema);

                // MOD TDQ-8718 20140505 yyin --the repository view cares about if use the filter or not, the column
                // select dialog cares about if connect to DB or not.
                if (tables.isEmpty()) {
                    if (isCallingFromColumnDialog()) {
                        tables = DqRepositoryViewService.getTables(getConnection(), catalog, null, isLoadDBFromDialog(), true);
                    } else if (!isOnFilterring()) {
                        // MOD mzhao 0022204 : when the tree is rendering with a filter, do not loading from db.
                        tables = DqRepositoryViewService.getTables(getConnection(), catalog, null, true, true);
                    }
                    if (tables != null && tables.size() > 0) {
                        ProxyRepositoryFactory.getInstance().save(getItem(), false);
                    }
                }

            } else {
                viewObject = ((MetadataSchemaRepositoryObject) metadataObject).getViewObject();
                setItem((ConnectionItem) viewObject.getProperty().getItem());
                setConnection(getItem().getConnection());
                if (((MetadataSchemaRepositoryObject) metadataObject).getSchema().eIsProxy()) {
                    // reload the connection to make sure the connection(and all it's owned elements) is not proxy
                    reloadConnectionViewObject();
                }
                schema = ((MetadataSchemaRepositoryObject) metadataObject).getSchema();
                tables = PackageHelper.getTables(schema);
                filterCharacter = RepositoryNodeHelper.getTableFilter(catalog, schema);
                RepositoryNode parent = metadataObject.getRepositoryNode().getParent();
                IRepositoryViewObject object = parent.getObject();
                if (object instanceof MetadataCatalogRepositoryObject && filterCharacter.equals(PluginConstant.EMPTY_STRING)) {
                    filterCharacter = RepositoryNodeHelper.getTableFilter(
                            ((MetadataCatalogRepositoryObject) object).getCatalog(), null);
                }

                if (tables.isEmpty()) {
                    if (isCallingFromColumnDialog()) {
                        tables = DqRepositoryViewService.getTables(getConnection(), schema, null, isLoadDBFromDialog(), true);
                    } else if (!isOnFilterring()) {
                        // MOD mzhao 0022204 : when the tree is rendering with a filter, do not loading from db.
                        tables = DqRepositoryViewService.getTables(getConnection(), schema, null, true, true);
                    }
                    if (tables != null && tables.size() > 0) {
                        ProxyRepositoryFactory.getInstance().save(getItem(), false);
                    }
                }
            }

            ConnectionUtils.retrieveColumn(tables);

        } catch (Exception e) {
            log.error(e, e);
        }
        if (filterCharacter != null && !filterCharacter.equals(PluginConstant.EMPTY_STRING)) {
            tables = RepositoryNodeHelper.filterTables(tables, filterCharacter);
        }
        createTableRepositoryNode(tables, children);
    }

    /**
     * DOC klliu Comment method "createTableRepositoryNode".
     * 
     * @param tables
     */
    private void createTableRepositoryNode(List<TdTable> tables, List<IRepositoryNode> node) {
        if (tables != null) {
            for (TdTable table : tables) {
                table.setTableType("TABLE"); //$NON-NLS-1$
                TdTableRepositoryObject metadataTable = new TdTableRepositoryObject(viewObject, table);
                metadataTable.setTableName(table.getName());
                metadataTable.setLabel(table.getName());
                metadataTable.setId(table.getName());

                DBTableRepNode tableNode = new DBTableRepNode(metadataTable, this, ENodeType.TDQ_REPOSITORY_ELEMENT, getProject());
                tableNode.setProperties(EProperties.LABEL, ERepositoryObjectType.METADATA_CON_TABLE);
                tableNode.setProperties(EProperties.CONTENT_TYPE, ERepositoryObjectType.METADATA_CON_TABLE);

                metadataTable.setRepositoryNode(tableNode);
                node.add(tableNode);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.RepositoryNode#getLabel()
     */
    @Override
    public String getLabel() {
        if (hasChildren()) {
            return "Tables (" + this.getChildrenCount() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return "Tables"; //$NON-NLS-1$
    }

    private int getChildrenCount() {
        List<TdTable> tables = new ArrayList<TdTable>();
        IRepositoryViewObject object = this.getParent().getObject();
        if (object instanceof MetadataCatalogRepositoryObject) {
            catalog = ((MetadataCatalogRepositoryObject) object).getCatalog();
            tables = PackageHelper.getTables(catalog);
        } else {
            schema = ((MetadataSchemaRepositoryObject) object).getSchema();
            tables = PackageHelper.getTables(schema);
        }
        return tables.size();
    }

    /**
     * return the Catalog or Schema, or null.
     * 
     * @return
     */
    public Package getPackage() {
        Package result = null;

        if (this.getCatalog() != null) {
            result = this.getCatalog();
        } else if (this.getSchema() != null) {
            result = this.getSchema();
        } else {
            RepositoryNode parent = this.getParent();

            if (parent instanceof DBSchemaRepNode) {
                this.schema = ((DBSchemaRepNode) parent).getSchema();
                result = this.schema;
            } else if (parent instanceof DBCatalogRepNode) {
                this.catalog = ((DBCatalogRepNode) parent).getCatalog();
                result = this.catalog;
            }
        }

        return result;
    }

    @Override
    public IImage getIcon() {
        return ECoreImage.FOLDER_CLOSE_ICON;
    }

}
