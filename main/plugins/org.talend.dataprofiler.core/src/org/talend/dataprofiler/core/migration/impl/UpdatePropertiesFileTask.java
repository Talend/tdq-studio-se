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
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.properties.Property;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.ui.imex.model.FileSystemImportWriter;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.dq.writer.AElementPersistance;
import org.talend.dq.writer.impl.ElementWriterFactory;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UpdatePropertiesFileTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(FileSystemImportWriter.class);

    private FilenameFilter nonPropertyFileFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            IPath namePath = new Path(name);
            String fileExtension = namePath.getFileExtension();
            return fileExtension != null && !name.startsWith(".") && !name.endsWith(FactoriesUtil.PROPERTIES_EXTENSION) //$NON-NLS-1$
                    && FactoriesUtil.isEmfFile(fileExtension);
        }
    };

    private List<File> fileList;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask#valid()
     */
    @Override
    public boolean valid() {
        fileList = new ArrayList<File>();

        for (File folder : getOldTopFolderList()) {
            FilesUtils.getAllFilesFromFolder(folder, fileList, nonPropertyFileFilter);
        }
        return !fileList.isEmpty() && super.valid();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean ok = true;
        EMFUtil emfUtil = new EMFUtil();

        for (File file : fileList) {

            if (file.isFile()) {
                URI uri = URI.createFileURI(file.getAbsolutePath());

                if (log.isDebugEnabled()) {
                    log.debug("---------Translate " + uri.toString()); //$NON-NLS-1$
                }

                EObject eObject = null;
                try {
                    Resource resource = emfUtil.getResourceSet().getResource(uri, true);

                    ResourceFileMap resourceFileMap = ModelElementFileFactory.getResourceFileMap(uri.fileExtension());

                    if (resourceFileMap != null) {
                        eObject = resourceFileMap.getModelElement(resource);
                    }
                } catch (Exception e) {
                    log.warn(DefaultMessagesImpl.getString("UpdatePropertiesFileTask_2", file.getAbsolutePath()), e); //$NON-NLS-1$
                    ok = false;
                }

                if (eObject != null) {
                    if (eObject instanceof ModelElement) {
                        try {
                            saveObject(emfUtil, file, uri, eObject);
                        } catch (Exception e) {
                            log.error(DefaultMessagesImpl.getString("UpdatePropertiesFileTask_3", eObject, file, uri), e); //$NON-NLS-1$
                            ok = false;
                        }
                    } else {
                        log.warn(DefaultMessagesImpl.getString("UpdatePropertiesFileTask_4", eObject.toString())); //$NON-NLS-1$
                        ok = false;
                    }
                }
            }
        }

        emfUtil = null;

        return ok;
    }

    private void saveObject(EMFUtil emfUtil, File file, URI uri, EObject eObject) {
        ModelElement modelElement = (ModelElement) eObject;

        AElementPersistance writer = ElementWriterFactory.getInstance().create(modelElement);

        Property oldPropery = PropertyHelper.getProperty(modelElement);
        if (writer != null) {
            Property property = writer.initProperty(modelElement);

            if (oldPropery != null) {
                property.setId(oldPropery.getId());
                property.setAuthor(oldPropery.getAuthor());
                property.getItem().setState(oldPropery.getItem().getState());
            }

            String statePathStr = PropertyHelper.computePath(property, file);
            property.getItem().getState().setPath(statePathStr);

            URI propURI = uri.trimFileExtension().appendFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

            Resource propResource = emfUtil.getResourceSet().createResource(propURI);
            propResource.getContents().add(property);
            propResource.getContents().add(property.getItem());
            propResource.getContents().add(property.getItem().getState());

            EMFUtil.saveResource(propResource);

        } else {
            log.warn(DefaultMessagesImpl.getString("UpdatePropertiesFileTask_5", modelElement.getName())); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        // MOD xqliu 2010-09-15 bug 13941 this task should be called before ExchangeFileNameToReferenceTask
        // return createDate(2010, 8, 17);
        return createDate(2010, 8, 15);
    }

    // /**
    // * DOC xqliu Comment method "file2IFile".
    // *
    // * @param file
    // * @return
    // */
    // private IFile file2IFile(File file) {
    // String filePath = file.getAbsolutePath();
    // String projectPath = getRootProjectFile().getAbsolutePath();
    // filePath = filePath.substring(projectPath.length());
    // return ReponsitoryContextBridge.getRootProject().getFile(filePath);
    // }
    //
    // /**
    // * DOC xqliu Comment method "getRootProjectFile".
    // *
    // * @return
    // */
    // private File getRootProjectFile() {
    // return ResourceManager.getRootProject().getLocation().toFile();
    // }
}
