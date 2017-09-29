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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.talend.dataprofiler.core.ui.action.actions.TdAddTaskAction;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * DOC mzhao class global comment. Detailled comment
 */
public class ModelElementTaskProvider extends AbstractCommonActionProvider {

    private TdAddTaskAction addTaskAction = null;

    private ICommonActionExtensionSite site = null;

    public ModelElementTaskProvider() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    @Override
    public void init(ICommonActionExtensionSite site) {
        this.site = site;
    }

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
        TreeSelection currentSelection = ((TreeSelection) this.getContext().getSelection());
        // MOD by hcheng 07-28-2009,for 8273,Remove the "add task" menu on the system indicators.
        Object firstElement = currentSelection.getFirstElement();
        if (firstElement instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) firstElement;
            addTaskAction = new TdAddTaskAction(site.getViewSite().getShell(), node);
            menu.add(addTaskAction);
            // RepositoryNode parent = node.getParent();
            // if (!(parent instanceof ReportSubFolderRepNode)) {
            // Item item = node.getObject().getProperty().getItem();
            // if (item instanceof TDQAnalysisItem || item instanceof TDQReportItem || item instanceof ConnectionItem) {
            // // IPath append = WorkbenchUtils.getFilePath(node);
            // // IFile file = ResourceManager.getRootProject().getFile(append);
            // // addTaskAction = new TdAddTaskAction(site.getViewSite().getShell(), file);
            // addTaskAction = new TdAddTaskAction(site.getViewSite().getShell(), node);
            // menu.add(addTaskAction);
            //
            // }
            // }

        }
    }

    // /**
    // * DOC xqliu Comment method "shouldShowAddTask".
    // *
    // * @param node
    // * @return
    // */
    // private boolean shouldShowAddTask(RepositoryNode node) {
    // return (node instanceof AnalysisRepNode || node instanceof ReportRepNode || node instanceof MDMConnectionRepNode
    // || node instanceof DBConnectionRepNode || node instanceof DFConnectionRepNode
    // || node instanceof DBConnectionRepNode || node instanceof DBCatalogRepNode || node instanceof DBSchemaRepNode
    // || node instanceof DBTableRepNode || node instanceof DBViewRepNode || node instanceof DBColumnRepNode
    // || node instanceof DFColumnRepNode || node instanceof DFTableRepNode || node instanceof ReportAnalysisRepNode);
    // }
}
