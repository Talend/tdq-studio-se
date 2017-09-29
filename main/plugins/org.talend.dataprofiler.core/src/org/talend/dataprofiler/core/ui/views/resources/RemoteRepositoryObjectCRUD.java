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
package org.talend.dataprofiler.core.ui.views.resources;

import org.eclipse.jface.viewers.ISelection;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IRepositoryNode;

/**
 * Remote Repository Object CRUD. only when the project is remote use this.
 */
public class RemoteRepositoryObjectCRUD extends LocalRepositoryObjectCRUD {

    @Override
    public Boolean handleDrop(final IRepositoryNode targetNode) {
        // TDQ-11324: surround with this to avoid after refresh, get selected node is empty for git remote project
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("handle Drop", this) { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                String[] pathBeforeRefresh = getSelectedNodePaths();
                if (pathBeforeRefresh.length == 0) {
                    showWarningDialog();
                    return;
                }

                // in remote project, refresh first.
                refreshDQViewForRemoteProject();

                String[] pathAfterRefresh = getSelectedNodePaths();
                if (pathAfterRefresh.length == 0) {
                    showWarningDialog();
                    return;
                }

                IRepositoryNode[] selectedRepositoryNodes = getSelectedRepositoryNodes();
                if (selectedRepositoryNodes.length == 0) {
                    showWarningDialog();
                    return;
                }
                // compare the node path value between before and after refresh
                for (int i = 0; i < getSelectedRepositoryNodes().length; i++) {
                    if (!pathBeforeRefresh[i].equals(pathAfterRefresh[i])) {
                        showWarningDialog();
                        return;
                    }
                }

                result = superHandle(targetNode);
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().executeRepositoryWorkUnit(repositoryWorkUnit);
        return (Boolean) repositoryWorkUnit.getResult();
    }

    private boolean superHandle(IRepositoryNode targetNode) {
        return super.handleDrop(targetNode);
    }

    /**
     * get the Selected Node Paths.
     * 
     * @return
     */
    public String[] getSelectedNodePaths() {
        String[] pathBeforeRefresh = new String[0];
        IRepositoryNode[] nodesBeforeRefresh = getSelectedRepositoryNodes();
        if (nodesBeforeRefresh.length > 0) {
            pathBeforeRefresh = new String[nodesBeforeRefresh.length];
            for (int i = 0; i < nodesBeforeRefresh.length; i++) {
                IRepositoryNode node = nodesBeforeRefresh[i];
                pathBeforeRefresh[i] = RepositoryNodeHelper.getPath(node).toString();
            }
        }
        return pathBeforeRefresh;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.LocalRepositoryObjectCRUD#refreshDQViewForRemoteProject()
     */
    @Override
    public void refreshDQViewForRemoteProject() {
        refreshWorkspaceDQView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.LocalRepositoryObjectCRUD#isSelectionAvailable()
     */
    @Override
    public Boolean isSelectionAvailable() {
        ISelection uiSelection = getUISelection();
        return uiSelection != null && !uiSelection.isEmpty();
    }

}
