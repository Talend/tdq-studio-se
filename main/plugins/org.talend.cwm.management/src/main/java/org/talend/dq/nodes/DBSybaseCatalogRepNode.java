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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by talend on Dec 30, 2013 Detailled comment
 * 
 */
public class DBSybaseCatalogRepNode extends DBCatalogRepNode {

    private static Logger log = Logger.getLogger(DBSybaseCatalogRepNode.class);

    /**
     * DOC talend DBSybaseCatalogRepNode constructor comment.
     * 
     * @param viewObject
     * @param parent
     * @param type
     */
    public DBSybaseCatalogRepNode(IRepositoryViewObject viewObject, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(viewObject, parent, type, inWhichProject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.nodes.DBCatalogRepNode#getChildren()
     */
    @Override
    public List<IRepositoryNode> getChildren() {
        // fixed TDQ-5662 this case is deal with old version sybase connection, because the schema can not be contain on
        // the catalog when create it
        if (!containSchemas()) {
            Item databaseItem = this.getObject().getProperty().getItem();
            boolean fillSucess = fillSchemaToCatalog(databaseItem);
            if (fillSucess) {
                try {
                    ProxyRepositoryFactory.getInstance().save(databaseItem);
                } catch (PersistenceException e) {
                    log.error(e, e);
                }
            } else {
                // if fill is not success then return an empty list
                return new ArrayList<IRepositoryNode>();

            }
        }
        return super.getChildren();
    }

    /**
     * judge whether the schemas has been filled
     * 
     * @return
     */
    private boolean containSchemas() {
        List<Schema> schemas = CatalogHelper.getSchemas(getCatalog());
        if (schemas.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 
     * fill schemas on the catalog
     * 
     * @return if fill action is success
     */
    private boolean fillSchemaToCatalog(Item databaseItem) {

        DatabaseConnection connection = (DatabaseConnection) ((DatabaseConnectionItem) databaseItem).getConnection();

        TypedReturnCode<Connection> rcConn = MetadataConnectionUtils.createConnection(connection);
        if (!rcConn.isOk()) {
            log.error(rcConn.getMessage());
            return false;
        }

        java.sql.Connection sqlConnection = rcConn.getObject();
        try {
            DatabaseMetaData dbJDBCMetadata = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(sqlConnection, connection,
                    false);
            EDatabaseTypeName currentEDatabaseType = EDatabaseTypeName.getTypeFromDbType(connection.getDatabaseType());
            List<Schema> schemaList = null;
            if (currentEDatabaseType != null) {
                schemaList = MetadataFillFactory.getDBInstance(currentEDatabaseType).fillSchemaToCatalog(connection,
                        dbJDBCMetadata, this.getCatalog(), null);
            }
            if (schemaList != null && schemaList.size() > 0) {
                CatalogHelper.addSchemas(schemaList, this.getCatalog());
            }
        } finally {
            if (sqlConnection != null) {
                ConnectionUtils.closeConnection(sqlConnection);
            }
        }
        return true;
    }

}
