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
import org.eclipse.jface.wizard.WizardDialog;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.database.TableViewFilterWizard;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableViewFilterAction extends Action {

    // private static Logger log = Logger.getLogger(TableViewFilterAction.class);

    private static final int WIDTH = 300;

    private static final int HEIGHT = 150;

    private Package packageObj;

    // ADD yyin TDQ-4959 20120503
    private RepositoryNode node;

    private TableViewFilterAction() {
        super(DefaultMessagesImpl.getString("TableViewFilterAction.tableViewFilter")); //$NON-NLS-1$
    }

    public TableViewFilterAction(Package packageObj, RepositoryNode node) {
        this();
        this.node = node;
        this.packageObj = packageObj;
    }

    @Override
    public void run() {
        TableViewFilterWizard wizard = new TableViewFilterWizard(packageObj);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(WIDTH, HEIGHT);
        dialog.open();
        CorePlugin.getDefault().refreshDQView(node);
    }
}
