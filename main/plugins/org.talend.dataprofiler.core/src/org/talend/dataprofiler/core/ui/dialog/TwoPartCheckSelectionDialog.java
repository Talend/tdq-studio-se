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
package org.talend.dataprofiler.core.ui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.ui.progress.UIJob;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.nodes.foldernode.ColumnFolderNode;
import org.talend.dataprofiler.core.model.nodes.foldernode.TableFolderNode;
import org.talend.dataprofiler.core.model.nodes.foldernode.ViewFolderNode;
import org.talend.dataprofiler.core.ui.editor.analysis.AbstractAnalysisMetadataPage;
import org.talend.dataprofiler.core.ui.filters.AbstractViewerFilter;
import org.talend.dataprofiler.core.ui.views.provider.ResourceViewContentProvider;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.DBCatalogRepNode;
import org.talend.dq.nodes.DBColumnRepNode;
import org.talend.dq.nodes.DBConnectionRepNode;
import org.talend.dq.nodes.DBSchemaRepNode;
import org.talend.dq.nodes.DBTableRepNode;
import org.talend.dq.nodes.DBViewRepNode;
import org.talend.dq.nodes.DQRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * A class to select elements out of a tree structure.
 * 
 * @since 2.0
 */
public abstract class TwoPartCheckSelectionDialog extends SelectionStatusDialog implements ISelectionChangedListener {

    private CheckboxTreeViewer fViewer;

    protected ILabelProvider fLabelProvider;

    protected ITreeContentProvider fContentProvider;

    private CheckboxTableViewer sTableViewer;

    protected ILabelProvider sLabelProvider;

    protected IStructuredContentProvider sContentProvider;

    private ISelectionStatusValidator fValidator = null;

    private ViewerComparator fComparator;

    private String fEmptyListMessage = DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.noEntriesAvailable"); //$NON-NLS-1$

    private IStatus fCurrStatus = new Status(IStatus.OK, PlatformUI.PLUGIN_ID, 0, "", null); //$NON-NLS-1$

    private List<ViewerFilter> fFilters;

    private Object fInput;

    private boolean fIsEmpty;

    private int fWidth = 60;

    private int fHeight = 18;

    protected boolean fContainerMode;

    private Object[] fExpandedElements;

    // ADD msjian 2011-7-8 feature 22206: Add filters
    private Text leftFilterText;

    private Text rightFilterText;

    private Label messageLabel;

    public Object[] getfExpandedElements() {
        return this.fExpandedElements;
    }

    public void setfExpandedElements(Object[] fExpandedElements) {
        this.fExpandedElements = fExpandedElements;
    }

    private AbstractAnalysisMetadataPage metadataFormPage;

    protected int dialogType;

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public static final int DIALOG_TYPE_COLUMN = 1;

    public static final int DIALOG_TYPE_TABLE = 2;

    protected IRepositoryNode selectedTreeRepoNode;

    private boolean addConnFilter = true;

    /**
     * Constructs an instance of <code>ElementTreeSelectionDialog</code>.
     * 
     * @param parent The shell to parent from.
     * @param labelProvider the label provider to render the entries
     * @param contentProvider the content provider to evaluate the tree structure
     */
    public TwoPartCheckSelectionDialog(AbstractAnalysisMetadataPage metadataFormPage, Shell parent, String message) {
        this(metadataFormPage, parent, message, true);
    }

    public TwoPartCheckSelectionDialog(AbstractAnalysisMetadataPage metadataFormPage, Shell parent, String message,
            boolean addConnFilter) {
        super(parent);
        setResult(new ArrayList(0));
        setStatusLineAboveButtons(true);
        fContainerMode = true;
        fExpandedElements = null;
        this.metadataFormPage = metadataFormPage;
        this.addConnFilter = addConnFilter;
        int shellStyle = getShellStyle();
        setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);
        setMessage(message);
        initProvider();
        // MOD mzhao 2009-05-05, bug 6587.
        if (addConnFilter) {
            addConnFilterListener();
        }

    }

    /**
     * Init the LabelProvider or ContentProvider for treeViewer and tableViewer.
     */
    protected abstract void initProvider();

    /**
     * If set, the checked /gray state of containers (inner nodes) is derived from the checked state of its leaf nodes.
     * 
     * @param containerMode The containerMode to set
     */
    public void setContainerMode(boolean containerMode) {
        fContainerMode = containerMode;
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
     * @deprecated since 3.3, use {@link TwoPartCheckSelectionDialog#setComparator(ViewerComparator)} instead
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
            fFilters = new ArrayList<ViewerFilter>(4);
        }
        fFilters.add(filter);
    }

    /**
     * Sets an optional validator to check if the selection is valid. The validator is invoked whenever the selection
     * changes.
     * 
     * @param validator the validator to validate the selection.
     */
    public void setValidator(ISelectionStatusValidator validator) {
        fValidator = validator;
    }

    /**
     * Sets the tree input.
     * 
     * @param input the tree input.
     */
    public void setInput(Object input) {
        fInput = input;
    }

    public void setOutput(Object output) {
        sTableViewer.setInput(output);
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
                updateStatus(fCurrStatus);
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

    private void parentCreate() {
        super.create();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#create()
     */
    @Override
    public void create() {
        BusyIndicator.showWhile(null, new Runnable() {

            public void run() {
                parentCreate();
                fViewer.setCheckedElements(getInitialElementSelections().toArray());
                fViewer.getTree().addTreeListener(new TreeAdapter() {

                    @Override
                    public void treeExpanded(TreeEvent e) {
                        TreeItem item = (TreeItem) e.item;
                        handleTreeExpanded(item);
                        super.treeExpanded(e);
                    }

                });

                updateOKStatus();
            }
        });
    }

    protected void handleTreeExpanded(TreeItem item) {
        if (!item.getText().endsWith(")")) { //$NON-NLS-1$
            Object obj = item.getData();

            if (obj instanceof TableFolderNode || obj instanceof ViewFolderNode || obj instanceof ColumnFolderNode) {
                item.setText(item.getText() + "(" + item.getItemCount() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                fViewer.getTree().layout();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets .Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        messageLabel = createMessageArea(composite);
        messageLabel.setText(this.getMessage());
        Composite twoPartComp = new Composite(composite, 0);
        GridLayout layout = new GridLayout(2, true);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        twoPartComp.setLayout(layout);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(twoPartComp);

        CheckboxTreeViewer treeViewer = createFirstPart(twoPartComp);
        sTableViewer = createSecondPart(twoPartComp);

        // ADD msjian 2011-7-8 feature 22206: Add filters
        // create the filter composite
        Control filterComposite = createFilterTexts(composite);
        // MOD klliu bug TDQ-4159 2012-03-07
        GridDataFactory.fillDefaults().hint(0, 30).applyTo(filterComposite);

        Control buttonComposite = createSelectionButtons(composite);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = convertWidthInCharsToPixels(fWidth);
        data.heightHint = convertHeightInCharsToPixels(fHeight);
        Tree treeWidget = treeViewer.getTree();
        treeWidget.setLayoutData(data);
        treeWidget.setFont(parent.getFont());
        if (fIsEmpty) {
            messageLabel.setEnabled(false);
            treeWidget.setEnabled(false);

            // ADD msjian 2011-7-8 feature 22206: Add filters
            filterComposite.setEnabled(false);

            buttonComposite.setEnabled(false);
        }
        addCheckedListener();
        // MOD mzhao bug 9240
        unfoldToCheckedElements();

        return composite;
    }

    /**
     * Add the checked listener for treeviewer or tableviewer.
     */
    protected abstract void addCheckedListener();

    /**
     * DOC mzhao 2009-05-05 bug: 6587, Add connection metadata filter.
     */

    private void addConnFilterListener() {
        AbstractViewerFilter connFiler = new AbstractViewerFilter() {

            @Override
            public int getId() {
                return 0;
            }

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof DBConnectionRepNode) {
                    DBConnectionRepNode node = (DBConnectionRepNode) element;
                    Integer selectIndex = metadataFormPage.getConnCombo().getSelectionIndex();
                    Integer connectionIndex = null;
                    Property property = (node.getObject()).getProperty();
                    ModelElement modelelement = PropertyHelper.getModelElement(property);
                    String dataKey = property.getDisplayName() + RepositoryNodeHelper.getConnectionType(node);
                    Object value = metadataFormPage.getConnCombo().getData(modelelement == null ? "" : dataKey); //$NON-NLS-1$
                    if (value != null && value instanceof Integer) {
                        connectionIndex = (Integer) value;
                    }
                    return (connectionIndex != null && selectIndex.intValue() == connectionIndex.intValue());
                }
                return true;
            }

        };
        addFilter(connFiler);
    }

    /**
     * Creates the tree viewer.
     * 
     * @param parent the parent composite
     * @return the tree viewer
     */
    protected CheckboxTreeViewer createFirstPart(Composite parent) {
        if (fContainerMode) {
            if (DIALOG_TYPE_TABLE == this.getDialogType()) {
                fViewer = new TableSelectionViewer(parent, SWT.BORDER);
            } else if (!this.addConnFilter) {
                fViewer = new MatchColumnSelectionViewer(parent, SWT.BORDER);
            } else {
                fViewer = new ColumnSelectionViewer(parent, SWT.BORDER);
            }
            // {
            //
            // protected void handleTreeExpand(TreeEvent event) {
            // super.handleTreeExpand(event);
            // checkElementChecked();
            // }
            // };
        } else {
            fViewer = new CheckboxTreeViewer(parent, SWT.BORDER) {

                @Override
                protected void handleTreeExpand(TreeEvent event) {
                    super.handleTreeExpand(event);
                    // checkElementChecked();
                }

            };
        }

        fViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
        applyDialogFont(fViewer.getTree());

        fViewer.setContentProvider(fContentProvider);
        // mod gdbu 2011-7-25 bug 23220
        ((ResourceViewContentProvider) fContentProvider).setTreeViewer(fViewer);
        // ~23220
        fViewer.setLabelProvider(fLabelProvider);
        fViewer.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                updateOKStatus();
            }
        });

        fViewer.setComparator(fComparator);
        if (fFilters != null) {
            for (int i = 0; i != fFilters.size(); i++) {
                fViewer.addFilter(fFilters.get(i));
            }
        }
        fViewer.setInput(fInput);
        // fViewer.
        fViewer.addSelectionChangedListener(this);
        return fViewer;
    }

    // protected void checkElementChecked() {
    // }

    protected CheckboxTableViewer createSecondPart(Composite parent) {
        CheckboxTableViewer viewer = CheckboxTableViewer.newCheckList(parent, SWT.MULTI | SWT.BORDER);
        viewer.setLabelProvider(this.sLabelProvider);
        viewer.setContentProvider(this.sContentProvider);
        viewer.addSelectionChangedListener(this);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(viewer.getTable());
        return viewer;
    }

    /**
     * Returns the tree viewer.
     * 
     * @return the tree viewer
     */
    protected CheckboxTreeViewer getTreeViewer() {
        return fViewer;
    }

    protected CheckboxTableViewer getTableViewer() {
        return sTableViewer;
    }

    /**
     * Adds the selection and deselection buttons to the dialog.
     * 
     * @param composite the parent composite
     * @return Composite the composite the buttons were created in.
     */
    protected Composite createSelectionButtons(Composite composite) {
        Composite buttonComposite = new Composite(composite, SWT.RIGHT);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        buttonComposite.setLayout(layout);
        buttonComposite.setFont(composite.getFont());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.GRAB_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        composite.setData(data);
        Button selectButton = createButton(buttonComposite, IDialogConstants.SELECT_ALL_ID,
                DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.SelectAll"), false); //$NON-NLS-1$

        Button deselectButton = createButton(buttonComposite, IDialogConstants.DESELECT_ALL_ID,
                DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.DeSelectAll"), false); //$NON-NLS-1$

        addSelectionButtonListener(selectButton, deselectButton);
        return buttonComposite;
    }

    /**
     * Add the listeners for all select(deselect) button.
     * 
     * @param selectButton
     * @param deselectButton
     */
    protected void addSelectionButtonListener(Button selectButton, Button deselectButton) {
        SelectionListener listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Object[] viewerElements = fContentProvider.getElements(fInput);
                if (fContainerMode) {
                    fViewer.setCheckedElements(viewerElements);
                } else {
                    for (Object viewerElement : viewerElements) {
                        fViewer.setSubtreeChecked(viewerElement, true);
                    }
                }
                updateOKStatus();
            }
        };
        selectButton.addSelectionListener(listener);

        listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                fViewer.setCheckedElements(new Object[0]);
                updateOKStatus();
            }
        };
        deselectButton.addSelectionListener(listener);
    }

    // ADD msjian 2011-7-8 feature 22206: Add filters
    /**
     * Adds the table and column filter texts to the dialog.
     * 
     * @param composite the parent composite
     * @return Composite the composite the filter texts were created in.
     */
    protected Composite createFilterTexts(Composite composite) {
        Composite filterTextsComposite = new Composite(composite, 0);
        GridLayout layout = new GridLayout(6, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        filterTextsComposite.setLayout(layout);
        filterTextsComposite.setFont(composite.getFont());
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(filterTextsComposite);

        Label leftFilterLabel = new Label(filterTextsComposite, SWT.NONE);
        if (DIALOG_TYPE_TABLE == this.getDialogType()) {
            leftFilterLabel.setText(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.SchemaCatalogFilter"));//$NON-NLS-1$
        } else {
            leftFilterLabel.setText(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.TableFilter"));//$NON-NLS-1$    
        }
        GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
        gd1.horizontalSpan = 1;
        gd1.grabExcessHorizontalSpace = false;
        leftFilterLabel.setLayoutData(gd1);

        leftFilterText = new Text(filterTextsComposite, SWT.BORDER | SWT.SINGLE);
        leftFilterText.setMessage(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.FilterMessage"));// $NON-NSL-1$
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.grabExcessHorizontalSpace = true;
        leftFilterText.setLayoutData(gd);

        Label rightFilterLabel = new Label(filterTextsComposite, SWT.NONE);
        if (DIALOG_TYPE_TABLE == this.getDialogType()) {
            rightFilterLabel.setText(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.TableFilter"));//$NON-NLS-1$    
        } else {
            rightFilterLabel.setText(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.ColumnFilter"));// $NON-NSL-1$
        }

        rightFilterText = new Text(filterTextsComposite, SWT.BORDER | SWT.SINGLE);
        rightFilterText.setMessage(DefaultMessagesImpl.getString("TwoPartCheckSelectionDialog.FilterMessage"));// $NON-NSL-1$
        rightFilterText.setLayoutData(gd);

        addFilterTextsListener();

        return filterTextsComposite;
    }

    /**
     * DOC msjian the FilterJob, use to run the filters.
     */
    class FilterJob extends UIJob {

        private String filter;

        private boolean isfViewer;

        /**
         * set the filter string.
         * 
         * @param text
         */
        public void setFilter(String text) {
            this.filter = text;
        }

        /**
         * Sets the viewer.
         * 
         * @param viewer (fViewer or sTableViewer)
         */
        public void setViewer(boolean isfViewer) {
            this.isfViewer = isfViewer;
        }

        public FilterJob(String name, String filter, boolean isfViewer) {
            super(name);
            this.filter = filter;
            this.isfViewer = isfViewer;
        }

        @Override
        public IStatus runInUIThread(final IProgressMonitor monitor) {
            getDisplay().asyncExec(new Runnable() {

                public void run() {

                    boolean save = DQRepositoryNode.isOnFilterring();
                    String value = DQRepositoryNode.getFilterStr();
                    boolean untilSchema = false;
                    boolean untilTable = false;

                    try {
                        if (DIALOG_TYPE_TABLE == getDialogType()) {
                            untilSchema = DQRepositoryNode.isUntilSchema();
                        } else if (isfViewer) {
                            untilTable = DQRepositoryNode.isUntilTable();
                        }

                        DQRepositoryNode.setFilterStr(filter);

                        if (filter.equals("")) { //$NON-NLS-1$
                            DQRepositoryNode.setFiltering(false);
                            if (isfViewer) {
                                fViewer.refresh();
                                restoreCheckStatus();
                            } else {
                                // ADD msjian 2011-7-27 22206: fixed note 93509 when cleared the filter, the selected
                                // turn to unselected
                                sTableViewer.refresh();
                            }
                            unfoldToCheckedElements();
                        } else {
                            DQRepositoryNode.setFiltering(true);
                            if (isfViewer) {
                                if (DIALOG_TYPE_TABLE == getDialogType()) {
                                    DQRepositoryNode.setUntilSchema(true);
                                } else {
                                    DQRepositoryNode.setUntilTable(true);
                                }
                                // MOD msjian 2011-8-24: TDQ-3282 because there is something changed in
                                // DQRepositoryNode.filterResultsIfAny
                                DQRepositoryNode.setIsReturnAllNodesWhenFiltering(false);
                                fViewer.refresh();
                                fViewer.expandAll();
                                DQRepositoryNode.setIsReturnAllNodesWhenFiltering(true);
                                restoreCheckStatus();
                            } else {
                                DQRepositoryNode.setIsReturnAllNodesWhenFiltering(false);
                                sTableViewer.refresh();
                                DQRepositoryNode.setIsReturnAllNodesWhenFiltering(true);
                                restoreTableCheckStatus();
                                // TDQ-3282 ~
                            }
                        }
                    } finally {
                        DQRepositoryNode.setFiltering(save);
                        DQRepositoryNode.setFilterStr(value);
                        if (isfViewer) {
                            DQRepositoryNode.setUntilTable(untilTable);
                        }
                        if (DIALOG_TYPE_TABLE == getDialogType()) {
                            DQRepositoryNode.setUntilSchema(untilSchema);
                        }
                    }

                    updateOKStatus();
                }

            });
            return Status.OK_STATUS;
        }
    }

    /**
     * restore the left viewer's check status.
     */
    protected abstract void restoreCheckStatus();

    /**
     * restore the right viewer's check status.
     */
    protected abstract void restoreTableCheckStatus();

    /**
     * Add the listeners for (table, column)filter texts.
     * 
     * @param leftFilterText
     * @param rightFilterText
     */
    protected void addFilterTextsListener() {
        final FilterJob job = new FilterJob("", leftFilterText.getText(), true);//$NON-NLS-1$
        ModifyListener listener1 = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                job.cancel();
                job.setFilter(leftFilterText.getText());
                // if there is no input in 500 milliseconds, run the job
                job.schedule(500);
            }
        };
        leftFilterText.addModifyListener(listener1);

        final FilterJob job2 = new FilterJob("", rightFilterText.getText(), false);//$NON-NLS-1$
        ModifyListener listener2 = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                job2.cancel();
                job2.setFilter(rightFilterText.getText());
                // if there is no input in 500 milliseconds, run the job
                job2.schedule(500);
            }
        };
        rightFilterText.addModifyListener(listener2);
    }

    private boolean evaluateIfTreeEmpty(Object input) {
        Object[] elements = fContentProvider.getElements(input);
        if (elements.length > 0) {
            if (fFilters != null) {
                for (int i = 0; i < fFilters.size(); i++) {
                    ViewerFilter curr = fFilters.get(i);
                    elements = curr.filter(fViewer, input, elements);
                }
            }
        }
        return elements.length == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionDialog#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {

        super.setMessage(message);
        if (messageLabel != null) {
            this.messageLabel.setText(message);
        }
    }

    /**
     * judge whether is hided node
     * 
     * @param selectNode
     * @return
     */
    protected boolean isHideNode(List<IRepositoryNode> nodeList) {
        for (IRepositoryNode selectNode : nodeList) {
            if (isHideNode(selectNode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * judge whether is hided node
     * 
     * @param selectNode
     * @return
     */
    protected boolean isHideNode(IRepositoryNode selectNode) {
        if (getRealNodeFromRepository(selectNode) == null) {
            return true;
        }
        return false;
    }

    /**
     * DOC talend Comment method "getRealNodeFromRepository".
     * 
     * @param selectNode
     * @return
     */
    protected IRepositoryNode getRealNodeFromRepository(IRepositoryNode selectNode) {
        IRepositoryNode returnNode = null;
        ModelElement currModelElement = null;
        if (selectNode.getObject() instanceof ISubRepositoryObject) {
            currModelElement = ((ISubRepositoryObject) selectNode.getObject()).getModelElement();
        }
        if (currModelElement != null) {
            returnNode = RepositoryNodeHelper.recursiveFind(currModelElement);
        }
        return returnNode;
    }

    /**
     * find the last one which is visible Real repository node.
     * 
     * @param reposNode
     */
    protected IRepositoryNode findLastVisibleNode(IRepositoryNode reposNode) {
        if (reposNode == null) {
            return null;
        }
        IRepositoryNode returnNode = getRealNodeFromRepository(reposNode);
        if (returnNode == null) {
            returnNode = findLastVisibleNode(reposNode.getParent());
        }
        return returnNode;
    }

    /**
     * when parentNode is catalogNode or SchemaNode we need to select tableFolderNode or ViewFolderNode or the left
     * treeView
     */
    protected IRepositoryNode getAdaptLocationNode(IRepositoryNode parentNode, IRepositoryNode childNode) {
        // to get TableFolderNode or viewFolderNode
        IRepositoryNode FolderNode = getContainedFolderNode(childNode);
        if (FolderNode == null || !(parentNode instanceof DBCatalogRepNode) && !(parentNode instanceof DBSchemaRepNode)) {
            return null;
        }
        if (childNode != null) {
            for (IRepositoryNode theFolderNode : parentNode.getChildren()) {
                if (theFolderNode.getLabel().equalsIgnoreCase(FolderNode.getLabel())) {
                    return theFolderNode;
                }
            }
        }
        return null;
    }

    /**
     * get TableFolderNode or ViewFolderNode.
     * 
     * @param childNode
     * @return
     */
    private IRepositoryNode getContainedFolderNode(IRepositoryNode childNode) {
        if (childNode instanceof DBColumnRepNode) {
            return childNode.getParent().getParent().getParent();
        } else if (childNode instanceof DBTableRepNode || childNode instanceof DBViewRepNode) {
            return childNode.getParent();
        }
        return null;
    }

    protected abstract void unfoldToCheckedElements();

}
