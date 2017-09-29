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
package org.talend.dataprofiler.ecos.proxy;

import java.util.HashMap;

import org.apache.axis.AxisProperties;
import org.apache.axis.components.net.TransportClientProperties;

/**
 * 
 * DOC mzhao class global comment. Detailled comment
 */
public class TransportClientPropertiesFactory {

    private static HashMap<String, TransportClientProperties> cache = new HashMap<String, TransportClientProperties>();

    private static HashMap<String, Class<?>> defaults = new HashMap<String, Class<?>>();

    static {
        defaults.put("http", DefaultHTTPTransportClientProperties.class); //$NON-NLS-1$
        defaults.put("https", DefaultHTTPSTransportClientProperties.class); //$NON-NLS-1$
    }

    public static TransportClientProperties create(String protocol) {
        TransportClientProperties tcp = (TransportClientProperties) cache.get(protocol);

        if (tcp == null) {
            tcp = (TransportClientProperties) AxisProperties.newInstance(TransportClientProperties.class, (Class<?>) defaults
                    .get(protocol));

            if (tcp != null) {
                cache.put(protocol, tcp);
            }
        }

        return tcp;
    }
}
