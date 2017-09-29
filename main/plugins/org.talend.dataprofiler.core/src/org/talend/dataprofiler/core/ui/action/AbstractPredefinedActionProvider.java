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
package org.talend.dataprofiler.core.ui.action;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider;

/**
 * DOC zqin class global comment. Detailled comment
 */
public abstract class AbstractPredefinedActionProvider extends AbstractCommonActionProvider {

    private AbstractPredefinedAnalysisAction action;

    @Override
    public void init(ICommonActionExtensionSite site) {

        if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            action = getAction();
        }
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }

        TreeSelection currentSelection = ((TreeSelection) this.getContext().getSelection());
        if (action != null) {
            action.setSelection(currentSelection);

            if (action.isAllowed()) {

                IMenuManager subMenuManager = getSubMenuManager(menu, NEW_MENU_NAME);
                if (subMenuManager != null) {
                    subMenuManager.add(action);
                } else {
                    menu.add(action);
                }
            }
        }
    }

    protected abstract AbstractPredefinedAnalysisAction getAction();

}
