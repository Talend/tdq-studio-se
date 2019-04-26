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

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * for TDQ-15039 msjian: add the expression can be used for BigQuery for the benford law and pattern.
 *
 */
public class AddBenfordLawPattern4BigQueryTask extends AbstractWorksapceUpdateTask {

    private static final String BENFORD_LAW_UUID = "_yRkFIezIEeG0fbygDv6UrQ"; //$NON-NLS-1$

    private static final String REGULAR_EXPRESSION_MATCHING_UUID = "_yb-_8Dh8Ed2XmO7pl5Yuyg"; //$NON-NLS-1$

    private final String language = DbmsLanguage.BIGQUERY;

    private DefinitionHandler definitionHandler;

    public Date getOrder() {
        return createDate(2019, 4, 2);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean isChanged = false;
        definitionHandler = DefinitionHandler.getInstance();
        IndicatorDefinition regexPatternDefinition = definitionHandler.getDefinitionById(BENFORD_LAW_UUID);
        if (regexPatternDefinition != null) {
            String benfordBody =
                    "SELECT substr(cast(<%=__COLUMN_NAMES__%> as STRING),1,1), COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY substr(cast(<%=__COLUMN_NAMES__%> as STRING),1,1)"; //$NON-NLS-1$
            if (!IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(regexPatternDefinition, language)) {
                IndicatorDefinitionFileHelper
                        .addSqlExpression(regexPatternDefinition, language,
                                benfordBody);
            } else {
                IndicatorDefinitionFileHelper
                        .updateSqlExpression(regexPatternDefinition, language,
                                benfordBody);
            }
            isChanged = true;
            IndicatorDefinitionFileHelper.save(regexPatternDefinition);
        }

        IndicatorDefinition regularExpMatchingDef =
                definitionHandler.getDefinitionById(REGULAR_EXPRESSION_MATCHING_UUID);
        if (regularExpMatchingDef != null) {
            String regularExpbody =
                    "SELECT COUNT(CASE WHEN REGEXP_CONTAINS(cast(<%=__COLUMN_NAMES__%> as STRING),<%=__PATTERN_EXPR__%>) THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$
            if (!IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(regularExpMatchingDef, language)) {
                IndicatorDefinitionFileHelper
                        .addSqlExpression(regularExpMatchingDef, language,
                                regularExpbody);
            } else {
                IndicatorDefinitionFileHelper
                        .updateSqlExpression(regularExpMatchingDef, language,
                                regularExpbody);
            }
            isChanged = true;
            IndicatorDefinitionFileHelper.save(regularExpMatchingDef);
        }

        if (isChanged) {
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        }

        return true;
    }
}
