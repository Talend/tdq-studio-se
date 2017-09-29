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
 * add Teradata, Sybase, Informix, Postgresql sql expression for Text Statistics Indicators.
 */
public class UpdateTextStatisticsIndicatorsForSomeDatabasesTask extends AbstractWorksapceUpdateTask {

    // langs
    private final static String LANG_TERADATA = "Teradata"; //$NON-NLS-1$

    private final static String LANG_SYBASE = "Adaptive Server Enterprise | Sybase Adaptive Server IQ"; //$NON-NLS-1$

    private final static String LANG_INFORMIX = "Informix"; //$NON-NLS-1$

    private final static String LANG_POSTGRESQL = "PostgreSQL"; //$NON-NLS-1$

    // ============================================

    private static Set<String> clearLanguages = new HashSet<String>();

    static {
        clearLanguages.add(LANG_TERADATA);
        clearLanguages.add(LANG_SYBASE);
        clearLanguages.add(LANG_INFORMIX);
        clearLanguages.add(LANG_POSTGRESQL);
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
    private final String BODY_AVGBN_TERADATA = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END ) ) =0  THEN '' ELSE   CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END  END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGBN_SYBASE = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) ) =0  THEN '' ELSE   CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END  END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGBN_INFORMIX = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( TRIM( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN '' ELSE  <%=__COLUMN_NAMES__%>  END ) ) =0  THEN '' ELSE   CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN '' ELSE  <%=__COLUMN_NAMES__%>  END  END)), COUNT(*) FROM  <%=__TABLE_NAME__%>   <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGBN_POSTGRESQL = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END ) ) =0  THEN '' ELSE   CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END  END)), COUNT(*) FROM <%=__TABLE_NAME__%>  <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // AVERAGE_LENGTH_WITH_BLANK bodys
    private final String BODY_AVGB_TERADATA = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( TRIM(<%=__COLUMN_NAMES__%>) ) =0  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGB_SYBASE = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( LTRIM(RTRIM(<%=__COLUMN_NAMES__%>)) ) =0  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGB_INFORMIX = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( TRIM( <%=__COLUMN_NAMES__%> ) ) =0  THEN '' ELSE   <%=__COLUMN_NAMES__%>  END)), COUNT(*) FROM  <%=__TABLE_NAME__%>   WHERE ( <%=__COLUMN_NAMES__%>  IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGB_POSTGRESQL = "SELECT SUM(CHAR_LENGTH( CASE WHEN   CHAR_LENGTH( TRIM(<%=__COLUMN_NAMES__%>) ) =0  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // AVERAGE_LENGTH_WITH_NULL bodys
    private final String BODY_AVGN_TERADATA = "SELECT SUM(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )), COUNT(*) FROM <%=__TABLE_NAME__%>  WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGN_SYBASE = "SELECT SUM(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )), COUNT(*) FROM <%=__TABLE_NAME__%>  WHERE ( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END ))  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGN_INFORMIX = "SELECT SUM(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )), COUNT(*) FROM <%=__TABLE_NAME__%>  WHERE ( TRIM( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN 'NULL TALEND' ELSE  <%=__COLUMN_NAMES__%>  END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVGN_POSTGRESQL = "SELECT SUM(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )), COUNT(*) FROM <%=__TABLE_NAME__%>  WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // AVERAGE_LENGTH bodys
    private final String BODY_AVG_TERADATA = "SELECT SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVG_SYBASE = "SELECT SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END ))  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVG_INFORMIX = "SELECT SUM(CHAR_LENGTH( <%=__COLUMN_NAMES__%> )), COUNT( <%=__COLUMN_NAMES__%> ) FROM  <%=__TABLE_NAME__%>   WHERE ( <%=__COLUMN_NAMES__%>  IS NOT NULL ) AND ( TRIM( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN 'NULL TALEND' ELSE  <%=__COLUMN_NAMES__%>  END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_AVG_POSTGRESQL = "SELECT SUM(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH_WITH_BLANK_AND_NULL bodys
    private final String BODY_MAXBN_TERADATA = "SELECT MAX(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXBN_SYBASE = "SELECT MAX(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXBN_INFORMIX = "SELECT MAX(CHAR_LENGTH( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN '' ELSE  <%=__COLUMN_NAMES__%>  END )) FROM  <%=__TABLE_NAME__%>   <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXBN_POSTGRESQL = "SELECT MAX(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH_WITH_BLANK bodys
    private final String BODY_MAXB_TERADATA = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXB_SYBASE = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXB_INFORMIX = "SELECT MAX(CHAR_LENGTH( <%=__COLUMN_NAMES__%> )) FROM  <%=__TABLE_NAME__%>   WHERE ( <%=__COLUMN_NAMES__%>  IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXB_POSTGRESQL = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH_WITH_NULL bodys
    private final String BODY_MAXN_TERADATA = "SELECT MAX(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXN_SYBASE = "SELECT MAX(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  WHERE ( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END ))  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXN_INFORMIX = "SELECT MAX(CHAR_LENGTH( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN '' ELSE  <%=__COLUMN_NAMES__%>  END )) FROM  <%=__TABLE_NAME__%>   WHERE ( TRIM( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN 'NULL TALEND' ELSE  <%=__COLUMN_NAMES__%>  END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAXN_POSTGRESQL = "SELECT MAX(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MAXIMAL_LENGTH bodys
    private final String BODY_MAX_TERADATA = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAX_SYBASE = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END ))  <>  '' )  <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAX_INFORMIX = "SELECT MAX(CHAR_LENGTH( <%=__COLUMN_NAMES__%> )) FROM  <%=__TABLE_NAME__%>   WHERE ( <%=__COLUMN_NAMES__%>  IS NOT NULL ) AND ( TRIM( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN 'NULL TALEND' ELSE  <%=__COLUMN_NAMES__%>  END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MAX_POSTGRESQL = "SELECT MAX(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH_WITH_BLANK_AND_NULL bodys
    private final String BODY_MINBN_TERADATA = "SELECT MIN(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINBN_SYBASE = "SELECT MIN(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINBN_INFORMIX = "SELECT MIN(CHAR_LENGTH( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN '' ELSE  <%=__COLUMN_NAMES__%>  END )) FROM  <%=__TABLE_NAME__%>   <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINBN_POSTGRESQL = "SELECT MIN(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH_WITH_BLANK bodys
    private final String BODY_MINB_TERADATA = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINB_SYBASE = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINB_INFORMIX = "SELECT MIN(CHAR_LENGTH( <%=__COLUMN_NAMES__%> )) FROM  <%=__TABLE_NAME__%>   WHERE ( <%=__COLUMN_NAMES__%>  IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINB_POSTGRESQL = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH_WITH_NULL bodys
    private final String BODY_MINN_TERADATA = "SELECT MIN(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINN_SYBASE = "SELECT MIN(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  WHERE ( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END ))  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINN_INFORMIX = "SELECT MIN(CHAR_LENGTH( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN '' ELSE  <%=__COLUMN_NAMES__%>  END )) FROM  <%=__TABLE_NAME__%>   WHERE ( TRIM( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN 'NULL TALEND' ELSE  <%=__COLUMN_NAMES__%>  END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MINN_POSTGRESQL = "SELECT MIN(CHAR_LENGTH( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN '' ELSE <%=__COLUMN_NAMES__%> END )) FROM <%=__TABLE_NAME__%>  WHERE ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    // MINIMAL_LENGTH bodys
    private final String BODY_MIN_TERADATA = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MIN_SYBASE = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( LTRIM(RTRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END ))  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MIN_INFORMIX = "SELECT MIN(CHAR_LENGTH( <%=__COLUMN_NAMES__%> )) FROM  <%=__TABLE_NAME__%>   WHERE ( <%=__COLUMN_NAMES__%>  IS NOT NULL ) AND ( TRIM( CASE WHEN  <%=__COLUMN_NAMES__%>  IS NULL  THEN 'NULL TALEND' ELSE  <%=__COLUMN_NAMES__%>  END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String BODY_MIN_POSTGRESQL = "SELECT MIN(CHAR_LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%>  WHERE (1=1) AND (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND ( TRIM( CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 'NULL TALEND' ELSE <%=__COLUMN_NAMES__%> END )  <>  '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    // ---------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2013, 6, 25);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();

        // AVERAGE_LENGTH_WITH_BLANK_AND_NULL
        IndicatorDefinition definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_AVGBN_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_AVGBN_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_AVGBN_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_AVGBN_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_AVGB_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_AVGB_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_AVGB_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_AVGB_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_AVGN_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_AVGN_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_AVGN_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_AVGN_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_AVG_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_AVG_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_AVG_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_AVG_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_BLANK_AND_NULL
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MAXBN_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MAXBN_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MAXBN_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MAXBN_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MAXB_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MAXB_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MAXB_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MAXB_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MAXN_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MAXN_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MAXN_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MAXN_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL_LENGTH
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MAX_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MAX_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MAX_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MAX_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_BLANK_AND_NULL
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MINBN_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MINBN_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MINBN_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MINBN_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MINB_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MINB_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MINB_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MINB_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH_WITH_NULL
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MINN_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MINN_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MINN_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MINN_POSTGRESQL);
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL_LENGTH
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_UUID);
        if (definition != null) {
            clearSqlGenericExpression(definition, clearLanguages);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_TERADATA, BODY_MIN_TERADATA);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_SYBASE, BODY_MIN_SYBASE);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_INFORMIX, BODY_MIN_INFORMIX);
            IndicatorDefinitionFileHelper.addSqlExpression(definition, LANG_POSTGRESQL, BODY_MIN_POSTGRESQL);
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
