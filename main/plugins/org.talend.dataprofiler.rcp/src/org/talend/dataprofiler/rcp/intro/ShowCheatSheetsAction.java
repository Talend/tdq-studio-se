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

import java.util.Properties;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.intro.IIntroConstants;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;

/**
 * after welcome page, show the cheat cheet view action.
 * 
 */
@SuppressWarnings("restriction")
public class ShowCheatSheetsAction extends Action implements IIntroAction {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    @Override
    public void run(IIntroSite site, Properties params) {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart findView = activePage.findView(IIntroConstants.INTRO_VIEW_ID);
        if (findView != null) {
            //Max cheat sheet action will be done when welcome page hide
            activePage.hideView(findView);
        }
    }
}
