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
package org.talend.dataprofiler.core.ui.imex.model;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.dataprofiler.core.ui.utils.DqFileUtils;
import org.talend.resource.EResourceConstant;
import org.talend.utils.io.FilesUtils;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ZipFileImportWriter extends FileSystemImportWriter {

    private static Logger log = Logger.getLogger(ZipFileImportWriter.class);

    private IPath sourcePath;

    private String tempFolderNameUuid = ""; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#computeInput(org.eclipse.core.runtime.IPath)
     */
    @Override
    public ItemRecord computeInput(IPath path) {
        // TDQ-15946 msjian: when reselect another import file, clean the before temp folder.
        deleteTempUnzipFolder();

        sourcePath = path.removeFileExtension();
        tempFolderNameUuid = EcoreUtil.generateUUID();

        try {
            FilesUtils.createFolder(getSourceFile());

            FilesUtils.unzip(path.toOSString(), getSourceFile().toString());
        } catch (Exception e) {
            log.error(e, e);
        }

        File libFolder = DqFileUtils.getFile(getSourceFile(), EResourceConstant.LIBRARIES.getName(), true);

        if (libFolder != null && libFolder.exists()) {
            IPath projectPath = new Path(libFolder.getParentFile().getAbsolutePath());
            return super.computeInput(projectPath);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#postFinish()
     */
    @Override
    public void postFinish() throws IOException {
        super.postFinish();
        deleteTempUnzipFolder();
    }

    /**
     * DOC msjian Comment method "deleteTempUnzipFolder".
     */
    private void deleteTempUnzipFolder() {
        if (sourcePath != null && getSourceFile().exists()) {
            FilesUtils.removeFolder(getSourceFile(), true);
        }
    }

    private File getSourceFile() {
        // TDQ-14949: fix cannot import the items when the source path like "XX .zip".
        return new File(sourcePath.toFile().getPath().trim() + tempFolderNameUuid);
    }
}
