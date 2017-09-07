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

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.dq.helper.EObjectHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by xqliu on Apr 15, 2013 Detailled comment
 * 
 */
public class DQDBFolderRepositoryNode extends DQRepositoryNode {

    /**
     * This field is used to distinguish which context calling getChildren() method: – true: calling from the column
     * select dialog, – false: calling from the repository view
     */
    private static boolean isCallingFromColumnDialog = false;

    /**
     * This field is used to distinguish if connect to database or not when there are no children found: – true: will
     * connect to database, – false: will not connect to database
     */
    private static boolean isLoadDBFromDialog = true;

    protected ConnectionItem item;

    private Connection connection;

    protected List<IRepositoryNode> children = new ArrayList<IRepositoryNode>();

    private boolean reload = false;

    public boolean isReload() {
        return this.reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public void setConnection(Connection con) {
        this.connection = con;
    }

    public Connection getConnection() {
        if (this.connection == null) {
            getConnectionFromViewObject();
        }
        return this.connection;
    }

    protected void getConnectionFromViewObject() {
        IRepositoryViewObject object = this.getObject() == null ? this.getParent().getObject() : this.getObject();
        if (object != null && object instanceof ISubRepositoryObject) {
            Property property = ((ISubRepositoryObject) object).getProperty();
            if (property == null) {
                return;
            }
            Item theItem = property.getItem();
            if (theItem != null && theItem instanceof ConnectionItem) {
                connection = ((ConnectionItem) theItem).getConnection();
            }
        }
    }

    /**
     * DOC xqliu DQConnectionRepositoryNode constructor comment.
     * 
     * @param object
     * @param parent
     * @param type
     */
    public DQDBFolderRepositoryNode(IRepositoryViewObject object, RepositoryNode parent, ENodeType type,
            org.talend.core.model.general.Project inWhichProject) {
        super(object, parent, type, inWhichProject);
    }

    /**
     * call getProperty() to reload the connection if it has been unloaded. only called when the related object is
     * eProxy.
     */
    protected void reloadConnectionViewObject() {
        IRepositoryViewObject subRepositoryObject = this.getObject() == null ? this.getParent().getObject() : this.getObject();
        if (subRepositoryObject != null && subRepositoryObject instanceof ISubRepositoryObject) {
            // call getProperty() to reload the connection if it has been unloaded
            ((ISubRepositoryObject) subRepositoryObject).getProperty();
        }
    }

    public static boolean isLoadDBFromDialog() {
        return isLoadDBFromDialog;
    }

    public static void setLoadDBFromDialog(boolean isLoadDBFromDialog) {
        DQDBFolderRepositoryNode.isLoadDBFromDialog = isLoadDBFromDialog;
    }

    public static boolean isCallingFromColumnDialog() {
        return isCallingFromColumnDialog;
    }

    public static void setCallingFromColumnDialog(boolean isCallingFromColumnDialog) {
        DQDBFolderRepositoryNode.isCallingFromColumnDialog = isCallingFromColumnDialog;
    }

    /**
     * Getter for item.
     * 
     * @return the item
     */
    public ConnectionItem getItem() {
        return (ConnectionItem) EObjectHelper.resolveObject(item);
    }

    /**
     * Sets the item.
     * 
     * @param item the item to set
     */
    public void setItem(ConnectionItem item) {
        this.item = item;
    }

}
