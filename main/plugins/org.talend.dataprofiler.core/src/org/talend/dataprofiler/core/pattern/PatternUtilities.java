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
package org.talend.dataprofiler.core.pattern;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ColumnIndicator;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.UDIFactory;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.impl.RegularExpressionImpl;
import org.talend.dataquality.factories.PatternIndicatorFactory;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.Expression;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public final class PatternUtilities {

    private static Logger log = Logger.getLogger(PatternUtilities.class);

    private PatternUtilities() {
    }

    /**
     * DOC qzhang Comment method "isPatternValid".
     * 
     * @param pattern
     * @return
     */
    public static boolean isPatternValid(Pattern pattern) {
        boolean valid = false;
        EList<PatternComponent> components = pattern.getComponents();
        for (int i = 0; i < components.size(); i++) {
            RegularExpressionImpl regularExpress = (RegularExpressionImpl) components.get(i);
            String body = regularExpress.getExpression().getBody();
            valid = ((body != null) && body.matches("'.*'")); //$NON-NLS-1$
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    public static TypedReturnCode<IndicatorUnit> createIndicatorUnit(IFile pfile, ModelElementIndicator modelElementIndicator,
            Analysis analysis) {
        return createIndicatorUnit(PatternResourceFileHelper.getInstance().findPattern(pfile), modelElementIndicator, analysis,
                null);
    }

    public static TypedReturnCode<IndicatorUnit> createIndicatorUnit(Pattern pattern,
            ModelElementIndicator modelElementIndicator, Analysis analysis) {
        return createIndicatorUnit(pattern, modelElementIndicator, analysis, null);
    }

    /**
     * DOC xqliu Comment method "createIndicatorUnit".
     * 
     * @param pfile
     * @param modelElementIndicator
     * @param analysis
     * @param indicatorDefinition
     * @return
     */
    public static TypedReturnCode<IndicatorUnit> createIndicatorUnit(Pattern pattern,
            ModelElementIndicator modelElementIndicator, Analysis analysis, IndicatorDefinition indicatorDefinition) {

        TypedReturnCode<IndicatorUnit> result = new TypedReturnCode<IndicatorUnit>();

        for (Indicator indicator : modelElementIndicator.getIndicators()) {
            // MOD xqliu 2009-08-12 bug 7810
            // MOD xwang 2011-08-01 bug TDQ-2730
            if (UDIHelper.getMatchingIndicatorName(indicatorDefinition, pattern).equals(indicator.getName())
                    && indicator instanceof PatternMatchingIndicator) {
                result.setOk(false);
                result.setMessage(DefaultMessagesImpl.getString("PatternUtilities.Selected")); //$NON-NLS-1$
                return result;
            }
            // ~
        }

        // MOD scorreia 2009-01-06: when expression type is not set (version
        // TOP-1.1.x), then it's supposed to be a
        // regexp pattern. This could be false because expression type was not
        // set into SQL pattern neither in TOP-1.1.
        // This means that there could exist the need for a migration task to
        // set the expression type depending on the
        // folder where the pattern is stored. The method
        // DomainHelper.getExpressionType(pattern) tries to find the type
        // of pattern.
        Indicator patternMatchingIndicator = null;

        String expressionType = DomainHelper.getExpressionType(pattern);
        boolean isSQLPattern = (ExpressionType.SQL_LIKE.getLiteral().equals(expressionType));

        if (indicatorDefinition != null) {
            patternMatchingIndicator = UDIFactory.createUserDefIndicator(indicatorDefinition, pattern);
        } else {
            patternMatchingIndicator = isSQLPattern ? PatternIndicatorFactory.createSqlPatternMatchingIndicator(pattern)
                    : PatternIndicatorFactory.createRegexpMatchingIndicator(pattern);
        }
        IEditorPart theEdit = CorePlugin.getDefault().getCurrentActiveEditor();
        if (theEdit != null && theEdit instanceof AnalysisEditor && analysis.getContext().getConnection() == null) {
            theEdit.doSave(null);
        }
        ExecutionLanguage executionLanguage = analysis.getParameters().getExecutionLanguage();
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(analysis);
        if (dbmsLanguage.isSql()) {
            MessageUI.openWarning(DefaultMessagesImpl.getString("PatternUtilities.ConnectionError")); //$NON-NLS-1$
            result.setOk(false);
            return result;
        }
        boolean isJavaEngin = ExecutionLanguage.JAVA.equals(executionLanguage);
        Expression returnExpression = dbmsLanguage.getRegexp(pattern);
        // MOD gdbu 2011-8-26 bug : TDQ-2169
        if ((ExpressionType.REGEXP.getLiteral().equals(expressionType) || ExpressionType.SQL_LIKE.getLiteral().equals(
                expressionType))
                && returnExpression == null) {
            // ~TDQ-2169
            String executeType = isJavaEngin ? executionLanguage.getName() : dbmsLanguage.getDbmsName();
            boolean openPattern = MessageDialog
                    .openQuestion(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            DefaultMessagesImpl.getString("PatternUtilities.Warning"), DefaultMessagesImpl.getString("PatternUtilities.NoExpression", executeType, pattern.getName())); //$NON-NLS-1$ //$NON-NLS-2$
            if (openPattern) {
                RepositoryNode node = RepositoryNodeHelper.recursiveFind(pattern);
                if (RepositoryNodeHelper.canOpenEditor(node)) {
                    new OpenItemEditorAction(new IRepositoryNode[] { node }).run();
                }
            }
            result.setOk(false);
            return result;
        }

        // a regular expression for the analyzed
        // database, but we probably test also whether the analyzed database
        // support the regular expressions (=> check
        // DB type, DB number version, existence of UDF)
        DataManager dm = analysis.getContext().getConnection();
        if (dm != null) {
            TypedReturnCode<java.sql.Connection> trc = JavaSqlFactory.createConnection((Connection) dm);

            // MOD qiongli 2011-1-10 feature 16796
            boolean isDelimitedFileConnection = ConnectionUtils.isDelimitedFileConnection((DataProvider) dm);
            if (trc != null) {
                // SoftwareSystem softwareSystem = DatabaseContentRetriever.getSoftwareSystem(conn);
                // MOD sizhaoliu TDQ-6316
                dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(dm);
            }

            // MOD xqliu 2010-08-12 bug 14601
            if (!(isSQLPattern
                    || DefinitionHandler.getInstance().canRunRegularExpressionMatchingIndicator(dbmsLanguage, isJavaEngin,
                            pattern) || isDelimitedFileConnection)) {
                // MessageDialogWithToggle.openInformation(null,
                //                        DefaultMessagesImpl.getString("PatternUtilities.Pattern"), DefaultMessagesImpl //$NON-NLS-1$
                //                                .getString("PatternUtilities.couldnotSetIndicator")); //$NON-NLS-1$
                result.setOk(false);
                result.setMessage(DefaultMessagesImpl.getString("PatternUtilities.couldnotSetIndicator")); //$NON-NLS-1$

                return result;
            }
            // ~ 14601
        }

        // MOD scorreia 2008-09-18: bug 5131 fixed: set indicator's definition
        // when the indicator is created.
        if (indicatorDefinition == null) {
            if (!DefinitionHandler.getInstance().setDefaultIndicatorDefinition(patternMatchingIndicator)) {
                log.error(DefaultMessagesImpl.getString("PatternUtilities.SetFailed", patternMatchingIndicator.getName())); //$NON-NLS-1$
            }
        } else {
            patternMatchingIndicator.setIndicatorDefinition(indicatorDefinition);
        }

        IndicatorEnum type = IndicatorEnum.findIndicatorEnum(patternMatchingIndicator.eClass());
        IndicatorUnit addIndicatorUnit = modelElementIndicator.addSpecialIndicator(type, patternMatchingIndicator);
        DependenciesHandler.getInstance().setUsageDependencyOn(analysis, pattern);
        result.setOk(true);
        result.setMessage(DefaultMessagesImpl.getString("PatternUtilities.OK")); //$NON-NLS-1$
        result.setObject(addIndicatorUnit);
        return result;
    }

    /**
     * DOC bzhou Comment method "isDBDefinedUDF".
     * 
     * This method is to check if user have defined the related funciton to this database type.
     * 
     * @param dbmsLanguage
     * @return
     * @deprecated
     */
    // private static boolean isDBDefinedUDF(DbmsLanguage dbmsLanguage) {
    // Preferences prefers = ResourcesPlugin.getPlugin().getPluginPreferences();
    // if (prefers != null) {
    // String udfValue = prefers.getString(dbmsLanguage.getDbmsName());
    //            if (udfValue != null && !"".equals(udfValue)) { //$NON-NLS-1$
    // return true;
    // }
    // }
    // return false;
    // }

    @Deprecated
    public static Set<String> getAllPatternNames(IFolder folder) {

        Set<String> list = new HashSet<String>();
        return getNestFolderPatternNames(list, folder);
    }

    /**
     * DOC zqin Comment method "getNestFolderPatternNames".
     * 
     * @param folder
     * @return
     */
    public static Set<String> getNestFolderPatternNames(Set<String> list, IFolder folder) {
        try {
            for (IResource resource : folder.members()) {
                if (resource instanceof IFile) {
                    Pattern fr = PatternResourceFileHelper.getInstance().findPattern((IFile) resource);
                    if (fr != null) {
                        list.add(fr.getName());
                    }
                } else {
                    getNestFolderPatternNames(list, (IFolder) resource);
                }
            }
        } catch (CoreException e) {
            log.error(e, e);
        }
        return list;
    }

    /**
     * @param clmIndicator
     * @return
     * @deprecated since repository nodes are used instead of IFiles
     */
    @Deprecated
    public static IFile[] getPatternFileByIndicator(ColumnIndicator clmIndicator) {
        Indicator[] patternIndicators = clmIndicator.getPatternIndicators();
        List<IFile> existedPatternFiles = new ArrayList<IFile>();

        if (patternIndicators.length != 0) {
            for (Indicator patternIndicator : patternIndicators) {
                PatternMatchingIndicator ptnIndicaotr = (PatternMatchingIndicator) patternIndicator;
                List<Pattern> patterns = ptnIndicaotr.getParameters().getDataValidDomain().getPatterns();
                for (Pattern pattern : patterns) {
                    for (IFile file : getAllPatternFiles()) {
                        Pattern fpattern = PatternResourceFileHelper.getInstance().findPattern(file);
                        if (pattern.getName().equals(fpattern.getName())) {
                            existedPatternFiles.add(file);
                        }
                    }
                }
            }
        }

        return existedPatternFiles.toArray(new IFile[existedPatternFiles.size()]);
    }

    /**
     * get the repository nodes corresponding to the indicator.
     * 
     * @param meIndicator
     * @return
     */
    public static Object[] getPatternRepNodesByIndicator(ModelElementIndicator meIndicator) {
        List<IRepositoryNode> patternRepNodes = RepositoryNodeHelper.getPatternsRepositoryNodes(false);
        ArrayList<Object> ret = new ArrayList<Object>();
        for (Indicator indicator : meIndicator.getPatternIndicators()) {
            Pattern patternInAnalysis = IndicatorHelper.getPattern(indicator);
            for (IRepositoryNode patternRepNode : patternRepNodes) {
                Pattern pattern = ((PatternRepNode) patternRepNode).getPattern();
                if (StringUtils.equals(ResourceHelper.getUUID(patternInAnalysis), ResourceHelper.getUUID(pattern))) {
                    ret.add(patternRepNode);
                }
            }
        }
        return ret.toArray();
    }

    private static Set<IFile> getNestedPatternFiles(Set<IFile> list, IFolder folder) {
        try {
            for (IResource resource : folder.members()) {
                if (resource instanceof IFile) {
                    IFile file = (IFile) resource;
                    if (FactoriesUtil.PATTERN.equals(file.getFileExtension())) {
                        list.add((IFile) resource);
                    }
                } else {
                    getNestedPatternFiles(list, (IFolder) resource);
                }
            }
        } catch (CoreException e) {
            log.error(e, e);
        }

        return list;
    }

    private static List<IFile> getAllPatternFiles() {
        List<IFile> patternFiles = new ArrayList<IFile>();

        IFolder pfolder = ResourceManager.getPatternFolder();
        IFolder sfolder = ResourceManager.getPatternSQLFolder();

        Set<IFile> list = new HashSet<IFile>();
        patternFiles.addAll(getNestedPatternFiles(list, pfolder));
        patternFiles.addAll(getNestedPatternFiles(list, sfolder));

        return patternFiles;
    }

    /**
     * create CheckedTreeSelectionDialog for patterns.
     * 
     * @param node Pattern root RepositoryNode.
     * @return
     */
    public static CheckedTreeSelectionDialog createPatternCheckedTreeSelectionDialog(IRepositoryNode node) {
        CheckedTreeSelectionDialog dialog = new CheckedTreeSelectionDialog(null, new DQRepositoryViewLabelProvider(),
                new ResourceViewContentProvider());
        dialog.setInput(node);
        dialog.setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                for (Object patte : selection) {
                    if (patte instanceof PatternRepNode) {
                        PatternRepNode patternNode = (PatternRepNode) patte;
                        Pattern findPattern = patternNode.getPattern();
                        boolean validStatus = TaggedValueHelper.getValidStatus(findPattern);
                        if (!validStatus) {
                            return new Status(IStatus.ERROR, CorePlugin.PLUGIN_ID, DefaultMessagesImpl
                                    .getString("AnalysisColumnTreeViewer.chooseValidPatterns")); //$NON-NLS-1$
                        }
                    }
                }
                return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, IStatus.OK, "", //$NON-NLS-1$
                        null);
            }

        });
        dialog.setContainerMode(true);
        dialog.setTitle(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.patternSelector")); //$NON-NLS-1$
        dialog.setMessage(DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.patterns")); //$NON-NLS-1$
        dialog.setSize(80, 30);
        return dialog;
    }
}

/**
 * DOC zqin AnalysisColumnTreeViewer class global comment. Detailled comment
 * 
 * @deprecated use DQRepositoryViewLabelProvider instead of
 */
@Deprecated
class PatternLabelProvider extends LabelProvider {

    @Override
    public Image getImage(Object element) {
        if (element instanceof IFolder) {
            return ImageLib.getImage(ImageLib.FOLDERNODE_IMAGE);
        }

        if (element instanceof IFile) {
            Pattern findPattern = PatternResourceFileHelper.getInstance().findPattern((IFile) element);
            boolean validStatus = TaggedValueHelper.getValidStatus(findPattern);
            ImageDescriptor imageDescriptor = ImageLib.getImageDescriptor(ImageLib.PATTERN_REG);
            if (!validStatus) {
                ImageDescriptor warnImg = PlatformUI.getWorkbench().getSharedImages()
                        .getImageDescriptor(ISharedImages.IMG_OBJS_WARN_TSK);
                DecorationOverlayIcon icon = new DecorationOverlayIcon(imageDescriptor.createImage(), warnImg,
                        IDecoration.BOTTOM_RIGHT);
                imageDescriptor = icon;
            }
            return imageDescriptor.createImage();
        }

        return null;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof IFile) {
            IFile file = (IFile) element;
            Pattern pattern = PatternResourceFileHelper.getInstance().findPattern(file);
            if (pattern != null) {
                return pattern.getName();
            }
        }

        if (element instanceof IFolder) {
            return ((IFolder) element).getName();
        }

        return ""; //$NON-NLS-1$
    }
}
