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
package org.talend.dataprofiler.core.ui.pref;

import java.io.File;
import java.sql.Driver;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.utils.loader.MyURLClassLoader;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.views.provider.DQRepositoryViewLabelProvider;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dq.CWMPlugin;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBConnectionFolderRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBConnectionSubFolderRepNode;
import org.talend.metadata.managment.ui.dialog.MappingFileSelectDialog;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.wizards.metadata.connection.database.SelectDatabaseJarDialog;
import org.talend.resource.EResourceConstant;

/**
 * 
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class SetJDBCDriverPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private static Logger log = Logger.getLogger(SetJDBCDriverPreferencePage.class);

    private Composite mainComposite;

    private Label header;

    private Label dbTypeLabel;

    private LabelledText generalJdbcUrlText = null;

    private LabelledText generalJdbcUserText = null;

    private LabelledText generalJdbcPasswordText = null;

    private LabelledCombo generalJdbcClassNameText = null;

    private LabelledText generalJdbcDriverjarText = null;

    private LabelledText generalMappingFileText = null;

    private Button generalMappingSelectButton = null;

    private Button browseJarFilesButton = null;

    private Button browseClassButton = null;

    private Button applyButton = null;

    private final String URL_KEY = "PREF_JDBC_URL";//$NON-NLS-1$

    private final String DRIVER_KEY = "PREf_JDBC_JAR";//$NON-NLS-1$

    private final String CLASSNAME_KEY = "PREF_JDBC_CLASS";//$NON-NLS-1$

    private final String USERNAME_KEY = "PREF_JDBC_USERNAME";//$NON-NLS-1$

    private final String PASSWORD_KEY = "PREF_JDBC_PASSWORD";//$NON-NLS-1$

    private final String MAPFILE_KEY = "PREF_JDBC_MAPFILE";//$NON-NLS-1$

    private final String dbType_jdbc = SupportDBUrlType.GENERICJDBCDEFAULTURL.getDBKey();

    public SetJDBCDriverPreferencePage() {
        super();
        IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

    public SetJDBCDriverPreferencePage(String title) {
        super(title);
    }

    public SetJDBCDriverPreferencePage(String title, ImageDescriptor image) {
        super(title, image);
    }

    public void init(IWorkbench workbench) {

    }

    @Override
    protected Control createContents(Composite parent) {
        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(new GridLayout(3, false));
        mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout2 = (GridLayout) mainComposite.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.horizontalSpan = 3;
        header = new Label(mainComposite, SWT.WRAP);
        header.setLayoutData(gridData);
        header.setText(DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.heard")); //$NON-NLS-1$
        dbTypeLabel = new Label(mainComposite, SWT.WRAP);
        dbTypeLabel.setText(DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.dbType") + dbType_jdbc); //$NON-NLS-1$
        dbTypeLabel.setLayoutData(gridData);

        generalJdbcUrlText = new LabelledText(mainComposite,
                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.general.url"), 2); //$NON-NLS-1$
        String url = getPreferenceStore().getString(URL_KEY);
        generalJdbcUrlText.setText(url != null ? url : PluginConstant.EMPTY_STRING);

        generalJdbcDriverjarText = new LabelledText(mainComposite,
                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.general.jarfile"), //$NON-NLS-1$
                1);
        String driver = getPreferenceStore().getString(DRIVER_KEY);
        generalJdbcDriverjarText.setText(driver != null ? driver : PluginConstant.EMPTY_STRING);

        browseJarFilesButton = new Button(mainComposite, SWT.NONE);
        browseJarFilesButton.setText("..."); //$NON-NLS-1$
        generalJdbcClassNameText = new LabelledCombo(mainComposite,
                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.general.classname"), "", null, 1, true, SWT.NONE); //$NON-NLS-1$

        String className = getPreferenceStore().getString(CLASSNAME_KEY);
        generalJdbcClassNameText.setText(className != null ? className : PluginConstant.EMPTY_STRING);
        browseClassButton = new Button(mainComposite, SWT.NONE);
        browseClassButton.setText("..."); //$NON-NLS-1$
        generalJdbcUserText = new LabelledText(mainComposite,
                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.general.user"), 2); //$NON-NLS-1$
        String userName = getPreferenceStore().getString(USERNAME_KEY);
        generalJdbcUserText.setText(userName != null ? userName : PluginConstant.EMPTY_STRING);
        generalJdbcPasswordText = new LabelledText(mainComposite,
                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.general.password"), 2); //$NON-NLS-1$
        generalJdbcPasswordText.getTextControl().setEchoChar('*'); // see
        String password = getPreferenceStore().getString(PASSWORD_KEY);
        generalJdbcPasswordText.setText(password != null ? password : PluginConstant.EMPTY_STRING);
        generalMappingFileText = new LabelledText(mainComposite,
                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.general.mapping"), 1); //$NON-NLS-1$
        String mapFile = getPreferenceStore().getString(MAPFILE_KEY);
        generalMappingFileText.setText(mapFile != null ? mapFile : PluginConstant.EMPTY_STRING);
        generalMappingSelectButton = new Button(mainComposite, SWT.NONE);
        generalMappingSelectButton.setText("..."); //$NON-NLS-1$
        applyButton = new Button(mainComposite, SWT.CENTER | SWT.BOTTOM);
        applyButton.setText(DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.selectConnectionButton")); //$NON-NLS-1$

        GridData gd = new GridData();
        gd.horizontalAlignment = GridData.CENTER;
        gd.horizontalSpan = 3;
        applyButton.setLayoutData(gd);
        applyButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                CheckedTreeSelectionDialog dialog = createConnSelectDialog();
                dialog.create();
                if (dialog.open() == Window.OK) {
                    Object selects[] = dialog.getResult();
                    if (selects != null && selects.length > 0) {
                        boolean isConfirm = MessageDialog.openConfirm(dialog.getShell(),
                                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.confirmTitle"), //$NON-NLS-1$
                                DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.confirmContent")); //$NON-NLS-1$
                        if (!isConfirm) {
                            return;
                        }
                        saveDatabases(selects);
                    }
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
        browseJarFilesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                SelectDatabaseJarDialog dialog = new SelectDatabaseJarDialog(getShell(), generalJdbcDriverjarText.getText());
                if (dialog.open() == Window.OK) {
                    generalJdbcDriverjarText.setText(dialog.getJarsString());
                }
            }

        });
        browseClassButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                generalJdbcClassNameText.removeAll();
                for (String stringToFile : generalJdbcDriverjarText.getText().trim().split(";")) { //$NON-NLS-1$
                    File file = new File(stringToFile);
                    if (file != null) {
                        try {
                            MyURLClassLoader cl = new MyURLClassLoader(file.toURL());
                            Class[] classes = cl.getAssignableClasses(Driver.class);
                            for (Class classe : classes) {
                                generalJdbcClassNameText.add(classe.getName());
                            }
                        } catch (Exception ex) {
                            ExceptionHandler.process(ex);
                        }
                    }
                }
                if (generalJdbcClassNameText.getItemCount() > 0) {
                    String driverClassName = generalJdbcClassNameText.getItem(0);
                    generalJdbcClassNameText.setText(driverClassName);
                }
            }
        });
        generalMappingSelectButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                MappingFileSelectDialog dialog = new MappingFileSelectDialog(getShell());
                dialog.open();
                generalMappingFileText.setText(dialog.getSelectId());
            }
        });
        CorePlugin.getDefault().handleUserReadOnlyStatus(mainComposite);
        return mainComposite;
    }

    @Override
    protected void performDefaults() {
        generalJdbcUrlText.setText(PluginConstant.EMPTY_STRING);
        generalJdbcDriverjarText.setText(PluginConstant.EMPTY_STRING);
        generalJdbcClassNameText.setText(PluginConstant.EMPTY_STRING);
        generalJdbcUserText.setText(PluginConstant.EMPTY_STRING);
        generalJdbcPasswordText.setText(PluginConstant.EMPTY_STRING);
        generalMappingFileText.setText(PluginConstant.EMPTY_STRING);
    }

    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(URL_KEY, generalJdbcUrlText.getText());
        getPreferenceStore().setValue(DRIVER_KEY, generalJdbcDriverjarText.getText());
        getPreferenceStore().setValue(CLASSNAME_KEY, generalJdbcClassNameText.getText());
        getPreferenceStore().setValue(USERNAME_KEY, generalJdbcUserText.getText());
        getPreferenceStore().setValue(PASSWORD_KEY, generalJdbcPasswordText.getText());
        getPreferenceStore().setValue(MAPFILE_KEY, generalMappingFileText.getText());
        return true;
    }

    private CheckedTreeSelectionDialog createConnSelectDialog() {
        RepositoryNode node = (RepositoryNode) RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.DB_CONNECTIONS);
        CheckedTreeSelectionDialog dialog = new CheckedTreeSelectionDialog(getShell(), new DQRepositoryViewLabelProvider(),
                new ResourceViewContentProvider());
        dialog.setInput(node);
        dialog.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof DBConnectionRepNode) {
                    return isJdbcConnectionNode((DBConnectionRepNode) element);
                } else if (element instanceof DBConnectionSubFolderRepNode) {
                    return hasJdbcConnNodeChild((DBConnectionSubFolderRepNode) element);
                }
                return false;
            }
        });
        dialog.setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                for (Object object : selection) {
                    if (object instanceof DBConnectionRepNode) {
                        IRepositoryViewObject nodeObject = ((DBConnectionRepNode) object).getObject();
                        // when it's locked, can not modify
                        if (nodeObject != null
                                && nodeObject.getProperty() != null
                                && nodeObject.getProperty().getItem() != null
                                && (nodeObject.getRepositoryStatus() == ERepositoryStatus.LOCK_BY_OTHER
                                        || nodeObject.getRepositoryStatus() == ERepositoryStatus.LOCK_BY_USER || RepositoryManager
                                            .isOpenedItemInEditor(nodeObject))) {

                            String displayName = nodeObject.getProperty().getDisplayName();
                            String version = nodeObject.getProperty().getVersion();
                            return new Status(IStatus.ERROR, CorePlugin.PLUGIN_ID, DefaultMessagesImpl.getString(
                                    "SetJDBCDriverPreferencePage.isLocked", displayName + " " + version)); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }
                }
                return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, IStatus.OK, "", //$NON-NLS-1$
                        null);
            }

        });
        dialog.setContainerMode(true);
        dialog.setTitle(DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.selectConnectionButton")); //$NON-NLS-1$
        dialog.setMessage(DefaultMessagesImpl.getString("SetJDBCDriverPreferencePage.ApplytoConnectionEditors")); //$NON-NLS-1$
        dialog.setSize(80, 30);
        return dialog;
    }

    /**
     * 
     * judge if has a jdbc connection child node under this parentNode
     * 
     * @param parrentNode
     * @return
     */
    private boolean hasJdbcConnNodeChild(IRepositoryNode parentNode) {
        if (parentNode != null
                && (parentNode instanceof DBConnectionFolderRepNode || parentNode instanceof DBConnectionSubFolderRepNode)) {
            List<IRepositoryNode> children = parentNode.getChildren(false);
            if (children.size() > 0) {
                for (IRepositoryNode inode : children) {
                    if (inode instanceof DBConnectionRepNode) {
                        if (isJdbcConnectionNode((DBConnectionRepNode) inode)) {
                            return true;
                        }
                    } else if (inode instanceof DBConnectionSubFolderRepNode) {
                        return hasJdbcConnNodeChild(inode);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * judge if has a jdbc connection node under this dbConnNode.
     * 
     * @param dbConnNode
     * @return
     */
    private boolean isJdbcConnectionNode(DBConnectionRepNode dbConnNode) {
        DatabaseConnection dbConn = dbConnNode.getDatabaseConnection();
        if (dbConn != null && dbType_jdbc.equalsIgnoreCase(dbConn.getDatabaseType())) {
            return true;
        }
        return false;
    }

    /**
     * 
     * persistence database setting.
     * 
     * @param selects
     * @return
     */
    private boolean saveDatabases(Object selects[]) {
        try {
            for (Object obj : selects) {
                if (obj instanceof DBConnectionRepNode) {
                    DBConnectionRepNode repNode = (DBConnectionRepNode) obj;
                    IRepositoryViewObject object = repNode.getObject();
                    if (object == null || object.getProperty() == null) {
                        continue;
                    }
                    Item item = object.getProperty().getItem();
                    if (item != null) {
                        ConnectionItem connItem = (ConnectionItem) item;
                        DatabaseConnection connection = (DatabaseConnection) connItem.getConnection();
                        connection.setDatabaseType(dbType_jdbc);
                        connection.setDriverClass(generalJdbcClassNameText.getText());
                        connection.setDriverJarPath(generalJdbcDriverjarText.getText());
                        connection.setURL(generalJdbcUrlText.getText());
                        connection.setUsername(generalJdbcUserText.getText());
                        connection.setRawPassword(generalJdbcPasswordText.getText());
                        String mapFileText = generalMappingFileText.getText();
                        if (mapFileText != null && !PluginConstant.EMPTY_STRING.equals(mapFileText.trim())) {
                            connection.setDbmsId(mapFileText);
                        }
                        ProxyRepositoryFactory.getInstance().save(item);
                        // notify SQLExplorer
                        CWMPlugin.getDefault().addConnetionAliasToSQLPlugin(connection);

                    }
                }
            }

            RepositoryNode node = (RepositoryNode) RepositoryNodeHelper.getMetadataFolderNode(EResourceConstant.DB_CONNECTIONS);
            CorePlugin.getDefault().refreshDQView(node);
        } catch (PersistenceException e) {
            log.error(e);
        }
        return true;
    }
}
