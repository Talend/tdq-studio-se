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
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.talend.core.repository.ui.actions.EmptyRecycleBinAction;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC qiongli class global comment. Detailled comment
 */
public class DQEmptyRecycleBinAction extends EmptyRecycleBinAction {

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    public DQEmptyRecycleBinAction() {
        super();
        setText(super.getText());
        setImageDescriptor(super.getImageDescriptor());
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

    @Override
    public void run() {
        repositoryObjectCRUD.refreshDQViewForRemoteProject();
        // MOD gdbu 2011-11-24 TDQ-4068
        Collection<IRepositoryNode> findAllRecycleBinNodes = needDeleteNodes();
        if (null == findAllRecycleBinNodes) {
            return;
        }
        DQDeleteAction dqDeleteAction = new DQDeleteAction(findAllRecycleBinNodes.toArray());
        // if there exist filtered out nodes, there will show the warning dialog in needDeleteNodes(), so don't need to
        // show the warning dialog again in DQDeleteAction
        dqDeleteAction.setShowFilteredOutWarning(false);
        dqDeleteAction.run();
    }

    /**
     * ADD gdbu 2011-11-24 TDQ-4068 To get all the nodes those need to be deleted when empty Recycle Bin node.
     * 
     * @return
     */
    private Collection<IRepositoryNode> needDeleteNodes() {
        // containsFilteredOutChildren list contains all of the first level children under the Recycle Bin
        // node.(include the filtered out nodes)
        List<IRepositoryNode> containsFilteredOutChildren = ((RepositoryNode) RepositoryNodeHelper.getRecycleBinRepNode())
                .getChildren();

        if (DQRepositoryNode.isOnFilterring()) {
            Collection<IRepositoryNode> allRecycleBinNodes = RepositoryNodeHelper
                    .findAllChildrenNodes(containsFilteredOutChildren);
            DQRepositoryNode.setIsReturnAllNodesWhenFiltering(false);
            // shownNodesInRecycleBin list contains only the first level shown children under the Recycle Bin
            // node.(don't contain the filtered out nodes)
            List<IRepositoryNode> shownNodesInRecycleBin = ((RepositoryNode) RepositoryNodeHelper.getRecycleBinRepNode())
                    .getChildren();
            DQRepositoryNode.setIsReturnAllNodesWhenFiltering(true);
            Collection<IRepositoryNode> allShownNodes = RepositoryNodeHelper.findAllChildrenNodes(shownNodesInRecycleBin);

            if (!RepositoryNodeHelper.canDeleteWhenFiltering(allRecycleBinNodes, allShownNodes)) {
                return null;
            }
        }
        return containsFilteredOutChildren;
    }
}
