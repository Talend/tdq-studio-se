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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnCompareTreeViewer;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnDependencyIndicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dq.analysis.AnalysisBuilder;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.ModelElementAnalysisHandler;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * This is a Master Detail Page for <a href="http://www.talendforge.org/bugs/view.php?id=8134">Functional Dependency
 * Analysis</a>.
 * 
 * @author jet
 */
public class FunctionalDependencyAnalysisDetailsPage extends AbstractAnalysisMetadataPage implements PropertyChangeListener {

    private static Logger log = Logger.getLogger(FunctionalDependencyAnalysisDetailsPage.class);

    private Section columnsComparisonSection = null;

    List<RepositoryNode> columnListA = null;

    List<RepositoryNode> columnListB = null;

    private AnalysisColumnCompareTreeViewer anaColumnCompareViewer;

    ModelElementAnalysisHandler analysisHandler;

    /**
     * @param editor
     * @param id
     * @param title
     */
    public FunctionalDependencyAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    public Section getColumnsComparisonSection() {
        return this.columnsComparisonSection;
    }

    public void setColumnsComparisonSection(Section columnsComparisonSection) {
        this.columnsComparisonSection = columnsComparisonSection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#initialize(org.eclipse.ui.forms.editor.FormEditor
     * )
     */
    @Override
    public void initialize(FormEditor editor) {
        super.initialize(editor);
        analysisHandler = new ModelElementAnalysisHandler();
        analysisHandler.setAnalysis(getCurrentModelElement());
        stringDataFilter = analysisHandler.getStringDataFilterwithContext();
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("ColumnDependencyMasterDetailsPage.ColumnDependencyAnalysis")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("ColumnDependencyMasterDetailsPage.analysisMeta")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.setAnalysisProperties")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        createDataPreviewSection(form, topComp, false, false, false);

        anaColumnCompareViewer = new AnalysisColumnCompareTreeViewer(this, topComp, getColumnLeftSet(), getColumnRightSet(),
                DefaultMessagesImpl.getString("FunctionalDependencyMasterDetailsPage.Title"), DefaultMessagesImpl //$NON-NLS-1$
                        .getString("FunctionalDependencyMasterDetailsPage.Description"), false, true); //$NON-NLS-1$

        anaColumnCompareViewer.addPropertyChangeListener(this);
        // the sampletable only observer the viewer
        anaColumnCompareViewer.addObserver(sampleTable);

        createDataFilterSection(form, topComp);
        dataFilterComp.addPropertyChangeListener(this);
        dataFilterComp.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                AnalysisHelper.setStringDataFilter(getCurrentModelElement(), dataFilterComp.getDataFilterString());
            }
        });

        createAnalysisParamSection(form, topComp);

        createContextGroupSection(form, topComp);

        columnListA = anaColumnCompareViewer.getColumnListA();
        columnListB = anaColumnCompareViewer.getColumnListB();

        columnsComparisonSection = anaColumnCompareViewer.getColumnsComparisonSection();
        anaColumnCompareViewer.addPropertyChangeListener(this);
        form.reflow(true);
    }

    @Override
    public ScrolledForm getScrolledForm() {
        return form;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#canRun()
     */
    @Override
    protected ReturnCode canRun() {
        return columnListA.size() > 0 ? new ReturnCode(true) : new ReturnCode(
                DefaultMessagesImpl.getString("ColumnDependencyMasterDetailsPage.columnsBlankRunMessage"), false); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#canSave()
     */
    @Override
    public ReturnCode canSave() {
        // do some validator.
        return validator(columnListA, columnListB);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#refresh()
     */
    @Override
    public void refreshGraphicsInSettingsPage() {
        // do nothing
    }

    @Override
    public AnalysisHandler getAnalysisHandler() {
        return analysisHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#saveAnalysis()
     */
    @Override
    protected void saveAnalysis() throws DataprofilerCoreException {
        analysisHandler.changeDefaultRowLoaded(rowLoadedText.getText());

        analysisHandler.changeSampleDataShowWay(sampleDataShowWayCombo.getText());

        for (Domain domain : getCurrentModelElement().getParameters().getDataFilter()) {
            domain.setName(getCurrentModelElement().getName());
        }

        IRepositoryViewObject reposObject = null;
        getAnalysisHandler().clearAnalysis();

        List<RepositoryNode> columnListAANode = anaColumnCompareViewer.getColumnListA();
        List<RepositoryNode> columnListBBNode = anaColumnCompareViewer.getColumnListB();

        AnalysisBuilder anaBuilder = new AnalysisBuilder();
        anaBuilder.setAnalysis(getCurrentModelElement());
        Connection tdDataProvider = null;

        for (int i = 0; i < columnListAANode.size(); i++) {
            if (columnListBBNode.size() > i) {
                ColumnDependencyIndicator indicator = ColumnsetFactory.eINSTANCE.createColumnDependencyIndicator();
                TdColumn columnA = (TdColumn) ((MetadataColumnRepositoryObject) columnListAANode.get(i).getObject())
                        .getTdColumn();
                TdColumn columnB = (TdColumn) ((MetadataColumnRepositoryObject) columnListBBNode.get(i).getObject())
                        .getTdColumn();
                indicator.setColumnA(columnA);
                indicator.setColumnB(columnB);
                indicator.setIndicatorDefinition(DefinitionHandler.getInstance().getFDRuleDefaultIndicatorDefinition());
                getCurrentModelElement().getResults().getIndicators().add(indicator);
                anaBuilder.addElementToAnalyze(columnA, indicator);
                // ADD this line qiongli 2010-6-8
                anaBuilder.addElementToAnalyze(columnB, indicator);
            }
        }

        if (columnListAANode.size() > 0) {
            reposObject = columnListAANode.get(0).getObject();
            tdDataProvider = ((ConnectionItem) reposObject.getProperty().getItem()).getConnection();
            // MOD qiongli bug 14437:Add dependency
            getCurrentModelElement().getContext().setConnection(tdDataProvider);
            TypedReturnCode<Dependency> rc = DependenciesHandler.getInstance().setDependencyOn(getCurrentModelElement(),
                    tdDataProvider);
            if (!rc.isOk()) {
                log.info("fail to save dependency analysis:" + getCurrentModelElement().getFileName());//$NON-NLS-1$
            }
        } else {
            getCurrentModelElement().getContext().setConnection(null);
        }

        // save the number of connections per analysis
        this.saveNumberOfConnectionsPerAnalysis();

        // 2011.1.12 MOD by zhsne to unify anlysis and connection id when saving.
        this.nameText.setText(getCurrentModelElement().getName());

        // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
        ReturnCode saved = ElementWriterFactory.getInstance().createAnalysisWrite()
                .save(getCurrentRepNode().getObject().getProperty().getItem(), true);
        // MOD yyi 2012-02-03 TDQ-3602:Avoid to rewriting all analyzes after saving, no reason to update all analyzes
        // which is depended in the referred connection.
        // Extract saving log function.
        // @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#logSaved(ReturnCode)
        logSaved(saved);

        anaColumnCompareViewer.setDirty(false);
        dataFilterComp.setDirty(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) currentEditor).firePropertyChange(IEditorPart.PROP_DIRTY);
            ((AnalysisEditor) currentEditor).setRefreshResultPage(true);
        } else if (PluginConstant.DATAFILTER_PROPERTY.equals(evt.getPropertyName())) {
            this.setDirty(true);
        }
    }

    private List<RepositoryNode> getColumnLeftSet() {
        return getColumnSet(ColumnsetPackage.Literals.COLUMN_DEPENDENCY_INDICATOR__COLUMN_A);
    }

    private List<RepositoryNode> getColumnRightSet() {
        return getColumnSet(ColumnsetPackage.Literals.COLUMN_DEPENDENCY_INDICATOR__COLUMN_B);
    }

    private List<RepositoryNode> getColumnSet(EReference reference) {
        List<RepositoryNode> columns = new ArrayList<RepositoryNode>();
        EList<Indicator> indicators = getCurrentModelElement().getResults().getIndicators();
        for (Indicator indicator : indicators) {
            TdColumn findColumn = (TdColumn) indicator.eGet(reference);
            RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(findColumn);
            if (recursiveFind == null) {
                recursiveFind = RepositoryNodeHelper.createRepositoryNode(findColumn);
            }
            if (recursiveFind != null) {
                columns.add(recursiveFind);
            }
        }
        return columns;
    }

    private ReturnCode validator(List<RepositoryNode> columnASet, List<RepositoryNode> columnBSet) {
        // MOD by gdbu 2011-3-21 bug 19179
        ReturnCode canModRetCode = super.canSave();
        if (!canModRetCode.isOk()) {
            return canModRetCode;
        }
        // ~19179

        if (columnASet.isEmpty() || columnBSet.isEmpty()) {
            return new ReturnCode(DefaultMessagesImpl.getString("ColumnDependencyMasterDetailsPage.columnsBlankMessag"), false); //$NON-NLS-1$
        }
        if (columnASet.size() != columnBSet.size()) {
            // two set must have equal number.
            return new ReturnCode(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.columnsSameMessage"), false); //$NON-NLS-1$
        }

        for (int i = 0; i < columnASet.size(); i++) {
            RepositoryNode columnANode = columnASet.get(i);
            RepositoryNode columnBNode = columnBSet.get(i);
            TdColumn tdColumnA = ((DBColumnRepNode) columnANode).getTdColumn();
            TdColumn tdColumnB = ((DBColumnRepNode) columnBNode).getTdColumn();
            ColumnSet ownerA = ColumnHelper.getColumnOwnerAsColumnSet(tdColumnA);
            ColumnSet ownerB = ColumnHelper.getColumnOwnerAsColumnSet(tdColumnB);
            String uuidA = ResourceHelper.getUUID(ownerA);
            String uuidB = ResourceHelper.getUUID(ownerB);
            if (!uuidA.equals(uuidB)) {
                // must come from one table
                return new ReturnCode(DefaultMessagesImpl.getString("ColumnDependencyMasterDetailsPage.tableMessage"), false); //$NON-NLS-1$
            }
        }

        return new ReturnCode(true);
    }

    /**
     * 
     * DOC zshen DOC mzhao Open column selection dialog for left column set. this method is intended to use from cheat
     * sheets.
     */
    public void openColumnsSetASelectionDialog() {
        anaColumnCompareViewer.openColumnsSetASelectionDialog();
    }

    /**
     * 
     * DOC zshen DOC mzhao Open column selection dialog for right column set. this method is intended to use from cheat
     * sheets.
     */
    public void openColumnsSetBSelectionDialog() {
        anaColumnCompareViewer.openColumnsSetBSelectionDialog();
    }

    /**
     * ADD xqliu 2012-04-19 TDQ-5005.
     */
    @Override
    public boolean isDirty() {
        if (anaColumnCompareViewer == null ? false : anaColumnCompareViewer.isDirty()) {
            this.setDirty(anaColumnCompareViewer.isDirty());
        }
        if (dataFilterComp == null ? false : dataFilterComp.isDirty()) {
            this.setDirty(dataFilterComp.isDirty());
        }
        return super.isDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#updateTreeView()
     */
    @Override
    protected void updateAnalysisTree() {
        if (anaColumnCompareViewer != null) {
            boolean beforeUpdateDirty = anaColumnCompareViewer.isDirty();
            anaColumnCompareViewer.updateModelViewer();
            if (!beforeUpdateDirty) {
                anaColumnCompareViewer.setDirty(false);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#getConnectionsWithoutDeleted()
     */
    @Override
    protected List<IRepositoryNode> getConnectionsWithoutDeleted() {
        return RepositoryNodeHelper.getConnectionRepositoryNodes(false, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#getSelectedColumns()
     */
    @Override
    protected ModelElement[] getSelectedColumns() {
        ColumnSet previewDataColumnOwner = anaColumnCompareViewer.getPreviewDataColumnOwner();
        if (previewDataColumnOwner == null) {
            return null;
        }
        List<TdColumn> columns = ColumnSetHelper.getColumns(previewDataColumnOwner);
        return columns.toArray(new TdColumn[columns.size()]);
    }

    @Override
    protected boolean isDataTableCompVisible() {
        if (anaColumnCompareViewer == null) {
            EList<ModelElement> analyzedColumns = analysisHandler.getAnalyzedColumns();
            return analyzedColumns != null && !analyzedColumns.isEmpty();
        } else {
            return anaColumnCompareViewer.getPreviewDataColumnOwner() != null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#createRunSampleDataButton(org.eclipse.swt.
     * widgets.Composite)
     */
    @Override
    protected void createRunSampleDataButton(Composite buttonComposite) {
        // here no need to implement anything override this method just to hide the checkbox.
        // anytime we want to reuse the checkbox just remove this override method
    }

}
