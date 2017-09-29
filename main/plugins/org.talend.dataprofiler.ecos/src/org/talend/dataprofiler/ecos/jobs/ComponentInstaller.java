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
package org.talend.dataprofiler.ecos.jobs;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.talend.utils.io.FilesUtils;

/**
 * Install component to tos.
 */
public class ComponentInstaller {

    /**
     * Unzip the component file to user folder.
     * 
     * @param zipFile The component zip file
     * @param targetFolder The user folder
     * @return
     * @throws Exception
     */
    public static File unzip(String zipFile, String targetFolder) throws Exception {

        ZipFile zip = new ZipFile(zipFile);

        // folder that contains all the unzipped files
        File rootFolder = getRootFolder(zip, targetFolder);

        if (rootFolder == null) {
            // the zip does not have any directory, fix it
            String fileName = zip.getName().substring(zip.getName().lastIndexOf(File.separatorChar) + 1);
            fileName = fileName.substring(0, fileName.lastIndexOf('.')); // remove extension
            rootFolder = new File(targetFolder, fileName);
            targetFolder = targetFolder + File.separatorChar + fileName;
        }

        if (rootFolder.exists()) {
            // we have installed older revision, delete it
            FileUtils.deleteDirectory(rootFolder);
        }
        rootFolder.mkdir();
        // move some common use codes for unzipping file to FilesUtils
        FilesUtils.unzip(zipFile, targetFolder);

        return rootFolder;
    }

    /**
     * Get the root folder that contains all the unzipped files
     * 
     * @param zip The component zip file
     * @param targetFolder The user folder
     * @return
     */
    @SuppressWarnings("unchecked")
    private static File getRootFolder(ZipFile zip, String targetFolder) {
        File rootFolder = null;
        Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zip.entries();
        String directory = null;
        while (enumeration.hasMoreElements()) {
            ZipEntry entry = enumeration.nextElement();
            String entryName = entry.getName();
            File file = new File(targetFolder, entryName);
            if (entry.isDirectory()) {
                // assumes that the first folder is root folder
                if (rootFolder == null) {
                    rootFolder = file;
                    break;
                }

            }
            // some zip file does not have a directory entry, see bug 0005472: [ecosystem view] cannot install
            // tFileDelimitedSplit from ecosystem
            if (directory == null) {
                int pos = entryName.indexOf("/"); //$NON-NLS-1$
                if (pos > -1) {
                    directory = entryName.substring(0, pos);
                } else {
                    pos = entryName.indexOf("\\"); //$NON-NLS-1$
                    if (pos > -1) {
                        directory = entryName.substring(0, pos);
                    }
                }
            }
        }
        if (rootFolder == null && directory != null) {
            rootFolder = new File(targetFolder, directory);
        }

        return rootFolder;
    }

}
