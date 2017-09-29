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
package org.talend.dataprofiler.common.ui.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.RefreshAction;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.utils.loader.MyURLClassLoader;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.common.ui.i18n.Messages;
import org.talend.dq.helper.CustomAttributeMatcherHelper;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

/**
 * @author zshen
 * 
 */
public abstract class AbstractJarSelectDialog<T> extends SelectionStatusDialog {

    protected CheckboxTreeViewer fViewer;

    protected ILabelProvider fLabelProvider;

    protected ITreeContentProvider fContentProvider;

    private ISelectionStatusValidator fValidator = null;

    protected ViewerComparator fComparator;

    private String fEmptyListMessage = WorkbenchMessages.CheckedTreeSelectionDialog_nothing_available;

    private IStatus fCurrStatus = new Status(IStatus.OK, PlatformUI.PLUGIN_ID, 0, StringUtils.EMPTY, null);

    private List fFilters;

    protected Object fInput;

    private boolean fIsEmpty;

    private int fWidth = 60;

    private int fHeight = 18;

    protected boolean fContainerMode;

    protected Object[] fExpandedElements;

    private List<Object> elements;

    // ADD msjian 2011-7-14 22092 feature: Java UDI: not convinient to delete udi jar files
    protected boolean isSelectTab;

    private int tableFolderHeight = 25;

    private CheckboxTreeViewer fManageViewer;

    // ADD end

    protected Map<Object, Boolean> selectedJars;

    // ADD end

    protected String selectClassName = StringUtils.EMPTY;

    protected org.eclipse.swt.widgets.List jarList = null;

    private String checkValue = StringUtils.EMPTY;

    protected List<URL> listURL;

    /**
     * Constructs an instance of <code>ElementTreeSelectionDialog</code>.
     * 
     * @param parent The shell to parent from.
     * @param labelProvider the label provider to render the entries
     * @param contentProvider the content provider to evaluate the tree structure
     */
    public AbstractJarSelectDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent);
        fLabelProvider = labelProvider;
        fContentProvider = contentProvider;
        setResult(new ArrayList(0));
        setStatusLineAboveButtons(true);
        fContainerMode = false;
        fExpandedElements = null;
        // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used already.
        selectedJars = new HashMap<Object, Boolean>();
        fValidator = initfValidator();
    }

    /**
     * DOC zshen Comment method "initfValidator".
     * 
     * @return
     */
    protected ISelectionStatusValidator initfValidator() {
        return null;
    }

    /**
     * If set, the checked /gray state of containers (inner nodes) is derived from the checked state of its leaf nodes.
     * 
     * @param containerMode The containerMode to set
     */
    public void setContainerMode(boolean containerMode) {
        fContainerMode = containerMode;
    }

    /**
     * Sets the initial selection. Convenience method.
     * 
     * @param selection the initial selection.
     */
    public void setInitialSelection(Object selection) {
        setInitialSelections(new Object[] { selection });
    }

    /**
     * Sets the message to be displayed if the list is empty.
     * 
     * @param message the message to be displayed.
     */
    public void setEmptyListMessage(String message) {
        fEmptyListMessage = message;
    }

    /**
     * Sets the sorter used by the tree viewer.
     * 
     * @param sorter
     * @deprecated since 3.3, use {@link CheckedTreeSelectionDialog#setComparator(ViewerComparator)} instead
     */
    @Deprecated
    public void setSorter(ViewerSorter sorter) {
        fComparator = sorter;
    }

    /**
     * Sets the comparator used by the tree viewer.
     * 
     * @param comparator
     * @since 3.3
     */
    public void setComparator(ViewerComparator comparator) {
        fComparator = comparator;
    }

    /**
     * Adds a filter to the tree viewer.
     * 
     * @param filter a filter.
     */
    public void addFilter(ViewerFilter filter) {
        if (fFilters == null) {
            fFilters = new ArrayList(4);
        }
        fFilters.add(filter);
    }

    /**
     * Sets the tree input.
     * 
     * @param input the tree input.
     */
    public void setInput(Object input) {
        fInput = input;
    }

    /**
     * Expands elements in the tree.
     * 
     * @param elements The elements that will be expanded.
     */
    public void setExpandedElements(Object[] elements) {
        fExpandedElements = elements;
    }

    /**
     * Sets the size of the tree in unit of characters.
     * 
     * @param width the width of the tree.
     * @param height the height of the tree.
     */
    public void setSize(int width, int height) {
        fWidth = width;
        fHeight = height;
    }

    /**
     * Validate the receiver and update the status with the result.
     * 
     */
    protected void updateOKStatus() {
        if (!fIsEmpty) {
            if (fValidator != null) {
                fCurrStatus = fValidator.validate(fViewer.getCheckedElements());
            } else if (!fCurrStatus.isOK()) {
                fCurrStatus = new Status(IStatus.OK, PlatformUI.PLUGIN_ID, IStatus.OK, "", //$NON-NLS-1$
                        null);
            }
        } else {
            fCurrStatus = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, fEmptyListMessage, null);
        }
        updateStatus(fCurrStatus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#open()
     */
    @Override
    public int open() {
        fIsEmpty = evaluateIfTreeEmpty(fInput);
        super.open();
        return getReturnCode();
    }

    private void accessSuperCreate() {
        super.create();
    }

    /**
     * Handles cancel button pressed event.
     */
    @Override
    protected void cancelPressed() {
        setResult(null);
        super.cancelPressed();
    }

    /*
     * @see SelectionStatusDialog#computeResult()
     */
    @Override
    protected void computeResult() {
        setResult(Arrays.asList(fViewer.getCheckedElements()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#create()
     */
    @Override
    public void create() {
        BusyIndicator.showWhile(null, new Runnable() {

            @Override
            public void run() {
                accessSuperCreate();
                fViewer.setCheckedElements(getInitialElementSelections().toArray());
                // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used already.
                for (Object obj : getInitialElementSelections().toArray()) {
                    selectedJars.put(obj, true);
                }
                // ADD end
                if (fExpandedElements != null) {
                    fViewer.setExpandedElements(fExpandedElements);
                }
                updateOKStatus();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        // MOD msjian 2011-7-14 22092 feature: Java UDI: not convinient to delete udi jar files
        final TabFolder tabFolder = new TabFolder(composite, SWT.FILL);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = convertWidthInCharsToPixels(fWidth);
        data.heightHint = convertHeightInCharsToPixels(tableFolderHeight);
        tabFolder.setLayoutData(data);

        TabItem manageTabItem = new TabItem(tabFolder, SWT.FILL);
        manageTabItem.setText(Messages.getString("AbstractJarSelectDialog.ManageLib")); //$NON-NLS-1$
        final Composite manageComposite = new SelectJarCom(tabFolder, SWT.FILL, false, false);
        manageComposite.setLayout(new GridLayout());
        manageTabItem.setControl(manageComposite);

        TabItem selectTabItem = new TabItem(tabFolder, SWT.FILL | SWT.NO_SCROLL);
        selectTabItem.setText(Messages.getString("AbstractJarSelectDialog.SelectLib")); //$NON-NLS-1$
        final Composite selectComposite = createSelectComposite(tabFolder);
        selectTabItem.setControl(selectComposite);

        tabFolder.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (Messages.getString("AbstractJarSelectDialog.SelectLib").equals(tabFolder.getSelection()[0].toString())) { //$NON-NLS-1$
                    isSelectTab = true;
                } else {
                    isSelectTab = false;
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });

        tabFolder.setSelection(selectTabItem);
        isSelectTab = true;

        return composite;
    }

    /**
     * DOC zshen Comment method "createSelectComposite".
     */
    protected Composite createSelectComposite(Composite parent) {
        return new SelectJarCom(parent, SWT.FILL, true, true);
    }

    /**
     * the main Composite.
     */
    public class SelectJarCom extends Composite {

        /**
         * Create the composite.
         * 
         * @param parent
         * @param style
         * @param isSelect (if true, createTreeViewer, else createManageTreeViewer)
         */
        public SelectJarCom(Composite parent, int style, boolean isSelect, boolean needDisClassName) {
            super(parent, style);
            this.setBackground(parent.getBackground());
            this.setLayout(new GridLayout());
            SashForm contentSashForm = new SashForm(this, SWT.VERTICAL);
            contentSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
            contentSashForm.setLayout(new GridLayout());
            // Label messageLabel = createMessageArea(this);
            CheckboxTreeViewer treefViewer = null;
            if (isSelect) {
                treefViewer = createTreeViewer(contentSashForm);
            } else {
                treefViewer = createManageTreeViewer(contentSashForm);
            }
            if (needDisClassName) {
                createClassNameSelectCom(contentSashForm);
                initSelectStatus();
            }
            createButtons(this, isSelect);
            GridData data = new GridData(GridData.FILL_BOTH);
            // data.widthHint = convertWidthInCharsToPixels(fWidth);
            // data.heightHint = convertHeightInCharsToPixels(fHeight);
            Tree treeWidget = treefViewer.getTree();
            treeWidget.setLayoutData(data);
            treeWidget.setFont(this.getFont());
        }

        @Override
        protected void checkSubclass() {
            // nothing need to do here
        }
    }

    /**
     * If want to select class name need to implement this method
     * 
     * @param selectJarCom
     */
    protected Composite createClassNameSelectCom(Composite selectJarCom) {
        Composite contextCom = new Composite(selectJarCom, SWT.BORDER);
        GridData gd1 = new GridData(GridData.FILL_BOTH);
        contextCom.setLayoutData(gd1);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        contextCom.setLayout(gridLayout);

        jarList = new org.eclipse.swt.widgets.List(contextCom, SWT.V_SCROLL);
        jarList.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (jarList.getSelection().length > 0) {
                    selectClassName = jarList.getSelection()[0];
                    updateOKStatus();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // nothing need to do here
            }
        });
        List<String> inputElements = new ArrayList<String>();
        jarList.setItems(inputElements.toArray(new String[0]));
        GridData gd2 = new GridData(GridData.FILL_BOTH);
        jarList.setLayoutData(gd2);
        return contextCom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // nothing need to do here
    }

    /**
     * Creates the tree viewer.
     * 
     * @param parent the parent composite
     * @return the tree viewer
     */
    protected CheckboxTreeViewer createTreeViewer(Composite parent) {
        if (fContainerMode) {
            fViewer = new ContainerCheckedTreeViewer(parent, SWT.BORDER);
        } else {
            fViewer = new CheckboxTreeViewer(parent, SWT.BORDER);
        }
        fViewer.setContentProvider(fContentProvider);
        fViewer.setLabelProvider(fLabelProvider);
        fViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used already.
                handleChecked();
                handleClassNameFromJarFile();
                // ADD end
                updateOKStatus();
            }
        });
        fViewer.setComparator(fComparator);
        if (fFilters != null) {
            for (int i = 0; i != fFilters.size(); i++) {
                fViewer.addFilter((ViewerFilter) fFilters.get(i));
            }
        }
        fViewer.setInput(fInput);
        return fViewer;
    }

    // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used already.
    /**
     * DOC msjian Comment method "handleChecked".
     */
    public void handleChecked() {
        computeResult();
        selectedJars.clear();
        for (Object obj : getResult()) {
            selectedJars.put(obj, true);
        }

    }

    /**
     * add and remove class name when check or uncheck some jar file
     */

    protected void handleClassNameFromJarFile() {
        jarList.removeAll();
        listURL = new ArrayList<URL>();
        for (Object obj : getResult()) {
            if (obj instanceof File) {
                File file = (File) obj;
                if (file != null) {
                    try {
                        listURL.add(new URL(CustomAttributeMatcherHelper.FILEPROTOCOL, StringUtils.EMPTY, file.toURI().getPath()));
                    } catch (MalformedURLException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }
        }
        try {
            MyURLClassLoader urlClassLoader = new MyURLClassLoader(listURL.toArray(new URL[0]), this.getClass().getClassLoader());
            Class<Object>[] assignableClasses = urlClassLoader.getAssignableClasses(getSuperClass());
            for (Class<Object> customClass : assignableClasses) {
                jarList.add(customClass.getName());
            }
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
    }

    /**
     * DOC zshen Comment method "getSuperClass".
     * 
     * @return
     */
    abstract protected Class<T> getSuperClass();

    /**
     * DOC msjian Comment method "createManageTreeViewer".
     * 
     * @param parent
     * @return
     */
    protected CheckboxTreeViewer createManageTreeViewer(Composite parent) {
        if (fContainerMode) {
            fManageViewer = new ContainerCheckedTreeViewer(parent, SWT.BORDER);
        } else {
            fManageViewer = new CheckboxTreeViewer(parent, SWT.BORDER);
        }
        fManageViewer.setContentProvider(fContentProvider);
        fManageViewer.setLabelProvider(fLabelProvider);
        fManageViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                updateOKStatus();
            }
        });
        fManageViewer.setComparator(fComparator);
        if (fFilters != null) {
            for (int i = 0; i != fFilters.size(); i++) {
                fManageViewer.addFilter((ViewerFilter) fFilters.get(i));
            }
        }
        fManageViewer.setInput(fInput);
        return fManageViewer;
    }

    /**
     * Returns the tree viewer.
     * 
     * @return the tree viewer
     */
    protected CheckboxTreeViewer getTreeViewer() {
        // MOD msjian 2011-7-14 22092 feature: Java UDI: not convinient to delete udi jar files
        return isSelectTab ? fViewer : fManageViewer;
    }

    /**
     * Adds the selection and deselection buttons to the dialog.
     * 
     * @param composite the parent composite
     * @return Composite the composite the buttons were created in.
     */
    protected Composite createButtons(final Composite composite, final boolean isSelect) {
        Composite buttonComposite = new Composite(composite, SWT.RIGHT);
        buttonComposite.setBackground(composite.getBackground());
        GridLayout layout = new GridLayout();
        layout.numColumns = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        buttonComposite.setLayout(layout);
        buttonComposite.setFont(composite.getFont());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.GRAB_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        buttonComposite.setLayoutData(data);
        Button selectButton = createButton(buttonComposite, IDialogConstants.SELECT_ALL_ID,
                WorkbenchMessages.CheckedTreeSelectionDialog_select_all, false);
        SelectionListener listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Object[] viewerElements = fContentProvider.getElements(fInput);
                if (fContainerMode) {
                    if (isSelect) {
                        fViewer.setCheckedElements(viewerElements);
                        // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used
                        // already.
                        handleChecked();
                        handleClassNameFromJarFile();
                        // ADD end
                    } else {
                        fManageViewer.setCheckedElements(viewerElements);
                    }
                } else {
                    for (Object viewerElement : viewerElements) {
                        if (isSelect) {
                            fViewer.setSubtreeChecked(viewerElement, true);
                        } else {
                            fManageViewer.setSubtreeChecked(viewerElement, true);
                        }
                    }
                }
                updateOKStatus();
            }
        };
        selectButton.addSelectionListener(listener);

        Button deselectButton = createButton(buttonComposite, IDialogConstants.DESELECT_ALL_ID,
                WorkbenchMessages.CheckedTreeSelectionDialog_deselect_all, false);
        listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (isSelect) {
                    fViewer.setCheckedElements(new Object[0]);
                    // ADD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used
                    // already.
                    handleChecked();
                    handleClassNameFromJarFile();
                    // ADD end
                } else {
                    fManageViewer.setCheckedElements(new Object[0]);
                }
                updateOKStatus();
            }
        };
        deselectButton.addSelectionListener(listener);

        if (!isSelect) {
            final Composite dialogComposite = composite;

            Button addButton = createButton(buttonComposite, 22, Messages.getString("AbstractJarSelectDialog.add"), false); //$NON-NLS-1$
            SelectionListener listenerAdd = new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    FileDialog dialog = new FileDialog(dialogComposite.getShell(), SWT.NONE | SWT.MULTI);
                    dialog.setFilterExtensions(new String[] { "*.jar" }); //$NON-NLS-1$
                    String path = dialog.open();

                    if (path != null) {
                        String[] fileNames = dialog.getFileNames();
                        for (String name : fileNames) {
                            IPath filePath = new Path(path);
                            filePath = filePath.removeLastSegments(1).append(name);

                            // TDQ-7451 Replace File copy with eclipse IFile create.make svn could syn and control.
                            IFile targetFile = ResourceManager.getUDIJarFolder().getFile(filePath.lastSegment());
                            createIFileFromFile(filePath.toFile(), targetFile, getDescriptionMessage(name));
                        }
                    }
                    // MOD msjian 2011-7-14 22092 feature: Java UDI: not convinient to delete udi jar files
                    fViewer.refresh();
                    fManageViewer.refresh();
                    fViewer.setInput(ResourceManager.getUDIJarFolder());
                    fManageViewer.setInput(ResourceManager.getUDIJarFolder());

                    fIsEmpty = evaluateIfTreeEmpty(fInput);
                    // getTreeViewer().setCheckedElements(new Object[0]);
                    updateOKStatus();
                }

            };
            addButton.addSelectionListener(listenerAdd);

            Button delButton = createButton(buttonComposite, 23, Messages.getString("AbstractJarSelectDialog.delete"), //$NON-NLS-1$
                    false);
            SelectionListener listenerDel = new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    for (Object delFile : fManageViewer.getCheckedElements()) {
                        // Object delFile = manageSelectList.get(i);
                        if (delFile instanceof File) {
                            // MOD msjian 2011-8-9 TDQ-3199 fixed: Make it convenient to delete the jar which is used
                            // already.

                            ReturnCode rc = checkJarDependency((File) delFile);

                            if (selectedJars.containsKey(delFile)) {
                                rc.setOk(false);
                                rc.setMessage(getFileHasBeenSelectedMessages(delFile));
                            }
                            // MOD end
                            if (rc.isOk()) {
                                boolean delete = ((File) delFile).delete();
                                if (!delete) {
                                    MessageDialog.openWarning(getParentShell(),
                                            Messages.getString("AbstractJarSelectDialog.delete"), //$NON-NLS-1$
                                            Messages.getString("AbstractJarSelectDialog.deleteFail")); //$NON-NLS-1$
                                }
                            } else {
                                MessageDialog.openWarning(getParentShell(),
                                        Messages.getString("AbstractJarSelectDialog.warning"), rc.getMessage()); //$NON-NLS-1$
                            }
                        }
                        refreshWorkspace();
                    }
                    // MOD msjian 2011-7-14 22092 feature: Java UDI: not convinient to delete udi jar files
                    fViewer.refresh();
                    fManageViewer.refresh();
                    fIsEmpty = evaluateIfTreeEmpty(fInput);
                    updateOKStatus();
                }

            };
            delButton.addSelectionListener(listenerDel);
        } else {

            createOKButton(buttonComposite);

            Button cancelButton = createButton(buttonComposite, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
            SelectionListener listenerCancel = new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    cancelPressed();
                }
            };
            cancelButton.addSelectionListener(listenerCancel);
            // TDQ-3556 ~
        }

        return buttonComposite;
    }

    /**
     * DOC zshen Comment method "refreshWorkspace".
     */
    private void refreshWorkspace() {
        RefreshAction refreshAction = new RefreshAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        refreshAction.run();
    }

    /**
     * DOC zshen Comment method "getFileHasBeenSelectedMessages".
     * 
     * @param delFile
     * @return
     */
    protected String getFileHasBeenSelectedMessages(Object delFile) {
        return Messages.getString("AbstractJarSelectDialog.fileHasBeenSelected", ((File) delFile).getName()); //$NON-NLS-1$
    }

    /**
     * DOC zshen Comment method "getDescriptionMessage".
     * 
     * @param name
     * @return
     */
    protected String getDescriptionMessage(String name) {
        return Messages.getString("AbstractJarSelectDialog.addJarFile", name); //$NON-NLS-1$
    }

    /**
     * DOC zshen Comment method "createOKButton".
     */
    protected void createOKButton(Composite buttonComposite) {
        createButton(buttonComposite, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);

    }

    public abstract ReturnCode checkJarDependency(File delFile);

    /**
     * 
     * create a IFile from File inputStream.
     * 
     * @param sourceFile
     * @param targetIFile
     * @param message
     */
    public static void createIFileFromFile(File sourceFile, IFile targetIFile, String message) {
        final IFile ifile = targetIFile;
        final File srcFile = sourceFile;
        RepositoryWorkUnit<Object> workUnit = new RepositoryWorkUnit<Object>(message) {

            @Override
            protected void run() {
                try {
                    File targetfile = WorkspaceUtils.ifileToFile(ifile);
                    if (!targetfile.exists() || srcFile.lastModified() > targetfile.lastModified()) {
                        FileInputStream fileInputStream = new FileInputStream(srcFile);
                        ifile.create(fileInputStream, Boolean.TRUE, new NullProgressMonitor());
                        fileInputStream.close();
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);

    }

    private boolean evaluateIfTreeEmpty(Object input) {
        Object[] inputElements = fContentProvider.getElements(input);
        if (inputElements.length > 0) {
            if (fFilters != null) {
                for (int i = 0; i < fFilters.size(); i++) {
                    ViewerFilter curr = (ViewerFilter) fFilters.get(i);
                    inputElements = curr.filter(fViewer, input, inputElements);
                }
            }
        }
        return inputElements.length == 0;
    }

    public void setCheckedElements(String[] selectElements) {
        elements = new ArrayList<Object>();
        for (Object element : fContentProvider.getElements(this.fInput)) {
            if (element instanceof File) {
                for (String sel : selectElements) {
                    if (sel.equals(((File) element).getName())) {
                        elements.add(element);
                        break;
                    }
                }
            }
        }
        this.setInitialElementSelections(elements);
        CheckboxTreeViewer treeViewer = this.getTreeViewer();
        if (treeViewer != null) {
            treeViewer.setCheckedElements(elements.toArray());
        }
    }

    /**
     * 
     * get class name which be selected by user
     * 
     * @return this method will return the class name which user selected default, this value is StringUtils.EMPTY
     */
    public String getSelectClassName() {
        return selectClassName;
    }

    public String getSelectResult() {
        return selectClassName;
    }

    /**
     * Getter for oldValue.
     * 
     * @return the oldValue
     */
    public String getCheckValue() {
        return this.checkValue;
    }

    /**
     * Sets the oldValue.
     * 
     * @param oldValue the oldValue to set
     */
    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#initSelectStatus()
     */
    protected void initSelectStatus() {
        Object[] allJarFile = fContentProvider.getElements(fInput);
        for (Object jarFile : allJarFile) {
            if (jarFile instanceof File) {
                for (String jarName : spliteJarFile()) {
                    if (jarName.equals(((File) jarFile).getName())) {
                        fViewer.setChecked(jarFile, true);
                    }
                }
            }
        }
        handleChecked();
        handleClassNameFromJarFile();
        selectClassName();
    }

    /**
     * DOC zshen Comment method "spliteJarFile".
     * 
     * @return
     */
    protected abstract String[] spliteJarFile();

    /**
     * select class name on the jarlist view when init the dialog
     */
    protected void selectClassName() {
        String className = CustomAttributeMatcherHelper.getClassName(getCheckValue());
        int indexOf = jarList.indexOf(className);
        if (indexOf != -1) {
            jarList.select(indexOf);
            this.selectClassName = className;
        }
    }

}
