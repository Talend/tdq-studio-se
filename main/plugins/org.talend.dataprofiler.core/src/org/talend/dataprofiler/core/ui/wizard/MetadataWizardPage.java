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
package org.talend.dataprofiler.core.ui.wizard;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.emf.EmfHelper;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.constants.DevelopmentStatus;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.management.api.FolderProvider;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.dialog.FolderSelectionDialog;
import org.talend.dataprofiler.core.ui.filters.DQFolderFilter;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.model.bridge.ReponsitoryContextBridge;
import orgomg.cwm.objectmodel.core.CorePackage;

/**
 * @author zqin
 */
public abstract class MetadataWizardPage extends AbstractWizardPage {

    private static final int TEXT_FIELD_DEFAULT_MAX_CHAR_LIMIT = 200;

    public String getNameTextValue() {
        return nameText != null ? nameText.getText() : ""; //$NON-NLS-1$
    }

    // protected members
    protected Text nameText;

    protected Text purposeText;

    protected Text descriptionText;

    protected Text authorText;

    protected Text versionText;

    protected Button button;

    protected CCombo statusText;

    protected Text pathText;

    protected List<IRepositoryViewObject> existRepObjects;

    protected static Logger log = Logger.getLogger(MetadataWizardPage.class);

    protected boolean isTextValueValid = true;

    // private members
    // private Button versionMajorBtn;
    // private Button versionMinorBtn;

    public MetadataWizardPage() {
        this.setPageComplete(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.PropertiesWizardPage#createControl
     * (org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        if (getParameter().getFolderProvider() == null || getParameter().getFolderProvider().isNull()) {
            FolderProvider defaultFolder = new FolderProvider();
            defaultFolder.setFolderResource(getStoredFolder());
            getParameter().setFolderProvider(defaultFolder);
        }

        Composite container = new Composite(parent, SWT.NONE);
        GridLayout gdLayout = new GridLayout(2, false);
        container.setLayout(gdLayout);

        GridData data;

        // Name
        Label nameLab = new Label(container, SWT.NONE);
        nameLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.name")); //$NON-NLS-1$

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        // set the max number of characters to be entered in the text field
        // ADDED sgandon 16/03/2010 bug 11760
        nameText.setTextLimit(EmfHelper.getStringMaxSize(CorePackage.Literals.MODEL_ELEMENT__NAME,
                TEXT_FIELD_DEFAULT_MAX_CHAR_LIMIT));

        // Purpose
        Label purposeLab = new Label(container, SWT.NONE);
        purposeLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.purpose")); //$NON-NLS-1$

        purposeText = new Text(container, SWT.BORDER);
        purposeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        // set the max number of characters to be entered in the text field
        // ADDED sgandon 16/03/2010 bug 11760
        purposeText
                .setTextLimit(TaggedValueHelper.getStringMaxSize(TaggedValueHelper.PURPOSE, TEXT_FIELD_DEFAULT_MAX_CHAR_LIMIT));

        // Description
        Label descriptionLab = new Label(container, SWT.NONE);
        descriptionLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.description")); //$NON-NLS-1$
        descriptionLab.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        descriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 60;
        descriptionText.setLayoutData(data);
        // set the max number of characters to be entered in the text field
        descriptionText.setTextLimit(TaggedValueHelper.getStringMaxSize(TaggedValueHelper.DESCRIPTION,
                TEXT_FIELD_DEFAULT_MAX_CHAR_LIMIT));

        // Author
        Label authorLab = new Label(container, SWT.NONE);
        authorLab.setText(DefaultMessagesImpl.getString("MetadataWizardPage.author")); //$NON-NLS-1$

        authorText = new Text(container, SWT.BORDER);
        authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        authorText.setTextLimit(TaggedValueHelper.getStringMaxSize(TaggedValueHelper.AUTHOR, TEXT_FIELD_DEFAULT_MAX_CHAR_LIMIT));

        String author = ReponsitoryContextBridge.getAuthor();
        authorText.setText(author);
        getParameter().setAuthor(author);
        // MOD 2016-10-19 xqliu TDQ-12692 change the author field to read-only always
        // both TDQ and TOS for DQ, the author field should be read-only
        authorText.setEnabled(false);
        // Version
        // Label versionLab = new Label(container, SWT.NONE);
        // versionLab.setText("Version");
        //
        // Composite versionContainer = new Composite(container, SWT.NONE);
        // versionContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        // GridLayout versionLayout = new GridLayout(3, false);
        // versionLayout.marginHeight = 0;
        // versionLayout.marginWidth = 0;
        // versionLayout.horizontalSpacing = 0;
        // versionContainer.setLayout(versionLayout);
        //
        // versionText = new Text(versionContainer, SWT.BORDER);
        // versionText.setEnabled(false);
        // versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        // versionText.setText(getParameter().getVersion());
        //
        // versionMajorBtn = new Button(versionContainer, SWT.PUSH);
        // versionMajorBtn.setText("M");
        //
        // versionMinorBtn = new Button(versionContainer, SWT.PUSH);
        //        versionMinorBtn.setText("m"); //$NON-NLS-1$

        // Status
        Label statusLab = new Label(container, SWT.NONE);
        statusLab.setText("Status"); //$NON-NLS-1$

        statusText = new CCombo(container, SWT.BORDER);
        // statusText.setText(DevelopmentStatus.DRAFT.getLiteral());
        statusText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        statusText.setEditable(false);
        // MOD mzhao feature 7479 2009-10-12
        List<org.talend.core.model.properties.Status> statusList = MetadataHelper.getTechnicalStatus();

        if (statusList != null && statusList.size() > 0) {
            String[] tempString = new String[statusList.size()];
            statusText.setItems(MetadataHelper.toArray(statusList).toArray(tempString));
        } else {
            for (DevelopmentStatus status : DevelopmentStatus.values()) {
                statusText.add(status.getLiteral());
            }
        }
        statusText.select(0);
        getParameter().setStatus(statusText.getText());
        // Path:
        Label pathLab = new Label(container, SWT.NONE);
        pathLab.setText("Path"); //$NON-NLS-1$

        Composite pathContainer = new Composite(container, SWT.NONE);
        pathContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout pathLayout = new GridLayout(2, false);
        pathLayout.marginHeight = 0;
        pathLayout.marginWidth = 0;
        pathLayout.horizontalSpacing = 0;
        pathContainer.setLayout(pathLayout);

        pathText = new Text(pathContainer, SWT.BORDER);
        pathText.setEnabled(false);
        pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        button = new Button(pathContainer, SWT.PUSH);
        button.setText(DefaultMessagesImpl.getString("MetadataWizardPage.select")); //$NON-NLS-1$

        createExtendedControl(container);
        addListeners();

        setControl(container);
    }

    /**
     * DOC bZhou Comment method "openFolderSelectionDialog".
     * 
     * @param inputFolder
     */
    protected void openFolderSelectionDialog(IFolder inputFolder) {
        FolderSelectionDialog dialog = new FolderSelectionDialog(getShell());
        dialog.setTitle(DefaultMessagesImpl.getString("MetadataWizardPage.selectFolder")); //$NON-NLS-1$
        dialog.setMessage(DefaultMessagesImpl.getString("MetadataWizardPage.selectFolderItem")); //$NON-NLS-1$
        dialog.setInput(inputFolder);
        dialog.addFilter(new DQFolderFilter());

        if (dialog.open() == Window.OK) {
            if (dialog.getResult() == null || dialog.getResult().length == 0) {
                return;
            }
            Object elements = dialog.getResult()[0];
            IResource elem = (IResource) elements;
            if (elem instanceof IFolder) {
                pathText.setText(elem.getFullPath().toString());

                getParameter().getFolderProvider().setFolderResource((IFolder) elem);
            }
        }
    }

    protected void addListeners() {

        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getParameter().setName(nameText.getText());
                fireEvent();
            }
        });

        purposeText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getParameter().setPurpose(purposeText.getText());
            }
        });

        descriptionText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getParameter().setDescription(descriptionText.getText());
            }
        });

        authorText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getParameter().setAuthor(authorText.getText());
            }

        });

        // versionMajorBtn.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        // String version = getParameter().getVersion();
        // version = VersionUtils.upMajor(version);
        // versionText.setText(version);
        // getParameter().setVersion(version);
        // }
        // });
        //
        // versionMinorBtn.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        // String version = getParameter().getVersion();
        // version = VersionUtils.upMinor(version);
        // versionText.setText(version);
        // getParameter().setVersion(version);
        // }
        // });

        statusText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String selected = ((CCombo) e.getSource()).getText();
                getParameter().setStatus(selected);
            }

        });
    }

    @Override
    public boolean checkFieldsValue() {
        if (PluginConstant.EMPTY_STRING.equals(nameText.getText().trim())) {
            updateStatus(IStatus.ERROR, MSG_EMPTY);
            isTextValueValid = false;
            return false;
        }

        // reopen this commented code for TDQ-4593.
        if (!checkDuplicateName()) {
            isTextValueValid = false;
            return false;
        }

        updateStatus(IStatus.OK, MSG_OK);
        isTextValueValid = super.checkFieldsValue();
        return isTextValueValid;
    }

    protected abstract void createExtendedControl(Composite container);

    protected abstract IFolder getStoredFolder();

    protected void fireEvent() {
        setPageComplete(checkFieldsValue());
    }

    /**
     * DOC yyi Comment method "checkDuplicateModelName".
     * 
     * @return
     */
    public boolean checkDuplicateName() {
        String elementName = getParameter().getName();

        if (elementName == null) {
            updateStatus(IStatus.ERROR, MSG_EMPTY);
            return false;
        }
        // TDQ-5078,normalize some special chars with "_".
        elementName = WorkspaceUtils.normalize(elementName);
        // elementName=elementName.replaceAll("/", "");
        // MOD qionlgi 2012-2-13 TDQ-4593,check if exsit name in old items list when input a name.
        if (existRepObjects == null || existRepObjects.isEmpty()) {
            ERepositoryObjectType type = null;
            switch (getParameter().getParamType()) {
            case ANALYSIS:
                type = ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT;
                break;
            case REPORT:
                type = ERepositoryObjectType.TDQ_REPORT_ELEMENT;
                break;
            case PATTERN:
                type = ERepositoryObjectType.TDQ_PATTERN_ELEMENT;
                break;
            case DQRULE:
                type = ERepositoryObjectType.TDQ_RULES;
                break;
            case UDINDICATOR:
                type = ERepositoryObjectType.TDQ_USERDEFINE_INDICATORS;
                break;
            default:
                break;
            }
            try {
                if (type != null) {
                    existRepObjects = ProxyRepositoryFactory.getInstance().getAll(type, true, false);
                }
            } catch (PersistenceException e) {
                log.error(e);
            }
        }

        if (existRepObjects != null) {
            for (IRepositoryViewObject object : existRepObjects) {
                Property prop = object.getProperty();
                if (prop == null) {
                    continue;
                }
                if (elementName.equalsIgnoreCase(prop.getLabel()) || elementName.equalsIgnoreCase(prop.getDisplayName())) {
                    updateStatus(IStatus.ERROR,
                            DefaultMessagesImpl.getString("UIMessages.ItemExistsErrorWithParameter", prop.getLabel()));
                    return false;
                }
            }
        }

        return true;
    }
}
