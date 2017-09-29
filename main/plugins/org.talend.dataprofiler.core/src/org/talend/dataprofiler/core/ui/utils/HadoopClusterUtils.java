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
package org.talend.dataprofiler.core.ui.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.service.IHadoopClusterService;
import org.talend.dq.helper.AbstractOSGIServiceUtils;
import org.talend.repository.model.RepositoryNode;

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

    public void initConnectionParameters(Map<String, String> initMap, RepositoryNode node) {
        if (getHadoopClusterService() != null) {
            getHadoopClusterService().initConnectionParameters(initMap, node);
        }
    }

    public IWizard createHDFSWizard(IWorkbench iWorkbench, boolean creation, RepositoryNode node, String[] existingNames) {
        if (getHadoopClusterService() != null) {
            return (IWizard) getHadoopClusterService().createHDFSWizard(iWorkbench, creation, node, existingNames);
        }
        return null;
    }

    public IWizard createHadoopClusterWizard(IWorkbench iWorkbench, boolean creation, RepositoryNode node, String[] existingNames) {
        if (getHadoopClusterService() != null) {
            return (IWizard) getHadoopClusterService().createHadoopClusterWizard(iWorkbench, creation, node, existingNames);
        }
        return null;
    }

    public Map<Object, Object> createHiveTable(RepositoryNode node) {
        if (getHadoopClusterService() != null) {
            return getHadoopClusterService().createHiveTable(node);
        }
        return null;
    }

    public boolean hideAction(RepositoryNode node) {
        if (getHadoopClusterService() != null) {
            return getHadoopClusterService().hideAction(node);
        }
        return true;
    }

    public IAction createActionOfHiveTable(RepositoryNode node) {
        if (getHadoopClusterService() != null) {
            return (IAction) getHadoopClusterService().createActionOfHiveTable(node);
        }
        return null;
    }

    public IAction createActionOfRetrieveHDFS(RepositoryNode node) {
        if (getHadoopClusterService() != null) {
            return (IAction) getHadoopClusterService().createActionOfRetrieveHDFS(node);
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
