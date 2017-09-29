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
import org.talend.dataprofiler.core.sql.RenameJrxmlTemplateFileAction;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class JrxmlTemplateFileActionProvider extends AbstractCommonActionProvider {

    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }
        TreeSelection treeSelection = ((TreeSelection) this.getContext().getSelection());
        if (treeSelection.size() == 1) {
            RepositoryNode node = (RepositoryNode) treeSelection.getFirstElement();
            if (node instanceof JrxmlTempleteRepNode) {
                menu.add(new RenameJrxmlTemplateFileAction(node));
            }
        }
    }
}
