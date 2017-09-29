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
package org.talend.dq.helper;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.service.IHadoopClusterService;

/**
 * created by yyin on 2015年6月25日 Detailled comment
 *
 */
public class HadoopClusterUtils extends AbstractOSGIServiceUtils {

    private IHadoopClusterService hcService;

    private static HadoopClusterUtils hcUtils;

    public static HadoopClusterUtils getDefault() {
        if (hcUtils == null) {
            hcUtils = new HadoopClusterUtils();
        }
        return hcUtils;
    }

    @Override
    public String getServiceName() {
        return IHadoopClusterService.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dq.helper.AbstractOSGIServiceUtils#isServiceInstalled()
     */
    @Override
    public boolean isServiceInstalled() {
        return getHadoopClusterService() != null && hcService.isBigData();
    }

    IHadoopClusterService getHadoopClusterService() {
        if (this.hcService == null) {
            initService(false);
        }
        return this.hcService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setService(BundleContext context, ServiceReference serviceReference) {
        Object obj = context.getService(serviceReference);
        if (obj != null) {
            this.hcService = (IHadoopClusterService) obj;
        }
    }

    public String getHadoopClusterID(Object object) {
        if (getHadoopClusterService() != null) {
            return getHadoopClusterService().getHadoopClusterID(object);
        }
        return StringUtils.EMPTY;
    }

    public ERepositoryObjectType getHDFSType() {
        if (getHadoopClusterService() != null) {
            return (ERepositoryObjectType) getHadoopClusterService().getHDFSType();
        }
        return null;
    }

    public ERepositoryObjectType getHadoopClusterType() {
        if (getHadoopClusterService() != null) {
            return (ERepositoryObjectType) getHadoopClusterService().getHadoopClusterType();
        }
        return null;
    }

    @Override
    public String getPluginName() {
        return null;
    }

    @Override
    protected String getMissingMessageName() {
        return null;
    }

    @Override
    protected String getRestartMessageName() {
        return null;
    }

}
