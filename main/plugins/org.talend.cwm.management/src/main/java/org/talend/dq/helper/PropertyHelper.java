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
package org.talend.dq.helper;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EmfFileResourceUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.emf.FactoriesUtil.EElementEName;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class PropertyHelper {

    private static Logger log = Logger.getLogger(PropertyHelper.class);

    private PropertyHelper() {

    }

    /**
     * DOC bZhou Comment method "getPropertyFile".
     *
     * @param elementFile
     * @return null if can't find.
     */
    public static IFile getPropertyFile(IFile elementFile) {
        if (!FactoriesUtil.PROPERTIES_EXTENSION.equals(elementFile.getFileExtension())) {
            IPath path = elementFile.getFullPath();

            path = path.removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

            return ResourceManager.getRoot().getFile(path);
        }

        return elementFile;
    }

    /**
     * get the companion property of an item file
     * 
     * @param itemFile
     * @return
     */
    public static Property getCompanionProperty(File itemFile) {
        File propertyFile = new Path(itemFile.getAbsolutePath()).removeFileExtension()
                .addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION).toFile();
        return getProperty(propertyFile);
    }

    /**
     * DOC bZhou Comment method "getPropertyFile".
     *
     * @param modelElement
     * @return null if can't find.
     */
    public static IFile getPropertyFile(ModelElement modelElement) {

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        String platformString = modelElement.eResource().getURI().toPlatformString(true);
        IPath propPath = new Path(platformString).removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

        return root.getFile(propPath);
    }

    /**
     * DOC klliu Comment method "getPropertyFile".
     *
     * @param elementResource
     * @return null if can't find.
     */
    public static IFile getPropertyFile(Resource elementResource) {
        assert elementResource != null;
        // MOD Beacuse of updating Model,then we use the same repository with tos .So wo can't cast the Resource to
        // ModelEelent klliu 2010-09-07
        if (elementResource.getURI().isPlatform()) {
            String pString = elementResource.getURI().toPlatformString(false);
            IPath ePath = new Path(pString);
            ePath = ePath.removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

            return ResourceManager.getRoot().getFile(ePath);
        }

        return null;
    }
/**
 * getItemFile from the resource of property
 */
    public static IFile getModelElementFile(Resource propertyResource) {
        assert propertyResource != null;
        if (propertyResource.getURI().isPlatform()) {
            String pString = propertyResource.getURI().toPlatformString(false);
            IPath ePath = new Path(pString);
            ePath = ePath.removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

            return ResourceManager.getRoot().getFile(ePath);
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "getProperty".
     *
     * @param file
     * @return Null if can't find.
     */
    public static Property getProperty(IFile file) {
        return getProperty(file, false);
    }
    
    public static Property getProperty(IFile file, boolean reload) {
        if (file != null && (file.exists() || file.getLocation() != null && file.getLocation().toFile().exists())) {
            if (StringUtils.equalsIgnoreCase(file.getFileExtension(), FactoriesUtil.PROPERTIES_EXTENSION)) {
                URI propURI = URI.createPlatformResourceURI(file.getFullPath().toOSString(), false);
                Resource resource = reload ? EMFSharedResources.getInstance().reloadResource(propURI) : EMFSharedResources.getInstance().getResource(propURI, true);
                if (resource != null) {
                    EList<EObject> contents = resource.getContents();
                    if (contents != null) {
                        Object object = EcoreUtil.getObjectByType(contents, PropertiesPackage.eINSTANCE.getProperty());
                        if (object != null) {
                            return (Property) object;
                        }
                    }
                }
            } else {
                // try to get property from element file.
                IFile propertyFile = getPropertyFile(file);
                return getProperty(propertyFile);
            }

        }
        return null;
    }

    /**
     * DOC bZhou Comment method "getProperty".
     *
     * @param propertyFile
     * @return
     */
    public static Property getProperty(File propertyFile) {
        // MOD qiongli 2012-2-2 TDQ-4431,move the detail code to method 'getProperty(File propertyFile,boolean
        // useRelativePath)'.
        return getProperty(propertyFile, false);
    }

    /**
     *
     * Get property by property file resource.
     *
     * @param propertyFile
     * @param useRelativePath,if true,get EMF resource by relative path for URI.
     * @return
     */
    public static Property getProperty(File propertyFile, boolean useRelativePath) {
        if (propertyFile == null) {
            return null;
        }
        Property property = null;
        if (useRelativePath) {
            IFile iFile = WorkspaceUtils.fileToIFile(propertyFile);
            property = getProperty(iFile);
        } else {
            if (propertyFile.exists()) {
                if (propertyFile.getName().endsWith(FactoriesUtil.PROPERTIES_EXTENSION)) {
                    Resource resource = null;
                    if (Platform.isRunning()) {
                        URI propURI = URI.createFileURI(propertyFile.getAbsolutePath());
                        resource = new ResourceSetImpl().getResource(propURI, true);
                    } else {
                        resource = EmfFileResourceUtil.getInstance().getFileResource(propertyFile.getAbsolutePath());
                    }
                    if (resource.getContents() != null) {
                        Object object = EcoreUtil.getObjectByType(resource.getContents(),
                                PropertiesPackage.eINSTANCE.getProperty());
                        if (object != null) {
                            property = (Property) object;
                        }
                    }
                }
            }
        }

        return property;
    }

    /**
     * DOC bZhou Comment method "getProperty".
     *
     * @param element
     * @return property or null
     */
    public static Property getProperty(ModelElement element) {
        URI uri = element.eResource() == null ? null : element.eResource().getURI();
        if (uri != null) {
            if (uri.isPlatform()) {
                IFile propertyFile = PropertyHelper.getPropertyFile(element);
                return getProperty(propertyFile);
            } else {
                File file = new Path(uri.toFileString()).removeFileExtension()
                        .addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION).toFile();
                return getProperty(file);
            }
        }
        return null;
    }

    /**
     * DOC bZhou Comment method "createFolderItemProperty".
     *
     * @return
     */
    public static Property createFolderItemProperty() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();

        FolderItem item = PropertiesFactory.eINSTANCE.createFolderItem();
        item.setType(FolderType.FOLDER_LITERAL);

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        item.setState(itemState);

        property.setId(EcoreUtil.generateUUID());
        property.setItem(item);
        item.setProperty(property);

        return property;
    }

    /**
     *
     * DOC bZhou Comment method "createFolderItemProperty".
     *
     * This mothod is to create a tdq item for TOP, this item is used to some element which is not have spacified item
     * definition.
     *
     * like *.sql files.
     *
     * @return
     */
    public static Property createTDQItemProperty() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setId(EcoreUtil.generateUUID());

        TDQItem item = PropertiesFactory.eINSTANCE.createTDQItem();
        item.setProperty(property);

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(false);
        item.setState(itemState);

        property.setItem(item);

        return property;
    }

    /**
     * DOC bZhou Comment method "getItemTypedPath".
     *
     * This method is to get the typed path for a specified item. Each typed item has a firm static path.
     *
     * e.g. Project/TypedPath/StatePath/xxxx.property
     *
     * @param item
     * @return
     */
    public static IPath getItemTypedPath(Property property) {
        return getItemTypedPath(property.getItem());
    }

    /**
     * DOC bZhou Comment method "getItemTypedPath".
     *
     * @param item
     * @return
     */
    public static IPath getItemTypedPath(Item item) {
        assert item != null;

        EResourceConstant rc = EResourceConstant.getTypedConstant(item);
        return rc != null ? new Path(rc.getPath()) : Path.EMPTY;
    }

    /**
     * DOC bZhou Comment method "getItemStatePath".
     *
     * This method is to get the relative path to the typed path of a item.
     *
     * e.g. Project/TypedPath/StatePath/xxxx.property
     *
     * @param property
     * @return
     */
    public static IPath getItemStatePath(Property property) {
        Item item = property.getItem();

        String statePathStr = null;
        if (item.getState() != null) {
            statePathStr = item.getState().getPath();
        } else {
            URI propURI = EObjectHelper.getURI(property);

            if (StringUtils.isBlank(statePathStr) && propURI.isPlatformResource()) {
                IPath propPath = new Path(propURI.toPlatformString(true)).removeLastSegments(1);
                IPath typedPath = ResourceManager.getRootProject().getFullPath().append(getItemTypedPath(property));
                return propPath.makeRelativeTo(typedPath);
            }
        }

        return statePathStr != null ? new Path(statePathStr) : Path.EMPTY;
    }

    /**
     * DOC bZhou Comment method "getItemPath".
     *
     * This method is to get the entire path of a item.
     *
     * e.g. getItemPath() = Project/TypedPath/StatePath/xxxx.property.
     *
     * @param property
     * @return
     */
    public static IPath getItemPath(Property property) {
        Item item = property.getItem();

        IPath path = null;
        String fileName = null;

        EElementEName elementEName = EElementEName.getElementEName(item);
        if (elementEName != null) {
            URI uri = EObjectHelper.getURI(property);
            if (uri.isFile()) {
                path = new Path(uri.toFileString());
            } else if (uri.isPlatform()) {
                path = new Path(uri.toPlatformString(false));
            } else {
                path = new Path(uri.lastSegment());
            }

            path = new Path(path.lastSegment());
            fileName = path.removeFileExtension().addFileExtension(elementEName.getFileExt()).toString();

        } else if (item instanceof TDQItem) {
            TDQItem dqItem = (TDQItem) item;

            if (!StringUtils.isBlank(dqItem.getFilename())) {
                fileName = dqItem.getFilename();
            }
        }

        if (fileName != null) {
            path = ResourceManager.getRootProject().getFullPath().append(getItemTypedPath(property))
                    .append(getItemStatePath(property)).append(fileName);
        }

        return path;
    }

    /**
     * DOC bZhou Comment method "getItemFile".
     *
     * @param property
     * @return
     */
    public static IFile getItemFile(Property property) {
        IPath itemPath = getItemPath(property);
        return ResourceManager.getRoot().getFile(itemPath);
    }

    /**
     * DOC bZhou Comment method "extractProjectLabel".
     *
     * This method is to extract the project technical label.
     *
     * @param property
     * @return
     */
    public static String extractProjectLabel(Property property) {
        User author = property.getAuthor();
        if (author != null && !author.eIsProxy()) {
            InternalEObject iAuthor = (InternalEObject) property.getAuthor();
            Resource projResource = iAuthor.eResource();
            if (projResource != null) {
                URI uri = projResource.getURI();
                String pathString = uri.isPlatform() ? uri.toPlatformString(false) : uri.toFileString();
                IPath projectPath = new Path(pathString);

                Object projOBJ = EObjectHelper.retrieveEObject(projectPath, PropertiesPackage.eINSTANCE.getProject());
                if (projOBJ != null) {
                    Project project = (Project) projOBJ;
                    return project.getTechnicalLabel();
                }
            }
        }

        return ReponsitoryContextBridge.getProjectName();
    }

    /**
     * DOC bZhou Comment method "computePath".
     *
     * @param property
     * @return
     */
    public static String computePath(Property property) {
        Resource eResource = property.eResource();
        if (eResource != null) {
            IPath propPath, typedPath;

            URI propURI = eResource.getURI();
            if (propURI.isPlatform()) {
                propPath = new Path(propURI.toPlatformString(true)).removeLastSegments(1);
                typedPath = ResourceManager.getRootProject().getFullPath().append(PropertyHelper.getItemTypedPath(property));

                IPath itemPath = propPath.makeRelativeTo(typedPath);

                return itemPath.toString();
            } else if (propURI.isFile()) {
                File file = new File(propURI.toFileString());
                return computePath(property, file);
            }
        }

        return "";
    }

    /**
     * DOC bZhou Comment method "computePath".
     *
     * @param property
     * @param file
     * @return
     */
    public static String computePath(Property property, File file) {
        IPath filePath = new Path(file.getAbsolutePath()).setDevice(null);

        boolean isConnFromOld = file.getAbsolutePath().contains(EResourceConstant.OLD_METADATA.getName());

        int flag = 0;
        EResourceConstant typedConstant = EResourceConstant.getTypedConstant(property.getItem());
        if (typedConstant != null && !isConnFromOld) {
            String typedName = typedConstant.getName();

            for (int i = 0; i < filePath.segmentCount(); i++) {
                String seg = filePath.segment(i);
                if (seg.equals(typedName)) {
                    flag = i + 1;
                    break;
                }
            }
        } else {
            List<EResourceConstant> typedConstantList = EResourceConstant.getTypedConstantList();
            typedConstantList.add(EResourceConstant.OLD_DB_CONNECTIONS);
            typedConstantList.add(EResourceConstant.OLD_MDM_CONNECTIONS);

            for (int i = 0; i < filePath.segmentCount() && flag == 0; i++) {
                String seg = filePath.segment(i);
                for (EResourceConstant constant : typedConstantList) {
                    if (seg.equals(constant.getName())) {
                        flag = i + 1;
                        break;
                    }
                }
            }
        }

        IPath statPath = filePath.removeFirstSegments(flag).removeLastSegments(1);

        return statPath.toString();
    }

    /**
     * DOC bZhou Comment method "getModelElement".
     *
     * Get model element from property recognized by current system.
     *
     * @param property
     * @return
     */
    public static ModelElement getModelElement(Property property) {
        Item item = property.getItem();

        assert item != null;

        ModelElement element = (ModelElement) new org.talend.core.model.properties.util.PropertiesSwitch() {

            @Override
            public Object caseDatabaseConnectionItem(DatabaseConnectionItem object) {
                return object.getConnection();
            }

            @Override
            public Object caseMDMConnectionItem(MDMConnectionItem object) {
                return object.getConnection();
            }

            @Override
            public Object caseDelimitedFileConnectionItem(DelimitedFileConnectionItem object) {
                return object.getConnection();
            }

        }.doSwitch(item);

        if (element == null) {
            element = new org.talend.dataquality.properties.util.PropertiesSwitch<ModelElement>() {

                @Override
                public ModelElement caseTDQReportItem(TDQReportItem object) {
                    return object.getReport();
                }

                @Override
                public ModelElement caseTDQAnalysisItem(TDQAnalysisItem object) {
                    return object.getAnalysis();
                }

                @Override
                public ModelElement caseTDQBusinessRuleItem(TDQBusinessRuleItem object) {
                    return object.getDqrule();
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.talend.dataquality.properties.util.PropertiesSwitch#caseTDQMatchRuleItem(org.talend.dataquality
                 * .properties.TDQMatchRuleItem)
                 */
                @Override
                public ModelElement caseTDQMatchRuleItem(TDQMatchRuleItem object) {
                    return object.getMatchRule();
                }

                @Override
                public ModelElement caseTDQIndicatorDefinitionItem(TDQIndicatorDefinitionItem object) {
                    return object.getIndicatorDefinition();
                }

                @Override
                public ModelElement caseTDQPatternItem(TDQPatternItem object) {
                    return object.getPattern();
                }

            }.doSwitch(item);
        }

        return element;
    }

    /**
     *
     * check if exist duplicate name.
     *
     * @param newName
     * @param oldName,it is just used to moidify Item,if it is null/empety,indicate that it is a new Item.
     * @param objectType
     * @param onlyCompDisplayName,if it is true,just compare the name of UI.
     * @return
     */
    public static boolean existDuplicateName(String newName, String oldName, ERepositoryObjectType objectType) {

        // if new name equals itself's old name ,return false
        if (newName == null || objectType == null || oldName != null && newName.equals(oldName)) {
            return false;
        }
        String normalizeName = WorkspaceUtils.normalize(newName);
        String normalizeName_old = WorkspaceUtils.normalize(oldName);
        if (normalizeName.equals(normalizeName_old)) {
            return false;
        }
        if (getDuplicateObject(newName, objectType) != null) {
            return true;
        }
        return false;
    }

    /**
     * return the duplicate object property.
     *
     * @param newName
     * @param objectType
     */
    public static Property getDuplicateObject(String newName, ERepositoryObjectType objectType) {
        Property prop = null;
        String normalizeName = WorkspaceUtils.normalize(newName);
        List<IRepositoryViewObject> existObjects;
        try {
            existObjects = ProxyRepositoryFactory.getInstance().getAll(objectType, true, false);

            if (existObjects != null) {
                for (IRepositoryViewObject object : existObjects) {
                    if (object == null || object.getProperty() == null) {
                        continue;
                    }
                    if (newName.equals(object.getProperty().getDisplayName())
                            || normalizeName.equals(object.getProperty().getLabel())) {
                        return object.getProperty();
                    }
                }
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }
        return prop;
    }

    /**
     *
     * @param property
     * @return get path of modelElement from property
     */
    public static String getModelElementPath(Property property) {
        if (property != null && property.getItem() != null) {
            EElementEName elementEName = FactoriesUtil.EElementEName.getElementEName(property.getItem());
            if (elementEName != null) {
                URI appendFileExtension = property.eResource().getURI().trimFileExtension()
                        .appendFileExtension(elementEName.getFileExt());
                if (appendFileExtension.isFile()) {
                    return appendFileExtension.toFileString();
                } else if (appendFileExtension.isPlatform()) {
                    return Platform.getLocation().append(appendFileExtension.toPlatformString(true)).toOSString();
                }
            }
        }
        return PluginConstant.EMPTY_STRING;

    }

    /**
     *
     * Comment method "changeName".
     *
     * @param property which one need to be changed.
     * @param newName the new value of name
     *
     * waitting a day to remove the name attribute from ModelElement. it let ue have to use service to do this simple
     * thing. If we need to do it on the TOS
     */
    public static void changeName(Property property, String newName) {
        property.setDisplayName(newName);
        property.setLabel(newName);
        ModelElement modelElement = PropertyHelper.getModelElement(property);
        modelElement.setName(newName);
    }
}
