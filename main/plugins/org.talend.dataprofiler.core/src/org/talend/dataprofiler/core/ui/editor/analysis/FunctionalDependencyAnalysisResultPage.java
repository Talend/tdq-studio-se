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

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.model.ChartTableFactory;
import org.talend.dataprofiler.core.ui.editor.preview.model.MenuItemEntity;
import org.talend.dataprofiler.core.ui.editor.preview.model.entity.TableStructureEntity;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnDependencyIndicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.explore.ColumnDependencyExplorer;
import org.talend.dq.helper.ColumnDependencyHelper;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;
import org.talend.utils.format.StringFormatUtil;

/**
 * DOC jet class global comment. Detailled comment
 */
public class FunctionalDependencyAnalysisResultPage extends AbstractAnalysisResultPageWithChart {

    private Composite analyzedColumnSetsComp;

    private Composite analysisResultsComp;

    private Section resultSection = null;

    private FunctionalDependencyAnalysisDetailsPage masterPage;

    private ColumnDependencyIndicator columnDependencyIndicator;

    public ColumnDependencyIndicator getColumnDependencyIndicator() {
        return this.columnDependencyIndicator;
    }

    public void setColumnDependencyIndicator(ColumnDependencyIndicator columnDependencyIndicator) {
        this.columnDependencyIndicator = columnDependencyIndicator;
    }

    /**
     * DOC jet ColumnDependencyResultPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public FunctionalDependencyAnalysisResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (FunctionalDependencyAnalysisDetailsPage) analysisEditor.getMasterPage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisResultPage#getAnalysisHandler()
     */
    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getAnalysisHandler();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisResultPage#refresh(org.talend.dataprofiler.core
     * .ui.editor.analysis.AbstractAnalysisMetadataPage)
     */
    @Override
    public void refresh(AbstractAnalysisMetadataPage masterPage1) {

        this.masterPage = (FunctionalDependencyAnalysisDetailsPage) masterPage1;
        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }

        if (analyzedColumnSetsComp != null && !analyzedColumnSetsComp.isDisposed()) {
            analyzedColumnSetsComp.dispose();
        }

        if (analysisResultsComp != null && !analysisResultsComp.isDisposed()) {
            analysisResultsComp.dispose();
        }

        createFormContent(getManagedForm());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
        // no need to implement
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);

        if (topComposite != null && !topComposite.isDisposed()) {
            analyzedColumnSetsComp = toolkit.createComposite(topComposite);
            analyzedColumnSetsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            analyzedColumnSetsComp.setLayout(new GridLayout());

            analysisResultsComp = toolkit.createComposite(topComposite);
            analysisResultsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            analysisResultsComp.setLayout(new GridLayout());
            createResultSection(analysisResultsComp);
            form.reflow(true);
        }
    }

    @Override
    protected void createResultSection(Composite parent) {
        resultSection = createSection(form, parent,
                DefaultMessagesImpl.getString("ColumnsComparisonAnalysisResultPage.analysisResults"), ""); //$NON-NLS-1$ //$NON-NLS-2$
        Composite sectionClient = toolkit.createComposite(resultSection);
        sectionClient.setLayout(new GridLayout(2, false));
        sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        resultSection.setClient(sectionClient);

        Analysis analysis = this.masterPage.getAnalysisHandler().getAnalysis();
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        if (indicators.size() > 0) {
            columnDependencyIndicator = (ColumnDependencyIndicator) indicators.get(0);
        }

        createTable(sectionClient);

        if (canShowChartForResultPage()) {
            createChart(sectionClient, analysis);
        }

        resultSection.layout();
    }

    /**
     * DOC jet create result chart.
     * <p>
     * the chart must include
     * 
     * @param sectionClient
     * @param analysis
     */
    private void createChart(Composite sectionClient, Analysis analysis) {
        CustomerDefaultCategoryDataset dataset = initCustomerDataset();

        Object createChart = TOPChartUtils.getInstance().createStackedBarChart(
                DefaultMessagesImpl.getString("ColumnDependencyResultPage.dependencyStrength"), dataset, true, true); //$NON-NLS-1$
        TOPChartUtils.getInstance().decorateColumnDependency(createChart);

        Object chartComp = TOPChartUtils.getInstance().createChartComposite(sectionClient, SWT.NONE, createChart, true);

        addMenuToChartComp(chartComp, new ColumnDependencyExplorer(), getAnalysisHandler().getAnalysis(),
                ((ICustomerDataset) dataset).getDataEntities());

    }

    @Override
    protected String getEditorName(Indicator indicator) {
        return ColumnDependencyHelper.getIndicatorName(indicator);
    }

    @Override
    protected Image getItemImage(MenuItemEntity menuItem) {
        return ImageLib.getImage(ImageLib.EXPLORE_IMAGE);
    }

    /**
     * DOC xqliu Comment method "createTable".
     * 
     * @param composite
     */
    private void createTable(Composite composite) {
        final TableViewer tableViewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
                | SWT.BORDER);
        Table resultTable = tableViewer.getTable();

        resultTable.setLinesVisible(true);
        resultTable.setHeaderVisible(true);
        // create table headers
        TableStructureEntity tableStructure = getTableStructure();
        String[] fieldNames = tableStructure.getFieldNames();
        Integer[] fieldWidths = tableStructure.getFieldWidths();
        for (int i = 0; i < fieldNames.length; ++i) {
            TableColumn columnHeader = new TableColumn(resultTable, SWT.LEFT);
            columnHeader.setText(fieldNames[i]);
            columnHeader.setWidth(fieldWidths[i]);
        }
        // create table items
        CustomerDefaultCategoryDataset dataset = initCustomerDataset();
        ChartDataEntity[] dataEntities = dataset.getDataEntities();
        if (dataEntities != null) {
            // MOD mzhao bug 8839 There might be duplicate dependencies on left and right columnSet.
            if (canShowChartForResultPage()
                    && TOPChartUtils.getInstance().getColumnCount(dataset.getDataset()) < dataEntities.length) {
                MessageDialog.openError(this.getEditor().getSite().getShell(), "Duplicate dependencies",//$NON-NLS-1$
                        "There might be duplicate dependencies on left and right columnSet.");//$NON-NLS-1$
            } else {
                for (ChartDataEntity dataEntity : dataEntities) {
                    TableItem item = new TableItem(resultTable, SWT.NULL);

                    String numMatch = ((PatternChartDataEntity) dataEntity).getNumMatch();
                    String numNoMatch = ((PatternChartDataEntity) dataEntity).getNumNoMatch();
                    Number match = Long.parseLong(numMatch);
                    Number notMatch = Long.parseLong(numNoMatch);
                    Number row = match.intValue() + notMatch.intValue();

                    item.setText(0, dataEntity.getLabel());
                    item.setText(1, String.valueOf(match.intValue()));
                    // TDQ-8695 display "N/A" if it is infinite or NaN
                    double percentage = match.doubleValue() / row.doubleValue();
                    if (Double.isNaN(percentage) || Double.isInfinite(percentage)) {
                        item.setText(2, PluginConstant.NA_STRING);
                    } else {
                        item.setText(2, StringFormatUtil.format(String.valueOf(percentage), StringFormatUtil.PERCENT).toString());
                    }
                    item.setText(3, String.valueOf(row));

                    item.setData(dataEntity);
                }
            }
        }

        GridData gd = new GridData();
        gd.heightHint = 180;
        gd.widthHint = 450;

        resultTable.setLayoutData(gd);

        // add menus
        ChartTableFactory.addMenuAndTip(tableViewer, new ColumnDependencyExplorer(), this.masterPage.getCurrentModelElement());
    }

    /**
     * DOC xqliu Comment method "initCustomerDataset".
     * 
     * @return
     */
    private CustomerDefaultCategoryDataset initCustomerDataset() {

        CustomerDefaultCategoryDataset dataset = new CustomerDefaultCategoryDataset();

        Analysis analysis = this.getAnalysisHandler().getAnalysis();

        for (Indicator indicator2 : analysis.getResults().getIndicators()) {
            ColumnDependencyIndicator indicator = (ColumnDependencyIndicator) indicator2;
            String label = ColumnDependencyHelper.getIndicatorName(indicator);
            if (getAnalysisHandler().getResultMetadata().getExecutionNumber() > 0) {
                Long matchCount = indicator.getDistinctACount() == null ? 0 : indicator.getDistinctACount();
                Long notMatchCount = indicator.getACount() == null ? 0 : indicator.getACount() - matchCount;
                dataset.addValue(matchCount, DefaultMessagesImpl.getString("ColumnDependencyResultPage.Match"), label); //$NON-NLS-1$
                dataset.addValue(notMatchCount, DefaultMessagesImpl.getString("ColumnDependencyResultPage.NotMatch"), label); //$NON-NLS-1$

                PatternChartDataEntity dataEntity = new PatternChartDataEntity();
                dataEntity.setLabel(label);
                dataEntity.setIndicator(indicator);
                dataEntity.setNumMatch(matchCount.toString());
                dataEntity.setNumNoMatch(notMatchCount.toString());

                dataset.addDataEntity(dataEntity);
            }
        }
        return dataset;
    }

    /**
     * DOC jet Comment method "getRowLabel".
     * <p>
     * according to ColumnDependencyIndicator get chart label.
     * <p>
     * value is columnA.getName() -> columnB.getName()
     * 
     * @param indicator
     * @return
     * @deprecated
     */
    // private String getRowLabel(ColumnDependencyIndicator indicator) {
    // assert indicator.getColumnA() != null;
    // assert indicator.getColumnB() != null;
    //
    //        return indicator.getColumnA().getName() + "-->" + indicator.getColumnB().getName(); //$NON-NLS-1$
    // }

    @Deprecated
    private TableStructureEntity getTableStructure() {
        TableStructureEntity entity = new TableStructureEntity();
        entity.setFieldNames(new String[] {
                DefaultMessagesImpl.getString("ColumnDependencyState.Label"), DefaultMessagesImpl.getString("ColumnDependencyState.Match"), DefaultMessagesImpl.getString("ColumnDependencyState.%Match"), DefaultMessagesImpl.getString("ColumnDependencyState.rows") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        entity.setFieldWidths(new Integer[] { 200, 85, 85, 85 });
        return entity;
    }

}
