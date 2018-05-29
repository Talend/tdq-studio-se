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
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.ContextViewHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
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
import org.talend.dq.helper.resourcehelper.ContextResourceFileHelper;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;

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

    private Set<File> dependencySet = new HashSet<File>();

    private List<String> errors = new ArrayList<String>();

    private ItemRecord parent;

    private ItemRecord[] childern;

    private EElementEName elementEName;

    // when we do import action this folder is the Path of temp folder. when we do export action this folder is Empty so
    // we get rootFolder of current project
    private IPath rootFolder = Path.EMPTY;

    public ItemRecord(File file) {
        this(file, ResourceManager.getRootProject().getLocation());
    }

    /**
     * the resourceSet attribute is static so that notice call {@link #clear()} method when next time
     * 
     * @param file the file which we want to import or export
     * @param rootFolder the location which file is come from
     */
    public ItemRecord(File file, IPath rootFolder) {
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
            Collection<TdReport> allReports =
                    (Collection<TdReport>) RepResourceFileHelper.getInstance().getAllElement();
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
        List<IFile> contextList = ContextResourceFileHelper.getInstance().getAllContexts();
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
        this.errors.add(err);
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
    public List<String> getErrors() {
        return this.errors;
    }

    /**
     * DOC bZhou Comment method "isValid".
     * 
     * @return
     */
    public boolean isValid() {
        return errors.isEmpty();
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
            childern = recordList.toArray(new ItemRecord[recordList.size()]);
        }
        ComparatorsFactory.sort(childern, ComparatorsFactory.ITEM_RECORD_COMPARATOR_ID);
        return this.childern;
    }

    /**
     * DOC bZhou Comment method "getName".
     * 
     * @return
     */
    public String getName() {
        if (property != null) {
            // only internationalization name of SystemIndicator
            ModelElement element = PropertyHelper.getModelElement(property);
            if (element != null && DefinitionPackage.eINSTANCE.getIndicatorDefinition().equals(element.eClass())) {
                return InternationalizationUtil.getDefinitionInternationalizationLabel(property.getLabel());
            }
            return property.getDisplayName();
        } else {
            return file == null ? StringUtils.EMPTY : file.getName();
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
                || (fileName != null && (fileName.equals(".Talend.definition") || fileName.equals(".Talend.properties")))) {//$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }

        return FactoriesUtil.JAR.equals(path.getFileExtension()) || propPath.toFile().exists()
                && !propPath.equals(path);
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

}
