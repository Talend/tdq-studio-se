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

/**
 * DOC zqin class global comment. Detailled comment
 */
public enum PatternToExcelEnum {

    Label(0, "Label"), //$NON-NLS-1$
    Purpose(1, "Purpose"), //$NON-NLS-1$
    Description(2, "Description"), //$NON-NLS-1$
    Author(3, "Author"), //$NON-NLS-1$
    RelativePath(4, "Relative_Path"), //$NON-NLS-1$
    AllDBRegexp(5, "All_DB_Regexp"), //$NON-NLS-1$
    DB2Regexp(6, "DB2_Regexp"), //$NON-NLS-1$
    MySQLRegexp(7, "MySQL_Regexp"), //$NON-NLS-1$
    OracleRegexp(8, "Oracle_Regexp"), //$NON-NLS-1$
    PostgreSQLRegexp(9, "PostgreSQL_Regexp"), //$NON-NLS-1$
    SQLServerRegexp(10, "SQL_Server_Regexp"), //$NON-NLS-1$
    SybaseRegexp(11, "Sybase_Regexp"), //$NON-NLS-1$
    IngresRegexp(12, "Ingres_Regexp"), //$NON-NLS-1$
    InformixRegexp(13, "Informix_Regexp"), //$NON-NLS-1$
    NETEZZARegexp(25, "NETEZZA_Regexp"), //$NON-NLS-1$
    SQLite3Regexp(14, "SQLite3_Regexp"), //$NON-NLS-1$
    Teradata(15, "Teradata_Regexp"), //$NON-NLS-1$
    JavaRegexp(16, "Java_Regexp"), //$NON-NLS-1$
    Category(17, "Category"), //$NON-NLS-1$
    Access(18, "Access"), //$NON-NLS-1$
    AS400(19, "AS400"), //$NON-NLS-1$
    JavaClassName(20, "CLASS_NAME_TEXT"), //$NON-NLS-1$
    JavaJarPath(21, "JAR_FILE_PATH"), //$NON-NLS-1$
    Hive(22, "Hive"), //$NON-NLS-1$
    Vertica(23, "Vertica"), //$NON-NLS-1$
    IndicatorDefinitionParameter(23, "IndicatorDefinitionParameter"), //$NON-NLS-1$
    REDSHIFT(24, "RedShift"), //$NON-NLS-1$
    EXASOL(25, "Exasol"); //$NON-NLS-1$

    private String literal;

    private int index;

    public int getIndex() {
        return index;
    }

    public String getLiteral() {
        return literal;
    }

    PatternToExcelEnum(int index, String literal) {
        this.index = index;
        this.literal = literal;
    }
}
