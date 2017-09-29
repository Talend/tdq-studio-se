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

import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;

/**
 * DOC yyi class global comment. The task is usesd to add sql template in Unique Count.definition,Duplicate
 * Count.definition,Blank Count.definition for Sybase. 14851: 2010-08-13
 */
public class UpdateSimpleStatisticsIndicatorsForSybaseIqTask extends AbstractWorksapceUpdateTask {

    private static final String UNIQUE_COUNT = "Unique Count"; //$NON-NLS-1$

    private static final String DUPLICATE_COUNT = "Duplicate Count"; //$NON-NLS-1$

    private static final String BLANK_COUNT = "Blank Count"; //$NON-NLS-1$

    private static final String SYBASE = SupportDBUrlType.SYBASEDEFAULTURL.getLanguage();

    private static final String REMOVELANGUAGE = "Adaptive Server Enterprise"; //$NON-NLS-1$
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {

        IndicatorDefinition definition0 = IndicatorDefinitionFileHelper.getSystemIndicatorByName(UNIQUE_COUNT);
        IndicatorDefinition definition1 = IndicatorDefinitionFileHelper.getSystemIndicatorByName(DUPLICATE_COUNT);
        IndicatorDefinition definition2 = IndicatorDefinitionFileHelper.getSystemIndicatorByName(BLANK_COUNT);
        if (definition0 != null && definition1 != null && definition2 != null) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            definition0,
                            SYBASE,
                            "SELECT COUNT(*) FROM (SELECT <%=__COLUMN_NAMES__%>, COUNT(*) mycount FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> HAVING COUNT(*) = 1) AS myquery"); //$NON-NLS-1$

            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            definition1,
                            SYBASE,
                            "SELECT COUNT(*) FROM (SELECT <%=__COLUMN_NAMES__%>, COUNT(*) mycount FROM <%=__TABLE_NAME__%>  m <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> HAVING COUNT(*) > 1) AS myquery"); //$NON-NLS-1$

            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            definition2,
                            SYBASE,
                            "SELECT COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE RTRIM(LTRIM(<%=__COLUMN_NAMES__%>)) = '' <%=__AND_WHERE_CLAUSE__%>"); //$NON-NLS-1$

            IndicatorDefinitionFileHelper.removeSqlExpression(definition2, REMOVELANGUAGE);
            return IndicatorDefinitionFileHelper.save(definition0) & IndicatorDefinitionFileHelper.save(definition1)
                    & IndicatorDefinitionFileHelper.save(definition2);
        }

        return false;
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
        return createDate(2010, 8, 13);
    }

}
