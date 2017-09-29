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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.talend.dataprofiler.ecos.EcosPlugin;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class EcosystemProxyAdapter {

    private static Logger log = Logger.getLogger(EcosystemProxyAdapter.class);

    private EcosystemProxyAdapter() {

    }

    /**
     * DOC bZhou Comment method "adapt".
     * 
     * @param httpclient
     * @param url
     */
    public static void adapt(HttpClient httpclient, String url) {
        IProxyService proxyService = EcosPlugin.getDefault().getProxyService();

        IProxyData proxyData = null;

        try {
            IProxyData[] proxyDatas = proxyService.select(new URI(url));
            if (proxyDatas != null && proxyDatas.length > 0) {
                proxyData = proxyDatas[0];
            }
        } catch (URISyntaxException e) {
            log.error(e, e);
        }

        if (proxyData == null) {
            proxyData = proxyService.getProxyData(IProxyData.HTTP_PROXY_TYPE);
        }

        if (proxyData != null & StringUtils.isNotEmpty(proxyData.getHost())) {
            // use proxy to connect
            ProxyHost host = new ProxyHost(proxyData.getHost(), proxyData.getPort());
            httpclient.getHostConfiguration().setProxyHost(host);
            httpclient.getParams().setAuthenticationPreemptive(true);

            String userId = proxyData.getUserId();
            if (StringUtils.isNotEmpty(userId)) {
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userId, proxyData.getPassword());
                httpclient.getState().setProxyCredentials(AuthScope.ANY, credentials);
            }
        }
    }
}
