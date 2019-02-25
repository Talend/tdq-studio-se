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
import org.apache.log4j.Priority;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.i18n.Messages;

/**
 * Exception handling via message box.<br/>
 * 
 * $Id: MessageBoxExceptionHandler.java 3889 2007-06-17 18:14:27 +0000 (星期日, 17 六月 2007) mhelleboid $
 * 
 */
public final class MessageBoxExceptionHandler {

    private static Throwable lastShowedAction;

    /**
     * Empty constructor.
     */
    private MessageBoxExceptionHandler() {
    }

    /**
     * Log the exeption then open a message box showing a generic message and exception message.
     * 
     * @param ex - exception to log
     */
    public static void process(Throwable ex) {
        process(ex, null);
    }

    public static void process(Throwable ex, Shell shell) {
        ExceptionHandler.process(ex);
        Shell messageShell = shell;
        if (messageShell == null) {
            try {
                messageShell = DisplayUtils.getDefaultShell(false);
            } catch (Exception e) {
                // ignore me
            }
        }

        showMessage(ex, messageShell);
    }

    public static void process(Throwable ex, Shell shell, String exceptionType) {
        ExceptionHandler.process(ex);
        Shell messageShell = shell;
        if (messageShell == null) {
            try {
                messageShell = DisplayUtils.getDefaultShell(false);
            } catch (Exception e) {
                // ignore me
            }
        }

        if (messageShell != null) {
            showMessage(ex, messageShell, exceptionType);
        }
    }

    private static void showMessage(Throwable ex, Shell shell, String exceptionType) {
        if (ex.equals(lastShowedAction)) {
            return;
        }
        lastShowedAction = ex;

        String title = Messages.getString("MessageBoxExceptionHandler.common.error"); //$NON-NLS-1$
        String msg = exceptionType + ":" + ex.getMessage(); //$NON-NLS-1$
        Priority priority = Level.ERROR;

        if (priority == Level.FATAL || priority == Level.ERROR) {
            MessageDialog.openError(shell, title, msg);
        } else if (priority == Level.WARN) {
            MessageDialog.openWarning(shell, title, msg);
        } else if (priority == Level.INFO) {
            MessageDialog.openInformation(shell, title, msg);
        } else {
            MessageDialog.openInformation(shell, title, msg);
        }
    }

    /**
     * Open a message box showing a specical message.
     * 
     * @param ex - exception to show
     */
    protected static void showMessage(Exception ex, Shell shell, Priority priority, String msg) {
        if (ex.equals(lastShowedAction)) {
            return;
        }
        lastShowedAction = ex;

        String title = Messages.getString("MessageBoxExceptionHandler.common.error"); //$NON-NLS-1$

        if (priority == Level.FATAL || priority == Level.ERROR) {
            MessageDialog.openError(shell, title, msg);
        } else if (priority == Level.WARN) {
            MessageDialog.openWarning(shell, title, msg);
        } else if (priority == Level.INFO) {
            MessageDialog.openInformation(shell, title, msg);
        } else {
            MessageDialog.openInformation(shell, title, msg);
        }
    }

    /**
     * Open a message box showing a generic message and exception message.
     * 
     * @param ex - exception to show
     */
    protected static void showMessage(Throwable ex, Shell shell) {
        if (ex.equals(lastShowedAction)) {
            return;
        }
        lastShowedAction = ex;

        String title = Messages.getString("MessageBoxExceptionHandler.common.error"); //$NON-NLS-1$
        String msg = Messages.getString("MessageBoxExceptionHandler.exception.errorOcured", ex.getMessage()); //$NON-NLS-1$
        Priority priority = Level.ERROR;

        if (priority == Level.FATAL || priority == Level.ERROR) {
            MessageDialog.openError(shell, title, msg);
        } else if (priority == Level.WARN) {
            MessageDialog.openWarning(shell, title, msg);
        } else if (priority == Level.INFO) {
            // FIXME use the same code in two branches.
            MessageDialog.openInformation(shell, title, msg);
        } else {
            MessageDialog.openInformation(shell, title, msg);
        }
    }
}
