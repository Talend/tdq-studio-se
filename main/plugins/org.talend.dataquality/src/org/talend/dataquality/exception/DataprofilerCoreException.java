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
package org.talend.dataquality.exception;

/**
 * @author rli
 * 
 */
public class DataprofilerCoreException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 556930098222177421L;

    public DataprofilerCoreException(String message) {
        super(message);
    }

    public DataprofilerCoreException(Throwable cause) {
        super(cause);
    }

}
