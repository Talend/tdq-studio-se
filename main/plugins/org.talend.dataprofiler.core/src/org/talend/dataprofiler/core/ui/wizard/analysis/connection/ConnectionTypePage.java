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
package org.talend.dataprofiler.core.ui.wizard.analysis.connection;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;
import org.talend.repository.ui.wizards.metadata.connection.files.delimited.DelimitedFileWizard;
import org.talend.resource.EResourceConstant;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class ConnectionTypePage extends WizardPage {

    private String defaultInfor = DefaultMessagesImpl.getString("ConnnectionTypePage.defaultInfor"); //$NON-NLS-1$

    private CCombo connectionType;

    private Composite relatedComposite;

    /**
     * DOC yyin ConnectionTypePage constructor comment.
     * 
     * @param pageName
     */
    protected ConnectionTypePage(String pageName) {
        super(pageName);
    }

    public ConnectionTypePage(Composite relatedComposite) {
        super("");
        setTitle(defaultInfor);
        this.relatedComposite = relatedComposite;
    }

    /*
     * Create the composites, initialize it and add controls.
     */
    public void createControl(Composite parent) {
        // create the dropdown list to select the connection type
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());

        // COnnection type
        Label selectionLab = new Label(container, SWT.NONE);
        selectionLab.setText(DefaultMessagesImpl.getString("ConnnectionTypePage.typeSelect")); //$NON-NLS-1$

        connectionType = new CCombo(container, SWT.BORDER);

        connectionType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        connectionType.setEditable(false);

        // TODO support MDM later.
        connectionType.add(Messages.getString("DQRepositoryViewLabelProvider.DBConnectionFolderName")); //$NON-NLS-1$
        connectionType.add(Messages.getString("DQRepositoryViewLabelProvider.DFConnectionFolderName")); //$NON-NLS-1$
        connectionType.select(0);
        setControl(container);
        super.setPageComplete(true);
    }

    @Override
    public boolean canFlipToNextPage() {
        return true;
    }

    @Override
    public IWizardPage getNextPage() {
        RepositoryNode node = null;
        IWizard nextWizard = null;
        // make the next wizard do not open the created connection
        ITDQRepositoryService tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(
                ITDQRepositoryService.class);
        tdqRepService.setIsOpenConnectionEditorAfterCreate(Boolean.TRUE);

        int selectionIndex = connectionType.getSelectionIndex();
        switch (selectionIndex) {
        case 0:// db
            node = (RepositoryNode) RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.DB_CONNECTIONS);
            nextWizard = new DatabaseWizard(PlatformUI.getWorkbench(), true, node, null);
            break;
        case 1:// file
            node = (RepositoryNode) RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.FILEDELIMITED);
            nextWizard = new DelimitedFileWizard(PlatformUI.getWorkbench(), true, node, null);
            break;
        default:
            break;
        }
        WizardDialog dialog = new WizardDialog(null, nextWizard);
        dialog.setPageSize(550, 550);
        if (WizardDialog.OK == dialog.open()) {
            publishSelectDataEvent(nextWizard, tdqRepService);
        }
        // make it back to initial value if cancel it
        tdqRepService.setIsOpenConnectionEditorAfterCreate(Boolean.FALSE);

        return null;
    }

    private void publishSelectDataEvent(IWizard nextWizard, ITDQRepositoryService tdqRepService) {
        int selectionIndex = connectionType.getSelectionIndex();
        ConnectionItem connectionItem = null;
        switch (selectionIndex) {
        case 0:
            connectionItem = ((DatabaseWizard) nextWizard).getConnectionItem();
            break;
        case 1:
            connectionItem = ((DelimitedFileWizard) nextWizard).getConnectionItem();
            break;
        default:
            break;
        }
        if (connectionItem != null) {
            Connection connection = connectionItem.getConnection();
            ((WizardDialog) getWizard().getContainer()).close();
            // make it back to initial value after finish creating
            tdqRepService.setIsOpenConnectionEditorAfterCreate(Boolean.FALSE);

            EventManager.getInstance().publish(relatedComposite, EventEnum.DQ_SELECT_ELEMENT_AFTER_CREATE_CONNECTION, connection);
        }
    }

    public void checkForCompletion() {
        setPageComplete(false);

        getWizard().getContainer().updateButtons();

    }
}
