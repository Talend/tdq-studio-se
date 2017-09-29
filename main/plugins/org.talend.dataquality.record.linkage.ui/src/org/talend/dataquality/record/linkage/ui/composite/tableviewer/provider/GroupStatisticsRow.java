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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider;

/**
 * created by zhao on Aug 19, 2013 Detailled comment
 * 
 */
public class GroupStatisticsRow {

    private Long groupSize;

    private Long groupCount;

    private Long recordCount;

    private String recordPercentage;

    /**
     * Getter for groupSize.
     * 
     * @return the groupSize
     */
    public Long getGroupSize() {
        return this.groupSize;
    }

    /**
     * Sets the groupSize.
     * 
     * @param groupSize the groupSize to set
     */
    public void setGroupSize(Long groupSize) {
        this.groupSize = groupSize;
    }

    /**
     * Getter for groupCount.
     * 
     * @return the groupCount
     */
    public Long getGroupCount() {
        return this.groupCount;
    }

    /**
     * Sets the groupCount.
     * 
     * @param groupCount the groupCount to set
     */
    public void setGroupCount(Long groupCount) {
        this.groupCount = groupCount;
    }

    /**
     * Getter for recordCount.
     * 
     * @return the recordCount
     */
    public Long getRecordCount() {
        return this.recordCount;
    }

    /**
     * Sets the recordCount.
     * 
     * @param recordCount the recordCount to set
     */
    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * Getter for recordPercentage.
     * 
     * @return the recordPercentage
     */
    public String getRecordPercentage() {
        return this.recordPercentage;
    }

    /**
     * Sets the recordPercentage.
     * 
     * @param recordPercentage the recordPercentage to set
     */
    public void setRecordPercentage(String recordPercentage) {
        this.recordPercentage = recordPercentage;
    }

}
