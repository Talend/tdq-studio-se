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
package org.talend.dataprofiler.core.ui.action.provider;

import org.eclipse.jface.action.IMenuManager;
import org.talend.dataprofiler.core.ui.action.actions.EditFileDelimitedAction;
import org.talend.repository.model.IRepositoryNode;

/**
 * MOD bzhou 2011-4-1 bug:20051 DOC bzhou class global comment. Detailled comment
 */
public class FileDelimitedContextActionProvider extends AbstractCommonActionProvider {

    /*
     * (non-Jsdoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }

        super.fillContextMenu(menu);

        IRepositoryNode node = getFirstRepositoryNode();

        if (node != null) {
            menu.add(new EditFileDelimitedAction(node));
        }
    }
}
