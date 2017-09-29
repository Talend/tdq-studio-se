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
package org.talend.dq.analysis;

import java.util.Date;

import org.apache.log4j.Logger;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.reports.TdReport;

/**
 * DOC huangssssx class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ReportBuilder {

    // private static final String REPORT_NOT_INITIALIZED =
    // "TdReport has not been initialized. Call initializeTdReport() method before.";

    private static Logger log = Logger.getLogger(ReportBuilder.class);

    private boolean initialized = false;

    private TdReport report;

    public boolean initializeTdReport(String reportName) {
        if (initialized) {
            log.warn("TdReport already initialized. ");//$NON-NLS-1$
            return false;
        }

        report = ReportHelper.createReport(reportName);
        // set the creation date
        Date date = new Date(System.currentTimeMillis());
        report.setCreationDate(date);
        initialized = true;
        return initialized;
    }

    /**
     * Getter for report.
     * 
     * @return the report
     */
    public TdReport getReport() {
        return this.report;
    }
}
