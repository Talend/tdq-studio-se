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
package org.talend.dq.analysis.parameters;

import org.talend.core.model.metadata.builder.connection.Connection;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class AnalysisFilterParameter extends AnalysisParameter {

    private String tableFilter;

    private String viewFilter;

    private Connection tdDataProvider;



    /**
     * Getter for tableFilter.
     * 
     * @return the tableFilter
     */
    public String getTableFilter() {
        return this.tableFilter;
    }

    /**
     * Sets the tableFilter.
     * 
     * @param tableFilter the tableFilter to set
     */
    public void setTableFilter(String tableFilter) {
        this.tableFilter = tableFilter;
    }

    /**
     * Getter for viewFilter.
     * 
     * @return the viewFilter
     */
    public String getViewFilter() {
        return this.viewFilter;
    }

    /**
     * Sets the viewFilter.
     * 
     * @param viewFilter the viewFilter to set
     */
    public void setViewFilter(String viewFilter) {
        this.viewFilter = viewFilter;
    }

    /**
     * Getter for tdDataProvider.
     * 
     * @return the tdDataProvider
     */
    public Connection getTdDataProvider() {
        return this.tdDataProvider;
    }

    /**
     * Sets the tdDataProvider.
     * 
     * @param tdDataProvider the tdDataProvider to set
     */
    public void setTdDataProvider(Connection tdDataProvider) {
        this.tdDataProvider = tdDataProvider;
    }

}
