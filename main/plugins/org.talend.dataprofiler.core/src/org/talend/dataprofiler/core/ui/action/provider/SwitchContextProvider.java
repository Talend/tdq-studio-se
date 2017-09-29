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

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.context.Context;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.SwitchContextAction;
import org.talend.dq.nodes.ConnectionRepNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class SwitchContextProvider extends AbstractCommonActionProvider {

    private static final String SWITCHCONTEXT = DefaultMessagesImpl.getString("SwitchContextProvider.SwitchContext");//$NON-NLS-1$

    /**
     * DOC msjian SwitchContextProvider constructor comment.
     */
    public SwitchContextProvider() {
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        if (!isShowMenu()) {
            return;
        }

        Object obj = ((TreeSelection) this.getContext().getSelection()).getFirstElement();
        if (obj instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) obj;
            if (shouldShowSwitchMenu(node)) {
                menu.add(new SwitchContextAction(node, SWITCHCONTEXT));
            }
        }
    }

    /**
     * DOC msjian Comment method "shouldShowReloadMenu".
     * 
     * @param node
     * @return
     */
    @SuppressWarnings("unchecked")
    private boolean shouldShowSwitchMenu(RepositoryNode node) {
        ENodeType type = node.getType();
        // MOD msjian 2012-2-13 TDQ-4559: make it support file/mdm connection
        if ((ENodeType.REPOSITORY_ELEMENT.equals(type) || ENodeType.TDQ_REPOSITORY_ELEMENT.equals(type))
                && node instanceof ConnectionRepNode) {

            // MOD qiongli 2011-10-10
            String contextId = null;
            Item item = node.getObject().getProperty().getItem();
            if (item instanceof ConnectionItem) {
                Connection connection = ((ConnectionItem) item).getConnection();
                if (connection != null) {
                    contextId = connection.getContextId();
                }
            }
            // TDQ-4559~

            // This menu will be available only when a context is not null and has more than one group.
            if (contextId != null && !PluginConstant.EMPTY_STRING.equals(contextId.trim())) {
                ContextItem objContextItem = ContextUtils.getContextItemById2(contextId);
                EList<Context> contexts = objContextItem.getContext();
                if (contexts != null && contexts.size() > 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
