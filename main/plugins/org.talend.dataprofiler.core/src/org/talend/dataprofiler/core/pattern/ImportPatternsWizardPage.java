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
package org.talend.dataprofiler.core.pattern;

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
import org.talend.dataprofiler.core.ui.wizard.indicator.CsvFileTableViewer;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class ImportPatternsWizardPage extends WizardPage {

    private Text fileText;

    private Button skipBtn;

    private Button renameBtn;

    private CsvFileTableViewer csvViewer;

    /**
     * DOC qzhang ImportPatternsWizardPage constructor comment.
     * 
     * @param pageName
     */
    protected ImportPatternsWizardPage() {
        super(DefaultMessagesImpl.getString("ImportPatternsWizardPage.importPatternsWizardPage")); //$NON-NLS-1$

        setTitle(DefaultMessagesImpl.getString("ImportPatternsWizardPage.importPatternsFromFile")); //$NON-NLS-1$
        setDescription(DefaultMessagesImpl.getString("ImportPatternsWizardPage.chooseFileToImportPattern")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
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
        label.setText(DefaultMessagesImpl.getString("ImportPatternsWizardPage.selectFile")); //$NON-NLS-1$
        fileText = new Text(fileComp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gridData);
        fileText.addModifyListener(new ModifyListener() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
             */
            public void modifyText(ModifyEvent e) {
                updatePreview();
            }
        });
        Button button = new Button(fileComp, SWT.PUSH);
        button.setText(DefaultMessagesImpl.getString("ImportPatternsWizardPage.browsing")); //$NON-NLS-1$
        button.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
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
        group.setText(DefaultMessagesImpl.getString("ImportPatternsWizardPage.duplicatePattern")); //$NON-NLS-1$
        layout = new GridLayout();
        group.setLayout(layout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gridData);

        skipBtn = new Button(group, SWT.RADIO);
        skipBtn.setText(DefaultMessagesImpl.getString("ImportPatternsWizardPage.skipExistPatttern")); //$NON-NLS-1$
        skipBtn.setSelection(true);

        renameBtn = new Button(group, SWT.RADIO);
        renameBtn.setText(DefaultMessagesImpl.getString("ImportPatternsWizardPage.renameNewPattern")); //$NON-NLS-1$

        Label label2 = new Label(container, SWT.NONE);
        label2.setText(DefaultMessagesImpl.getString("ImportPatternsWizardPage.Preview")); //$NON-NLS-1$

        csvViewer = new CsvFileTableViewer(container, SWT.NONE);
        csvViewer.setLayoutData(new GridData(GridData.FILL_BOTH));
        setPageComplete(false);
        setControl(container);
    }

    /**
     * DOC qzhang Comment method "getXLSFile".
     * 
     * @return
     */
    public String getSourceFile() {
        return fileText.getText();
    }

    /**
     * DOC qzhang Comment method "getSkip".
     * 
     * @return
     */
    public boolean getSkip() {
        return skipBtn.getSelection();
    }

    /**
     * DOC qzhang Comment method "getRename".
     * 
     * @return
     */
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
            if (csvViewer.isEmptyDefinition()) {
                setMessage(DefaultMessagesImpl.getString("ImportPatternsWizardPage.EmptyError"), IMessageProvider.WARNING); //$NON-NLS-1$
            } else if (csvViewer.isQuotesError()) {
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
