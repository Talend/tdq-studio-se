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
package org.talend.dataprofiler.core.ui.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.provider.DBTablesViewLabelProvider;
import org.talend.dataprofiler.core.ui.wizard.analysis.provider.MatchAnaColumnContentProvider;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.ConnectionRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MetadataAndColumnSelectionDialog extends ColumnsSelectWithConstraintDialog {

    /**
     * MetadataAndColumnSelectionDialog constructor: the last parameter:false means no need to:addConnFilterListener, in
     * the super class.
     * 
     * @param parent
     */
    public MetadataAndColumnSelectionDialog(Shell parent, String title, List<IRepositoryNode> checkedRepoNodes, String message) {
        super(null, parent, title, checkedRepoNodes, message, false);
        // set the root of the tree, must use the RepositoryNode type.
        setInput(RepositoryNodeHelper.getRootNode(ERepositoryObjectType.METADATA, true));// ResourceManager.getMetadataFolder());
    }

    // TDQ-8248: to only show the datamanager in the dialog, after just created this datamanager
    public MetadataAndColumnSelectionDialog(Shell parent, String title, DataManager dataManager, String message) {
        super(null, parent, title, new ArrayList<IRepositoryNode>(), message, false);
        // set the root of the tree, must use the RepositoryNode type.
        setInput(RepositoryNodeHelper.recursiveFind(dataManager));// ResourceManager.getMetadataFolder());
    }

    // no need to create "select All " buttons
    @Override
    protected Composite createSelectionButtons(Composite composite) {
        Composite buttonComposite = new Composite(composite, SWT.RIGHT);
        return buttonComposite;
    }

    @Override
    protected void initProvider() {
        fLabelProvider = new DBTablesViewLabelProvider();
        // use the contenprovider of MatchAnaColumnContentProvider for match analysis
        fContentProvider = new MatchAnaColumnContentProvider(false);
        sLabelProvider = new DBTablesViewLabelProvider();
        sContentProvider = new ModelElementContentProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.dialog.TwoPartCheckSelectionDialog#updateOKStatus()
     */
    @Override
    protected void updateOKStatus() {
        boolean connectionIsDeleted = false;
        String connectionName = StringUtils.EMPTY;
        List<IRepositoryNode> connectionList = new ArrayList<IRepositoryNode>();

        Iterator iterator = modelElementCheckedMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next != null && next instanceof IRepositoryNode) {
                RepositoryNode repNode = (RepositoryNode) next;
                IRepositoryNode connectionNode = getConnectionNode(repNode);
                if (!connectionList.contains(connectionNode)) {
                    connectionList.add(connectionNode);
                }
            }
        }

        if (!connectionList.isEmpty()) {
            for (IRepositoryNode node : connectionList) {
                if (node != null && node.getObject() != null) {
                    if (node.getObject().isDeleted()) {
                        connectionIsDeleted = true;
                        connectionName = node.getLabel();
                        break;
                    }
                }
            }
        }

        if (connectionIsDeleted) {
            IStatus status = new Status(IStatus.WARNING, PlatformUI.PLUGIN_ID, 0, DefaultMessagesImpl.getString(
                    "MetadataAndColumnSelectionDialog.connectionDeleted", connectionName), null); //$NON-NLS-1$
            updateStatus(status);
        } else {
            super.updateOKStatus();
        }
    }

    private IRepositoryNode getConnectionNode(RepositoryNode repNode) {
        if (repNode != null) {
            RepositoryNode parent = repNode.getParent();
            if (parent != null) {
                if (parent instanceof ConnectionRepNode) {
                    return parent;
                } else {
                    return getConnectionNode(parent);
                }
            }
        }
        return null;
    }
}
