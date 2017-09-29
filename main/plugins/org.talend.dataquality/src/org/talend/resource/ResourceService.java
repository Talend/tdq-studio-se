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
package org.talend.resource;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class ResourceService {

    private static Logger log = Logger.getLogger(ResourceService.class);

    private ResourceService() {

    }

    /**
     * DOC bZhou Comment method "setReadOnlyProperty".
     * 
     * @param resource
     * @throws CoreException
     */
    public static void setReadOnlyProperty(IResource resource) throws CoreException {
        assert resource != null;

        String property = resource.getPersistentProperty(ResourceConstant.READONLY);
        if (property == null) {
            resource.setPersistentProperty(ResourceConstant.READONLY, ResourceConstant.READONLY_PROPERTY);
        }
    }

    /**
     * DOC bZhou Comment method "isReadOnlyFolder".
     * 
     * @param resource
     * @return
     * @throws CoreException
     */
    public static boolean isReadOnlyFolder(IResource resource) {
        assert resource != null;

        try {
            String property = resource.getPersistentProperty(ResourceConstant.READONLY);
            return StringUtils.equals(property, ResourceConstant.READONLY_PROPERTY);
        } catch (Exception e) {
            log.error(e, e);
        }

        return false;
    }

    /**
     * DOC bZhou Comment method "setNoSubFolderProperty".
     * 
     * @param resource
     * @throws CoreException
     */
    public static void setNoSubFolderProperty(IResource resource) throws CoreException {
        assert resource != null;

        String property = resource.getPersistentProperty(ResourceConstant.NO_SUBFOLDER);
        if (property == null) {
            resource.setPersistentProperty(ResourceConstant.NO_SUBFOLDER, ResourceConstant.NO_SUBFOLDER_PROPERTY);
        }
    }

    /**
     * DOC bZhou Comment method "isNoSubFolder".
     * 
     * @param resource
     * @return
     * @throws CoreException
     */
    public static boolean isNoSubFolder(IResource resource) {
        assert resource != null;

        try {
            String property = resource.getPersistentProperty(ResourceConstant.NO_SUBFOLDER);
            return StringUtils.equals(property, ResourceConstant.NO_SUBFOLDER_PROPERTY);
        } catch (Exception e) {
            log.error(e, e);
        }

        return false;
    }

    /**
     * DOC bZhou Comment method "refreshStructure".
     */
    public static void refreshStructure() {
        refreshStructure(ResourceManager.getRootProject());
    }

    /**
     * DOC bZhou Comment method "refreshStructure".
     */
    public static void refreshStructure(IResource resource) {
        try {
            resource.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (CoreException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * DOC bZhou Comment method "isSubFolder".
     * 
     * @param parentFolder
     * @param subFolder
     * @return
     */
    public static boolean isSubFolder(IFolder parentFolder, IFolder subFolder) {
        assert parentFolder != null;
        assert subFolder != null;

        return parentFolder.getFullPath().isPrefixOf(subFolder.getFullPath());
    }

    /**
     * DOC xqliu Comment method "isSubFolder".
     * 
     * @param parentPath
     * @param subPath
     * @return
     */
    public static boolean isSubFolder(IPath parentPath, IPath subPath) {
        return isSubFolder(parentPath, subPath, true);
    }

    /**
     * DOC xqliu Comment method "isSubFolder".
     * 
     * @param parentPath
     * @param subPath
     * @param true2SamePath
     * @return
     */
    public static boolean isSubFolder(IPath parentPath, IPath subPath, boolean true2SamePath) {
        assert parentPath != null;
        assert subPath != null;

        if (parentPath.toOSString().equals(subPath.toOSString())) {
            return true2SamePath;
        }

        return parentPath.isPrefixOf(subPath);
    }

    /**
     * DOC bZhou Comment method "isSubFolder".
     * 
     * @param parentFolder
     * @param subFolders
     * @return
     */
    public static boolean isSubFolder(IFolder parentFolder, IFolder... subFolders) {
        assert parentFolder != null;
        assert subFolders != null;

        for (IFolder subFolder : subFolders) {
            if (!isSubFolder(parentFolder, subFolder)) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOC bZhou Comment method "checkResource".
     * 
     * @return
     */
    public static boolean checkResource() {
        // MOD remove the adjust about whether roorProject is exist.
        return ResourceManager.getDataProfilingFolder().exists()
                && ResourceManager.getLibrariesFolder().exists()
                && (ResourceManager.getMetadataFolder().exists() || ResourceManager.getRootProject().getFolder("TDQ_Metadata")
                        .exists());//$NON-NLS-1$
    }

    /**
     * DOC bZhou Comment method "checkResource".
     * 
     * @return
     */
    public static boolean checkSecludedResource() {

        String dpProject = "Data Profiling";//$NON-NLS-1$
        String lbProject = "Libraries";//$NON-NLS-1$
        String mtProject = "Metadata";//$NON-NLS-1$

        IWorkspaceRoot root = ResourceManager.getRoot();

        return root.getProject(dpProject).exists() && root.getProject(lbProject).exists() && root.getProject(mtProject).exists();
    }

    /**
     * DOC bzhou Comment method "initResourcePersistence".
     */
    public static ReturnCode initResourcePersistence() {
        ReturnCode rc = new ReturnCode();

        try {
            IPath[] allPathes = EResourceConstant.getPathes();
            if (allPathes != null) {
                for (IPath path : allPathes) {
                    IFolder folder = ResourceManager.getRootProject().getFolder(path);
                    if (folder.exists()) {
                        QualifiedName[] qualifications = EResourceConstant.findQualificationsByPath(path.toString());
                        for (QualifiedName qualification : qualifications) {
                            if (qualification == ResourceConstant.READONLY) {
                                setReadOnlyProperty(folder);
                            }

                            if (qualification == ResourceConstant.NO_SUBFOLDER) {
                                setNoSubFolderProperty(folder);
                            }
                        }
                    }
                }
            }

            rc.setOk(true);
        } catch (CoreException e) {
            rc.setOk(false);
            rc.setMessage(e.getMessage());
        }

        return rc;
    }

    /**
     * DOC bZhou Comment method "file2IFile".
     * 
     * @param file
     * @return
     */
    public static IFile file2IFile(File file) {
        if (file.isFile()) {
            IPath path = new Path(file.getAbsolutePath());
            path = path.makeRelativeTo(ResourceManager.getRootProject().getLocation());
            return ResourceManager.getRootProject().getFile(path);
        }

        return null;
    }
}
