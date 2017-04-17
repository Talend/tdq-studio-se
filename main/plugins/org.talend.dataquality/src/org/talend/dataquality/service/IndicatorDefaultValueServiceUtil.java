// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.service;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.dataquality.DataQualityPlugin;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class IndicatorDefaultValueServiceUtil {

    private IIndicatorDefaultValueService indicatorDV = null;

    private static IndicatorDefaultValueServiceUtil instance = null;

    private IndicatorDefaultValueServiceUtil() {

    }

    public static IndicatorDefaultValueServiceUtil getIstance() {
        if (instance == null) {
            instance = new IndicatorDefaultValueServiceUtil();
        }
        return instance;
    }

    public IIndicatorDefaultValueService getIndicatorDVService() {
        DataQualityPlugin defaultDQPlugin = DataQualityPlugin.getDefault();
        if (indicatorDV == null && defaultDQPlugin != null) {
            BundleContext context = defaultDQPlugin.getBundleContext();
            if (context != null) {
                ServiceReference serviceReference = context.getServiceReference(IIndicatorDefaultValueService.class.getName());
                if (serviceReference != null) {
                    Object obj = context.getService(serviceReference);
                    if (obj != null) {
                        this.indicatorDV = (IIndicatorDefaultValueService) obj;
                    }
                }
            }
        }
        return this.indicatorDV;
    }
}
