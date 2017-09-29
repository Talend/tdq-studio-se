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
package org.talend.dataprofiler.core.ui.wizard.analysis.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * @author zqin
 */
public class ConnectionsContentProvider extends ResourceViewContentProvider {

    // private static Logger log = Logger.getLogger(ConnectionsContentProvider.class);

    /**
     * @param adapterFactory
     */
    public ConnectionsContentProvider() {
        // super(MNComposedAdapterFactory.getAdapterFactory());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IContainer) {
            IContainer container = (IContainer) parentElement;
            IRepositoryNode node = null;
            // IResource[] members = null;
            // try {
            //
            // // if (ResourceManager.getConnectionFolder().equals(container)) {
            // // return
            // // ProxyRepositoryFactory.getInstance().getAll(ERepositoryObjectType.METADATA_CONNECTIONS).toArray();
            // // } else if (ResourceManager.getMDMConnectionFolder().equals(container)) {
            // // return
            // // ProxyRepositoryFactory.getInstance().getAll(ERepositoryObjectType.METADATA_CONNECTIONS).toArray();
            // // }
            // } catch (PersistenceException e) {
            // log.error(e, e);
            // }
            // try {
            // members = container.members();
            // } catch (CoreException e) {
            //                log.error("Can't get the children of container:" + ((IContainer) parentElement).getLocation()); //$NON-NLS-1$
            // }
            List<RepositoryNode> analyzeNode = new ArrayList<RepositoryNode>();
            if (ResourceManager.isMetadataFolder(container)) {

                IRepositoryViewObject viewObject = new Folder(((IFolder) container).getName(), ((IFolder) container).getName());
                node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
                viewObject.setRepositoryNode(node);
                Object[] children = super.getChildren(node);
                // if analyze Connection/Catalog/Schema,now only surpport DB type klliu 2011-01-28
                for (Object object : children) {
                    if (object instanceof DBConnectionFolderRepNode) {
                        analyzeNode.add((RepositoryNode) object);
                    }
                }
                return analyzeNode.toArray();
            }
        }
        return super.getChildren(parentElement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getElements(java.lang.Object)
     */
    @Override
    public Object[] getElements(Object object) {
        return this.getChildren(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getParent(java.lang.Object)
     */
    @Override
    public Object getParent(Object element) {
        if (element instanceof IContainer) {
            return ((IContainer) element).getParent();
        }
        return super.getParent(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#hasChildren(java.lang.Object)
     */
    @Override
    public boolean hasChildren(Object element) {
        return !(element instanceof DBConnectionRepNode);
    }
}
