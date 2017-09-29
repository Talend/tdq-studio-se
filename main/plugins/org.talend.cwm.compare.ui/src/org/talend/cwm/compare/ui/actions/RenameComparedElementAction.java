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

package org.talend.cwm.compare.ui.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.cwm.compare.DQStructureComparer;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.i18n.Messages;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.helper.FolderNodeHelper;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.dq.nodes.foldernode.IFolderNode;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Feature;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public class RenameComparedElementAction extends Action {

    private static Logger log = Logger.getLogger(RenameComparedElementAction.class);

    private ModelElement theSelectedElement = null;

    private Namespace originCompareElement = null;

    private IFolderNode selectedFolderNode = null;

    private Map<String, Object> options = null;

    private List<ModelElement> newAddedColumnSet = null;

    public RenameComparedElementAction(IFolderNode selectedFolderNode, ModelElement theSelectedElement,
            List<ModelElement> addElementList) {
        this.selectedFolderNode = selectedFolderNode;
        this.originCompareElement = (Namespace) selectedFolderNode.getParent();
        this.theSelectedElement = theSelectedElement;

        this.newAddedColumnSet = addElementList;
        options = new HashMap<String, Object>();
        // options.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);
    }

    @Override
    public String getText() {
        return Messages.getString("RenameComparedElementAction.ChooseCorrespondElement"); //$NON-NLS-1$
    }

    @Override
    public void run() {
        // Open the add element model dialog

        if (newAddedColumnSet == null || newAddedColumnSet.size() == 0) {
            MessageDialog.openConfirm(null, "", Messages.getString("RenameComparedElementAction.NoNewElement")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        RightPanelAddedElementsDialog addedEleDialog = new RightPanelAddedElementsDialog(null, newAddedColumnSet);
        if (addedEleDialog.open() != Window.OK) {
            return;
        }
        // Propagate the changes
        ModelElement checkedElement = addedEleDialog.getCheckedColumnSet();
        if (originCompareElement instanceof Package) {
            theSelectedElement.setNamespace(originCompareElement);
            checkedElement.setNamespace(originCompareElement);
        }
        if (originCompareElement instanceof Package) {
            originCompareElement.getOwnedElement().remove(theSelectedElement);
            originCompareElement.getOwnedElement().remove(checkedElement);
        }
        // Save to the copied resource.
        refreshReposigoryTree(checkedElement);
        refreshEditor();
    }

    public void refreshEditor() {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        activePage.closeEditor(activePage.getActiveEditor(), false);
        new PopComparisonUIAction(selectedFolderNode, Messages.getString("RenameComparedElementAction.Compare")).run(); //$NON-NLS-1$
    }

    private void refreshReposigoryTree(ModelElement checkedColumnSet) {
        // ~ Save to the original resource.

        // Remove theSelectedElement by iteratively comparing its name.
        // Because theSelectedElement in compare editor is not the same
        // instance with that of in repository tree.
        if (originCompareElement instanceof Package) {
            // ColumnSet
            for (Iterator<ModelElement> it = originCompareElement.getOwnedElement().iterator(); it.hasNext();) {
                if (it.next().getName().equalsIgnoreCase(theSelectedElement.getName())) {
                    it.remove();
                    break;
                }
            }
            originCompareElement.getOwnedElement().add(checkedColumnSet);
        } else {
            // Column
            for (Iterator<Feature> it = ((ColumnSet) originCompareElement).getFeature().iterator(); it.hasNext();) {
                if (it.next().getName().equalsIgnoreCase(theSelectedElement.getName())) {
                    it.remove();
                    break;
                }
            }
            ((ColumnSet) originCompareElement).getFeature().add((Feature) checkedColumnSet);
        }

        EMFSharedResources.getInstance().saveResource(originCompareElement.eResource());
        // ~
    }

    // private void getDiffElements(DiffElement diffEle, List<DiffElement> diffElementList) {
    // if (diffEle instanceof DiffGroup) {
    // for (DiffElement subDiffEle : ((DiffGroup) diffEle).getSubDiffElements()) {
    // getDiffElements(subDiffEle, diffElementList);
    // }
    // } else {
    // diffElementList.add(diffEle);
    // }
    // }

    private Resource getLeftResource() throws ReloadCompareException {
        ColumnSet selectedColumnSet = (ColumnSet) theSelectedElement;
        Connection copyedDataProvider = createCopyedProvider();
        ColumnSet findMatchedColumnSet = DQStructureComparer.findMatchedColumnSet(selectedColumnSet, copyedDataProvider);
        List<TdColumn> columnList = new ArrayList<TdColumn>();
        columnList.addAll(ColumnSetHelper.getColumns(findMatchedColumnSet));

        Resource leftResource = copyedDataProvider.eResource();

        leftResource.getContents().clear();
        for (TdColumn column : columnList) {
            DQStructureComparer.clearSubNode(column);
            leftResource.getContents().add(column);
        }
        EMFSharedResources.getInstance().saveResource(leftResource);
        return leftResource;
    }

    @SuppressWarnings("deprecation")
    private Connection createCopyedProvider() {
        Package catalogOrSchema = getTopLevelPackage();
        IFile selectedFile = PrvResourceFileHelper.getInstance().findCorrespondingFile(catalogOrSchema.getDataManager().get(0));
        IFile createNeedReloadElementsFile = DQStructureComparer.getNeedReloadElementsFile();
        IFile copyedFile = DQStructureComparer.copyedToDestinationFile(selectedFile, createNeedReloadElementsFile);
        TypedReturnCode<Connection> returnValue = DqRepositoryViewService.readFromFile(copyedFile);
        return returnValue.getObject();
    }

    private Package getTopLevelPackage() {
        Package catalogOrSchema = (Package) originCompareElement;
        if (originCompareElement instanceof Schema) {
            if (originCompareElement.eContainer() != null && originCompareElement.eContainer() instanceof Catalog) {
                catalogOrSchema = (Package) originCompareElement.eContainer();
            }
        }
        return catalogOrSchema;
    }

    private Resource getRightResource(ColumnSet selectedColumnSet) throws ReloadCompareException {
        Connection tempReloadProvider = createTempConnectionFile();
        Package matchedPackage = DQStructureComparer.findMatchedPackage((Package) originCompareElement, tempReloadProvider);
        IFolderNode columnSetFolderNode = FolderNodeHelper.getFolderNode(matchedPackage, selectedColumnSet);
        columnSetFolderNode.loadChildren();

        ColumnSet findMatchedColumnSet = DQStructureComparer.findMatchedColumnSet(selectedColumnSet, tempReloadProvider);
        List<TdColumn> columns = null;
        try {
            columns = DqRepositoryViewService.getColumns(tempReloadProvider, findMatchedColumnSet, null, true);
        } catch (Exception e1) {
            throw new ReloadCompareException(e1);
        }

        URI uri = tempReloadProvider.eResource().getURI();
        Resource rightResource = null;
        rightResource = EMFSharedResources.getInstance().getResource(uri, true);
        if (rightResource == null) {
            throw new ReloadCompareException("NoFactoryFoundForURI" + uri.toFileString()); //$NON-NLS-1$
        }
        rightResource.getContents().clear();
        for (TdColumn column : columns) {
            DQStructureComparer.clearSubNode(column);
            rightResource.getContents().add(column);
        }
        EMFSharedResources.getInstance().saveResource(rightResource);
        return rightResource;
    }

    private Connection createTempConnectionFile() throws ReloadCompareException {
        Package catalogOrSchema = getTopLevelPackage();
        Connection oldDataProvider = (Connection) catalogOrSchema.getDataManager().get(0);
        IFile tempConnectionFile = DQStructureComparer.getSecondComparisonLocalFile();
        // MOD mzhao ,Extract method getRefreshedDataProvider to class
        // DQStructureComparer for common use.
        TypedReturnCode<Connection> returnProvider = DQStructureComparer.getRefreshedDataProvider(oldDataProvider);
        if (!returnProvider.isOk()) {
            throw new ReloadCompareException(returnProvider.getMessage());
        }
        Connection tempReloadProvider = returnProvider.getObject();
        tempReloadProvider.setComponent(oldDataProvider.getComponent());
        ElementWriterFactory.getInstance().createDataProviderWriter().create(tempReloadProvider, tempConnectionFile);
        tempReloadProvider.setComponent(null);
        oldDataProvider.getComponent();
        return tempReloadProvider;
    }

    /**
     * 
     * DOC mzhao RenameComparedElementAction class global comment. Detailled comment
     * 
     * FIXME is it better to make it static?
     */
    private class RightPanelAddedElementsDialog extends Dialog {

        private CheckboxTableViewer tableViewer = null;

        private List<ModelElement> newAddedColumnSet;

        private ModelElement checkedElement = null;

        protected RightPanelAddedElementsDialog(Shell parentShell, List<ModelElement> newAddedColumnSet) {
            super(parentShell);
            this.newAddedColumnSet = newAddedColumnSet;
        }

        public ModelElement getCheckedColumnSet() {
            return checkedElement;
        }

        @Override
        protected boolean isResizable() {
            return true;
        }

        @Override
        protected void createButtonsForButtonBar(Composite parent) {
            // create OK and Cancel buttons by default
            Button okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
            okButton.setEnabled(false);
            createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        }

        /*
         * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets .Shell)
         */
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText(Messages.getString("RenameComparedElementAction.NewNameOfElement")); //$NON-NLS-1$
        }

        @Override
        protected Point getInitialSize() {
            return new Point(455, 340);
        }

        @Override
        protected void okPressed() {
            if (tableViewer.getCheckedElements().length > 0) {
                checkedElement = (ModelElement) tableViewer.getCheckedElements()[0];
            }

            super.okPressed();
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite) super.createDialogArea(parent);
            composite.setLayout(new FillLayout());
            tableViewer = CheckboxTableViewer.newCheckList(composite, SWT.SINGLE);
            tableViewer.setContentProvider(new IStructuredContentProvider() {

                @Override
                public Object[] getElements(Object inputElement) {
                    if (inputElement instanceof List<?>) {
                        return ((List<?>) inputElement).toArray();
                    }
                    return new Object[] { inputElement };
                }

                @Override
                public void dispose() {
                    // needn't to do anyting ???
                }

                @Override
                public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                    // needn't to do anyting ???
                }
            });

            tableViewer.setLabelProvider(new ILabelProvider() {

                @Override
                public Image getImage(Object element) {
                    return null;
                }

                @Override
                public String getText(Object element) {
                    ModelElement me = (ModelElement) element;
                    return me.getName();
                }

                @Override
                public void addListener(ILabelProviderListener listener) {
                    // needn't to do anyting ???
                }

                @Override
                public void dispose() {
                    // needn't to do anyting ???
                }

                @Override
                public boolean isLabelProperty(Object element, String property) {
                    return false;
                }

                @Override
                public void removeListener(ILabelProviderListener listener) {
                    // needn't to do anyting ???
                }
            });
            tableViewer.setInput(newAddedColumnSet);
            tableViewer.addCheckStateListener(new ICheckStateListener() {

                @Override
                public void checkStateChanged(CheckStateChangedEvent event) {
                    tableViewer.setAllChecked(false);
                    tableViewer.setChecked(event.getElement(), event.getChecked());
                    getButton(IDialogConstants.OK_ID).setEnabled(tableViewer.getCheckedElements().length > 0);
                }

            });
            return composite;
        }
    }
}
