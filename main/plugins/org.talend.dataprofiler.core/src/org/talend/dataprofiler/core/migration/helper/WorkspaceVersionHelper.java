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
package org.talend.dataprofiler.core.migration.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.ProductVersion;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public final class WorkspaceVersionHelper {

    protected static Logger log = Logger.getLogger(WorkspaceVersionHelper.class);

    public static final String VERSION = "version"; //$NON-NLS-1$

    private WorkspaceVersionHelper() {

    }

    /**
     * DOC bZhou Comment method "getVersionFile".
     * 
     * @return
     */
    public static IFile getVersionFile() {
        return ResourceManager.getLibrariesFolder().getFile(PluginConstant.VERSION_FILE_PATH);
    }

    /**
     * 
     * MOD mzhao Get version file by static way, not by IFile. See feature 6066
     * 
     * @return
     */
    public static ProductVersion getVesion() {
        return getVesion(getVersionFile());
    }

    /**
     * DOC bZhou Get version file by IFile.
     * 
     * @param versionFile
     * @return
     */
    public static ProductVersion getVesion(IFile versionFile) {
        return getVesion(versionFile.getLocation());
    }

    /**
     * DOC bZhou Comment method "getVesion".
     * 
     * @param versionFile
     * @return
     */
    public static ProductVersion getVesion(IPath versionPath) {

        File versionFile = versionPath == null ? null : versionPath.toFile();

        ProductVersion pVersion = null;
        try {
            if (versionFile != null && versionFile.exists()) {
                FileInputStream inStream = new FileInputStream(versionFile);

                Properties pros = new Properties();
                pros.load(inStream);

                String version = pros.getProperty(VERSION);
                if (version != null && !"".equals(version)) { //$NON-NLS-1$
                    pVersion = ProductVersion.fromString(version);
                }

                inStream.close();
            } else {
                pVersion = new ProductVersion(0, 0, 0);
            }
        } catch (Exception e) {
            log.error(e, e);
        }

        return pVersion;
    }

    /**
     * 
     * MOD mzhao 2009-04-03.
     */
    public static void storeVersion(File file) {
        Properties pros = new Properties();
        pros.setProperty(VERSION, CorePlugin.getDefault().getProductVersion().toString());
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            pros.store(outStream, null);
            outStream.close();
        } catch (FileNotFoundException e) {
            log.error(e, e);
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    /**
     * 
     * MOD mzhao 2009-04-03.
     */
    public static void storeVersion() {
        storeVersion(getVersionFile().getLocation().toFile());
    }
}
