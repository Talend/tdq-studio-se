package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;


public class AddTeradataExpressionInRegularExpressionIndicatorTask extends AbstractWorksapceUpdateTask {

    private static final String REGULAR_EXPRESSION_UUID = "_yb-_8Dh8Ed2XmO7pl5Yuyg";

    private final String Teradata = SupportDBUrlType.TERADATADEFAULTURL.getDBKey();

    private DefinitionHandler definitionHandler;

    @Override
    public Date getOrder() {
        return createDate(2019, 6, 4);
    }

    @Override
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        definitionHandler = DefinitionHandler.getInstance();
        IndicatorDefinition regularExpressionIndicator = definitionHandler.getDefinitionById(REGULAR_EXPRESSION_UUID);
        if (regularExpressionIndicator != null) {
            if (!IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(regularExpressionIndicator, Teradata)) {
                IndicatorDefinitionFileHelper.addSqlExpression(regularExpressionIndicator, Teradata,
                                "SELECT COUNT(CASE WHEN REGEXP_SIMILAR(CAST(<%=__COLUMN_NAMES__%> as varchar(100)),<%=__PATTERN_EXPR__%>)=1 THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"); //$NON-NLS-1$
            } else {
                IndicatorDefinitionFileHelper.updateSqlExpression(regularExpressionIndicator, Teradata,
                                "SELECT COUNT(CASE WHEN REGEXP_SIMILAR(CAST(<%=__COLUMN_NAMES__%> as varchar(100)),<%=__PATTERN_EXPR__%>)=1 THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"); //$NON-NLS-1$
            }
            IndicatorDefinitionFileHelper.save(regularExpressionIndicator);
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        }

        return true;
    }

}
