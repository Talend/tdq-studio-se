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

package net.sourceforge.sqlexplorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.sourceforge.sqlexplorer.dbproduct.DriverManager;
import net.sourceforge.sqlexplorer.dbproduct.ManagedDriver;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 *
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 *
 */
public enum EDriverName {
    ODBCDEFAULTURL("Generic ODBC", //$NON-NLS-1$
                   "sun.jdbc.odbc.JdbcOdbcDriver", //$NON-NLS-1$
                   "-1"), //$NON-NLS-1$
    MYSQLDEFAULTURL("Mysql", //$NON-NLS-1$
                    "org.gjt.mm.mysql.Driver", //$NON-NLS-1$
                    "-6",
                    "mysql-connector-java-5.1.22-bin.jar"),
    HSQLDEFAULTURL("HSql", //$NON-NLS-1$
                   "org.hsqldb.jdbcDriver", //$NON-NLS-1$
                   "-18",
                   "hsqldb.jar"),
    ORACLEDEFAULTURL("Oracle", //$NON-NLS-1$
                     // MOD scorreia 2008-08-22: oracle.jdbc.driver package is not
                     // supported anymore: replaced
                     "oracle.jdbc.OracleDriver", //$NON-NLS-1$
                     "-4",
                     "ojdbc14.jar"),
    MSSQLDEFAULTURL("MSSQL", //$NON-NLS-1$
                    "net.sourceforge.jtds.jdbc.Driver", //$NON-NLS-1$
                    "-11", "jtds-1.2.5.jar"), //$NON-NLS-1$
    MSSQL2008URL("MSSQL2008", //$NON-NLS-1$
                 "com.microsoft.sqlserver.jdbc.SQLServerDriver", //$NON-NLS-1$
                 "-52", isAboveJDK15() ? "sqljdbc4.jar" : "sqljdbc.jar"), //$NON-NLS-1$
    DB2DEFAULTURL("DB2", //$NON-NLS-1$
                  "com.ibm.db2.jcc.DB2Driver", //$NON-NLS-1$
                  "-24",
                  "db2jcc_license_cu.jar",
                  "db2jcc_license_cisuz.jar",
                  "db2jcc.jar"),
    DB2ZOSDEFAULTURL("DB2", //$NON-NLS-1$
                     "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver", //$NON-NLS-1$
                     "-42",
                     "db2jcc_license_cu.jar",
                     "db2jcc_license_cisuz.jar",
                     "db2jcc.jar"),
    POSTGRESQLEFAULTURL("PostgreSQL", //$NON-NLS-1$
                        "org.postgresql.Driver", //$NON-NLS-1$
                        "-7",
                        "postgresql-8.1-405.jdbc3.jar"),
    INTERBASEDEFAULTURL("Interbase", //$NON-NLS-1$
                        "interbase.interclient.Driver", //$NON-NLS-1$
                        "-3",
                        "interclient.jar"),
    SYBASEDEFAULTURL("Sybase", //$NON-NLS-1$
                     "com.sybase.jdbc3.jdbc.SybDriver", //$NON-NLS-1$
                     "-9",
                     "jconn3.jar"),
    INFORMIXDEFAULTURL("Informix", //$NON-NLS-1$
                       "com.informix.jdbc.IfxDriver", //$NON-NLS-1$
                       "-26",
                       "ifxjdbc.jar"), //$NON-NLS-1$
    FIREBIRDDEFAULTURL("FireBird", //$NON-NLS-1$
                       "org.firebirdsql.jdbc.FBDriver", //$NON-NLS-1$
                       "-25",
                       "jaybird-2.1.1.jar"),
    TERADATADEFAULTURL("Teradata",
    // MOD klliu 2010-06-04 bug 12819: upgrade jdbc driver class used in sql explorer
                       "com.teradata.jdbc.TeraDriver",
                       "-50",
                       "terajdbc4.jar",
                       "tdgssconfig.jar"),
    SQLITE3DEFAULTURL("SQLite", "org.sqlite.JDBC", "-30", "sqlitejdbc-v056.jar"),
    AS400DEFAULTURL("AS400", "com.ibm.as400.access.AS400JDBCDriver", "-51", "jt400_V5R3.jar"),
    // MOD klliu bug 14791 add ingres database url and modify default_driver.xml
    INGRESDEFAULTURL("Ingres", "ca.ingres.jdbc.IngresDriver", "-88", "iijdbc.jar"),
    NETEZZADEFAULTURL("Netezza", "org.netezza.Driver", "-66", "nzjdbc.jar"), //$NON-NLS-1$
    VERTICA("Vertica", "com.vertica.Driver", "-70", "vertica_4.1.14_jdk_5.jar"), //$NON-NLS-1$
    VERTICA2("Vertica", "com.vertica.jdbc.Driver", "-71", "vertica-jdk5-6.0.0-0.jar"), //$NON-NLS-1$
    HIVE("Hive", //$NON-NLS-1$
         "org.apache.hadoop.hive.jdbc.HiveDriver", //$NON-NLS-1$
         "-55", //$NON-NLS-1$
         "hive-jdbc-0.9.0.jar", //$NON-NLS-1$
         "hive-metastore-0.9.0.jar", //$NON-NLS-1$
         "hive-exec-0.9.0.jar", //$NON-NLS-1$
         "hive-service-0.9.0.jar", //$NON-NLS-1$
         "libfb303_new.jar", //$NON-NLS-1$
         "hadoop-core-1.0.0.jar", //$NON-NLS-1$
         "commons-logging-1.0.4.jar", "log4j-1.2.15.jar", "slf4j-api-1.6.1.jar", "slf4j-log4j12-1.6.1.jar") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    ;

    private final String dbKey;

    private final String dbDriver;

    private String sqlEid;

    private String[] jars;

    private static ILibraryManagerService libManagerServic = null;
    // MOD gdbu 2011-4-20 bug : 18975
    private static Map<String, ArrayList<String>> special_database = new HashMap<String, ArrayList<String>>();
    static {
        ArrayList spDB = new ArrayList();
        spDB.add("com.mysql.jdbc.Driver");//$NON-NLS-1$
        special_database.put("org.gjt.mm.mysql.Driver", spDB);//$NON-NLS-1$

        // MOD gdbu 2011-6-9 bug : 21854
        ArrayList oracleJDBCDB = new ArrayList();
        oracleJDBCDB.add("oracle.jdbc.driver.OracleDriver");//$NON-NLS-1$
        special_database.put("oracle.jdbc.OracleDriver", oracleJDBCDB);//$NON-NLS-1$
        // ~ 21854

        // According to the above example we can continue to fill special driverclass name...
    }

    // ~18975

    EDriverName(String dbKey, String dbDriver, String sqlEid, String... jars) {
        this.dbKey = dbKey;
        this.dbDriver = dbDriver;
        this.sqlEid = sqlEid;
        this.jars = jars;
    }

    public String getDBKey() {
        return dbKey;
    }

    /**
     * Getter for dbDriver.
     *
     * @return the dbDriver
     */
    public String getDbDriver() {
        return dbDriver;
    }

    /**
     * Getter for sqlEid.
     *
     * @return the sqlEid
     */
    public String getSqlEid() {
        return this.sqlEid;
    }

    /**
     * DOC qzhang Comment method "getJars".
     *
     * @return
     */
    public LinkedList<String> getJars() {
        // find jdbc jar path from 'lib/java'.if not found,find it from "librariesIndex.xml"
        LinkedList<String> linkedList = new LinkedList<String>();
        if (this.getLibManagerServic() != null) {
            boolean jarNotFound = false;
            for (String jarName : jars) {
                String libPath = libManagerServic.getJarPath(jarName);
                if (libPath == null) {
                    jarNotFound = true;
                    break;
                }
                linkedList.add(libPath);
            }
            if (jarNotFound) {
                linkedList.clear();
            }
        }

        return linkedList;
    }

    public static Boolean isAboveJDK15() {
        Float curVersion = Float.parseFloat(StringUtils.substringBeforeLast(System.getProperty("java.version"), "."));
        return NumberUtils.compare(curVersion, 1.5) > 0;
    }

    /**
     * DOC qzhang Comment method "getId".
     *
     * @param string
     */
    public static String getId(String driver) {
        String id = ODBCDEFAULTURL.sqlEid;
        for (EDriverName driverName : values()) {
            if (driverName.dbDriver.equalsIgnoreCase(driver)) {
                return driverName.sqlEid;
            }
        }

        // MOD gdbu 2011-4-20 bug : 18975
        String specID = getDriFromSpecialDB(driver);
        if (!specID.equals("")) {
            return specID;
        }
        // ~18975

        return id;
    }

    /**
     * MOD gdbu 2011-4-20 bug : 18975
     *
     * DOC gdbu Comment method "getDriFromSpecialDB".
     *
     * If the above method : getId() can not return required driverclass, we can return by this method requires the
     * driverclass, but only if we have to fill specialdatabase instance
     *
     * @param driver
     * @return driverID
     */
    private static String getDriFromSpecialDB(String driver) {
        if (driver != null) {
            Set<String> kss = special_database.keySet();
            for (String ks : kss) {
                ArrayList<String> specDb = special_database.get(ks);
                for (int i = 0; i < specDb.size(); i++) {
                    if (specDb.get(i).trim().equals(driver.trim())) {
                        return getId(ks);
                    }
                }
            }
        }

        // Added yin 20121018 TDQ-6167
        // find the driver from the added (unsupported) database
        DriverManager driverModel = SQLExplorerPlugin.getDefault().getDriverModel();
        for (ManagedDriver mdriver : driverModel.getDrivers()) {
            if (driver.equalsIgnoreCase(mdriver.getDriverClassName())) {
                return mdriver.getId();
            }
        }// ~

        return "";
    }


    private ILibraryManagerService getLibManagerServic() {
        if (libManagerServic == null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
                libManagerServic = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerService.class);
            }
        }
        return libManagerServic;
    }

}
