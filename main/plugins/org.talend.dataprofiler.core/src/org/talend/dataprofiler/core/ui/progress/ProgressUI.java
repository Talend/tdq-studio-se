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
package org.talend.dataprofiler.core.ui.progress;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * DOC rli class global comment. Detailled comment <br/>
 * 
 */
public final class ProgressUI {

    private ProgressUI() {
    }

    /**
     * DOC bZhou Comment method "popProgressDialog".
     * 
     * @param runnable
     * @param shell
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    public static void popProgressDialog(IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException {
        popProgressDialog(runnable, true, true);
    }

    /**
     * DOC bZhou Comment method "popProgressDialog".
     * 
     * @param shell
     * @param runnable
     * @param fork
     * @param cancelable
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    public static void popProgressDialog(IRunnableWithProgress runnable, boolean fork, boolean cancelable)
            throws InvocationTargetException, InterruptedException {
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
        dialog.run(fork, cancelable, runnable);
    }
}
