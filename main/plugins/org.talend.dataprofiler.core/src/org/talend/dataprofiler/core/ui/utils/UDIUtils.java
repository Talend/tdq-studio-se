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
package org.talend.dataprofiler.core.ui.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.Item;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction;
import org.talend.dataprofiler.core.ui.dialog.JavaUdiJarSelectDialog;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.filters.FolderObjFilter;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.JavaUDIIndicatorParameter;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.helpers.IndicatorCategoryHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorParameters;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorDefinitionParameter;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.dbms.GenericSQLHandler;
import org.talend.dq.helper.CustomAttributeMatcherHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.dq.nodes.UserDefIndicatorSubFolderRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.dates.DateUtils;
import org.talend.utils.sugars.ReturnCode;

import orgomg.cwm.objectmodel.core.Expression;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class UDIUtils {

    public static final String JAREXTENSIONG = "jar"; //$NON-NLS-1$

    static Logger log = Logger.getLogger(UDIUtils.class);

    private UDIUtils() {
    }

    /**
     * get Current DateTime
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return DateUtils.getCurrentDate(DateUtils.PATTERN_5);
    }

    /**
     * check the TdExpression is Language And Version.
     *
     * @param tdExp
     * @param language
     * @param version
     * @return
     */
    public static boolean isCurrentLanguageAndVersion(TdExpression tdExp, String language, String version) {
        return tdExp.getLanguage().equals(language)
                && (StringUtils.isBlank(tdExp.getVersion()) && StringUtils.isBlank(version) || StringUtils.equals(
                        tdExp.getVersion(), version));
    }

    /**
     * check if Exist In List.
     *
     * @param list
     * @param language
     * @param version
     * @return
     */
    public static boolean checkExistInList(EList<TdExpression> list, String language, String version) {
        boolean isExist = false;
        for (TdExpression temp : list) {
            if (isCurrentLanguageAndVersion(temp, language, version)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * create New TdExpression.
     *
     * @param language
     * @param version
     * @param body
     * @return
     */
    public static TdExpression createNewTdExpression(String language, String version, String body) {
        TdExpression newTdExp = BooleanExpressionHelper.createTdExpression(language, body, version);
        newTdExp.setModificationDate(getCurrentDateTime());
        return newTdExp;
    }

    /**
     * create Default Drill Down List for UDI.
     *
     * @param indiDefinition
     * @return
     */
    public static UDIndicatorDefinition createDefaultDrillDownList(UDIndicatorDefinition indiDefinition) {
        IndicatorCategory category = IndicatorCategoryHelper.getCategory(indiDefinition);
        if (IndicatorCategoryHelper.isUserDefMatching(category)) {
            // set default value from templates
            EList<TdExpression> viewValidRowsList = indiDefinition.getViewValidRowsExpression();
            EList<TdExpression> viewInvalidRowsList = indiDefinition.getViewInvalidRowsExpression();
            EList<TdExpression> viewValidValuesList = indiDefinition.getViewValidValuesExpression();
            EList<TdExpression> viewInvalidValuesList = indiDefinition.getViewInvalidValuesExpression();

            EList<TdExpression> sqlGenericExpression = indiDefinition.getSqlGenericExpression();
            if (sqlGenericExpression != null) {
                for (TdExpression tdExp : sqlGenericExpression) {
                    String language = tdExp.getLanguage();
                    String version = tdExp.getVersion();

                    // if not exist, add one.(when do migration more than one time, will add one)
                    if (!UDIUtils.checkExistInList(viewValidRowsList, language, version)) {
                        // for match is View Valid Rows template
                        String body = UDIHelper.getQueryFromTemplates(2, language, category);
                        viewValidRowsList.add(UDIUtils.createNewTdExpression(language, version, replaceQueryForMatchUDI(body)));
                    }

                    if (!UDIUtils.checkExistInList(viewInvalidRowsList, language, version)) {
                        // for match is View Invalid Rows Template
                        String body = UDIHelper.getQueryFromTemplates(3, language, category);
                        viewInvalidRowsList.add(UDIUtils.createNewTdExpression(language, version, replaceQueryForMatchUDI(body)));
                    }

                    if (!UDIUtils.checkExistInList(viewValidValuesList, language, version)) {
                        // for match is View Valid Values Template
                        String body = UDIHelper.getQueryFromTemplates(4, language, category);
                        viewValidValuesList.add(UDIUtils.createNewTdExpression(language, version, replaceQueryForMatchUDI(body)));

                    }

                    if (!UDIUtils.checkExistInList(viewInvalidValuesList, language, version)) {
                        // for match is View Invalid Values Template
                        String body = UDIHelper.getQueryFromTemplates(5, language, category);
                        viewInvalidValuesList.add(UDIUtils
                                .createNewTdExpression(language, version, replaceQueryForMatchUDI(body)));
                    }
                }
            }
        } else {
            // for others is view rows template
            EList<TdExpression> viewRowsList = indiDefinition.getViewRowsExpression();
            EList<TdExpression> sqlGenericExpression = indiDefinition.getSqlGenericExpression();
            if (sqlGenericExpression != null) {
                for (TdExpression tdExp : sqlGenericExpression) {
                    String language = tdExp.getLanguage();
                    String version = tdExp.getVersion();

                    // if not exist, add one.(when do migration more than one time, will add one)
                    if (!UDIUtils.checkExistInList(viewRowsList, language, version)) {
                        String body = UDIHelper.getQueryFromTemplates(2, language, category);

                        GenericSQLHandler genericSQLHandler = new GenericSQLHandler(body);
                        if (IndicatorCategoryHelper.isUserDefRealValue(category)) {
                            // replace <COLUMN_EXPRESSION_TEXT_FIELD>
                            genericSQLHandler.replaceUDIColumn(PluginConstant.EMPTY_STRING);

                        } else if (IndicatorCategoryHelper.isUserDefFrequency(category)) {
                            // replace <FIRST_COLUMN_EXPRESSION_TEXT_FIELD>
                            // replace <SECOND_COLUMN_EXPRESSION_TEXT_FIELD>
                            genericSQLHandler.replaceUDIFirstColumn(PluginConstant.EMPTY_STRING).replaceUDISecondColumn(
                                    PluginConstant.EMPTY_STRING);
                        }
                        viewRowsList.add(UDIUtils.createNewTdExpression(language, version, genericSQLHandler
                                .replaceUDIQueryToMatch().getSqlString()));
                    }
                }
            }

        }

        return indiDefinition;
    }

    /**
     * replace Query For Match UDI.
     *
     * @param genericSQLHandler
     * @return
     */
    private static String replaceQueryForMatchUDI(String body) {
        GenericSQLHandler genericSQLHandler = new GenericSQLHandler(body);
        // replace <MATCHING_EXPRESSION_TEXT_FIELD>
        genericSQLHandler.replaceUDIMatching(PluginConstant.EMPTY_STRING);
        // replace <WHERE_TEXT_FIELD>
        genericSQLHandler.replaceUDIWhere(PluginConstant.EMPTY_STRING);

        return genericSQLHandler.replaceUDIQueryToMatch().getSqlString();
    }

    public static IndicatorCategory getUDICategory(IndicatorUnit unit) {
        return UDIHelper.getUDICategory(unit.getIndicator().getIndicatorDefinition());
    }

    public static String getUDICategoryLabel(IndicatorUnit unit) {
        IndicatorCategory ic = getUDICategory(unit);
        if (ic != null) {
            return ic.getLabel();
        }
        return null;
    }

    public static IndicatorUnit[] createIndicatorUnit(IndicatorDefinition udid, ModelElementIndicator meIndicator,
            Analysis analysis) throws Throwable {
        List<IndicatorUnit> addIndicatorUnits = new ArrayList<IndicatorUnit>();

        // can't add the same user defined indicator
        for (Indicator indicator : meIndicator.getIndicators()) {
            // MOD xwang 2011-08-01 bug TDQ-2730
            if (indicator instanceof UserDefIndicator) {
                if (udid.getName().equals(indicator.getName())) {
                    // TDQ-13646 msjian : check if they are also in the same project.
                    DQRepositoryNode udidNode = RepositoryNodeHelper.recursiveFind(udid);
                    DQRepositoryNode indicatorNode =
                            RepositoryNodeHelper.recursiveFind(indicator.getIndicatorDefinition());
                    if (RepositoryNodeHelper.isSameProject(udidNode.getProject(), indicatorNode.getProject())) {
                        MessageUI
                                .openWarning(DefaultMessagesImpl
                                        .getString("UDIUtils.UDISelected", udidNode.getDisplayTextWithProjectName())); //$NON-NLS-1$
                        return null;
                    }
                }
            }
        }

        Indicator udi = UDIFactory.createUserDefIndicator(udid);
        udi.setIndicatorDefinition(udid);
        // MOD mzhao feature 11128, Handle Java User Defined Indicator.
        Indicator judi = UDIHelper.adaptToJavaUDI(udi);
        if (judi != null) {
            udi = judi;
        }
        IEditorPart activeEditor = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();
        if (activeEditor == null || !(activeEditor instanceof AnalysisEditor)) {
            return null;
        }
        ExecutionLanguage executionLanguage = ((AnalysisEditor) activeEditor).getUIExecuteEngin();
        boolean isJavaEngin = ExecutionLanguage.JAVA.equals(executionLanguage);
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(analysis, executionLanguage);
        Expression returnExpression = dbmsLanguage.getExpression(udi);
        String executeType = isJavaEngin ? executionLanguage.getName() : dbmsLanguage.getDbmsName();
        // MOD qiongli 2013.5.22 TDQ-7282.if don't find a valid java expression for JUDI,should also pop this dialog.
        boolean finddExpression = true;
        if (isJavaEngin && judi == null || !isJavaEngin && returnExpression == null) {
            finddExpression = false;
        }
        if (!finddExpression) {
            // open the editor
            boolean openUDI = MessageDialog
                    .openQuestion(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            DefaultMessagesImpl.getString("PatternUtilities.Warning"), DefaultMessagesImpl.getString("UDIUtils.NoExpression", executeType)); //$NON-NLS-1$ //$NON-NLS-2$
            if (openUDI) {
                RepositoryNode node = RepositoryNodeHelper.recursiveFind(udid);
                if (RepositoryNodeHelper.canOpenEditor(node)) {
                    new OpenItemEditorAction(new IRepositoryNode[] { node }).run();
                }
            }
            return null;
        }
        // dbmsLanguage
        IndicatorParameters parameters = udi.getParameters();
        if (parameters == null) {
            parameters = IndicatorsFactory.eINSTANCE.createIndicatorParameters();
            udi.setParameters(parameters);
        }
        Domain indicatorValidDomain = parameters.getIndicatorValidDomain();
        if (indicatorValidDomain == null) {
            indicatorValidDomain = DomainHelper.createDomain("JAVA_UDI_PARAMETERS"); //$NON-NLS-1$
            parameters.setIndicatorValidDomain(indicatorValidDomain);
        }
        List<IndicatorDefinitionParameter> indicatorDefs = udid.getIndicatorDefinitionParameter();
        for (IndicatorDefinitionParameter idp : indicatorDefs) {
            JavaUDIIndicatorParameter judip = DomainHelper.createJavaUDIIndicatorParameter(idp.getKey(), idp.getValue());
            indicatorValidDomain.getJavaUDIIndicatorParameter().add(judip);
        }

        IndicatorEnum indicatorType = IndicatorEnum.findIndicatorEnum(udi.eClass());

        addIndicatorUnits.add(meIndicator.addSpecialIndicator(indicatorType, udi));

        DependenciesHandler.getInstance().setUsageDependencyOn(analysis, udid);
        return addIndicatorUnits.toArray(new IndicatorUnit[addIndicatorUnits.size()]);
    }

    /**
     * DOC xqliu Comment method "createUdiCheckedTreeSelectionDialog".
     *
     * @param meIndicator
     * @return
     */
    public static CheckedTreeSelectionDialog createUdiCheckedTreeSelectionDialog(ModelElementIndicator meIndicator) {
        CheckedTreeSelectionDialog dialog = new CheckedTreeSelectionDialog(null, new DQRepositoryViewLabelProvider(),
                new ResourceViewContentProvider());
        dialog.addFilter(new FolderObjFilter());

        DQRepositoryNode udiDialogInputData = AnalysisUtils.getSelectDialogInputData(EResourceConstant.USER_DEFINED_INDICATORS);
        dialog.setInput(udiDialogInputData);

        dialog.setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                for (Object patte : selection) {
                    if (patte instanceof SysIndicatorDefinitionRepNode) {
                        SysIndicatorDefinitionRepNode udiNode = (SysIndicatorDefinitionRepNode) patte;
                        IndicatorDefinition indicatorDefinition = udiNode.getIndicatorDefinition();
                        boolean validStatus = TaggedValueHelper.getValidStatus(indicatorDefinition);
                        if (!validStatus) {
                            return new Status(IStatus.ERROR, CorePlugin.PLUGIN_ID, DefaultMessagesImpl
                                    .getString("AnalysisColumnTreeViewer.chooseValidUdis")); //$NON-NLS-1$
                        }
                    }
                }
                return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, IStatus.OK, "", //$NON-NLS-1$
                        null);
            }

        });

        dialog.setContainerMode(true);
        dialog.setInitialSelections(getUDIFilesByIndicator(udiDialogInputData, meIndicator));
        dialog.setTitle(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.udiSelector")); //$NON-NLS-1$
        dialog.setMessage(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.udis")); //$NON-NLS-1$
        dialog.setSize(80, 30);
        return dialog;
    }

    /**
     * get the repository nodes corresponding to the indicator.
     *
     * @param meIndicator
     * @return
     */
    private static Object[] getUDIFilesByIndicator(DQRepositoryNode udiDialogInputData, ModelElementIndicator meIndicator) {
        ArrayList<Object> ret = new ArrayList<Object>();
        for (Indicator indicator : meIndicator.getIndicators()) {
            if (indicator instanceof UserDefIndicator) {
                UserDefIndicator selectedIndicator = (UserDefIndicator) indicator;

                for (IRepositoryNode udiFolderNode : udiDialogInputData.getChildren()) {

                    for (IRepositoryNode udiNode : udiFolderNode.getChildren()) {
                        if (udiNode instanceof SysIndicatorDefinitionRepNode) {

                            if (selectedIndicator.getName().equals(
                                    ((SysIndicatorDefinitionRepNode) udiNode).getIndicatorDefinition().getName())) {
                                ret.add(udiNode);
                            }
                        } else if (udiNode instanceof UserDefIndicatorSubFolderRepNode) {
                            for (IRepositoryNode udiNode2 : udiNode.getChildren()) {
                                if (udiNode2 instanceof SysIndicatorDefinitionRepNode) {

                                    if (selectedIndicator.getName().equals(
                                            ((SysIndicatorDefinitionRepNode) udiNode2).getIndicatorDefinition().getName())) {
                                        ret.add(udiNode2);
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
        return ret.toArray();
    }

    /**
     *
     * zshen Comment method "createUdiJarCheckedTreeSelectionDialog".
     *
     * @param udiJarProject
     * @return
     */
    // MOD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used already.
    public static JavaUdiJarSelectDialog createUdiJarCheckedTreeSelectionDialog(IndicatorDefinition definition,
            IFolder udiJarProject, String[] selectionPath) {
        JavaUdiJarSelectDialog dialog = new JavaUdiJarSelectDialog(definition, null, new UdiLabelProvider(),
                new UdiJarContentProvider());
        dialog.setCheckValue(convertToString(selectionPath));
        dialog.setInput(udiJarProject);
        dialog.setCheckedElements(selectionPath);
        dialog.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof File) {
                    File file = (File) element;
                    if (JAREXTENSIONG.equals(new Path(file.getName()).getFileExtension())) {
                        return true;
                    }
                }
                return false;
            }
        });
        dialog.setContainerMode(true);
        dialog.setTitle(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.udiSelector")); //$NON-NLS-1$
        dialog.setMessage(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.udis")); //$NON-NLS-1$
        dialog.setSize(80, 30);
        return dialog;
    }

    /**
     * convert the string array to String({a,b,c}==>a||b||c)
     *
     * @param selectionPath
     * @return
     */
    private static String convertToString(String[] selectionPath) {
        String result = StringUtils.EMPTY;
        if (selectionPath == null || selectionPath.length <= 0) {
            return result;
        }
        result = selectionPath[0];
        for (int index = 1; index < selectionPath.length; index++) {
            result += CustomAttributeMatcherHelper.SEPARATOR + selectionPath[index];
        }
        return result;
    }

    /**
     *
     * zshen Comment method "getLibJarFileList".
     *
     * @return
     */
    public static List<IFile> getLibJarFileList() {
        List<IFile> fileList = new ArrayList<IFile>();
        File newFile = ResourceManager.getUDIJarFolder().getLocation().toFile();
        if (!newFile.exists()) {
            newFile.mkdir();
        }
        List<File> libJarFileList = getLibJarFileList(newFile);

        for (File jarFile : libJarFileList) {
            IFile ifile = WorkspaceUtils.fileToIFile(jarFile);
            if (ifile.exists()) {
                fileList.add(ifile);
            }
        }

        return fileList;
    }

    public static List<File> getLibJarFileList(File udiJarFolder) {
        List<File> fileList = new ArrayList<File>();

        if (udiJarFolder.isDirectory()) {

            for (File jarFile : udiJarFolder.listFiles()) {
                if (jarFile.isFile() && jarFile.getName().endsWith(JAREXTENSIONG)) {
                    fileList.add(jarFile);
                }
            }
        }

        return fileList;
    }

    /**
     *
     * zshen Comment method "getContainJarFile".
     *
     * @param jarPathStr
     * @return
     */
    public static List<IFile> getContainJarFile(String jarPathStr) {
        List<IFile> fileList = new ArrayList<IFile>();

        for (String containJarName : jarPathStr.split("\\|\\|")) { //$NON-NLS-1$
            for (IFile libJarFile : getLibJarFileList()) {
                if (libJarFile.getName().equalsIgnoreCase(containJarName)) {
                    fileList.add(libJarFile);
                    break;
                }
            }
        }
        return fileList;
    }

    public static ReturnCode checkUDIDependency(IndicatorDefinition definition, File delFile) {
        ReturnCode result = new ReturnCode(true);
        IPath filePath = new Path(delFile.getPath());
        if (!ResourceManager.getUDIJarFolder().getLocation().isPrefixOf(filePath)) {
            // filePath.makeRelativeTo(ResourceManager.getRootFolderLocation()))) {
            return result;
        }
        // for (IndicatorDefinition file : IndicatorResourceFileHelper.getInstance().getAllUDIs()) {
        // EMFSharedResources.getInstance().reloadResource(file.eResource().getURI());
        // }
        // IndicatorResourceFileHelper.getInstance().clear();

        for (IRepositoryNode indiDefNode : RepositoryNodeHelper.getUdisRepositoryNodes(true)) {
            IndicatorDefinition indiDef = null;
            Item item = indiDefNode.getObject().getProperty().getItem();
            if (item instanceof TDQIndicatorDefinitionItem) {
                indiDef = ((TDQIndicatorDefinitionItem) item).getIndicatorDefinition();
            } else {
                continue;
            }

            // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used already.
            // when it is itself, don't use this check.
            if (indiDef == definition) {
                continue;
            }
            // ADD end

            TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.JAR_FILE_PATH, indiDef.getTaggedValue());
            if (tv == null) {
                continue;
            }
            String[] strArray = tv.getValue().split("\\|\\|"); //$NON-NLS-1$
            int index = Arrays.binarySearch(strArray, filePath.lastSegment());
            if (index >= 0) {
                result.setMessage("The jar file(" + strArray[index] + ") has in use by UDI for " + indiDef.getName());//$NON-NLS-1$ //$NON-NLS-2$
                result.setOk(false);
                return result;
            }
        }
        return result;
    }
}
