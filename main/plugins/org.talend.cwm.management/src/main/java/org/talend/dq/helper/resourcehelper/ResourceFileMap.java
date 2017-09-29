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
package org.talend.dq.helper.resourcehelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dq.writer.AElementPersistance;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC rli class global comment. Detailled comment
 * 
 */
public abstract class ResourceFileMap {

    private static Logger log = Logger.getLogger(ResourceFileMap.class);

    public static List<ModelElement> getAll() {
        List<ModelElement> all = new ArrayList<ModelElement>();

        all.addAll(AnaResourceFileHelper.getInstance().getAllElement());
        all.addAll(DQRuleResourceFileHelper.getInstance().getAllElement());
        all.addAll(IndicatorResourceFileHelper.getInstance().getAllElement());
        all.addAll(PatternResourceFileHelper.getInstance().getAllElement());
        all.addAll(PrvResourceFileHelper.getInstance().getAllElement());
        if (!PluginChecker.isOnlyTopLoaded()) {
            all.addAll(RepResourceFileHelper.getInstance().getAllElement());
        }

        return all;
    }

    /**
     * DOC bZhou Comment method "getFileResource".
     * 
     * @param file
     * @return get null if the file is not exist.
     */
    public synchronized Resource getFileResource(IFile file) {
        if (file.exists()) {
            URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);

            Resource res = EMFSharedResources.getInstance().getResource(uri, false);

            if (EMFSharedResources.getInstance().isNeedReload(res)) {
                res = EMFSharedResources.getInstance().reloadResource(uri);
            }

            return res;
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "findCorrespondingFile".
     * 
     * @param element
     * @return
     */
    public static IFile findCorrespondingFile(ModelElement element) {
        assert element != null;

        URI uri = null;
        if (element.eIsProxy()) {
            uri = ((InternalEObject) element).eProxyURI();
        } else {
            uri = element.eResource().getURI();
        }

        if (uri != null && uri.isPlatform()) {
            IPath path = new Path(uri.toPlatformString(false));
            return ResourceManager.getRoot().getFile(path);
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "getModelElement".
     * 
     * @param IFile
     * @return
     */
    public final ModelElement getModelElement(IFile file) {
        return getModelElement(getFileResource(file));
    }

    /**
     * DOC bZhou Comment method "getModelElement".
     * 
     * @param resource
     * @return
     */
    public final ModelElement getModelElement(Resource resource) {
        if (resource != null) {
            EList<EObject> contents = resource.getContents();
            if (contents.isEmpty()) {
                log.error(Messages.getString("ResourceFileMap.NOCONTENT") + resource);//$NON-NLS-1$
            }
            if (log.isDebugEnabled()) {
                log.debug("Nb elements in contents " + contents.size());//$NON-NLS-1$
                log.debug("for URI: " + resource.getURI());//$NON-NLS-1$
            }

            for (EObject object : contents) {
                ModelElement switchObject = doSwitch(object);
                if (switchObject != null) {
                    return switchObject;
                }
            }
        }

        return null;
    }

    /**
     * DOC bZhou Comment method "getAll".
     * 
     * @param parentFolder
     * @return
     */
    public List<? extends ModelElement> getAllElement(IFolder parentFolder) {
        List<ModelElement> elementList = new ArrayList<ModelElement>();
        try {
            List<IFile> allIFiles = searchAllIFiles(parentFolder);
            // MOD qiongli 2011-4-19.bug 20566,avoid NPE
            ModelElement mod = null;
            for (IFile file : allIFiles) {
                mod = getModelElement(file);
                if (mod != null) {
                    // MOD msjian TDQ-4672 2012-2-17: modify another issue
                    elementList.add(mod);
                    // TDQ-4672~
                }
            }
        } catch (CoreException e) {
            log.error(e);
        }

        return elementList;
    }

    /**
     * DOC bZhou Comment method "getAllElement".
     * 
     * @return
     */
    public List<? extends ModelElement> getAllElement() {
        return getAllElement(getTypedFolder());
    }

    /**
     * DOC bZhou Comment method "save".
     * 
     * @param dqrule
     * @return
     * @deprecated use {@link AElementPersistance#save(org.talend.core.model.properties.Item, boolean)} class instead
     */
    @Deprecated
    public ReturnCode save(ModelElement element) {
        AElementPersistance writer = ElementWriterFactory.getInstance().create(element);
        return writer.save(element);
    }

    private List<IFile> searchAllIFiles(IFolder folder) throws CoreException {
        List<IFile> fileList = new ArrayList<IFile>();

        for (IResource resource : folder.members()) {
            if (resource.getType() == IResource.FOLDER) {
                fileList.addAll(searchAllIFiles(folder.getFolder(resource.getName())));
                continue;
            }
            IFile file = (IFile) resource;

            if (checkFile(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    public abstract IFolder getTypedFolder();

    public abstract ModelElement doSwitch(EObject object);

    protected abstract boolean checkFile(IFile file);

}
