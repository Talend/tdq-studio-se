// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.analysis.parameters.DBConnectionParameter;
import org.talend.dq.nodes.MDMConnectionRepNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class CreateConnectionAction extends Action implements ICheatSheetAction {

    private static final int WIDTH = 550;

    private static final int HEIGHT = 550;

    private static final int MDM_CONNECTION_TYPE_VALUE = 1;

    private IFolder folder;

    private RepositoryNode node;

    public CreateConnectionAction() {
        super(DefaultMessagesImpl.getString("CreateConnectionAction.newConnection")); //$NON-NLS-1$
        // setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.NEW_CONNECTION));
    }

    public CreateConnectionAction(IFolder folder) {
        this();
        this.folder = folder;
        // MOD qiongli bug 14203
        if (ResourceService.isSubFolder(ResourceManager.getMDMConnectionFolder(), folder)) {
            setImageDescriptor(ImageLib.createAddedIcon(ImageLib.MDM_CONNECTION));
        } else {
            setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.NEW_CONNECTION));
        }
    }

    public CreateConnectionAction(RepositoryNode node) {
        this();
        this.node = node;
        // MOD qiongli bug 14203
        if (node instanceof MDMConnectionRepNode) {
            setImageDescriptor(ImageLib.createAddedIcon(ImageLib.MDM_CONNECTION));
        } else {
            setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.NEW_CONNECTION));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        DBConnectionParameter connectionParam = new DBConnectionParameter();
        connectionParam.setParameters(new Properties());

        if (folder != null) {
            FolderProvider provider = new FolderProvider();
            provider.setFolderResource(folder);
            connectionParam.setFolderProvider(provider);
        }
        Wizard wizard = new DatabaseWizard(PlatformUI.getWorkbench(), true, node, null);

        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(WIDTH, HEIGHT);
        wizard.setContainer(dialog);
        dialog.open();

        CorePlugin.getDefault().refreshDQView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
     * org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] params, ICheatSheetManager manager) {
        if (params == null || params.length == 0) {
            return;
        }
        Integer connectionType = null;
        if (NumberUtils.isNumber(params[0])) {
            connectionType = NumberUtils.toInt(params[0]);
        }
        switch (connectionType) {
        case MDM_CONNECTION_TYPE_VALUE:
            this.folder = ResourceManager.getMDMConnectionFolder();
            break;
        default:
            this.folder = ResourceManager.getConnectionFolder();
        }
        run();
    }
}
