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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.editor.analysis.TablesSelectionDialog.TableSelectionType;
import org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisTableTreeViewer;
import org.talend.dataprofiler.core.ui.editor.preview.CompositeIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.WhereRuleStatisticsStateTable;
import org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.IEventReceiver;
import org.talend.dataprofiler.core.ui.events.TableDynamicChartEventReceiver;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.AnalysisUtils;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.TableAnalysisHandler;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class BusinessRuleAnalysisDetailsPage extends DynamicAnalysisMasterPage implements PropertyChangeListener {

    private static Logger log = Logger.getLogger(BusinessRuleAnalysisDetailsPage.class);

    AnalysisTableTreeViewer treeViewer;

    TableAnalysisHandler analysisHandler;

    private TableIndicator[] currentTableIndicators;

    private static final int TREE_MAX_LENGTH = 400;

    private Composite[] previewChartCompsites;

    private Section analysisTableSection = null;

    private List<ExpandableComposite> previewChartList = null;

    // Added TDQ-8787 20140617 yyin : store the temp indicator and its related dataset between one running
    private List<DynamicIndicatorModel> dynamicList = new ArrayList<DynamicIndicatorModel>();

    /**
     * DOC xqliu TableMasterDetailsPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public BusinessRuleAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        currentEditor = (AnalysisEditor) editor;
        execLang = ExecutionLanguage.SQL.getLiteral();
    }

    public TableIndicator[] getCurrentTableIndicators() {
        return currentTableIndicators;
    }

    @Override
    public void initialize(FormEditor editor) {
        super.initialize(editor);
        recomputeIndicators();
    }

    public void recomputeIndicators() {
        analysisHandler = new TableAnalysisHandler();
        analysisHandler.setAnalysis(getCurrentModelElement());
        stringDataFilter = analysisHandler.getStringDataFilter();
        EList<ModelElement> analyzedTables = analysisHandler.getAnalyzedTables();
        List<TableIndicator> tableIndicatorList = new ArrayList<TableIndicator>();
        for (ModelElement element : analyzedTables) {
            NamedColumnSet set = SwitchHelpers.NAMED_COLUMN_SET_SWITCH.doSwitch(element);
            if (set == null) {
                continue;
            }

            TableIndicator currentTableIndicator = new TableIndicator(set);
            Collection<Indicator> indicatorList = analysisHandler.getIndicators(set);
            currentTableIndicator.setIndicators(indicatorList.toArray(new Indicator[indicatorList.size()]));
            tableIndicatorList.add(currentTableIndicator);
        }
        currentTableIndicators = tableIndicatorList.toArray(new TableIndicator[tableIndicatorList.size()]);
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        this.form = managedForm.getForm();
        Composite body = form.getBody();

        body.setLayout(new GridLayout());
        sForm = new SashForm(body, SWT.NULL);
        sForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        topComp = toolkit.createComposite(sForm);
        topComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        topComp.setLayout(new GridLayout());
        form.setText(DefaultMessagesImpl.getString("TableMasterDetailsPage.tableAna")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("TableMasterDetailsPage.analysisMeta")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("TableMasterDetailsPage.setPropOfAnalysis")); //$NON-NLS-1$
        metadataSection = creatMetadataSection(form, topComp);

        createAnalysisTablesSection(form, topComp);

        createDataFilterSection(form, topComp);
        dataFilterComp.addPropertyChangeListener(this);

        createAnalysisParamSection(form, topComp);

        createContextGroupSection(form, topComp);

        // MOD klliu Hide the setting pate graphics 2011-03-11
        if (canShowGraphicsSectionForSettingsPage()) {
            createPreviewComposite();

            createPreviewSection(form, previewComp);
        }

    }

    void createAnalysisTablesSection(final ScrolledForm form1, Composite anasisDataComp) {
        analysisTableSection = createSection(form1, anasisDataComp,
                DefaultMessagesImpl.getString("TableMasterDetailsPage.analyzeTable"), null); //$NON-NLS-1$

        Composite topComp1 = toolkit.createComposite(analysisTableSection);
        topComp1.setLayout(new GridLayout());
        // ~ MOD mzhao 2009-05-05,Bug 6587.
        createConnBindWidget(topComp1);
        // ~

        Composite actionBarComp = toolkit.createComposite(topComp1);
        GridLayout gdLayout = new GridLayout();
        gdLayout.numColumns = 3;
        actionBarComp.setLayout(gdLayout);

        ImageHyperlink collapseAllImageLink = toolkit.createImageHyperlink(actionBarComp, SWT.NONE);
        collapseAllImageLink.setToolTipText(DefaultMessagesImpl.getString("TableMasterDetailsPage.collapseAllTables")); //$NON-NLS-1$
        WorkbenchUtils.setHyperlinkImage(collapseAllImageLink, ImageLib.getImage(ImageLib.COLLAPSE_ALL));
        collapseAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                TreeItem[] items = treeViewer.getTree().getItems();
                expandTreeItems(items, false);
                packOtherColumns();
            }
        });

        ImageHyperlink expandAllImageLink = toolkit.createImageHyperlink(actionBarComp, SWT.NONE);
        expandAllImageLink.setToolTipText(DefaultMessagesImpl.getString("TableMasterDetailsPage.expandAllTables")); //$NON-NLS-1$
        WorkbenchUtils.setHyperlinkImage(expandAllImageLink, ImageLib.getImage(ImageLib.EXPAND_ALL));
        expandAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                TreeItem[] items = treeViewer.getTree().getItems();
                expandTreeItems(items, true);
                packOtherColumns();

            }
        });

        Button clmnBtn = toolkit.createButton(actionBarComp, DefaultMessagesImpl.getString("TableMasterDetailsPage.selectTable"), //$NON-NLS-1$
                SWT.NONE);
        clmnBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                openTableSelectionDialog();
            }
        });

        Composite treeComp = toolkit.createComposite(topComp1, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(treeComp);
        treeComp.setLayout(new GridLayout());
        ((GridData) treeComp.getLayoutData()).heightHint = TREE_MAX_LENGTH;
        treeComp.setBackgroundMode(SWT.INHERIT_DEFAULT);

        treeViewer = new AnalysisTableTreeViewer(treeComp, this);
        treeViewer.setDirty(false);
        treeViewer.addPropertyChangeListener(this);
        analysisTableSection.setClient(topComp1);

    }

    /**
     * DOC xqliu Comment method "packOtherColumns".
     */
    private void packOtherColumns() {
        TreeColumn[] columns = treeViewer.getTree().getColumns();
        for (TreeColumn column : columns) {
            column.pack();
        }
    }

    /**
     * DOC xqliu Comment method "expandTreeItems".
     * 
     * @param items
     * @param expandOrCollapse
     */
    private void expandTreeItems(TreeItem[] items, boolean expandOrCollapse) {
        for (TreeItem item : items) {
            item.setExpanded(expandOrCollapse);
            TreeItem[] its = item.getItems();
            if (its != null && its.length > 0) {
                expandTreeItems(its, expandOrCollapse);
            }
        }
    }

    public void openTableSelectionDialog() {
        if (!RepositoryNodeHelper.isOpenDQCommonViewer()) {
            if (RepositoryNodeHelper.getDQCommonViewer(true) == null) {
                MessageDialog
                        .openWarning(
                                this.getSite().getShell(),
                                DefaultMessagesImpl.getString("TableMasterDetailsPage.WarningDialogTitle"), DefaultMessagesImpl.getString("TableMasterDetailsPage.WarningDialogCantFindNode")); //$NON-NLS-1$ //$NON-NLS-2$
                return;
            }
        }
        TableIndicator[] tableIndicators = treeViewer.getTableIndicator();
        List<IRepositoryNode> setList = new ArrayList<IRepositoryNode>();
        for (TableIndicator tableIndicator : tableIndicators) {
            RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(tableIndicator.getColumnSet());
            if (recursiveFind == null) {
                recursiveFind = RepositoryNodeHelper.createRepositoryNode(tableIndicator.getColumnSet());
            }
            setList.add(recursiveFind);
        }

        RepositoryNode connComboSelectNode = getConnComboSelectNode();
        TablesSelectionDialog dialog = new TablesSelectionDialog(this, null,
                DefaultMessagesImpl.getString("TableMasterDetailsPage.tableSelection"), setList, DefaultMessagesImpl //$NON-NLS-1$
                        .getString("TableMasterDetailsPage.tableSelections"), connComboSelectNode); //$NON-NLS-1$
        dialog.setTableType(TableSelectionType.ALL);
        if (dialog.open() == Window.OK) {
            Object[] tables = dialog.getResult();
            treeViewer.setInput(tables);
            return;
        }
    }

    @Override
    public void createPreviewCharts(final ScrolledForm form1, final Composite composite) {
        previewChartList = new ArrayList<ExpandableComposite>();
        dynamicList.clear();
        for (final TableIndicator tableIndicator : this.treeViewer.getTableIndicator()) {
            final NamedColumnSet set = tableIndicator.getColumnSet();
            ExpandableComposite exComp = toolkit.createExpandableComposite(composite, ExpandableComposite.TREE_NODE
                    | ExpandableComposite.CLIENT_INDENT);
            // bug 10541 modify by zshen,Change some character set to be proper to add view in the table anasys
            if (tableIndicator.isTable()) {
                exComp.setText(DefaultMessagesImpl.getString("TableMasterDetailsPage.table") + set.getName()); //$NON-NLS-1$
            } else {
                exComp.setText(DefaultMessagesImpl.getString("TableMasterDetailsPage.view") + set.getName()); //$NON-NLS-1$
            }
            exComp.setLayout(new GridLayout());
            exComp.setLayoutData(new GridData(GridData.FILL_BOTH));
            exComp.setData(tableIndicator);
            previewChartList.add(exComp);

            final Composite comp = toolkit.createComposite(exComp);
            comp.setLayout(new GridLayout());
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));

            exComp.setExpanded(true);
            exComp.setClient(comp);

            if (tableIndicator.getIndicators().length != 0) {
                IRunnableWithProgress rwp = new IRunnableWithProgress() {

                    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        monitor.beginTask(DefaultMessagesImpl.getString("TableMasterDetailsPage.createPreview") //$NON-NLS-1$
                                + set.getName(), IProgressMonitor.UNKNOWN);
                        Display.getDefault().syncExec(new Runnable() {

                            public void run() {
                                Map<EIndicatorChartType, List<TableIndicatorUnit>> indicatorComposite = CompositeIndicator
                                        .getInstance().getTableIndicatorComposite(tableIndicator);
                                for (EIndicatorChartType chartType : indicatorComposite.keySet()) {
                                    List<TableIndicatorUnit> units = indicatorComposite.get(chartType);
                                    if (!units.isEmpty()) {
                                        final IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartStateOfTableAna(
                                                chartType, units, tableIndicator);
                                        // get all indicator lists separated by chart, and only
                                        // WhereRuleStatisticsStateTable can get not-null charts
                                        List<List<Indicator>> pagedIndicators = ((WhereRuleStatisticsStateTable) chartTypeState)
                                                .getPagedIndicators();
                                        // Added TDQ-9241: for each list(for each chart), check if the current
                                        // list has been registered dynamic event
                                        List<Object> datasets = new ArrayList<Object>();
                                        for (List<Indicator> oneChart : pagedIndicators) {
                                            IEventReceiver event = EventManager.getInstance().findRegisteredEvent(
                                                    oneChart.get(0), EventEnum.DQ_DYMANIC_CHART, 0);
                                            if (event != null) {
                                                // get the dataset from the event
                                                Object dataset = ((TableDynamicChartEventReceiver) event).getDataset();
                                                // if there has the dataset for the current rule, use it to replace,
                                                // (only happen when first switch from master to result page, during
                                                // one running)
                                                if (dataset != null) {
                                                    datasets.add(dataset);
                                                }

                                            }// ~
                                        }
                                        // create chart
                                        List<Object> charts = null;
                                        if (datasets.size() > 0) {
                                            charts = chartTypeState.getChartList(datasets);
                                        } else {
                                            charts = chartTypeState.getChartList();
                                        }
                                        int index = 0;

                                        if (charts != null) {
                                            for (Object chart : charts) {
                                                Object chartComp = TOPChartUtils.getInstance().createChartComposite(comp,
                                                        SWT.NONE, chart, true);

                                                // Added TDQ-8787 20140707 yyin: create and store the dynamic model
                                                DynamicIndicatorModel dyModel = AnalysisUtils.createDynamicModel(chartType,
                                                        pagedIndicators.get(index++), chart);
                                                dynamicList.add(dyModel);
                                                // ~

                                                TOPChartUtils.getInstance().addListenerToChartComp(chartComp,
                                                        chartTypeState.getReferenceLink(),
                                                        DefaultMessagesImpl.getString("TableMasterDetailsPage.what")); //$NON-NLS-1$
                                            }
                                        }
                                    }
                                }
                            }
                        });

                        monitor.done();
                    }

                };

                try {
                    new ProgressMonitorDialog(getSite().getShell()).run(true, false, rwp);
                } catch (Exception ex) {
                    log.error(ex, ex);
                }
            }

            exComp.addExpansionListener(new ExpansionAdapter() {

                @Override
                public void expansionStateChanged(ExpansionEvent e) {
                    getChartComposite().layout();
                    form1.reflow(true);
                    composite.pack();
                }

            });

            getChartComposite().layout();
            form1.reflow(true);
            composite.pack();
        }
        if (!previewChartList.isEmpty()) {
            this.previewChartCompsites = previewChartList.toArray(new Composite[previewChartList.size()]);
        }
    }

    @Override
    public Composite getChartComposite() {
        return chartComposite;
    }

    public Composite[] getPreviewChartCompsites() {
        // ADD msjian TDQ-6213 2012-12-18: filter the disposed composite
        if (previewChartList != null && !previewChartList.isEmpty()) {
            List<ExpandableComposite> withOutDisposed = new ArrayList<ExpandableComposite>();
            for (ExpandableComposite com : previewChartList) {
                if (!com.isDisposed()) {
                    withOutDisposed.add(com);
                }
            }
            this.previewChartCompsites = withOutDisposed.toArray(new ExpandableComposite[withOutDisposed.size()]);
        }
        // TDQ-6213~
        return previewChartCompsites;
    }

    public void setPreviewChartCompsites(Composite[] previewChartCompsites) {
        this.previewChartCompsites = previewChartCompsites;
    }

    @Override
    protected ReturnCode canRun() {
        TableIndicator[] tableIndicators = treeViewer.getTableIndicator();
        if (tableIndicators == null || tableIndicators.length == 0) {
            return new ReturnCode(DefaultMessagesImpl.getString("TableMasterDetailsPage.NoTableAssigned"), false); //$NON-NLS-1$
        }
        for (TableIndicator tableIndicator : tableIndicators) {
            if (tableIndicator.getIndicators().length == 0) {
                return new ReturnCode(DefaultMessagesImpl.getString("TableMasterDetailsPage.NoIndicatorAssigned"), false); //$NON-NLS-1$
            }
        }
        return new ReturnCode(true);
    }

    @Override
    public ReturnCode canSave() {
        // MOD by gdbu 2011-3-21 bug 19179
        ReturnCode canModRetCode = super.canSave();
        if (!canModRetCode.isOk()) {
            return canModRetCode;
        }
        // ~19179

        return new ReturnCode(true);
    }

    @Override
    public void saveAnalysis() throws DataprofilerCoreException {
        // ADD gdbu 2011-3-3 bug 19179
        for (Domain domain : getCurrentModelElement().getParameters().getDataFilter()) {
            domain.setName(getCurrentModelElement().getName());
        }
        // ~

        analysisHandler.clearAnalysis();
        TableIndicator[] tableIndicators = treeViewer.getTableIndicator();
        Connection tdProvider = null;
        Analysis analysis = analysisHandler.getAnalysis();
        analysis.getParameters().setExecutionLanguage(ExecutionLanguage.get(execLang));
        if (tableIndicators != null && tableIndicators.length != 0) {

            tdProvider = ConnectionHelper.getDataProvider(tableIndicators[0].getColumnSet());
            if (tdProvider.eIsProxy()) {
                // Resolve the connection again
                tdProvider = (Connection) EObjectHelper.resolveObject(tdProvider);
            }
            analysis.getContext().setConnection(tdProvider);
            for (TableIndicator tableIndicator : tableIndicators) {
                analysisHandler.addIndicator(tableIndicator.getColumnSet(), tableIndicator.getIndicators());
            }
        } else {
            analysis.getContext().setConnection(null);
        }
        analysisHandler.setStringDataFilter(dataFilterComp.getDataFilterString());

        // save the number of connections per analysis
        this.saveNumberOfConnectionsPerAnalysis();

        // 2011.1.12 MOD by zhsne to unify anlysis and connection id when saving.
        this.nameText.setText(analysisHandler.getName());
        // TDQ-5581,if has removed rules,should remove dependency each other before saving.
        // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
        ReturnCode saved = ElementWriterFactory.getInstance().createAnalysisWrite()
                .save(getCurrentRepNode().getObject().getProperty().getItem(), true);
        // MOD yyi 2012-02-03 TDQ-3602:Avoid to rewriting all analyzes after saving, no reason to update all analyzes
        // which is depended in the referred connection.
        // Extract saving log function.
        // @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#logSaved(ReturnCode)
        logSaved(saved);

        treeViewer.setDirty(false);
        dataFilterComp.setDirty(false);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) currentEditor).firePropertyChange(IEditorPart.PROP_DIRTY);
            ((AnalysisEditor) currentEditor).setRefreshResultPage(true);
        } else if (PluginConstant.DATAFILTER_PROPERTY.equals(evt.getPropertyName())) {
            this.analysisHandler.setStringDataFilter((String) evt.getNewValue());
        }
    }

    @Override
    public void refreshGraphicsInSettingsPage() {
        if (EditorPreferencePage.isHideGraphicsSectionForSettingsPage()) {
            if (sForm.getChildren().length > 1) {
                if (null != sForm.getChildren()[1] && !sForm.getChildren()[1].isDisposed()) {
                    sForm.getChildren()[1].dispose();
                }
                topComp.getParent().layout();
                topComp.layout();
            }
        } else {
            if (chartComposite != null && !chartComposite.isDisposed()) {
                try {
                    for (Control control : chartComposite.getChildren()) {
                        control.dispose();
                    }
                    createPreviewCharts(form, chartComposite);
                    chartComposite.getParent().layout();
                    chartComposite.layout();
                } catch (Exception ex) {
                    log.error(ex, ex);
                }
            } else {
                createPreviewComposite();
                createPreviewSection(form, previewComp);
                createPreviewCharts(form, chartComposite);
            }
        }
    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || (treeViewer != null && treeViewer.isDirty())
                || (dataFilterComp != null && dataFilterComp.isDirty());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.treeViewer != null) {
            this.treeViewer.removePropertyChangeListener(this);
        }
        if (dataFilterComp != null) {
            this.dataFilterComp.removePropertyChangeListener(this);
        }
    }

    @Override
    public TableAnalysisHandler getAnalysisHandler() {
        return analysisHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#getTreeViewer()
     */
    @Override
    public AbstractColumnDropTree getTreeViewer() {
        return this.treeViewer;
    }

    @Override
    List<ExpandableComposite> getPreviewChartList() {
        return this.previewChartList;
    }

    @Override
    String getExpandString() {
        return DefaultMessagesImpl.getString("TableMasterDetailsPage.expandAllTables"); //$NON-NLS-1$
    }

    @Override
    String getCollapseAllString() {
        return DefaultMessagesImpl.getString("TableMasterDetailsPage.collapseAllTables");//$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.DynamicAnalysisMasterPage#getDynamicDatasets()
     */
    @Override
    public List<DynamicIndicatorModel> getDynamicDatasets() {

        return dynamicList;
    }

    @Override
    public void clearDynamicDatasets() {
        super.clearDynamicDatasets();
        this.dynamicList.clear();
    }

    @Override
    protected DynamicChartEventReceiver createEventReceiver(DynamicIndicatorModel indModel, int index, Indicator oneIndicator) {
        TableDynamicChartEventReceiver eReceiver = new TableDynamicChartEventReceiver();
        eReceiver.setDataset(indModel.getDataset());
        eReceiver.setIndexInDataset(index);
        eReceiver.setIndicatorName(oneIndicator.getName());
        eReceiver.setIndicator(oneIndicator);
        eReceiver.setIndicatorType(IndicatorEnum.findIndicatorEnum(oneIndicator.eClass()));
        // clear data
        eReceiver.clearValue();
        return eReceiver;
    }

}
