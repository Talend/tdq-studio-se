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
package org.talend.dataprofiler.core.ui.action.actions;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataprofiler.core.ui.wizard.dqrules.NewDQRulesWizard;
import org.talend.dq.analysis.parameters.DQRulesParameter;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.resource.ResourceManager;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class CreateDQRulesAction extends Action implements ICheatSheetAction {

    private IFolder folder;

    public CreateDQRulesAction() {
        setText(DefaultMessagesImpl.getString("DQRulesAction.newDQRule")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ADD_DQ));
        this.folder = ResourceManager.getRulesSQLFolder();
    }

    public CreateDQRulesAction(IFolder folder) {
        setText(DefaultMessagesImpl.getString("DQRulesAction.newDQRule")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ADD_DQ));
        this.folder = folder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        DQRulesParameter parameter = new DQRulesParameter();
        FolderProvider folderProvider = new FolderProvider();
        folderProvider.setFolderResource(folder);
        parameter.setFolderProvider(folderProvider);
        NewDQRulesWizard fileWizard = WizardFactory.createNewDQRuleWizard(parameter);
        fileWizard.setWindowTitle(getText());
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), fileWizard);
        if (WizardDialog.OK == dialog.open())
            ProxyRepositoryManager.getInstance().save();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
     * org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] params, ICheatSheetManager manager) {
        // ADD xqliu TDQ-4285 2011-12-27
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        // ~ TDQ-4285

        run();
    }
}
