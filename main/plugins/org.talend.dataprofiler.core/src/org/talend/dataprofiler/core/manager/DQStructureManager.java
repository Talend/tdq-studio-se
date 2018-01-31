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
package org.talend.dataprofiler.core.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.context.Context;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.migration.helper.WorkspaceVersionHelper;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;
import org.talend.dataprofiler.core.ui.utils.HadoopClusterUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.provider.RepositoryNodeBuilder;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.dq.factory.ModelElementFileFactory;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.AElementPersistance;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;
import org.talend.utils.ProductVersion;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Create the folder structure for the DQ Reponsitory view.
 * 
 */
public final class DQStructureManager {

    protected static Logger log = Logger.getLogger(DQStructureManager.class);

    private static final String DEMO_PATH = "/demo"; //$NON-NLS-1$

    public static final String RULES_PATH = "/dqrules"; //$NON-NLS-1$

    public static final String RULES_PARSER = "/parser"; //$NON-NLS-1$

    private static final String PATTERN_PATH = "/patterns"; //$NON-NLS-1$

    private static final String SQL_LIKE_PATH = "/sql_like";//$NON-NLS-1$

    public static final String SYSTEM_INDICATOR_PATH = "/indicators";//$NON-NLS-1$

    private static DQStructureManager manager;

    public static DQStructureManager getInstance() {
        if (manager == null) {
            manager = new DQStructureManager();
        }

        return manager;
    }

    /**
     * DOC bZhou DQStructureManager constructor comment.
     */
    private DQStructureManager() {
        ResourceService.refreshStructure();
    }

    /**
     * DOC bZhou Comment method "getCurrentProject".
     * 
     * @return
     */
    public Project getCurrentProject() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        return (Project) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    /**
     * DOC bZhou Comment method "createDQStructure".
     */
    public void createDQStructure() {
        RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>(
                DefaultMessagesImpl.getString("DQStructureManager.SVN_LOG_CREATE_STRUCTURE")) { //$NON-NLS-1$

            @Override
            protected void run() {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceRunnable operation = new IWorkspaceRunnable() {

                    public void run(IProgressMonitor monitor) throws CoreException {
                        createDQStructureUnit();
                    }
                };
                ISchedulingRule schedulingRule = workspace.getRoot();
                try {
                    workspace.run(operation, schedulingRule, IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
                } catch (CoreException e) {
                    log.error(e, e);
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }

    /**
     * DOC zhao Comment method "createDQStructureUnit".
     */
    private void createDQStructureUnit() {
        RepositoryNodeBuilder instance = RepositoryNodeBuilder.getInstance();
        Plugin plugin = CorePlugin.getDefault();
        try {

            IProject project = ResourceManager.getRootProject();
            if (!project.exists()) {
                project = createNewProject(ResourceManager.getRootProjectName());

            }

            if (!project.getFolder(EResourceConstant.DB_CONNECTIONS.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.METADATA, Path.EMPTY,
                        EResourceConstant.DB_CONNECTIONS.getName());
            }

            if (!project.getFolder(EResourceConstant.FILEDELIMITED.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.METADATA, Path.EMPTY,
                        EResourceConstant.FILEDELIMITED.getName());
            }
            if (!project.getFolder(EResourceConstant.ANALYSIS.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_DATA_PROFILING, Path.EMPTY,
                        EResourceConstant.ANALYSIS.getName());
            }

            if (PluginChecker.isTDQLoaded()) {
                if (!project.getFolder(EResourceConstant.REPORTS.getPath()).exists()) {
                    ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_DATA_PROFILING, Path.EMPTY,
                            EResourceConstant.REPORTS.getName());
                }
                // Added 20150421 TDQ-9605
                if (!project.getFolder(EResourceConstant.HADOOP_CLUSTER.getPath()).exists()
                        && HadoopClusterUtils.getDefault().isServiceInstalled()) {
                    ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.METADATA, Path.EMPTY,
                            EResourceConstant.HADOOP_CLUSTER.getName());
                }
            }
            if (!project.getFolder(EResourceConstant.PATTERNS.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_LIBRARIES, Path.EMPTY,
                        EResourceConstant.PATTERNS.getName());
            }
            if (!project.getFolder(EResourceConstant.RULES.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_LIBRARIES, Path.EMPTY,
                        EResourceConstant.RULES.getName());
            }
            Folder rulesSQLFoler = null;
            if (!project.getFolder(EResourceConstant.RULES_SQL.getPath()).exists()) {
                rulesSQLFoler = ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_RULES, Path.EMPTY,
                        EResourceConstant.RULES_SQL.getName());
            } else {
                rulesSQLFoler = instance.getObjectFolder(EResourceConstant.RULES_SQL);
            }
            rulesSQLFoler.getProperty().getItem().getState().setPath(ERepositoryObjectType.TDQ_RULES_SQL.getFolder());
            Folder rulesMatchLFoler = null;
            if (!project.getFolder(EResourceConstant.RULES_MATCHER.getPath()).exists()) {
                rulesMatchLFoler = ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_RULES, Path.EMPTY,
                        EResourceConstant.RULES_MATCHER.getName());
            } else {
                rulesMatchLFoler = instance.getObjectFolder(EResourceConstant.RULES_MATCHER);
            }
            rulesMatchLFoler.getProperty().getItem().getState().setPath(ERepositoryObjectType.TDQ_RULES_MATCHER.getFolder());
            Folder rulesParserFoler = null;
            if (!project.getFolder(EResourceConstant.RULES_PARSER.getPath()).exists()) {
                if (PluginChecker.isTDQLoaded()) {
                    rulesParserFoler = ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_RULES,
                            Path.EMPTY, EResourceConstant.RULES_PARSER.getName());
                }
            } else {
                rulesParserFoler = instance.getObjectFolder(EResourceConstant.RULES_PARSER);
            }
            if (rulesParserFoler != null) {
                rulesParserFoler.getProperty().getItem().getState().setPath(ERepositoryObjectType.TDQ_RULES_PARSER.getFolder());
            }
            if (!project.getFolder(EResourceConstant.EXCHANGE.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_LIBRARIES, Path.EMPTY,
                        EResourceConstant.EXCHANGE.getName());
            }
            if (!project.getFolder(EResourceConstant.INDICATORS.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_LIBRARIES, Path.EMPTY,
                        EResourceConstant.INDICATORS.getName());
            }
            Folder systemIndicatorFoler = null;
            if (!project.getFolder(EResourceConstant.SYSTEM_INDICATORS.getPath()).exists()) {
                systemIndicatorFoler = ProxyRepositoryFactory.getInstance().createFolder(
                        ERepositoryObjectType.TDQ_INDICATOR_ELEMENT, Path.EMPTY, EResourceConstant.SYSTEM_INDICATORS.getName());
            } else {
                systemIndicatorFoler = instance.getObjectFolder(EResourceConstant.SYSTEM_INDICATORS);
            }
            systemIndicatorFoler.getProperty().getItem().getState()
                    .setPath(ERepositoryObjectType.TDQ_SYSTEM_INDICATORS.getFolder());

            if (!project.getFolder(EResourceConstant.USER_DEFINED_INDICATORS.getPath()).exists()) {
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_INDICATOR_ELEMENT, Path.EMPTY,
                        EResourceConstant.USER_DEFINED_INDICATORS.getName());
            }
            if (!project.getFolder(EResourceConstant.USER_DEFINED_INDICATORS_LIB.getPath()).exists()) {
                // MOD zhsne 18724: Java UDI enhancements add lib folder under UDI folder.
                ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS, Path.EMPTY,
                        EResourceConstant.USER_DEFINED_INDICATORS_LIB.getName());
            }

            if (PluginChecker.isTDQLoaded()) {
                if (!project.getFolder(EResourceConstant.JRXML_TEMPLATE.getPath()).exists()) {
                    ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_LIBRARIES, Path.EMPTY,
                            EResourceConstant.JRXML_TEMPLATE.getName());
                }
                if (!project.getFolder(EResourceConstant.CONTEXT.getPath()).exists()) {
                    ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.CONTEXT, Path.EMPTY,
                            EResourceConstant.CONTEXT.getName());
                }
            }
            Folder patternRegexFoler = null;
            if (!project.getFolder(EResourceConstant.PATTERN_REGEX.getPath()).exists()) {
                patternRegexFoler = ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_PATTERN_ELEMENT,
                        Path.EMPTY, EResourceConstant.PATTERN_REGEX.getName());
            } else {
                patternRegexFoler = instance.getObjectFolder(EResourceConstant.PATTERN_REGEX);
            }
            patternRegexFoler.getProperty().getItem().getState().setPath(ERepositoryObjectType.TDQ_PATTERN_REGEX.getFolder());

            Folder patternSQLFoler = null;
            if (!project.getFolder(EResourceConstant.PATTERN_SQL.getPath()).exists()) {
                patternSQLFoler = ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_PATTERN_ELEMENT,
                        Path.EMPTY, EResourceConstant.PATTERN_SQL.getName());
            } else {
                patternSQLFoler = instance.getObjectFolder(EResourceConstant.PATTERN_SQL);
            }
            patternSQLFoler.getProperty().getItem().getState().setPath(ERepositoryObjectType.TDQ_PATTERN_SQL.getFolder());

            Folder sourceFileFoler = null;
            if (!project.getFolder(EResourceConstant.SOURCE_FILES.getPath()).exists()) {
                sourceFileFoler = ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType.TDQ_LIBRARIES,
                        Path.EMPTY, EResourceConstant.SOURCE_FILES.getName());
            } else {
                sourceFileFoler = instance.getObjectFolder(EResourceConstant.SOURCE_FILES);
            }
            sourceFileFoler.getProperty().getItem().getState().setPath(ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT.getFolder());
            // use the tos create folder API
            if (systemIndicatorFoler != null && patternRegexFoler != null && patternSQLFoler != null && sourceFileFoler != null
                    && rulesSQLFoler != null) {

                copyFilesToFolder(plugin, SYSTEM_INDICATOR_PATH, true, systemIndicatorFoler, null,
                        ERepositoryObjectType.TDQ_SYSTEM_INDICATORS);
                copyFilesToFolder(plugin, PATTERN_PATH, true, patternRegexFoler, null, ERepositoryObjectType.TDQ_PATTERN_REGEX);
                copyFilesToFolder(plugin, SQL_LIKE_PATH, true, patternSQLFoler, null, ERepositoryObjectType.TDQ_PATTERN_SQL);
                copyFilesToFolder(plugin, DEMO_PATH, true, sourceFileFoler, null, ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT);
                copyFilesToFolder(plugin, RULES_PATH, true, rulesSQLFoler, null, ERepositoryObjectType.TDQ_RULES_SQL);

            }
            if (rulesParserFoler != null) {
                copyFilesToFolder(plugin, RULES_PARSER, true, rulesParserFoler, null, ERepositoryObjectType.TDQ_RULES_PARSER);
            }

            WorkspaceVersionHelper.storeVersion();

            // MOD qiongli 2011-12-6 TDQ-4095,make the definition file commit to svn with the DQ struct files.move
            // this code from DQRespositoryView.initWorkspace()
            IFile defFile = ResourceManager.getLibrariesFolder().getFile(DefinitionHandler.FILENAME);
            if (!defFile.exists()) {
                DefinitionHandler handler = DefinitionHandler.getInstance();
                // TDQ-4714 update the aggregateDefinitions tag for some indicator definition.
                handler.updateAggregates();
            }

            ResourceService.refreshStructure();

        } catch (Exception ex) {
            ExceptionHandler.process(ex);
            ProxyRepositoryManager.getInstance().save();
        }
    }

    /**
     * 
     * DOC klliu Comment method "copyFilesToFolder".
     * 
     * @param plugin
     * @param srcPath
     * @param recurse
     * @param desFolder
     * @param suffix
     * @param type
     * @throws IOException
     * @throws CoreException
     */
    public void copyFilesToFolder(Plugin plugin, String srcPath, boolean recurse, Folder desFolder, String suffix,
            ERepositoryObjectType type) throws IOException, CoreException {
        if (plugin == null) {
            return;
        }

        IProject project = ResourceManager.getRootProject();
        Enumeration<?> paths = null;
        paths = plugin.getBundle().getEntryPaths(srcPath);
        if (paths == null) {
            return;
        }

        while (paths.hasMoreElements()) {
            String nextElement = (String) paths.nextElement();
            String currentPath = "/" + nextElement; //$NON-NLS-1$
            URL resourceURL = plugin.getBundle().getEntry(currentPath);
            URL fileURL = null;
            File file = null;
            try {
                fileURL = FileLocator.toFileURL(resourceURL);
                file = new File(fileURL.getFile());
                if (file.isDirectory() && recurse) {
                    if (file.getName().startsWith(".")) { //$NON-NLS-1$
                        continue;
                    }
                    Folder sourcefolder = null;
                    IFolder targetfolder = WorkbenchUtils.folder2IFolder(desFolder);
                    if (!targetfolder.getFolder(file.getName()).exists()) {
                        sourcefolder = ProxyRepositoryFactory.getInstance().createFolder(type, Path.EMPTY, file.getName());
                    } else {
                        FolderItem folderItem = ProxyRepositoryFactory.getInstance().getFolderItem(
                                ProjectManager.getInstance().getCurrentProject(), type, new Path(file.getName()));

                        if (folderItem == null) {
                            sourcefolder = ProxyRepositoryFactory.getInstance().createFolder(type, Path.EMPTY, file.getName());
                            String subSourceFolder = type.getFolder().concat("/").concat(file.getName()); //$NON-NLS-1$
                            sourcefolder.getProperty().getItem().getState().setPath(subSourceFolder);
                        } else {
                            String subSourceFolder = type.getFolder().concat("/").concat(file.getName()); //$NON-NLS-1$
                            sourcefolder = new Folder(folderItem.getProperty(), type);
                            sourcefolder.getProperty().getItem().getState().setPath(subSourceFolder);
                        }
                    }
                    copyFilesToFolder(plugin, currentPath, recurse, sourcefolder, suffix, type);
                    continue;
                }

                if (suffix != null && !file.getName().endsWith(suffix)) {
                    continue;
                }

                String fileName = new Path(fileURL.getPath()).lastSegment();
                InputStream openStream = fileURL.openStream();
                String folderName = null;

                if (type.equals(ERepositoryObjectType.TDQ_PATTERN_ELEMENT)) {
                    folderName = ERepositoryObjectType.getFolderName(type);
                }
                if (type.equals(ERepositoryObjectType.TDQ_RULES_SQL) || type.equals(ERepositoryObjectType.TDQ_RULES_PARSER)) {
                    folderName = ERepositoryObjectType.getFolderName(type);
                } else if (type.equals(ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT)) {
                    folderName = ERepositoryObjectType.getFolderName(type);
                } else {
                    folderName = ERepositoryObjectType.getFolderName(type) + "/" + desFolder.getLabel(); //$NON-NLS-1$
                }
                if (folderName.equals("date")) { //$NON-NLS-1$
                    continue;
                }
                IFolder folder = project.getFolder(folderName);
                if (type.equals(ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT)) {
                    String name = file.getName();
                    int indexOf = name.indexOf("."); //$NON-NLS-1$
                    String label = name.substring(0, indexOf);
                    String extendtion = name.substring(indexOf + 1);
                    createSourceFileItem(file, Path.EMPTY, label, extendtion);
                } else {
                    copyFileToFolder(openStream, fileName, folder);
                }

                openStream.close();
            } catch (IOException e) {
                log.error(e, e);
            } catch (PersistenceException e) {
                log.error(e, e);
            }
        }
    }

    /**
     * 
     * DOC klliu Comment method "copyFileToFolder".
     * 
     * @param inputStream
     * @param fileName
     * @param folder
     * @throws CoreException
     * @throws IOException
     */
    private void copyFileToFolder(InputStream inputStream, String fileName, IFolder folder, boolean... isImportItem)
            throws CoreException, IOException {
        if (inputStream == null) {
            return;
        }
        IFile elementFile = folder.getFile(fileName);
        if (!elementFile.exists()) {
            elementFile.create(inputStream, false, null);
            ModelElement modelElement = ModelElementFileFactory.getModelElement(elementFile);
            if (modelElement != null) {
                AElementPersistance writer = ElementWriterFactory.getInstance().create(elementFile.getFileExtension());
                if (writer != null) {
                    Property initProperty = writer.initProperty(modelElement);
                    String propFileName = initProperty.getLabel() + "_" + initProperty.getVersion() + "." //$NON-NLS-1$ //$NON-NLS-2$
                            + elementFile.getFileExtension();
                    IFile file = elementFile.getParent().getFile(new Path(propFileName));
                    if (file == null || !file.exists()) {
                        writer.create(modelElement, folder, isImportItem);
                    }
                    elementFile.delete(true, null);
                }
            }
        }
    }

    public TDQSourceFileItem createSourceFileItem(File initFile, IPath path, String label, String extension) {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(PluginConstant.EMPTY_STRING);
        property.setLabel(label);

        TDQSourceFileItem sourceFileItem = org.talend.dataquality.properties.PropertiesFactory.eINSTANCE
                .createTDQSourceFileItem();
        sourceFileItem.setProperty(property);
        sourceFileItem.setName(label);
        sourceFileItem.setExtension(extension);

        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        try {
            byteArray.setInnerContentFromFile(initFile);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
        sourceFileItem.setContent(byteArray);

        String sqlFileName = property.getLabel() + "_" + property.getVersion() + "." //$NON-NLS-1$ //$NON-NLS-2$
                + extension;
        IFile file = ResourceManager.getSourceFileFolder().getFile(path.append(new Path(sqlFileName)));
        if (file == null || !file.exists()) {
            IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
            try {
                property.setId(repositoryFactory.getNextId());
                repositoryFactory.create(sourceFileItem, path);
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return sourceFileItem;
    }

    /**
     * 
     * DOC qiongli Comment method "createSourceFileItem".
     * 
     * @param content:cotanin sql sentence
     * @param path
     * @param label:file name
     * @param extension:file extension
     * @return
     */
    public TDQSourceFileItem createSourceFileItem(String content, IPath path, String label, String extension) {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setVersion(VersionUtils.DEFAULT_VERSION);
        property.setStatusCode(PluginConstant.EMPTY_STRING);
        property.setLabel(label);

        TDQSourceFileItem sourceFileItem = org.talend.dataquality.properties.PropertiesFactory.eINSTANCE
                .createTDQSourceFileItem();
        sourceFileItem.setProperty(property);
        sourceFileItem.setName(label);
        sourceFileItem.setExtension(extension);

        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        // MOD sizhaoliu 2012-04-02 for TDQ-5070 Encoding issue with saving generated sql query action
        byteArray.setInnerContent(content.getBytes(Charset.forName("UTF-8"))); //$NON-NLS-1$
        sourceFileItem.setContent(byteArray);
        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        try {
            property.setId(repositoryFactory.getNextId());
            repositoryFactory.create(sourceFileItem, path);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return sourceFileItem;
    }

    /**
     * Method "isNeedCreateStructure" created by bzhou@talend.com.
     * 
     * @return true if need to create new resource structure.
     */
    public boolean isNeedCreateStructure() {
        // MOD zshen when use commandLine application maybe there don't should have a project, so before there should
        // create a new project firstly.
        if (!ResourceManager.getRootProject().exists()) {
            return false;
        }
        // ~zshen
        // MOD klliu bug TDQ-3897 2011-11-08
        // if (isSecludedVersion()) {
        // return !ResourceService.checkSecludedResource();
        // }
        //
        // return !ResourceService.checkResource();
        // we only check the dq structure is on workspace,otherwise it will be created.
        return !ResourceService.checkSecludedResource() && !ResourceService.checkResource();

    }

    /**
     * DOC bZhou Comment method "isNeedMigration".
     * 
     * @return
     */
    public boolean isNeedMigration() {
        if (isSecludedVersion()) {
            return true;
        }

        ProductVersion wVersion = WorkspaceVersionHelper.getVesion();
        ProductVersion cVersion = CorePlugin.getDefault().getProductVersion();
        return wVersion.compareTo(cVersion) < 0;
    }

    /**
     * Method "isSecludedVersion" created by bzhou@talend.com.
     * 
     * @return true if version is before 3.0.0
     */
    private boolean isSecludedVersion() {
        return !WorkspaceVersionHelper.getVersionFile().exists();
    }

    /**
     * Creates a new project resource with the special name.MOD mzhao 2009-03-18 make this method as public.For
     * {@link org.talend.dataprofiler.core.migration.impl.TDCPFolderMergeTask} use.
     * 
     * @return the created project resource, or <code>null</code> if the project was not created
     * @throws InterruptedException
     * @throws InvocationTargetException
     * @throws CoreException
     */
    public IProject createNewProject(String projectName) throws InvocationTargetException, InterruptedException, CoreException {

        // get a project handle
        final IProject newProjectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());

        // create the new project operation
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                CreateProjectOperation createProjOp = new CreateProjectOperation(description,
                        DefaultMessagesImpl.getString("DQStructureManager.createDataProfile")); //$NON-NLS-1$
                try {
                    PlatformUI.getWorkbench().getOperationSupport().getOperationHistory()
                            .execute(createProjOp, monitor, WorkspaceUndoUtil.getUIInfoAdapter(null));
                } catch (ExecutionException e) {
                    throw new InvocationTargetException(e);
                }
            }
        };

        // run the new project creation o`peration
        // try {
        // MOD mzhao 2009-03-16 Feature 6066 First check whether project with
        // this name already exist or not. For TDCP
        // launching,
        // project always exist.
        if (!newProjectHandle.exists()) {
            ProgressUI.popProgressDialog(op);
        }
        return newProjectHandle;
    }

    /**
     * DOC bzhou Comment method "createNewFolder".
     * 
     * @param parent
     * @param constant
     * @return
     * @throws CoreException
     * @deprecated
     */
    @Deprecated
    public IFolder createNewFolder(IContainer parent, EResourceConstant constant) throws CoreException {
        return createNewFolder(parent, constant.getName());
    }

    /**
     * Method "createNewFolder" creates a new folder.
     * 
     * @param parent
     * @param folderName
     * @return
     * @throws CoreException
     * @deprecated
     */
    @Deprecated
    public IFolder createNewFolder(IContainer parent, String folderName) throws CoreException {
        IFolder desFolder = null;

        if (parent instanceof IProject) {
            desFolder = ((IProject) parent).getFolder(folderName);
        } else if (parent instanceof IFolder) {
            desFolder = ((IFolder) parent).getFolder(folderName);
        }
        assert desFolder != null;

        if (desFolder != null && !desFolder.exists()) {
            desFolder.create(false, true, null);
        }
        return desFolder;
    }

    /**
     * Copy the files from srcPath to destination folder.
     * 
     * @param srcPath The path name in which to look. The path is always relative to the root of this bundle and may
     * begin with &quot;/&quot;. A path value of &quot;/&quot; indicates the root of this bundle.
     * @param srcPath
     * @param recurse If <code>true</code>, recurse into subdirectories(contains directories). Otherwise only return
     * entries from the specified path.
     * @param desFolder
     * @throws IOException
     * @throws CoreException
     */
    public void copyFilesToFolder(Plugin plugin, String srcPath, boolean recurse, IFolder desFolder, String suffix,
            boolean... isImportItem) throws IOException, CoreException {
        if (plugin == null) {
            return;
        }

        Enumeration<?> paths = null;
        paths = plugin.getBundle().getEntryPaths(srcPath);
        if (paths == null) {
            return;
        }
        while (paths.hasMoreElements()) {
            String nextElement = (String) paths.nextElement();
            String currentPath = "/" + nextElement; //$NON-NLS-1$
            URL resourceURL = plugin.getBundle().getEntry(currentPath);
            URL fileURL = null;
            File file = null;
            try {
                fileURL = FileLocator.toFileURL(resourceURL);
                file = new File(fileURL.getFile());
                if (file.isDirectory() && recurse) {
                    if (file.getName().startsWith(".")) { //$NON-NLS-1$
                        continue;
                    }
                    IFolder folder = desFolder.getFolder(file.getName());
                    if (!folder.exists()) {
                        // ProxyRepositoryFactory.getInstance().createFolder(ERepositoryObjectType., path, label)
                        folder.create(true, true, null);
                        // this.createDQStructure();
                    }

                    copyFilesToFolder(plugin, currentPath, recurse, folder, suffix, isImportItem);
                    continue;
                }

                if (suffix != null && !file.getName().endsWith(suffix)) {
                    continue;
                }

                String fileName = new Path(fileURL.getPath()).lastSegment();
                InputStream openStream = null;
                openStream = fileURL.openStream();
                copyFileToFolder(openStream, fileName, desFolder, isImportItem);

                openStream.close();
            } catch (IOException e) {
                log.error(e, e);
            }
        }

    }

    public boolean isPathValid(IPath path, String label) {
        IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
        IFolder newFolder = folder.getFolder(label);
        return !newFolder.exists();
    }

}
