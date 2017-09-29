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
package net.sourceforge.sqlexplorer.plugin.editors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.views.navigator.ResourceComparator;
import org.talend.core.utils.WorkspaceUtils;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.utils.io.FilesUtils;

/**
 * 
 * this class PTODO qzhang fixed bug 3907.
 * 
 */
public class FolderSelectionDialog extends ElementTreeSelectionDialog implements ISelectionChangedListener {

    private static final String EMPTY_STR = ""; //$NON-NLS-1$

    private static final String SOURCE_FILES = "Source Files"; //$NON-NLS-1$

    private static final String TDQ_LIBRARIES = "TDQ_Libraries"; //$NON-NLS-1$

    // ADD xqliu 2010-03-08 feature 10675
    private static final String DEFAULT_FILE_EXTENSION = ".sql"; //$NON-NLS-1$

    // TODO should use the file version property
    private static final String DEFAULT_VERSION_STRING = "_0.1"; //$NON-NLS-1$

    private static final String LABEL_TEXT = Messages.getString("FolderSelectionDialog_1"); //$NON-NLS-1$

    private static final String DIALOG_MESSAGE = Messages.getString("FolderSelectionDialog_2"); //$NON-NLS-1$

    private static final String DIALOG_TITLE = Messages.getString("FolderSelectionDialog_3"); //$NON-NLS-1$

    private static final Status SELECT_FOLDER_ERROR_STATUS = new Status(IStatus.ERROR, SQLExplorerPlugin.PLUGIN_ID,
            Messages.getString("FolderSelectionDialog_6")); //$NON-NLS-1$

    private static final Status OK_STATUS = new Status(IStatus.OK, SQLExplorerPlugin.PLUGIN_ID, EMPTY_STR);

    private static final Status FILE_EXIST_STATUS = new Status(IStatus.ERROR, SQLExplorerPlugin.PLUGIN_ID,
            Messages.getString("FolderSelectionDialog_7")); //$NON-NLS-1$

    private static final Status SPECIAL_CHAR_STATUS = new Status(IStatus.ERROR, SQLExplorerPlugin.PLUGIN_ID,
            Messages.getString("FolderSelectionDialog_8")); //$NON-NLS-1$

    private static final Status EMPTY_NAME_STATUS = new Status(IStatus.ERROR, SQLExplorerPlugin.PLUGIN_ID,
            Messages.getString("FolderSelectionDialog_9")); //$NON-NLS-1$

    private String fileName = EMPTY_STR;

    private IFolder selectedFolder;

    private Text fileNameText;

    IFolder rootFolder = ReponsitoryContextBridge.getRootProject().getFolder(TDQ_LIBRARIES);

    final IFolder defaultValidFolder = rootFolder.getFolder(SOURCE_FILES);

    /**
     * qzhang FolderSelectionDialog constructor comment.
     * 
     * @param parent
     * @param labelProvider
     * @param contentProvider
     */
    public FolderSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);

        this.setTitle(DIALOG_TITLE);
        this.setMessage(DIALOG_MESSAGE);

        // MOD sizhaoliu 2012-04-05 TDQ-4958 NPE when save sql script
        this.setInput(rootFolder);
        this.setInitialSelection(defaultValidFolder);

        this.addFilter(new ViewerFilter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
             * java.lang.Object)
             */
            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof IFolder) {
                    IFolder folder = (IFolder) element;
                    if (SOURCE_FILES.equals(folder.getName())) {
                        return true;
                    } else {
                        return defaultValidFolder.getFullPath().isPrefixOf(folder.getFullPath())
                                && !FilesUtils.isSVNFolder(folder.getName());
                    }
                }
                return false;
            }
        });
        setValidator(new ISelectionStatusValidator() {

            public IStatus validate(Object[] selection) {
                if (selection.length == 1) {
                    if (selection[0] instanceof IFolder) {
                        selectedFolder = (IFolder) selection[0];
                        IPath projectRelativePath = selectedFolder.getProjectRelativePath();
                        if (SOURCE_FILES.equals(selectedFolder.getName())
                                || defaultValidFolder.getFullPath().isPrefixOf(selectedFolder.getFullPath())) {
                            return checkFileName(fileNameText.getText());
                        }
                    }
                }
                return SELECT_FOLDER_ERROR_STATUS;
            }

        });
        setComparator(new ResourceComparator(ResourceComparator.NAME));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite result = (Composite) super.createDialogArea(parent);

        // ADD xqliu 2010-03-08 feature 10675
        Composite fileNameComp = new Composite(result, SWT.NULL);
        fileNameComp.setLayout(new GridLayout(2, false));
        fileNameComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label label = new Label(fileNameComp, SWT.NULL);
        label.setText(LABEL_TEXT);
        fileNameText = new Text(fileNameComp, SWT.BORDER);
        fileNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fileNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                updateStatus(checkFileName(fileNameText.getText()));
            }
        });
        // ~10675

        getTreeViewer().addSelectionChangedListener(this);
        getTreeViewer().expandAll();
        applyDialogFont(result);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent
     * )
     */
    public void selectionChanged(SelectionChangedEvent event) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionStatusDialog#okPressed()
     */
    @Override
    protected void okPressed() {
        fileName = fileNameText.getText();
        super.okPressed();
    }

    /**
     * DOC xqliu Comment method "getFileName". ADD xqliu 2010-03-08 feature 10675
     * 
     * @return
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * check File Name.
     * 
     * @param String: file name
     */
    private Status checkFileName(final String name) {
        if (name == null || EMPTY_STR.equals(name.trim())) {
            return EMPTY_NAME_STATUS;
        } else {
            if (!WorkspaceUtils.checkNameIsOK(name)) {
                return SPECIAL_CHAR_STATUS;
            }
            // the source file can't have the same name even there under different sub folders
            if (fileNameExist(WorkspaceUtils.ifolderToFile(defaultValidFolder), name)) {
                return FILE_EXIST_STATUS;
            }
        }
        return OK_STATUS;
    }

    /**
     * check the fileName exist in the folder (recursive).
     * 
     * @param folder
     * @param fileName
     * @return
     */
    private boolean fileNameExist(File folder, String fileName) {
        boolean result = false;
        String realFileName = getRealFileName(fileName);
        List<File> files = new ArrayList<File>();
        getAllSqlFiles(files, folder);
        for (File file : files) {
            if (realFileName.equalsIgnoreCase(file.getName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * get the final source file name (with the version).
     * 
     * @param fileName source file name (without the version)
     * @return
     */
    private String getRealFileName(String fileName) {
        return fileName + DEFAULT_VERSION_STRING + DEFAULT_FILE_EXTENSION;
    }

    /**
     * get all sql files which under parentFile (recursive).
     * 
     * @param files
     * @param parentFile
     */
    private void getAllSqlFiles(List<File> files, File parentFile) {
        if (parentFile.isFile()) {
            if (parentFile.getName().endsWith(DEFAULT_FILE_EXTENSION)) {
                files.add(parentFile);
            }
        } else if (parentFile.isDirectory()) {
            File[] listFiles = parentFile.listFiles();
            for (File file : listFiles) {
                getAllSqlFiles(files, file);
            }
        }
    }
}
