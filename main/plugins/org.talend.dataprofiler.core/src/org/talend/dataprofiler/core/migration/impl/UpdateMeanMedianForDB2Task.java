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
import java.util.List;

import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * Add/Update Sql Expression in Mean/Median indicators for DB2 language.
 * 
 * ADDed by msjian 2012-8-31 for TDQ-5960
 */
public class UpdateMeanMedianForDB2Task extends AbstractWorksapceUpdateTask {

    private final String MEAN_UUID = "_ccI48RF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String MEDIAN_UUID = "_ccI48hF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String DB2 = SupportDBUrlType.DB2DEFAULTURL.getLanguage();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2012, 8, 31);
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
     * update Mean/Median indicators definition by uuid.
     */
    @Override
    protected boolean doExecute() throws Exception {
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();
        boolean isMeanAdded = true;
        boolean isMedianUpdated = true;

        // Add sql of DB2 to Mean indicator
        IndicatorDefinition meanDefinition = definitionHandler.getDefinitionById(MEAN_UUID);
        if (meanDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(meanDefinition, DB2)) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            meanDefinition,
                            DB2,
                            "SELECT SUM(double(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"); //$NON-NLS-1$
            isMeanAdded = IndicatorDefinitionFileHelper.save(meanDefinition);
        }

        // Update sql of DB2 to Median indicator
        IndicatorDefinition medianDefinition = definitionHandler.getDefinitionById(MEDIAN_UUID);
        if (medianDefinition != null && IndicatorDefinitionFileHelper.removeSqlExpression(medianDefinition, DB2)) {
            List<TdExpression> remainExpLs = new ArrayList<TdExpression>();
            remainExpLs.addAll(medianDefinition.getSqlGenericExpression());
            medianDefinition.getSqlGenericExpression().clear();
            IndicatorDefinitionFileHelper
                    .addSqlExpression(medianDefinition, DB2,
                            "SELECT AVG(double(<%=__COLUMN_NAMES__%>)) FROM ( SELECT <%=__COLUMN_NAMES__%>, COUNT(*) OVER( ) total, CAST(COUNT(*) OVER( ) AS DECIMAL)/2 mid, CEIL(CAST(COUNT(*) OVER( ) AS DECIMAL)/2) next, ROW_NUMBER() OVER ( ORDER BY <%=__COLUMN_NAMES__%>) rn FROM <%=__TABLE_NAME__%> WHERE <%=__COLUMN_NAMES__%> IS NOT NULL <%=__AND_WHERE_CLAUSE__%>) x WHERE ( MOD(total,2) = 0 AND rn IN ( mid, mid+1 ) ) OR ( MOD(total,2) = 1 AND rn = next )"); //$NON-NLS-1$
            medianDefinition.getSqlGenericExpression().addAll(remainExpLs);
            isMedianUpdated = IndicatorDefinitionFileHelper.save(medianDefinition);
        }
        
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        if (!isMeanAdded || !isMedianUpdated) {
            return false;
        }
        return true;
    }

}
