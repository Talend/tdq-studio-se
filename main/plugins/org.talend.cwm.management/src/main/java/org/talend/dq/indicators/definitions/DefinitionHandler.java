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
package org.talend.dq.indicators.definitions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.emf.EmfFileResourceUtil;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.management.i18n.InternationalizationUtil;
import org.talend.cwm.management.i18n.Messages;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.DefinitionPackage;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorsDefinitions;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.util.DefinitionSwitch;
import org.talend.dataquality.indicators.util.IndicatorsSwitch;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.model.emf.CwmResource;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author scorreia
 * 
 * This class contains the singleton instance for the default indicator' definitions.
 */
public final class DefinitionHandler {

    /**
     * The label of the Regular Expression Matching indicator definition.
     */
    public static final String REGULAR_EXPRESSION_MATCHING = "Regular Expression Matching"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(DefinitionHandler.class);

    private static DefinitionHandler instance;

    private static final String DQ_RULE_CATEGORY = "_8i9eQBI5Ed6TWL6NwMMHzQ"; //$NON-NLS-1$

    private static final String USER_DEFINED_COUNT_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_COUNT_CATEGORY;

    private static final String USER_DEFINED_FREQUENCY_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_FREQUENCY_CATEGORY;

    private static final String USER_DEFINED_MATCH_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_MATCH_CATEGORY;

    private static final String USER_DEFINED_REAL_VALUE_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_REAL_VALUE_CATEGORY;

    private static final String USER_DEFINED_COMPARISON_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_COMPARISON_CATEGORY;

    private static final String USER_DEFINED_NOMINAL_CORRELATION_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_NOMINAL_CORRELATION_CATEGORY;

    private static final String USER_DEFINED_INTERVAL_CORRELATION_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_INTERVAL_CORRELATION_CATEGORY;

    private static final String USER_DEFINED_TIME_CORRELATION_CATEGORY = IndicatorCategoryHelper.USER_DEFINED_TIME_CORRELATION_CATEGORY;

    private static final String DQ_RULE_DEFINITION = "_UUIyoCOMEd6YB57jaCfKaA"; //$NON-NLS-1$

    private static final String FD_RULE_DEFINITION = "_YqcX0XHpEd6udst2R2sgpA"; //$NON-NLS-1$

    private static Map<String, IndicatorCategory> userDefinedIndicatorCategoryMap;

    private List<IndicatorDefinition> indicatorDefinitions = new ArrayList<IndicatorDefinition>();

    private Map<String, String> definitionToIdMap = new HashMap<String, String>();

    private EList<IndicatorCategory> indicatorCategories;

    private static String PROJECT_PATH = System.getProperty("talend.project.path"); //$NON-NLS-1$

    /**
     * plugin relative path to the default file.
     */
    public static final String FILENAME = ".Talend." + FactoriesUtil.DEFINITION; //$NON-NLS-1$

    private static final String PLUGIN_PATH = "/org.talend.dataquality/" + FILENAME; //$NON-NLS-1$

    public static DefinitionHandler getInstance() {
        if (instance == null) {
            instance = new DefinitionHandler();
            // try to copy into workspace
            if (Platform.isRunning() && !getTalendDefinitionFile().exists()) {
                instance.copyDefinitionsIntoFolder(ResourceManager.getLibrariesFolder());
            }
            // MOD mzhao feature 13676.
            instance.initializeDefinitions();
        }
        return instance;
    }

    private DefinitionHandler() {

    }

    private void initializeDefinitions() {
        this.indicatorDefinitions.clear();
        this.indicatorCategories = loadDefinitionsFromFile().getCategories();
        if (Platform.isRunning()) {
            indicatorDefinitions.addAll((List<IndicatorDefinition>) IndicatorResourceFileHelper.getInstance().getAllElement());
        } else {// reporting engine is running as library
            indicatorDefinitions.addAll((List<IndicatorDefinition>) getAllIndicatorElement(getTdqLibPath() + "Indicators")); //$NON-NLS-1$
        }
    }

    private List<? extends ModelElement> getAllIndicatorElement(String parentFolder) {
        List<ModelElement> elementList = new ArrayList<ModelElement>();
        try {
            List<File> allFiles = searchAllFiles(new File(parentFolder));
            // MOD qiongli 2011-4-19.bug 20566,avoid NPE
            ModelElement mod = null;
            for (File file : allFiles) {
                mod = getIndicatorModelElement(file);
                if (mod != null) {
                    // MOD msjian TDQ-4672 2012-2-17: modify another issue
                    elementList.add(mod);
                    // TDQ-4672~
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        return elementList;
    }

    private final ModelElement getIndicatorModelElement(File file) {
        DefinitionSwitch<IndicatorDefinition> definitionSwitch = new DefinitionSwitch<IndicatorDefinition>() {

            @Override
            public IndicatorDefinition caseIndicatorDefinition(IndicatorDefinition object) {
                return object;
            }

        };
        if (file != null) {
            Resource resource = EmfFileResourceUtil.getInstance().getFileResource(file.getAbsolutePath());

            EList<EObject> contents = resource.getContents();
            if (contents.isEmpty()) {
                log.error("no content in: " + resource);//$NON-NLS-1$
            }

            for (EObject object : contents) {
                ModelElement switchObject = definitionSwitch.doSwitch(object);

                if (switchObject != null) {
                    return switchObject;
                }
            }
        }
        return null;
    }

    private List<File> searchAllFiles(File file) {
        List<File> fileList = new ArrayList<File>();
        if (file.exists()) {
            for (File child : file.listFiles()) {
                if (child.isDirectory()) {
                    fileList.addAll(searchAllFiles(child));
                    continue;
                }

                if (checkFile(child)) {
                    fileList.add(child);
                }
            }
        }
        return fileList;
    }

    private boolean checkFile(File file) {
        String path = file.getAbsolutePath();
        String extension = path.substring(path.lastIndexOf(".") + 1);
        return file != null && FactoriesUtil.DEFINITION.equalsIgnoreCase(extension);
    }

    public String getTdqProjectPath() {
        String projectPath = null;
        int pos = PROJECT_PATH.lastIndexOf(File.separator);
        if (pos > 0 && pos < PROJECT_PATH.length() + 1) {
            projectPath = "items" + File.separator + PROJECT_PATH.substring(pos + 1).toLowerCase(); //$NON-NLS-1$
            File f = new File(projectPath);
            if (f.exists()) { // running exported job
                return projectPath + File.separator;
            }
        }
        // running job in studio
        return PROJECT_PATH + File.separator;
    }

    public String getTdqLibPath() {
        return getTdqProjectPath() + "TDQ_Libraries" + File.separator;
    }

    /**
     * 
     * DOC mzhao feature 13676 split system indicators.
     * 
     * @return
     */
    public IndicatorsDefinitions loadDefinitionsFromFile() {

        Resource definitionsFile = getDefCategoryResourceFromFile();

        EList<EObject> contents = definitionsFile.getContents();
        if (contents == null || contents.isEmpty()) {
            log.error(Messages.getString("DefinitionHandler.NoContentFound", definitionsFile.getURI())); //$NON-NLS-1$
            return null;
        }
        DefinitionSwitch<IndicatorsDefinitions> catSwitch = new DefinitionSwitch<IndicatorsDefinitions>() {

            @Override
            public IndicatorsDefinitions caseIndicatorsDefinitions(IndicatorsDefinitions object) {
                return object;
            }

        };
        return catSwitch.doSwitch(contents.get(0));
    }

    /**
     * 
     * DOC mzhao Get definition category files from file.
     * 
     * @return
     */
    private Resource getDefCategoryResourceFromFile() {
        // MOD scorreia 2008-08-04 use EMFUtil instead of EMFSharedResources
        // because this file does not need to be saved
        // with the other files. Moreover, we need to be able to edit it when
        // needed (with default ".definition" editor
        // for development purposes)
        Resource definitionsFile = null;

        if (Platform.isRunning()) {
            IPath definitionPath = ResourceManager.getLibrariesFolder().getFullPath().append(FILENAME);
            URI uri = URI.createPlatformResourceURI(definitionPath.toString(), false);
            try {
                // load from workspace path
                // do not create it here if it does not exist.
                // ADD msjian TDQ-6865 2013-3-8: reload first, because the file is copied into workspace just now
                definitionsFile = EMFSharedResources.getInstance().reloadResource(uri);
                // TDQ-6865~
                if (log.isDebugEnabled()) {
                    log.debug("Definition of indicators loaded from " + uri); //$NON-NLS-1$
                }
            } catch (RuntimeException e) {
                if (log.isDebugEnabled()) {
                    log.debug("ERROR: " + e.getMessage(), e); //$NON-NLS-1$
                }
            }
            if (definitionsFile == null) {
                uri = URI.createPlatformPluginURI(PLUGIN_PATH, false);
                try { // load from plugin path
                    definitionsFile = EMFSharedResources.getInstance().getResource(uri, true);
                    if (log.isDebugEnabled()) {
                        log.debug("Definition of indicators loaded from " + uri); //$NON-NLS-1$
                    }
                } catch (RuntimeException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("ERROR: " + e.getMessage(), e); //$NON-NLS-1$
                    }
                }

                if (definitionsFile == null) {
                    // try to load from a local file
                    URI createFileURI = URI.createFileURI(".." + File.separator + PLUGIN_PATH); //$NON-NLS-1$
                    definitionsFile = EMFSharedResources.getInstance().getResource(createFileURI, true);
                    if (log.isDebugEnabled()) {
                        log.debug("Definition of indicators loaded from " + createFileURI); //$NON-NLS-1$
                    }
                }

                if (definitionsFile == null) {
                    log.error(Messages.getString("DefinitionHandler.NoResourceFound", PLUGIN_PATH, uri));//$NON-NLS-1$
                    return null;
                }
            }

        } else { // call reporting engine as java library

            try { // try to load from a local file
                IPath definitionPath = Path.fromOSString(getTdqLibPath()).append(FILENAME);
                definitionsFile = EmfFileResourceUtil.getInstance().getFileResource(definitionPath.toString());
            } catch (RuntimeException e) {
                if (log.isDebugEnabled()) {
                    log.debug("ERROR: " + e.getMessage(), e); //$NON-NLS-1$
                }
            }

            if (definitionsFile == null) {
                log.error(Messages.getString("DefinitionHandler.NoResourceFound", PLUGIN_PATH));//$NON-NLS-1$
                return null;
            }
        }

        return definitionsFile;
    }

    /**
     * Method "getIndicatorsDefinitions".
     * 
     * @return the singleton analysis categories (or throws an exception if a problem occured)
     */
    public List<IndicatorDefinition> getIndicatorsDefinitions() {
        if (indicatorDefinitions == null || indicatorDefinitions.isEmpty()) {
            initializeDefinitions();
        } else {
            // resolve the IndicatorDefinition if need and only when it isn't initialized.
            indicatorDefinitions = resolve(indicatorDefinitions);
            if (indicatorDefinitions == null) {
                throw new RuntimeException(Messages.getString("DefinitionHandler.IndicatorsDefinition")); //$NON-NLS-1$
            }
        }
        return indicatorDefinitions;
    }

    /**
     * resolve the IndicatorDefinition if it is proxy.
     * 
     * @param definitions
     * @return
     */
    private List<IndicatorDefinition> resolve(List<IndicatorDefinition> definitions) {
        List<IndicatorDefinition> result = new ArrayList<IndicatorDefinition>();
        if (definitions != null && !definitions.isEmpty()) {
            for (IndicatorDefinition indDef : definitions) {
                if (indDef.eIsProxy()) {
                    indDef = (IndicatorDefinition) EObjectHelper.resolveObject(indDef);
                }
                result.add(indDef);
            }
        }
        return result;
    }

    /**
     * 
     * reload all the indicators' Definitions.
     */
    public void reloadIndicatorsDefinitions() {
        initializeDefinitions();
    }

    /**
     * DOC bZhou Comment method "copyDefinitionsIntoFolder".
     * 
     * @param ifolder
     * @return
     */
    public Resource copyDefinitionsIntoFolder(IFolder ifolder) {
        URI destinationUri = URI.createPlatformResourceURI(ifolder.getFullPath().toString(), false);
        return copyDefinitionsIntoFolder(destinationUri);
    }

    /**
     * DOC bZhou Comment method "copyDefinitionsIntoFolder".
     * 
     * @param destinationUri
     * @return
     */
    public Resource copyDefinitionsIntoFolder(URI destinationUri) {
        // MOD mzhao feature 13676,Reload from original place of .talend.definition file. 2010-07-09
        URI uri = URI.createPlatformPluginURI(PLUGIN_PATH, false);
        Resource resource = EMFSharedResources.getInstance().getResource(uri, true);
        if (resource != null) {
            EMFUtil.changeUri(resource, destinationUri);

            if (EMFSharedResources.getInstance().saveResource(resource)) {
                if (log.isInfoEnabled()) {
                    log.info("Indicator default definitions correctly saved in " + resource.getURI()); //$NON-NLS-1$
                }
            } else {
                log.error(Messages.getString("DefinitionHandler.FailToSave", resource.getURI()));//$NON-NLS-1$

            }
        }
        return resource;
    }

    /**
     * Method "setDefaultIndicatorDefinition" sets the indicator's default definition.
     * 
     * @param indicator the indicator
     * @return true when set, false when not set.
     */
    public boolean setDefaultIndicatorDefinition(Indicator indicator) {
        return indicatorSwitch.doSwitch(indicator);
    }

    /**
     * 
     * DOC mzhao Get indicator definition by label.
     * 
     * @param label
     * @return
     */
    public IndicatorDefinition getIndicatorDefinition(String label) {
        for (IndicatorDefinition indicatorDefinition : this.getIndicatorsDefinitions()) {
            if (indicatorDefinition != null && indicatorDefinition.getLabel() != null
                    && indicatorDefinition.getLabel().compareTo(label) == 0) {
                return indicatorDefinition;
            } else if (indicatorDefinition != null && indicatorDefinition.getName() != null
                    && indicatorDefinition.getName().compareTo(label) == 0) {
                return indicatorDefinition;
            }
        }
        return null;
    }

    public IndicatorDefinition getIndicatorDefinitionBySemanticLabel(String label) {
        for (IndicatorDefinition indicatorDefinition : this.getIndicatorsDefinitions()) {
            if (indicatorDefinition != null && indicatorDefinition.getName() != null
                    && indicatorDefinition.getName().replace(" ", "_").compareTo(label) == 0) {
                return indicatorDefinition;
            }
        }
        return null;
    }

    /**
     * get UDI Indicator Definitions.
     * 
     * @return
     */
    public List<IndicatorDefinition> getUserDefinedIndicatorDefinitions() {
        List<IndicatorDefinition> list = new ArrayList<IndicatorDefinition>();
        for (IndicatorDefinition indicatorDefinition : getIndicatorsDefinitions()) {
            IndicatorCategory category = IndicatorCategoryHelper.getCategory(indicatorDefinition);
            if (IndicatorCategoryHelper.isUserDefCategory(category)) {
                list.add(indicatorDefinition);
            }
        }
        return list;
    }

    public boolean updateRegex(String dbmsName, String regexpFunction) {
        boolean ok = true;
        boolean replaced = false;
        IndicatorDefinition regexIndDef = this.getIndicatorDefinition(REGULAR_EXPRESSION_MATCHING);
        EList<TdExpression> sqlGenericExpression = regexIndDef.getSqlGenericExpression();

        for (Expression expression : sqlGenericExpression) {
            // MOD qiongli 2011-4-18,bug 16723.data cleansing.
            if (DbmsLanguageFactory.compareDbmsLanguage(dbmsName, expression.getLanguage())) {
                replaced = replaceBodyWith(expression, regexpFunction);
            }
        }
        if (!replaced) {
            // add new expression
            String genericSQL = getGenericSQL(dbmsName, regexpFunction);
            TdExpression createdExpression = BooleanExpressionHelper.createTdExpression(dbmsName, genericSQL);
            sqlGenericExpression.add(createdExpression);
        }
        return ok;
    }

    /**
     * DOC scorreia Comment method "getGenericSQL".
     * 
     * @param dbmsName
     * @param regexpFunction
     * @return
     */
    private String getGenericSQL(String dbmsName, String regexpFunction) {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(dbmsName);
        return dbmsLanguage.createGenericSqlWithRegexFunction(regexpFunction);
    }

    /**
     * DOC scorreia Comment method "replaceBodyWith".
     * 
     * @param expression
     * @param regexpFunction
     * @return
     */
    private boolean replaceBodyWith(Expression expression, String regexpFunction) {
        expression.setBody(this.getGenericSQL(expression.getLanguage(), regexpFunction));
        return true;
    }

    /**
     * Note: scorreia. All indicator definitions defined in .Talend.definition file must be implemented here.
     * 
     * WARNING: The label of the indicator definition in .Talend.definition must be exactly the same as the strings used
     * here.
     */
    private final IndicatorsSwitch<Boolean> indicatorSwitch = new IndcatorSwitchImp(); // EOC

    // IndicatorsSwitch

    /**
     * Method "getDQRuleIndicatorCategory".
     * 
     * @return the category of the DQ Rule indicators
     */
    public IndicatorCategory getDQRuleIndicatorCategory() {
        return getIndicatorCategory(DQ_RULE_CATEGORY);
    }

    /**
     * Method "getDQRuleDefaultIndicatorDefinition".
     * 
     * @return the default indicator definition of the DQ rule.
     */
    public IndicatorDefinition getDQRuleDefaultIndicatorDefinition() {
        return getDefinitionById(DQ_RULE_DEFINITION);
    }

    public IndicatorDefinition getFDRuleDefaultIndicatorDefinition() {
        return getDefinitionById(FD_RULE_DEFINITION);
    }

    /**
     * 
     * DOC mzhao Get indicator definition by definition id (xmi id).
     * 
     * @param definitionId
     * @return
     */
    public IndicatorDefinition getDefinitionById(String definitionId) {
        for (IndicatorDefinition indDef : this.getIndicatorsDefinitions()) {
            CwmResource resource = (CwmResource) indDef.eResource();
            if (resource == null) {
                return null;
            }
            EObject object = resource.getEObject(definitionId);
            if (object != null && DefinitionPackage.eINSTANCE.getIndicatorDefinition().equals(object.eClass())) {
                return (IndicatorDefinition) object;
            }
        }
        return null;
    }

    public String getIdByDefinition(IndicatorDefinition definition) {
        String id = definitionToIdMap.get(definition.getName());
        if (id == null) {
            id = ResourceHelper.getUUID(definition);
            definitionToIdMap.put(definition.getName(), id);
        }
        return id;
    }

    public IndicatorCategory getUserDefinedCountIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_COUNT_CATEGORY);
    }

    public IndicatorCategory getUserDefinedMatchIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_MATCH_CATEGORY);
    }

    public IndicatorCategory getUserDefinedFrequencyIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_FREQUENCY_CATEGORY);
    }

    public IndicatorCategory getUserDefinedRealValueIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_REAL_VALUE_CATEGORY);
    }

    public IndicatorCategory getUserDefinedComparisonIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_COMPARISON_CATEGORY);
    }

    public IndicatorCategory getUserDefinedNominalCorrelationIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_NOMINAL_CORRELATION_CATEGORY);
    }

    public IndicatorCategory getUserDefinedIntervalCorrelationIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_INTERVAL_CORRELATION_CATEGORY);
    }

    public IndicatorCategory getUserDefinedTimeCorrelationIndicatorCategory() {
        return getIndicatorCategory(USER_DEFINED_TIME_CORRELATION_CATEGORY);
    }

    private IndicatorCategory getIndicatorCategory(String categoryId) {
        // MOD by zshen for feature 18724 if it is proxy then reload the definitions
        if (indicatorCategories.size() > 0 && indicatorCategories.get(0).eIsProxy()) {
            this.initializeDefinitions();
        }
        for (IndicatorCategory indCategory : indicatorCategories) {

            CwmResource resource = (CwmResource) indCategory.eResource();
            if (resource != null) {
                EObject object = resource.getEObject(categoryId);
                if (object != null && DefinitionPackage.eINSTANCE.getIndicatorCategory().equals(object.eClass())) {
                    return (IndicatorCategory) object;
                }
            }
        }
        return null;
    }

    public Map<String, IndicatorCategory> getUserDefinedIndicatorCategoryMap() {
        // FIXME lazy initialization of a static field
        if (userDefinedIndicatorCategoryMap == null) {
            userDefinedIndicatorCategoryMap = new HashMap<String, IndicatorCategory>();

            // init user defined indicator categories
            List<IndicatorCategory> categoryList = new ArrayList<IndicatorCategory>();
            categoryList.add(getUserDefinedCountIndicatorCategory());
            categoryList.add(getUserDefinedFrequencyIndicatorCategory());
            categoryList.add(getUserDefinedMatchIndicatorCategory());
            categoryList.add(getUserDefinedRealValueIndicatorCategory());
            // categoryList.add(getUserDefinedComparisonIndicatorCategory());
            // categoryList.add(getUserDefinedIntervalCorrelationIndicatorCategory());
            // categoryList.add(getUserDefinedNominalCorrelationIndicatorCategory());
            // categoryList.add(getUserDefinedTimeCorrelationIndicatorCategory());

            for (IndicatorCategory category : categoryList) {
                userDefinedIndicatorCategoryMap.put(
                        InternationalizationUtil.getCategoryInternationalizationLabel(category.getLabel()), category);
            }

            categoryList = null;
        }
        return userDefinedIndicatorCategoryMap;
    }

    public Collection<String> getUserDefinedIndicatorCategoryLabels() {
        return getUserDefinedIndicatorCategoryMap().keySet();
    }

    public Collection<IndicatorCategory> getUserDefinedIndicatorCategoryList() {
        return getUserDefinedIndicatorCategoryMap().values();
    }

    public IndicatorCategory getIndicatorCategoryByLabel(String label) {
        return getUserDefinedIndicatorCategoryMap().get(label);
    }

    public static IFile getTalendDefinitionFile() {
        return ResourceManager.getLibrariesFolder().getFile(FILENAME);
    }

    /**
     * reload. if (resource == null) { + return null; + }
     * 
     * @deprecated see {{@link #reloadIndicatorsDefinitions()}
     */
    @Deprecated
    public static void reload() {
        instance = null;
    }

    /**
     * DOC judge the database can run Regular Expression Matching Indicator or not.
     * 
     * @param dbmsLanguage
     * @return
     */
    public boolean canRunRegularExpressionMatchingIndicator(DbmsLanguage dbmsLanguage, boolean isJavaEngin, Pattern pattern) {

        EList<PatternComponent> sqlGenericExpression = pattern.getComponents();
        for (PatternComponent exp : sqlGenericExpression) {
            String compareLanguage = null;
            if (!isJavaEngin) {
                compareLanguage = dbmsLanguage.getDbmsName();
            } else {
                compareLanguage = ExecutionLanguage.JAVA.getName();
            }
            if (exp instanceof RegularExpression) {
                String expressionLanguage = ((RegularExpression) exp).getExpression().getLanguage();
                if (DbmsLanguageFactory.isAllDatabaseType(expressionLanguage)
                        || DbmsLanguageFactory.compareDbmsLanguage(compareLanguage, expressionLanguage)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 
     * Update aggregates for some indicators(Simple Statistic Indicator/Text Statistic Indicator...).make the path/name
     * of indicator definition in agregate right and is not a proxy).
     */
    public void updateAggregates() {
        List<IndicatorDefinition> indicatorsDefinitions = getIndicatorsDefinitions();

        String defFileExt = PluginConstant.DOT_STRING + FactoriesUtil.DEFINITION;
        for (IndicatorDefinition indiDef : indicatorsDefinitions) {
            EList<IndicatorDefinition> aggregatedDefinitions = indiDef.getAggregatedDefinitions();
            List<IndicatorDefinition> updatedAggrDefinitions = new ArrayList<IndicatorDefinition>();
            if (indiDef.eIsProxy() || aggregatedDefinitions == null || aggregatedDefinitions.isEmpty()) {
                continue;
            }
            for (IndicatorDefinition aggrDef : aggregatedDefinitions) {
                URI oldUri = EObjectHelper.getURI(aggrDef);
                if (oldUri == null) {
                    continue;
                }
                String oldLabel = (oldUri.lastSegment());
                oldLabel = oldLabel.replace(defFileExt, PluginConstant.EMPTY_STRING);
                if (oldLabel.equalsIgnoreCase("Inter Quartile Range")) { //$NON-NLS-1$
                    oldLabel = "IQR"; //$NON-NLS-1$
                }
                IndicatorDefinition find = getIndicatorDefinition(oldLabel);
                if (find != null) {
                    updatedAggrDefinitions.add(find);
                }
            }
            indiDef.getAggregatedDefinitions().clear();
            indiDef.getAggregatedDefinitions().addAll(updatedAggrDefinitions);
            EMFSharedResources.getInstance().saveResource(indiDef.eResource());
        }
    }

    /**
     * reload all user defined indicators, when some udi's name is changed, this method will be needed
     */
    public void reloadAllUDIs() {
        List<IndicatorDefinition> udis = new ArrayList<IndicatorDefinition>();
        for (IndicatorDefinition indicatorDefinition : this.getIndicatorsDefinitions()) {
            if (indicatorDefinition instanceof UDIndicatorDefinition) {
                udis.add(indicatorDefinition);
            }
        }

        // remove all udis
        this.getIndicatorsDefinitions().removeAll(udis);

        // reload all udis
        this.getIndicatorsDefinitions().addAll(IndicatorResourceFileHelper.getInstance().getAllUDIs());
    }
}
