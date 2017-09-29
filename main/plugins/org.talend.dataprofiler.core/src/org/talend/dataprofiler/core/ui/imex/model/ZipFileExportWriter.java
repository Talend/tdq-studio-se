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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ZipFileExportWriter extends FileSystemExportWriter {

    private String fileExtension;

    private ZipOutputStream outputStream;

    private Map<File, String> tempMap = new HashMap<File, String>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.FileSystemExportWriter#setBasePath(org.eclipse.core.runtime.IPath)
     */
    @Override
    public void setBasePath(IPath path) {
        this.fileExtension = path.getFileExtension();
        super.setBasePath(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.FileSystemExportWriter#write(org.eclipse.core.runtime.IPath,
     * org.eclipse.core.runtime.IPath)
     */
    @Override
    public void write(IPath resPath, IPath desPath) throws IOException, CoreException {

        File file = getBasePath().toFile();

        if (!file.exists()) {
            file.createNewFile();
        }

        tempMap.put(resPath.toFile(), desPath.toPortableString());

        // File file = getBasePath().toFile();
        // if (!file.exists()) {
        // // remove first '/'
        // desPath = desPath.makeRelative();
        //
        // byte[] readBuffer = new byte[4096];
        //
        // if (outputStream == null) {
        // outputStream = new ZipOutputStream(new FileOutputStream(file));
        // }
        //
        // ZipEntry newEntry = new ZipEntry(desPath.toString());
        //
        // outputStream.putNextEntry(newEntry);
        // InputStream contentStream = new FileInputStream(resPath.toFile());
        // try {
        // int n;
        // while ((n = contentStream.read(readBuffer)) > 0) {
        // outputStream.write(readBuffer, 0, n);
        // }
        // } finally {
        // if (contentStream != null) {
        // contentStream.close();
        // }
        // }
        //
        // outputStream.closeEntry();
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.FileSystemExportWriter#finish(org.talend.dataprofiler.core.ui.imex
     * .model.ItemRecord[])
     */
    @Override
    public void finish(ItemRecord[] records) throws IOException, CoreException {
        super.finish(records);

        addFilesToExistingZip(getBasePath().toFile(), tempMap);
        tempMap.clear();
        if (outputStream != null) {
            outputStream.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.FileSystemExportWriter#check()
     */
    @Override
    public List<String> check() {
        List<String> errors = new ArrayList<String>();

        ReturnCode rc = new ReturnCode("The root file extension is invalid!", false);//$NON-NLS-1$

        if (fileExtension != null) {
            String[] validExtensions = new String[] { "zip", "tar", "tar.gz" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            for (String ext : validExtensions) {
                if (StringUtils.equalsIgnoreCase(fileExtension, ext)) {
                    rc.setOk(true);
                    break;
                }
            }
        }

        if (!rc.isOk()) {
            errors.add(rc.getMessage());
        }

        return errors;
    }

    /**
     * DOC bZhou Comment method "addFilesToExistingZip".
     * 
     * @param zipFile
     * @param files
     * @throws IOException
     */
    public static void addFilesToExistingZip(File zipFile, Map<File, String> fileMap) throws IOException {
        // get a temp file
        File tempFile = File.createTempFile(zipFile.getName(), null);
        // delete it, otherwise you cannot rename your existing zip to it.
        tempFile.delete();
        // MOD klliu bug TDQ-1691 2011-09-09
        FileUtils.copyFile(zipFile, tempFile);
        // renameTo works on windows, don't work on linux
        // boolean renameOk = zipFile.renameTo(tempFile);
        // if (!renameOk) {
        //            throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to "//$NON-NLS-1$ //$NON-NLS-2$
        // + tempFile.getAbsolutePath());
        // }
        // ~
        byte[] buf = new byte[4096];

        ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String name = entry.getName();

            boolean notInFiles = true;
            for (File f : fileMap.keySet()) {
                if (f.getName().equals(new Path(name).lastSegment())) {
                    notInFiles = false;
                    break;
                }
            }
            if (notInFiles) {
                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(name));
                // Transfer bytes from the ZIP file to the output file
                int len;
                while ((len = zin.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            entry = zin.getNextEntry();
        }
        // Close the streams
        zin.close();
        // Compress the files
        for (File f : fileMap.keySet()) {
            InputStream in = new FileInputStream(f);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(fileMap.get(f)));
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
        }
        // Complete the ZIP file
        out.close();
        tempFile.delete();
    }

}
