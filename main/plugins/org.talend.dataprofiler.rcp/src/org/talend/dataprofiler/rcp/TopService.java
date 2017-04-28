// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.rcp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.ICoreService;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.utils.KeywordsValidator;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.utils.io.FilesUtils;

/**
 * 
 * @author zshen
 * 
 * This class is needed by ProxyRepositoryFactory, so if simple remove it will effect startup for TOP. If you came to
 * ProxyRepositoryFactory class you will find coreService variable and it need a service (which implements ICoreService)
 * to initialize itself. We have a CoreService calss in the org.talend.core plugin but in TOP we can't get it from
 * org.talend.core(TOP don't contain it). So we need another one to instead of it. And ICoreService is main responsible
 * to check Job name and logon project, logon project we have be done by myself and we don't have job on the TOP. So
 * TopService only is empty class.
 */
public class TopService implements ICoreService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#addWorkspaceTaskDone(java.lang.String)
     */
    @Override
    public void addWorkspaceTaskDone(String task) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#checkJob(java.lang.String)
     */
    @Override
    public boolean checkJob(String name) throws BusinessException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#createStatsLogAndImplicitParamter(org.talend.core.model.general.Project)
     */
    @Override
    public void createStatsLogAndImplicitParamter(Project project) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#deleteAllJobs(boolean)
     */
    @Override
    public void deleteAllJobs(boolean fromPluginModel) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#deleteRoutinefile(org.talend.core.model.repository.IRepositoryViewObject)
     */
    @Override
    public void deleteRoutinefile(IRepositoryViewObject objToDelete) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#filterSpecialChar(java.lang.String)
     */
    @Override
    public String filterSpecialChar(String input) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getColumnNameChanged(org.talend.core.model.metadata.IMetadataTable,
     * org.talend.core.model.metadata.IMetadataTable)
     */
    @Override
    public List<ColumnNameChanged> getColumnNameChanged(IMetadataTable oldTable, IMetadataTable newTable) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getContextFileNameForPerl(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getContextFileNameForPerl(String projectName, String jobName, String version, String context) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getContextFlagFromQueryUtils()
     */
    @Override
    public boolean getContextFlagFromQueryUtils() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getImageWithDocExt(java.lang.String)
     */
    @Override
    public Image getImageWithDocExt(String extension) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getImageWithSpecial(org.eclipse.swt.graphics.Image)
     */
    @Override
    public ImageDescriptor getImageWithSpecial(Image source) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getJavaJobFolderName(java.lang.String, java.lang.String)
     */
    @Override
    public String getJavaJobFolderName(String jobName, String version) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getJavaProjectFolderName(org.talend.core.model.properties.Item)
     */
    @Override
    public String getJavaProjectFolderName(Item item) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getLanTypeString()
     */
    @Override
    public String getLanTypeString() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getLastUser()
     */
    @Override
    public String getLastUser() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getNewMetadataColumns(org.talend.core.model.metadata.IMetadataTable,
     * org.talend.core.model.metadata.IMetadataTable)
     */
    @Override
    public List<ColumnNameChanged> getNewMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ICoreService#getParameterUNIQUENAME(org.talend.designer.core.model.utils.emf.talendfile.NodeType)
     */
    @Override
    public String getParameterUNIQUENAME(NodeType node) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getRemoveMetadataColumns(org.talend.core.model.metadata.IMetadataTable,
     * org.talend.core.model.metadata.IMetadataTable)
     */
    @Override
    public List<ColumnNameChanged> getRemoveMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getRootProjectNameForPerl(org.talend.core.model.properties.Item)
     */
    @Override
    public String getRootProjectNameForPerl(Item item) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getRoutineAndJars()
     */
    @Override
    public Map<String, List<LibraryInfo>> getRoutineAndJars() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getSpecificResourceInJavaProject(org.eclipse.core.runtime.IPath)
     */
    @Override
    public IResource getSpecificResourceInJavaProject(IPath path) throws CoreException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getSpecificResourceInPerlProject(org.eclipse.core.runtime.IPath)
     */
    @Override
    public IResource getSpecificResourceInPerlProject(IPath path) throws CoreException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#getTemplateString()
     */
    @Override
    public String getTemplateString() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#initializeForTalendStartupJob()
     */
    @Override
    public void initializeForTalendStartupJob() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#initializeTemplates()
     */
    @Override
    public Job initializeTemplates() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#isAlreadyBuilt(org.talend.core.model.general.Project)
     */
    @Override
    public boolean isAlreadyBuilt(Project project) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#isContainContextParam(java.lang.String)
     */
    @Override
    public boolean isContainContextParam(String code) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#isKeyword(java.lang.String)
     */
    @Override
    public boolean isKeyword(String word) {
        return KeywordsValidator.isKeyword(word);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#readWorkspaceTasksDone()
     */
    @Override
    public List<String> readWorkspaceTasksDone() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#removeItemRelations(org.talend.core.model.properties.Item)
     */
    @Override
    public void removeItemRelations(Item item) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#removeJobLaunch(org.talend.core.model.repository.IRepositoryViewObject)
     */
    @Override
    public void removeJobLaunch(IRepositoryViewObject objToDelete) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#setFlagForQueryUtils(boolean)
     */
    @Override
    public void setFlagForQueryUtils(boolean flag) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#syncAllRoutines()
     */
    @Override
    public void syncAllRoutines() throws SystemException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#syncLibraries(org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void syncLibraries(IProgressMonitor... monitorWrap) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#validateValueForDBType(java.lang.String)
     */
    @Override
    public String validateValueForDBType(String columnName) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#synchronizeMapptingXML()
     */
    @Override
    public void synchronizeMapptingXML() {
        // I do not know what the method is supposed to be doing to I do nothing.
    }

    @Override
    public IPreferenceStore getPreferenceStore() {
        // MOD qiongli 2011-4-11.bug 20115.
        return CorePlugin.getDefault().getPreferenceStore();
    }

    @Override
    public boolean isOpenedItemInEditor(IRepositoryViewObject object) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#deleteBeanfile(org.talend.core.model.repository.IRepositoryViewObject)
     */
    @Override
    public void deleteBeanfile(IRepositoryViewObject objToDelete) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#syncAllBeans()
     */
    @Override
    public void syncAllBeans() throws SystemException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ICoreService#convert(org.talend.core.model.metadata.builder.connection.MetadataTable)
     */
    @Override
    public IMetadataTable convert(MetadataTable originalTable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void syncLog4jSettings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void syncMappingsFileFromSystemToProject() {
        RepositoryWorkUnit workUnit = new RepositoryWorkUnit("Sync mapping files from system to project") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                try {
                    File sysMappingfolder = new File(MetadataTalendType.getSystemForderURLOfMappingsFile().getPath());
                    IFolder projectMappingFolder = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject())
                            .getFolder(MetadataTalendType.PROJECT_MAPPING_FOLDER);
                    if (!projectMappingFolder.exists()) {
                        projectMappingFolder.create(true, true, null);
                    }
                    for (File in : sysMappingfolder.listFiles()) {
                        IFile out = projectMappingFolder.getFile(in.getName());
                        FilesUtils.copyFile(in, out.getLocation().toFile());
                    }
                } catch (SystemException | CoreException | IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }
}
