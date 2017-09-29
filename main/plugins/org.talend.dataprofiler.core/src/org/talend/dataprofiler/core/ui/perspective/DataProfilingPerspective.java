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
package org.talend.dataprofiler.core.ui.perspective;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * DOC rli class global comment. Detailled comment <br/>
 * 
 */
public class DataProfilingPerspective implements IPerspectiveFactory {

    private static Logger log = Logger.getLogger(DataProfilingPerspective.class);

    public void createInitialLayout(IPageLayout layout) {
        if (log.isDebugEnabled()) { // MODSCA 2008-03-13
            log.debug("Creating Data profiling perspective's layout."); //$NON-NLS-1$
        }
    }
}
