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
package org.talend.dq.helper.resourcehelper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch;
import org.talend.resource.ResourceManager;

/**
 * This class help the '.prv' file to store the corresponding DataProvider value.
 * 
 */
public final class PrvResourceFileHelper extends ResourceFileMap<Connection> {

    private static PrvResourceFileHelper instance;

    ConnectionSwitch<Connection> connectionSwitch = new ConnectionSwitch<Connection>() {

        @Override
        public Connection caseConnection(Connection object) {
            return object;
        };
    };

    private PrvResourceFileHelper() {
        super();
    }

    public static PrvResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new PrvResourceFileHelper();
        }
        return instance;
    }

    /**
     * Method "readFromFile".
     * 
     * @param file the file to read
     * @return the Data provider if found.
     */
    public Connection findProvider(IFile file) {
        if (checkFile(file)) {
            return getModelElement(file);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#checkFile(org.eclipse.core.resources.IFile)
     */
    @Override
    protected boolean checkFile(IFile file) {
        if (file == null) {
            return false;
        }
        String fileExtension = file.getFileExtension();
        return file != null && (FactoriesUtil.isProvFile(fileExtension) || FactoriesUtil.isItemFile(fileExtension));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#getTypedFolder()
     */
    @Override
    public IFolder getTypedFolder() {
        return ResourceManager.getMetadataFolder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.resourcehelper.ResourceFileMap#doSwitch(org.eclipse.emf.ecore.EObject)
     */
    @Override
    public Connection doSwitch(EObject object) {
        return connectionSwitch.doSwitch(object);
    }
}
