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
package org.talend.commons.emf;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.talend.model.emf.CwmResourceFactory;

/**
 * DOC sizhaoliu class global comment. Detailled comment
 */
public class EmfFileResourceUtil {

    private ResourceSet resourceSet;

    private static Logger log = Logger.getLogger("EmfFileResourceUtil");

    private static EmfFileResourceUtil instance;

    public static EmfFileResourceUtil getInstance() {
        if (instance == null) {
            instance = new EmfFileResourceUtil();
        }
        return instance;
    }

    private EmfFileResourceUtil() {
        EMFUtil util = new EMFUtil();
        resourceSet = util.getResourceSet();

        // ADD sizhaoliu TDQ-6698 used by tDqReportRun jobs, the project name and description need to be read from
        // "talend.project" file.
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        CwmResourceFactory cwmFactory = new CwmResourceFactory();
        reg.getExtensionToFactoryMap().put("project", cwmFactory);//$NON-NLS-1$
        reg.getExtensionToFactoryMap().put("properties", cwmFactory);//$NON-NLS-1$
    }

    public Resource getFileResource(String string) {
        URI uri = URI.createFileURI(new File(string).getAbsolutePath());
        Resource res = resourceSet.getResource(uri, true);
        return res;
    }

}
