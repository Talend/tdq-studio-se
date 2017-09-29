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

import java.util.Iterator;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.action.actions.RemoveAnalysisAction;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dq.nodes.ReportAnalysisRepNode;

/**
 * DOC rli class global comment. Detailled comment
 */
public class RemoveAnalysisActionProvider extends AbstractCommonActionProvider {

    public RemoveAnalysisActionProvider() {
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        TreeSelection treeSelection = ((TreeSelection) this.getContext().getSelection());
        if (!treeSelection.isEmpty()) {
            Iterator iterator = treeSelection.iterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj instanceof ReportAnalysisRepNode) {
                    ReportAnalysisRepNode repAnaNode = (ReportAnalysisRepNode) obj;
                    TDQReportItem reportItem = repAnaNode.getReportItem();
                    if (CorePlugin.getDefault().itemIsOpening(reportItem, false)) {
                        // if the report's editor is opening, don't show the menu
                        return;
                    }
                } else {
                    // if include other type node, don't show the menu
                    return;
                }
            }
        }
        // show the menu
        menu.add(new RemoveAnalysisAction());
    }
}
