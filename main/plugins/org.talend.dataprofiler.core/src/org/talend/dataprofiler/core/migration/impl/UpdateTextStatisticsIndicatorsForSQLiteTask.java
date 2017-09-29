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
 * DOC msjian class global comment. The task is used to add sql template in all 12 Text Statistics for SQLite. 22517:
 * 2011-07-20
 */
public class UpdateTextStatisticsIndicatorsForSQLiteTask extends AbstractWorksapceUpdateTask {

    private static final String AVERAGE_LENGTH = "Average Length"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_BLANK = "Average Length With Blank"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_BLANK_AND_NULL = "Average Length With Blank and Null"; //$NON-NLS-1$

    private static final String AVERAGE_LENGTH_WITH_NULL = "Average Length With Null"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH = "Maximal Length"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_BLANK = "Maximal Length With Blank"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_BLANK_AND_NULL = "Maximal Length With Blank and Null"; //$NON-NLS-1$

    private static final String MAXIMAL_LENGTH_WITH_NULL = "Maximal Length With Null"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH = "Minimal Length"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_BLANK = "Minimal Length With Blank"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_BLANK_AND_NULL = "Minimal Length With Blank and Null"; //$NON-NLS-1$

    private static final String MINIMAL_LENGTH_WITH_NULL = "Minimal Length With Null"; //$NON-NLS-1$

    private static final String SQLITE = SupportDBUrlType.SQLITE3DEFAULTURL.getLanguage();

    private static final String[] TextStatisticsIndicators = { AVERAGE_LENGTH, AVERAGE_LENGTH_WITH_BLANK,
            AVERAGE_LENGTH_WITH_BLANK_AND_NULL, AVERAGE_LENGTH_WITH_NULL, MAXIMAL_LENGTH, MAXIMAL_LENGTH_WITH_BLANK,
            MAXIMAL_LENGTH_WITH_BLANK_AND_NULL, MAXIMAL_LENGTH_WITH_NULL, MINIMAL_LENGTH, MINIMAL_LENGTH_WITH_BLANK,
            MINIMAL_LENGTH_WITH_BLANK_AND_NULL, MINIMAL_LENGTH_WITH_NULL };

    private static final String BODY_AVERAGE = "SELECT SUM(LENGTH(<%=__COLUMN_NAMES__%>)), COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String BODY_MAX = "SELECT MAX(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    private static final String BODY_MIN = "SELECT MIN(LENGTH(<%=__COLUMN_NAMES__%>)) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        for (String indicatorName : TextStatisticsIndicators) {
            IndicatorDefinition indiDefinition = IndicatorDefinitionFileHelper.getSystemIndicatorByName(indicatorName);
            if (indiDefinition != null && IndicatorDefinitionFileHelper.removeSqlExpression(indiDefinition, SQLITE)) {
                String body = ""; //$NON-NLS-1$
                if (indicatorName.startsWith("Average")) { //$NON-NLS-1$
                    body = BODY_AVERAGE;
                } else if (indicatorName.startsWith("Maximal")) { //$NON-NLS-1$
                    body = BODY_MAX;
                } else if (indicatorName.startsWith("Minimal")) { //$NON-NLS-1$
                    body = BODY_MIN;
                }

                List<TdExpression> remainExpLs = new ArrayList<TdExpression>();
                remainExpLs.addAll(indiDefinition.getSqlGenericExpression());
                indiDefinition.getSqlGenericExpression().clear();

                IndicatorDefinitionFileHelper.addSqlExpression(indiDefinition, SQLITE, body);
                indiDefinition.getSqlGenericExpression().addAll(remainExpLs);

                result = result && IndicatorDefinitionFileHelper.save(indiDefinition);
            }

        }

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        return result;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
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
        return createDate(2011, 7, 20);
    }

}
