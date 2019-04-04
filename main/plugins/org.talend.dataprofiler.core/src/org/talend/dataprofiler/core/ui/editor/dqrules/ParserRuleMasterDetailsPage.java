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
package org.talend.dataprofiler.core.ui.editor.dqrules;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC klliu class global comment. Detailled comment
 */
public class ParserRuleMasterDetailsPage extends AbstractMetadataFormPage implements PropertyChangeListener {

    final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();

    protected RuleRepNode ruleRepNode;

    private Section parserRuleDefinitionSection;

    private ParserRuleTableViewer parserRuleTableViewer;

    public ParserRuleMasterDetailsPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    @Override
    public void setDirty(boolean isDirty) {
        if (this.isDirty != isDirty) {
            this.isDirty = isDirty;
            ((DQRuleEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
            // MOD klliu 2009-06-25 bug 7687
            this.firePropertyChange(IEditorPart.PROP_DIRTY);
            // ~
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            ((DQRuleEditor) this.getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.ParserRuleSettings")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.ParserRuleMetadata")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.description")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        createParserRuleDefinitionSection(topComp);
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        if (!checkWhithspace()) {
            MessageUI.openError(DefaultMessagesImpl.getString("AbstractMetadataFormPage.whitespace")); //$NON-NLS-1$
            return;
        }
        if (!canSave().isOk()) {
            return;
        }
        super.doSave(monitor);
        if (saveParserRule()) {
            this.isDirty = false;
        }
    }

    private boolean saveParserRule() {
        TaggedValueHelper.setValidStatus(true, getCurrentModelElement());
        getCurrentModelElement().getExpression().addAll(parserRuleTableViewer.getParserRuleTdExpressions());

        TDQBusinessRuleItem parserRuleItem = (TDQBusinessRuleItem) getCurrentRepNode().getObject().getProperty().getItem();
        ReturnCode rc = ElementWriterFactory.getInstance().createdRuleWriter().save(parserRuleItem, Boolean.FALSE);
        if (!rc.isOk()) {
            return false;
        }
        return true;

    }

    /**
     * DOC klliu Comment method "createJoinConditionSection".
     * 
     * @param comp
     */
    private void createParserRuleDefinitionSection(Composite comp) {
        parserRuleDefinitionSection = createSection(form, comp,
                DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.parserRule"), null); //$NON-NLS-1$

        Label label = new Label(parserRuleDefinitionSection, SWT.WRAP);
        label.setText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.typeRule")); //$NON-NLS-1$

        parserRuleDefinitionSection.setDescriptionControl(label);
        createParserRuleDefinitionComp();
    }

    /**
     * DOC klliu Comment method "createJoinConditionComp".
     * 
     * @return
     */
    private Composite createParserRuleDefinitionComp() {
        Composite newComp = toolkit.createComposite(parserRuleDefinitionSection);
        FormLayout fromlayout = new FormLayout();
        newComp.setLayout(fromlayout);
        // create parser rules label
        Label label1 = new Label(newComp, SWT.ON_TOP);
        label1.setText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.parserRule")); //$NON-NLS-1$
        FormData data = new FormData();
        data.top = new FormAttachment(0, 10);
        data.left = new FormAttachment(0, 5);
        data.bottom = new FormAttachment(50, -5);
        data.right = new FormAttachment(10, -5);
        label1.setLayoutData(data);
        // create expresstion viewer
        Composite ruleViewerComp = new Composite(newComp, SWT.None);
        GridLayout layout = new GridLayout(1, false);
        ruleViewerComp.setLayout(layout);
        data = new FormData();
        data.top = new FormAttachment(0, 5);
        data.left = new FormAttachment(label1, 5);
        ruleViewerComp.setLayoutData(data);

        createParserRuleViewColumns(ruleViewerComp);
        parserRuleDefinitionSection.setClient(newComp);
        return newComp;
    }

    /**
     * DOC klliu Comment method "createParserRuleViewColumns".
     * 
     * @param parView
     */
    private void createParserRuleViewColumns(Composite ruleViewerComp) {
        parserRuleTableViewer = new ParserRuleTableViewer(ruleViewerComp, this);
        parserRuleTableViewer.setDirty(false);
        createButtons(ruleViewerComp, parserRuleTableViewer);
    }

    /**
     * DOC klliu Comment method "createButtons".
     * 
     * @param parent
     */
    private void createButtons(Composite parent, final ParserRuleTableViewer parserRuleTableViewer) {
        // boolean isNeedTestButton = false;
        // if (GlobalServiceRegister.getDefault().isServiceRegistered(IAntlrEditorUIService.class)) {
        // isNeedTestButton = true;
        // }

        Composite buttonsComposite = new Composite(parent, SWT.NONE);
        buttonsComposite.setLayout(new GridLayout(7, true));

        GridData labelGd = new GridData();
        labelGd.horizontalAlignment = SWT.LEFT;
        labelGd.widthHint = 30;

        final Button addButton = new Button(buttonsComposite, SWT.NONE);
        addButton.setToolTipText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.addItem")); //$NON-NLS-1$
        addButton.setImage(ImageLib.getImage(ImageLib.ADD_ACTION));
        addButton.setLayoutData(labelGd);
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                parserRuleTableViewer.addTdExpression();
            }
        });

        final Button delButton = new Button(buttonsComposite, SWT.NONE);
        delButton.setToolTipText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.deleteItem")); //$NON-NLS-1$
        delButton.setImage(ImageLib.getImage(ImageLib.DELETE_ACTION));
        delButton.setLayoutData(labelGd);
        delButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TdExpression tdExpression = (TdExpression) ((IStructuredSelection) parserRuleTableViewer.getSelection())
                        .getFirstElement();
                if (tdExpression != null) {
                    parserRuleTableViewer.removeTdExpression(tdExpression);
                }
            }
        });
        final Button upButton = new Button(buttonsComposite, SWT.NONE);
        upButton.setToolTipText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.moveupItem")); //$NON-NLS-1$
        upButton.setImage(ImageLib.getImage(ImageLib.UP_ACTION));
        upButton.setLayoutData(labelGd);
        upButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TdExpression tdExpression = (TdExpression) ((IStructuredSelection) parserRuleTableViewer.getSelection())
                        .getFirstElement();
                if (tdExpression != null) {
                    parserRuleTableViewer.moveTdExpression(tdExpression, 1);
                }
            }
        });

        final Button downButton = new Button(buttonsComposite, SWT.NONE);
        downButton.setToolTipText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.moveDownItem")); //$NON-NLS-1$
        downButton.setImage(ImageLib.getImage(ImageLib.DOWN_ACTION));
        downButton.setLayoutData(labelGd);
        downButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TdExpression tdExpression = (TdExpression) ((IStructuredSelection) parserRuleTableViewer.getSelection())
                        .getFirstElement();
                if (tdExpression != null) {
                    parserRuleTableViewer.moveTdExpression(tdExpression, -1);
                }
            }
        });
        final Button copyButton = new Button(buttonsComposite, SWT.NONE);
        copyButton.setToolTipText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.copyItem")); //$NON-NLS-1$
        copyButton.setImage(ImageLib.getImage(ImageLib.COPY_ACTION));
        copyButton.setLayoutData(labelGd);
        copyButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                List<TdExpression> listTdExpression = ((IStructuredSelection) parserRuleTableViewer.getSelection()).toList();
                parserRuleTableViewer.copyTdExpression(listTdExpression);

            }
        });

        final Button pasteButton = new Button(buttonsComposite, SWT.NONE);
        pasteButton.setToolTipText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.parseItem")); //$NON-NLS-1$
        pasteButton.setImage(ImageLib.getImage(ImageLib.PASTE_ACTION));
        pasteButton.setLayoutData(labelGd);
        pasteButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                parserRuleTableViewer.pasteTdExpression();
            }
        });
        // TDQ-16431 20190404 because of upgrading to Antlr4, the old code can not be used. waiting for next step.
        // if (isNeedTestButton) {
        // final Button testButton = new Button(buttonsComposite, SWT.NONE);
        // testButton.setImage(ImageLib.getImage(ImageLib.RULE_TEST));
        // testButton.setToolTipText(DefaultMessagesImpl.getString("ParserRuleMasterDetailsPage.testRule"));//$NON-NLS-1$
        // testButton.setLayoutData(labelGd);
        // testButton.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        // if (GlobalServiceRegister.getDefault().isServiceRegistered(IAntlrEditorUIService.class)) {
        // IAntlrEditorUIService antlrEditorUIService = (IAntlrEditorUIService) GlobalServiceRegister.getDefault()
        // .getService(IAntlrEditorUIService.class);
        // antlrEditorUIService.runTestRuleAction(getCurrentModelElement(),
        // ParserRuleMasterDetailsPage.this.getEditor());
        // }
        //
        // }
        // });
        // }
    }

    public ParserRuleTableViewer getParserRuleTableViewer() {
        return parserRuleTableViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#canSave()
     */
    @Override
    public ReturnCode canSave() {
        ReturnCode rc = canModifyName(ERepositoryObjectType.TDQ_RULES);
        if (!rc.isOk()) {
            MessageDialogWithToggle.openError(null,
                    DefaultMessagesImpl.getString("AbstractMetadataFormPage.saveFailed"), rc.getMessage()); //$NON-NLS-1$
        }
        return rc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentModelElement()
     */
    @Override
    public ParserRule getCurrentModelElement() {
        return (ParserRule) ruleRepNode.getRule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentRepNode()
     */
    @Override
    public RuleRepNode getCurrentRepNode() {
        return ruleRepNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#init(org.eclipse.ui.forms.editor.FormEditor)
     */
    @Override
    protected void init(FormEditor editor) {
        currentEditor = (DQRuleEditor) editor;
        ruleRepNode = getParseRuleRepNodeFromInput(currentEditor.getEditorInput());
    }

    /**
     * get PatternRepNode From editorInput
     * 
     * @param editorInput
     * @return
     */
    private RuleRepNode getParseRuleRepNodeFromInput(IEditorInput editorInput) {
        if (editorInput instanceof FileEditorInput) {
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (file != null) {
                ParserRule parserRule = (ParserRule) DQRuleResourceFileHelper.getInstance().findDQRule(file);
                parserRule = (ParserRule) EObjectHelper.resolveObject(parserRule);
                return RepositoryNodeHelper.recursiveFindRuleParser(parserRule);
            }
        } else if (editorInput instanceof BusinessRuleItemEditorInput) {
            return ((BusinessRuleItemEditorInput) editorInput).getRepNode();
        }
        return null;
    }

}
