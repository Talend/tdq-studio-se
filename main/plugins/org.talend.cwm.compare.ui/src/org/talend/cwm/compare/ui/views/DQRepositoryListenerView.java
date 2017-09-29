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
package org.talend.cwm.compare.ui.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class DQRepositoryListenerView extends ViewPart implements ISelectionListener {

    private Label label;

    public DQRepositoryListenerView() {
        super();
    }

    @Override
    public void setFocus() {
        label.setFocus();
    }

    @Override
    public void createPartControl(Composite parent) {
        label = new Label(parent, 0);
        label.setText(""); //$NON-NLS-1$
        getViewSite().getPage().addSelectionListener(this);
    }

    /**
     * @see ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
     */
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            Object first = ((IStructuredSelection) selection).getFirstElement();
            if (first instanceof Connection) {
                label.setText(((Connection) first).getName() + "\n\r" + first.getClass().toString()); //$NON-NLS-1$
            } else if (first instanceof Catalog) {
                label.setText(((Catalog) first).getName() + "\n\r" + first.getClass().toString()); //$NON-NLS-1$
            } else if (first instanceof Schema) {
                label.setText(((Schema) first).getName() + "\n\r" + first.getClass().toString()); //$NON-NLS-1$
            } else if (first instanceof TdTable) {
                label.setText(((TdTable) first).getName() + "\n\r" + first.getClass().toString()); //$NON-NLS-1$
            } else if (first instanceof TdView) {
                label.setText(((TdView) first).getName() + "\n\r" + first.getClass().toString()); //$NON-NLS-1$
            } else if (first instanceof TdColumn) {
                label.setText(((TdColumn) first).getName() + "\n\r" + first.getClass().toString()); //$NON-NLS-1$
            } else {
                label.setText(first.toString() + "\n\r" + first.getClass().toString()); //$NON-NLS-1$
            }
        }
    }
}
