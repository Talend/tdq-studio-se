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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.composite.AnalysisColumnCompareTreeViewer;
import org.talend.dataprofiler.core.ui.editor.composite.DataFilterComp;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.columnset.ColumnsetFactory;
import org.talend.dataquality.indicators.columnset.RowMatchingIndicator;
import org.talend.dq.analysis.AnalysisBuilder;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * This page show the comparisons information of column set.
 */
public class RedundancyAnalysisDetailsPage extends AbstractAnalysisMetadataPage implements PropertyChangeListener {

    private static Logger log = Logger.getLogger(RedundancyAnalysisDetailsPage.class);

    private RowMatchingIndicator rowMatchingIndicatorA;

    private RowMatchingIndicator rowMatchingIndicatorB;

    private AnalysisColumnCompareTreeViewer anaColumnCompareViewer;

    private Section columnsComparisonSection = null;

    DataFilterComp dataFilterCompA;

    DataFilterComp dataFilterCompB;

    private String stringDataFilterA;

    private String stringDataFilterB;

    public RedundancyAnalysisDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    public Section getColumnsComparisonSection() {
        return this.columnsComparisonSection;
    }

    public void setColumnsComparisonSection(Section columnsComparisonSection) {
        this.columnsComparisonSection = columnsComparisonSection;
    }

    @Override
    public void initialize(FormEditor editor) {
        super.initialize(editor);
        // MOD xqliu 2009-06-10 bug7334
        stringDataFilterA = AnalysisHelper.getStringDataFilter(getCurrentModelElement(), 0);
        stringDataFilterB = AnalysisHelper.getStringDataFilter(getCurrentModelElement(), 1);
        // ~
        if (getCurrentModelElement().getResults().getIndicators().size() == 0) {
            ColumnsetFactory factory = ColumnsetFactory.eINSTANCE;
            rowMatchingIndicatorA = factory.createRowMatchingIndicator();
            rowMatchingIndicatorB = factory.createRowMatchingIndicator();
            Indicator[] currentIndicators = new Indicator[] { rowMatchingIndicatorA, rowMatchingIndicatorB };
            setDefaultIndDef(currentIndicators);
        } else {
            EList<Indicator> indicators = getCurrentModelElement().getResults().getIndicators();
            rowMatchingIndicatorA = (RowMatchingIndicator) indicators.get(0);
            rowMatchingIndicatorB = (RowMatchingIndicator) indicators.get(1);
        }
    }

    /**
     * DOC rli Comment method "setDefaultIndDef".
     * 
     * @param indicator
     */
    private void setDefaultIndDef(Indicator[] indicators) {
        for (int i = 0; i < indicators.length; i++) {
            if (!DefinitionHandler.getInstance().setDefaultIndicatorDefinition(indicators[i])) {
                log.error(DefaultMessagesImpl.getString("TableIndicator.couldnotSetDef") + indicators[i].getName());//$NON-NLS-1$
            }
        }
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.columnSetComparisionAnalysis")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.analysisMetadata")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.setAnalysisProperties")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        // MOD mzhao 2009-06-17 feature 5887.
        anaColumnCompareViewer = new AnalysisColumnCompareTreeViewer(this, topComp, getCurrentModelElement(), false);
        columnsComparisonSection = anaColumnCompareViewer.getColumnsComparisonSection();

        anaColumnCompareViewer.refreash();
        anaColumnCompareViewer.addPropertyChangeListener(this);

        createDataFilterSection(form, topComp);
        createAnalysisParamSection(form, topComp);
        createContextGroupSection(form, topComp);
    }

    @Override
    void createDataFilterSection(final ScrolledForm form, Composite anasisDataComp) {
        dataFilterSection = createSection(
                form,
                anasisDataComp,
                DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.dataFilter"), DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.editDataFilter")); //$NON-NLS-1$ //$NON-NLS-2$
        Composite sectionClient = toolkit.createComposite(dataFilterSection);
        sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
        sectionClient.setLayout(new GridLayout());

        SashForm sashForm = new SashForm(sectionClient, SWT.NULL);
        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite leftComp = toolkit.createComposite(sashForm);
        leftComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        leftComp.setLayout(new GridLayout());
        dataFilterCompA = new DataFilterComp(leftComp, stringDataFilterA);
        installProposals(dataFilterCompA.getDataFilterText());
        dataFilterCompA.addPropertyChangeListener(this);
        dataFilterCompA.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                AnalysisHelper.setStringDataFilter(getCurrentModelElement(), dataFilterCompA.getDataFilterString(), 0);
            }
        });

        Composite rightComp = toolkit.createComposite(sashForm);
        rightComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        rightComp.setLayout(new GridLayout());
        dataFilterCompB = new DataFilterComp(rightComp, stringDataFilterB);
        installProposals(dataFilterCompB.getDataFilterText());
        dataFilterCompB.addPropertyChangeListener(this);
        dataFilterCompB.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                AnalysisHelper.setStringDataFilter(getCurrentModelElement(), dataFilterCompB.getDataFilterString(), 1);
            }
        });

        // ADD yyi 2011-05-31 16158:add whitespace check for text fields.
        addWhitespaceValidate(dataFilterCompA.getDataFilterText(), dataFilterCompB.getDataFilterText());
        dataFilterSection.setClient(sectionClient);
    }

    /**
     * 
     * DOC mzhao Open column selection dialog for left column set. this method is intended to use from cheat sheets.
     */
    public void openColumnsSetASelectionDialog() {
        anaColumnCompareViewer.openColumnsSetASelectionDialog();
    }

    /**
     * 
     * DOC mzhao Open column selection dialog for right column set. this method is intended to use from cheat sheets.
     */
    public void openColumnsSetBSelectionDialog() {
        anaColumnCompareViewer.openColumnsSetBSelectionDialog();
    }

    public AnalysisHandler getAnalysisHandler() {
        AnalysisHandler analysisHandler = new AnalysisHandler();
        analysisHandler.setAnalysis(getCurrentModelElement());
        return analysisHandler;
    }

    @Override
    protected void saveAnalysis() throws DataprofilerCoreException {
        for (Domain domain : getCurrentModelElement().getParameters().getDataFilter()) {
            domain.setName(getCurrentModelElement().getName());
        }
        IRepositoryViewObject reposObject = null;
        getAnalysisHandler().clearAnalysis();
        List<ModelElement> analysedElements = new ArrayList<ModelElement>();
        anaColumnCompareViewer.setColumnABForMatchingIndicator(rowMatchingIndicatorA, anaColumnCompareViewer.getColumnListA(),
                anaColumnCompareViewer.getColumnListB());
        anaColumnCompareViewer.setColumnABForMatchingIndicator(rowMatchingIndicatorB, anaColumnCompareViewer.getColumnListB(),
                anaColumnCompareViewer.getColumnListA());
        Connection tdDataProvider = null;
        for (int i = 0; i < anaColumnCompareViewer.getColumnListA().size(); i++) {
            reposObject = anaColumnCompareViewer.getColumnListA().get(i).getObject();
            analysedElements.add(((MetadataColumnRepositoryObject) reposObject).getTdColumn());
        }
        for (int i = 0; i < anaColumnCompareViewer.getColumnListB().size(); i++) {
            reposObject = anaColumnCompareViewer.getColumnListB().get(i).getObject();
            analysedElements.add(((MetadataColumnRepositoryObject) reposObject).getTdColumn());
        }
        if (analysedElements.size() > 0) {
            tdDataProvider = ConnectionHelper.getTdDataProvider((TdColumn) analysedElements.get(0));
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
        AnalysisBuilder anaBuilder = new AnalysisBuilder();
        anaBuilder.setAnalysis(getCurrentModelElement());
        if (anaColumnCompareViewer.getCheckComputeButton().getSelection()) {
            getCurrentModelElement().getParameters().getDeactivatedIndicators().add(rowMatchingIndicatorB);
        } else {
            getCurrentModelElement().getParameters().getDeactivatedIndicators().clear();
        }
        anaBuilder.addElementsToAnalyze(analysedElements.toArray(new ModelElement[analysedElements.size()]), new Indicator[] {
                rowMatchingIndicatorA, rowMatchingIndicatorB });

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
        // ADD xqliu 2012-04-19 TDQ-5005
        anaColumnCompareViewer.setDirty(false);
        dataFilterCompA.setDirty(false);
        dataFilterCompB.setDirty(false);
    }

    @Override
    public void refreshGraphicsInSettingsPage() {
        // do nothing
    }

    @Override
    public ReturnCode canSave() {
        // MOD by gdbu 2011-3-21 bug 19179
        ReturnCode canModRetCode = super.canSave();
        if (!canModRetCode.isOk()) {
            return canModRetCode;
        }
        // ~19179

        if (anaColumnCompareViewer.getColumnListA().size() != anaColumnCompareViewer.getColumnListB().size()) {
            return new ReturnCode(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.columnsSameMessage"), false); //$NON-NLS-1$
        }

        List<TdColumn> columnAList = new ArrayList<TdColumn>();
        List<TdColumn> columnBList = new ArrayList<TdColumn>();
        for (RepositoryNode rd : anaColumnCompareViewer.getColumnListA()) {
            columnAList.add((TdColumn) ((MetadataColumnRepositoryObject) rd.getObject()).getTdColumn());
        }
        for (RepositoryNode rd : anaColumnCompareViewer.getColumnListB()) {
            columnBList.add((TdColumn) ((MetadataColumnRepositoryObject) rd.getObject()).getTdColumn());
        }
        if (anaColumnCompareViewer.getColumnListA().size() > 0) {
            if (!ColumnHelper.isFromSameTable(columnAList) || !ColumnHelper.isFromSameTable(columnBList)) {
                return new ReturnCode(
                        DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.notSameElementMessage"), false); //$NON-NLS-1$
            }

            for (int i = 0; i < anaColumnCompareViewer.getColumnListA().size(); i++) {
                TdColumn columnA = columnAList.get(i);
                TdColumn columnB = columnBList.get(i);

                ColumnSet ownerA = ColumnHelper.getColumnOwnerAsColumnSet(columnA);
                ColumnSet ownerB = ColumnHelper.getColumnOwnerAsColumnSet(columnB);

                int typeA = columnA.getSqlDataType().getJavaDataType();
                int typeB = columnB.getSqlDataType().getJavaDataType();
                if (!Java2SqlType.isGenericSameType(typeA, typeB)) {
                    return new ReturnCode(
                            DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.notSameColumnType"), false); //$NON-NLS-1$
                }
                if (!ColumnSetHelper.isFromSamePackage(ownerA, ownerB)) {
                    return new ReturnCode(
                            DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.schemaSameMessage"), false); //$NON-NLS-1$
                }
            }

            List<TdColumn> allColumns = new ArrayList<TdColumn>();
            allColumns.addAll(columnAList);
            allColumns.addAll(columnBList);

            // MOD scorreia 2009-05-25 allow to compare elements from the same
            // table
            // if (ColumnHelper.isFromSameTable(allColumns)) {
            //                return new ReturnCode(DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.TwoSideColumns"), false); //$NON-NLS-1$
            // }
        }

        return new ReturnCode(true);
    }

    @Override
    protected ReturnCode canRun() {
        return 0 == rowMatchingIndicatorA.getColumnSetA().size() ? new ReturnCode(
                DefaultMessagesImpl.getString("ColumnsComparisonMasterDetailsPage.columnsBlankRunMessage"), false) : new ReturnCode(true); //$NON-NLS-1$
    }

    @Override
    public ScrolledForm getScrolledForm() {
        return form;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) currentEditor).firePropertyChange(IEditorPart.PROP_DIRTY);
            ((AnalysisEditor) currentEditor).setRefreshResultPage(true);
        } else if (PluginConstant.DATAFILTER_PROPERTY.equals(evt.getPropertyName())) {
            this.setDirty(true);
        }
    }

    /**
     * ADD xqliu 2012-04-19 TDQ-5005.
     */
    @Override
    public boolean isDirty() {
        if (anaColumnCompareViewer == null ? false : anaColumnCompareViewer.isDirty()) {
            this.setDirty(anaColumnCompareViewer.isDirty());
        }
        if (dataFilterCompA == null ? false : dataFilterCompA.isDirty()) {
            this.setDirty(dataFilterCompA.isDirty());
        }
        if (dataFilterCompB == null ? false : dataFilterCompB.isDirty()) {
            this.setDirty(dataFilterCompB.isDirty());
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

}
