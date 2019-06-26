// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.RepositoryUpdateManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.compare.exception.ReloadCompareException;
import org.talend.cwm.compare.factory.ComparisonLevelFactory;
import org.talend.cwm.compare.factory.IComparisonLevel;
import org.talend.cwm.db.connection.ConnectionUtils;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.api.SoftwareSystemManager;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.ContextViewHelper;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.manager.DQStructureManager;
import org.talend.dataprofiler.core.ui.action.actions.CreateHiveOfHCAction;
import org.talend.dataprofiler.core.ui.dialog.message.DeleteModelElementConfirmDialog;
import org.talend.dataprofiler.core.ui.editor.PartListener;
import org.talend.dataprofiler.core.ui.editor.dqrules.BusinessRuleItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.dqrules.DQRuleEditor;
import org.talend.dataprofiler.core.ui.events.EventEnum;
import org.talend.dataprofiler.core.ui.events.EventManager;
import org.talend.dataprofiler.core.ui.events.EventReceiver;
import org.talend.dataprofiler.core.ui.events.SoftwareSystemUpdateEventReceiver;
import org.talend.dataprofiler.core.ui.utils.TableUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataquality.indicators.definition.IndicatorCategory;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dq.CWMPlugin;
import org.talend.dq.dqrule.DqRuleBuilder;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.dq.writer.impl.DataProviderWriter;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.hive.handler.HiveConnectionHandler;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.EResourceConstant;
import org.talend.resource.ResourceManager;
import org.talend.utils.dates.DateUtils;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class TOPRepositoryService implements ITDQRepositoryService {

    private static Logger log = Logger.getLogger(TOPRepositoryService.class);

    // after creating the database connection, will open the connection editor when this parameter= false;
    // will not open the connection editor when this parameter=true;
    private boolean noNeedToOpenConnectionEditor = Boolean.FALSE;

    public IViewPart getTDQRespositoryView() {
        return CorePlugin.getDefault().getRepositoryView();
    }

    public void notifySQLExplorer(Item... items) {
        if (items == null) {
            return;
        }

        for (Item item : items) {
            if (item instanceof ConnectionItem) {
                Connection connection = ((ConnectionItem) item).getConnection();
                CWMPlugin.getDefault().addConnetionAliasToSQLPlugin(connection);
            }
        }
    }

    /**
     * Fill MDM connection only.
     */
    public void fillMetadata(ConnectionItem connItem) {
        MetadataConnectionUtils.fillConnectionInformation(connItem);
        // MOD gdbu 2011-7-12 bug : 22598
        MDMConnection mdmConnection = (MDMConnection) connItem.getConnection();
        mdmConnection.setLabel(connItem.getProperty().getLabel() + "");
        mdmConnection.setName(connItem.getProperty().getLabel() + "");
        ElementWriterFactory.getInstance().createDataProviderWriter().save(mdmConnection);
        // ~22598
    }

    public void refresh() {
        CorePlugin.getDefault().refreshWorkSpace();
        // ~ TDQ-5133 mzhao 2012-05-31 when there are many children for a db connection, it's more elegant to collapse
        // the metadata folder when refreshing the tree to save time.
        IRepositoryNode metadataNode = RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.DB_CONNECTIONS);
        // MOD msjian TUP-274 2012-11-14: avoid NPE of metadataNode
        // getRepositoryView() maybe return null when DqRepository veiw not be opened
        DQRespositoryView repositoryView = CorePlugin.getDefault().getRepositoryView();
        if (repositoryView != null) {
            CommonViewer commonViewer = repositoryView.getCommonViewer();
            if (commonViewer != null) {
                if (metadataNode != null) {
                    commonViewer.collapseToLevel(metadataNode, AbstractTreeViewer.ALL_LEVELS);
                } else {
                    commonViewer.collapseAll();
                }
            }
        }
        // TUP-274~
        // ~ TDQ-5311
        CorePlugin.getDefault().refreshDQView();
    }

    /**
     * ADDED yyin 20120503 TDQ-4959.
     *
     * @param node
     */
    public void refresh(Object refreshObject) {
        if (refreshObject == null) {
            this.refresh();
            return;
        }
        if (refreshObject instanceof RepositoryNode) {
            CorePlugin.getDefault().refreshWorkSpace();
            CorePlugin.getDefault().refreshDQView(refreshObject);
        } else if (refreshObject instanceof Item) {
            RepositoryNode node = RepositoryNodeHelper.recursiveFind(((Item) refreshObject).getProperty());
            CorePlugin.getDefault().refreshWorkSpace();
            CorePlugin.getDefault().refreshDQView(node == null ? null : node.getParent());
        } else {
            this.refresh();
        }
    }

    public void initProxyRepository() {
        CorePlugin.getDefault().initProxyRepository();
    }

    public void addPartListener() {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        // Calculate the extensions to register partListener.
        IPartListener listener = PartListener.getPartListener();
        if (listener != null) {
            activePage.addPartListener(listener);
        }
    }

    public boolean removeAliasInSQLExplorer(IRepositoryNode children) {
        boolean hasDependencyItem = true;
        // MOD klliu 2011-04-28 bug 20204 removing connection is synced to the connection view of SQL explore
        Item item = children.getObject().getProperty().getItem();
        // MOD mzhao filter the connections which is not a type of database.
        if (item != null && item instanceof ConnectionItem) {
            Connection connection = ((ConnectionItem) item).getConnection();
            if (connection instanceof DatabaseConnection || connection instanceof DelimitedFileConnection
                    || connection instanceof MDMConnection) {
                CWMPlugin.getDefault().removeAliasInSQLExplorer(connection);
            }
        }

        return hasDependencyItem;
    }

    public void createParserRuleItem(ArrayList<HashMap<String, Object>> values, String parserRuleName) {
        ParserRule parserRule = null;
        DqRuleBuilder ruleBuilder = new DqRuleBuilder();
        boolean ruleInitialized = ruleBuilder.initializeParserRuleBuilder(parserRuleName);
        if (ruleInitialized) {
            parserRule = ruleBuilder.getParserRule();
        }
        TaggedValueHelper.setValidStatus(true, parserRule);
        for (HashMap<String, Object> expression : values) {
            parserRule
                    .addExpression(
                            expression.get(RULE_NAME).toString(),
                            expression.get(RULE_TYPE) instanceof Integer ? Integer.toString((Integer) expression
                                    .get(RULE_TYPE)) : expression.get(RULE_TYPE).toString(), expression
                                    .get(RULE_VALUE)
                                    .toString());
        }
        IndicatorCategory ruleIndicatorCategory = DefinitionHandler.getInstance().getDQRuleIndicatorCategory();
        if (ruleIndicatorCategory != null && !parserRule.getCategories().contains(ruleIndicatorCategory)) {
            parserRule.getCategories().add(ruleIndicatorCategory);
        }
        IFolder folder = ResourceManager.getRulesParserFolder();
        TypedReturnCode<Object> returnObject =
                ElementWriterFactory.getInstance().createdRuleWriter().create(parserRule, folder);
        Object object = returnObject.getObject();

        RuleRepNode parserRuleNode = RepositoryNodeHelper.recursiveFindRuleParser(parserRule);
        BusinessRuleItemEditorInput parserRuleEditorInput = new BusinessRuleItemEditorInput(parserRuleNode);
        CorePlugin.getDefault().openEditor(parserRuleEditorInput, DQRuleEditor.class.getName());
        refresh(object);
    }

    public List<Map<String, String>> getPaserRulesFromRules(Object parser) {
        if (parser != null && parser instanceof ParserRule) {
            List<Map<String, String>> ruleValues = new ArrayList<Map<String, String>>();
            for (TdExpression exp : ((ParserRule) parser).getExpression()) {
                Map<String, String> pr = new HashMap<String, String>();
                // MOD yyi 2011-08-12 TDQ-1698:avoid importing null value
                pr.put(RULE_NAME, null == exp.getName() ? StringUtils.EMPTY : exp.getName());
                pr.put(RULE_VALUE, null == exp.getBody() ? StringUtils.EMPTY : exp.getBody());
                pr.put(RULE_TYPE, null == exp.getLanguage() ? StringUtils.EMPTY : exp.getLanguage().toUpperCase());
                ruleValues.add(pr);
            }
            return ruleValues;
        }
        return null;
    }

    /*
     * Added yyi 2011-08-04 TDQ-3186
     *
     * @see org.talend.core.ITDQRepositoryService#getPaserRulesFromResources(java.lang.Object[])
     */
    public List<Map<String, String>> getPaserRulesFromResources(Object[] rules) {
        List<Map<String, String>> ruleValues = new ArrayList<Map<String, String>>();
        for (Object rule : rules) {
            if (rule instanceof IFile) {
                ParserRule parserRule = (ParserRule) DQRuleResourceFileHelper.getInstance().findDQRule((IFile) rule);
                ruleValues.addAll(getPaserRulesFromRules(parserRule));
            }
        }
        return ruleValues;
    }

    /**
     *
     * Comment method "reloadDatabase".
     *
     * @param connectionItem
     * @deprecated instead of it by TDQCompareService.reloadDatabase
     */
    @Deprecated
    public ReturnCode reloadDatabase(ConnectionItem connectionItem) {
        ReturnCode retCode = new ReturnCode(Boolean.TRUE);
        Connection conn = connectionItem.getConnection();
        try {
            if (conn instanceof DatabaseConnection) {
                List<ModelElement> dependencyClients = EObjectHelper.getDependencyClients(conn);
                if (!(dependencyClients == null || dependencyClients.isEmpty())) {
                    int isOk =
                            DeleteModelElementConfirmDialog.showElementImpactConfirmDialog(
                                    null,
                                    new ModelElement[] { conn },
                                    DefaultMessagesImpl.getString("TOPRepositoryService.dependcyTile"), //$NON-NLS-1$
                                    DefaultMessagesImpl.getString(
                                            "TOPRepositoryService.dependcyMessage", conn.getLabel())); //$NON-NLS-1$
                    if (isOk != Dialog.OK) {
                        retCode.setOk(Boolean.FALSE);
                        retCode.setMessage("The user canceled the operation!"); //$NON-NLS-1$
                        return retCode;
                    }
                }

                final IComparisonLevel creatComparisonLevel = ComparisonLevelFactory.creatComparisonLevel(conn);
                Connection newConnection = creatComparisonLevel.reloadCurrentLevelElement();

                // update the sql explore.
                Property property = PropertyHelper.getProperty(newConnection);
                if (property != null) {
                    Item newItem = property.getItem();
                    if (newItem != null) {
                        CWMPlugin.getDefault().updateConnetionAliasByName(newConnection, newConnection.getLabel());
                        // notifySQLExplorer(newItem);
                    }
                }
                // update the related analyses.
                WorkbenchUtils.impactExistingAnalyses(newConnection);
            }
        } catch (ReloadCompareException e) {
            log.error(e, e);
            retCode.setOk(Boolean.FALSE);
            retCode.setMessage(e.getMessage());
        } catch (PartInitException e) {
            log.error(e, e);
            retCode.setOk(Boolean.FALSE);
            retCode.setMessage(e.getMessage());
        }
        return retCode;
    }

    public void updateImpactOnAnalysis(ConnectionItem connectionItem) {
        try {
            if (connectionItem == null) {
                return;
            }
            Connection connection = connectionItem.getConnection();
            List<ModelElement> clientDependencys = EObjectHelper.getDependencyClients(connection);
            if (clientDependencys != null && clientDependencys.size() > 0) {
                WorkbenchUtils.impactExistingAnalyses(connection);
                IRepositoryNode node = RepositoryNodeHelper.recursiveFind(connection);
                if (node != null) {
                    CorePlugin.getDefault().refreshDQView(node);
                }
            }

        } catch (PartInitException e) {
            log.error(e, e);
        }
    }

    public void reloadMetadataOfDelimitedFile(MetadataTable newMetadataTable) throws BusinessException {
        WorkbenchUtils.reloadMetadataOfDelimitedFile(newMetadataTable);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#confirmUpdateAnalysis(org.talend.core.model.properties.ConnectionItem)
     */
    public boolean confirmUpdateAnalysis(ConnectionItem connectionItem) {
        // optimize code, just open an dialog,don't need to judge if has dependce in here.
        if (MessageDialog.openQuestion(null, DefaultMessagesImpl.getString("TOPRepositoryService.dependcyTile"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("TOPRepositoryService.propgateAnalyses", connectionItem.getProperty() //$NON-NLS-1$
                        .getLabel()))) {
            return true;
        }
        return false;
    }

    public boolean hasClientDependences(ConnectionItem connItem) {
        if (connItem != null) {
            Connection connection = connItem.getConnection();
            if (connection != null) {
                List<ModelElement> clientDependences = EObjectHelper.getDependencyClients(connection);
                if (clientDependences != null && !clientDependences.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Comment method "confimDelete".
     *
     * @param deleteObject which you want to delete
     * @return SWT.OK or SWT.Cancel
     */
    public int confimDelete(IRepositoryViewObject deleteObject) {
        Shell workbenchShell = CorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
        Property deleteProperty = deleteObject.getProperty();
        ModelElement deleteModelElement = PropertyHelper.getModelElement(deleteProperty);
        List<ModelElement> dependencyElements = EObjectHelper.getDependencyClients(deleteObject);
        String lable =
                deleteProperty.getDisplayName() == null ? PluginConstant.EMPTY_STRING : deleteProperty.getDisplayName();
        String dialogTitle = DefaultMessagesImpl.getString("DeleteModelElementConfirmDialog.confirmResourceDelete");//$NON-NLS-1$
        String dialogMessage = DefaultMessagesImpl.getString("DQDeleteAction.dependencyByOther", lable);//$NON-NLS-1$
        return DeleteModelElementConfirmDialog.showConfirmDialog(workbenchShell, deleteModelElement,
                dependencyElements.toArray(new ModelElement[dependencyElements.size()]), dialogTitle, dialogMessage);
    }

    /**
     * Comment method "getInputDialog".
     *
     * @param get input dialog
     * @return inputDialog
     */
    public InputDialog getInputDialog(final Item newItem) {
        Shell parentShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        String dialogTitle = DefaultMessagesImpl.getString("TOPRepositoryService.InputDialog.Title");//$NON-NLS-1$
        String dialogMessage = DefaultMessagesImpl.getString("TOPRepositoryService.InputDialog.Message");//$NON-NLS-1$
        final InputDialog inputDialog =
                new InputDialog(parentShell, dialogTitle, dialogMessage, newItem.getProperty().getLabel()
                        + DateUtils.formatTimeStamp(DateUtils.PATTERN_6, System.currentTimeMillis()),
                        new IInputValidator() {

                            public String isValid(String newText) {
                                String returnStr = null;
                                Item item = newItem;
                                ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);
                                // String pattern = RepositoryConstants.getPattern(type);
                                String pattern = "[_A-Za-z0-9-][a-zA-Z0-9\\\\.\\\\-_(), ]*";//$NON-NLS-1$
                                boolean matches = Pattern.matches(pattern, newText);
                                boolean nameAvailable = false;
                                try {
                                    List<IRepositoryViewObject> listExistingObjects =
                                            ProxyRepositoryFactory.getInstance().getAll(type, true, false);
                                    nameAvailable =
                                            ProxyRepositoryFactory.getInstance().isNameAvailable(item, newText,
                                                    listExistingObjects);
                                } catch (PersistenceException e) {
                                    log.error(e, e);
                                    return e.getMessage();
                                }
                                if (!matches) {
                                    returnStr =
                                            DefaultMessagesImpl
                                                    .getString("TOPRepositoryService.InputDialog.ErrorMessage1");//$NON-NLS-1$
                                } else if (!nameAvailable) {
                                    returnStr =
                                            DefaultMessagesImpl
                                                    .getString("TOPRepositoryService.InputDialog.ErrorMessage2");//$NON-NLS-1$
                                }
                                return returnStr;
                            }
                        });
        return inputDialog;
    }

    /**
     * Comment method "changeElementName".
     *
     * @param item the item which will be changed
     * @param newName
     *
     */
    public void changeElementName(Item item, String newName) {
        Property property = item.getProperty();
        PropertyHelper.changeName(property, newName);
    }

    /*
     * (non-Jsdoc)
     *
     * @see org.talend.core.ITDQRepositoryService#sourceFileOpening(org.talend.repository.model.RepositoryNode)
     */
    public boolean sourceFileOpening(RepositoryNode node) {
        boolean result = false;
        if (node instanceof SourceFileRepNode) {
            result = WorkspaceResourceHelper.checkSourceFileNodeOpening(node).isOk();
        } else if (node instanceof SourceFileSubFolderNode) {
            result = WorkspaceResourceHelper.checkSourceFileSubFolderNodeOpening(node).isOk();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#checkUsernameBeforeSaveConnection(org.talend.core.model.properties.
     * ConnectionItem)
     */
    public void checkUsernameBeforeSaveConnection(ConnectionItem connectionItem) {
        Connection connection = connectionItem.getConnection();
        ConnectionUtils.checkUsernameBeforeSaveConnection4Sqlite(connection);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#removeSoftWareSystem(org.talend.repository.model.IRepositoryNode)
     */
    @Deprecated
    public boolean removeSoftWareSystem(IRepositoryNode children) {
        Item item = children.getObject().getProperty().getItem();
        if (item != null && item instanceof ConnectionItem) {
            return SoftwareSystemManager.getInstance().cleanSoftWareSystem(((ConnectionItem) item).getConnection());
        }
        return false;
    }

    /**
     * TDQ-6166,Add this function for init all connections in DataExplorer perspective.
     */
    public void initAllConnectionsToSQLExplorer() {
        SqlExplorerUtils.getDefault().initAllConnectionsToSQLExplorer();
    }

    public void saveConnectionWithDependency(ConnectionItem connectionItem) {
        DataProviderWriter dataProviderWriter = ElementWriterFactory.getInstance().createDataProviderWriter();
        dataProviderWriter.save(connectionItem, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#refreshCurrentAnalysisEditor()
     */
    public void refreshCurrentAnalysisEditor(ConnectionItem connectionItem) {
        WorkbenchUtils.nodifyDependedAnalysis(connectionItem);
        // WorkbenchUtils.refreshCurrentAnalysisEditor();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#refreshCurrentAnalysisAndConnectionEditor()
     */
    @Deprecated
    public void refreshCurrentAnalysisAndConnectionEditor() {
        WorkbenchUtils.refreshCurrentAnalysisAndConnectionEditor();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.core.ITDQRepositoryService#createHiveConnection(org.talend.core.model.metadata.IMetadataConnection)
     */
    public java.sql.Connection createHiveConnection(IMetadataConnection metadataConnection) {
        java.sql.Connection connection = null;
        if (metadataConnection != null
                && EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConnection.getDbType())) {
            try {

                HiveConnectionHandler hiveConnHandler =
                        HiveConnectionManager.getInstance().createHandler(metadataConnection);
                connection = hiveConnHandler.createHiveConnection();
            } catch (ClassNotFoundException e) {
                log.error(e);
            } catch (InstantiationException e) {
                log.error(e);
            } catch (IllegalAccessException e) {
                log.error(e);
            } catch (SQLException e) {
                log.error(e);
            }
        }
        return connection;
    }

    /**
     * judge if the related editor is opened.
     **/
    public boolean isDQEditorOpened(Item item) {
        if (item != null) {
            boolean hasOpened = CorePlugin.getDefault().itemIsOpening(item, false);
            if (hasOpened) {
                return true;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#addSoftwareSystemUpdateListener()
     */
    public void addSoftwareSystemUpdateListener() {
        EventReceiver softwareSystemUpdateEventReceiver = new SoftwareSystemUpdateEventReceiver();
        EventManager.getInstance().register(EventEnum.DQ_SOFTWARESYSTEM_UPDATE.name(),
                EventEnum.DQ_SOFTWARESYSTEM_UPDATE, softwareSystemUpdateEventReceiver);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#publishSoftwareSystemUpdateEvent()
     */
    public void publishSoftwareSystemUpdateEvent(DatabaseConnection databaseConnection) {
        EventManager.getInstance().publish(EventEnum.DQ_SOFTWARESYSTEM_UPDATE.name(),
                EventEnum.DQ_SOFTWARESYSTEM_UPDATE, databaseConnection);
    }

    public void setIsOpenConnectionEditorAfterCreate(boolean noNeedToOpenConnectionEditor) {
        this.noNeedToOpenConnectionEditor = noNeedToOpenConnectionEditor;
    }

    public void refreshTableWithResult(ModelElement analysis, List<Object[]> result) {
        EventManager.getInstance().publish(analysis, EventEnum.DQ_MATCH_ANALYSIS_REFRESH_WITH_RESULT, result);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#updateAliasInSQLExplorer(java.sql.Connection, java.lang.String)
     */
    public void updateAliasInSQLExplorer(ConnectionItem connectionItem, String oldConnName) {
        Connection connection = connectionItem.getConnection();
        CWMPlugin.getDefault().updateConnetionAliasByName(connection, oldConnName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#getAllDataProviders()
     */
    public Collection<Connection> getAllDataProviders() {
        return CorePlugin.getDefault().getAllDataProviders();
    }

    public void publishDynamicEvent(ModelElement indicator, Object value) {
        EventManager.getInstance().publish(indicator, EventEnum.DQ_DYMANIC_CHART, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#createFile(java.lang.String, org.eclipse.core.runtime.IPath,
     * java.lang.String, java.lang.String)
     */
    public Item createFile(String content, IPath path, String label, String extension) {
        Item item = DQStructureManager.getInstance().createSourceFileItem(content, path, label, extension);
        // Added TDQ-7532, 20130719 yyin: to lock the editor when creating the sql file from "preview table"
        ProxyRepositoryFactory.getInstance().isEditableAndLockIfPossible(item);// ~
        CorePlugin.getDefault().refreshDQView(
                RepositoryNodeHelper.getLibrariesFolderNode(EResourceConstant.SOURCE_FILES));
        return item;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#ChangePerspectiveAction(java.lang.String)
     */
    public void changePerspectiveAction(String perspectiveId) {
        new org.talend.dataprofiler.core.ui.perspective.ChangePerspectiveAction(perspectiveId).run();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.ITDQRepositoryService#createHive(org.talend.repository.model.RepositoryNode)
     */
    public ConnectionItem createHive(RepositoryNode currentNode) {
        // to open the wizard: create hive
        CreateHiveOfHCAction createHive = new CreateHiveOfHCAction(currentNode.getParent().getParent());
        createHive.run();
        return createHive.getConnectionItem();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.talend.core.ITDQRepositoryService#reloadTableList(org.talend.core.model.properties.DatabaseConnectionItem)
     */
    public void reloadTableList(final DatabaseConnectionItem hiveConnectionItem2) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        IWorkspaceRunnable operation = new IWorkspaceRunnable() {

            public void run(IProgressMonitor monitor) throws CoreException {

                RepositoryNode tableFolder = TableUtils.getTableFolder(hiveConnectionItem2);
                if (tableFolder != null) {
                    IComparisonLevel creatComparisonLevel = ComparisonLevelFactory.creatComparisonLevel(tableFolder);
                    try {
                        creatComparisonLevel.reloadCurrentLevelElement();
                    } catch (ReloadCompareException e) {
                        log.error(e, e);
                    }
                    CorePlugin.getDefault().refreshDQView(tableFolder);
                }
            }

        };
        try {
            workspace.run(operation, null);
        } catch (CoreException e) {
            log.error(e, e);
        }
    }

    /**
     * Go through every analysis and reports, to find if it imported the current context, and update the context value
     * in it
     */
    public void updateAllContextInAnalysisAndReport(RepositoryUpdateManager ruManager, Object parameter, boolean isUpdated) {
        if (parameter instanceof ContextItem) {
            ContextViewHelper.updateAllContextInAnalysisAndReport(ruManager, (ContextItem) parameter, isUpdated);
        }
    }

}
