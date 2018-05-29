package org.talend.dq.helper.resourcehelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;


public class ContextResourceFileHelper extends ResourceFileMap {

    private static ContextResourceFileHelper instance;

    private static Logger log = Logger.getLogger(ContextResourceFileHelper.class);

    private List<IFile> contextList;

    private ContextResourceFileHelper() {
        super();
        contextList = new ArrayList<IFile>();
    }

    public static ContextResourceFileHelper getInstance() {
        if (instance == null) {
            instance = new ContextResourceFileHelper();
        }
        return instance;
    }

    public List<IFile> getAllContexts() {
        if (contextList.isEmpty()) {
            try {
                contextList = searchAllIFiles(getTypedFolder());
            } catch (CoreException e) {
                log.error(e);
            }
        }
        return contextList;
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
