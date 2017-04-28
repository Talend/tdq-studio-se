// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.nodes.factory;

import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBSybaseCatalogRepNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * the Factory to create DQ RepositoryNode
 * 
 */
public class DQRepNodeCreateFactory {

    /**
     * 
     * this method is used to create a DBCatalogRepNode. And Sybase is a specal case because we don't create schema
     * level when the version of stdio is lower than 5.0.0.So we will create DBSybaseCatalogRepNode to deal with this
     * special case
     * 
     * @param viewObject
     * @param parent parent of repositoryNode
     * @param type
     * @return
     */
    public static RepositoryNode createDBCatalogRepNode(IRepositoryViewObject viewObject, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        Item databaseItem = viewObject.getProperty().getItem();
        DatabaseConnection dbConnection = (DatabaseConnection) ((DatabaseConnectionItem) databaseItem).getConnection();
        SupportDBUrlType dbTypeByKey = SupportDBUrlType.getDBTypeByKey(dbConnection.getDatabaseType());
        switch (dbTypeByKey == null ? SupportDBUrlType.MYSQLDEFAULTURL : dbTypeByKey) {
        case SYBASEDEFAULTURL:
            return new DBSybaseCatalogRepNode(viewObject, parent, type, inWhichProject);
        default:
            return new DBCatalogRepNode(viewObject, parent, type, inWhichProject);
        }
    }

}
