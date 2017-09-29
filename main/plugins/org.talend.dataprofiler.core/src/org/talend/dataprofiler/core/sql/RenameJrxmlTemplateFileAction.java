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
package org.talend.dataprofiler.core.sql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataquality.properties.TDQJrxmlItem;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class RenameJrxmlTemplateFileAction extends Action {

    protected static Logger log = Logger.getLogger(RenameSqlFileAction.class);

    private String newName;

    private ArrayList<String> existNames;

    private IRepositoryNode jrxmlNode;

    private IRepositoryNode parentNode;

    private TDQJrxmlItem jrxmlItem;

    public RenameJrxmlTemplateFileAction(RepositoryNode node) {
        setText(DefaultMessagesImpl.getString("RenameJrxmlFileAction.renameJrxmlFile")); //$NON-NLS-1$
        this.parentNode = node.getParent();
        this.jrxmlItem = (TDQJrxmlItem) node.getObject().getProperty().getItem();
        this.jrxmlNode = node;
    }

    @Override
    public void run() {
        if (jrxmlNode != null) {
            if (WorkspaceResourceHelper.sourceFileHasBeenOpened(jrxmlNode)) {
                MessageUI.openWarning(DefaultMessagesImpl.getString(
                        "RenameJrxmlFileAction.jrxmlFileOpening", jrxmlNode.getLabel())); //$NON-NLS-1$
                return;
            }
        }

        RenameDialog dialog = new RenameDialog(Display.getDefault().getActiveShell());
        existNames = new ArrayList<String>();
        getExistNames(parentNode, existNames);
        if (dialog.open() == RenameDialog.OK) {
            CorePlugin.getDefault().closeEditorIfOpened(jrxmlItem);
            try {
                String filenameBeforeRename = RepNodeUtils.getSeparator() + RepositoryNodeHelper.getFileNameOfTheNode(jrxmlNode);
                IPath path = RepositoryNodeHelper.getPath(jrxmlNode);
                // rename the Jrxml file
                renameJrxmlFile();

                // update the related reports which use it as user defined template
                String filenameAfterRename = RepNodeUtils.getSeparator() + RepositoryNodeHelper.getFileNameOfTheNode(jrxmlNode);
                RepNodeUtils.updateJrxmlRelatedReport(path.append(filenameBeforeRename), path.append(filenameAfterRename));

                CorePlugin.getDefault().refreshDQView(parentNode);
            } catch (PersistenceException e) {
                log.error(e, e);
            }
        }
    }

    /**
     * DOC yyin Comment method "renameJrxmlFile".
     * 
     * @throws PersistenceException
     */
    private void renameJrxmlFile() throws PersistenceException {
        Project project = ProjectManager.getInstance().getCurrentProject();

        Property property = jrxmlItem.getProperty();
        property.setDisplayName(newName);
        property.setLabel(WorkspaceUtils.normalize(newName));
        jrxmlItem.setName(newName);
        // XMIResourcemanager use file extension to fetch the item resource
        jrxmlItem.setFileExtension(FileConstants.JRXML_EXTENSION);
        jrxmlItem.setFilename(RepositoryNodeHelper.getFileNameOfTheNode(jrxmlNode));
        ProxyRepositoryFactory.getInstance().save(project, jrxmlItem);
    }

    private void getExistNames(IRepositoryNode parentNode, List<String> existNames) {
        List<IRepositoryNode> children = parentNode.getChildren();
        for (IRepositoryNode existNode : children) {
            if (existNode instanceof SourceFileSubFolderNode) {
                getExistNames(existNode, existNames);
            } else {
                existNames.add(existNode.getLabel());
            }
        }
    }

    class RenameDialog extends TitleAreaDialog {

        protected RenameDialog(Shell parentShell) {
            super(parentShell);
            setShellStyle(getShellStyle() | SWT.RESIZE);
        }

        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setSize(600, 200);
            newShell.setText(DefaultMessagesImpl.getString("RenameJrxmlFileAction.renameJrxmlFile")); //$NON-NLS-1$
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            Composite createDialogArea = (Composite) super.createDialogArea(parent);
            Composite composite = new Composite(createDialogArea, SWT.NONE);
            GridLayout gridLayout = new GridLayout(2, false);
            composite.setLayout(gridLayout);
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            composite.setLayoutData(gridData);
            Label label = new Label(composite, SWT.NONE);
            label.setText(DefaultMessagesImpl.getString("RenameSqlFileAction.setNewName")); //$NON-NLS-1$
            final Text text = new Text(composite, SWT.BORDER);

            text.setText(jrxmlNode.getLabel());
            newName = text.getText();
            text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            text.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    newName = text.getText();
                    if (newName.length() == 0) {
                        getButton(IDialogConstants.OK_ID).setEnabled(false);
                        setErrorMessage(DefaultMessagesImpl.getString("RenameSqlFileAction.sqlFileNameNotEmpty")); //$NON-NLS-1$
                    } else if (existNames.contains(newName)) {
                        getButton(IDialogConstants.OK_ID).setEnabled(false);
                        setErrorMessage(DefaultMessagesImpl.getString("RenameSqlFileAction.sqlFileAlwaysExist")); //$NON-NLS-1$
                    } else {
                        setErrorMessage(null);
                        getButton(IDialogConstants.OK_ID).setEnabled(true);
                    }
                }
            });
            return createDialogArea;
        }

        @Override
        protected void okPressed() {
            if (jrxmlNode.getLabel().equals(newName)) {
                getButton(IDialogConstants.OK_ID).setEnabled(false);
                setErrorMessage(DefaultMessagesImpl.getString("RenameSqlFileAction.sqlFileAlwaysExist")); //$NON-NLS-1$
                return;
            }
            setReturnCode(OK);
            close();
        }
    }
}
