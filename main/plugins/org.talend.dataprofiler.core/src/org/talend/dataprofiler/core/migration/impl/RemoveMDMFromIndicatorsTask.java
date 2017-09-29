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

public class RemoveMDMFromIndicatorsTask extends AbstractWorksapceUpdateTask {

    private final String ROW_COUNT_UUID = "_ccFOkBF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String NULL_COUNT_UUID = "_GrAeADh9Ed2XmO7pl5Yuyg"; //$NON-NLS-1$

    private final String UNIQUE_COUNT_UUID = "_ccHq0RF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String DISTINCT_COUNT_UUID = "_ccHq0BF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String DUPLICATE_COUNT_UUID = "_ccHq0hF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$

    private final String LANGUAGE = SupportDBUrlType.MDM.getLanguage();

    public RemoveMDMFromIndicatorsTask() {
        // TODO Auto-generated constructor stub
    }

    public Date getOrder() {
        return createDate(2014, 12, 4);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();
        // ROW COUNT
        IndicatorDefinition definition = definitionHandler.getDefinitionById(ROW_COUNT_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.removeSqlExpression(definition, LANGUAGE);
            IndicatorDefinitionFileHelper.save(definition);
        }

        definition = definitionHandler.getDefinitionById(NULL_COUNT_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.removeSqlExpression(definition, LANGUAGE);
            IndicatorDefinitionFileHelper.save(definition);
        }

        definition = definitionHandler.getDefinitionById(UNIQUE_COUNT_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.removeSqlExpression(definition, LANGUAGE);
            IndicatorDefinitionFileHelper.save(definition);
        }

        definition = definitionHandler.getDefinitionById(DISTINCT_COUNT_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.removeSqlExpression(definition, LANGUAGE);
            IndicatorDefinitionFileHelper.save(definition);
        }

        definition = definitionHandler.getDefinitionById(DUPLICATE_COUNT_UUID);
        if (definition != null) {
            IndicatorDefinitionFileHelper.removeSqlExpression(definition, LANGUAGE);
            IndicatorDefinitionFileHelper.save(definition);
        }
        return true;
    }

}
