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
package org.talend.dataprofiler.core.ui.dialog.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC rli class global comment. Detailled comment
 */
public class DeleteModelElementConfirmDialog {

    private static final String REQUIRES = "requires"; //$NON-NLS-1$

    private static LabelProvider fLabelProvider;

    private static List<ImpactNode> impactNodes = new ArrayList<ImpactNode>();

    /**
     * DOC rli DeleteModelElementConfirmDialog class global comment. Detailled comment
     */
    static class ImpactNode {

        List<Object> children = new ArrayList<Object>();

        private ModelElement nodeElement = null;

        private IRepositoryNode node = null;

        /**
         * Getter for node.
         * 
         * @return the node
         */
        public IRepositoryNode getNode() {
            return this.node;
        }

        /**
         * Sets the node.
         * 
         * @param node the node to set
         */
        public void setNode(IRepositoryNode node) {
            this.node = node;
        }

        public ImpactNode(ModelElement modelElement) {
            this.nodeElement = modelElement;
        }

        /**
         * DOC yyin ImpactNode constructor comment.
         * 
         * @param node
         */
        public ImpactNode(IRepositoryNode node) {
            this.node = node;
        }

        public void addRequireModelElement(Object object) {
            if (!children.contains(object)) {
                this.children.add(object);
            }
        }

        public Object[] getChildren() {
            return children.toArray(new Object[children.size()]);
        }

        @Override
        public String toString() {
            if (this.nodeElement != null) {
                return nodeElement.getName();
            } else {
                return this.node.getObject().getLabel();
            }
        }

        /**
         * Getter for nodeElement.
         * 
         * @return the nodeElement
         */
        public ModelElement getNodeElement() {
            return nodeElement;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((nodeElement == null) ? 0 : nodeElement.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
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
            final ImpactNode other = (ImpactNode) obj;
            if (nodeElement == null) {
                if (other.nodeElement != null) {
                    return false;
                }
            } else if (nodeElement.getName() == other.nodeElement.getName()) {
                return true;
            } else if (!nodeElement.getName().equals(other.nodeElement.getName())) {
                return false;
            }
            return true;
        }

    }

    static void addDenpendencyElements(ModelElement[] children) {
        for (ModelElement element : children) {
            EList<Dependency> supplierDependencies = element.getSupplierDependency();
            ImpactNode impactNode;
            for (Dependency dependency : supplierDependencies) {
                EList<ModelElement> clients = dependency.getClient();
                for (ModelElement client : clients) {
                    // ~MOD mzhao feature 7488 2009-08-21.
                    // if (DependenciesHandler.isTDQElementProperties(client)) {
                    // continue;
                    // }
                    // ~
                    if (!client.eIsProxy()) {
                        impactNode = new ImpactNode(client);
                        int index = impactNodes.indexOf(impactNode);
                        if (index == -1) {
                            impactNode.addRequireModelElement(element);
                            impactNodes.add(impactNode);
                        } else {
                            ImpactNode existNode = impactNodes.get(index);
                            existNode.addRequireModelElement(element);
                        }
                    }
                }
            }
        }
    }

    static ImpactNode[] getImpactNodes() {
        return impactNodes.toArray(new ImpactNode[impactNodes.size()]);
    }

    static void clear() {
        impactNodes.clear();
    }

    /**
     * DOC rli RequirsResourceConfirmDialog class global comment. Detailled comment
     */
    protected static class DialogContentProvider implements ITreeContentProvider {

        ImpactNode[] treeNode;

        DialogContentProvider(ImpactNode[] treeNode) {
            this.treeNode = treeNode;
        }

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof ImpactNode) {
                return ((ImpactNode) parentElement).getChildren();
            }
            return null;
        }

        public Object getParent(Object element) {
            return null;
        }

        public boolean hasChildren(Object element) {
            return element instanceof ImpactNode;
        }

        public Object[] getElements(Object inputElement) {
            return treeNode;
        }

        public void dispose() {
            // do nothing here ???
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // do nothing here ???
        }
    }

    /**
     * DOC bZhou Comment method "showDialog".
     * 
     * @param parentShell
     * @param file
     * @param dependencyElements
     * @param dialogMessage
     * @return
     */
    public static boolean showDialog(Shell parentShell, Object obj, ModelElement[] dependencyElements, String dialogMessage,
            boolean isNeedCheckbox) {
        // MOD qiongli 2011-3-1 feature 17588.return boolean,add 'dialog.setNeedCheckbox(true)'
        for (ModelElement element : dependencyElements) {
            ImpactNode node = new ImpactNode(element);
            if (!impactNodes.contains(node)) {
                node.addRequireModelElement(obj);
                impactNodes.add(node);
            }
        }

        ImpactNode[] impactElements = getImpactNodes();
        boolean isChecked = false;
        if (impactElements.length > 0) {
            TreeMessageInfoDialog dialog = new TreeMessageInfoDialog(parentShell,
                    DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmResourceDelete"), null, dialogMessage, //$NON-NLS-1$
                    MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL }, 1);
            dialog.setNeedCheckbox(isNeedCheckbox);
            dialog.setContentProvider(new DialogContentProvider(impactElements));
            dialog.setInput(new Object());
            clear();
            // MOD qiongli 2012-1-6 if don't click OK button,should return false;
            int result = dialog.open();
            isChecked = dialog.isChecked() && (result == Window.OK);

        }

        return isChecked;
    }

    /**
     * 
     * DOC qiongli Comment method "showDialog".
     * 
     * @param parentShell
     * @param repositoryNodes
     * @param dependencyElements
     * @param dialogMessage
     * @return
     */
    public static int showDialog(Shell parentShell, List<IRepositoryNode> repositoryNodes, String dialogMessage) {
        for (IRepositoryNode repNode : repositoryNodes) {
            List<ModelElement> dependencies = EObjectHelper.getDependencyClients(repNode);
            Object object = RepositoryNodeHelper.getModelElementFromRepositoryNode(repNode);
            for (ModelElement element : dependencies) {
                ImpactNode node = new ImpactNode(element);
                if (!impactNodes.contains(node)) {
                    node.addRequireModelElement(object);
                    impactNodes.add(node);
                } else if (!Arrays.asList(node.getChildren()).contains(object)) {
                    int index = impactNodes.indexOf(node);
                    if (index != -1) {
                        impactNodes.get(index).addRequireModelElement(object);
                    }
                }
            }
        }
        ImpactNode[] impactElements = getImpactNodes();
        if (impactElements.length > 0) {
            TreeMessageInfoDialog dialog = new TreeMessageInfoDialog(parentShell,
                    DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmResourceDelete"), null, dialogMessage, //$NON-NLS-1$
                    MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL }, 1);
            dialog.setContentProvider(new DialogContentProvider(impactElements));
            dialog.setInput(new Object());
            clear();
            return dialog.open();
        }

        return Window.CANCEL;
    }

    /**
     * DOC bzhou Comment method "showConfirmDialog".
     * 
     * @param parentShell
     * @param obj
     * @param dependencyElements
     * @param dialogMessage
     * @return
     */
    public static int showConfirmDialog(Shell parentShell, Object obj, ModelElement[] dependencyElements, String dialogTitle,
            String dialogMessage) {
        for (ModelElement element : dependencyElements) {
            ImpactNode node = new ImpactNode(element);
            if (!impactNodes.contains(node)) {
                node.addRequireModelElement(obj);
                impactNodes.add(node);
            }
        }

        ImpactNode[] impactElements = getImpactNodes();
        if (impactElements.length > 0) {
            TreeMessageInfoDialog dialog = new TreeMessageInfoDialog(parentShell, dialogTitle, null, dialogMessage,
                    MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
            dialog.setContentProvider(new DialogContentProvider(impactElements));
            dialog.setInput(new Object());
            clear();
            return dialog.open();
        }

        return Window.OK;
    }

    /**
     * DOC rli Comment method "showDialog".
     * 
     * @param parentShell
     * @param modelElements
     * @param dialogMessage
     * @return
     */
    public static int showDialog(Shell parentShell, ModelElement[] modelElements, String dialogMessage) {
        addDenpendencyElements(modelElements);
        ImpactNode[] impactElements = getImpactNodes();
        if (impactElements.length > 0) {
            TreeMessageInfoDialog dialog = new TreeMessageInfoDialog(parentShell,
                    DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmResourceDelete"), null, dialogMessage, //$NON-NLS-1$
                    MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL }, 1);
            dialog.setContentProvider(new DialogContentProvider(impactElements));
            dialog.setInput(new Object());
            clear();
            dialog.open();
            return Window.CANCEL;
        } else {
            return popConfirmDialog(modelElements);
        }
    }

    /**
     * 
     * MOD mzhao 2009-03-26, Impact existing analyses when copy remote structure from remote to local on comparison
     * editor.
     * 
     * @param parentShell
     * @param modelElements
     * @param dialogMessage
     * @return
     */
    public static int showElementImpactDialog(Shell parentShell, ModelElement[] modelElements, String dialogTitle,
            String dialogMessage) {
        addDenpendencyElements(modelElements);
        ImpactNode[] impactElements = getImpactNodes();
        if (impactElements.length > 0) {
            TreeMessageInfoDialog dialog = new TreeMessageInfoDialog(parentShell, dialogTitle, null, dialogMessage,
                    MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL }, 0);
            dialog.setContentProvider(new DialogContentProvider(impactElements));
            dialog.setInput(new Object());
            clear();
            int result = dialog.open();
            return result;
        } else {
            return Window.OK;
        }
    }

    public static int showElementImpactConfirmDialog(Shell parentShell, ModelElement[] modelElements, String dialogTitle,
            String dialogMessage) {
        addDenpendencyElements(modelElements);
        ImpactNode[] impactElements = getImpactNodes();
        if (impactElements.length > 0) {
            TreeMessageInfoDialog dialog = new TreeMessageInfoDialog(parentShell, dialogTitle, null, dialogMessage,
                    MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
            dialog.setContentProvider(new DialogContentProvider(impactElements));
            dialog.setInput(new Object());
            clear();
            int result = dialog.open();
            return result;
        } else {
            return Window.OK;
        }
    }

    private static int popConfirmDialog(ModelElement[] modelElements) {
        MessageDialog messageDialog;
        if (modelElements.length > 1) {
            messageDialog = new MessageDialog(
                    null,
                    DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmResourceDelete"), null, //$NON-NLS-1$
                    DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.deleleTheseResources"), MessageDialog.WARNING, new String[] { //$NON-NLS-1$
                    IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);

        } else {
            messageDialog = new MessageDialog(
                    null,
                    DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmResourcesDelete"), null, DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.areYouDelele", modelElements[0].getName()) //$NON-NLS-1$ //$NON-NLS-2$ 
                    , MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
        }
        return messageDialog.open();
    }

    /**
     * show all nodes with its dependencies (in nodeWithDependsMap) in one dialog. if the dependency has its own
     * depends, also show them under this dependsency
     * 
     * @param nodeWithDependsMap key is the repostiory node and value is the dependencies of this node.
     * @param dialogMessage the message shown in dialog
     * @param needCheckBox show the check box or not
     * @return true if the user choose force delete, false if the user did not choose force delete
     */
    public static boolean showDialog(Map<IRepositoryNode, List<ModelElement>> nodeWithDependsMap, String dialogMessage,
            boolean needCheckBox) {
        // for each node in the map, add the node as a ImpactNode, and its children are its depends.
        Iterator iter = nodeWithDependsMap.entrySet().iterator();
        List<ModelElement> lockedByOthersList = new ArrayList<ModelElement>();
        while (iter.hasNext()) {
            Map.Entry<IRepositoryNode, List<ModelElement>> entry = (Map.Entry<IRepositoryNode, List<ModelElement>>) iter.next();
            IRepositoryNode node = entry.getKey();
            List<ModelElement> dependencies = entry.getValue();
            addDenpendencyElements(node, dependencies);
            findLockedByOthersModelElement(lockedByOthersList, dependencies);
        }
        ImpactNode[] impactElements = getImpactNodes();
        boolean isChecked = false;

        // show the dialog
        if (impactElements.length > 0) {
            boolean lockedByOthersFlag = !lockedByOthersList.isEmpty();

            String message = lockedByOthersFlag ? DefaultMessagesImpl.getString("DQDeleteAction.lockedByOthers") : dialogMessage; //$NON-NLS-1$
            String[] buttonLabels = lockedByOthersFlag ? new String[] {} : new String[] { IDialogConstants.OK_LABEL };

            TreeMessageInfoDialog dialog = new TreeMessageInfoDialog(null,
                    DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmResourceDelete"), null, message, //$NON-NLS-1$
                    MessageDialog.WARNING, buttonLabels, 1);
            dialog.setNeedCheckbox(lockedByOthersFlag ? false : needCheckBox);
            dialog.setContentProvider(new DialogContentProvider(impactElements));
            dialog.setInput(new Object());
            clear();
            // MOD qiongli 2012-1-6 if don't click OK button,should return false;
            int result = dialog.open();
            isChecked = dialog.isChecked() && (result == Window.OK);
        }

        return isChecked;
    }

    /**
     * it the ModelElement is locked by others, add it into lockedByOthers.
     * 
     * @param lockedByOthers
     * @param dependencies
     */
    private static void findLockedByOthersModelElement(List<ModelElement> lockedByOthers, List<ModelElement> dependencies) {
        for (ModelElement me : dependencies) {
            if (lockedByOthers(me)) {
                lockedByOthers.add(me);
            }

            // TDQ-8129 msjian : find from sub-level's also(for analysis, can get dependency client report, so we need to check
            // report's lock status)
            List<ModelElement> dependencyList = EObjectHelper.getDependencyClients(me);
            findLockedByOthersModelElement(lockedByOthers, dependencyList);
            // TDQ-8129 ~
        }
    }

    /**
     * if the ModelElement locked by others return true, else return false.
     * 
     * @param me ModelElement
     * @return
     */
    private static boolean lockedByOthers(ModelElement me) {
        boolean result = false;
        Property property = PropertyHelper.getProperty(me);
        if (property != null) {
            result = ProxyRepositoryManager.getInstance().isLockByOthers(property.getItem());
        }
        return result;
    }

    /**
     * The first level node is (connection, DQ rule, Pattern), the second level is analysis, Jrxml, the third is report
     * only for analysis , need to check if there are some reports depends on this analysis and list them if any.
     * 
     * @param node
     * @param dependencies
     */
    private static void addDenpendencyElements(IRepositoryNode node, List<ModelElement> dependencies) {
        ImpactNode impactNode;
        // if node is a jrxml
        if (ERepositoryObjectType.TDQ_JRAXML_ELEMENT.equals(node.getObject().getRepositoryObjectType())) {
            impactNode = new ImpactNode(node);
        } else {
            impactNode = new ImpactNode(RepositoryNodeHelper.getModelElementFromRepositoryNode(node));
        }

        for (ModelElement element : dependencies) {

            // only for analysis
            if (element instanceof Analysis) {
                List<ModelElement> dependReports = EObjectHelper.getDependencyClients(element);
                if (dependReports.size() > 0) {
                    ImpactNode anaNode = new ImpactNode(element);
                    for (ModelElement report : dependReports) {
                        anaNode.addRequireModelElement(report);
                    }
                    // impactNodes.add(anaNode);
                    impactNode.addRequireModelElement(anaNode);
                } else {
                    impactNode.addRequireModelElement(element);
                }
            } else {
                impactNode.addRequireModelElement(element);
            }
        }
        impactNodes.add(impactNode);
    }
}
