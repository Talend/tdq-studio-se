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
package org.talend.dataprofiler.core.ui.action.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.ComparisonLevelFactory;
import org.talend.cwm.compare.factory.IComparisonLevel;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.helper.FolderNodeHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.CheatSheetActionHelper;
import org.talend.dataprofiler.core.ui.dialog.AnalyzedColumnSetsSynDialog;
import org.talend.dataprofiler.core.ui.dialog.AnalyzedColumnsSynDialog;
import org.talend.dataprofiler.core.ui.dialog.AnalyzedElementSynDialog;
import org.talend.dataprofiler.core.ui.dialog.AnalyzedElementSynDialog.SynTreeModel;
import org.talend.dataprofiler.core.ui.dialog.AnalyzedPackageSynDialog;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnDependencyIndicator;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsCompareIndicator;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.analysis.AnalysisBuilder;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DFConnectionRepNode;
import org.talend.dq.nodes.foldernode.IFolderNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC mzhao class global comment. Detailled comment
 */
public class ChangeConnectionAction extends Action implements ICheatSheetAction {

    private static Logger log = Logger.getLogger(ChangeConnectionAction.class);

    private Connection oldDataProvider;

    private Connection newDataProvider;

    private TDQAnalysisItem analysisItem = null;

    private AnalyzedElementSynDialog anaEleSynDialog = null;

    private ReturnCode changeActionStatus;

    public ChangeConnectionAction(AbstractAnalysisMetadataPage masterPage, Connection tdProvider) {
        Object connectionObj = masterPage.getConnCombo().getData(
                masterPage.getConnCombo().getSelectionIndex() + PluginConstant.EMPTY_STRING);
        if (connectionObj instanceof DBConnectionRepNode || connectionObj instanceof DFConnectionRepNode) {
            this.newDataProvider = ((ConnectionItem) (((RepositoryNode) connectionObj).getObject().getProperty().getItem()))
                    .getConnection();
        } else {
            this.newDataProvider = (Connection) connectionObj;
        }

        this.oldDataProvider = tdProvider;
        this.analysisItem = (TDQAnalysisItem) masterPage.getCurrentRepNode().getObject().getProperty().getItem();
        this.changeActionStatus = new ReturnCode(Boolean.FALSE);
    }

    public void run(String[] params, ICheatSheetManager manager) {
        // ADD xqliu TDQ-4285 2011-12-27
        if (!CheatSheetActionHelper.canRun()) {
            return;
        }
        // ~ TDQ-4285

        run();
    }

    @Override
    public void run() {
        try {
            changeActionStatus = changedDatabaseConnection();
        } catch (ReloadCompareException e) {
            log.error(e, e);
        }
    }

    private ReturnCode changedDatabaseConnection() throws ReloadCompareException {

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        final EList<ModelElement> analyzedElements = analysisItem.getAnalysis().getContext() == null ? null : analysisItem
                .getAnalysis().getContext().getAnalysedElements();
        if (analyzedElements == null || analyzedElements.size() == 0) {
            analysisItem.getAnalysis().getContext().setConnection(newDataProvider);
            return new ReturnCode(Boolean.TRUE);
        }

        // MOD qiongli 2011-1-10,feature 16796.
        if (oldDataProvider instanceof DelimitedFileConnection) {
            // MessageDialog.openInformation(shell,
            // DefaultMessagesImpl.getString("ChangeConnectionAction.ChangeConnection"),
            // "Can't change this connection!");
            if (!MessageDialog.openConfirm(shell, DefaultMessagesImpl.getString("ChangeConnectionAction.ChangeConnection"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("ChangeConnectionAction.ChangeConnectionTips"))) { //$NON-NLS-1$
                return new ReturnCode(Boolean.FALSE);
            }
        }

        // Open synchronized dialog.
        boolean retCode = MessageDialog.openQuestion(shell,
                DefaultMessagesImpl.getString("ChangeConnectionAction.ChangeConnection"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("ChangeConnectionAction.MayCauseAsynProblem")); //$NON-NLS-1$
        if (retCode) {
            if (analyzedElements.get(0) instanceof TdColumn) {
                anaEleSynDialog = new AnalyzedColumnsSynDialog(shell, analysisItem.getAnalysis(), newDataProvider,
                        analyzedElements);
            } else if (analyzedElements.get(0) instanceof ColumnSet) {
                anaEleSynDialog = new AnalyzedColumnSetsSynDialog(shell, analysisItem.getAnalysis(), newDataProvider,
                        analyzedElements);
            } else if (analyzedElements.get(0) instanceof Package) {
                anaEleSynDialog = new AnalyzedPackageSynDialog(shell, analysisItem.getAnalysis(), newDataProvider,
                        analyzedElements);
            }

            final List<SynTreeModel> treeModelLs = anaEleSynDialog == null ? null : anaEleSynDialog.getSynInputModel();
            if (treeModelLs != null && treeModelLs.size() > 0) {
                // Make attempt to reload from db before showing asyned
                // message.
                boolean isReload = MessageDialog.openQuestion(shell,
                        DefaultMessagesImpl.getString("ChangeConnectionAction.ReloadFromDatabase"), //$NON-NLS-1$
                        DefaultMessagesImpl.getString("ChangeConnectionAction.ExistElementAsynchronuos")); //$NON-NLS-1$
                if (isReload) {
                    ModelElement newDataProviderModel = treeModelLs.get(0).getNewDataProvElement();
                    if (newDataProviderModel != null
                            && (newDataProviderModel instanceof ColumnSet || newDataProviderModel instanceof Package)) {
                        if (newDataProviderModel instanceof Package) {

                            IRunnableWithProgress op = new IRunnableWithProgress() {

                                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                                    try {
                                        reloadByColumnSetFolderLevel(treeModelLs, anaEleSynDialog, newDataProvider);

                                        if (analyzedElements.get(0) instanceof TdColumn) {
                                            // Reload column folder
                                            reloadByColumnFolderLevel(treeModelLs, anaEleSynDialog, newDataProvider);
                                        }
                                    } catch (ReloadCompareException e) {
                                        log.error(e, e);
                                    }
                                }

                            };

                            try {
                                ProgressUI.popProgressDialog(op);
                            } catch (InvocationTargetException e) {
                                log.error(e, e);
                            } catch (InterruptedException e) {
                                log.error(e, e);
                            }

                        } else if (newDataProviderModel instanceof ColumnSet) {
                            IRunnableWithProgress op = new IRunnableWithProgress() {

                                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                                    try {
                                        reloadByColumnFolderLevel(treeModelLs, anaEleSynDialog, newDataProvider);
                                    } catch (ReloadCompareException e) {
                                        log.error(e, e);
                                    }
                                }

                            };

                            try {
                                ProgressUI.popProgressDialog(op);
                            } catch (InvocationTargetException e) {
                                log.error(e, e);
                            } catch (InterruptedException e) {
                                log.error(e, e);
                            }

                        }
                    }

                }

            }
            // Open asyned dialog.
            if (treeModelLs != null && treeModelLs.size() > 0) {
                // Open confirmation dialog to see whether user want to
                // continue or not.
                int returnCode = anaEleSynDialog.open();
                if (returnCode != Window.OK) {
                    return new ReturnCode(Boolean.FALSE);
                }
            }
            // Synchronize analyzed elements.
            boolean isExistSynedElement = synAnalyzedElements(anaEleSynDialog, analysisItem.getAnalysis(), oldDataProvider,
                    newDataProvider);
            // Add new dependencies.
            if (isExistSynedElement) {
                DependenciesHandler.getInstance().setDependencyOn(analysisItem.getAnalysis(), newDataProvider);
                ElementWriterFactory.getInstance().createDataProviderWriter().save(newDataProvider);
            }
            // Refresh analysis editor viewer.
            ElementWriterFactory.getInstance().createAnalysisWrite().save(analysisItem, false);
            // Refresh the repository tree view to adapt for the new analysis
            CorePlugin.getDefault().refreshDQView(RepositoryNodeHelper.getDataProfilingFolderNode(EResourceConstant.ANALYSIS));
        } else {
            return new ReturnCode(Boolean.FALSE);
        }
        return new ReturnCode(Boolean.TRUE);
    }

    private void reloadByColumnSetFolderLevel(List<SynTreeModel> treeModelLs, AnalyzedElementSynDialog anaEleSynDialog,
            Connection newDataProv) throws ReloadCompareException {
        ModelElement oldDataProviderModel = treeModelLs.get(0).getOldDataProvElement();
        // Reload columnSet folder
        ColumnSet columnset = null;
        if (oldDataProviderModel instanceof ColumnSet) {
            columnset = (ColumnSet) oldDataProviderModel;
        } else if (oldDataProviderModel instanceof TdColumn) {
            columnset = ColumnHelper.getColumnOwnerAsColumnSet(oldDataProviderModel);
        } else {
            return;
        }
        // MOD mzhao bug 8567 2010-03-29
        IFolderNode reloadFolder = FolderNodeHelper
                .getFolderNode((Package) treeModelLs.get(0).getNewDataProvElement(), columnset);
        if (reloadFolder != null) {
            IComparisonLevel creatComparisonLevel = ComparisonLevelFactory.creatComparisonLevel(reloadFolder);
            if (creatComparisonLevel != null) {
                // FIXME newDataProv is never used in the following codes.
                newDataProv = creatComparisonLevel.reloadCurrentLevelElement();
            }
            // Recompute after reload
            // FIXME treeModelLs is never used in the following codes.
            treeModelLs = anaEleSynDialog.getSynInputModel();
        }
    }

    private void reloadByColumnFolderLevel(List<SynTreeModel> treeModelLs, AnalyzedElementSynDialog anaEleSynDialog,
            Connection newDataProv) throws ReloadCompareException {
        ModelElement newDataProviderModel = treeModelLs.get(0).getNewDataProvElement();
        // If schema or catalog changed, we did not load anymore.
        // Reload column folder
        ColumnSet columnset = null;
        if (newDataProviderModel instanceof ColumnSet) {
            columnset = (ColumnSet) newDataProviderModel;
        } else if (newDataProviderModel instanceof TdColumn) {
            columnset = ColumnHelper.getColumnOwnerAsColumnSet(newDataProviderModel);
        } else {
            return;
        }
        // MOD mzhao bug 8567 2010-03-29
        IFolderNode reloadFolder = FolderNodeHelper.getFolderNodes(columnset)[0];
        if (reloadFolder != null) {
            IComparisonLevel creatComparisonLevel = ComparisonLevelFactory.creatComparisonLevel(reloadFolder);
            if (creatComparisonLevel != null) {
                // FIXME newDataProv is never used in the following codes.
                newDataProv = creatComparisonLevel.reloadCurrentLevelElement();
            }
            // Recompute after reload
            // FIXME treeModelLs is never used in the following codes.
            treeModelLs = anaEleSynDialog.getSynInputModel();
        }
    }

    private boolean synAnalyzedElements(AnalyzedElementSynDialog anaEleSynDialog, Analysis synAnalysis,
            Connection oldDataProvider, Connection newDataProv) {
        // Change connection uuid.
        Map<ModelElement, ModelElement> synEleMap = anaEleSynDialog == null ? null : anaEleSynDialog.getSynedEleMap();
        AnalysisBuilder anaBuilder = new AnalysisBuilder();
        anaBuilder.setAnalysis(synAnalysis);
        // Remove old dependencies.
        boolean isRemovedDependency = DependenciesHandler.getInstance().removeConnDependencyAndSave(analysisItem);
        if (!isRemovedDependency) {
            return false;
        }
        synAnalysis.getContext().setConnection(newDataProv);

        // Synchronize analysis result.
        EList<Indicator> indcList = synAnalysis.getResults().getIndicators();
        Indicator[] copiedIndArray = new Indicator[indcList.size()];
        System.arraycopy(indcList.toArray(), 0, copiedIndArray, 0, indcList.size());
        // MOD qiongli 2010-6-13,bug 12766:To column analysis, which has same
        // construct connection with before and maybe have not indicator
        boolean isExistSynedElement = false;
        AnalysisType analysisType = synAnalysis.getParameters().getAnalysisType();
        if (analysisType == AnalysisType.MULTIPLE_COLUMN) {
            EList<ModelElement> meLs = synAnalysis.getContext().getAnalysedElements();
            ModelElement[] mes = new ModelElement[meLs.size()];
            System.arraycopy(meLs.toArray(), 0, mes, 0, meLs.size());
            synAnalysis.getContext().getAnalysedElements().clear();
            for (ModelElement me : mes) {
                if (synEleMap != null && synEleMap.get(me) != null) {
                    TdColumn newColumn = (TdColumn) synEleMap.get(me);
                    synAnalysis.getContext().getAnalysedElements().add(newColumn);
                    isExistSynedElement = true;
                }
            }
        }
        if (!isExistSynedElement) {
            synAnalysis.getContext().getAnalysedElements().clear();
        }
        // ~
        synAnalysis.getResults().getIndicators().clear();

        for (Indicator indicator : copiedIndArray) {
            // Add new analyzed element contained in new
            // connection.
            if (indicator instanceof ColumnSetMultiValueIndicator) {
                ColumnSetMultiValueIndicator compositeInd = (ColumnSetMultiValueIndicator) indicator;
                ModelElement[] mes = new ModelElement[compositeInd.getAnalyzedColumns().size()];
                ((ColumnSetMultiValueIndicator) indicator).getAnalyzedColumns().toArray(mes);
                compositeInd.getAnalyzedColumns().clear();
                for (ModelElement me : mes) {
                    if (synEleMap != null && synEleMap.get(me) != null) {
                        TdColumn newColumn = (TdColumn) synEleMap.get(me);
                        DataminingType dataminingType = MetadataHelper.getDataminingType((TdColumn) me);
                        if (dataminingType == null) {
                            dataminingType = MetadataHelper.getDefaultDataminingType(((TdColumn) me).getSqlDataType()
                                    .getJavaDataType());
                        }
                        MetadataHelper.setDataminingType(dataminingType, newColumn);
                        compositeInd.getAnalyzedColumns().add(newColumn);
                        anaBuilder.addElementToAnalyze(newColumn, indicator);
                        isExistSynedElement = true;
                    }
                }

            } else if (indicator instanceof ColumnsCompareIndicator) {
                // Correlation compare
                ColumnsCompareIndicator compInd = (ColumnsCompareIndicator) indicator;
                if ((compInd.getColumnSetA() == null || compInd.getColumnSetA().size() == 0)
                        || (compInd.getColumnSetB() == null || compInd.getColumnSetB().size() == 0)) {
                    return false;
                }
                // Column set(Columns)
                ModelElement[] mesA = new ModelElement[compInd.getColumnSetA().size()];
                compInd.getColumnSetA().toArray(mesA);
                compInd.getColumnSetA().clear();
                for (ModelElement me : mesA) {
                    if (synEleMap != null && synEleMap.get(me) != null) {
                        TdColumn newColumn = (TdColumn) synEleMap.get(me);
                        compInd.getColumnSetA().add(newColumn);
                        anaBuilder.addElementToAnalyze(newColumn, indicator);
                        isExistSynedElement = true;
                    }
                }

                ModelElement[] mesB = new ModelElement[compInd.getColumnSetB().size()];
                compInd.getColumnSetB().toArray(mesB);
                compInd.getColumnSetB().clear();
                for (ModelElement me : mesB) {
                    if (synEleMap != null && synEleMap.get(me) != null) {
                        TdColumn newColumn = (TdColumn) synEleMap.get(me);
                        compInd.getColumnSetB().add(newColumn);
                        anaBuilder.addElementToAnalyze(newColumn, indicator);
                        isExistSynedElement = true;
                    }
                }
                // Analyzed element(Table)
                ModelElement oldAnaEle = compInd.getAnalyzedElement();
                compInd.setAnalyzedElement(null);
                ColumnSet oldColSetA = ColumnHelper.getColumnOwnerAsColumnSet(mesA[0]);
                ColumnSet oldColSetB = ColumnHelper.getColumnOwnerAsColumnSet(mesB[0]);
                if (oldColSetA == oldAnaEle) {
                    if (synEleMap != null && synEleMap.get(mesA[0]) != null) {
                        compInd.setAnalyzedElement(ColumnHelper.getColumnOwnerAsColumnSet(synEleMap.get(mesA[0])));
                    }
                }
                if (oldColSetB == oldAnaEle) {
                    if (synEleMap != null && synEleMap.get(mesB[0]) != null) {
                        compInd.setAnalyzedElement(ColumnHelper.getColumnOwnerAsColumnSet(synEleMap.get(mesB[0])));
                    }
                }
            } else if (indicator instanceof ColumnDependencyIndicator) { // ADD qiongli bug 0012766
                // Functional Dependency indicator
                ColumnDependencyIndicator funDepInd = (ColumnDependencyIndicator) indicator;
                if (funDepInd.getColumnA() == null || funDepInd.getColumnB() == null) {
                    return false;
                }
                // Column A
                if (synEleMap != null && synEleMap.get(funDepInd.getColumnA()) != null) {
                    TdColumn newColumn = (TdColumn) synEleMap.get(funDepInd.getColumnA());
                    funDepInd.setColumnA(newColumn);
                    anaBuilder.addElementToAnalyze(newColumn, indicator);
                    isExistSynedElement = true;
                }

                // Column B
                if (synEleMap != null && synEleMap.get(funDepInd.getColumnB()) != null) {
                    TdColumn newColumn = (TdColumn) synEleMap.get(funDepInd.getColumnB());
                    funDepInd.setColumnB(newColumn);
                    anaBuilder.addElementToAnalyze(newColumn, indicator);
                    isExistSynedElement = true;
                }

                // Analyzed element(Table)
                ModelElement oldAnaEle = funDepInd.getAnalyzedElement();
                funDepInd.setAnalyzedElement(null);
                ColumnSet oldColSetA = ColumnHelper.getColumnOwnerAsColumnSet(funDepInd.getColumnA());
                ColumnSet oldColSetB = ColumnHelper.getColumnOwnerAsColumnSet(funDepInd.getColumnB());
                if (oldColSetA == oldAnaEle) {
                    if (synEleMap != null && synEleMap.get(funDepInd.getColumnA()) != null) {
                        funDepInd
                                .setAnalyzedElement(ColumnHelper.getColumnOwnerAsColumnSet(synEleMap.get(funDepInd.getColumnA())));
                    }
                }
                if (oldColSetB == oldAnaEle) {
                    if (synEleMap != null && synEleMap.get(funDepInd.getColumnB()) != null) {
                        funDepInd
                                .setAnalyzedElement(ColumnHelper.getColumnOwnerAsColumnSet(synEleMap.get(funDepInd.getColumnB())));
                    }
                }

            } else {
                ModelElement me = indicator.getAnalyzedElement();
                if (synEleMap != null && synEleMap.get(me) != null) {
                    indicator.setAnalyzedElement(synEleMap.get(me));
                    if (analysisType == AnalysisType.MULTIPLE_COLUMN) {
                        synAnalysis.getResults().getIndicators().add(indicator);
                    } else {
                        anaBuilder.addElementToAnalyze(synEleMap.get(me), indicator);
                    }
                    isExistSynedElement = true;
                }
            }
        }
        return isExistSynedElement;
    }

    public ReturnCode getStatus() {
        return changeActionStatus;
    }

}
