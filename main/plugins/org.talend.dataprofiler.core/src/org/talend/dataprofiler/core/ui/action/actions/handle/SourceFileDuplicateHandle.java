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
package org.talend.dataprofiler.core.ui.action.actions.handle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Duplicate a SQL source file
 */
public class SourceFileDuplicateHandle extends AbstractTDQFileDuplicateHandle {

    SourceFileDuplicateHandle(IRepositoryNode node) {
        super(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.actions.handle.IDuplicateHandle#duplicate(java.lang.String)
     */
    @Override
    protected Item createFileItemByDuplicateFile(IFile newFile, String fileExtension, String newName) {

        IPath makeRelativeTo = newFile.getFullPath().removeFirstSegments(1).removeLastSegments(1)
                .makeRelativeTo(ResourceManager.getSourceFileFolder().getFullPath().removeFirstSegments(1));
        return DQStructureManager.getInstance().createSourceFileItem(WorkspaceUtils.ifileToFile(file), makeRelativeTo, newName,
                PluginConstant.SQL_STRING);
    }

    /**
     * DOC msjian Comment method "extractFolder".
     * 
     * @param oldItem
     * @param oldModelElement
     * @return
     */
    @Override
    protected IFolder extractFolder(Item oldItem, ModelElement oldObject) {
        boolean inCurrentMainProject = ProjectManager.getInstance().isInCurrentMainProject(oldItem);
        if (inCurrentMainProject) {
            Resource resource = oldItem.eResource();
            IPath path = new Path(resource.getURI().toPlatformString(false));
            IFile oldFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
            return (IFolder) oldFile.getParent();
        } else {
            // for the reference project node, we get its folder in current project.
            return ResourceManager.getOneFolder(EResourceConstant.SOURCE_FILES);
        }
    }

}
