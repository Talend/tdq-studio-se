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
package org.talend.dataprofiler.core.ui.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class DqFileUtils {

    /**
     * DOC bZhou FileUtils constructor comment.
     */
    private DqFileUtils() {

    }

    /**
     * DOC bZhou Comment method "getFile".
     * 
     * @param parent
     * @param fileName
     * @return
     */
    public static File getFile(File parent, String fileName) {
        List<File> allFiles = new ArrayList<File>();
        searchAllFile(allFiles, parent, true, false);

        for (File file : allFiles) {
            if (StringUtils.equals(fileName, file.getName())) {
                return file;
            }
        }

        return null;
    }

    public static File getFile(File parent, String fileName, boolean withFolder) {
        List<File> allFiles = new ArrayList<File>();
        searchAllFile(allFiles, parent, true, withFolder);

        for (File file : allFiles) {
            if (StringUtils.equals(fileName, file.getName())) {
                return file;
            }
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "existFile".
     * 
     * @param parent
     * @param fileName
     * @return
     */
    public static boolean existFile(File parent, String fileName) {
        File file = getFile(parent, fileName);
        return file != null && file.exists();
    }

    /**
     * DOC bZhou Comment method "existFile".
     * 
     * @param parent
     * @param targetFile
     * @return
     */
    public static boolean existFile(File parent, File targetFile) {
        return existFile(parent, targetFile.getName());
    }

    /**
     * DOC bZhou Comment method "existFile".
     * 
     * @param parent
     * @param targetPath
     * @return
     */
    public static boolean existFile(File parent, IPath targetPath) {
        return existFile(parent, targetPath.toFile());
    }

    /**
     * DOC bZhou Comment method "searchAllFile".
     * 
     * @param result
     * @param parent
     * @param recursive
     * @param withFolder
     */
    public static void searchAllFile(List<File> result, File parent, boolean recursive, boolean withFolder) {
        File[] files = parent.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && recursive) {
                    if (withFolder) {
                        result.add(file);
                    }
                    searchAllFile(result, file, recursive, withFolder);
                } else {
                    result.add(file);
                }
            }
        } else {
            result.add(parent);
        }
    }

    /**
     * DOC bZhou Comment method "searchAllFileInPlugin".
     * 
     * @param result
     * @param plugin
     * @param srcPath
     * @param recurse
     * @param suffixs
     * @throws IOException
     */
    public static void searchAllFileInPlugin(List<File> result, Plugin plugin, String srcPath, boolean recurse, String[] suffixs)
            throws IOException {

        Enumeration<String> paths = plugin.getBundle().getEntryPaths(srcPath);

        if (paths == null) {
            return;
        }

        while (paths.hasMoreElements()) {
            String nextElement = paths.nextElement();
            String currentPath = "/" + nextElement; //$NON-NLS-1$
            URL resourceURL = plugin.getBundle().getEntry(currentPath);

            URL fileURL = FileLocator.toFileURL(resourceURL);
            File file = new File(fileURL.getFile());

            if (file.isDirectory() && recurse) {
                if (!file.getName().startsWith(".")) { //$NON-NLS-1$
                    searchAllFileInPlugin(result, plugin, currentPath, recurse, suffixs);
                }
                continue;
            }

            for (String suffix : suffixs) {
                if (suffix == null || file.getName().endsWith(suffix)) {
                    result.add(file);
                }
            }
        }
    }

    /**
     * check whether the file is the local project file.(for example: we can use to check the dependency file when export)
     * 
     * @param file
     * @return boolean
     */
    public static boolean isLocalProjectFile(File file) {
        return file.getAbsolutePath().startsWith(ResourceManager.getRootProject().getLocation().toOSString());
    }

    /**
     * check if the DQ file under the basePath.(for example: we can use to check the dependency file when import)
     * 
     * @param file
     * @param basePath
     * @return boolean
     */
    public static boolean isFileUnderBasePath(File file, IPath basePath) {
        Path dependencyFilePath = new Path(file.getPath());
        String makeRelativeTo = dependencyFilePath.makeRelativeTo(basePath).segment(0);
        if (makeRelativeTo.equals(EResourceConstant.DATA_PROFILING.getName())
                || makeRelativeTo.equals(EResourceConstant.LIBRARIES.getName())
                || makeRelativeTo.equals(EResourceConstant.METADATA.getName())) {
            return true;
        }
        return false;
    }

}
