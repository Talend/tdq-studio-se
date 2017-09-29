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

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dataprofiler.core.ui.wizard.folder.FolderWizard;
import org.talend.dq.helper.ProxyRepositoryManager;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class CreateRepositoryNodeAction extends Action {

    public IFolder currentSelection;

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    public CreateRepositoryNodeAction(IFolder folder) {
        super(DefaultMessagesImpl.getString("CreateUserFolderProvider.createFolder")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.FOLDER_NEW_IMAGE));
        currentSelection = folder;
    }

    /*
     * (non-Javadoc) Method declared on IAction.
     */
    public void run() {
        repositoryObjectCRUD.refreshDQViewForRemoteProject();
        FolderWizard processWizard = new FolderWizard(currentSelection.getFullPath(), null);
        Shell activeShell = Display.getCurrent().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, processWizard);
        dialog.setPageSize(400, 60);
        dialog.create();
        if (WizardDialog.OK == dialog.open()) {

            ProxyRepositoryManager.getInstance().save();

        }
    }
}
