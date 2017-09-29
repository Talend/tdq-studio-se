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
package org.talend.dq.writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.cwm.softwaredeployment.SoftwaredeploymentPackage;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public final class EMFSharedResources {

    private static Logger log = Logger.getLogger(EMFSharedResources.class);

    private static EMFSharedResources instance;

    private EMFUtil emfUtil;

    private XmiResourceManager resourceManager;

    private ResourceSet resourceSet;

    private Resource softwareDeploymentResource = null;

    private EMFSharedResources() {
        resourceManager = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider().getResourceManager();
        resourceSet = resourceManager.resourceSet;

        emfUtil = new EMFUtil();
        emfUtil.setResourceSet(resourceSet);
    }

    /**
     * Getter for instance.
     * 
     * @return the instance
     */
    public static synchronized EMFSharedResources getInstance() {
        if (instance == null) {
            instance = new EMFSharedResources();
        }
        return instance;
    }

    /**
     * DOC bZhou Comment method "reloadResource".
     * 
     * @param uri
     */
    public synchronized Resource reloadResource(URI uri) {
        unloadResource(uri.toString());
        return getResource(uri, true);
    }

    /**
     * reload resource of softwareDeployment
     * 
     * @param uri
     */
    public synchronized Resource reloadsoftwareDeploymentResource() {
        if (softwareDeploymentResource == null) {
            return this.getSoftwareDeploymentResource();
        }
        URI uri = softwareDeploymentResource.getURI();
        softwareDeploymentResource = reloadResource(uri);
        return softwareDeploymentResource;
    }

    /**
     * Method "unloadResources" unloads and removes all the resources from the resource set.
     */
    public synchronized void unloadResources() {
        List<Resource> resources = new ArrayList<Resource>(resourceSet.getResources());
        for (Resource resource : resources) {
            resource.unload();
            resourceSet.getResources().remove(resource);
        }
    }

    /**
     * Method "unloadResource" unload and remove the specification resource from the resource set.
     * 
     * @param uriString the uri sting of resource.
     */
    public synchronized void unloadResource(String uriString) {
        List<Resource> resources = new ArrayList<Resource>(resourceSet.getResources());
        for (Resource res : resources) {
            if (uriString.equals(res.getURI().toString())) {
                res.unload();
                resourceSet.getResources().remove(res);
            }
        }
    }

    /**
     * Method "addEObjectToResourceSet".
     * 
     * @param filePath the file path to the resource which contains the given object
     * @param eObject an EMF object to save in the appropriate resource
     * @return true when ok
     */
    public boolean addEObjectToResourceSet(String filePath, EObject eObject) {
        return this.emfUtil.addPoolToResourceSet(filePath, eObject);
    }

    public synchronized Resource getResource(URI uri, boolean loadOnDemand) {
        try {
            return resourceSet.getResource(uri, loadOnDemand);
        } catch (Exception e) {
            log.error("The file " + uri.lastSegment() + " cannot be loaded. ", e); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }
    }

    public Resource getResource(IFile file, boolean loadOnDemand) {
        URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);
        return getResource(uri, loadOnDemand);
    }

    /**
     * 
     * DOC mzhao Comment method "createResource".
     * 
     * @param uri
     * @return
     */
    public Resource createResource(URI uri) {
        return resourceSet.createResource(uri);
    }

    /**
     * Method "saveAll" saves all the resources of the resourceSet.
     * 
     * @return true when ok
     */
    public synchronized boolean saveAll() {
        return this.emfUtil.save();
    }

    /**
     * Method "saveResource" saves the resources of the resourceSet.
     * 
     * @return true when ok
     */
    public boolean saveResource(Resource resource) {
        try {
            resourceManager.saveResource(resource);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    /**
     * Save the resource to destinationUri and saved the related resources.
     * 
     * @param res
     * @param destinationUri
     * @return
     */
    public URI saveToUri(Resource res, URI destinationUri) {

        // resolve all proxies of the resource to be moved
        EcoreUtil.resolveAll(res);

        // get all external cross references and for each resolve all proxies
        // (inverse links)
        Map<EObject, Collection<Setting>> find = EcoreUtil.ExternalCrossReferencer.find(res);
        List<Resource> needSaves = new ArrayList<Resource>();
        for (EObject object : find.keySet()) {
            Resource resource = object.eResource();
            if (resource == null) {
                continue;
            }
            EcoreUtil.resolveAll(resource);
            needSaves.add(resource);
        }

        URI changeUri = EMFUtil.changeUri(res, destinationUri);
        needSaves.add(res);
        for (Resource toSave : needSaves) {
            saveResource(toSave);
        }
        return changeUri;
    }

    /**
     * DOC scorreia Comment method "getSharedEmfUtil".
     * 
     * @return
     * @deprecated do not use directly EMFUtil.
     * 
     */
    @Deprecated
    public static EMFUtil getSharedEmfUtil() {
        return getInstance().emfUtil;
    }

    /**
     * 
     * DOC zshen Comment method "setUsePlatformRelativePath".
     * 
     * @param usePlatformRelativePath
     */
    public void setUsePlatformRelativePath(boolean usePlatformRelativePath) {
        emfUtil.setUsePlatformRelativePath(usePlatformRelativePath);
    }

    /**
     * 
     * DOC zshen Comment method "addPoolToResourceSet".
     * 
     * @param eObject
     * @param path
     */
    public boolean addPoolToResourceSet(String path, EObject eObject) {
        return emfUtil.addPoolToResourceSet(path, eObject);
    }

    /**
     * Method "getLastErrorMessage".
     * 
     * @return the last error message or null when none exists.
     */
    public String getLastErrorMessage() {
        return this.emfUtil.getLastErrorMessage();
    }

    public EObject copyEObject(EObject oldObject) {
        EObject newObject = EcoreUtil.copy(oldObject);

        return newObject;
    }

    public ResourceSet getResourceSet() {
        return resourceSet;
    }

    /**
     * Getter for softwareDeploymentResource.
     * 
     * @return the softwareDeploymentResource
     */
    public Resource getSoftwareDeploymentResource() {
        if (softwareDeploymentResource == null) {
            softwareDeploymentResource = initSoftwareDeploymentResource();
        }
        return this.softwareDeploymentResource;
    }

    public boolean saveSoftwareDeploymentResource(TdSoftwareSystem softwareSystem) {
        getSoftwareDeploymentResource().getContents().add(softwareSystem);
        return saveSoftwareDeploymentResource();
    }

    public boolean saveSoftwareDeploymentResource() {
        return (softwareDeploymentResource != null) ? EMFUtil.saveSingleResource(softwareDeploymentResource) : false;
    }

    /**
     * DOC scorreia Comment method "initSoftwareDeploymentResource".
     * 
     * @return
     */
    private Resource initSoftwareDeploymentResource() {
        // MOD mzhao 2009-03-23,Feature 6066
        String softwareFile = ".softwaresystem." + SoftwaredeploymentPackage.eNAME; //$NON-NLS-1$
        String softwarePath = ResourceManager.getLibrariesFolder().getFullPath().append(softwareFile).toString();
        URI sUri = URI.createPlatformResourceURI(softwarePath, false);
        Resource resource = resourceSet.getResource(sUri, false);
        IFile softwareDeploymentFile = WorkspaceUtils.getModelElementResource(sUri);
        if (!softwareDeploymentFile.exists()) {
            resource = resourceSet.createResource(sUri);
        }
        if (resource == null) {
            resource = EMFSharedResources.getInstance().reloadResource(sUri);
        }
        return resource;
    }

    public void changeUri(Resource resource, URI destinationUri) {
        URI uri = resource.getURI();
        URI newUri = destinationUri.appendSegment(uri.lastSegment());

        // unloadResource(newUri.toString());

        resource.setURI(newUri);
    }

    public boolean isNeedReload(Resource resource) {
        if (resource == null || resource.getContents().isEmpty()) {
            return true;
        }

        Map<EObject, Collection<Setting>> referencerMap = EcoreUtil.CrossReferencer.find(resource.getContents());
        for (EObject object : referencerMap.keySet()) {
            if (object.eIsProxy()) {
                return true;
            }
        }

        return false;
    }

    public ModelElement reloadModelElementInNode(IRepositoryNode repNode) {
        try {
            IRepositoryViewObject repViewObj = repNode.getObject();
            String id = repViewObj.getProperty().getId();

            URI uri = repViewObj.getProperty().getItem().eResource().getURI();

            ProxyRepositoryFactory.getInstance().unloadResources(repViewObj.getProperty());
            String fileExtension = repViewObj.getProperty().getItem().getFileExtension();
            String removeEnd = StringUtils.removeEnd(uri.path(), "." + FactoriesUtil.PROPERTIES_EXTENSION); //$NON-NLS-1$

            ProxyRepositoryFactory.getInstance().unloadResources(uri.scheme() + ":" + removeEnd + "." + fileExtension); //$NON-NLS-1$ //$NON-NLS-2$

            IRepositoryViewObject lastVersion = ProxyRepositoryFactory.getInstance().getLastVersion(id);
            Item item = lastVersion.getProperty().getItem();
            return PropertyHelper.getModelElement(item.getProperty());
        } catch (PersistenceException e) {
            log.error("reload model element error: " + e.getMessage()); //$NON-NLS-1$
            return null;
        }
    }
}
