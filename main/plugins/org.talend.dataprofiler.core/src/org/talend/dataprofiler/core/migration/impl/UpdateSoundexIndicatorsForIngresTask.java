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
 * DOC qiongli class global comment. Detailled comment
 */
public class UpdateSoundexIndicatorsForIngresTask extends AbstractWorksapceUpdateTask {

    private static final String SOUNDEX_FREQUENCY = "Soundex Frequency Table"; //$NON-NLS-1$

    private static final String SOUNDEX_LOW_FREQUENCY = "Soundex Low Frequency Table"; //$NON-NLS-1$

    private static final String INGRES = SupportDBUrlType.INGRESDEFAULTURL.getLanguage();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        IndicatorDefinition definitionHigh = IndicatorDefinitionFileHelper.getSystemIndicatorByName(SOUNDEX_FREQUENCY);
        IndicatorDefinitionFileHelper
                .addSqlExpression(
                        definitionHigh,
                        INGRES,
                        "SELECT MAX(<%=__COLUMN_NAMES__%>), SOUNDEX(<%=__COLUMN_NAMES__%>) , COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 2 ORDER BY d DESC,c DESC"); //$NON-NLS-1$
        IndicatorDefinition definitionLow = IndicatorDefinitionFileHelper.getSystemIndicatorByName(SOUNDEX_LOW_FREQUENCY);
        IndicatorDefinitionFileHelper
                .addSqlExpression(
                        definitionLow,
                        INGRES,
                        "SELECT MAX(<%=__COLUMN_NAMES__%>), SOUNDEX(<%=__COLUMN_NAMES__%>) , COUNT(*) c, COUNT(DISTINCT <%=__COLUMN_NAMES__%>) d FROM <%=__TABLE_NAME__%> t <%=__WHERE_CLAUSE__%> GROUP BY 2 ORDER BY d,c ASC"); //$NON-NLS-1$

        return IndicatorDefinitionFileHelper.save(definitionHigh) & IndicatorDefinitionFileHelper.save(definitionLow);
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
        return createDate(2010, 8, 25);
    }

}
