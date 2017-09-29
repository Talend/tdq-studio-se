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
package org.talend.cwm.management.api;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.talend.dataquality.PluginConstant;

/**
 * @author scorreia
 * 
 * A simple container for a folder.
 */
public class FolderProvider {

    private File folder;

    private IFolder folderResource;

    /**
     * Getter for folder.
     * 
     * @return the folder
     */
    public File getFolder() {
        return this.folder;
    }

    /**
     * Sets the folder.
     * 
     * @param folder the folder to set
     */
    public void setFolder(File folder) {
        this.folder = folder;
    }

    /**
     * Getter for folderResource.
     * 
     * @return the folderResource
     */
    public IFolder getFolderResource() {
        return folderResource;
    }

    /**
     * Sets the folderResource.
     * 
     * @param folderResource the folderResource to set
     */
    public void setFolderResource(IFolder folderResource) {
        this.folderResource = folderResource;
        this.folder = folderResource.getLocation().toFile();
    }

    public String getFolderURI() {
        if (this.folderResource != null) {
            return this.folderResource.getFullPath().toString();
        }

        return PluginConstant.EMPTY_STRING;
    }

    public boolean isNull() {
        return this.folderResource == null;
    }
}
