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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.BusinessException;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.dq.CWMPlugin;
import org.talend.dq.writer.EMFSharedResources;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;

/**
 * duplicate a connection
 */
public class DBConnectionDuplicateHandle extends MetadataDuplicateHandle {

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

    public Item duplicateItem(Item oldItem, String newName) throws BusinessException {
        Item duplicateItem = super.duplicateItem(oldItem, newName);
        if (duplicateItem != null) {
            CWMPlugin.getDefault().addConnetionAliasToSQLPlugin(((DatabaseConnectionItem) duplicateItem).getConnection());
        }
        return duplicateItem;
    }
}
