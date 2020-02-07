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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.emf.FactoriesUtil.EElementEName;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.ContextViewHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataprofiler.core.ui.utils.DqFileUtils;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.helpers.ReportHelper;
import org.talend.dataquality.helpers.ReportHelper.ReportType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.RecordMatchingIndicator;
import org.talend.dataquality.indicators.definition.DefinitionPackage;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.utils.CustomAttributeMatcherClassNameConvert;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.dq.helper.CustomAttributeMatcherHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.resourcehelper.ContextResourceFileHelper;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.repository.ProjectManager;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwmx.analysis.informationreporting.Report;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ItemRecord {

    private static Logger log = Logger.getLogger(ItemRecord.class);

    private static ResourceSet resourceSet;

    private static List<ItemRecord> allItemRecords;

    private static Map<File, ModelElement> FILE_ELEMENT_MAP;

    public static ModelElement getElement(File file) {
        return FILE_ELEMENT_MAP.get(file);
    }

    private File file;

    private Property property;

    private URI itemURI;

    private URI propURI;

    private IRepositoryViewObject conflictObject;

    private List<Dependency> clientDepenList;

    private List<Dependency> supplierDepenList;

    private List<IFile> clientDepenFileList;

    private List<IFile> supplierDepenFileList;

    // two type of conflict name and uuid
    private EConflictType eConflictType;

    private Set<File> dependencySet = new HashSet<File>();

    private List<ImportMessage> errors = new ArrayList<ImportMessage>();

    private ItemRecord parent;

    private ItemRecord[] childern;

    private EElementEName elementEName;

    // when we do import action this folder is the Path of temp folder. when we do export action this folder is Empty so
    // we get rootFolder of current project
    private IPath rootFolder = Path.EMPTY;

    private Project project = null;

    public ItemRecord(File file) {
        this(file, ResourceManager.getRootProject().getLocation());
    }

    public ItemRecord(File file, IPath rootFolder) {
        this(file, rootFolder, null);
    }

    /**
     * the resourceSet attribute is static so that notice call {@link #clear()} method when next time
     *
     * @param file the file which we want to import or export
     * @param rootFolder the location which file is come from
     * @param proj the current file is from which project(main or reference project)
     */
    public ItemRecord(File file, IPath rootFolder, Project proj) {
        this.file = file;
        this.rootFolder = rootFolder;

        if (resourceSet == null) {
            // the resourceSet attribute is static so that notice call {@link #clear()} method when next time
            resourceSet = new ResourceSetImpl();
        }

        if (allItemRecords == null) {
            allItemRecords = new ArrayList<ItemRecord>();
        }

        if (FILE_ELEMENT_MAP == null) {
            FILE_ELEMENT_MAP = new HashMap<File, ModelElement>();
        }
        if (proj != null) {
            project = proj;
        } else if (rootFolder != null) {
            String lastSegment = rootFolder.lastSegment();
            project = ProjectManager.getInstance().getProjectFromProjectTechLabel(lastSegment);
        }
        if (project == null) {
            project = ProjectManager.getInstance().getCurrentProject();
        }
        try {
            initialize();
        } catch (Exception e) {
            String errorMessage =
                    DefaultMessagesImpl.getString("ItemRecord.cantInitializeElement", getName(), e.getMessage()); //$NON-NLS-1$
            addError(errorMessage);
            log.error(errorMessage);
        }
    }

    /**
     * DOC bZhou Comment method "initialize".
     */
    private void initialize() {
        if (file != null && file.isFile()) {
            if (!isJarFile()) {
                ModelElement element = null;

                itemURI = URI.createFileURI(file.getAbsolutePath());
                propURI = itemURI.trimFileExtension().appendFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);
                elementEName = EElementEName.findENameByExt(itemURI.fileExtension());

                if (!file.getName().endsWith(PluginConstant.JASPER_STRING)) {
                    Resource resource = resourceSet.getResource(propURI, true);
                    property =
                            (Property) EcoreUtil.getObjectByType(resource.getContents(),
                                    PropertiesPackage.eINSTANCE.getProperty());

                    if (property != null) {
                        element = PropertyHelper.getModelElement(property);
                    }

                }

                computeDependencies(element);
            }

            allItemRecords.add(this);
        }
    }

    /**
     * DOC bZhou Comment method "getElement".
     *
     * @return
     */
    public ModelElement getElement() {
        return property == null ? null : PropertyHelper.getModelElement(property);
    }

    /**
     * DOC bZhou Comment method "getFilePath".
     *
     * @return
     */
    public IPath getFilePath() {
        return new Path(file.getAbsolutePath());
    }

    /**
     *
     * when we do import action this folder is the Path of temp folder. when we do export action this folder is Empty so
     * we get rootFolder of current project
     *
     * @return
     */
    public IPath getRootFolderPath() {
        return rootFolder;
    }

    /**
     * DOC bZhou Comment method "getPropertyPath".
     *
     * @return
     */
    public IPath getPropertyPath() {
        if (file != null) {
            IPath itemResPath = new Path(file.getAbsolutePath());
            return itemResPath.removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);
        }
        return null;
    }

    /**
     * DOC bZhou Comment method "getFullPath".
     *
     * @return
     */
    public IPath getFullPath() {
        IPath path = new Path(file.getAbsolutePath());
        path = path.makeRelativeTo(ResourcesPlugin.getWorkspace().getRoot().getLocation());
        return path;
    }

    public Set<File> getDependencySet() {
        return this.dependencySet;
    }

    /**
     * DOC bZhou Comment method "computeDependencies".
     */
    private void computeDependencies(ModelElement mElement) {
        if (isJRXml()) {
            Collection<TdReport> allReports = RepResourceFileHelper.getInstance().getAllElement();
            for (TdReport report : allReports) {
                // MOD yyi 2012-02-20 TDQ-4545 TDQ-4701: Change to relative path comparing.
                IPath pathRepFile = RepResourceFileHelper.findCorrespondingFile(report).getLocation();
                IPath pathJrxmlFile = new Path(file.getPath());
                String path = pathJrxmlFile.makeRelativeTo(pathRepFile).toString();

                for (AnalysisMap anaMap : report.getAnalysisMap()) {
                    if (StringUtils.equals(path, anaMap.getJrxmlSource())) {
                        // TODO the File is jrxml, but the ModelElement is report ???
                        this.dependencySet.add(file);
                    }
                }
            }
        } else if (mElement != null) {
            if (mElement instanceof Connection) {
                includeContextDependency((Connection) mElement);
                return;
            }
            List<File> dependencyFile = getClintDependencyForExport(mElement);
            for (File df : dependencyFile) {
                ModelElement modelElement = getElement(df);
                if (modelElement != null) {
                    File depFile = EObjectHelper.modelElement2File(modelElement);
                    if (depFile != null) {
                        this.dependencySet.add(depFile);
                    }
                    // MOD sizhaoliu 2013-04-13 TDQ-7082
                    if (modelElement instanceof IndicatorDefinition) {
                        if (modelElement instanceof UDIndicatorDefinition) {
                            includeJUDIDependencies((IndicatorDefinition) modelElement);
                        } else {
                            for (IndicatorDefinition definition : ((IndicatorDefinition) modelElement)
                                    .getAggregatedDefinitions()) {
                                includeAggregatedDependencies(definition);
                            }
                        }
                    }
                }
            }
            // MOD yyi 2012-02-20 TDQ-4545 TDQ-4701: Map user define jrxm templates with report.
            if (mElement instanceof TdReport) {
                TdReport rep = (TdReport) mElement;
                for (AnalysisMap anaMap : rep.getAnalysisMap()) {
                    ReportType reportType =
                            ReportHelper.ReportType.getReportType(anaMap.getAnalysis(), anaMap.getReportType());
                    boolean isUserMade = ReportHelper.ReportType.USER_MADE.equals(reportType);
                    if (isUserMade) {
                        traverseFolderAndAddJrxmlDependencies(getJrxmlFolderFromReport(rep,
                                ResourceManager.getJRXMLFolder()));
                    }
                }
                this.dependencySet.addAll(includeImportedContext(mElement));
            } else if (mElement instanceof IndicatorDefinition) { // MOD sizhaoliu 2013-04-13 TDQ-7082
                IndicatorDefinition definition = (IndicatorDefinition) mElement;
                if (definition instanceof UDIndicatorDefinition) {
                    includeJUDIDependencies(definition);
                } else {
                    for (IndicatorDefinition defInd : definition.getAggregatedDefinitions()) {
                        includeAggregatedDependencies(defInd);
                    }
                }
                // MatchRule and match Analysis come from different location so that we must recompute the path of jar
                // folder
                if (mElement instanceof MatchRuleDefinition) {
                    includeCustomMatcherJarDependencies((MatchRuleDefinition) mElement);
                }
            } else if (mElement instanceof Analysis) {
                this.dependencySet.addAll(includeImportedContext(mElement));

                if (AnalysisType.MATCH_ANALYSIS == AnalysisHelper.getAnalysisType((Analysis) mElement)) {
                    includeCustomMatcherJarDependencies((Analysis) mElement);
                }
            }
        }
    }

    /**
     * include dependency context for Connection
     *
     * @param connection
     */
    private void includeContextDependency(Connection connection) {
        String contextId = connection.getContextId();
        if (connection.isContextMode() && !StringUtils.isEmpty(contextId)) {
            for (ItemRecord record : allItemRecords) {
                Property property2 = record.getProperty();
                if (property2 != null) {
                    Item item = property2.getItem();
                    if (item instanceof ContextItem && contextId.equals(property2.getId())) {
                        dependencySet.add(record.getFile());
                    }
                }
            }
        }
    }

    /**
     * include imported contexts for analysis/report
     */
    private List<File> includeImportedContext(ModelElement mElement) {
        List<File> result = new ArrayList<File>();
        if (mElement instanceof Analysis) {
            result.addAll(getAllImportedContext(((Analysis) mElement).getContextType()));
        } else if (mElement instanceof TdReport) {
            result.addAll(getAllImportedContext(((TdReport) mElement).getContext()));
        }
        return result;
    }

    private List<File> getAllImportedContext(EList<ContextType> contextTypes) {
        List<File> result = new ArrayList<File>();
        if (contextTypes.isEmpty()) {
            return result;
        }
        List<String> importedContextName = ContextViewHelper.getImportedListContextNames(contextTypes);
        if (importedContextName.isEmpty()) {
            return result;
        }
        // get contexts from the current project
        IProject iProject = ReponsitoryContextBridge.findProject(project.getTechnicalLabel());
        List<IFile> contextList = ContextResourceFileHelper.getInstance().getAllContextFromProject(iProject);
        for (IFile contextFile : contextList) {
            String name = contextFile.getName().substring(0, contextFile.getName().lastIndexOf("_"));
            if (importedContextName.contains(name)) {
                // FILE_ELEMENT_MAP.put(depFile, element);
                result.add(WorkspaceUtils.ifileToFile(contextFile));
            }
        }

        return result;
    }

    /**
     * @param mElement
     * @return SupplierDependency
     *
     * getClintDependency here will contain system indicators so only will be used by export case
     */
    public List<File> getClintDependencyForExport(ModelElement mElement) {
        List<File> result = new ArrayList<File>();
        if (mElement != null) {
            result = iterateClientDependencies(mElement);
            // current object is analysis case
            if (mElement instanceof Analysis) {
                result.addAll(getSystemIndicaotrOfAnalysis(mElement));
            } else {
                // if object is report, then the analyses inside reports should be considered. The system indicators of
                // analyses should be added into the result list too.
                List<File> tempList = new ArrayList<File>();
                tempList.addAll(result);
                for (File tempFile : tempList) {
                    ModelElement me = getElement(tempFile);
                    if (me != null && me instanceof Analysis) {
                        result.addAll(getSystemIndicaotrOfAnalysis(me));
                    }
                }
            }
        }
        return result;
    }

    /**
     * get Analysis Dependency (for indicator only).
     *
     * @return get the list of indicator which in use by the analysis
     *
     */
    private List<File> getSystemIndicaotrOfAnalysis(ModelElement mElement) {
        List<File> listFile = new ArrayList<File>();
        if (mElement instanceof Analysis) {
            Analysis anaElement = (Analysis) mElement;
            List<Indicator> indicators = IndicatorHelper.getIndicators(anaElement.getResults());
            for (Indicator indicator : indicators) {
                if (indicator instanceof UserDefIndicator) {
                    // whereRuleIndicator or UDIIndicator
                    continue;
                }
                boolean isContain = false;
                IndicatorDefinition newIndicatorDefinition = indicator.getIndicatorDefinition();
                // MOD qiongli 2012-5-11 TDQ-5256
                if (newIndicatorDefinition == null) {
                    continue;
                }
                for (File lf : listFile) {
                    ModelElement me = getElement(lf);
                    if (me != null && me instanceof IndicatorDefinition) {
                        IndicatorDefinition oldIndicatorDefinition = (IndicatorDefinition) me;
                        if (ModelElementHelper.compareUUID(oldIndicatorDefinition, newIndicatorDefinition)) {
                            isContain = true;
                            break;
                        }
                    }
                }
                if (!isContain) {
                    File depFile = EObjectHelper.modelElement2File(newIndicatorDefinition);
                    if (depFile != null) {
                        FILE_ELEMENT_MAP.put(depFile, newIndicatorDefinition);
                        listFile.add(depFile);
                    }
                }
            }
        }
        return listFile;
    }

    public List<File> getClintDependency(ModelElement mElement) {
        List<File> listFile = new ArrayList<File>();
        if (mElement == null) {
            return listFile;
        }
        EList<Dependency> clientDependency = mElement.getClientDependency();
        for (Dependency clienter : clientDependency) {
            for (ModelElement depencyModelElement : clienter.getSupplier()) {
                File depFile = EObjectHelper.modelElement2File(depencyModelElement);
                if (depFile != null) {
                    FILE_ELEMENT_MAP.put(depFile, depencyModelElement);
                    listFile.add(depFile);
                }
            }
        }
        return listFile;
    }

    private List<File> iterateClientDependencies(ModelElement mElement) {
        List<File> returnList = new ArrayList<File>();
        for (File depFile : getClintDependency(mElement)) {
            ModelElement me = getElement(depFile);
            if (me != null) {
                if (me instanceof Connection) {
                    includeContextDependency((Connection) me);
                } else {
                    returnList.addAll(iterateClientDependencies(me));
                }
            }
            returnList.add(depFile);
        }
        return returnList;
    }

    /**
     * DOC zshen Comment method "includeCustomMatcherJarDependencies".
     *
     * @param matchRuleDef
     */
    private void includeCustomMatcherJarDependencies(MatchRuleDefinition matchRuleDef) {
        for (MatchRule matchRule : matchRuleDef.getMatchRules()) {
            for (MatchKeyDefinition matchKeyDefinition : matchRule.getMatchKeys()) {
                if (AttributeMatcherType.CUSTOM.getComponentValue().equalsIgnoreCase(
                        matchKeyDefinition.getAlgorithm().getAlgorithmType())) {
                    File libFolder = getUDILibFolderFile();
                    if (libFolder.exists()) {
                        for (File udiJarFile : UDIUtils.getLibJarFileList(libFolder)) {
                            for (String str : CustomAttributeMatcherHelper.splitJarPath(matchKeyDefinition
                                    .getAlgorithm()
                                    .getAlgorithmParameters())) {
                                if (udiJarFile.getName().equals(str)) {
                                    this.dependencySet.add(udiJarFile);
                                }
                            }
                        }
                    } else {
                        log.error(libFolder + " does not exist. Dependent match rule is " + matchRuleDef.getLabel()); //$NON-NLS-1$
                    }
                }
            }
        }
    }

    /**
     * Both export and import use this method to find used jar file in UDI's lib, so the lib folder path is different
     * between export and import, should based on: getRootFolderPath(), and then appent the lib path directly.
     *
     * @return the file of the UDI lib folder
     */
    private File getUDILibFolderFile() {
        IPath libFolderPath = getRootFolderPath().append(EResourceConstant.USER_DEFINED_INDICATORS_LIB.getPath());
        File libFolder = libFolderPath.toFile();
        return libFolder;
    }

    /**
     * DOC zshen Comment method "includeCustomMatcherJarDependencies".
     *
     * @param matchAnalysis
     */
    private void includeCustomMatcherJarDependencies(Analysis matchAnalysis) {
        RecordMatchingIndicator recordMatchIndicatorFromAna =
                AnalysisHelper.getRecordMatchIndicatorFromAna(matchAnalysis);
        MatchRuleDefinition builtInMatchRuleDefinition = recordMatchIndicatorFromAna.getBuiltInMatchRuleDefinition();
        includeCustomMatcherJarDependencies(builtInMatchRuleDefinition);
    }

    /**
     * get the jrxml folder according to the Report file(if the Report file is out of current workspace, the Jrxml
     * Folder should also out of it).
     *
     * @param rep the Report file
     * @param folder the Jrxml Folder in the current project
     * @return
     */
    private File getJrxmlFolderFromReport(TdReport rep, IFolder folder) {
        File jrxmlFolderFile = null;
        String repFileString = new File(rep.eResource().getURI().toFileString()).getAbsolutePath();
        String projectString = folder.getProject().getLocation().toFile().getAbsolutePath();
        if (repFileString.startsWith(projectString)) {
            jrxmlFolderFile = folder.getLocation().toFile();
        } else {
            String jrxmlFolderString = folder.getLocation().toFile().getAbsolutePath();
            jrxmlFolderFile =
                    new File(StringUtils.replace(
                            jrxmlFolderString,
                            projectString,
                            repFileString.substring(0,
                                    repFileString.indexOf(EResourceConstant.DATA_PROFILING.getPath()) - 1), 1));
        }
        return jrxmlFolderFile;
    }

    private void includeJUDIDependencies(IndicatorDefinition definition) {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.JAR_FILE_PATH, definition.getTaggedValue());
        if (tv != null) {
            // MOD TDQ-8926 yyin 20140513,Because there maybe some sub folders, so the lib folder path can not be
            // computed from the path of the UDI(which caused TDQ-8926)
            File libFolder = getUDILibFolderFile();

            if (libFolder.exists()) {
                List<File> libJarFileList = UDIUtils.getLibJarFileList(libFolder);
                String[] splitTagValues = tv.getValue().split(CustomAttributeMatcherClassNameConvert.REGEXKEY);
                for (File udiJarFile : libJarFileList) {
                    for (String str : splitTagValues) {
                        if (udiJarFile.getName().equals(str)) {
                            this.dependencySet.add(udiJarFile);
                        }
                    }
                }
            }
        }
    }

    private void traverseFolderAndAddJrxmlDependencies(File folderFile) {
        for (File subFile : folderFile.listFiles()) {
            if (subFile.isDirectory()) {
                traverseFolderAndAddJrxmlDependencies(subFile);
            } else if (subFile.isFile()) {
                String name = subFile.getName();
                int dotIndex = name.lastIndexOf("."); //$NON-NLS-1$
                if (dotIndex > 0) {
                    String ext = name.substring(dotIndex + 1);
                    if (FactoriesUtil.JRXML.equals(ext)) {
                        this.dependencySet.add(subFile);
                    }
                }
            }
        }
    }

    private void includeAggregatedDependencies(IndicatorDefinition definition) {
        File depFile = EObjectHelper.modelElement2File(definition);
        if (depFile != null) {
            FILE_ELEMENT_MAP.put(depFile, definition);
            this.dependencySet.add(depFile);
        }

        for (IndicatorDefinition aggregatedDefinition : definition.getAggregatedDefinitions()) {
            includeAggregatedDependencies(aggregatedDefinition);
        }
    }

    /**
     * clear the resource set.
     */
    public static void clear() {
        if (resourceSet != null) {
            for (Resource resource : resourceSet.getResources()) {
                resource.unload();
            }
            resourceSet.getResources().clear();
            resourceSet = null;
        }

        if (allItemRecords != null) {
            allItemRecords.clear();
            allItemRecords = null;
        }

        if (FILE_ELEMENT_MAP != null) {
            FILE_ELEMENT_MAP.clear();
            FILE_ELEMENT_MAP = null;
        }
    }

    /**
     * DOC bZhou Comment method "addError".
     *
     * @param error
     */
    public void addError(String error) {
        String err = (elementEName != null) ? "[" + elementEName.name() + "]" + error : error; //$NON-NLS-1$ //$NON-NLS-2$
        this.errors.add(new ImportMessage(err, EMessageType.ERROR));
    }

    /**
     * Add dependency error
     *
     * @param error the content of dependency error message
     */
    public void addDependencyError(String error) {
        String err = (elementEName != null) ? "[" + elementEName.name() + "]" + error : error; //$NON-NLS-1$ //$NON-NLS-2$
        this.errors.add(new ImportMessage(err, EMessageType.DEPENDENCYERROR));
    }

    /**
     * zshen Comment method "addWarn".
     *
     * @param warn
     */
    public void addWarn(String warnItem) {
        String warn = (elementEName != null) ? "[" + elementEName.name() + "]" + warnItem : warnItem; //$NON-NLS-1$ //$NON-NLS-2$
        this.errors.add(new ImportMessage(warn, EMessageType.WARN));
    }

    /**
     * Getter for file.
     *
     * @return the file
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Getter for errors.
     *
     * @return the errors
     */
    public List<ImportMessage> getErrors() {
        return this.errors;
    }

    /**
     *
     */
    public List<String> getErrorMessage() {
        List<String> errorMessage = new ArrayList<String>();
        for (ImportMessage message : this.getErrors()) {
            if (EMessageType.ERROR == message.getType() || EMessageType.DEPENDENCYERROR == message.getType()) {
                errorMessage.add(message.toString());
            }
        }
        return errorMessage;
    }

    /**
     *
     */
    public List<String> getErrorMessage(boolean isOrverWrite) {
        List<String> errorMessage = new ArrayList<String>();
        for (ImportMessage message : this.getErrors()) {
            if (isOrverWrite && EMessageType.ERROR == message.getType()) {
                errorMessage.add(message.toString());
            } else if (!isOrverWrite && EMessageType.DEPENDENCYERROR == message.getType()) {
                errorMessage.add(message.toString());
            }
        }
        return errorMessage;
    }

    /**
     * DOC bZhou Comment method "isValid".
     *
     * @return
     */
    public boolean isValid() {
        for (ImportMessage importMessage : errors) {
            if (EMessageType.WARN == importMessage.getType()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Judge whether there are some errors or warns need to show
     *
     * @return
     */
    public boolean existMessageToShow() {
        return !errors.isEmpty();
    }

    /**
     * Judge whether there are only some warns need to show
     */
    public boolean onlyWarnToShow() {
        return existMessageToShow() && isValid();
    }

    /**
     * Getter for property.
     *
     * @return the property
     */
    public Property getProperty() {
        return property;
    }

    /**
     * Getter for parent.
     *
     * @return the parent
     */
    public ItemRecord getParent() {
        if (parent == null && file != null) {
            parent = new ItemRecord(file.getParentFile());
        }

        return this.parent;
    }

    /**
     * Getter for childern.
     *
     * @return the childern
     */
    public ItemRecord[] getChildern() {
        if (childern == null) {
            List<ItemRecord> recordList = new ArrayList<ItemRecord>();
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File aFile : listFiles) {
                    if (isValid(aFile)) {
                        ItemRecord itemRecord = new ItemRecord(aFile, rootFolder);
                        if (itemRecord.isValid()) {
                            recordList.add(itemRecord);
                        }
                    }
                }
            }
            List<Project> referencedProjects = ProjectManager.getInstance().getReferencedProjects(project);
            if (org.talend.core.PluginChecker.isRefProjectLoaded() && project.getEmfProject() != null
                    && referencedProjects.size() > 0) {
                addRefProjChildToExport(recordList, referencedProjects);
            }
            childern = recordList.toArray(new ItemRecord[recordList.size()]);
        }
        ComparatorsFactory.sort(childern, ComparatorsFactory.ITEM_RECORD_COMPARATOR_ID);
        return this.childern;
    }

    private void addRefProjChildToExport(List<ItemRecord> recordList, List<Project> referencedProjects) {
        boolean isMeredRefProject = ProxyRepositoryManager.getInstance().isMergeRefProject();
        if (file == null || !file.exists() || referencedProjects.size() == 0) {
            return;
        }
        // merged model for Reference project
        if (isMeredRefProject && DqFileUtils.isLocalProjectFile(file)) {
            for (Project refProj : referencedProjects) {
                findChildrenFromRefFolder(refProj, recordList, file);
                List<Project> subRefProjs = ProjectManager.getInstance().getReferencedProjects(refProj);
                if (!subRefProjs.isEmpty()) {
                    addRefProjChildToExport(recordList, subRefProjs);
                }
            }
        }
        // none-merged mode for Reference project
        if (!isMeredRefProject) {
            // Add a Virtual node "Referenced Project", both file and root folder are workspace root path
            if (checkFileIsProject()) {
                ItemRecord itemRecordRefRoot =
                        new ItemRecord(ResourceManager.getWorskpacePath().toFile(), ResourceManager.getWorskpacePath(),
                                this.project);
                recordList.add(itemRecordRefRoot);

            } else if (ResourceManager.getWorskpacePath().toFile().equals(file)
                    && rootFolder.equals(ResourceManager.getWorskpacePath())) {
                // Add children under the Reference Virtual root node
                for (Project refProj : referencedProjects) {
                    IProject iProject = ReponsitoryContextBridge.findProject(refProj.getTechnicalLabel());
                    IPath refRootPath = iProject.getLocation();
                    ItemRecord itemRecordRef = new ItemRecord(refRootPath.toFile(), refRootPath, refProj);
                    recordList.add(itemRecordRef);
                }

            }
        }
    }

    /**
     * @Description:Check the current file path if it is a project path
     * @return
     */
    private boolean checkFileIsProject() {
        if (this.file != null && file.isDirectory()) {
            IProject checkProject = ResourceManager.getProject(this.file.getName());
            if (checkProject != null && getFilePath().equals(checkProject.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC bZhou Comment method "getName".
     *
     * @return
     */
    public String getName() {
        String displayName = null;
        if (property != null) {
            // only internationalization name of SystemIndicator
            ModelElement element = PropertyHelper.getModelElement(property);
            if (element != null && DefinitionPackage.eINSTANCE.getIndicatorDefinition().equals(element.eClass())) {
                displayName = InternationalizationUtil.getDefinitionInternationalizationLabel(property.getLabel());
                if (!ResourceManager.getRootProject().getName().equalsIgnoreCase(project.getTechnicalLabel())) {
                    return displayName + "(@" + project.getLabel() + ")";
                }
                return displayName;
            }

            displayName = property.getDisplayName();
        } else {
            if (file == null) {
                return StringUtils.EMPTY;
            }
            if (ResourceManager.getWorskpacePath().toFile().equals(file)
                    && rootFolder.equals(ResourceManager.getWorskpacePath())) {
                return ERepositoryObjectType.REFERENCED_PROJECTS.getLabel();
            }
            String name = file.getName();
            if (name.equals(EResourceConstant.DATA_PROFILING.getName())
                    || name.equals(EResourceConstant.LIBRARIES.getName())
                    || name.equals(EResourceConstant.METADATA.getName())
                    || name.equals(EResourceConstant.ANALYSIS.getName())
                    || name.equals(EResourceConstant.REPORTS.getName())
                    || name.equals(EResourceConstant.CONTEXT.getName())
                    || name.equals(EResourceConstant.HADOOP_CLUSTER.getName())
                    || name.equals(EResourceConstant.EXCHANGE.getName())
                    || name.equals(EResourceConstant.RULES.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS.getName())
                    || name.equals(EResourceConstant.JRXML_TEMPLATE.getName())
                    || name.equals(EResourceConstant.USER_DEFINED_INDICATORS.getName())
                    || name.equals(EResourceConstant.PATTERNS.getName())
                    || name.equals(EResourceConstant.INDICATORS.getName())
                    || name.equals(EResourceConstant.FILEDELIMITED.getName())
                    || name.equals(EResourceConstant.DB_CONNECTIONS.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_TEXT_STATISTICS.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_CORRELATION.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_FRAUDDETECTION.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_PATTERN_MATCHING.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_ADVANCED_STATISTICS.getName())
                    || name.equals(EResourceConstant.SOURCE_FILES.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_ROW_COMPARISON.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_SIMPLE_STATISTICS.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_SUMMARY_STATISTICS.getName())
                    || name.equals(EResourceConstant.SYSTEM_INDICATORS_BUSINESS_RULES.getName())
                    || name.equals(EResourceConstant.RULES_PARSER.getName())
                    || name.equals(EResourceConstant.RULES_MATCHER.getName())) {
                return Messages.getString("RepositoryNodeHelper." + name.replace(' ', '_'));
            } else if (name.equals(EResourceConstant.SYSTEM_INDICATORS_PHONENUMBER_STATISTICS.getName())) {
                return Messages.getString(name.replace(' ', '_'));
            } else {
                displayName = name;
            }

        }
        if (!ResourceManager.getRootProject().getName().equalsIgnoreCase(project.getTechnicalLabel())) {
            return displayName + "(@" + project.getLabel() + ")";
        }
        return displayName;
    }



    /**
     * @Description:
     * @param iProject
     * @param recordList
     * @param fileOrFolder
     * @return For merge mode,find the related folders in reference project based on the current file name
     */
    private void findChildrenFromRefFolder(Project refProj, List<ItemRecord> recordList, File fileOrFolder) {
        boolean isValidFolder = fileOrFolder != null && fileOrFolder.exists() && fileOrFolder.isDirectory();
        if (!ProxyRepositoryManager.getInstance().isMergeRefProject() || !isValidFolder) {
            return;
        }
        List<IResource> resources = new ArrayList<>();
        String name = fileOrFolder.getName();
        IProject iProject = ReponsitoryContextBridge.findProject(refProj.getTechnicalLabel());
        try {
            for (EResourceConstant eResConst : EResourceConstant.getReferenceNeededConstants()) {
                IFolder refEResFolder = ResourceManager.getOneFolder(iProject, eResConst);
                boolean isPatternOrJRXML = eResConst == EResourceConstant.PATTERN_REGEX
                        || eResConst == EResourceConstant.PATTERN_SQL || eResConst == EResourceConstant.JRXML_TEMPLATE;
                if (refEResFolder.exists() && fileOrFolder.getPath().contains(eResConst.getPath().replace("/", "\\"))) {
                    // 1.sub-folder in PatternRegex(like 'address'),PatternSQL,JRMX
                    // 2.user-defined resources in some reference folders like folder "abc" in "Regex"
                    if (isPatternOrJRXML) {
                        if (refEResFolder.getFolder(name).exists()) {// 1
                            for (IResource res : refEResFolder.getFolder(name).members()) {
                                resources.add(res);
                            }
                            break;
                        } else if (eResConst.getName().equals(name)) {// 2
                            for (IResource res : refEResFolder.members()) {
                                // if not found in Main project,take it as user-defined resource in RefProject
                                IResource findMemberFromRootProj =
                                        ResourceManager.getOneFolder(eResConst).findMember(res.getName());
                                if (findMemberFromRootProj == null || !findMemberFromRootProj.exists()) {
                                    resources.add(res);
                                }
                            }
                            break;
                        }

                    } else if (eResConst.getName().equals(name)) {// Analyses,Report,Indicators...
                        for (IResource res : refEResFolder.members()) {
                            resources.add(res);
                        }
                        break;
                    }
                }
            }
        } catch (CoreException e) {
            log.error(e.getMessage());
        }
        for (IResource res : resources) {
            if (res != null && res.exists()) {
                File refFile = res.getLocation().toFile();
                if (isValid(refFile)) {
                    ItemRecord itemRecord = new ItemRecord(refFile, iProject.getLocation());
                    if (itemRecord.isValid()) {
                        recordList.add(itemRecord);
                    }
                }
            }
        }
    }

    /**
     * DOC bZhou Comment method "isValid".
     *
     * @param f
     * @return
     */
    private boolean isValid(File f) {
        if (f.isDirectory()) {
            return isValidDirectory(f);
        }

        return isValidFile(f);
    }

    /**
     * DOC bZhou Comment method "isValidFile".
     *
     * @param f
     * @return
     */
    private boolean isValidFile(File f) {
        IPath path = new Path(f.getAbsolutePath());
        IPath propPath = path.removeFileExtension().addFileExtension(FactoriesUtil.PROPERTIES_EXTENSION);

        String fileName = f.getName();
        // MOD qiongli 2012-5-14 TDQ-5259.".Talend.properties" exists on 401,need to filter it and ".Talend.definition".
        if ("jasper".equals(path.getFileExtension()) //$NON-NLS-1$
                || (fileName != null
                        && (fileName.equals(".Talend.definition") || fileName.equals(".Talend.properties")))) {//$NON-NLS-1$ //$NON-NLS-2$
            return false;
        } else if (path.toString().contains(EResourceConstant.SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS.getPath())
                && path.lastSegment().endsWith(".definition")) {
            // this part hidden the CI/CS_Word_Pattern_(Low_)Frequency
            return !(fileName != null && fileName.startsWith("C"));
        }

        return FactoriesUtil.JAR.equals(path.getFileExtension())
                || propPath.toFile().exists() && !propPath.equals(path);
    }

    /**
     * DOC bZhou Comment method "isTOPFile".
     *
     * @param f
     * @return
     */
    private boolean isValidDirectory(File f) {
        // filter the bin folder
        if (!f.getName().startsWith(".") && !f.getName().equals("bin")) { //$NON-NLS-1$ //$NON-NLS-2$
            IPath filePath = new Path(f.getAbsolutePath());
            String pathStr = filePath.toPortableString();

            for (EResourceConstant constant : EResourceConstant.getTdqConstants()) {
                if (filePath.toString().indexOf(constant.getPath()) > 0) {
                    String lastSeg = filePath.lastSegment();
                    if (constant == EResourceConstant.METADATA) {
                        return lastSeg.equals(constant.getName())
                                || pathStr.contains(EResourceConstant.DB_CONNECTIONS.getPath())
                                || pathStr.contains(EResourceConstant.FILEDELIMITED.getPath());
                    } else if (constant == EResourceConstant.LIBRARIES) {
                        // hide the System Indicators/Overview
                        return !pathStr.contains(EResourceConstant.SYSTEM_INDICATORS_OVERVIEW.getPath());
                    }

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * DOC bZhou Comment method "isJRXml".
     *
     * @return
     */
    private boolean isJRXml() {
        return file.getName().endsWith(FactoriesUtil.JRXML);
    }

    private boolean isJarFile() {
        return file.getName().endsWith(FactoriesUtil.JAR);
    }

    /**
     * DOC bZhou Comment method "findRecord".
     *
     * @param file
     * @return
     */
    public static ItemRecord findRecord(File file) {
        for (ItemRecord record : allItemRecords) {
            if (file.getAbsolutePath().equals(record.getFile().getAbsolutePath())) {
                return record;
            }
        }

        return null;
    }

    /**
     * Find checked record
     *
     * @param filereturn
     */
    public static ItemRecord findCheckedRecord(ItemRecord[] checkedRecords, File findFile) {
        ItemRecord findRecord = ItemRecord.findRecord(findFile);
        if (findRecord == null) {
            return null;
        }
        for (ItemRecord record : checkedRecords) {
            if (findRecord.getFile().getAbsolutePath().equals(record.getFile().getAbsolutePath())) {
                return record;
            }
        }
        return null;
    }

    /**
     * Getter for allItemRecords.
     *
     * @return the allItemRecords
     */
    public static List<ItemRecord> getAllItemRecords() {
        return allItemRecords;
    }

    /**
     * Getter for conflictObject.
     *
     * @return the conflictObject
     */
    public IRepositoryViewObject getConflictObject() {
        return this.conflictObject;
    }

    /**
     * Sets the conflictObject.
     *
     * @param conflictObject the conflictObject to set
     */
    public void setConflictObject(IRepositoryViewObject conflictObject) {
        this.conflictObject = conflictObject;
    }

    /**
     * Getter for eConflictType.
     *
     * @return the eConflictType
     */
    protected EConflictType geteConflictType() {
        return this.eConflictType;
    }

    /**
     * Sets the eConflictType.
     *
     * @param eConflictType the eConflictType to set
     */
    protected void seteConflictType(EConflictType eConflictType) {
        this.eConflictType = eConflictType;
    }

    /**
     * Getter for clientDepenList.
     *
     * @return the clientDepenList
     */
    protected List<Dependency> getClientDepenList() {
        if (clientDepenList == null) {
            clientDepenList = new ArrayList<Dependency>();
        }
        return this.clientDepenList;
    }

    /**
     * Getter for supplierDepenList.
     *
     * @return the supplierDepenList
     */
    protected List<Dependency> getSupplierDepenList() {
        if (supplierDepenList == null) {
            supplierDepenList = new ArrayList<Dependency>();
        }
        return this.supplierDepenList;
    }

    /**
     * Getter for clientDepenFileList.
     *
     * @return the clientDepenFileList
     */
    protected List<IFile> getClientDepenFileList() {
        if (clientDepenFileList == null) {
            clientDepenFileList = new ArrayList<IFile>();
        }
        return this.clientDepenFileList;
    }

    /**
     * Getter for supplierDepenFileList.
     *
     * @return the supplierDepenFileList
     */
    protected List<IFile> getSupplierDepenFileList() {
        if (supplierDepenFileList == null) {
            supplierDepenFileList = new ArrayList<IFile>();
        }
        return this.supplierDepenFileList;
    }

    /**
     * Judge whether current record is emf element
     */
    public boolean isEMFValid() {
        return this.getElement() != null;
    }

    public boolean needMergeDependency() {
        return this.getSupplierDepenFileList().size() > 0 || this.getClientDepenFileList().size() > 0;
    }

    /**
     * Judge whether there is a name confilct exist
     */
    public boolean isInvalidNAMEConflictExist() {
        if (EConflictType.NAME == this.eConflictType) {
            return true;
        } else if (EConflictType.UUIDBUTNAME == this.eConflictType && !(this.getElement() instanceof Connection
                || this.getElement() instanceof Analysis || this.getElement() instanceof Report)) {
            // analysis connection and report do that first then we will do others
            return true;
        }
        return false;
    }

    /**
     * Judge whether it is the case which need to rename first
     */
    public boolean isNeedToRenameFirst() {
        if (EConflictType.UUIDBUTNAME == this.eConflictType && (this.getElement() instanceof Connection
                || this.getElement() instanceof Analysis || this.getElement() instanceof Report)) {
            // analysis connection and report do that first then we will do others
            return true;
        }
        return false;
    }

}
