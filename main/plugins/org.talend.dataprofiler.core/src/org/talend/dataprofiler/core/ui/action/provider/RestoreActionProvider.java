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
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.ui.action.actions.DQRestoreAction;
import org.talend.repository.model.IRepositoryNode;

/**
 * @author qiongli
 *
 */
public class RestoreActionProvider extends AbstractCommonActionProvider {

	private DQRestoreAction restoreAction;
	public RestoreActionProvider() {
		super();
	}
	/**
     * Adds a submenu to the given menu with the name "New Component".
     */
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        if (obj instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) obj;
            if (node.getObject() != null) {
                if (node.getObject().isDeleted()) {
                    restoreAction = new DQRestoreAction();
                    menu.add(restoreAction);
                }
            }
        }
    }
}
