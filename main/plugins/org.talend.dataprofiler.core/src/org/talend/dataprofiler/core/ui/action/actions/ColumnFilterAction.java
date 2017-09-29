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
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.database.ColumnFilterWizard;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ColumnFilterAction extends Action {

    private static final int WIDTH = 300;

    private static final int HEIGHT = 150;

    private NamedColumnSet namedColumnSet;

    private IRepositoryNode node;

    public ColumnFilterAction() {
        super(DefaultMessagesImpl.getString("ColumnFilterAction.columnFilter")); //$NON-NLS-1$
    }

    public ColumnFilterAction(NamedColumnSet namedColumnSet) {
        this();
        this.namedColumnSet = namedColumnSet;
    }

    /**
     * DOC klliu ColumnFilterAction constructor comment.
     * 
     * @param node
     */
    public ColumnFilterAction(IRepositoryNode node) {
        this();
        this.node = node;
        if (node instanceof DBTableRepNode) {
            this.namedColumnSet = ((DBTableRepNode) node).getTdTable();
        } else if (node instanceof DBViewRepNode) {
            this.namedColumnSet = ((DBViewRepNode) node).getTdView();
        }
    }

    @Override
    public void run() {
        ColumnFilterWizard wizard = new ColumnFilterWizard(this.namedColumnSet);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(WIDTH, HEIGHT);
        if (dialog.open() == Dialog.OK) {
            // MOD klliu if set the column filter, need to clear the cashe children of Table node.
            if (node instanceof DBTableRepNode) {
                ((DBTableRepNode) node).getCasheChildren().clear();
            }
            CorePlugin.getDefault().refreshDQView(node);
        }
    }

}
