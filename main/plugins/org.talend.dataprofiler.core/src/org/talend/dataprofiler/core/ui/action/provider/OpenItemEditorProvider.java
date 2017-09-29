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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.ReportRepNode;
import org.talend.repository.model.IRepositoryNode;

/**
 * 
 * DOC mzhao Open TDQ item editor provider.
 */
public class OpenItemEditorProvider extends AbstractCommonActionProvider {

    public OpenItemEditorProvider() {
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isSelectionSameType()) {
            return;
        }

        Object[] array = ((TreeSelection) this.getContext().getSelection()).toArray();
        List<IRepositoryNode> selectedItemsList = new ArrayList<IRepositoryNode>();

        for (Object obj : array) {
            if (obj instanceof IRepositoryNode) {
                IRepositoryNode node = (IRepositoryNode) obj;
                if (RepositoryNodeHelper.canOpenEditor(node) && !(node instanceof ReportRepNode)) {
                    selectedItemsList.add((IRepositoryNode) obj);
                }
            }
        }

        if (!selectedItemsList.isEmpty()) {
            menu.add(new OpenItemEditorAction(selectedItemsList.toArray(new IRepositoryNode[selectedItemsList.size()])));
        }
    }
}
