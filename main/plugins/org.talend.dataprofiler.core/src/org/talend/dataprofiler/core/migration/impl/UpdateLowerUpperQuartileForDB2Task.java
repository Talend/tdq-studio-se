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
 * update the sql expression for DB2 in Lower/Upper Quartile indicator.
 */
public class UpdateLowerUpperQuartileForDB2Task extends AbstractWorksapceUpdateTask {

    private final String LOWER_QUARTILE = "Lower Quartile"; //$NON-NLS-1$

    private final String LOWER_QUARTILE_SQL = "SELECT <%=__COLUMN_NAMES__%> FROM ( SELECT <%=__COLUMN_NAMES__%>, ROW_NUMBER() OVER(ORDER BY <%=__COLUMN_NAMES__%>) AS NUMBER FROM <%=__TABLE_NAME__%> WHERE <%=__COLUMN_NAMES__%> IS NOT NULL <%=__AND_WHERE_CLAUSE__%> ) T1 WHERE (NUMBER =  INTEGER((SELECT COUNT(*) FROM <%=__TABLE_NAME__%> WHERE <%=__COLUMN_NAMES__%> IS NOT NULL <%=__AND_WHERE_CLAUSE__%>) /4) +1) "; //$NON-NLS-1$

    private final String UPPER_QUARTILE = "Upper Quartile"; //$NON-NLS-1$

    private final String UPPER_QUARTILE_SQL = "SELECT <%=__COLUMN_NAMES__%> FROM ( SELECT <%=__COLUMN_NAMES__%>, ROW_NUMBER() OVER(ORDER BY <%=__COLUMN_NAMES__%>) AS NUMBER FROM <%=__TABLE_NAME__%> WHERE <%=__COLUMN_NAMES__%> IS NOT NULL <%=__AND_WHERE_CLAUSE__%> ) T1 WHERE ( NUMBER =  INTEGER((SELECT COUNT(*) FROM <%=__TABLE_NAME__%> WHERE <%=__COLUMN_NAMES__%> IS NOT NULL <%=__AND_WHERE_CLAUSE__%>)*3 /4) +1 ) "; //$NON-NLS-1$

    private final String DB2 = SupportDBUrlType.DB2DEFAULTURL.getLanguage();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2013, 2, 24);
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
     * update Lower/Upper Quartile indicators definition.
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean isUpdated = true;

        // Update sql of DB2 to Upper Quartile indicator
        isUpdated = isUpdated && updateSqlForDB2(LOWER_QUARTILE, LOWER_QUARTILE_SQL);
        isUpdated = isUpdated && updateSqlForDB2(UPPER_QUARTILE, UPPER_QUARTILE_SQL);

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        if (!isUpdated) {
            return false;
        }
        return true;
    }

    private boolean updateSqlForDB2(String indicatorName, String sqlExpression) {
        IndicatorDefinition indiDefinition = IndicatorDefinitionFileHelper.getSystemIndicatorByName(indicatorName);
        if (indiDefinition != null && IndicatorDefinitionFileHelper.removeSqlExpression(indiDefinition, DB2)) {
            List<TdExpression> remainExpLs = new ArrayList<TdExpression>();
            remainExpLs.addAll(indiDefinition.getSqlGenericExpression());
            indiDefinition.getSqlGenericExpression().clear();
            IndicatorDefinitionFileHelper.addSqlExpression(indiDefinition, DB2, sqlExpression);
            indiDefinition.getSqlGenericExpression().addAll(remainExpLs);
            return IndicatorDefinitionFileHelper.save(indiDefinition);
        }
        return true;
    }

}
