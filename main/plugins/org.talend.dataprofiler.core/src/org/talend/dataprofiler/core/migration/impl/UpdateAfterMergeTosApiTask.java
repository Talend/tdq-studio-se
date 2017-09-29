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
package org.talend.dataprofiler.core.migration.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.writer.AElementPersistance;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.EResourceConstant;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * update TDQ_Data Profiling and TDQ_Libraries folders after use same api with TOS(metadata folder has been updated
 * before).
 */
public class UpdateAfterMergeTosApiTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(UpdateAfterMergeTosApiTask.class);

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2011, 1, 20);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {

        ArrayList<File> fileList = getNeedUpdateFiles();

        for (File file : fileList) {

            createNewItemFile(file);

            deleteOldItemFile(file);
        }

        updateConnectionFile();

        return true;
    }

    /**
     * DOC bZhou Comment method "addConnectionFileToUpdate".
     */
    private void updateConnectionFile() {
        File rawDir = getWorkspacePath().append(EResourceConstant.METADATA.getPath()).toFile();
        ArrayList<File> fileList = new ArrayList<File>();

        if (rawDir.exists()) {
            getAllFilesFromFolder(rawDir, fileList, new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(FactoriesUtil.ITEM_EXTENSION);
                }
            });
        }

        for (File file : fileList) {
            IFile iFile = WorkspaceUtils.fileToIFile(file);

            Property property = PropertyHelper.getProperty(iFile);
            if (property != null) {
                Item item = property.getItem();
                EResourceConstant type = EResourceConstant.getTypedConstant(item);
                if (type == EResourceConstant.DB_CONNECTIONS) {
                    DatabaseConnectionItem dbItem = (DatabaseConnectionItem) item;
                    DatabaseConnection connection = (DatabaseConnection) dbItem.getConnection();

                    String otherParameter = ConnectionHelper.getOtherParameter(connection);
                    if (!StringUtils.isBlank(otherParameter)) {
                        connection.setUiSchema(otherParameter);
                        EMFSharedResources.getInstance().saveResource(connection.eResource());
                    }
                }
            }
        }

    }

    /**
     * DOC bZhou Comment method "getNeedUpdateFiles".
     * 
     * @return
     */
    private ArrayList<File> getNeedUpdateFiles() {
        File dapRawFile = getWorkspacePath().append(EResourceConstant.DATA_PROFILING.getPath()).toFile();
        File libRawFile = getWorkspacePath().append(EResourceConstant.LIBRARIES.getPath()).toFile();

        ArrayList<File> fileList = findRawFiles(dapRawFile);
        fileList.addAll(findRawFiles(libRawFile));
        return fileList;
    }

    /**
     * DOC bZhou Comment method "createNewItemFile".
     * 
     * @param file
     */
    private void createNewItemFile(File file) {
        ModelElement modelElement = getModelElement(file);

        if (modelElement != null) {
            boolean needUpdateFlag = !modelElement.eIsProxy();
            // log.info("needUpdateFlag======>" + needUpdateFlag);
            // log.info("modelElement======>" + modelElement);
            if (needUpdateFlag) {
                IFile iFile = WorkspaceUtils.fileToIFile(file);
                IFolder parentFolder = (IFolder) iFile.getParent();

                Resource oldRes = modelElement.eResource();

                List<Resource> needSaves = getReferenceResources(oldRes, modelElement);

                AElementPersistance writer = ElementWriterFactory.getInstance().create(modelElement);
                writer.create(modelElement, parentFolder, true);

                EMFUtil.changeUri(oldRes, EObjectHelper.getURI(modelElement));

                for (Resource toSave : needSaves) {
                    EMFSharedResources.getInstance().saveResource(toSave);
                }
            }
        }

    }

    /**
     * DOC bZhou Comment method "getReferenceResources".
     * 
     * @param res
     * @param modelElement
     * @return
     */
    public List<Resource> getReferenceResources(Resource res, ModelElement modelElement) {
        EcoreUtil.resolveAll(res);

        Map<EObject, Collection<Setting>> find = EcoreUtil.ExternalCrossReferencer.find(res);
        List<Resource> needSaves = new ArrayList<Resource>();
        for (EObject object : find.keySet()) {
            Resource resource = object.eResource();
            if (resource == null) {
                continue;
            }
            EcoreUtil.resolveAll(resource);
            needSaves.add(resource);
        }

        if (res.getURI().toPlatformString(false).contains(EResourceConstant.LIBRARIES.getPath())) {
            final List<? extends ModelElement> allElement = AnaResourceFileHelper.getInstance().getAllElement();
            for (ModelElement element : allElement) {
                Map<EObject, Collection<Setting>> allFind = EcoreUtil.ExternalCrossReferencer.find(element.eResource());
                if (allFind.keySet().contains(modelElement)) {
                    needSaves.add(element.eResource());
                }
            }
        }

        needSaves.add(res);

        return needSaves;
    }

    /**
     * DOC bZhou Comment method "deleteOldFile".
     * 
     * @param file
     * @throws CoreException
     */
    private void deleteOldItemFile(File file) throws CoreException {
        IFile itemFile = WorkspaceUtils.fileToIFile(file);
        IFile propFile = PropertyHelper.getPropertyFile(itemFile);

        Property property = PropertyHelper.getProperty(propFile);

        Item currentItem = property.getItem();
        if (currentItem.getParent() instanceof FolderItem) {
            ((FolderItem) currentItem.getParent()).getChildren().remove(currentItem);
        }

        itemFile.delete(true, null);
        propFile.delete(true, null);
    }

    /**
     * get the ModelElement from the file.
     * 
     * @param parentFile
     * @return
     */
    private ModelElement getModelElement(File parentFile) {
        ModelElement result = null;
        try {
            IFile iFile = WorkspaceUtils.fileToIFile(parentFile);
            // log.info("iFile============>" + iFile.toString());
            result = ModelElementFileFactory.getModelElement(iFile);
            // log.info("result============>" + result.toString());
        } catch (NullPointerException e) {
            log.info(e, e);
        } catch (Exception e) {
            log.error(e, e);
        }
        return result;
    }

    private ArrayList<File> findRawFiles(File rawDir) {
        ArrayList<File> fileList = new ArrayList<File>();

        if (rawDir.exists()) {
            getAllFilesFromFolder(rawDir, fileList, new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (isNotSVNFolder(dir)) {
                        File file = new Path(dir.getAbsolutePath()).append(name).toFile();
                        if (file.isFile()) {
                            ModelElement element = getModelElement(file);
                            if (element != null) {
                                String newName = WorkspaceUtils.normalize(element.getName());
                                return !name.contains(newName) || !name.contains("0.1"); //$NON-NLS-1$
                            }
                        }
                    }
                    return false;
                }
            });
        }

        return fileList;
    }

    /**
     * DOC bZhou Comment method "isNotSVNFolder".
     * 
     * @param file
     * @return
     */
    private boolean isNotSVNFolder(File file) {
        return !file.getAbsolutePath().contains("svn"); //$NON-NLS-1$
    }
}
