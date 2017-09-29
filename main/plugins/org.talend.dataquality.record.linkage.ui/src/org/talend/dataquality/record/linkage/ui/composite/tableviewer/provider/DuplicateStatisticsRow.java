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
public class DuplicateStatisticsRow {

    private String label;

    private Long count;

    private String percentage;

    private Boolean isRowCount = Boolean.FALSE;

    /**
     * Getter for isRowCount.
     * 
     * @return the isRowCount
     */
    public Boolean getIsRowCount() {
        return this.isRowCount;
    }

    /**
     * Sets the isRowCount.
     * 
     * @param isRowCount the isRowCount to set
     */
    public void setIsRowCount(Boolean isRowCount) {
        this.isRowCount = isRowCount;
    }

    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for count.
     * 
     * @return the count
     */
    public Long getCount() {
        return this.count;
    }

    /**
     * Sets the count.
     * 
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * Getter for percentage.
     * 
     * @return the percentage
     */
    public String getPercentage() {
        return this.percentage;
    }

    /**
     * Sets the percentage.
     * 
     * @param percentage the percentage to set
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

}
