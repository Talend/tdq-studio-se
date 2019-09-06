// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.PlatformUI;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.database.DatabaseWizard;

/**
 * created by yyin on 2015年4月23日 Detailled comment
 *
 */
public class CreateHiveOfHCAction extends AbstractMetadataCreationAction {

    public CreateHiveOfHCAction(RepositoryNode node) {
        super.node = node;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler
     * .core.ui.action.AbstractMetadataCreationAction#createWizard()
     */
    @Override
    protected IWizard createWizard() {
        RepositoryNode dbRootNode = RepositoryNodeHelper.getDBConnectionRootNode();
        Map<String, String> initMap = new HashMap<String, String>();
        HadoopClusterUtils.getDefault().initConnectionParameters(initMap, node);
        DatabaseWizard wizard =
                new DatabaseWizard(PlatformUI.getWorkbench(), true, dbRootNode, getExistingNames(), initMap);
        // TDQ-17500 msjian: set the connection type is Hive
        Connection connection = wizard.getConnectionItem().getConnection();
        if (connection != null && connection instanceof DatabaseConnection) {
            ((DatabaseConnection) connection).setDatabaseType(EDatabaseTypeName.HIVE.getDisplayName());
        }
        return wizard;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#getActionLabel()
     */
    @Override
    protected String getActionLabel() {
        return DefaultMessagesImpl.getString("CreateHiveOfHCAction.create"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#getConnectionItem()
     */
    @Override
    public ConnectionItem getConnectionItem() {

        ConnectionItem newconnectionItem = super.getConnectionItem();
        if (newconnectionItem.getProperty() == null || (newconnectionItem.getProperty().getId() == null)) {
            return null;
        }
        return newconnectionItem;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.action.AbstractMetadataCreationAction#getActionImage()
     */
    @Override
    protected ImageDescriptor getActionImage() {
        // TODO Auto-generated method stub
        return null;
    }

}
