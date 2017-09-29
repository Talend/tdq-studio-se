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
package org.talend.commons.utils;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public final class WorkspaceUtils {

    private static final String RESOURCE = "resource"; //$NON-NLS-1$

    private static final String PLATFORM = "platform"; //$NON-NLS-1$

    public static String SQL_EXTENSION = "sql"; //$NON-NLS-1$

    public static final String NULL_FIELD = "<null>"; //$NON-NLS-1$

    public static final String DEFAULT_VERSION = "0.1"; //$NON-NLS-1$

    /**
     * @Deprecated use {@link #org.talend.repository.model.RepositoryConstants.ITEM_FORBIDDEN_IN_LABEL}
     */
    @Deprecated
    public static final String[] ITEM_FORBIDDEN_IN_LABEL = { "~", "!", "`", "#", "^", "&", "*", "\\", "/", "?", ":", ";", "\"", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
            ".", "(", ")", "，", "。", "'", "￥", "‘", "”", "、", "《", "，", "》", "<", ">", " " }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$

    private WorkspaceUtils() {
    }

    public static File ifolderToFile(IFolder ifolder) {
        IPath location = ifolder.getLocation() == null ? ResourceManager.getRootProject().getLocation()
                .append(ifolder.getFullPath()) : ifolder.getLocation();
        return location.toFile();
    }

    public static File ifileToFile(IFile ifile) {
        IPath location = ifile.getLocation() == null ? ResourceManager.getRootProject().getLocation().append(ifile.getFullPath())
                : ifile.getLocation();
        return location.toFile();
    }

    public static IFile fileToIFile(File file) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IPath location = Path.fromOSString(file.getAbsolutePath());
        IFile ifile = workspace.getRoot().getFileForLocation(location);
        if (ifile == null) {
            ifile = workspace.getRoot().getFile(location);
        }
        return ifile;
    }

    public static IFolder fileToIFolder(File file) {
        IFolder folder = null;
        String filePath = file.getAbsolutePath();
        String rootPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
        if (filePath.startsWith(rootPath)) {
            folder = ResourcesPlugin.getWorkspace().getRoot()
                    .getFolder(new Path(filePath.substring(rootPath.length(), filePath.length())));
        }
        return folder;
    }

    /**
     * 
     * Comment method "toFile".
     * 
     * @param object
     * @return turn URI to File
     */
    public static String toFile(Object object) {
        if (object instanceof URI) {
            URI uri = ((URI) object);
            if (uri.isFile()) {
                return uri.toFileString();
            } else if (uri.isPlatform()) {
                return ResourceManager.getRootFolderLocation().append(uri.toPlatformString(true)).toOSString();
            }
        }
        return null;
    }

    /**
     * 
     * DOC mzhao convert emf resource to workspace resource.
     * 
     * @param me ,modelElement of EObject
     * @return File this element links.
     */
    public static IFile getModelElementResource(ModelElement me) {
        URI uri = me.eResource().getURI();
        uri = me.eResource().getResourceSet().getURIConverter().normalize(uri);
        return getModelElementResource(uri);
    }

    /**
     * 
     * convert emf resource to workspace resource.
     * 
     * @param uri ,URI of EObject
     * @return File this element links.
     */
    public static IFile getModelElementResource(URI uri) {
        IFile resourceFile = null;
        String scheme = uri.scheme();
        if (PLATFORM.equals(scheme) && uri.segmentCount() > 1 && RESOURCE.equals(uri.segment(0))) {
            StringBuffer platformResourcePath = new StringBuffer();
            for (int j = 1, size = uri.segmentCount(); j < size; ++j) {
                platformResourcePath.append('/');
                platformResourcePath.append(uri.segment(j));
            }
            resourceFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformResourcePath.toString()));
        }
        return resourceFile;
    }

    /**
     * make the pathName to normal(replace the special forbidden chars to "_").
     * 
     * @param pathName
     * @return
     */
    public static String normalize(String pathName) {
        if (pathName == null) {
            return pathName;
        }
        String label = pathName;
        for (String toReplace : ITEM_FORBIDDEN_IN_LABEL) {
            label = label.replace(toReplace, "_"); //$NON-NLS-1$
        }
        return label;

    }

    public static IFile getFile(URI uri) {
        IPath path = convert(uri);
        if (path != null) {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        }
        return null;
    }

    public static IPath convert(URI uri) {
        if (PLATFORM.equals(uri.scheme()) && uri.segmentCount() > 1 && RESOURCE.equals(uri.segment(0))) {
            StringBuffer platformResourcePath = new StringBuffer();
            for (int i = 1, size = uri.segmentCount(); i < size; ++i) {
                platformResourcePath.append('/');
                platformResourcePath.append(URI.decode(uri.segment(i)));
            }

            return new Path(platformResourcePath.toString());
        }

        return null;
    }
}
