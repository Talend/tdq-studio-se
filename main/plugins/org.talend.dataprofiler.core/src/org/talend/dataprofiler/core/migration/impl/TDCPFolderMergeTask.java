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
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.StringUtils;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

/**
 * 
 * DOC mzhao 2009-03-18 Migration task for merge tdq/top system to tos. All folders existing in tdq/tos before must all
 * be moved to one project, default project name is {@link org.talend.dataquality.PluginConstant#rootProjectName}
 */
public class TDCPFolderMergeTask extends AbstractWorksapceUpdateTask {

    Logger logger = Logger.getLogger(TDCPFolderMergeTask.class);

    public TDCPFolderMergeTask() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        // Create one project.
        IProject rootProject = ResourceManager.getRootProject();
        if (!rootProject.exists()) {
            rootProject = DQStructureManager.getInstance().createNewProject(ResourceManager.getRootProjectName());
        }

        // Copy "top level" folders already as projects in TOP/TDQ into this
        // project.
        IResource[] resources = ResourcesPlugin.getWorkspace().getRoot().members();

        if (resources != null && resources.length > 0) {
            for (IResource resource : resources) {
                // copy three folders:
                if (resource.getName().equals("Data Profiling") //$NON-NLS-1$
                        || resource.getName().equals("Libraries") //$NON-NLS-1$
                        || resource.getName().equals("Metadata")) { //$NON-NLS-1$
                    IPath destination = null;
                    IFolder prefixFolder = rootProject.getFolder(RepositoryNodeHelper.PREFIX_TDQ + resource.getName());
                    prefixFolder.create(IResource.FORCE, true, new NullProgressMonitor());
                    for (IResource rs : ((IProject) resource).members()) {
                        if (rs.getName().equals(".project")) { //$NON-NLS-1$
                            continue;
                        }
                        destination = prefixFolder.getFolder(rs.getName()).getFullPath();
                        rs.copy(destination, IResource.FORCE, new NullProgressMonitor());
                    }
                    resource.delete(true, new NullProgressMonitor());
                }
            }
        }
        // Reporting_db
        String pathName = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString() + "/reporting_db/"; //$NON-NLS-1$
        File repFolder = ResourceManager.getReportDBFolder().getLocation().toFile();
        if (repFolder.exists()) {
            FileUtils.copyDirectory(repFolder, ResourceManager.getReportDBFolder().getLocation().toFile());
            FileUtils.forceDelete(new File(pathName));
        }
        // ~MOD mzhao 2009-04-28, upgrade .prv,.ana,rep files.
        return fileContentUpgrade(rootProject);
    }

    private boolean fileContentUpgrade(IProject rootProject) throws CoreException {
        String[] extensions = { FactoriesUtil.ANA, FactoriesUtil.PROV, FactoriesUtil.REP, FactoriesUtil.SOFTWARE_SYSTEM };
        boolean recursive = true;
        Collection<?> files = FileUtils.listFiles(new File(rootProject.getLocation().toOSString()), extensions, recursive);
        for (Object name2 : files) {
            File file = (File) name2;
            if (file != null) {
                try {
                    String content = FileUtils.readFileToString(file, EMFUtil.ENCODING);
                    content = StringUtils.replace(content, "/Metadata/", "/" + EResourceConstant.METADATA.getName() + "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    content = StringUtils.replace(content, "/Libraries/", "/" + EResourceConstant.LIBRARIES.getName() + "/"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    content = StringUtils.replace(content, "/resource/" + EResourceConstant.LIBRARIES.getName() + "/", //$NON-NLS-1$ //$NON-NLS-2$
                            "/resource/" + ResourceManager.getRootProjectName() + "/" + EResourceConstant.LIBRARIES.getName() //$NON-NLS-1$ //$NON-NLS-2$
                                    + "/"); //$NON-NLS-1$
                    content = StringUtils.replace(content, "/Data Profiling/", "/" + EResourceConstant.DATA_PROFILING.getName() //$NON-NLS-1$ //$NON-NLS-2$
                            + "/"); //$NON-NLS-1$

                    FileUtils.writeStringToFile(file, content, EMFUtil.ENCODING);
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                    return false;
                } catch (Throwable e) {
                    ExceptionHandler.process(e);
                    return false;
                }
            }
        }
        return true;
    }

    public Date getOrder() {
        return createDate(1949, 10, 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.dataprofiler.core.migration.IWorkspaceMigrationTask# getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.STUCTRUE;
    }

}
