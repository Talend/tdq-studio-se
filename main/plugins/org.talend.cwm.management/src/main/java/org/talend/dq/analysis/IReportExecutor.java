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

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.cwm.exception.AnalysisExecutionException;
import org.talend.dataquality.reports.TdReport;
import org.talend.utils.sugars.ReturnCode;

/**
 * @author scorreia
 * 
 * Interface for executing a report (this means executing the analyses contained in the report).
 */
public interface IReportExecutor {

    /**
     * Method "execute".
     * 
     * @param report the report to execute
     * @return a return code with an error message in case when something went bad
     * @throws AnalysisExecutionException
     */
    public abstract ReturnCode execute(final TdReport report, IProgressMonitor monitor) throws AnalysisExecutionException;
}
