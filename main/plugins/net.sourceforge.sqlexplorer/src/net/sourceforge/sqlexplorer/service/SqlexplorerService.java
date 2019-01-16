// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package net.sourceforge.sqlexplorer.service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.sqlexplorer.IConstants;
import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dataset.DataSet;
import net.sourceforge.sqlexplorer.dataset.actions.ExportCSVAction;
import net.sourceforge.sqlexplorer.dataset.mapdb.MapDBColumnSetDataSet;
import net.sourceforge.sqlexplorer.dataset.mapdb.MapDBDataSet;
import net.sourceforge.sqlexplorer.dataset.mapdb.MapDBSetDataSet;
import net.sourceforge.sqlexplorer.dataset.mapdb.TalendDataSet;
import net.sourceforge.sqlexplorer.dbdetail.DetailTabManager;
import net.sourceforge.sqlexplorer.dbproduct.Alias;
import net.sourceforge.sqlexplorer.dbproduct.AliasManager;
import net.sourceforge.sqlexplorer.dbproduct.DriverManager;
import net.sourceforge.sqlexplorer.dbproduct.ManagedDriver;
import net.sourceforge.sqlexplorer.dbproduct.User;
import net.sourceforge.sqlexplorer.dbstructure.nodes.DatabaseNode;
import net.sourceforge.sqlexplorer.dbstructure.nodes.INode;
import net.sourceforge.sqlexplorer.dbstructure.nodes.SchemaNode;
import net.sourceforge.sqlexplorer.dbstructure.nodes.TableFolderNode;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.plugin.actions.OpenPasswordConnectDialogAction;
import net.sourceforge.sqlexplorer.plugin.editors.SQLEditor;
import net.sourceforge.sqlexplorer.plugin.editors.SQLEditorInput;
import net.sourceforge.sqlexplorer.plugin.perspectives.SQLExplorerPluginPerspective;
import net.sourceforge.sqlexplorer.plugin.views.DatabaseStructureView;
import net.sourceforge.sqlexplorer.sqleditor.actions.ExecSQLAction;
import net.sourceforge.sqlexplorer.util.AliasAndManaDriverHelper;
import net.sourceforge.sqlexplorer.util.MyURLClassLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.classloader.DynamicClassLoader;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.indicator.ColumnFilter;
import org.talend.cwm.indicator.DataValidation;
import org.talend.dataprofiler.service.ISqlexplorerService;
import org.talend.metadata.managment.hive.HiveClassLoaderFactory;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by xqliu on 2014-9-15 Detailled comment
 * 
 */
public class SqlexplorerService implements ISqlexplorerService {

    private static Logger log = Logger.getLogger(SqlexplorerService.class);

    public static final String SEMICOLON_STRING = ";";//$NON-NLS-1$

    /**
     * Extension used for the files which is integrated in TOS properties.
     */
    public static final String PROPERTIES_EXTENSION = "properties"; //$NON-NLS-1$

    private static SqlexplorerService INSTANCE;

    public static SqlexplorerService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqlexplorerService();
        }
        return INSTANCE;
    }

    @Override
    public void runInDQViewer(DatabaseConnection databaseConnection, String editorName, String query) {
        String lEditorName = editorName;
        if (lEditorName == null) {
            lEditorName = String.valueOf(SQLExplorerPlugin.getDefault().getEditorSerialNo());
        }
        Alias alias = SQLExplorerPlugin.getDefault().getAliasManager().getAlias(databaseConnection.getName());
        if (alias == null) {
            // add the connection to alias and call this method again
            addConnetionAliasToSQLPlugin(databaseConnection);
            alias = SQLExplorerPlugin.getDefault().getAliasManager().getAlias(databaseConnection.getName());
        }
        if (alias != null) {
            runInDQViewer(alias, databaseConnection, lEditorName, query);
        }
    }

    /**
     * open the sql editor and run it.
     * 
     * @param alias
     * @param databaseConnection
     * @param lEditorName
     * @param query
     */
    private void runInDQViewer(Alias alias, DatabaseConnection databaseConnection, String lEditorName, String query) {
        String url = JavaSqlFactory.getURL(databaseConnection);
        String username = JavaSqlFactory.getUsername(databaseConnection);
        String password = JavaSqlFactory.getPassword(databaseConnection);
        SQLEditorInput input = new SQLEditorInput("SQL Editor (" + alias.getName() + "." + lEditorName + ").sql"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        User user = alias.getUser(username);
        if (PluginConstant.EMPTY_STRING.equals(username)) {
            // get the user both the dbtype and username are the same.
            if (!alias.getUrl().equals(url)) {
                user = new net.sourceforge.sqlexplorer.dbproduct.User(username, password);
                user.setAlias(alias);
                alias.addUser(user);
            }
        } else {
            if (user == null) {
                user = alias.getDefaultUser();
            }
        }
        alias.setDefaultUser(user);

        // set IMetadataConnection into the user, if the db type is hive, should use IMetadataConnection to
        // create the hive connection
        if (databaseConnection != null) {
            user.setDatabaseConnection(databaseConnection);
            // if ManagedDriver class is not Loaded,check if it lack jars then update the realted jar.
            updateDriverIfClassNotLoad(databaseConnection);
        }

        input.setUser(user);
        // TDQ-9533 append a "limit X" in sql query for vertica database.
        if (EDatabaseTypeName.VERTICA.getProduct().equals(databaseConnection.getProductId())) {
            String maxPref = SQLExplorerPlugin.getDefault().getPreferenceStore().getString(IConstants.MAX_SQL_ROWS);
            int maxNum = maxPref == null ? 100 : Integer.parseInt(maxPref);
            query = query + " limit " + maxNum;
        }
        IWorkbenchPage page = SQLExplorerPlugin.getDefault().getActivePage();
        try {
            SQLEditor editorPart = (SQLEditor) page.openEditor(input, SQLEditor.class.getName());
            editorPart.setText(query);
            new ExecSQLAction(editorPart).run();
        } catch (PartInitException e) {
            log.error(e, e);
        }
    }

    /**
     * if the sqlexplorer driver is unRegisted,load the driver jar by lib manage system.
     * 
     * @param sqlPlugin
     * @param connection
     * @param databaseConnection
     */
    public void updateDriverIfClassNotLoad(DatabaseConnection databaseConnection) {
        SQLExplorerPlugin sqlPlugin = SQLExplorerPlugin.getDefault();
        DriverManager driverManager = sqlPlugin.getDriverModel();
        String driverClassName = JavaSqlFactory.getDriverClass(databaseConnection);
        if (driverClassName != null) {
            String id = AliasAndManaDriverHelper.getInstance().joinManagedDriverId(databaseConnection);
            ManagedDriver manDr = driverManager.getDriver(id);
            if (manDr != null && !manDr.isDriverClassLoaded()) {
                loadDriverByLibManageSystem(databaseConnection);
            }
        }
    }

    @Override
    public void initSqlExplorerRootProject(IProject rootProject) {
        if (SQLExplorerPlugin.getDefault().getRootProject() == null) {
            SQLExplorerPlugin.getDefault().setRootProject(rootProject);
        }
    }

    @Override
    public void addConnetionAliasToSQLPlugin(ModelElement... dataproviders) {
        SQLExplorerPlugin sqlPlugin = SQLExplorerPlugin.getDefault();
        AliasManager aliasManager = sqlPlugin.getAliasManager();
        DriverManager driverManager = sqlPlugin.getDriverModel();

        List<String> tdqSupportDBType = MetadataConnectionUtils.getTDQSupportDBTemplate();
        // if all dataproviders are not supported on DQ side,don't save files SQLAliases.xml and
        // SQLDrivers.xml.Otherwise,save it.
        AliasAndManaDriverHelper aliasManaDriverHelper = AliasAndManaDriverHelper.getInstance();
        for (ModelElement dataProvider : dataproviders) {
            try {
                Connection connection = SwitchHelpers.CONNECTION_SWITCH.doSwitch(dataProvider);
                // MOD bug mzhao filter the other connections except database connection.
                if (connection != null && connection instanceof DatabaseConnection) {
                    // TDQ-8379 do nothing if the database type isn't supproted on DQ side.
                    DatabaseConnection dbConn = ((DatabaseConnection) connection);
                    String databaseType = dbConn.getDatabaseType();
                    if (!tdqSupportDBType.contains(databaseType)) {
                        continue;
                    }
                    // only new Alias when it is not in aliasManager
                    Alias alias = aliasManager.getAlias(dataProvider.getName());
                    String url = JavaSqlFactory.getURL(connection);
                    // if the alias is not null and the url is same with the connection, this means the alias is already
                    // exist; if the alias is not null but hte url is not same with the connection, this means the
                    // connection has been overwrite , need to rebuild the alias
                    boolean aliasExist = alias != null && StringUtils.equals(url, alias.getUrl());
                    if (!aliasExist) {
                        if (alias == null) {
                            alias = new Alias(dataProvider.getName());
                        }

                        String user = JavaSqlFactory.getUsername(connection);
                        // MOD gdbu 2011-3-17 bug 19539
                        String password = JavaSqlFactory.getPassword(connection);
                        // ~19539

                        // user should not be null
                        user = user == null ? "" : user; //$NON-NLS-1$
                        // password should not be null
                        password = password == null ? "" : password; //$NON-NLS-1$

                        // MOD scorreia 2010-07-24 set empty string instead of null password so that database xml file
                        // is serialized correctly.
                        assert user != null;
                        assert password != null;

                        User previousUser = new User(user, password);
                        previousUser.setDatabaseConnection(dbConn);
                        alias.setDefaultUser(previousUser);

                        alias.setAutoLogon(false);
                        alias.setConnectAtStartup(true);
                        alias.setUrl(url);

                        ManagedDriver manDr = aliasManaDriverHelper.getManaDriverByConnection(dbConn);
                        if (manDr == null) {
                            manDr = aliasManaDriverHelper.createNewManagerDriver(dbConn);
                            driverManager.addDriver(manDr);
                        } else if (!manDr.isDriverClassLoaded()) {
                            this.loadDriverByLibManageSystem(dbConn);
                        }

                        if (manDr != null) {
                            alias.setDriver(manDr);
                        }
                    }

                    if (!aliasManager.contains(alias) && alias.getName() != null) {
                        aliasManager.addAlias(alias);
                    }

                    // Add yyi 2010-09-15 14549: hide connections in SQL Explorer when a connection is moved to the
                    // trash bin.
                    // MOD Qiongli TDQ-6166 just put once for every Alias
                    if (sqlPlugin.getPropertyFile().get(alias) == null) {
                        sqlPlugin.getPropertyFile().put(alias, getPropertyFile(dataProvider));
                    }
                    aliasManager.modelChanged();

                }
            } catch (Throwable e) { // MOD scorreia 2010-07-24 catch all exceptions
                log.error(e, e);
                continue;
            }
        }
    }

    @Override
    public void loadDriverByLibManageSystem(DatabaseConnection connection) {
        if (ConnectionHelper.isHive(connection)) {
            loadManagedDriverForHive(connection);
        } else {
            loadManagedDriver(connection);
        }
    }

    /**
     * 
     * Load the driver by lib management system , which will configure the SQL Explorer driver classpath from xml.
     * 
     * @param dbType
     * @param dbVersion
     * @param driverClassName
     * @param userName. the userName is used a special case :it MSSQL with empty userName.
     */
    private void loadManagedDriver(DatabaseConnection dbConn) {
        String dbType = dbConn.getDatabaseType();
        String dbVersion = dbConn.getDbVersionString();
        String driverClassName = JavaSqlFactory.getDriverClass(dbConn);
        if (dbType == null || driverClassName == null) {
            return;
        }
        DriverManager driverManager = SQLExplorerPlugin.getDefault().getDriverModel();
        AliasAndManaDriverHelper aliasManaHelper = AliasAndManaDriverHelper.getInstance();
        String manaDriverId = aliasManaHelper.joinManagedDriverId(dbType, driverClassName, dbVersion);
        ManagedDriver manDr = driverManager.getDriver(manaDriverId);
        if (manDr != null && !manDr.isDriverClassLoaded()) {
            // find driver jars from 'temp\dbWizard', prefrence page or installation path 'lib\java',
            // "librariesIndex.xml".
            try {
                List<String> jarNames = EDatabaseVersion4Drivers.getDrivers(dbType, dbVersion);
                if (jarNames.isEmpty() && "JDBC".equals(dbType)) {
                    String driverJarPath = dbConn.getDriverJarPath();
                    if (driverJarPath != null) {
                        String[] pathArray = driverJarPath.split(";"); //$NON-NLS-1$
                        for (String path : pathArray) {
                            jarNames.add(path);
                        }
                    }
                }
                LinkedList<String> driverJarRealPaths = aliasManaHelper.getDriverJarRealPaths(jarNames);
                if (!driverJarRealPaths.isEmpty()) {
                    manDr.getJars().clear();
                    manDr.getJars().addAll(driverJarRealPaths);
                }

                manDr.registerSQLDriver(dbConn);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    /**
     * TDDQ-8113 load hive drive by DynamicClassLoader,then set the attribute for Hive ManagedDriver.
     * 
     * @param connection
     */
    private void loadManagedDriverForHive(DatabaseConnection connection) {
        DriverManager driverManager = SQLExplorerPlugin.getDefault().getDriverModel();
        String id = AliasAndManaDriverHelper.getInstance().joinManagedDriverId(connection);
        ManagedDriver manDr = driverManager.getDriver(id);
        IMetadataConnection metadataConnection = ConvertionHelper.convert(connection);
        ClassLoader classLoader = null;
        if (EDatabaseTypeName.IMPALA.getXmlName().equalsIgnoreCase(connection.getDatabaseType())) {
            classLoader = AliasAndManaDriverHelper.getInstance().getImpalaClassLoader(metadataConnection);
        } else {
            classLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataConnection);
        }
        if (classLoader != null && classLoader instanceof DynamicClassLoader) {
            DynamicClassLoader dynClassLoader = (DynamicClassLoader) classLoader;
            String libStorePath = dynClassLoader.getLibStorePath();
            File libFolder = new File(libStorePath);
            if (libFolder.exists()) {
                List<String> relaPathLs = new ArrayList<String>();
                relaPathLs.addAll(dynClassLoader.getLibraries());
                Set<String> findAllJarPath = findAllJarPath(libFolder, relaPathLs);
                if (!findAllJarPath.isEmpty()) {
                    manDr.getJars().addAll(findAllJarPath);
                    try {
                        manDr.registerHiveSQLDriver(connection);
                    } catch (ClassNotFoundException e) {
                        log.error(e);
                    } catch (InstantiationException e) {
                        log.error(e);
                    } catch (IllegalAccessException e) {
                        log.error(e);
                    }

                }
            }
        }
    }

    /**
     * 
     * find all jar pathes by jar names.
     * 
     * @param root
     * @param jarNames
     * @return if return an empty Set,indicate that it find failed.
     */
    private Set<String> findAllJarPath(File root, List<String> jarNames) {
        Set<String> jarPathes = new HashSet<String>();
        if (!root.exists() || jarNames == null || jarNames.isEmpty()) {
            return jarPathes;
        }
        boolean allIsOK = true;
        try {
            for (String jarName : jarNames) {
                List<File> jarFiles = FilesUtils.getJarFilesFromFolder(root, jarName);
                if (jarFiles.isEmpty()) {
                    allIsOK = false;
                    break;
                }
                for (File file : jarFiles) {
                    jarPathes.add(file.getPath());
                }
            }
        } catch (MalformedURLException e) {
            log.error(e);
        }
        if (!allIsOK) {
            jarPathes.clear();
        }
        return jarPathes;
    }

    private IFile getPropertyFile(ModelElement modelElement) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        Resource eResource = modelElement.eResource();
        if (eResource != null) {
            String platformString = eResource.getURI().toPlatformString(true);
            IPath propPath = new Path(platformString).removeFileExtension().addFileExtension(PROPERTIES_EXTENSION);
            return root.getFile(propPath);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.service.ISqlexplorerService#findSqlExplorerTableNode(org.talend.core.model.metadata.builder
     * .connection.Connection, orgomg.cwm.objectmodel.core.Package, java.lang.String, java.lang.String)
     */
    @Override
    public void findSqlExplorerTableNode(Connection providerConnection, Package parentPackageElement, String tableName,
            String activeTabName) {
        // Open data explore perspective.
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
            ITDQRepositoryService service = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(
                    ITDQRepositoryService.class);
            if (service != null) {
                service.changePerspectiveAction(SQLExplorerPluginPerspective.class.getName());
            } else {
                return;
            }
        }
        Collection<Alias> aliases = SQLExplorerPlugin.getDefault().getAliasManager().getAliases();
        String url = JavaSqlFactory.getURL(providerConnection);
        User currentUser = null;
        for (Alias alias : aliases) {
            if (alias.getUrl().equals(url)) {
                currentUser = alias.getDefaultUser();
                OpenPasswordConnectDialogAction openDlgAction = new OpenPasswordConnectDialogAction(alias,
                        alias.getDefaultUser(), false);
                openDlgAction.run();
                break;
            }
        }

        // MOD qiongli bug 13093,2010-7-2,show the warning dialog when the table can't be found
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        if (currentUser == null) {
            MessageDialog.openWarning(shell, Messages.getString("SqlExplorerBridge.Warning"), //$NON-NLS-1$
                    Messages.getString("SqlExplorerBridge.MissTable") + tableName); //$NON-NLS-1$
            return;
        }
        DatabaseNode root = currentUser.getMetaDataSession().getRoot();
        root.load();
        List<INode> catalogs = root.getCatalogs();
        List<INode> schemas = root.getSchemas();

        Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(parentPackageElement);
        Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(parentPackageElement);

        INode catalogOrSchemaNode = null;
        // TDQ-12005: fix Exasol/hive(TDQ-11887: hdp20 at least) database can view index/keys well
        String findCatalogNodeName = isExasol(url) ? "EXA_DB" : (isHive(url) ? "NoCatalog" : "");
        if (!findCatalogNodeName.equals("") && !catalogs.isEmpty()) {
            for (INode catalogNode : catalogs) {
                if (findCatalogNodeName.equalsIgnoreCase(catalogNode.getName())) {
                    catalogOrSchemaNode = catalogNode;
                    break;
                }
            }
        } else {
            if (catalog != null) {
                // MOD klliu bug 14662 2010-08-05
                if (!catalogs.isEmpty()) {
                    for (INode catalogNode : catalogs) {
                        if (parentPackageElement.getName().equalsIgnoreCase(catalogNode.getName())) {
                            catalogOrSchemaNode = catalogNode;
                            break;
                        }
                    }
                } else {
                    catalogOrSchemaNode = root;
                }
            } else {
                // MOD by zshen for 20517
                if (schemas.isEmpty()) {
                    // the case for mssql/postgrel(which have catalog and schema structor) schema analysis.
                    Catalog shcmeaOfCatalogNode = CatalogHelper.getParentCatalog(parentPackageElement);
                    for (INode catalogNode : catalogs) {
                        if (shcmeaOfCatalogNode != null && shcmeaOfCatalogNode.getName().equalsIgnoreCase(catalogNode.getName())) {
                            catalogOrSchemaNode = catalogNode;
                            break;
                        }
                    }
                }
                for (INode schemaNode : schemas) {
                    if (parentPackageElement.getName().equalsIgnoreCase(schemaNode.getName())) {
                        catalogOrSchemaNode = schemaNode;
                        break;
                    }
                }
            }
        }

        // find the table folder node.
        if (catalogOrSchemaNode == null) {
            throw new NullPointerException(Messages.getString("SqlExplorerBridge.CATORSCHMISNULL")); //$NON-NLS-1$
        }
        // Added 20130409 TDQ-6823 yyin when want to get some schema's tables, should give the schema name to the
        // catalog node.
        if (schema != null) {
            if (catalogOrSchemaNode.getSchemaName() == null) {
                catalogOrSchemaNode.setSchemaName(schema.getName());
            } else if (!StringUtils.equals(catalogOrSchemaNode.getSchemaName(), schema.getName())) {
                // if this catalog already loaded its children of some schema, should reload for this schema.
                if (catalogOrSchemaNode.isChildrenLoaded()) {
                    SQLExplorerPlugin.getDefault().getDatabaseStructureView()
                            .refreshSessionTrees(currentUser.getMetaDataSession());
                    List<INode> catalogs2 = currentUser.getMetaDataSession().getRoot().getCatalogs();
                    if (catalogs2.size() != 0) {
                        for (INode catalogNode : catalogs2) {
                            if (catalogOrSchemaNode.getName().equalsIgnoreCase(catalogNode.getName())) {
                                catalogOrSchemaNode = catalogNode;
                                catalogOrSchemaNode.setSchemaName(schema.getName());
                                break;
                            }
                        }

                    }
                }
            }
        }// ~

        INode[] childNodes = catalogOrSchemaNode.getChildNodes();

        // TDQ-9543 if import netezza from 5.6.0 or lower version, the structure of it will include the catalog only, so
        // need to find the schema and load the table nodes
        if (isNetezza(url)) {
            SchemaNode sNode = getNetezzaSchema(childNodes, JavaSqlFactory.getUsername(providerConnection));
            if (sNode != null) {
                childNodes = sNode.getChildNodes();
            }
        }

        TableFolderNode tableFolderNode = null;
        for (INode node : childNodes) {
            if ("TABLE".equals(node.getQualifiedName())) { //$NON-NLS-1$
                tableFolderNode = (TableFolderNode) node;
                break;
            }
        }
        if (tableFolderNode == null) {
            log.fatal(Messages.getString("SqlExplorerBridge.TABLE_FOLDER_NULL0")); //$NON-NLS-1$
        } else {
            INode[] tableNodes = tableFolderNode.getChildNodes();
            for (INode node : tableNodes) {
                if (tableName.equalsIgnoreCase(node.getName())) {
                    DetailTabManager.setActiveTabName(activeTabName);
                    DatabaseStructureView dsView = SQLExplorerPlugin.getDefault().getDatabaseStructureView();
                    dsView.setSessionSelectionNode(currentUser.getMetaDataSession(), new StructuredSelection(node));
                    // MOD qiongli bug 13093,2010-7-2
                    SQLExplorerPlugin.getDefault().getConnectionsView().getTreeViewer()
                            .setSelection(new StructuredSelection(currentUser));
                    return;
                }
            }
        }
        MessageDialog.openWarning(shell, Messages.getString("SqlExplorerBridge.Warning"), //$NON-NLS-1$
                Messages.getString("SqlExplorerBridge.MissTable") + tableName); //$NON-NLS-1$
    }

    /**
     * DOC xqliu Comment method "getNetezzaSchema".
     * 
     * @param childNodes
     * @param username
     * @return
     */
    private SchemaNode getNetezzaSchema(INode[] childNodes, String username) {
        for (INode node : childNodes) {
            if (node != null && node instanceof SchemaNode) {
                SchemaNode sNode = (SchemaNode) node;
                if (StringUtils.equalsIgnoreCase(username, sNode.getName())) {
                    return sNode;
                }
            }
        }
        return null;
    }

    /**
     * DOC xqliu Comment method "isNetezza".
     * 
     * @param url
     * @return
     */
    private boolean isNetezza(String url) {
        return StringUtils.startsWithIgnoreCase(url, "jdbc:netezza");
    }

    private boolean isExasol(String url) {
        return StringUtils.startsWithIgnoreCase(url, "jdbc:exa");
    }

    private boolean isHive(String url) {
        return StringUtils.startsWithIgnoreCase(url, "jdbc:hive");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#getDriver(java.lang.String, java.lang.String)
     */
    @Override
    public Driver getDriver(String driverClassName, String jarsPath) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        Driver driver = null;
        if (StringUtils.isNotEmpty(jarsPath)) {
            try {
                driver = this.createGenericJDBC(jarsPath, driverClassName);
            } catch (Exception e) {
                log.error(e, e);
            }
            return driver;
        }
        SQLExplorerPlugin sqlExplorerPlugin = SQLExplorerPlugin.getDefault();
        if (sqlExplorerPlugin != null) {
            net.sourceforge.sqlexplorer.dbproduct.DriverManager driverModel = sqlExplorerPlugin.getDriverModel();
            try {
                Collection<ManagedDriver> drivers = driverModel.getDrivers();
                for (ManagedDriver managedDriver : drivers) {
                    LinkedList<String> jars = managedDriver.getJars();
                    List<URL> urls = new ArrayList<URL>();
                    for (int i = 0; i < jars.size(); i++) {
                        File file = new File(jars.get(i));
                        if (file.exists()) {
                            urls.add(file.toURI().toURL());
                        }
                    }
                    if (!urls.isEmpty()) {
                        try {
                            MyURLClassLoader cl;
                            cl = new MyURLClassLoader(urls.toArray(new URL[0]));
                            Class<?> clazz = cl.findClass(driverClassName);
                            if (clazz != null) {
                                driver = (Driver) clazz.newInstance();
                                if (driver != null) {
                                    return driver;
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            // do nothings
                        }
                    }

                }
            } catch (MalformedURLException e) {
                // do nothings
            }
        }
        if (driver == null) {
            driver = (Driver) Class.forName(driverClassName).newInstance();
        }
        return driver;
    }

    private Driver createGenericJDBC(String driverJars, String driverName) throws Exception {
        Driver driver = null;
        String[] driverJarPath = driverJars.split(SEMICOLON_STRING);
        try {
            int driverCount = 0;
            URL[] driverUrl = new URL[driverJarPath.length];
            for (String dirverpath : driverJarPath) {
                driverUrl[driverCount++] = new File(dirverpath).toURL();
            }
            URLClassLoader cl = URLClassLoader.newInstance(driverUrl, Thread.currentThread().getContextClassLoader());
            Class c = cl.loadClass(driverName);
            driver = (Driver) c.newInstance();
        } catch (Exception ex) {
            log.error(ex, ex);
            throw ex;
        }
        return driver;
    }

    @Override
    public void initAllConnectionsToSQLExplorer(List<Connection> conns) {
        for (Connection conn : conns) {
            addConnetionAliasToSQLPlugin(conn);
        }
    }

    @Override
    public void setSqlEditorEditable(Object part, boolean lock) {
        if (part instanceof SQLEditor) {
            ((SQLEditor) part).setEditable(lock);
        }

    }

    @Override
    public boolean needAddDriverConnection(DatabaseConnection dbConn) {
        boolean isNeed = false;
        DriverManager driverManager = SQLExplorerPlugin.getDefault().getDriverModel();
        String id = AliasAndManaDriverHelper.getInstance().joinManagedDriverId(dbConn);
        ManagedDriver manDr = driverManager.getDriver(id);
        if (manDr == null) {
            isNeed = true;
        } else if (!manDr.isDriverClassLoaded()) {
            loadDriverByLibManageSystem(dbConn);
            if (!manDr.isDriverClassLoaded()) {
                isNeed = true;
            }
        }
        return isNeed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#getMyURLClassLoaderAssignableClasses(java.net.URL)
     */
    @Override
    public Class[] getMyURLClassLoaderAssignableClasses(URL url) {
        Class[] classes = null;
        try {
            classes = new MyURLClassLoader(url).getAssignableClasses(Driver.class);
        } catch (Exception e) {
            classes = new Class[] {};
        }
        return classes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.service.ISqlexplorerService#updateConnetionAliasByName(org.talend.core.model.metadata.
     * builder.connection.Connection, java.lang.String)
     */
    @Override
    public void updateConnetionAliasByName(Connection connection, String aliasName) {
        if (connection == null || aliasName == null) {
            return;
        }
        SQLExplorerPlugin sqlPlugin = SQLExplorerPlugin.getDefault();
        // if the ctabItem is open,close it.
        DatabaseStructureView view = sqlPlugin.findDatabaseStructureView();
        if (view != null) {
            view.closeCurrentCabItem(aliasName);
        }

        AliasManager aliasManager = sqlPlugin.getAliasManager();
        Alias alias = aliasManager.getAlias(aliasName);
        if (alias != null) {
            try {
                aliasManager.removeAlias(aliasName);
                addConnetionAliasToSQLPlugin(connection);
            } catch (Exception e) {
                log.error(e, e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.service.ISqlexplorerService#removeAliasInSQLExplorer(orgomg.cwm.foundation.softwaredeployment
     * .DataProvider[])
     */
    @Override
    public void removeAliasInSQLExplorer(DataProvider... dataproviders) {
        SQLExplorerPlugin sqlPlugin = SQLExplorerPlugin.getDefault();
        AliasManager aliasManager = sqlPlugin.getAliasManager();

        DatabaseStructureView dsView = sqlPlugin.findDatabaseStructureView();
        // MOD qiongli 2012-11-12 TDQ-6166,only load aliases from file when AliasManager'Aliases is empty.should remove
        // alias from propertyFile map at the same time.
        try {
            Collection<Alias> aliases = aliasManager.getAliases();
            if (aliases.isEmpty()) {
                return;
            }
            for (DataProvider dataProvider : dataproviders) {
                String aliasName = dataProvider.getName();
                if (null == aliasName) {
                    continue;
                }
                Alias alias = aliasManager.getAlias(aliasName);
                if (alias != null) {
                    sqlPlugin.getPropertyFile().remove(alias);
                    aliasManager.removeAlias(aliasName);
                }
                // if the ctabItem is open,close it.
                if (dsView != null) {
                    dsView.closeCurrentCabItem(aliasName);
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        aliasManager.modelChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#aliasExist(java.lang.String)
     */
    @Override
    public boolean aliasExist(String connectionName) {
        SQLExplorerPlugin sqlPlugin = SQLExplorerPlugin.getDefault();
        AliasManager aliasManager = sqlPlugin.getAliasManager();
        Alias alias = aliasManager.getAlias(connectionName);
        return alias != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#createExportCSVAction()
     */
    @Override
    public Action createExportCSVAction() {
        ExportCSVAction exportAction = new ExportCSVAction();
        exportAction.setEnabled(true);
        return exportAction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#setExportCSVActionTable(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void setExportCSVActionTable(Object action, Object table) {
        if (action instanceof ExportCSVAction && table instanceof Table) {
            ExportCSVAction exportAction = (ExportCSVAction) action;
            exportAction.setEnabled(true);
            exportAction.setTable((Table) table);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#create(java.lang.String[], java.lang.Object, int,
     * java.lang.Object, int)
     */
    @Override
    public Object createMapDBColumnSetDataSet(String[] columnHeader, Object mapDB, Long size, Object currIndicator, int pageSize) {
        return new MapDBColumnSetDataSet(columnHeader, (Map<List<Object>, Long>) mapDB, size, (DataValidation) currIndicator,
                pageSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#createMapDBSetDataSet(java.lang.String[],
     * java.lang.Object, int)
     */
    @Override
    public Object createMapDBSetDataSet(String[] columnHeader, Object mapDB, int pageSize) {
        return new MapDBSetDataSet(columnHeader, (Set<Object>) mapDB, pageSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#createMapDBDataSet(java.lang.String[], java.lang.Object,
     * int, java.lang.Object, java.lang.Long)
     */
    @Override
    public Object createMapDBDataSet(String[] columnHeader, Object mapDB, int pageSize, Object columnFilter, Long itemSize) {
        return new MapDBDataSet(columnHeader, (Map<Object, List<Object>>) mapDB, pageSize, (ColumnFilter) columnFilter, itemSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#resetTalendDataSetIndex(java.lang.Object, int, int)
     */
    @Override
    public void resetTalendDataSetIndex(Object talendDataSet, long fromIndex, long toIndex) {
        TalendDataSet tds = (TalendDataSet) talendDataSet;
        tds.setStartIndex(fromIndex);
        tds.setEndIndex(toIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#createDataSet(java.lang.String[], java.lang.String[][])
     */
    @Override
    public Object createDataSet(String[] columnHeader, String[][] columnValue) {
        return new DataSet(columnHeader, columnValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ISqlexplorerService#isInstanceofTalendDataSetIndex(java.lang.Object)
     */
    @Override
    public boolean isInstanceofTalendDataSet(Object talendDataSet) {
        if (TalendDataSet.class.isInstance(talendDataSet)) {
            return true;
        }
        return false;
    }

}
