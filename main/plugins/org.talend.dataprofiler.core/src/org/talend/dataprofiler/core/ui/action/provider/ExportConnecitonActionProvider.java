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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.ui.action.actions.ExportConnectionToTOSAction;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.AnalysisSubFolderRepNode;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ExportConnecitonActionProvider extends AbstractCommonActionProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        List<Object> selectionList = ((TreeSelection) this.getContext().getSelection()).toList();
        List<Package> packList = new ArrayList<Package>();
        boolean showMenu = false;
        for (Object obj : selectionList) {
            if (obj instanceof DBCatalogRepNode || obj instanceof DBSchemaRepNode) {
                // MOD gdbu 2011-12-29 TDQ-4281 Remove the "create a new connection" menu under
                // AnalysisSubFolderRepNode.
                RepositoryNode node = (RepositoryNode) obj;
                if (node.getParent() instanceof AnalysisSubFolderRepNode) {
                    return;
                }
                // ~TDQ-4281
                ModelElement me = RepositoryNodeHelper.getModelElementFromRepositoryNode(node);
                if (me != null && me instanceof Package) {
                    Package pack = (Package) me;
                    packList.add(pack);
                    showMenu = true;
                }
            }
        }
        if (showMenu) {
            ExportConnectionToTOSAction action = new ExportConnectionToTOSAction(packList);
            menu.add(action);
        }
    }
}
