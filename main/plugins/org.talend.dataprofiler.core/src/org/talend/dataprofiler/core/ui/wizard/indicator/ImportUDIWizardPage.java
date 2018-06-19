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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import java.io.File;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ImportUDIWizardPage extends WizardPage {

    private Text fileText;

    private Button skipBtn;

    private Button renameBtn;

    private CsvFileTableViewer csvViewer;

    protected ImportUDIWizardPage() {
        super(DefaultMessagesImpl.getString("ImportUDIWizardPage.importUDIWizardPage")); //$NON-NLS-1$
        setTitle(DefaultMessagesImpl.getString("ImportUDIWizardPage.importUDIFromFile")); //$NON-NLS-1$
        setDescription(DefaultMessagesImpl.getString("ImportUDIWizardPage.chooseFileToImportUDI")); //$NON-NLS-1$
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
        label.setText(DefaultMessagesImpl.getString("ImportUDIWizardPage.selectFile")); //$NON-NLS-1$
        fileText = new Text(fileComp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gridData);
        fileText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updatePreview();
            }
        });
        Button button = new Button(fileComp, SWT.PUSH);
        button.setText(DefaultMessagesImpl.getString("ImportUDIWizardPage.browsing")); //$NON-NLS-1$
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell());
                dialog.setFilterExtensions(new String[] { "*.csv" }); //$NON-NLS-1$
                if (fileText.getText() != null) {
                    dialog.setFileName(fileText.getText());
                }
                String path = dialog.open();
                if (path != null) {
                    fileText.setText(path);
                    updatePreview();
                }
            }
        });

        Group group = new Group(container, SWT.NONE);
        group.setText(DefaultMessagesImpl.getString("ImportUDIWizardPage.duplicateIndicator")); //$NON-NLS-1$
        layout = new GridLayout();
        group.setLayout(layout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gridData);

        skipBtn = new Button(group, SWT.RADIO);
        skipBtn.setText(DefaultMessagesImpl.getString("ImportUDIWizardPage.skipExistIndicator")); //$NON-NLS-1$
        skipBtn.setSelection(true);

        renameBtn = new Button(group, SWT.RADIO);
        renameBtn.setText(DefaultMessagesImpl.getString("ImportUDIWizardPage.renameNewIndicator")); //$NON-NLS-1$

        Label label2 = new Label(container, SWT.NONE);
        label2.setText(DefaultMessagesImpl.getString("ImportUDIWizardPage.Preview")); //$NON-NLS-1$

        csvViewer = new CsvFileTableViewer(container, SWT.NONE);
        csvViewer.setLayoutData(new GridData(GridData.FILL_BOTH));
        setPageComplete(false);
        setControl(container);
    }

    public String getSourceFile() {
        return fileText.getText();
    }

    public boolean getSkip() {
        return skipBtn.getSelection();
    }

    public boolean getRename() {
        return renameBtn.getSelection();
    }

    public CsvFileTableViewer getCsvViewer() {
        return csvViewer;
    }

    private void updatePreview() {
        File file = new File(fileText.getText());
        if (!file.exists()) {
            setMessage(DefaultMessagesImpl.getString("ImportPatternsWizardPage.FileNotExist"), IMessageProvider.ERROR); //$NON-NLS-1$
            setPageComplete(false);
            return;
        }
        if (csvViewer.setCsvFile(file)) {
            if (csvViewer.isHeadersInvalid()) {
                setMessage(DefaultMessagesImpl.getString("ImportPatternsWizardPage.FileHeaderInvalid"), IMessageProvider.ERROR); //$NON-NLS-1$
                setPageComplete(false);
                return;
            }
            if (csvViewer.isQuotesError()) {
                setMessage(DefaultMessagesImpl.getString("ImportPatternsWizardPage.QuoteError"), IMessageProvider.WARNING); //$NON-NLS-1$
            } else {
                setMessage(null);
            }
            setPageComplete(true);
        } else {
            setMessage(DefaultMessagesImpl.getString("ImportPatternsWizardPage.ReadError"), IMessageProvider.ERROR); //$NON-NLS-1$
            setPageComplete(false);
        }
    }
}
