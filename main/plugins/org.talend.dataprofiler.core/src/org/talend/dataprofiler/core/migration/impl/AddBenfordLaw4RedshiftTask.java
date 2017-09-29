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
 * for TDQ-11558 TDQ-11853 msjian: add the expression can be used for redshift for the benford law.
 *
 */
public class AddBenfordLaw4RedshiftTask extends AbstractWorksapceUpdateTask {

    private static final String BENFORD_LAW_UUID = "_yRkFIezIEeG0fbygDv6UrQ"; //$NON-NLS-1$

    private final String Redshift = SupportDBUrlType.REDSHIFT.getDBKey();

    private DefinitionHandler definitionHandler;

    public Date getOrder() {
        return createDate(2016, 3, 3);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        definitionHandler = DefinitionHandler.getInstance();
        IndicatorDefinition regexPatternDefinition = definitionHandler.getDefinitionById(BENFORD_LAW_UUID);
        if (regexPatternDefinition != null) {
            if (!IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(regexPatternDefinition, Redshift)) {
                IndicatorDefinitionFileHelper
                        .addSqlExpression(regexPatternDefinition, Redshift,
                                "SELECT SUBSTRING(<%=__COLUMN_NAMES__%>,1,1), COUNT(*)  FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 1 order by 1"); //$NON-NLS-1$
            } else {
                IndicatorDefinitionFileHelper
                        .updateSqlExpression(regexPatternDefinition, Redshift,
                                "SELECT SUBSTRING(<%=__COLUMN_NAMES__%>,1,1), COUNT(*)  FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 1 order by 1"); //$NON-NLS-1$
            }
            IndicatorDefinitionFileHelper.save(regexPatternDefinition);
            DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        }

        return true;
    }
}
