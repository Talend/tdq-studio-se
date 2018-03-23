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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.AbstractWizardPage;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class MetaDataFilterWizardPage extends AbstractWizardPage {

    private MetaDataFilterWizard parentWizard;

    protected Text metadataFilterText;

    protected static final String MSG_FILTER_VALID = DefaultMessagesImpl.getString("TableViewColumnFilterWizardPage.filterValid"); //$NON-NLS-1$

    protected static final String MSG_FILTER_INVALID = DefaultMessagesImpl
            .getString("TableViewColumnFilterWizardPage.filterInvalid"); //$NON-NLS-1$

    public Text getMetadataFilterText() {
        return metadataFilterText;
    }

    public void setMetadataFilterText(Text metaDataFilterText) {
        this.metadataFilterText = metaDataFilterText;
    }

    public MetaDataFilterWizardPage(MetaDataFilterWizard parent) {
        this();
        this.parentWizard = parent;
    }

    public MetaDataFilterWizardPage() {
        super();
    }

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
        initPackageFilterComposite(comp, gd);
        temp = this.parentWizard.getOldPackageFilter();
        metadataFilterText = new Text(comp, SWT.BORDER);
        temp = temp == null ? "" : temp; //$NON-NLS-1$
        metadataFilterText.setText(temp);
        metadataFilterText.setLayoutData(gd);

        addFieldsListeners(metadataFilterText);
        this.setControl(comp);
    }

    private void initPackageFilterComposite(Composite comp, GridData gd) {
        Label t11 = new Label(comp, SWT.BORDER);
        t11.setText(this.parentWizard.getDatabaseConnectionItem().getConnection().getName());
        t11.setLayoutData(gd);
        Label label1 = new Label(comp, SWT.NONE);
        label1.setText(DefaultMessagesImpl.getString("MetaDataFilterWizard.PackageFilter")); //$NON-NLS-1$
    }

    @Override
    public boolean checkFieldsValue() {
        String metadataFilter = this.metadataFilterText.getText();
        if (metadataFilter.indexOf("\\") > -1 || metadataFilter.indexOf("/") > -1) { //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }
        return true;
    }

    protected void addFieldsListeners(Text text) {
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (checkFieldsValue()) {
                    updateStatus(IStatus.OK, MSG_FILTER_VALID);
                } else {
                    updateStatus(IStatus.ERROR, MSG_FILTER_INVALID);
                }
            }
        });
    }

}
