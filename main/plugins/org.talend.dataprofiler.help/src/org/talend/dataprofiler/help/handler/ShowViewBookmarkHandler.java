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
package org.talend.dataprofiler.help.handler;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.help.internal.base.BaseHelpSystem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.help.BookmarksHelpView;

/**
 * 
 * DOC fywang class global comment. Detailled comment
 */
@SuppressWarnings("restriction")
public class ShowViewBookmarkHandler extends AbstractHandler {

    protected static Logger log = Logger.getLogger(ShowViewBookmarkHandler.class);

    @SuppressWarnings({ "static-access" })
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {

            if (BaseHelpSystem.getInstance().ensureWebappRunning()) {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (window != null) {
                    IWorkbenchPage page = window.getActivePage();
                    if (page != null) {
                        try {
                            IViewPart part = page.findView(BookmarksHelpView.HELP_VIEW_ID);
                            if (part == null) {
                                page.showView(BookmarksHelpView.HELP_VIEW_ID);
                            }
                        } catch (PartInitException e) {
                        }
                    }
                }
                // DefaultHelpUI.getInstance().displayHelp();
            }

        } catch (Exception e) {
            log.error(e, e);
        }
        return null;
    }
}
