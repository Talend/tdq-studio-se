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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.FolderHelper;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.ComparisonLevelFactory;
import org.talend.cwm.compare.factory.comparisonlevel.FileMetadataTableComparisonLevel;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction;
import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.report.ReportItemEditorInput;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisContext;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.localprovider.model.LocalFolderHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;

import orgomg.cwm.foundation.softwaredeployment.DataProvider;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public final class WorkbenchUtils {

    private static Logger log = Logger.getLogger(WorkbenchUtils.class);

    private WorkbenchUtils() {
    }

    /**
     * DOC bZhou Comment method "changePerspective".
     *
     * @param perspectiveID
     */
    public static void changePerspective(final String perspectiveID) {
        // TDQ-11403 make it is suspended until the runnable complete.
        Display.getCurrent().syncExec(new Runnable() {

            public void run() {
                IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                try {
                    PlatformUI.getWorkbench().showPerspective(perspectiveID, activeWindow);
                } catch (WorkbenchException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    public static IFolder folder2IFolder(Folder folder) {
        IProject rootProject = ResourceManager.getRootProject();
        return rootProject.getFolder(folder.getPath());
    }

    public static IPath getPath(RepositoryViewObject viewObject) {
        return RepositoryNodeHelper.getPath(viewObject.getRepositoryNode());
    }

    public static IFolder getFolder(RepositoryNode node) {
        // MOD qiongli 2011-1-18 if it is recyclebin,return the root folder
        IPath path = RepositoryNodeHelper.getPath(node);
        if (path.toString().equals(PluginConstant.EMPTY_STRING)) {
            return ResourceManager.getRootProject().getFolder(ResourceManager.getRootFolderLocation());
        }
        return ResourceManager.getRootProject().getFolder(RepositoryNodeHelper.getPath(node));
    }

    public static IFolder getFolder(RepositoryViewObject viewObject) {
        return getFolder((RepositoryNode) viewObject.getRepositoryNode());
    }

    public static String getItemExtendtion(int classID) {
        String fileExtension = FileConstants.ITEM_EXTENSION;
        switch (classID) {

        case org.talend.dataquality.properties.PropertiesPackage.TDQ_ANALYSIS_ITEM:
            fileExtension = FileConstants.ANA_EXTENSION;
            break;
        case org.talend.dataquality.properties.PropertiesPackage.TDQ_REPORT_ITEM:
            fileExtension = FileConstants.REP_EXTENSION;
            break;
        case org.talend.dataquality.properties.PropertiesPackage.TDQ_INDICATOR_DEFINITION_ITEM:
            fileExtension = FileConstants.DEF_EXTENSION;
            break;
        case org.talend.dataquality.properties.PropertiesPackage.TDQ_PATTERN_ITEM:
            fileExtension = FileConstants.PAT_EXTENSION;
            break;
        case org.talend.dataquality.properties.PropertiesPackage.TDQ_BUSINESS_RULE_ITEM:
            fileExtension = FileConstants.RULE_EXTENSION;

            break;
        case org.talend.dataquality.properties.PropertiesPackage.TDQ_JRXML_ITEM:
            fileExtension = FileConstants.JRXML_EXTENSION;
            break;
        case org.talend.dataquality.properties.PropertiesPackage.TDQ_SOURCE_FILE_ITEM:
            fileExtension = FileConstants.SQL_EXTENSION;
            break;
        }
        return fileExtension;

    }

    public static IPath getFilePath(IRepositoryNode node) {
        Item item = node.getObject().getProperty().getItem();
        IPath folderPath = RepositoryNodeHelper.getPath(node);
        String name = node.getObject()
                .getProperty()
                .getLabel()
                .concat("_") //$NON-NLS-1$
                .concat(node.getObject().getProperty().getVersion())
                .concat(".") //$NON-NLS-1$
                .concat(WorkbenchUtils.getItemExtendtion(item != null ? item.eClass().getClassifierID() : node.getObject()
                        .getProperty().getItem().eClass().getClassifierID()));
        IPath append = folderPath.append(new Path(name));
        return append;
    }

    /**
     *
     * if it is TDQ_Data Profiling,TDQ_Libraries or metadata.
     *
     * @param folderItem
     * @return
     */
    public static boolean isTDQOrMetadataRootFolder(FolderItem folderItem, org.talend.core.model.properties.Project newProject) {
        FolderHelper folderHelper = LocalFolderHelper.createInstance(newProject, ProxyRepositoryFactory.getInstance()
                .getRepositoryContext().getUser());
        String path = folderHelper.getFullFolderPath(folderItem);
        if (path != null
                && (path.startsWith(RepositoryNodeHelper.PREFIX_TDQ) || path.startsWith(EResourceConstant.METADATA.getName()))) {
            return true;
        }
        return false;
    }

    /**
     *
     * Add qiongli: get the detail ERepositoryObjectType of folderItem.
     *
     * @param folderItem
     * @return
     */
    public static ERepositoryObjectType getFolderContentType(FolderItem folderItem) {
        if (!folderItem.getType().equals(FolderType.SYSTEM_FOLDER_LITERAL)) {
            if (!(folderItem.getParent() instanceof FolderItem)) {
                return null; // appears only for a folder for expression builder !
            }
            return getFolderContentType((FolderItem) folderItem.getParent());
        }
        for (ERepositoryObjectType objectType : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            String folderName;
            try {
                folderName = ERepositoryObjectType.getFolderName(objectType);
            } catch (Exception e) {
                // just catch exception to avoid the types who don't have folders
                continue;
            }
            if (folderName.contains("/")) { //$NON-NLS-1$
                String[] folders = folderName.split("/"); //$NON-NLS-1$
                FolderItem currentFolderItem = folderItem;
                boolean found = true;
                for (int i = folders.length - 1; i >= 0; i--) {
                    if (!currentFolderItem.getProperty().getLabel().equals(folders[i])) {
                        found = false;
                        break;
                    }
                    if (i > 0) {
                        if (!(currentFolderItem.getParent() instanceof FolderItem)) {
                            found = false;
                            break;
                        }
                        currentFolderItem = (FolderItem) currentFolderItem.getParent();
                    }
                }
                if (found) {
                    return objectType;
                }
            } else {
                if (folderName.equals(folderItem.getProperty().getLabel())) {
                    return objectType;
                }
            }
        }
        if (folderItem.getParent() instanceof FolderItem) {
            return getFolderContentType((FolderItem) folderItem.getParent());
        }
        return null;
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toUpperCase().indexOf("LINUX") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().indexOf("WIN") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isWinXP() {
        return System.getProperty("os.name").toUpperCase().indexOf("WINDOWS XP") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isWin7() {
        return System.getProperty("os.name").toUpperCase().indexOf("WINDOWS 7") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static boolean isMac() {
        return System.getProperty("os.name").toUpperCase().indexOf("MAC") > -1; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * judgement one string equals another string dependency on the OS's case sensitive type.
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalsOS(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        } else {
            if (isLinux() || isMac()) {
                return str1.equals(str2);
            } else if (isWindows()) {
                return str1.equalsIgnoreCase(str2);
            } else {
                // other os
                return str1.equals(str2);
            }
        }
    }

    /**
     *
     * Refresh the analysis and Connection which is openning
     */
    @Deprecated
    public static void refreshCurrentAnalysisAndConnectionEditor() {
        List<IEditorReference> iEditorReference = getIEditorReference(AnalysisEditor.class.getName(), StringUtils.EMPTY);
        iEditorReference.addAll(getIEditorReference(MatchAnalysisEditor.class.getName(), StringUtils.EMPTY));
        closeAndOpenEditor(iEditorReference);
    }

    /**
     * close and open the editors same method {@link CorePlugin}.getDefault().itemIsOpening() MOD TDQ-8360 20140410
     * yyin: will only operate the analysis who is related and has opened (by its observer --added when opening)
     *
     * @param iEditorReference
     */
    private static void closeAndOpenEditor(List<IEditorReference> iEditorReference) {
        // Refresh current opened editors.
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IWorkbenchPartReference activePartReference = activePage.getActivePartReference();
        // MOD qiongli 2011-9-8 TDQ-3317.when focucs on DI perspective,don't refresh the open editors
        DQRespositoryView findView = (DQRespositoryView) activePage.findView(DQRespositoryView.ID);
        if (findView == null) {
            return;
        }
        if (iEditorReference.size() > 0) {
            try {
                for (IEditorReference editorRef : iEditorReference) {
                    IEditorInput editorInput = editorRef.getEditorInput();
                    if (editorInput instanceof AbstractItemEditorInput) {
                        AbstractItemEditorInput anaItemEditorInput = (AbstractItemEditorInput) editorInput;
                        // close the editor
                        activePage.closeEditor(editorRef.getEditor(false), false);
                        // reopen the analysis
                        new OpenItemEditorAction(new IRepositoryNode[] { anaItemEditorInput.getRepNode() }).run();
                    }
                }
            } catch (PartInitException e) {
                log.error(e);
            }
        }
        activePage.activate(activePartReference.getPart(false));
    }

    /**
     *
     * Refresh the analysis which is openning
     *
     * @param String analysisName: can be StringUtils.EMPTY
     */
    public static void refreshCurrentAnalysisEditor(String analysisName) {
        List<IEditorReference> iEditorReference = getIEditorReference(AnalysisEditor.class.getName(), analysisName);
        // update match analysis if any opened:TDQ-8267 added by yyin
        iEditorReference.addAll(getIEditorReference(MatchAnalysisEditor.class.getName(), analysisName));

        closeAndOpenEditor(iEditorReference);
    }

    public static void refreshCurrentReportEditor(String reportName) {
        List<IEditorReference> iEditorReference = getReportIEditorReference(reportName);
        closeAndOpenEditor(iEditorReference);
    }

    private static List<IEditorReference> getReportIEditorReference(String reportName) {
        List<IEditorReference> returnCode = new ArrayList<IEditorReference>();
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editors = activePage.getEditorReferences();
        if (editors != null) {
            for (IEditorReference editorRef : editors) {
                IEditorInput editorInput;
                try {
                    editorInput = editorRef.getEditorInput();
                    if (editorInput instanceof ReportItemEditorInput) {
                        ReportItemEditorInput repItemEditorInput = (ReportItemEditorInput) editorInput;
                        if (StringUtils.equals(reportName, repItemEditorInput.getModel().getName())) {
                            returnCode.add(editorRef);
                        }
                    }
                } catch (PartInitException e) {
                    log.error(e, e);
                }
            }

        }
        return returnCode;
    }

    public static void nodifyDependedAnalysis(ConnectionItem connectionItem) {
        // Added TDQ-8360 20140410 yyin: notify each depended analysis
        EList<Dependency> clientDependencies = connectionItem.getConnection().getSupplierDependency();
        for (Dependency dep : clientDependencies) {
            for (ModelElement mod : dep.getClient()) {
                if (!(mod instanceof Analysis)) {
                    continue;
                }
                EventManager.getInstance().publish(mod, EventEnum.DQ_ANALYSIS_REFRESH_DATAPROVIDER_LIST, null);
                EventManager.getInstance().publish(mod, EventEnum.DQ_MATCH_ANALYSIS_REFRESH_DATAPROVIDER_LABEL, null);
            }
        }

    }

    /**
     *
     * Get Editors which is is same as editorID --used for the analysis editor only
     *
     * @param editorID
     * @param analysisName
     * @return
     */
    private static List<IEditorReference> getIEditorReference(String editorID, String analysisName) {
        List<IEditorReference> returnCode = new ArrayList<IEditorReference>();
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorReference[] editors = activePage.getEditorReferences();
        if (editors != null) {
            for (IEditorReference editorRef : editors) {
                if (editorRef.getId().equals(editorID)) {
                    IEditorInput editorInput;
                    try {
                        editorInput = editorRef.getEditorInput();
                        if (editorInput instanceof AbstractItemEditorInput) {
                            AnalysisItemEditorInput anaItemEditorInput = (AnalysisItemEditorInput) editorInput;
                            // if not pointed which analysis, add all opened ones.
                            if (StringUtils.isEmpty(analysisName)
                                    || StringUtils.equals(analysisName, anaItemEditorInput.getModel().getName())) {
                                returnCode.add(editorRef);
                            }
                        }
                    } catch (PartInitException e) {
                        log.error(e, e);
                    }
                }
            }

        }
        return returnCode;
    }

    public static void refreshAnalysesNode() {
        IRepositoryNode analysesNode = RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.ANALYSIS);
        if (analysesNode != null) {
            CorePlugin.getDefault().refreshDQView(analysesNode);
        }
    }

    public static void refreshMetadataNode() {
        // database connection node
        IRepositoryNode dbNode = RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.DB_CONNECTIONS);
        if (dbNode != null) {
            CorePlugin.getDefault().refreshDQView(dbNode);
        }
        // delimited file connection node
        IRepositoryNode dfNode = RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.FILEDELIMITED);
        if (dfNode != null) {
            CorePlugin.getDefault().refreshDQView(dfNode);
        }
        // hadoop cluster connection node
        IRepositoryNode hcNode = RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.HADOOP_CLUSTER);
        if (dfNode != null) {
            CorePlugin.getDefault().refreshDQView(hcNode);
        }

    }

    /**
     *
     * DOC qiongli TDQ-3317:move this method from ReloadDatabaseAction. to this class .
     *
     * @param oldDataProvider
     * @throws PartInitException
     */
    public static void impactExistingAnalyses(DataProvider oldDataProvider) throws PartInitException {
        EList<Dependency> clientDependencies = oldDataProvider.getSupplierDependency();
        List<Analysis> unsynedAnalyses = new ArrayList<Analysis>();
        for (Dependency dep : clientDependencies) {
            for (ModelElement mod : dep.getClient()) {
                // MOD mzhao 2009-08-24 The dependencies include "Property" and "Analysis"
                if (!(mod instanceof Analysis)) {
                    continue;
                }
                Analysis ana = (Analysis) mod;
                unsynedAnalyses.add(ana);
            }

            for (Analysis analysis : unsynedAnalyses) {
                // Reload.
                IFile file = getTempAnalysisFile(analysis);
                Analysis tempAnalysis = (Analysis) AnaResourceFileHelper.getInstance().getModelElement(file);
                // MOD qiongli 2010-8-17,bug 14977
                if (tempAnalysis != null) {
                    Resource eResource = tempAnalysis.eResource();

                    Map<EObject, Collection<Setting>> referenceMaps = EcoreUtil.UnresolvedProxyCrossReferencer.find(eResource);

                    Iterator<EObject> it = referenceMaps.keySet().iterator();
                    ModelElement eobj = null;
                    boolean isModified = false;
                    while (it.hasNext()) {
                        eobj = (ModelElement) it.next();
                        Collection<Setting> settings = referenceMaps.get(eobj);
                        for (Setting setting : settings) {
                            if (setting.getEObject() instanceof AnalysisContext) {
                                tempAnalysis.getContext().getAnalysedElements().remove(eobj);
                                isModified = true;
                            } else if (setting.getEObject() instanceof Indicator) {
                                // when remove a column from the column set analysis, it should only remove this column
                                // from the simple stat indicator, but not remove the simple stat indicator, which is
                                // the only indicator in the column set analysis. (Added TDQ-8842 20140512 yyin)
                                if (AnalysisType.COLUMN_SET.equals(tempAnalysis.getParameters().getAnalysisType())) {
                                    ((SimpleStatIndicator) setting.getEObject()).getAnalyzedColumns().remove(eobj);// ~
                                } else {
                                    tempAnalysis.getResults().getIndicators().remove(setting.getEObject());
                                }
                                isModified = true;
                            }
                        }

                    }
                    if (isModified) {
                        saveTempAnalysis(file, tempAnalysis);

                    }
                    // Should reopen this analyis's editor if it is opened now. what ever it is modified or not.
                    EventManager.getInstance().publish(tempAnalysis.getName(), EventEnum.DQ_ANALYSIS_REOPEN_EDITOR, null);
                }
            }
        }
    }

    private static void saveTempAnalysis(IFile file, Analysis tempAnalysis) {
        Property tempAnaProperty = PropertyHelper.getProperty(file);

        // only when all elements of the data provider are removed from the analysis, the dependency between
        // them should be removed too. If only parts of them removed, the dependendy should not be removed.
        // TDQ-8267, since the dependency is removed, the connection in the analysis context should also be
        // removed
        if (tempAnaProperty == null) {
            log.error(Messages.getString("WorkbenchUtils.fialToSaveImapctAnalysis", tempAnalysis.getName())); //$NON-NLS-1$
            return;
        }
        if (tempAnalysis.getContext().getAnalysedElements().isEmpty()) {
            DependenciesHandler.getInstance().removeConnDependencyAndSave((TDQAnalysisItem) tempAnaProperty.getItem());

        } else {
            ElementWriterFactory.getInstance().createAnalysisWrite().save(tempAnaProperty.getItem(), false);
        }
    }

    /**
     * DOC yyin Comment method "getTempAnalysis".
     *
     * @param analysis
     * @return
     */
    private static IFile getTempAnalysisFile(Analysis analysis) {
        Resource eResource = analysis.eResource();
        if (eResource == null) {
            return null;
        }

        EMFSharedResources.getInstance().unloadResource(eResource.getURI().toString());

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        // MOD by zshen for bug 12316 to avoid null argument.
        Path path = new Path(analysis.getFileName() == null ? eResource.getURI().toPlatformString(false) : analysis.getFileName());
        return root.getFile(path);
    }

    /**
     * Reload the metadata table of the current file connection, when the file connection changed schema: if the
     * analysis 's analyzed columns are in the changed schema: compare the columns, remain the columns with same name,
     * remove the columns not in new schema, and add the new columns in new schema. TDQ-8360 20140324 yyin
     *
     * @param MetadataTable with new schema
     */
    public static void reloadMetadataOfDelimitedFile(MetadataTable metadataTable) throws BusinessException {
        FileMetadataTableComparisonLevel creatComparisonLevel = (FileMetadataTableComparisonLevel) ComparisonLevelFactory
                .creatComparisonLevel(metadataTable);
        try {
            creatComparisonLevel.reloadCurrentLevelElement();

        } catch (ReloadCompareException e) {
            log.error(e, e);
            throw new BusinessException(Messages.getString("WorkbenchUtils.failToReloadMetadataOfDelimitedFile"), e);
        }
    }

    /**
     * Get viewPart with special partId. If the active page doesn't exsit, the method will return null; Else, it will
     * get the viewPart and focus it. if the viewPart closed, it will be opened.
     *
     * @param viewId the identifier of viewPart
     * @return
     */
    public static IViewPart getAndOpenView(String viewId) {
        return getView(viewId, true);
    }

    /**
     * Get viewPart with special partId. If the active page doesn't exsit, the method will return null; Else, it will
     * get the viewPart and focus it. if the viewPart closed, it will return null too.
     *
     * @param viewId the identifier of viewPart
     * @param openIfClose decide whether we will open the view when it is closing
     * @return
     */
    public static IViewPart getView(String viewId, boolean openIfClose) {
        return getView(viewId, openIfClose, true);
    }

    /**
     * Get viewPart with special partId. If the active page doesn't exsit, the method will return null; Else, it will
     * get the viewPart and focus it or not. if the viewPart closed, it will return null too.
     *
     * @param viewId the identifier of viewPart
     * @param openIfClose decide whether we will open the view when it is closing
     * @param bringToTop decide whether we will bring the part To Top
     * @return
     */
    public static IViewPart getView(String viewId, boolean openIfClose, boolean bringToTop) {
        IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWorkbenchWindow == null) {
            return null;
        }
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        if (page == null) {
            return null;
        }
        IViewPart part = page.findView(viewId);
        if (part == null) {
            try {
                if (openIfClose) {
                    part = page.showView(viewId);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e, Level.ERROR);
            }
        } else {
            if (bringToTop) {
                page.bringToTop(part);
            }
        }

        return part;
    }

    /**
     * use this method to set ImageHyperlink's image insteadof using ImageHyperlink.setImage() to avoid new Image()
     * NOTE: this method only can be used when the ImageHyperlink is alway enable.
     *
     * @param imageHyperlink
     * @param image
     */
    public static void setHyperlinkImage(ImageHyperlink imageHyperlink, Image image) {
        imageHyperlink.setActiveImage(image);
        imageHyperlink.setBackgroundImage(image);
    }
}
