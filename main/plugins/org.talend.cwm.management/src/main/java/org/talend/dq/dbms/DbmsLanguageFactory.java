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
package org.talend.dq.dbms;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlStore;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.utils.ProductVersion;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.SoftwareSystem;

/**
 * @author scorreia
 * 
 * Factory for the creation of DbmsLanguage objects.
 */
public final class DbmsLanguageFactory {

    private static Logger log = Logger.getLogger(DbmsLanguageFactory.class);

    private DbmsLanguageFactory() {
        // avoid instantiation
    }

    /**
     * Method "createDbmsLanguage".
     * 
     * @param dataManager a data manager used for initializing the correct language in the created DbmsLanguage
     * @return a new DbmsLanguage even if the data manager did not allow to get the correct language
     */
    public static DbmsLanguage createDbmsLanguage(DataManager dataManager, ExecutionLanguage... exeLanguage) {
        if (exeLanguage.length > 0 && exeLanguage[0] == ExecutionLanguage.JAVA) {
            return createDbmsLanguage(SupportDBUrlType.JAVADEFAULTURL);
        }
        DbmsLanguage dbmsLanguage = new DbmsLanguage();
        if (dataManager == null) {
            return dbmsLanguage;
        }
        Connection dataprovider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(dataManager);
        if (dataprovider == null) {
            return dbmsLanguage;
        }

        // MOD sizhaoliu TDQ-6316 deprecate software system
        // TdSoftwareSystem softwareSystem = SoftwareSystemManager.getInstance().getSoftwareSystem(dataprovider);
        // MOD qiongli 2011-1-11 feature 16796.handle the delimited file
        boolean isDelimitedFile = ConnectionUtils.isDelimitedFileConnection(dataprovider);
        if (isDelimitedFile) {
            dbmsLanguage = createDbmsLanguage(DbmsLanguage.DELIMITEDFILE, PluginConstant.EMPTY_STRING);
        } else if (dataprovider != null) {
            String productSubtype = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_NAME, dataprovider);
            // Added 20130222 TDQ-6760 yyin, if the tag value is null, update it
            // All analysis & reports will go here, so check it here is better than other place.
            if (StringUtils.isBlank(productSubtype)) {
                ConnectionUtils.updataTaggedValueForConnectionItem(dataprovider);
                productSubtype = TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_NAME, dataprovider);
            }
            // ~
            String productVersion =
                    TaggedValueHelper.getValueString(TaggedValueHelper.DB_PRODUCT_VERSION, dataprovider);

            if (log.isDebugEnabled()) {
                log.debug("Software system subtype (Database type): " + productSubtype); //$NON-NLS-1$
            }
            if (StringUtils.isNotBlank(productSubtype)) {
                dbmsLanguage = createDbmsLanguage(productSubtype, productVersion);
            }
        }
        String identifierQuoteString = ConnectionHelper.getIdentifierQuoteString(dataprovider);
        if (identifierQuoteString == null || identifierQuoteString.length() == 0) {
            // MOD scorreia 2009-11-24 check for null because in some cases (DB2 z/OS and TOP 3.2.2), the identifier
            // quote was stored as null.
            // given data provider has not stored the identifier quote (version 1.1.0 of TOP)
            // we must set it by hand
            identifierQuoteString = dbmsLanguage.getHardCodedQuoteIdentifier();
        }
        dbmsLanguage.setDbQuoteString(identifierQuoteString);
        return dbmsLanguage;
    }

    /**
     * Method "createDbmsLanguage".
     * 
     * @param dbmsSubtype
     * @return the appropriate DbmsLanguage
     */
    public static DbmsLanguage createDbmsLanguage(String dbmsSubtype, String databaseVersion) {
        // TODO find an appropriate place to set the database version (e.g for hive embedded)
        String dbVersionStr = databaseVersion;
        if (StringUtils.isEmpty(dbVersionStr)) {
            dbVersionStr = "0.0.0"; //$NON-NLS-1$
        }
        ProductVersion dbVersion = ProductVersion.fromString(dbVersionStr, true);
        DbmsLanguage dbmsLanguage = null;
        if (isMySQL(dbmsSubtype)) {
            if (dbVersion.getMajor() >= 10) {
                dbmsLanguage = new MariaDBDbmsLanguage(dbmsSubtype, dbVersion);
            } else {
                dbmsLanguage = new MySQLDbmsLanguage(dbmsSubtype, dbVersion);
            }
        } else if (isOracle(dbmsSubtype)) {
            dbmsLanguage = new OracleDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isDB2(dbmsSubtype)) {
            dbmsLanguage = new DB2DbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isAS400(dbmsSubtype)) {
            dbmsLanguage = new AS400DbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isMSSQL(dbmsSubtype)) {
            dbmsLanguage = new MSSqlDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isPostgresql(dbmsSubtype)) {
            dbmsLanguage = new PostgresqlDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isRedshift(dbmsSubtype)) {
            dbmsLanguage = new RedshiftDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isExasol(dbmsSubtype)) {
            dbmsLanguage = new ExasolDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isSybase(dbmsSubtype)) {
            dbmsLanguage = new SybaseASEDbmsLanguage(dbVersion);
        } else if (isSQLite(dbmsSubtype)) {
            dbmsLanguage = new SQLiteDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isTeradata(dbmsSubtype)) {
            dbmsLanguage = new TeradataDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isIngres(dbmsSubtype)) {
            dbmsLanguage = new IngresDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isDelimitedFile(dbmsSubtype)) {
            dbmsLanguage = new DelimitedFileLanguage(dbmsSubtype, dbVersion);
        } else if (isInfomix(dbmsSubtype)) {
            // MOD zshen fixed bug 11005: SQL syntax error for all analysis on Informix databases in Talend Open
            // Profiler
            dbmsLanguage = new InfomixDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isHive(dbmsSubtype) || isImpala(dbmsSubtype)) {
            dbmsLanguage = new HiveDbmsLanguage(DbmsLanguage.HIVE, dbVersion);
        } else if (isVertica(dbmsSubtype)) {
            dbmsLanguage = new VerticaDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isNetezza(dbmsSubtype)) {
            dbmsLanguage = new NetezzaDbmsLanguage(dbmsSubtype, dbVersion);
        } else if (isBigQuery(dbmsSubtype)) {
            dbmsLanguage = new BigQueryDbmsLanguage(dbmsSubtype, dbVersion);
        } else {
            dbmsLanguage = new DbmsLanguage(dbmsSubtype, dbVersion);
        }
        if (PluginConstant.EMPTY_STRING.equals(dbmsLanguage.getDbQuoteString())) {
            dbmsLanguage.setDbQuoteString(dbmsLanguage.getHardCodedQuoteIdentifier());
        }
        return dbmsLanguage;
    }

    /**
     * DOC talend Comment method "isImpala".
     * 
     * @param dbmsSubtype
     * @return
     */
    private static boolean isImpala(String dbmsSubtype) {
        return DbmsLanguage.IMPALA.equalsIgnoreCase(dbmsSubtype);
    }

    public static DbmsLanguage createDbmsLanguage(String dataType) {
        SupportDBUrlType dbType = SupportDBUrlStore.getInstance().getDBUrlType(dataType);
        return createDbmsLanguage(dbType);
    }

    /**
     * DOC jet Comment method "getDbmsLanguage".
     * 
     * @param dbType
     * @return
     */
    public static DbmsLanguage createDbmsLanguage(SupportDBUrlType dbType) {

        DbmsLanguage result = null;

        if (dbType == null) {
            return new DbmsLanguage();
        }
        // MOD qiongli 2011-4-18 bug 16723,data cleansing
        result = createDbmsLanguage(dbType.getLanguage(), PluginConstant.EMPTY_STRING);
        return result;
    }

    /**
     * Method "createDbmsLanguage".
     * 
     * @param connection a connection (must be open)
     * @return the appropriate DbmsLanguage or a default one if something failed with the connection.
     */
    @SuppressWarnings("deprecation")
    public static DbmsLanguage createDbmsLanguage(java.sql.Connection connection) {
        assert connection != null;
        // MOD xqliu 2009-07-13 bug 7888
        String databaseProductName = null;
        try {
            databaseProductName =
                    org.talend.utils.sql.ConnectionUtils.getConnectionMetadata(connection).getDatabaseProductName();
            databaseProductName = databaseProductName == null ? PluginConstant.EMPTY_STRING : databaseProductName;
            String databaseProductVersion = null;
            try {
                databaseProductVersion = org.talend.utils.sql.ConnectionUtils
                        .getConnectionMetadata(connection)
                        .getDatabaseProductVersion();
                databaseProductVersion = databaseProductVersion == null ? "0" : databaseProductVersion; //$NON-NLS-1$
            } catch (Exception e) {
                log.warn(Messages.getString("DbmsLanguageFactory.RetrieveVerSionException", databaseProductName), e);//$NON-NLS-1$
            }
            DbmsLanguage dbmsLanguage = createDbmsLanguage(databaseProductName, databaseProductVersion);
            dbmsLanguage
                    .setDbQuoteString(org.talend.utils.sql.ConnectionUtils
                            .getConnectionMetadata(connection)
                            .getIdentifierQuoteString());
            return dbmsLanguage;
        } catch (SQLException e) {
            log.warn(Messages.getString("DbmsLanguageFactory.RetrieveInfoException", e), e);//$NON-NLS-1$
            return new DbmsLanguage();
        }
    }

    private static boolean isMySQL(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.MYSQL, dbms);
    }

    public static boolean isOracle(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.ORACLE, dbms);
    }

    private static boolean isPostgresql(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.POSTGRESQL, dbms);
    }

    private static boolean isMSSQL(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.MSSQL, dbms);
    }

    private static boolean isDB2(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.DB2, dbms);
    }

    private static boolean isAS400(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.AS400, dbms);
    }

    private static boolean isSybase(String dbms) {
        return ConnectionUtils.isSybaseeDBProducts(dbms);
    }

    private static boolean isSQLite(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.SQLITE3, dbms);
    }

    private static boolean isTeradata(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.TERADATA, dbms);
    }

    private static boolean isIngres(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.INGRES, dbms);
    }

    private static boolean isDelimitedFile(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.DELIMITEDFILE, dbms);
    }

    // MOD zshen 11005: SQL syntax error for all analysis on Informix databases in Talend Open Profiler
    public static boolean isInfomix(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.INFOMIX, dbms);
    }

    public static boolean isHive(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.HIVE, dbms);
    }

    public static boolean isVertica(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.VERTICA, dbms);
    }

    public static boolean isRedshift(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.REDSHIFT, dbms);
    }

    public static boolean isExasol(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.EXASOLUTION, dbms);
    }

    private static boolean isNetezza(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.NETEZZA, dbms);
    }

    private static boolean isBigQuery(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.BIGQUERY, dbms);
    }

    public static boolean isAllDatabaseType(String dbms) {
        return compareDbmsLanguage(DbmsLanguage.SQL, dbms);
    }

    /**
     * if lang1 equals lang2 (ignore case) return true, else return false.
     * 
     * @param lang1
     * @param lang2
     * @return
     */
    public static boolean equalsDbmsLanguage(String lang1, String lang2) {
        if (lang1 == null || lang2 == null) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(lang1, lang2);
    }

    public static boolean compareDbmsLanguage(String lang1, String lang2) {
        if (lang1 == null || lang2 == null) {
            return false;
        }
        // When source language is defaule or target language is default they are must equals between lang1 and lang2,
        // else will cause of error mapping.
        // For example default will mapping MySQL, PostgreSQL,Microsoft SQL Server
        if (StringUtils.equalsIgnoreCase(lang1, DbmsLanguage.SQL)
                || StringUtils.equalsIgnoreCase(lang2, DbmsLanguage.SQL)) {
            return StringUtils.equalsIgnoreCase(lang1, lang2);
        }
        // MOD xqliu 2011-12-20 TDQ-4232, FOR AS400
        if (StringUtils.contains(lang1, DbmsLanguage.AS400) && StringUtils.contains(lang2, DbmsLanguage.AS400)) {
            return true;
        }
        if (StringUtils.contains(lang1, DbmsLanguage.AS400) && !StringUtils.contains(lang2, DbmsLanguage.AS400)) {
            return false;
        }
        if (!StringUtils.contains(lang1, DbmsLanguage.AS400) && StringUtils.contains(lang2, DbmsLanguage.AS400)) {
            return false;
        }
        // ~ TDQ-4232
        // MOD mzhao 2010-08-02 bug 14464, for AS400
        if (StringUtils.contains(lang1, DbmsLanguage.AS400)
                && StringUtils.contains(StringUtils.upperCase(lang2), lang1)) {
            return true;
        }
        // MOD 2008-08-04 scorreia: for DB2 database, dbName can be "DB2/NT" or "DB2/6000" or "DB2"...
        if (lang1.startsWith(DbmsLanguage.DB2)) {
            if (StringUtils.contains(lang2, DbmsLanguage.AS400)) {
                return false;
            }
            return StringUtils.upperCase(lang1).startsWith(StringUtils.upperCase(lang2))
                    || StringUtils.upperCase(lang2).startsWith(StringUtils.upperCase(lang1));
        } else
        // MOD 2010-01-26 zshen: for informix database, dbName can be "informix" or "informix Dynamic Server"
        if (lang1.startsWith(DbmsLanguage.INFOMIX)) {
            return StringUtils.upperCase(lang1).startsWith(StringUtils.upperCase(lang2))
                    || StringUtils.upperCase(lang2).startsWith(StringUtils.upperCase(lang1));
        }
        if (StringUtils.contains(lang1, DbmsLanguage.VERTICA) && StringUtils.contains(lang2, DbmsLanguage.VERTICA)) {
            return true;
        }
        // MOD 2014-02-10 for TDQ-8600 Column Analysis fails with Netezza
        if (StringUtils.contains(lang1, DbmsLanguage.NETEZZA) && StringUtils.contains(lang2, DbmsLanguage.NETEZZA)) {
            return true;
        }
        // MOD 2014-02-27 for TDQ-8601
        if (StringUtils.contains(lang1, DbmsLanguage.HIVE) && StringUtils.contains(lang2, DbmsLanguage.HIVE)) {
            return true;
        }

        // ADD msjian TDQ-16020: support redshift-sso
        if (StringUtils.contains(lang1, DbmsLanguage.REDSHIFT) && StringUtils.contains(lang2, DbmsLanguage.REDSHIFT)) {
            return true;
        }

        return StringUtils.contains(StringUtils.upperCase(lang1), StringUtils.upperCase(lang2))
                || StringUtils.contains(StringUtils.upperCase(lang2), StringUtils.upperCase(lang1));
    }

    /**
     * Method "createDbmsLanguage".
     * 
     * @param analysis
     * @return the dbms language associated to the connection of the given analysis or a default one.
     */
    public static DbmsLanguage createDbmsLanguage(Analysis analysis, ExecutionLanguage... exeLanguage) {
        final AnalysisContext context = analysis.getContext();
        ExecutionLanguage theLanguage = null;
        if (exeLanguage != null && exeLanguage.length > 0) {
            theLanguage = exeLanguage[0];
        } else {
            theLanguage = analysis.getParameters().getExecutionLanguage();
        }
        if (theLanguage == ExecutionLanguage.JAVA) {
            return createDbmsLanguage(SupportDBUrlType.JAVADEFAULTURL);
        }
        if (context != null) {
            final DataManager dm = context.getConnection();
            if (dm != null) {
                return createDbmsLanguage(dm);
            }
        }
        return new DbmsLanguage();
    }

    @Deprecated
    public static DbmsLanguage createDbmsLanguage(SoftwareSystem softwareSystem) {
        if (softwareSystem != null) {
            return createDbmsLanguage(softwareSystem.getName(), softwareSystem.getVersion());
        }

        return new DbmsLanguage();
    }
}
