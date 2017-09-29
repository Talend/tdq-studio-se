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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.exception.ExceptionFactory;
import org.talend.dataprofiler.core.exception.ExceptionHandler;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.FolderSelectionDialog;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.properties.TDQSourceFileItem;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class RenameSqlFileAction extends Action {

    protected static Logger log = Logger.getLogger(RenameSqlFileAction.class);

    private String newName;

    private String newFolderPath;

    private ArrayList<String> existNames;

    private IPath filePath;

    private IRepositoryNode node;

    private IRepositoryNode parentNode;

    private TDQSourceFileItem sourceFiletem;

    /**
     * DOC qzhang AddSqlFileAction constructor comment.
     * 
     * @param folder
     */
    public RenameSqlFileAction(IFile folder) {
        setText(DefaultMessagesImpl.getString("RenameSqlFileAction.renameSQLFile")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.CREATE_SQL_ACTION));
        // this.folder = folder;
    }

    /**
     * DOC klliu RenameSqlFileAction constructor comment.
     * 
     * @param node
     */
    public RenameSqlFileAction(RepositoryNode node) {
        setText(DefaultMessagesImpl.getString("RenameSqlFileAction.renameSQLFile")); //$NON-NLS-1$
        setImageDescriptor(ImageLib.getImageDescriptor(ImageLib.CREATE_SQL_ACTION));
        this.filePath = WorkbenchUtils.getFilePath(node);
        this.parentNode = node.getParent();
        this.sourceFiletem = (TDQSourceFileItem) node.getObject().getProperty().getItem();
        this.node = node;
    }

    @Override
    public void run() {
        try {
            // ADD xqliu 2012-05-24 TDQ-4831
            if (this.node != null) {
                if (WorkspaceResourceHelper.sourceFileHasBeenOpened(this.node)) {
                    MessageUI.openWarning(DefaultMessagesImpl.getString(
                            "SourceFileAction.sourceFileOpening", this.node.getLabel())); //$NON-NLS-1$
                    return;
                }
            }
            if (!ResourceManager.getRootProject().exists(filePath)) {
                BusinessException createBusinessException = ExceptionFactory.getInstance().createBusinessException(
                        this.filePath.toFile().getName());
                throw createBusinessException;
            }
            // ~ TDQ-4831
            RenameDialog dialog = new RenameDialog(Display.getDefault().getActiveShell());
            existNames = new ArrayList<String>();
            getExistNames(parentNode, existNames);
            if (dialog.open() == RenameDialog.OK) {
                try {
                    CorePlugin.getDefault().closeEditorIfOpened(sourceFiletem);
                    Project project = ProjectManager.getInstance().getCurrentProject();
                    if (!isNeedToMove(newFolderPath)) {
                        renameSourceFile(project);
                    } else {
                        moveSourceFile(project, newFolderPath);
                    }
                    CorePlugin.getDefault().refreshDQView(parentNode);
                } catch (PersistenceException e) {
                    log.error(e);
                } catch (BusinessException e) {
                    log.error(e);
                }
            }
        } catch (BusinessException e) {
            ExceptionHandler.process(e, Level.FATAL);
        }
    }

    private void moveSourceFile(Project project, String newFolderPath) throws PersistenceException, BusinessException {
        renameSourceFile(project);
        List<IRepositoryNode> children = this.parentNode.getChildren();
        IPath path = new Path(newFolderPath);
        IPath targetPath = path.makeRelativeTo(ResourceManager.getSourceFileFolder().getProjectRelativePath());
        for (IRepositoryNode newNode : children) {
            if (newNode.getLabel().equals(newName)) {
                ProxyRepositoryFactory.getInstance().moveObject(this.node.getObject(), targetPath);
            }
        }
    }

    private void renameSourceFile(Project project) throws PersistenceException {
        CorePlugin.getDefault().closeEditorIfOpened(sourceFiletem);

        Property property = sourceFiletem.getProperty();
        property.setDisplayName(newName);
        property.setLabel(WorkspaceUtils.normalize(newName));
        sourceFiletem.setName(newName);
        sourceFiletem.setFileExtension(FileConstants.SQL_EXTENSION);
        sourceFiletem.setFilename(property.getLabel() + "_" + property.getVersion() + PluginConstant.DOT_STRING
                + PluginConstant.SQL_STRING);
        ProxyRepositoryFactory.getInstance().save(project, sourceFiletem);

        CorePlugin.getDefault().refreshDQView(parentNode);
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

    private boolean isNeedToMove(String newFolderPath) {
        String sourceFilePath = filePath.toString();
        if (newFolderPath != null && !sourceFilePath.equals(newFolderPath)) {
            return true;
        }
        return false;
    }

    /**
     * DOC qzhang RenameSqlFileAction class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
     * 
     */
    class RenameDialog extends TitleAreaDialog {

        /**
         * DOC qzhang RenameDialog constructor comment.
         * 
         * @param parentShell
         */
        protected RenameDialog(Shell parentShell) {
            super(parentShell);
            setShellStyle(getShellStyle() | SWT.RESIZE);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets .Shell)
         */
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setSize(300, 300);
            newShell.setText(DefaultMessagesImpl.getString("RenameSqlFileAction.renameSQLFileTwo")); //$NON-NLS-1$
        }

        private Text pathText;

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt .widgets.Composite)
         */
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

            text.setText(node.getLabel());
            newName = text.getText();
            text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            text.addModifyListener(new ModifyListener() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse .swt.events.ModifyEvent)
                 */
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
            Composite pathcomposite = new Composite(createDialogArea, SWT.NONE);
            gridLayout = new GridLayout(3, false);
            pathcomposite.setLayout(gridLayout);
            gridData = new GridData(GridData.FILL_HORIZONTAL);
            pathcomposite.setLayoutData(gridData);
            Label label2 = new Label(pathcomposite, SWT.NONE);
            label2.setText(DefaultMessagesImpl.getString("RenameSqlFileAction.path")); //$NON-NLS-1$
            pathText = new Text(pathcomposite, SWT.BORDER);
            pathText.setEnabled(false);
            pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            Button button = new Button(pathcomposite, SWT.PUSH);
            button.setText(DefaultMessagesImpl.getString("RenameSqlFileAction.select")); //$NON-NLS-1$

            pathText.setText(filePath.toString());
            pathText.addModifyListener(new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    String path = pathText.getText();
                    if (!filePath.toString().equals(path)) {
                        getButton(IDialogConstants.OK_ID).setEnabled(true);
                    }
                }
            });
            button.addSelectionListener(new SelectionAdapter() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org .eclipse.swt.events.SelectionEvent)
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {

                    ILabelProvider lp = new WorkbenchLabelProvider();
                    ITreeContentProvider cp = new WorkbenchContentProvider();

                    FolderSelectionDialog dialog = new FolderSelectionDialog(getShell(), lp, cp);
                    // dialog.setValidator(validator);
                    dialog.setTitle(DefaultMessagesImpl.getString("RenameSqlFileAction.selectFolder")); //$NON-NLS-1$
                    dialog.setMessage(DefaultMessagesImpl.getString("RenameSqlFileAction.selectFolderInItem")); //$NON-NLS-1$
                    dialog.setInput(ResourceManager.getSourceFileFolder());

                    dialog.setComparator(new ResourceComparator(ResourceComparator.NAME));

                    if (dialog.open() == Window.OK) {
                        Object elements = dialog.getResult()[0];
                        IResource elem = (IResource) elements;
                        if (elem instanceof IFolder) {
                            newFolderPath = elem.getProjectRelativePath().toString();
                            pathText.setText(newFolderPath);
                        }
                    }

                }
            });
            return createDialogArea;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.dialogs.Dialog#okPressed()
         */
        @Override
        protected void okPressed() {
            if (!isNeedToMove(newFolderPath)) {
                if (node.getLabel().equals(newName)) {
                    getButton(IDialogConstants.OK_ID).setEnabled(false);
                    setErrorMessage(DefaultMessagesImpl.getString("RenameSqlFileAction.sqlFileAlwaysExist")); //$NON-NLS-1$
                    return;
                }
            }
            setReturnCode(OK);
            close();

        }
    }
}
