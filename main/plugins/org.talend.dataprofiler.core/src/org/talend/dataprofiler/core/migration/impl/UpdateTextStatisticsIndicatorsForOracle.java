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

import java.util.Date;

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

public class UpdateTextStatisticsIndicatorsForOracle extends AbstractWorksapceUpdateTask {

    // indicator uuids
    private final String AVG_LENGTH_WITH_BLANK_AND_NULL_UUID = "__TbUIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVG_LENGTH_WITH_BLANK_UUID = "__gPoIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVG_LENGTH_WITH_NULL_UUID = "__vI_wJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID = "_-hzp8JSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_UUID = "_-xmZcJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_NULL_UUID = "_-_UFUJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_UUID = "_ccHq1RF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID = "_9HDjMJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_UUID = "_G4EzQZU9Ed-Y15ulK_jijQ"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_NULL_UUID = "_a4KsoI1qEd-xwI2imLgHRA"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_UUID = "_ccHq1BF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    // sql expression body

    private final String AVG_LENGTH_WITH_BLANK_BODY = "SELECT SUM(CASE WHEN <%=__COLUMN_NAMES__%> IS NOT NULL AND  TRIM(<%=__COLUMN_NAMES__%>) IS NULL THEN 0 ELSE  LENGTH(<%=__COLUMN_NAMES__%>)END), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String AVG_LENGTH_WITH_NULL_BODY = "SELECT SUM(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL THEN 0 ELSE LENGTH(<%=__COLUMN_NAMES__%>) END), COUNT(*) FROM <%=__TABLE_NAME__%> WHERE (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String AVG_LENGTH_WITH_BLANK_AND_NULL_BODY = "SELECT SUM(CASE WHEN <%=__COLUMN_NAMES__%> IS NOT NULL AND  TRIM(<%=__COLUMN_NAMES__%>) IS NULL THEN 0 ELSE CASE WHEN <%=__COLUMN_NAMES__%> IS NULL THEN 0 ELSE LENGTH(<%=__COLUMN_NAMES__%>) END  END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY = "SELECT MIN(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 0 ELSE LENGTH(<%=__COLUMN_NAMES__%>) END) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_BLANK_BODY = "SELECT MIN( LENGTH(<%=__COLUMN_NAMES__%>) ) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_WITH_NULL_BODY = "SELECT MIN(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL THEN 0 ELSE LENGTH(<%=__COLUMN_NAMES__%>) END) FROM <%=__TABLE_NAME__%> WHERE (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MINIMAL_LENGTH_BODY = "SELECT MIN( LENGTH(<%=__COLUMN_NAMES__%>) ) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY = "SELECT MAX(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL  THEN 0 ELSE LENGTH(<%=__COLUMN_NAMES__%>) END) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_BLANK_BODY = "SELECT MAX( LENGTH(<%=__COLUMN_NAMES__%>) ) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_WITH_NULL_BODY = "SELECT MAX(CASE WHEN <%=__COLUMN_NAMES__%> IS NULL THEN 0 ELSE LENGTH(<%=__COLUMN_NAMES__%>) END) FROM <%=__TABLE_NAME__%> WHERE (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String MAXIMAL_LENGTH_BODY = "SELECT MAX( LENGTH(<%=__COLUMN_NAMES__%>) ) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> IS NOT NULL ) AND (TRIM(NVL(<%=__COLUMN_NAMES__%>,'NULL TALEND')) IS NOT NULL) <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private final String LANGUAGE = "Oracle"; //$NON-NLS-1$

    public UpdateTextStatisticsIndicatorsForOracle() {
        // TODO Auto-generated constructor stub
    }

    public Date getOrder() {
        return createDate(2014, 4, 10);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();
        // AVERAGE LENGTH
        IndicatorDefinition definition = definitionHandler.getDefinitionById(AVG_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, AVG_LENGTH_WITH_BLANK_AND_NULL_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(AVG_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, AVG_LENGTH_WITH_BLANK_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(AVG_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, AVG_LENGTH_WITH_NULL_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }

        // MAXIMAL LENGTH
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MAXIMAL_LENGTH_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MAXIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MAXIMAL_LENGTH_WITH_BLANK_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(MAXIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MAXIMAL_LENGTH_WITH_NULL_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }

        // MINIMAL LENGTH
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MINIMAL_LENGTH_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MINIMAL_LENGTH_WITH_BLANK_AND_NULL_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MINIMAL_LENGTH_WITH_BLANK_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definition = definitionHandler.getDefinitionById(MINIMAL_LENGTH_WITH_NULL_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, LANGUAGE, MINIMAL_LENGTH_WITH_NULL_BODY);
            IndicatorDefinitionFileHelper.save(definition);
        }
        definitionHandler.reloadIndicatorsDefinitions();

        return true;
    }
}
