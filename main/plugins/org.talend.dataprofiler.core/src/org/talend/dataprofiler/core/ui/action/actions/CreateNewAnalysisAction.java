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

import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.utils.OpeningHelpWizardDialog;
import org.talend.dataprofiler.core.ui.wizard.analysis.CreateNewAnalysisWizard;
import org.talend.dataprofiler.core.ui.wizard.analysis.WizardFactory;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dq.analysis.parameters.AnalysisLabelParameter;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC zqin class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class CreateNewAnalysisAction extends Action implements ICheatSheetAction, IIntroAction {

    public IPath path;

    public RepositoryNode node;

    public CreateNewAnalysisAction() {
        super(DefaultMessagesImpl.getString("CreateNewAnalysisAction.newAnalysis")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.ACTION_NEW_ANALYSIS));
    }

    /**
     * DOC klliu CreateNewAnalysisAction constructor comment.
     * 
     * @param path2
     * @param node
     */
    public CreateNewAnalysisAction(IPath path, RepositoryNode node) {
        this();
        this.path = path;
        this.node = node;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        CreateNewAnalysisWizard wizard = WizardFactory.createNewAnalysisWizard(node);
        wizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new OpeningHelpWizardDialog(null, wizard, null);
        wizard.setContainer(dialog);
        if (WizardDialog.OK == dialog.open()) {
            ProxyRepositoryManager.getInstance().save();
        }
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

        // MOD mzhao 2009-02-03 open analysis creation wizard according to
        // passed parameter.
        if (params == null || params.length == 0) {
            return;
        }
        Integer analysisCatigory = null;
        if (NumberUtils.isNumber(params[0])) {
            analysisCatigory = NumberUtils.toInt(params[0]);
        }
        AnalysisType analysisType = null;
        Wizard wizard = null;
        if (analysisCatigory != null) {
            switch (analysisCatigory) {
            case AnalysisType.BUSINESS_RULE_VALUE:
                analysisType = AnalysisType.TABLE;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            case AnalysisType.TABLE_FUNCTIONAL_DEPENDENCY_VALUE:
                analysisType = AnalysisType.TABLE_FUNCTIONAL_DEPENDENCY;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            case AnalysisType.COLUMN_SET_VALUE:
                analysisType = AnalysisType.COLUMN_SET;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            case AnalysisType.MULTIPLE_COLUMN_VALUE:
                analysisType = AnalysisType.MULTIPLE_COLUMN;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            case AnalysisType.CATALOG_VALUE:
                analysisType = AnalysisType.CATALOG;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            case AnalysisType.SCHEMA_VALUE:
                analysisType = AnalysisType.SCHEMA;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            case AnalysisType.COLUMNS_COMPARISON_VALUE:
                analysisType = AnalysisType.COLUMNS_COMPARISON;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            case AnalysisType.COLUMN_CORRELATION_VALUE:
                analysisType = AnalysisType.COLUMN_CORRELATION;
                if (params[1] != null) {
                    if (NumberUtils.isNumber(params[1])) {
                        AnalysisLabelParameter parameter = new AnalysisLabelParameter();
                        parameter.setAnalysisTypeName(analysisType.getLiteral());
                        int correAnaType = NumberUtils.toInt(params[1]);
                        if (correAnaType == 0) {
                            parameter.setCategoryLabel(AnalysisLabelParameter.NUMBERIC_CORRELATION);
                        } else if (correAnaType == 1) {
                            parameter.setCategoryLabel(AnalysisLabelParameter.NOMINAL_CORRELATION);
                        } else {
                            parameter.setCategoryLabel(AnalysisLabelParameter.DATE_CORRELATION);
                        }
                        wizard = WizardFactory.createAnalysisWizard(analysisType, parameter);
                    }
                }
                break;
            case AnalysisType.MATCH_ANALYSIS_VALUE:
                analysisType = AnalysisType.MATCH_ANALYSIS;
                wizard = WizardFactory.createAnalysisWizard(analysisType);
                break;
            default:
                break;
            }
        }
        if (analysisType == null || wizard == null) {
            return;
        }
        wizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.setPageSize(500, 340);

        if (WizardDialog.OK == dialog.open()) {
            ProxyRepositoryManager.getInstance().save();
        }
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    public void run(IIntroSite site, Properties params) {

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            MessageDialog.openWarning(null, DefaultMessagesImpl.getString("CreateNewAnalysisAction.readOnlyErrorTitle"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("CreateNewAnalysisAction.readOnlyErrorMessage")); //$NON-NLS-1$
        } else {
            PlatformUI.getWorkbench().getIntroManager().closeIntro(PlatformUI.getWorkbench().getIntroManager().getIntro());
            IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (null == workbenchWindow) {
                return;
            }
            IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
            if (null == workbenchPage) {
                return;
            }

            IPerspectiveDescriptor currentPerspective = workbenchPage.getPerspective();
            if (!IBrandingConfiguration.PERSPECTIVE_DQ_ID.equals(currentPerspective.getId())) {
                // show dq perspective
                try {
                    workbenchWindow.getWorkbench().showPerspective(IBrandingConfiguration.PERSPECTIVE_DQ_ID, workbenchWindow);
                    workbenchPage = workbenchWindow.getActivePage();
                } catch (WorkbenchException e) {
                    ExceptionHandler.process(e);
                    return;
                }
            }

            run();
        }

    }
}
