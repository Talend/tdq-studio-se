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

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.ui.action.actions.ExportIndicatorDefinitionAction;
import org.talend.dataprofiler.core.ui.action.actions.ImportIndicatorDefinitionAction;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ImportExportIndicatorProvider extends AbstractCommonActionProvider {

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
        // MOD xqliu 2010-09-21 bug 15269
        TreeSelection currentSelection = ((TreeSelection) this.getContext().getSelection());
        Object firstElement = currentSelection.getFirstElement();
        if (firstElement instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) firstElement;
            if (ENodeType.SYSTEM_FOLDER.equals(node.getType()) || ENodeType.SIMPLE_FOLDER.equals(node.getType())) {
                IFolder folder = WorkbenchUtils.getFolder(node);
                String sysIndicatorFolderPath = ResourceManager.getSystemIndicatorFolder().getFullPath().toOSString();
                String selectedFolderPath = folder.getFullPath().toOSString();
                if (selectedFolderPath.startsWith(sysIndicatorFolderPath)) {
                    menu.add(new ImportIndicatorDefinitionAction());
                    menu.add(new ExportIndicatorDefinitionAction());
                }
            }
        }
        // ~ 15269
    }
}
