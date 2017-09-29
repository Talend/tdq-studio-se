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
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * Update All Indicators from language="Default " to language="SQL".
 * 
 */
public class UpdateAllIndicators4DefaultTask extends AbstractWorksapceUpdateTask {

    private String Language = PatternLanguageType.Default.getName();

    private String newLanguage = PatternLanguageType.Default.getLiteral();

    public Date getOrder() {
        return createDate(2013, 2, 8);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        List<IndicatorDefinition> indiDefinitions = DefinitionHandler.getInstance().getIndicatorsDefinitions();
        for (IndicatorDefinition indiDefinition : indiDefinitions) {
            if (indiDefinition != null) {
                EList<TdExpression> sqlGenericExpression = indiDefinition.getSqlGenericExpression();
                for (TdExpression exp : sqlGenericExpression) {
                    // both "Default" and "Default "
                    if (Language.equals(exp.getLanguage().trim())) {
                        exp.setLanguage(newLanguage);
                        break;
                    }
                }
                result = result && IndicatorDefinitionFileHelper.save(indiDefinition);
            }
        }
        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();
        return result;
    }
}
