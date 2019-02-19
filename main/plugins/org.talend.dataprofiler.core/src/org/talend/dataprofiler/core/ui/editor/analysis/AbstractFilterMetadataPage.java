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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.DqRepositoryViewService;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.OverviewIndUIElement;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisType;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractFilterMetadataPage extends AbstractAnalysisMetadataPage {

    private Text tableFilterText;

    private Text viewFilterText;

    protected Connection tdDataProvider;

    private String latestTableFilterValue;

    private String latestViewFilterValue;

    private Button reloadDatabasesBtn = null;

    public Connection getTdDataProvider() {
        return this.tdDataProvider;
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.connectionAnalysis")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.analysisMeta")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.setAnalysisProp")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        createAnalysisParamSection();
        createContextGroupSection(form, topComp);
    }

    /**
     * create Analysis Param Section.
     * 
     */
    private void createAnalysisParamSection() {
        analysisParamSection = createSection(form, topComp,
                DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.analysisParameter")); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(analysisParamSection);
        sectionClient.setLayout(new GridLayout(1, false));

        Composite comp1 = new Composite(sectionClient, SWT.NONE);
        this.createAnalysisLimitComposite(comp1);

        Composite comp2 = new Composite(sectionClient, SWT.NONE);
        comp2.setLayout(new GridLayout(2, false));
        GridDataFactory.fillDefaults().grab(true, true).applyTo(comp2);
        Label tableFilterLabel = new Label(comp2, SWT.None);
        tableFilterLabel.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.filterOnTable")); //$NON-NLS-1$
        tableFilterLabel.setLayoutData(new GridData());
        tableFilterText = new Text(comp2, SWT.BORDER);
        EList<Domain> dataFilters = getCurrentModelElement().getParameters().getDataFilter();
        String tablePattern = DomainHelper.getTablePattern(dataFilters);
        latestTableFilterValue = tablePattern == null ? PluginConstant.EMPTY_STRING : tablePattern;
        tableFilterText.setText(latestTableFilterValue);
        tableFilterText.setToolTipText(DefaultMessagesImpl.getString("AbstractFilterMetadataPage.FilterTables")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().grab(true, false).applyTo(tableFilterText);
        // add shortcut
        installProposals(tableFilterText);
        tableFilterText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }

        });
        Label viewFilterLabel = new Label(comp2, SWT.None);
        viewFilterLabel.setText(DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.filterOnView")); //$NON-NLS-1$
        viewFilterLabel.setLayoutData(new GridData());
        viewFilterText = new Text(comp2, SWT.BORDER);
        String viewPattern = DomainHelper.getViewPattern(dataFilters);
        latestViewFilterValue = viewPattern == null ? PluginConstant.EMPTY_STRING : viewPattern;
        viewFilterText.setText(latestViewFilterValue);
        viewFilterText.setToolTipText(DefaultMessagesImpl.getString("AbstractFilterMetadataPage.FilterViews")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().grab(true, false).applyTo(viewFilterText);
        // add shortcut
        installProposals(viewFilterText);
        viewFilterText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }

        });

        // ADD yyi 2011-05-31 16158:add whitespace check for text fields.
        addWhitespaceValidate(tableFilterText, viewFilterText);
        // ADD xqliu 2010-01-04 bug 10190
        createReloadDatabasesButton(comp2);
        // ~
        analysisParamSection.setClient(sectionClient);
    }

    /**
     * DOC xqliu Comment method "createReloadDatabasesButton".
     * 
     * @param sectionClient
     */
    private void createReloadDatabasesButton(Composite sectionClient) {
        if (isConnectionAnalysis()) {
            reloadDatabasesBtn = new Button(sectionClient, SWT.CHECK);
            reloadDatabasesBtn.setText(DefaultMessagesImpl.getString("AbstractFilterMetadataPage.ReloadDatabases"));//$NON-NLS-1$
            if (DqRepositoryViewService.isComeFromRefrenceProject(this.getTdDataProvider())) {
                reloadDatabasesBtn.setEnabled(false);
                reloadDatabasesBtn.setSelection(false);
            } else {
                reloadDatabasesBtn.setSelection(AnalysisHelper.getReloadDatabases(getCurrentModelElement()));
            }
            reloadDatabasesBtn.addMouseListener(new MouseListener() {

                public void mouseDoubleClick(MouseEvent e) {
                    // do nothing here until now
                }

                public void mouseDown(MouseEvent e) {
                    setDirty(true);
                }

                public void mouseUp(MouseEvent e) {
                    // do nothing here until now
                }
            });
        }
    }

    /**
     * DOC xqliu Comment method "isConnectionAnalysis".
     * 
     * @return
     */
    private boolean isConnectionAnalysis() {
        if (getCurrentModelElement() != null) {
            return AnalysisType.CONNECTION.equals(AnalysisHelper.getAnalysisType(getCurrentModelElement()));
        }
        return false;
    }

    protected abstract void fillDataProvider();

    protected abstract List<Catalog> getCatalogs();

    protected abstract List<OverviewIndUIElement> getCatalogIndicators();

    protected abstract List<OverviewIndUIElement> getSchemaIndicators();

    /**
     * find all analyzed schemas
     * 
     * @param tdCatalogs
     * @return
     */
    protected int getSchamas(List<Catalog> tdCatalogs) {
        List<Schema> tdSchema = new ArrayList<Schema>();
        if (!tdCatalogs.isEmpty()) {
            for (Catalog catalog : tdCatalogs) {
                tdSchema.addAll(CatalogHelper.getSchemas(catalog));
            }
        }
        if (tdSchema.isEmpty()) {
            tdSchema = ConnectionHelper.getSchema(tdDataProvider);
        }
        return tdSchema.size();
    }

    @Override
    public void saveAnalysis() throws DataprofilerCoreException {
        // ADD xqliu 2010-01-04 bug 10190
        if (isConnectionAnalysis()) { // MOD zshen 2010-03-19 bug 12041
            AnalysisHelper.setReloadDatabases(getCurrentModelElement(), reloadDatabasesBtn.getSelection());
        }
        // ~

        EList<Domain> dataFilters = getCurrentModelElement().getParameters().getDataFilter();
        if (!this.tableFilterText.getText().equals(DomainHelper.getTablePattern(dataFilters))) {
            DomainHelper.setDataFilterTablePattern(dataFilters, tableFilterText.getText());
            latestTableFilterValue = this.tableFilterText.getText();
        }
        if (!this.viewFilterText.getText().equals(DomainHelper.getViewPattern(dataFilters))) {
            DomainHelper.setDataFilterViewPattern(dataFilters, viewFilterText.getText());
            latestViewFilterValue = this.viewFilterText.getText();
        }

        // save the number of connections per analysis
        this.saveNumberOfConnectionsPerAnalysis();

        // 2011.1.12 MOD by zhsne to unify anlysis and connection id when saving.
        this.nameText.setText(getCurrentModelElement().getName());
        // ~
        // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
        ReturnCode saved = ElementWriterFactory.getInstance().createAnalysisWrite()
                .save(getCurrentRepNode().getObject().getProperty().getItem(), true);
        // MOD yyi 2012-02-03 TDQ-3602:Avoid to rewriting all analyzes after saving, no reason to update all analyzes
        // which is depended in the referred connection.
        // Extract saving log function.
        // @see org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage#logSaved(ReturnCode)
        logSaved(saved);
    }

    @Override
    public void updateFieldWhichUseContext() {
        super.updateFieldWhichUseContext();
        Analysis analysis = getCurrentModelElement();
        JobContextManager contextManager = (JobContextManager) currentEditor.getContextManager();

        if (tableFilterText != null && ContextHelper.isContextVar(tableFilterText.getText())) {
            String changedValue = ContextHelper.getChangedValue(analysis.getContextType(), contextManager,
                    tableFilterText.getText());
            if (StringUtils.isNotBlank(changedValue)) {
                tableFilterText.setText(changedValue);
            }
        }

        if (viewFilterText != null && ContextHelper.isContextVar(viewFilterText.getText())) {
            String changedValue = ContextHelper.getChangedValue(analysis.getContextType(), contextManager,
                    viewFilterText.getText());
            if (StringUtils.isNotBlank(changedValue)) {
                viewFilterText.setText(changedValue);
            }
        }

    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((AnalysisEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }

    }

    public AbstractFilterMetadataPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    @Override
    protected ReturnCode canRun() {
        return new ReturnCode(true);
    }

    @Override
    public void refreshGraphicsInSettingsPage() {
        // do nothing here
    }

    @Override
    public AnalysisHandler getAnalysisHandler() {
        AnalysisHandler analysisHandler = new AnalysisHandler();
        analysisHandler.setAnalysis(getCurrentModelElement());
        return analysisHandler;
    }

}
