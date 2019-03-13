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
package net.sourceforge.sqlexplorer.util;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sourceforge.sqlexplorer.dbproduct.DriverManager;
import net.sourceforge.sqlexplorer.dbproduct.ManagedDriver;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.classloader.ClassLoaderFactory;
import org.talend.core.classloader.DynamicClassLoader;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.metadata.managment.connection.manager.DatabaseConnConstants;

/**
 * created by qiongli on 2014-5-8 Detailled comment
 * 
 */
public class AliasAndManaDriverHelper {

    private static AliasAndManaDriverHelper instance;

    private ILibraryManagerService libManagerServic = null;

    private static Logger log = Logger.getLogger(AliasAndManaDriverHelper.class);

    public static AliasAndManaDriverHelper getInstance() {
        if (instance == null) {
            instance = new AliasAndManaDriverHelper();
        }
        return instance;
    }

    /**
     * create a New ManagerDriver. id is like as "dbType:driverClassName:dbVersion",name is like as "dbType:dbVersion".
     * then regist jdbc driver.
     * 
     * @param dbConn
     * @param id
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public ManagedDriver createNewManagerDriver(DatabaseConnection dbConn) throws Exception {
        String id = this.joinManagedDriverId(dbConn);
        ManagedDriver manaDriver = new ManagedDriver(id);
        String dbType = dbConn.getDatabaseType();
        String dbVersion = dbConn.getDbVersionString();
        manaDriver.setName(dbVersion == null ? dbType : dbType + ":" + dbVersion);
        manaDriver.setDriverClassName(JavaSqlFactory.getDriverClass(dbConn));
        manaDriver.setUrl(JavaSqlFactory.getURL(dbConn));
        // set jar path
        addJars(dbConn, manaDriver);
        // register jdbc driver for Object ManagedDriver when its jars are not empty.
        if (!manaDriver.getJars().isEmpty()) {
            manaDriver.registerSQLDriver(dbConn);
        }
        return manaDriver;
    }

    /**
     * 
     * find a ManaDriver based on driver class name.if not found ,create a new one ManagedDriver.
     * 
     * @param connection
     * @param driverManager
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public ManagedDriver getManaDriverByConnection(Connection connection) throws Exception {
        ManagedDriver manaDriver = null;
        DriverManager driverManager = SQLExplorerPlugin.getDefault().getDriverModel();
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        String driverClass = JavaSqlFactory.getDriverClass(connection);
        if (dbConn == null || dbConn.getDatabaseType() == null || driverClass == null) {
            log.error("can not find a right ManagedDriver by null!");
            return null;
        }
        String managedDriverId = joinManagedDriverId(dbConn);
        String databaseType = dbConn.getDatabaseType();
        manaDriver = driverManager.getDriver(managedDriverId);

        return manaDriver;
    }

    /**
     * find driver jar path from 'temp\dbWizard',if nof found,find it from 'lib\java' and "librariesIndex.xml".
     * 
     * @return
     * @throws MalformedURLException
     */
    public LinkedList<String> getDriverJarRealPaths(List<String> driverJarNameList) throws MalformedURLException {
        LinkedList<String> linkedList = new LinkedList<String>();
        initLibManagerServic();
        boolean jarNotFound = false;

        for (String jarName : driverJarNameList) {
            String tempLibPath = ExtractMetaDataUtils.getInstance().getJavaLibPath();
            File tempFolder = new File(tempLibPath);
            if (tempFolder.exists()) {
                List<File> jarFiles = FilesUtils.getJarFilesFromFolder(tempFolder, jarName);
                if (!jarFiles.isEmpty()) {
                    linkedList.add(jarFiles.get(0).getPath());
                    continue;
                }
            }
            if (libManagerServic != null) {
                String libPath = libManagerServic.getJarPath(jarName);
                if (libPath == null) {
                    jarNotFound = true;
                    break;
                }
                linkedList.add(libPath);
            } else {
                jarNotFound = true;
            }
        }
        // if has one jar file not be found,return a empty list
        if (jarNotFound) {
            linkedList.clear();
        }

        return linkedList;
    }

    private ILibraryManagerService initLibManagerServic() {
        if (libManagerServic == null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
                libManagerServic = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerService.class);
            }
        }
        return libManagerServic;
    }

    /**
     * 
     * add jars into ManagedDriver attribute.these jars have a real path so that can be accessed.
     * 
     * @param connection
     * @param manDr
     * @throws MalformedURLException
     */
    public void addJars(Connection connection, ManagedDriver manDr) throws MalformedURLException {
        List<String> driverJarNameList = new ArrayList<String>();
        DatabaseConnection dbConnnection = (DatabaseConnection) connection;
        String driverJarPath = JavaSqlFactory.getDriverJarPath(dbConnnection);
        if (ConnectionHelper.isJDBC(dbConnnection) && driverJarPath != null) {
            String[] pathArray = driverJarPath.split(";"); //$NON-NLS-1$
            for (String path : pathArray) {
                driverJarNameList.add(TalendQuoteUtils.removeQuotes(path));
            }
        } else {
            String databaseType = dbConnnection.getDatabaseType();
            if (StringUtils.equals(EDatabaseTypeName.IMPALA.getDisplayName(), databaseType)) {
                driverJarNameList = getImpalaDriverJarNameList(dbConnnection);
            } else {
                driverJarNameList = EDatabaseVersion4Drivers.getDrivers(databaseType, dbConnnection.getDbVersionString());
            }
        }
        manDr.setJars(getDriverJarRealPaths(driverJarNameList));
    }

    private List<String> getImpalaDriverJarNameList(DatabaseConnection dbConnnection) {
        List<String> driverJarNameList = new ArrayList<String>();
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            String distribution = dbConnnection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
            String version = dbConnnection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);

            IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(distribution, false);
            if (impalaDistribution != null) {
                String impalaIndex = EDatabaseTypeName.IMPALA.getProduct() + ClassLoaderFactory.KEY_SEPARATOR
                        + impalaDistribution.getName();
                if (impalaDistribution.useCustom()) {
                    // TODO handle custom impala here
                } else {
                    IHDistributionVersion impalaVersion = impalaDistribution.getHDVersion(version, false);
                    if (impalaVersion != null) {
                        List<ModuleNeeded> modulesNeeded = impalaVersion.getModulesNeeded();
                        Set<String> libraries = new HashSet<String>();
                        for (ModuleNeeded m : modulesNeeded) {
                            libraries.add(m.getModuleName());
                        }
                        driverJarNameList.addAll(libraries);
                    }
                }
            }
        }
        return driverJarNameList;
    }

    private IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;
    }

    /**
     * 
     * DOC qiongli Comment method "joinSqlId".
     * 
     * @param dbConn
     * @return
     */
    public String joinManagedDriverId(DatabaseConnection dbConn) {
        String databaseType = dbConn.getDatabaseType();
        String id = PluginConstant.EMPTY_STRING;
        if (databaseType.equalsIgnoreCase(EDatabaseTypeName.HIVE.getXmlName())) {
            id = joinHiveManagedDriverId(dbConn);
        } else {
            String driverClassName = JavaSqlFactory.getDriverClass(dbConn);
            String dbVersion = dbConn.getDbVersionString();
            id = joinManagedDriverId(databaseType, driverClassName, dbVersion);
        }
        return id;

    }

    private String joinHiveManagedDriverId(DatabaseConnection dbConn) {
        IMetadataConnection metadataConn = ConvertionHelper.convert(dbConn);
        String url = metadataConn.getUrl();
        StringBuffer str = new StringBuffer();
        String id = PluginConstant.EMPTY_STRING;
        if (url != null) {
            if (url.startsWith(DatabaseConnConstants.HIVE_2_URL_FORMAT)) {
                str.append("HIVE2");
            } else {
                str.append("HIVE");
            }
            String distroKey = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
            String distroVersion = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
            String hiveModel = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
            str.append(":").append(distroKey).append(":").append(distroVersion).append(":").append(hiveModel);
            id = str.toString();
        }
        return id;
    }

    /**
     * 
     * join dbtype:driverClassName:dbVersion as a ManagedDriver id.
     * 
     * @param databaseType
     * @param driverClassName
     * @param dbVersion
     * @return
     */
    public String joinManagedDriverId(String databaseType, String driverClassName, String dbVersion) {
        StringBuffer id = new StringBuffer(PluginConstant.EMPTY_STRING);
        String colon = ":";
        if (databaseType != null) {
            id.append(databaseType);
        }
        if (driverClassName != null) {
            id.append(colon).append(driverClassName);
        }
        if (dbVersion != null) {
            id.append(colon).append(dbVersion);
        }
        return id.toString();
    }

    public ClassLoader getImpalaClassLoader(IMetadataConnection metadataConn) {
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            String distribution = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
            String version = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);

            IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(distribution, false);
            if (impalaDistribution != null) {
                String impalaIndex = EDatabaseTypeName.IMPALA.getProduct() + ClassLoaderFactory.KEY_SEPARATOR
                        + impalaDistribution.getName();
                if (impalaDistribution.useCustom()) {
                    String jarsStr = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
                    String index = "CustomImpala" + ClassLoaderFactory.KEY_SEPARATOR + impalaIndex + ClassLoaderFactory.KEY_SEPARATOR + metadataConn.getId(); //$NON-NLS-1$
                    DynamicClassLoader classLoader = ClassLoaderFactory.getCustomClassLoader(index, jarsStr);
                    if (classLoader != null) {
                        return classLoader;
                    }
                } else {
                    IHDistributionVersion impalaVersion = impalaDistribution.getHDVersion(version, false);
                    if (impalaVersion != null) {
                        boolean isKeb = Boolean.valueOf((String) metadataConn
                                .getParameter(ConnParameterKeys.CONN_PARA_KEY_USE_KRB));
                        DynamicClassLoader classLoader = ClassLoaderFactory.getClassLoader(impalaIndex
                                + ClassLoaderFactory.KEY_SEPARATOR + impalaVersion.getVersion() + (isKeb ? "?USE_KRB" : ""));//$NON-NLS-1$//$NON-NLS-2$

                        // if not work for extension point, try modules from hadoop distribution
                        if (classLoader == null) {
                            classLoader = ClassLoaderFactory.getClassLoader(impalaVersion);
                        }
                        if (classLoader != null) {
                            return classLoader;
                        }
                    }
                }
            }
        }
        return this.getClass().getClassLoader();
    }
}
