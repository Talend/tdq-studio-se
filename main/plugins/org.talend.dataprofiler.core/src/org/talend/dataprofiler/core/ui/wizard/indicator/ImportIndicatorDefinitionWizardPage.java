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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ImportIndicatorDefinitionWizardPage extends WizardPage {

    protected static Logger log = Logger.getLogger(ImportIndicatorDefinitionWizardPage.class);

    private Text fileText;

    private ProgressBar progressBar;

    public ImportIndicatorDefinitionWizardPage() {
        super(DefaultMessagesImpl.getString("ImportIndicatorDefinitionWizardPage.ImportSystemIndicator")); //$NON-NLS-1$
        setTitle(DefaultMessagesImpl.getString("ImportIndicatorDefinitionWizardPage.ImportSystemIndicators")); //$NON-NLS-1$
        setDescription(DefaultMessagesImpl.getString("ImportIndicatorDefinitionWizardPage.ChooseFile")); //$NON-NLS-1$
    }

    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        GridData gridData = new GridData(GridData.FILL_BOTH);
        container.setLayout(layout);
        container.setLayoutData(gridData);

        Composite fileComp = new Composite(container, SWT.NONE);
        layout = new GridLayout(3, false);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileComp.setLayout(layout);
        fileComp.setLayoutData(gridData);
        Label label = new Label(fileComp, SWT.NONE);
        label.setText(DefaultMessagesImpl.getString("ImportIndicatorDefinitionWizardPage.SelectFile")); //$NON-NLS-1$

        fileText = new Text(fileComp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gridData);
        fileText.setEditable(false);

        Button button = new Button(fileComp, SWT.PUSH);
        button.setText(DefaultMessagesImpl.getString("ExportUDIWizardPage.browse")); //$NON-NLS-1$
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String path = ""; //$NON-NLS-1$

                FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell());
                dialog.setFilterExtensions(new String[] { "*.definition" }); //$NON-NLS-1$
                if (fileText.getText() != null) {
                    dialog.setFileName(fileText.getText());
                }

                path = dialog.open();
                if (path != null) {
                    fileText.setText(path);
                }
            }
        });

        setControl(container);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public String getTargetFile() {
        return fileText.getText();
    }
}
