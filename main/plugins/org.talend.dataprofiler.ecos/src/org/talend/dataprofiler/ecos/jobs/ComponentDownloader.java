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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Download components from web site.
 */
public class ComponentDownloader {

    protected static Logger log = Logger.getLogger(ComponentDownloader.class);

    private List<DownloadListener> fListeners = new ArrayList<DownloadListener>();

    private boolean fCancel = false;

    private static final int BUFFER_SIZE = 8192;

    /**
     * Download file from specific url to local folder.
     * 
     * @param componentUrl The file url to download
     * @param targetFolder Local folder to store downloaded file
     * @throws Exception
     */
    public void download(String componentUrl, String targetFolder) throws Exception {
        String fileName = componentUrl.substring(componentUrl.lastIndexOf('/'));
        File destination = new File(targetFolder + fileName);
        URL url = new URL(componentUrl);

        download(url, destination);
    }

    /**
     * Download file from specific url.
     * 
     * @param componentUrl The file url to download
     * @param destination The local file to be saved
     * @throws Exception
     */
    public void download(URL componentUrl, File destination) throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {

            URLConnection connection = componentUrl.openConnection();

            bis = new BufferedInputStream(connection.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(destination));
            fireDownloadStart(connection.getContentLength());

            byte[] buf = new byte[BUFFER_SIZE];
            int bytesDownloaded = 0;
            int bytesRead = -1;
            while ((bytesRead = bis.read(buf)) != -1) {
                bos.write(buf, 0, bytesRead);
                bytesDownloaded += bytesRead;

                fireDownloadProgress(bytesRead);
                if (fCancel) {
                    // cacel download process
                    return;
                }
            }

            bos.flush();
            // System.out.println(bytesDownloaded);
            fireDownloadComplete();
        } finally {
            Exception e = null;
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    e = ioe;
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioe) {
                    e = ioe;
                }
            }
            if (e != null) {
                // rethrow the exception to caller
                throw e;
            }
        }
    }

    /**
     * Return true if the user cancel download process.
     * 
     * @return the cancel
     */
    public boolean isCancel() {
        return fCancel;
    }

    /**
     * Set true if the user cacel download process.
     * 
     * @param cancel the cancel to set
     */
    public void setCancel(boolean cancel) {
        fCancel = cancel;
    }

    /**
     * Notify listeners about progress.
     * 
     * @param bytesRead
     */
    private void fireDownloadProgress(int bytesRead) {
        for (DownloadListener listener : fListeners) {
            listener.downloadProgress(this, bytesRead);
        }
    }

    /**
     * Notify listeners at the end of download process.
     */
    private void fireDownloadComplete() {
        for (DownloadListener listener : fListeners) {
            listener.downloadComplete();
        }
    }

    /**
     * Notify listeners at the begining of download process.
     */
    private void fireDownloadStart(int contentLength) {
        for (DownloadListener listener : fListeners) {
            listener.downloadStart(contentLength);
        }

    }

    /**
     * Add listener to observe the download process.
     * 
     * @param listener
     */
    public void addDownloadListener(DownloadListener listener) {
        fListeners.add(listener);
    }

    public void removeDownloadListener(DownloadListener listener) {
        fListeners.remove(listener);
    }

    public static void main(String[] args) {
        try {
            new ComponentDownloader().download("http://talendforge.org/ext/upload/extension-33/revision-45/tFileOutputPDF.zip", //$NON-NLS-1$
                    "d:/temp"); //$NON-NLS-1$
        } catch (Exception e) {
            log.error(e, e);
        }
    }

}
