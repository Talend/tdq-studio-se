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
package org.talend.dataprofiler.core.ui.imex.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.dataprofiler.core.ui.utils.DqFileUtils;
import org.talend.resource.EResourceConstant;
import org.talend.utils.io.FilesUtils;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ZipFileImportWriter extends FileSystemImportWriter {

    private IPath sourcePath;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#computeInput(org.eclipse.core.runtime.IPath)
     */
    @Override
    public ItemRecord computeInput(IPath path) {
        sourcePath = path.removeFileExtension();

        try {
            FilesUtils.createFolder(sourcePath.toFile());

            FilesUtils.unzip(path.toOSString(), sourcePath.toOSString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        File libFolder = DqFileUtils.getFile(sourcePath.toFile(), EResourceConstant.LIBRARIES.getName(), true);

        if (libFolder != null && libFolder.exists()) {
            IPath projectPath = new Path(libFolder.getParentFile().getAbsolutePath());
            return super.computeInput(projectPath);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#backUPWorksapce(org.eclipse.core.runtime.IPath)
     */
    @Override
    protected File backUPWorksapce(IPath workspacePath) {
        // no need to create a backup here because the folder is already created by the unzip action
        return workspacePath.toFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter#postFinish()
     */
    @Override
    public void postFinish() throws IOException {
        super.postFinish();
        if (sourcePath != null && sourcePath.toFile().exists()) {
            FilesUtils.removeFolder(sourcePath.toFile(), true);
        }
    }
}
