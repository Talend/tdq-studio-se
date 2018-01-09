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

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dq.CWMPlugin;
import org.talend.dq.nodes.ConnectionRepNode;
import org.talend.metadata.managment.ui.utils.SwitchContextGroupNameImpl;
import org.talend.metadata.managment.ui.wizard.metadata.ContextSetsSelectionDialog;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class SwitchContextAction extends Action {

    private static Logger log = Logger.getLogger(SwitchContextAction.class);

    private Object selectedObject;

    public SwitchContextAction(Object selectedNode, String menuText) {
        super(menuText);
        this.selectedObject = selectedNode;
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.SWITCH_IMAGE));
    }

    /*
     * MOD yyin 20120917 TDQ-5362, do NOT reload DB when switching context.
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        // MOD msjian 2012-2-13 TDQ-4559: make it support file connection
        if (selectedObject instanceof ConnectionRepNode) {
            ConnectionRepNode conRepNode = (ConnectionRepNode) selectedObject;
            Item item = conRepNode.getObject().getProperty().getItem();
            if (item instanceof ConnectionItem) {
                ConnectionItem connItem = (ConnectionItem) item;

                // Added yyin 20120925 TDQ-5668, check this connection's editor is open or not, if open, close.
                CorePlugin.getDefault().closeEditorIfOpened(connItem);
                // ~
                String chooseContext = chooseContext(connItem.getConnection());

                boolean isUpdated = SwitchContextGroupNameImpl.getInstance().updateContextGroup(connItem, chooseContext);

                if (isUpdated) {

                    if (log.isDebugEnabled()) {
                        log.debug(DefaultMessagesImpl.getString("SwitchContextAction.saveMessage", chooseContext, "Succeeded"));//$NON-NLS-1$ //$NON-NLS-2$ 
                    }

                    // ADD msjian TDQ-8834 2014-4-10: after switch the context, update the sql explore alias
                    // information.
                    CWMPlugin.getDefault()
                            .updateConnetionAliasByName(connItem.getConnection(), connItem.getProperty().getLabel());
                    // TDQ-8834~

                    CorePlugin.getDefault().refreshDQView(selectedObject);
                } else {
                    MessageDialog.openWarning(CorePlugin.getDefault().getWorkbench().getDisplay().getActiveShell(), "", //$NON-NLS-1$
                            DefaultMessagesImpl.getString("SwitchContextAction.nullParameterError")); //$NON-NLS-1$
                    log.error(DefaultMessagesImpl.getString("SwitchContextAction.saveMessage", chooseContext, "Failed"));//$NON-NLS-1$ //$NON-NLS-2$ 
                }
            }
        }
        // TDQ-4559~
    }

    /**
     * DOC talend Comment method "chooseContext".
     */
    private String chooseContext(Connection conn) {
        String selectedContext = null;
        if (conn != null) {
            String contextId = conn.getContextId();
            ContextItem contextItem = ContextUtils.getContextItemById2(contextId);
            selectedContext = contextItem.getDefaultContext();
            if (contextItem.getContext().size() > 1) {
                ContextSetsSelectionDialog setsDialog = new ContextSetsSelectionDialog(contextItem);
                if (setsDialog.open() == Window.OK) {
                    selectedContext = setsDialog.getSelectedContext();
                }
            }
        }
        return selectedContext;
    }

}
