// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.TwoPartCheckSelectionDialog;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataprofiler.core.ui.wizard.analysis.table.TableContentProvider;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFConnectionFolderRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TablesSelectionDialog extends TwoPartCheckSelectionDialog {

    private Map<RepositoryNodeKey, List<IRepositoryNode>> packageCheckedMap;

    private TableSelectionType tableType;

    public TableSelectionType getTableType() {
        return this.tableType;
    }

    public void setTableType(TableSelectionType tableType) {
        this.tableType = tableType;
    }

    public TablesSelectionDialog(AbstractAnalysisMetadataPage metadataFormPage, Shell parent, String title,
            List<IRepositoryNode> columnSetList, String message, RepositoryNode connComboSelectNode) {
        super(metadataFormPage, parent, message);
        this.setDialogType(DIALOG_TYPE_TABLE);
        this.setInput(connComboSelectNode == null ? RepositoryNodeHelper.getRootNode(ERepositoryObjectType.METADATA)
                : connComboSelectNode);
        packageCheckedMap = new HashMap<RepositoryNodeKey, List<IRepositoryNode>>();
        initCheckedColumnSet(columnSetList);
        this.setTitle(title);
        this.tableType = TableSelectionType.TABLE;
    }

    @Override
    /**
     * DOC mzhao bug 9240 mzhao 2009-11-05.
     * 
     * @param columnSetList
     */
    protected void unfoldToCheckedElements() {
        // if there have a selected tree node, only unfold it is enough
        if (this.selectedTreeRepoNode != null) {
            Iterator<RepositoryNodeKey> it = packageCheckedMap.keySet().iterator();
            while (it.hasNext()) {
                RepositoryNodeKey csk = it.next();
                if (this.selectedTreeRepoNode.equals(csk.getPackageNode())) {
                    unfoldRepositoryNode(csk);
                    break;
                }
            }
        } else { // if there have not a selected tree node, unfold all nodes
            Iterator<RepositoryNodeKey> it = packageCheckedMap.keySet().iterator();
            while (it.hasNext()) {
                RepositoryNodeKey csk = it.next();
                unfoldRepositoryNode(csk);
            }
        }
    }

    private void unfoldRepositoryNode(RepositoryNodeKey csk) {
        IRepositoryNode packageNode = csk.getPackageNode();
        List<IRepositoryNode> checkedTableNodeList = packageCheckedMap.get(csk);
        if (checkedTableNodeList != null && !checkedTableNodeList.isEmpty()) {
            if (isHideNode(checkedTableNodeList) && RepositoryNodeHelper.isOpenDQCommonViewer()) {
                packageNode = findLastVisibleNode(checkedTableNodeList.get(0));
                this.setMessage(DefaultMessagesImpl.getString("ColumnSelectionDialog.CannotFindNodeMessage")); //$NON-NLS-1$
                if (packageNode == null) {
                    return;
                }
            }
            // // to get TableFolderNode or viewFolderNode
            IRepositoryNode selectNode = getAdaptLocationNode(packageNode, checkedTableNodeList.get(0));
            if (selectNode != null) {
                packageNode = selectNode;
            }
            getTreeViewer().expandToLevel(packageNode, 1);
            StructuredSelection structSel = new StructuredSelection(packageNode);
            getTreeViewer().setSelection(structSel);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.dialog.TwoPartCheckSelectionDialog#restoreCheckStatus()
     */
    @Override
    protected void restoreCheckStatus() {
        if (packageCheckedMap.keySet().size() == 0) {
            return;
        }
        RepositoryNodeKey[] packeNodeKeyArray = new RepositoryNodeKey[packageCheckedMap.keySet().size()];
        packeNodeKeyArray = packageCheckedMap.keySet().toArray(packeNodeKeyArray);
        List<IRepositoryNode> folderNodeList = new ArrayList<IRepositoryNode>();
        for (RepositoryNodeKey pKey : packeNodeKeyArray) {
            List<IRepositoryNode> allCheckedTableNodeList = packageCheckedMap.get(pKey);
            int addedCount = 0;
            for (IRepositoryNode tableOrViewNode : allCheckedTableNodeList) {
                // Add the adapted table or view node into list.
                if (addedCount == 2) {
                    break;
                }
                IRepositoryNode adaptLocationNode = null;
                adaptLocationNode = getAdaptLocationNode(pKey.getPackageNode(), tableOrViewNode);
                if (!folderNodeList.contains(adaptLocationNode)) {
                    folderNodeList.add(adaptLocationNode);
                    addedCount++;
                }
            }
        }
        getTreeViewer().setCheckedElements(folderNodeList.toArray());
    }

    @Override
    protected void restoreTableCheckStatus() {
        List<IRepositoryNode> allCheckedTables = this.getAllCheckedTables();
        if (!allCheckedTables.isEmpty()) {
            getTableViewer().setCheckedElements(allCheckedTables.toArray(new IRepositoryNode[allCheckedTables.size()]));
        }
    }

    private void initCheckedColumnSet(List<IRepositoryNode> columnSetList) {
        List<IRepositoryNode> packageList = new ArrayList<IRepositoryNode>();
        for (IRepositoryNode columnSetNode : columnSetList) {
            if (columnSetNode instanceof DBTableRepNode) {
                DBTableRepNode tableRepNode = (DBTableRepNode) columnSetNode;
                // MOD qiongli 2012-5-4 TDQ-5137 use the table parent(DBTableFolderRepNode not DBCatalogRepNode or
                // DBSchemaRepNode).
                IRepositoryNode parentPackageNode = tableRepNode.getParent();
                if (parentPackageNode != null) {
                    if (!packageList.contains(parentPackageNode)) {
                        packageList.add(parentPackageNode);
                    }
                    RepositoryNodeKey packageKey = new RepositoryNodeKey(parentPackageNode);
                    List<IRepositoryNode> repoNodelist = packageCheckedMap.get(packageKey);
                    if (repoNodelist == null) {
                        repoNodelist = new ArrayList<IRepositoryNode>();
                        this.packageCheckedMap.put(packageKey, repoNodelist);
                    }
                    repoNodelist.add(tableRepNode);
                }
            } else if (columnSetNode instanceof DBViewRepNode) {
                // support DBview
                DBViewRepNode viewRepNode = (DBViewRepNode) columnSetNode;
                IRepositoryNode parentPackageNode = viewRepNode.getParent();
                if (parentPackageNode != null) {
                    if (!packageList.contains(parentPackageNode)) {
                        packageList.add(parentPackageNode);
                    }
                    RepositoryNodeKey packageKey = new RepositoryNodeKey(parentPackageNode);
                    List<IRepositoryNode> repoNodelist = packageCheckedMap.get(packageKey);
                    if (repoNodelist == null) {
                        repoNodelist = new ArrayList<IRepositoryNode>();
                        this.packageCheckedMap.put(packageKey, repoNodelist);
                    }
                    repoNodelist.add(viewRepNode);
                }
            }
        }
        this.setInitialElementSelections(packageList);
    }

    @Override
    protected void initProvider() {
        fLabelProvider = new DQRepositoryViewLabelProvider();
        fContentProvider = new DBTreeViewContentProvider();
        sLabelProvider = new TableLabelProvider();
        sContentProvider = new TableContentProvider();
    }

    @Override
    protected void addCheckedListener() {
        // When user checks a checkbox in the tree, check all its children
        getTreeViewer().addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {

                TreePath treePath = new TreePath(new Object[] { event.getElement() });
                getTreeViewer().setSelection(new TreeSelection(treePath));

                if (event.getChecked()) {
                    Object element = event.getElement();
                    getTreeViewer().setSubtreeChecked(element, true);
                    if (element instanceof IRepositoryNode) {
                        setOutput(element);
                        handleTablesChecked((IRepositoryNode) element, true);
                    }

                } else {
                    Object element = event.getElement();
                    getTreeViewer().setSubtreeChecked(element, false);
                    if (element instanceof IRepositoryNode) {
                        setOutput(element);
                        handleTablesChecked((IRepositoryNode) element, false);
                    }
                }
                updateOKStatus();
            }
        });

        getTableViewer().addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                Object element = event.getElement();
                if (element instanceof DBTableRepNode || element instanceof DBViewRepNode) {
                    handleTableChecked((IRepositoryNode) element, event.getChecked());
                }
            }
        });
    }

    private IRepositoryNode[] getCheckedTableNodes(IRepositoryNode node) {
        RepositoryNodeKey packageKey = new RepositoryNodeKey(node);
        List<IRepositoryNode> repoNodeList = packageCheckedMap.get(packageKey);
        if (repoNodeList == null) {
            Boolean allCheckFlag = this.getTreeViewer().getChecked(node);
            this.getTableViewer().setAllChecked(allCheckFlag);
            repoNodeList = allCheckFlag ? RepositoryNodeHelper.getNamedColumnSetNodes(node) : new ArrayList<IRepositoryNode>();
            packageCheckedMap.put(packageKey, repoNodeList);
            return allCheckFlag ? repoNodeList.toArray(new IRepositoryNode[repoNodeList.size()]) : null;
        } else {
            return repoNodeList.toArray(new IRepositoryNode[repoNodeList.size()]);
        }
    }

    private void handleTableChecked(IRepositoryNode columnSet, Boolean checkedFlag) {
        IRepositoryNode parentPackageNode = columnSet.getParent();
        if (checkedFlag) {
            getTreeViewer().setChecked(parentPackageNode, true);
        }
        RepositoryNodeKey repositoryNodeKey = new RepositoryNodeKey(parentPackageNode);
        List<IRepositoryNode> repoNodeList = packageCheckedMap.get(repositoryNodeKey);
        if (repoNodeList == null) {
            repoNodeList = new ArrayList<IRepositoryNode>();
            packageCheckedMap.put(repositoryNodeKey, repoNodeList);
        }
        if (checkedFlag) {
            repoNodeList.add(columnSet);
        } else {
            repoNodeList.remove(columnSet);
        }
        // If the element in the right table check viewer are all DESELECTED, then clear the check status in the left
        // tree viewer.
        if (!checkedFlag && repoNodeList.size() == 0) {
            getTreeViewer().setChecked(parentPackageNode, false);
        }
    }

    private void handleTablesChecked(IRepositoryNode packageNode, Boolean checkedFlag) {
        RepositoryNodeKey key = new RepositoryNodeKey(packageNode);
        List<IRepositoryNode> repoNodeList = checkedFlag ? RepositoryNodeHelper.getNamedColumnSetNodes(packageNode) : null;
        packageCheckedMap.put(key, repoNodeList);
        getTableViewer().setAllChecked(checkedFlag);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.dialog.TwoPartCheckSelectionDialog#updateOKStatus()
     */
    @Override
    protected void updateOKStatus() {
        int initSize = 0;
        for (RepositoryNodeKey nodeKey : packageCheckedMap.keySet()) {
            List<IRepositoryNode> tableList = packageCheckedMap.get(nodeKey);
            initSize += tableList == null ? 0 : tableList.size();
        }
        if (initSize <= 0) {
            Status fCurrStatus = new Status(IStatus.WARNING, PlatformUI.PLUGIN_ID, IStatus.WARNING,
                    DefaultMessagesImpl.getString("TablesSelectionDialog.noTableFoundWarning", PluginConstant.SPACE_STRING), null); //$NON-NLS-1$
            this.updateStatus(fCurrStatus);
            return;
        }
        super.updateOKStatus();
    }

    @Override
    protected void addSelectionButtonListener(Button selectButton, Button deselectButton) {
        SelectionListener listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Object[] viewerElements = fContentProvider.getElements(getTreeViewer().getInput());
                if (fContainerMode) {
                    getTreeViewer().setCheckedElements(viewerElements);
                } else {
                    for (Object viewerElement : viewerElements) {
                        getTreeViewer().setSubtreeChecked(viewerElement, true);
                    }
                }
                packageCheckedMap.clear();
                if (getTableViewer().getInput() != null) {
                    handleTablesChecked((IRepositoryNode) getTableViewer().getInput(), true);
                }
                updateOKStatus();
            }
        };
        selectButton.addSelectionListener(listener);

        listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getTreeViewer().setCheckedElements(new Object[0]);
                packageCheckedMap.clear();
                if (getTableViewer().getInput() != null) {
                    handleTablesChecked((IRepositoryNode) getTableViewer().getInput(), false);
                }
                updateOKStatus();
            }
        };
        deselectButton.addSelectionListener(listener);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        Object selectedObj = ((IStructuredSelection) event.getSelection()).getFirstElement();
        if (selectedObj != null) {
            if (selectedObj instanceof DBTableFolderRepNode || selectedObj instanceof DBViewFolderRepNode) {
                IRepositoryNode node = (IRepositoryNode) selectedObj;
                this.selectedTreeRepoNode = node.getParent();
                this.setOutput(node);
                IRepositoryNode[] tables = getCheckedTableNodes(node);
                if (tables != null) {
                    this.getTableViewer().setCheckedElements(tables);
                }
            }
        }
    }

    /**
     * This class will combine catlogName and/or schemaName as a key.
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class RepositoryNodeKey {

        private static final String CATALOG_NAME = "__Catalog_Name__";//$NON-NLS-1$

        private static final String SCHEMA_NAME = "__Schema_Name__";//$NON-NLS-1$

        private String catalogName;

        private String schemaName;

        private IRepositoryNode node = null;

        public RepositoryNodeKey(IRepositoryNode node) {
            this.node = node;
            initNames(node);
        }

        private void initNames(IRepositoryNode node) {
            if (node instanceof DBCatalogRepNode) {
                DBCatalogRepNode catalogNode = (DBCatalogRepNode) node;
                catalogName = SwitchHelpers.CATALOG_SWITCH.doSwitch(catalogNode.getCatalog()) == null ? CATALOG_NAME
                        : SwitchHelpers.CATALOG_SWITCH.doSwitch(catalogNode.getCatalog()).getName();
                schemaName = SCHEMA_NAME;
            } else if (node instanceof DBSchemaRepNode) {
                DBSchemaRepNode schemaNode = (DBSchemaRepNode) node;
                RepositoryNode parentNode = schemaNode.getParent();
                if (parentNode instanceof DBCatalogRepNode) {
                    initNames(parentNode);
                } else {
                    catalogName = CATALOG_NAME;
                }
                schemaName = SwitchHelpers.SCHEMA_SWITCH.doSwitch(schemaNode.getSchema()) == null ? SCHEMA_NAME
                        : SwitchHelpers.SCHEMA_SWITCH.doSwitch(schemaNode.getSchema()).getName();
            } else if (node instanceof DBTableFolderRepNode || node instanceof DBViewFolderRepNode) {
                initNames(node.getParent());
                // MOD qiongli 2012-5-4 TDQ-5137.
                this.node = node.getParent();
            } else {
                catalogName = CATALOG_NAME;
                schemaName = SCHEMA_NAME;
            }
        }

        public IRepositoryNode getPackageNode() {
            return this.node;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((catalogName == null) ? 0 : catalogName.hashCode());
            result = prime * result + ((schemaName == null) ? 0 : schemaName.hashCode());
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
            final RepositoryNodeKey other = (RepositoryNodeKey) obj;
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

    @Override
    protected void computeResult() {
        setResult(getAllCheckedTables());
    }

    private List<IRepositoryNode> getAllCheckedTables() {
        Object[] checkedNodes = this.getTreeViewer().getCheckedElements();
        List<IRepositoryNode> tableList = new ArrayList<IRepositoryNode>();
        for (Object obj : checkedNodes) {
            if (!(obj instanceof DBTableFolderRepNode || obj instanceof DBViewFolderRepNode)) {
                continue;
            }
            IRepositoryNode node = (IRepositoryNode) obj;
            RepositoryNodeKey packageKey = new RepositoryNodeKey(node);
            if (packageCheckedMap.containsKey(packageKey)) {
                tableList.addAll(packageCheckedMap.get(packageKey));
            } else {
                tableList.addAll(RepositoryNodeHelper.getNamedColumnSetNodes(node));
            }
        }

        // ADD msjian 2011-7-8 feature 22206: Add filters
        // add the miss of before checked when use the filter
        Iterator<?> it = packageCheckedMap.keySet().iterator();
        while (it.hasNext()) {
            RepositoryNodeKey packageKey = (RepositoryNodeKey) it.next();
            List<?> checkedTableNodeList = packageCheckedMap.get(packageKey);
            if (checkedTableNodeList != null) {
                for (int i = 0; i < checkedTableNodeList.size(); i++) {
                    if (!tableList.contains(checkedTableNodeList.get(i))) {
                        tableList.add((IRepositoryNode) checkedTableNodeList.get(i));
                    }
                }
            }
        }

        return tableList;
    }

    @Override
    protected void okPressed() {
        List<IRepositoryNode> allCheckedTables = getAllCheckedTables();
        // Added TDQ-8718 20140505 yyin: if no columns checked, warn the user
        if (allCheckedTables.size() == 0) {
            MessageDialogWithToggle.openWarning(this.getShell(), DefaultMessagesImpl.getString("ColumnSelectionDialog.warning"),//$NON-NLS-1$ 
                    DefaultMessagesImpl.getString("TablesSelectionDialog.NoTableSelected"));//$NON-NLS-1$
            return;
        }// ~
        super.okPressed();
        this.packageCheckedMap = null;
    }

    /**
     * label provider for table viewer.
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class TableLabelProvider extends LabelProvider {

        @Override
        public Image getImage(Object element) {
            if (element instanceof DBTableRepNode) {
                return ImageLib.getImage(ImageLib.TABLE);
            } else if (element instanceof DBViewRepNode) {
                return ImageLib.getImage(ImageLib.VIEW);
            } else {
                return null;
            }
        }

        @Override
        public String getText(Object element) {
            if (element instanceof IRepositoryNode) {
                return ((IRepositoryNode) element).getLabel();
            }
            return "";//$NON-NLS-1$
        }

    }

    /**
     * DOC xqliu TablesSelectionDialog class global comment. Detailled comment
     */
    class DBTreeViewContentProvider extends ResourceViewContentProvider {

        public DBTreeViewContentProvider() {
            super();
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            if (!(parentElement instanceof DBTableFolderRepNode || parentElement instanceof DBViewFolderRepNode)) {
                if (parentElement instanceof IRepositoryNode) {
                    IRepositoryNode repoNode = (IRepositoryNode) parentElement;
                    return filterTableView(repoNode.getChildren()).toArray();
                }
            }
            return null;
        }

        @Override
        public Object getParent(Object element) {
            if (element instanceof IRepositoryNode) {
                return ((IRepositoryNode) element).getParent();
            }
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof IRepositoryNode) {
                if (!(element instanceof DBTableFolderRepNode || element instanceof DBViewFolderRepNode)) {
                    return ((IRepositoryNode) element).hasChildren();
                }
            }
            return false;
        }

    }

    /**
     * Table Selection Dialog type enum.
     */
    public enum TableSelectionType {
        TABLE("table", "Table"), //$NON-NLS-1$//$NON-NLS-2$
        VIEW("view", "View"), //$NON-NLS-1$//$NON-NLS-2$
        ALL("all", "Table/View"); //$NON-NLS-1$//$NON-NLS-2$

        private String key;

        private String label;

        public String getKey() {
            return this.key;
        }

        public String getLabel() {
            return this.label;
        }

        TableSelectionType() {
            this("table", "Table"); //$NON-NLS-1$//$NON-NLS-2$
        }

        TableSelectionType(String key) {
            this(key, key);
        }

        TableSelectionType(String key, String label) {
            this.key = key;
            this.label = label;
        }
    }

    /**
     * filter for table and view according to the dialog's type.
     * 
     * @param nodes
     * @return
     */
    public List<IRepositoryNode> filterTableView(List<IRepositoryNode> nodes) {
        List<IRepositoryNode> list = new ArrayList<IRepositoryNode>();
        for (IRepositoryNode node : nodes) {
            // MOD klliu 2001-03-08 bug 19243: In table selection wizard should filter mdm and file connection
            if (!(node instanceof DFConnectionFolderRepNode)) {
                if (node instanceof DBTableFolderRepNode) {
                    if (TableSelectionType.TABLE.equals(getTableType()) || TableSelectionType.ALL.equals(getTableType())) {
                        list.add(node);
                    }
                } else if (node instanceof DBViewFolderRepNode) {
                    if (TableSelectionType.VIEW.equals(getTableType()) || TableSelectionType.ALL.equals(getTableType())) {
                        list.add(node);
                    }
                } else {
                    list.add(node);
                }
            }
        }
        return list;
    }
}
