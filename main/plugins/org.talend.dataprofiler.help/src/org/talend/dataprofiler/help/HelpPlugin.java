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
package org.talend.dataprofiler.help;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class HelpPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.dataprofiler.help"; //$NON-NLS-1$

    public static final String HELP_FILE_SUFFIX = "html"; //$NON-NLS-1$

    // The shared instance
    private static HelpPlugin plugin;

    /**
     * The constructor
     */
    public HelpPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static HelpPlugin getDefault() {
        return plugin;
    }

    public String getIndicatorHelpContextID() {
        return PLUGIN_ID + ".mycontexthelpid"; //$NON-NLS-1$
    }

    public String getIndicatorSelectorHelpContextID() {
        return PLUGIN_ID + ".indicatorhelpcontext"; //$NON-NLS-1$
    }

    public String getPatternHelpContextID() {
        return PLUGIN_ID + ".helpPatternContext"; //$NON-NLS-1$
    }

    public String getDQRulesHelpContextID() {
        return PLUGIN_ID + ".helpDQRulesContext"; //$NON-NLS-1$
    }

    public String getAnalysisHelpContextID() {
        return PLUGIN_ID + ".helpAnalysisContext"; //$NON-NLS-1$
    }

    public String getUDIndicatorHelpContextID() {
        return PLUGIN_ID + ".helpUDIndicatorContext";//$NON-NLS-1$
    }

    public String getExpressionEditContextID() {
        return PLUGIN_ID + ".helpExpressionEditContext";//$NON-NLS-1$
    }
}
