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
package org.talend.cwm.compare.ui.actions.provider;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.compare.i18n.Messages;
import org.talend.cwm.compare.ui.actions.PopComparisonUIAction;
import org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dq.nodes.DBColumnFolderRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * DOC rli class global comment. Detailled comment
 */
public class PopComparisonUIProvider extends AbstractCommonActionProvider {

    private static final String COMPAREDATABASE_MENUTEXT = Messages.getString("PopComparisonUIProvider.dbCompare"); //$NON-NLS-1$

    private static final String COMPARETABLES_MENUTEXT = Messages.getString("PopComparisonUIProvider.tableCompare"); //$NON-NLS-1$

    private static final String COMPAREVIEWS_MENUTEXT = Messages.getString("PopComparisonUIProvider.viewCompare"); //$NON-NLS-1$

    private static final String COMPARECOLUMNS_MENUTEXT = Messages.getString("PopComparisonUIProvider.columnCompare"); //$NON-NLS-1$

    public PopComparisonUIProvider() {
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        // TDQ-9394 hide all "Reload" menu.
        if (isShowMenu()) {
            return;
        }
        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        if (obj instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) obj;
            if (shouldShowReloadMenu(node)) {
                String menuText = COMPAREDATABASE_MENUTEXT;
                if (node instanceof DBTableFolderRepNode) {
                    menuText = COMPARETABLES_MENUTEXT;
                } else if (node instanceof DBViewFolderRepNode) {
                    menuText = COMPAREVIEWS_MENUTEXT;
                } else if (node instanceof DBColumnFolderRepNode) {
                    menuText = COMPARECOLUMNS_MENUTEXT;
                }
                menu.add(new PopComparisonUIAction(node, menuText));
            }
        }

    }

    /**
     * DOC xqliu Comment method "shouldShowReloadMenu".
     * 
     * @param node
     * @return
     */
    private boolean shouldShowReloadMenu(RepositoryNode node) {
        ENodeType type = node.getType();
        IFolder folder = WorkbenchUtils.getFolder(node);
        IFolder connectionFolder = ResourceManager.getConnectionFolder();
        if ((ENodeType.REPOSITORY_ELEMENT.equals(type) || ENodeType.TDQ_REPOSITORY_ELEMENT.equals(type))
                && ResourceService.isSubFolder(connectionFolder, folder)) {
            Connection conn = getConnection(node);

            if (MetadataConnectionUtils.isTDQSupportDBTemplate(conn)) {
                return true;
            }
        }
        return false;
    }
}
