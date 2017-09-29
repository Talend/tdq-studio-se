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
package org.talend.dataprofiler.core.migration.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * set default username for sqlite connection.
 */
public class UpdateSqliteUsernameTask extends AbstractWorksapceUpdateTask {

    public Date getOrder() {
        return createDate(2012, 7, 24);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        // get all Connections
        List<? extends ModelElement> allElement = PrvResourceFileHelper.getInstance().getAllElement();
        for (ModelElement me : allElement) {
            if (me instanceof DatabaseConnection) {
                DatabaseConnection conn = (DatabaseConnection) me;
                if (ConnectionUtils.isSqlite(conn)) {
                    String username = conn.getUsername();
                    if (username == null || "".equals(username.trim())) { //$NON-NLS-1$
                        ConnectionHelper.setUsername(conn, JavaSqlFactory.DEFAULT_USERNAME);
                        saveConn(conn);
                    }
                }
            }
        }
        return true;
    }

    /**
     * DOC xqliu Comment method "saveConn".
     * 
     * @param conn
     */
    private void saveConn(DatabaseConnection conn) {
        URI uriItem = conn.eResource().getURI();
        File fileItem = null;
        if (uriItem.isPlatform()) {
            fileItem = WorkspaceUtils.ifileToFile(getIFile(conn));
        } else {
            fileItem = new File(uriItem.toFileString());
        }

        File fileProp = WorkspaceUtils.ifileToFile(PropertyHelper.getPropertyFile(conn));
        Property property = PropertyHelper.getProperty(conn);
        Item item = property.getItem();

        // save DatabaseConnectionItem
        DatabaseConnectionItem dbConnItem = (DatabaseConnectionItem) item;
        dbConnItem.setConnection(conn);
        Resource itemResource = getResource(fileItem);
        EMFUtil.saveResource(itemResource);

        // save the property
        Resource propResource = getResource(fileProp);
        Property newProperty = (Property) EcoreUtil.getObjectByType(propResource.getContents(),
                PropertiesPackage.eINSTANCE.getProperty());

        newProperty.setAuthor(property.getAuthor());
        newProperty.setLabel(dbConnItem.getConnection().getName());
        newProperty.setItem(item);

        item.setProperty(newProperty);

        propResource.getContents().clear();
        propResource.getContents().add(newProperty);
        propResource.getContents().add(item);
        propResource.getContents().add(item.getState());

        EMFUtil.saveResource(propResource);
    }

    /**
     * DOC xqliu Comment method "getIFile".
     * 
     * @param modelElement
     * @return
     */
    private IFile getIFile(ModelElement modelElement) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        String platformString = modelElement.eResource().getURI().toPlatformString(true);
        return root.getFile(new Path(platformString));
    }
}
