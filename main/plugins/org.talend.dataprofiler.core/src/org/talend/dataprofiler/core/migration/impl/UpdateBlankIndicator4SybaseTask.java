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
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * created by xqliu on Oct 15, 2012 Detailled comment
 */
public class UpdateBlankIndicator4SybaseTask extends AbstractWorksapceUpdateTask {

    private String indicatorLabel = "Blank Count"; //$NON-NLS-1$

    private String expressionBody = "SELECT COUNT(<%=__COLUMN_NAMES__%>) FROM <%=__TABLE_NAME__%> WHERE (<%=__COLUMN_NAMES__%> = '' or RTRIM(LTRIM(<%=__COLUMN_NAMES__%>)) = '') <%=__AND_WHERE_CLAUSE__%>"; //$NON-NLS-1$

    public Date getOrder() {
        return createDate(2012, 10, 15);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = false;
        IndicatorDefinition indiDefinition = IndicatorDefinitionFileHelper.getSystemIndicatorByName(indicatorLabel);
        String sybaseLanguage = SupportDBUrlType.SYBASEDEFAULTURL.getLanguage();
        if (indiDefinition != null) {
            EList<TdExpression> sqlGenericExpression = indiDefinition.getSqlGenericExpression();
            for (TdExpression exp : sqlGenericExpression) {
                if (sybaseLanguage.equals(exp.getLanguage())) {
                    exp.setBody(expressionBody);
                }
            }
            result = IndicatorDefinitionFileHelper.save(indiDefinition);
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        }
        return result;
    }
}
