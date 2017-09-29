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
package org.talend.dataprofiler.core.pattern;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.management.api.SoftwareSystemManager;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * DOC zqin class global comment. Detailled comment
 */
public enum PatternLanguageType {

    Default(0, DefaultMessagesImpl.getString("PatternLanguageType.Default"), //$NON-NLS-1$
            "SQL", //$NON-NLS-1$
            PatternToExcelEnum.AllDBRegexp),
    MYSQL(
          1,
          SupportDBUrlType.MYSQLDEFAULTURL.getLanguage(),
          SupportDBUrlType.MYSQLDEFAULTURL.getLanguage(),
          PatternToExcelEnum.MySQLRegexp),
    ORACLE(
           2,
           SupportDBUrlType.ORACLEWITHSIDDEFAULTURL.getLanguage(),
           SupportDBUrlType.ORACLEWITHSIDDEFAULTURL.getLanguage(),
           PatternToExcelEnum.OracleRegexp),
    SQLSERVER(
              3,
              SupportDBUrlType.MSSQLDEFAULTURL.getLanguage(),
              SupportDBUrlType.MSSQLDEFAULTURL.getLanguage(),
              PatternToExcelEnum.SQLServerRegexp),
    DB2(
        4,
        SupportDBUrlType.DB2DEFAULTURL.getLanguage(),
        SupportDBUrlType.DB2DEFAULTURL.getLanguage(),
        PatternToExcelEnum.DB2Regexp),
    POSTGRESQL(
               5,
               SupportDBUrlType.POSTGRESQLEFAULTURL.getLanguage(),
               SupportDBUrlType.POSTGRESQLEFAULTURL.getLanguage(),
               PatternToExcelEnum.PostgreSQLRegexp),
    SYBASE(
           6,
           SupportDBUrlType.SYBASEDEFAULTURL.getLanguage(),
           SupportDBUrlType.SYBASEDEFAULTURL.getLanguage(),
           PatternToExcelEnum.SybaseRegexp),
    INGRES(
           7,
           SupportDBUrlType.INGRESDEFAULTURL.getLanguage(),
           SupportDBUrlType.INGRESDEFAULTURL.getLanguage(),
           PatternToExcelEnum.IngresRegexp),
    INFORMIX(
             8,
             SupportDBUrlType.INFORMIXDEFAULTURL.getLanguage(),
             SupportDBUrlType.INFORMIXDEFAULTURL.getLanguage(),
             PatternToExcelEnum.InformixRegexp),
    // MOD by zshen, this will effect for the indicatorDefinition so add new method to return differents result.
    NETEZZA(
            15,
            SupportDBUrlType.NETEZZADEFAULTURL.getLanguage(),
            SupportDBUrlType.NETEZZADEFAULTURL.getLanguage(),
            PatternToExcelEnum.NETEZZARegexp),

    // ADD by msjian 2011-7-20 22517: no such function: CHAR_LENGTH for SQLite
    SQLITE3(
            9,
            SupportDBUrlType.SQLITE3DEFAULTURL.getLanguage(),
            SupportDBUrlType.SQLITE3DEFAULTURL.getLanguage(),
            PatternToExcelEnum.SQLite3Regexp),
    // MOD scorreia 2008-12-16 removed "generic jdbc" because its meaning is the same as ALL_DATABASE_TYPE
    TERADATA(
             10,
             SupportDBUrlType.TERADATADEFAULTURL.getLanguage(),
             SupportDBUrlType.TERADATADEFAULTURL.getLanguage(),
             PatternToExcelEnum.Teradata),
    JAVA(
         11,
         SupportDBUrlType.JAVADEFAULTURL.getLanguage(),
         SupportDBUrlType.JAVADEFAULTURL.getLanguage(),
         PatternToExcelEnum.JavaRegexp),
    ACCESS(12, SupportDBUrlType.ACCESS.getLanguage(), SupportDBUrlType.ACCESS.getLanguage(), PatternToExcelEnum.Access),
    AS400(
          12,
          SupportDBUrlType.AS400DEFAULTURL.getLanguage(),
          SupportDBUrlType.AS400DEFAULTURL.getLanguage(),
          PatternToExcelEnum.AS400),
    HIVE(13, SupportDBUrlType.HIVEDEFAULTURL.getLanguage() + " | " + SupportDBUrlType.IMPALA.getLanguage(), //$NON-NLS-1$
         SupportDBUrlType.HIVEDEFAULTURL.getLanguage(),
         PatternToExcelEnum.Hive),

    VERTICA(13, SupportDBUrlType.VERTICA.getLanguage(), SupportDBUrlType.VERTICA.getLanguage(), PatternToExcelEnum.Vertica),
    REDSHIFT(14, SupportDBUrlType.REDSHIFT.getDBKey(), SupportDBUrlType.REDSHIFT.getLanguage(), PatternToExcelEnum.REDSHIFT),
    EXASOL(15, SupportDBUrlType.EXASOL.getDBKey(), SupportDBUrlType.EXASOL.getLanguage(), PatternToExcelEnum.EXASOL);

    private int index;

    private String language;

    private String name;

    private PatternToExcelEnum excelEnum;

    public PatternToExcelEnum getExcelEnum() {
        return excelEnum;
    }

    public int getIndex() {
        return index;
    }

    public String getLiteral() {
        return language;
    }

    public String getName() {
        return name;
    }

    PatternLanguageType(int index, String name, String language, PatternToExcelEnum excelEnum) {
        this.index = index;
        this.name = name;
        this.language = language;
        this.excelEnum = excelEnum;
    }

    public static String findLanguageByName(String name) {
        if (name.equalsIgnoreCase(SupportDBUrlType.REDSHIFT.getDBKey())) {
            return SupportDBUrlType.REDSHIFT.getLanguage();
        }
        if (name.equalsIgnoreCase(SupportDBUrlType.EXASOL.getDBKey())) {
            return SupportDBUrlType.EXASOL.getLanguage();
        }
        for (PatternLanguageType oneType : values()) {
            // we should consider the name like "Hive | Impala"
            if (name != null && StringUtils.startsWithIgnoreCase(name, oneType.getName())) {
                return oneType.getLiteral();
            }
        }
        // When the type is not supported officially, return the database name.
        return name;
    }

    public static String findNameByLanguage(String language) {
        if (language.equalsIgnoreCase(SupportDBUrlType.REDSHIFT.getLanguage())) {
            return SupportDBUrlType.REDSHIFT.getDBKey();
        }
        if (language.equalsIgnoreCase(SupportDBUrlType.EXASOL.getLanguage())) {
            return SupportDBUrlType.EXASOL.getDBKey();
        }
        for (PatternLanguageType oneType : values()) {
            if (language != null && StringUtils.equalsIgnoreCase(language, oneType.getLiteral())) {
                return oneType.getName();
            }
        }
        // When the type is not supported officially, return the database name.
        return language;
    }

    public static String[] getAllLanguageTypes() {
        Set<String> existingTypes = new HashSet<String>();

        for (PatternLanguageType oneType : values()) {
            existingTypes.add(oneType.getName());
        }
        // Get the new database types which is not defined offically.
        List<String> newTypes = SoftwareSystemManager.getInstance().getNewDBTypesFromSoftwareSystem(existingTypes);
        existingTypes.addAll(newTypes);
        return existingTypes.toArray(new String[existingTypes.size()]);
    }

    /**
     * DOC yyi 2009-09-28 Feature: 9289.
     * 
     * @param hasJava set contains Java or not.
     * @return
     */
    public static String[] getAllLanguageTypes(boolean hasJava) {
        Set<String> set = new HashSet<String>();

        for (PatternLanguageType oneType : values()) {
            set.add(oneType.getName());
        }

        if (!hasJava) {
            set.remove(SupportDBUrlType.JAVADEFAULTURL.getLanguage());
        }

        return set.toArray(new String[set.size()]);
    }

    /**
     * DOC zshen 2011-06-29 bug: 22668.
     * 
     * @param
     * @return
     */
    public static String[] getAllLanguageTypesForPattern() {
        Set<String> existingTypes = new HashSet<String>();

        for (PatternLanguageType oneType : values()) {
            existingTypes.add(oneType.getName());
        }

        // Get the new database types which is not defined offically.
        List<String> newTypes = SoftwareSystemManager.getInstance().getNewDBTypesFromSoftwareSystem(existingTypes);
        existingTypes.addAll(newTypes);

        return existingTypes.toArray(new String[existingTypes.size()]);
    }

}
