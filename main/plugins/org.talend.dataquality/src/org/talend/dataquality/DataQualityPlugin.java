package org.talend.dataquality;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class DataQualityPlugin extends Plugin {

    private static DataQualityPlugin plugin = null;

    private BundleContext bundleContext = null;

    public static DataQualityPlugin getDefault() {
        if (plugin == null && !Platform.isRunning()) {
            plugin = new DataQualityPlugin();
        }
        return plugin;
    }

    public BundleContext getBundleContext() {
        return this.bundleContext;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.bundleContext = context;
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        this.bundleContext = null;
        plugin = null;
    }

}
