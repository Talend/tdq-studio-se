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
package org.talend.dq.analysis;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.talend.commons.emf.EMFUtil;

/**
 * @author scorreia
 * 
 * Files used are generated when running the Test application ConnectionIndicEvalMain.
 */
public final class MoveProviderMain {

    private MoveProviderMain() {
    }

    /**
     * DOC scorreia Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        // instantiate a new resource set
        EMFUtil util = new EMFUtil();
        ResourceSet rs = util.getResourceSet();

        // load data provider resource and store it into the resource set
        File file = new File("out/columnTest_0.1.ana");
        System.out.println("Loading file " + file.getAbsolutePath());
        Resource r = rs.getResource(URI.createFileURI(file.getAbsolutePath()), true);

        // resolve all proxies of the resource to be moved (catalogs and provider connection)
        // EcoreUtil.resolveAll(r);

        // get all external cross references and for each resolve all proxies (inverse links)
        // Map<EObject, Collection<Setting>> find =
        // EcoreUtil.ExternalCrossReferencer.find(util.getResourceSet().getResources()
        // .iterator().next());
        // for (EObject object : find.keySet()) {
        // Resource resource = object.eResource();
        // EcoreUtil.resolveAll(resource);
        // }

        // change uri for the loaded data provider resource
        URI destinationUri = URI.createFileURI("out/move");
        EMFUtil.changeUri(r, destinationUri);
        System.out.println("Destination = " + destinationUri);
        // delete original file
        file.delete();

        // save resource set
        util.save();

    }

}
