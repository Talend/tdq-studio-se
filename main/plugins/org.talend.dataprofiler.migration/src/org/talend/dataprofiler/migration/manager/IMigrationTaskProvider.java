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
package org.talend.dataprofiler.migration.manager;

import org.talend.dataprofiler.migration.IMigrationTask;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public interface IMigrationTaskProvider {

    public String EXTENSION_ID = "org.talend.dataprofiler.migration"; //$NON-NLS-1$

    public String ATTR_CLASS = "class"; //$NON-NLS-1$

    public String ATTR_VERSION = "version"; //$NON-NLS-1$

    public String ATTR_NAME = "name"; //$NON-NLS-1$

    public String ATTR_PID = "id"; //$NON-NLS-1$

    public IMigrationTask[] getMigrationTasks();
}
