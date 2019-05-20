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
package org.talend.dataprofiler.core.ui.imex;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.properties.Property;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataprofiler.core.ui.imex.model.EImexType;
import org.talend.dataprofiler.core.ui.imex.model.EMessageType;
import org.talend.dataprofiler.core.ui.imex.model.IImportWriter;
import org.talend.dataprofiler.core.ui.imex.model.ImportMessage;
import org.talend.dataprofiler.core.ui.imex.model.ImportWriterFactory;
import org.talend.dataprofiler.core.ui.imex.model.ItemRecord;
import org.talend.dataprofiler.core.ui.progress.ProgressUI;
import org.talend.dataprofiler.core.ui.utils.DqFileUtils;
import org.talend.dataprofiler.core.ui.utils.ImportAndExportUtils;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.helper.ReportFileHelper;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ImportWizardPage extends WizardPage {

    private static final String SELECT_ARCHIVE_FILE = Messages.getString("ExportWizardPage.8"); //$NON-NLS-1$

    private static final String BROWSE = Messages.getString("ExportWizardPage.7"); //$NON-NLS-1$

    private static final String SELECT_ROOT_DIR = Messages.getString("ExportWizardPage.6"); //$NON-NLS-1$

    private static final String OVERWRITE_EXIST_ITEM = Messages.getString("ImportWizardPage.4"); //$NON-NLS-1$

    private CheckboxTreeViewer repositoryTree;

    private TableViewer errorsList;

    protected Button dirBTN, archBTN;

    private Button browseDirBTN, browseArchBTN;

    protected Button overwriteBTN;

    protected Text dirTxt, archTxt;

    protected IImportWriter writer;

    protected String basePath;

    private List<ImportMessage> errors = new ArrayList<ImportMessage>();

    private final String underlineStr = "_";//$NON-NLS-1$

    private final String subrepName = "subreports";//$NON-NLS-1$

    private static final String[] FILE_EXPORT_MASK = { "*.zip;*.tar;*.tar.gz", "*.*" }; //$NON-NLS-1$//$NON-NLS-2$

    public ImportWizardPage() {
        super(Messages.getString("ImportWizardPage.2")); //$NON-NLS-1$
        setMessage(Messages.getString("ImportWizardPage.3")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite top = new Composite(parent, SWT.NONE);
        top.setLayout(new GridLayout());
        top.setLayoutData(new GridData(GridData.FILL_BOTH));

        createSelectComposite(top);

        createRepositoryTree(top);

        createErrorsList(top);

        createOptionComposite(top);

        initControlState();

        addListeners();

        setControl(top);

    }

    /**
     * DOC bZhou Comment method "addListeners".
     */
    private void addListeners() {

        SelectionListener modeSwitchListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // clear the temp folder
                if (writer != null) {
                    try {
                        writer.clearTempFolder();
                    } catch (IOException e1) {
                        // Nothing needed
                    }
                }

                // keep do one time for every method when every time switch between two radios
                if (dirBTN == e.getSource()) {
                    setDirState(isDirState());
                }
                if (archBTN == e.getSource()) {
                    setArchState(!isDirState());
                }
            }
        };

        dirBTN.addSelectionListener(modeSwitchListener);

        archBTN.addSelectionListener(modeSwitchListener);

        ModifyListener populateListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                // clear the temp folder
                if (writer != null) {
                    try {
                        writer.clearTempFolder();
                    } catch (IOException e1) {
                        // Nothing needed
                    }
                }
                updateBasePath();
            }
        };

        dirTxt.addModifyListener(populateListener);

        archTxt.addModifyListener(populateListener);

        browseDirBTN.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String result = openDirectoryDialog();
                if (result != null) {
                    dirTxt.setText(result);
                }
            }

            private String openDirectoryDialog() {
                DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
                if (dirTxt.getText() != null) {
                    dialog.setFilterPath(dirTxt.getText());
                }

                return dialog.open();
            }
        });

        browseArchBTN.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String result = openFileDialog();
                if (result != null) {
                    archTxt.setText(result);
                }
            }

            private String openFileDialog() {
                FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell());
                dialog.setFilterExtensions(FILE_EXPORT_MASK);
                if (archTxt.getText() != null) {
                    dialog.setFileName(archTxt.getText());
                }

                return dialog.open();
            }
        });

        overwriteBTN.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // MOD msjian 2012-2-29 TDQ-4701: fixed another issue, when the user checked the overwrite checkbox, do
                // not do migration
                populateElement();
                checkforErrors();
                // TDQ-4701 ~
            }
        });

        repositoryTree.getTree().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem item = (TreeItem) e.item;
                ItemRecord record = (ItemRecord) item.getData();

                if (record.getFile().isFile()) {
                    // MOD qiongli TDQ-5368 only uncheck the client dependecy when uncheck an item,not supplier
                    // dependency.
                    boolean checked = item.getChecked();
                    if (checked) {
                        for (File file : record.getDependencySet()) {
                            ItemRecord findRecord = ItemRecord.findRecord(file);
                            if (findRecord != null) {
                                repositoryTree.setChecked(findRecord, checked);
                            }
                        }
                    } else {
                        ModelElement element = record.getElement();
                        List<ModelElement> dependencyClients;
                        if (element != null) {
                            dependencyClients = EObjectHelper.getDependencyClients(element);
                            ImportAndExportUtils.iterateUncheckClientDependency(dependencyClients, repositoryTree);
                        } else {// for context, the dependency should include related Connection
                            Property property = record.getProperty();
                            dependencyClients = ImportAndExportUtils.getContextClientDepend(property.getId());
                        }
                        if (!dependencyClients.isEmpty()) {
                            ImportAndExportUtils.iterateUncheckClientDependency(dependencyClients, repositoryTree);
                        }
                    }

                }

                populateElement();

                repositoryTree.refresh();

                checkforErrors();
            }
        });
    }

    /**
     * 
     * Comment method "updateBasePath".
     */
    public void updateBasePath() {
        if (isDirState()) {
            basePath = dirTxt.getText();
        } else {
            basePath = archTxt.getText();
        }

        textModified(basePath);
    }

    /**
     * if the user select:overwrite, the conflick records become valid.otherwise, invalid when conflict
     */
    private void populateElement() {
        ItemRecord[] invalidRecords = writer.populate(getElements(), overwriteBTN.getSelection());
        updateErrorList(invalidRecords);

        if (invalidRecords.length > 0) {
            removeInvalidRecords(invalidRecords);
        }
    }

    /**
     * DOC bZhou Comment method "removeInvalidRecords".
     * 
     * @param invalidRecords
     */
    public void removeInvalidRecords(ItemRecord[] invalidRecords) {
        if (overwriteBTN.getSelection()) {
            return;
        }
        if (invalidRecords != null) {
            for (ItemRecord record : invalidRecords) {
                repositoryTree.setChecked(record, false);
            }

            repositoryTree.refresh();
        }
    }

    /**
     * DOC sgandon Comment method "dirTextModified".
     */
    protected void textModified(String pathStr) {
        IPath path = new Path(pathStr);

        if (path.toFile().exists()) {
            ItemRecord input = writer.computeInput(path);

            migrate();

            repositoryTree.setInput(input);

            repositoryTree.expandAll();
            TreeItem[] topItems = repositoryTree.getTree().getItems();
            for (TreeItem treeItem : topItems) {
                repositoryTree.setSubtreeChecked(treeItem.getData(), true);
            }
            repositoryTree.refresh();

        } else {
            repositoryTree.setInput(null);
        }

        populateElement();

        checkforErrors();
    }

    /**
     * DOC bZhou Comment method "migrate".
     */
    private void migrate() {
        IRunnableWithProgress op = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                writer.migration(monitor);
            }
        };

        try {
            ProgressUI.popProgressDialog(op);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * update the page state that is the finish button enable state according to the error message being present or not.
     */
    protected void updatePageStatus() {
        boolean valid = getErrorMessage() == null;
        valid = valid && (this.errors.size() == 0 || isOverWrite());
        setPageComplete(valid);
    }

    /**
     * check that directory exist and issue an error message if not. <br>
     * check that the folder is a data quality repository or issue an error.<br>
     * check that anything is check in the tree or issue an error.<br>
     */
    protected void checkforErrors() {
        List<String> dErrors = new ArrayList<String>();

        if (repositoryTree.getTree().getItems().length == 0) {
            dErrors.add(Messages.getString("ImportWizardPage.0")); //$NON-NLS-1$
        }

        if (repositoryTree.getCheckedElements().length == 0) {
            dErrors.add(Messages.getString("ImportWizardPage.1")); //$NON-NLS-1$
        }

        dErrors.addAll(writer.check());

        ItemRecord[] elements = getElements();

        boolean onlyWarnMessage = false;
        for (ItemRecord record : elements) {
            // only warn to show mean that no error message
            if (!onlyWarnMessage && record.onlyWarnToShow()) {
                onlyWarnMessage = true;
            }
            dErrors.addAll(record.getErrorMessage());
            for (File depFile : record.getDependencySet()) {
                ItemRecord findRecord = ItemRecord.findRecord(depFile);
                if (findRecord == null || !repositoryTree.getChecked(findRecord)) {

                    // TDQ-12410: if the dependency comes from reference project, we ingore it.
                    if (!DqFileUtils.isFileUnderBasePath(depFile, writer.getBasePath())) {
                        continue;
                    }

                    ModelElement element = ItemRecord.getElement(depFile);
                    // if the element is IndicatorDefinition and it exist in the current project and don't include any
                    // sql and java templates and the AggregatedDefinitions is not empty or TableOverview/ViewOverview
                    // Indicator, don't add it into errors even if it is not exist
                    if (element instanceof IndicatorDefinition) {
                        String uuid = ResourceHelper.getUUID(element);
                        if (IndicatorDefinitionFileHelper.isTechnialIndicator(uuid)) {
                            continue;
                        }
                    }

                    // MOD qiongli 2012-12-13 TDQ-5356 use itself file name for jrxml
                    boolean isJrxmlDepFile = depFile.getName().endsWith(FactoriesUtil.JRXML);
                    // MOD msjian TDQ-5909: modify to displayName
                    String dptLabel =
                            element != null && !isJrxmlDepFile && PropertyHelper.getProperty(element) != null ? PropertyHelper
                                    .getProperty(element)
                                    .getDisplayName() : depFile.getName();
                    // TDQ-5909~
                    dErrors.add("\"" + record.getName() + "\" miss dependency :" + dptLabel); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        if (!dErrors.isEmpty()) {
            setErrorMessage(dErrors.get(0));
        } else if (onlyWarnMessage && elements.length > 0) {
            setErrorMessage(null);
            this.setMessage(Messages.getString("ImportWizardPage.replaceWarn"), ERROR); //$NON-NLS-1$
        } else {
            setErrorMessage(null);
            // restore error message if no error here
            this.setMessage(StringUtils.EMPTY, NONE);
        }

        updatePageStatus();
    }

    /**
     * DOC bZhou Comment method "initControlState".
     */
    protected void initControlState() {
        setArchState(false);
        setPageComplete(false);

        this.writer = ImportWriterFactory.create(EImexType.FILE);
    }

    /**
     * DOC bZhou Comment method "createRepositoryTree".
     * 
     * @param top
     */
    private void createRepositoryTree(Composite top) {
        repositoryTree = new ContainerCheckedTreeViewer(top);
        repositoryTree.setContentProvider(new FileTreeContentProvider());
        repositoryTree.setLabelProvider(new FileTreeLabelProvider());
        repositoryTree.setInput(PluginConstant.EMPTY_STRING);

        GridDataFactory.fillDefaults().grab(true, true).applyTo(repositoryTree.getTree());
    }

    /**
     * DOC bZhou Comment method "createErrorsList".
     * 
     * @param top
     */
    private void createErrorsList(Composite top) {

        Group errorGroup = new Group(top, SWT.NONE);
        errorGroup.setLayout(new GridLayout());
        errorGroup.setText(Messages.getString("ImportWizardPage.5")); //$NON-NLS-1$

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 100;
        errorGroup.setLayoutData(gridData);

        errorsList = new TableViewer(errorGroup, SWT.BORDER);
        errorsList.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

        errorsList.setContentProvider(new IStructuredContentProvider() {

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            public Object[] getElements(Object inputElement) {
                return errors.toArray();
            }
        });

        errorsList.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                return element.toString();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
             */
            @Override
            public Image getImage(Object element) {
                ImportMessage imporMessage = (ImportMessage) element;
                if (EMessageType.WARN == imporMessage.getType()) {
                    return ImageLib.getImage(ImageLib.WARN_OVR);
                }
                return ImageLib.getImage(ImageLib.ICON_ERROR_INFO);
            }
        });

        errorsList.setInput(this);
        errorsList.setSorter(new ViewerSorter());

    }

    /**
     * DOC bZhou Comment method "createOptionComposite".
     * 
     * @param top
     */
    private void createOptionComposite(Composite top) {
        overwriteBTN = new Button(top, SWT.CHECK);
        overwriteBTN.setText(OVERWRITE_EXIST_ITEM);
        overwriteBTN.setToolTipText(OVERWRITE_EXIST_ITEM);
    }

    /**
     * DOC bZhou Comment method "setDirState".
     * 
     * @param state
     */
    protected void setDirState(boolean state) {
        dirTxt.setEnabled(state);
        browseDirBTN.setEnabled(state);

        if (state) {
            writer = ImportWriterFactory.create(EImexType.FILE);
            updateBasePath();
        }
    }

    /**
     * DOC bZhou Comment method "isDirState".
     * 
     * @return
     */
    public boolean isDirState() {
        return dirBTN.getSelection();
    }

    /**
     * DOC bZhou Comment method "setArchState".
     * 
     * @param state
     */
    protected void setArchState(boolean state) {
        archTxt.setEnabled(state);
        browseArchBTN.setEnabled(state);

        if (state) {
            writer = ImportWriterFactory.create(EImexType.ZIP_FILE);
            updateBasePath();
        }
    }

    /**
     * DOC bZhou Comment method "isArchState".
     * 
     * @return
     */
    public boolean isArchState() {
        return archBTN.getSelection();
    }

    /**
     * DOC bZhou Comment method "createSelectComposite".
     * 
     * @param top
     */
    private void createSelectComposite(Composite top) {
        Composite selectComp = new Composite(top, SWT.NONE);
        selectComp.setLayout(new GridLayout(3, false));
        selectComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        dirBTN = new Button(selectComp, SWT.RADIO);
        dirBTN.setText(SELECT_ROOT_DIR);
        dirBTN.setToolTipText(SELECT_ROOT_DIR);
        setButtonLayoutData(dirBTN);

        dirTxt = new Text(selectComp, SWT.BORDER);
        dirTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        browseDirBTN = new Button(selectComp, SWT.PUSH);
        browseDirBTN.setText(BROWSE);
        browseDirBTN.setToolTipText(BROWSE);
        setButtonLayoutData(browseDirBTN);

        archBTN = new Button(selectComp, SWT.RADIO);
        archBTN.setText(SELECT_ARCHIVE_FILE);
        archBTN.setToolTipText(SELECT_ARCHIVE_FILE);
        setButtonLayoutData(archBTN);

        archTxt = new Text(selectComp, SWT.BORDER);
        archTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        browseArchBTN = new Button(selectComp, SWT.PUSH);
        browseArchBTN.setText(BROWSE);
        browseArchBTN.setToolTipText(BROWSE);
        setButtonLayoutData(browseArchBTN);

    }

    /**
     * DOC bZhou Comment method "getElements".
     * 
     * @return
     */
    public ItemRecord[] getElements() {
        List<ItemRecord> itemRecords = new ArrayList<ItemRecord>();

        Object[] checkedElements = repositoryTree.getCheckedElements();
        for (Object obj : checkedElements) {
            if (obj instanceof ItemRecord) {
                ItemRecord record = (ItemRecord) obj;
                // MOD qiongli 2012-4-20,add related subreport jrxml/jasper file to elements.
                File file = record.getFile();
                if (file.isFile()) {
                    itemRecords.add(record);
                    if (file.getName().endsWith(FactoriesUtil.JRXML)) {
                        addSubRepToElements(record, itemRecords);
                    }
                    // if it is Mean Indicator, import it's dependency Sum Indicator also
                    String meanIndicatorUuid = "_ccI48RF2Ed2PKb6nEJEvhw"; //$NON-NLS-1$
                    ModelElement element = record.getElement();
                    if (element != null && element instanceof IndicatorDefinition
                            && meanIndicatorUuid.equals(ResourceHelper.getUUID(element))) {
                        File sumIndicatorFile = getSumIndicatorFile(element);
                        if (sumIndicatorFile != null) {
                            itemRecords.add(new ItemRecord(sumIndicatorFile, record.getRootFolderPath()));
                        }
                    }
                }
            }
        }
        return itemRecords.toArray(new ItemRecord[itemRecords.size()]);
    }

    /**
     * get the Sum Indicator File according to the Mean Indicator's ModelElement.
     * 
     * @param meanIndicator
     * @return
     */
    private File getSumIndicatorFile(ModelElement meanIndicator) {
        String sumIndicatorLabel = "Sum"; //$NON-NLS-1$
        File sumIndicatorFile = null;
        File meanIndicatorFile = new File(meanIndicator.eResource().getURI().toFileString());
        if (meanIndicatorFile.exists() && meanIndicatorFile.isFile()) {
            File parentFile = meanIndicatorFile.getParentFile();
            File[] listFiles = parentFile.listFiles();
            for (File listFile : listFiles) {
                if (listFile.isFile()) {
                    String fileName = listFile.getName();
                    if (!StringUtils.isBlank(fileName)) {
                        if (fileName.startsWith(sumIndicatorLabel) && fileName.endsWith(FactoriesUtil.DEFINITION)) {
                            sumIndicatorFile = listFile;
                            break;
                        }
                    }
                }
            }
        }
        return sumIndicatorFile;
    }

    /**
     * Getter for writer.
     * 
     * @return the writer
     */
    public IImportWriter getWriter() {
        return this.writer;
    }

    /**
     * DOC bZhou Comment method "updateErrorList".
     * 
     * @param records
     */
    public void updateErrorList(ItemRecord[] records) {
        errors.clear();

        for (ItemRecord record : records) {
            errors.addAll(record.getErrors());
        }

        errorsList.setInput(errors);
        errorsList.refresh();

    }

    /**
     * 
     * add related subReport jrxml/jasper file to List.
     * 
     * @param record
     * @param itemRecords
     */
    private void addSubRepToElements(ItemRecord record, List<ItemRecord> itemRecords) {
        File file = record.getFile();
        if (record.getProperty() == null) {
            return;
        }
        String version = record.getProperty().getVersion();
        String nameWithoutVersion =
                file.getName()
                        .replaceAll(underlineStr + version, PluginConstant.EMPTY_STRING)
                        .replaceAll(PluginConstant.DOT_STRING + FactoriesUtil.JRXML, PluginConstant.EMPTY_STRING);
        File parentFile = file.getParentFile().getParentFile();
        if (parentFile == null || !parentFile.exists()) {
            return;
        }
        File subRepFolder = new Path(parentFile.getPath() + IPath.SEPARATOR + subrepName).toFile();
        if (subRepFolder == null || !subRepFolder.exists()) {
            return;
        }
        Map<String, List<String>> mainSubRepMap = ReportFileHelper.getMainSubRepMap();
        String fName;
        for (File f : subRepFolder.listFiles()) {
            fName = f.getName();
            if (f.getName().equalsIgnoreCase(file.getName())) {
                continue;
            }
            // add the same name jasper file when the sub-report jrxml is checked on UI.
            if (fName.equalsIgnoreCase(nameWithoutVersion + PluginConstant.DOT_STRING + PluginConstant.JASPER_STRING)) {
                ItemRecord itemRecord = new ItemRecord(f, record.getRootFolderPath());
                itemRecords.add(itemRecord);
                continue;
            }
            // add sub-reports jrxml and jasper when the main-report is checked on UI.
            List<String> subLsFromMap = mainSubRepMap.get(nameWithoutVersion);
            if (subLsFromMap != null) {
                for (String name : subLsFromMap) {
                    if (fName.startsWith(name)) {
                        ItemRecord itemRecord = new ItemRecord(f, record.getRootFolderPath());
                        itemRecords.add(itemRecord);
                    }
                }
            }
        }
    }

    /**
     * Get the status of overwrite
     * 
     * @return true if all conflict element should be overwrite else false
     */
    public boolean isOverWrite() {
        return this.overwriteBTN.getSelection();
    }

}
