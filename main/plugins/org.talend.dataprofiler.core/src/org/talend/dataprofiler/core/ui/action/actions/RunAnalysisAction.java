// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.ComparisonLevelFactory;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.exception.ExceptionFactory;
import org.talend.dataprofiler.core.exception.ExceptionHandler;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.IRuningStatusListener;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.analysis.BusinessRuleAnalysisResultPage;
import org.talend.dataprofiler.core.ui.editor.analysis.ColumnAnalysisResultPage;
import org.talend.dataprofiler.core.ui.editor.analysis.DynamicAnalysisMasterPage;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dataprofiler.service.ISemanticStudioService;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.analysis.AnalysisExecutorSelector;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * Run Analysis Action.
 * 
 */
public class RunAnalysisAction extends Action implements ICheatSheetAction {

    private static Logger log = Logger.getLogger(RunAnalysisAction.class);

    public static final String ID = "org.talend.common.runTalendElement"; //$NON-NLS-1$

    private static final DecimalFormat FORMAT_SECONDS = new DecimalFormat("0.00"); //$NON-NLS-1$

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    private IRuningStatusListener listener;

    /**
     * Important: keep using the Item, no need to used AnalysisRepNode in this class, remember this!!!
     * 
     */
    private TDQAnalysisItem[] items;

    private boolean isNeedUnlock = false;

    /**
     * RunAnalysisAction constructor.
     */
    public RunAnalysisAction() {
        super(DefaultMessagesImpl.getString("RunAnalysisAction.Run")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.RUN_IMAGE));
    }

    public void setAnalysisItems(TDQAnalysisItem[] items) {
        this.items = items;
    }

    public void setListener(IRuningStatusListener listener) {
        this.listener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (items != null) {
            // TDQ-11550: refresh first especially for remote project, can get the lastest item status correctly
            repositoryObjectCRUD.refreshDQViewForRemoteProject();

            for (TDQAnalysisItem anaItem : items) {
                runAnalysisForItem(anaItem);
            }
        }
    }

    /**
     * DOC msjian Comment method "runAnalysisForItem".
     */
    protected void runAnalysisForItem(final TDQAnalysisItem anaItem) {
        try {
            if (anaItem == null) {
                log.error("Analysis item is null"); //$NON-NLS-1$
                return;
            }

            if (ifLockByOthers(anaItem)) {
                return;
            }

            // check if the analysis need to be saved or can run before real running by the event
            if (!EventManager.getInstance().publish(anaItem.getAnalysis(), EventEnum.DQ_ANALYSIS_CHECK_BEFORERUN, null)) {
                // if the analysis need save but can not be saved, return without continue;
                // or the analysis can not run, return without continue
                return;
            }

            // to do validate after save.
            validateAnalysis(anaItem);

            if (!isConnectedAvailable(anaItem)) {
                return;
            }

            if (log.isInfoEnabled()) {
                addTaggedVaLueIntoConnection(anaItem);
            }

            AnalysisType analysisType = anaItem.getAnalysis().getParameters().getAnalysisType();
            if (AnalysisType.COLUMNS_COMPARISON.equals(analysisType)) {
                // If the analysis type is column comparison, ask user to continue to run or not.
                if (!isContinueRun()) {
                    return;
                }
            } else if (AnalysisType.CONNECTION.equals(analysisType)) {
                // If the analysis type is overview analysis, reload the database.
                // TODO check here the needed of reloading database
                reloadConnection(anaItem);
            }

            // lock the analysis before running it if it is not locked yet.(whenever the editor is not opened)
            // when the run comes from the button in editor, the listener is not null;
            // when the run comes from context menu, the listener is null
            if (this.listener == null && !ProxyRepositoryManager.getInstance().isLocked(anaItem)) {
                if (!ProxyRepositoryFactory.getInstance().isEditableAndLockIfPossible(anaItem)) {
                    // if the analysis is not editable , return without running.
                    isNeedUnlock = false;
                    return;
                } else {// it is locked here, means that it need the unlock too.
                    isNeedUnlock = true;
                }
            } else {
                isNeedUnlock = false;
            }

            final WorkspaceJob job = new WorkspaceJob("Run Analysis") { //$NON-NLS-1$

                @Override
                public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

                    final boolean isSupportDynamicChart = isSupportDynamicChart(anaItem);
                    monitor.beginTask(
                            DefaultMessagesImpl.getString("RunAnalysisAction.running", anaItem.getAnalysis().getName()), 100); //$NON-NLS-1$ 
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            if (listener != null) {
                                listener.fireRuningItemChanged(false, isSupportDynamicChart);
                            }
                            // register dynamic event for who supported dynamic chart
                            if (isSupportDynamicChart) {
                                EventManager.getInstance().publish(anaItem.getAnalysis(),
                                        EventEnum.DQ_DYNAMIC_REGISTER_DYNAMIC_CHART, null);
                            }
                        }

                    });

                    ReturnCode executed = null;
                    try {
                        monitor.worked(10);
                        executed = AnalysisExecutorSelector.executeAnalysis(anaItem, monitor);

                        if (monitor.isCanceled()) {
                            TdqAnalysisConnectionPool.closeConnectionPool(anaItem.getAnalysis());
                            executed = new ReturnCode(DefaultMessagesImpl.getString("RunAnalysisAction.TaskCancel"), false); //$NON-NLS-1$
                            monitor.done();
                            if (isNeedUnlock) {
                                unlockAnalysis(anaItem);
                            }
                            return Status.CANCEL_STATUS;
                        }

                        if (isNeedUnlock) {
                            unlockAnalysis(anaItem);
                        }
                        monitor.subTask(DefaultMessagesImpl.getString("RunAnalysisAction.refresh.page")); //$NON-NLS-1$

                    } finally {// if any exception, still need to unregister dynamic events.
                        Display.getDefault().syncExec(new Runnable() {

                            public void run() {
                                // Added TDQ-8787 20140616 yyin: unregister all dynamic chart events after executing
                                // the analysis
                                if (isSupportDynamicChart) {
                                    EventManager.getInstance().publish(anaItem.getAnalysis(),
                                            EventEnum.DQ_DYNAMIC_UNREGISTER_DYNAMIC_CHART, null);
                                }

                                if (listener != null) {
                                    listener.fireRuningItemChanged(true);
                                } else {
                                    // TODO yyin publish the event from listener.
                                    EventManager.getInstance().publish(anaItem.getAnalysis(),
                                            EventEnum.DQ_ANALYSIS_RUN_FROM_MENU, null);
                                }

                            }

                        });
                    }
                    displayResultStatus(executed, anaItem);
                    // TODO move this code to the right place
                    addAnalysisToRef(anaItem.getAnalysis());
                    monitor.worked(20);
                    monitor.done();
                    return Status.OK_STATUS;
                }

            };

            job.setUser(true);
            job.schedule();

            // TDQ-11433: fix the job name still show after run analysis for remote project.(maybe this is not the best
            // way to fix this issue)
            job.addJobChangeListener(new JobChangeAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * org.eclipse.core.runtime.jobs.JobChangeAdapter#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
                 */
                @Override
                public void done(IJobChangeEvent event) {
                    job.setName(""); //$NON-NLS-1$
                }
            });
            // TDQ-11433~

        } catch (BusinessException e) {
            ExceptionHandler.process(e, Level.FATAL);
        }
    }

    /**
     * only sql mode, and column, table, dependency analysis support dynamic chart now.
     * 
     * @return boolean
     */
    private boolean isSupportDynamicChart(TDQAnalysisItem runItem) {
        ExecutionLanguage executionEngine = AnalysisHelper.getExecutionEngine(runItem.getAnalysis());
        if (ExecutionLanguage.SQL.equals(executionEngine)) {
            if (listener == null) {// when run from context menu.
                if (AnalysisType.MULTIPLE_COLUMN.equals(runItem.getAnalysis().getParameters().getAnalysisType())
                        || AnalysisType.BUSINESS_RULE.equals(runItem.getAnalysis().getParameters().getAnalysisType())) {
                    return true;
                }
                return false;
            } else {// run from the run button in the editor
                return listener instanceof DynamicAnalysisMasterPage || listener instanceof ColumnAnalysisResultPage
                        || listener instanceof BusinessRuleAnalysisResultPage;
            }
        } else {
            return false;
        }
    }

    private void addTaggedVaLueIntoConnection(TDQAnalysisItem runItem) {
        DataManager datamanager = runItem.getAnalysis().getContext().getConnection();
        if (datamanager instanceof DatabaseConnection) {
            TaggedValue productName = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DB_PRODUCT_NAME,
                    datamanager.getTaggedValue());
            TaggedValue productVersion = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DB_PRODUCT_VERSION,
                    datamanager.getTaggedValue());
            log.info("DB Product Name: " + productName.getValue()); //$NON-NLS-1$
            log.info("DB Product Version: " + productVersion.getValue()); //$NON-NLS-1$
        } else if (datamanager instanceof DelimitedFileConnection) {
            log.info("File Connection path: " + ((DelimitedFileConnection) datamanager).getFilePath()); //$NON-NLS-1$
        }
    }

    private void addAnalysisToRef(Analysis analysis) {
        ISemanticStudioService service = CorePlugin.getDefault().getSemanticStudioService();
        if (service != null) {
            service.enrichOntRepoWithAnalysisResult(analysis);
        }
    }

    /**
     * unlock analysis.
     */
    private void unlockAnalysis(TDQAnalysisItem runItem) {
        try {
            ProxyRepositoryFactory.getInstance().unlock(runItem);
        } catch (PersistenceException e) {
            log.error(e, e);
        } catch (LoginException e) {
            log.error(e, e);
        }
    }

    /**
     * popup dialog isContinueRun.
     * 
     * @return
     */
    private Boolean isContinueRun() {
        return MessageDialogWithToggle.openConfirm(null, DefaultMessagesImpl.getString("RunAnalysisAction.confirmTitle"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("RunAnalysisAction.confirmMSG")); //$NON-NLS-1$
    }

    /**
     * reload analysis connection.
     */
    private void reloadConnection(TDQAnalysisItem runItem) {
        if (AnalysisHelper.getReloadDatabases(runItem.getAnalysis())) {
            Connection conntion = (Connection) runItem.getAnalysis().getContext().getConnection();
            if (conntion != null) {
                try {
                    RepositoryNode connectionNode = RepositoryNodeHelper.recursiveFind(conntion);
                    ComparisonLevelFactory.creatComparisonLevel(connectionNode).reloadCurrentLevelElement();
                    CorePlugin.getDefault().refreshDQView(connectionNode);
                } catch (ReloadCompareException e) {
                    log.error(e, e);
                }
            }
        }
    }

    /**
     * check whether the connection of analysis is available.
     * 
     * @return true when the connection is well connected
     */
    private boolean isConnectedAvailable(TDQAnalysisItem runItem) {
        DataManager datamanager = runItem.getAnalysis().getContext().getConnection();
        if (datamanager == null) {
            log.error(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.NoColumnAssigned", runItem.getAnalysis().getName())); //$NON-NLS-1$
            MessageDialogWithToggle.openError(null, DefaultMessagesImpl.getString("RunAnalysisAction.runAnalysis"),//$NON-NLS-1$
                    DefaultMessagesImpl.getString("ColumnMasterDetailsPage.NoColumnAssigned", runItem.getAnalysis().getName()));//$NON-NLS-1$
            return false;
        }
        return ConnectionUtils.checkConnection(datamanager, runItem.getAnalysis().getName());
    }

    /**
     * check whether the item is locked by Others.
     * 
     * @return
     */
    private boolean ifLockByOthers(TDQAnalysisItem runItem) {
        // MOD sizhaoliu TDQ-5452 verify the lock status before running an analysis
        if (ProxyRepositoryManager.getInstance().isLockByOthers(runItem)) {
            RepositoryNode node1 = RepositoryNodeHelper.recursiveFind(runItem.getProperty());
            if (node1 != null) {
                CorePlugin.getDefault().refreshDQView(node1.getParent());
            }
            MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                    DefaultMessagesImpl.getString("RunAnalysisAction.runAnalysis"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("RunAnalysisAction.error.lockByOthers", runItem.getAnalysis().getName())); //$NON-NLS-1$
            return true;
        } // ~ TDQ-5452
        return false;
    }

    /**
     * validate analysis.
     * 
     * @throws BusinessException
     */
    private void validateAnalysis(TDQAnalysisItem runItem) throws BusinessException {
        if (runItem.getAnalysis() == null || runItem.getAnalysis().getParameters() == null) {
            throw ExceptionFactory.getInstance().createBusinessException(runItem.getFilename());
        }

        try {
            // check whether the field is integer value
            AnalysisHandler.createHandler(runItem.getAnalysis()).getNumberOfConnectionsPerAnalysis();
        } catch (NumberFormatException nfe) {
            BusinessException businessException = new BusinessException();
            businessException.setAdditonalMessage(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.mustBeNumber", //$NON-NLS-1$
                    DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.NumberOfConnectionsPerAnalysis"))); //$NON-NLS-1$
            throw businessException;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[], org.eclipse.ui.cheatsheets.ICheatSheetManager)
     */
    public void run(String[] params, ICheatSheetManager manager) {
        // ADD mzhao 2009-02-03 If there is no active editor opened, run
        // analysis action from cheat sheets will do nothing.
        IEditorPart activateEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (activateEditor == null || !(activateEditor instanceof AnalysisEditor)) {
            return;
        }
        // MOD TDQ-8117 20131008 yyin: find the analysis item from the editor
        IEditorInput editorInput = activateEditor.getEditorInput();
        if (editorInput instanceof AnalysisItemEditorInput) {
            setAnalysisItems(new TDQAnalysisItem[] { ((TDQAnalysisItem) ((AnalysisItemEditorInput) editorInput).getItem()) });
        }
        AbstractAnalysisMetadataPage masterPage = ((AnalysisEditor) activateEditor).getMasterPage();
        listener = masterPage;
        // ~
        run();
    }

    /**
     * display Result Status.
     * 
     * @param executed
     */
    private void displayResultStatus(final ReturnCode executed, final TDQAnalysisItem runItem) {
        if (log.isInfoEnabled()) {
            int executionDuration = runItem.getAnalysis().getResults().getResultMetadata().getExecutionDuration();
            log.info(DefaultMessagesImpl
                    .getString(
                            "RunAnalysisAction.displayInformation", new Object[] { runItem.getAnalysis().getName(), executed, FORMAT_SECONDS.format(Double.valueOf(executionDuration) / 1000) })); //$NON-NLS-1$ 
        }

        if (!StringUtils.isBlank(executed.getMessage())) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    // use the ActiveWorkbenchWindow's shell, don't use the default shell, otherwize the error dialog
                    // will be cloed when the "Run Analysis" dialog (if user don't check "Always run in background", it
                    // will always show this dialog when run an analysis) close, just like don't show the error dialog
                    Shell shell = null;
                    if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
                        shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                    }
                    String errorMessage = DefaultMessagesImpl.getString("RunAnalysisAction.failRunAnalysis",//$NON-NLS-1$
                            runItem.getAnalysis().getName(), executed.getMessage());
                    log.error(errorMessage);
                    MessageDialogWithToggle.openError(shell,
                            DefaultMessagesImpl.getString("RunAnalysisAction.runAnalysis"), errorMessage); //$NON-NLS-1$
                }
            });
        }
    }

}
