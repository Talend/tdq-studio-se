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
 * @author msjian
 * 
 * TDQ-12897: This migration task adds CharacterMapping for hive/impala for (low)PatternFrenquency indicators.
 */
public class AddHiveCharMapInPatternFrenquencyTask extends AbstractWorksapceUpdateTask {

    private final String PATTERN_FREQUENCY_UUID = "_kQzTsJR-Ed2XO-JvLwVAwg"; //$NON-NLS-1$

    private final String PATTERN_LOW_FREQUENCY_UUID = "_OCTbwJR_Ed2XO-JvLwVAwg"; //$NON-NLS-1$

    private final String hive = SupportDBUrlType.HIVEDEFAULTURL.getLanguage();

    private final String CHAR_TOREPLACE = "0123456789abcdefghijklmnopqrstuvwxyz?aê???éèù??üABCDEFGHIJKLMNOPQRSTUVWXYZ??ê???éèù??ü";//$NON-NLS-1$

    private final String CHAR_REPLACE = "9999999999aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";//$NON-NLS-1$

    private final String CHAR_NAME = "Characters mapping on Hive";//$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        DefinitionHandler definitionHandler = DefinitionHandler.getInstance();
        boolean isFrequencyAdded = true;
        boolean isLowFrequencyAdded = true;

        // Add CharacterMapping of hive to pattern frenquency indicator
        IndicatorDefinition hiveDefinition = definitionHandler.getDefinitionById(PATTERN_FREQUENCY_UUID);
        if (hiveDefinition != null && !IndicatorDefinitionFileHelper.isExistCharacterMappingWithLanguage(hiveDefinition, hive)) {
            IndicatorDefinitionFileHelper.addCharacterMapping(hiveDefinition, hive, CHAR_NAME, CHAR_TOREPLACE, CHAR_REPLACE);
            isFrequencyAdded = IndicatorDefinitionFileHelper.save(hiveDefinition);
        }

        // Update CharacterMapping of hive to pattern low frenquency indicator
        IndicatorDefinition lowDefinition = definitionHandler.getDefinitionById(PATTERN_LOW_FREQUENCY_UUID);
        if (hiveDefinition != null && !IndicatorDefinitionFileHelper.isExistCharacterMappingWithLanguage(lowDefinition, hive)) {
            IndicatorDefinitionFileHelper.addCharacterMapping(lowDefinition, hive, CHAR_NAME, CHAR_TOREPLACE, CHAR_REPLACE);
            isLowFrequencyAdded = IndicatorDefinitionFileHelper.save(lowDefinition);
        }

        DefinitionHandler.getInstance().reloadIndicatorsDefinitions();

        if (!isFrequencyAdded || !isLowFrequencyAdded) {
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
        return createDate(2016, 11, 30);
    }

}
