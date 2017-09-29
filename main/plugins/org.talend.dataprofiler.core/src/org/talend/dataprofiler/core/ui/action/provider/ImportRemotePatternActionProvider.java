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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.dataprofiler.core.ui.action.actions.ImportRemotePatternAction;
import org.talend.dataprofiler.core.ui.exchange.ExchangeComponentRepNode;
import org.talend.dataprofiler.ecos.model.IEcosComponent;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ImportRemotePatternActionProvider extends AbstractCommonActionProvider {

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

        List<IEcosComponent> selectedComponents = new ArrayList<IEcosComponent>();

        for (Object obj : currentSelection.toArray()) {
            if (obj instanceof ExchangeComponentRepNode) {
                ExchangeComponentRepNode node = (ExchangeComponentRepNode) obj;
                selectedComponents.add(node.getEcosComponent());
            }
        }

        if (selectedComponents.size() > 0) {
            ImportRemotePatternAction importAction = new ImportRemotePatternAction(
                    selectedComponents.toArray(new IEcosComponent[selectedComponents.size()]));
            menu.add(importAction);
        }
    }
}
