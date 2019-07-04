package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;


public class UpdateAverageIndicatorsExpressionTask extends AbstractWorksapceUpdateTask {

    // indicator uuids
    private final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID = "__TbUIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    private final String AVERAGE_LENGTH_WITH_BLANK_UUID = "__gPoIJSOEd-TE5ti6XNR2Q"; //$NON-NLS-1$

    @Override
    public Date getOrder() {
        return createDate(2019, 7, 3);
    }

    @Override
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();

        // AVERAGE_LENGTH_WITH_BLANK_AND_NULL
        IndicatorDefinition definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "SQL",
                    "SELECT SUM(CHAR_LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Oracle",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "DB2",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Ingres",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "SQLite",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Hive",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Teradata",
                    "SELECT SUM(CHAR_LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%>  WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Informix",
                    "SELECT SUM(CHAR_LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM  &lt;%=__TABLE_NAME__%>   WHERE ( &lt;%=__COLUMN_NAMES__%>  IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "PostgreSQL",
                    "SELECT SUM(CHAR_LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%>  WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Snowflake",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> WHERE (&lt;%=__COLUMN_NAMES__%> IS NOT NULL ) &lt;%=__AND_WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "", "");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }

        // AVERAGE_LENGTH_WITH_BLANK
        definition = definitionHandler.getDefinitionById(AVERAGE_LENGTH_WITH_BLANK_AND_NULL_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "SQL",
                    "SELECT SUM(CHAR_LENGTH(IFNULL(&lt;%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Oracle",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "DB2",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Ingres",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "SQLite",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Hive",
                    "SELECT SUM(LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Teradata",
                    "SELECT SUM(CHAR_LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Informix",
                    "SELECT SUM(CHAR_LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM  &lt;%=__TABLE_NAME__%>   &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "PostgreSQL",
                    "SELECT SUM(CHAR_LENGTH(&lt;%=__COLUMN_NAMES__%>)), COUNT(*) FROM &lt;%=__TABLE_NAME__%>  &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Netezza",
                    "SELECT SUM(CHAR_LENGTH(ISNULL(&lt;%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            IndicatorDefinitionFileHelper.updateSqlExpression(definition, "Snowflake",
                    "SELECT SUM(LENGTH(IFNULL(&lt;%=__COLUMN_NAMES__%>,''))), COUNT(*) FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%>");
            result = result && IndicatorDefinitionFileHelper.save(definition);
        }
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        return result;
    }

}
