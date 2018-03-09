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
package org.talend.cwm.db.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.IRepositoryContextService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.CloneConnectionUtils;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.cwm.constants.DevelopmentStatus;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dq.CWMPlugin;
import org.talend.dq.analysis.parameters.DBConnectionParameter;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.ui.utils.DBConnectionContextUtils;
import org.talend.metadata.managment.utils.DatabaseConstant;
import org.talend.utils.ProductVersion;
import org.talend.utils.sql.metadata.constants.GetColumn;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * Utility class for database connection handling.
 */
public final class ConnectionUtils {

    private static Logger log = Logger.getLogger(ConnectionUtils.class);

    // MOD xqliu 2009-02-02 bug 5261
    public static final int LOGIN_TEMEOUT_MILLISECOND = 20000;

    public static final int LOGIN_TIMEOUT_SECOND = 20;

    private static Boolean timeout = null;

    // MOD mzhao 2009-06-05 Bug 7571
    // private static final Map<String, Driver> DRIVER_CACHE = new HashMap<String, Driver>();

    public static boolean isTimeout() {
        if (timeout == null) {
            IPreferencesService service = Platform.getPreferencesService();
            if (service == null) {
                timeout = true;
            } else {
                timeout =
                        service.getBoolean(CWMPlugin.getDefault().getBundle().getSymbolicName(),
                                PluginConstant.CONNECTION_TIMEOUT, false, null);
            }
        }
        return timeout;
    }

    public static void setTimeout(boolean timeout) {
        ConnectionUtils.timeout = timeout;
    }

    /**
     * private constructor.
     */
    private ConnectionUtils() {
    }

    /**
     * check whether the connection is available.
     * 
     * @param datamanager
     * @param analysisName
     * @return boolean
     */
    public static boolean checkConnection(DataManager datamanager, String analysisName) {
        Connection analysisDataProvider = ConnectionUtils.getConnectionFromDatamanager(datamanager);
        ReturnCode connectionAvailable = isConnectionAvailable(analysisDataProvider);
        if (!connectionAvailable.isOk()) {
            log.error(connectionAvailable.getMessage());
            MessageDialogWithToggle.openError(Display.getCurrent().getActiveShell(),
                    Messages.getString("ConnectionUtils.checkConnFailTitle"),//$NON-NLS-1$
                    Messages.getString("ConnectionUtils.checkConnFailMsg", analysisName));//$NON-NLS-1$
            return false;
        }
        return true;
    }

    /**
     * This method is used to check conectiton is avalible for analysis or report ,when analysis or report runs.
     * 
     * @param analysisDataProvider
     * @return
     */
    public static ReturnCode isConnectionAvailable(Connection analysisDataProvider) {
        ReturnCode returnCode = new ReturnCode();
        if (analysisDataProvider == null) {
            returnCode.setOk(false);
            returnCode.setMessage(Messages.getString("ConnectionUtils.checkConnFailTitle")); //$NON-NLS-1$
            return returnCode;
        }

        // check hive connection
        IMetadataConnection metadataConnection = ConvertionHelper.convert(analysisDataProvider);
        if (metadataConnection != null) {
            if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConnection.getDbType())) {
                try {
                    // need to do this first when check for hive embed connection.
                    if (isHiveEmbedded(analysisDataProvider)) {
                        JavaSqlFactory.doHivePreSetup(analysisDataProvider);
                    }
                    HiveConnectionManager.getInstance().checkConnection(metadataConnection);
                    returnCode.setOk(true);
                } catch (ClassNotFoundException e) {
                    returnCode.setOk(false);
                    returnCode.setMessage(e.toString());
                } catch (InstantiationException e) {
                    returnCode.setOk(false);
                    returnCode.setMessage(e.toString());
                } catch (IllegalAccessException e) {
                    returnCode.setOk(false);
                    returnCode.setMessage(e.toString());
                } catch (SQLException e) {
                    returnCode.setOk(false);
                    returnCode.setMessage(e.toString());
                }
                return returnCode;
            }
        }

        // MOD klliu check file connection is available
        if (analysisDataProvider instanceof FileConnection) {
            FileConnection fileConn = (FileConnection) analysisDataProvider;
            // ADD msjian TDQ-4559 2012-2-28: when the fileconnection is context mode, getOriginalFileConnection.
            if (fileConn.isContextMode()) {
                IRepositoryContextService service = CoreRuntimePlugin.getInstance().getRepositoryContextService();
                if (service != null) {
                    fileConn = service.cloneOriginalValueConnection(fileConn);
                }
            }
            // TDQ-4559 ~
            String filePath = fileConn.getFilePath();
            try {
                BufferedReader filePathAvailable = FilesUtils.isFilePathAvailable(filePath, fileConn);
                if (filePathAvailable == null) {
                    returnCode.setOk(false);
                    returnCode.setMessage(Messages.getString("ConnectionUtils.checkConnFailTitle") + " " + filePath); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } catch (UnsupportedEncodingException e) {
                returnCode.setOk(false);
                returnCode.setMessage(e.toString());
            } catch (FileNotFoundException e) {
                returnCode.setOk(false);
                returnCode.setMessage(e.toString());
            } catch (IOException e) {
                returnCode.setOk(false);
                returnCode.setMessage(e.toString());
            }
            return returnCode;
        }
        // ~
        Properties props = new Properties();
        props.put(TaggedValueHelper.USER, JavaSqlFactory.getUsername(analysisDataProvider));
        props.put(TaggedValueHelper.PASSWORD, JavaSqlFactory.getPassword(analysisDataProvider));

        if (analysisDataProvider instanceof DatabaseConnection) {
            // MOD qiongli TDQ-11507,for GeneralJdbc,should check connection too after validation jar and jdbc driver .
            if (isTcompJdbc(analysisDataProvider) || isGeneralJdbc(analysisDataProvider)) {
                ReturnCode rcJdbc = checkJdbcJarFilePathDriverClassName((DatabaseConnection) analysisDataProvider);
                if (!rcJdbc.isOk()) {
                    return rcJdbc;
                }
            }
            // MOD qiongli 2014-5-14 in order to check and connect a dbConnection by a correct driver,replace
            // 'ConnectionUtils.checkConnection(...)' with 'managerConn.check(metadataConnection)'.
            ManagerConnection managerConn = new ManagerConnection();
            returnCode.setOk(managerConn.check(metadataConnection));
            returnCode.setMessage(managerConn.getMessageException());
        }
        return returnCode;
    }

    /**
     * if the Connection's type is hive embedded return true.
     * 
     * @param analysisDataProvider
     * @return
     */
    public static boolean isHiveEmbedded(Connection analysisDataProvider) {
        if (!Platform.isRunning()) {
            return false;
        }
        // MOD 20130313 TDQ-6524 avoid popup context select dialog when running analysis,yyin
        IMetadataConnection metadataConnection =
                ConvertionHelper.convert(analysisDataProvider, false, analysisDataProvider.getContextName());
        return isHiveEmbedded(metadataConnection);
    }

    /**
     * if the Connection's type is hive embedded return true.
     * 
     * @param metadataConnection
     * @return
     */
    public static boolean isHiveEmbedded(IMetadataConnection metadataConnection) {
        if (!Platform.isRunning()) {
            return false;
        }
        // FIXME do not use metadata connection to test hive mode if possible.
        String dbType = metadataConnection.getDbType();
        String dbVersionString = metadataConnection.getDbVersionString();
        if (EDatabaseTypeName.HIVE.getDisplayName().equalsIgnoreCase(dbType)
                && HiveModeInfo.get(dbVersionString) == HiveModeInfo.EMBEDDED) {
            return true;
        }
        return false;
    }

    /**
     * if the Connection's type is General JDBC return true.
     * 
     * @param conn a database connection
     * @return
     */
    public static boolean isGeneralJdbc(Connection conn) {
        boolean jdbc = false;
        if (conn instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) conn;
            EDatabaseTypeName databaseType = EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType());
            if (EDatabaseTypeName.GENERAL_JDBC.equals(databaseType)) {
                jdbc = true;
            }
        }
        return jdbc;
    }

    /**
     * if the DriverClassName is empty or Jar File Path is invalid return false.
     * 
     * @param dbConn a General JDBC database connection
     * @return
     * @throws MalformedURLException
     * @deprecated replace it with checkJdbcJarFilePathDriverClassName(DatabaseConnection)
     */
    public static ReturnCode checkGeneralJdbcJarFilePathDriverClassName(DatabaseConnection dbConn)
            throws MalformedURLException {
        ReturnCode returnCode = new ReturnCode();
        String driverClass = JavaSqlFactory.getDriverClass(dbConn);
        String driverJarPath = JavaSqlFactory.getDriverJarPath(dbConn);
        if (driverClass == null || driverClass.trim().equals("")) { //$NON-NLS-1$
            returnCode.setOk(false);
            returnCode.setMessage(Messages.getString("ConnectionUtils.DriverClassEmpty")); //$NON-NLS-1$
        } else {
            if (driverJarPath == null || driverJarPath.trim().equals("")) { //$NON-NLS-1$
                returnCode.setOk(false);
                returnCode.setMessage(Messages.getString("ConnectionUtils.DriverJarFileEmpty")); //$NON-NLS-1$
            } else {
                List<String> driverJarNameList = new ArrayList<String>();
                String[] splits = driverJarPath.split(";"); //$NON-NLS-1$
                for (String str : splits) {
                    if (!StringUtils.isBlank(str)) {
                        driverJarNameList.add(str);
                    }
                }
                LinkedList<String> driverJarRealPaths = getDriverJarRealPaths(driverJarNameList);
                if (driverJarRealPaths.isEmpty()) {
                    returnCode.setOk(false);
                    returnCode.setMessage(Messages.getString("ConnectionUtils.JarFileCanNotBeFound")); //$NON-NLS-1$
                }
                for (String str : driverJarRealPaths) {
                    File jarFile = new File(str);
                    if (!jarFile.exists() || jarFile.isDirectory()) {
                        returnCode.setOk(false);
                        returnCode.setMessage(Messages.getString("ConnectionUtils.DriverJarFileInvalid")); //$NON-NLS-1$
                        break;
                    }
                }
            }
        }
        return returnCode;
    }

    /**
     * if the DriverClassName is empty or Jar File Path is invalid return false.
     * 
     * @param dbConn a General JDBC or TCOMP JDBC connection
     * @return
     * @throws MalformedURLException
     */
    public static ReturnCode checkJdbcJarFilePathDriverClassName(DatabaseConnection dbConn) {
        ReturnCode returnCode = new ReturnCode();
        String driverClass = JavaSqlFactory.getDriverClass(dbConn);
        String driverJarPath = JavaSqlFactory.getDriverJarPath(dbConn);
        if (StringUtils.isBlank(driverClass)) {
            returnCode.setOk(false);
            returnCode.setMessage(Messages.getString("ConnectionUtils.DriverClassEmpty")); //$NON-NLS-1$
            return returnCode;
        } else if (StringUtils.isBlank(driverJarPath)) {
            returnCode.setOk(false);
            returnCode.setMessage(Messages.getString("ConnectionUtils.DriverJarFileEmpty")); //$NON-NLS-1$
        }
        if (returnCode.isOk()) {
            List<String> driverJarNameList = new ArrayList<String>();
            String slashStr = "/"; //$NON-NLS-1$
            String semicolonStr = ";"; //$NON-NLS-1$
            if (driverJarPath.contains(slashStr)) {
                if (driverJarPath.contains(semicolonStr)) {
                    String jars[] = driverJarPath.split(semicolonStr);
                    for (String jar : jars) {
                        String jarName = jar.split(slashStr)[1] + ".jar"; //$NON-NLS-1$
                        driverJarNameList.add(jarName);
                    }
                } else {
                    String jarName = driverJarPath.split(slashStr)[1] + ".jar"; //$NON-NLS-1$
                    driverJarNameList.add(jarName);
                }
            } else {
                String[] splits = driverJarPath.split(semicolonStr);
                for (String str : splits) {
                    if (!StringUtils.isBlank(str)) {
                        driverJarNameList.add(str);
                    }
                }
            }
            if (!driverJarNameList.isEmpty()) {
                try {
                    LinkedList<String> driverJarRealPaths;
                    driverJarRealPaths = getDriverJarRealPaths(driverJarNameList);
                    // only check the real path for Platform running
                    if (Platform.isRunning() && driverJarRealPaths.isEmpty()) {
                        returnCode.setOk(false);
                        returnCode.setMessage(Messages.getString("ConnectionUtils.JarFileCanNotBeFound")); //$NON-NLS-1$
                    }
                    for (String str : driverJarRealPaths) {
                        File jarFile = new File(str);
                        if (!jarFile.exists() || jarFile.isDirectory()) {
                            returnCode.setOk(false);
                            returnCode.setMessage(Messages.getString("ConnectionUtils.DriverJarFileInvalid")); //$NON-NLS-1$
                            break;
                        }
                    }
                } catch (MalformedURLException e) {
                    returnCode.setOk(false);
                    returnCode.setMessage(e.getMessage());
                }
            }
        }
        return returnCode;
    }

    /**
     * Method "isValid".
     * 
     * @param connection the connection to test
     * @return a return code with the appropriate message (never null)
     */
    public static ReturnCode isValid(final java.sql.Connection connection) {
        return org.talend.utils.sql.ConnectionUtils.isValid(connection);
    }

    /**
     * Method "closeConnection".
     * 
     * @param connection the connection to close.
     * @return a ReturnCode with true if ok, false if problem. {@link ReturnCode#getMessage()} gives the error message
     * when there is a problem.
     */
    public static ReturnCode closeConnection(final java.sql.Connection connection) {
        return org.talend.utils.sql.ConnectionUtils.closeConnection(connection);
    }

    /**
     * DOC xqliu Comment method "isOdbcMssql". bug 9822
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcMssql(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata.getDatabaseProductName().equals(DatabaseConstant.ODBC_MSSQL_PRODUCT_NAME)) {
            return true;
        }
        return false;
    }

    public static boolean isOdbcProgress(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata
                        .getDatabaseProductName()
                        .toLowerCase()
                        .indexOf(DatabaseConstant.ODBC_PROGRESS_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isTeradata".
     * 
     * @param connection
     * @return
     */
    public static boolean isTeradata(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType =
                    dbConn.getDatabaseType() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : dbConn
                            .getDatabaseType();
            return EDatabaseTypeName.TERADATA.getXmlName().equalsIgnoreCase(databaseType);
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isMssql".
     * 
     * @param connection
     * @return decide to whether is mssql connection
     * @throws SQLException
     */
    public static boolean isMssql(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && !connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata.getDatabaseProductName().equals(DatabaseConstant.ODBC_MSSQL_PRODUCT_NAME)) {
            return true;
        }
        return false;
    }

    /**
     * mzhao bug: TDQ-4622 Is the connection is an ingres connection?
     * 
     * @param connection
     * @return true if connection is ingres, false otherwise
     */
    public static boolean isIngres(Connection connection) throws SQLException {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType =
                    dbConn.getDatabaseType() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : dbConn
                            .getDatabaseType();
            return EDatabaseTypeName.INGRES.getXmlName().equalsIgnoreCase(databaseType);
        }
        return false;
    }

    /**
     * mzhao bug: TDQ-4622 Is the connection is an informix connection?
     * 
     * @param connection
     * @return true if connection is informix, false otherwise
     */
    public static boolean isInformix(Connection connection) throws SQLException {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType =
                    dbConn.getDatabaseType() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : dbConn
                            .getDatabaseType();
            return EDatabaseTypeName.INFORMIX.getXmlName().equalsIgnoreCase(databaseType);
        }
        return false;
    }

    /**
     * mzhao bug: TDQ-4622 Is the connection is an DB2 connection?
     * 
     * @param connection
     * @return true if connection is DB2, false otherwise
     */
    public static boolean isDB2(Connection connection) throws SQLException {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType =
                    dbConn.getDatabaseType() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : dbConn
                            .getDatabaseType();
            // databaseType: IBM DB2, but DBKey is DB2
            return databaseType.contains(EDatabaseTypeName.IBMDB2.getXmlName());
        }
        return false;
    }

    /**
     * Comment method "isDB2".
     * 
     * @param metadata
     * @return
     * @throws SQLException
     */
    public static boolean isDB2(DatabaseMetaData metadata) throws SQLException {
        if (metadata != null && metadata.getDatabaseProductName() != null
                && metadata.getDatabaseProductName().indexOf(DatabaseConstant.IBM_DB2_ZOS_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isMssql".
     * 
     * @param connection
     * @return
     */
    public static boolean isMssql(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType =
                    dbConn.getDatabaseType() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : dbConn
                            .getDatabaseType();
            return EDatabaseTypeName.MSSQL.getXmlName().equalsIgnoreCase(databaseType);
        }
        return false;
    }

    /**
     * DOC hwang Comment method "isMysql".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isMysql(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.MYSQL_PRODUCT_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata
                        .getDatabaseProductName()
                        .toLowerCase()
                        .indexOf(DatabaseConstant.MYSQL_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isAs400".
     * 
     * @param connection
     * @return
     */
    public static boolean isAs400(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType =
                    dbConn.getDatabaseType() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : dbConn
                            .getDatabaseType();
            return EDatabaseTypeName.AS400.getDisplayName().equalsIgnoreCase(databaseType);
        }
        return false;
    }

    public static boolean isSybaseeDBProducts(String productName) {
        return Arrays.asList(org.talend.utils.sql.ConnectionUtils.getSybaseDBProductsName()).contains(productName);
    }

    /**
     * DOC qiongli Comment method "isDelimitedFileConnection".
     * 
     * @param dataprovider
     * @return
     */
    public static boolean isDelimitedFileConnection(DataProvider dataprovider) {
        return dataprovider instanceof DelimitedFileConnection;
    }

    /**
     * DOC zshen Comment method "isOdbcMssql". feature 10630
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcExcel(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata.getDatabaseProductName().equals(DatabaseConstant.ODBC_EXCEL_PRODUCT_NAME)) {
            return true;
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isOdbcConnection". feature 10630
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcConnection(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isPostgresql".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isPostgresql(java.sql.Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        if (metaData != null) {
            String databaseProductName = metaData.getDatabaseProductName();
            if (databaseProductName != null) {
                return databaseProductName.toLowerCase().indexOf(DatabaseConstant.POSTGRESQL_PRODUCT_NAME) > -1;
            }
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isPostgresql".
     * 
     * @param connection
     * @return
     */
    public static boolean isPostgresql(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType =
                    dbConn.getDatabaseType() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : dbConn
                            .getDatabaseType();
            return EDatabaseTypeName.PSQL.getXmlName().equalsIgnoreCase(databaseType);
        }
        return false;
    }

    public static boolean isPostgresql(DBConnectionParameter connectionParam) {
        String sqlTypeName = connectionParam == null ? null : connectionParam.getSqlTypeName();
        if (sqlTypeName != null) {
            return sqlTypeName.toLowerCase().contains("postgres"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isOdbcPostgresql".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcPostgresql(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata
                        .getDatabaseProductName()
                        .toLowerCase()
                        .indexOf(DatabaseConstant.POSTGRESQL_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isOdbcOracle".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcOracle(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata
                        .getDatabaseProductName()
                        .toLowerCase()
                        .indexOf(DatabaseConstant.ODBC_ORACLE_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * 
     * DOC qiongli Comment method "isOdbcOracle".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcTeradata(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata
                        .getDatabaseProductName()
                        .toLowerCase()
                        .indexOf(DatabaseConstant.ODBC_TERADATA_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isOdbcIngres".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    public static boolean isOdbcIngres(java.sql.Connection connection) throws SQLException {
        @SuppressWarnings("deprecation")
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().toLowerCase().startsWith(DatabaseConstant.ODBC_DRIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata
                        .getDatabaseProductName()
                        .toLowerCase()
                        .indexOf(DatabaseConstant.INGRES_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isJdbcIngres".
     * 
     * @param connection
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("deprecation")
    public static boolean isJdbcIngres(java.sql.Connection connection) throws SQLException {
        DatabaseMetaData connectionMetadata = org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection);
        if (connectionMetadata.getDriverName() != null
                && connectionMetadata.getDriverName().equals(DatabaseConstant.JDBC_INGRES_DEIVER_NAME)
                && connectionMetadata.getDatabaseProductName() != null
                && connectionMetadata
                        .getDatabaseProductName()
                        .toLowerCase()
                        .indexOf(DatabaseConstant.INGRES_PRODUCT_NAME) > -1) {
            return true;
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isSqlite".
     * 
     * @param connection
     * @return
     */
    public static boolean isSqlite(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            return SupportDBUrlType.SQLITE3DEFAULTURL.getDBKey().equals(dbConn.getDatabaseType());
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "printResultSetColumns".
     * 
     * @param rs
     */
    public static void printResultSetColumns(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnSize = metaData.getColumnCount();
            for (int i = 0; i < columnSize; ++i) {
                System.out.println("[" + (i + 1) + "]:" + metaData.getColumnName(i + 1));//$NON-NLS-1$//$NON-NLS-2$
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOC xqliu Comment method "getDatabaseType".
     * 
     * @param connection
     * @return the database type string or null
     */
    public static String getDatabaseType(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            return dbConn.getDatabaseType();
        }
        return null;
    }

    /**
     * DOC zshen Comment method "setName".
     * 
     * @param conn
     * @param password
     */
    public static void setName(Connection conn, String name) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            dbConn.setName(name);
            dbConn.setLabel(name);
        }
    }

    /**
     * DOC xqliu Comment method "createConnectionParam".
     * 
     * @param conn
     * @return
     */
    public static DBConnectionParameter createConnectionParam(Connection conn) {
        DBConnectionParameter connectionParam = new DBConnectionParameter();

        Properties properties = new Properties();
        // MOD xqliu 2010-08-06 bug 14593
        properties.setProperty(TaggedValueHelper.USER, JavaSqlFactory.getUsername(conn));
        properties.setProperty(TaggedValueHelper.PASSWORD, JavaSqlFactory.getPassword(conn));
        // ~ 14593
        connectionParam.setParameters(properties);
        connectionParam.setName(conn.getName());
        connectionParam.setAuthor(MetadataHelper.getAuthor(conn));
        connectionParam.setDescription(MetadataHelper.getDescription(conn));
        connectionParam.setPurpose(MetadataHelper.getPurpose(conn));
        connectionParam.setStatus(MetadataHelper.getDevStatus(conn));
        connectionParam.setDriverPath(((DatabaseConnection) conn).getDriverJarPath());
        connectionParam.setDriverClassName(JavaSqlFactory.getDriverClass(conn));
        connectionParam.setJdbcUrl(JavaSqlFactory.getURL(conn));
        connectionParam.setHost(JavaSqlFactory.getServerName(conn));
        connectionParam.setPort(JavaSqlFactory.getPort(conn));

        if (conn instanceof DatabaseConnection) {
            connectionParam.setSqlTypeName(((DatabaseConnection) conn).getDatabaseType());
            String dbmsId = ((DatabaseConnection) conn).getDbmsId();
            connectionParam.setDbmsId(dbmsId);
            // ADD klliu 2011-05-19 21704: Refactoring this "otherParameter" !
            connectionParam.setFilterCatalog(dbmsId);
        }
        // MOD klliu if oracle set schema to other parameter
        if (conn instanceof DatabaseConnection) {
            DatabaseConnection dbConnection = (DatabaseConnection) conn;
            connectionParam.setOtherParameter(dbConnection.getUiSchema());
            // ADD klliu 2011-05-19 21704: Refactoring this "otherParameter" !
            connectionParam.setFilterSchema(dbConnection.getUiSchema());
        }
        // MOD mzhao adapte model. MDM connection editing need handle
        // additionally.
        // connectionParam.getParameters().setProperty(TaggedValueHelper.UNIVERSE,
        // DataProviderHelper.getUniverse(connection));
        connectionParam.setDbName(JavaSqlFactory.getSID(conn));
        // MOD by zshen for bug 15314
        String retrieveAllMetadata = MetadataHelper.getRetrieveAllMetadata(conn);
        connectionParam.setRetrieveAllMetadata(retrieveAllMetadata == null ? true : new Boolean(retrieveAllMetadata)
                .booleanValue());

        return connectionParam;
    }

    /**
     * DOC xqliu Comment method "fillConnectionMetadataInformation".
     * 
     * @param conn
     * @return
     */
    public static Connection fillConnectionMetadataInformation(Connection conn) {
        // ADD xqliu 2010-10-13 bug 15756
        int tSize = conn.getTaggedValue().size();
        EList<Package> dataPackage = conn.getDataPackage();
        // ~ 15756
        Property property = PropertyHelper.getProperty(conn);
        // fill name and label
        conn.setName(property.getLabel());
        conn.setLabel(property.getLabel());
        // fill metadata
        MetadataHelper.setAuthor(conn, property.getAuthor().getLogin());
        MetadataHelper.setDescription(property.getDescription(), conn);
        String statusCode =
                property.getStatusCode() == null ? org.talend.dataquality.PluginConstant.EMPTY_STRING : property
                        .getStatusCode();
        MetadataHelper.setDevStatus(
                conn,
                org.talend.dataquality.PluginConstant.EMPTY_STRING.equals(statusCode) ? DevelopmentStatus.DRAFT
                        .getLiteral() : statusCode);
        MetadataHelper.setPurpose(property.getPurpose(), conn);
        MetadataHelper.setVersion(property.getVersion(), conn);
        String retrieveAllMetadataStr = MetadataHelper.getRetrieveAllMetadata(conn);
        // ADD xqliu 2010-10-13 bug 15756
        if (tSize == 0 && dataPackage.size() == 1
                && !org.talend.dataquality.PluginConstant.EMPTY_STRING.equals(dataPackage.get(0).getName())) {
            retrieveAllMetadataStr = "false";//$NON-NLS-1$
        }
        // ~ 15756
        // MOD klliu bug 15821 retrieveAllMetadataStr for Diff database
        MetadataHelper.setRetrieveAllMetadata(retrieveAllMetadataStr == null ? "true" : retrieveAllMetadataStr, conn);//$NON-NLS-1$
        String schema = MetadataHelper.getOtherParameter(conn);
        MetadataHelper.setOtherParameter(schema, conn);
        return conn;
    }

    /**
     * DOC xqliu Comment method "createDatabaseVersionString".
     * 
     * @param dbConn
     * @return
     */
    public static String createDatabaseVersionString(DatabaseConnection dbConn) {
        List<EDatabaseVersion4Drivers> eVersions = EDatabaseVersion4Drivers.indexOfByDbType(dbConn.getProductId());
        if (eVersions != null && !eVersions.isEmpty()) {
            return eVersions.get(0).getVersionValue();
        }
        return null;
    }

    /**
     * retrieve sqlDataType if it have a name is "Null".
     * 
     * @param tdTable
     */
    public static void retrieveColumn(MetadataTable tdTable) {
        List<TdColumn> columnList = ColumnSetHelper.getColumns((ColumnSet) tdTable);
        if (columnList != null && columnList.size() > 0) {
            TdColumn tempColumn = columnList.get(0);
            if (tempColumn.getSqlDataType() == null || "NULL".equalsIgnoreCase(tempColumn.getSqlDataType().getName())//$NON-NLS-1$
                    && 0 == tempColumn.getSqlDataType().getJavaDataType()) {

                if (tdTable != null) {
                    Connection tempConnection = ConnectionHelper.getConnection(tempColumn);
                    if (tempConnection != null) {
                        java.sql.Connection connection = JavaSqlFactory.createConnection(tempConnection).getObject();
                        if (connection == null) {
                            return;
                        }
                        for (TdColumn colobj : columnList) {
                            TdColumn tdColumn = colobj;

                            try {
                                List<TdSqlDataType> newDataTypeList =
                                        getDataType(ConnectionUtils.getName(CatalogHelper.getParentCatalog(tdTable)),
                                                ConnectionUtils.getName(SchemaHelper.getParentSchema(tdTable)),
                                                tdTable.getName(), tdColumn.getName(), connection);
                                if (newDataTypeList.size() > 0) {
                                    tdColumn.setSqlDataType(newDataTypeList.get(0));
                                }
                            } catch (SQLException e) {
                                log.error(e, e);
                            }
                        }
                        ConnectionUtils.closeConnection(connection);
                    }
                    ElementWriterFactory.getInstance().createDataProviderWriter().save(tempConnection);
                }
            }
        }
    }

    public static void retrieveColumn(List<? extends MetadataTable> tdTableList) {
        if (tdTableList == null) {
            return;
        }
        for (MetadataTable currColumnSet : tdTableList) {
            retrieveColumn(currColumnSet);
        }
    }

    static String getName(ModelElement element) {
        String name = element == null ? null : element.getName();
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return name;
    }

    /**
     * DOC zshen Comment method "getPackageFilter".
     * 
     * @param connectionParam
     * @return
     * @deprecated After branch4.2 we unique the ui for the wizard which to create connection so no retrieveAll and Data
     * filter again don't needed this method. And have all kinds of filter can be use in the repository view when the
     * tree be display not fill connection.
     */
    @Deprecated
    public static List<String> getPackageFilter(DBConnectionParameter connectionParam) {
        List<String> packageFilter = null;

        if (!connectionParam.isRetrieveAllMetadata()) {
            packageFilter = new ArrayList<String>();
            String dbName = connectionParam.getDbName();
            // String otherParameter = null; // MOD scorreia 2010-10-20 bug 16562 avoid NPE
            // MOD by msjian 2011-5-16 20875: "reload table list" for postgres have some issue
            if (isOracle(connectionParam)) {
                dbName = getDbName(connectionParam);
            }
            // MOD qiongli 2011-9-14 TDQ-3317,avoid empty string
            if (dbName != null && !dbName.equals(PluginConstant.EMPTY_STRING)) {
                packageFilter.add(dbName);
            }
        }
        return packageFilter;
    }

    private static boolean isOracle(DBConnectionParameter connectionParam) {
        if (connectionParam == null) {
            return false;
        } else {
            return connectionParam.getSqlTypeName().equalsIgnoreCase(
                    SupportDBUrlType.ORACLEWITHSERVICENAMEDEFAULTURL.getDBKey())
                    || connectionParam.getSqlTypeName().equalsIgnoreCase(
                            SupportDBUrlType.ORACLEWITHSIDDEFAULTURL.getDBKey());
        }
    }

    /**
     * get the DbName from DBConnectionParameter, this value is for package filter only.
     * 
     * @param connectionParam
     * @return
     */
    private static String getDbName(DBConnectionParameter connectionParam) {
        String dbName = null;
        String filterSchema = null;
        if (connectionParam != null && connectionParam.getFilterSchema() != null) {
            filterSchema = connectionParam.getFilterSchema().toUpperCase();
        }
        if (filterSchema != null) {
            dbName = filterSchema;
        } else {
            dbName = connectionParam.getParameters().getProperty(TaggedValueHelper.USER).toUpperCase();
        }
        return dbName;
    }

    /**
     * Method "getDataType".
     * 
     * @param catalogName the catalog (can be null)
     * @param schemaPattern the schema(s) (can be null)
     * @param tablePattern the table(s)
     * @param columnPattern the column(s)
     * @param connection the connection
     * @return the list of datatypes of the given columns
     * @throws SQLException
     */
    public static List<TdSqlDataType> getDataType(String catalogName, String schemaPattern, String tablePattern,
            String columnPattern, java.sql.Connection connection) throws SQLException {
        List<TdSqlDataType> sqlDatatypes = new ArrayList<TdSqlDataType>();
        @SuppressWarnings("deprecation")
        ResultSet columns =
                org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection).getColumns(catalogName,
                        schemaPattern, tablePattern, columnPattern);
        while (columns.next()) {
            sqlDatatypes.add(createDataType(columns));
        }
        columns.close();
        return sqlDatatypes;
    }

    public static TdSqlDataType createDataType(ResultSet columns) throws SQLException {
        TdSqlDataType sqlDataType = RelationalFactory.eINSTANCE.createTdSqlDataType();
        try {
            sqlDataType.setJavaDataType(columns.getInt(GetColumn.DATA_TYPE.name()));
        } catch (Exception e1) {
            log.warn(e1, e1);
        }
        try {
            sqlDataType.setName(columns.getString(GetColumn.TYPE_NAME.name()));
        } catch (Exception e1) {
            log.warn(e1, e1);
        }
        try {
            sqlDataType.setNumericPrecision(columns.getInt(GetColumn.DECIMAL_DIGITS.name()));
            sqlDataType.setNumericPrecisionRadix(columns.getInt(GetColumn.NUM_PREC_RADIX.name()));
        } catch (Exception e) {
            log.warn(e);
        }
        return sqlDataType;
    }

    /**
     * if the connection has sid return false, else return true (don't need the TaggedValue any more).
     * 
     * @param element
     * @return
     */
    public static boolean getRetrieveAllMetadata(ModelElement element) {
        if (element != null && element instanceof Connection) {
            if (element instanceof DatabaseConnection) {
                DatabaseConnection dbConn = (DatabaseConnection) element;
                String sid = JavaSqlFactory.getSID(dbConn);
                if (sid != null && !"".equals(sid.trim())) { //$NON-NLS-1$
                    // MOD klliu bug 22900
                    TaggedValue taggedValue =
                            TaggedValueHelper.getTaggedValue(TaggedValueHelper.RETRIEVE_ALL, element.getTaggedValue());
                    // if connection is created by 4.2 or 5.0 ,the tagedValue(RETRIEVE_ALL) has been removed.
                    if (taggedValue != null) {
                        String value = taggedValue.getValue();
                        if (value.equals("true")) { //$NON-NLS-1$
                            return true;
                        }
                    }
                    // ~
                    if (ConnectionHelper.isOracle(dbConn) || isPostgresql(dbConn)) {
                        String uiSchema = dbConn.getUiSchema();
                        if (uiSchema != null && !"".equals(uiSchema.trim())) { //$NON-NLS-1$
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else if (element instanceof FileConnection) {
                // do file connection can filter catalog/schema?
                return true;
            }
        }
        return true;
    }

    /**
     * Get the original DatabaseConnection for context mode.
     * 
     * @param connection
     * @return
     */
    public static DatabaseConnection getOriginalDatabaseConnection(DatabaseConnection connection) {
        if (connection == null) {
            return null;
        }
        if (connection.isContextMode()) {
            String contextName = connection.getContextName();
            if (contextName == null) {
                return DBConnectionContextUtils.cloneOriginalValueConnection(connection, true, null);
            }
            return DBConnectionContextUtils.cloneOriginalValueConnection(connection, false, contextName);
        }
        return connection;
    }

    /**
     * Get the original FileConnection for context mode.
     * 
     * @param fileConn
     * @return
     */
    public static FileConnection getOriginalFileConnection(FileConnection fileConn) {
        if (fileConn == null) {
            return null;
        }
        if (fileConn.isContextMode()) {
            return CloneConnectionUtils.cloneOriginalValueConnection(fileConn, false);
        }
        return fileConn;
    }

    /**
     * if sqlite connection don't set username, set it with a default username.
     * 
     * @param connection
     */
    public static void checkUsernameBeforeSaveConnection4Sqlite(Connection connection) {
        if (ConnectionUtils.isSqlite(connection)) {
            String username = JavaSqlFactory.getUsername(connection);
            if (username == null || "".equals(username)) { //$NON-NLS-1$
                ConnectionHelper.setUsername(connection, JavaSqlFactory.DEFAULT_USERNAME);
            }
        }
    }

    /**
     * 
     * Get connection from data manager.
     * 
     * @param datamanager
     * @return
     */
    public static Connection getConnectionFromDatamanager(DataManager datamanager) {
        if (datamanager != null && datamanager.eIsProxy()) {
            datamanager = (DataManager) EObjectHelper.resolveObject(datamanager);
        }
        Connection analysisDataProvider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(datamanager);
        return analysisDataProvider;

    }

    /**
     * Updata DB_PRODUCT tagged values for connection item in case they are not present in current file.
     * 
     * @throws SQLException
     */
    public static synchronized void updataTaggedValueForConnectionItem(Connection dataProvider) {
        if (dataProvider instanceof DatabaseConnection
                && StringUtils.isBlank(TaggedValueHelper
                        .getValueString(TaggedValueHelper.DB_PRODUCT_NAME, dataProvider))) {
            Property property = PropertyHelper.getProperty(dataProvider);
            if (property != null) {
                Item item = property.getItem();
                if (item != null) {
                    DatabaseConnection dbConn = (DatabaseConnection) dataProvider;
                    IMetadataConnection metaConnection = ConvertionHelper.convert(dbConn);
                    dbConn =
                            (DatabaseConnection) MetadataFillFactory.getDBInstance(dataProvider).fillUIConnParams(
                                    metaConnection, dbConn);
                    if (dbConn != null && Platform.isRunning()) {
                        try {
                            ProxyRepositoryFactory.getInstance().save(item);
                        } catch (PersistenceException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * get the database product version.
     * 
     * @param connection
     * @return
     */
    public static ProductVersion getDatabaseVersion(IMetadataConnection connection) {
        ProductVersion version = null;

        Connection conn = (Connection) connection.getCurrentConnection();
        Properties props = new Properties();
        String userName = JavaSqlFactory.getUsername(conn);
        String password = JavaSqlFactory.getPassword(conn);
        props.put(TaggedValueHelper.USER, userName);
        props.put(TaggedValueHelper.PASSWORD, password);
        String dbType = connection.getDbType();
        if (dbType != null) {
            props.put(TaggedValueHelper.DBTYPE, dbType);
        }

        java.sql.Connection createConnection = null;
        try {
            List list =
                    ExtractMetaDataUtils.getInstance().connect(dbType, connection.getUrl(), userName, password,
                            connection.getDriverClass(), connection.getDriverJarPath(),
                            connection.getDbVersionString(), connection.getAdditionalParams());
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof java.sql.Connection) {
                    createConnection = (java.sql.Connection) list.get(i);
                    break;
                }
            }
            if (createConnection != null) {
                if (createConnection.getMetaData() != null) {
                    String temp = createConnection.getMetaData().getDatabaseProductVersion();
                    if (temp != null) {
                        version = ProductVersion.fromString(temp);
                    }

                    if (version == null) {
                        version =
                                ProductVersion.fromString(createConnection.getMetaData().getDatabaseMajorVersion()
                                        + "." //$NON-NLS-1$
                                        + createConnection.getMetaData().getDatabaseMinorVersion() + ".0"); //$NON-NLS-1$
                    }
                }
            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (createConnection != null) {
                try {
                    createConnection.close();
                } catch (SQLException e) {
                    ExceptionHandler.process(e);
                }
            }
        }

        if (version == null) {
            version = ProductVersion.fromString("0.0.0"); //$NON-NLS-1$
        }

        return version;
    }

    /**
     * get the hadoop cluster ID .
     * 
     * @param viewObject
     * @return
     */
    public static String getHadoopClusterIDOfHive(IRepositoryViewObject viewObject) {
        DatabaseConnectionItem dbItem = (DatabaseConnectionItem) viewObject.getProperty().getItem();
        DatabaseConnection dbConnection = (DatabaseConnection) dbItem.getConnection();
        return dbConnection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
    }

    /**
     * find driver jar path from 'temp\dbWizard',if nof found,find it from 'lib\java' and "librariesIndex.xml".
     * 
     * @return
     * @throws MalformedURLException
     */
    public static LinkedList<String> getDriverJarRealPaths(List<String> driverJarNameList) throws MalformedURLException {
        LinkedList<String> linkedList = new LinkedList<String>();
        // no check the real jar path for None-Platform-Running
        if (!Platform.isRunning()) {
            return linkedList;
        }

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
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
                ILibraryManagerService libManagerServic =
                        (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                                ILibraryManagerService.class);
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

    /**
     * judge if the Connection is a TCOMP JDBC connection
     * 
     * @param conn
     * @return
     */
    public static boolean isTcompJdbc(Connection conn) {
        if (conn instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) conn;
            return isTcompJdbc(dbConn.getDatabaseType());
        }
        return false;
    }

    /**
     * judge if it is a TCOMP JDBC connection according to data type String
     * 
     * @param dbType
     * @return
     */
    public static boolean isTcompJdbc(String dbType) {
        if (!Platform.isRunning()) {
            return "JDBC".equals(dbType); //$NON-NLS-1$
        }
        List<ERepositoryObjectType> extraTypes = new ArrayList<ERepositoryObjectType>();
        IGenericDBService dbService = getGenericDBService();
        if (dbService != null) {
            extraTypes.addAll(dbService.getExtraTypes());
        }
        for (ERepositoryObjectType type : extraTypes) {
            if (type.getType().equals(dbType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @return
     */
    public static IGenericDBService getGenericDBService() {
        IGenericDBService dbService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(IGenericDBService.class);
        }
        return dbService;
    }
}
