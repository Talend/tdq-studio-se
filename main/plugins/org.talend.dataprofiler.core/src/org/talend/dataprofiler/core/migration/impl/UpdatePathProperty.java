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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.properties.Property;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.PropertyHelper;

/**
 * 
 * zshen update the value of path attribute on the perproties file.
 * 
 */
public class UpdatePathProperty extends AbstractWorksapceUpdateTask {

    private List<File> fileList;

    private static Logger log = Logger.getLogger(UpdatePathProperty.class);

    private FilenameFilter propertyFileFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return !dir.getName().startsWith(".") && name.endsWith(FactoriesUtil.PROPERTIES_EXTENSION); //$NON-NLS-1$
        }

        // private boolean isUserCreateDir(File dir) {
        // if (dir.isDirectory()) {
        // IPath workspacePath = UpdatePathProperty.this.getWorkspacePath();
        // IPath dirPath = new Path(dir.getPath());
        // if (EResourceConstant.isConstantPath(dirPath.makeRelativeTo(workspacePath))) {
        // return false;
        // }
        // }
        // return true;
        // }
    };

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2011, 2, 14);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        boolean ok = true;
        EMFUtil emfUtil = new EMFUtil();

        for (File file : fileList) {
            if (file.isFile()) {
                URI uri = URI.createFileURI(file.getAbsolutePath());

                if (log.isDebugEnabled()) {
                    log.debug("---------Translate " + uri.toString()); //$NON-NLS-1$
                }

                Property property = null;
                try {
                    Resource resource = emfUtil.getResourceSet().getResource(uri, true);

                    for (EObject object : resource.getContents()) {
                        if (object instanceof Property) {
                            property = (Property) object;
                            break;
                        }

                    }
                } catch (Exception e) {
                    log.warn(DefaultMessagesImpl.getString("UpdatePathProperty_UpdateFailed", file.getAbsolutePath()), e); //$NON-NLS-1$
                    ok = false;
                }

                if (property != null) {
                    try {
                        saveObject(emfUtil, file, uri, property);
                    } catch (Exception e) {
                        log.error(DefaultMessagesImpl.getString("UpdatePathProperty_UpdateError", property, file, uri), e); //$NON-NLS-1$
                        ok = false;
                    }
                }
            }
        }
        return ok;
    }

    private void saveObject(EMFUtil emfUtil, File file, URI uri, Property oldPropery) {

        String statePathStr = PropertyHelper.computePath(oldPropery, file);
        oldPropery.getItem().getState().setPath(statePathStr);

        Resource propResource = emfUtil.getResourceSet().getResource(uri, true);
        EMFUtil.saveResource(propResource);
    }

    @Override
    public boolean valid() {
        fileList = new ArrayList<File>();
        for (File folder : getTopFolderList()) {
            FilesUtils.getAllFilesFromFolder(folder, fileList, propertyFileFilter);
        }
        return !fileList.isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#isModelTask()
     */
    @Override
    public Boolean isModelTask() {
        return true;
    }
}
