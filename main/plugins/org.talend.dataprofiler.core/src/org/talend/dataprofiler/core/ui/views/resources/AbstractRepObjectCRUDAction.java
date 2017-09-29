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

import org.eclipse.jface.viewers.ISelection;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Added 20130515 yyin: Axtracted this class for the purpose of: when there are some values have been set into the
 * related object but the user cancel the changes, and when operate this object(related repository node), for example:
 * open and move, the changed values will be loaded which the user has cancelled. So it is better to reload the obect
 * from its file(which not changed) before the operations. Referenced issue: TDQ-7289
 */
public abstract class AbstractRepObjectCRUDAction implements IRepositoryObjectCRUDAction {

    public AbstractRepObjectCRUDAction() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction#validateDrop(org.talend.repository
     * .model.IRepositoryNode)
     */
    public Boolean validateDrop(IRepositoryNode targetNode) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction#handleDrop(org.talend.repository.
     * model.IRepositoryNode)
     */
    public Boolean handleDrop(IRepositoryNode targetNode) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction#handleRenameFolder(org.talend.repository
     * .model.IRepositoryNode)
     */
    public Boolean handleRenameFolder(IRepositoryNode targetNode) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction#getUISelection()
     */
    public ISelection getUISelection() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction#refreshDQViewForRemoteProject()
     */
    public void refreshDQViewForRemoteProject() {
        // do nothing here.
    }

    protected Boolean loadModelElement(IRepositoryNode sourceNode) {
        if (sourceNode == null) {
            return Boolean.FALSE;
        }
        if (sourceNode.getObject() == null) {
            return Boolean.FALSE;
        }

        ModelElement modelElement = PropertyHelper.getModelElement(sourceNode.getObject().getProperty());
        if (modelElement != null) {
            EMFSharedResources.getInstance().reloadResource(modelElement.eResource().getURI());
        }
        // ~
        return Boolean.TRUE;
    }
}
