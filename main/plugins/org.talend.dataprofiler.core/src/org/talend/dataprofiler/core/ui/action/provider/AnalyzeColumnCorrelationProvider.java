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

import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.dataprofiler.core.ui.action.actions.AnalyzeColumnCorrelationAction;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class AnalyzeColumnCorrelationProvider extends AbstractCommonActionProvider {

    private AnalyzeColumnCorrelationAction analyzeColumnCorrAction;

    public AnalyzeColumnCorrelationProvider() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    @Override
    public void init(ICommonActionExtensionSite site) {

        if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            analyzeColumnCorrAction = new AnalyzeColumnCorrelationAction();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        boolean showMenu = true;
        TreeSelection currentSelection = ((TreeSelection) this.getContext().getSelection());
        List list = currentSelection.toList();
        for (Object obj : list) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                if (ENodeType.TDQ_REPOSITORY_ELEMENT.equals(node.getType())) {
                    IRepositoryViewObject viewObject = node.getObject();
                    if (viewObject instanceof MetadataColumnRepositoryObject) {
                        showMenu = true;
                    } else {
                        showMenu = false;
                        break;
                    }
                } else {
                    showMenu = false;
                    break;
                }
            } else {
                showMenu = false;
                break;
            }
        }
        if (showMenu) {
            analyzeColumnCorrAction.setColumnSelection(currentSelection);
            menu.add(analyzeColumnCorrAction);
        }
    }

}
