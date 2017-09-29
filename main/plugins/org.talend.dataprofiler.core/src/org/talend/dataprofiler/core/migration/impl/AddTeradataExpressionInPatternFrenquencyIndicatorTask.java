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
 * @author scorreia
 * 
 * This migration task removes the call to the Oracle TRIM function when computing the length indicators
 */
public class AddTeradataExpressionInPatternFrenquencyIndicatorTask extends AbstractWorksapceUpdateTask {

    private final String PATTERN_FREQUENCY_UUID = "_kQzTsJR-Ed2XO-JvLwVAwg"; //$NON-NLS-1$

    private final String PATTERN_LOW_FREQUENCY_UUID = "_OCTbwJR_Ed2XO-JvLwVAwg"; //$NON-NLS-1$

    private final String Teradata = SupportDBUrlType.TERADATADEFAULTURL.getLanguage();

    private final String CHAR_TOREPLACE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//$NON-NLS-1$

    private final String CHAR_REPLACE = "aaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAA9999999999";//$NON-NLS-1$

    private final String CHAR_NAME = "Characters mapping on Teradata";//$NON-NLS-1$

    private final String PATTERN_FREQUENCY_SQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c DESC";//$NON-NLS-1$

    private final String PATTERN_LOW_FREQUENCY_SQL = "SELECT <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY <%=__GROUP_BY_ALIAS__%> ORDER BY c ASC";//$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();
        boolean isFrequencyAdded = true;
        boolean isLowFrequencyUpdated = true;

        // Add sql of Teradata to pattern frenquency table indicator
        IndicatorDefinition teraDefinition = definitionHandler.getDefinitionById(PATTERN_FREQUENCY_UUID);
        if (teraDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(teraDefinition, Teradata)) {
            IndicatorDefinitionFileHelper.addSqlExpression(teraDefinition, Teradata, PATTERN_FREQUENCY_SQL);
            IndicatorDefinitionFileHelper.addCharacterMapping(teraDefinition, Teradata, CHAR_NAME, CHAR_TOREPLACE, CHAR_REPLACE);
            isFrequencyAdded = IndicatorDefinitionFileHelper.save(teraDefinition);
        }

        // Update sql of Teradata to pattern low frenquency table indicator
        IndicatorDefinition lowDefinition = definitionHandler.getDefinitionById(PATTERN_LOW_FREQUENCY_UUID);
        if (teraDefinition != null && !IndicatorDefinitionFileHelper.isExistSqlExprWithLanguage(lowDefinition, Teradata)) {
            IndicatorDefinitionFileHelper.addSqlExpression(lowDefinition, Teradata, PATTERN_LOW_FREQUENCY_SQL);
            IndicatorDefinitionFileHelper.addCharacterMapping(lowDefinition, Teradata, CHAR_NAME, CHAR_TOREPLACE, CHAR_REPLACE);
            isLowFrequencyUpdated = IndicatorDefinitionFileHelper.save(lowDefinition);
        }

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        if (!isFrequencyAdded || !isLowFrequencyUpdated) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2012, 12, 27);
    }

}
