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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.dependencies.DependenciesHandler;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.IRuningStatusListener;
import org.talend.dataprofiler.core.ui.dialog.ColumnsSelectWithConstraintDialog;
import org.talend.dataprofiler.core.ui.dialog.ColumnsSelectionDialog;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;
import org.talend.dataprofiler.core.ui.editor.composite.AbstractColumnDropTree;
import org.talend.dataprofiler.core.ui.editor.composite.DataFilterComp;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.EventReceiver;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.wizard.analysis.connection.ConnectionWizard;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.AnalysisParameters;
import org.talend.dataquality.analysis.ExecutionLanguage;
import org.talend.dataquality.analysis.SampleDataShowWay;
import org.talend.dataquality.exception.DataprofilerCoreException;
import org.talend.dataquality.helpers.AnalysisHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dataquality.record.linkage.ui.composite.table.ColumnAnalysisDataSamTable;
import org.talend.dataquality.rules.DQRule;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.analysis.connpool.TdqAnalysisConnectionPool;
import org.talend.dq.helper.ContextHelper;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.nodes.AnalysisRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DFConnectionRepNode;
import org.talend.dq.nodes.ReportAnalysisRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.Dependency;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractAnalysisMetadataPage extends AbstractMetadataFormPage implements IRuningStatusListener,
        PropertyChangeListener {

    private static Logger log = Logger.getLogger(AbstractAnalysisMetadataPage.class);

    protected IRepositoryNode analysisRepNode;

    protected Section analysisParamSection;

    protected DataFilterComp dataFilterComp;

    protected Section dataFilterSection = null;

    protected String stringDataFilter;

    // Used for Execute Engine section
    protected CCombo execCombo = null;

    protected String execLang = null;

    protected Button drillDownCheck;

    protected Text maxNumText;

    // ~Execute Engine section

    // MOD yyin 201204 TDQ-4977, change to TableCombo type to show the connection type.
    protected TableCombo connCombo;

    protected Text textConnVersion;

    protected Label labelConnDeleted;

    // Added 20140411 TDQ-8360 yyin
    private EventReceiver refreshDataProvider = null;

    // Added datapreview section part TDQ-11606 msjian
    protected Section dataPreviewSection = null;

    protected Composite dataTableComp = null;

    protected ColumnAnalysisDataSamTable sampleTable = null;

    protected Composite warningComposite = null;

    protected Text rowLoadedText = null;

    protected CCombo sampleDataShowWayCombo;

    protected static final int PREVIEW_MAX_ROW_COUNT = 999;

    protected static final int PREVIEW_SUGGEST_ROW_COUNT = 500;

    protected ModelElementIndicator[] currentModelElementIndicators;

    protected EventReceiver afterCreateConnectionReceiver = null;

    protected Boolean isRunWithSampleData = TaggedValueHelper.getValueBoolean(TaggedValueHelper.IS_USE_SAMPLE_DATA,
            getCurrentModelElement());

    protected Button runWithSampleBtn = null;

    /**
     * the temp value used to store the old connection value, when the user didn't save this page, use to revert
     */
    protected DataManager oldConn = null;

    public AbstractAnalysisMetadataPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    public ModelElementIndicator[] getCurrentModelElementIndicators() {
        return this.currentModelElementIndicators;
    }

    public void setCurrentModelElementIndicators(ModelElementIndicator[] modelElementIndicator) {
        this.currentModelElementIndicators = modelElementIndicator;
    }

    protected IRepositoryNode getCurrentConnectionRepNode() {
        // MOD klliu 2010-12-10
        IEditorInput editorInput = getEditor().getEditorInput();
        if (editorInput instanceof AnalysisItemEditorInput) {
            AnalysisItemEditorInput fileEditorInput = (AnalysisItemEditorInput) editorInput;
            return fileEditorInput.getConnectionNode();
        } else {
            // ADD TDQ-9613 msjian: when the user do something from the other views for example: from task view
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            Analysis findAnalysis = AnaResourceFileHelper.getInstance().findAnalysis(fileEditorInput.getFile());
            DataManager connection = findAnalysis.getContext().getConnection();
            if (connection != null) {
                return RepositoryNodeHelper.recursiveFind(connection);
            }
            // TDQ-9613~
        }
        return null;
    }

    public TableCombo getConnCombo() {
        return connCombo;
    }

    /**
     * get Connection Combo Selected Node.
     * 
     * @return RepositoryNode
     */
    public RepositoryNode getConnComboSelectNode() {
        Object data = getConnCombo().getData(String.valueOf(getConnCombo().getSelectionIndex()));
        if (data != null) {
            if (data instanceof RepositoryNode) {
                return (RepositoryNode) data;
            }
        }
        return null;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        ReturnCode rc = canSave();
        if (!rc.isOk()) {
            // MOD yyi 2012-02-29 TDQ-3605 Pop an error if rc is not ok.
            MessageDialogWithToggle.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    DefaultMessagesImpl.getString("AbstractAnalysisMetadataPage.SaveAnalysis"), rc.getMessage()); //$NON-NLS-1$
            return;
        } else if (!checkWhithspace()) {
            MessageDialogWithToggle
                    .openError(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            DefaultMessagesImpl.getString("AbstractAnalysisMetadataPage.SaveAnalysis"), DefaultMessagesImpl.getString("AbstractMetadataFormPage.whitespace")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            super.doSave(monitor);
            try {
                // SaveContext
                saveContext();

                saveAnalysis();
                this.isDirty = false;
                // MOD qiongli bug 0012766,2010-5-31:After change to another connection
                // which has same columns with before,the editor should not
                // dirty.
                ((AnalysisEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
                this.updateAnalysisConnectionVersionInfo();
            } catch (DataprofilerCoreException e) {
                MessageDialogWithToggle.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                        DefaultMessagesImpl.getString("AbstractAnalysisMetadataPage.SaveAnalysis"), e.getMessage()); //$NON-NLS-1$ 
                ExceptionHandler.process(e, Level.ERROR);
            }
        }
    }

    public ScrolledForm getScrolledForm() {
        return null;
    }

    protected abstract ReturnCode canRun();

    public abstract void refreshGraphicsInSettingsPage();

    protected abstract void saveAnalysis() throws DataprofilerCoreException;

    @Override
    public void setDirty(boolean isDirty) {
        if (this.isDirty != isDirty) {
            this.isDirty = isDirty;
            ((AnalysisEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    public void fireRuningItemChanged(boolean status) {
        fireRuningItemChanged(status, status);
    }

    public void fireRuningItemChanged(boolean status, boolean isSupportDynamicChart) {
        ((AnalysisEditor) currentEditor).setRunActionButtonState(status);
        ((AnalysisEditor) currentEditor).setRefreshResultPage(isSupportDynamicChart);
        // switch to result page at the beginning of running analysis, the status is false at the time
        if (!status) {
            switchToResultPage();
        }
    }

    protected void switchToResultPage() {
        IFormPage resultPage = currentEditor.findPage(AnalysisEditor.RESULT_PAGE);
        if (resultPage != null && !resultPage.isActive()) {
            IFormPage activePageInstance = currentEditor.getActivePageInstance();
            if (activePageInstance.canLeaveThePage()) {
                currentEditor.setActivePage(AnalysisEditor.RESULT_PAGE);
            }
        }
    }

    /**
     * MOD mzhao 2009-06-17 feature 5887.
     * 
     * @param parentComp
     */
    public void createConnBindWidget(Composite parentComp) {
        // ~ MOD mzhao 2009-05-05,Bug 6587.
        Composite labelButtonClient = toolkit.createComposite(parentComp, SWT.NONE);
        GridLayout labelButtonClientLayout = new GridLayout();
        labelButtonClientLayout.numColumns = 4;
        labelButtonClient.setLayout(labelButtonClientLayout);

        toolkit.createLabel(labelButtonClient, DefaultMessagesImpl.getString("AbstractMetadataFormPage.connBind")); //$NON-NLS-1$

        // MOD yyin 201204 TDQ-4977, change to TableCombo type to show the connection type.
        // create TableCombo
        connCombo = new TableCombo(labelButtonClient, SWT.BORDER | SWT.READ_ONLY);
        connCombo.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));

        // tell the TableCombo that I want 2 blank columns auto sized.
        connCombo.defineColumns(2);

        // set which column will be used for the selected item.
        connCombo.setDisplayColumnIndex(0);
        connCombo.setSize(SWT.DEFAULT, SWT.DEFAULT);

        // add listener
        // connCombo = new TableCombo(labelButtonClient, SWT.BORDER);
        connCombo.setEditable(false);
        connCombo.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                updateAnalysisConnectionVersionInfo();
                setSampleDataShowWayStatus();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        // ADD msjian TDQ-5184 2012-4-8: set the connCombo background color as system set color
        connCombo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        // TDQ-5184~
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(labelButtonClient);

        // register: refresh the dataprovider combobox when the name of the data provider is changed.
        refreshDataProvider = new EventReceiver() {

            @Override
            public boolean handle(Object data) {
                reloadDataproviderAndFillConnCombo();
                // TDQ-9345,avoid to get an old column RepositoryNode when click "selecet columns..."
                updateAnalysisTree();
                return true;
            }
        };
        EventManager.getInstance().register(getCurrentModelElement(), EventEnum.DQ_ANALYSIS_REFRESH_DATAPROVIDER_LIST,
                refreshDataProvider);

        connCombo.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                EventManager.getInstance().clearEvent(getCurrentModelElement(), EventEnum.DQ_ANALYSIS_REFRESH_DATAPROVIDER_LIST);
            }
        });

        createConnVersionText(labelButtonClient);
        createConnDeletedLabel(labelButtonClient);
        reloadDataproviderAndFillConnCombo();
        // ~
    }

    /**
     * create the version Text of the connection.
     * 
     * @param parentComp
     */
    private void createConnVersionText(Composite parentComp) {
        textConnVersion = toolkit.createText(parentComp, PluginConstant.EMPTY_STRING, SWT.FLAT);
        textConnVersion.setEditable(false);
        updateAnalysisConnectionVersionInfo();
    }

    /**
     * update the version info of the analysis.
     */
    public void updateAnalysisConnectionVersionInfo() {
        if (this.textConnVersion != null) {
            String strConnVersion = DefaultMessagesImpl.getString("AbstractMetadataFormPage.connVersion") //$NON-NLS-1$
                    + getConnectionVersion();
            textConnVersion.setText(strConnVersion);
        }
    }

    /**
     * get the database's version of the Analysis.
     * 
     * @return
     */
    public String getConnectionVersion() {
        String version = null;
        if (getCurrentModelElement() != null) {
            DataManager dm = getCurrentModelElement().getContext().getConnection();
            if (dm != null) {
                if (dm instanceof Connection) {
                    Connection connection = (Connection) dm;
                    version = connection.getVersion();
                    if (version == null) {
                        version = initConnectionVersion(connection);
                    }
                }
            }
        }
        return version == null ? getConnectionVersionDefault() : version;
    }

    /**
     * get the default connection's version of this analysis.
     * 
     * @returnd efault connection's version
     */
    private String getConnectionVersionDefault() {
        String version = DefaultMessagesImpl.getString("AbstractAnalysisMetadataPage.Unknown"); //$NON-NLS-1$
        Object data = getConnComboSelectNode();
        if (data != null) {
            if (data instanceof DBConnectionRepNode) {
                DBConnectionRepNode dbConnRepNode = (DBConnectionRepNode) data;
                if (dbConnRepNode.getObject() != null && dbConnRepNode.getObject().getProperty() != null) {
                    version = dbConnRepNode.getObject().getProperty().getVersion();
                }
            } else if (data instanceof DFConnectionRepNode) {
                DFConnectionRepNode dfConnRepNode = (DFConnectionRepNode) data;
                if (dfConnRepNode.getObject() != null && dfConnRepNode.getObject().getProperty() != null) {
                    version = dfConnRepNode.getObject().getProperty().getVersion();
                }
            }
        }
        return version;
    }

    /**
     * init the version of the Connection accroding to the file name.
     * 
     * @param connection
     * @return
     */
    private String initConnectionVersion(Connection connection) {
        String version = DefaultMessagesImpl.getString("AbstractAnalysisMetadataPage.Version"); //$NON-NLS-1$
        Resource eResource = connection.eResource();
        if (eResource != null) {
            URI uri = eResource.getURI();
            if (uri != null) {
                String fileName = uri.toString().toLowerCase();
                String[] splits = fileName.split("_"); //$NON-NLS-1$
                if (splits.length > 0) {
                    String str = splits[splits.length - 1];
                    int indexOf = str.indexOf("." + FactoriesUtil.ITEM_EXTENSION); //$NON-NLS-1$
                    version = str.substring(0, indexOf);
                }
            }
        }
        return version;
    }

    /**
     * 
     * This method will make connection elem become proxy, look out for use it.
     */
    public void reloadDataproviderAndFillConnCombo() {
        List<IRepositoryNode> connsWithoutDeletion = getConnectionsWithoutDeleted();

        if (connsWithoutDeletion.size() == 0 && !RepositoryNodeHelper.isOpenDQCommonViewer()) {
            return;
        }

        connCombo.getTable().removeAll();
        fillComb(connsWithoutDeletion);

        DataManager connection = getCurrentModelElement().getContext().getConnection();
        if (connection == null) {
            // MOD TDQ-10654 msjian : when there is no connection selected, select 0,else still use the current one
            if (StringUtils.isBlank(connCombo.getText())) {
                connCombo.select(0);
            } else {
                int connIdx = findPositionOfCurrentConnection(connsWithoutDeletion, connCombo);
                connCombo.select(connIdx);
            }
            // TDQ-10654~
        } else {
            // Find the conn index first
            int connIdx = findPositionOfCurrentConnection(connsWithoutDeletion, connection);
            if (connIdx == -1) {
                IRepositoryNode currentConnectionNode = getCurrentConnectionRepNode();
                // The current connection is logical deleted!
                int deleteIndex = connCombo.getItemCount();
                if (currentConnectionNode != null) {
                    addItemToCombo(currentConnectionNode, deleteIndex);
                } else {
                    getLabelConnDeleted().setVisible(true);
                    getLabelConnDeleted().setText(
                            DefaultMessagesImpl.getString(
                                    "AbstractPagePart.ChangeConnectionError1", EObjectHelper.getURI(connection).path()));//$NON-NLS-1$
                }
                connCombo.select(deleteIndex);
            } else {
                connCombo.select(connIdx);
            }
        }

        setSampleDataShowWayStatus();
    }

    /**
     * DOC msjian Comment method "getConnectionsWithoutDeleted".
     * 
     * @return
     */
    protected List<IRepositoryNode> getConnectionsWithoutDeleted() {
        return RepositoryNodeHelper.getConnectionRepositoryNodes(false, true);
    }

    /**
     * find the Position by the connection.
     * 
     * @param connsWithoutDeletion
     * @param connection
     * @return
     */
    private int findPositionOfCurrentConnection(List<IRepositoryNode> connsWithoutDeletion, DataManager connection) {
        int index = 0;
        for (IRepositoryNode repNode : connsWithoutDeletion) {
            if (StringUtils.equals(repNode.getObject().getLabel(), connection.getName())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * find the Position by the connCombo.
     * 
     * @param connsWithoutDeletion
     * @param connCombo
     * @return
     */
    private int findPositionOfCurrentConnection(List<IRepositoryNode> connsWithoutDeletion, TableCombo connCombo) {
        int index = 0;
        for (IRepositoryNode repNode : connsWithoutDeletion) {
            String displayName = RepositoryNodeHelper.getAnalysisConComboDisplayName(repNode);
            if (StringUtils.equals(displayName, connCombo.getText())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * add a connection node into the combobox.
     * 
     * @param repNode
     * @param index
     */
    protected void addItemToCombo(IRepositoryNode repNode, int index) {
        TableItem ti = new TableItem(connCombo.getTable(), SWT.NONE);
        String displayName = RepositoryNodeHelper.getAnalysisConComboDisplayName(repNode);
        String connectionType = RepositoryNodeHelper.getConnectionType(repNode);
        ti.setText(new String[] { displayName, connectionType });
        connCombo.setData(displayName + connectionType, index);
        connCombo.setData(index + PluginConstant.EMPTY_STRING, repNode);
    }

    /**
     * fill the combobox of connections
     * 
     * @param connsWithoutDeletion
     */
    private void fillComb(List<IRepositoryNode> connsWithoutDeletion) {
        int index = 0;
        for (IRepositoryNode repNode : connsWithoutDeletion) {
            addItemToCombo(repNode, index);
            index++;
        }
    }

    /**
     * check if the connection repNode is supported.
     * 
     * @param repNode
     * @return boolean true:support
     */
    protected boolean isConnectionSupport(IRepositoryNode repNode) {
        return true;
    }

    /**
     * ADD gdbu 2011-6-1 bug : 19833
     * 
     * DOC gdbu Comment method "updateDQRuleDependency".
     * 
     * @param dqRules
     */
    protected void updateDQRuleDependency(List<DQRule> dqRules) {
        for (DQRule dqRule : dqRules) {
            List<Dependency> realSupplierDependency = new ArrayList<Dependency>();
            EList<Dependency> supplierDependency = dqRule.getSupplierDependency();
            for (Dependency dependency : supplierDependency) {
                EList<ModelElement> client = dependency.getClient();
                for (ModelElement modelElement : client) {
                    if (modelElement instanceof Analysis) {
                        List<DQRule> dqRules2 = getDqRules((Analysis) modelElement);
                        if (dqRules2.contains(dqRule)) {
                            realSupplierDependency.add(dependency);
                        }
                    }
                }
            }
            supplierDependency.clear();
            supplierDependency.addAll(realSupplierDependency);
        }
    }

    /**
     * ADD gdbu 2011-6-1 bug : 19833
     * 
     * DOC gdbu Comment method "getDqRules". Get all DQRule from analysis.
     * 
     * @param analysis
     * @return
     */
    public List<DQRule> getDqRules(Analysis analysis) {
        List<DQRule> result = new ArrayList<DQRule>();
        EList<Indicator> indicators = analysis.getResults().getIndicators();
        for (Indicator indicator : indicators) {
            IndicatorDefinition indicatorDefinition = indicator.getIndicatorDefinition();
            if (indicatorDefinition instanceof DQRule) {
                result.add((DQRule) indicatorDefinition);
            }
        }
        return result;
    }

    /**
     * DOC bZhou Comment method "getTreeViewer".
     * 
     * @return
     */
    public AbstractColumnDropTree getTreeViewer() {
        return null;
    }

    /**
     * create a label to indicate this connection is logical deleted.
     * 
     * @param parentComp
     */
    private void createConnDeletedLabel(Composite parentComp) {
        this.labelConnDeleted = toolkit.createLabel(parentComp, PluginConstant.EMPTY_STRING, SWT.NONE);
        labelConnDeleted.setVisible(false);
        labelConnDeleted.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
    }

    public Label getLabelConnDeleted() {
        return this.labelConnDeleted;
    }

    public ExecutionLanguage getUIExecuteEngin() {
        return ExecutionLanguage.SQL;
    }

    /**
     * Log on debug enable.
     * 
     * @param logger
     * @param level
     * @param message
     */
    protected void doLog(Logger logger, Level level, String message) {
        logger.log(level, message);
    }

    /**
     * log when analysis saved
     * 
     * @param saved
     * @throws DataprofilerCoreException
     */
    protected void logSaved(ReturnCode saved) throws DataprofilerCoreException {
        Analysis analysis = getCurrentModelElement();
        String urlString = analysis.eResource() != null ? (analysis.eResource().getURI().isFile() ? analysis.eResource().getURI()
                .toFileString() : analysis.eResource().getURI().toString()) : PluginConstant.EMPTY_STRING;
        if (!saved.isOk()) {
            throw new DataprofilerCoreException(DefaultMessagesImpl.getString(
                    "ColumnMasterDetailsPage.problem", analysis.getName(), urlString, saved.getMessage())); //$NON-NLS-1$

        } else if (log.isDebugEnabled()) {
            // MOD yyi 2012-02-06 TDQ-4581:avoid the instantiation of the strings to optimize the performances.
            doLog(log, Level.INFO, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.success", urlString)); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#canSave()
     */
    @Override
    public ReturnCode canSave() {
        return canModifyName(ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT);
    }

    /**
     * 
     * 
     * @param TDQAnalysisItem
     * @return whether it has been deleted
     * 
     * delete the dependency between analysis and connection
     */
    protected boolean deleteConnectionDependency(TDQAnalysisItem anaItem) {
        return DependenciesHandler.getInstance().removeConnDependencyAndSave(anaItem);
    }

    /**
     * DOC xqliu Comment method "createAnalysisLimitComposite".
     * 
     * @param pComp
     * @return
     */
    protected Composite createAnalysisLimitComposite(Composite pComp) {
        Composite comp = pComp;
        comp.setLayout(new GridLayout(2, false));
        this.toolkit.createLabel(comp,
                DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.NumberOfConnectionsPerAnalysis")); //$NON-NLS-1$

        this.numberOfConnectionsPerAnalysisText = this.toolkit.createText(comp,
                AnalysisHandler.createHandler(getCurrentModelElement()).getNumberOfConnectionsPerAnalysisWithContext(),
                SWT.BORDER);
        GridDataFactory.fillDefaults().grab(false, true).applyTo(this.numberOfConnectionsPerAnalysisText);
        ((GridData) this.numberOfConnectionsPerAnalysisText.getLayoutData()).widthHint = 240;
        installProposals(numberOfConnectionsPerAnalysisText);

        this.numberOfConnectionsPerAnalysisText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }

        });

        this.numberOfConnectionsPerAnalysisText.addVerifyListener(new VerifyListener() {

            public void verifyText(VerifyEvent e) {
                String inputValue = e.text;
                // if it is context variable, do not check
                if (!ContextHelper.isContextVar(inputValue)) {
                    Pattern pattern = Pattern.compile("^[0-9]"); //$NON-NLS-1$
                    char[] charArray = inputValue.toCharArray();
                    for (char c : charArray) {
                        if (!pattern.matcher(String.valueOf(c)).matches()) {
                            e.doit = false;
                        }
                    }
                }
            }
        });

        return comp;
    }

    /**
     * DOC xqliu Comment method "saveNumberOfConnectionsPerAnalysis".
     * 
     * @throws DataprofilerCoreException
     */
    protected void saveNumberOfConnectionsPerAnalysis() throws DataprofilerCoreException {
        String numberStr = numberOfConnectionsPerAnalysisText.getText().trim();
        // check whether the field is valid(not blank and not 0).
        if (StringUtils.isBlank(numberStr)
                || Integer.parseInt(ContextHelper.getAnalysisContextValue(getCurrentModelElement(), numberStr)) == 0) {
            throw new DataprofilerCoreException(DefaultMessagesImpl.getString(
                    "AbstractAnalysisMetadataPage.connectionPerAnaError", //$NON-NLS-1$
                    DefaultMessagesImpl.getString("AnalysisTuningPreferencePage.NumberOfConnectionsPerAnalysis"))); //$NON-NLS-1$

        }
        TaggedValueHelper.setTaggedValue(getCurrentModelElement(), TdqAnalysisConnectionPool.NUMBER_OF_CONNECTIONS_PER_ANALYSIS,
                this.numberOfConnectionsPerAnalysisText.getText());
    }

    /**
     * set ScrolledForm.
     * 
     * @param form
     */
    public void setForm(ScrolledForm form) {
        super.form = form;
    }

    /**
     * get ScrolledForm.
     * 
     * @return
     */
    public ScrolledForm getForm() {
        return form;
    }

    /**
     * get ChartComposite.
     * 
     * @return
     */
    public Composite getChartComposite() {
        return null;
    }

    /**
     * create Analysis Param Section.
     * 
     * @param pForm
     * @param pComp
     */
    protected void createAnalysisParamSection(final ScrolledForm pForm, Composite pComp) {
        analysisParamSection = createSection(pForm, pComp,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.AnalysisParameter"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(analysisParamSection);
        createAnalysisLimitComposite(sectionClient);
        analysisParamSection.setClient(sectionClient);
    }

    /**
     * Extracted from the column and column set master page, to create the execution language selection section
     * 
     * @param form1
     * @param anasisDataComp
     * @param analyzedColumns
     * @param anaParameters
     * @return
     */
    protected Composite createExecuteEngineSection(final ScrolledForm form1, Composite anasisDataComp,
            EList<ModelElement> analyzedColumns, AnalysisParameters anaParameters) {
        analysisParamSection = createSection(form1, anasisDataComp,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.AnalysisParameter"), null); //$NON-NLS-1$
        Composite sectionClient = toolkit.createComposite(analysisParamSection);
        sectionClient.setLayout(new GridLayout(1, false));

        Composite comp1 = new Composite(sectionClient, SWT.NONE);
        this.createAnalysisLimitComposite(comp1);

        Composite comp2 = new Composite(sectionClient, SWT.NONE);
        comp2.setLayout(new GridLayout(2, false));
        GridDataFactory.fillDefaults().grab(true, true).applyTo(comp2);
        toolkit.createLabel(comp2, DefaultMessagesImpl.getString("ColumnMasterDetailsPage.ExecutionEngine")); //$NON-NLS-1$
        // MOD zshen:need to use the component with finish indicator Selection.
        execCombo = new CCombo(comp2, SWT.BORDER);
        // ~
        execCombo.setEditable(false);
        // run with sample data mode only used by java engin
        if (this.isRunWithSampleData) {
            execCombo.setEnabled(false);
        }
        for (ExecutionLanguage language : ExecutionLanguage.VALUES) {
            String temp = language.getLiteral();
            execCombo.add(temp);
        }
        // MOD qiongli 2011-3-17 set DataFilterText disabled except TdColumn.
        if (analyzedColumns != null && !analyzedColumns.isEmpty()) {
            ModelElement mod = analyzedColumns.get(0);
            TdColumn tdColumn = SwitchHelpers.COLUMN_SWITCH.doSwitch(mod);
            dataFilterComp.getDataFilterText().setEnabled((tdColumn != null) ? true : false);
            if (tdColumn == null) {
                dataFilterComp.getDataFilterText().setEnabled(false);
                changeExecuteLanguageToJava(true);
            }
        }

        ExecutionLanguage executionLanguage = getCurrentModelElement().getParameters().getExecutionLanguage();

        execLang = executionLanguage.getLiteral();
        execCombo.setText(execLang);
        // ADD xqliu 2009-08-24 bug 8776
        setLanguageToTreeViewer(ExecutionLanguage.get(execLang));
        // ~

        // MOD msjian TDQ-9467: this part is only for column analysis
        createDrillDownPart(anaParameters, comp2, executionLanguage);

        analysisParamSection.setClient(sectionClient);

        return comp2;
    }

    /**
     * DOC msjian Comment method "createDrillDownPart".
     * 
     * @param anaParameters
     * @param comp2
     * @param executionLanguage
     */
    protected void createDrillDownPart(AnalysisParameters anaParameters, Composite comp2, ExecutionLanguage executionLanguage) {
        // do nothing here, only ColumnMasterDetailsPage need to overwrite this
    }

    /**
     * change ExecutionLanuage to Java.
     */
    public void changeExecuteLanguageToJava(boolean isDisabled) {
        if (this.execCombo == null) {
            return;
        }
        if (currentModelIsSqlEngine()) {

            int i = 0;
            for (ExecutionLanguage language : ExecutionLanguage.VALUES) {
                if (language.compareTo(ExecutionLanguage.JAVA) == 0 && this.execCombo.getSelectionIndex() != i) {
                    this.execCombo.select(i);
                    refreshEnginSection();
                } else {
                    i++;
                }
            }
        }
        if (isDisabled) {
            execCombo.setEnabled(false);
        }
    }

    /**
     * DOC zshen Comment method "refreshEnginSection".
     * 
     * @param javaEnginSection
     * @param currentLanguage
     */
    protected void refreshEnginSection() {
    }

    public void enableExecuteLanguage() {
        // TDQ-12766: when "Run with sample data" checked, change "Execution engine" nothing
        if (runWithSampleBtn != null && runWithSampleBtn.getSelection()) {
            // can not set enable
        } else {
            execCombo.setEnabled(true);
        }
    }

    protected boolean includeJavaEngineIndicator() {
        // only needed in column and column set master page
        return false;
    }

    /**
     * set the execute Language To the related TreeViewer in the child class.
     */
    protected void setLanguageToTreeViewer(ExecutionLanguage executionLanguage) {
        // no need to implement there
    }

    // ADD yyin 20131204 TDQ-8413, get the current selected value to judge, no need to use the related parameter in the
    // analysis.
    public String getCurrentExecuteLanguage() {
        return this.execLang;
    }

    /**
     * DOC talend Comment method "createDataPreviewSection".
     * 
     * @param form
     * @param topComp
     */
    protected void createDataPreviewSection(ScrolledForm form1, Composite anasisDataComp, boolean hasSelectColumnsButton,
            boolean hasSelectIndicatorButton, boolean hasAfterCreateConnectionReceiver) {
        dataPreviewSection = createSection(form1, anasisDataComp,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.dataPreview"), null); //$NON-NLS-1$
        Composite dataPreviewTableCom = toolkit.createComposite(dataPreviewSection, SWT.NONE);
        dataPreviewTableCom.setLayout(new GridLayout(1, true));
        createConnBindWidget(dataPreviewTableCom);
        Composite buttonComposite = toolkit.createComposite(dataPreviewTableCom, SWT.NONE);
        buttonComposite.setLayout(new GridLayout(6, false));
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(buttonComposite);
        createConnectionButton(buttonComposite, dataPreviewSection);
        if (hasSelectColumnsButton) {
            createColumnSelectButton(buttonComposite);
        }
        if (hasSelectIndicatorButton) {
            createIndicatorSelectButton(buttonComposite);
        }
        createRefreshDataButtonComp(buttonComposite);
        createRunButton(buttonComposite);
        createRunSampleDataButton(buttonComposite);
        // create the data table
        createDataTableComposite(dataPreviewTableCom);
        dataPreviewSection.setClient(dataPreviewTableCom);
        if (hasAfterCreateConnectionReceiver) {
            registerEvents();
        }
    }

    /**
     * DOC zshen Comment method "createRunSampleDataButton".
     * 
     * @param buttonComposite
     */
    protected void createRunSampleDataButton(Composite buttonComposite) {
        runWithSampleBtn = toolkit.createButton(buttonComposite,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.RunSamepData"), SWT.CHECK); //$NON-NLS-1$
        runWithSampleBtn.setToolTipText(DefaultMessagesImpl.getString("ColumnAnalysisDetailsPage.runWithSampleDataTooltip")); //$NON-NLS-1$
        // init the button
        runWithSampleBtn.setSelection(isRunWithSampleData);

        runWithSampleBtn.addSelectionListener(new SelectionAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                Boolean isSqlSelected = TaggedValueHelper.getValueBoolean(TaggedValueHelper.IS_SQL_ENGIN_BEFORE_CHECK,
                        getCurrentModelElement());
                isRunWithSampleData = ((Button) e.getSource()).getSelection();
                if (isRunWithSampleData && checkSqlEngineIndicatorExist()) {
                    MessageUI.openWarning(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.SqlIndicatorExistWarning")); //$NON-NLS-1$
                    runWithSampleBtn.setSelection(!isRunWithSampleData);
                    isRunWithSampleData = false;
                    return;
                } else if (!isRunWithSampleData && isSqlSelected && includeJavaEngineIndicator()) {
                    MessageUI.openWarning(getNonJavaIndicatorMessage());
                    runWithSampleBtn.setSelection(!isRunWithSampleData);
                    isRunWithSampleData = true;
                    return;
                }
                doCheckOption();
                AbstractAnalysisMetadataPage.this.setDirty(true);
            }
        });
    }

    /**
     * Get message why the status of checkbox can not be changed
     * 
     * @return
     */
    protected String getNonJavaIndicatorMessage() {
        return DefaultMessagesImpl.getString("ColumnMasterDetailsPage.JavaIndicatorExistWarning"); //$NON-NLS-1$
    }

    protected boolean checkSqlEngineIndicatorExist() {
        return false;
    }

    protected void doCheckOption() {

    }

    /**
     * Judge current execute engine is sql mode
     * 
     * @return
     */
    protected boolean currentModelIsSqlEngine() {
        return ExecutionLanguage.SQL.getLiteral().equals(this.execLang);
    }

    /**
     * DOC msjian Comment method "createIndicatorSelectButton".
     * 
     * @param buttonComposite
     */
    protected void createIndicatorSelectButton(Composite buttonComposite) {
        // do nothing here
    }

    /**
     * DOC msjian Comment method "registerEvents".
     */
    protected void registerEvents() {
        // register: refresh the result page after running it from menu
        afterCreateConnectionReceiver = new EventReceiver() {

            @Override
            public boolean handle(Object data) {
                // check if the connection is unavailable, give a warning dialog to user without opening the columns
                // select dialog
                Connection conn = (Connection) data;
                if (ConnectionUtils.checkConnection(conn, getAnalysisHandler().getAnalysis().getName())) {
                    // need to give the new connection to the dialog to show only this new one in the dialog.
                    openColumnsSelectionDialog(conn);
                }

                return true;
            }
        };
        EventManager.getInstance().register(dataPreviewSection, EventEnum.DQ_SELECT_ELEMENT_AFTER_CREATE_CONNECTION,
                afterCreateConnectionReceiver);

    }

    /**
     * create DataTable Composite.
     * 
     * @param dataparent
     */
    private void createDataTableComposite(Composite dataparent) {
        dataTableComp = toolkit.createComposite(dataparent, SWT.NONE);
        dataTableComp.setLayout(new GridLayout(1, Boolean.TRUE));
        GridDataFactory.fillDefaults().span(4, 1).align(SWT.FILL, SWT.CENTER).hint(SWT.DEFAULT, 250).grab(true, false)
                .applyTo(dataTableComp);

        sampleTable = new ColumnAnalysisDataSamTable();

        // no need to fetch the data after select data, only do fetch when "refresh" or run analysis
        ModelElement[] analyzedColumns = (ModelElement[]) getAnalysisHandler().getAnalyzedColumns().toArray();
        sampleTable.createNatTable(null, dataTableComp, analyzedColumns);

        dataTableComp.setVisible(isDataTableCompVisible());
        sampleTable.addPropertyChangeListener(this);
    }

    /**
     * DOC msjian Comment method "isDataTableCompVisible".
     * 
     * @return
     */
    protected boolean isDataTableCompVisible() {
        return this.currentModelElementIndicators != null && this.currentModelElementIndicators.length > 0;
    }

    /**
     * DOC talend Comment method "getSelectedColumns".
     * 
     * @return
     */
    protected ModelElement[] getSelectedColumns() {
        ModelElement[] selectedColumns = new ModelElement[this.currentModelElementIndicators.length];
        int index = 0;
        for (ModelElementIndicator modelElemIndi : this.currentModelElementIndicators) {
            IRepositoryNode modelElementRepositoryNode = modelElemIndi.getModelElementRepositoryNode();
            if (modelElementRepositoryNode != null) {
                IRepositoryViewObject currentObject = modelElementRepositoryNode.getObject();
                if (ISubRepositoryObject.class.isInstance(currentObject)) {
                    selectedColumns[index++] = ((ISubRepositoryObject) currentObject).getModelElement();
                }
            }
        }
        return selectedColumns;
    }

    public abstract AnalysisHandler getAnalysisHandler();

    public void refreshPreviewTable(boolean loadData) {
        // set sample table parameters
        sampleTable.setLimitNumber(this.getPreviewLimit());
        sampleTable.setShowRandomData(isShowRandomData());
        // TDQ-11981: when get the preview data use the data filter real value when set a context value
        sampleTable.setDataFilter(getDataFilterStr());

        sampleTable.reDrawTable(getSelectedColumns(), loadData);
        redrawWarningLabel();
    }

    /**
     * DOC zshen Comment method "getDataFilterStr".
     * 
     * @return
     */
    protected String getDataFilterStr() {
        return ContextHelper.getAnalysisContextValue(getCurrentModelElement(), dataFilterComp.getDataFilterString());
    }

    /**
     * DOC zshen Comment method "isShowRandomData".
     * 
     * @return
     */
    protected boolean isShowRandomData() {
        return SampleDataShowWay.RANDOM.getLiteral().equals(sampleDataShowWayCombo.getText());
    }

    public void redrawWarningLabel() {
        String message = PluginConstant.EMPTY_STRING;
        boolean isVisible;
        if (!sampleTable.isDataAvailable().isOk()) {
            message = sampleTable.isDataAvailable().getMessage();
            isVisible = true;
        } else {
            message = DefaultMessagesImpl.getString(
                    "ColumnMasterDetailsPage.noSameTableWarning", org.talend.dataprofiler.core.PluginConstant.EMPTY_STRING); //$NON-NLS-1$
            isVisible = !sampleTable.isSameTable();
        }

        createWarningComposite(message, isVisible);
        dataTableComp.setVisible(isDataTableCompVisible());
    }

    /**
     * DOC msjian Comment method "createWarningComposite".
     * 
     * @param message
     * @param isVisible
     */
    protected void createWarningComposite(String message, boolean isVisible) {
        if (warningComposite != null && !warningComposite.isDisposed()) {
            warningComposite.dispose();
        }

        warningComposite = toolkit.createComposite(dataTableComp);
        warningComposite.setLayout(new GridLayout(2, false));

        Label warningImage = toolkit.createLabel(warningComposite, PluginConstant.EMPTY_STRING);
        warningImage.setImage(ImageLib.getImage(ImageLib.WARNING_PNG));

        Label warningLabel = toolkit.createLabel(warningComposite, message);
        warningLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

        warningComposite.setVisible(isVisible);

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).exclude(!isVisible)
                .applyTo(warningComposite);

        dataTableComp.layout(new Control[] { warningComposite });
    }

    /**
     * create "Refresh Button", and the row control input.
     * 
     * @param buttonComposite
     */
    private void createRefreshDataButtonComp(Composite parent) {
        Composite dataQueryComp = toolkit.createComposite(parent, SWT.NONE);
        GridLayout dataQueryCompLayout = new GridLayout(4, Boolean.FALSE);
        dataQueryComp.setLayout(dataQueryCompLayout);

        // create the input to control how many rows will be loaded.
        Label rowLoadedLabel = toolkit.createLabel(dataQueryComp,
                DefaultMessagesImpl.getString("MatchMasterDetailsPage.ControlRowsLabel"), SWT.NONE); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(rowLoadedLabel);
        rowLoadedText = toolkit.createText(dataQueryComp, null, SWT.BORDER);
        rowLoadedText.setToolTipText(DefaultMessagesImpl.getString("ColumnAnalysisDetailsPage.ControlRowsLabelTooltip")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(rowLoadedText);
        // fix the width of the text field
        GridData textData = new GridData();
        textData.widthHint = 100;
        rowLoadedText.setLayoutData(textData);
        int number = Integer.valueOf(getAnalysisHandler().getDefaultLoadedRowCount()) > PREVIEW_MAX_ROW_COUNT ? PREVIEW_SUGGEST_ROW_COUNT
                : Integer.valueOf(getAnalysisHandler().getDefaultLoadedRowCount());
        rowLoadedText.setText(String.valueOf(number));
        rowLoadedText.setTextLimit(3);
        rowLoadedText.addVerifyListener(new VerifyListener() {

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

        rowLoadedText.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                setDirty(true);
            }
        });

        // ADD msjian TDQ-8428: add random way to show data
        sampleDataShowWayCombo = new CCombo(dataQueryComp, SWT.BORDER);
        sampleDataShowWayCombo.setEditable(false);
        for (SampleDataShowWay value : SampleDataShowWay.VALUES) {
            sampleDataShowWayCombo.add(value.getLiteral());
        }

        SampleDataShowWay sampleDataShowWay = getCurrentModelElement().getParameters().getSampleDataShowWay();
        sampleDataShowWayCombo.setText(sampleDataShowWay.getLiteral());
        sampleDataShowWayCombo.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                setDirty(true);
            }
        });
        // TDQ-8428~

        Button refreshDataBtn = toolkit.createButton(dataQueryComp,
                DefaultMessagesImpl.getString("MatchMasterDetailsPage.RefreshDataButton"), SWT.NONE);//$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(refreshDataBtn);

        refreshDataBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                refreshPreviewData();
            }

        });
    }

    /**
     * open the column selection dialog.
     * 
     * @param dataManager
     */
    public void openColumnsSelectionDialog(DataManager dataManager) {
        if (connCombo != null) {
            reloadDataproviderAndFillConnCombo();
        }
        List<IRepositoryNode> reposViewObjList = new ArrayList<IRepositoryNode>();
        RepositoryNode connNode = getConnComboSelectNode();
        int connIndex = getConnCombo().getSelectionIndex();
        String connName = dataManager.getName();
        for (int index = 0; index < getConnCombo().getItemCount(); index++) {
            if (connName.equalsIgnoreCase(getConnCombo().getItem(index))) {
                connNode = (RepositoryNode) getConnCombo().getData(String.valueOf(index));
                connIndex = index;
                break;
            }
        }
        ColumnsSelectionDialog dialog = new ColumnsSelectWithConstraintDialog(
                this,
                null,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.columnSelection"), reposViewObjList, connNode, DefaultMessagesImpl //$NON-NLS-1$
                        .getString("ColumnMasterDetailsPage.columnSelections")); //$NON-NLS-1$
        if (dialog.open() == Window.OK) {
            getConnCombo().select(connIndex);
            // save the old first, need to use this to revert
            oldConn = getCurrentModelElement().getContext().getConnection();

            getCurrentModelElement().getContext().setConnection(dataManager);
            Object[] modelElements = dialog.getResult();
            setTreeViewInput(modelElements);
        }
    }

    /**
     * DOC msjian Comment method "setTreeViewInput".
     * 
     * @param modelElements
     */
    protected void setTreeViewInput(Object[] modelElements) {
        // do nothing here
    }

    /**
     * check if the row loaded value is valid or not
     * 
     * @return
     */
    protected boolean isValidateRowCount() {
        if (rowLoadedText != null && StringUtils.isEmpty(rowLoadedText.getText())) {
            return false;
        }
        return true;
    }

    /**
     * 
     * DOC qiongli Comment method "createRunButton".
     * 
     * @param buttonComposite
     */
    public void createRunButton(Composite buttonComposite) {
        String text = org.talend.dataprofiler.core.PluginConstant.SPACE_STRING
                + DefaultMessagesImpl.getString("ColumnAnalysisDetailsPage.runButton") //$NON-NLS-1$
                + org.talend.dataprofiler.core.PluginConstant.SPACE_STRING;
        Button runBtn = toolkit.createButton(buttonComposite, text, SWT.NONE);
        runBtn.setToolTipText(DefaultMessagesImpl.getString("ColumnAnalysisDetailsPage.runButtonTooltip")); //$NON-NLS-1$
        runBtn.setImage(ImageLib.getImage(ImageLib.RUN_IMAGE));
        GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(runBtn);
        runBtn.addMouseListener(new MouseAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
             */
            @Override
            public void mouseDown(MouseEvent e) {
                Action runAnalysisAction = ((AnalysisEditor) currentEditor).getRunAnalysisAction();
                if (runAnalysisAction != null) {
                    runAnalysisAction.run();
                }
            }
        });
    }

    /**
     * DOC talend Comment method "createColumnSelectButton".
     * 
     * @param buttonComposite
     */
    protected void createColumnSelectButton(Composite buttonComposite) {
        Button clmnBtn = toolkit.createButton(buttonComposite,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.selectColumnBtn"), //$NON-NLS-1$
                SWT.NONE);
        clmnBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                openColumnsSelectionDialog();

            }
        });
    }

    /**
     * open the column selection dialog.
     */
    public void openColumnsSelectionDialog() {
        if (connCombo != null) {
            reloadDataproviderAndFillConnCombo();
        }
        RepositoryNode connNode = getConnComboSelectNode();
        List<IRepositoryNode> reposViewObjList = new ArrayList<IRepositoryNode>();
        for (ModelElementIndicator modelElementIndicator : currentModelElementIndicators) {
            reposViewObjList.add(modelElementIndicator.getModelElementRepositoryNode());
        }
        ColumnsSelectionDialog dialog = new ColumnsSelectWithConstraintDialog(
                this,
                null,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.columnSelection"), reposViewObjList, connNode, DefaultMessagesImpl //$NON-NLS-1$
                        .getString("ColumnMasterDetailsPage.columnSelections")); //$NON-NLS-1$
        if (dialog.open() == Window.OK) {
            Object[] modelElements = dialog.getResult();
            setTreeViewInput(modelElements);
            // TDQ-11590: automatically refresh the data table after we select column with the "select columns" dialog.
            refreshPreviewData();
            // TDQ-11590~
        }
    }

    /**
     * DOC talend Comment method "createConnectionButton".
     * 
     * @param dataPreviewTableCom
     */
    protected void createConnectionButton(final Composite dataPreviewTableCom, final Section dataPreviewSection) {
        Button createConnectionButton = toolkit.createButton(dataPreviewTableCom,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.createConnectionBtn"), SWT.NONE); //$NON-NLS-1$
        createConnectionButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                ConnectionWizard connectionWizard = new ConnectionWizard(PlatformUI.getWorkbench(), dataPreviewSection);
                connectionWizard.setForcePreviousAndNextButtons(true);
                WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), connectionWizard);
                dialog.setPageSize(500, 200);
                dialog.open();

            }
        });
    }

    /**
     * create the datafilter section.
     * 
     * @param form1
     * @param anasisDataComp
     * @param needFillBoth: if true, will fill both the section.
     */
    void createDataFilterSection(final ScrolledForm form1, Composite anasisDataComp, boolean needFillBoth) {
        dataFilterSection = createSection(
                form1,
                anasisDataComp,
                DefaultMessagesImpl.getString("ColumnMasterDetailsPage.dataFilter"), DefaultMessagesImpl.getString("ColumnMasterDetailsPage.editDataFilter")); //$NON-NLS-1$ //$NON-NLS-2$

        Composite sectionClient = toolkit.createComposite(dataFilterSection);

        if (needFillBoth) {
            // the text will fill both the section. can see ColumnDependencyMasterDetailsPage
            sectionClient.setLayoutData(new GridData(GridData.FILL_BOTH));
            sectionClient.setLayout(new GridLayout());
        }

        dataFilterComp = new DataFilterComp(sectionClient, stringDataFilter);
        installProposals(dataFilterComp.getDataFilterText());
        // dataFilterComp.addPropertyChangeListener(this);
        addWhitespaceValidate(dataFilterComp.getDataFilterText());
        dataFilterSection.setClient(sectionClient);
    }

    /**
     * create the datafilter section without fill both the section.
     * 
     * @param form1
     * @param anasisDataComp
     */
    void createDataFilterSection(final ScrolledForm form1, Composite anasisDataComp) {
        createDataFilterSection(form1, anasisDataComp, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#saveContext()
     */
    @Override
    protected void saveContext() {
        // save contexts
        Analysis analysis = getCurrentModelElement();
        IContextManager contextManager = currentEditor.getContextManager();
        contextManager.saveToEmf(analysis.getContextType());
        analysis.setDefaultContext(getDefaultContextGroupName((SupportContextEditor) currentEditor));
        AnalysisHelper.setLastRunContext(currentEditor.getLastRunContextGroupName(), analysis);
    }

    /**
     * 
     * when rename the related connection ,it will reload connection combo,also need to update TreeViewer,so that avoid
     * some old column RepositoryNode instance .if it is not dirty before updating,should keep the not dirty satus.
     */
    protected void updateAnalysisTree() {
        AbstractColumnDropTree treeViewer = getTreeViewer();
        if (treeViewer != null) {
            boolean beforeUpdateDirty = treeViewer.isDirty();
            treeViewer.updateModelViewer();
            if (!beforeUpdateDirty) {
                treeViewer.setDirty(false);
            }
        }
    }

    public void refreshPreviewData() {
        if (isValidateRowCount()) {
            refreshPreviewTable(true);
        } else {
            MessageDialog.openWarning(null, DefaultMessagesImpl.getString("MatchMasterDetailsPage.NotValidate"), //$NON-NLS-1$
                    DefaultMessagesImpl.getString("MatchMasterDetailsPage.LoadedRowCountError")); //$NON-NLS-1$
            rowLoadedText.setFocus();
        }
    }

    /**
     * automatically Refresh Preview Data.
     */
    public void autoRefreshPreviewData() {
        // TDQ-11513 msjian 20160203: automatically refresh data when the analysis opens
        Job job = new Job(DefaultMessagesImpl.getString("ColumnMasterDetailsPage.dataPreviewLoadingData")) { //$NON-NLS-1$

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        // for the analysis didn't have the preview section, no need to do refresh. like the overview
                        // analysis
                        if (rowLoadedText == null) {
                            return;
                        }

                        refreshPreviewData();
                    }
                });
                return Status.OK_STATUS;
            }

        };
        job.setUser(true);
        job.schedule();
        // TDQ-11513~
    }

    protected void setSampleDataShowWayStatus() {
        if (sampleDataShowWayCombo != null) {
            sampleDataShowWayCombo.setEnabled(true);

            Object data = getConnComboSelectNode();
            if (data != null) {
                if (data instanceof DBConnectionRepNode) {
                    DBConnectionRepNode dbConnRepNode = (DBConnectionRepNode) data;
                    String databaseType = dbConnRepNode.getDatabaseConnection().getDatabaseType() == null ? "" : dbConnRepNode.getDatabaseConnection().getDatabaseType(); //$NON-NLS-1$
                    if (databaseType.toLowerCase().contains("informix") || databaseType.toLowerCase().contains("sybase")) { //$NON-NLS-1$ //$NON-NLS-2$
                        sampleDataShowWayCombo.select(0);
                        sampleDataShowWayCombo.setEnabled(false);
                    }

                } else if (data instanceof DFConnectionRepNode) {
                    sampleDataShowWayCombo.select(0);
                    sampleDataShowWayCombo.setEnabled(false);
                }
            } else {
                sampleDataShowWayCombo.select(0);
                sampleDataShowWayCombo.setEnabled(false);
            }
        }
    }

    /**
     * DOC zshen Comment method "getExecCombo".
     * 
     * @return the Combo for executeLanguage
     */
    public CCombo getExecCombo() {
        return execCombo;
    }

    /**
     * DOC qiongli Comment method "setWhereClauseDisabled".
     */
    public void setWhereClauseDisabled() {
        if (dataFilterComp != null) {
            dataFilterComp.getDataFilterText().setEnabled(false);
        }
    }

    /**
     * DOC xqliu Comment method "setWhereClauseEnable".
     */
    public void setWhereClauseEnable() {
        if (dataFilterComp != null) {
            dataFilterComp.getDataFilterText().setEnabled(true);
        }
    }

    /**
     * DOC xqliu Comment method "changeExecuteLanguageToSql".
     * 
     * @param enabled
     */
    public void changeExecuteLanguageToSql(boolean enabled) {
        if (this.execCombo == null) {
            return;
        }

        if (!currentModelIsSqlEngine()) {
            int i = 0;
            for (ExecutionLanguage language : ExecutionLanguage.VALUES) {
                if (language.compareTo(ExecutionLanguage.SQL) == 0 && execCombo.getSelectionIndex() != i) {
                    this.execCombo.select(i);
                    refreshEnginSection();
                } else {
                    i++;
                }
            }
        }

        execCombo.setEnabled(enabled);
    }

    /**
     * 
     * Get the limit of preivew table
     * 
     * @return
     */
    public int getPreviewLimit() {
        return Integer.parseInt(rowLoadedText.getText());
    }

    public ColumnAnalysisDataSamTable getSampleTable() {
        return this.sampleTable;
    }

    public DataFilterComp getDataFilterComp() {
        return this.dataFilterComp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentRepNode()
     */
    @Override
    public IRepositoryNode getCurrentRepNode() {
        return analysisRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentModelElement()
     */
    @Override
    public Analysis getCurrentModelElement() {
        if (analysisRepNode instanceof ReportAnalysisRepNode) {
            return ((ReportAnalysisRepNode) analysisRepNode).getAnalysis();
        }
        return ((AnalysisRepNode) analysisRepNode).getAnalysis();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#init(org.eclipse.ui.forms.editor.FormEditor)
     */
    @Override
    protected void init(FormEditor editor) {
        currentEditor = (AnalysisEditor) editor;
        this.analysisRepNode = getAnalysisRepNodeFromInput(currentEditor.getEditorInput());
    }

    /**
     * get AnalysisRepNode From editorInput
     * 
     * @param editorInput
     * @return
     */
    private IRepositoryNode getAnalysisRepNodeFromInput(IEditorInput editorInput) {
        if (editorInput instanceof FileEditorInput) {
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (file != null) {
                Analysis analysis = AnaResourceFileHelper.getInstance().findAnalysis(file);
                analysis = (Analysis) EObjectHelper.resolveObject(analysis);
                return RepositoryNodeHelper.recursiveFindAnalysis(analysis);
            }
        } else if (editorInput instanceof AnalysisItemEditorInput) {
            return ((AnalysisItemEditorInput) editorInput).getRepNode();
        }
        return null;
    }
}
