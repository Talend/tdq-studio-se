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
package org.talend.dq.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.definition.userdefine.UserdefineFactory;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.indicators.sql.util.IndicatorSqlSwitch;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.classloader.TalendURLClassLoader;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class UDIHelper {

    private static final Map<Indicator, Indicator> JAVAUDIMAP = new HashMap<Indicator, Indicator>();

    private static Properties UDI_TEMPLATES_PROPERTIES = new Properties();

    public static final String JAREXTENSIONG = "jar";//$NON-NLS-1$

    /**
     * separate the key from value.
     */
    public static final String PARA_SEPARATE_1 = "__PARA_SEP_1__"; //$NON-NLS-1$

    /**
     * the display name of PARA_SEPARATE_1.
     */
    public static final String PARA_SEPARATE_1_DISPLAY = ":"; //$NON-NLS-1$

    /**
     * separate the KeyValue from another one.
     */
    public static final String PARA_SEPARATE_2 = "__PARA_SEP_2__"; //$NON-NLS-1$

    /**
     * the display name of PARA_SEPARATE_2.
     */
    public static final String PARA_SEPARATE_2_DISPLAY = ";"; //$NON-NLS-1$

    private static IndicatorSqlSwitch<UserDefIndicator> userDefIndSwitch = new IndicatorSqlSwitch<UserDefIndicator>() {

        @Override
        public UserDefIndicator caseUserDefIndicator(UserDefIndicator object) {
            return object;
        }

    };

    private UDIHelper() {
    }

    private static Logger log = Logger.getLogger(UDIHelper.class);

    // ("select * from *").length
    public static final int MIN_EXPRESSION_LENGTH = 16;

    public static IndicatorCategory getUDICategory(IndicatorDefinition indicatorDefinition) {
        if (indicatorDefinition != null) {
            EList<IndicatorCategory> categories = indicatorDefinition.getCategories();
            if (categories != null && categories.size() > 0) {
                return categories.get(0);
            }
        }
        return null;
    }

    /**
     * getUDICategory by modelElement
     */
    public static IndicatorCategory getUDICategory(ModelElement modelElement) {
        if (modelElement != null && modelElement instanceof IndicatorDefinition) {
            return getUDICategory((IndicatorDefinition) modelElement);
        }
        return null;
    }

    public static IndicatorCategory getUDICategory(Indicator indicator) {
        if (indicator != null) {
            return getUDICategory(indicator.getIndicatorDefinition());
        }
        return null;
    }

    /**
     * Set the category of the IndicatorDefinition, if the category is null set UserDefinedCount category.
     * 
     * @param definition
     * @param category
     */
    public static void setUDICategory(IndicatorDefinition definition, IndicatorCategory category) {
        if (definition != null) {
            if (category == null) {
                category = DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory();
            }

            EList<IndicatorCategory> categories = definition.getCategories();
            if (categories != null) {
                categories.clear();
                categories.add(category);
            }
        }
    }

    public static void setUDICategory(IndicatorDefinition indicatorDefinition, String categoryLabel) {
        setUDICategory(indicatorDefinition, DefinitionHandler.getInstance().getIndicatorCategoryByLabel(categoryLabel));
    }

    public static void setUDICategory(Indicator indicator, String categoryLabel) {
        if (indicator != null) {
            setUDICategory(indicator.getIndicatorDefinition(), categoryLabel);
        }
    }

    public static UDIndicatorDefinition createUDI(String name, String author, String description, String purpose, String status,
            String category, String javaClassName, String javaJarPath) {
        // IndicatorDefinition id = DefinitionFactory.eINSTANCE.createIndicatorDefinition();
        UDIndicatorDefinition id = UserdefineFactory.eINSTANCE.createUDIndicatorDefinition();
        id.setName(name);
        MetadataHelper.setAuthor(id, author == null ? PluginConstant.EMPTY_STRING : author);
        MetadataHelper.setDescription(description == null ? PluginConstant.EMPTY_STRING : description, id);
        MetadataHelper.setPurpose(purpose == null ? PluginConstant.EMPTY_STRING : purpose, id);
        // MOD mzhao feature 7479 2009-10-16
        MetadataHelper.setDevStatus(id, status == null ? PluginConstant.EMPTY_STRING : status);
        TaggedValueHelper.setTaggedValue(id, TaggedValueHelper.CLASS_NAME_TEXT, javaClassName);
        TaggedValueHelper.setTaggedValue(id, TaggedValueHelper.JAR_FILE_PATH, javaJarPath);
        setUDICategory(id, category);
        return id;
    }

    public static Set<String> getAllIndicatorNames(IFolder folder) {
        Set<String> list = new HashSet<String>();
        return getNestFolderIndicatorNames(list, folder);
    }

    private static Set<String> getNestFolderIndicatorNames(Set<String> list, IFolder folder) {
        try {
            for (IResource resource : folder.members()) {
                if (resource instanceof IFile) {
                    IndicatorDefinition id = IndicatorResourceFileHelper.getInstance().findIndDefinition((IFile) resource);
                    if (id != null) {
                        list.add(id.getName());
                    }
                } else {
                    getNestFolderIndicatorNames(list, (IFolder) resource);
                }
            }
        } catch (CoreException e) {
            log.error(e, e);
        }
        return list;
    }

    public static String getMatchingIndicatorName(IndicatorDefinition indicatorDefinition, Pattern pattern) {
        if (indicatorDefinition != null) {
            return pattern.getName() + "(" + indicatorDefinition.getName() + ")";//$NON-NLS-1$//$NON-NLS-2$
        } else {
            return pattern.getName();
        }
    }

    public static boolean isCount(Indicator indicator) {
        return isCategory(indicator, DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory());
    }

    public static boolean isCount(IndicatorDefinition indicatorDefinition) {
        return isCategory(indicatorDefinition, DefinitionHandler.getInstance().getUserDefinedCountIndicatorCategory());
    }

    public static boolean isRealValue(Indicator indicator) {
        return isCategory(indicator, DefinitionHandler.getInstance().getUserDefinedRealValueIndicatorCategory());
    }

    public static boolean isRealValue(IndicatorDefinition indicatorDefinition) {
        return isCategory(indicatorDefinition, DefinitionHandler.getInstance().getUserDefinedRealValueIndicatorCategory());
    }

    public static boolean isFrequency(Indicator indicator) {
        return isCategory(indicator, DefinitionHandler.getInstance().getUserDefinedFrequencyIndicatorCategory());
    }

    public static boolean isFrequency(IndicatorDefinition indicatorDefinition) {
        return isCategory(indicatorDefinition, DefinitionHandler.getInstance().getUserDefinedFrequencyIndicatorCategory());
    }

    public static boolean isMatching(Indicator indicator) {
        return isCategory(indicator, DefinitionHandler.getInstance().getUserDefinedMatchIndicatorCategory());
    }

    public static boolean isMatching(IndicatorDefinition indicatorDefinition) {
        return isCategory(indicatorDefinition, DefinitionHandler.getInstance().getUserDefinedMatchIndicatorCategory());
    }

    public static boolean isCategory(Indicator indicator, IndicatorCategory indicatorCategory) {
        if (indicator != null) {
            return isCategory(indicator.getIndicatorDefinition(), indicatorCategory);
        }
        return false;
    }

    public static boolean isCategory(IndicatorDefinition indicatorDefinition, IndicatorCategory indicatorCategory) {
        if (indicatorDefinition != null && indicatorCategory != null) {
            return indicatorCategory.equals(getUDICategory(indicatorDefinition));
        }
        return false;
    }

    public static boolean isUDI(Indicator indicator) {
        return indicator instanceof UserDefIndicator;
    }

    /**
     * yyi 2009-09-22 To check the expression is null, empty or less than 16 characters Feature : 8866.
     */
    public static boolean isUDIValid(IndicatorDefinition indicatorDefinition) {
        boolean valid = true;

        if ("".equals(indicatorDefinition.getName())) { //$NON-NLS-1$
            valid = false;
        }

        if ('\'' == indicatorDefinition.getName().charAt(0)) {
            valid = false;
        }

        if (!isJUDIValid(indicatorDefinition)) {
            if (0 == indicatorDefinition.getSqlGenericExpression().size() && !isJUDIValid(indicatorDefinition)) {
                valid = false;
            }

            for (Expression exp : indicatorDefinition.getSqlGenericExpression()) {
                if (null == exp.getBody() || exp.getBody().length() + 1 < MIN_EXPRESSION_LENGTH) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    /**
     * if the config information if valid for Java Engine return true, else return false.
     * 
     * @param indicatorDefinition
     * @return
     */
    public static boolean isJUDIValid(IndicatorDefinition indicatorDefinition) {
        if (indicatorDefinition == null) {
            return false;
        }
        boolean valid = true;
        String classNameText = TaggedValueHelper.getClassNameText(indicatorDefinition);
        String jarFilePath = TaggedValueHelper.getJarFilePath(indicatorDefinition);
        if (StringUtils.isBlank(classNameText) || StringUtils.isBlank(jarFilePath)) {
            valid = false;
        }
        return valid;
    }

    public static ReturnCode validate(IndicatorDefinition indicatorDefinition) {

        ReturnCode rc = new ReturnCode(true);
        List<String> errorList = new ArrayList<String>();

        // MOD mzhao feature 11128, In case of Java UDI, No expression is allowed to be saved.
        if (!containsJavaUDI(indicatorDefinition)) {
            if (0 == indicatorDefinition.getSqlGenericExpression().size()) {
                errorList.add(Messages.getString("UDIHelper.validateNoExpression"));//$NON-NLS-1$
                rc.setOk(false);
            }
        }

        if (PluginConstant.EMPTY_STRING.equals(indicatorDefinition.getName())) {
            errorList.add(Messages.getString("UDIHelper.validateNoName"));//$NON-NLS-1$
            rc.setOk(false);
        }

        for (Expression exp : indicatorDefinition.getSqlGenericExpression()) {
            if (null == exp.getBody() || exp.getBody().length() + 1 < MIN_EXPRESSION_LENGTH) {
                errorList.add(Messages.getString("UDIHelper.validateTooShort"));//$NON-NLS-1$
                rc.setOk(false);
            }
        }

        String message = Messages.getString("UDIHelper.validateCannotSave");//$NON-NLS-1$
        String wrap = System.getProperty("line.separator");//$NON-NLS-1$
        for (int i = 0; i < errorList.size(); i++) {
            message += wrap + (i + 1) + org.talend.dataquality.PluginConstant.DOT_STRING + errorList.get(i);
        }
        rc.setMessage(message);

        return rc;
    }

    /**
     * 
     * DOC mzhao feature 11128, If the execute engine and by the same time Java User Defined Indicator is also defined,
     * then compute via Java UDI, here convert common udi to a Java UDI.
     * 
     * @param udi
     * @return
     * @throws Exception
     */
    public static Indicator adaptToJavaUDI(Indicator indicator) throws Throwable {
        Indicator returnIndicator = getUDIFromMap(indicator);
        if (returnIndicator != null) {
            return returnIndicator;
        }
        UserDefIndicator adaptedUDI = null;
        if (userDefIndSwitch.doSwitch(indicator) != null) {
            EList<TaggedValue> taggedValues = indicator.getIndicatorDefinition().getTaggedValue();
            String userJavaClassName = null;
            String jarPath = null;
            for (TaggedValue tv : taggedValues) {
                if (tv.getTag().equals(TaggedValueHelper.CLASS_NAME_TEXT)) {
                    userJavaClassName = tv.getValue();
                    continue;
                }
                if (tv.getTag().equals(TaggedValueHelper.JAR_FILE_PATH)) {
                    jarPath = tv.getValue();
                }
            }
            // MOD by zshen for feature 18724
            if (validateJavaUDI(userJavaClassName, jarPath)) {
                List<URL> jarUrls = new ArrayList<URL>();
                for (File file : getContainJarFile(jarPath)) {
                    jarUrls.add(file.toURI().toURL());
                }
                TalendURLClassLoader cl;
                // Note that the 2nd parameter (classloader) is needed to load class UserDefinitionIndicator from
                // org.talend.dataquality plugin.
                cl = new TalendURLClassLoader(jarUrls.toArray(new URL[jarUrls.size()]), UDIHelper.class.getClassLoader());

                Class<?> clazz = null;
                clazz = cl.findClass(userJavaClassName);
                if (clazz != null) {
                    // MOD yyin 20121012 TDQ-6259
                    UserDefIndicator judi = (UserDefIndicator) clazz.newInstance();
                    // judi.setIndicatorDefinition(indicator.getIndicatorDefinition());
                    PropertyUtils.copyProperties(judi, indicator);
                    // judi.setAnalyzedElement(indicator.getAnalyzedElement());
                    adaptedUDI = judi;
                    JAVAUDIMAP.put(indicator, adaptedUDI);
                }

            }
        }
        return adaptedUDI;
    }

    /**
     * DOC zshen Comment method "getUDIFromMap".
     * 
     * @param indicator
     */
    private static Indicator getUDIFromMap(Indicator indicator) {
        // If the JUDI already been initiatated
        if (JAVAUDIMAP.get(indicator) != null) {
            return JAVAUDIMAP.get(indicator);
        }
        // indicator itself already be a java user define indicator.
        if (JAVAUDIMAP.values().contains(indicator)) {
            return indicator;
        }
        return null;
    }

    private static boolean validateJavaUDI(String className, String jarPath) {
        if (className == null || jarPath == null || className.trim().equals(PluginConstant.EMPTY_STRING)
                || jarPath.trim().equals(PluginConstant.EMPTY_STRING)) {
            return false;
        }
        return true;
    }

    public static boolean containsJavaUDI(IndicatorDefinition definition) {
        EList<TaggedValue> tvs = definition.getTaggedValue();
        int findCount = 0;
        for (TaggedValue tv : tvs) {
            if (tv.getTag().equals(TaggedValueHelper.CLASS_NAME_TEXT)) {
                findCount++;
            } else if (tv.getTag().equals(TaggedValueHelper.JAR_FILE_PATH)) {
                findCount++;
            }
            if (findCount == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC klliu Comment method "isJavaUDI".
     * 
     * @param indicator
     * @return
     */
    public static boolean isJavaUDI(Indicator indicator) {
        IndicatorDefinition definition = indicator.getIndicatorDefinition();
        if (definition == null) {
            return false;
        }
        return containsJavaUDI(definition);
    }

    /**
     * 
     * zshen Comment method "getLibJarFileList".
     * 
     * @return
     */
    private static List<File> getLibJarFileList() {
        List<File> fileList = new ArrayList<File>();
        try {
            File newFile = ResourceManager.getUDIJarFolder().getLocation().toFile();
            if (!newFile.exists()) {
                newFile.mkdir();
            }
            for (org.eclipse.core.resources.IResource fileResource : ResourceManager.getUDIJarFolder().members()) {
                if (IResource.FILE == fileResource.getType()
                        && JAREXTENSIONG.equalsIgnoreCase(fileResource.getFullPath().getFileExtension())) {
                    fileList.add(fileResource.getLocation().toFile());
                }
            }
        } catch (CoreException e) {
            log.error(e, e);
        }
        return fileList;
    }

    private static List<File> getLibJarFileListForJobs() {
        List<File> fileList = new ArrayList<File>();
        String tdqLibPath = DefinitionHandler.getInstance().getTdqLibPath();
        String libJarPath = tdqLibPath + "Indicators" + File.separator + "User Defined Indicators" + File.separator + "lib"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        File newFile = new File(libJarPath);
        if (newFile.exists() && newFile.isDirectory()) {
            for (File libFile : newFile.listFiles()) {
                if (libFile.getName().endsWith(".jar")) { //$NON-NLS-1$
                    fileList.add(libFile);
                }
            }
        }
        return fileList;
    }

    /**
     * Judge whether current folder is udi jar folder
     * 
     * @param folder
     * @return true if it is else false
     */
    public static boolean isUDILibFolder(IFolder folder) {
        return folder.getName().equals(EResourceConstant.USER_DEFINED_INDICATORS_LIB.getName())
                && folder.getFullPath().segmentCount() > 2
                && folder.getFullPath().segment(folder.getFullPath().segmentCount() - 2)
                        .equals(EResourceConstant.USER_DEFINED_INDICATORS.getName());
    }

    /**
     * 
     * zshen Comment method "getContainJarFile".
     * 
     * @param jarPathStr
     * @return
     */
    private static List<File> getContainJarFile(String jarPathStr) {
        List<File> fileList = new ArrayList<File>();

        List<File> libJarFileList = null;
        if (Platform.isRunning()) {
            libJarFileList = getLibJarFileList();
        } else {
            libJarFileList = getLibJarFileListForJobs();
        }
        for (String containJarName : jarPathStr.split("\\|\\|")) {//$NON-NLS-1$            
            for (File libJarFile : libJarFileList) {
                if (libJarFile.getName().equalsIgnoreCase(containJarName)) {
                    fileList.add(libJarFile);
                    break;
                }
            }
        }
        return fileList;
    }

    /**
     * Create Templates Properties..
     * 
     * @return
     */
    public static Properties getCreateTemplatesProperties() {
        InputStream in = UDIHelper.class.getClassLoader().getResourceAsStream("viewRowsTemplates.txt"); //$NON-NLS-1$
        try {
            UDI_TEMPLATES_PROPERTIES.load(in);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return UDI_TEMPLATES_PROPERTIES;
    }

    /**
     * get Templates Properties which content is used only by use define indicators.
     * 
     * @return
     */
    public static Properties getUDITemplatesProperties() {
        if (UDI_TEMPLATES_PROPERTIES != null && !UDI_TEMPLATES_PROPERTIES.isEmpty()) {
            return UDI_TEMPLATES_PROPERTIES;
        }
        return getCreateTemplatesProperties();
    }

    /**
     * get Query From Templates.
     * 
     * @param selectTabNumber
     * @param language
     * @param category
     * @return
     */
    public static String getQueryFromTemplates(int selectTabNumber, String language, IndicatorCategory category) {
        Properties properties = UDIHelper.getUDITemplatesProperties();
        // get sql template depend on the language and selectTabNumber

        String key = PluginConstant.EMPTY_STRING;
        String autoGenSql = PluginConstant.EMPTY_STRING;
        // when from tab0 change to tab1, get full sql
        if (selectTabNumber == 1) {
            if (IndicatorCategoryHelper.isUserDefCount(category)) {
                key = "SqlTemplate." + language + ".UDI.Count"; //$NON-NLS-1$ //$NON-NLS-2$
            } else if (IndicatorCategoryHelper.isUserDefRealValue(category)) {
                key = "SqlTemplate." + language + ".UDI.RealValue"; //$NON-NLS-1$ //$NON-NLS-2$
            } else if (IndicatorCategoryHelper.isUserDefFrequency(category)) {
                key = "SqlTemplate." + language + ".UDI.Frequency"; //$NON-NLS-1$ //$NON-NLS-2$
            } else if (IndicatorCategoryHelper.isUserDefMatching(category)) {
                key = "SqlTemplate." + language + ".UDI.Match"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else if (selectTabNumber == 2) {
            // for match is View Valid Rows template
            // for others is view rows template
            if (IndicatorCategoryHelper.isUserDefMatching(category)) {
                key = "SqlTemplate." + language + ".UDI.Match.ViewValidRows"; //$NON-NLS-1$ //$NON-NLS-2$
            } else if (IndicatorCategoryHelper.isUserDefRealValue(category)) {
                key = "SqlTemplate." + language + ".UDI.RealValue.ViewRows"; //$NON-NLS-1$ //$NON-NLS-2$
            } else if (IndicatorCategoryHelper.isUserDefFrequency(category)) {
                key = "SqlTemplate." + language + ".UDI.Frequency.ViewRows"; //$NON-NLS-1$ //$NON-NLS-2$
            } else if (IndicatorCategoryHelper.isUserDefCount(category)) {
                key = "SqlTemplate." + language + ".UDI.Count.ViewRows"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else if (selectTabNumber == 3) {
            // for match is View Invalid Rows Template
            key = "SqlTemplate." + language + ".UDI.Match.ViewInvalidRows"; //$NON-NLS-1$ //$NON-NLS-2$
        } else if (selectTabNumber == 4) {
            // for match is View Valid Values Template
            key = "SqlTemplate." + language + ".UDI.Match.ViewValidValues"; //$NON-NLS-1$ //$NON-NLS-2$
        } else if (selectTabNumber == 5) {
            // for match is View Invalid Values Template
            key = "SqlTemplate." + language + ".UDI.Match.ViewInvalidValues"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        key = key.replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
        autoGenSql = properties.getProperty(key);
        if (autoGenSql == null) {
            if (StringUtils.equalsIgnoreCase(DbmsLanguage.SQL, language)) {
                log.error(Messages.getString("UDIHelper.NO_DEFAULT_SQL_EXPR_DEFINED")); //$NON-NLS-1$
                return org.talend.dataquality.PluginConstant.EMPTY_STRING;
            }
            // use default SQL expression if the auto generated SQL cannot be find by given language.
            autoGenSql = getQueryFromTemplates(selectTabNumber, DbmsLanguage.SQL, category);
        }
        return autoGenSql;
    }

    /**
     * check all the indicators, convert common udi to a Java UDI if needed.
     * 
     * @param analysis
     */
    public static void updateJUDIsForAnalysis(Analysis analysis) {
        EList<Indicator> allIndics = analysis.getResults().getIndicators();
        List<Indicator> updatedIndWithJUDI = new ArrayList<Indicator>();
        for (Indicator indicator : allIndics) {
            // MOD TDQ-8177 sizhaoliu update only the UDIs
            if (UDIHelper.isUDI(indicator) && UDIHelper.needUpdateJUDI(indicator)) {
                try {
                    indicator = UDIHelper.adaptToJavaUDI(indicator);
                } catch (Throwable e) {
                    ExceptionHandler.process(e);
                }
            }
            updatedIndWithJUDI.add(indicator);
        }
        allIndics.clear();
        allIndics.addAll(updatedIndWithJUDI);
    }

    /**
     * 
     * If oldUDI == null mean that jar is changed so need to update
     * 
     * @param udi
     * @return
     */
    public static boolean needUpdateJUDI(Indicator udi) {
        if (UDIHelper.isJUDIValid(udi.getIndicatorDefinition())) {
            Indicator oldUDI = UDIHelper.getUDIFromMap(udi);
            if (oldUDI == null) {
                return true;
            }
        }
        return false;

    }

    /**
     * 
     * clear special element from JAVAUDIMAP
     * 
     * @param indDef
     */
    public static void clearJAVAUDIMAPByIndicatorDefinition(IndicatorDefinition indDef) {
        if (indDef == null || !isJUDIValid(indDef)) {
            return;
        }
        for (Indicator indicator : JAVAUDIMAP.keySet()) {
            if (indDef.equals(indicator.getIndicatorDefinition())
                    || indDef.equals(JAVAUDIMAP.get(indicator).getIndicatorDefinition())) {
                // if the jar used by UDI is changed need to set null then adaptToJavaUDI() will reload again
                JAVAUDIMAP.put(indicator, null);
            }
        }

    }

    public static void clearJAVAUDIMAP() {
        JAVAUDIMAP.clear();
    }

}
