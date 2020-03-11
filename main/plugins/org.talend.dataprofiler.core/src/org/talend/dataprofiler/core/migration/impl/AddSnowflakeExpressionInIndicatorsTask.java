package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.indicators.definitions.DefinitionHandler;


public class AddSnowflakeExpressionInIndicatorsTask extends AbstractWorksapceUpdateTask {

    private static final String LOWER_QUARTILE_UUID = "_ccI49BF2Ed2PKb6nEJEvhw";

    private static final String UPPER_QUARTILE_UUID = "_ccI49RF2Ed2PKb6nEJEvhw";

    private static final String MEDIAN_UUID = "_ccI48hF2Ed2PKb6nEJEvhw";

    private static final String LOWER_QUARTILE_EXPRESSION = "SELECT PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY <%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$

    private static final String UPPER_QUARTILE_EXPRESSION = "SELECT PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY <%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$

    private static final String MEDIAN_EXPRESSION = "SELECT MEDIAN(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE <%=__COLUMN_NAMES__%> IS NOT NULL <%=__AND_WHERE_CLAUSE__%>";//$NON-NLS-1$

    private final String Snowflake = DbmsLanguage.SNOWFLAKE;

    private DefinitionHandler definitionHandler;

    @Override
    public Date getOrder() {
        return createDate(2020, 3, 4);
    }

    @Override
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        definitionHandler = DefinitionHandler.getInstance();
        insertExpression(LOWER_QUARTILE_UUID, LOWER_QUARTILE_EXPRESSION);
        insertExpression(UPPER_QUARTILE_UUID, UPPER_QUARTILE_EXPRESSION);
        insertExpression(MEDIAN_UUID, MEDIAN_EXPRESSION);
        definitionHandler.reloadIndicatorsDefinitions();
        return true;
    }

    private void insertExpression(String indicator_id, String expression) {
        IndicatorDefinition regularExpressionIndicator = definitionHandler.getDefinitionById(indicator_id);
        if (regularExpressionIndicator != null) {
            if (!IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(regularExpressionIndicator, Snowflake)) {
                IndicatorDefinitionFileHelper.addSqlExpression(regularExpressionIndicator, Snowflake, expression); // $NON-NLS-1$
            } else {
                IndicatorDefinitionFileHelper.updateSqlExpression(regularExpressionIndicator, Snowflake, expression); // $NON-NLS-1$
            }
            IndicatorDefinitionFileHelper.save(regularExpressionIndicator);
        }
    }

}
