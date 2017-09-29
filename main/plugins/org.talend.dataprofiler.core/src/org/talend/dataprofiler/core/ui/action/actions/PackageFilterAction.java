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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.database.MetaDataFilterWizard;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class PackageFilterAction extends Action {

    private static final int WIDTH = 300;

    private static final int HEIGHT = 150;

    private IRepositoryNode node;

    public PackageFilterAction() {
        super(DefaultMessagesImpl.getString("PackageFilterAction.PackageFilter")); //$NON-NLS-1$
    }

    /**
     * DOC klliu PackageFilterAction constructor comment.
     * 
     * @param node
     */
    public PackageFilterAction(IRepositoryNode node) {
        this();
        if (node instanceof DBConnectionRepNode) {
            this.node = node;
        }
    }

    @Override
    public void run() {
        DatabaseConnectionItem databaseConnectionItem = (DatabaseConnectionItem) node.getObject().getProperty().getItem();
        MetaDataFilterWizard wizard = new MetaDataFilterWizard(databaseConnectionItem);
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        dialog.setPageSize(WIDTH, HEIGHT);
        if (dialog.open() == Dialog.OK) {
            CorePlugin.getDefault().refreshDQView(node);
        }
    }

}
