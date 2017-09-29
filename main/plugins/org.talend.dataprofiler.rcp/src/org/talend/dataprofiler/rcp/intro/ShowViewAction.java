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
package org.talend.dataprofiler.rcp.intro;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.ShowViewDialog;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.views.IViewDescriptor;
import org.talend.dataprofiler.rcp.i18n.Messages;

/**
 * Displays a window for view selection. <br/>
 * 
 * $Id: ShowViewAction.java,v 1.1 2007/03/07 05:08:59 pub Exp $
 * 
 */
@SuppressWarnings("restriction")
public class ShowViewAction extends Action {

    /**
     * Constructs a new ShowViewAction.
     */
    public ShowViewAction() {
        super(Messages.getString("ShowViewAction.showView")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final IWorkbenchPage page = window.getActivePage();
        if (page == null) {
            return;
        }

        final ShowViewDialog dialog = new ShowViewDialog(window, WorkbenchPlugin.getDefault().getViewRegistry());
        dialog.open();

        if (dialog.getReturnCode() == Window.CANCEL) {
            return;
        }

        final IViewDescriptor[] descriptors = dialog.getSelection();
        for (int i = 0; i < descriptors.length; ++i) {
            try {
                page.showView(descriptors[i].getId());
            } catch (PartInitException e) {
                StatusUtil.handleStatus(e.getStatus(), WorkbenchMessages.ShowView_errorTitle + ": " + e.getMessage(), //$NON-NLS-1$
                        StatusManager.SHOW);
            }
        }
    }
}
