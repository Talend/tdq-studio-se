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
package org.talend.dataprofiler.core.ui.action.provider;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.MatchAnalysisAction;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dq.nodes.ColumnSetRepNode;

public class MatchAnalysisActionProvider extends AbstractCommonActionProvider {

    private MatchAnalysisAction matchAnalysisAction;

    public MatchAnalysisActionProvider() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
     */
    @Override
    public void init(ICommonActionExtensionSite site) {
        if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            matchAnalysisAction = new MatchAnalysisAction();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }

        TreeSelection currentSelection = ((TreeSelection) this.getContext().getSelection());
        // Added TDQ-8647 yyin 20140221 :only when the selection is valid, the menu will be added
        if (currentSelection != null && !RepNodeUtils.isValidSelectionFromSameTable(currentSelection.toList())) {
            return;
        }

        matchAnalysisAction.setColumnSelection(currentSelection);
        // when the selection is valid, only two possible status: only one columnset is select, otherwise is some
        // columns in the same columnset are selected; so only check the fist node in the selection is enough.
        if (currentSelection.toList().get(0) instanceof ColumnSetRepNode) {
            matchAnalysisAction.setText(DefaultMessagesImpl.getString("MatchAnalysisAction.matchAnalysis")); //$NON-NLS-1$
        } else {
            matchAnalysisAction.setText(DefaultMessagesImpl.getString("MatchAnalysisAction.analyzeMatches")); //$NON-NLS-1$
        }
        menu.add(matchAnalysisAction);
    }

}
