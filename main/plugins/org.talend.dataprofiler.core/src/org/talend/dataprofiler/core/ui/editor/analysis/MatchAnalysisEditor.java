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
package org.talend.dataprofiler.core.ui.editor.analysis;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.ExportMatchRuleAction;
import org.talend.dataprofiler.core.ui.action.actions.ImportMatchRuleAction;
import org.talend.dataprofiler.core.ui.editor.TdEditorToolBar;
import org.talend.dataquality.record.linkage.ui.composite.utils.MatchRuleAnlaysisUtils;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchAnalysisEditor extends AnalysisEditor {

    ImportMatchRuleAction importMatchRuleAction = null;

    ExportMatchRuleAction exportMatchRuleAction = null;

    @Override
    protected void addPages() {
        super.addPages();

        TdEditorToolBar toolbar = getToolBar();
        if (toolbar != null) {
            importMatchRuleAction = new ImportMatchRuleAction(getMasterPage());
            importMatchRuleAction.setToolTipText(DefaultMessagesImpl.getString("MatchAnalysisEditor.importMatchRule"));//$NON-NLS-1$
            toolbar.addActions(importMatchRuleAction);

            // when there are some keys in the analysis
            if (getMasterPage().getCurrentModelElement().getResults() != null) {
                exportMatchRuleAction = new ExportMatchRuleAction(
                        MatchRuleAnlaysisUtils.getRecordMatchIndicatorFromAna(getMasterPage().getCurrentModelElement()));
                exportMatchRuleAction.setToolTipText(DefaultMessagesImpl.getString("MatchAnalysisEditor.exportMatchRule"));//$NON-NLS-1$
                toolbar.addActions(exportMatchRuleAction);
            }
        }
    }
}
