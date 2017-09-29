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

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * created by qiongli on 2013-11-7 Detailled comment
 * 
 */
public class MatchAnaColumnContentProvider extends ColumnContentProvider {

    private boolean unfoldNodeToColumn = false;

    /**
     * 
     * DOC qiongli MatchAnaColumnContentProvider constructor comment.
     * 
     * @param unfoldToColumn,if it is true,means will unfold tableNode and display columnNodes
     */
    public MatchAnaColumnContentProvider(boolean unfoldToColumn) {
        this.unfoldNodeToColumn = unfoldToColumn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#getChildren(java.lang.Object)
     */
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IRepositoryNode) {
            IRepositoryNode node = (IRepositoryNode) parentElement;
            // filter MDM folder node for Match analysis.
            if (node.getContentType() == ERepositoryObjectType.METADATA) {
                List<IRepositoryNode> children = new ArrayList<IRepositoryNode>();
                for (IRepositoryNode childNode : node.getChildren()) {
                    children.add(childNode);
                }
                return children.toArray();
            }
        }
        return super.getChildren(parentElement);
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof RepositoryNode) {
            RepositoryNode repoNode = (RepositoryNode) element;
            return repoNode.getParent();
        }
        return super.getParent(element);
    }

    @Override
    public boolean hasChildren(Object element) {
        if (!unfoldNodeToColumn) {
            if (element instanceof RepositoryNode) {
                RepositoryNode repoNode = (RepositoryNode) element;
                if (repoNode instanceof DBTableRepNode || repoNode instanceof DBViewRepNode || repoNode instanceof DFTableRepNode) {
                    return Boolean.FALSE;
                }
                return repoNode.hasChildren();
            }
        }

        return super.hasChildren(element);
    }
}
