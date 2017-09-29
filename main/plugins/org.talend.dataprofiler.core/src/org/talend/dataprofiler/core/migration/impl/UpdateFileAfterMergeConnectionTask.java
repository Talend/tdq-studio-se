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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

/**
 * 
 * DOC zshen class global comment. Detailled comment Move Connection file form TDQ_Metadata to metadata
 */
public class UpdateFileAfterMergeConnectionTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(UpdateFileAfterMergeConnectionTask.class);

    private Map<String, String> replaceStringMap;

    private FilenameFilter nonPropertyFileFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(FactoriesUtil.PROPERTIES_EXTENSION);
        }
    };

    public Map<String, String> getReplaceStringMap() {
        if (this.replaceStringMap == null) {
            this.replaceStringMap = initReplaceStringMap();
        }
        return this.replaceStringMap;
    }

    public UpdateFileAfterMergeConnectionTask() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;

        Map<File, File> folderMap = initStructure();

        for (File folder : folderMap.keySet()) {
            try {
                // Move the content of connection folder
                tansferFile(folder, folderMap);
            } catch (Exception e) {
                log.error(e, e);
            }

        }

        // ADD xqliu 2010-12-21 bug 17704
        // copy .xsd folder to new location
        File srcMdmXsdFolder = getWorkspacePath().append(
                ExchangeFileNameToReferenceTask.MDM_CONNECTION + IPath.SEPARATOR + ".xsd").toFile();//$NON-NLS-1$
        File tarMdmXsdFolder = getWorkspacePath().append(
                ERepositoryObjectType.getFolderName(ERepositoryObjectType.METADATA_MDMCONNECTION) + IPath.SEPARATOR + ".xsd")//$NON-NLS-1$
                .toFile();
        FilesUtils.copyFolder(srcMdmXsdFolder, tarMdmXsdFolder, true, null, null, true, new NullProgressMonitor());
        // update MDMConnection XSDPath
        File mdmConnectionFolder = new File(ResourceManager.getMDMConnectionFolder().getRawLocationURI());
        final String[] metadataFileExtentionNames = { ".item" };//$NON-NLS-1$
        UpdateMDMConnectionXSDPathTask.doUpdate(true, true, mdmConnectionFolder, metadataFileExtentionNames);
        // reload the mdm connection
        ArrayList<File> fileList = new ArrayList<File>();
        FilesUtils.getAllFilesFromFolder(mdmConnectionFolder, fileList, new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.endsWith("item")) {//$NON-NLS-1$
                    return true;
                }
                return false;
            }
        });
        for (File file : fileList) {
            Connection connection = PrvResourceFileHelper.getInstance().findProvider(WorkspaceUtils.fileToIFile(file));
            connection.eResource().unload();
            EObjectHelper.resolveObject(connection);
        }
        // ~ 17704

        File tdqMetadataFile = getWorkspacePath().append(OLD_MEATADATA_FOLDER_NAME).toFile();
        if (tdqMetadataFile.exists()) {
            FileUtils.deleteDirectory(tdqMetadataFile);
        }

        // change the path which conation in analysis and dqrule.
        File fileAnalysis = getWorkspacePath().append(EResourceConstant.ANALYSIS.getPath()).toFile();
        File fileRule = getWorkspacePath().append(EResourceConstant.RULES.getPath()).toFile();
        try {
            String[] anaFileExtentionNames = { FactoriesUtil.ANA };
            String[] rulesFileEctentionNames = { FactoriesUtil.DQRULE };
            result &= FilesUtils.migrateFolder(fileAnalysis, anaFileExtentionNames, this.getReplaceStringMap(), log)
                    && FilesUtils.migrateFolder(fileRule, rulesFileEctentionNames, this.getReplaceStringMap(), log);

            // AnaResourceFileHelper.getInstance().clear();
            // AnaResourceFileHelper.getInstance().getAllAnalysis();
            // ResourceService.refreshStructure();
        } catch (Exception e) {
            result = false;
            log.error(e, e);
        }

        // ADD xqliu 2011-12-31 TDQ-4331
        fillConnectionNameLabel();
        // ~ 4331

        return result;

    }

    /**
     * fill connection's name or label if they are null.
     */
    private void fillConnectionNameLabel() {
        File connectionFolder = getWorkspacePath().append("metadata").toFile(); //$NON-NLS-1$
        ArrayList<File> fileList = new ArrayList<File>();
        FilesUtils.getAllFilesFromFolder(connectionFolder, fileList, new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.endsWith("item")) {//$NON-NLS-1$
                    return true;
                }
                return false;
            }
        });
        for (File file : fileList) {

            Connection connection = getConnectionFromFile(file);

            if (connection == null) {
                continue;
            }

            Property property = PropertyHelper.getProperty(connection);

            if (connection.getName() == null && connection.getLabel() == null) {
                if (property.getLabel() != null) {
                    connection.setName(property.getLabel());
                    connection.setLabel(property.getLabel());
                }
            } else {
                if (connection.getName() != null && connection.getLabel() == null) {
                    connection.setLabel(connection.getName());
                } else if (connection.getName() == null && connection.getLabel() != null) {
                    connection.setName(connection.getLabel());
                }
            }

            if (connection instanceof DatabaseConnection) {
                DatabaseConnection dbconn = (DatabaseConnection) connection;

                String[] urlParamArray = DatabaseConnStrUtil.analyseURL(dbconn.getDatabaseType(), dbconn.getDbVersionString(),
                        dbconn.getURL());

                if (StringUtils.isEmpty(dbconn.getServerName()) || StringUtils.isEmpty(dbconn.getPort())
                        || StringUtils.isEmpty(dbconn.getAdditionalParams())) {
                    fillParametersFromURL(dbconn, urlParamArray);
                }
            }

            EMFUtil.saveResource(connection.eResource());
        }
    }

    /**
     * DOC bZhou Comment method "fillParametersFromURL".
     * 
     * @param dbconn
     * @param s
     */
    private void fillParametersFromURL(DatabaseConnection dbconn, String[] s) {
        String selection = s[0];

        int index = 1;
        if (!s[index].equals("")) {//$NON-NLS-1$
            if (selection.equals(EDatabaseConnTemplate.GODBC.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.MSODBC.getDBDisplayName())) {
                dbconn.setDatasourceName(s[index]);
            } else if (selection.equals(EDatabaseConnTemplate.SQLITE.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.ACCESS.getDBDisplayName())) {
                dbconn.setFileFieldName(s[index]);
            } else if (selection.equals(EDatabaseConnTemplate.JAVADB_EMBEDED.getDBDisplayName())) {
                dbconn.setSID(s[index]);
            } else if (selection.equals(EDatabaseConnTemplate.HSQLDB_IN_PROGRESS.getDBDisplayName())) {
                dbconn.setDBRootPath(s[index]);
            } else {
                dbconn.setServerName(s[index]);
            }
        }

        index = 2;
        if (!s[index].equals("")) { //$NON-NLS-1$
            if (selection.equals(EDatabaseConnTemplate.INTERBASE.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.TERADATA.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.AS400.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.HSQLDB_IN_PROGRESS.getDBDisplayName())) {
                dbconn.setSID(s[index]);
            } else if (selection.equals(EDatabaseConnTemplate.FIREBIRD.getDBDisplayName())) {
                dbconn.setFileFieldName(s[index]);
            } else {
                dbconn.setPort(s[index]);
            }
        }

        index = 3;
        if (!s[index].equals("")) { //$NON-NLS-1$
            if (selection.equals(EDatabaseConnTemplate.IBMDB2.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.INFORMIX.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.ORACLEFORSID.getDBDisplayName())
                    || selection.equals(EDatabaseConnTemplate.INGRES.getDBDisplayName())) {
                dbconn.setSID(s[index]);
            }
        }

        index = 4;
        if (!s[index].equals("")) { //$NON-NLS-1$
            if (selection.equals(EDatabaseConnTemplate.INFORMIX.getDBDisplayName())) {
                dbconn.setDatasourceName(s[index]);
            } else {
                dbconn.setAdditionalParams(s[index]);
            }
        }

        index = 5;
        if (!s[index].equals("")) { //$NON-NLS-1$
            dbconn.setAdditionalParams(s[index]);
        }
    }

    private Map<File, File> initStructure() throws Exception {
        Map<File, File> folderMap = new HashMap<File, File>();

        File srcDBFolder = getWorkspacePath().append(ExchangeFileNameToReferenceTask.DB_CONNECTION).toFile();
        File srcMDMFolder = getWorkspacePath().append(ExchangeFileNameToReferenceTask.MDM_CONNECTION).toFile();

        if (isWorksapcePath()) {
            if (!ResourceManager.getConnectionFolder().exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.METADATA, Path.EMPTY,
                        EResourceConstant.DB_CONNECTIONS.getName());
            }
            if (!ResourceManager.getMDMConnectionFolder().exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.METADATA, Path.EMPTY,
                        EResourceConstant.MDM_CONNECTIONS.getName());
            }
        }
        File file = getWorkspacePath().append(EResourceConstant.METADATA.getPath()).toFile();
        if (!file.exists()) {
            file.mkdir();
        }

        file = getWorkspacePath().append(EResourceConstant.DB_CONNECTIONS.getPath()).toFile();
        if (!file.exists()) {
            file.mkdir();
        }
        folderMap.put(srcDBFolder, file);

        file = getWorkspacePath().append(EResourceConstant.MDM_CONNECTIONS.getPath()).toFile();
        if (!file.exists()) {
            file.mkdir();
        }
        folderMap.put(srcMDMFolder, file);

        return folderMap;
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    public Date getOrder() {
        return createDate(2010, 8, 16);
    }

    private Map<String, String> initReplaceStringMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("TDQ_Metadata/DB Connections", EResourceConstant.DB_CONNECTIONS.getPath()); //$NON-NLS-1$
        result.put("TDQ_Metadata/MDM Connections", EResourceConstant.MDM_CONNECTIONS.getPath()); //$NON-NLS-1$
        result.put(".prv", ".item"); //$NON-NLS-1$ //$NON-NLS-2$

        return result;
    }

    private void tansferFile(File parentFolder, Map<File, File> folderMap) throws Exception {
        new ResourceSetImpl();

        if (!parentFolder.exists()) {
            return;
        }

        List<File> fileList = new ArrayList<File>();

        FilesUtils.getAllFilesFromFolder(parentFolder, fileList, nonPropertyFileFilter);

        for (File propFile : fileList) {
            try {
                handlePropertiesFile(propFile, folderMap, parentFolder);

            } catch (Exception e) {
                log.warn(e, e);
            }
        }
        if (isWorksapcePath()) {
            collectConnectionID();
        }
    }

    private void collectConnectionID() {
        try {
            RootContainer<String, IRepositoryViewObject> rc = ProxyRepositoryFactory.getInstance().getMetadata(
                    ERepositoryObjectType.METADATA_CONNECTIONS);
            for (IRepositoryViewObject repViewObj : rc.getMembers()) {
                Connection conn = ((ConnectionItem) repViewObj.getProperty().getItem()).getConnection();
                String uuid = ResourceHelper.getUUID(conn);
                String connid = conn.getId();
                if (connid != null && uuid != null) {
                    getReplaceStringMap().put(connid, uuid);
                }
            }
        } catch (PersistenceException e) {
            log.error(e, e);
        }
    }

    private void handlePropertiesFile(File propFile, Map<File, File> folderMap, File parentFolder) throws PersistenceException,
            IOException {
        // MOD qiongli 2012-1-31 TDQ-4431.should use a relative path here.if it is absolute path,dependency client tag
        // is also a absolute path in connection file.
        Property property = PropertyHelper.getProperty(propFile, isWorksapcePath());

        if (property != null) {
            File targetFolder = folderMap.get(parentFolder);
            Item item = property.getItem();
            String connNameBofore = null;
            String connNameAfter = null;

            IPath path = new Path(item.getState().getPath());
            if (ProxyRepositoryFactory.getInstance().getFolderItem(ProjectManager.getInstance().getCurrentProject(),
                    ERepositoryObjectType.getItemType(item), path) == null) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.getItemType(item),
                        path.removeLastSegments(1), path.lastSegment());
            }
            // if (path == null) {
            // path = Path.EMPTY;
            // }
            String connName = null;

            if (item instanceof ConnectionItem) {
                Connection conn = ((ConnectionItem) item).getConnection();
                connNameBofore = conn.eResource().getURI().trimFileExtension().lastSegment();

                String version = property.getVersion();
                if (version == null) {
                    version = "0.1"; //$NON-NLS-1$
                }

                String label = conn.getName() + "_" + version; //$NON-NLS-1$
                connNameAfter = label;

                File destItemFile = new Path(targetFolder.getAbsolutePath()).append(path).append(connNameAfter)
                        .addFileExtension(FactoriesUtil.ITEM_EXTENSION).toFile();
                File destPropFile = new Path(targetFolder.getAbsolutePath()).append(path).append(connNameAfter)
                        .addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION).toFile();
                if (destItemFile.exists()) {
                    int num = 0;

                    // ~
                    while (destItemFile.exists() || destPropFile.exists()) {

                        connName = conn.getName() + String.valueOf(++num);
                        connNameAfter = connName + "_" + version; //$NON-NLS-1$
                        destItemFile = new Path(destItemFile.getPath()).removeLastSegments(1).append(connNameAfter)
                                .addFileExtension(FactoriesUtil.ITEM_EXTENSION).toFile();
                        destPropFile = new Path(destPropFile.getPath()).removeLastSegments(1).append(connNameAfter)
                                .addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION).toFile();
                    }
                    conn.setName(connName);
                    conn.setLabel(connName);
                    property.setLabel(connName);
                    // MOD qiongli 2012-5-7 TDQ-4939 save the new displayName in this case.
                    property.setDisplayName(connName);
                    EMFUtil.saveResource(property.eResource());

                    // EMFUtil.saveResource(itemResource);
                }
                String relationPropPath = ReponsitoryContextBridge.getRootProject().getFullPath()
                        .append(new Path(destPropFile.getPath()).makeRelativeTo(this.getWorkspacePath())).toOSString();
                MetadataHelper.setPropertyPath(relationPropPath, conn);
            }

            if (isWorksapcePath()) {

                ProxyRepositoryFactory.getInstance().create(item, path, true);

            } else {
                connNameAfter = copyFile(targetFolder, propFile, property, path, connNameAfter, connName, folderMap);
            }

            if (connNameBofore != null && connNameAfter != null) {
                getReplaceStringMap().put(connNameBofore, connNameAfter);
            }
        }
    }

    private String copyFile(File targetFolder, File propFile, Property property, IPath path, String connNameAfter,
            String connName, Map<File, File> folderMap) throws IOException {

        File destItemFile = new Path(targetFolder.getAbsolutePath()).append(path).append(connNameAfter)
                .addFileExtension(FactoriesUtil.ITEM_EXTENSION).toFile();
        File destPropFile = new Path(targetFolder.getAbsolutePath()).append(path).append(connNameAfter)
                .addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION).toFile();

        File srcItemFile = new Path(propFile.getAbsolutePath()).removeFileExtension()
                .addFileExtension(FactoriesUtil.ITEM_EXTENSION).toFile();
        File srcPropFile = propFile;
        Item item = property.getItem();
        // MOD by zshen to resolve repetitious name question(any time it should't come in here)
        FileUtils.copyFile(srcItemFile, destItemFile);
        FileUtils.copyFile(srcPropFile, destPropFile);
        // if (destItemFile.exists()) {
        // int num = 0;
        //
        // if (item instanceof ConnectionItem) {
        //
        // Resource itemResource = getResource(srcItemFile.getAbsolutePath());
        // Connection conn = null;
        // for (EObject object : itemResource.getContents()) {
        // if (object instanceof Connection) {
        // conn = (Connection) object;
        // connName = conn.getName();
        // break;
        // }
        // }
        //
        // // ~
        // while (destItemFile.exists() || destPropFile.exists()) {
        // String version = property.getVersion();
        // if (version == null) {
        // version = "0.1";
        // }
        //
        // connName = connName + String.valueOf(++num);
        // conn.setName(connName);
        // connNameAfter = connName + "_" + version;
        // destItemFile = new Path(destItemFile.getPath()).removeLastSegments(1).append(connNameAfter)
        // .addFileExtension(FactoriesUtil.ITEM_EXTENSION).toFile();
        // destPropFile = new Path(destPropFile.getPath()).removeLastSegments(1).append(connNameAfter)
        // .addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION).toFile();
        //
        //
        // }
        // // EMFUtil.saveResource(itemResource);
        // }
        // }

        if (item instanceof ConnectionItem) {
            ConnectionItem connectionItem = (ConnectionItem) item;
            Connection conn = getConnectionFromFile(destItemFile);

            if (conn != null) {
                if (connName != null) {
                    conn.setName(connName);
                    conn.setLabel(connName);
                }
                String relationPropPath = ReponsitoryContextBridge.getRootProject().getFullPath()
                        .append(new Path(destPropFile.getPath()).makeRelativeTo(this.getWorkspacePath())).toOSString();
                MetadataHelper.setPropertyPath(relationPropPath, conn);
                connectionItem.setConnection(conn);
                EMFUtil.saveResource(conn.eResource());
            }

            Resource propResource = getResource(destPropFile);

            Property newProperty = (Property) EcoreUtil.getObjectByType(propResource.getContents(),
                    PropertiesPackage.eINSTANCE.getProperty());
            newProperty.setAuthor(property.getAuthor());
            newProperty.setLabel(connectionItem.getConnection().getName());
            newProperty.setItem(item);
            // String propertyPath = String.valueOf(Path.SEPARATOR)
            // + (new
            // Path(destPropFile.getPath()).makeRelativeTo(this.getWorkspacePath().removeLastSegments(1)).toString());
            // MetadataHelper.setPropertyPath(propertyPath, connectionItem.getConnection());
            item.setProperty(newProperty);

            propResource.getContents().clear();

            propResource.getContents().add(newProperty);
            propResource.getContents().add(item);
            propResource.getContents().add(item.getState());

            EMFUtil.saveResource(propResource);

        }
        return connNameAfter;
    }

    private Connection getConnectionFromFile(File connectionFile) {
        Resource itemResource = getResource(connectionFile);
        for (EObject object : itemResource.getContents()) {
            if (object instanceof Connection) {
                return (Connection) object;
            }
        }

        return null;
    }
}
