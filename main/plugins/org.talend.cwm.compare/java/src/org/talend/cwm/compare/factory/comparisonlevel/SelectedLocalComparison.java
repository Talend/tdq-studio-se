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

package org.talend.cwm.compare.factory.comparisonlevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.compare.DQStructureComparer;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.IComparisonLevel;
import org.talend.cwm.compare.factory.IUIHandler;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * 
 * DOC mzhao class global comment. Compare two selected element in local structure.
 */
public class SelectedLocalComparison implements IComparisonLevel {

    private static final int LEFT_RESOURCE = 0;

    private static final int RIGHT_RESOURCE = 1;

    private Object firstSelectedObj = null, secondSelectedObj = null;

    private Connection firstSelectedDataProvider;

    private Connection secondSelectedDataProvider;

    private Connection tempFirstSelectedDataProvider;

    private Connection tempSecondSelectedDataProvider;

    private Map<String, Object> options;

    public SelectedLocalComparison(Object firstSelectedObj, Object secondSelectedObj) {
        this.firstSelectedObj = firstSelectedObj;
        this.secondSelectedObj = secondSelectedObj;
        options = new HashMap<String, Object>();
        // options.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);
    }

    @Override
    public void popComparisonUI(IUIHandler uiHandler) throws ReloadCompareException {

        // Judge selected elements types.
        ModelElementAdapter meAdapter = new ModelElementAdapter();
        firstSelectedDataProvider = meAdapter.getAdaptableProvider(firstSelectedObj);
        if (firstSelectedDataProvider != null && firstSelectedDataProvider.eIsProxy()) {
            firstSelectedDataProvider = (DatabaseConnection) EObjectHelper.resolveObject(firstSelectedDataProvider);
        }
        secondSelectedDataProvider = meAdapter.getAdaptableProvider(secondSelectedObj);
        if (secondSelectedDataProvider != null && secondSelectedDataProvider.eIsProxy()) {
            secondSelectedDataProvider = (DatabaseConnection) EObjectHelper.resolveObject(secondSelectedDataProvider);
        }
        if (firstSelectedDataProvider == null || secondSelectedDataProvider == null) {
            return;
        }

        DQStructureComparer.deleteFirstResourceFile();
        DQStructureComparer.deleteSecondResourceFile();

        createTempConnectionFile();
        // createCopyedProvider();
        // MOD mzhao 2009-03-09 Set default dbname is first. (When compare local
        // with distant structure, dbname need to
        // displayed at left panel of compare editor,have not handled case when
        // compared models both from local
        // structure)
        // DQStructureComparer.openDiffCompareEditor(getResource(LEFT_RESOURCE), getResource(RIGHT_RESOURCE), options,
        // uiHandler,
        // DQStructureComparer.getLocalDiffResourceFile(), firstSelectedDataProvider.getName(), firstSelectedObj, true);

    }

    @SuppressWarnings("deprecation")
    protected void createTempConnectionFile() throws ReloadCompareException {
        // First resource.
        IFile selectedFile1 = PrvResourceFileHelper.getInstance().findCorrespondingFile(firstSelectedDataProvider);
        if (selectedFile1 == null) {
            selectedFile1 = ResourceManager.getRoot().getFile(
                    new Path(firstSelectedDataProvider.eResource().getURI().toPlatformString(false)));
        }
        IFile firstConnectionFile = DQStructureComparer.getFirstComparisonLocalFile();
        IFile copyedFile1 = DQStructureComparer.copyedToDestinationFile(selectedFile1, firstConnectionFile);
        TypedReturnCode<Connection> returnProvider = DqRepositoryViewService.readFromFile(copyedFile1);
        if (!returnProvider.isOk()) {
            throw new ReloadCompareException(returnProvider.getMessage());
        }
        tempFirstSelectedDataProvider = returnProvider.getObject();

        // Second resource.
        IFile selectedFile2 = PrvResourceFileHelper.getInstance().findCorrespondingFile(secondSelectedDataProvider);
        if (selectedFile2 == null) {
            selectedFile2 = ResourceManager.getRoot().getFile(
                    new Path(secondSelectedDataProvider.eResource().getURI().toPlatformString(false)));
        }
        IFile secondConnectionFile = DQStructureComparer.getSecondComparisonLocalFile();
        IFile copyedFile2 = DQStructureComparer.copyedToDestinationFile(selectedFile2, secondConnectionFile);
        TypedReturnCode<Connection> returnProvider2 = DqRepositoryViewService.readFromFile(copyedFile2);
        if (!returnProvider2.isOk()) {
            throw new ReloadCompareException(returnProvider2.getMessage());
        }
        tempSecondSelectedDataProvider = returnProvider2.getObject();
    }

    private Resource getResource(int pos) throws ReloadCompareException {
        Connection tdProvider = null;
        Object selectedObj = null;
        switch (pos) {
        case LEFT_RESOURCE:
            selectedObj = firstSelectedObj;
            tdProvider = tempFirstSelectedDataProvider;
            break;
        case RIGHT_RESOURCE:
            selectedObj = secondSelectedObj;
            tdProvider = tempSecondSelectedDataProvider;
            break;
        default:
            break;
        }

        ModelElementAdapter meAdapter = new ModelElementAdapter();

        Object rootElement = meAdapter.getListModelElements(selectedObj, tdProvider);
        Resource leftResource = null;
        if (rootElement instanceof Resource) {
            leftResource = (Resource) rootElement;
        } else {
            // leftResource = tdProvider.eResource();
            // leftResource.getContents().clear();
            leftResource = ((ModelElement) rootElement).eResource();
            leftResource.getContents().clear();
            leftResource.getContents().add((ModelElement) rootElement);
        }
        EMFSharedResources.getInstance().saveResource(leftResource);
        return leftResource;
    }

    /**
     * DOC mzhao Interface that do instanceof converter to provider common object to client.
     * 
     * FIXME the class should be made static.
     */
    private class ModelElementAdapter {

        public Connection getAdaptableProvider(Object element) {
            Connection adaptedDataProvider = null;

            if (element instanceof IFile) {
                // IFile
                adaptedDataProvider = PrvResourceFileHelper.getInstance().findProvider((IFile) element);
            } else if (element instanceof IRepositoryViewObject) {
                Item item = ((IRepositoryViewObject) element).getProperty().getItem();
                if (item instanceof ConnectionItem) {
                    adaptedDataProvider = ((ConnectionItem) item).getConnection();
                }
            } else if (element instanceof Connection) {
                adaptedDataProvider = ConnectionUtils.fillConnectionMetadataInformation((Connection) element);
            } else {

                Package package1 = SwitchHelpers.PACKAGE_SWITCH.doSwitch((ModelElement) element);

                if (package1 != null) {
                    adaptedDataProvider = ConnectionHelper.getTdDataProvider(package1);
                } else {
                    ColumnSet columnSet1 = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch((ModelElement) element);
                    if (columnSet1 != null) {
                        adaptedDataProvider = ConnectionHelper.getDataProvider(columnSet1);
                    } else {
                        TdColumn column1 = SwitchHelpers.COLUMN_SWITCH.doSwitch((TdColumn) element);
                        if (column1 != null) {
                            adaptedDataProvider = ConnectionHelper.getTdDataProvider(column1);
                        }
                    }

                }
            }
            return adaptedDataProvider;
        }

        public Object getListModelElements(Object element, Connection tdProvider) throws ReloadCompareException {

            Object rootElement = null;
            // List<ModelElement> meList = new ArrayList<ModelElement>();

            if (element instanceof IFile) {
                rootElement = tdProvider.eResource();
            } else if (element instanceof IRepositoryViewObject) {
                rootElement = tdProvider.eResource();
            } else if (element instanceof Connection) {
                Resource eResource = tdProvider.eResource();
                EList<Package> contents = ((Connection) element).getDataPackage();// eResource().getContents();
                eResource.getContents().clear();
                List<EObject> objects = new ArrayList<EObject>();
                for (EObject object : contents) {
                    if (!(object instanceof Connection || object instanceof Component)) {
                        objects.add(object);
                    }
                }
                eResource.getContents().addAll(objects);
                rootElement = eResource;

            } else {
                Package package1 = SwitchHelpers.PACKAGE_SWITCH.doSwitch((ModelElement) element);

                if (package1 != null) {
                    Package findMatchPackage = DQStructureComparer.findMatchedPackage((Package) element, tdProvider);
                    findMatchPackage.getDataManager().clear();
                    rootElement = findMatchPackage;
                } else {
                    ColumnSet columnSet1 = SwitchHelpers.COLUMN_SET_SWITCH.doSwitch((ModelElement) element);
                    if (columnSet1 != null) {
                        ColumnSet findMatchedColumnSet = DQStructureComparer.findMatchedColumnSet(columnSet1, tdProvider);
                        rootElement = findMatchedColumnSet;
                    } else {
                        TdColumn column1 = SwitchHelpers.COLUMN_SWITCH.doSwitch((TdColumn) element);
                        if (column1 != null) {
                            TdColumn findMathedColumn = DQStructureComparer.findMatchedColumn(column1, tdProvider);
                            rootElement = findMathedColumn;

                            ((TdColumn) rootElement).getTaggedValue().clear();
                            // ~MOD 2009-04-21 Clear primary key as well. If
                            // not clear, it
                            // will cause exception: not contained in
                            // a resource
                            ((TdColumn) rootElement).getUniqueKey().clear();

                            // ~MOD 2009-04-21 Clear foreign key.
                            ((TdColumn) rootElement).getKeyRelationship().clear();
                        }
                    }

                }
            }

            return rootElement;

        }
    }

    @Override
    public Connection reloadCurrentLevelElement() throws ReloadCompareException {
        // FIXME implement this method
        return null;
    }
}
