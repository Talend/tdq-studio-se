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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataquality.properties.TDQJrxmlItem;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Duplicate a Jrxml file
 */
public class JrxmlFileDuplicateHandle extends AbstractTDQFileDuplicateHandle {

    JrxmlFileDuplicateHandle(IRepositoryNode node) {
        super(node);
    }

    @Override
    protected Item createFileItemByDuplicateFile(IFile newFile, String fileExtension, String newName) {

        IPath makeRelativeTo = newFile.getFullPath().removeFirstSegments(1).removeLastSegments(1)
                .makeRelativeTo(ResourceManager.getJRXMLFolder().getFullPath().removeFirstSegments(1));
        return createJrxml(makeRelativeTo, newName, WorkspaceUtils.ifileToFile(file), fileExtension);
    }

    /**
     * create jrxml file.
     * 
     * @param path
     * @param label
     * @param initFile
     * @param extendtion
     * @return
     */
    public TDQJrxmlItem createJrxml(IPath path, String label, File initFile, String extendtion) {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(PluginConstant.EMPTY_STRING);
        property.setLabel(label);

        TDQJrxmlItem routineItem = org.talend.dataquality.properties.PropertiesFactory.eINSTANCE.createTDQJrxmlItem();
        routineItem.setProperty(property);
        routineItem.setExtension(extendtion);
        routineItem.setName(label);

        ByteArray byteArray = duplicateByteArray(initFile);
        routineItem.setContent(byteArray);
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            property.setId(repositoryFactory.getNextId());
            if (path != null) {
                repositoryFactory.createParentFoldersRecursively(ERepositoryObjectType.getItemType(routineItem), path);
            }
            repositoryFactory.create(routineItem, path);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return routineItem;
    }

    @SuppressWarnings("deprecation")
    private ByteArray duplicateByteArray(File initFile) {
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        InputStream stream = null;
        try {
            stream = initFile.toURL().openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);

            byteArray.setInnerContent(innerContent);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        String routineContent = new String(byteArray.getInnerContent());
        byteArray.setInnerContent(routineContent.getBytes());
        return byteArray;
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
            return ResourceManager.getOneFolder(EResourceConstant.JRXML_TEMPLATE);
        }
    }
}
