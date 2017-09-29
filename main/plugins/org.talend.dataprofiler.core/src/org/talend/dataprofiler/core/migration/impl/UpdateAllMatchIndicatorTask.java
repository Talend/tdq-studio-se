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

import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.TalendDefinitionFileUpdate;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class UpdateAllMatchIndicatorTask extends AbstractWorksapceUpdateTask {

    String separator = System.getProperty("line.separator"); //$NON-NLS-1$

    String allMatchIndicatorDefinitions = "  <indicatorDefinitions xmi:id=\"_JoeMkM-jEd6qN5aKpPNGTh\" name=\"All Match\" label=\"All Match\" categories=\"_ccKHEBF2Ed2PKb6nEJEvhw\">" //$NON-NLS-1$
            + separator
            + "    <taggedValue xmi:id=\"_JoezoW-jEd6qN5aKpPNGTg\" tag=\"Description\" value=\"All Match\"/>" //$NON-NLS-1$
            + separator
            + "    <taggedValue xmi:id=\"_JoezoA-jEd6qN5aKpPNGTg\" tag=\"Purpose\" value=\"All Match\"/>" //$NON-NLS-1$
            + separator
            + "    <sqlGenericExpression xmi:id=\"_Joezos-jEd6qN5aKpPNGTg\" body=\"SELECT &lt;%=__COLUMN_NAMES__%> FROM &lt;%=__TABLE_NAME__%> &lt;%=__WHERE_CLAUSE__%> GROUP BY &lt;%=__GROUP_BY_ALIAS__%>\" language=\"SQL\"/>" //$NON-NLS-1$
            + separator + "  </indicatorDefinitions>"; //$NON-NLS-1$

    String endLine = "</dataquality.indicators.definition:IndicatorsDefinitions>"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        TalendDefinitionFileUpdate talendDefinitionFileUpdate = new TalendDefinitionFileUpdate();
        if (-1 == talendDefinitionFileUpdate.indexOf("name=\"All Match\"")) { //$NON-NLS-1$
            talendDefinitionFileUpdate.add(endLine, allMatchIndicatorDefinitions + separator + endLine);
            return talendDefinitionFileUpdate.replace(this.getClass().getName());
        }

        return false;
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
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2010, 6, 9);
    }
}
