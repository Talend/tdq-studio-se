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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.provider.DBTablesViewLabelProvider;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * show the columns which need to create Duplicates Analysis.
 */
public class ColumnsMapSelectionDialog extends ColumnsSelectionDialog {

    private Map<ColumnSet, List<TdColumn>> allMap = new HashMap<ColumnSet, List<TdColumn>>();

    public void setAllMap(Map<ColumnSet, List<TdColumn>> allMap) {
        if (allMap != null && !allMap.isEmpty()) {
            this.allMap = allMap;
            if (this.fContentProvider != null) {
                ((ConnectionColumnSetContentProvider) this.fContentProvider).init(allMap);
            }
            if (this.sContentProvider != null && this.checkedRepoNodes != null) {
                ((ColumnContentProvider) this.sContentProvider).init(this.checkedRepoNodes);
            }
        }
    }

    private Map<ColumnSet, List<TdColumn>> userMap = new HashMap<ColumnSet, List<TdColumn>>();

    public Map<ColumnSet, List<TdColumn>> getUserMap() {
        return this.userMap;
    }

    private List<? extends IRepositoryNode> checkedRepoNodes;

    /**
     * DOC xqliu ColumnsMapSelectionDialog constructor comment.
     * 
     * @param metadataFormPage
     * @param parent
     * @param title
     * @param checkedRepoNodes
     * @param rootNode
     * @param message
     */
    public ColumnsMapSelectionDialog(AbstractAnalysisMetadataPage metadataFormPage, Shell parent, String title,
            List<? extends IRepositoryNode> checkedRepoNodes, RepositoryNode rootNode, String message) {
        super(metadataFormPage, parent, title, checkedRepoNodes, rootNode, message);
        this.checkedRepoNodes = checkedRepoNodes;
    }

    @Override
    protected void initProvider() {
        this.fLabelProvider = new DBTablesViewLabelProvider();
        this.fContentProvider = new ConnectionColumnSetContentProvider();
        this.sLabelProvider = new DBTablesViewLabelProvider();
        this.sContentProvider = new ColumnContentProvider();
    }

    @Override
    protected void okPressed() {
        this.allCheckedElements.clear();
        getAllCheckElements();
        if (this.allCheckedElements.size() == 0) {
            MessageDialog.openWarning(this.getShell(),
                    DefaultMessagesImpl.getString("ColumnsMapSelectionDialog.columnSelection"),
                    DefaultMessagesImpl.getString("ColumnsMapSelectionDialog.noColumnSelected"));
        } else {
            buildUserMap(this.allCheckedElements);
            super.okPressed();
            this.modelElementCheckedMap = null;
        }
    }

    /**
     * DOC xqliu Comment method "buildUserMap".
     * 
     * @param allCheckedElements
     */
    private void buildUserMap(List<IRepositoryNode> allCheckedElements) {
        if (allCheckedElements != null && !allCheckedElements.isEmpty()) {
            List<TdColumn> userColumnsAll = new ArrayList<TdColumn>();
            for (IRepositoryNode node : allCheckedElements) {
                if (node instanceof DBColumnRepNode) {
                    userColumnsAll.add(((DBColumnRepNode) node).getTdColumn());
                }
            }

            Set<ColumnSet> keySet = this.allMap.keySet();
            for (ColumnSet cs : keySet) {
                List<TdColumn> userColumns = new ArrayList<TdColumn>();
                List<TdColumn> list = this.allMap.get(cs);
                for (TdColumn column : list) {
                    if (userColumnsAll.contains(column)) {
                        userColumns.add(column);
                    }
                }
                if (!userColumns.isEmpty()) {
                    this.userMap.put(cs, userColumns);
                }
            }
        }
    }

    @Override
    protected void updateStatusBySelection() {
        // empty implement here, because it could select different table and the OK button are enabled.
    }

    /**
     * DOC xqliu ColumnsMapSelectionDialog class global comment. Detailled comment
     */
    class ConnectionColumnSetContentProvider extends ResourceViewContentProvider {

        List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();

        public ConnectionColumnSetContentProvider() {
            super();
        }

        /**
         * DOC xqliu Comment method "init".
         * 
         * @param map
         */
        public void init(Map<ColumnSet, List<TdColumn>> map) {
            List<RepositoryNode> columnSetNodes = new ArrayList<RepositoryNode>();
            List<RepositoryNode> folderNodes = new ArrayList<RepositoryNode>();
            List<RepositoryNode> schemaNodes = new ArrayList<RepositoryNode>();
            List<RepositoryNode> catalogNodes = new ArrayList<RepositoryNode>();

            if (map != null && !map.isEmpty()) {
                Set<ColumnSet> keySet = map.keySet();
                for (ColumnSet cs : keySet) {
                    columnSetNodes.add(RepositoryNodeHelper.recursiveFind(cs));
                }

                for (RepositoryNode csNode : columnSetNodes) {
                    RepositoryNode parent = csNode.getParent();
                    if (!folderNodes.contains(parent)) {
                        folderNodes.add(parent);
                    }
                }

                for (RepositoryNode folderNode : folderNodes) {
                    RepositoryNode parent = folderNode.getParent();
                    if (parent instanceof DBSchemaRepNode) {
                        if (!schemaNodes.contains(parent)) {
                            schemaNodes.add(parent);
                        }
                    } else if (parent instanceof DBCatalogRepNode) {
                        if (!catalogNodes.contains(parent)) {
                            catalogNodes.add(parent);
                        }
                    }
                }

                for (RepositoryNode schemaNode : schemaNodes) {
                    RepositoryNode parent = schemaNode.getParent();
                    if (parent instanceof DBCatalogRepNode) {
                        if (!catalogNodes.contains(parent)) {
                            catalogNodes.add(parent);
                        }
                    }
                }
            }

            this.nodes.addAll(columnSetNodes);
            this.nodes.addAll(folderNodes);
            this.nodes.addAll(schemaNodes);
            this.nodes.addAll(catalogNodes);
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            List result = new ArrayList();

            Object[] children = super.getChildren(parentElement);

            if (children != null && children.length > 0) {
                if (parentElement instanceof DBConnectionRepNode || parentElement instanceof DBCatalogRepNode
                        || parentElement instanceof DBSchemaRepNode || parentElement instanceof DBTableFolderRepNode
                        || parentElement instanceof DBViewFolderRepNode) {
                    for (Object node : children) {
                        if (this.nodes.contains(node)) {
                            result.add(node);
                        }
                    }
                }
            }
            return result.toArray();
        }

        @Override
        public Object getParent(Object element) {
            Object parent = null;
            if (element instanceof RepositoryNode) {
                RepositoryNode repoNode = (RepositoryNode) element;
                parent = repoNode.getParent();
            } else {
                parent = super.getParent(element);
            }
            return parent;
        }

        @Override
        public boolean hasChildren(Object element) {
            return getChildren(element).length > 0;
        }
    }

    /**
     * DOC xqliu ColumnsMapSelectionDialog class global comment. Detailled comment
     */
    class ColumnContentProvider extends ResourceViewContentProvider {

        private List<RepositoryNode> nodes = new ArrayList<RepositoryNode>();

        public ColumnContentProvider() {
            super();
        }

        public void init(List<? extends IRepositoryNode> checkedRepoNodes) {
            if (checkedRepoNodes != null && !checkedRepoNodes.isEmpty()) {
                for (IRepositoryNode iNode : checkedRepoNodes) {
                    this.nodes.add((RepositoryNode) iNode);

                }
            }
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            List result = new ArrayList();
            if (parentElement instanceof DBTableRepNode || parentElement instanceof DBViewRepNode) {
                IRepositoryNode repoNode = (IRepositoryNode) parentElement;
                if (repoNode.getChildren().size() != 0) {
                    List<IRepositoryNode> children = repoNode.getChildren().get(0).getChildren();
                    if (children != null && !children.isEmpty()) {
                        for (IRepositoryNode node : children) {
                            if (this.nodes.contains(node)) {
                                result.add(node);
                            }
                        }
                    }
                }
            }
            return result.toArray();
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
            return Boolean.FALSE;
        }
    }
}
