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

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.ui.action.actions.CreateRepositoryNodeAction;
import org.talend.dataprofiler.core.ui.exchange.ExchangeFolderRepNode;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dq.nodes.ContextFolderRepNode;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.dq.nodes.hadoopcluster.HDFSOfHCFolderRepNode;
import org.talend.dq.nodes.hadoopcluster.HiveOfHCFolderRepNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class CreateNewRepositoryNodeFolder extends AbstractCommonActionProvider {

    private IFolder currentSelection;

    /**
     * Adds a submenu to the given menu with the name "New Component".
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }

        // ADD msjian TDQ-10444: fix get error when click on the exchange node
        if (isExchangeNode()) {
            return;
        }
        // TDQ-10444~

        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        RepositoryNode node = (RepositoryNode) obj;
        RepositoryNode parent = node.getParent();
        if (!(parent instanceof ReportSubFolderRepNode)) {
            if (parent != null || node instanceof ContextFolderRepNode) {
                IFolder folder = WorkbenchUtils.getFolder(node);
                if (!(node instanceof ExchangeFolderRepNode) && !ResourceManager.getRulesFolder().equals(folder)
                        && !ResourceManager.getPatternFolder().equals(folder)
                        && !ResourceManager.getIndicatorFolder().equals(folder)
                        && !ResourceService.isSubFolder(ResourceManager.getSystemIndicatorFolder(), folder)
                        && (ENodeType.SYSTEM_FOLDER.equals(node.getType()) || ENodeType.SIMPLE_FOLDER.equals(node.getType()))
                        && !(node instanceof HDFSOfHCFolderRepNode || node instanceof HiveOfHCFolderRepNode)) {
                    // MOD qiongli 2011-1-20 don't add it for recycle bin elements
                    if (node.getObject() != null && !node.getObject().getProperty().getItem().getState().isDeleted()) {
                        currentSelection = WorkbenchUtils.getFolder(node);
                        CreateRepositoryNodeAction createSubFolderAction = new CreateRepositoryNodeAction(currentSelection);
                        menu.add(createSubFolderAction);
                    }

                }
            }
        }

    }
}
