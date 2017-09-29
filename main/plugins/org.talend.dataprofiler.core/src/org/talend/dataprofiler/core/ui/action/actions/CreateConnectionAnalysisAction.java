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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.helper.ProxyRepositoryManager;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class CreateConnectionAnalysisAction extends Action implements ICheatSheetAction {

    public CreateConnectionAnalysisAction() {
    }

    public void run(String[] params, ICheatSheetManager manager) {
        // ADD xqliu TDQ-4285 2011-12-27
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        // ~ TDQ-4285

        openStandardAnalysisDialog(AnalysisType.CONNECTION);
    }

    private void openStandardAnalysisDialog(AnalysisType type) {
        Wizard wizard = WizardFactory.createAnalysisWizard(type);
        wizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);

        if (WizardDialog.OK == dialog.open())
            ProxyRepositoryManager.getInstance().save();
    }
}
