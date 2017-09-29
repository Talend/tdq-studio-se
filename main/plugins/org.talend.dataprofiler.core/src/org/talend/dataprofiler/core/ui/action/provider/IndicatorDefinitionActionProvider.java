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
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.ui.action.actions.OpenIndicatorDefinitionAction;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class IndicatorDefinitionActionProvider extends AbstractCommonActionProvider {

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
        List<IndicatorDefinition> list = new ArrayList<IndicatorDefinition>();
        Object[] objs = currentSelection.toArray();
        for (Object obj : objs) {
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                if (ERepositoryObjectType.TDQ_INDICATOR_ELEMENT.equals(node.getContentType())) {
                    TDQIndicatorDefinitionItem item = (TDQIndicatorDefinitionItem) node.getObject().getProperty().getItem();
                    list.add(item.getIndicatorDefinition());
                }
            }
        }

        if (!list.isEmpty()) {
            menu.add(new OpenIndicatorDefinitionAction(list.toArray(new IndicatorDefinition[list.size()])));
        }
    }
}
