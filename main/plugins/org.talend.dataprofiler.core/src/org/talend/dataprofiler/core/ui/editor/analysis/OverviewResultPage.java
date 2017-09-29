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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlStore;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ViewHelper;
import org.talend.cwm.relational.TdView;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.FolderNodeHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.OverviewIndUIElement;
import org.talend.dataprofiler.core.ui.ColumnSortListener;
import org.talend.dataprofiler.core.ui.action.actions.AnalyzeColumnSetAction;
import org.talend.dataprofiler.core.ui.action.actions.OverviewAnalysisAction;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dataquality.analysis.ExecutionInformations;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dataquality.indicators.schema.TableIndicator;
import org.talend.dataquality.indicators.schema.ViewIndicator;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.nodes.DBTableFolderRepNode;
import org.talend.dq.nodes.DBViewFolderRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class OverviewResultPage extends AbstractAnalysisResultPage implements PropertyChangeListener {

    private Composite resultComp;

    private AbstractFilterMetadataPage masterPage;

    private Section statisticalSection = null;

    private TableViewer catalogTableViewer;

    private Composite sumSectionClient;

    private Composite tableAndViewComposite;

    private TableViewer tableOfCatalogOrSchemaViewer;

    private TableViewer viewOfCatalogOrSchemaViewer;

    private SchemaIndicator currentSelectionSchemaIndicator;

    private SchemaIndicator currentCatalogIndicator = null; // used in sqlserver

    private static final int COLUMN_TABLE_WIDTH = 100;

    private static final int COLUMN_VIEW_WIDTH = 150;

    /**
     * Width of the first column.
     */
    private static final int COL1_WIDTH = 200;

    /**
     * Width of columns.
     */
    private static final int COL_WIDTH = 100;

    private TableViewer schemaTableViewer;

    private AbstractStatisticalViewerProvider provider;

    private static final Logger log = Logger.getLogger(OverviewResultPage.class);

    private SchemaTableSorter[][] tableSorters = {
            { new SchemaTableSorter(SchemaTableSorter.TABLE), new SchemaTableSorter(-SchemaTableSorter.TABLE) },
            { new SchemaTableSorter(SchemaTableSorter.ROWS), new SchemaTableSorter(-SchemaTableSorter.ROWS) },
            { new SchemaTableSorter(SchemaTableSorter.KEYS), new SchemaTableSorter(-SchemaTableSorter.KEYS) },
            { new SchemaTableSorter(SchemaTableSorter.INDEXES), new SchemaTableSorter(-SchemaTableSorter.INDEXES) } };

    private SchemaViewSorter[][] viewSorters = {
            { new SchemaViewSorter(SchemaViewSorter.VIEW), new SchemaViewSorter(-SchemaViewSorter.VIEW) },
            { new SchemaViewSorter(SchemaViewSorter.ROWS), new SchemaViewSorter(-SchemaViewSorter.ROWS) } };

    private SchemaSorter[][] schemaSorters = { { new SchemaSorter(SchemaSorter.SCHEMA), new SchemaSorter(-SchemaSorter.SCHEMA) },
            { new SchemaSorter(SchemaSorter.ROWS), new SchemaSorter(-SchemaSorter.ROWS) },
            { new SchemaSorter(SchemaSorter.TABLES), new SchemaSorter(-SchemaSorter.TABLES) },
            { new SchemaSorter(SchemaSorter.ROWS_TABLES), new SchemaSorter(-SchemaSorter.ROWS_TABLES) },
            { new SchemaSorter(SchemaSorter.VIEWS), new SchemaSorter(-SchemaSorter.VIEWS) },
            { new SchemaSorter(SchemaSorter.ROWS_VIEWS), new SchemaSorter(-SchemaSorter.ROWS_VIEWS) },
            { new SchemaSorter(SchemaSorter.KEYS), new SchemaSorter(-SchemaSorter.KEYS) },
            { new SchemaSorter(SchemaSorter.INDEXES), new SchemaSorter(-SchemaSorter.INDEXES) } };

    private CatalogWithSchemaSorter[][] catalogWithSchemaSorters = {
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.CATALOG),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.CATALOG) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.ROWS),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.ROWS) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.SCHEMAS),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.SCHEMAS) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.ROWS_SCHEMAS),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.ROWS_SCHEMAS) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.TABLES),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.TABLES) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.ROWS_TABLES),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.ROWS_TABLES) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.VIEWS),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.VIEWS) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.ROWS_VIEWS),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.ROWS_VIEWS) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.KEYS),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.KEYS) },
            { new CatalogWithSchemaSorter(CatalogWithSchemaSorter.INDEXES),
                    new CatalogWithSchemaSorter(-CatalogWithSchemaSorter.INDEXES) } };

    /**
     * DOC msjian OverviewResultPage constructor comment.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public OverviewResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        AnalysisEditor analysisEditor = (AnalysisEditor) editor;
        this.masterPage = (AbstractFilterMetadataPage) analysisEditor.getMasterPage();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);

        if (topComposite != null && !topComposite.isDisposed()) {
            resultComp = toolkit.createComposite(topComposite);
            resultComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            resultComp.setLayout(new GridLayout());
            createResultSection(resultComp);
            form.reflow(true);
        }
    }

    @Override
    protected void createSummarySection(ScrolledForm form, Composite parent, AnalysisHandler analysisHandler) {
        summarySection = this.createSection(form, parent,
                DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.analysisSummary")); //$NON-NLS-1$
        sumSectionClient = toolkit.createComposite(summarySection);
        sumSectionClient.setLayout(new GridLayout(2, false));
        sumSectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        refreshSumSection();
        summarySection.setClient(sumSectionClient);
    }

    @Override
    protected AnalysisHandler getAnalysisHandler() {
        return this.masterPage.getAnalysisHandler();
    }

    @Override
    protected void createResultSection(Composite parent) {
        statisticalSection = this.createSection(form, parent,
                DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.statisticalinformations"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(statisticalSection);
        sectionClient.setLayout(new GridLayout());

        catalogTableViewer = new TableViewer(sectionClient, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        Table catalogTable = catalogTableViewer.getTable();
        TableUtils.addActionTooltip(catalogTable);
        catalogTable.setHeaderVisible(true);
        catalogTable.setBackgroundMode(SWT.INHERIT_FORCE);
        catalogTable.setLinesVisible(true);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(catalogTable);
        List<Catalog> catalogs = getCatalogs();
        boolean containSubSchema = false;
        for (Catalog catalog : catalogs) {
            List<Schema> schemas = CatalogHelper.getSchemas(catalog);
            if (schemas.size() > 0) {
                containSubSchema = true;
                break;
            }
        }

        if (catalogs.size() > 0 && containSubSchema) {
            createCatalogSchemaColumns(catalogTable);
            provider = new CatalogSchemaViewerProvier();
            addColumnSorters(catalogTableViewer, catalogTable.getColumns(), catalogWithSchemaSorters);
            createSchemaTableViewer(sectionClient);
            schemaTableViewer.addSelectionChangedListener(new DisplayTableAndViewListener());
            catalogTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {
                    StructuredSelection selection = (StructuredSelection) event.getSelection();
                    OverviewIndUIElement firstElement = (OverviewIndUIElement) selection.getFirstElement();
                    List<OverviewIndUIElement> cataUIEleList = new ArrayList<OverviewIndUIElement>();
                    if (firstElement != null) {
                        Indicator overviewIndicator = firstElement.getOverviewIndicator();
                        CatalogIndicator catalogIndicator = (CatalogIndicator) overviewIndicator;// selection.getFirstElement();
                        // MOD qiongli bug 13093,2010-7-2,
                        currentCatalogIndicator = (SchemaIndicator) overviewIndicator; // selection.getFirstElement();
                        // MOD xqliu 2009-11-30 bug 9114
                        if (catalogIndicator != null) {
                            EList<SchemaIndicator> schemaIndicators = catalogIndicator.getSchemaIndicators();
                            for (SchemaIndicator schemaIndicator : schemaIndicators) {
                                RepositoryNode schemaNode = RepositoryNodeHelper.recursiveFind(schemaIndicator
                                        .getAnalyzedElement());
                                OverviewIndUIElement cataUIEle = new OverviewIndUIElement();
                                cataUIEle.setNode(schemaNode);
                                cataUIEle.setOverviewIndicator(schemaIndicator);
                                cataUIEleList.add(cataUIEle);
                            }
                            schemaTableViewer.setInput(cataUIEleList);
                            schemaTableViewer.getTable().setVisible(true);
                            addColumnSorters(schemaTableViewer, schemaTableViewer.getTable().getColumns(), schemaSorters);
                        }
                    }
                    // ~
                }

            });
            createContextMenuFor(schemaTableViewer);
        } else {
            if (catalogs.size() > 0) {
                createCatalogTableColumns(catalogTable);
                provider = new CatalogViewerProvier();
            } else {
                createSchemaTableColumns(catalogTable);
                provider = new SchemaViewerProvier();
            }
            addColumnSorters(catalogTableViewer, catalogTable.getColumns(), schemaSorters);
            catalogTableViewer.addSelectionChangedListener(new DisplayTableAndViewListener());
        }
        catalogTableViewer.setLabelProvider(provider);
        catalogTableViewer.setContentProvider(provider);
        doSetInput();

        tableAndViewComposite = new Composite(sectionClient, SWT.NONE);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(tableAndViewComposite);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.horizontalSpacing = 50;
        tableAndViewComposite.setLayout(layout);
        tableAndViewComposite.setVisible(false);

        sectionClient.layout();
        statisticalSection.setClient(sectionClient);

        createContextMenuFor(catalogTableViewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
        // no implementation
    }

    /*
     * (non-Javadoc)
     * 
     * @seejava.beans.PropertyChangeListener#propertyChange(java.beans. PropertyChangeEvent)
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
    public void refresh(AbstractAnalysisMetadataPage masterPage1) {
        this.masterPage = (AbstractFilterMetadataPage) masterPage1;

        disposeComposite();

        createFormContent(getManagedForm());
    }

    private void disposeComposite() {
        if (summaryComp != null && !summaryComp.isDisposed()) {
            summaryComp.dispose();
        }

        if (resultComp != null && !resultComp.isDisposed()) {
            resultComp.dispose();
        }
    }

    private void createSchemaTableViewer(Composite parent) {
        schemaTableViewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        Table schemaTable = schemaTableViewer.getTable();
        schemaTable.setHeaderVisible(true);
        schemaTable.setBackgroundMode(SWT.INHERIT_FORCE);
        schemaTable.setLinesVisible(true);
        TableUtils.addActionTooltip(schemaTable);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(schemaTable);
        ((GridData) schemaTable.getLayoutData()).heightHint = 60;
        createSchemaTableColumns(schemaTable);
        SchemaViewerProvier svProvider = new SchemaViewerProvier();
        schemaTableViewer.setLabelProvider(svProvider);
        schemaTableViewer.setContentProvider(svProvider);
        schemaTable.setVisible(false);
    }

    private void createCatalogTableColumns(Table table) {
        TableColumn tableColumn = new TableColumn(table, SWT.LEFT | SWT.FILL);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.catalog")); //$NON-NLS-1$
        tableColumn.setWidth(COL1_WIDTH);
        createNbRowsCol(table, DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.catalog")); //$NON-NLS-1$
        createCommonStatisticalColumns(table);
    }

    private void createSchemaTableColumns(Table table) {
        TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.schema")); //$NON-NLS-1$
        tableColumn.setWidth(COL1_WIDTH);
        createNbRowsCol(table, DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.schema")); //$NON-NLS-1$
        createCommonStatisticalColumns(table);
    }

    /**
     * DOC scorreia Comment method "createCommonStatisticalColumns".
     * 
     * @param table
     */
    private void createCommonStatisticalColumns(Table table) {
        TableColumn tableColumn;
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.table")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.numberOfTable")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.rows_table")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.numberOfRowPerTable")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.view")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.numberOfViews")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.rows_view")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.nubmerOfRowsPerView")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.key")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.numberOfPrimaryKeys")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.indexes")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.numberOfIndexes")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
    }

    private void createCatalogSchemaColumns(Table table) {
        TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.catalog")); //$NON-NLS-1$
        tableColumn.setWidth(COL1_WIDTH);
        createNbRowsCol(table, DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.catalog")); //$NON-NLS-1$
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.otherSchemata")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.rows_schema")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.numberofRow")); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
        createCommonStatisticalColumns(table);
    }

    /**
     * The provider for display the data of schema table viewer.
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class SchemaViewerProvier extends AbstractStatisticalViewerProvider {

        @Override
        protected String getOtherColumnText(int columnIndex, SchemaIndicator schemaIndicator) {
            String text = PluginConstant.EMPTY_STRING;
            switch (columnIndex) {
            case 2:
                return text + schemaIndicator.getTableCount();
            case 3:
                return text + getRowCountPerTable(schemaIndicator);
            case 4:
                return text + schemaIndicator.getViewCount();
            case 5:
                return text + getRowCountPerView(schemaIndicator);
            case 6:
                return text + schemaIndicator.getKeyCount();
            case 7:
                return text + schemaIndicator.getIndexCount();
            default:
                break;
            }
            return text;
        }
    }

    /**
     * The provider for display the data of catalog(contain schemas) table viewer.
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class CatalogSchemaViewerProvier extends AbstractStatisticalViewerProvider {

        @Override
        protected String getOtherColumnText(int columnIndex, SchemaIndicator schemaIndicator) {
            String text = PluginConstant.EMPTY_STRING;
            CatalogIndicator catalogIndicator = null;
            if (schemaIndicator instanceof CatalogIndicator) {
                catalogIndicator = (CatalogIndicator) schemaIndicator;
            } else {
                return PluginConstant.EMPTY_STRING;
            }
            switch (columnIndex) {
            case 2:
                return text + catalogIndicator.getSchemaCount();
            case 3:
                return text + getRowCountPerSchema(catalogIndicator);
            case 4:
                return text + catalogIndicator.getTableCount();
            case 5:
                return text + getRowCountPerTable(catalogIndicator);
            case 6:
                return text + schemaIndicator.getViewCount();
            case 7:
                return text + getRowCountPerView(schemaIndicator);
            case 8:
                return text + catalogIndicator.getKeyCount();
            case 9:
                return text + catalogIndicator.getIndexCount();
            default:
                break;
            }
            return text;
        }
    }

    /**
     * DOC scorreia Comment method "createNbRowsCol".
     * 
     * @param table
     * @param container
     */
    private void createNbRowsCol(Table table, String container) {
        TableColumn tableColumn;
        tableColumn = new TableColumn(table, SWT.LEFT);
        tableColumn.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.rows")); //$NON-NLS-1$
        tableColumn.setToolTipText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.numberOfRows", container)); //$NON-NLS-1$
        tableColumn.setWidth(COL_WIDTH);
    }

    protected static String getRowCountPerTable(SchemaIndicator schemaIndicator) {
        Double frac = Double.valueOf(schemaIndicator.getTableRowCount()) / schemaIndicator.getTableCount();
        return formatDouble(frac);
    }

    protected static String getRowCountPerView(SchemaIndicator schemaIndicator) {
        Double frac = Double.valueOf(schemaIndicator.getViewRowCount()) / schemaIndicator.getViewCount();
        return formatDouble(frac);
    }

    protected static String getRowCountPerSchema(CatalogIndicator catalogIndicator) {
        Double frac = Double.valueOf(catalogIndicator.getTableRowCount()) / catalogIndicator.getSchemaCount();
        return formatDouble(frac);
    }

    private static String formatDouble(Double frac) {
        if (frac.isNaN()) {
            return String.valueOf(Double.NaN);
        }
        String formatValue = new DecimalFormat("0.00").format(frac); //$NON-NLS-1$
        return formatValue;
    }

    class DisplayTableAndViewListener implements ISelectionChangedListener {

        public void selectionChanged(SelectionChangedEvent event) {
            StructuredSelection selection = (StructuredSelection) event.getSelection();
            OverviewIndUIElement firstElement = (OverviewIndUIElement) selection.getFirstElement();
            if (firstElement != null) {
                // TableViewer viewer = (TableViewer) event.getSource();

                IRepositoryNode node = firstElement.getNode();
                currentSelectionSchemaIndicator = (SchemaIndicator) firstElement.getOverviewIndicator();
                if (currentSelectionSchemaIndicator == null) {
                    return;
                }
                displayTableAndViewComp(currentSelectionSchemaIndicator, node);
            }
        }
    }

    /**
     * The provider for display the data of catalog table viewer.
     * 
     * FIXME this inner class should be static. Confirm and fix the error.
     */
    class CatalogViewerProvier extends AbstractStatisticalViewerProvider {

        @Override
        protected String getOtherColumnText(int columnIndex, SchemaIndicator schemaIndicator) {
            String text = PluginConstant.EMPTY_STRING;
            switch (columnIndex) {
            case 1:
                return text + schemaIndicator.getTableRowCount();
            case 2:
                return text + schemaIndicator.getTableCount();
            case 3:
                return text + getRowCountPerTable(schemaIndicator);
            case 4:
                return text + schemaIndicator.getViewCount();
            case 5:
                return text + getRowCountPerView(schemaIndicator);
            case 6:
                return text + schemaIndicator.getKeyCount();
            case 7:
                return text + schemaIndicator.getIndexCount();
            default:
                break;
            }
            return text;
        }
    }

    /**
     * 
     * DOC klliu Comment method "displayTableAndViewComp".
     * 
     * @param schemaIndicator
     * @param parentNode
     */
    protected void displayTableAndViewComp(final SchemaIndicator schemaIndicator, final IRepositoryNode parentNode) {
        tableAndViewComposite.setVisible(true);
        // DOC wapperInput retrun OverViewUIElement

        EList<TableIndicator> indicatorTableList = schemaIndicator.getTableIndicators();
        List<OverviewIndUIElement> tableElements = wapperInput(indicatorTableList, parentNode);
        if (tableOfCatalogOrSchemaViewer == null || tableOfCatalogOrSchemaViewer.getTable().isDisposed()) {
            tableOfCatalogOrSchemaViewer = new TableViewer(tableAndViewComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
                    | SWT.FULL_SELECTION); // SWT.SINGLE);
            final Table catalogOrSchemaTable = tableOfCatalogOrSchemaViewer.getTable();
            TableUtils.addActionTooltip(catalogOrSchemaTable);
            catalogOrSchemaTable.setHeaderVisible(true);
            catalogOrSchemaTable.setLinesVisible(true);
            GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
            layoutData.heightHint = 150;
            catalogOrSchemaTable.setLayoutData(layoutData);
            String[] columnTexts = new String[] {
                    DefaultMessagesImpl.getString("AbstractFilterMetadataPage.Table"), DefaultMessagesImpl.getString("AbstractFilterMetadataPage.rows"), DefaultMessagesImpl.getString("AbstractFilterMetadataPage.keys"), DefaultMessagesImpl.getString("AbstractFilterMetadataPage.indexes") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            createSorterColumns(tableOfCatalogOrSchemaViewer, columnTexts, tableSorters, COLUMN_TABLE_WIDTH);
            TableOfCatalogOrSchemaProvider tableProvider = new TableOfCatalogOrSchemaProvider();
            tableOfCatalogOrSchemaViewer.setLabelProvider(tableProvider);
            tableOfCatalogOrSchemaViewer.setContentProvider(tableProvider);

            catalogOrSchemaTable.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    if (e.button == 3) {
                        TableItem item = catalogOrSchemaTable.getItem(catalogOrSchemaTable.getSelectionIndex());
                        // TDQ-11430: show the menu only when there have data and have selected one.
                        if (catalogOrSchemaTable.getItemCount() > 0 && catalogOrSchemaTable.getSelectionIndex() != -1) {
                            final Menu menu = new Menu(catalogOrSchemaTable.getShell(), SWT.POP_UP);
                            catalogOrSchemaTable.setMenu(menu);

                            MenuItem viewKeyMenuItem = new MenuItem(menu, SWT.PUSH);
                            viewKeyMenuItem.setText(DefaultMessagesImpl.getString("AbstractFilterMetadataPage.ViewKeys")); //$NON-NLS-1$
                            viewKeyMenuItem.setImage(ImageLib.getImage(ImageLib.PK_DECORATE));
                            viewKeyMenuItem.addSelectionListener(new SelectionAdapter() {

                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                    runMenu(catalogOrSchemaTable,
                                            DefaultMessagesImpl.getString("DatabaseDetailView.Tab.PrimaryKeys")); //$NON-NLS-1$
                                }

                            });

                            MenuItem viewIndexMenuItem = new MenuItem(menu, SWT.PUSH);
                            viewIndexMenuItem.setText(DefaultMessagesImpl.getString("AbstractFilterMetadataPage.ViewIndexes")); //$NON-NLS-1$
                            viewIndexMenuItem.setImage(ImageLib.getImage(ImageLib.INDEX_VIEW));
                            viewIndexMenuItem.addSelectionListener(new SelectionAdapter() {

                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                    runMenu(catalogOrSchemaTable, DefaultMessagesImpl.getString("DatabaseDetailView.Tab.Indexes")); //$NON-NLS-1$
                                }

                            });
                            TableItem tableItem = catalogOrSchemaTable.getItem(catalogOrSchemaTable.getSelectionIndex());
                            final OverviewIndUIElement data = (OverviewIndUIElement) tableItem.getData();
                            MenuItem tableAnalysisMenuItem = new MenuItem(menu, SWT.PUSH);
                            tableAnalysisMenuItem.setText(DefaultMessagesImpl
                                    .getString("CreateTableAnalysisAction.tableAnalysis")); //$NON-NLS-1$
                            tableAnalysisMenuItem.setImage(ImageLib.getImage(ImageLib.ACTION_NEW_ANALYSIS));
                            tableAnalysisMenuItem.addSelectionListener(new SelectionAdapter() {

                                @Override
                                public void widgetSelected(SelectionEvent e) {

                                    runTableAnalysis(data);
                                }

                            });
                            if (data.isVirtualNode()) {
                                tableAnalysisMenuItem.setEnabled(false);
                            }
                        } else {
                            // TDQ-11430: when there is no views will not show menu
                            catalogOrSchemaTable.setMenu(null);
                        }
                    }
                }
            });

            viewOfCatalogOrSchemaViewer = new TableViewer(tableAndViewComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER
                    | SWT.FULL_SELECTION);
            final Table tableCatalogOrSchemaView = viewOfCatalogOrSchemaViewer.getTable();
            TableUtils.addActionTooltip(tableCatalogOrSchemaView);
            tableCatalogOrSchemaView.setHeaderVisible(true);
            tableCatalogOrSchemaView.setLinesVisible(true);
            layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
            layoutData.heightHint = 150;
            tableCatalogOrSchemaView.setLayoutData(layoutData);
            columnTexts = new String[] {
                    DefaultMessagesImpl.getString("AbstractFilterMetadataPage.view"), DefaultMessagesImpl.getString("AbstractFilterMetadataPage.rows") }; //$NON-NLS-1$ //$NON-NLS-2$
            createSorterColumns(viewOfCatalogOrSchemaViewer, columnTexts, viewSorters, COLUMN_VIEW_WIDTH);
            ViewOfCatalogOrSchemaProvider viewProvider = new ViewOfCatalogOrSchemaProvider();
            viewOfCatalogOrSchemaViewer.setLabelProvider(viewProvider);
            viewOfCatalogOrSchemaViewer.setContentProvider(viewProvider);

            // ADD msjian TDQ-4523 2013-1-22: Add "Table analysis" menu on the views
            tableCatalogOrSchemaView.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    if (e.button == 3) {
                        // TDQ-11430: show the menu only when there have data and have selected one.
                        TableItem tableItem = tableCatalogOrSchemaView.getItem(tableCatalogOrSchemaView.getSelectionIndex());
                        final OverviewIndUIElement data = (OverviewIndUIElement) tableItem.getData();

                        if (tableCatalogOrSchemaView.getItemCount() > 0 && tableCatalogOrSchemaView.getSelectionIndex() != -1) {
                            final Menu menu = new Menu(tableCatalogOrSchemaView.getShell(), SWT.POP_UP);
                            tableCatalogOrSchemaView.setMenu(menu);

                            MenuItem tableAnalysisMenuItem = new MenuItem(menu, SWT.PUSH);
                            tableAnalysisMenuItem.setText(DefaultMessagesImpl
                                    .getString("CreateTableAnalysisAction.tableAnalysis")); //$NON-NLS-1$
                            tableAnalysisMenuItem.setImage(ImageLib.getImage(ImageLib.ACTION_NEW_ANALYSIS));
                            tableAnalysisMenuItem.addSelectionListener(new SelectionAdapter() {

                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                    ViewIndicator viewIndicator = (ViewIndicator) data.getOverviewIndicator();
                                    runTableAnalysis(viewIndicator.getTableName());
                                }

                            });
                            if (data.isVirtualNode()) {
                                tableAnalysisMenuItem.setEnabled(false);
                            }
                        } else {
                            // TDQ-11430: when there is no views will not show menu
                            tableCatalogOrSchemaView.setMenu(null);
                        }
                    }
                }
            });
            // TDQ-4523~
        }
        tableOfCatalogOrSchemaViewer.getTable().setMenu(null);
        tableOfCatalogOrSchemaViewer.setInput(tableElements);
        List<ViewIndicator> indicatorViewList = schemaIndicator.getViewIndicators();
        List<OverviewIndUIElement> viewElements = wapperInput(indicatorViewList, parentNode);
        viewOfCatalogOrSchemaViewer.setInput(viewElements);
        viewOfCatalogOrSchemaViewer.getTable().setMenu(null);
        // MOD xqliu 2009-11-05 bug 9521
        tableAndViewComposite.pack();
        statisticalSection.pack();
        statisticalSection.layout();
        // ~
        form.reflow(true);

    }

    /**
     * DOC zshen Comment method "wapperInput".
     * 
     * @param indicatorViewList
     * @param parentNode
     * @return
     */
    private List<OverviewIndUIElement> wapperInput(List<ViewIndicator> indicatorViewList, IRepositoryNode parentNode) {
        List<OverviewIndUIElement> cataUIEleList = new ArrayList<OverviewIndUIElement>();
        List<IRepositoryNode> children = parentNode.getChildren();
        for (IRepositoryNode folderNode : children) {
            if (folderNode instanceof DBViewFolderRepNode) {
                List<IRepositoryNode> tableNodes = folderNode.getChildren();
                // MOD 20120315 klliu&yyin TDQ-2391, avoid getting many times for table nodes.
                for (ViewIndicator indicator : indicatorViewList) {
                    boolean equals = false;
                    for (IRepositoryNode tableNode : tableNodes) {
                        MetadataTable table = ((MetadataTableRepositoryObject) tableNode.getObject()).getTable();
                        String name = table.getName();
                        String tableName = indicator.getTableName();
                        // String connUuid = ResourceHelper.getUUID(table);
                        // String anaUuid = ResourceHelper.getUUID(indicator.getAnalyzedElement());

                        equals = name.equals(tableName);
                        if (equals) {
                            OverviewIndUIElement tableUIEle = new OverviewIndUIElement();
                            tableUIEle.setNode(tableNode);
                            tableUIEle.setOverviewIndicator(indicator);
                            if (DqRepositoryViewService.isComeFromRefrenceProject(getTdDataProvider())
                                    && ColumnSetHelper.getColumns((ColumnSet) table).isEmpty()) {
                                tableUIEle.setVirtualNode(true);
                            }

                            cataUIEleList.add(tableUIEle);
                            break;
                        }
                    }
                    if (!equals) {
                        OverviewIndUIElement tableUIEle = new OverviewIndUIElement();
                        tableUIEle.setOverviewIndicator(indicator);
                        tableUIEle.setVirtualNode(true);
                        cataUIEleList.add(tableUIEle);
                    }
                }
            }
        }

        return cataUIEleList;
    }

    private void runTableAnalysis(OverviewIndUIElement data) {
        new AnalyzeColumnSetAction(data.getNode()).run();
    }

    /**
     * run TableAnalysis with view name.
     * 
     * @param viewName
     */
    protected void runTableAnalysis(String viewName) {
        Package parentPack = (Package) currentSelectionSchemaIndicator.getAnalyzedElement();
        TdView tdView = getView(parentPack, viewName);
        if (null == tdView) {
            FolderNodeHelper.getTableFolderNode(parentPack).loadChildren();
            tdView = getView(parentPack, viewName);
        }
        DBViewRepNode dbViewRepNode = RepositoryNodeHelper.recursiveFindTdView(tdView);
        new AnalyzeColumnSetAction(dbViewRepNode).run();
    }

    /**
     * when select menu, run.
     * 
     * @param cursor
     * @param message
     */
    private void runMenu(final Table catalogOrSchemaTable, String message) {
        TableItem tableItem = catalogOrSchemaTable.getItem(catalogOrSchemaTable.getSelectionIndex());
        String tableName = tableItem.getText(0);
        Package parentPack = (Package) currentSelectionSchemaIndicator.getAnalyzedElement();
        // MOD qiongli bug 13093,2010-7-2
        // MOD 20130409 TDQ-6823 yyin pass the schema insteadof catalog when schema has value
        if (currentCatalogIndicator != null && parentPack == null) {
            parentPack = (Package) currentCatalogIndicator.getAnalyzedElement();
        }

        SqlExplorerUtils.getDefault().findSqlExplorerTableNode(getTdDataProvider(), parentPack, tableName, message);
    }

    /**
     * DOC klliu Comment method "wapperInput". relations
     * 
     * @param indicatorTableList
     * @param parentNode
     * @return
     */
    private List<OverviewIndUIElement> wapperInput(EList<TableIndicator> indicatorTableList, IRepositoryNode parentNode) {
        List<OverviewIndUIElement> cataUIEleList = new ArrayList<OverviewIndUIElement>();
        List<IRepositoryNode> children = parentNode.getChildren();
        for (IRepositoryNode folderNode : children) {
            if (folderNode instanceof DBTableFolderRepNode) {
                List<IRepositoryNode> tableNodes = folderNode.getChildren();
                // MOD 20120315 klliu&yyin TDQ-2391, avoid getting many times for table nodes.
                for (TableIndicator indicator : indicatorTableList) {
                    boolean equals = false;
                    for (IRepositoryNode tableNode : tableNodes) {
                        MetadataTable table = ((MetadataTableRepositoryObject) tableNode.getObject()).getTable();
                        String name = table.getName();
                        String tableName = indicator.getTableName();
                        // String connUuid = ResourceHelper.getUUID(table);
                        // String anaUuid = ResourceHelper.getUUID(indicator.getAnalyzedElement());

                        equals = name.equals(tableName);
                        if (equals) {
                            OverviewIndUIElement tableUIEle = new OverviewIndUIElement();
                            tableUIEle.setNode(tableNode);
                            tableUIEle.setOverviewIndicator(indicator);
                            if (DqRepositoryViewService.isComeFromRefrenceProject(getTdDataProvider())
                                    && ColumnSetHelper.getColumns((ColumnSet) table).isEmpty()) {
                                tableUIEle.setVirtualNode(true);
                            }
                            cataUIEleList.add(tableUIEle);
                            break;
                        }
                    }
                    if (!equals) {
                        OverviewIndUIElement tableUIEle = new OverviewIndUIElement();
                        tableUIEle.setOverviewIndicator(indicator);
                        tableUIEle.setVirtualNode(true);
                        cataUIEleList.add(tableUIEle);
                    }
                }
            }
        }

        return cataUIEleList;
    }

    private void createSorterColumns(final TableViewer tableViewer, String[] columnTexts, ViewerSorter[][] sorters,
            int columnWidth) {
        Table table = tableViewer.getTable();
        TableColumn[] columns = new TableColumn[columnTexts.length];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new TableColumn(table, SWT.LEFT | SWT.FILL);
            columns[i].setText(columnTexts[i]);
            columns[i].setWidth(columnWidth);
            columns[i].addSelectionListener(new ColumnSortListener(columns, i, tableViewer, sorters));
        }
    }

    /**
     * get view from package by its name.
     * 
     * @param pkg
     * @param viewName
     * @return
     */
    private TdView getView(Package pkg, String viewName) {
        for (TdView view : ViewHelper.getViews(pkg.getOwnedElement())) {
            if (view.getName().equals(viewName)) {
                return view;
            }
        }
        return null;
    }

    protected void createContextMenuFor(final StructuredViewer viewer) {
        final MenuManager contextMenu = new MenuManager("#PopUp");//$NON-NLS-1$
        contextMenu.add(new Separator("additions"));//$NON-NLS-1$
        contextMenu.setRemoveAllWhenShown(true);
        contextMenu.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                Object overviewObject = ((StructuredSelection) viewer.getSelection()).getFirstElement();
                if (overviewObject instanceof OverviewIndUIElement) {
                    OverviewIndUIElement overview = (OverviewIndUIElement) overviewObject;
                    IRepositoryNode node = overview.getNode();
                    List<IRepositoryNode> nodes = new ArrayList<IRepositoryNode>();
                    nodes.add(node);
                    contextMenu.add(new OverviewAnalysisAction(nodes));
                }
                // if (obj instanceof SchemaIndicator) {
                // SchemaIndicator schemaIndicator = (SchemaIndicator) obj;
                // contextMenu.add(new OverviewAnalysisAction(new Package[] { (Package)
                // schemaIndicator.getAnalyzedElement() }));
                // }
            }
        });
        Menu menu = contextMenu.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }

    /**
     * DOC qzhang Comment method "doSetInput".
     */
    public void doSetInput() {
        List<OverviewIndUIElement> indicatorList = null;
        if (masterPage.getCurrentModelElement().getResults().getIndicators().size() > 0) {
            indicatorList = getCatalogIndicators();
            if (indicatorList.size() == 0) {
                catalogTableViewer.setInput(getSchemaIndicators());
            } else {
                // MOD xqliu 2009-11-30 bug 9114
                // List<SchemaIndicator> schemaIndicators = new ArrayList<SchemaIndicator>();
                // schemaIndicators.addAll(getSchemaIndicators());
                // schemaIndicators.addAll(indicatorList);
                // catalogTableViewer.setInput(schemaIndicators);
                catalogTableViewer.setInput(indicatorList);
                // ~
            }
        }
    }

    /**
     * DOC xqliu Comment method "addColumnSorters".
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

    /**
     * DOC qzhang Comment method "refreshSumSection".
     * 
     * @param summarySection
     */
    private void refreshSumSection() {

        fillDataProvider();
        if (getTdDataProvider() == null) {
            return;
        }

        if (sumSectionClient != null && !sumSectionClient.isDisposed()) {
            Control[] children = sumSectionClient.getChildren();
            for (Control control : children) {
                control.dispose();
            }
        }
        Composite leftComp = new Composite(sumSectionClient, SWT.NONE);
        leftComp.setLayout(new GridLayout(2, false));
        GridData subCompData = new GridData(GridData.FILL_HORIZONTAL);
        subCompData.verticalAlignment = GridData.BEGINNING;
        leftComp.setLayoutData(subCompData);
        Composite rightComp = new Composite(sumSectionClient, SWT.NONE);
        rightComp.setLayout(new GridLayout(2, false));
        rightComp.setLayoutData(subCompData);

        String connectionStr = JavaSqlFactory.getURL(getTdDataProvider());
        Properties pameterProperties = SupportDBUrlStore.getInstance().getDBPameterProperties(connectionStr);
        toolkit.createLabel(leftComp, DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.DBMS")); //$NON-NLS-1$
        // MOD TDQ-8539, find the db type from the connection, not from the properties file
        toolkit.createLabel(leftComp, getDatabaseType(pameterProperties));

        String serverName = JavaSqlFactory.getServerName(getTdDataProvider());
        toolkit.createLabel(leftComp, DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.server")); //$NON-NLS-1$
        toolkit.createLabel(leftComp, serverName == null ? PluginConstant.EMPTY_STRING : serverName);

        String port = JavaSqlFactory.getPort(getTdDataProvider());
        toolkit.createLabel(leftComp, DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.port")); //$NON-NLS-1$
        toolkit.createLabel(leftComp, port == null ? PluginConstant.EMPTY_STRING : port);

        String username = JavaSqlFactory.getUsername(getTdDataProvider());
        toolkit.createLabel(leftComp, DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.connectAs")); //$NON-NLS-1$
        toolkit.createLabel(leftComp, username == null ? PluginConstant.EMPTY_STRING : username);

        List<Catalog> tdCatalogs = getCatalogs();
        // TDQ-6735 get the correct numbers of schema.
        // MOD TDQ-8539 , get the schemas size
        int schemaSize = masterPage.getSchamas(tdCatalogs);
        toolkit.createLabel(leftComp,
                DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.catalogs", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(leftComp, PluginConstant.EMPTY_STRING + tdCatalogs.size());

        toolkit.createLabel(leftComp,
                DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.schemata", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(leftComp, PluginConstant.EMPTY_STRING + schemaSize);

        ExecutionInformations resultMetadata = masterPage.getCurrentModelElement().getResults().getResultMetadata();

        toolkit.createLabel(rightComp,
                DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.createionDate", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(rightComp, getFormatDateStr(masterPage.getCurrentModelElement().getCreationDate()));
        toolkit.createLabel(rightComp,
                DefaultMessagesImpl.getString("AbstractAnalysisResultPage.executionDate", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(rightComp, getFormatDateStr(resultMetadata.getExecutionDate()));

        toolkit.createLabel(rightComp,
                DefaultMessagesImpl.getString("AbstractAnalysisResultPage.executionDuration", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(rightComp, PluginConstant.EMPTY_STRING + resultMetadata.getExecutionDuration() / 1000.0d + "s"); //$NON-NLS-1$

        String executeStatus = (resultMetadata.isLastRunOk() ? DefaultMessagesImpl
                .getString("ConnectionMasterDetailsPage.success") : DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.failure", resultMetadata.getMessage())); //$NON-NLS-1$ //$NON-NLS-2$
        Label rightLabel = toolkit.createLabel(rightComp,
                DefaultMessagesImpl.getString("AbstractAnalysisResultPage.executionStatus"));//$NON-NLS-1$  
        int executionNumber = resultMetadata.getExecutionNumber();
        if (!resultMetadata.isLastRunOk() && executionNumber != 0) {
            rightLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        }
        rightLabel = toolkit.createLabel(rightComp, executionNumber == 0 ? PluginConstant.EMPTY_STRING : executeStatus);

        toolkit.createLabel(rightComp,
                DefaultMessagesImpl.getString("AbstractAnalysisResultPage.numberOfExecution", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(rightComp, PluginConstant.EMPTY_STRING + executionNumber);
        toolkit.createLabel(rightComp,
                DefaultMessagesImpl.getString("AbstractAnalysisResultPage.lastSucessfulExecution", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(rightComp, PluginConstant.EMPTY_STRING + resultMetadata.getLastExecutionNumberOk());
        // MOD qiongli 2011-5-16
        DataManager connection = masterPage.getCurrentModelElement().getContext().getConnection();
        if (connection != null) {
            RepositoryNode connNode = RepositoryNodeHelper.recursiveFind(connection);
            if (connNode != null && connNode.getObject().isDeleted()) {
                Label leftLabel = toolkit.createLabel(leftComp,
                        DefaultMessagesImpl.getString("AbstractPagePart.LogicalDeleteWarn", connNode.getLabel())); //$NON-NLS-1$
                leftLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                toolkit.createLabel(rightComp, PluginConstant.EMPTY_STRING);
                toolkit.createLabel(rightComp, PluginConstant.EMPTY_STRING);
            }
        }

        sumSectionClient.layout();
    }

    /**
     * DOC msjian Comment method "getTdDataProvider".
     * 
     * @return Connection
     */
    protected Connection getTdDataProvider() {
        return masterPage.getTdDataProvider();
    }

    private String getFormatDateStr(Date date) {
        if (date == null) {
            return PluginConstant.EMPTY_STRING;
        }
        String format = SimpleDateFormat.getDateTimeInstance().format(date);
        return format;
    }

    /**
     * get the connection type from the connection, when the connection is DB. otherwise, just return the labelCOntent
     * which get the value from the properties.
     * 
     * @param labelContent
     * @return
     */
    private String getDatabaseType(Properties pameterProperties) {
        DataManager dataManager = masterPage.getCurrentModelElement().getContext().getConnection();
        if (dataManager instanceof DatabaseConnection) {
            return ((DatabaseConnection) dataManager).getDatabaseType();
        } else {
            return pameterProperties.getProperty(org.talend.core.model.metadata.builder.database.PluginConstant.DBTYPE_PROPERTY);
        }
    }

    @Override
    public void dispose() {
        if (provider != null) {
            provider.getZeroRowColor().dispose();
        }
        super.dispose();
    }

    void fillDataProvider() {
        masterPage.fillDataProvider();
    }

    List<Catalog> getCatalogs() {
        return masterPage.getCatalogs();
    }

    List<OverviewIndUIElement> getCatalogIndicators() {
        return masterPage.getCatalogIndicators();
    }

    List<OverviewIndUIElement> getSchemaIndicators() {
        return masterPage.getSchemaIndicators();
    }
}
