// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.imex.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.migration.helper.WorkspaceVersionHelper;
import org.talend.dataprofiler.core.ui.utils.DqFileUtils;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.resource.ResourceManager;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class FileSystemExportWriter implements IExportWriter {

    private static Logger log = Logger.getLogger(FileSystemExportWriter.class);

    private IPath basePath;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImexWriter#populate(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * [], boolean)
     */
    public ItemRecord[] populate(ItemRecord[] elements, boolean checkExisted) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImexWriter#mapping(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * )
     */
    public Map<IPath, IPath> mapping(ItemRecord record) {
        Map<IPath, IPath> toExportMap = new HashMap<IPath, IPath>();

        // item
        IPath itemResPath = new Path(record.getFile().getAbsolutePath());
        // TDQ-11349 get path from property, should not contain path "repositories\1638449237\master..."
        Property property = record.getProperty();
        IPath itemDesPath = null;
        if (property != null && DqFileUtils.isLocalProjectFile(record.getFile())) {
            // TDQ-11370: get a relative path to make the export structure correctly.
            itemDesPath = PropertyHelper.getItemPath(property).makeRelative();
        } else {
            itemDesPath =
                    itemResPath.makeRelativeTo(record.getRootFolderPath().removeLastSegments(1));
        }

        // property
        IPath propResPath = record.getPropertyPath();
        IPath propDesPath = itemDesPath.removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

        toExportMap.put(itemResPath, itemDesPath);
        if (!propResPath.toFile().exists()) {
            return toExportMap;
        }
        toExportMap.put(propResPath, propDesPath);

        return toExportMap;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImexWriter#write(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * [], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void write(ItemRecord[] records, IProgressMonitor monitor) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        try {
            Set<IPath> projectPathes = new HashSet<IPath>();
            List<IProject> expProjects = new ArrayList<IProject>();
            for (ItemRecord record : records) {

                if (monitor.isCanceled()) {
                    break;
                }

                Map<IPath, IPath> toImportMap = mapping(record);

                monitor.subTask(DefaultMessagesImpl.getString("FileSystemExportWriter.Export", record.getName()));//$NON-NLS-1$

                if (record.isValid()) {

                    //log.info("Exporting " + record.getFile().getAbsolutePath());//$NON-NLS-1$

                    for (IPath resPath : toImportMap.keySet()) {
                        IPath desPath = toImportMap.get(resPath);
                        synchronized (ProxyRepositoryFactory
                                .getInstance()
                                .getRepositoryFactoryFromProvider()
                                .getResourceManager().resourceSet) {
                            write(resPath, desPath);
                        }
                    }
                    projectPathes.add(record.getRootFolderPath());
                } else {// may not come here check if have time
                    for (ImportMessage error : record.getErrors()) {
                        if (EMessageType.ERROR == error.getType()) {
                            log.error(error.toString());
                        }
                    }
                }

                monitor.worked(1);
            }
            // compute which projects are selected
            for (IPath projectPat : projectPathes) {
                IFile projDir = ResourceManager.getRoot().getFile(projectPat);
                IProject project = ReponsitoryContextBridge.findProject(projDir.getName());
                if (project.exists()) {
                    expProjects.add(project);
                }
            }

            finish(records, expProjects);

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#write(org.eclipse.core.runtime.IPath,
     * org.eclipse.core.runtime.IPath)
     */
    public void write(IPath resPath, IPath desPath) throws IOException, CoreException {
        File resFile = resPath.toFile();
        File desFile = this.basePath.append(desPath).toFile();

        if (resFile.exists()) {
            FilesUtils.copyFile(resFile, desFile);
        } else {
            log.warn(DefaultMessagesImpl.getString("FileSystemExportWriter.ExportFail", resFile.getAbsolutePath()));//$NON-NLS-1$
        }
    }

    public void finish(ItemRecord[] records) throws IOException, CoreException {
        List<IProject> projects = new ArrayList<>();
        projects.add(ResourceManager.getRootProject());
        finish(records, projects);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImexWriter#finish(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * [])
     */
    public void finish(ItemRecord[] records, List<IProject> projects) throws IOException, CoreException {
        for (IProject project : projects) {
            IFile projFile = project.getFile(FileConstants.LOCAL_PROJECT_FILENAME);// named 'talend.project'

            writeSysFile(projFile);

            IFile definitonFile = DefinitionHandler.getTalendDefinitionFile(project);
            writeSysFile(definitonFile);

            IFile versionFile = WorkspaceVersionHelper.getVersionFile(project);
            writeSysFile(versionFile);
        }

        ItemRecord.clear();

    }

    /**
     * DOC bZhou Comment method "writeSysFile".
     *
     * @param file
     * @throws IOException
     * @throws CoreException
     */
    private void writeSysFile(IFile file) throws IOException, CoreException {
        if (file.exists()) {
            write(file.getLocation(), file.getFullPath().makeRelative());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#computeInput(org.eclipse.core.runtime.IPath)
     */
    public ItemRecord computeInput(IPath path) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        File file =
                path == null ? ResourceManager.getRootProject().getLocation().toFile() : workspace
                        .getRoot()
                        .getFolder(path)
                        .getLocation()
                        .toFile();

        return new ItemRecord(file);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#setBasePath(org.eclipse.core.runtime.IPath)
     */
    public void setBasePath(IPath path) {
        if (path == null) {
            this.basePath = null;
        } else {
            this.basePath = new Path(path.toString().trim());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#getBasePath()
     */
    public IPath getBasePath() {
        return this.basePath;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#check()
     */
    public List<String> check() {
        List<String> errors = new ArrayList<String>();

        if (basePath == null || !basePath.toFile().exists()) {
            errors.add(DefaultMessagesImpl.getString("FileSystemExportWriter.RootNotExist"));//$NON-NLS-1$
        }

        return errors;
    }
}
