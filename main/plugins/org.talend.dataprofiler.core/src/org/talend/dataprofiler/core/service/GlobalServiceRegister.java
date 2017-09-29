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
package org.talend.dataprofiler.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;

/**
 * A global service register provides the service registration and acquirement. <br/>
 * 
 * 
 */
public class GlobalServiceRegister {

    // The shared instance
    private static GlobalServiceRegister instance = new GlobalServiceRegister();

    private static IConfigurationElement[] configurationElements;

    private static IConfigurationElement[] bandingConfigurationElements;

    private static IConfigurationElement[] svnRepositoryElements;

    public static GlobalServiceRegister getDefault() {
        return instance;
    }

    private Map<Class<?>, IService> services = new HashMap<Class<?>, IService>();

    private Map<Class<?>, AbstractSvnRepositoryService> svnRepositoryServices = new HashMap<Class<?>, AbstractSvnRepositoryService>();

    private Map<Class<?>, org.talend.core.ui.branding.IBrandingService> brandingServices = new HashMap<Class<?>, org.talend.core.ui.branding.IBrandingService>();

    private Map<Class<?>, List<IService>> serviceGroups = new HashMap<Class<?>, List<IService>>();

    static {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        if (registry != null) {
            configurationElements = registry.getConfigurationElementsFor("org.talend.dataprofiler.core.service"); //$NON-NLS-1$
            bandingConfigurationElements = registry.getConfigurationElementsFor("org.talend.core.runtime.service"); //$NON-NLS-1$
            svnRepositoryElements = registry.getConfigurationElementsFor("org.talend.dataprofiler.core.svnRepositoryService"); //$NON-NLS-1$
        }
    }

    /**
     * Gets the specific IService.
     * 
     * @param klass the Service type you want to get
     * @return IService IService
     */
    public IService getService(Class<?> klass) {
        IService service = services.get(klass);
        if (service == null && configurationElements != null) {
            service = findService(klass);
            if (service == null) {
                throw new RuntimeException(DefaultMessagesImpl.getString(
                        "GlobalServiceRegister.cannotFindService", klass.getName())); //$NON-NLS-1$
            }
            services.put(klass, service);
        }
        return service;
    }

    /**
     * 
     * zshen adjust whether the service has been registered.
     * 
     * @param klass
     * @return
     */
    public boolean isServiceRegistered(Class klass) {
        IService service = services.get(klass);
        if (service == null) {
            service = findService(klass);
            if (service == null) {
                return false;
            }
            services.put(klass, service);
        }
        return true;
    }

    /**
     * Gets the specific IService.overide klliu 2010-09-15 bug 15520.
     * 
     * @param klass the Service type you want to get
     * @return IService IService
     */
    public org.talend.core.ui.branding.IBrandingService getBrandingService(Class<?> klass) {
        org.talend.core.ui.branding.IBrandingService service = brandingServices.get(klass);
        if (service == null && bandingConfigurationElements != null) {
            service = findBrandingService(klass);
            if (service == null) {
                throw new RuntimeException(DefaultMessagesImpl.getString(
                        "GlobalServiceRegister.cannotFindService", klass.getName())); //$NON-NLS-1$
            }
            brandingServices.put(klass, service);
        }
        return service;
    }

    /**
     * 
     * DOC mzhao Get svn repository service.
     * 
     * @param klass
     * @return
     */
    public AbstractSvnRepositoryService getSvnRepositoryService(Class<?> klass) {
        AbstractSvnRepositoryService service = svnRepositoryServices.get(klass);
        if (service == null) {
            service = findSvnRepositoryService(klass);
            if (service != null) {
                svnRepositoryServices.put(klass, service);
            }
        }
        return service;
    }

    private AbstractSvnRepositoryService findSvnRepositoryService(Class<?> klass) {
        // String key = klass.getName();
        for (int i = 0; i < svnRepositoryElements.length; i++) {
            IConfigurationElement element = svnRepositoryElements[i];
            if (element == null) {
                continue;
            }
            try {
                Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                if (klass.isInstance(service)) {
                    return (AbstractSvnRepositoryService) service;
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    /**
     * Gets the specific IService group.
     * 
     * @param klass the Service type you want to get
     * @return List
     */
    public List<IService> getServiceGroup(Class<?> klass) {
        List<IService> serviceGroup = serviceGroups.get(klass);
        if (serviceGroup == null) {
            serviceGroup = findServiceGroup(klass);
            if (serviceGroup == null) {
                return new ArrayList<IService>();
            }
            serviceGroups.put(klass, serviceGroup);
        }
        return serviceGroup;
    }

    /**
     * Finds the specific service from the list. overide klliu 2010-09-15 bug 15520
     * 
     * @param klass the interface type want to find.
     * @return IService
     */
    private org.talend.core.ui.branding.IBrandingService findBrandingService(Class<?> klass) {
        String key = klass.getName();
        for (int i = 0; i < bandingConfigurationElements.length; i++) {
            IConfigurationElement element = bandingConfigurationElements[i];
            String id = element.getAttribute("serviceId"); //$NON-NLS-1$

            if (!key.endsWith(id)) {
                continue;
            }
            try {
                Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                if (klass.isInstance(service)) {
                    return (org.talend.core.ui.branding.IBrandingService) service;
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    private IService findService(Class<?> klass) {
        String key = klass.getName();
        for (int i = 0; i < configurationElements.length; i++) {
            IConfigurationElement element = configurationElements[i];
            String id = element.getAttribute("serviceId"); //$NON-NLS-1$

            if (!key.endsWith(id)) {
                continue;
            }
            try {
                Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                if (klass.isInstance(service)) {
                    return (IService) service;
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    /**
     * Finds the special service group from the list.
     * 
     * @param klass the interface type want to find.
     * @return List
     */
    private List<IService> findServiceGroup(Class<?> klass) {
        String key = klass.getName();
        List<IService> serviceGroup;
        if (configurationElements == null || configurationElements.length == 0) {
            return null;
        }
        serviceGroup = new ArrayList<IService>();
        for (int i = 0; i < configurationElements.length; i++) {
            IConfigurationElement element = configurationElements[i];
            String id = element.getAttribute("serviceId"); //$NON-NLS-1$

            if (!key.endsWith(id)) {
                continue;
            }
            try {
                Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                if (klass.isInstance(service)) {
                    serviceGroup.add((IService) service);
                }
            } catch (CoreException e) {
                ExceptionHandler.process(e);
            }
        }
        return serviceGroup;
    }
}
