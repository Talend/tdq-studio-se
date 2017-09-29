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
import org.talend.dataprofiler.core.ui.wizard.matchrule.NewMatchRuleWizard;
import org.talend.dataquality.record.linkage.constant.RecordMatcherType;
import org.talend.dq.analysis.parameters.DQMatchRuleParameter;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.resource.ResourceManager;

/**
 * created by zshen on Aug 19, 2013 Detailled comment
 * 
 */
public class CreateMatcherRuleAction extends Action implements ICheatSheetAction {

    private IFolder folder;

    private RecordMatcherType defaultAlgorithmType;

    public CreateMatcherRuleAction() {
        this(ResourceManager.getRulesMatcherFolder());
    }

    public CreateMatcherRuleAction(IFolder folder) {
        this(folder, RecordMatcherType.simpleVSRMatcher);
    }

    public CreateMatcherRuleAction(IFolder folder, RecordMatcherType defaultAlgorithmType) {
        setText(DefaultMessagesImpl.getString("CreateMatcherRuleAction.newMatchRule")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.MATCH_RULE_ICON));
        this.folder = folder;
        this.defaultAlgorithmType = defaultAlgorithmType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        doRun();
    }

    public boolean doRun() {
        DQMatchRuleParameter parameter = new DQMatchRuleParameter();
        FolderProvider folderProvider = new FolderProvider();
        folderProvider.setFolderResource(folder);
        parameter.setFolderProvider(folderProvider);
        parameter.setDefaultAlgorithmType(defaultAlgorithmType);
        NewMatchRuleWizard matchWizard = WizardFactory.createNewMatchRuleWizard(parameter);
        matchWizard.setWindowTitle(getText());
        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), matchWizard);
        if (WizardDialog.OK == dialog.open()) {
            ProxyRepositoryManager.getInstance().save();
            return true;
        }
        return false;
    }

    /**
     * support cheat sheet action
     */
    public void run(String[] params, ICheatSheetManager manager) {
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        run();
    }

}
