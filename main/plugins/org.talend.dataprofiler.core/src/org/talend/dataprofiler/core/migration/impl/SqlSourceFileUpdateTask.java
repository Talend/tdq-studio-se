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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.helper.ByteArrayResource;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.properties.TDQFileItem;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.dq.helper.FileUtils;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC bZhou class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z bzhou $
 * 
 */
public class SqlSourceFileUpdateTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(SqlSourceFileUpdateTask.class);

    private static final String SQL_EXT = "sql"; //$NON-NLS-1$

    private static final String PROP_EXT = "properties"; //$NON-NLS-1$

    private static final String TDQ_SOURCE_FILE_PATH = "TDQ_Libraries/Source Files"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2011, 1, 1);
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
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        File rawFile = getWorkspacePath().append(TDQ_SOURCE_FILE_PATH).toFile();

        EMFUtil emfUtil = new EMFUtil();

        if (rawFile.exists() && rawFile.isDirectory()) {
            ArrayList<File> fileList = new ArrayList<File>();
            getAllFilesFromFolder(rawFile, fileList, new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith(SQL_EXT)) { 
                        return true;
                    }
                    return false;
                }
            });

            log.info("-----------Updating sql source file task ---------------"); //$NON-NLS-1$
            int counter = 0;
            for (File sqlFile : fileList) {
                log.info(counter + ". Updating: " + sqlFile.getAbsolutePath()); //$NON-NLS-1$
                String fileNamePart = FileUtils.getName(sqlFile) + "_" + VersionUtils.DEFAULT_VERSION; //$NON-NLS-1$

                IPath newSQLPath = new Path(sqlFile.getAbsolutePath()).removeLastSegments(1).append(fileNamePart)
                        .addFileExtension(SQL_EXT);

                Property property = createProperty(sqlFile);
                TDQFileItem item = (TDQFileItem) property.getItem();

                URI itemResourceURI = URI.createFileURI(newSQLPath.toString());
                Resource itemResource = new ByteArrayResource(itemResourceURI);
                itemResource.getContents().add(item.getContent());

                URI propertyResourceURI = itemResourceURI.trimFileExtension().appendFileExtension(PROP_EXT);
                Resource propertyResource = emfUtil.getResourceSet().createResource(propertyResourceURI);
                propertyResource.getContents().add(item.getProperty());
                propertyResource.getContents().add(item.getState());
                propertyResource.getContents().add(item);

                EMFUtil.saveResource(itemResource);
                EMFUtil.saveResource(propertyResource);

                sqlFile.delete();

                counter++;
            }

            emfUtil = null;
        }
        return true;
    }

    private Property createProperty(File initFile) throws Exception {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(""); //$NON-NLS-1$
        property.setLabel(FileUtils.getName(initFile));

        Item item = initItem(initFile);
        if (item != null) {
            item.setProperty(property);
            property.setItem(item);

            IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
            property.setId(repositoryFactory.getNextId());
            property.setVersion(VersionUtils.DEFAULT_VERSION);
            property.setAuthor(ReponsitoryContextBridge.getUser());
            property.setCreationDate(new Date());
            property.setModificationDate(property.getCreationDate());
        }
        return property;
    }

    private Item initItem(File srcFile) throws IOException {
        TDQSourceFileItem item = org.talend.dataquality.properties.PropertiesFactory.eINSTANCE.createTDQSourceFileItem();

        item.setExtension(FileUtils.getExtension(srcFile));
        item.setName(FileUtils.getName(srcFile));
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContentFromFile(srcFile);

        item.setContent(byteArray);

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(false);

        IPath rootPath = getWorkspacePath().append(TDQ_SOURCE_FILE_PATH);
        IPath folderPath = new Path(srcFile.getAbsolutePath()).removeLastSegments(1);
        IPath itemStatePath = folderPath.makeRelativeTo(rootPath);
        itemState.setPath(itemStatePath.toString());

        item.setState(itemState);
        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#isModelTask()
     */
    @Override
    public Boolean isModelTask() {
        return true;
    }
}
