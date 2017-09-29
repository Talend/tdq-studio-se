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
import org.talend.dataquality.analysis.Analysis;
import org.talend.utils.sugars.ReturnCode;

/**
 * @author scorreia
 * 
 * Interface for executing an analysis.
 */
public interface IAnalysisExecutor {

    /**
     * Method "execute".
     * 
     * @param analysis the analysis to execute
     * @return a return code with an error message in case when something went bad
     */
    public abstract ReturnCode execute(final Analysis analysis);

    /**
     * 
     * DOC xqliu Comment method "setMonitor".
     * 
     * @param monitor
     */
    public void setMonitor(IProgressMonitor monitor);

    
    // TODO scorreia add a method in order to separate preparation of analysis and execution.
    // public abstract ReturnCode prepareAnalysis(final Analysis analysis)
}
