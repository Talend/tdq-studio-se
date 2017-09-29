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
package org.talend.dataprofiler.core.ui.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEffect;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Control;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dq.helper.PropertyHelper;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 */
public abstract class AbstractSelectionReceiver extends DropTargetEffect {

    private String extension;

    protected static int lastValidOperation;

    /**
     * DOC qzhang AbstractSelectionReceiver constructor comment.
     */
    public AbstractSelectionReceiver(Control control, String extension) {
        super(control);
        this.extension = extension;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.dnd.DropTargetAdapter#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void dragEnter(DropTargetEvent event) {
        super.dragEnter(event);
        execValidation(event, doDropValidation(event, LocalSelectionTransfer.getTransfer()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.dnd.DropTargetAdapter#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public void dropAccept(DropTargetEvent event) {
        super.dropAccept(event);
        execValidation(event, doDropValidation(event, LocalSelectionTransfer.getTransfer()));
    }

    /**
     * DOC qzhang Comment method "execValidation".
     * 
     * @param event
     * @param valid
     */
    public void execValidation(DropTargetEvent event, boolean valid) {
        if (event.detail != DND.DROP_NONE) {
            // FIXME static value should not be assigned valus in a instance method.
            lastValidOperation = event.detail;
        }
        if (valid) {
            event.detail = lastValidOperation;
        } else {
            event.detail = DND.DROP_NONE;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.views.ISelectionReceiver#doDropValidation(org.eclipse.swt.dnd.DropTargetEvent,
     * org.eclipse.ui.navigator.CommonViewer)
     */
    public boolean doDropValidation(DropTargetEvent event, LocalSelectionTransfer transfer) {
        boolean ret = false;
        Object[] firstElements = ((StructuredSelection) transfer.getSelection()).toArray();
        for (Object object : firstElements) {
            if (object instanceof IFile) {
                IFile fe = (IFile) object;
                if (extension != null && extension.equals(fe.getFileExtension())) {
                    ret = true;
                }
            } else if (object instanceof IRepositoryNode) {
                // // ADD msjian TDQ-4919: can not drag the ref project analysis to report
                IRepositoryViewObject obj = ((IRepositoryNode) object).getObject();
                // if (!ProxyRepositoryFactory.getInstance().isEditableAndLockIfPossible(obj)) {
                // return false;
                // }
                // // TDQ-4919~

                IPath itemPath = PropertyHelper.getItemPath(obj.getProperty());
                if (itemPath == null) {
                    continue;
                }
                if (extension != null && extension.equals(itemPath.getFileExtension())) {
                    ret = true;
                }
            }
            if (!ret) {
                break;
            }
        }
        return ret;
    }

    @Override
    public void drop(DropTargetEvent event) {
        super.drop(event);
        drop(event, LocalSelectionTransfer.getTransfer());
    }

    public abstract void drop(DropTargetEvent event, LocalSelectionTransfer transfer);
}
