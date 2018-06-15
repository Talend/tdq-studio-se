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
package org.talend.dataprofiler.core.ui.editor.analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.action.actions.RunAnalysisAction;
import org.talend.dataprofiler.core.ui.dialog.ColumnsSelectWithConstraintDialog;
import org.talend.dataprofiler.core.ui.dialog.ColumnsSelectionDialog;
import org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnSetTreeViewer;
import org.talend.dataprofiler.core.ui.editor.composite.IndicatorsComp;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnSetIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.CompositeIndicator;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.IndicatorsFactory;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.SimpleStatIndicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.ColumnSetAnalysisHandler;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.DFColumnFolderRepNode;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * @author yyi 2009-12-16
 */
public class ColumnSetAnalysisDetailsPage extends AbstractAnalysisMetadataPage implements PropertyChangeListener {

    private static Logger log = Logger.getLogger(ColumnSetAnalysisDetailsPage.class);

    AnalysisColumnSetTreeViewer treeViewer;

    IndicatorsComp indicatorsViewer;

    ColumnSetAnalysisHandler columnSetAnalysisHandler;

    private SimpleStatIndicator simpleStatIndicator;

    private AllMatchIndicator allMatchIndicator;

    private Composite chartComposite;

    private static final int TREE_MAX_LENGTH = 300;

    private static final int INDICATORS_SECTION_HEIGHT = 300;

    protected Composite[] previewChartCompsites;

    private EList<ModelElement> analyzedColumns;

    private Section analysisColSection;

    private Section previewSection;

    private Section indicatorsSection;

    private Button storeDataCheck;

    private SashForm sForm;

    private Composite previewComp;

    public ColumnSetAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        currentEditor = (AnalysisEditor) editor;
    }

    @Override
    public void initialize(FormEditor editor) {
        super.initialize(editor);
        recomputeIndicators();
    }

    public void recomputeIndicators() {
        columnSetAnalysisHandler = new ColumnSetAnalysisHandler();
        columnSetAnalysisHandler.setAnalysis(getCurrentModelElement());
        stringDataFilter = columnSetAnalysisHandler.getStringDataFilter();
        analyzedColumns = columnSetAnalysisHandler.getAnalyzedColumns();
        if (columnSetAnalysisHandler.getSimpleStatIndicator() == null
                || columnSetAnalysisHandler.getSimpleStatIndicator().eIsProxy()) {
            ColumnsetFactory columnsetFactory = ColumnsetFactory.eINSTANCE;
            simpleStatIndicator = columnsetFactory.createSimpleStatIndicator();
            simpleStatIndicator.setRowCountIndicator(IndicatorsFactory.eINSTANCE.createRowCountIndicator());
            simpleStatIndicator.setDistinctCountIndicator(IndicatorsFactory.eINSTANCE.createDistinctCountIndicator());
            simpleStatIndicator.setDuplicateCountIndicator(IndicatorsFactory.eINSTANCE.createDuplicateCountIndicator());
            simpleStatIndicator.setUniqueCountIndicator(IndicatorsFactory.eINSTANCE.createUniqueCountIndicator());
        } else {
            simpleStatIndicator = (SimpleStatIndicator) columnSetAnalysisHandler.getSimpleStatIndicator();
        }
        if (columnSetAnalysisHandler.getAllmatchIndicator() == null) {
            ColumnsetFactory columnsetFactory = ColumnsetFactory.eINSTANCE;
            allMatchIndicator = columnsetFactory.createAllMatchIndicator();
            DefinitionHandler.getInstance().setDefaultIndicatorDefinition(allMatchIndicator);
        } else {
            allMatchIndicator = (AllMatchIndicator) columnSetAnalysisHandler.getAllmatchIndicator();
        }

        initializeIndicator(simpleStatIndicator);
        List<ModelElementIndicator> meIndicatorList = new ArrayList<ModelElementIndicator>();
        ModelElementIndicator currentIndicator = null;
        for (ModelElement element : analyzedColumns) {

            // MOD yyi 2011-02-16 17871:delimitefile
            MetadataColumn mdColumn = SwitchHelpers.METADATA_COLUMN_SWITCH.doSwitch(element);
            TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(element);

            if (tdColumn == null && mdColumn == null) {
                continue;
            }

            if (tdColumn == null && mdColumn != null) {
                currentIndicator = ModelElementIndicatorHelper.createDFColumnIndicator(RepositoryNodeHelper
                        .recursiveFind(mdColumn));
            } else if (tdColumn != null) {
                RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(tdColumn);
                if (recursiveFind == null) {
                    recursiveFind = RepositoryNodeHelper.createRepositoryNode(tdColumn);
                }
                currentIndicator = ModelElementIndicatorHelper.createModelElementIndicator(recursiveFind);
            }

            DataminingType dataminingType = MetadataHelper.getDataminingType(element);
            MetadataHelper.setDataminingType(dataminingType == null ? DataminingType.NOMINAL : dataminingType, element);

            Collection<Indicator> indicatorList = columnSetAnalysisHandler.getRegexMathingIndicators(element);
            if (null != currentIndicator) {
                currentIndicator.setIndicators(indicatorList.toArray(new Indicator[indicatorList.size()]));
                meIndicatorList.add(currentIndicator);
            }
        }
        currentModelElementIndicators = meIndicatorList.toArray(new ModelElementIndicator[meIndicatorList.size()]);

    }

    private void initializeIndicator(Indicator indicator) {
        if (indicator.getIndicatorDefinition() == null || indicator.getIndicatorDefinition().eIsProxy()) {
            DefinitionHandler.getInstance().setDefaultIndicatorDefinition(indicator);
        }
        if (indicator instanceof CompositeIndicator) {
            for (Indicator child : ((CompositeIndicator) indicator).getChildIndicators()) {
                initializeIndicator(child); // recurse
            }
        }
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

        setMetadataSectionTitle(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.analysisMeta")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.setPropOfAnalysis")); //$NON-NLS-1$
        metadataSection = creatMetadataSection(form, topComp);
        form.setText(DefaultMessagesImpl.getString("ColumnSetMasterPage.title")); //$NON-NLS-1$

        createDataPreviewSection(form, topComp, true, false, true);
        createAnalysisColumnsSection(form, topComp);

        createIndicatorsSection(form, topComp);

        createDataFilterSection(form, topComp);
        dataFilterComp.addPropertyChangeListener(this);

        Composite exeEngineComp = createExecuteEngineSection(form, topComp, analyzedColumns, columnSetAnalysisHandler
                .getAnalysis().getParameters());
        addListenerToExecuteEngine(execCombo);

        createStoreDataCheck(exeEngineComp);

        createContextGroupSection(form, topComp);

        if (canShowGraphicsSectionForSettingsPage()) {
            previewComp = toolkit.createComposite(sForm);
            previewComp.setLayoutData(new GridData(GridData.FILL_BOTH));
            previewComp.setLayout(new GridLayout());
            // add by hcheng for 0007290: Chart cannot auto compute it's size in
            // DQRule analsyis Editor
            previewComp.addControlListener(new ControlAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.ControlAdapter#controlResized(org.eclipse .swt.events.ControlEvent)
                 */
                @Override
                public void controlResized(ControlEvent e) {
                    super.controlResized(e);
                    sForm.redraw();
                    form.reflow(true);
                }
            });
            // ~
            createPreviewSection(form, previewComp);
        }
    }

    private void addListenerToExecuteEngine(final CCombo execCombo1) {
        execCombo1.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                execLang = execCombo1.getText();
                setStoreDataValueAndStatus();
                setDirty(true);
            }

        });
    }

    /**
     * DOC yyi Comment method "createIndicatorsSection".
     * 
     * @param topComp
     * @param form
     */
    private void createIndicatorsSection(ScrolledForm form, Composite topComp) {
        indicatorsSection = createSection(form, topComp, DefaultMessagesImpl.getString("IndicatorsComp.Indicators"), null); //$NON-NLS-1$

        Composite indicatorsComp = toolkit.createComposite(indicatorsSection, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(indicatorsComp);
        indicatorsComp.setLayout(new GridLayout());
        ((GridData) indicatorsComp.getLayoutData()).heightHint = INDICATORS_SECTION_HEIGHT;

        indicatorsViewer = new IndicatorsComp(indicatorsComp, this);
        indicatorsViewer.setDirty(false);
        indicatorsViewer.addPropertyChangeListener(this);
        // MOD msjian TDQ-8860 2014-5-4: only when there have patterns, show allMatchIndicators
        if (0 < allMatchIndicator.getCompositeRegexMatchingIndicators().size()) {
            indicatorsViewer.setInput(simpleStatIndicator, allMatchIndicator);
        } else {
            indicatorsViewer.setInput(simpleStatIndicator);
        }
        // TDQ-8860~
        indicatorsSection.setClient(indicatorsComp);
    }

    void createAnalysisColumnsSection(final ScrolledForm form, Composite anasisDataComp) {
        analysisColSection = createSection(form, anasisDataComp,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.analyzeColumn"), null); //$NON-NLS-1$

        Composite topComp = toolkit.createComposite(analysisColSection);
        topComp.setLayout(new GridLayout());
        GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(topComp);

        Composite buttonComp = toolkit.createComposite(topComp);
        buttonComp.setLayout(new GridLayout(2, false));
        createColumnSelectButton(buttonComp);
        createRunButton(buttonComp);

        Composite tree = toolkit.createComposite(topComp, SWT.None);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tree);
        tree.setLayout(new GridLayout());
        ((GridData) tree.getLayoutData()).heightHint = TREE_MAX_LENGTH;

        tree.setBackgroundMode(SWT.INHERIT_DEFAULT);

        treeViewer = new AnalysisColumnSetTreeViewer(tree, this);
        treeViewer.addObserver(sampleTable);
        sampleTable.addObserver(treeViewer);
        treeViewer.addPropertyChangeListener(this);
        treeViewer.setInput(analyzedColumns.toArray());
        treeViewer.setDirty(false);
        analysisColSection.setClient(topComp);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#openColumnsSelectionDialog()
     */
    @Override
    public void openColumnsSelectionDialog() {
        List<IRepositoryNode> columnList = treeViewer.getColumnSetMultiValueList();
        if (columnList == null) {
            columnList = new ArrayList<IRepositoryNode>();
        }
        RepositoryNode connNode = getConnComboSelectNode();
        ColumnsSelectionDialog dialog = new ColumnsSelectWithConstraintDialog(
                this,
                null,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.columnSelection"), columnList, connNode, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.columnSelections")); //$NON-NLS-1$ //$NON-NLS-2$
        if (dialog.open() == Window.OK) {
            Object[] columns = dialog.getResult();
            setTreeViewInput(columns);
            // ADD msjian TDQ-8860 2014-4-30:only for column set analysis, when there have pattern(s) when java
            // engine,show all match indicator in the Indicators section.
            EventManager.getInstance().publish(getCurrentModelElement(), EventEnum.DQ_COLUMNSET_SHOW_MATCH_INDICATORS, null);
            // TDQ-8860~
        }
    }

    void createPreviewSection(final ScrolledForm form, Composite parent) {
        previewSection = createSection(
                form,
                parent,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.graphics"), DefaultMessagesImpl.getString("ColumnMasterDetailsPage.space")); //$NON-NLS-1$ //$NON-NLS-2$
        previewSection.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite sectionClient = toolkit.createComposite(previewSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        Button chartButton = new Button(sectionClient, SWT.NONE);
        chartButton.setText(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.refreshGraphics")); //$NON-NLS-1$

        final Label message = toolkit.createLabel(sectionClient,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.spaceWhite")); //$NON-NLS-1$
        message.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        message.setVisible(false);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        chartComposite = toolkit.createComposite(sectionClient);
        chartComposite.setLayout(new GridLayout());
        chartComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        final Analysis analysis = columnSetAnalysisHandler.getAnalysis();

        chartButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                disposeChartComposite();

                boolean analysisStatue = analysis.getResults().getResultMetadata() != null
                        && analysis.getResults().getResultMetadata().getExecutionDate() != null;

                if (!analysisStatue) {
                    boolean returnCode = MessageDialog.openConfirm(null,
                            DefaultMessagesImpl.getString("ColumnMasterDetailsPage.ViewResult"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("ColumnMasterDetailsPage.RunOrSeeSampleData")); //$NON-NLS-1$

                    if (returnCode) {
                        new RunAnalysisAction().run();
                        message.setVisible(false);
                    } else {
                        createPreviewCharts(form, chartComposite, false);
                        message.setText(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.warning")); //$NON-NLS-1$
                        message.setVisible(true);
                    }
                } else {
                    createPreviewCharts(form, chartComposite, true);
                }

                chartComposite.layout();
                form.reflow(true);

            }

        });

        previewSection.setClient(sectionClient);
    }

    public void createPreviewCharts(final ScrolledForm theForm, final Composite parentComp, final boolean isCreate) {
        Section previewSimpleStatSection = createSection(
                theForm,
                parentComp,
                DefaultMessagesImpl.getString("ColumnSetResultPage.SimpleStatistics"), DefaultMessagesImpl.getString("ColumnMasterDetailsPage.space")); //$NON-NLS-1$ //$NON-NLS-2$
        previewSimpleStatSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite sectionClient = toolkit.createComposite(previewSimpleStatSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite simpleComposite = toolkit.createComposite(sectionClient);
        simpleComposite.setLayout(new GridLayout(1, true));
        simpleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createSimpleStatistics(simpleComposite);
        previewSimpleStatSection.setClient(sectionClient);

        // match
        if (0 < allMatchIndicator.getCompositeRegexMatchingIndicators().size()) {

            Section previewMatchSection = createSection(theForm, parentComp,
                    DefaultMessagesImpl.getString("ColumnSetResultPage.All_Match"), StringUtils.EMPTY); //$NON-NLS-1$
            previewMatchSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            Composite sectionMatchClient = toolkit.createComposite(previewMatchSection);
            sectionMatchClient.setLayout(new GridLayout());
            sectionMatchClient.setLayoutData(new GridData(GridData.FILL_BOTH));

            Composite matchComposite = toolkit.createComposite(sectionMatchClient);
            matchComposite.setLayout(new GridLayout(1, true));
            matchComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

            createAllMatch(matchComposite);
            previewMatchSection.setClient(sectionMatchClient);
        }
    }

    private void createAllMatch(final Composite composite) {
        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.AllMatchIndicatorEnum, allMatchIndicator));

        EIndicatorChartType matchingType = EIndicatorChartType.PATTERN_MATCHING;
        IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartState(matchingType, units);

        Object chart = chartTypeState.getChart();
        TOPChartUtils.getInstance().decorateChart(chart, false);
        if (chart != null) {
            TOPChartUtils.getInstance().createChartComposite(composite, SWT.NONE, chart, true);
        }
    }

    private void createSimpleStatistics(final Composite composite) {
        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.RowCountIndicatorEnum, simpleStatIndicator.getRowCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.DistinctCountIndicatorEnum, simpleStatIndicator
                .getDistinctCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.DuplicateCountIndicatorEnum, simpleStatIndicator
                .getDuplicateCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.UniqueIndicatorEnum, simpleStatIndicator.getUniqueCountIndicator()));

        IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartState(EIndicatorChartType.SIMPLE_STATISTICS, units);

        // create chart
        Object chart = chartTypeState.getChart();
        TOPChartUtils.getInstance().decorateChart(chart, false);
        if (chart != null) {
            TOPChartUtils.getInstance().createChartComposite(composite, SWT.NONE, chart, true);
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
                    createPreviewCharts(form, chartComposite, true);
                    chartComposite.getParent().layout();
                    chartComposite.layout();
                } catch (Exception ex) {
                    log.error(ex, ex);
                }
            } else {
                previewComp = toolkit.createComposite(sForm);
                previewComp.setLayoutData(new GridData(GridData.FILL_BOTH));
                previewComp.setLayout(new GridLayout());
                previewComp.addControlListener(new ControlAdapter() {

                    @Override
                    public void controlResized(ControlEvent e) {
                        super.controlResized(e);
                        sForm.redraw();
                        form.reflow(true);
                    }
                });
                createPreviewSection(form, previewComp);
                createPreviewCharts(form, chartComposite, true);
            }
        }
    }

    /**
     * create StoreData Checkbox.
     * 
     * @param sectionClient
     */
    private Composite createStoreDataCheck(Composite sectionClient) {
        Composite storeDataSection = toolkit.createComposite(sectionClient);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        storeDataSection.setLayout(gridLayout);
        toolkit.createLabel(storeDataSection, DefaultMessagesImpl.getString("ColumnSetAnalysisDetailsPage.StoreData")).setToolTipText(DefaultMessagesImpl.getString("ColumnSetAnalysisDetailsPage.StoreDataTip")); //$NON-NLS-1$ //$NON-NLS-2$
        storeDataCheck = new Button(storeDataSection, SWT.CHECK | SWT.RIGHT_TO_LEFT);
        setStoreDataValueAndStatus();

        storeDataCheck.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                simpleStatIndicator.setStoreData(storeDataCheck.getSelection());
                allMatchIndicator.setStoreData(storeDataCheck.getSelection());
                setDirty(true);
            }
        });

        return storeDataSection;
    }

    /**
     * set StoreData Value And Status.
     */
    private void setStoreDataValueAndStatus() {
        // TDQ-9467 msjian: for columnset when is jave engine, we set this always true, and can not edit
        if (execLang.equals(ExecutionLanguage.JAVA.getLiteral())) {
            storeDataCheck.setSelection(true);
            storeDataCheck.setEnabled(false);
        } else {
            storeDataCheck.setSelection(simpleStatIndicator.isStoreData());
            storeDataCheck.setEnabled(true);
        }
        simpleStatIndicator.setStoreData(storeDataCheck.getSelection());
        allMatchIndicator.setStoreData(storeDataCheck.getSelection());
    }

    /**
     * @param outputFolder
     * @throws DataprofilerCoreException
     */

    @Override
    public void saveAnalysis() throws DataprofilerCoreException {
        columnSetAnalysisHandler.changeDefaultRowLoaded(rowLoadedText.getText());

        columnSetAnalysisHandler.changeSampleDataShowWay(sampleDataShowWayCombo.getText());

        // remove the space from analysis name
        // columnSetAnalysisHandler.setName(columnSetAnalysisHandler.getName().replace(" ", ""));
        for (Domain domain : getCurrentModelElement().getParameters().getDataFilter()) {
            domain.setName(getCurrentModelElement().getName());
        }
        // ~

        columnSetAnalysisHandler.clearAnalysis();
        simpleStatIndicator.getAnalyzedColumns().clear();
        allMatchIndicator.getAnalyzedColumns().clear();
        // set execute engine
        Analysis analysis = columnSetAnalysisHandler.getAnalysis();
        analysis.getParameters().setExecutionLanguage(ExecutionLanguage.get(execLang));

        // set data filter
        columnSetAnalysisHandler.setStringDataFilter(dataFilterComp.getDataFilterString());

        // save analysis
        List<IRepositoryNode> repositoryNodes = treeViewer.getColumnSetMultiValueList();

        Connection tdProvider = null;
        IRepositoryViewObject reposObject = null;
        if (repositoryNodes != null && repositoryNodes.size() != 0) {
            ConnectionItem item = (ConnectionItem) repositoryNodes.get(0).getObject().getProperty().getItem();
            tdProvider = item.getConnection();
            if (tdProvider.eIsProxy()) {
                // Resolve the connection again
                tdProvider = (Connection) EObjectHelper.resolveObject(tdProvider);
            }
            analysis.getContext().setConnection(tdProvider);

            List<ModelElement> columnList = new ArrayList<ModelElement>();
            for (IRepositoryNode rd : repositoryNodes) {
                reposObject = rd.getObject();
                columnList.add(((MetadataColumnRepositoryObject) reposObject).getTdColumn());
            }

            simpleStatIndicator.getAnalyzedColumns().addAll(columnList);
            columnSetAnalysisHandler.addIndicator(columnList, simpleStatIndicator);
            // ~ MOD mzhao feature 13040. 2010-05-21
            allMatchIndicator.getCompositeRegexMatchingIndicators().clear();
            ModelElementIndicator[] modelElementIndicator = treeViewer.getModelElementIndicator();
            if (modelElementIndicator != null) {
                for (ModelElementIndicator modelElementInd : modelElementIndicator) {
                    Indicator[] inds = modelElementInd.getPatternIndicators();
                    for (Indicator ind : inds) {
                        if (ind instanceof RegexpMatchingIndicator) {
                            // MOD yyi 2011-06-15 22419:column set pattern for MDM
                            IRepositoryViewObject obj = modelElementInd.getModelElementRepositoryNode().getObject();
                            ModelElement analyzedElt = ((MetadataColumnRepositoryObject) obj).getTdColumn();
                            ind.setAnalyzedElement(analyzedElt);
                            allMatchIndicator.getCompositeRegexMatchingIndicators().add((RegexpMatchingIndicator) ind);
                        }
                    }
                }

            }
            if (allMatchIndicator.getCompositeRegexMatchingIndicators().size() > 0) {
                allMatchIndicator.getAnalyzedColumns().addAll(columnList);
                columnSetAnalysisHandler.addIndicator(columnList, allMatchIndicator);
            }
            // ~
        } else {
            analysis.getContext().setConnection(null);
        }
        TaggedValueHelper.setTaggedValue(getCurrentModelElement(), TaggedValueHelper.IS_USE_SAMPLE_DATA,
                isRunWithSampleData.toString());
        // save the number of connections per analysis
        this.saveNumberOfConnectionsPerAnalysis();

        // 2011.1.12 MOD by zhsne to unify anlysis and connection id when saving.
        this.nameText.setText(columnSetAnalysisHandler.getName());
        // TDQ-5581,if has removed emlements(patten),should remove dependency each other before saving.
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
        } else if (PluginConstant.DATAFILTER_PROPERTY.equals(evt.getPropertyName())) {
            this.columnSetAnalysisHandler.setStringDataFilter((String) evt.getNewValue());
        }

    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || (treeViewer != null && treeViewer.isDirty())
                || (dataFilterComp != null && dataFilterComp.isDirty())
                || (indicatorsViewer != null && indicatorsViewer.isDirty());
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

    public SimpleStatIndicator getSimpleStatIndicator() {
        return simpleStatIndicator;
    }

    public Composite[] getPreviewChartCompsites() {
        // ADD msjian TDQ-6213 2012-12-18: filter the disposed composite
        if (previewChartCompsites != null && previewChartCompsites.length > 0) {
            List<Composite> withOutDisposed = new ArrayList<Composite>();
            for (Composite com : previewChartCompsites) {
                if (!com.isDisposed()) {
                    withOutDisposed.add(com);
                }
            }
            this.previewChartCompsites = withOutDisposed.toArray(new ExpandableComposite[withOutDisposed.size()]);
        }
        // TDQ-6213~
        return previewChartCompsites;
    }

    @Override
    public Composite getChartComposite() {
        return chartComposite;
    }

    @Override
    public ReturnCode canSave() {
        // MOD by gdbu 2011-3-21 bug 19179
        ReturnCode canModRetCode = super.canSave();
        if (!canModRetCode.isOk()) {
            return canModRetCode;
        }
        // ~19179

        String message = null;

        List<IRepositoryNode> columnSetMultiValueList = this.treeViewer.getColumnSetMultiValueList();

        // MOD yyi 2011-02-16 17871:delimitefile
        List<ModelElement> columnList = new ArrayList<ModelElement>();
        // MOD klliu 2001-03-28 19464 filter column from same table
        Set<EObject> nodeTypeName = new HashSet<EObject>();
        for (IRepositoryNode rd : columnSetMultiValueList) {
            ModelElement modelElementFromRepositoryNode = RepositoryNodeHelper.getModelElementFromRepositoryNode(rd);
            EObject eContainer = modelElementFromRepositoryNode.eContainer();
            nodeTypeName.add(eContainer);
            columnList.add(RepositoryNodeHelper.getModelElementFromRepositoryNode(rd));
        }
        if (nodeTypeName.size() > 1) {
            return new ReturnCode(DefaultMessagesImpl.getString("ColumnSetMasterPage.CannotCreateAnalysis"), false); //$NON-NLS-1$
        }
        if (!columnSetMultiValueList.isEmpty()) {
            // MOD klliu bug 19464,file delimit connection does not need to check from one table.
            if (columnList.get(0) instanceof TdColumn) {
                if (!ModelElementHelper.isFromSameTable(columnList)) {
                    message = DefaultMessagesImpl.getString("ColumnSetMasterPage.CannotCreateAnalysis"); //$NON-NLS-1$
                }
            } else if (columnList.get(0) instanceof MetadataColumn) {
                if (!isFromSameTableByDFColumn(columnSetMultiValueList)) {
                    message = DefaultMessagesImpl.getString("ColumnSetMasterPage.CannotCreateAnalysis"); //$NON-NLS-1$
                }
            }
        }
        if (message == null) {
            resetResultPageData();
            return new ReturnCode(true);
        }

        return new ReturnCode(message, false);
    }

    private boolean isFromSameTableByDFColumn(List<IRepositoryNode> columnSetMultiValueList) {
        Set<String> folderItemIds = new HashSet<String>();
        for (IRepositoryNode node : columnSetMultiValueList) {
            RepositoryNode parent = node.getParent();
            if (parent instanceof DFColumnFolderRepNode) {
                DFColumnFolderRepNode folderNode = (DFColumnFolderRepNode) parent;
                String id = folderNode.getmTable().getId();
                folderItemIds.add(id);
            }
        }
        return folderItemIds.size() == 1;
    }

    @Override
    protected ReturnCode canRun() {
        List<IRepositoryNode> columnSetMultiValueList = this.treeViewer.getColumnSetMultiValueList();
        if (columnSetMultiValueList.isEmpty()) {
            return new ReturnCode(DefaultMessagesImpl.getString("ColumnSetMasterPage.NoColumnsAssigned"), false); //$NON-NLS-1$
        }
        resetResultPageData();
        return new ReturnCode(true);

    }

    public AllMatchIndicator getAllMatchIndicator() {
        return allMatchIndicator;
    }

    private void resetResultPageData() {
        ColumnSetAnalysisResultPage theResultPage = null;
        if (((AnalysisEditor) currentEditor).getResultPage() instanceof ColumnSetAnalysisResultPage) {
            theResultPage = (ColumnSetAnalysisResultPage) ((AnalysisEditor) currentEditor).getResultPage();
        }
        if (theResultPage.getTableFilterResult() != null) {
            theResultPage.setTableFilterResult(null);
        }
    }

    /**
     * DOC qiongli Comment method "includeDatePatternFreqIndicator".
     * 
     * @return
     */
    @Override
    protected boolean includeJavaEngineIndicator() {
        for (ModelElementIndicator modelElementIndicator : this.treeViewer.getModelElementIndicator()) {
            if (modelElementIndicator.contains(IndicatorEnum.RegexpMatchingIndicatorEnum)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#getNoNJavaIndiatorMessage()
     */
    @Override
    protected String getNonJavaIndicatorMessage() {
        return DefaultMessagesImpl.getString("PatternMouseAdapter.noSupportForSqlEngine"); //$NON-NLS-1$
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
     * refresh the Indicators Section.
     */
    public void refreshIndicatorsSection() {
        // when there have pattern, show allmatchindicator
        ModelElementIndicator[] modelElementIndicator = treeViewer.getModelElementIndicator();
        if (modelElementIndicator != null) {
            for (ModelElementIndicator modelElementInd : modelElementIndicator) {
                Indicator[] inds = modelElementInd.getPatternIndicators();
                for (Indicator ind : inds) {
                    if (ind instanceof RegexpMatchingIndicator) {
                        indicatorsViewer.setInput(simpleStatIndicator, allMatchIndicator);
                        return;
                    }
                }
            }
        }
        indicatorsViewer.setInput(simpleStatIndicator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#getAnalysisHandler()
     */
    @Override
    public AnalysisHandler getAnalysisHandler() {
        return columnSetAnalysisHandler;
    }

    /**
     * 
     * Refresh the column tree
     * 
     * @param modelElements
     */
    public void refreshTheTree(ModelElementIndicator[] modelElements) {
        this.currentModelElementIndicators = modelElements;
        disposeChartComposite();
        treeViewer.setElements(currentModelElementIndicators);
    }

    public void disposeChartComposite() {
        if (chartComposite != null && !chartComposite.isDisposed()) {
            for (Control control : chartComposite.getChildren()) {
                control.dispose();
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
        // TDQ-11878: fix the new created column set analysis about file connection should be java engine and not
        // editable, the setInput method contains the filterInputData method
        treeViewer.setInput(modelElements);
        // TDQ-11878~
        refreshPreviewTable(treeViewer.getModelElementIndicator(), true);
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

    protected ModelElementIndicator[] getAllTheElementIndicator() {
        List<IRepositoryNode> columnSetMultiValueList = this.treeViewer.getColumnSetMultiValueList();
        return (ModelElementIndicator[]) columnSetMultiValueList.toArray();

    }

    /*
     * (non-Javadoc)
     * Same with column detail page to do a merge when have a time
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#doCheckOption()
     */
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
     * Same with column detail page to do a merge when have a time
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

}
