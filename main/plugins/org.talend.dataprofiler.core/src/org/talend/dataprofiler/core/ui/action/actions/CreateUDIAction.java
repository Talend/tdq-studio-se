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
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IHelpResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataprofiler.core.ui.wizard.indicator.NewUDIndicatorWizard;
import org.talend.dataprofiler.help.HelpPlugin;
import org.talend.dq.analysis.parameters.UDIndicatorParameter;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.resource.ResourceManager;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class CreateUDIAction extends Action implements ICheatSheetAction {

    private IFolder folder;

    public CreateUDIAction() {
        setText(DefaultMessagesImpl.getString("UserDefinedIndicatorsActionProvider.newIndicator")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ADD_IND_DEFINITION));
    }

    public CreateUDIAction(IFolder folder) {
        this();
        this.folder = folder;
    }

    @Override
    public void run() {
        if (this.folder == null) {
            this.folder = ResourceManager.getUDIFolder();
        }
        UDIndicatorParameter parameter = new UDIndicatorParameter();
        FolderProvider folderProvider = new FolderProvider();
        folderProvider.setFolderResource(folder);
        parameter.setFolderProvider(folderProvider);
        NewUDIndicatorWizard fileWizard = WizardFactory.createNewUDIWizard(parameter);
        fileWizard.setWindowTitle(getText());
        IContext context = HelpSystem.getContext(HelpPlugin.getDefault().getUDIndicatorHelpContextID());
        IHelpResource[] relatedTopics = context.getRelatedTopics();
        String href = relatedTopics[0].getHref();
        WizardDialog dialog = new OpeningHelpWizardDialog(null, fileWizard, href);
        if (WizardDialog.OK == dialog.open()) {
            ProxyRepositoryManager.getInstance().save();
        }
    }

    public void run(String[] params, ICheatSheetManager manager) {
        // ADD xqliu TDQ-4285 2011-12-27
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        // ~ TDQ-4285

        run();
    }
}
