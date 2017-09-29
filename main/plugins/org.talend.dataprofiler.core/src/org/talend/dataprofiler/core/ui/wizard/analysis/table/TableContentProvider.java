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
package org.talend.dataprofiler.core.ui.wizard.analysis.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableContentProvider extends ResourceViewContentProvider {

    // private static Logger log = Logger.getLogger(TableContentProvider.class);

    public TableContentProvider() {
        super();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        List<RepositoryNode> analyzeNode = new ArrayList<RepositoryNode>();
        if (parentElement instanceof DBTableFolderRepNode || parentElement instanceof DBViewFolderRepNode) {
            // return ((IRepositoryNode) parentElement).getChildren().toArray();
            return super.getChildren(parentElement);
        } else if (parentElement instanceof IContainer) {
            IContainer container = (IContainer) parentElement;
            if (ResourceManager.isMetadataFolder(container)) {
                IRepositoryViewObject viewObject = new Folder(((IFolder) container).getName(), ((IFolder) container).getName());
                IRepositoryNode node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
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
        return !(element instanceof DBTableRepNode);
    }

    /**
     * This class will combine catlogName and columnSetName as a key.
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class CatalogSchemaKey {

        private final String catalogName;

        private final String schemaName;

        public CatalogSchemaKey(ColumnSet columnSet) {
            this.schemaName = SchemaHelper.getParentSchema(columnSet) == null ? "" //$NON-NLS-1$
                    : SchemaHelper.getParentSchema(columnSet).getName();
            this.catalogName = CatalogHelper.getParentCatalog(columnSet) == null ? "" //$NON-NLS-1$
                    : CatalogHelper.getParentCatalog(columnSet).getName();
        }

        @Override
        public int hashCode() {
            final int prime = 22;
            int result = 1;
            result = prime * result + ((schemaName == null) ? 0 : schemaName.hashCode());
            result = prime * result + ((catalogName == null) ? 0 : catalogName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CatalogSchemaKey other = (CatalogSchemaKey) obj;
            if (catalogName == null) {
                if (other.catalogName != null) {
                    return false;
                }
            } else if (!catalogName.equals(other.catalogName)) {
                return false;
            }
            if (schemaName == null) {
                if (other.schemaName != null) {
                    return false;
                }
            } else if (!schemaName.equals(other.schemaName)) {
                return false;
            }
            return true;
        }
    }
}
