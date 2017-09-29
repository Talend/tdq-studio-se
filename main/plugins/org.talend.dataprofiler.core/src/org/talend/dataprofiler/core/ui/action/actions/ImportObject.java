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
package org.talend.dataprofiler.core.ui.action.actions;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.dataprofiler.ecos.jobs.ComponentInstaller;
import org.talend.dataprofiler.ecos.model.IEcosComponent;
import org.talend.dq.helper.FileUtils;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

/**
 * an object which can be import into TDQ, include an Object File (csv) and a Jar File list.
 */
public class ImportObject {

    protected static Logger log = Logger.getLogger(ImportObject.class);

    /**
     * create a ImportObject.
     * 
     * @param pObjfile
     * @param pJarfiles
     * @return
     */
    public static ImportObject createImportObject(File pObjfile, List<File> pJarfiles) {
        return new ImportObject(pObjfile, pJarfiles);
    }

    /**
     * build a ImportObject list.
     * 
     * @param componet
     * @param information
     * @return
     */
    public static List<ImportObject> extractImportObject(IEcosComponent componet, List<ReturnCode> information) {
        List<ImportObject> importObjects = new ArrayList<ImportObject>();
        try {
            String targetFolder = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
            File componentFileFolder = ComponentInstaller.unzip(componet.getInstalledLocation(), targetFolder);

            // get obj files(csv)
            List<File> objFiles = FilesUtils.getAllFilesFromFolder(componentFileFolder, new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return !FilesUtils.isSVNFolder(dir) && name.endsWith(FileUtils.CSV);
                }
            });
            if (objFiles.isEmpty()) {
                information.add(new ReturnCode("No valid exchange extension file(CSV) found in " + componet.getName(), false)); //$NON-NLS-1$
            } else {
                // get jar files
                List<File> jarFiles = FilesUtils.getAllFilesFromFolder(componentFileFolder, new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return !FilesUtils.isSVNFolder(dir) && name.endsWith("jar"); //$NON-NLS-1$
                    }
                });
                for (File file : objFiles) {
                    importObjects.add(ImportObject.createImportObject(file, jarFiles));
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return importObjects;
    }

    private File objFile;

    private List<File> jarFiles;

    private ImportObject(File pObjfile, List<File> pJarfiles) {
        this.setObjFile(pObjfile);
        this.setJarFiles(pJarfiles);
    }

    /**
     * copy jar file into TDQ_Libraries/Indicators/User Defined Indicators/lib.
     */
    public void copyJarFiles() {
        if (!this.getJarFiles().isEmpty()) {
            try {
                for (File file : this.getJarFiles()) {
                    FilesUtils.copyFile(file,
                            WorkspaceUtils.ifileToFile(ResourceManager.getUDIJarFolder().getFile(file.getName())));
                }
            } catch (IOException e) {
                log.warn(e, e);
            }
        }
    }

    public List<File> getJarFiles() {
        if (this.jarFiles == null) {
            this.jarFiles = new ArrayList<File>();
        }
        return this.jarFiles;
    }

    public File getObjFile() {
        return this.objFile;
    }

    private void setJarFiles(List<File> jarFiles) {
        this.jarFiles = jarFiles;
    }

    private void setObjFile(File objFile) {
        this.objFile = objFile;
    }
}
