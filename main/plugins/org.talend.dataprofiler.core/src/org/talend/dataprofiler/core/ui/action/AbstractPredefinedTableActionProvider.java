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
package org.talend.dataprofiler.core.ui.action;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.TdTableRepositoryObject;
import org.talend.core.repository.model.repositoryObject.TdViewRepositoryObject;
import org.talend.dataprofiler.core.ui.action.provider.AbstractCommonActionProvider;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public abstract class AbstractPredefinedTableActionProvider extends AbstractCommonActionProvider {

    private AbstractPredefinedTableAnalysisAction action;

    @Override
    public void init(ICommonActionExtensionSite site) {

        if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            action = getAction();
        }
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        TreeSelection currentSelection = ((TreeSelection) this.getContext().getSelection());
        Object fe = currentSelection.getFirstElement();
        if (fe instanceof IRepositoryNode) {
            IRepositoryViewObject object = ((IRepositoryNode) fe).getObject();
            if (object instanceof TdTableRepositoryObject || object instanceof TdViewRepositoryObject) {
                action.setSelection(currentSelection);

                if (action.isAllowed()) {
                    menu.add(action);
                }
            }
        }

    }

    protected abstract AbstractPredefinedTableAnalysisAction getAction();

}
