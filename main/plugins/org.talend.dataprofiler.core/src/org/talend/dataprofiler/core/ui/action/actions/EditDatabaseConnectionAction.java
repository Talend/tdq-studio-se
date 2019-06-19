// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;

/**
 * created by msjian on 2019年6月19日
 * Detailled comment
 *
 */
public class EditDatabaseConnectionAction extends Action {

    private IRepositoryNode node;

    public EditDatabaseConnectionAction(IRepositoryNode node) {
        this.node = node;
        setText(DefaultMessagesImpl.getString("EditDatabaseConnectionAction.Editconnection"));//$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.CONNECTION));
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (node != null) {
            Wizard wizard = new DatabaseWizard(PlatformUI.getWorkbench(), false, (RepositoryNode) node, null);
            WizardDialog dialog = new WizardDialog(null, wizard);
            if (Window.OK == dialog.open()) {
                CorePlugin.getDefault().refreshDQView(node);
            }
        }
    }
}
