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
package org.talend.cwm.exception;

import org.talend.utils.exceptions.TalendException;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class AnalysisExecutionException extends TalendException {

    /**
     * DOC scorreia AnalysisExecutionException constructor comment.
     */
    public AnalysisExecutionException() {
        super();
    }

    /**
     * DOC scorreia AnalysisExecutionException constructor comment.
     * 
     * @param message
     * @param cause
     */
    public AnalysisExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * DOC scorreia AnalysisExecutionException constructor comment.
     * 
     * @param message
     */
    public AnalysisExecutionException(String message) {
        super(message);
    }

    /**
     * DOC scorreia AnalysisExecutionException constructor comment.
     * 
     * @param cause
     */
    public AnalysisExecutionException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 9128680124097106920L;

}
