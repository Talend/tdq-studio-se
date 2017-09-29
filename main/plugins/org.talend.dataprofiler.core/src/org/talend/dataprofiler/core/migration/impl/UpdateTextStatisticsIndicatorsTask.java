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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * Update the sql template of Text Statistics Indicators.
 */
public class UpdateTextStatisticsIndicatorsTask extends AbstractWorksapceUpdateTask {

    // langs
    private final static String LANG_SQL = "SQL"; //$NON-NLS-1$

    private final static String LANG_ORACLE = "Oracle"; //$NON-NLS-1$

    private final static String LANG_SQL_SERVER = "Microsoft SQL Server"; //$NON-NLS-1$

    private final static String LANG_DB2 = "DB2"; //$NON-NLS-1$

    private final static String LANG_INGRES = "Ingres"; //$NON-NLS-1$

    private final static String LANG_SQLITE = "SQLite"; //$NON-NLS-1$

    private final static String LANG_HIVE = "Hive"; //$NON-NLS-1$

    // ============================================

    private static Set<String> clearLanguages = new HashSet<String>();

    static {
        clearLanguages.add(LANG_SQL);
        clearLanguages.add(LANG_ORACLE);
        clearLanguages.add(LANG_SQL_SERVER);
        clearLanguages.add(LANG_DB2);
        clearLanguages.add(LANG_INGRES);
        clearLanguages.add(LANG_SQLITE);
        clearLanguages.add(LANG_HIVE);
    }

    // indicator uuids
    private final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID = "__TbUIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_WITH_BLANK_UUID = "__gPoIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_WITH_NULL_UUID = "__vI_wJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_UUID = "_ccIR4BF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID = "_-hzp8JSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_UUID = "_-xmZcJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_NULL_UUID = "_-_UFUJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_UUID = "_ccHq1RF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID = "_9HDjMJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_UUID = "_G4EzQZU9Ed-Y15ulK_jijQ"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_NULL_UUID = "_a4KsoI1qEd-xwI2imLgHRA"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_UUID = "_ccHq1BF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    // ============================================

    // AVERAGE_LENGTH_WITH_BLANK_AND_NULL bodys
    private final String BODY_AVGBN_SQL = "SELECT SUM(CHAR_LENGTH(CASE WHEN   CHAR_LENGTH( TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'')) ) =0  THEN '' ELSE  IFNULL(<%=__COLUMN_NAMES__%>,'') END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGBN_ORACLE = "SELECT SUM(LENGTH(CASE WHEN  CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END  IS NOT NULL AND  LENGTH( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END ) ) IS NULL  THEN '' ELSE   CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END  END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGBN_SQL_SERVER = "SELECT SUM(LEN(CASE WHEN  LEN( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) )=0  THEN '' ELSE   CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END  END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_AVGBN_DISH = "SELECT SUM(LENGTH(CASE WHEN   LENGTH( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END ) ) =0  THEN '' ELSE   CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END  END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // AVERAGE_LENGTH_WITH_BLANK bodys
    private final String BODY_AVGB_SQL = "SELECT SUM(CHAR_LENGTH(CASE WHEN   CHAR_LENGTH( TRIM(<%=__COLUMN_NAMES__%>) ) =0  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGB_ORACLE = "SELECT SUM(LENGTH(CASE WHEN <%=__COLUMN_NAMES__%> IS NOT NULL AND  LENGTH( TRIM(<%=__COLUMN_NAMES__%>) ) IS NULL  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGB_SQL_SERVER = "SELECT SUM(LEN(CASE WHEN  LEN( LTRIM(RTRIM(<%=__COLUMN_NAMES__%>)) )=0  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_AVGB_DISH = "SELECT SUM(LENGTH(CASE WHEN   LENGTH( TRIM(<%=__COLUMN_NAMES__%>) ) =0  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // AVERAGE_LENGTH_WITH_NULL bodys
    private final String BODY_AVGN_SQL = "SELECT SUM(CHAR_LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGN_ORACLE = "SELECT SUM(LENGTH(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGN_SQL_SERVER = "SELECT SUM(LEN(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (LTRIM(RTRIM(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END)) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_AVGN_DISH = "SELECT SUM(LENGTH(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // AVERAGE_LENGTH bodys
    private final String BODY_AVG_SQL = "SELECT SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVG_ORACLE = "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVG_SQL_SERVER = "SELECT SUM(LEN(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (LTRIM(RTRIM(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END)) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_AVG_DISH = "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH_WITH_BLANK_AND_NULL bodys
    private final String BODY_MAXBN_SQL = "SELECT MAX(CHAR_LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXBN_ORACLE = "SELECT MAX(LENGTH('XX' || CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXBN_SQL_SERVER = "SELECT MAX(LEN(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MAXBN_DISH = "SELECT MAX(LENGTH(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH_WITH_BLANK bodys
    private final String BODY_MAXB_SQL = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXB_ORACLE = "SELECT MAX(LENGTH('XX' || <%=__COLUMN_NAMES__%>)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXB_SQL_SERVER = "SELECT MAX(LEN(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MAXB_DISH = "SELECT MAX(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH_WITH_NULL bodys
    private final String BODY_MAXN_SQL = "SELECT MAX(CHAR_LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXN_ORACLE = "SELECT MAX(LENGTH('XX' || CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> WHERE (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXN_SQL_SERVER = "SELECT MAX(LEN(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> WHERE (LTRIM(RTRIM(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END)) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MAXN_DISH = "SELECT MAX(LENGTH(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH bodys
    private final String BODY_MAX_SQL = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAX_ORACLE = "SELECT MAX(LENGTH('XX' || <%=__COLUMN_NAMES__%>)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAX_SQL_SERVER = "SELECT MAX(LEN(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (LTRIM(RTRIM(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END)) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MAX_DISH = "SELECT MAX(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH_WITH_BLANK_AND_NULL bodys
    private final String BODY_MINBN_SQL = "SELECT MIN(CHAR_LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINBN_ORACLE = "SELECT MIN(LENGTH('XX' || CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINBN_SQL_SERVER = "SELECT MIN(LEN(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MINBN_DISH = "SELECT MIN(LENGTH(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH_WITH_BLANK bodys
    private final String BODY_MINB_SQL = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINB_ORACLE = "SELECT MIN(LENGTH('XX' || <%=__COLUMN_NAMES__%>)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINB_SQL_SERVER = "SELECT MIN(LEN(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MINB_DISH = "SELECT MIN(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH_WITH_NULL bodys
    private final String BODY_MINN_SQL = "SELECT MIN(CHAR_LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINN_ORACLE = "SELECT MIN(LENGTH('XX' || CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> WHERE (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINN_SQL_SERVER = "SELECT MIN(LEN(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> WHERE (LTRIM(RTRIM(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END)) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MINN_DISH = "SELECT MIN(LENGTH(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END)) FROM <%=__TABLE_NAME__%> WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH bodys
    private final String BODY_MIN_SQL = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MIN_ORACLE = "SELECT MIN(LENGTH('XX' || <%=__COLUMN_NAMES__%>)) - LENGTH('XX') FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MIN_SQL_SERVER = "SELECT MIN(LEN(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (LTRIM(RTRIM(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END)) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // DB2, Ingres, Sqlite, Hive use the same sql template
    private final String BODY_MIN_DISH = "SELECT MIN(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    public Date getOrder() {
        return createDate(2013, 1, 16);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();

        // AVERAGE_LENGTH_WITH_BLANK_AND_NULL
        IndicatorDefinition definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_AVGBN_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_AVGBN_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_AVGBN_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_AVGBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_AVGBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_AVGBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_AVGBN_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_AVGB_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_AVGB_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_AVGB_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_AVGB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_AVGB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_AVGB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_AVGB_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_AVGN_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_AVGN_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_AVGN_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_AVGN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_AVGN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_AVGN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_AVGN_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_AVG_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_AVG_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_AVG_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_AVG_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_AVG_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_AVG_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_AVG_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_BLANK_AND_NULL
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MAXBN_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MAXBN_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MAXBN_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MAXBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MAXBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MAXBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MAXBN_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MAXB_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MAXB_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MAXB_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MAXB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MAXB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MAXB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MAXB_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MAXN_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MAXN_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MAXN_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MAXN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MAXN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MAXN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MAXN_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MAX_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MAX_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MAX_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MAX_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MAX_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MAX_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MAX_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_BLANK_AND_NULL
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MINBN_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MINBN_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MINBN_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MINBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MINBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MINBN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MINBN_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MINB_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MINB_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MINB_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MINB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MINB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MINB_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MINB_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MINN_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MINN_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MINN_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MINN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MINN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MINN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MINN_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL, BODY_MIN_SQL);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_ORACLE, BODY_MIN_ORACLE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQL_SERVER, BODY_MIN_SQL_SERVER);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_DB2, BODY_MIN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INGRES, BODY_MIN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SQLITE, BODY_MIN_DISH);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_HIVE, BODY_MIN_DISH);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        return result;
    }

    /**
     * clear the specified language from the IndicatorDefinition.
     * 
     * @param definition
     * @param languages the languages which need to be clear
     */
    private void clearSqlGenericExpression(IndicatorDefinition definition, Set<String> languages) {
        List<TdExpression> expressions = new ArrayList<TdExpression>();
        for (TdExpression expression : definition.getSqlGenericExpression()) {
            if (expression != null && !languages.contains(expression.getLanguage())) {
                expressions.add(expression);
            }
        }
        definition.getSqlGenericExpression().clear();
        definition.getSqlGenericExpression().addAll(expressions);
    }
}
