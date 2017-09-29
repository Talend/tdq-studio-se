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
 * 
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * 
 */
public class UpdateDistinctCountIndicatorTask extends AbstractWorksapceUpdateTask {

    private String indicatorLabel = "Distinct Count"; //$NON-NLS-1$
    public UpdateDistinctCountIndicatorTask() {

    }

    public Date getOrder() {
        return createDate(2012, 3, 9);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        IndicatorDefinition indiDefinition = IndicatorDefinitionFileHelper.getSystemIndicatorByName(indicatorLabel);
        String defLanguage = "SQL"; //$NON-NLS-1$
        // update sql expression for default language(the updated also adapt AceessDB),remove sql expression for Access.
        if (indiDefinition != null && IndicatorDefinitionFileHelper.removeSqlExpression(indiDefinition, defLanguage)) {
            IndicatorDefinitionFileHelper.removeSqlExpression(indiDefinition, SupportDBUrlType.ACCESS.getLanguage());
            List<TdExpression> remainExpLs = new ArrayList<TdExpression>();
            remainExpLs.addAll(indiDefinition.getSqlGenericExpression());
            indiDefinition.getSqlGenericExpression().clear();
            IndicatorDefinitionFileHelper
                    .addSqlExpression(indiDefinition, defLanguage,
                            "SELECT COUNT(*) FROM (SELECT DISTINCT <%=__COLUMN_NAMES__%> FROM <%=__TABLE_NAME__%> <%=__WHERE_CLAUSE__%>) A"); //$NON-NLS-1$
            indiDefinition.getSqlGenericExpression().addAll(remainExpLs);
            boolean save = IndicatorDefinitionFileHelper.save(indiDefinition);
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
            return save;
        }
        return false;
    }

}
