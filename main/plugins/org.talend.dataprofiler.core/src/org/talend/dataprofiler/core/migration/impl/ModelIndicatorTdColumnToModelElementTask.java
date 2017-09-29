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
package org.talend.dataprofiler.core.migration.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;

/**
 * 
 * DOC mzhao feature 17869, migration tasks for indicator model change (return value of getAnalyzedElement) from
 * TdColumn to ModelElement.
 */
public class ModelIndicatorTdColumnToModelElementTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(ModelIndicatorTdColumnToModelElementTask.class);

    // private TopDataqualityMigrationFrom410to420usingGenericVM indicatorModelMigration = null;

    private static final String MIGRATION_FILE_EXT = ".mig"; //$NON-NLS-1$

    private static final String MIGRATION_FOLDER_EXT = "_mig"; //$NON-NLS-1$

    private static final String TDQ_DATAPROFILING = "TDQ_Data Profiling"; //$NON-NLS-1$

    private static final String TO_BE_RPLACED_STRING_PREFIX = "<analyzedColumns"; //$NON-NLS-1$

    private static final String REPLACED_STRING = " xsi:type=\"relational:TdColumn\""; //$NON-NLS-1$

    public ModelIndicatorTdColumnToModelElementTask() {
    }

    public Date getOrder() {
        // Model migratin must be executed at the first place so that other tasks can correctly loading resource.
        return createDate(2010, 6, 23);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        // if (indicatorModelMigration == null) {
        // indicatorModelMigration = new TopDataqualityMigrationFrom410to420usingGenericVM();
        // }

        File rawFile = getWorkspacePath().append(TDQ_DATAPROFILING).toFile();
        if (rawFile.exists()) {
            File migFile = new Path(rawFile.getAbsolutePath()).addFileExtension(MIGRATION_FOLDER_EXT).toFile();
            rawFile.renameTo(migFile);
            migrate(migFile);
            migFile.renameTo(rawFile.getAbsoluteFile());
        }
        return Boolean.TRUE;
    }

    /**
     * 
     * DOC mzhao migrate each.
     */
    private void migrate(File migFolder) {
        ArrayList<File> fileList = new ArrayList<File>();
        getAllFilesFromFolder(migFolder, fileList, new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.endsWith(".ana")) { //$NON-NLS-1$
                    return true;
                }
                return false;
            }
        });

        int counter = 0;
        int errorCounter = 0;
        Throwable error = null;
        for (File srcFile : fileList) {
            log.info("-------------- Migrating (" + counter++ + ") : " + srcFile.getAbsolutePath()); //$NON-NLS-1$ //$NON-NLS-2$
            try {
                // String inURI = sample.toURI().toString();
                // String outURI = new File(sample.getAbsolutePath() + MIGRATION_FILE_EXT).toURI().toString();
                // indicatorModelMigration.migrate(inURI, outURI, new NullProgressMonitor());

                // ATL transformation give empty content so that I simply perform a string replacement.
                replace(srcFile);
            } catch (Throwable e) {

                errorCounter++;
                log.error("!!!!!!!!!!!  Error transforming (" + srcFile.getAbsolutePath() + ")\n" + e.getMessage(), e); //$NON-NLS-1$ //$NON-NLS-2$
            }
            log.info("-------------- Migration done of " + counter + " files" //$NON-NLS-1$ //$NON-NLS-2$
                    + (errorCounter != 0 ? (",  there are " + errorCounter + " files in error.") : ".")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        if (error == null) {
            // remove original files and rename new ones to old ones
            for (File sample : fileList) {
                boolean isDeleted = sample.delete();
                log.info(sample.getAbsolutePath() + (isDeleted ? " is deleted." : " failed to delete.")); //$NON-NLS-1$ //$NON-NLS-2$
                boolean isrenamed = new File(sample.getAbsolutePath() + MIGRATION_FILE_EXT).renameTo(sample); 
                log.info(sample.getAbsolutePath() + MIGRATION_FILE_EXT + (isrenamed ? " is renamed." : " failed to rename.")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    /**
     * 
     * DOC mzhao string replacement method.
     * 
     * @param srcFile
     * @return
     */
    private void replace(File srcFile) throws Throwable {
        File destFile = new File(srcFile.getAbsolutePath() + MIGRATION_FILE_EXT);

        BufferedReader fileReader = new BufferedReader(new FileReader(srcFile));
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(destFile));

        while (fileReader.ready()) {
            String line = fileReader.readLine();
            if (StringUtils.contains(line, TO_BE_RPLACED_STRING_PREFIX) && !StringUtils.contains(line, REPLACED_STRING)) {
                line = StringUtils.replace(line, TO_BE_RPLACED_STRING_PREFIX, TO_BE_RPLACED_STRING_PREFIX + REPLACED_STRING); 
                log.debug(line);
            }
            fileWriter.append(line);
            fileWriter.newLine();
        }
        fileWriter.flush();
        fileWriter.close();
        fileWriter = null;
        fileReader.close();
        fileReader = null;
    }

    @Override
    public Boolean isModelTask() {
        return Boolean.TRUE;
    }
}
