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

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.filters.DQFolderFilter;
import org.talend.dataprofiler.core.ui.utils.DQCheckedTreeViewer;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class ExportPatternsWizardPage extends WizardPage {

    protected static Logger log = Logger.getLogger(ExportPatternsWizardPage.class);

    private IFolder folder;

    private Text fileText;

    private ProgressBar progressBar;

    private DQCheckedTreeViewer selectedPatternsTree;

    private boolean isForExchange;

    private RepositoryNode node;

    /**
     * DOC zqin ExportPatternsWizardPage constructor comment.
     */
    public ExportPatternsWizardPage(IFolder folder, boolean isForExchange) {
        super(DefaultMessagesImpl.getString("ExportPatternsWizardPage.exportPatternWizardPage")); //$NON-NLS-1$

        if (isForExchange) {
            setTitle(DefaultMessagesImpl.getString("ExportPatternsWizardPage.ExportPatternForExchange")); //$NON-NLS-1$
            setDescription(DefaultMessagesImpl.getString("ExportPatternsWizardPage.ChooseFolder")); //$NON-NLS-1$
        } else {
            setTitle(DefaultMessagesImpl.getString("ExportPatternsWizardPage.exportPatternToFile")); //$NON-NLS-1$
            setDescription(DefaultMessagesImpl.getString("ExportPatternsWizardPage.chooseFileToExportPattern")); //$NON-NLS-1$
        }

        this.isForExchange = isForExchange;
        this.folder = folder;
    }

    /**
     * DOC klliu ExportPatternsWizardPage constructor comment.
     * 
     * @param node
     * @param isForExchange
     */
    public ExportPatternsWizardPage(RepositoryNode node, boolean isForExchange) {
        super(DefaultMessagesImpl.getString("ExportPatternsWizardPage.exportPatternWizardPage")); //$NON-NLS-1$

        if (isForExchange) {
            setTitle(DefaultMessagesImpl.getString("ExportPatternsWizardPage.ExportPatternForExchange")); //$NON-NLS-1$
            setDescription(DefaultMessagesImpl.getString("ExportPatternsWizardPage.ChooseFolder")); //$NON-NLS-1$
        } else {
            setTitle(DefaultMessagesImpl.getString("ExportPatternsWizardPage.exportPatternToFile")); //$NON-NLS-1$
            setDescription(DefaultMessagesImpl.getString("ExportPatternsWizardPage.chooseFileToExportPattern")); //$NON-NLS-1$
        }
        this.node = node;
        this.isForExchange = isForExchange;
        this.folder = WorkbenchUtils.getFolder(node);
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
        if (isForExchange) {
            label.setText(DefaultMessagesImpl.getString("ExportPatternsWizardPage.SelectFolder")); //$NON-NLS-1$
        } else {
            label.setText(DefaultMessagesImpl.getString("ExportPatternsWizardPage.selectFile")); //$NON-NLS-1$
        }
        fileText = new Text(fileComp, SWT.BORDER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gridData);
        fileText.setEditable(false);

        Button button = new Button(fileComp, SWT.PUSH);
        button.setText(DefaultMessagesImpl.getString("ExportPatternsWizardPage.browse")); //$NON-NLS-1$
        button.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
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
        group.setText(DefaultMessagesImpl.getString("ExportPatternsWizardPage.selectPatternss")); //$NON-NLS-1$
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_BOTH));

        selectedPatternsTree = new DQCheckedTreeViewer(group);
        selectedPatternsTree.addFilter(new DQFolderFilter(true));
        selectedPatternsTree.setInput(this.node);
        selectedPatternsTree.setWizardPage(this);

        GridDataFactory.fillDefaults().grab(true, true).applyTo(selectedPatternsTree.getTree());

        try {
            selectedPatternsTree.setCheckedElements(folder.members());
        } catch (CoreException e1) {
            log.error(e1, e1);
        }

        createSelectionButtons(container);
        Composite monitorComp = new Composite(container, SWT.NONE);
        monitorComp.setLayout(new GridLayout());
        monitorComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        ProgressBar bar = new ProgressBar(monitorComp, SWT.NONE);
        bar.setLayoutData(new GridData(GridData.FILL_BOTH));
        bar.setVisible(false);

        setControl(container);
    }

    /**
     * Adds the selection and deselection buttons on the dialog.for 8116.
     * 
     * @param composite
     * @return
     */
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

    /**
     * Add the listeners for all select(deselect) button.
     * 
     * @param selectButton
     * @param deselectButton
     */
    protected void addSelectionButtonListener(Button selectButton, Button deselectButton) {
        SelectionListener listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // MOD msjian 2012-1-17 TDQ-4066: set the checked elememts correctly
                selectedPatternsTree.setCheckedElements(((ResourceViewContentProvider) selectedPatternsTree.getContentProvider())
                        .getElements(node));
                // TDQ-4066 ~
            }
        };
        selectButton.addSelectionListener(listener);

        listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                selectedPatternsTree.setCheckedElements(new Object[0]);
                selectedPatternsTree.updateWizardStatus();
            }
        };
        deselectButton.addSelectionListener(listener);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public CheckboxTreeViewer getSelectedPatternsTree() {
        return selectedPatternsTree;
    }

    public String getTargetFile() {
        return fileText.getText();
    }
}
