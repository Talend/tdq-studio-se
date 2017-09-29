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
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataprofiler.core.ui.action.actions.CreateDQRulesAction;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class DQRulesActionProvider extends AbstractCommonActionProvider {

    protected static Logger log = Logger.getLogger(DQRulesActionProvider.class);

    public static final String EXTENSION_DQRULE = FactoriesUtil.DQRULE;

    public DQRulesActionProvider() {

    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        TreeSelection treeSelection = ((TreeSelection) this.getContext().getSelection());

        if (treeSelection.size() == 1) {
            Object obj = treeSelection.getFirstElement();
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                if (ENodeType.SYSTEM_FOLDER.equals(node.getType()) || ENodeType.SIMPLE_FOLDER.equals(node.getType())) {
                    IFolder folder = WorkbenchUtils.getFolder(node);
                        menu.add(new CreateDQRulesAction(folder));
                }
            }
        }
    }
}
