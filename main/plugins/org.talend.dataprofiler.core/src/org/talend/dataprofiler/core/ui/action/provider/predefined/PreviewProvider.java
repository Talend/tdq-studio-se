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
package org.talend.dataprofiler.core.ui.action.provider.predefined;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.ui.action.actions.predefined.PreviewAction;
import org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class PreviewProvider extends AbstractCommonActionProvider {

    /**
     * PreviewProvider constructor.
     */
    public PreviewProvider() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        TreeSelection treeSelection = ((TreeSelection) this.getContext().getSelection());
        Object firstElement = treeSelection.getFirstElement();

        NamedColumnSet set = null;
        if (firstElement instanceof DBTableRepNode) {
            set = ((DBTableRepNode) firstElement).getTdTable();
        } else if (firstElement instanceof DBViewRepNode) {
            set = ((DBViewRepNode) firstElement).getTdView();
        }
        if (set != null) {
            PreviewAction action = new PreviewAction(set);
            menu.add(action);
        }
    }
}
