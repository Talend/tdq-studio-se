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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.ui.action.actions.RunAnalysisAction;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC rli class global comment. Detailled comment
 */
public class RunAnalysisActionProvider extends AbstractCommonActionProvider {

    /**
     * Adds a submenu to the given menu with the name "New Component".
     */
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        Object[] array = ((TreeSelection) this.getContext().getSelection()).toArray();
        ArrayList<TDQAnalysisItem> selectedItemsList = new ArrayList<TDQAnalysisItem>();

        for (Object obj : array) {
            RepositoryNode node = (RepositoryNode) obj;
            RepositoryNode parent = node.getParent();
            if (!(parent instanceof ReportSubFolderRepNode)) {
                // IPath append = WorkbenchUtils.getFilePath(node);
                Item item = node.getObject().getProperty().getItem();
                if (item instanceof TDQAnalysisItem) {
                    selectedItemsList.add((TDQAnalysisItem) item);
                }
            }
        }

        if (!selectedItemsList.isEmpty()) {
            // IFile file = ResourceManager.getRootProject().getFile(append);
            RunAnalysisAction runAnalysisAction = new RunAnalysisAction();
            // runAnalysisAction.setSelectionFile(file);
            runAnalysisAction.setAnalysisItems(selectedItemsList.toArray(new TDQAnalysisItem[selectedItemsList.size()]));
            menu.add(runAnalysisAction);
        }
    }
}
