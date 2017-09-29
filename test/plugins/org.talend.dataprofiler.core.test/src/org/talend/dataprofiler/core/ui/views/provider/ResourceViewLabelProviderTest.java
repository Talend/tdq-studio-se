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
package org.talend.dataprofiler.core.ui.views.provider;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.helper.StatusHelper;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.IRepositoryFactory;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.RepositoryFactoryProvider;
import org.talend.core.repository.utils.ProjectHelper;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataprofiler.core.exception.ExceptionHandler;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.properties.impl.PropertiesFactoryImpl;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryConstants;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC zshen class global comment. Test the method
 * org.talend.dataprofiler.core.ui.views.provider.ResourceViewLabelProvider#getFileCount
 */

public class ResourceViewLabelProviderTest {

    private static String[] filterExtensions = { "ana", "rep" };//$NON-NLS-1$//$NON-NLS-2$

    final private String anaFolderName = "TDQ_Data Profiling/Analyses";//$NON-NLS-1$

    final private String repFolderName = "TDQ_Data Profiling/Reports";//$NON-NLS-1$

    Logger log = Logger.getLogger(ResourceViewLabelProviderTest.class);

    /**
     * Test method for
     * {@link org.talend.dataprofiler.core.ui.views.provider.ResourceViewLabelProvider#getFileCount(org.eclipse.core.resources.IFolder, java.lang.String[])}
     * 
     */
    @Test
    public void testGetFileCount() {
        log.debug("##############testGetFileCount start");
        IFolder repFolder = initFolder(EResourceConstant.REPORTS.getPath());
        IFolder anaFolder = initFolder(EResourceConstant.ANALYSIS.getPath());
        ResourceViewLabelProvider reViewLabelProvider = new ResourceViewLabelProvider();
        int analysisNum = reViewLabelProvider.getFileCount(anaFolder, filterExtensions);
        int reportNum = reViewLabelProvider.getFileCount(repFolder, filterExtensions);
        log.debug("##############AnalysisNum is" + analysisNum);
        log.debug("##############reportNum is" + reportNum);
        assertEquals(3, analysisNum);
        assertEquals(3, reportNum);
    }

    /**
     * 
     * DOC zshen Comment method "initFolder". init the folder which contain ana and rep files
     * 
     * @param folderName
     * @return
     */
    public IFolder initFolder(String folderName) {
        IProject rootProject = ReponsitoryContextBridge.getRootProject();
        if (!rootProject.exists()) {
            log.debug("##############ResourceViewLabelProviderTest enter initProxyRepository method by debug");
            initProxyRepository(rootProject);
        }

        if (DQStructureManager.getInstance().isNeedCreateStructure()) {
            DQStructureManager.getInstance().createDQStructure();
        }
        IFolder elementRootFolder = rootProject.getFolder(folderName);
        if (!elementRootFolder.exists()) {
            try {
                elementRootFolder.create(true, true, null);
            } catch (CoreException e) {
                Assert.fail(e.getMessage());
            }
        }

        // for analyses
        if (anaFolderName.equals(folderName)) {
            IFolder analysisFolder = createFolder(elementRootFolder, "ResourceViewLabelProviderTestAnalysisFolder");//$NON-NLS-1$
            IFolder subfolder1 = createFolder(analysisFolder, "subfolder1");//$NON-NLS-1$
            IFolder subfolder2 = createFolder(analysisFolder, "subfolder2");//$NON-NLS-1$
            elementRootFolder = analysisFolder;
            // logic delete one
            createAnalysis("a4", new Path(analysisFolder.getFullPath().lastSegment()), true);//$NON-NLS-1$
            // ~logic delete one
            createAnalysis("a1", new Path(analysisFolder.getFullPath().lastSegment()), false);//$NON-NLS-1$
            createAnalysis(
                    "a3", new Path(analysisFolder.getFullPath().lastSegment()).append(subfolder2.getFullPath().lastSegment()), false);//$NON-NLS-1$
            createAnalysis(
                    "a2", new Path(analysisFolder.getFullPath().lastSegment()).append(subfolder1.getFullPath().lastSegment()), false);//$NON-NLS-1$

        }
        // for reports
        if (repFolderName.equals(folderName)) {
            IFolder reportFolder = createFolder(elementRootFolder, "ResourceViewLabelProviderTestReportFolder");//$NON-NLS-1$
            IFolder subfolder1 = createFolder(reportFolder, "subfolder1");//$NON-NLS-1$
            IFolder subfolder2 = createFolder(reportFolder, "subfolder2");//$NON-NLS-1$
            elementRootFolder = reportFolder;
            // logic delete one
            createReport("a4", new Path(reportFolder.getFullPath().lastSegment()), true);//$NON-NLS-1$
            // ~logic delete one
            createReport("a1", new Path(reportFolder.getFullPath().lastSegment()), false);//$NON-NLS-1$
            createReport(
                    "a3", new Path(reportFolder.getFullPath().lastSegment()).append(subfolder2.getFullPath().lastSegment()), false);//$NON-NLS-1$
            createReport(
                    "a2", new Path(reportFolder.getFullPath().lastSegment()).append(subfolder1.getFullPath().lastSegment()), false);//$NON-NLS-1$
        }
        return elementRootFolder;
    }

    /**
     * DOC talend Comment method "initProxyRepository".
     */
    private void initProxyRepository(IProject rootProject) {
        Project project = null;

        ProxyRepositoryFactory proxyRepository = ProxyRepositoryFactory.getInstance();
        IRepositoryFactory repository = RepositoryFactoryProvider.getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID);
        if (repository == null) {
            log.fatal(DefaultMessagesImpl
                    .getString("No local Repository found! Probably due to a missing plugin in the product.")); //$NON-NLS-1$
        }
        proxyRepository.setRepositoryFactoryFromProvider(repository);
        try {
            proxyRepository.checkAvailability();
            proxyRepository.initialize();

            XmiResourceManager xmiResourceManager = new XmiResourceManager();

            if (rootProject.getFile(FileConstants.LOCAL_PROJECT_FILENAME).exists()) {
                // Initialize TDQ EMF model packages.
                new EMFUtil();
                project = new Project(xmiResourceManager.loadProject(rootProject));
            } else {
                User user = org.talend.core.model.properties.impl.PropertiesFactoryImpl.eINSTANCE.createUser();
                user.setLogin("talend@talend.com"); //$NON-NLS-1$
                user.setPassword("talend@talend.com".getBytes()); //$NON-NLS-1$
                String projectName = ResourceManager.getRootProjectName();
                String projectDesc = ResourcesPlugin.getWorkspace().newProjectDescription(projectName).getComment();
                Project projectInfor = ProjectHelper.createProject(projectName, projectDesc, ECodeLanguage.JAVA.getName(), user);

                // MOD zshen create project by proxyRepository
                checkFileName(projectInfor.getLabel(), RepositoryConstants.PROJECT_PATTERN);

                project = proxyRepository.getRepositoryFactoryFromProvider().createProject(projectInfor);

            }

            if (project != null) {
                initRepositoryContext(project);

                // add status
                String defaultTechnicalStatusList = "DEV development;TEST testing;PROD production"; //$NON-NLS-1$
                List<Status> statusList = StatusHelper.parse(defaultTechnicalStatusList);
                proxyRepository.setTechnicalStatus(statusList);
            }

        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            log.error(e, e);
            Assert.fail(e.getMessage());
        }

    }

    private void initRepositoryContext(Project project) {

        RepositoryContext repositoryContext = new RepositoryContext();
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        ctx.putProperty(Context.REPOSITORY_CONTEXT_KEY, repositoryContext);

        repositoryContext.setUser(project.getAuthor());
        repositoryContext.setClearPassword(project.getLabel());
        repositoryContext.setProject(project);
        repositoryContext.setFields(new HashMap<String, String>());
        //        repositoryContext.getFields().put(IProxyRepositoryFactory.BRANCH_SELECTION + "_" + project.getTechnicalLabel(), ""); //$NON-NLS-1$ //$NON-NLS-2$
        ProjectManager.getInstance().setMainProjectBranch(project, null);

        ReponsitoryContextBridge.initialized(project.getEmfProject(), project.getAuthor());
        // MOD zshen for bug tdq-4757 remove this init from corePlugin.start() to here because the initLocal command of
        // commandLine
        // SqlExplorerUtils.getDefault().initSqlExplorerRootProject();
    }

    /**
     * 
     * DOC zshen Comment method "checkFileName".
     * 
     * @param fileName
     * @param pattern
     * 
     * copy the method from ProxyRepositoryFactory to avoid tos migeration.
     */
    private void checkFileName(String fileName, String pattern) {
        if (!Pattern.matches(pattern, fileName)) {
            Assert.fail(DefaultMessagesImpl.getString("ProxyRepositoryFactory.illegalArgumentException.labelNotMatchPattern", //$NON-NLS-1$
                    new Object[] { fileName, pattern }));
            throw new IllegalArgumentException(DefaultMessagesImpl.getString(
                    "ProxyRepositoryFactory.illegalArgumentException.labelNotMatchPattern", new Object[] { fileName, pattern })); //$NON-NLS-1$
        }
    }

    private void createReport(String name, IPath createPath, Boolean isDelete) {

        Report report1 = ReportHelper.createReport(name);
        TDQReportItem item1 = PropertiesFactoryImpl.eINSTANCE.createTDQReportItem();
        org.talend.core.model.properties.Property property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setId(EcoreUtil.generateUUID());
        property1.setItem(item1);
        property1.setLabel(report1.getName());
        item1.setProperty(property1);
        item1.setReport(report1);
        ItemState itemState = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(isDelete);
        item1.setState(itemState);

        try {
            ProxyRepositoryFactory.getInstance().create(item1, createPath, false);
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }

    }

    private void createAnalysis(String name, IPath createPath, Boolean isDelete) {
        Analysis analysis1 = AnalysisHelper.createAnalysis(name);
        TDQAnalysisItem item1 = PropertiesFactoryImpl.eINSTANCE.createTDQAnalysisItem();
        org.talend.core.model.properties.Property property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setId(EcoreUtil.generateUUID());
        property1.setItem(item1);
        property1.setLabel(analysis1.getName());
        item1.setProperty(property1);
        item1.setAnalysis(analysis1);
        ItemState itemState = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createItemState();
        itemState.setDeleted(isDelete);
        item1.setState(itemState);
        AnalysisResult analysisResult1 = AnalysisFactory.eINSTANCE.createAnalysisResult();
        analysis1.setResults(analysisResult1);
        try {
            ProxyRepositoryFactory.getInstance().create(item1, createPath, false);
        } catch (PersistenceException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * 
     * DOC zshen Comment method "createFolder". create the subfolder under the parentFolder and named for folderName
     * 
     * @param parentFolder
     * @param folderName
     * @return
     */
    public IFolder createFolder(IFolder parentFolder, String folderName) {
        IFolder currFolder = parentFolder.getFolder(folderName);
        if (!currFolder.exists()) {
            try {
                currFolder.create(true, true, null);
            } catch (CoreException e) {
                Assert.fail(e.getMessage());
            }
        }
        return currFolder;
    }

    /**
     * 
     * DOC zshen Comment method "createFile". create the file under the parentFolder and named for filName
     * 
     * @param parentFolder
     * @param filName
     * @return
     */
    public IFile createFile(IFolder parentFolder, String filName) {
        IFile file = parentFolder.getFile(filName);
        IPath parentPath = parentFolder.getFullPath().removeFirstSegments(1);
        if (!file.exists()) {
            try {
                InputStream resourceAsStream = ResourceViewLabelProviderTest.class.getClassLoader().getResourceAsStream(
                        "/temp/" + parentPath.toOSString() + "/" + filName);//$NON-NLS-1$//$NON-NLS-2$
                file.create(resourceAsStream, true, null);
            } catch (CoreException e) {
                Assert.fail(e.getMessage());
            }
        }
        return file;
    }
}
