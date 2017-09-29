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
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * 
 * Add Sql Expression in some indicators for hive language.
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class UpdateIndicatorForHiveTask extends AbstractWorksapceUpdateTask {


    private final String AVERAGE_LENGTH_UUID = "_ccIR4BF2Ed2PKb6nEJEvhw";//$NON-NLS-1$

    private final String MODE_UUID = "_ccIR4RF2Ed2PKb6nEJEvhw";//$NON-NLS-1$

    private final String ROWCOMPARISON_UUID = "_jMh4gJE3Ed2HGNmGoaS";//$NON-NLS-1$

    private final String UNIQUE_UUID = "_ccHq0RF2Ed2PKb6nEJEvhw";//$NON-NLS-1$

    private final String DUPLICATE_UUID = "_ccHq0hF2Ed2PKb6nEJEvhw";//$NON-NLS-1$

    private final String REGEXPATTERNMATCH_UUID = "_yb-_8Dh8Ed2XmO7pl5Yuyg";//$NON-NLS-1$

    private final String HIVE = SupportDBUrlType.HIVEDEFAULTURL.getLanguage();

    private DefinitionHandler definitionHandler;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2012, 8, 10);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /**
     * updatae some indicator definition by uuid.
     */
    @Override
    protected boolean doExecute() throws Exception {
        definitionHandler = DefinitionHandler.getInstance();
        IndicatorDefinition uniqueDefinition = definitionHandler.getDefinitionById(UNIQUE_UUID);
        if (uniqueDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(uniqueDefinition, HIVE)) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            uniqueDefinition,
                            HIVE,
                            "SELECT COUNT(*) FROM (SELECT <%=__COLUMN_NAMES__%>, COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> HAVING COUNT(*) = 1) myquery"); //$NON-NLS-1$
            IndicatorDefinitionFileHelper.save(uniqueDefinition);
        }
        IndicatorDefinition duplicateDefinition = definitionHandler.getDefinitionById(DUPLICATE_UUID);
        if (duplicateDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(duplicateDefinition, HIVE)) {

            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            duplicateDefinition,
                            HIVE,
                            "SELECT COUNT(*) FROM (SELECT <%=__COLUMN_NAMES__%>, COUNT(*) FROM <%=__TABLE_NAME__%>  m <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> HAVING COUNT(*) > 1) myquery"); //$NON-NLS-1$
            IndicatorDefinitionFileHelper.save(duplicateDefinition);
        }
        IndicatorDefinition regexPatternDefinition = definitionHandler.getDefinitionById(REGEXPATTERNMATCH_UUID);
        if (regexPatternDefinition != null
                && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(regexPatternDefinition, HIVE)) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            regexPatternDefinition,
                            HIVE,
                            "SELECT COUNT(CASE WHEN <%=__COLUMN_NAMES__%> REGEXP <%=__PATTERN_EXPR__%> THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"); //$NON-NLS-1$
            IndicatorDefinitionFileHelper.save(regexPatternDefinition);
        }
        IndicatorDefinition rowCompareDefinition = definitionHandler.getDefinitionById(ROWCOMPARISON_UUID);
        if (rowCompareDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(rowCompareDefinition, HIVE)) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            rowCompareDefinition,
                            HIVE,
                            "SELECT COUNT(*) FROM <%=__TABLE_NAME__%> LEFT OUTER JOIN <%=__TABLE_NAME_2__%> ON (<%=__JOIN_CLAUSE__%>) WHERE (<%=__WHERE_CLAUSE__%>)"); //$NON-NLS-1$
            IndicatorDefinitionFileHelper.save(rowCompareDefinition);
        }
        IndicatorDefinition modeDefinition = definitionHandler.getDefinitionById(MODE_UUID);
        if (modeDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(modeDefinition, HIVE)) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            modeDefinition,
                            HIVE,
                            "SELECT  <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> m <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> ORDER BY c DESC "); //$NON-NLS-1$
            IndicatorDefinitionFileHelper.save(modeDefinition);
        }
        updateTextIndicators();
        return true;
    }

    private void updateTextIndicators() {
        String minLengthSql = "SELECT MIN(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$
        String maxLengthSql = "SELECT MAX(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$
        String averageLengthSql = "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$
        String averageLengthWithSql = "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>";//$NON-NLS-1$
        IndicatorDefinition textIndicatorDefinition = definitionHandler.getDefinitionById("_yb9x0zh8Ed2XmO7pl5Yuyg");//$NON-NLS-1$
        if(textIndicatorDefinition!=null){
            EList<IndicatorDefinition> aggregatedDefinitions = textIndicatorDefinition.getAggregatedDefinitions();
            if(!aggregatedDefinitions.isEmpty()){
                for (IndicatorDefinition indiDef : aggregatedDefinitions) {
                    if (indiDef == null || indiDef.eIsProxy() || indiDef.getLabel() == null
                            || IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(indiDef, HIVE)) {
                        continue;
                    }
                    if (indiDef.getLabel().startsWith("Minimal")) {//$NON-NLS-1$
                        IndicatorDefinitionFileHelper.addSqlExpression(indiDef, HIVE, minLengthSql);
                    } else if (indiDef.getLabel().startsWith("Maximal")) {//$NON-NLS-1$
                        IndicatorDefinitionFileHelper.addSqlExpression(indiDef, HIVE, maxLengthSql);
                    } else if (AVERAGE_LENGTH_UUID.equals(ResourceHelper.getUUID(indiDef))) {
                        // Average Length
                        IndicatorDefinitionFileHelper.addSqlExpression(indiDef, HIVE, averageLengthSql);
                    } else if (indiDef.getLabel().startsWith("Average")) {//$NON-NLS-1$
                        IndicatorDefinitionFileHelper.addSqlExpression(indiDef, HIVE, averageLengthWithSql);
                    }
                    IndicatorDefinitionFileHelper.save(indiDef);
                }
            }
        }
    }

}
