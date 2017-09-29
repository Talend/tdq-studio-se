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
package org.talend.cwm.compare.exception;

import org.talend.dataquality.exception.DataprofilerCoreException;

/**
 * DOC rli class global comment. Detailled comment
 */
public class ReloadCompareException extends DataprofilerCoreException {

	/**
     * 
     */
	private static final long serialVersionUID = -6647762192837141336L;

	public ReloadCompareException(String message) {
		super(message);
	}

	public ReloadCompareException(Throwable cause) {
		super(cause);
	}

}
