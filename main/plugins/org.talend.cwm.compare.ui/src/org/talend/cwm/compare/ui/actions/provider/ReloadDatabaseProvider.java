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
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.compare.i18n.Messages;
import org.talend.cwm.compare.ui.actions.ReloadDatabaseAction;
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
public class ReloadDatabaseProvider extends AbstractCommonActionProvider {

    public static final String RELOADDATABASE_MENUTEXT = Messages.getString("ReloadDatabaseProvider.reloadDBList"); //$NON-NLS-1$

    private static final String RELOADTABLES_MENUTEXT = Messages.getString("ReloadDatabaseProvider.reloadTableList"); //$NON-NLS-1$

    private static final String RELOADVIEWS_MENUTEXT = Messages.getString("ReloadDatabaseProvider.reloadViewList"); //$NON-NLS-1$

    private static final String RELOADCOLUMNS_MENUTEXT = Messages.getString("ReloadDatabaseProvider.reloadColumnList"); //$NON-NLS-1$

    public ReloadDatabaseProvider() {
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }

        // MOD bug 16532 scorreia 2010-10-19 hide menu when not in TOP standalone
        // if (!PluginChecker.isOnlyTopLoaded()) {
        // return;
        // }
        // ~
        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        if (obj instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) obj;
            if (shouldShowReloadMenu(node)) {
                String menuText = RELOADDATABASE_MENUTEXT;
                if (node instanceof DBTableFolderRepNode) {
                    menuText = RELOADTABLES_MENUTEXT;
                } else if (node instanceof DBViewFolderRepNode) {
                    menuText = RELOADVIEWS_MENUTEXT;
                } else if (node instanceof DBColumnFolderRepNode) {
                    menuText = RELOADCOLUMNS_MENUTEXT;
                }
                // menu.add(new ReloadDatabaseAction(node.getObject(), menuText));
                menu.add(new ReloadDatabaseAction(node, menuText));
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
            IRepositoryViewObject object = node.getObject();
            Connection conn = getConnection(node);

            if (MetadataConnectionUtils.isTDQSupportDBTemplate(conn)) {
                return true;
            }
        }
        return false;
    }

}
