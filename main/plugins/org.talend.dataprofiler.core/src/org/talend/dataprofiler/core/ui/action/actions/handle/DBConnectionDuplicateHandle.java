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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.CWMPlugin;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.writer.AElementPersistance;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;

/**
 * duplicate a connection
 */
public class DBConnectionDuplicateHandle extends MetadataDuplicateHandle {

    private final Logger LOG = Logger.getLogger(DBConnectionDuplicateHandle.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.actions.handle.IDuplicateHandle#duplicate(java.lang.String)
     */
    public ModelElement duplicateModelElement(ModelElement oldModelElement, String newName) {
        ModelElement duplicateModelElement = super.duplicateModelElement(oldModelElement, newName);
        // need to update the label
        ((AbstractMetadataObject) duplicateModelElement).setLabel(newName);

        // duplicate all packages
        DatabaseConnection dbcon = (DatabaseConnection) oldModelElement;
        EList<Package> dataPackages = dbcon.getDataPackage();
        if (dataPackages != null) {
            for (Package oldDataPackage : dataPackages) {
                EList<Package> newDataPackages = ((DatabaseConnection) duplicateModelElement).getDataPackage();
                Package copyEObject = (Package) EMFSharedResources.getInstance().copyEObject(oldDataPackage);
                newDataPackages.add(copyEObject);
            }
        }

        return duplicateModelElement;
    }

    @Override
    public Item duplicateItem(Item oldItem, String newName) throws BusinessException {
        ModelElement oldModelElement = PropertyHelper.getModelElement(oldItem.getProperty());

        // create the related item and save
        AElementPersistance elementWriter = ElementWriterFactory.getInstance().create(oldModelElement);
        ModelElement newModelElement = duplicateModelElement(oldModelElement, newName);
        IFolder folder = extractFolder(oldItem, oldModelElement);
        IPath itemPath = folder.getFullPath().makeRelativeTo(ResourceManager.getConnectionFolder().getFullPath());
        Property property = elementWriter.initProperty(newModelElement);
        DatabaseConnectionItem newItem = (DatabaseConnectionItem) property.getItem();
        // must set TypeName at here,or else, TypeName will be lost.
        newItem.setTypeName(((DatabaseConnectionItem) oldItem).getTypeName());
        try {
            ProxyRepositoryFactory.getInstance().create(newItem, itemPath);
        } catch (Exception e) {
            createBusinessException(DefaultMessagesImpl.getString("ModelElementDuplicateHandle.duplicateFail",
                    oldModelElement.getName(), e.getMessage()));
        }
        if (newItem != null) {
            CWMPlugin.getDefault().addConnetionAliasToSQLPlugin(newItem.getConnection());
        }
        return newItem;
    }
}
