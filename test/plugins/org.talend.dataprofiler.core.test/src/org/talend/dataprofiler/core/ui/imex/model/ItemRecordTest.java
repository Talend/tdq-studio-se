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
package org.talend.dataprofiler.core.ui.imex.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.helper.StatusHelper;
import org.talend.core.model.repository.ERepositoryObjectType;
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
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisFactory;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.NullCountIndicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.definition.DefinitionFactory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.PropertiesFactory;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.writer.impl.AnalysisWriter;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryConstants;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC talend class global comment. Detailled comment
 */
public class ItemRecordTest {

    private Logger log = Logger.getLogger(ItemRecordTest.class);

    /**
     * Test method for {@link org.talend.dataprofiler.core.ui.imex.model.ItemRecord#loadProperty()}.
     * 
     * @throws PersistenceException
     */
    @Test
    public void testLoadProperty() throws PersistenceException {

        chooseRightProject();
        Property analysisProperty = createAnalysis("ItemRecordTestanalysis1"); //$NON-NLS-1$
        TDQAnalysisItem item = (TDQAnalysisItem) analysisProperty.getItem();
        Analysis analysis = item.getAnalysis();
        AnalysisResult createAnalysisResult = analysis.getResults();
        Assert.assertEquals(0, createAnalysisResult.getIndicators().size());
        // create Indicator
        RowCountIndicator rowCountIndicator = IndicatorsFactory.eINSTANCE.createRowCountIndicator();
        String rowCountPropertyID = EcoreUtil.generateUUID();
        saveIndicatorDefintion(rowCountPropertyID, "ItemRecordWithRefreshedTestIndicatorDefinition1"); //$NON-NLS-1$
        rowCountIndicator.setIndicatorDefinition(((TDQIndicatorDefinitionItem) ProxyRepositoryFactory.getInstance()
                .getLastVersion(rowCountPropertyID).getProperty().getItem()).getIndicatorDefinition());
        Assert.assertNotNull("Row count indicator definition should not be null", rowCountIndicator.getIndicatorDefinition()); //$NON-NLS-1$
        Assert.assertEquals(
                "ItemRecordWithRefreshedTestIndicatorDefinition1", rowCountIndicator.getIndicatorDefinition().getLabel()); //$NON-NLS-1$
        createAnalysisResult.getIndicators().add(rowCountIndicator);
        Assert.assertEquals(1, createAnalysisResult.getIndicators().size());
        ReturnCode saveAnalysis = saveAnalysis(analysis);
        Assert.assertEquals(1, ((TDQAnalysisItem) analysisProperty.getItem()).getAnalysis().getResults().getIndicators().size());
        Assert.assertTrue("The analysis first time saving is not work", saveAnalysis.isOk()); //$NON-NLS-1$

        File analysisFile = WorkspaceUtils.ifileToFile(PropertyHelper.getItemFile(analysisProperty));
        ItemRecord itemRecord = new ItemRecord(analysisFile);
        Assert.assertEquals(1, itemRecord.getDependencySet().size());

        // create Indicator
        NullCountIndicator nullCountIndicator = IndicatorsFactory.eINSTANCE.createNullCountIndicator();
        String nullCountPropertyID = EcoreUtil.generateUUID();
        saveIndicatorDefintion(nullCountPropertyID, "ItemRecordWithRefreshedTestIndicatorDefinition2"); //$NON-NLS-1$
        nullCountIndicator.setIndicatorDefinition(((TDQIndicatorDefinitionItem) ProxyRepositoryFactory.getInstance()
                .getLastVersion(nullCountPropertyID).getProperty().getItem()).getIndicatorDefinition());
        analysis = item.getAnalysis();
        analysis.getResults().getIndicators().add(nullCountIndicator);
        ReturnCode saveAnalysis2 = saveAnalysis(analysis);
        Assert.assertTrue("The analysis second time saving is not work", saveAnalysis2.isOk()); //$NON-NLS-1$
        // get last resource so that the dependecy will not changed
        itemRecord = new ItemRecord(analysisFile);
        Assert.assertEquals(1, itemRecord.getDependencySet().size());
        // after clear the resource will be lastest so that the dependency is added
        ItemRecord.clear();
        itemRecord = new ItemRecord(analysisFile);
        Assert.assertEquals(2, itemRecord.getDependencySet().size());
    }

    /**
     * DOC zshen Comment method "chooseRightProject".
     */
    private void chooseRightProject() {
        String projectName = "TEST_NOLOGIN"; //getAppArgValue("-project", "TEST_NOLOGIN"); //$NON-NLS-1$ 
        log.debug("##############current setting Project name is " + projectName); //$NON-NLS-1$
        IProject rootProject = ReponsitoryContextBridge.getRootProject();
        if (!projectName.equals(rootProject.getName())) {
            log.debug("##############Project name is different"); //$NON-NLS-1$
            log.debug("##############current Project is " + rootProject.getName() + " decided reset project"); //$NON-NLS-1$
            IProject initProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            // rootProject.delete(true, true, null);
            log.debug("##############get Project name is " + initProject + " decided reset project"); //$NON-NLS-1$
            initProxyRepository(initProject);
            rootProject = ReponsitoryContextBridge.getRootProject();
            log.debug("##############After reset current Project is " + rootProject.getName() + " decided reset project"); //$NON-NLS-1$
            if (DQStructureManager.getInstance().isNeedCreateStructure()) {
                DQStructureManager.getInstance().createDQStructure();
            }
        }

    }

    /**
     * DOC talend Comment method "initProxyRepository".
     */
    private void initProxyRepository(IProject rootProject) {
        Project project = null;
        log.debug("##############ProxyRepositoryFactory.getInstance()"); //$NON-NLS-1$
        ProxyRepositoryFactory proxyRepository = ProxyRepositoryFactory.getInstance();
        log.debug("##############RepositoryFactoryProvider.getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID)"); //$NON-NLS-1$
        IRepositoryFactory repository = RepositoryFactoryProvider.getRepositoriyById(RepositoryConstants.REPOSITORY_LOCAL_ID);
        proxyRepository.setRepositoryFactoryFromProvider(repository);
        try {
            log.debug("##############proxyRepository.checkAvailability()"); //$NON-NLS-1$
            proxyRepository.checkAvailability();
            log.debug("##############proxyRepository.initialize()"); //$NON-NLS-1$
            try {
                proxyRepository.initialize();
            } catch (Exception e) {
                log.error(e, e);
            }
            proxyRepository.logOffProject();
            XmiResourceManager xmiResourceManager = new XmiResourceManager();
            log.debug("##############rootProject.getFile(FileConstants.LOCAL_PROJECT_FILENAME).exists()"); //$NON-NLS-1$
            if (rootProject.getFile(FileConstants.LOCAL_PROJECT_FILENAME).exists()) {
                // Initialize TDQ EMF model packages.
                log.debug("##############Project name is " + rootProject.getName()); //$NON-NLS-1$
                new EMFUtil();
                project = new Project(xmiResourceManager.loadProject(rootProject));
                log.debug("############## after loadProject Project name is " + project.getTechnicalLabel()); //$NON-NLS-1$
            } else {
                User user = org.talend.core.model.properties.impl.PropertiesFactoryImpl.eINSTANCE.createUser();
                user.setLogin("talend@talend.com"); //$NON-NLS-1$
                user.setPassword("talend@talend.com".getBytes()); //$NON-NLS-1$
                String projectName = rootProject.getName();
                log.debug("##############default project is not exist then create new project which is  " + projectName); //$NON-NLS-1$
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
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            log.error(e, e);
            log.debug("##############exception message is  " + e.getMessage()); //$NON-NLS-1$
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

    private String getAppArgValue(String arg, String defaultValue) {
        String value = defaultValue;
        int index = ArrayUtils.indexOf(Platform.getApplicationArgs(), arg);
        if (index > 0) {
            if (index + 1 < Platform.getApplicationArgs().length) {
                value = Platform.getApplicationArgs()[index + 1];
            }
        }
        return value;
    }

    /**
     * DOC zshen Comment method "saveAnalysis".
     * 
     * @param analysis
     */
    private ReturnCode saveAnalysis(Analysis analysis) {
        AnalysisWriter createAnalysisWriter = ElementWriterFactory.getInstance().createAnalysisWrite();
        return createAnalysisWriter.save(analysis);
    }

    private Property createAnalysis(String name) throws PersistenceException {

        // create analysis
        Analysis createAnalysis = AnalysisFactory.eINSTANCE.createAnalysis();
        AnalysisResult createAnalysisResult = AnalysisFactory.eINSTANCE.createAnalysisResult();
        AnalysisContext createAnalysisContext = AnalysisFactory.eINSTANCE.createAnalysisContext();
        createAnalysis.setResults(createAnalysisResult);
        createAnalysis.setContext(createAnalysisContext);
        createAnalysis.setName(name);

        // create analysis item
        TDQAnalysisItem createTDQAnalysisItem = PropertiesFactory.eINSTANCE.createTDQAnalysisItem();
        createTDQAnalysisItem.setAnalysis(createAnalysis);
        Property createAnalysisProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createAnalysisProperty.setLabel(name);
        createTDQAnalysisItem.setProperty(createAnalysisProperty);
        createAnalysisProperty.setId(EcoreUtil.generateUUID());
        ProxyRepositoryFactory.getInstance().create(createTDQAnalysisItem, Path.EMPTY, false);
        return createAnalysisProperty;
    }

    private void saveIndicatorDefintion(String uuid, String name) throws PersistenceException {

        // create definition
        IndicatorDefinition createIndicatorDefinition = DefinitionFactory.eINSTANCE.createIndicatorDefinition();

        TDQIndicatorDefinitionItem createTDQIndicatorDefinitionItem = PropertiesFactory.eINSTANCE
                .createTDQIndicatorDefinitionItem();
        createTDQIndicatorDefinitionItem.setIndicatorDefinition(createIndicatorDefinition);
        Property createProperty = org.talend.core.model.properties.PropertiesFactory.eINSTANCE.createProperty();
        createProperty.setLabel(name);
        createIndicatorDefinition.setLabel(name);
        createIndicatorDefinition.setName(name);
        createTDQIndicatorDefinitionItem.setProperty(createProperty);
        createProperty.setId(uuid);
        ProxyRepositoryFactory.getInstance().create(createTDQIndicatorDefinitionItem,
                new Path(ERepositoryObjectType.TDQ_SYSTEM_INDICATORS.getFolder()).removeFirstSegments(2), false);
    }

}
