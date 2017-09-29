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
package org.talend.dataprofiler.core.ui.action;

import org.talend.core.repository.model.ProxyRepositoryFactory;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class CheatSheetActionHelper {

    private CheatSheetActionHelper() {
    }

    /**
     * DOC xqliu Comment method "canRun".
     * 
     * @return
     */
    public static boolean canRun() {
        boolean result = true;
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            result = false;
        }
        return result;
    }
}
