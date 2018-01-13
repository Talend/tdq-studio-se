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
import org.talend.dataprofiler.core.ui.action.actions.DQCreateContextAction;
import org.talend.dataprofiler.core.ui.action.actions.DQEditContextAction;
import org.talend.dq.nodes.ContextFolderRepNode;
import org.talend.dq.nodes.ContextRepNode;
import org.talend.dq.nodes.ContextSubFolderRepNode;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * @author qiongli
 * A context provider
 *
 */
public class NewContextActionProvider extends AbstractCommonActionProvider {

    public NewContextActionProvider() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }

        RepositoryNode node = (RepositoryNode) getFirstRepositoryNode();
        if (node instanceof ContextFolderRepNode || node instanceof ContextSubFolderRepNode) {
            menu.add(new DQCreateContextAction(node));
        } else if (node instanceof ContextRepNode) {
            // Edit context group
            menu.add(new DQEditContextAction(node, false));
            // Read context group
            menu.add(new DQEditContextAction(node, true));
        }

    }
}
