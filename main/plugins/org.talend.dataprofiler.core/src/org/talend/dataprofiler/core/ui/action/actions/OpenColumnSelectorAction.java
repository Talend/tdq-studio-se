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

import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.CorrelationAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.FunctionalDependencyAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnSetAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.RedundancyAnalysisDetailsPage;
import org.talend.dataprofiler.core.ui.editor.analysis.BusinessRuleAnalysisDetailsPage;
import org.talend.dataquality.analysis.AnalysisType;

/**
 * DOC zqin class global comment. MOD mzhao 2009-02-03 Make this class common to service all category analysis<br>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z zqin $
 * 
 */
public class OpenColumnSelectorAction extends Action implements ICheatSheetAction {

    public OpenColumnSelectorAction() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
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

        if (params == null || params.length == 0) {
            return;
        }
        Integer analysisCatigory = null;
        if (NumberUtils.isNumber(params[0])) {
            analysisCatigory = NumberUtils.toInt(params[0]);
        }
        if (analysisCatigory != null) {
            AnalysisEditor editor = (AnalysisEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            if (editor == null) {
                return;
            }
            switch (analysisCatigory) {
            case AnalysisType.BUSINESS_RULE_VALUE:
                BusinessRuleAnalysisDetailsPage page4 = (BusinessRuleAnalysisDetailsPage) editor.getMasterPage();
                page4.openTableSelectionDialog();
                page4.doSave(null);
                break;
            case AnalysisType.TABLE_FUNCTIONAL_DEPENDENCY_VALUE:
                FunctionalDependencyAnalysisDetailsPage page3 = (FunctionalDependencyAnalysisDetailsPage) editor.getMasterPage();
                if (params[1] != null) {
                    if ("A".equalsIgnoreCase(params[1])) {//$NON-NLS-1$
                        page3.openColumnsSetASelectionDialog();
                    } else {
                        page3.openColumnsSetBSelectionDialog();
                    }
                }
                // page3.doSave(null);
                break;
            case AnalysisType.COLUMN_SET_VALUE:
                ColumnSetAnalysisDetailsPage page0 = (ColumnSetAnalysisDetailsPage) editor.getMasterPage();
                page0.openColumnsSelectionDialog();
                page0.doSave(null);
                break;

            case AnalysisType.MULTIPLE_COLUMN_VALUE:
                ColumnAnalysisDetailsPage page = (ColumnAnalysisDetailsPage) editor.getMasterPage();
                page.openColumnsSelectionDialog();
                page.doSave(null);
                break;

            case AnalysisType.COLUMNS_COMPARISON_VALUE:
                RedundancyAnalysisDetailsPage page1 = (RedundancyAnalysisDetailsPage) editor.getMasterPage();
                if (params[1] != null) {
                    if (NumberUtils.isNumber(params[1])) {
                        int pos = NumberUtils.toInt(params[1]);
                        if (pos == 0) {
                            page1.openColumnsSetASelectionDialog();
                        } else {
                            page1.openColumnsSetBSelectionDialog();
                        }
                    }
                }
                // MOD qiongli 2010-12-17 bug 17677 .Don't save it in this case.
                // page1.doSave(null);
                break;

            case AnalysisType.COLUMN_CORRELATION_VALUE:
                CorrelationAnalysisDetailsPage page2 = (CorrelationAnalysisDetailsPage) editor
                        .getMasterPage();
                page2.openColumnsSelectionDialog();
                page2.doSave(null);
                break;

            default:
                break;
            }
        }
    }

}
