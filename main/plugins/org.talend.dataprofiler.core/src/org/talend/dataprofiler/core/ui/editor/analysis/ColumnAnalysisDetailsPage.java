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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.dynamic.DynamicIndicatorModel;
import org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnTreeViewer;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.utils.pagination.UIPagination;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.mapdb.MapDBManager;
import org.talend.dataquality.indicators.sql.UserDefIndicator;
import org.talend.dq.analysis.ModelElementAnalysisHandler;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.UDIHelper;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author rli
 */
public class ColumnAnalysisDetailsPage extends DynamicAnalysisMasterPage {

    private Composite tree;

    private Composite navigationComposite;

    private Composite drillDownComposite;

    AnalysisColumnTreeViewer treeViewer;

    ModelElementAnalysisHandler analysisHandler;

    private static final int TREE_MAX_LENGTH = 400;

    private ExpandableComposite[] previewChartCompsites;

    private Section analysisColumnSection = null;

    final private List<ExpandableComposite> previewChartList = new ArrayList<ExpandableComposite>();

    private UIPagination uiPagination;

    private int lastTimePageNumber = 1;

    public ColumnAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        currentEditor = (AnalysisEditor) editor;
    }

    @Override
    public void initialize(FormEditor editor) {
        super.initialize(editor);
        recomputeIndicators();
    }

    public void recomputeIndicators() {
        analysisHandler = new ModelElementAnalysisHandler();
        analysisHandler.setAnalysis(getCurrentModelElement());
        // Handle JUDIs
        UDIHelper.updateJUDIsForAnalysis(getCurrentModelElement());

        stringDataFilter = analysisHandler.getStringDataFilterwithContext();
        EList<ModelElement> analyzedColumns = analysisHandler.getAnalyzedColumns();
        List<ModelElementIndicator> meIndicatorList = new ArrayList<ModelElementIndicator>();
        ModelElementIndicator currentIndicator = null;
        for (ModelElement element : analyzedColumns) {
            TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(element);

            // MOD qiongli 2011-1-10,16796:delimitefile
            MetadataColumn mdColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(element);
            if (tdColumn == null && mdColumn == null) {
                continue;
            }
            // MOD qiongli TDQ-7052 if the node is filtered ,it will be return null,so should create a new node.
            RepositoryNode repNode = RepositoryNodeHelper.recursiveFind(element);
            if (repNode == null) {
                repNode = RepositoryNodeHelper.createRepositoryNode(element);
            }
            // MOD mzhao feature 15750, The column is recompute from the file, here create a new repository view object.
            if (tdColumn == null && mdColumn != null) {
                currentIndicator = ModelElementIndicatorHelper.createDFColumnIndicator(repNode);
            } else if (tdColumn != null) {
                currentIndicator = ModelElementIndicatorHelper.createModelElementIndicator(repNode);
            }

            DataminingType dataminingType = DataminingType.get(analysisHandler.getDatamingType(element));
            MetadataHelper.setDataminingType(dataminingType == null ? DataminingType.NOMINAL : dataminingType, element);
            Collection<Indicator> indicatorList = analysisHandler.getIndicators(element);
            if (currentIndicator != null) {
                currentIndicator.setIndicators(indicatorList.toArray(new Indicator[indicatorList.size()]));
                meIndicatorList.add(currentIndicator);
            }
        }
        currentModelElementIndicators = meIndicatorList.toArray(new ModelElementIndicator[meIndicatorList.size()]);
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

        form.setText(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.columnAna")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.analysisMeta")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.setPropOfAnalysis")); //$NON-NLS-1$
        metadataSection = creatMetadataSection(form, topComp);

        createDataPreviewSection(form, topComp, true, true, true);
        createAnalysisColumnsSection(form, topComp);
        createDataFilterSection(form, topComp);
        dataFilterComp.addPropertyChangeListener(this);

        // MOD xqliu 2009-07-01 bug 7068
        createExecuteEngineSection(form, topComp, analysisHandler.getAnalyzedColumns(), analysisHandler.getAnalysis()
                .getParameters());
        // ~

        createContextGroupSection(form, topComp);

        if (!EditorPreferencePage.isHideGraphicsSectionForSettingsPage()) {
            createPreviewComposite();

            createPreviewSection(form, previewComp);
        }
    }

    void createAnalysisColumnsSection(final ScrolledForm form1, Composite anasisDataComp) {
        analysisColumnSection = createSection(form1, anasisDataComp,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.analyzeColumn"), null); //$NON-NLS-1$

        Composite topComp1 = toolkit.createComposite(analysisColumnSection, SWT.NONE);
        topComp1.setLayout(new GridLayout());

        Composite actionBarComp = toolkit.createComposite(topComp1, SWT.NONE);
        GridLayout gdLayout = new GridLayout();
        gdLayout.numColumns = 5;
        gdLayout.horizontalSpacing = 20;
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        actionBarComp.setLayoutData(data);
        actionBarComp.setLayout(gdLayout);

        ImageHyperlink collapseAllImageLink = toolkit.createImageHyperlink(actionBarComp, SWT.NONE);
        collapseAllImageLink.setToolTipText(DefaultMessagesImpl.getString("CollapseAllColumns")); //$NON-NLS-1$
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
        expandAllImageLink.setToolTipText(DefaultMessagesImpl.getString("ExpandAllColumns")); //$NON-NLS-1$
        WorkbenchUtils.setHyperlinkImage(expandAllImageLink, ImageLib.getImage(ImageLib.EXPAND_ALL));
        expandAllImageLink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                TreeItem[] items = treeViewer.getTree().getItems();
                expandTreeItems(items, true);
                packOtherColumns();
            }
        });
        createIndicatorSelectButton(actionBarComp);
        createRunButton(actionBarComp);

        navigationComposite = toolkit.createComposite(actionBarComp, SWT.NONE);

        navigationComposite.setLayoutData(data);
        navigationComposite.setLayout(new GridLayout());

        createPaginationTree(topComp1);
        analysisColumnSection.setClient(topComp1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#checkSqlEnginIndicatorExist()
     */
    @Override
    protected boolean checkSqlEngineIndicatorExist() {
        for (ModelElementIndicator modelElementIndicator : this.treeViewer.getModelElementIndicator()) {
            if (modelElementIndicator.contains(IndicatorEnum.SqlPatternMatchingIndicatorEnum)
                    || (modelElementIndicator.contains(IndicatorEnum.UserDefinedIndicatorEnum) && checkUDIInvalid(
                            modelElementIndicator, false))) {
                return true;
            }
        }
        return super.checkSqlEngineIndicatorExist();
    }

    /**
     * DOC zshen Comment method "createPaginationTree".
     * 
     * @param topComp1
     */
    private void createPaginationTree(Composite topComp1) {
        tree = toolkit.createComposite(topComp1, SWT.NONE);
        tree.setBackgroundMode(SWT.INHERIT_DEFAULT);

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tree);
        tree.setLayout(new GridLayout());
        ((GridData) tree.getLayoutData()).heightHint = TREE_MAX_LENGTH;

        treeViewer = new AnalysisColumnTreeViewer(tree, this);
        treeViewer.addObserver(sampleTable);
        sampleTable.addObserver(treeViewer);
        treeViewer.setDirty(false);
        treeViewer.addPropertyChangeListener(this);

        // Added TDQ-9272 : give the execute language to it
        treeViewer.setLanguage(getCurrentModelElement().getParameters().getExecutionLanguage());

        // pagination compoent
        computePagination();
        // ~
    }

    /**
     * Go to last of the page.
     */
    public void goLastPage() {
        uiPagination.goToLastPage();
    }

    /**
     * Restore last time of page.
     */
    public void restorePage() {
        uiPagination.goToPage(lastTimePageNumber + 1);
    }

    /**
     * DOC zshen Comment method "computePagination".
     */
    private void computePagination() {
        disposeChartComposite();

        if (uiPagination == null) {
            uiPagination = new UIPagination(toolkit, navigationComposite);
        } else {
            lastTimePageNumber = uiPagination.getCurrentPageNumber();
            uiPagination.reset();
        }

        final ModelElementIndicator[] modelElementIndicatorArrary = this.getCurrentModelElementIndicators();
        int pageSize = IndicatorPaginationInfo.getPageSize();
        int totalPages = modelElementIndicatorArrary.length / pageSize;
        List<ModelElementIndicator> modelElementIndicatorList = null;

        for (int index = 0; index < totalPages; index++) {
            modelElementIndicatorList = new ArrayList<ModelElementIndicator>();
            for (int idx = 0; idx < pageSize; idx++) {
                modelElementIndicatorList.add(modelElementIndicatorArrary[index * pageSize + idx]);
            }
            IndicatorPaginationInfo pginfo = new MasterPaginationInfo(form, previewChartList, modelElementIndicatorList,
                    uiPagination, treeViewer);
            uiPagination.addPage(pginfo);
        }

        int left = modelElementIndicatorArrary.length % pageSize;
        if (left != 0 || totalPages == 0) {
            modelElementIndicatorList = new ArrayList<ModelElementIndicator>();
            for (int leftIdx = 0; leftIdx < left; leftIdx++) {
                modelElementIndicatorList.add(modelElementIndicatorArrary[totalPages * pageSize + leftIdx]);
            }
            IndicatorPaginationInfo pginfo = new MasterPaginationInfo(form, previewChartList, modelElementIndicatorList,
                    uiPagination, treeViewer);
            uiPagination.addPage(pginfo);
            // FIXME totalPages won't used anymore.
            totalPages++;
        }
        uiPagination.init();
    }

    /**
     * DOC mzhao Comment method "packOtherColumns".
     */
    private void packOtherColumns() {
        TreeColumn[] columns = treeViewer.getTree().getColumns();
        for (TreeColumn column : columns) {
            column.pack();
        }
        treeViewer.getTree().pack();
        treeViewer.getTree().getParent().layout();
    }

    /**
     * 
     * DOC mzhao Comment method "expandTreeItems".
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#setTreeViewInput(java.lang.Object[])
     */
    @Override
    public void setTreeViewInput(Object[] modelElements) {
        // MOD yyi 2012-02-29 TDQ-3605 Empty column table.
        treeViewer.filterInputData(modelElements);
        // RefreshPreviewTable should be first then the tree can be refresh
        refreshPreviewTable(treeViewer.getModelElementIndicator(), false);
        refreshTheTree(treeViewer.getModelElementIndicator());
        this.setDirty(true);
    }

    /**
     * Refresh the preview Table
     * 
     * @param modelElementIndicator
     */
    public void refreshPreviewTable(ModelElementIndicator[] modelElements, boolean loadData) {
        this.currentModelElementIndicators = modelElements;
        this.refreshPreviewTable(loadData);
    }

    /**
     * 
     * Refresh the column tree
     * 
     * @param modelElements
     */
    public void refreshTheTree(ModelElementIndicator[] modelElements) {
        this.currentModelElementIndicators = modelElements;
        this.computePagination();
    }

    @Override
    public void createPreviewCharts(final ScrolledForm form1, final Composite composite) {
        uiPagination.setChartComposite(composite);
        uiPagination.init();

        for (ExpandableComposite comp : previewChartList) {
            registerSection(comp);
        }

        composite.layout();
        composite.pack();
        form1.reflow(true);
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
                disposeChartComposite();
                createPreviewCharts(form, chartComposite);
                reLayoutChartComposite();
            } else {
                createPreviewComposite();
                createPreviewSection(form, previewComp);
                createPreviewCharts(form, chartComposite);
            }
        }
    }

    /**
     * ADD xqliu 2009-08-24 bug 8776.
     * 
     * @return
     */
    protected boolean includeUDI() {
        ModelElementIndicator[] modelElementIndicators = this.getTreeViewer().getModelElementIndicator();
        for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {
            Indicator[] indicators = modelElementIndicator.getIndicators();
            for (Indicator indicator : indicators) {
                if (indicator instanceof UserDefIndicator) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param outputFolder
     * @throws DataprofilerCoreException
     */
    @Override
    public void saveAnalysis() throws DataprofilerCoreException {
        analysisHandler.changeDefaultRowLoaded(rowLoadedText.getText());

        analysisHandler.changeSampleDataShowWay(sampleDataShowWayCombo.getText());

        analysisHandler.clearAnalysis();

        Analysis analysis = analysisHandler.getAnalysis();

        for (Domain domain : getCurrentModelElement().getParameters().getDataFilter()) {
            domain.setName(getCurrentModelElement().getName());
        }

        analysis.getParameters().setExecutionLanguage(ExecutionLanguage.get(execLang));

        // save the number of connections per analysis
        this.saveNumberOfConnectionsPerAnalysis();

        try {
            // MOD zshen feature 12919 to save analysisParameter.
            analysis.getParameters().setMaxNumberRows(Integer.parseInt(maxNumText.getText()));
        } catch (NumberFormatException nfe) {
            MessageDialogWithToggle.openError(null, DefaultMessagesImpl.getString("AbstractAnalysisMetadataPage.SaveAnalysis"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("ColumnMasterDetailsPage.emptyField", //$NON-NLS-1$
                            DefaultMessagesImpl.getString("ColumnMasterDetailsPage.maxNumberLabel"))); //$NON-NLS-1$ 
            maxNumText.setText(String.valueOf(analysis.getParameters().getMaxNumberRows()));
        }
        analysis.getParameters().setStoreData(drillDownCheck.getSelection());
        // ~12919

        ModelElementIndicator[] modelElementIndicators = this.getCurrentModelElementIndicators();
        if (modelElementIndicators != null && modelElementIndicators.length != 0) {
            Connection tdProvider = ModelElementIndicatorHelper.getTdDataProvider(modelElementIndicators[0]);
            if (tdProvider.eIsProxy()) {
                // Resolve the connection again
                tdProvider = (Connection) EObjectHelper.resolveObject(tdProvider);
            }
            analysis.getContext().setConnection(tdProvider);

            for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {
                IRepositoryViewObject reposObject = modelElementIndicator.getModelElementRepositoryNode().getObject();
                ModelElement modelEle = null;
                if (reposObject instanceof MetadataColumnRepositoryObject) {
                    modelEle = ((MetadataColumnRepositoryObject) reposObject).getTdColumn();
                }
                analysisHandler.addIndicator(modelEle, modelElementIndicator.getIndicators());
                DataminingType type = MetadataHelper.getDataminingType(modelEle);
                if (type == null) {
                    type = MetadataHelper.getDefaultDataminingType(modelElementIndicator.getJavaType());
                }
                analysisHandler.setDatamingType(type.getLiteral(), modelEle);
            }
        } else {
            analysis.getContext().setConnection(null);
        }

        analysisHandler.setStringDataFilter(dataFilterComp.getDataFilterString());
        // 2011.1.12 MOD by zshen to unify anlysis and connection id when saving.

        this.nameText.setText(analysisHandler.getName());
        TaggedValueHelper.setTaggedValue(getCurrentModelElement(), TaggedValueHelper.IS_USE_SAMPLE_DATA,
                isRunWithSampleData.toString());
        // TDQ-5581,if has removed emlements(patten/udi),should remove dependency each other before saving.
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
            // synNagivatorStat();
        } else if (PluginConstant.DATAFILTER_PROPERTY.equals(evt.getPropertyName())) {
            this.analysisHandler.setStringDataFilter((String) evt.getNewValue());
        } else if (PluginConstant.EXPAND_TREE.equals(evt.getPropertyName())) {

            ModelElementIndicator indicator = (ModelElementIndicator) ((Widget) evt.getNewValue())
                    .getData(AbstractColumnDropTree.MODELELEMENT_INDICATOR_KEY);
            expandChart(indicator);
        }
    }

    /**
     * DOC zshen Comment method "synNagivatorStat".
     */
    public void synNagivatorStat() {
        if (this.uiPagination != null) {
            this.uiPagination.synNagivatorState(this.treeViewer.getModelElementIndicator());
            this.currentModelElementIndicators = this.uiPagination.getAllTheModelElementIndicator();
        }
    }

    /**
     * DOC yyi 2011-06-02 16929:expand the selected column in the graphical chart.
     * 
     * @param indicator
     */
    protected void expandChart(ModelElementIndicator indicator) {
        // MOD klliu bug 22407 check the uiPagination is null.
        if (uiPagination == null) {
            return;
        }

        ExpandableComposite[] previewChartCompsitesa = getPreviewChartCompsites();
        if (previewChartCompsitesa != null) {
            for (ExpandableComposite comp : previewChartCompsitesa) {
                if (comp.getData() != indicator) {
                    comp.setExpanded(false);
                    comp.getParent().pack();
                }
            }
        }
    }

    /**
     * DOC yyi 2011-06-02 16929:get index of the column selected in the tree viewer.
     */
    protected int indexOfSelectedItem(ModelElementIndicator data) {
        int index = -1;
        for (int i = 0; i < treeViewer.getModelElementIndicator().length; i++) {
            if (data == treeViewer.getModelElementIndicator()[i]) {
                index = i + 1;
                break;
            }
        }
        return index;
    }

    @Override
    public boolean isDirty() {
        if (treeViewer == null ? false : treeViewer.isDirty()) {
            this.setDirty(treeViewer.isDirty());
        }
        if (dataFilterComp == null ? false : dataFilterComp.isDirty()) {
            this.setDirty(dataFilterComp.isDirty());
        }
        return super.isDirty();
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
        EventManager.getInstance().clearEvent(dataPreviewSection, EventEnum.DQ_SELECT_ELEMENT_AFTER_CREATE_CONNECTION);
        if (AnalysisHelper.isJavaExecutionEngine(getCurrentModelElement())) {
            MapDBManager.getInstance().closeDB(getCurrentModelElement());
        }

        // when the user didn't save, revert the connection combo value
        if (oldConn != null && isDirty()) {
            getCurrentModelElement().getContext().setConnection(oldConn);
        }

        if (this.getSampleTable().getExistPreviewData() != null) {
            this.getSampleTable().getExistPreviewData().clear();
        }
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
    public ModelElementAnalysisHandler getAnalysisHandler() {
        return analysisHandler;
    }

    public ExpandableComposite[] getPreviewChartCompsites() {
        if (previewChartList != null && !previewChartList.isEmpty()) {
            // ADD msjian TDQ-6213 2012-12-18: filter the disposed composite
            List<ExpandableComposite> withOutDisposed = new ArrayList<ExpandableComposite>();
            for (ExpandableComposite com : previewChartList) {
                if (!com.isDisposed()) {
                    withOutDisposed.add(com);
                }
            }
            // TDQ-6213~
            this.previewChartCompsites = withOutDisposed.toArray(new ExpandableComposite[withOutDisposed.size()]);
        }
        return previewChartCompsites;
    }

    @Override
    public Composite getChartComposite() {
        return chartComposite;
    }

    @Override
    protected ReturnCode canRun() {
        // ADD xqliu 2010-01-22 bug 11200
        ReturnCode checkMdmExecutionEngine = checkMdmExecutionEngine();
        if (!checkMdmExecutionEngine.isOk()) {
            return checkMdmExecutionEngine;
        }

        if (checkJUDIUpdate()) {
            recomputeIndicators();
            computePagination();
        }
        // ~
        ModelElementIndicator[] modelElementIndicators = treeViewer.getModelElementIndicator();
        if (modelElementIndicators == null || modelElementIndicators.length == 0) {
            return new ReturnCode(DefaultMessagesImpl.getString(
                    "ColumnMasterDetailsPage.NoColumnAssigned", analysisHandler.getName()), false); //$NON-NLS-1$
        }
        for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {
            if (modelElementIndicator.getIndicators().length == 0) {
                return new ReturnCode(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.NoIndicatorAssigned"), false); //$NON-NLS-1$
            }
        }

        return new ReturnCode(true);
    }

    /**
     * check whether JUDI need to update
     * 
     * @return
     */
    private boolean checkJUDIUpdate() {
        ModelElementIndicator[] modelElementIndicators = this.getTreeViewer().getModelElementIndicator();
        for (ModelElementIndicator modelElementIndicator : modelElementIndicators) {
            Indicator[] indicators = modelElementIndicator.getIndicators();
            for (Indicator indicator : indicators) {
                if (UDIHelper.isJavaUDI(indicator) && UDIHelper.needUpdateJUDI(indicator)) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public ReturnCode canSave() {
        // MOD by gdbu 2011-3-21 bug 19179
        ReturnCode canModRetCode = super.canSave();
        if (!canModRetCode.isOk()) {
            return canModRetCode;
        }
        // ~19179

        List<ModelElement> analyzedElement = new ArrayList<ModelElement>();
        for (ModelElementIndicator modelElementIndicator : treeViewer.getModelElementIndicator()) {
            IRepositoryViewObject modelElementRepositoryObj = modelElementIndicator.getModelElementRepositoryNode().getObject();
            if (modelElementRepositoryObj instanceof MetadataColumnRepositoryObject) {
                analyzedElement.add(((MetadataColumnRepositoryObject) modelElementRepositoryObj).getTdColumn());
            }
        }

        if (!analyzedElement.isEmpty()) {
            if (!ModelElementHelper.isFromSameTable(analyzedElement) && !"".equals(dataFilterComp.getDataFilterString())) { //$NON-NLS-1$
                return new ReturnCode(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.CannotCreatAnalysis"), false); //$NON-NLS-1$
            }
        }
        if (!this.isValidateRowCount()) {
            return new ReturnCode(DefaultMessagesImpl.getString("MatchMasterDetailsPage.LoadedRowCountError"), false); //$NON-NLS-1$
        }

        return new ReturnCode(true);
    }

    /**
     * DOC xqliu Comment method "checkMdmExecutionEngine".
     */
    private ReturnCode checkMdmExecutionEngine() {
        ModelElementIndicator[] modelElementIndicators = treeViewer.getModelElementIndicator();
        if (modelElementIndicators != null && modelElementIndicators.length != 0) {
            getCurrentModelElement().getContext().setConnection(
                    ModelElementIndicatorHelper.getTdDataProvider(modelElementIndicators[0]));
        }
        return new ReturnCode(true);
    }

    /**
     * Judge whether there are java indicator exist.
     * 
     * @return whether there are java indicator exist.
     */
    @Override
    protected boolean includeJavaEngineIndicator() {
        for (ModelElementIndicator modelElementIndicator : this.treeViewer.getModelElementIndicator()) {
            if (modelElementIndicator.containsAny(IndicatorEnum.getJavaIndicatorsEnum())
                    || (modelElementIndicator.contains(IndicatorEnum.UserDefinedIndicatorEnum) && checkUDIInvalid(
                            modelElementIndicator, true))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Judge whether there are exist invalid udi
     * 
     * @param modelElementIndicator
     * @return
     */
    private boolean checkUDIInvalid(ModelElementIndicator modelElementIndicator, boolean isExist) {
        for (IndicatorUnit indicatorUnit : modelElementIndicator.getIndicatorUnits()) {
            boolean isExistSqlExpr = false;
            boolean sqlCaseNormal = false;
            if (IndicatorEnum.UserDefinedIndicatorEnum == indicatorUnit.getType()) {
                // mean that is there exist sql expression
                if (!indicatorUnit.getIndicator().getIndicatorDefinition().getSqlGenericExpression().isEmpty()) {
                    isExistSqlExpr = true;
                    DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(this.getCurrentModelElement(),
                            ExecutionLanguage.SQL);
                    if (dbmsLanguage.getSqlExpression(indicatorUnit.getIndicator().getIndicatorDefinition()) != null) {
                        sqlCaseNormal = true;
                    }
                }

                if (isExist && !(isExistSqlExpr && sqlCaseNormal)) {
                    // case1:find JUDI exist and isExist=true then return true
                    return true;
                } else if (!isExist && !UDIHelper.isJavaUDI(indicatorUnit.getIndicator())) {
                    // case2:find JUDI is not exist and isExist=false then return true
                    return true;
                }
            }
        }
        // other case(both valid)
        return false;
    }

    @Override
    public ExecutionLanguage getUIExecuteEngin() {
        return ExecutionLanguage.get(execCombo.getText());
    }

    /**
     * set the Language To TreeViewer.
     * 
     * @param executionLanguage
     */
    @Override
    protected void setLanguageToTreeViewer(ExecutionLanguage executionLanguage) {
        treeViewer.setLanguage(executionLanguage);
    }

    /**
     * 
     * refresh current Tree Viewer
     * 
     * @param result
     */
    public void refreshCurrentTreeViewer(ModelElementIndicator[] result) {
        if (result.length > 0) {
            lastTimePageNumber = uiPagination.getCurrentPageNumber();
            refreshTheTree(result);
            // the number of current page is from 0 to n so need to add one when we use it.
            uiPagination.goToPage(lastTimePageNumber + 1);
            treeViewer.setElements(treeViewer.getModelElementIndicator(), true, true);
        }
    }

    @Override
    List<ExpandableComposite> getPreviewChartList() {
        return this.previewChartList;
    }

    @Override
    String getExpandString() {
        return DefaultMessagesImpl.getString("ExpandAllColumns"); //$NON-NLS-1$
    }

    @Override
    String getCollapseAllString() {
        return DefaultMessagesImpl.getString("CollapseAllColumns"); //$NON-NLS-1$
    }

    /**
     * return all created charts for the current running, from the current pagination. The charts which are not on the
     * current page no need to return. TODO check if can use IndicatorUnit
     */
    @Override
    public List<DynamicIndicatorModel> getDynamicDatasets() {
        return uiPagination.getAllIndcatorAndDatasetOfCurrentPage();
    }

    /**
     * clear all related maps which store the dynamic state and charts.
     */
    @Override
    public void clearDynamicDatasets() {
        uiPagination.clearAllDynamicMapOfCurrentPage();
        super.clearDynamicDatasets();
    }

    /**
     * DOC msjian Comment method "createDrillDownPart".
     * 
     * @param anaParameters
     * @param comp2
     * @param executionLanguage
     */
    @Override
    protected void createDrillDownPart(AnalysisParameters anaParameters, Composite comp2, ExecutionLanguage executionLanguage) {
        // MOD zshen feature 12919 : add allow drill down and max number row component for java engine.
        drillDownComposite = createDrillDownComposite(comp2, anaParameters);
        if (ExecutionLanguage.SQL.equals(executionLanguage)) {
            drillDownComposite.setVisible(false);
            showDrillDownComposite(drillDownComposite, 10);
        }
        addListenerToExecuteEngine(execCombo, drillDownComposite);
    }

    @Override
    protected void doCheckOption() {
        Boolean isSqlSelected = TaggedValueHelper.getValueBoolean(TaggedValueHelper.IS_SQL_ENGIN_BEFORE_CHECK,
                getCurrentModelElement());
        if (isRunWithSampleData) {
            if (currentModelIsSqlEngine()) {
                changeExecuteLanguageToJava(false);
                if (!isSqlSelected) {
                    TaggedValueHelper.setTaggedValue(getCurrentModelElement(), TaggedValueHelper.IS_SQL_ENGIN_BEFORE_CHECK,
                            "true"); //$NON-NLS-1$
                }
            }
            execCombo.setEnabled(false);
        } else {
            if (isSqlSelected) {
                changeExecuteLanguageToSql(true);
                if (!isRunWithSampleData) {// java engin exchange failed so that don't change this attribute
                    TaggedValueHelper.setTaggedValue(getCurrentModelElement(), TaggedValueHelper.IS_SQL_ENGIN_BEFORE_CHECK,
                            "false"); //$NON-NLS-1$
                }
            }
            if (!isFileConnection()) {
                execCombo.setEnabled(true);
            }
        }

    }

    /**
     * DOC zshen Comment method "isFileConnection".
     * 
     * @return
     */
    private boolean isFileConnection() {
        DataManager connection = getCurrentModelElement().getContext().getConnection();
        if (FileConnection.class.isInstance(connection)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#refreshEnginSection()
     */
    @Override
    protected void refreshEnginSection() {
        execLang = execCombo.getText();
        ExecutionLanguage currentLanguage = ExecutionLanguage.get(execLang);
        refreshEnginSection(drillDownComposite, currentLanguage);
    }

    /**
     * add zshen feature 12919.
     */
    private Composite createDrillDownComposite(Composite sectionClient, AnalysisParameters anaParameters) {
        Composite javaEnginSection = toolkit.createComposite(sectionClient);
        Composite checkSection = toolkit.createComposite(javaEnginSection);
        Composite numberSection = toolkit.createComposite(javaEnginSection);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;

        GridDataFactory.fillDefaults().grab(true, false).span(2, 0).align(SWT.FILL, SWT.BEGINNING).applyTo(javaEnginSection);
        GridDataFactory.fillDefaults().grab(true, false).span(2, 0).align(SWT.FILL, SWT.BEGINNING).applyTo(checkSection);
        GridDataFactory.fillDefaults().grab(true, false).span(2, 0).align(SWT.FILL, SWT.BEGINNING).applyTo(numberSection);

        javaEnginSection.setLayout(gridLayout);
        checkSection.setLayout(gridLayout);
        numberSection.setLayout(gridLayout);
        toolkit.createLabel(checkSection, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.allowDrillDownLabel"));//$NON-NLS-1$
        drillDownCheck = toolkit.createButton(checkSection, "", SWT.CHECK);//$NON-NLS-1$
        drillDownCheck.setSelection(true);
        drillDownCheck.setSelection(anaParameters.isStoreData());
        drillDownCheck.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
            }

        });
        Label maxNumLabel = toolkit.createLabel(numberSection,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.maxNumberLabel")); //$NON-NLS-1$
        maxNumText = toolkit.createText(numberSection, null, SWT.BORDER);
        maxNumText.setText(String.valueOf(anaParameters.getMaxNumberRows()));
        maxNumText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }

        });
        maxNumText.addVerifyListener(new VerifyListener() {

            public void verifyText(VerifyEvent e) {
                String inputValue = e.text;
                Pattern pattern = Pattern.compile("^[0-9]"); //$NON-NLS-1$
                char[] charArray = inputValue.toCharArray();
                for (char c : charArray) {
                    if (!pattern.matcher(String.valueOf(c)).matches()) {
                        e.doit = false;
                    }
                }
            }
        });
        GridDataFactory.fillDefaults().grab(true, false).applyTo(maxNumText);
        GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(maxNumLabel);
        GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(drillDownCheck);
        return javaEnginSection;
    }

    private void showDrillDownComposite(Composite drillDownComposite, int height) {
        GridData data = (GridData) drillDownComposite.getLayoutData();
        data.heightHint = height;
        drillDownComposite.setLayoutData(data);
    }

    private void addListenerToExecuteEngine(final CCombo execCombo1, final Composite javaEnginSection) {
        execCombo1.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                // MOD xqliu 2009-08-24 bug 8776
                execLang = execCombo1.getText();

                // MOD zshen 11104 2010-01-27: when have a datePatternFreqIndicator in the
                // "analyzed Columns",ExecutionLanguage only is Java.
                ExecutionLanguage currentLanguage = ExecutionLanguage.get(execLang);
                if (ExecutionLanguage.SQL.equals(currentLanguage) && includeJavaEngineIndicator()) {
                    String dialogMessage = getNonJavaIndicatorMessage();
                    MessageUI.openWarning(dialogMessage);
                    changeExecuteLanguageToJava(false);
                    execLang = execCombo1.getText();
                    return;
                } else if (ExecutionLanguage.JAVA.equals(currentLanguage) && checkSqlEngineIndicatorExist()) {
                    if (isRunWithSampleData) {
                        MessageUI.openWarning(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.SqlIndicatorExistWarning")); //$NON-NLS-1$
                    } else {
                        MessageUI.openWarning(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.SqlIndicatorExistWarning2")); //$NON-NLS-1$
                    }
                    changeExecuteLanguageToSql(true);
                    execLang = execCombo1.getText();
                    return;
                }
                refreshEnginSection(javaEnginSection, currentLanguage);
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#getNonJavaIndicatorMessage()
     */
    @Override
    protected String getNonJavaIndicatorMessage() {
        DbmsLanguage dbmsLanguage = DbmsLanguageFactory.createDbmsLanguage(
                ColumnAnalysisDetailsPage.this.getCurrentModelElement(), ExecutionLanguage.SQL);
        return DefaultMessagesImpl.getString("ColumnMasterDetailsPage.SqlExprssionNoExistWarning", dbmsLanguage.getDbmsName());//$NON-NLS-1$
    }

    /**
     * DOC zshen Comment method "refreshEnginSection".
     * 
     * @param javaEnginSection
     * @param currentLanguage
     */
    protected void refreshEnginSection(final Composite javaEnginSection, ExecutionLanguage currentLanguage) {
        if (ExecutionLanguage.SQL.equals(currentLanguage)) {
            javaEnginSection.setVisible(false);
            showDrillDownComposite(javaEnginSection, 10);
        } else {
            javaEnginSection.setVisible(true);
            showDrillDownComposite(javaEnginSection, 100);
        }
        // 12919~
        setDirty(true);
        setLanguageToTreeViewer(currentLanguage);
        // ~

        // TDQ-11694 msjian : column analysis change from sql to java engine, can not show java parameters
        // correctly(UI)
        form.reflow(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#createIndicatorSelectButton(org.
     * eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createIndicatorSelectButton(Composite buttonComposite) {
        Button indcBtn = toolkit.createButton(buttonComposite,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.selectIndicatorBtn"), SWT.NONE); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(indcBtn);
        indcBtn.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseDown(MouseEvent e) {
                if (!isValidateRowCount()) {
                    MessageDialog.openWarning(null, DefaultMessagesImpl.getString("MatchMasterDetailsPage.NotValidate"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("MatchMasterDetailsPage.LoadedRowCountError")); //$NON-NLS-1$
                } else {
                    if (currentModelElementIndicators == null || currentModelElementIndicators.length == 0) {
                        MessageDialog.openWarning(null,
                                DefaultMessagesImpl.getString("AnalysisColumnTreeViewer.indicatorSelection"), //$NON-NLS-1$
                                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.CannotOpenSelectIndicatorDialog")); //$NON-NLS-1$
                        return;
                    }
                    ModelElementIndicator[] result = treeViewer.openIndicatorSelectDialog(ColumnAnalysisDetailsPage.this
                            .getSite().getShell());
                    if (result != null) {
                        refreshCurrentTreeViewer(result);
                        if (treeViewer.isGridPreviewColumnMoved()) {
                            refreshPreviewTable(true);
                        }
                    }
                }

            }

        });
    }

}
