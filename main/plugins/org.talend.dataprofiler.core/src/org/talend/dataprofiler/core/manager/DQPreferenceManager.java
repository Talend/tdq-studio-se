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
package org.talend.dataprofiler.core.manager;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.pref.WebBrowserPreferencePage;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class DQPreferenceManager {

    /**
     * the default value for the "block help" option.
     */
    public static final boolean BLOCK_HELP_DEFAULT = false;

    /**
     * DOC bZhou DQPreferenceManager constructor comment.
     */
    private DQPreferenceManager() {

    }

    /**
     * DOC bZhou Comment method "isBlockWeb".
     * 
     * @return
     */
    public static boolean isBlockWeb() {
        return Platform.getPreferencesService().getBoolean(CorePlugin.PLUGIN_ID, WebBrowserPreferencePage.BLOCK_WEB_BROWSER,
                BLOCK_HELP_DEFAULT, new IScopeContext[] { InstanceScope.INSTANCE });
    }
}
