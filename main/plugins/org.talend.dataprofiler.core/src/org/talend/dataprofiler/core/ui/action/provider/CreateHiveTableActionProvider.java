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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.talend.dataprofiler.core.service.AbstractSvnRepositoryService;
import org.talend.dataprofiler.core.service.GlobalServiceRegister;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dq.nodes.hadoopcluster.HDFSOfHCConnectionNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by yyin on 2015年4月27日 Detailled comment
 *
 */
public class CreateHiveTableActionProvider extends AbstractCommonActionProvider {

    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }
        AbstractSvnRepositoryService svnReposService = GlobalServiceRegister.getDefault().getSvnRepositoryService(
                AbstractSvnRepositoryService.class);
        if (svnReposService != null && svnReposService.isReadonly()) {
            return;
        }

        RepositoryNode node = (RepositoryNode) getFirstRepositoryNode();
        if (node != null) {
            IAction action = null;
            if (node instanceof HDFSOfHCConnectionNode) {
                action = HadoopClusterUtils.getDefault().createActionOfHiveTable(node);
                menu.add(action);
            }

        }
    }
}
