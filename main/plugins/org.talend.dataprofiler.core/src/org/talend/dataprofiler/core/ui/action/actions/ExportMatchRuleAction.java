// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisEditor;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataprofiler.core.ui.wizard.matchrule.NewMatchRuleWizard;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dq.analysis.parameters.DQMatchRuleParameter;
import org.talend.resource.ResourceManager;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class ExportMatchRuleAction extends Action implements ICheatSheetAction {

    private MatchRuleDefinition matchRule = null;

    public ExportMatchRuleAction(RecordMatchingIndicator recordMatchingIndicator) {
        ImageDescriptor imageDescriptor = ImageLib.getImageDescriptor(ImageLib.EXPORT_MATCH_RULE_ICON);
        setText(DefaultMessagesImpl.getString("MatchAnalysisEditor.exportMatchRule")); //$NON-NLS-1$
        this.setImageDescriptor(imageDescriptor);

        this.matchRule = recordMatchingIndicator.getBuiltInMatchRuleDefinition();
    }

    public ExportMatchRuleAction() {
        ImageDescriptor imageDescriptor = ImageLib.getImageDescriptor(ImageLib.EXPORT_MATCH_RULE_ICON);
        setText(DefaultMessagesImpl.getString("MatchAnalysisEditor.exportMatchRule")); //$NON-NLS-1$
        this.setImageDescriptor(imageDescriptor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        // if there are no match rule, or no keys in the match rule.
        if ((matchRule == null)
                || ((matchRule.getBlockKeys() == null || matchRule.getBlockKeys().size() < 1) && (matchRule.getMatchRules() == null || matchRule
                        .getMatchRules().size() < 1))) {

            MessageDialog.openWarning(null, DefaultMessagesImpl.getString("ExportMatchRuleAction.noRule"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("ExportMatchRuleAction.noKeys")); //$NON-NLS-1$
            return;
        }

        DQMatchRuleParameter parameter = new DQMatchRuleParameter();
        FolderProvider folderProvider = new FolderProvider();
        folderProvider.setFolderResource(ResourceManager.getRulesMatcherFolder());
        parameter.setFolderProvider(folderProvider);
        NewMatchRuleWizard matchWizard = WizardFactory.createNewMatchRuleWizard(parameter);
        matchWizard.setWindowTitle(getText());
        // TDQ-8236 used for display related help
        matchWizard.setHelpContextId("org.talend.help.export_match_rule");//$NON-NLS-1$

        matchWizard.setMatchRule(matchRule);

        WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), matchWizard);
        dialog.open();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
     * org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] arg0, ICheatSheetManager arg1) {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor instanceof MatchAnalysisEditor) {
            MatchAnalysisDetailsPage masterPage = (MatchAnalysisDetailsPage) ((MatchAnalysisEditor) editor).getMasterPage();
            RecordMatchingIndicator rmIndicator = MatchRuleAnlaysisUtils.getRecordMatchIndicatorFromAna(masterPage
                    .getCurrentModelElement());
            this.matchRule = rmIndicator.getBuiltInMatchRuleDefinition();
            this.run();
        }
    }
}
