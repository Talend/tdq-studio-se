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
package org.talend.dataprofiler.core.ui.editor.composite;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.relational.TdColumn;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.action.actions.ChangeConnectionAction;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;

/**
 * DOC rli class global comment. Detailled comment
 */
public abstract class AbstractPagePart {

    private static Logger log = Logger.getLogger(AbstractPagePart.class);

    private boolean isDirty;

    protected PropertyChangeSupport propertyChangeSupport;

    private SelectionListener selectionListener;

    public AbstractPagePart() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void setDirty(boolean dirty) {
        if (isDirty != dirty) {
            this.isDirty = dirty;
            propertyChangeSupport.firePropertyChange(PluginConstant.ISDIRTY_PROPERTY, null, Boolean.valueOf(dirty));
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * ADD mzhao 2009-05-05 bug:6587. MOD 20130524 TDQ-7327 yyin: even when the indicators is empty, if the tdProvider
     * is not null, should also set the connection state
     */
    protected void updateBindConnection(AbstractAnalysisMetadataPage masterPage, ModelElementIndicator[] indicators, Tree tree) {
        // MOD mzhao 2010-07-24, avoid a NPE, feature 13221
        DataManager connection = masterPage.getCurrentModelElement().getContext().getConnection();
        Connection tdProvider = null;
        if (connection != null) {
            tdProvider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(connection);
        }

        if (indicators != null && indicators.length != 0 && tdProvider == null) {
            tdProvider = ModelElementIndicatorHelper.getTdDataProvider(indicators[0]);
        }

        if (tdProvider != null) {
            setConnectionState(masterPage, tdProvider);
        }
    }

    /**
     * 
     * ADD mzhao 2009-05-05 bug:6587.
     */
    protected void updateBindConnection(AbstractAnalysisMetadataPage masterPage, TableIndicator[] indicators, Tree tree) {
        Connection tdProvider = null;
        if (indicators != null && indicators.length != 0) {
            tdProvider = ConnectionHelper.getDataProvider(SwitchHelpers.COLUMN_SET_SWITCH.doSwitch(indicators[0].getColumnSet()));
            setConnectionState(masterPage, tdProvider);
        }
    }

    /**
     * 
     * ADD mzhao 2009-05-05 bug:6587.
     */
    protected void updateBindConnection(AbstractAnalysisMetadataPage masterPage, Tree tree) {
        if (!isAnalyzedColumnsEmpty(tree)) {
            DataManager dataManager = masterPage.getCurrentModelElement().getContext().getConnection();
            if (dataManager == null) {
                if (tree.getData() instanceof AnalysisColumnNominalIntervalTreeViewer) {
                    AnalysisColumnNominalIntervalTreeViewer treeViewer = (AnalysisColumnNominalIntervalTreeViewer) tree.getData();
                    RepositoryNode repositoryNode = treeViewer.getColumnSetMultiValueList().get(0);
                    dataManager = ((ConnectionItem) repositoryNode.getObject().getProperty().getItem()).getConnection();
                } else if (tree.getData() instanceof AnalysisColumnSetTreeViewer) {
                    AnalysisColumnSetTreeViewer treeViewer = (AnalysisColumnSetTreeViewer) tree.getData();
                    IRepositoryNode reposNode = treeViewer.getColumnSetMultiValueList().get(0);
                    dataManager = ((ConnectionItem) reposNode.getObject().getProperty().getItem()).getConnection();
                }
            }
            setConnectionState(masterPage, dataManager);
        }
    }

    /**
     * ADD mzhao 2009-05-05 bug:6587.
     */
    protected void updateBindConnection(AbstractAnalysisMetadataPage masterPage, List<TableViewer> tableViewerPosStack) {
        boolean isEmpty1 = tableViewerPosStack.get(0) == null || tableViewerPosStack.get(0).getInput() == null
                || ((List) tableViewerPosStack.get(0).getInput()).size() == 0;
        boolean isEmpty2 = tableViewerPosStack.get(1) == null || tableViewerPosStack.get(1).getInput() == null
                || ((List) tableViewerPosStack.get(1).getInput()).size() == 0;
        if (isEmpty1 && isEmpty2) {
            return;
        } else {
            TableViewer columnsElementViewer = null;
            if (!isEmpty1) {
                columnsElementViewer = tableViewerPosStack.get(0);
            } else {
                columnsElementViewer = tableViewerPosStack.get(1);
            }
            Connection tdProvider = null;
            Object input = columnsElementViewer.getInput();

            List<DBColumnRepNode> columnSet = (List<DBColumnRepNode>) input;
            if (columnSet != null && columnSet.size() != 0) {
                TdColumn column = (TdColumn) ((MetadataColumnRepositoryObject) columnSet.get(0).getObject()).getTdColumn();

                if (column != null && column.eIsProxy()) {
                    column = (TdColumn) EObjectHelper.resolveObject(column);
                }
                tdProvider = ConnectionHelper.getTdDataProvider(column);
                setConnectionState(masterPage, tdProvider);
            }
        }
    }

    private void setConnectionState(final AbstractAnalysisMetadataPage masterPage, final DataManager dataManager) {
        DataManager newDataManager = dataManager;
        final DataManager fianlDataManager;
        if (newDataManager != null) {
            if (newDataManager.eIsProxy()) {
                newDataManager = (DataManager) EObjectHelper.resolveObject(newDataManager);
            }
            Property prop = PropertyHelper.getProperty(newDataManager);
            // MOD gdbu 2011-8-15 bug : TDQ-3213
            masterPage.reloadDataproviderAndFillConnCombo();
            fianlDataManager = newDataManager;

            // MOD yyin 201204 TDQ-4977
            Integer index = null;

            // use property.getLabel() instead of dataManager.getDisplayName() because of we set it use first one for
            // TDQ-6286.

            boolean isConnectionAvailble = !(prop == null);

            // MOD qiongli 2011-1-7 delimitedFile connection dosen't use 'dataManager.getName()'.
            if (SwitchHelpers.CONNECTION_SWITCH.doSwitch(newDataManager) != null) {
                // TDQ-10765: support ref project connection name, make the format of display is: label+(@reference
                // project name)
                DQRepositoryNode repNode = (DQRepositoryNode) RepositoryNodeHelper.recursiveFind(prop);
                if (repNode != null) {
                    String displayName = RepositoryNodeHelper.getAnalysisConComboDisplayName(repNode);
                    index = (Integer) masterPage.getConnCombo().getData(
                            displayName + RepositoryNodeHelper.getConnectionType(newDataManager));
                } else {
                    // when the current project(maybe local project) have no ref project, can not find the ref connection
                    isConnectionAvailble = false;
                }
            }
            if (index != null) {
                masterPage.getConnCombo().select(index);
            }

            // MOD qiongli 2011-5-16 bug 21453
            if (prop != null && prop.getItem() != null && prop.getItem().getState() != null) {
                masterPage.getLabelConnDeleted().setVisible(true);
                if (prop.getItem().getState().isDeleted()) {
                    masterPage.getLabelConnDeleted().setText(
                            DefaultMessagesImpl.getString("AbstractPagePart.LogicalDeleteWarn", prop.getDisplayName()));//$NON-NLS-1$
                } else {
                    if (!isConnectionAvailble) {
                        // when the connection is from ref project, but current project have not set ref project
                        masterPage.getLabelConnDeleted().setText(
                                DefaultMessagesImpl.getString("AbstractPagePart.ChangeConnectionError1", prop.getDisplayName()));//$NON-NLS-1$
                    }
                }
            } else {
                masterPage.getLabelConnDeleted().setVisible(false);
                // when the connection is from ref project, but current project have not set ref project
                if (!isConnectionAvailble) {
                    masterPage.getLabelConnDeleted().setVisible(true);
                    masterPage.getLabelConnDeleted().setText(
                            DefaultMessagesImpl.getString(
                                    "AbstractPagePart.ChangeConnectionError1", EObjectHelper.getURI(newDataManager).path()));//$NON-NLS-1$
                }
            }

            // MOD mzhao 2009-06-09 feature 5887
            if (selectionListener == null) {
                selectionListener = new SelectionListener() {

                    private int prevSelect = masterPage.getConnCombo().getSelectionIndex();

                    private DataManager dataProvider = fianlDataManager;

                    public void widgetDefaultSelected(SelectionEvent e) {
                        widgetSelected(e);
                    }

                    public void widgetSelected(SelectionEvent e) {
                        ReturnCode rc = selectedObjectAvailable();
                        if (rc.isOk()) {
                            // Added TDQ-7327 20130523 yyin: to resolve the proxy connection which is caused by the
                            // refresh of the db connection tree
                            if (dataProvider.eIsProxy()) {
                                dataProvider = (Connection) EObjectHelper.resolveObject(dataProvider);
                            }// ~
                            Connection connection = null;
                            if (dataProvider instanceof RepositoryNode) {
                                connection = ((ConnectionItem) ((RepositoryNode) dataProvider).getObject().getProperty()
                                        .getItem()).getConnection();
                            } else if (dataProvider instanceof Connection) {
                                connection = (Connection) dataProvider;
                            }
                            connection = callChangeConnectionAction(masterPage, prevSelect, connection);
                            prevSelect = masterPage.getConnCombo().getSelectionIndex();
                        } else {
                            // show error message
                            MessageDialogWithToggle
                                    .openError(
                                            null,
                                            DefaultMessagesImpl.getString("AbstractPagePart.ChangeConnectionTitle"), DefaultMessagesImpl.getString("AbstractPagePart.ChangeConnectionError1", rc.getMessage())); //$NON-NLS-1$ //$NON-NLS-2$
                            // reload the connection combo
                            masterPage.reloadDataproviderAndFillConnCombo();
                            // reselect the old connection
                            masterPage.getConnCombo().removeSelectionListener(selectionListener);
                            String uuid = ResourceHelper.getUUID(dataProvider);
                            if (uuid != null) {
                                int itemCount = masterPage.getConnCombo().getItemCount();
                                for (int i = 0; i < itemCount; i++) {
                                    Object connectionObj = masterPage.getConnCombo().getData(String.valueOf(i));
                                    RepositoryNode repoNode = (RepositoryNode) connectionObj;
                                    Connection connection = getConnectionFromRepositoryNode(repoNode);
                                    if (connection != null && uuid.endsWith(ResourceHelper.getUUID(connection))) {
                                        masterPage.getConnCombo().select(i);
                                        break;
                                    }
                                }
                            }
                            masterPage.getConnCombo().addSelectionListener(selectionListener);
                        }
                    }

                    /**
                     * check the connection is available or not(the connection is exist and not proxy, don't check the
                     * connection).
                     * 
                     * @return a ReturnCode, the message is the connection label
                     */
                    private ReturnCode selectedObjectAvailable() {
                        ReturnCode rc = new ReturnCode("", Boolean.FALSE); //$NON-NLS-1$
                        RepositoryNode repoNode = masterPage.getConnComboSelectNode();
                        rc.setMessage(repoNode.getLabel());
                        Connection connection = getConnectionFromRepositoryNode(repoNode);
                        if (connection != null && !connection.eIsProxy()) {
                            rc.setOk(true);
                        }
                        return rc;
                    }

                    /**
                     * DOC xqliu Comment method "getConnectionFromRepositoryNode".
                     * 
                     * @param repoNode
                     * @return
                     */
                    private Connection getConnectionFromRepositoryNode(RepositoryNode repoNode) {
                        Connection connection = null;
                        IRepositoryViewObject repoViewObject = repoNode.getObject();
                        if (repoViewObject != null) {
                            Property property = repoViewObject.getProperty();
                            if (property != null) {
                                Item item = property.getItem();
                                if (item != null && item instanceof ConnectionItem) {
                                    connection = ((ConnectionItem) item).getConnection();
                                }
                            }
                        }
                        return connection;
                    }
                };
                masterPage.getConnCombo().addSelectionListener(selectionListener);
            }
        } else {
            masterPage.getConnCombo().setText(DefaultMessagesImpl.getString("AbstractPagePart.unknown"));//$NON-NLS-1$
        }
    }

    // MOD mzhao 2009-06-09 feature 5887
    private Connection callChangeConnectionAction(AbstractAnalysisMetadataPage masterPage, final int oldSelect,
            Connection tdProvider) {
        Connection returnProvider = tdProvider;
        ChangeConnectionAction changeConnAction = new ChangeConnectionAction(masterPage, tdProvider);
        changeConnAction.run();
        ReturnCode ret = changeConnAction.getStatus();
        if (ret.isOk()) {
            IRunnableWithProgress op = new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            updateModelViewer();
                        }

                    });
                }
            };

            try {
                ProgressUI.popProgressDialog(op);
            } catch (InvocationTargetException e) {
                log.error(e, e);
            } catch (InterruptedException e) {
                log.error(e, e);
            }

            // The newest dataprovider now would be the old one for next
            // time connection changes.
            Object connObject = masterPage.getConnCombo().getData(masterPage.getConnCombo().getSelectionIndex() + ""); //$NON-NLS-1$
            if (connObject instanceof RepositoryNode) {
                returnProvider = ((ConnectionItem) ((RepositoryNode) connObject).getObject().getProperty().getItem())
                        .getConnection();
            } else if (connObject instanceof Connection) {
                returnProvider = (Connection) connObject;
            }
            // MOD mzhao bug 12766, 2010-04-22 save the editor automatically.
            masterPage.doSave(new NullProgressMonitor());
            //
            masterPage.updateAnalysisConnectionVersionInfo();
        } else {
            cancelSelection(masterPage, oldSelect);
        }
        return returnProvider;
    }

    private void cancelSelection(AbstractAnalysisMetadataPage masterPage, final int oldSelect) {
        masterPage.getConnCombo().removeSelectionListener(selectionListener);
        masterPage.getConnCombo().select(oldSelect);
        masterPage.getConnCombo().addSelectionListener(selectionListener);
    }

    public abstract void updateModelViewer();

    /**
     * 
     * ADD mzhao 2009-05-05 bug:6587.
     */
    private boolean isAnalyzedColumnsEmpty(Tree tree) {
        boolean isEmpty = false;
        if (tree == null || tree.getItemCount() == 0) {
            isEmpty = true;
        }
        return isEmpty;
    }

    /**
     * create the similar label for tree item
     * 
     * @param parent
     * @param imageName
     * @param tooltipTextName
     * @return Label
     */
    protected Label createTreeItemLabel(Tree parent, String imageName, String tooltipTextName) {
        Label label = new Label(parent, SWT.NONE);
        label.setBackground(parent.getBackground());
        label.setImage(ImageLib.getImage(imageName));
        label.setToolTipText(DefaultMessagesImpl.getString(tooltipTextName));
        label.pack();
        return label;
    }
}
