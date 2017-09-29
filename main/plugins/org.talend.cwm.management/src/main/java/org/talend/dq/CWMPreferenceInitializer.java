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
package org.talend.dq;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.dq.analysis.memory.AnalysisThreadMemoryChangeNotifier;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class CWMPreferenceInitializer extends AbstractPreferenceInitializer {

    public CWMPreferenceInitializer() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initializeDefaultPreferences() {
        IEclipsePreferences prefs = DefaultScope.INSTANCE.getNode(CWMPlugin.getDefault().getBundle().getSymbolicName());
        prefs.putBoolean(PluginConstant.CONNECTION_TIMEOUT, false);
        prefs.putBoolean(PluginConstant.FILTER_TABLE_VIEW_COLUMN, true);
        PlatformUI.getPreferenceStore().setDefault(AnalysisThreadMemoryChangeNotifier.ANALYSIS_AUTOMATIC_MEMORY_CONTROL, false);

    }

}
