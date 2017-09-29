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
import org.eclipse.core.resources.IResource;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * DOC klliu class global comment. Detailled comment,2011-02-16 feature 15387
 */
public class ColumnContentProvider extends ResourceViewContentProvider {

    // private static Logger log = Logger.getLogger(CatalogContentProvider.class);

    public ColumnContentProvider() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object parentElement) {
        List<RepositoryNode> analyzeNode = new ArrayList<RepositoryNode>();
        if (parentElement instanceof IContainer) {
            if (ResourceManager.isMetadataFolder((IResource) parentElement)) {
                IFolder container = (IFolder) parentElement;
                IRepositoryViewObject viewObject = new Folder(container.getName(), container.getName());
                RepositoryNode node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
                viewObject.setRepositoryNode(node);
                Object[] children = super.getChildren(node);
                // if analyze Connection/Catalog/Schema,now only surpport DB type klliu 2011-01-28
                for (Object object : children) {
                    analyzeNode.add((RepositoryNode) object);
                }
                return analyzeNode.toArray();
            }
        }
        return super.getChildren(parentElement);
    }

    @Override
    public Object[] getElements(Object object) {
        return this.getChildren(object);
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof IContainer) {
            return ((IContainer) element).getParent();
        }
        return super.getParent(element);
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof DBColumnRepNode) {
            return false;
        }
        return true;
    }
}
