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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.wizard.analysis.connection.ConnectionWizard;

public class CreateNewConnectionForMatchAnalysisAction extends Action implements ICheatSheetAction {

    public CreateNewConnectionForMatchAnalysisAction() {
        super(DefaultMessagesImpl.getString("MatchMasterDetailsPage.CreateConnectionButton")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.CONNECTION));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
     * org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] arg0, ICheatSheetManager arg1) {
        ConnectionWizard connectionWizard = new ConnectionWizard(PlatformUI.getWorkbench(), null);
        connectionWizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new WizardDialog(null, connectionWizard);
        dialog.setPageSize(500, 200);
        dialog.open();

        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof MatchAnalysisEditor) {
            MatchAnalysisDetailsPage masterPage = (MatchAnalysisDetailsPage) ((MatchAnalysisEditor) editor).getMasterPage();
            if (masterPage.getAnalysisHandler() != null) {
                masterPage.openColumnsSelectionDialog(masterPage.getAnalysisHandler().getConnection());
            }
        }
    }

}
