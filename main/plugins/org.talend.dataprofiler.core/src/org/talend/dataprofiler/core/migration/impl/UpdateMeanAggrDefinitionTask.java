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

/**
 * to update the aggregatedDefinitions of Mean Indicator, this task should be the same order of
 * UpdateAggrDefinitionTask.
 */
public class UpdateMeanAggrDefinitionTask extends UpdateAggrDefinitionTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.impl.UpdateAggrDefinitionTask#initializtion()
     */
    @Override
    protected void initializtion() {
        needUpateKeys = new String[] { "Mean" }; //$NON-NLS-1$

        String[] meanArray = new String[] { "Row Count", "Sum" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("Mean", meanArray); //$NON-NLS-1$
    }
}
