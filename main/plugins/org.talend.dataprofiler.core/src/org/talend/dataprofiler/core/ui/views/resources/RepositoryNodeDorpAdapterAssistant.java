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
package org.talend.dataprofiler.core.ui.views.resources;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.navigator.CommonDropAdapter;
import org.eclipse.ui.navigator.CommonDropAdapterAssistant;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC mzhao Handle drop event of repositoryNode on DQ repository viewer.
 */
public class RepositoryNodeDorpAdapterAssistant extends CommonDropAdapterAssistant {

    protected static Logger log = Logger.getLogger(RepositoryNodeDorpAdapterAssistant.class);

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    public RepositoryNodeDorpAdapterAssistant() {
    }

    @Override
    public IStatus validateDrop(Object target, int operation, TransferData transferType) {
        if (!(target instanceof IRepositoryNode)) {
            return Status.CANCEL_STATUS;
        }
        IRepositoryNode targetNode = (IRepositoryNode) target;
        Boolean isValid = repositoryObjectCRUD.validateDrop(targetNode);
        if (isValid) {
            return Status.OK_STATUS;
        } else {
            return Status.CANCEL_STATUS;
        }

    }

    @Override
    public IStatus handleDrop(CommonDropAdapter aDropAdapter, DropTargetEvent aDropTargetEvent, Object aTarget) {
        IRepositoryNode targetNode = (IRepositoryNode) aTarget;
        if (aDropAdapter.getCurrentTarget() == null || aDropTargetEvent.data == null) {
            return Status.CANCEL_STATUS;
        }
        TransferData currentTransfer = aDropAdapter.getCurrentTransfer();
        if (LocalSelectionTransfer.getTransfer().isSupportedType(currentTransfer)) {
            // Call repository node CRULD handler to handle drop action.
            boolean isHandledOK = repositoryObjectCRUD.handleDrop(targetNode);
            if (isHandledOK) {
                return Status.OK_STATUS;
            } else {
                return Status.CANCEL_STATUS;
            }
        } else {
            return Status.CANCEL_STATUS;
        }
    }

}
