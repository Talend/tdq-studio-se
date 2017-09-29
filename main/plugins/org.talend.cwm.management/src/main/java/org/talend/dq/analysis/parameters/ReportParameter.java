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

import java.util.List;

import org.talend.dataquality.analysis.Analysis;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class ReportParameter extends ConnectionParameter {

    private String header;

    private String footer;

    private List<Analysis> analysises;

    private boolean refresh;

    private String format;

    public ReportParameter() {
        super(EParameterType.REPORT);
    }

    /**
     * Getter for header.
     * 
     * @return the header
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * Sets the header.
     * 
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Getter for footer.
     * 
     * @return the footer
     */
    public String getFooter() {
        return this.footer;
    }

    /**
     * Sets the footer.
     * 
     * @param footer the footer to set
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * Getter for analysises.
     * 
     * @return the analysises
     */
    public List<Analysis> getAnalysises() {
        return this.analysises;
    }

    /**
     * Sets the analysises.
     * 
     * @param analysises the analysises to set
     */
    public void setAnalysises(List<Analysis> analysises) {
        this.analysises = analysises;
    }

    /**
     * Getter for refresh.
     * 
     * @return the refresh
     */
    public boolean isRefresh() {
        return this.refresh;
    }

    /**
     * Sets the refresh.
     * 
     * @param refresh the refresh to set
     */
    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    /**
     * Getter for format.
     * 
     * @return the format
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * Sets the format.
     * 
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

}
