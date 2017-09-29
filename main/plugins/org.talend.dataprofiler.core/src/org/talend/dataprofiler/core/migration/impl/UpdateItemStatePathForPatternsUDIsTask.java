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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.PropertyHelper;
import org.talend.resource.ResourceManager;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UpdateItemStatePathForPatternsUDIsTask extends AbstractWorksapceUpdateTask {

    private Logger log = Logger.getLogger(UpdateItemStatePathForPatternsUDIsTask.class);

    public Date getOrder() {
        return createDate(2012, 06, 07);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        try {
            updateItemStatePath(ResourceManager.getPatternRegexFolder(), ERepositoryObjectType.TDQ_PATTERN_REGEX);
            updateItemStatePath(ResourceManager.getPatternSQLFolder(), ERepositoryObjectType.TDQ_PATTERN_SQL);
            updateItemStatePath(ResourceManager.getUDIFolder(), ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS);
        } catch (Exception e) {
            log.error(e, e);
            result = false;
        }
        return result;
    }

    /**
     * DOC xqliu Comment method "updateItemStatePath".
     * 
     * @param folder
     * @param type
     */
    private void updateItemStatePath(IFolder folder, ERepositoryObjectType type) throws Exception {
        for (IResource resource : folder.members()) {
            if (resource instanceof IFolder) {
                IFolder subFolder = (IFolder) resource;
                updateItemStatePath(subFolder, type);
            }

            if (resource instanceof IFile) {
                IFile propFile = (IFile) resource;
                if (FactoriesUtil.PROPERTIES_EXTENSION.equals(propFile.getFileExtension())) {
                    Property property = PropertyHelper.getProperty(propFile);
                    if (property != null) {
                        ItemState itemState = property.getItem().getState();
                        if (itemState != null) {
                            String itemStatePath1 = itemState.getPath();
                            String itemStatePath2 = getItemStatePath(propFile, type);
                            if (!itemStatePath1.equals(itemStatePath2)) {
                                itemState.setPath(itemStatePath2);
                                Resource propResource = getResource(WorkspaceUtils.ifileToFile(propFile));
                                EList<EObject> contents = propResource.getContents();
                                for (EObject eObject : contents) {
                                    if (eObject instanceof ItemState) {
                                        eObject = itemState;
                                    }
                                }
                                EMFUtil.saveResource(propResource);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * DOC xqliu Comment method "getItemStatePath".
     * 
     * @param propFile
     * @param type
     * @return
     */
    private String getItemStatePath(IFile propFile, ERepositoryObjectType type) {
        IPath basePath = Path.fromOSString(type.getFolder()).removeLastSegments(1);
        IPath folderPath = propFile.getParent().getFullPath().removeFirstSegments(1);
        return folderPath.removeFirstSegments(basePath.segmentCount()).toString();
    }
}
