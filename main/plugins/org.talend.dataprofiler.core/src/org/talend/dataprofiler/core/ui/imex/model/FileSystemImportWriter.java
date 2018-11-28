// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.URIHelper;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataprofiler.core.migration.helper.WorkspaceVersionHelper;
import org.talend.dataprofiler.core.migration.impl.RenamePatternFinderFolderTask;
import org.talend.dataprofiler.core.service.AbstractSvnRepositoryService;
import org.talend.dataprofiler.core.service.GlobalServiceRegister;
import org.talend.dataprofiler.core.ui.utils.DqFileUtils;
import org.talend.dataprofiler.migration.IMigrationTask;
import org.talend.dataprofiler.migration.IWorkspaceMigrationTask.MigrationTaskType;
import org.talend.dataprofiler.migration.manager.MigrationTaskManager;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisResult;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.PatternFactory;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.reports.TdReport;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.WhereRule;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.dq.CWMPlugin;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.PrvResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;
import org.talend.utils.ProductVersion;

import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class FileSystemImportWriter implements IImportWriter {

    private static Logger log = Logger.getLogger(FileSystemImportWriter.class);

    private static final String VERSION_FILE_NAME = ".version.txt";//$NON-NLS-1$ 

    private static final String DEFINITION_FILE_NAME = DefinitionHandler.FILENAME;

    private List<IMigrationTask> commTasks = new ArrayList<IMigrationTask>();

    private File tempFolder;

    private File versionFile;

    private File definitionFile;

    private IPath basePath;

    private String projectName;

    private List<File> allCopiedFiles = new ArrayList<File>();

    private final List<ItemRecord> need2MergeModelElementList = new ArrayList<ItemRecord>();

    private final List<IPath> allImportItems = new ArrayList<IPath>();

    private List<File> updateFiles = new ArrayList<File>();

    private List<File> updateFilesCoverd = new ArrayList<File>();

    /*
     * check the dependency and conflict; when the record is a indicator(system or user): if overwrite should not add
     * error in record(only check conflict, but not check dependency)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#populate(org.talend.dataprofiler.core.ui.imex.model.
     * ItemRecord [], boolean)
     */

    public ItemRecord[] populate(ItemRecord[] elements, boolean isOverWrite) {
        List<ItemRecord> inValidRecords = new ArrayList<ItemRecord>();

        for (ItemRecord record : elements) {

            record.getErrors().clear();

            // modify: if it is a indicator and used in analysis, do not add errors
            checkConflict(record, isIndicatorDefinition(record.getElement()) || isPattern(record.getElement()),
                    isOverWrite);

            checkDependency(record);

            checkBuiltInMode(record);

            if (!isOverWrite && record.getConflictObject() != null) {
                record.addError(DefaultMessagesImpl.getString(
                        "FileSystemImproWriter.hasConflictObject", record.getName()));//$NON-NLS-1$ 
            }

            if (record.existMessageToShow()) {
                inValidRecords.add(record);
            }
        }

        return inValidRecords.toArray(new ItemRecord[inValidRecords.size()]);
    }

    /**
     * Check regex/sql pattern on column analysis.
     * Check regex pattern on columnSet analysis.
     */
    private void checkBuiltInMode(ItemRecord record) {
        IRepositoryViewObject conflictObject = record.getConflictObject();
        if (conflictObject == null) {
            // return if no conflict exist
            return;
        }
        Property property = conflictObject.getProperty();
        ModelElement modelElement = PropertyHelper.getModelElement(property);
        if (modelElement instanceof Analysis) {
            checkBuintInOnAnalysis(record, modelElement);
        } else if (modelElement instanceof Connection || modelElement instanceof IndicatorDefinition
                || modelElement instanceof Pattern) {
            EList<Dependency> supplierDependency = modelElement.getSupplierDependency();
            for (Dependency clientDep : supplierDependency) {
                for (ModelElement clientModelElement : clientDep.getClient()) {
                    if (clientModelElement instanceof Analysis) {
                        checkBuintInOnAnalysis(record, clientModelElement);
                    }
                }
            }
        }
    }

    private void checkBuintInOnAnalysis(ItemRecord record, ModelElement modelElement) {
        AnalysisResult results = ((Analysis) modelElement).getResults();
        if (results != null && results.getIndicators() != null) {
            for (Indicator indicator : results.getIndicators()) {
                if (indicator instanceof AllMatchIndicator) {
                    List<RegexpMatchingIndicator> compositeIndicators =
                            ((AllMatchIndicator) indicator).getCompositeRegexMatchingIndicators();
                    for (Indicator ind : compositeIndicators) {
                        if (ind.getParameters().getDataValidDomain().getBuiltInPatterns().size() > 0) {
                            record.addError(DefaultMessagesImpl.getString("FileSystemImportWriter.builtinCheck", //$NON-NLS-1$
                                    ind.getName(), modelElement.getName()));
                        }
                    }
                } else if (indicator instanceof PatternMatchingIndicator
                        && indicator.getParameters().getDataValidDomain().getBuiltInPatterns().size() > 0) {
                    record.addError(DefaultMessagesImpl.getString("FileSystemImportWriter.builtinCheck", //$NON-NLS-1$
                            indicator.getName(), modelElement.getName()));
                }
            }
        }
    }

    /**
     * judge if the record is a IndicatorDefinition or not.
     * 
     * @param element
     * @return
     */
    private boolean isIndicatorDefinition(ModelElement element) {
        return element instanceof IndicatorDefinition;
    }

    /**
     * judge if the record is a Connection or not.
     * 
     * @param element
     * @return
     */
    private boolean isConnection(ModelElement element) {
        return element instanceof Connection;
    }

    /**
     * judge if the record is a Analysis or not.
     * 
     * @param element
     * @return
     */
    private boolean isAnalysis(ModelElement element) {
        return element instanceof Analysis;
    }

    /**
     * judge if the record is a Analysis or not.
     * 
     * @param element
     * @return
     */
    private boolean isReport(ModelElement element) {
        return element instanceof Report;
    }

    /**
     * judge if the record is a DQRule or not.
     * 
     * @param element
     * @return
     */
    private boolean isDQRule(ModelElement element) {
        return element instanceof DQRule;
    }

    /**
     * judge if the record is a ParserRule or not.
     * 
     * @param element
     * @return
     */
    private boolean isParserRule(ModelElement element) {
        return element instanceof ParserRule;
    }

    /**
     * judge if the record is a WhereRule or not.
     * 
     * @param element
     * @return
     */
    private boolean isWhereRule(ModelElement element) {
        return element instanceof WhereRule;
    }

    /**
     * judge if the record is a MatchRuleDefinition or not.
     * 
     * @param element
     * @return
     */
    private boolean isMatchRuleDefinition(ModelElement element) {
        return element instanceof MatchRuleDefinition;
    }

    /**
     * judge if the record is a indicator or not.
     * 
     * @param element
     * @return
     */
    private boolean isPattern(ModelElement element) {
        return element instanceof Pattern;
    }

    /**
     * 
     * judge if the record is a DataBaseConnection or not.
     * 
     * @param element
     * @return
     */
    private boolean isDBConnection(ModelElement element) {
        return element instanceof DatabaseConnection;
    }

    /**
     * MOdified 20120810 yyin TDQ-4189 when the record is a system indicator and be used by some analysis, only remember
     * the conflict object to merge, but do not add any errors which will cause it can't be imported
     * 
     * @param record
     * @param checkExisted
     */
    private void checkConflict(ItemRecord record, boolean isIndicator, boolean isOverWrite) {
        Property property = record.getProperty();
        if (property != null) {
            try {
                ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(property.getItem());
                List<IRepositoryViewObject> allObjects = ProxyRepositoryFactory.getInstance().getAll(itemType, true);
                boolean compareNameAndPropertyOnly = false;
                if (ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT == itemType
                        || ERepositoryObjectType.TDQ_JRAXML_ELEMENT == itemType) {
                    compareNameAndPropertyOnly = true;
                }
                for (IRepositoryViewObject object : allObjects) {
                    if (isConflict(property, object, record, compareNameAndPropertyOnly)) {
                        // dependency error is invalid for overWrite case
                        if (!isIndicator && itemType != ERepositoryObjectType.CONTEXT && !isOverWrite) {
                            List<IRepositoryViewObject> supplierDependency =
                                    DependenciesHandler.getInstance().getSupplierDependency(object);
                            for (IRepositoryViewObject supplierViewObject : supplierDependency) {
                                String message =
                                        DefaultMessagesImpl
                                                .getString(
                                                        "FileSystemImproWriter.DependencyWarning", new Object[] { record.getName(), supplierViewObject.getProperty().getLabel(), object.getLabel() });//$NON-NLS-1$
                                record.addDependencyError(message);
                            }
                        }
                        return;
                    }
                }
            } catch (Exception e) {
                record.addError(DefaultMessagesImpl.getString("FileSystemImportWriter.CheckFailed", record.getName()));//$NON-NLS-1$ 
            }
        }
    }

    private boolean isConflict(Property p1, IRepositoryViewObject confilctObject, ItemRecord record,
            boolean compareNameAndPropertyOnly) {
        Property p2 = confilctObject.getProperty();

        // If set this parameter will delete the object when finished the wizard.
        boolean isIdSame = p1.getId().equals(p2.getId());
        // for source sql file and jrxml case
        boolean isNameSame =
                WorkspaceUtils.normalize(p1.getLabel()).equalsIgnoreCase(WorkspaceUtils.normalize(p2.getLabel()));
        if (compareNameAndPropertyOnly) {
            if (isIdSame) {
                record.setConflictObject(confilctObject);
                record.seteConflictType(EConflictType.UUID);
                if (!isNameSame) {
                    record.addWarn(DefaultMessagesImpl.getString("FileSystemImproWriter.sameUUIDDifferentNameReplace", //$NON-NLS-1$
                            record.getName(), record.getConflictObject().getLabel(), record.getName()));
                } else {
                    record.addWarn(DefaultMessagesImpl.getString("FileSystemImproWriter.sameUUIDReplace", //$NON-NLS-1$
                            record.getName()));
                }
            } else if (isNameSame) {
                record.setConflictObject(confilctObject);
                record.seteConflictType(EConflictType.UUID);
                record.addWarn(DefaultMessagesImpl.getString("FileSystemImproWriter.hasNameConflictObject", //$NON-NLS-1$
                        record.getName()));
            }
            return isNameSame;
        }

        // if property id is different then compre with xmi:id of item
        if (!isIdSame) {
            ModelElement modelElement1 = PropertyHelper.getModelElement(p1);
            ModelElement modelElement2 = PropertyHelper.getModelElement(p2);
            if (modelElement1 != null && modelElement2 != null) {
                // source file or jrxml case never come here
                String uriFragment1 = modelElement1.eResource().getURIFragment(modelElement1);
                String uriFragment2 = modelElement2.eResource().getURIFragment(modelElement2);
                isIdSame = uriFragment1.equals(uriFragment2);
            }
        }

        boolean isSamePath = true;
        // same item and name need to check path
        URI uri1 = p1.eResource().getURI();
        URI uri2 = p2.eResource().getURI();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        // only check path don't check name
        for (int j = 1, size = uri1.segmentCount(); j < size - 1; ++j) {
            if (root.getLocation().segment(j) != null && root.getLocation().segment(j).equals(uri1.segment(j))) {
                continue;
            } else if (uri1.segment(j).equals(this.tempFolder.getName())) {
                // continue and next one should be project name
                continue;
            } else if (!URI.decode(uri1.segment(j)).equals(
                    URI.decode(uri2.segment(uri2.segmentCount() - (uri1.segmentCount() - j))))) {
                isSamePath = false;
                break;
            }
        }

        // same item different name case
        if (isIdSame && !isNameSame) {
            record.setConflictObject(confilctObject);
            record.seteConflictType(EConflictType.UUIDBUTNAME);

            if (record.isInvalidNAMEConflictExist()) {
                record.addError(DefaultMessagesImpl.getString("FileSystemImproWriter.needSameNameConflictObject", //$NON-NLS-1$
                        record.getName()));
            } else if (!isSamePath) {
                record.addError(DefaultMessagesImpl.getString("FileSystemImproWriter.needSamePathConflictObject", //$NON-NLS-1$
                        record.getName()));
            } else {
                // replace message needed by warn
                record
                        .addWarn(DefaultMessagesImpl
                                .getString(
                                        "FileSystemImproWriter.sameUUIDDifferentNameReplace", record.getName(), record.getConflictObject().getLabel(), record.getName())); //$NON-NLS-1$
            }
            return true;
            // same item and name case
        } else if (isIdSame) {
            record.setConflictObject(confilctObject);
            record.seteConflictType(EConflictType.UUID);

            if (!isSamePath) {
                record.addError(DefaultMessagesImpl.getString("FileSystemImproWriter.needSamePathConflictObject", //$NON-NLS-1$
                        record.getName()));
            } else {
                // replace message needed by warn
                record
                        .addWarn(DefaultMessagesImpl.getString(
                                "FileSystemImproWriter.sameUUIDReplace", record.getName())); //$NON-NLS-1$
            }
            return true;
            // different item same name case
        } else if (isNameSame) {
            record.setConflictObject(confilctObject);
            record.seteConflictType(EConflictType.NAME);
            if (record.isInvalidNAMEConflictExist()) {
                record.addError(DefaultMessagesImpl.getString("FileSystemImproWriter.hasNameConflictObject", //$NON-NLS-1$
                        record.getName()));
            }
            return true;
        }
        // else case mean that no conflict
        return false;
    }

    /**
     * DOC bZhou Comment method "checkDependency".
     * 
     * @param record
     */
    private void checkDependency(ItemRecord record) {
        for (File depFile : record.getDependencySet()) {
            ModelElement melement = ItemRecord.getElement(depFile);
            if (melement != null && melement.eIsProxy()) {

                // TDQ-12410: if the dependency comes from reference project, we ingore it.
                if (!DqFileUtils.isFileUnderBasePath(depFile, getBasePath())) {
                    continue;
                }

                // if the element is IndicatorDefinition and it exist in the current project and don't include any
                // sql and java templates and the AggregatedDefinitions is not empty or TableOverview/ViewOverview
                // Indicator, don't add it into errors even if it is not exist
                if (melement instanceof IndicatorDefinition) {
                    String uuid = ResourceHelper.getUUID(melement);
                    if (IndicatorDefinitionFileHelper.isTechnialIndicator(uuid)) {
                        continue;
                    }
                }
                InternalEObject inObject = (InternalEObject) melement;
                record.addError(DefaultMessagesImpl.getString(
                        "FileSystemImportWriter.MissDepend", record.getName(), inObject.eProxyURI().toFileString()));//$NON-NLS-1$ 
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImexWriter#mapping(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * )
     */
    public Map<IPath, IPath> mapping(ItemRecord record) {

        Map<IPath, IPath> toImportMap = new HashMap<IPath, IPath>();

        if (record.isValid()) {
            Property property = record.getProperty();
            // MOD by zshen for bug 18724 2011.02.23
            IPath itemPath = null;
            IPath itemDesPath = null;
            if (property != null) {
                itemPath = PropertyHelper.getItemPath(property);
            } else {
                String currentProjectName = ResourceManager.getRootProjectName();

                IPath fullPath = record.getFullPath();

                int constantIndex = EResourceConstant.getTopConstantIndexFromPath(fullPath);

                itemPath = new Path(currentProjectName).append(fullPath.removeFirstSegments(constantIndex));
            }
            itemDesPath = ResourcesPlugin.getWorkspace().getRoot().getFile(itemPath).getLocation();
            IPath propDesPath = itemDesPath.removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

            toImportMap.put(record.getFilePath(), itemDesPath);
            if (property == null) {
                return toImportMap;
            }
            toImportMap.put(record.getPropertyPath(), propDesPath);
        }

        return toImportMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#write(org.eclipse.core.runtime.IPath,
     * org.eclipse.core.runtime.IPath)
     */
    public void write(IPath resPath, IPath desPath) throws IOException, CoreException {
        File resFile = resPath.toFile();
        File desFile = desPath.toFile();

        boolean isCovered = desFile.exists();
        if (isCovered) {
            // we'd better delete the old file first to make the file name case sensitive to avoid causing get resource
            // error

            desFile.delete();
            log.warn(DefaultMessagesImpl.getString("FileSystemImportWriter.Overwritten", desFile.getAbsoluteFile()));//$NON-NLS-1$ 
        }

        FileUtils.copyFile(resFile, desFile);

        if (isCovered) {
            updateFilesCoverd.add(desFile);
        } else {
            updateFiles.add(desFile);
        }
    }

    /**
     * DOC bZhou Comment method "update".
     * 
     * @param desFile
     * @param isCovered
     * 
     * @throws IOException
     * @throws CoreException
     * 
     * @throws Exception
     */
    private void update(File desFile, boolean isCovered) throws IOException, CoreException {
        String curProjectLabel = ResourceManager.getRootProjectName();
        boolean needReloadResource = false;
        if (desFile.exists()) {
            IFile desIFile = ResourceService.file2IFile(desFile);
            String fileExt = desIFile.getFileExtension();

            if (FactoriesUtil.isEmfFile(fileExt)) {
                needReloadResource = true;
                if (!StringUtils.equals(projectName, curProjectLabel)) {
                    String content = FileUtils.readFileToString(desFile, "utf-8");//$NON-NLS-1$
                    content = StringUtils.replace(content, "/" + projectName + "/", "/" + curProjectLabel + "/");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
                    FileUtils.writeStringToFile(desFile, content, "utf-8");//$NON-NLS-1$
                }
            }

            if (fileExt.equals(FactoriesUtil.PROPERTIES_EXTENSION)) {
                needReloadResource = true;
                Property property = PropertyHelper.getProperty(desIFile, true);

                if (property != null) {
                    User user = ReponsitoryContextBridge.getUser();
                    if (user != null && property.getAuthor().getLogin() == null) {
                        property.setAuthor(user);
                        EMFSharedResources.getInstance().saveResource(property.eResource());
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("property file for " + desIFile + " = " + property.getLabel());//$NON-NLS-1$ //$NON-NLS-2$
                    }
                } else {
                    log.error("Loading property error: " + desIFile.getFullPath().toString());//$NON-NLS-1$
                }
            }
            if (isCovered && needReloadResource) {
                URI uri = URI.createPlatformResourceURI(desIFile.getFullPath().toString(), false);
                EMFSharedResources.getInstance().reloadResource(uri);
            }

        } else {
            log.error(DefaultMessagesImpl.getString(
                    "FileSystemImportWriter.destinationFileIsNotExist", desFile.getAbsolutePath())); //$NON-NLS-1$
        }
    }

    /*
     * After check the conflicts of the imported object, calling this method (from ImportWizard) replace the conflicts
     * object in the records if the record is valid; then call the finish to do migrate. OR: merge the conflict system
     * indicators if valid.(overwrite)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImexWriter#write(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * [], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void write(ItemRecord[] records, IProgressMonitor monitor) {
        write(records, monitor, false);
    }

    /*
     * After check the conflicts of the imported object, calling this method (from ImportWizard) replace the conflicts
     * object in the records if the record is valid; then call the finish to do migrate. OR: merge the conflict system
     * indicators if valid.(overwrite)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImexWriter#write(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * [], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void write(ItemRecord[] records, IProgressMonitor monitor, boolean isOverwrite) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        final ItemRecord[] fRecords = records;
        final IProgressMonitor fMonitor = monitor;
        final boolean fIsOverwrite = isOverwrite;

        need2MergeModelElementList.clear();
        allImportItems.clear();

        RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("Import TDQ Element") {//$NON-NLS-1$

                    @Override
                    protected void run() {
                        try {
                            List<ItemRecord> needToRemoveList = new ArrayList<ItemRecord>();
                            int work = 0;
                            for (ItemRecord record : fRecords) {
                                IRepositoryViewObject object = record.getConflictObject();
                                if (object != null) {
                                    Property conflictProperty = object.getProperty();
                                    ModelElement modEle = record.getElement();
                                    if (record.isNeedToRenameFirst()) {
                                        conflictProperty.setLabel(modEle.getName());
                                        ElementWriterFactory
                                                .getInstance()
                                                .create(conflictProperty.getItem())
                                                .save(conflictProperty.getItem(), true);
                                        record.seteConflictType(EConflictType.UUID);
                                    }
                                }
                            }

                    List<String> importedContext = new ArrayList<String>();
                    for (ItemRecord record : fRecords) {

                                if (fMonitor.isCanceled()) {
                                    break;
                                }

                                Map<IPath, IPath> toImportMap = mapping(record);

                                fMonitor.subTask(DefaultMessagesImpl.getString(
                                        "FileSystemImportWriter.Importing", record.getName()));//$NON-NLS-1$

                                if (record.isValid()) {
                                    log.info(DefaultMessagesImpl.getString(
                                            "FileSystemImportWriter.Importing", record.getFile().getAbsolutePath()));//$NON-NLS-1$

                                    // Delete the conflict node before import.
                                    IRepositoryViewObject object = record.getConflictObject();
                                    boolean isDelete = true;
                                    ModelElement modEle = record.getElement();
                                    if (object != null) {
                                        // added 20120808 yyin TDQ-4189
                                        // when record is valid&conflict, means it need to be merged with the current
                                        // one if it
                                        // is a system indicator definition, (using its UUid to find this SI not label)
                                        if (isIndicatorDefinition(modEle)) {
                                            if (isDQRule(modEle)) {
                                                if (isParserRule(modEle)) {
                                                    mergeParserRule(record, (TDQBusinessRuleItem) object
                                                            .getProperty()
                                                            .getItem());
                                                    isDelete = false;
                                                } else if (isWhereRule(modEle)) {
                                                    storeDependencyForIndicator(record);
                                                    isDelete = false;
                                                    needToRemoveList.add(record);
                                                }
                                            } else if (isMatchRuleDefinition(modEle)) {
                                                // do nothing here now
                                            } else {
                                                // System Indicator and UDI need merge
                                                TDQIndicatorDefinitionItem indItem =
                                                        (TDQIndicatorDefinitionItem) object.getProperty().getItem();
                                                mergeSystemIndicator(record, indItem);
                                                // only add it when it is UDIndicatorDefinition
                                                if (record.getElement() instanceof UDIndicatorDefinition) {
                                                    need2MergeModelElementList.add(record);
                                                    storeDependencyForIndicator(record);
                                                }
                                                isDelete = false;
                                            }
                                        } else if (isPattern(modEle)) {
                                            TDQPatternItem patternItem =
                                                    (TDQPatternItem) object.getProperty().getItem();
                                            mergePattern(record, patternItem);
                                            need2MergeModelElementList.add(record);
                                            storeDependencyForIndicator(record);
                                            isDelete = false;
                                        } else if (isConnection(modEle) || isAnalysis(modEle) || isReport(modEle)) {
                                            storeDependency(record);
                                            isDelete = false;
                                            if (!fIsOverwrite) {
                                                continue;
                                            }
                                            work--;
                                            needToRemoveList.add(record);
                                        } else {
                                            if (!fIsOverwrite) {
                                                continue;
                                            }
                                            // remove the dependency of the object
                                            EObjectHelper.removeDependencys(PropertyHelper.getModelElement(object
                                                    .getProperty()));
                                            isDelete = true;
                                            // delete the object
                                            ProxyRepositoryFactory.getInstance().deleteObjectPhysical(object);
                                        }
                                    }

                                    if (isDelete) {
                                        updateFiles.clear();
                                        updateFilesCoverd.clear();

                                        for (IPath resPath : toImportMap.keySet()) {
                                            IPath desPath = toImportMap.get(resPath);
                                            ResourceSet resourceSet =
                                                    ProxyRepositoryFactory
                                                            .getInstance()
                                                            .getRepositoryFactoryFromProvider()
                                                            .getResourceManager().resourceSet;
                                            synchronized (resourceSet) {
                                                write(resPath, desPath);
                                                allCopiedFiles.add(desPath.toFile());
                                            }
                                            allImportItems.add(desPath);
                                            // TDQ-12180
                                            AbstractSvnRepositoryService svnReposService =
                                                    GlobalServiceRegister.getDefault().getSvnRepositoryService(
                                                            AbstractSvnRepositoryService.class);
                                            if (svnReposService != null) {
                                                svnReposService.addIfImportOverride(desPath);
                                            }
                                        }
                                        for (File file : updateFiles) {
                                            update(file, false);
                                        }
                                        for (File file : updateFilesCoverd) {
                                            update(file, true);
                                        }
                                    }

                            // Added TDQ-15353, need to check the context if any in analysis/report
                            if (isAnalysis(modEle) || isReport(modEle)) {
                                clearContextIfNotImported(record, importedContext);
                            }
                                } else {
                                    for (String error : record.getErrorMessage()) {
                                        log.error(error);
                                    }
                                }

                                fMonitor.worked(++work);
                            }

                            for (ItemRecord removeRecord : needToRemoveList) {

                                Map<IPath, IPath> toImportMap = mapping(removeRecord);
                                IRepositoryViewObject removeViewObject = removeRecord.getConflictObject();
                                // remove the dependency of the object
                                // EObjectHelper.removeDependencys(PropertyHelper.getModelElement(removeViewObject
                                // .getProperty()));
                                // delete the object
                                ProxyRepositoryFactory.getInstance().deleteObjectPhysical(removeViewObject);

                                updateFiles.clear();
                                updateFilesCoverd.clear();

                                for (IPath resPath : toImportMap.keySet()) {
                                    IPath desPath = toImportMap.get(resPath);
                                    ResourceSet resourceSet =
                                            ProxyRepositoryFactory
                                                    .getInstance()
                                                    .getRepositoryFactoryFromProvider()
                                                    .getResourceManager().resourceSet;
                                    synchronized (resourceSet) {
                                        write(resPath, desPath);
                                        allCopiedFiles.add(desPath.toFile());
                                    }
                                    allImportItems.add(desPath);
                                    // TDQ-12180
                                    AbstractSvnRepositoryService svnReposService =
                                            GlobalServiceRegister.getDefault().getSvnRepositoryService(
                                                    AbstractSvnRepositoryService.class);
                                    if (svnReposService != null) {
                                        svnReposService.addIfImportOverride(desPath);
                                    }
                                }
                                for (File file : updateFiles) {
                                    update(file, false);
                                }
                                for (File file : updateFilesCoverd) {
                                    update(file, true);
                                }

                                fMonitor.worked(++work);
                            }

                            finish(fRecords, fMonitor);

                        } catch (Exception e) {
                            log.error(e, e);
                        }
                    }

                    private void storeDependency(ItemRecord record) {
                        // can not add unused dependency else there is not be save.So that we need to compare at here
                        Property needStoreProperty = record.getConflictObject().getProperty();
                        ModelElement modelElement = PropertyHelper.getModelElement(needStoreProperty);
                        record.getClientDepenList().addAll(EcoreUtil.copyAll(modelElement.getClientDependency()));
                        record.getSupplierDepenList().addAll(EcoreUtil.copyAll(modelElement.getSupplierDependency()));
                        Iterator<Dependency> iterator = modelElement.getClientDependency().iterator();
                        int index = 0;
                        while (iterator.hasNext()) {
                            Dependency clientDependency = iterator.next();
                            record.getClientDepenList()
                                    .get(index)
                                    .getClient()
                                    .addAll(EcoreUtil.copyAll(clientDependency.getClient()));
                            record.getClientDepenList()
                                    .get(index)
                                    .getSupplier()
                                    .addAll(EcoreUtil.copyAll(clientDependency.getSupplier()));
                            index++;
                            Iterator<ModelElement> elementIterator = clientDependency.getSupplier().iterator();
                            while (elementIterator.hasNext()) {
                                ModelElement next = elementIterator.next();
                                Property property = PropertyHelper.getProperty(next);

                                if (property == null) {
                                    property = PropertyHelper.getProperty(clientDependency);
                                }
                                IFile modelElementIfile =
                                        WorkspaceUtils.getModelElementResource(property.eResource().getURI());
                                record.getSupplierDepenFileList().add(modelElementIfile);
                            }
                        }
                        iterator = modelElement.getSupplierDependency().iterator();
                        index = 0;
                        while (iterator.hasNext()) {
                            Dependency supplierDependency = iterator.next();
                            record.getSupplierDepenList()
                                    .get(index)
                                    .getClient()
                                    .addAll(EcoreUtil.copyAll(supplierDependency.getClient()));
                            record.getSupplierDepenList()
                                    .get(index)
                                    .getSupplier()
                                    .addAll(EcoreUtil.copyAll(supplierDependency.getSupplier()));
                            index++;
                            Iterator<ModelElement> elementIterator = supplierDependency.getClient().iterator();
                            while (elementIterator.hasNext()) {
                                ModelElement next = elementIterator.next();
                                if (next.eIsProxy()) {
                                    continue;
                                }
                                IFile modelElementIfile =
                                        WorkspaceUtils.getModelElementResource(PropertyHelper
                                                .getProperty(next)
                                                .eResource()
                                                .getURI());
                                record.getClientDepenFileList().add(modelElementIfile);
                            }
                        }
                    }

                    private void storeDependencyForIndicator(ItemRecord record) {
                        // can not add unused dependency else there is not be save.So that we need to compare at here
                        Property needStoreProperty = record.getProperty();
                        ModelElement modelElement = PropertyHelper.getModelElement(needStoreProperty);
                        record.getClientDepenList().addAll(EcoreUtil.copyAll(modelElement.getClientDependency()));
                        record.getSupplierDepenList().addAll(EcoreUtil.copyAll(modelElement.getSupplierDependency()));
                        Iterator<Dependency> iterator = modelElement.getClientDependency().iterator();
                        int index = 0;
                        while (iterator.hasNext()) {
                            Dependency clientDependency = iterator.next();
                            record.getClientDepenList()
                                    .get(index)
                                    .getClient()
                                    .addAll(EcoreUtil.copyAll(clientDependency.getClient()));
                            record.getClientDepenList()
                                    .get(index)
                                    .getSupplier()
                                    .addAll(EcoreUtil.copyAll(clientDependency.getSupplier()));
                            index++;
                            Iterator<ModelElement> elementIterator = clientDependency.getSupplier().iterator();
                            while (elementIterator.hasNext()) {
                                ModelElement next = elementIterator.next();
                                IFile modelElementIfile =
                                        WorkspaceUtils.getModelElementResource(PropertyHelper
                                                .getProperty(next)
                                                .eResource()
                                                .getURI());
                                record.getSupplierDepenFileList().add(modelElementIfile);
                            }
                        }
                        iterator = modelElement.getSupplierDependency().iterator();
                        index = 0;
                        while (iterator.hasNext()) {
                            Dependency supplierDependency = iterator.next();
                            record.getSupplierDepenList()
                                    .get(index)
                                    .getClient()
                                    .addAll(EcoreUtil.copyAll(supplierDependency.getClient()));
                            record.getSupplierDepenList()
                                    .get(index)
                                    .getSupplier()
                                    .addAll(EcoreUtil.copyAll(supplierDependency.getSupplier()));
                            index++;
                            Iterator<ModelElement> elementIterator = supplierDependency.getClient().iterator();
                            while (elementIterator.hasNext()) {
                                ModelElement next = elementIterator.next();
                                if (next.eIsProxy()) {
                                    continue;
                                }
                                IFile modelElementIfile =
                                        WorkspaceUtils.getModelElementResource(PropertyHelper
                                                .getProperty(next)
                                                .eResource()
                                                .getURI());
                                record.getClientDepenFileList().add(modelElementIfile);
                            }
                        }
                    }

            // need to check every context id: go through every context parameters in analysis/report, if its contextid
            // : the context is not in the imported list,then clear it.
            private void clearContextIfNotImported(ItemRecord record, List<String> importedContext) {
                List<ContextType> contexts = null;

                if (isAnalysis(record.getElement())) {
                    contexts = ((Analysis) record.getElement()).getContextType();
                } else if (isReport(record.getElement())) {
                    contexts = ((TdReport) record.getElement()).getContext();
                }

                if (contexts != null && !contexts.isEmpty()) {
                    // store <context id, isimported = true>
                    Map<String, Boolean> contextIdMap = new HashMap<String, Boolean>();
                    boolean isModified = false;

                    for (ContextType contextType : contexts) {
                        Iterator<?> contextParams = contextType.getContextParameter().iterator();
                        while (contextParams.hasNext()) {
                            ContextParameterType contextParameterType = (ContextParameterType) contextParams.next();
                            String repositoryContextId = contextParameterType.getRepositoryContextId();
                            Boolean contextImported = contextIdMap.get(repositoryContextId);
                            if (contextImported == null) {
                                contextImported = findContextInImportList(repositoryContextId, importedContext);
                                contextIdMap.put(repositoryContextId, contextImported);
                            }
                            if (!contextImported) {
                                contextParameterType.setRepositoryContextId(null);
                                isModified = true;
                            }
                        }
                    }

                    if (isModified) {
                        try {
                            ElementWriterFactory.getInstance().create(record.getElement()).save(record.getElement());
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                }
            }

            // only need to go through all records once to find all selected contexts.
            private Boolean findContextInImportList(String repositoryContextId, List<String> importedContext) {
                if (importedContext.isEmpty()) {
                    for (ItemRecord record : fRecords) {
                        if (record.getProperty().getItem() instanceof ContextItem) {
                            importedContext.add(record.getProperty().getId());
                        }
                    }
                }
                return importedContext.contains(repositoryContextId);
            }
                };

        workUnit.setAvoidUnloadResources(Boolean.TRUE);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);

        // after above workUnit executed, the imported items will worked, than can do merge/update about UDI and Pattern
        RepositoryWorkUnit<Object> workUnitFinish = new RepositoryWorkUnit<Object>("Finish Import TDQ Element") { //$NON-NLS-1$

                    @Override
                    protected void run() throws LoginException, PersistenceException {
                        try {
                            postFinish();
                        } catch (IOException e) {
                            log.error(e, e);
                        }
                    }
                };

        workUnitFinish.setAvoidUnloadResources(Boolean.TRUE);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnitFinish);
    }

    /**
     * for ParserRule: 1) replace the same name old rule with new rule; 2) will keep the old rule if new one don't
     * include the same name rule
     * 
     * @param record imported modified parser rule
     * @param parserRuleItem the parser rule in the current studio
     */
    protected void mergeParserRule(ItemRecord record, TDQBusinessRuleItem parserRuleItem) {
        // only when the parser rule is modified, do the save
        boolean isModified = false;

        // old object
        DQRule parserRule = parserRuleItem.getDqrule();
        Property parserRuleProp = parserRuleItem.getProperty();

        // new object
        DQRule recordRule = (DQRule) record.getElement();
        Property recordRuleProp = record.getProperty();

        // get expression list from record
        EList<TdExpression> importedExs = recordRule.getSqlGenericExpression();
        // for each expression:
        for (TdExpression importedEx : importedExs) {
            TdExpression systemExpression = null;
            for (TdExpression ex : parserRule.getSqlGenericExpression()) {
                if (ex.getName().equals(importedEx.getName())) {
                    systemExpression = ex;
                    break;
                }
            }
            if (systemExpression != null) {
                IndicatorDefinitionFileHelper.removeSqlExpressionByName(parserRule, importedEx.getName());
                IndicatorDefinitionFileHelper.addSqlExpression(parserRule, importedEx.getName(),
                        importedEx.getLanguage(), importedEx.getBody(), importedEx.getModificationDate());
            } else {
                IndicatorDefinitionFileHelper.addSqlExpression(parserRule, importedEx.getName(),
                        importedEx.getLanguage(), importedEx.getBody(), importedEx.getModificationDate());
            }
            isModified = true;
        }

        // for ParserRule Metadata
        if (parserRuleProp != null && recordRuleProp != null) {
            if (!StringUtils.isBlank(recordRuleProp.getPurpose())) {
                parserRuleProp.setPurpose(recordRuleProp.getPurpose());
            }
            if (!StringUtils.isBlank(recordRuleProp.getDescription())) {
                parserRuleProp.setDescription(recordRuleProp.getDescription());
            }
            parserRuleProp.setAuthor(recordRuleProp.getAuthor());
            parserRuleProp.setStatusCode(recordRuleProp.getStatusCode());
            isModified = true;
        }

        if (isModified) {
            ElementWriterFactory.getInstance().createdRuleWriter().save(parserRuleItem, false);
        }
    }

    /**
     * Added: (20120808 yyin, TDQ-4189) The system indicators are not read-only because the user may want to write his
     * own SQL template. so this task deals with the modified SI from imported one, and merge them with the current
     * studio. 1)only when the user select the"Overwrite existing items" on the import wizard(and the modifydate is
     * newer than the current studio's SI), the conflict modification in imported SI will overwrite the ones in current
     * studio, otherwise, the SI in current studio will keep. 2)If a language does not exist in the system indicator but
     * exists in the user modified indicator, then we add it 3)if a language exists in the system indicator but has been
     * removed in the user modified indicator, then we keep the system indicator definition. [for Indicator
     * matadata(Purpose, Description, Author, Status): 1) will replace old value with new value if new value is not
     * blank; 2) will keep old value if new value is blank][for IndicatorDefinitionParameter: 1) will replace the same
     * name old parameter with new parameter; 2) will keep the old parameter if new one don't include the same name
     * parameter ]
     * 
     * @param record imported modified system indicator
     * @param siDef the system indicator in the current studio
     */
    protected void mergeSystemIndicator(ItemRecord record, TDQIndicatorDefinitionItem siDefItem) {
        // only when the Si is modified, do the save
        boolean isModified = false;

        // old object
        IndicatorDefinition siDef = siDefItem.getIndicatorDefinition();
        Property siProp = siDefItem.getProperty();

        // new object
        IndicatorDefinition indDef = (IndicatorDefinition) record.getElement();
        Property indDefProp = record.getProperty();

        // get expression list from record
        EList<TdExpression> importedExs = indDef.getSqlGenericExpression();
        // for each expression:
        for (TdExpression importedEx : importedExs) {
            // if the modify date ==null, means it is not modified, do nothing
            if (importedEx.getModificationDate() == null) {
                continue;
            }

            // find the related template in system indicator(with same language)
            TdExpression systemExpression = null;
            for (TdExpression ex : siDef.getSqlGenericExpression()) {
                if (ex.getLanguage().equals(importedEx.getLanguage())) {
                    systemExpression = ex;
                    break;
                }
            }

            // if new, add to SI
            if (systemExpression == null) {
                IndicatorDefinitionFileHelper.addSqlExpression(siDef, importedEx.getLanguage(), importedEx.getBody(),
                        importedEx.getModificationDate());
                isModified = true;
            } else {// if the expression are different: compare the modify date, make the SI keep the new one
                if (replaceExpression(systemExpression, importedEx)) {
                    IndicatorDefinitionFileHelper.removeSqlExpression(siDef, importedEx.getLanguage());
                    IndicatorDefinitionFileHelper.addSqlExpression(siDef, importedEx.getLanguage(),
                            importedEx.getBody(), importedEx.getModificationDate());
                    isModified = true;
                }
            }
        }

        // handle the category
        IndicatorCategory siDefCategory = IndicatorCategoryHelper.getCategory(siDef);
        IndicatorCategory indDefCategory = IndicatorCategoryHelper.getCategory(indDef);
        siDefCategory = (IndicatorCategory) EObjectHelper.resolveObject(siDefCategory);
        indDefCategory = (IndicatorCategory) EObjectHelper.resolveObject(indDefCategory);

        if (!ModelElementHelper.compareUUID(siDefCategory, indDefCategory)) {
            // use the imported one
            IndicatorCategoryHelper.setCategory(siDef, indDefCategory);
            isModified = true;
        } else {
            // if the uuid is the same, but the label is different
            if (siDefCategory != null && indDefCategory != null && !siDefCategory.eIsProxy()) {
                if (!indDefCategory.equals(siDefCategory)) {
                    // especially: "Pattern Finder" is changed by us
                    if (!indDefCategory.getLabel().equals(RenamePatternFinderFolderTask.PATTERN_FINDER)) {
                        IndicatorCategoryHelper.setCategory(siDef, indDefCategory);
                        isModified = true;
                    }
                }
            }
        }

        // for Indicator Metadata
        if (siProp != null && indDefProp != null) {
            if (!StringUtils.isBlank(indDefProp.getPurpose())) {
                siProp.setPurpose(indDefProp.getPurpose());
            }
            if (!StringUtils.isBlank(indDefProp.getDescription())) {
                siProp.setDescription(indDefProp.getDescription());
            }
            siProp.setAuthor(indDefProp.getAuthor());
            siProp.setStatusCode(indDefProp.getStatusCode());
            isModified = true;
        }

        // for judi's jar file information
        String jarFilePath = TaggedValueHelper.getJarFilePath(indDef);
        if (!StringUtils.isBlank(jarFilePath)) {
            TaggedValueHelper.setJarFilePath(jarFilePath, siDef);
            isModified = true;
        }
        String classNameText = TaggedValueHelper.getClassNameText(indDef);
        if (!StringUtils.isBlank(classNameText)) {
            TaggedValueHelper.setClassNameText(classNameText, siDef);
            isModified = true;
        }

        // for IndicatorDefinintionParameter
        EList<IndicatorDefinitionParameter> siParameter = siDef.getIndicatorDefinitionParameter();
        EList<IndicatorDefinitionParameter> indDefParameter = indDef.getIndicatorDefinitionParameter();
        List<IndicatorDefinitionParameter> tempParameter = new ArrayList<IndicatorDefinitionParameter>();
        for (IndicatorDefinitionParameter indDefPara : indDefParameter) {
            boolean include = false;
            String key = indDefPara.getKey();
            for (IndicatorDefinitionParameter siPara : siParameter) {
                if (key.equals(siPara.getKey())) {
                    include = true;
                    siPara.setValue(indDefPara.getValue());
                    isModified = true;
                }
            }
            if (!include) {
                tempParameter.add(indDefPara);
                isModified = true;
            }
        }
        if (isModified && !tempParameter.isEmpty()) {
            siParameter.addAll(tempParameter);
        }

        // replace the name (using the imported name incase of modify the name), and save the SI
        // siDef.setName(record.getElement().getName());

        if (isModified) {
            try {
                ElementWriterFactory.getInstance().createIndicatorDefinitionWriter().save(siDefItem, false);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    /**
     * when imported pattern is from lower version, even if it is modified, the "modify date" is still null, so, even if
     * the modify date is null ,still do the comparation. [for Pattern matadata(Purpose, Description, Author, Status):
     * 1) will replace old value with new value if new value is not blank; 2) will keep old value if new value is blank]
     * 
     * @param record
     * @param patternItem
     */
    protected void mergePattern(ItemRecord record, TDQPatternItem patternItem) {
        // only when the Si is modified, do the save
        boolean isModified = false;

        // old objects
        Pattern pattern = patternItem.getPattern();
        Property patternProp = patternItem.getProperty();

        // new objects
        Pattern recordPattern = (Pattern) record.getElement();
        Property recordProp = record.getProperty();

        // get expression list from record
        EList<PatternComponent> importComponents = recordPattern.getComponents();
        // for each expression:
        for (PatternComponent component : importComponents) {
            // if the modify date ==null, maybe it is from lower version, still do the compare
            TdExpression importEx = ((RegularExpression) component).getExpression();
            PatternComponent replaced = null;
            for (PatternComponent pComp : pattern.getComponents()) {
                TdExpression pex = ((RegularExpression) pComp).getExpression();
                if (importEx.getLanguage().equals(pex.getLanguage())) {
                    replaced = pComp;
                    break;
                }
            }

            // if new, add to SI
            if (replaced == null) {
                pattern.getComponents().add(createPatternComponent(component));
                isModified = true;
            } else {// if the expression are different: compare the modify date, make the SI keep the new one
                if (replaceExpression(((RegularExpression) replaced).getExpression(), importEx)) {
                    pattern.getComponents().remove(replaced);
                    pattern.getComponents().add(createPatternComponent(component));
                    isModified = true;
                }
            }
        }

        // for Pattern Metadata
        if (patternProp != null && recordProp != null) {
            if (!StringUtils.isBlank(recordProp.getPurpose())) {
                patternProp.setPurpose(recordProp.getPurpose());
            }
            if (!StringUtils.isBlank(recordProp.getDescription())) {
                patternProp.setDescription(recordProp.getDescription());
            }
            patternProp.setAuthor(recordProp.getAuthor());
            patternProp.setStatusCode(recordProp.getStatusCode());
            isModified = true;
        }

        // replace the name (using the imported name incase of modify the name), and save the SI
        // siDef.setName(record.getElement().getName());

        if (isModified) {
            ElementWriterFactory.getInstance().createPatternWriter().save(patternItem, false);
        }
    }

    /**
     * remove the old client dependency add a new one in Anlaysis.
     * 
     * @param supplierItem
     * @param modelElement
     * @throws PersistenceException
     */
    private void updateAnalysisClientDependence(Property clientProperty, Property anaProperty)
            throws PersistenceException {
        ModelElement anaModelElement = PropertyHelper.getModelElement(anaProperty);
        if (anaModelElement != null) {
            Analysis analysis = (Analysis) anaModelElement;
            EList<Dependency> clientDependency = anaModelElement.getClientDependency();
            Iterator<Dependency> it = clientDependency.iterator();
            ModelElement supplierModelElement = PropertyHelper.getModelElement(clientProperty);
            Resource supModeResource = supplierModelElement.eResource();
            while (it.hasNext()) {
                Dependency clientDep = it.next();
                // when the client dependence is proxy and its lastSegment of uri is same as
                // systemSupplyModelElement,remove it.
                if (clientDep.eResource() == null) {
                    URI clientDepURI = ((InternalEObject) clientDep).eProxyURI();
                    boolean isUDI =
                            clientDepURI.path().contains(
                                    ResourceManager.getUDIFolder().getProjectRelativePath().toString());
                    boolean isPattern =
                            clientDepURI.path().contains(
                                    ResourceManager.getPatternFolder().getProjectRelativePath().toString());
                    if (supModeResource != null && (isUDI || isPattern)
                            && clientDepURI.lastSegment().equals(supModeResource.getURI().lastSegment())) {
                        it.remove();
                        break;
                    }
                }
            }
            DependenciesHandler.getInstance().setUsageDependencyOn(anaModelElement, supplierModelElement);
            // TDQ-8436 remove the old pattern and add the new pattern in analysis Indicator Parameters.
            if (isPattern(supplierModelElement)) {
                updatePatternInAnaParams(supplierModelElement, analysis);
                ElementWriterFactory.getInstance().createPatternWriter().save(clientProperty.getItem(), true);
            }

            // remove old udi and set a new one in the analysis indicators.
            if (supplierModelElement instanceof UDIndicatorDefinition) {
                if (analysis.getResults() != null) {
                    EList<Indicator> indicators = analysis.getResults().getIndicators();
                    Iterator<Indicator> itIndicators = indicators.iterator();
                    while (itIndicators.hasNext()) {
                        Indicator indicator = itIndicators.next();
                        IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
                        if (indicatorDefinition.eResource() == null) {
                            URI indicatorDefURI = ((InternalEObject) indicatorDefinition).eProxyURI();
                            if (supModeResource != null && UDIHelper.isUDI(indicator)
                                    && indicatorDefURI.lastSegment().equals(supModeResource.getURI().lastSegment())) {
                                indicator.setIndicatorDefinition((UDIndicatorDefinition) supplierModelElement);
                                break;
                            }
                        }
                    }
                    ElementWriterFactory
                            .getInstance()
                            .createIndicatorDefinitionWriter()
                            .save(clientProperty.getItem(), true);
                }
            }

            // only save analysis item at here.
            ProxyRepositoryFactory.getInstance().save(anaProperty.getItem(), true);
        }

    }

    /**
     * remove the old client dependency add a new one in Anlaysis.
     * 
     * @param supplierItem
     * @param modelElement
     * @throws PersistenceException
     */
    private void updateAnalysisClientDependence(TDQItem supplierItem, Property property) throws PersistenceException {
        ModelElement anaModelElement = PropertyHelper.getModelElement(property);
        if (anaModelElement != null) {
            Analysis analysis = (Analysis) anaModelElement;
            EList<Dependency> clientDependency = anaModelElement.getClientDependency();
            Iterator<Dependency> it = clientDependency.iterator();
            ModelElement supplierModelElement = RepositoryNodeHelper.getResourceModelElement(supplierItem);
            Resource supModeResource = supplierModelElement.eResource();
            while (it.hasNext()) {
                Dependency clientDep = it.next();
                // when the client dependence is proxy and its lastSegment of uri is same as
                // systemSupplyModelElement,remove it.
                if (clientDep.eResource() == null) {
                    URI clientDepURI = ((InternalEObject) clientDep).eProxyURI();
                    boolean isUDI =
                            clientDepURI.path().contains(
                                    ResourceManager.getUDIFolder().getProjectRelativePath().toString());
                    boolean isPattern =
                            clientDepURI.path().contains(
                                    ResourceManager.getPatternFolder().getProjectRelativePath().toString());
                    if (supModeResource != null && (isUDI || isPattern)
                            && clientDepURI.lastSegment().equals(supModeResource.getURI().lastSegment())) {
                        it.remove();
                        break;
                    }
                }
            }
            DependenciesHandler.getInstance().setUsageDependencyOn(anaModelElement, supplierModelElement);
            // TDQ-8436 remove the old pattern and add the new pattern in analysis Indicator Parameters.
            if (isPattern(supplierModelElement)) {
                updatePatternInAnaParams(supplierModelElement, analysis);
                ElementWriterFactory.getInstance().createPatternWriter().save(supplierItem, true);
            }

            // remove old udi and set a new one in the analysis indicators.
            if (supplierModelElement instanceof UDIndicatorDefinition) {
                if (analysis.getResults() != null) {
                    EList<Indicator> indicators = analysis.getResults().getIndicators();
                    Iterator<Indicator> itIndicators = indicators.iterator();
                    while (itIndicators.hasNext()) {
                        Indicator indicator = itIndicators.next();
                        IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
                        if (indicatorDefinition.eResource() == null) {
                            URI indicatorDefURI = ((InternalEObject) indicatorDefinition).eProxyURI();
                            if (supModeResource != null && UDIHelper.isUDI(indicator)
                                    && indicatorDefURI.lastSegment().equals(supModeResource.getURI().lastSegment())) {
                                indicator.setIndicatorDefinition((UDIndicatorDefinition) supplierModelElement);
                                break;
                            }
                        }
                    }
                    ElementWriterFactory.getInstance().createIndicatorDefinitionWriter().save(supplierItem, true);
                }
            }

            // only save analysis item at here.
            ProxyRepositoryFactory.getInstance().save(property.getItem(), true);
        }

    }

    /**
     * if there is a same name pattern in current workspace,update the pattern in imported analysis IndicatorParameters.
     * 
     * @param systemSupplyModelElement
     * @param analysis
     */
    private void updatePatternInAnaParams(ModelElement systemSupplyModelElement, Analysis analysis) {
        if (analysis.getResults() != null) {
            EList<Indicator> indicators = analysis.getResults().getIndicators();
            IndicatorParameters parameters = null;
            for (Indicator indicator : indicators) {
                // AllMatchIndicator is in column set analysis.
                if (indicator instanceof AllMatchIndicator) {
                    EList<RegexpMatchingIndicator> list =
                            ((AllMatchIndicator) indicator).getCompositeRegexMatchingIndicators();
                    for (RegexpMatchingIndicator regMatchIndicator : list) {
                        parameters = regMatchIndicator.getParameters();
                        removOldAddSysPatternInAnaParams(parameters, (Pattern) systemSupplyModelElement, analysis);
                    }
                } else if (indicator instanceof PatternMatchingIndicator) {
                    parameters = ((PatternMatchingIndicator) indicator).getParameters();
                    removOldAddSysPatternInAnaParams(parameters, (Pattern) systemSupplyModelElement, analysis);
                }
            }
        }
    }

    /**
     * 
     * remove the old pattern from IndicatorParameters of imported analysis ,then add the current workspace pattern into
     * IndicatorParameters.
     * 
     * @param indParameters
     * @param sysPattern
     * @param analysis
     */
    private void removOldAddSysPatternInAnaParams(IndicatorParameters indParameters, Pattern sysPattern,
            Analysis analysis) {
        if (null == indParameters || null == indParameters.getDataValidDomain()) {
            return;
        }
        EList<Pattern> patterns = indParameters.getDataValidDomain().getPatterns();
        Iterator<Pattern> itPatterns = patterns.iterator();
        while (itPatterns.hasNext()) {
            Pattern oldPattern = itPatterns.next();
            if (oldPattern.eResource() == null) {
                URI oldPatternUri = EObjectHelper.getURI(oldPattern);
                URI sysPatternUri = EObjectHelper.getURI(sysPattern);
                if (oldPatternUri != null && sysPatternUri != null
                        && sysPatternUri.lastSegment().equals(oldPatternUri.lastSegment())) {
                    itPatterns.remove();
                    indParameters.getDataValidDomain().getPatterns().add(sysPattern);
                    log.info("Pattern '" + sysPattern.getName() + "' is updated in Analysis '" + analysis.getName() //$NON-NLS-1$ //$NON-NLS-2$
                            + "'"); //$NON-NLS-1$
                    break;
                }
            }
        }
    }

    private PatternComponent createPatternComponent(PatternComponent oldComponent) {
        RegularExpression newComponent = PatternFactory.eINSTANCE.createRegularExpression();
        newComponent.setExpression(((RegularExpression) oldComponent).getExpression());
        newComponent.setExpressionType(((RegularExpression) oldComponent).getExpressionType());

        return newComponent;
    }

    private boolean replaceExpression(TdExpression currentEx, TdExpression importedEx) {
        // when both modify date=null, compare the body
        if (importedEx.getModificationDate() == null) {
            if (currentEx.getModificationDate() == null) {
                if (!importedEx.getBody().equalsIgnoreCase(currentEx.getBody())) {
                    return true;
                }
            } else {
                // when current workspace has modified, the imported one not, keep the workspace
                return false;
            }
        } else {// import has modify date
            if (currentEx.getModificationDate() == null) {
                return true;
            } else {
                if (importedEx.getModificationDate().compareToIgnoreCase(currentEx.getModificationDate()) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * when clicking the finish button on the import wizard, execute this method.
     * 
     * @see
     * org.talend.dataprofiler.core.ui.imex.model.IImportWriter#finish(org.talend.dataprofiler.core.ui.imex.model.ItemRecord
     * [], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void finish(ItemRecord[] records, IProgressMonitor monitor) throws IOException, CoreException {
        mergeImportItemsDependency(records);
        cleanImportedItems();
        doMigration(monitor);
        // MOD qiongli 2012-11-8 TDQ-6166.
        notifySQLExplorerForConnection();
        allCopiedFiles.clear();
    }

    private void mergeImportItemsDependency(ItemRecord[] records) {
        // TDQ-15946: for cancel, do nothing here
        if (records == null) {
            return;
        }
        // TDQ-15946~
        for (ItemRecord itemRecord : records) {
            // If record is not a emf element then we don't need to comput dependency
            if (itemRecord.geteConflictType() != null && itemRecord.isEMFValid() && itemRecord.needMergeDependency()) {
                try {
                    Property conflictProperty = itemRecord.getConflictObject().getProperty();
                    IFile itemFile = PropertyHelper.getItemFile(conflictProperty);
                    File locationFile = itemFile.getLocation().toFile();

                    if (conflictProperty.eIsProxy()) {
                        conflictProperty = PropertyHelper.getProperty(itemFile, true);
                    }
                    Property currentProperty = null;
                    if (locationFile.exists()) {
                        currentProperty = EMFSharedResources.getInstance().reloadModelElementInNode(conflictProperty);
                    } else {
                        // sql rule case
                        Map<IPath, IPath> toImportMap = mapping(itemRecord);
                        IFile desIFile =
                                ResourceService.file2IFile(toImportMap.get(itemRecord.getPropertyPath()).toFile());
                        URI platformResourceURI =
                                URI.createPlatformResourceURI(desIFile.getFullPath().toOSString(), false);
                        IFile afterCopyPropertyFile = URIHelper.getFile(platformResourceURI);
                        currentProperty =
                                EMFSharedResources.getInstance().reloadModelElementInNode(conflictProperty,
                                        afterCopyPropertyFile);
                    }
                    // NAME conflict will not come here
                    if (itemRecord.isNeedToRenameFirst()) {
                        // find a way to create a new item and keep old one
                        currentProperty = itemRecord.getProperty();
                    }
                    ModelElement modelElement = PropertyHelper.getModelElement(currentProperty);

                    for (IFile clientElementIFile : itemRecord.getClientDepenFileList()) {
                        IFile workspaceChekFile =
                                ResourcesPlugin.getWorkspace().getRoot().getFile(clientElementIFile.getFullPath());
                        File osCheckFile = WorkspaceUtils.ifileToLocationPath(clientElementIFile).toFile();
                        if (workspaceChekFile != null && workspaceChekFile.exists() && osCheckFile != null
                                && osCheckFile.exists()) {
                            Property clientProperty =
                                    EMFSharedResources.getInstance().reloadModelElementInNode(
                                            PropertyHelper.getProperty(clientElementIFile, false));
                            ModelElement clientElement = PropertyHelper.getModelElement(clientProperty);
                            DependenciesHandler.getInstance().removeClientDependency(clientElement, modelElement);
                            DependenciesHandler.getInstance().setUsageDependencyOn(clientElement, modelElement);
                            ProxyRepositoryFactory.getInstance().save(clientProperty.getItem(), false);
                        }
                        this.allImportItems.add(WorkspaceUtils.ifileToLocationPath(clientElementIFile));
                    }
                    for (IFile SupplierElementIFile : itemRecord.getSupplierDepenFileList()) {
                        IFile workspaceChekFile =
                                ResourcesPlugin.getWorkspace().getRoot().getFile(SupplierElementIFile.getFullPath());
                        File osCheckFile = WorkspaceUtils.ifileToLocationPath(SupplierElementIFile).toFile();
                        if (workspaceChekFile != null && workspaceChekFile.exists() && osCheckFile != null
                                && osCheckFile.exists()) {
                            Property supplierProperty =
                                    EMFSharedResources.getInstance().reloadModelElementInNode(
                                            PropertyHelper.getProperty(SupplierElementIFile, false));
                            ModelElement supplierElement = PropertyHelper.getModelElement(supplierProperty);
                            DependenciesHandler.getInstance().removeClientDependency(modelElement, supplierElement);
                            DependenciesHandler.getInstance().setUsageDependencyOn(modelElement, supplierElement);
                            ProxyRepositoryFactory.getInstance().save(supplierProperty.getItem(), false);
                        }
                        this.allImportItems.add(WorkspaceUtils.ifileToLocationPath(SupplierElementIFile));
                    }
                    ProxyRepositoryFactory.getInstance().save(currentProperty.getItem(), false);
                } catch (PersistenceException e) {
                    log.error(e, e);
                }
            }
        }

    }

    /**
     * removeInvalidDependency and removeLockStatus.
     */
    private void cleanImportedItems() {
        for (File file : allCopiedFiles) {
            if (!file.exists()) {
                continue;
            }
            Property property = PropertyHelper.getProperty(file);
            if (property == null) {
                continue;
            }

            removeInvalidDependency(property);

            // ADD msjian TDQ-7534 2013-8-2: remove all the locked status
            removeLockStatus(property);
            // TDQ-7534~
        }
    }

    private void doMigration(IProgressMonitor monitor) {
        // when monitor is null, it means doing cancel, no need refresh
        if (!commTasks.isEmpty() && monitor != null) {
            ResourceService.refreshStructure();
            MigrationTaskManager.doMigrationTask(commTasks, monitor);
        }
    }

    private void deleteTempProjectFolder() throws IOException {
        if (tempFolder != null && tempFolder.exists()) {
            if (log.isDebugEnabled()) {
                log.debug("Deleting temporary workspace..." + tempFolder.getAbsolutePath());//$NON-NLS-1$
            }
            FileUtils.deleteDirectory(tempFolder);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#migration(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void migration(IProgressMonitor monitor) {

        List<IMigrationTask> modelTasks = new ArrayList<IMigrationTask>();

        if (versionFile != null && versionFile.exists()) {
            ProductVersion version = WorkspaceVersionHelper.getVesion(new Path(versionFile.getAbsolutePath()));
            MigrationTaskManager manager = new MigrationTaskManager(version, MigrationTaskType.FILE);
            List<IMigrationTask> taskList = manager.getValidTasks();

            if (!taskList.isEmpty()) {

                for (IMigrationTask task : taskList) {
                    if (task.isModelTask()) {
                        ((AbstractWorksapceUpdateTask) task).setWorkspacePath(basePath);
                        modelTasks.add(task);
                    } else {
                        // MOD msjian TDQ-7365 2013-5-27: only added the not inclued tasks
                        if (!commTasks.contains(task)) {
                            commTasks.add(task);
                        }
                        // TDQ-7365~
                    }
                }
            }

        }

        if (!modelTasks.isEmpty()) {
            MigrationTaskManager.doMigrationTask(modelTasks, monitor);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#computeInput(org.eclipse.core.runtime.IPath)
     */
    public ItemRecord computeInput(IPath path) {

        if (path != null) {
            versionFile = path.append(EResourceConstant.LIBRARIES.getPath()).append(VERSION_FILE_NAME).toFile();
            definitionFile = path.append(EResourceConstant.LIBRARIES.getPath()).append(DEFINITION_FILE_NAME).toFile();

            if (!versionFile.exists()) {
                return null;
            }

            tempFolder = backUPWorksapce(path);
            if (tempFolder == null) {
                // the error is logged in #checkTempPath()
                return null;
            }

            IPath tempBasePath = new Path(tempFolder.getAbsolutePath());
            IPath projPath = tempBasePath.append(FileConstants.LOCAL_PROJECT_FILENAME);
            setBasePath(tempBasePath);
            if (projPath.toFile().exists()) {
                Object projOBJ = EObjectHelper.retrieveEObject(projPath, PropertiesPackage.eINSTANCE.getProject());
                if (projOBJ != null) {
                    Project project = (Project) projOBJ;
                    projectName = project.getTechnicalLabel();
                }
            } else {
                projectName = ReponsitoryContextBridge.getProjectName();
            }
            return new ItemRecord(tempFolder, tempBasePath);
        }
        return null;
    }

    /**
     * Backup worksapce into stdio worksapce and create a folder which start with tempFolder to store it.
     * Note that we should keep sub class call this method if it must be override.
     * So that we can keep copy import files from same location
     */
    protected File backUPWorksapce(IPath workspacePath) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Back-up workspace...." + workspacePath.toOSString());//$NON-NLS-1$
            }

            File temporaryFolder =
                    ResourceManager.getRootFolderLocation().append("tempFolder" + EcoreUtil.generateUUID())//$NON-NLS-1$
                            .toFile();
            if (!temporaryFolder.exists()) {
                temporaryFolder.mkdir();
            }

            FileUtils.copyDirectory(workspacePath.toFile(), temporaryFolder);
            return temporaryFolder;
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.IImexWriter#setBasePath(org.eclipse.core.runtime.IPath)
     */
    public void setBasePath(IPath path) {
        this.basePath = path;
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
        if (!checkBasePath()) {
            errors.add(DefaultMessagesImpl.getString("FileSystemImportWriter.RootInvalid"));//$NON-NLS-1$
        } else if (!checkVersion()) {
            errors.add(DefaultMessagesImpl.getString("FileSystemImportWriter.VeriryVersion"));//$NON-NLS-1$
        } else if (!checkProject()) {
            errors.add(DefaultMessagesImpl.getString("FileSystemImportWriter.InvalidProject"));//$NON-NLS-1$
        } else if (!checkTempPath()) {
            errors.add(DefaultMessagesImpl.getString("FileSystemImportWriter.CannotCreate"));//$NON-NLS-1$
        }

        return errors;
    }

    /**
     * DOC bZhou Comment method "checkProject".
     * 
     * @return
     */
    private boolean checkProject() {
        return projectName != null;
    }

    /**
     * DOC bZhou Comment method "checkVersion".
     * 
     * @return
     */
    private boolean checkVersion() {
        return versionFile.exists();
    }

    /**
     * DOC bZhou Comment method "checkBasePath".
     * 
     * @return
     */
    private boolean checkBasePath() {
        return basePath != null && basePath.toFile().exists()
                && basePath.append(EResourceConstant.LIBRARIES.getPath()).toFile().exists();
    }

    private boolean checkTempPath() {
        return tempFolder != null && tempFolder.exists();
    }

    /**
     * remove invalid client depenedences before migration.
     */
    private void removeInvalidDependency(Property property) {
        ModelElement modelElement = PropertyHelper.getModelElement(property);
        // no client dependency between Connection and Context.
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(property.getItem());
        if (itemType != ERepositoryObjectType.CONTEXT) {
            if (modelElement != null) {
                removeInvalidDependency(modelElement);
            } else {
                log.info(DefaultMessagesImpl.getString("FileSystemImproWriter.modelElementIsNull")); //$NON-NLS-1$
            }
        }
    }

    private void removeInvalidDependency(ModelElement modelElement) {
        // remove invalid supplier depenedences,e.g,remove some invalid analyses in connection file .
        // remove from model
        boolean needSaveResource = false;
        EList<Dependency> supplierDependencys = modelElement.getSupplierDependency();
        for (Dependency dependency : supplierDependencys) {
            EList<ModelElement> clients = dependency.getClient();
            Iterator<ModelElement> dependencyIterator = clients.iterator();
            while (dependencyIterator.hasNext()) {
                ModelElement client = dependencyIterator.next();
                if (client == null || client.eIsProxy()) {
                    // remove client here
                    dependencyIterator.remove();
                    needSaveResource = true;
                }
            }
        }
        // remove from resource
        Resource modEResource = modelElement.eResource();
        if (modEResource != null) {
            Iterator<EObject> iterator = modEResource.getContents().iterator();
            while (iterator.hasNext()) {
                EObject eObject = iterator.next();
                if (eObject instanceof Dependency && !supplierDependencys.contains(eObject)) {
                    iterator.remove();
                    needSaveResource = true;
                }
            }
        }
        // remove clint Dependency from model
        Iterator<Dependency> ClientDependencyIterator = modelElement.getClientDependency().iterator();
        while (ClientDependencyIterator.hasNext()) {
            Dependency dependency = ClientDependencyIterator.next();
            EList<ModelElement> suppliers = dependency.getSupplier();
            // If dependency is empty then remove dependency directly
            if (suppliers.isEmpty()) {
                ClientDependencyIterator.remove();
                needSaveResource = true;
            }
            // else remove the elemet from dependency
            Iterator<ModelElement> suppLiterator = suppliers.iterator();
            while (suppLiterator.hasNext()) {
                ModelElement supplier = suppLiterator.next();
                if (supplier == null || supplier.eIsProxy()) {
                    suppLiterator.remove();
                    needSaveResource = true;
                }
            }
        }

        if (needSaveResource) {
            EMFSharedResources.getInstance().saveResource(modEResource);
        }
    }

    /**
     * when the item's status is locked, change to unlocked.
     * 
     * @param property
     */
    private void removeLockStatus(Property property) {
        Item item = property.getItem();
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        ERepositoryStatus status = factory.getStatus(item);
        if (status != null && status == ERepositoryStatus.LOCK_BY_USER) {
            try {
                factory.unlock(item);
                // after unlock, reload the resource.
                EMFSharedResources.getInstance().reloadResource(getUri(property));
            } catch (PersistenceException e) {
                log.error(e, e);
            } catch (LoginException e) {
                log.error(e, e);
            }
        }
    }

    /**
     * get the uri from property.
     * 
     * @param property
     * @return
     */
    private URI getUri(Property property) {
        URI uri = property.eResource().getURI();
        if (!uri.isPlatform()) {
            // change the schema from "file" to "platform"
            IFile ifile = WorkspaceUtils.fileToIFile(new File(uri.toFileString()));
            if (ifile != null && ifile.exists()) {
                uri = URI.createPlatformResourceURI(ifile.getFullPath().toString(), false);
            }
        }
        return uri;
    }

    /***
     * need to notify sql explorer when import a connection.
     */
    private void notifySQLExplorerForConnection() {
        for (File file : allCopiedFiles) {
            IFile iFile = WorkspaceUtils.fileToIFile(file);
            if (iFile != null) {
                Connection conn = PrvResourceFileHelper.getInstance().findProvider(iFile);
                if (conn != null && conn instanceof DatabaseConnection) {
                    DatabaseConnection dbConn = (DatabaseConnection) conn;
                    if (JavaSqlFactory.getUsername(dbConn) != null) {
                        CWMPlugin.getDefault().addConnetionAliasToSQLPlugin(dbConn);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.imex.model.IImportWriter#postFinish()
     */
    public void postFinish() throws IOException {
        // reload all import items
        for (IPath path : this.allImportItems) {
            IFile desIFile = ResourceService.file2IFile(path.toFile());
            if (desIFile != null && desIFile.getFileExtension().equals(FactoriesUtil.PROPERTIES_EXTENSION)) {
                Property property = PropertyHelper.getProperty(desIFile, true);
                if (property != null) {
                    try {
                        ProxyRepositoryFactory.getInstance().reload(property, desIFile);
                    } catch (PersistenceException e) {
                        log.error(e);
                    }
                }
            }
        }
        ItemRecord.clear();
        // delete the temp folder
        deleteTempProjectFolder();
    }

}
