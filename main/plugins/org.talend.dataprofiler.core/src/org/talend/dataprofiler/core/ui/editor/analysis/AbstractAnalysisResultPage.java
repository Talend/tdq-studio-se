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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.IRuningStatusListener;
import org.talend.dataprofiler.core.ui.editor.AbstractFormPage;
import org.talend.dataprofiler.core.ui.editor.CommonFormEditor;
import org.talend.dq.analysis.AnalysisHandler;

/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractAnalysisResultPage extends AbstractFormPage implements IRuningStatusListener {

    /**
     * width hint for tables in data area. ADDED sgandon 15/03/2010 bug 11769 : setup the size of the table to avoid
     * crash and add consistency.
     */
    // private static final int TABLE_WIDTH_HINT = 1100;

    /**
     * size in rows of a small table (that has less than TABLE_MIN_ROW_LIMIT rows). ADDED sgandon 15/03/2010 bug 11769 :
     * setup the size of the table to avoid crash and add consistency.
     */
    private static final int SMALL_TABLE_NUM_ROWS = 10;

    /**
     * size in rows of a big table (that has more than TABLE_MIN_ROW_LIMIT rows). ADDED sgandon 15/03/2010 bug 11769 :
     * setup the size of the table to avoid crash and add consistency.
     */
    private static final int BIG_TABLE_NUM_ROWS = 30;

    /**
     * limit of row to create a small table. ADDED sgandon 15/03/2010 bug 11769 : setup the size of the table to avoid
     * crash and add consistency.
     */
    private static final int TABLE_MIN_ROW_LIMIT = 10;

    protected ScrolledForm form;

    protected Composite topComposite;

    protected Composite summaryComp;

    protected Section summarySection = null;

    public AbstractAnalysisResultPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        currentEditor = (CommonFormEditor) editor;
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        this.form = managedForm.getForm();
        if (this.form != null && !this.form.isDisposed()) {
            this.form.setText(DefaultMessagesImpl.getString("AbstractAnalysisResultPage.analysisResult")); //$NON-NLS-1$
            topComposite = form.getBody();
            topComposite.setLayout(new GridLayout());
            summaryComp = toolkit.createComposite(topComposite);
            summaryComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
            summaryComp.setLayout(new GridLayout());
            createSummarySection(form, summaryComp, getAnalysisHandler());
        }
    }

    protected abstract AnalysisHandler getAnalysisHandler();

    protected void createSummarySection(ScrolledForm form, Composite parent, AnalysisHandler analysisHandler) {
        summarySection = createSection(form, parent,
                DefaultMessagesImpl.getString("AbstractAnalysisResultPage.analysisSummary"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(summarySection);
        sectionClient.setLayout(new GridLayout(2, false));
        sectionClient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite databaseComp = toolkit.createComposite(sectionClient);
        databaseComp.setLayout(new GridLayout(2, false));
        GridData databaseCompData = new GridData(GridData.FILL_HORIZONTAL);
        databaseCompData.verticalAlignment = GridData.BEGINNING;
        databaseComp.setLayoutData(databaseCompData);

        toolkit.createLabel(databaseComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.connection")); //$NON-NLS-1$
        toolkit.createLabel(databaseComp, analysisHandler.getConnectionName());
        if (analysisHandler.isCatalogExisting()) {
            toolkit.createLabel(databaseComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.catalog")); //$NON-NLS-1$
            toolkit.createLabel(databaseComp, analysisHandler.getCatalogNames());
        }

        if (analysisHandler.isSchemaExisting()) {
            toolkit.createLabel(databaseComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.schema")); //$NON-NLS-1$
            toolkit.createLabel(databaseComp, analysisHandler.getSchemaNames());
        }

        toolkit.createLabel(databaseComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.table")); //$NON-NLS-1$
        toolkit.createLabel(databaseComp, analysisHandler.getTableNames());

        // bug 10541 fix by zshen,Change some character set to be proper to add view in the table anasys
        toolkit.createLabel(databaseComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.view"));//$NON-NLS-1$
        toolkit.createLabel(databaseComp, analysisHandler.getViewNames());

        Composite executionComp = toolkit.createComposite(sectionClient);
        executionComp.setLayout(new GridLayout(2, false));
        GridData executionCompData = new GridData(GridData.FILL_HORIZONTAL);
        executionCompData.verticalAlignment = GridData.BEGINNING;
        executionComp.setLayoutData(executionCompData);
        toolkit.createLabel(executionComp,
                DefaultMessagesImpl.getString("ConnectionMasterDetailsPage.createionDate", PluginConstant.EMPTY_STRING)); //$NON-NLS-1$
        toolkit.createLabel(executionComp, getFormatDateStr(analysisHandler.getAnalysis().getCreationDate()));
        toolkit.createLabel(executionComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.executionDate")); //$NON-NLS-1$
        toolkit.createLabel(executionComp, getFormatDateStr(analysisHandler.getAnalysis().getResults().getResultMetadata()
                .getExecutionDate()));
        toolkit.createLabel(executionComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.executionDuration")); //$NON-NLS-1$
        toolkit.createLabel(executionComp, analysisHandler.getExecuteDuration());
        toolkit.createLabel(executionComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.executionStatus")); //$NON-NLS-1$
        if (analysisHandler.getResultMetadata().isLastRunOk()) {
            toolkit.createLabel(executionComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.success")); //$NON-NLS-1$
        } else {
            // MOD msjian TDQ-5119 2012-12-24: the "execution status" should not display an error message when the
            // analysis is not executed yet
            String errMessage = PluginConstant.EMPTY_STRING;
            if (analysisHandler.getResultMetadata().getExecutionNumber() != 0) {
                errMessage = DefaultMessagesImpl.getString("AbstractAnalysisResultPage.failure") + analysisHandler.getResultMetadata().getMessage(); //$NON-NLS-1$
            }
            toolkit.createLabel(executionComp, errMessage).setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
            // TDQ-5119~
        }

        toolkit.createLabel(executionComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.numberOfExecution")); //$NON-NLS-1$
        toolkit.createLabel(executionComp, analysisHandler.getExecuteNumber());
        toolkit.createLabel(executionComp, DefaultMessagesImpl.getString("AbstractAnalysisResultPage.lastSucessfulExecution")); //$NON-NLS-1$
        toolkit.createLabel(executionComp, analysisHandler.getLastExecutionNumberOk());

        summarySection.setClient(sectionClient);
    }

    private String getFormatDateStr(Date date) {
        if (date == null) {
            return PluginConstant.EMPTY_STRING;
        }
        String format = SimpleDateFormat.getDateTimeInstance().format(date);
        return format;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.IRuningStatusListener#fireRuningItemChanged(boolean)
     */
    public void fireRuningItemChanged(boolean status) {
        fireRuningItemChanged(status, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.IRuningStatusListener#fireRuningItemChanged(boolean)
     */
    public void fireRuningItemChanged(boolean status, boolean isSupportDynamicChart) {
        ((AnalysisEditor) currentEditor).setRunActionButtonState(status);
        ((AnalysisEditor) currentEditor).setRefreshResultPage(isSupportDynamicChart);
        IEditorPart editor = CorePlugin.getDefault().getCurrentActiveEditor();
        if (editor instanceof AnalysisEditor && status) {
            refresh(((AnalysisEditor) editor).getMasterPage());
        }
    }

    public abstract void refresh(AbstractAnalysisMetadataPage masterPage);

    protected abstract void createResultSection(Composite parent);

    /**
     * setup the Grid Layout Data to limit the vertical size of the table according to the numOfRows. if (numOfRows <=
     * 10) then table will be 10 rows height, if (numOfRows > 10) then the table will be 30 rows heigth
     * 
     * @param table the table to set the GridData value on
     * @param numOfRows number of rows in the table ADDED sgandon 15/03/2010 bug 11769 : setup the size of the table to
     * avoid crash and add consistency.
     */
    protected void setupTableGridDataLimitedSize(Table table, int numOfRows) {
        int itemHeight = table.getItemHeight();
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.heightHint = numOfRows > TABLE_MIN_ROW_LIMIT ? BIG_TABLE_NUM_ROWS * itemHeight : SMALL_TABLE_NUM_ROWS * itemHeight;

        // MOD yyi compute table width
        // int tableWidth = 0;
        // for (TableColumn column : table.getColumns()) {
        // tableWidth += column.getWidth() + 50;
        // }
        // data.widthHint = TABLE_WIDTH_HINT > tableWidth ? tableWidth : TABLE_WIDTH_HINT;
        table.setLayoutData(data);
    }

}
