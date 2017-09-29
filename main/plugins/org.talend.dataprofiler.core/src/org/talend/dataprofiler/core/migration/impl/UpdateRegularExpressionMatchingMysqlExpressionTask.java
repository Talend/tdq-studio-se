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

import org.eclipse.emf.common.util.EList;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UpdateRegularExpressionMatchingMysqlExpressionTask extends AbstractWorksapceUpdateTask {

    private static final String REGULAR_EXPRESSION_MATCHING_LABEL = "Regular Expression Matching"; //$NON-NLS-1$

    private static final String MYSQL_EXPRESSION = "SELECT COUNT(CASE WHEN <%=__COLUMN_NAMES__%> REGEXP BINARY <%=__PATTERN_EXPR__%> THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2010, 8, 18);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        IndicatorDefinition indicatorDefinition = DefinitionHandler.getInstance().getIndicatorDefinition(
                REGULAR_EXPRESSION_MATCHING_LABEL);
        if (indicatorDefinition != null) {
            EList<TdExpression> sqlGenericExpression = indicatorDefinition.getSqlGenericExpression();
            if (sqlGenericExpression != null && sqlGenericExpression.size() > 0) {
                boolean notFoundMysqlExpression = true;
                for (TdExpression expression : sqlGenericExpression) {
                    if (expression != null && SupportDBUrlType.MYSQLDEFAULTURL.getLanguage().equals(expression.getLanguage())) {
                        notFoundMysqlExpression = false;
                        expression.setBody(MYSQL_EXPRESSION);
                        break;
                    }
                }
                if (notFoundMysqlExpression) {
                    TdExpression newExpression = BooleanExpressionHelper.createTdExpression(SupportDBUrlType.MYSQLDEFAULTURL
                            .getLanguage(), MYSQL_EXPRESSION);
                    sqlGenericExpression.add(newExpression);
                } else {
                    // mysql expression have exist, so needn't to create a new mysql expression
                }
            }
        }
        return true;
    }

}
