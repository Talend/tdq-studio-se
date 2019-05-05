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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * for TDQ-16610 msjian: add the expression can be used for snowflake for TextStatistics and regular expresstion
 * matching.
 *
 */
public class AddTextStatistics4SnowflakeTask extends AbstractWorksapceUpdateTask {

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

    private final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL_BODY =
            "SELECT SUM(LENGTH(CASE WHEN LENGTH( TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'')) ) =0  THEN '' ELSE  IFNULL(<%=__COLUMN_NAMES__%>,'') END)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_WITH_BLANK_BODY =
            "SELECT SUM(LENGTH(CASE WHEN   LENGTH( TRIM(<%=__COLUMN_NAMES__%>) ) =0  THEN '' ELSE  <%=__COLUMN_NAMES__%> END)), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_WITH_NULL_BODY =
            "SELECT SUM(LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_BODY =
            "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY =
            "SELECT MAX(LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_BODY =
            "SELECT MAX(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_NULL_BODY =
            "SELECT MAX(LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_BODY =
            "SELECT MAX(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY =
            "SELECT MIN(LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_BODY =
            "SELECT MIN(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_NULL_BODY =
            "SELECT MIN(LENGTH(IFNULL(<%=__COLUMN_NAMES__%>,''))) FROM <%=__TABLE_NAME__%> WHERE (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_BODY =
            "SELECT MIN(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(IFNULL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) <> '' ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String REGULAR_EXPRESSION_MATCHING_UUID = "_yb-_8Dh8Ed2XmO7pl5Yuyg"; //$NON-NLS-1$

    private final String REGULAR_EXPRESSION_MATCHING_BODY =
            "SELECT COUNT(CASE WHEN REGEXP_LIKE(<%=__COLUMN_NAMES__%>,<%=__PATTERN_EXPR__%>) THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String language = DbmsLanguage.SNOWFLAKE;

    private DefinitionHandler definitionHandler;

    public Date getOrder() {
        return createDate(2019, 5, 5);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        Map<String, String> addIndicators = new HashMap<>();
        addIndicators.put(AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID, AVERAGE_LENGTH_WITH_BLANK_AND_NULL_BODY);
        addIndicators.put(AVERAGE_LENGTH_WITH_BLANK_UUID, AVERAGE_LENGTH_WITH_BLANK_BODY);
        addIndicators.put(AVERAGE_LENGTH_WITH_NULL_UUID, AVERAGE_LENGTH_WITH_NULL_BODY);
        addIndicators.put(AVERAGE_LENGTH_UUID, AVERAGE_LENGTH_BODY);
        addIndicators.put(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID, MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY);
        addIndicators.put(MAXIMAL_LENGTH_WITH_BLANK_UUID, MAXIMAL_LENGTH_WITH_BLANK_BODY);
        addIndicators.put(MAXIMAL_LENGTH_WITH_NULL_UUID, MAXIMAL_LENGTH_WITH_NULL_BODY);
        addIndicators.put(MAXIMAL_LENGTH_UUID, MAXIMAL_LENGTH_BODY);
        addIndicators.put(MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID, MINIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY);
        addIndicators.put(MINIMAL_LENGTH_WITH_BLANK_UUID, MINIMAL_LENGTH_WITH_BLANK_BODY);
        addIndicators.put(MINIMAL_LENGTH_WITH_NULL_UUID, MINIMAL_LENGTH_WITH_NULL_BODY);
        addIndicators.put(MINIMAL_LENGTH_UUID, MINIMAL_LENGTH_BODY);
        addIndicators.put(REGULAR_EXPRESSION_MATCHING_UUID, REGULAR_EXPRESSION_MATCHING_BODY);

        definitionHandler = DefinitionHandler.getInstance();
        
        for (String uuid : addIndicators.keySet()) {
            IndicatorDefinition definition = definitionHandler.getDefinitionById(uuid);
            if (definition != null) {
                String expressionBody = addIndicators.get(uuid);
                if (!IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(definition, language)) {
                    IndicatorDefinitionFileHelper.addSqlExpression(definition, language, expressionBody);
                } else {
                    IndicatorDefinitionFileHelper.updateSqlExpression(definition, language, expressionBody);
                }
                IndicatorDefinitionFileHelper.save(definition);
            }
        }

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        return true;
    }
}
