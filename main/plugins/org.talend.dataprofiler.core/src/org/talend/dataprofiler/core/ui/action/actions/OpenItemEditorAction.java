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
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.exception.ExceptionFactory;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.TDQFileEditorInput;
import org.talend.dataprofiler.core.ui.editor.TDQFileTextEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.analysis.MatchAnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.connection.ConnectionEditor;
import org.talend.dataprofiler.core.ui.editor.connection.ConnectionItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.dqrules.BusinessRuleItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.dqrules.DQRuleEditor;
import org.talend.dataprofiler.core.ui.editor.indicator.IndicatorDefinitionItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.indicator.IndicatorEditor;
import org.talend.dataprofiler.core.ui.editor.pattern.PatternEditor;
import org.talend.dataprofiler.core.ui.editor.pattern.PatternItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.report.ReportItemEditorInput;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.properties.TDQFileItem;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dataquality.properties.TDQReportItem;
import org.talend.dataquality.reports.AnalysisMap;
import org.talend.dataquality.reports.TdReport;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.dq.nodes.RecycleBinRepNode;
import org.talend.dq.nodes.ReportFileRepNode;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC mzhao Open TDQ items editor action.
 */
public class OpenItemEditorAction extends Action implements IIntroAction {

    protected static Logger log = Logger.getLogger(OpenItemEditorAction.class);

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    protected String editorID = null;

    private IFile file = null;

    private IRepositoryNode[] repNodes = null;

    public OpenItemEditorAction(IRepositoryNode[] selectedNodes) {
        super(DefaultMessagesImpl.getString("OpenIndicatorDefinitionAction.Open")); //$NON-NLS-1$
        this.repNodes = selectedNodes;
    }

    @Override
    public void run() {
        // MOD qiongli 2011-7-14 bug 21707,unload all unlocked resources before opening an editor.move all code in this
        // method to method doRun().
        if (repNodes != null) {
            RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>("Open an DQ editor") {//$NON-NLS-1$

                        @Override
                        protected void run() {
                            try {
                                for (IRepositoryNode repNode : repNodes) {
                                    setImageDescriptor(ImageLib.getImageDescriptorByRepositoryNode(repNode));
                                    duRun(repNode);
                                }
                            } catch (BusinessException e) {
                                org.talend.dataprofiler.core.exception.ExceptionHandler.process(e, Level.FATAL);
                            } catch (Throwable e) {
                                log.error(e, e);
                            }
                        }
                    };
            workUnit.setAvoidUnloadResources(true);
            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
        }
    }

    protected void duRun(IRepositoryNode repNode) throws BusinessException {
        // TDQ-12200: fix a NPE when the open item is unsynchronized status(for example is deleted by others).
        repositoryObjectCRUD.refreshDQViewForRemoteProject();

        // TDQ-13357: fix NPE, because for ReportFileRepNode, repNode.getObject() == null
        if (repNode.getObject() != null) {
            if (repNode.getObject().getProperty() == null) {
                repositoryObjectCRUD.showWarningDialog();
                return;
            }
            // TDQ-12200~

            // TDQ-12034: before open the object editor, reload it first especially for git remote project
            // TDQ-12771: for local, also can avoid error when the cache node is changed but not save it.
            try {
                ProxyRepositoryFactory.getInstance().reload(repNode.getObject().getProperty());
                IFile objFile = PropertyHelper.getItemFile(repNode.getObject().getProperty());
                objFile.refreshLocal(IResource.DEPTH_INFINITE, null);
            } catch (Exception e1) {
                log.error(e1, e1);
            }
        }
        // TDQ-12034~
        IEditorInput itemEditorInput = computeEditorInput(repNode, true);
        if (itemEditorInput != null) {
            // open ItemEditorInput
            CorePlugin.getDefault().openEditor(itemEditorInput, editorID);
        } else {
            // not find ItemEditorInput
            if (repNode.getObject() == null) {
                // open Report's genetated doc file
                if (repNode != null && repNode instanceof ReportFileRepNode) {
                    ReportFileRepNode reportFileNode = (ReportFileRepNode) repNode;
                    IPath location = Path.fromOSString(reportFileNode.getResource().getRawLocation().toOSString());
                    // TDQ-5458 sizhaoliu 2012-07-17 add "." before the full name to make sure it is ignored by SVN.
                    IFile latestRepIFile =
                            ResourceManager
                                    .getRootProject()
                                    .getFile(PluginConstant.DOT_STRING + location.lastSegment());
                    try {
                        // TDQ-5458 sizhaoliu 2012-07-17 the link creation should be after report generation, but not at
                        // the openning.
                        // latestRepIFile.createLink(location, IResource.REPLACE, null);
                        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        // MOD yyin 20121224 TDQ-5329, using the default editor setted by the user.
                        IDE.openEditor(page, latestRepIFile);
                    } catch (PartInitException e) {
                        MessageDialog.openError(Display.getCurrent().getActiveShell(),
                                Messages.getString("NewFolderWizard.failureTitle"), //$NON-NLS-1$
                                e.getMessage());
                        ExceptionHandler.process(e);
                    }
                }
            } else {
                // if there don't found the correct ItemEditorInput and it is not Report's genetated doc file, try to
                // open it as a File, this code will not be execute when method computeEditorInput() work well
                IPath append = WorkbenchUtils.getFilePath(repNode.getObject().getRepositoryNode());
                DQRepositoryNode node = (DQRepositoryNode) repNode.getObject().getRepositoryNode();
                file = ResourceManager.getRoot().getProject(node.getProject().getTechnicalLabel()).getFile(append);
                if (!file.exists()) {
                    throw ExceptionFactory.getInstance().createBusinessException(repNode.getObject());
                }
                try {
                    IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file, true);
                } catch (PartInitException e) {
                    log.error(e, e);
                }
            }
        }
    }

    /**
     * get the ItemEditorInput according to the repNode, if there no ItemEditorInput return null.
     * 
     * @param repNode
     * @param isOpenItemEditorAction
     * @return IEditorInput
     * @throws PersistenceException
     */
    public IEditorInput computeEditorInput(IRepositoryNode repNode, boolean isOpenItemEditorAction)
            throws BusinessException {
        // TDQ-12499 msjian add : when click the node under recyclebin, no need to find a EditorInput
        if (repNode != null && !isOpenItemEditorAction) {
            IRepositoryNode currentNode = repNode;
            do {
                RepositoryNode parentNode = currentNode.getParent();
                if (parentNode != null && parentNode instanceof RecycleBinRepNode) {
                    return null;
                }
                currentNode = parentNode;
            } while (currentNode != null);
        }
        // TDQ-12499~

        IEditorInput result = null;
        IRepositoryViewObject repViewObj = repNode.getObject();

        if (repViewObj != null) {
            // Connection editor
            String key = repViewObj.getRepositoryObjectType().getKey();
            Item item = repViewObj.getProperty().getItem();
            if (item instanceof TDQItem && !(item instanceof TDQFileItem)) {

                ModelElement modelElement = PropertyHelper.getModelElement(repViewObj.getProperty());
                if (modelElement.eIsProxy() && repNode != null) {
                    modelElement = (ModelElement) EObjectHelper.resolveObject(modelElement);
                    item = repViewObj.getProperty().getItem();
                }
                if (modelElement == null || modelElement.eResource() == null) {
                    throw ExceptionFactory.getInstance().createBusinessException(((TDQItem) item).getFilename());
                }
            }
            if (ERepositoryObjectType.METADATA_CONNECTIONS.getKey().equals(key) || ConnectionUtils.isTcompJdbc(key)) {
                result = new ConnectionItemEditorInput(repNode);
                Connection connection = ((ConnectionItem) item).getConnection();
                if (connection == null || connection.getDataPackage().size() == 0) {
                    throw ExceptionFactory.getInstance().createBusinessException(repViewObj);
                }
                editorID = ConnectionEditor.class.getName();
            } else if (ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT.getKey().equals(key)) {
                result = new AnalysisItemEditorInput(repNode);
                Analysis analysis = ((TDQAnalysisItem) item).getAnalysis();
                if (analysis == null || analysis.getContext() == null) {
                    throw ExceptionFactory.getInstance().createBusinessException(repViewObj);
                }
                EList<ModelElement> analysedElements = analysis.getContext().getAnalysedElements();
                RepositoryNode connectionRepositoryNode = null;
                if (!analysedElements.isEmpty()) {
                    Connection connection = null;
                    ModelElement modelElement = analysedElements.get(0);
                    if (modelElement instanceof Connection) {
                        connection = (Connection) modelElement;
                    } else if (modelElement instanceof Catalog) {
                        Catalog catalog = SwitchHelpers.CATALOG_SWITCH.caseCatalog((Catalog) modelElement);
                        connection = ConnectionHelper.getConnection(catalog);
                    } else if (modelElement instanceof Schema) {
                        Schema schema = SwitchHelpers.SCHEMA_SWITCH.caseSchema((Schema) modelElement);
                        if (schema != null) {
                            connection = ConnectionHelper.getConnection(schema);
                        }
                    } else if (modelElement instanceof TdTable) {
                        TdTable tdTable = SwitchHelpers.TABLE_SWITCH.caseTdTable((TdTable) modelElement);
                        connection = ConnectionHelper.getConnection(tdTable);
                    } else if (modelElement instanceof TdView) {
                        TdView tdView = SwitchHelpers.VIEW_SWITCH.caseTdView((TdView) modelElement);
                        connection = ConnectionHelper.getConnection(tdView);
                    } else if (modelElement instanceof TdColumn) {
                        TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.caseTdColumn((TdColumn) modelElement);
                        connection = ConnectionHelper.getConnection(tdColumn);
                    } else if (modelElement instanceof MetadataColumn) {
                        MetadataColumn mColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(modelElement);
                        connection = ConnectionHelper.getTdDataProvider(mColumn);
                    }
                    connectionRepositoryNode = RepositoryNodeHelper.recursiveFind(connection);
                }

                ((AnalysisItemEditorInput) result).setConnectionNode(connectionRepositoryNode);

                if (analysis.getParameters() != null
                        && analysis.getParameters().getAnalysisType().equals(AnalysisType.MATCH_ANALYSIS)) {
                    editorID = MatchAnalysisEditor.class.getName();
                } else {
                    editorID = AnalysisEditor.class.getName();
                }
            } else if (ERepositoryObjectType.TDQ_INDICATOR_ELEMENT.getKey().equals(key)) {
                result = new IndicatorDefinitionItemEditorInput(repNode);
                TDQIndicatorDefinitionItem definitionItem = (TDQIndicatorDefinitionItem) item;
                if (definitionItem.getIndicatorDefinition().eResource() == null) {
                    throw ExceptionFactory.getInstance().createBusinessException(definitionItem.getFilename());
                }
                if (UDIHelper.getUDICategory(definitionItem.getIndicatorDefinition()) == null) {
                    throw ExceptionFactory.getInstance().createBusinessException(definitionItem.getFilename());
                }
                editorID = IndicatorEditor.class.getName();
            } else if (ERepositoryObjectType.TDQ_RULES_SQL.getKey().equals(key)
                    || ERepositoryObjectType.TDQ_RULES_PARSER.getKey().equals(key)
                    || ERepositoryObjectType.TDQ_RULES_MATCHER.getKey().equals(key)) {
                result = new BusinessRuleItemEditorInput(repNode);
                editorID = DQRuleEditor.class.getName();
            } else if (ERepositoryObjectType.TDQ_PATTERN_ELEMENT.getKey().equals(key)) {
                result = new PatternItemEditorInput(repNode);
                TDQPatternItem patternItem = (TDQPatternItem) item;
                if (patternItem.getPattern() == null || patternItem.getPattern().eResource() == null) {
                    throw ExceptionFactory.getInstance().createBusinessException(patternItem.getFilename());
                }
                editorID = PatternEditor.class.getName();
            } else if (ERepositoryObjectType.TDQ_REPORT_ELEMENT.getKey().equals(key)) {
                result = new ReportItemEditorInput(repNode);
                TDQReportItem reportItem = (TDQReportItem) item;
                if (!(reportItem.getReport() instanceof TdReport)) {
                    throw ExceptionFactory.getInstance().createBusinessException(reportItem.getFilename());
                }
                for (AnalysisMap anaMap : ((TdReport) reportItem.getReport()).getAnalysisMap()) {
                    Analysis analysis = anaMap.getAnalysis();
                    if (analysis.eResource() == null) {
                        throw ExceptionFactory.getInstance().createBusinessException(reportItem.getFilename());
                    }
                }
                editorID = "org.talend.dataprofiler.core.tdq.ui.editor.report.ReportEditor"; //$NON-NLS-1$
            } else if (ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT.getKey().equals(key)
                    || ERepositoryObjectType.TDQ_JRAXML_ELEMENT.getKey().equals(key)) {
                IPath append = WorkbenchUtils.getFilePath(repViewObj.getRepositoryNode());
                DQRepositoryNode node = (DQRepositoryNode) repViewObj.getRepositoryNode();
                file = ResourceManager.getRoot().getProject(node.getProject().getTechnicalLabel()).getFile(append);
                if (!file.exists()) {
                    throw ExceptionFactory.getInstance().createBusinessException(repViewObj);
                }

                // TDQ-14934 msjian: fix can use the studio associate application to open file
                IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
                if (desc != null) {
                    editorID = desc.getId();
                } else {
                    if (ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT.getKey().equals(key)) {
                        editorID = SqlExplorerUtils.SQLEDITOR_ID;
                    } else {
                        editorID = TDQFileTextEditor.FILE_EDITOR_ID;
                    }
                }
                // TDQ-14934~

                result = new TDQFileEditorInput(file);
                // Added TDQ-7143 yyin 20130531
                ((TDQFileEditorInput) result).setFileItem(item);
                CorePlugin.getDefault().refreshDQView(repNode);
                // ~
            }
            // ADD msjian TDQ-4209 2012-2-7 : return the editorInput of *.jrxml and *.sql files
            if (!isOpenItemEditorAction) {
                if (ERepositoryObjectType.TDQ_JRAXML_ELEMENT.getKey().equals(key)
                        || ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT.getKey().equals(key)) {

                    // if there don't found the correct ItemEditorInput, try to open it as a File
                    result = new FileEditorInput(file);
                    editorID = FileEditorInput.class.getName();
                }
            }
            // TDQ-4209~
        }
        return result;
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroAction#run(org.eclipse.ui.intro.IIntroSite, java.util.Properties)
     */
    public void run(IIntroSite site, Properties params) {
        this.repNodes = new IRepositoryNode[] { RepositoryNodeHelper.recursiveFind(params.getProperty("nodeId")) }; //$NON-NLS-1$
        PlatformUI.getWorkbench().getIntroManager().closeIntro(PlatformUI.getWorkbench().getIntroManager().getIntro());
        run();
    }

}
