// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.wizard.database;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * created by qiongli on 2013-12-25 Detailled comment
 * 
 */
public class ColumnFilterWizardPage extends MetaDataFilterWizardPage {

    private ColumnFilterWizard parentWizard;

    private Text columnFilterText;

    public ColumnFilterWizardPage() {
        super();
    }

    public ColumnFilterWizardPage(ColumnFilterWizard parent) {
        this();
        this.parentWizard = parent;
    }

    @Override
    public void createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        GridLayout grid = new GridLayout(2, false);
        comp.setLayout(grid);

        GridData gd = new GridData();
        gd.widthHint = 280;
        gd.heightHint = 22;

        Label l1 = new Label(comp, SWT.NONE);
        l1.setText(DefaultMessagesImpl.getString("MetaDataFilterWizardPage.conn")); //$NON-NLS-1$

        String temp = "";//$NON-NLS-1$
        initColumnFilterComposite(comp, gd);
        temp = this.parentWizard.getOldColumnFilter();
        columnFilterText = new Text(comp, SWT.BORDER);
        temp = temp == null ? "" : temp; //$NON-NLS-1$
        columnFilterText.setText(temp);
        columnFilterText.setLayoutData(gd);

        addFieldsListeners(columnFilterText);
        this.setControl(comp);
    }

    private void initColumnFilterComposite(Composite comp, GridData gd) {
        Label t11 = new Label(comp, SWT.BORDER);
        t11.setText(this.parentWizard.getTdDataProvider().getName());
        t11.setLayoutData(gd);

        Label l2 = new Label(comp, SWT.NONE);
        l2.setText(DefaultMessagesImpl.getString("MetaDataFilterWizardPage.catalog")); //$NON-NLS-1$

        Label t22 = new Label(comp, SWT.BORDER);
        t22.setText(this.parentWizard.getPackageObj().getName());
        t22.setLayoutData(gd);

        Label l3 = new Label(comp, SWT.NONE);
        l3.setText(DefaultMessagesImpl.getString("MetaDataFilterWizardPage.table")); //$NON-NLS-1$

        Label t33 = new Label(comp, SWT.BORDER);
        t33.setText(this.parentWizard.getNamedColumnSet().getName());
        t33.setLayoutData(gd);
        Label label1 = new Label(comp, SWT.NONE);
        label1.setText(DefaultMessagesImpl.getString("MetaDataFilterWizardPage.columnFilter")); //$NON-NLS-1$
    }

    @Override
    public boolean checkFieldsValue() {
        String metadataFilter = this.columnFilterText.getText();
        if (metadataFilter.indexOf("\\") > -1 || metadataFilter.indexOf("/") > -1) { //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }
        return true;
    }

    public Text getColumnFilterText() {
        return this.columnFilterText;
    }

}
