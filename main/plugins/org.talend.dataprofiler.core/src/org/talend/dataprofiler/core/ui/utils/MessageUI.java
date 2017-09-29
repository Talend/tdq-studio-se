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
package org.talend.dataprofiler.core.ui.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public final class MessageUI {

    private MessageUI() {
    }

    public static void openError(String message) {
        MessageDialogWithToggle.openError(CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
                DefaultMessagesImpl.getString("MessageUI.Error"), message); //$NON-NLS-1$
    }

    public static void openWarning(String message) {
        MessageDialogWithToggle.openWarning(CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
                DefaultMessagesImpl.getString("MessageUI.Warning"), message); //$NON-NLS-1$
    }

    public static boolean openConfirm(String message) {
        return MessageDialogWithToggle.openConfirm(CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
                DefaultMessagesImpl.getString("MessageUI.Confirm"), message); //$NON-NLS-1$
    }

    public static boolean openYesNoQuestion(String message) {
        return MessageDialog.openQuestion(CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
                DefaultMessagesImpl.getString("MessageUI.Confirm"), message); //$NON-NLS-1$
    }
}
