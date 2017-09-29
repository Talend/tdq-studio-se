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
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.table.files.FileDelimitedTableWizard;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class EditDFTableAction extends Action {

    private DFTableRepNode tableNode = null;

    public EditDFTableAction(IRepositoryNode node) {
        this.tableNode = (DFTableRepNode) node;
        setText(DefaultMessagesImpl.getString("EditDFTableAction.EditDFTable"));//$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.METADATA));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (tableNode != null) {
            RepositoryNode parent = tableNode.getParent();
            Item item = parent.getObject().getProperty().getItem();
            if (item != null) {
                ConnectionItem connItem = (ConnectionItem) item;
                FileDelimitedTableWizard wizard = new FileDelimitedTableWizard(PlatformUI.getWorkbench(), false, connItem,
                        tableNode.getMetadataTable(), false);
                wizard.setRepositoryObject(tableNode.getObject());
                WizardDialog dialog = new WizardDialog(null, wizard);
                if (Window.OK == dialog.open()) {
                    CorePlugin.getDefault().refreshDQView(tableNode);
                }
            }
        }
        super.run();
    }
}
