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
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.DQCheckedTreeViewer;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.EResourceConstant;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ExportUDIWizardPage extends WizardPage {

    protected static Logger log = Logger.getLogger(ExportUDIWizardPage.class);

    private IFolder folder;

    private IRepositoryNode udiFolder;

    private Text fileText;

    private ProgressBar progressBar;

    private DQCheckedTreeViewer selectedTree;

    private boolean isForExchange;

    public ExportUDIWizardPage(IFolder folder, boolean isForExchange) {
        super(DefaultMessagesImpl.getString("ExportUDIWizardPage.exportUDIWizardPage")); //$NON-NLS-1$

        if (isForExchange) {
            setTitle(DefaultMessagesImpl.getString("ExportUDIWizardPage.ExportIndicatorForExchange")); //$NON-NLS-1$
            setDescription(DefaultMessagesImpl.getString("ExportUDIWizardPage.ChooseFolderExpIndicator")); //$NON-NLS-1$
        } else {
            setTitle(DefaultMessagesImpl.getString("ExportUDIWizardPage.exportUDIToCSVFile")); //$NON-NLS-1$
            setDescription(DefaultMessagesImpl.getString("ExportUDIWizardPage.chooseFolderToExportIndicators")); //$NON-NLS-1$
        }
        udiFolder = RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.USER_DEFINED_INDICATORS);
        this.folder = folder;
        this.isForExchange = isForExchange;
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
        if (isForExchange) {
            label.setText(DefaultMessagesImpl.getString("ExportUDIWizardPage.SelectFolder")); //$NON-NLS-1$
        } else {
            label.setText(DefaultMessagesImpl.getString("ExportPatternsWizardPage.selectFile")); //$NON-NLS-1$
        }
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

                if (isForExchange) {
                    DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
                    if (fileText.getText() != null) {
                        dialog.setFilterPath(fileText.getText());
                    }
                    path = dialog.open();
                } else {
                    FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell());
                    dialog.setFilterExtensions(new String[] { "*.csv" }); //$NON-NLS-1$
                    if (fileText.getText() != null) {
                        dialog.setFileName(fileText.getText());
                    }

                    path = dialog.open();
                    if (path != null && !path.endsWith(".csv")) { //$NON-NLS-1$
                        path = path + ".csv"; //$NON-NLS-1$
                    }
                }

                if (path != null) {
                    fileText.setText(path);
                }
            }
        });

        Group group = new Group(container, SWT.NONE);
        group.setText(DefaultMessagesImpl.getString("ExportUDIWizardPage.selectIndicators")); //$NON-NLS-1$
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_BOTH));

        selectedTree = new DQCheckedTreeViewer(group);
        selectedTree.setInput(RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.USER_DEFINED_INDICATORS));
        selectedTree.setWizardPage(this);

        GridDataFactory.fillDefaults().grab(true, true).applyTo(selectedTree.getTree());

        selectedTree.setCheckedElements(udiFolder.getChildren().toArray());

        // FIXME buttonComposite never used.
        Control buttonComposite = createSelectionButtons(container);
        Composite monitorComp = new Composite(container, SWT.NONE);
        monitorComp.setLayout(new GridLayout());
        monitorComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        ProgressBar bar = new ProgressBar(monitorComp, SWT.NONE);
        bar.setLayoutData(new GridData(GridData.FILL_BOTH));
        bar.setVisible(false);

        setControl(container);
    }

    protected Composite createSelectionButtons(Composite composite) {
        Composite buttonComposite = new Composite(composite, SWT.RIGHT);
        GridLayout layout = new GridLayout(2, false);
        buttonComposite.setLayout(layout);
        buttonComposite.setFont(composite.getFont());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.GRAB_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        composite.setData(data);

        Button selectButton = new Button(buttonComposite, SWT.PUSH);
        selectButton.setText(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.selectAll")); //$NON-NLS-1$

        Button deselectButton = new Button(buttonComposite, SWT.PUSH);
        deselectButton.setText(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.deselectAll")); //$NON-NLS-1$

        addSelectionButtonListener(selectButton, deselectButton);
        return buttonComposite;
    }

    protected void addSelectionButtonListener(Button selectButton, Button deselectButton) {
        SelectionListener listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {

                selectedTree.setCheckedElements(udiFolder.getChildren().toArray());

            }
        };
        selectButton.addSelectionListener(listener);

        listener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                selectedTree.setCheckedElements(new Object[0]);
                selectedTree.updateWizardStatus();
            }
        };
        deselectButton.addSelectionListener(listener);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public CheckboxTreeViewer getSelectedTree() {
        return selectedTree;
    }

    public String getTargetFile() {
        return fileText.getText();
    }
}
