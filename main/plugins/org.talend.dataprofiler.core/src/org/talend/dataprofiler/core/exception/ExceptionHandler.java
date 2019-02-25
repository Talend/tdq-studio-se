// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.exception;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.ui.gmf.util.DisplayUtils;

/**
 * Implementation of exception handling strategy.<br/>
 * 
 * $Id: ExceptionHandler.java 3351 2007-05-04 12:14:00 +0000 (星期五, 04 五月 2007) plegall $
 * 
 */
public final class ExceptionHandler {

    private static Logger log = Logger.getLogger(ExceptionHandler.class);

    /**
     * Empty constructor.
     */
    private ExceptionHandler() {
    }

    /**
     * Log message relative to ex param. Log level depends on exception type.
     * 
     * @param ex - exception to log
     */
    public static void process(Throwable ex) {
        // Priority priority = getPriority(ex);
        process(ex, Level.ERROR);
    }

    public static void process(Throwable ex, Priority priority) {
        String message = ex.getMessage();

        log.log(priority, message, ex);

        if (priority == Level.FATAL) {
            MessageBoxExceptionHandler.showMessage(ex, DisplayUtils.getDefaultShell(false));
        }
    }

    public static void process(BusinessException ex, Priority priority) {
        String message = ex.getMessage();

        log.log(priority, message, ex);

        if (priority == Level.FATAL) {
            MessageBoxExceptionHandler.showMessage(ex, DisplayUtils.getDefaultShell(false), priority, ex.getAdditonalMessage());
        }
    }


}
