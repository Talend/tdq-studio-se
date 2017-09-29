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
package org.talend.dataquality.indicators.mapdb;

/**
 * created by talend on Jul 15, 2014 Detailled comment
 * 
 */
public class DBMapParameter {

    private int limitSize = 2;// 2 mean the limit is 2 GB

    private boolean mmapFileEnablePartial = false;// whether use memory mapping file.warring that With this mode you
                                                  // will

    // experience

    // `java.lang.OutOfMemoryError: Map failed` exceptions on 32bit JVMs eventually

    private boolean deleteFilesAfterClose = true;// whether will delete temp db file after close db.

    private int cacheSize = 12 * 1024;// the size of cache default is 12*1024

    private boolean asyncWriteEnable = true;// Enables mode where all modifications are queued and written into disk on

    // Background Writer Thread.

    private boolean closeOnJvmShutdown = true;// Adds JVM shutdown hook and closes DB just before JVM.

    public DBMapParameter(int limitSize, boolean mmapFileEnablePartial, boolean deleteFilesAfterClose, int cacheSize,
            boolean asyncWriteEnable, boolean closeOnJvmShutdown) {

        this.limitSize = limitSize;
        this.mmapFileEnablePartial = mmapFileEnablePartial;
        this.deleteFilesAfterClose = deleteFilesAfterClose;
        this.cacheSize = cacheSize;
        this.asyncWriteEnable = asyncWriteEnable;
        this.closeOnJvmShutdown = closeOnJvmShutdown;
    }

    public DBMapParameter() {

    }

    /**
     * Getter for limitSize.
     * 
     * @return the limitSize
     */
    public int getLimitSize() {
        return this.limitSize;
    }

    /**
     * Sets the limitSize.
     * 
     * @param limitSize the limitSize to set
     */
    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * Getter for mmapFileEnablePartial.
     * 
     * @return the mmapFileEnablePartial
     */
    public boolean isMmapFileEnablePartial() {
        return this.mmapFileEnablePartial;
    }

    /**
     * Sets the mmapFileEnablePartial.
     * 
     * @param mmapFileEnablePartial the mmapFileEnablePartial to set
     */
    public void setMmapFileEnablePartial(boolean mmapFileEnablePartial) {
        this.mmapFileEnablePartial = mmapFileEnablePartial;
    }

    /**
     * Getter for deleteFilesAfterClose.
     * 
     * @return the deleteFilesAfterClose
     */
    public boolean isDeleteFilesAfterClose() {
        return this.deleteFilesAfterClose;
    }

    /**
     * Sets the deleteFilesAfterClose.
     * 
     * @param deleteFilesAfterClose the deleteFilesAfterClose to set
     */
    public void setDeleteFilesAfterClose(boolean deleteFilesAfterClose) {
        this.deleteFilesAfterClose = deleteFilesAfterClose;
    }

    /**
     * Getter for cacheSize.
     * 
     * @return the cacheSize
     */
    public int getCacheSize() {
        return this.cacheSize;
    }

    /**
     * Sets the cacheSize.
     * 
     * @param cacheSize the cacheSize to set
     */
    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    /**
     * Getter for asyncWriteEnable.
     * 
     * @return the asyncWriteEnable
     */
    public boolean isAsyncWriteEnable() {
        return this.asyncWriteEnable;
    }

    /**
     * Sets the asyncWriteEnable.
     * 
     * @param asyncWriteEnable the asyncWriteEnable to set
     */
    public void setAsyncWriteEnable(boolean asyncWriteEnable) {
        this.asyncWriteEnable = asyncWriteEnable;
    }

    /**
     * Getter for closeOnJvmShutdown.
     * 
     * @return the closeOnJvmShutdown
     */
    public boolean isCloseOnJvmShutdown() {
        return this.closeOnJvmShutdown;
    }

    /**
     * Sets the closeOnJvmShutdown.
     * 
     * @param closeOnJvmShutdown the closeOnJvmShutdown to set
     */
    public void setCloseOnJvmShutdown(boolean closeOnJvmShutdown) {
        this.closeOnJvmShutdown = closeOnJvmShutdown;
    }

}
