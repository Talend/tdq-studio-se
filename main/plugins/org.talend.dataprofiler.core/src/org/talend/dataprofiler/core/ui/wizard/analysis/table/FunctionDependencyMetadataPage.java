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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.model.bridge.ReponsitoryContextBridge;

/**
 * DOC jet class global comment. Detailled comment
 */
public class FunctionDependencyMetadataPage extends WizardPage {

    // protected members
    protected Text nameText;

    protected Text purposeText;

    protected Text descriptionText;

    protected Text authorText;

    protected Text versionText;

    protected Button button;

    protected CCombo statusText;

    protected Text pathText;

    /**
     * DOC jet FunctionDependencyMetadataPage constructor comment.
     * 
     * @param pageName
     */
    protected FunctionDependencyMetadataPage(String pageName) {
        super(pageName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {

        Composite container = new Composite(parent, SWT.NONE);
        GridLayout gdLayout = new GridLayout(2, false);
        container.setLayout(gdLayout);

        GridData data;

        // Name
        Label nameLab = new Label(container, SWT.NONE);
        nameLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.name")); //$NON-NLS-1$

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Purpose
        Label purposeLab = new Label(container, SWT.NONE);
        purposeLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.purpose")); //$NON-NLS-1$

        purposeText = new Text(container, SWT.BORDER);
        purposeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Description
        Label descriptionLab = new Label(container, SWT.NONE);
        descriptionLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.description")); //$NON-NLS-1$
        descriptionLab.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        descriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 60;
        descriptionText.setLayoutData(data);

        // Author
        Label authorLab = new Label(container, SWT.NONE);
        authorLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.author")); //$NON-NLS-1$

        authorText = new Text(container, SWT.BORDER);
        authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        authorText.setText(ReponsitoryContextBridge.getAuthor());

        setControl(container);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, "org.talend.dataprofiler.help.helpDQRules"); //$NON-NLS-1$
    }
}
