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

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.talend.dataprofiler.core.ui.action.actions.CreateDBConnectionAction;
import org.talend.dataprofiler.core.ui.action.actions.CreateDFConnectionAction;
import org.talend.dataprofiler.core.ui.action.actions.CreateHadoopClusterAction;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBConnectionSubFolderRepNode;
import org.talend.dq.nodes.DFConnectionFolderRepNode;
import org.talend.dq.nodes.DFConnectionSubFolderRepNode;
import org.talend.dq.nodes.hadoopcluster.HadoopClusterFolderRepNode;
import org.talend.dq.nodes.hadoopcluster.HadoopClusterSubFolderRepNode;
import org.talend.repository.model.RepositoryNode;

/**
 * @author rli
 * 
 */
public class NewConnectionActionProvider extends AbstractCommonActionProvider {

    protected static Logger log = Logger.getLogger(NewConnectionActionProvider.class);

    /**
     * 
     */
    public NewConnectionActionProvider() {
    }

    /**
     * Adds a submenu to the given menu with the name "New Component".
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }

        // MOD gdbu 2011-4-1 bug 20051
        RepositoryNode node = (RepositoryNode) getFirstRepositoryNode();

        if (node != null) {
            // ~20051
            IAction action = null;
            if (node instanceof DBConnectionFolderRepNode || node instanceof DBConnectionSubFolderRepNode) {
                action = new CreateDBConnectionAction(node);
            } else if (node instanceof DFConnectionFolderRepNode || node instanceof DFConnectionSubFolderRepNode) {
                action = new CreateDFConnectionAction(node);
            } else if (node instanceof HadoopClusterFolderRepNode || node instanceof HadoopClusterSubFolderRepNode) {
                action = new CreateHadoopClusterAction(node);
            }
            if (action != null) {
                menu.add(action);
            }

        }
    }
}
