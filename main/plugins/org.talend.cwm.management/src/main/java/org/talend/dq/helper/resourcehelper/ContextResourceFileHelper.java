package org.talend.dq.helper.resourcehelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

import orgomg.cwm.objectmodel.core.ModelElement;

public class ContextResourceFileHelper extends ResourceFileMap<ModelElement> {

    private static ContextResourceFileHelper instance;

    private static Logger log = Logger.getLogger(ContextResourceFileHelper.class);

    private ContextResourceFileHelper() {
        super();
    }

    public static ContextResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new ContextResourceFileHelper();
        }
        return instance;
    }

    /**
     * GetAllContexts method is used to get all of Contexts
     * 
     * @return A list of context and make sure get news from system. Empty list if any exception generated.
     * 
     */
    public List<IFile> getAllContexts() {
        try {
            return searchAllIFiles(getTypedFolder());
        } catch (CoreException e) {
            log.error(e);
        }
        return new ArrayList<IFile>();
    }

    public List<IFile> getAllContextFromProject(IProject project) {
        try {
            return searchAllIFiles(ResourceManager.getOneFolder(project, EResourceConstant.CONTEXT));
        } catch (CoreException e) {
            log.error(e);
        }
        return new ArrayList<IFile>();
    }

    @Override
    protected List<IFile> searchAllIFiles(IFolder folder) throws CoreException {
        return super.searchAllIFiles(folder);
    }

    @Override
    public IFolder getTypedFolder() {
        return ResourceManager.getContextFolder();
    }

    @Override
    public ModelElement doSwitch(EObject object) {
        return null;
    }

    @Override
    protected boolean checkFile(IFile file) {
        return file != null && FactoriesUtil.ITEM_EXTENSION.equalsIgnoreCase(file.getFileExtension());
    }

}
