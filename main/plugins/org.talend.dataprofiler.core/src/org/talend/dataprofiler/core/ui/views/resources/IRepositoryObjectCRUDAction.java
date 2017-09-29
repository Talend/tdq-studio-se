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
import org.talend.repository.model.IRepositoryNode;

/**
 * IRepository Object CRUD. this class is used when the node is move/rename... (two types project "remote" and "local"
 * should be considerred )
 * 
 */
public interface IRepositoryObjectCRUDAction {

    public Boolean validateDrop(IRepositoryNode targetNode);

    public Boolean handleDrop(IRepositoryNode targetNode);

    public Boolean handleRenameFolder(IRepositoryNode targetNode);

    /**
     * The UI selection will be vary when the project varies (e.g remote project will refresh the tree when get
     * selection on UI, but local project won't)
     * 
     * @return
     */
    public ISelection getUISelection();

    /**
     * refresh the DQView in Remote Project, do nothing in Local Project.
     */
    public void refreshDQViewForRemoteProject();

    /**
     * check whether the selected node is Available.
     * 
     * @return
     */
    public Boolean isSelectionAvailable();

    /**
     * show the unsynchronized warning dialog.
     */
    public void showWarningDialog();
}
