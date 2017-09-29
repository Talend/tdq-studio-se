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
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * 
 * TDQ-11797: msjian Add Exasol Expression into RegularExpressionMatching.
 * 
 */
public class AddExasolRegularExpressionMatchingTask extends AbstractWorksapceUpdateTask {

    private final String REGEXPATTERNMATCH_UUID = "_yb-_8Dh8Ed2XmO7pl5Yuyg";//$NON-NLS-1$

    private final String EXASolution = SupportDBUrlType.EXASOL.getLanguage();

    private DefinitionHandler definitionHandler;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2016, 4, 26);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        definitionHandler = DefinitionHandler.getInstance();

        IndicatorDefinition regexPatternDefinition = definitionHandler.getDefinitionById(REGEXPATTERNMATCH_UUID);
        if (regexPatternDefinition != null
                && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(regexPatternDefinition, EXASolution)) {
            IndicatorDefinitionFileHelper
                    .addSqlExpression(
                            regexPatternDefinition,
                            EXASolution,
                            "SELECT COUNT(CASE WHEN <%=__COLUMN_NAMES__%> REGEXP_LIKE <%=__PATTERN_EXPR__%> THEN 1 END), COUNT(*) FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>"); //$NON-NLS-1$
            IndicatorDefinitionFileHelper.save(regexPatternDefinition);
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        }
        return true;
    }

}
