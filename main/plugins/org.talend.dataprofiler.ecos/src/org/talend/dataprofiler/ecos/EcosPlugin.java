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
package org.talend.dataprofiler.ecos;

import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class EcosPlugin extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.dataprofiler.ecos"; //$NON-NLS-1$

    // The shared instance
    private static EcosPlugin plugin;

    // The Service tracker
    private ServiceTracker tracker;

    /**
     * The constructor
     */
    public EcosPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        initProxyService();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * DOC bZhou Comment method "initProxyService".
     */
    private void initProxyService() {
        tracker = new ServiceTracker(getBundle().getBundleContext(), IProxyService.class.getName(), null);
        tracker.open();
    }

    /**
     * DOC bZhou Comment method "getProxyService".
     * 
     * @return
     */
    public IProxyService getProxyService() {
        return (IProxyService) tracker.getService();
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static EcosPlugin getDefault() {
        return plugin;
    }

}
