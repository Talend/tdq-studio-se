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
package org.talend.dataprofiler.core.ui.editor.pattern;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.views.PatternTestView;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.domain.pattern.impl.RegularExpressionImpl;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.DomainHelper;
import org.talend.dataquality.properties.TDQPatternItem;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.nodes.PatternRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.dates.DateUtils;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC rli class global comment. Detailled comment
 */
public class PatternMasterDetailsPage extends AbstractMetadataFormPage implements PropertyChangeListener {

    private PatternRepNode patternRepNode;

    private Composite patternDefinitionSectionComp;

    private Composite componentsComp;

    private List<PatternComponent> tempPatternComponents;

    private List<String> allDBTypeList;

    private List<String> remainDBTypeList;

    private Section patternDefinitionSection;

    private String expressionType;

    /**
     * PatternMasterDetailsPage constructor.
     * 
     * @param editor
     * @param id
     * @param title
     */
    public PatternMasterDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /**
     * DOC rli Comment method "reset".
     */
    private void reset() {
        String[] supportTypes = PatternLanguageType.getAllLanguageTypesForPattern();
        allDBTypeList = new ArrayList<String>();
        allDBTypeList.addAll(Arrays.asList(supportTypes));
        if (tempPatternComponents == null) {
            tempPatternComponents = new ArrayList<PatternComponent>();
        } else {
            tempPatternComponents.clear();
        }
        remainDBTypeList = new ArrayList<String>();
        remainDBTypeList.addAll(allDBTypeList);
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternSettings")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternMetadata")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("PatternMasterDetailsPage.setProperties")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        creatPatternDefinitionSection(topComp);
    }

    private void creatPatternDefinitionSection(Composite topCmp) {
        patternDefinitionSection = createSection(form, topCmp,
                DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternDefinition"), null); //$NON-NLS-1$

        Label label = new Label(patternDefinitionSection, SWT.WRAP);
        label.setText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.text")); //$NON-NLS-1$
        patternDefinitionSection.setDescriptionControl(label);

        patternDefinitionSectionComp = createPatternDefinitionComp();

    }

    public void updatePatternDefinitonSection() {
        patternDefinitionSectionComp.dispose();
        reset();
        patternDefinitionSectionComp = createPatternDefinitionComp();
        patternDefinitionSection.layout();
        // patternDefinitionSectionComp.layout();
        form.reflow(true);
    }

    /**
     * DOC rli Comment method "ceatePatternDefinitionComp".
     * 
     * @param form
     * @param section
     */
    private Composite createPatternDefinitionComp() {
        Composite newComp = toolkit.createComposite(patternDefinitionSection);
        newComp.setLayout(new GridLayout());

        componentsComp = new Composite(newComp, SWT.NONE);
        componentsComp.setLayout(new GridLayout());

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(componentsComp);
        EList<PatternComponent> components = getCurrentModelElement().getComponents();
        for (int i = 0; i < components.size(); i++) {
            RegularExpression regularExpress = (RegularExpression) components.get(i);
            // RegularExpressionImpl newRegularExpress = (RegularExpressionImpl)
            // PatternFactory.eINSTANCE.createRegularExpression();
            // Expression newExpression = CoreFactory.eINSTANCE.createExpression();
            // newExpression.setBody(regularExpress.getExpression().getBody());
            // newExpression.setLanguage(regularExpress.getExpression().getLanguage());
            // newRegularExpress.setExpression(newExpression);
            tempPatternComponents.add(regularExpress);
            creatNewExpressLine(regularExpress);
        }
        createAddButton(newComp);

        patternDefinitionSection.setClient(newComp);
        return newComp;
    }

    private void createAddButton(Composite parent) {
        final Button addButton = new Button(parent, SWT.NONE);
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.add")); //$NON-NLS-1$
        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.CENTER;
        labelGd.widthHint = 65;
        addButton.setLayoutData(labelGd);
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                remainDBTypeList.clear();
                remainDBTypeList.addAll(allDBTypeList);
                for (PatternComponent patternComponent : tempPatternComponents) {
                    String language = ((RegularExpressionImpl) patternComponent).getExpression().getLanguage();
                    String languageName = PatternLanguageType.findNameByLanguage(language);
                    remainDBTypeList.remove(languageName);
                }
                if (remainDBTypeList.isEmpty()) {
                    MessageDialog.openWarning(
                            null,
                            DefaultMessagesImpl.getString("PatternMasterDetailsPage.warning"), DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternExpression")); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }

                String language = PatternLanguageType.findLanguageByName(remainDBTypeList.get(0));
                RegularExpression newRegularExpress = BooleanExpressionHelper.createRegularExpression(language, null);
                newRegularExpress.setExpressionType(expressionType);
                creatNewExpressLine(newRegularExpress);
                tempPatternComponents.add(newRegularExpress);
                patternDefinitionSection.setExpanded(true);
                setDirty(true);
            }
        });
    }

    private void creatNewExpressLine(RegularExpression regularExpress) {
        final Composite expressComp = new Composite(componentsComp, SWT.NONE);
        expressComp.setLayout(new GridLayout(10, false));
        final CCombo combo = new CCombo(expressComp, SWT.BORDER);
        combo.setEditable(false);
        combo.setItems(remainDBTypeList.toArray(new String[remainDBTypeList.size()]));
        final RegularExpression finalRegExpress = regularExpress;
        String language = regularExpress.getExpression().getLanguage();
        String body = regularExpress.getExpression().getBody();
        // added yyin 20120815 TDQ-5982
        // regularExpress.getExpression().setModificationDate(getCurrentDateTime());
        // ~

        if (language == null) {
            combo.setText(remainDBTypeList.get(0));
        } else {
            combo.setText(PatternLanguageType.findNameByLanguage(language));
        }

        GridDataFactory.fillDefaults().span(2, 1).grab(false, false).applyTo(combo);

        combo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String lang = combo.getText();
                finalRegExpress.getExpression().setLanguage(PatternLanguageType.findLanguageByName(lang));
                // added yyin 20120815 TDQ-5982
                finalRegExpress.getExpression().setModificationDate(getCurrentDateTime());
                setDirty(true);
            }
        });
        final Text patternText = new Text(expressComp, SWT.BORDER);
        patternText.setText(body == null ? PluginConstant.EMPTY_STRING : body);
        GridDataFactory.fillDefaults().span(6, 1).grab(true, true).applyTo(patternText);
        // TDQ-10804, if the content is too long, the below "add" button will be disapper.set widthHint to '0' so that
        // the Text width isn't adaptive width.
        ((GridData) patternText.getLayoutData()).widthHint = 0;
        patternText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                finalRegExpress.getExpression().setBody(patternText.getText());
                // added yyin 20120815 TDQ-5982
                finalRegExpress.getExpression().setModificationDate(getCurrentDateTime());
                setDirty(true);
            }

        });
        Button delButton = new Button(expressComp, SWT.NONE);
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.delete")); //$NON-NLS-1$
        GridDataFactory.fillDefaults().span(1, 1).grab(false, false).applyTo(delButton);
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                tempPatternComponents.remove(finalRegExpress);
                expressComp.dispose();
                patternDefinitionSection.setExpanded(true);
                setDirty(true);
            }
        });

        // MOD scorreia 2008-12-15 do not display button when pattern is "SQL Like"
        if (!ExpressionType.SQL_LIKE.getLiteral().equals(expressionType)) {
            Button testPatternButton = new Button(expressComp, SWT.NONE);
            // testPatternButton.setImage(ImageLib.getImage(ImageLib.));
            testPatternButton.setText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.test")); //$NON-NLS-1$
            testPatternButton.setToolTipText(DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternTest")); //$NON-NLS-1$
            GridDataFactory.fillDefaults().span(1, 1).grab(false, false).applyTo(testPatternButton);
            testPatternButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    // Open test pattern viewer
                    PatternTestView patternTestView = CorePlugin.getDefault().getPatternTestView();
                    patternTestView
                            .setPatternExpression(PatternMasterDetailsPage.this, getCurrentModelElement(), finalRegExpress);
                }
            });
        }

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(expressComp);
    }

    private String getCurrentDateTime() {
        return DateUtils.getCurrentDate(DateUtils.PATTERN_5);
    }

    @Override
    public void setDirty(boolean isDirty) {
        if (this.isDirty != isDirty) {
            this.isDirty = isDirty;
            ((PatternEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
            this.firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((PatternEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        // ADD yyi 2011-05-31 16158:add whitespace check for text fields.
        if (!checkWhithspace()) {
            MessageUI.openError(DefaultMessagesImpl.getString("AbstractMetadataFormPage.whitespace")); //$NON-NLS-1$
            return;
        }
        if (!canSave().isOk()) {
            return;
        }
        super.doSave(monitor);
        if (savePattern()) {
            this.isDirty = false;
        }
    }

    private boolean savePattern() {
        if (tempPatternComponents.isEmpty()) {
            MessageDialog
                    .openError(
                            null,
                            DefaultMessagesImpl.getString("PatternMasterDetailsPage.error"), DefaultMessagesImpl.getString("PatternMasterDetailsPage.cannotSave", getCurrentModelElement().getName())); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        }
        getCurrentModelElement().getComponents().clear();
        getCurrentModelElement().getComponents().addAll(tempPatternComponents);

        // PTODO fixed bug 4296: set the Pattern is valid
        TaggedValueHelper.setValidStatus(true, getCurrentModelElement());
        EList<PatternComponent> components = getCurrentModelElement().getComponents();
        List<String> existLanguage = new ArrayList<String>();
        for (int i = 0; i < components.size(); i++) {
            RegularExpressionImpl regularExpress = (RegularExpressionImpl) components.get(i);
            String language = regularExpress.getExpression().getLanguage();
            if ((regularExpress.getExpression().getBody() == null) || (!regularExpress.getExpression().getBody().matches("'.*'"))) { //$NON-NLS-1$
                MessageDialog
                        .openWarning(
                                null,
                                DefaultMessagesImpl.getString("PatternMasterDetailsPage.warning"), DefaultMessagesImpl.getString("PatternMasterDetailsPage.patternExpressions", language)); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            }
            if (existLanguage.contains(language)) {
                MessageDialog
                        .openError(
                                null,
                                DefaultMessagesImpl.getString("PatternMasterDetailsPage.error"), DefaultMessagesImpl.getString("PatternMasterDetailsPage.languageType", language)); //$NON-NLS-1$ //$NON-NLS-2$
                return false;
            } else {
                existLanguage.add(language);
            }
        }
        TDQPatternItem patternItem = (TDQPatternItem) this.patternRepNode.getObject().getProperty().getItem();
        // Added TDQ-14249 rename udi, 20170920
        patternRepNode.getObject().getProperty().setDisplayName(patternItem.getPattern().getName());
        // MOD yyi 2012-02-08 TDQ-4621:Explicitly set true for updating dependencies.
        ElementWriterFactory.getInstance().createPatternWriter().save(patternItem, true);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#canSave()
     */
    @Override
    public ReturnCode canSave() {
        ReturnCode rc = canModifyName(ERepositoryObjectType.TDQ_PATTERN_ELEMENT);
        if (!rc.isOk()) {
            MessageDialogWithToggle.openError(null,
                    DefaultMessagesImpl.getString("AbstractMetadataFormPage.saveFailed"), rc.getMessage()); //$NON-NLS-1$
        }
        return rc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentRepNode()
     */
    @Override
    public PatternRepNode getCurrentRepNode() {
        return this.patternRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentModelElement()
     */
    @Override
    public Pattern getCurrentModelElement() {
        return patternRepNode.getPattern();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#init(org.eclipse.ui.forms.editor.FormEditor)
     */
    @Override
    protected void init(FormEditor editor) {
        currentEditor = (PatternEditor) editor;
        reset();
        this.patternRepNode = getPatternRepNodeFromInput(currentEditor.getEditorInput());
        this.expressionType = DomainHelper.getExpressionType(getCurrentModelElement());
    }

    /**
     * get PatternRepNode From editorInput
     * 
     * @param editorInput
     * @return
     */
    private PatternRepNode getPatternRepNodeFromInput(IEditorInput editorInput) {
        if (editorInput instanceof FileEditorInput) {
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (file != null) {
                Pattern pattern = PatternResourceFileHelper.getInstance().findPattern(file);
                pattern = (Pattern) EObjectHelper.resolveObject(pattern);
                return RepositoryNodeHelper.recursiveFindPattern(pattern);
            }
        } else if (editorInput instanceof PatternItemEditorInput) {
            return ((PatternItemEditorInput) editorInput).getRepNode();
        }
        return null;
    }
}
