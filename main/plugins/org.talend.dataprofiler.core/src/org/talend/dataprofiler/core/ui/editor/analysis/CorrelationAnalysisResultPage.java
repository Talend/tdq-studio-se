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
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.ColumnSortListener;
import org.talend.dataprofiler.core.ui.chart.jung.JungGraphGenerator;
import org.talend.dataprofiler.core.ui.editor.preview.ColumnSetIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.HideSeriesChartComposite;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableTypeStatesFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.table.ITableTypeStates;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.CountAvgNullIndicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.indicators.graph.GraphBuilder;
import org.talend.dq.indicators.preview.EIndicatorChartType;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xzhao class global comment. Detailled comment
 */
public class CorrelationAnalysisResultPage extends AbstractAnalysisResultPage implements PropertyChangeListener {

    protected static Logger log = Logger.getLogger(CorrelationAnalysisResultPage.class);

    private Composite graphicsAndTableComp;

    private CorrelationAnalysisDetailsPage masterPage;

    private ColumnSetMultiValueIndicator columnSetMultiIndicator;

    private Composite chartComposite;

    private Composite[] previewChartCompsites;

    private String executeData;

    private Section graphicsAndTableSection = null;

    /**
     * DOC zqin ColumnAnalysisResultPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public CorrelationAnalysisResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (CorrelationAnalysisDetailsPage) analysisEditor.getMasterPage();
        columnSetMultiIndicator = masterPage.getColumnSetMultiValueIndicator();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);

        if (topComposite != null && !topComposite.isDisposed()) {
            graphicsAndTableComp = toolkit.createComposite(topComposite);
            graphicsAndTableComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            graphicsAndTableComp.setLayout(new GridLayout());
            createResultSection(graphicsAndTableComp);
            form.reflow(true);
        }
    }

    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getAnalysisHandler();
    }

    @Override
    protected void createResultSection(Composite parent) {
        executeData = getAnalysisHandler().getExecuteData();
        graphicsAndTableSection = this.createSection(form, parent,
                DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.AnalysisResult"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(graphicsAndTableSection);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite graphicsComp = toolkit.createComposite(sectionClient);
        graphicsComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        graphicsComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            if (canShowChartForResultPage()) {
                this.createGraphicsSectionPart(sectionClient);
            }
        }

        Composite simpleSatisticsComp = toolkit.createComposite(sectionClient);
        simpleSatisticsComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        simpleSatisticsComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            this.createSimpleStatisticsPart(
                    sectionClient,
                    DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.SimpleStatistics"), columnSetMultiIndicator); //$NON-NLS-1$
        }

        Composite tableComp = toolkit.createComposite(sectionClient);
        tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        tableComp.setLayout(new GridLayout());
        if (executeData == null || executeData.equals(PluginConstant.EMPTY_STRING)) {
            return;
        } else {
            this.createTableSectionPart(sectionClient,
                    DefaultMessagesImpl.getString("ColumnCorrelationNominalIntervalResultPage.Data"), columnSetMultiIndicator); //$NON-NLS-1$
        }
        graphicsAndTableSection.setExpanded(true);
        graphicsAndTableSection.setClient(sectionClient);
    }

    protected Section createGraphicsSectionPart(Composite parentComp) {
        Section section = createSection(form, parentComp, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.graphics"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.space")); //$NON-NLS-1$ 
        section.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite sectionClient = toolkit.createComposite(section);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        chartComposite = toolkit.createComposite(sectionClient);
        chartComposite.setLayout(new GridLayout());

        if (ColumnsetPackage.eINSTANCE.getWeakCorrelationIndicator() == columnSetMultiIndicator.eClass()) {
            GridData gd = new GridData();
            gd.widthHint = 900;
            gd.heightHint = 450;
            chartComposite.setLayoutData(gd);
            GraphBuilder gBuilder = new GraphBuilder();
            gBuilder.setTotalWeight(columnSetMultiIndicator.getCount());
            List<Object[]> listRows = columnSetMultiIndicator.getListRows();
            // MOD msjian TDQ-4781 2012-6-8: make sure exist data
            if (listRows != null && listRows.size() > 0) {
                JungGraphGenerator generator = new JungGraphGenerator(gBuilder, listRows);
                // MOD yyi 2009-09-09 feature 8834
                generator.generate(chartComposite, false, false);
            }
            // TDQ-4781~
        } else {
            createBubbleOrGanttChart(form, chartComposite, columnSetMultiIndicator);
        }

        section.setClient(sectionClient);
        return section;
    }

    private void createBubbleOrGanttChart(final ScrolledForm sForm, final Composite composite,
            final ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
        List<Composite> previewChartList = new ArrayList<Composite>();
        List<ModelElement> bubOrGanttColumnList = new ArrayList<ModelElement>();
        if (columnSetMultiValueIndicator instanceof CountAvgNullIndicator) {
            bubOrGanttColumnList = columnSetMultiValueIndicator.getNumericColumns();
        } else {
            bubOrGanttColumnList = columnSetMultiValueIndicator.getDateColumns();
        }
        for (ModelElement column : bubOrGanttColumnList) {
            final MetadataColumn tdColumn = (MetadataColumn) column;

            final ExpandableComposite exComp = toolkit.createExpandableComposite(composite, ExpandableComposite.TREE_NODE
                    | ExpandableComposite.CLIENT_INDENT);
            exComp.setText(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.column", tdColumn.getName())); //$NON-NLS-1$
            exComp.setLayout(new GridLayout());
            exComp.setData(columnSetMultiValueIndicator);
            previewChartList.add(exComp);

            final Composite comp = toolkit.createComposite(exComp);
            comp.setLayout(new GridLayout(2, false));
            comp.setLayoutData(new GridData(GridData.FILL_BOTH));

            if (tdColumn != null) {
                IRunnableWithProgress rwp = new IRunnableWithProgress() {

                    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        monitor.beginTask(
                                DefaultMessagesImpl.getString(
                                        "ColumnCorrelationNominalIntervalResultPage.CreatePreview", tdColumn.getName()), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                        Display.getDefault().asyncExec(new Runnable() {

                            public void run() {
                                HideSeriesChartComposite hcc = new HideSeriesChartComposite(comp, getAnalysisHandler()
                                        .getAnalysis(), columnSetMultiValueIndicator, tdColumn, true);
                                GridData gd = new GridData();
                                gd.widthHint = 800;
                                gd.heightHint = 450;
                                hcc.setLayoutData(gd);
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
                    sForm.reflow(true);
                }

            });

            exComp.setExpanded(true);
            exComp.setClient(comp);
        }
        if (!previewChartList.isEmpty()) {
            this.previewChartCompsites = previewChartList.toArray(new Composite[previewChartList.size()]);
        }
    }

    private Section createSimpleStatisticsPart(Composite parentComp, String title,
            ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
        Section section = createSection(form, parentComp, title, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.space")); //$NON-NLS-1$
        section.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite sectionClient = toolkit.createComposite(section);
        sectionClient.setLayout(new GridLayout());
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(sectionClient);

        Composite simpleComposite = toolkit.createComposite(sectionClient);
        simpleComposite.setLayout(new GridLayout(2, true));
        simpleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createSimpleStatistics2(simpleComposite, columnSetMultiValueIndicator);

        section.setClient(sectionClient);
        return section;
    }

    private void createSimpleStatistics2(final Composite composite,
            final ColumnSetMultiValueIndicator columnSetMultiValueIndicator) {
        List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.RowCountIndicatorEnum, columnSetMultiValueIndicator
                .getRowCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.DistinctCountIndicatorEnum, columnSetMultiValueIndicator
                .getDistinctCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.DuplicateCountIndicatorEnum, columnSetMultiValueIndicator
                .getDuplicateCountIndicator()));
        units.add(new ColumnSetIndicatorUnit(IndicatorEnum.UniqueIndicatorEnum, columnSetMultiValueIndicator
                .getUniqueCountIndicator()));

        EIndicatorChartType simpleStatType = EIndicatorChartType.SIMPLE_STATISTICS;
        // create table viewer firstly
        ITableTypeStates tableTypeState = TableTypeStatesFactory.getInstance().getTableState(simpleStatType, units);
        TableWithData chartData = new TableWithData(simpleStatType, tableTypeState.getDataEntity());

        TableViewer tableviewer = tableTypeState.getTableForm(composite);
        tableviewer.setInput(chartData);
        TableUtils.addTooltipForTable(tableviewer.getTable());

        // create chart
        if (canShowChartForResultPage()) {
            // then create chart
            IChartTypeStates chartTypeState = ChartTypeStatesFactory.getChartState(simpleStatType, units);
            Object chart = chartTypeState.getChart();
            TOPChartUtils.getInstance().decorateChart(chart, false);
            if (chart != null) {
                TOPChartUtils.getInstance().createChartComposite(composite, SWT.NONE, chart, true);
            }
        }
    }

    private Section createTableSectionPart(Composite parentComp, String title, ColumnSetMultiValueIndicator csMultiIndicator) {
        Section columnSetElementSection = this.createSection(form, parentComp, title, null);
        Composite sectionTableComp = toolkit.createComposite(columnSetElementSection);
        sectionTableComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        sectionTableComp.setLayout(new GridLayout());

        TableViewer columnsElementViewer = new TableViewer(sectionTableComp, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
        Table table = columnsElementViewer.getTable();

        List<String> tableColumnNames = getTableColumnNames(csMultiIndicator);
        for (String tableColumnName : tableColumnNames) {
            final TableColumn columnHeader = new TableColumn(table, SWT.NONE);
            columnHeader.setText(tableColumnName);
        }
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        TableSectionViewerProvider provider = new TableSectionViewerProvider();
        List<Object[]> tableRows = csMultiIndicator.getListRows();
        columnsElementViewer.setContentProvider(provider);
        columnsElementViewer.setLabelProvider(provider);
        columnsElementViewer.setInput(tableRows);
        for (int i = 0; i < tableColumnNames.size(); i++) {
            table.getColumn(i).pack();
        }
        columnSetElementSection.setClient(sectionTableComp);
        // ADDED sgandon 15/03/2010 bug 11769 : setup the size of the table to avoid crash and add consistency.
        // MOD msjian 2011-5-30 17479: Excel Odbc connection can not run well on the correlation analysis
        setupTableGridDataLimitedSize(table, tableRows != null ? tableRows.size() : 0);
        // ~
        addColumnSorters(columnsElementViewer, table.getColumns(), this.buildSorter(tableRows));
        columnSetElementSection.setExpanded(false);
        return columnSetElementSection;
    }

    /**
     * get the table column names from ColumnSetMultiValueIndicator, all the column with nominal type should be ahead of
     * the column with other types.
     * 
     * @param csMultiIndicator
     * @return
     */
    private List<String> getTableColumnNames(ColumnSetMultiValueIndicator csMultiIndicator) {
        EList<ModelElement> nominalColumns = csMultiIndicator.getNominalColumns();
        List<String> nominalColumnNames = new ArrayList<String>();
        for (ModelElement me : nominalColumns) {
            nominalColumnNames.add(me.getName());
        }

        List<String> nominalHeaders = new ArrayList<String>();
        List<String> otherHeaders = new ArrayList<String>();
        EList<String> columnHeaders = csMultiIndicator.getColumnHeaders();
        for (String s : columnHeaders) {
            if (nominalColumnNames.contains(s)) {
                nominalHeaders.add(s);
            } else {
                otherHeaders.add(s);
            }
        }

        List<String> finalHeaders = new ArrayList<String>();
        finalHeaders.addAll(nominalColumnNames);
        finalHeaders.addAll(otherHeaders);

        return finalHeaders;
    }

    /**
     * 
     * DOC hcheng Comment method "addColumnSorters".For 8267.
     * 
     * @param tableViewer
     * @param tableColumns
     * @param sorters
     */
    protected void addColumnSorters(TableViewer tableViewer, TableColumn[] tableColumns, ViewerSorter[][] sorters) {
        for (int i = 0; i < tableColumns.length; ++i) {
            tableColumns[i].addSelectionListener(new ColumnSortListener(tableColumns, i, tableViewer, sorters));
        }
    }

    private Color bg = new Color(null, 249, 139, 121);

    /**
     * 
     * DOC zhaoxinyi ColumnCorrelationNominalIntervalResultPage class global comment. Detailled comment
     */
    class TableSectionViewerProvider implements IStructuredContentProvider, ITableLabelProvider, ITableColorProvider {

        @SuppressWarnings("unchecked")
        public Object[] getElements(Object inputElement) {
            List<Object> columnDataSet = (List<Object>) inputElement;
            return columnDataSet.toArray();
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // no need to implement
        }

        public Image getImage(Object element) {
            if (element instanceof TdColumn) {
                return ImageLib.getImage(ImageLib.TD_COLUMN);
            }
            return null;
        }

        public void dispose() {
            // TODO Auto-generated method stub

        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            for (int i = 0; i < ((Object[]) element).length; i++) {
                if (columnIndex == i) {
                    return String.valueOf(((Object[]) element)[i]);
                }
            }
            return null;
        }

        public void addListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        public void removeListener(ILabelProviderListener listener) {
            // TODO Auto-generated method stub

        }

        public Color getBackground(Object element, int columnIndex) {
            Object[] elements = (Object[]) element;

            for (Object elem : elements) {
                if (elem == null || "".equals(elem)) { //$NON-NLS-1$
                    return bg;
                }
            }
            return null;
        }

        public Color getForeground(Object element, int columnIndex) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
        // TODO do nothing here?
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisResultPage#refresh(org.talend.dataprofiler.core
     * .ui.editor.analysis.AbstractAnalysisMetadataPage)
     */
    @Override
    public void refresh(AbstractAnalysisMetadataPage mPage) {
        this.masterPage = (CorrelationAnalysisDetailsPage) mPage;

        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }

        if (graphicsAndTableComp != null && !graphicsAndTableComp.isDisposed()) {
            graphicsAndTableComp.dispose();
        }
        createFormContent(getManagedForm());
    }

    public Composite getChartComposite() {
        return chartComposite;
    }

    public Composite[] getPreviewChartCompsites() {
        return previewChartCompsites;
    }

    @Override
    public void dispose() {
        if (bg != null) {
            bg.dispose();
        }
        super.dispose();
    }

    private CorrelationDataSorter[][] buildSorter(List<Object[]> tableRows) {
        CorrelationDataSorter[][] result = null;
        if (tableRows != null && tableRows.size() > 0) {
            Object[] objs = tableRows.get(0);
            int length = objs.length;
            result = new CorrelationDataSorter[length][2];
            for (int i = 0; i < length; ++i) {
                result[i][0] = new CorrelationDataSorter(i + 1);
                result[i][1] = new CorrelationDataSorter(-(i + 1));
            }
        }
        return result;
    }
}
