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
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.ui.actions.RestoreAction;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * @author qiongli Restore recycle bin element
 */
public class DQRestoreAction extends RestoreAction {

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    /**
	 * 
	 */
    public DQRestoreAction() {
        // setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ADD_ACTION));
        super();
        // setText(DefaultMessagesImpl.getString("DQRestoreAction.restore"));
    }

    @Override
    public void run() {
        repositoryObjectCRUD.refreshDQViewForRemoteProject();

        // ADD msjian TDQ-7006 2013-7-24: after refresh get the selection to check.
        if (!repositoryObjectCRUD.isSelectionAvailable()) {
            repositoryObjectCRUD.showWarningDialog();
            return;
        }
        // TDQ-7006~

        // MOD qiongli 2011-5-9 bug 21035,avoid to unload resource.
        super.setAvoidUnloadResources(true);
        super.run();

        CorePlugin.getDefault().refreshDQView();

        CorePlugin.getDefault().refreshWorkSpace();

        // MOD gdbu 2011-11-17 TDQ-3969 : after restore items re-filter the tree , to create a new list .
        if (DQRepositoryNode.isOnFilterring()) {
            RepositoryNodeHelper.fillTreeList(null);
            RepositoryNodeHelper
                    .setFilteredNode(RepositoryNodeHelper.getRootNode(ERepositoryObjectType.TDQ_DATA_PROFILING, true));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.RestoreAction#restoreNode(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void restoreNode(RepositoryNode node) {
        RepositoryNode tempNode = node;
        List<IRepositoryNode> recycleBinNodeFirstLevelChildren = ((RepositoryNode) RepositoryNodeHelper.getRecycleBinRepNode())
                .getChildren();
        Collection<IRepositoryNode> allRecycleBinNodes = RepositoryNodeHelper
                .findAllChildrenNodes(recycleBinNodeFirstLevelChildren);
        Iterator<IRepositoryNode> iterator = allRecycleBinNodes.iterator();
        while (iterator.hasNext()) {
            IRepositoryNode next = iterator.next();
            if (next.equals(node)) {
                tempNode = (RepositoryNode) next;
                break;
            }
        }
        super.restoreNode(tempNode);
    }

    @Override
    public void init(TreeViewer viewer, IStructuredSelection selection) {

    }

    @Override
    public ISelection getSelection() {
        DQRespositoryView findView = CorePlugin.getDefault().getRepositoryView();
        ISelection selection = findView.getCommonViewer().getSelection();
        return selection;
    }

    @Override
    protected ISelection getRepositorySelection() {
        return this.getSelection();
    }

}
