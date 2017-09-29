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
package net.sourceforge.sqlexplorer.service;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.sqlexplorer.Messages;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;



/**
 * DOC qiongli class global comment. A global service register provides the service registration and acquirement.
 */
public class GlobalServiceRegister {

    private static GlobalServiceRegister instance = new GlobalServiceRegister();;


    private Map<Class, IService> services = new HashMap<Class, IService>();
    private static Logger log = Logger.getLogger(GlobalServiceRegister.class);

    private static IExtensionRegistry registry = Platform.getExtensionRegistry();

    private static IConfigurationElement[] configurationElements = registry == null ? null : registry
            .getConfigurationElementsFor("net.sourceforge.sqlexplorer.saveAs"); //$NON-NLS-1$

    public static GlobalServiceRegister getDefault() {
        return instance;
    }

    /**
     * DOC qiongli Comment method "getService".Gets the specific IService.
     * 
     * @param klass the Service type you want to get
     * @return IService IService
     */
    public IService getService(Class klass) {
        IService service = services.get(klass);
        if (service == null && configurationElements != null) {
            service = findService(klass);
            if (service == null){
                throw new RuntimeException(Messages.getString("GlobalServiceRegister.ServiceNotRegistered", klass.getName())); //$NON-NLS-1$ //$NON-NLS-2$
                //                throw new RuntimeException("The service has not been registered."); //$NON-NLS-1$ //$NON-NLS-2$
            }
            services.put(klass, service);
        }
        return service;
    }

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
     * DOC qiongli Comment method "findService".Finds the specific service from the list.
     * 
     * @param klass the interface type want to find.
     * @return IService
     */
    private IService findService(Class klass) {
        String key = klass.getName();
        for (int i = 0; i < configurationElements.length; i++) {
            IConfigurationElement element = configurationElements[i];
            String id = element.getAttribute("saveAsServiceId"); //$NON-NLS-1$

            if (!key.endsWith(id)) {
                continue;
            }
            try {
                Object service = element.createExecutableExtension("class"); //$NON-NLS-1$
                if (klass.isInstance(service)) {
                    return (IService) service;
                }
            } catch (CoreException e) {
                // ExceptionHandler.process(e);
                e.printStackTrace();
            }
        }
        return null;
    }


}
