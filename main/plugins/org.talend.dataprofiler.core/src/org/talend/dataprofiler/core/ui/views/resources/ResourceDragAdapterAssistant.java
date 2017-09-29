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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.navigator.CommonDragAdapterAssistant;
import org.eclipse.ui.part.ResourceTransfer;

/**
 * DOC rli class global comment. Detailled comment
 * 
 * @deprecated not used any more
 */
public class ResourceDragAdapterAssistant extends CommonDragAdapterAssistant {

    private static final Transfer[] SUPPORTED_TRANSFERS = new Transfer[] { ResourceTransfer.getInstance(),
            LocalSelectionTransfer.getTransfer() };

    private static final Class<IResource> IRESOURCE_TYPE = IResource.class;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonDragAdapterAssistant#getSupportedTransferTypes()
     */
    public Transfer[] getSupportedTransferTypes() {
        return SUPPORTED_TRANSFERS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.navigator.CommonDragAdapterAssistant#setDragData(org.eclipse.swt.dnd.DragSourceEvent,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public boolean setDragData(DragSourceEvent anEvent, IStructuredSelection aSelection) {
        if (LocalSelectionTransfer.getTransfer().isSupportedType(anEvent.dataType)) {
            anEvent.data = aSelection;
            return true;
        } else if (ResourceTransfer.getInstance().isSupportedType(anEvent.dataType)) {
            anEvent.data = getSelectedResources(aSelection);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private IResource[] getSelectedResources(IStructuredSelection aSelection) {
        Set<IResource> resources = new LinkedHashSet<IResource>();
        IResource resource = null;
        for (Iterator iter = aSelection.iterator(); iter.hasNext();) {
            Object selected = iter.next();
            resource = adaptToResource(selected);
            if (resource != null) {
                resources.add(resource);
            }
        }
        return (IResource[]) resources.toArray(new IResource[resources.size()]);
    }

    private IResource adaptToResource(Object selected) {
        IResource resource;
        if (selected instanceof IResource) {
            resource = (IResource) selected;
        } else if (selected instanceof IAdaptable) {
            resource = (IResource) ((IAdaptable) selected).getAdapter(IRESOURCE_TYPE);
        } else {
            resource = (IResource) Platform.getAdapterManager().getAdapter(selected, IRESOURCE_TYPE);
        }
        return resource;
    }
}
