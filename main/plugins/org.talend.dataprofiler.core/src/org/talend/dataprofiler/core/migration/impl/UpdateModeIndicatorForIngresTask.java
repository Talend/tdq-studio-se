package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;


public class UpdateModeIndicatorForIngresTask extends AbstractWorksapceUpdateTask {

    private static final String INGRES = SupportDBUrlType.INGRESDEFAULTURL.getLanguage();

    public Date getOrder() {
        return createDate(2019, 3, 7);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        IndicatorDefinition indiDefinition = IndicatorDefinitionFileHelper.getSystemIndicatorByName("Mode");
        if (!IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(indiDefinition, INGRES)) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            indiDefinition,
                            INGRES,
                            "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c DESC");

            result = IndicatorDefinitionFileHelper.save(indiDefinition);
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        }

        return result;
    }

}
