// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dq.helper.PropertyHelper;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;

/**
 * created by msjian on 2019年6月19日
 * Detailled comment
 *
 */
public class EditDatabaseConnectionAction extends Action {

    protected static Logger log = Logger.getLogger(EditDatabaseConnectionAction.class);

    private IRepositoryNode node;

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    public EditDatabaseConnectionAction(IRepositoryNode node) {
        this.node = node;
        setText(DefaultMessagesImpl.getString("EditDatabaseConnectionAction.Editconnection"));//$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.CONNECTION));
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (node != null) {
            // TDQ-18643: fix connection to show the latest used ref project context info
            RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("Open a database connection") {//$NON-NLS-1$

                @Override
                protected void run() {
                    repositoryObjectCRUD.refreshDQViewForRemoteProject();

                    if (!repositoryObjectCRUD.isSelectionAvailable()) {
                        repositoryObjectCRUD.showWarningDialog();
                        return;
                    }
                    try {
                        ProxyRepositoryFactory.getInstance().reload(node.getObject().getProperty());
                        IFile objFile = PropertyHelper.getItemFile(node.getObject().getProperty());
                        objFile.refreshLocal(IResource.DEPTH_INFINITE, null);
                    } catch (Exception e1) {
                        log.error(e1, e1);
                    }

                    // TDQ-18173 msjian: update context propagation over reference project
                    try {
                        RepositoryUpdateManager.updateConnectionContextParam((RepositoryNode) node);
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                    // TDQ-18173~

                    Wizard wizard = new DatabaseWizard(PlatformUI.getWorkbench(), false, (RepositoryNode) node, null);
                    WizardDialog dialog = new WizardDialog(null, wizard);
                    if (Window.OK == dialog.open()) {
                        CorePlugin.getDefault().refreshDQView(node);
                    }
                }
            };
            workUnit.setAvoidUnloadResources(false);
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
        }
    }
}
