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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.provider.DBTablesViewLabelProvider;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.filters.ColumnAndFolderNodeFilter;
import org.talend.dataprofiler.core.ui.filters.DQFolderFilter;
import org.talend.dataprofiler.core.ui.filters.EMFObjFilter;
import org.talend.dataprofiler.core.ui.filters.HadoopCLusterFolderNodeFilter;
import org.talend.dataprofiler.core.ui.filters.TDQEEConnectionFolderFilter;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DFColumnRepNode;
import org.talend.dq.nodes.DFTableRepNode;
import org.talend.dq.nodes.DQDBFolderRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * @author mzhao<br>
 * Select the special columns from this dialog.
 * 
 */
public class ColumnsSelectionDialog extends TwoPartCheckSelectionDialog {

    // Multiple map: one left checked element map to multiple right checked map. (e.g:
    // tableA->columnA,columnB,columnC...)
    protected MultiMap modelElementCheckedMap = null;

    // Root node (database/xml/flat file connection)
    private RepositoryNode connNode = null;

    public ColumnsSelectionDialog(AbstractAnalysisMetadataPage metadataFormPage, Shell parent, String title,
            List<? extends IRepositoryNode> checkedRepoNodes, RepositoryNode rootNode, String message) {
        super(metadataFormPage, parent, message);
        initDialog(title, checkedRepoNodes);
        connNode = rootNode;
        setInput(connNode);
    }

    public ColumnsSelectionDialog(AbstractAnalysisMetadataPage metadataFormPage, Shell parent, String title,
            List<? extends IRepositoryNode> checkedRepoNodes, String message, boolean addConnFilter) {
        super(metadataFormPage, parent, message, addConnFilter);
        initDialog(title, checkedRepoNodes);
    }

    /**
     * init this Dialog.
     * 
     * @param title
     * @param checkedRepoNodes
     */
    private void initDialog(String title, List<? extends IRepositoryNode> checkedRepoNodes) {
        modelElementCheckedMap = new MultiValueMap();
        initCheckedElements(checkedRepoNodes);
        addFilter(new EMFObjFilter());
        addFilter(new DQFolderFilter(true));
        addFilter(new TDQEEConnectionFolderFilter());
        // ADD msjian TDQ-10441: hide the hadoop cluster folder node
        addFilter(new HadoopCLusterFolderNodeFilter());
        // TDQ-10441~
        // ADD msjian TDQ-11253: hide the column folder nodes and column nodes
        addFilter(new ColumnAndFolderNodeFilter());
        // TDQ-11253~
        setTitle(title);
    }

    @Override
    /**
     * DOC mzhao bug 9240 mzhao 2009-11-05.
     */
    protected void unfoldToCheckedElements() {
        if (this.selectedTreeRepoNode != null) {
            // if there have a selected tree node, only unfold it is enough
            unfoldRepositoryNode(this.selectedTreeRepoNode);
        } else {
            // if there have not a selected tree node, unfold all nodes
            Iterator<?> it = modelElementCheckedMap.keySet().iterator();
            while (it.hasNext()) {
                IRepositoryNode selectNode = (IRepositoryNode) it.next();
                unfoldRepositoryNode(selectNode);
                break;
            }
        }
    }

    private void unfoldRepositoryNode(IRepositoryNode selectNode) {
        IRepositoryNode reposNode = selectNode;
        List<IRepositoryNode> columnNodeList = (List<IRepositoryNode>) modelElementCheckedMap.get(selectNode);
        if (columnNodeList != null && !columnNodeList.isEmpty()) {
            if (isHideNode(columnNodeList) && RepositoryNodeHelper.isOpenDQCommonViewer()) {
                reposNode = findLastVisibleNode(columnNodeList.get(0));
                this.setMessage(DefaultMessagesImpl.getString("ColumnSelectionDialog.CannotFindNodeMessage")); //$NON-NLS-1$
                if (reposNode == null) {
                    return;
                }
            }
            selectNode = getAdaptLocationNode(reposNode, columnNodeList.get(0));
            if (selectNode != null) {
                reposNode = selectNode;
            }
            getTreeViewer().expandToLevel(reposNode, 1);
            StructuredSelection structSel = new StructuredSelection(reposNode);
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
        Object[] checkElementArray = modelElementCheckedMap.keySet().toArray();
        if (checkElementArray.length > 0) {
            getTreeViewer().setCheckedElements(checkElementArray);
            getTreeViewer().expandToLevel(checkElementArray[0], AbstractTreeViewer.ALL_LEVELS);
        }
    }

    @Override
    protected void restoreTableCheckStatus() {
        this.getAllCheckElements();
        if (!this.allCheckedElements.isEmpty()) {
            getTableViewer().setCheckedElements(
                    this.allCheckedElements.toArray(new IRepositoryNode[this.allCheckedElements.size()]));
        }
    }

    /**
     * DOC mzhao Initiate the checked list in column selection dialog.
     * 
     * @param reposNodeLs
     */
    private void initCheckedElements(List<? extends IRepositoryNode> reposNodeLs) {
        List<IRepositoryNode> containerList = new ArrayList<IRepositoryNode>();
        for (int i = 0; i < reposNodeLs.size(); i++) {
            RepositoryNode repNode = (RepositoryNode) reposNodeLs.get(i);
            // IRepositoryNode parentNode = repNode.getParent().getParent();
            IRepositoryNode parentNode = getParentNode(repNode);
            // Add the parent->children relation to the multi map

            modelElementCheckedMap.put(parentNode, repNode);
            // Check the parent element either if any of their columns had already been checked.
            if (!containerList.contains(parentNode)) {
                containerList.add(parentNode);
            }
        }
        this.setInitialElementSelections(containerList);
    }

    /**
     * DOC xqliu Comment method "getParentNode".
     * 
     * @param repNode
     * @return
     */
    protected IRepositoryNode getParentNode(RepositoryNode repNode) {
        IRepositoryNode node = repNode.getParent();
        if (repNode instanceof DBColumnRepNode || repNode instanceof DFColumnRepNode) {
            node = repNode.getParent().getParent();
        }
        return node;
    }

    @Override
    protected void initProvider() {
        fLabelProvider = new DBTablesViewLabelProvider();
        fContentProvider = new DBTreeViewContentProvider();
        sLabelProvider = new DBTablesViewLabelProvider();
        sContentProvider = new ModelElementContentProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.dataprofiler.core.ui.dialog.TwoPartCheckSelectionDialog# addCheckedListener()
     */
    @Override
    protected void addCheckedListener() {

        // When user checks a checkbox in the tree, check all its children
        getTreeViewer().addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                // Added TDQ-8718 20140506 yyin: should not load from db when checking
                DQDBFolderRepositoryNode.setCallingFromColumnDialog(true);
                DQDBFolderRepositoryNode.setLoadDBFromDialog(false);
                try {
                    ColumnSelectionViewer columnViewer = (ColumnSelectionViewer) event.getSource();
                    TreePath treePath = new TreePath(new Object[] { event.getElement() });
                    columnViewer.setSelection(new TreeSelection(treePath));
                    setOutput(event.getElement());
                    RepositoryNode selectedNode = (RepositoryNode) event.getElement();
                    if (selectedNode instanceof DBTableRepNode || selectedNode instanceof DBViewRepNode
                            || selectedNode instanceof DFTableRepNode) {
                        handleTreeElementsChecked(selectedNode, event.getChecked());
                    } else {
                        checkChildrenElements(selectedNode, event.getChecked());
                    }
                    getTreeViewer().setSubtreeChecked(event.getElement(), event.getChecked());
                    updateStatusBySelection();
                } finally {
                    // Added TDQ-8718 20140506 yyin
                    DQDBFolderRepositoryNode.setCallingFromColumnDialog(false);
                    DQDBFolderRepositoryNode.setLoadDBFromDialog(true);
                }
            }

            private void checkChildrenElements(RepositoryNode repNode, Boolean checkedFlag) {
                Set<?> keySet = modelElementCheckedMap.keySet();

                RepositoryNode[] repNodeArray = (RepositoryNode[]) modelElementCheckedMap.keySet().toArray(
                        new RepositoryNode[keySet.size()]);
                for (RepositoryNode reposNode : repNodeArray) {
                    RepositoryNode parentNode = reposNode.getParent();
                    while (parentNode != null) {
                        if (repNode.equals(parentNode)) {
                            handleTreeElementsChecked(reposNode, checkedFlag);
                            break;
                        } else {
                            parentNode = parentNode.getParent();
                        }
                    }
                }
            }
        });

        getTableViewer().addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                handleTableElementsChecked((RepositoryNode) event.getElement(), event.getChecked());
                updateStatusBySelection();
            }
        });
    }

    /**
     * Update the status of dailog by select action
     */
    protected void updateStatusBySelection() {
        Status fCurrStatus = new Status(IStatus.OK, PlatformUI.PLUGIN_ID, IStatus.OK, PluginConstant.EMPTY_STRING, null);
        // TDQ-12215: filter the tableNodes when it has set column filter, because they are duplicate in the map.
        Set<?> keySet = modelElementCheckedMap.keySet();
        RepositoryNode[] tableNodesFromMap = keySet.toArray(new RepositoryNode[keySet.size()]);

        List<RepositoryNode> allcheckedTableNodes = getAllCheckedTableNodes();
        // when checked table count >1, means there are more than one table's column selected. then make the OK status disable
        if (allcheckedTableNodes.size() > 1) {
            fCurrStatus = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.ERROR, DefaultMessagesImpl.getString(
                    "ColumnMasterDetailsPage.noSameTableWarning", PluginConstant.SPACE_STRING), null); //$NON-NLS-1$
        } else if (tableNodesFromMap.length <= 0) {// current connection is come from another reference project and no columns
                                                   // checked on right tree.
            Item item = connNode == null ? null : connNode.getObject().getProperty().getItem();
            if (item != null && DqRepositoryViewService.isComeFromRefrenceProject(((ConnectionItem) item).getConnection())) {
                fCurrStatus = new Status(IStatus.WARNING, PlatformUI.PLUGIN_ID, IStatus.WARNING, DefaultMessagesImpl.getString(
                        "ColumnMasterDetailsPage.noColumnFoundWarning", PluginConstant.SPACE_STRING), null); //$NON-NLS-1$ 
            }
        }

        updateStatus(fCurrStatus);
    }

    /**
     * 
     * Compute the checked Table Nodes.
     * 
     * @param repNodeArray
     * @return
     */
    private List<RepositoryNode> getAllCheckedTableNodes() {
        Set<?> keySet = modelElementCheckedMap.keySet();
        RepositoryNode[] tableNodesFromMap = keySet.toArray(new RepositoryNode[keySet.size()]);
        List<RepositoryNode> allcheckedTableNodes = new ArrayList<RepositoryNode>();
        for (Object checkElement : this.getTreeViewer().getCheckedElements()) {
            if ((checkElement instanceof DBTableRepNode || checkElement instanceof DBViewRepNode || checkElement instanceof DFTableRepNode)
                    && ((RepositoryNode) checkElement).getId() != null) {
                allcheckedTableNodes.add((RepositoryNode) checkElement);
            }
        }
        for (RepositoryNode node : tableNodesFromMap) {
            if (node.getId() == null) {
                continue;
            }
            if (!allcheckedTableNodes.contains(node)) {
                allcheckedTableNodes.add(node);
            }
        }
        return allcheckedTableNodes;
    }

    /**
     * 
     * when the table count >1, means there are more than one table's column selected. then make the ok status disable
     * 
     * @param repNodes
     * @param tableCount
     * @return
     */
    // private Status getStatusByTableCount(Object[] repNodes) {
    // int tableCount = 0;
    // Status fCurrStatus = new Status(IStatus.OK, PlatformUI.PLUGIN_ID, IStatus.OK, PluginConstant.EMPTY_STRING, null);
    // for (Object node : repNodes) {
    // if (!(node instanceof RepositoryNode) || ((RepositoryNode) node).getId() == null) {
    // continue;
    // }
    // tableCount++;
    // }
    //
    // // TDQ-12215~
    //
    // if (tableCount > 1) {
    // fCurrStatus = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.ERROR, DefaultMessagesImpl.getString(
    //                    "ColumnMasterDetailsPage.noSameTableWarning", PluginConstant.SPACE_STRING), null); //$NON-NLS-1$ 
    // }
    // return fCurrStatus;
    // }

    /**
     * 
     * DOC mzhao Handle check state of table view elements. propagate to tree viewer and multi map.
     * 
     * @param reposNode
     * @param checkedFlag
     */
    protected void handleTableElementsChecked(RepositoryNode reposNode, Boolean checkedFlag) {
        // RepositoryNode tableParent = reposNode.getParent().getParent();
        IRepositoryNode tableParent = getParentNode(reposNode);
        if (checkedFlag) {
            getTreeViewer().setChecked(tableParent, checkedFlag);
            modelElementCheckedMap.put(tableParent, reposNode);
        } else {
            // If there is no elements checked in table view (right panel), then the parent element in tree viewer
            // should be unchecked.
            List<?> checkedList = (List<?>) modelElementCheckedMap.get(tableParent);
            if (checkedList != null) {
                checkedList.remove(reposNode);
                if (checkedList.size() == 0) {
                    // all the sub nodes has been unchecked, so need to uncheck the parent and remove it from the map
                    getTreeViewer().setChecked(tableParent, checkedFlag);
                    modelElementCheckedMap.remove(tableParent);
                }
            }
        }
    }

    /**
     * 
     * DOC mzhao Handle check events. propagate the state to the table viewer and update the multi map.
     * 
     * @param repNode
     * @param checkedFlag
     */
    protected void handleTreeElementsChecked(RepositoryNode repNode, Boolean checkedFlag) {
        if (checkedFlag) {
            // MOD klliu 2011-03-03 bug 19195 the MDM node is defferent from DF/DB connection Structure
            // MDM does not have Column folder

            List<IRepositoryNode> children = repNode.getChildren();
            for (IRepositoryNode colFolderNode : children) {
                for (IRepositoryNode colNode : colFolderNode.getChildren()) {
                    modelElementCheckedMap.put(repNode, colNode);
                }
            }
        } else {
            modelElementCheckedMap.remove(repNode);
        }
        getTableViewer().setAllChecked(checkedFlag);
    }

    /**
     * Added TDQ-8718 20140506 yyin: when expand the nodes in the column select dialog, should refresh the table/view
     * folder. otherwise: if the parent catalog is checked before expanding, it may get no children(without connecting
     * to db) If the user has checked some high level node, must refresh the table/view folder node. otherwise, it
     * always return 0
     */
    @Override
    protected void handleTreeExpanded(TreeItem item) {
        Object node = item.getData();

        if (node instanceof DBCatalogRepNode || node instanceof DBSchemaRepNode) {
            TreeItem[] children = item.getItems();
            if (children != null) {
                for (TreeItem child : children) {
                    Object data = child.getData();
                    if (data != null) {
                        if (!(data instanceof DQDBFolderRepositoryNode)) {
                            break;
                        } else {
                            getTreeViewer().refresh(data, true);
                        }
                    }
                }
            }
        }

        super.handleTreeExpanded(item);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.dataprofiler.core.ui.dialog.TwoPartCheckSelectionDialog#
     * addSelectionButtonListener(org.eclipse.swt .widgets.Button, org.eclipse.swt.widgets.Button)
     */
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
                modelElementCheckedMap.clear();
                if (getTableViewer().getInput() != null) {
                    handleTreeElementsChecked((RepositoryNode) getTableViewer().getInput(), true);
                }
                updateOKStatus();
            }
        };
        selectButton.addSelectionListener(listener);

        listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                getTreeViewer().setCheckedElements(new Object[0]);
                modelElementCheckedMap.clear();
                if (getTableViewer().getInput() != null) {
                    handleTreeElementsChecked((RepositoryNode) getTableViewer().getInput(), false);
                }
                updateOKStatus();
            }
        };
        deselectButton.addSelectionListener(listener);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        // MOD klliu 2011-01-27 15750 todo 31
        RepositoryNode selectedObj = (RepositoryNode) ((IStructuredSelection) event.getSelection()).getFirstElement();
        if (selectedObj != null) {
            if (selectedObj.hasChildren()) {
                this.selectedTreeRepoNode = selectedObj;
                this.setOutput(selectedObj);
                Boolean allCheckFlag = this.getTreeViewer().getChecked(selectedObj);
                List<?> repositoryNodeList = (List<?>) modelElementCheckedMap.get(selectedObj);
                if (repositoryNodeList != null) {
                    this.getTableViewer().setCheckedElements(repositoryNodeList.toArray());
                }// MOD klliu check Table/View node is select
                else if (selectedObj instanceof DBTableRepNode || selectedObj instanceof DBViewRepNode
                        || selectedObj instanceof DFTableRepNode) {
                    if (allCheckFlag) {
                        this.getTableViewer().setCheckedElements(selectedObj.getChildren().get(0).getChildren().toArray());
                    }
                }
                // ~
            }
        }
    }

    List<IRepositoryNode> allCheckedElements = new ArrayList<IRepositoryNode>();

    protected void getAllCheckElements() {
        // Added TDQ-8718 20140506 yyin: should not load from DB(connect) when get checked elements
        DQDBFolderRepositoryNode.setCallingFromColumnDialog(true);
        DQDBFolderRepositoryNode.setLoadDBFromDialog(false);
        try {
            Object[] checkedNodes = this.getTreeViewer().getCheckedElements();

            for (Object checkedNode : checkedNodes) {
                IRepositoryNode repNode = (IRepositoryNode) checkedNode;
                if (repNode instanceof DBColumnRepNode || repNode instanceof DFColumnRepNode) {
                    if (!allCheckedElements.contains(repNode)) {
                        allCheckedElements.add(repNode);
                    }
                } else if (repNode instanceof DFTableRepNode || repNode instanceof DBTableRepNode
                        || repNode instanceof DBViewRepNode) {
                    if (!getTableviewCheckedElements(allCheckedElements, repNode)) {
                        List<IRepositoryNode> children = repNode.getChildren().get(0).getChildren();
                        for (IRepositoryNode node : children) {
                            if (!allCheckedElements.contains(node)) {
                                allCheckedElements.add(node);
                            }
                        }
                    }
                }
            }
        } finally {
            // Added TDQ-8718 20140506 yyin
            DQDBFolderRepositoryNode.setCallingFromColumnDialog(false);
            DQDBFolderRepositoryNode.setLoadDBFromDialog(true);
        }

        // ADD msjian 2011-7-8 feature 22206: Add filters
        Iterator<?> it = modelElementCheckedMap.keySet().iterator();
        while (it.hasNext()) {
            RepositoryNode reposNode = (RepositoryNode) it.next();
            List<?> columnList = (ArrayList<?>) modelElementCheckedMap.get(reposNode);
            for (int i = 0; i < columnList.size(); i++) {
                if (columnList.get(i) instanceof DBColumnRepNode || columnList.get(i) instanceof DFColumnRepNode) {
                    if (!allCheckedElements.contains(columnList.get(i))) {
                        allCheckedElements.add((IRepositoryNode) columnList.get(i));
                    }
                }
            }
        }
    }

    private boolean getTableviewCheckedElements(List<IRepositoryNode> allCheckedElements, IRepositoryNode repNode) {
        List<IRepositoryNode> columnFolder = (List<IRepositoryNode>) modelElementCheckedMap.get(repNode);
        if (columnFolder == null) {
            return false;
        }
        for (IRepositoryNode column : columnFolder) {
            // MOD msjian 2011-7-25 22206: for filter, avoid to add the exsit column
            if (!allCheckedElements.contains(column)) {
                allCheckedElements.add(column);
            }
        }
        return columnFolder.size() > 0;
    }

    @Override
    protected void okPressed() {
        allCheckedElements.clear();
        getAllCheckElements();
        // Added TDQ-8718 20140505 yyin: if no columns checked, warn the user
        if (allCheckedElements.size() == 0) {
            MessageDialogWithToggle.openWarning(this.getShell(), DefaultMessagesImpl.getString("ColumnSelectionDialog.warning"),//$NON-NLS-1$ 
                    DefaultMessagesImpl.getString("ColumnSelectionDialog.NoColumnSelected"));//$NON-NLS-1$
            return;
        }// ~
        super.okPressed();
        this.modelElementCheckedMap = null;
    }

    @Override
    protected void computeResult() {
        setResult(allCheckedElements);
    }

    /**
     * @author rli
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class ModelElementContentProvider extends ResourceViewContentProvider {

        @Override
        public Object[] getChildren(Object parentElement) {
            // Table || view node
            if (parentElement instanceof DBTableRepNode || parentElement instanceof DBViewRepNode
                    || parentElement instanceof DFTableRepNode) {
                IRepositoryNode repoNode = (IRepositoryNode) parentElement;
                // feature 22206 2011-7-12 msjian: fixed its note 91852 issue1
                if (repoNode.getChildren().size() == 0) {
                    return repoNode.getChildren().toArray();
                }
                return repoNode.getChildren().get(0).getChildren().toArray();
            }
            // if (parentElement instanceof DBColumnFolderRepNode || parentElement instanceof MDMSchemaRepNode) {
            // IRepositoryNode repoNode = (IRepositoryNode) parentElement;
            // return repoNode.getChildren().toArray();
            // }
            if (parentElement instanceof DBColumnRepNode || parentElement instanceof DFColumnRepNode) {
                return new Object[] { parentElement };
            }

            return new Object[0];
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
            // ~
        }
    }

    /**
     * DOC zqin ColumnsSelectionDialog class global comment. Detailled comment
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class DBTreeViewContentProvider extends ResourceViewContentProvider {

        public DBTreeViewContentProvider() {
            super();
        }

        @Override
        public Object[] getChildren(Object parentElement) {
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
            if (element instanceof RepositoryNode) {
                RepositoryNode repoNode = (RepositoryNode) element;
                if (repoNode instanceof DBTableRepNode || repoNode instanceof DBViewRepNode || repoNode instanceof DFTableRepNode) {
                    return Boolean.FALSE;
                }
                return repoNode.hasChildren();
            }
            return super.hasChildren(element);
        }

    }
}
