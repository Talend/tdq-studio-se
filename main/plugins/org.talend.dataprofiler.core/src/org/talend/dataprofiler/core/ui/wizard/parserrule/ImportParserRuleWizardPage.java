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
package org.talend.dataprofiler.core.ui.wizard.parserrule;

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
 * DOC klliu class global comment. Detailled comment
 */
public class ImportParserRuleWizardPage extends WizardPage {

    private Text fileText;

    private Button skipBtn;

    private Button renameBtn;

    private CsvFileTableViewer csvViewer;

    /**
     * DOC klliu ImportParserRuleWizardPage constructor comment.
     * 
     * @param pageName
     */
    protected ImportParserRuleWizardPage() {
        super(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.importParserRuleWizardPage")); //$NON-NLS-1$

        setTitle(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.importParserRuleFromFile")); //$NON-NLS-1$
        setDescription(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.chooseFileToImportPattern")); //$NON-NLS-1$
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
        label.setText(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.selectFile")); //$NON-NLS-1$
        fileText = new Text(fileComp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gridData);
        fileText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updatePreview();
            }
        });
        Button button = new Button(fileComp, SWT.PUSH);
        button.setText(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.browsing")); //$NON-NLS-1$
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
        group.setText(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.duplicatePattern")); //$NON-NLS-1$
        layout = new GridLayout();
        group.setLayout(layout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        group.setLayoutData(gridData);

        skipBtn = new Button(group, SWT.RADIO);
        skipBtn.setText(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.skipExistPatttern")); //$NON-NLS-1$
        skipBtn.setSelection(true);

        renameBtn = new Button(group, SWT.RADIO);
        renameBtn.setText(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.renameNewPattern")); //$NON-NLS-1$

        Label label2 = new Label(container, SWT.NONE);
        label2.setText(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.Preview")); //$NON-NLS-1$

        csvViewer = new CsvFileTableViewer(container, SWT.NONE);
        csvViewer.setLayoutData(new GridData(GridData.FILL_BOTH));
        setPageComplete(false);
        setControl(container);
    }

    /**
     * DOC klliu Comment method "getXLSFile".
     * 
     * @return
     */
    public String getSourceFile() {
        return fileText.getText();
    }

    /**
     * DOC klliu Comment method "getSkip".
     * 
     * @return
     */
    public boolean getSkip() {
        return skipBtn.getSelection();
    }

    /**
     * DOC klliu Comment method "getRename".
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
            setMessage(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.FileNotExist"), IMessageProvider.ERROR); //$NON-NLS-1$
            setPageComplete(false);
            return;
        }
        if (csvViewer.setCsvFile(file)) {
            if (csvViewer.isHeadersInvalid()) {
                setMessage(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.FileHeaderInvalid"), IMessageProvider.ERROR); //$NON-NLS-1$
                setPageComplete(false);
                return;
            }
            if (csvViewer.isQuotesError()) {
                setMessage(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.QuoteError"), IMessageProvider.WARNING); //$NON-NLS-1$
            } else {
                setMessage(null);
            }
            setPageComplete(true);
        } else {
            setMessage(DefaultMessagesImpl.getString("ImportParserRuleWizardPage.ReadError"), IMessageProvider.ERROR); //$NON-NLS-1$
            setPageComplete(false);
        }

    }
}
