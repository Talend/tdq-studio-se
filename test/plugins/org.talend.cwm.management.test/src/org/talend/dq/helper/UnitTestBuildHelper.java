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
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.i18n.internal.DefaultMessagesImpl;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.helper.StatusHelper;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.IRepositoryFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.RepositoryFactoryProvider;
import org.talend.core.repository.utils.ProjectHelper;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.impl.PropertiesFactoryImpl;
import org.talend.dq.analysis.parameters.DBConnectionParameter;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.ui.model.ProjectNodeHelper;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.test.utils.DBPropertiesUtils;
import org.talend.utils.string.StringUtilities;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.record.RecordFactory;
import orgomg.cwm.resource.record.RecordFile;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UnitTestBuildHelper {

    private static Logger log = Logger.getLogger(UnitTestBuildHelper.class);

    private static UnitTestBuildHelper INSTANCE;

    private boolean init = false;

    private IProject iProject;

    private Project tProject;

    private File projectFile;

    private RepositoryNode dataProfilingNode;

    private RepositoryNode librariesNode;

    private RepositoryNode metadataNode;

    public File getProjectFile() {
        return this.projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }

    public boolean isInit() {
        return this.init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public IProject getiProject() {
        return this.iProject;
    }

    public void setiProject(IProject iProject) {
        this.iProject = iProject;
    }

    public Project gettProject() {
        return this.tProject;
    }

    public void settProject(Project tProject) {
        this.tProject = tProject;
    }

    public RepositoryNode getDataProfilingNode() {
        return this.dataProfilingNode;
    }

    public void setDataProfilingNode(RepositoryNode dataProfilingNode) {
        this.dataProfilingNode = dataProfilingNode;
    }

    public RepositoryNode getLibrariesNode() {
        return this.librariesNode;
    }

    public void setLibrariesNode(RepositoryNode librariesNode) {
        this.librariesNode = librariesNode;
    }

    public RepositoryNode getMetadataNode() {
        return this.metadataNode;
    }

    public void setMetadataNode(RepositoryNode metadataNode) {
        this.metadataNode = metadataNode;
    }

    private UnitTestBuildHelper() {
    }

    public static UnitTestBuildHelper getDefault() {
        if (INSTANCE == null) {
            INSTANCE = new UnitTestBuildHelper();
        }
        return INSTANCE;
    }

    public static IProject initProjectStructure() {
        IProject rootProject = ReponsitoryContextBridge.getRootProject();
        if (!rootProject.exists()) {
            initProxyRepository(rootProject);
        }

        return rootProject;
    }

    /**
     * DOC talend Comment method "initProxyRepository".
     */
    public static void initProxyRepository(IProject rootProject) {

        Project project = null;

        ProxyRepositoryFactory proxyRepository = ProxyRepositoryFactory.getInstance();
        IRepositoryFactory repository = RepositoryFactoryProvider.getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID);
        if (repository == null) {
            log.fatal(DefaultMessagesImpl
                    .getString("No local Repository found! Probably due to a missing plugin in the product.")); //$NON-NLS-1$
        }
        proxyRepository.setRepositoryFactoryFromProvider(repository);
        try {
            proxyRepository.checkAvailability();
            proxyRepository.setFullLogonFinished(false);
            // proxyRepository.initialize();

            XmiResourceManager xmiResourceManager = new XmiResourceManager();

            if (rootProject.getFile(FileConstants.LOCAL_PROJECT_FILENAME).exists()) {
                // Initialize TDQ EMF model packages.
                new EMFUtil();
                project = new Project(xmiResourceManager.loadProject(rootProject));
            } else {
                User user = org.talend.core.model.properties.impl.PropertiesFactoryImpl.eINSTANCE.createUser();
                user.setLogin("talend@talend.com"); //$NON-NLS-1$
                user.setPassword("talend@talend.com".getBytes()); //$NON-NLS-1$
                String projectName = rootProject.getName();
                String projectDesc = ResourcesPlugin.getWorkspace().newProjectDescription(projectName).getComment();
                Project projectInfor = ProjectHelper.createProject(projectName, projectDesc, ECodeLanguage.JAVA.getName(), user);

                // MOD zshen create project by proxyRepository
                checkFileName(projectInfor.getLabel(), RepositoryConstants.PROJECT_PATTERN);

                project = proxyRepository.getRepositoryFactoryFromProvider().createProject(projectInfor);

            }

            if (project != null) {
                initRepositoryContext(project);

                // add status
                String defaultTechnicalStatusList = "DEV development;TEST testing;PROD production"; //$NON-NLS-1$
                List<Status> statusList = StatusHelper.parse(defaultTechnicalStatusList);
                proxyRepository.setTechnicalStatus(statusList);
            }

        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
    }

    private static void initRepositoryContext(Project project) {
        RepositoryContext repositoryContext = new RepositoryContext();
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        ctx.putProperty(Context.REPOSITORY_CONTEXT_KEY, repositoryContext);
        repositoryContext.setUser(project.getAuthor());
        repositoryContext.setClearPassword(project.getLabel());
        repositoryContext.setProject(project);
        repositoryContext.setFields(new HashMap<String, String>());
        ProjectManager.getInstance().setMainProjectBranch(project, null);

        ReponsitoryContextBridge.initialized(project.getEmfProject(), project.getAuthor());
    }

    /**
     * create the subfolder under the project and named for folderName.
     * 
     * @param project
     * @param folderName
     * @return
     */
    public static IFolder createRealFolder(IProject project, String folderName) {
        IFolder iFolder = project.getFolder(folderName);
        if (!iFolder.exists()) {
            try {
                iFolder.create(true, true, null);
                File file = WorkspaceUtils.ifolderToFile(iFolder);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
                log.error(e, e);
            }
        }
        return iFolder;
    }

    /**
     * create the subfolder under the parent folder and named for folderName.
     * 
     * @param parentFolder
     * @param folderName
     * @return
     */
    public static IFolder createRealFolder(IFolder parentFolder, String folderName) {
        IFolder iFolder = parentFolder.getFolder(folderName);
        if (!iFolder.exists()) {
            try {
                iFolder.create(true, true, null);
                File file = WorkspaceUtils.ifolderToFile(iFolder);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
                log.error(e, e);
            }
        }
        return iFolder;
    }

    /**
     * create the file under the parentFolder and named for fileName.
     * 
     * @param parentFolder
     * @param fileName
     * @return
     */
    public static IFile createRealFile(IFolder parentFolder, String fileName) {
        IFile iFile = parentFolder.getFile(fileName);
        if (!iFile.exists()) {
            try {
                iFile.create(null, true, null);
                File file = WorkspaceUtils.ifileToFile(iFile);
                if (!file.exists()) {
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file.createNewFile();
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
                log.error(e, e);
            } catch (IOException e) {
                ExceptionHandler.process(e);
                log.error(e, e);
            }
        }
        return iFile;
    }

    /**
     * copy the method from ProxyRepositoryFactory to avoid tos migeration.
     * 
     * @param fileName
     * @param pattern
     */
    private static void checkFileName(String fileName, String pattern) {
        if (!java.util.regex.Pattern.matches(pattern, fileName)) {
            throw new IllegalArgumentException("the file name [" + fileName + "] is invalid of the pattern [" + pattern + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }

    /**
     * create the real RepositoryNode for DataProfiling.
     * 
     * @param iProject
     * @return
     */
    public static RepositoryNode createRealDataProfilingNode(IProject iProject) {
        RepositoryNode node = null;

        if (iProject != null) {
            UnitTestBuildHelper.createRealFolder(iProject, ERepositoryObjectType.TDQ_DATA_PROFILING.getFolder());

            Project tProject = ProjectManager.getInstance().getCurrentProject();
            if (tProject != null && tProject.getEmfProject() != null && tProject.getAuthor() != null) {
                IRepositoryViewObject viewObject = buildRepositoryViewObjectSystemFolder(tProject.getEmfProject(),
                        tProject.getAuthor(), ERepositoryObjectType.TDQ_DATA_PROFILING);

                node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
                viewObject.setRepositoryNode(node);
            }
        }

        return node;
    }

    /**
     * create the real RepositoryNode for Libraries.
     * 
     * @param iProject
     * @return
     */
    public static RepositoryNode createRealLibrariesNode(IProject iProject) {
        RepositoryNode node = null;

        if (iProject != null) {
            UnitTestBuildHelper.createRealFolder(iProject, ERepositoryObjectType.TDQ_LIBRARIES.getFolder());

            Project tProject = ProjectManager.getInstance().getCurrentProject();
            if (tProject != null && tProject.getEmfProject() != null) {
                IRepositoryViewObject viewObject = buildRepositoryViewObjectSystemFolder(tProject.getEmfProject(),
                        tProject.getAuthor(), ERepositoryObjectType.TDQ_LIBRARIES);

                node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
                viewObject.setRepositoryNode(node);
            }
        }

        return node;
    }

    /**
     * create the real RepositoryNode for Metadata.
     * 
     * @param iProject
     * @return
     */
    public static RepositoryNode createRealMetadataNode(IProject iProject) {
        RepositoryNode node = null;

        if (iProject != null) {
            UnitTestBuildHelper.createRealFolder(iProject, ERepositoryObjectType.METADATA.getFolder());

            Project tProject = ProjectManager.getInstance().getCurrentProject();
            if (tProject != null && tProject.getEmfProject() != null && tProject.getAuthor() != null) {
                IRepositoryViewObject viewObject = buildRepositoryViewObjectSystemFolder(tProject.getEmfProject(),
                        tProject.getAuthor(), ERepositoryObjectType.METADATA);

                node = new RepositoryNode(viewObject, null, ENodeType.SYSTEM_FOLDER);
                viewObject.setRepositoryNode(node);
            }
        }

        return node;
    }

    /**
     * create a RepositoryViewObject which is a system folder.
     * 
     * @param tProject
     * @param author
     * @param type
     * @return
     */
    public static IRepositoryViewObject buildRepositoryViewObjectSystemFolder(
            org.talend.core.model.properties.Project emfProject, User author, ERepositoryObjectType type) {
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        FolderItem folderItem = PropertiesFactory.eINSTANCE.createFolderItem();
        Property property = PropertiesFactory.eINSTANCE.createProperty();

        itemState.setItemRelated(folderItem);
        folderItem.setState(itemState);

        folderItem.setParent(emfProject);
        folderItem.setType(FolderType.STABLE_SYSTEM_FOLDER_LITERAL);
        folderItem.setProperty(property);

        property.setAuthor(author);
        property.setCreationDate(new Date());
        property.setId(StringUtilities.getRandomString(26));
        property.setItem(folderItem);
        property.setLabel(type.getFolder());
        property.setMaxInformationLevel(InformationLevel.DEBUG_LITERAL);
        property.setOldStatusCode(""); //$NON-NLS-1$
        property.setVersion("0.1"); //$NON-NLS-1$

        return new Folder(property, type);
    }

    /**
     * delete the project which has been login else will effect the result of junit.
     */
    public static void deleteCurrentProject() {
        // IProject rootProject = ReponsitoryContextBridge.getRootProject();
        // if (rootProject.exists()) {
        // try {
        // rootProject.delete(true, true, null);
        // } catch (CoreException e) {
        // log.error(e, e);
        // Assert.fail(e.getMessage());
        // }
        // }
    }

    /**
     * create a real file connection witl file url
     * 
     * @param fileUrl
     * @param delimitedFileconnection
     * @return
     */
    public MetadataTable initFileConnection(URL fileUrl, DelimitedFileConnection delimitedFileconnection) {
        try {
            delimitedFileconnection.setFilePath(FileLocator.toFileURL(fileUrl).toURI().getPath().toString());
            delimitedFileconnection.setRowSeparatorValue("\n"); //$NON-NLS-1$
            delimitedFileconnection.setEncoding("UTF-8"); //$NON-NLS-1$
            delimitedFileconnection.setFieldSeparatorValue(","); //$NON-NLS-1$
            delimitedFileconnection.setName(ERepositoryObjectType.METADATA_FILE_DELIMITED.getKey());

            MetadataTable metadataTable = ConnectionFactory.eINSTANCE.createMetadataTable();
            // IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            // metadataTable.setId(factory.getNextId());
            RecordFile record = (RecordFile) ConnectionHelper.getPackage(delimitedFileconnection.getName(),
                    delimitedFileconnection, RecordFile.class);
            if (record != null) { // hywang
                PackageHelper.addMetadataTable(metadataTable, record);
            } else {
                RecordFile newrecord = RecordFactory.eINSTANCE.createRecordFile();
                newrecord.setName(delimitedFileconnection.getName());
                ConnectionHelper.addPackage(newrecord, delimitedFileconnection);
                PackageHelper.addMetadataTable(metadataTable, newrecord);
            }
            return metadataTable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * init the file's related metadata table with columns: name, company, city,country, comment. and add all columns as
     * analyzed elements.
     * 
     * @param context
     */
    public MetadataColumn initColumns(AnalysisContext context, MetadataTable metadataTable) {
        List<ModelElement> anaElements = context.getAnalysedElements();

        // // Name
        MetadataColumn name = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        name.setName("name"); //$NON-NLS-1$
        name.setLabel("name"); //$NON-NLS-1$
        anaElements.add(name);
        metadataTable.getColumns().add(name);

        // Company
        MetadataColumn company = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        company.setName("company"); //$NON-NLS-1$
        company.setLabel("company"); //$NON-NLS-1$
        anaElements.add(company);
        metadataTable.getColumns().add(company);
        // City
        MetadataColumn city = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        city.setName("city"); //$NON-NLS-1$
        city.setLabel("city"); //$NON-NLS-1$
        anaElements.add(city);
        metadataTable.getColumns().add(city);
        // Country
        MetadataColumn country = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        country.setName("country"); //$NON-NLS-1$
        country.setLabel("country"); //$NON-NLS-1$
        anaElements.add(country);
        metadataTable.getColumns().add(country);

        // comment
        MetadataColumn comment = ConnectionPackage.eINSTANCE.getConnectionFactory().createMetadataColumn();
        comment.setName("comment"); //$NON-NLS-1$
        comment.setLabel("comment"); //$NON-NLS-1$
        anaElements.add(comment);
        metadataTable.getColumns().add(comment);

        return name;
    }

    /**
     * 
     * DOC qiongli Comment method "createRealAnalysis".
     * 
     * @param name
     * @param folder
     * @param isDelete
     * @return
     */
    public static Analysis createRealAnalysis(String name, IFolder folder, Boolean isDelete) {
        IPath createPath = Path.EMPTY;
        if (folder != null) {
            createPath = new Path(folder.getFullPath().lastSegment());
        }
        Analysis analysis1 = AnalysisHelper.createAnalysis(name);
        TDQAnalysisItem item1 = PropertiesFactoryImpl.eINSTANCE.createTDQAnalysisItem();
        org.talend.core.model.properties.Property property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setId(EcoreUtil.generateUUID());
        property1.setItem(item1);
        property1.setLabel(analysis1.getName());
        item1.setProperty(property1);
        item1.setAnalysis(analysis1);
        ItemState itemState = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(isDelete);
        item1.setState(itemState);
        AnalysisResult analysisResult1 = AnalysisFactory.eINSTANCE.createAnalysisResult();
        analysis1.setResults(analysisResult1);
        try {
            ProxyRepositoryFactory.getInstance().create(item1, createPath, false);
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
        return analysis1;
    }

    public static DatabaseConnectionItem createDatabaseConnectionItem(String name, IFolder folder, Boolean isDelete) {
        IPath createPath = Path.EMPTY;
        if (folder != null) {
            createPath = new Path(folder.getFullPath().lastSegment());
        }
        // connection
        DatabaseConnection createConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        createConnection.setName(name);
        createConnection.setUsername("UserName"); //$NON-NLS-1$
        createConnection.setRawPassword("Password"); //$NON-NLS-1$
        createConnection.setURL("URL"); //$NON-NLS-1$
        createConnection.setDatabaseType(EDatabaseTypeName.MYSQL.getXmlName());
        // ~connection
        DatabaseConnectionItem createDatabaseConnectionItem = PropertiesFactory.eINSTANCE.createDatabaseConnectionItem();

        org.talend.core.model.properties.Property createDatabaseConnectionProperty = PropertiesFactory.eINSTANCE.createProperty();
        createDatabaseConnectionProperty.setId(EcoreUtil.generateUUID());
        createDatabaseConnectionProperty.setItem(createDatabaseConnectionItem);
        createDatabaseConnectionProperty.setLabel(createConnection.getName());
        createDatabaseConnectionItem.setProperty(createDatabaseConnectionProperty);
        createDatabaseConnectionItem.setConnection(createConnection);
        try {
            ProxyRepositoryFactory.getInstance().create(createDatabaseConnectionItem, createPath, false);
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
        return createDatabaseConnectionItem;
    }

    /**
     * getDataManager of DB2
     * 
     * @return
     */
    public Connection getDB2DataManager() {
        //        TypedProperties connectionParams = PropertiesLoader.getProperties(IndicatorEvaluator.class, "db.properties"); //$NON-NLS-1$
        String driverClassName = "com.ibm.db2.jcc.DB2Driver"; //$NON-NLS-1$
        String dbUrl = "jdbc:db2://192.168.31.135:50000/sample"; //$NON-NLS-1$
        String sqlTypeName = "IBM DB2"; //$NON-NLS-1$

        DBConnectionParameter params = new DBConnectionParameter();
        params.setName("DB2_Connection"); //$NON-NLS-1$
        params.setDriverClassName(driverClassName);
        params.setJdbcUrl(dbUrl);
        params.setSqlTypeName(sqlTypeName);

        Properties properties = new Properties();
        properties.setProperty(TaggedValueHelper.UNIVERSE, "");
        properties.setProperty(TaggedValueHelper.DATA_FILTER, "");
        properties.setProperty(TaggedValueHelper.USER, "db2inst1");
        properties.setProperty(TaggedValueHelper.PASSWORD, "db2inst1");

        params.setParameters(properties);

        // create connection
        ConnectionUtils.setTimeout(false);

        MetadataFillFactory instance = MetadataFillFactory.getDBInstance();
        IMetadataConnection metaConnection = instance.fillUIParams(ParameterUtil.toMap(params));

        ReturnCode rc = null;
        try {
            rc = instance.checkConnection(metaConnection);
        } catch (java.lang.RuntimeException e) {
            Assert.fail("connect to " + dbUrl + "failed," + e.getMessage());
        }
        Connection dataProvider = null;
        if (rc.isOk()) {
            dataProvider = instance.fillUIConnParams(metaConnection, null);
            dataProvider.setName("DB2_Connection");

            // because the DI side code is changed, modify the following code.
            metaConnection.setCurrentConnection(dataProvider);
            try {
                ProjectNodeHelper.fillCatalogAndSchemas(metaConnection, (DatabaseConnection) dataProvider);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Assert.assertNotNull("Can not connect to Database: " + dbUrl, dataProvider);
        return dataProvider;
    }

    /**
     * get a real database connection,the connection parameters load from a propery file.
     * 
     * @return
     */
    public Connection getRealOracleDatabase() {
        Properties connectionParams = DBPropertiesUtils.getDefault().getProperties();
        String driverClassName = connectionParams.getProperty("driver_oracle"); //$NON-NLS-1$
        String dbUrl = connectionParams.getProperty("url_oracle"); //$NON-NLS-1$
        String sqlTypeName = connectionParams.getProperty("sqlTypeName_oracle"); //$NON-NLS-1$
        String dbVersion = connectionParams.getProperty("dbVersion_oracle"); //$NON-NLS-1$
        String userName = connectionParams.getProperty("user_oracle"); //$NON-NLS-1$
        String password = connectionParams.getProperty("password_oracle"); //$NON-NLS-1$

        DBConnectionParameter params = new DBConnectionParameter();
        params.setName("oracle_Connection"); //$NON-NLS-1$
        params.setDriverClassName(driverClassName);
        params.setJdbcUrl(dbUrl);
        params.setSqlTypeName(sqlTypeName);

        Properties properties = new Properties();
        properties.setProperty(TaggedValueHelper.USER, userName);
        properties.setProperty(TaggedValueHelper.PASSWORD, password);
        properties.setProperty(TaggedValueHelper.DB_PRODUCT_VERSION, dbVersion);

        params.setParameters(properties);

        // create connection
        ConnectionUtils.setTimeout(false);

        MetadataFillFactory instance = MetadataFillFactory.getDBInstance();
        IMetadataConnection metaConnection = instance.fillUIParams(ParameterUtil.toMap(params));
        metaConnection.setDbVersionString(dbVersion);
        ReturnCode rc = null;
        try {
            rc = instance.checkConnection(metaConnection);
        } catch (java.lang.RuntimeException e) {
            Assert.fail("connect to " + dbUrl + "failed," + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        Connection dataProvider = null;
        if (rc.isOk()) {
            dataProvider = instance.fillUIConnParams(metaConnection, null);
            dataProvider.setName("oracleDB");
            // because the DI side code is changed, modify the following code.
            metaConnection.setCurrentConnection(dataProvider);
            try {
                ProjectNodeHelper.fillCatalogAndSchemas(metaConnection, (DatabaseConnection) dataProvider);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Assert.assertNotNull(dataProvider);
        return dataProvider;
    }

    public static Analysis createAndInitAnalysis() {
        Analysis ana = UnitTestBuildHelper.createRealAnalysis("anaA", null, false);

        AnalysisParameters parameters = AnalysisFactory.eINSTANCE.createAnalysisParameters();
        parameters.setExecutionLanguage(ExecutionLanguage.SQL);
        ana.setParameters(parameters);

        AnalysisContext context = AnalysisFactory.eINSTANCE.createAnalysisContext();
        ana.setContext(context);
        Connection createConnection = ConnectionFactory.eINSTANCE.createConnection();
        createConnection.setName("MySQL");
        context.setConnection(createConnection);
        return ana;
    }

    public static TdColumn createRealTdColumn(String columnName, String tdSqlName, int javaType) {
        TdTable table = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdTable();
        table.setName("TDQ_CALENDAR"); //$NON-NLS-1$
        TdColumn column = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdColumn();
        column.setName(columnName);
        TdSqlDataType tdsql = org.talend.cwm.relational.RelationalFactory.eINSTANCE.createTdSqlDataType();
        tdsql.setName(tdSqlName);
        tdsql.setJavaDataType(javaType);
        column.setSqlDataType(tdsql);
        table.getOwnedElement().add(column);
        column.setOwner(table);
        return column;
    }

}
