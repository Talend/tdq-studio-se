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
import org.talend.dataprofiler.core.ui.action.actions.DQEmptyRecycleBinAction;
import org.talend.repository.model.IRepositoryNode;


/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DQEmptyRecycleBinActionProvider extends AbstractCommonActionProvider {

    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }
        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        IRepositoryNode recycleNode = (IRepositoryNode) obj;
        if (recycleNode.getChildren().size() > 0) {
            menu.add(new DQEmptyRecycleBinAction());
        }

    }

}
