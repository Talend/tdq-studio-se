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
package org.talend.dataprofiler.core.ui.editor.matchrule;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.editor.dqrules.BusinessRuleItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.dqrules.DQRuleEditor;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dataquality.record.linkage.constant.RecordMatcherType;
import org.talend.dataquality.record.linkage.ui.section.SelectAlgorithmSection;
import org.talend.dataquality.record.linkage.ui.section.definition.BlockingKeyDefinitionSection;
import org.talend.dataquality.record.linkage.ui.section.definition.DefaultSurvivorshipDefinitionSection;
import org.talend.dataquality.record.linkage.ui.section.definition.MatchAndSurvivorKeySection;
import org.talend.dataquality.record.linkage.ui.section.definition.MatchKeyDefinitionSection;
import org.talend.dataquality.record.linkage.ui.section.definition.ParticularDefSurshipDefinitionSection;
import org.talend.dataquality.record.linkage.ui.section.definition.ParticularDefSurshipMatchRuleSection;
import org.talend.dataquality.record.linkage.ui.service.IMatchRuleChangeService;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dq.helper.EObjectHelper;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.nodes.RuleRepNode;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.utils.sugars.ReturnCode;

/**
 * created by zshen on Aug 19, 2013 Detailled comment
 * 
 */
public class MatchRuleMasterDetailsPage extends AbstractMetadataFormPage implements PropertyChangeListener {

    private SelectAlgorithmSection selectAlgorithmSection = null;

    private BlockingKeyDefinitionSection blockingKeyDefinitionSection = null;

    private MatchKeyDefinitionSection matchingKeyDefinitionSection = null;

    private MatchAndSurvivorKeySection matchAndSurvivorKeySection = null;

    private DefaultSurvivorshipDefinitionSection defaultSurvivorshipDefinitionSection = null;

    private ParticularDefSurshipDefinitionSection particularDefaultSurvivorshipSection = null;

    protected RuleRepNode ruleRepNode;

    /*
     * these two variables are used only when this page comes from MDM team
     */
    private MatchRuleDefinition matchRuleDefiniton;

    private Item item;

    /**
     * DOC zshen MatchRuleMasterDetailsPage constructor comment.
     * 
     * @param editor
     */
    public MatchRuleMasterDetailsPage(FormEditor editor) {
        super(editor, "MatchRuleEditor.masterPage", DefaultMessagesImpl.getString("DQRuleEditor.matchRuleSettings")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.editor.FormPage#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (getCurrentModelElement() != null && getCurrentModelElement().eResource() != null) {
            getCurrentModelElement().eResource().unload();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (PluginConstant.ISDIRTY_PROPERTY.equals(evt.getPropertyName())) {
            this.setDirty(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#canSave()
     */
    @Override
    public ReturnCode canSave() {
        ReturnCode rc = new ReturnCode(false);
        if (this.isDirty) {
            ReturnCode checkResultStatus = blockingKeyDefinitionSection.checkResultStatus();
            String algorithmName = this.selectAlgorithmSection.getAlgorithmName();
            boolean isTSwoosh = RecordMatcherType.T_SwooshAlgorithm.name().equals(algorithmName);
            if (checkResultStatus.isOk()) {
                if (isTSwoosh) {
                    checkResultStatus = matchAndSurvivorKeySection.checkResultStatus();
                } else {
                    checkResultStatus = matchingKeyDefinitionSection.checkResultStatus();
                }
            }
            if (checkResultStatus.isOk() && isTSwoosh) {
                checkResultStatus = this.particularDefaultSurvivorshipSection.checkResultStatus();
            }
            if (!checkResultStatus.isOk()) {
                return checkResultStatus;
            }
            rc.setOk(true);
        }
        return rc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractFormPage#setDirty(boolean)
     */
    @Override
    public void setDirty(boolean isDirty) {
        if (this.isDirty != isDirty) {
            this.isDirty = isDirty;
            ((DQRuleEditor) getEditor()).firePropertyChange(IEditorPart.PROP_DIRTY);
            firePropertyChange(IEditorPart.PROP_DIRTY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#createFormContent(org.eclipse.ui.forms.IManagedForm
     * )
     */
    @Override
    protected void createFormContent(IManagedForm managedForm) {
        setFormTitle(DefaultMessagesImpl.getString("DQRuleEditor.matchRuleSettings")); //$NON-NLS-1$
        setMetadataSectionTitle(DefaultMessagesImpl.getString("DQRuleEditor.matchRuleMetadata")); //$NON-NLS-1$
        setMetadataSectionDescription(DefaultMessagesImpl.getString("MatchRuleMasterDetailsPage.setProperties")); //$NON-NLS-1$
        super.createFormContent(managedForm);

        // resetJoinElements();
        createSelectRecordLinkageSection(topComp);

        createGenerationOfBlockingKeySection(topComp);
        selectAlgorithmSection.setBlockkeySection(blockingKeyDefinitionSection);

        createMatchingKeySection(topComp);
        selectAlgorithmSection.setMatchKeySection(matchingKeyDefinitionSection);

        createMatchAndSurvivorKeySection(topComp);
        selectAlgorithmSection.setMatchAndSurvivorKeySection(matchAndSurvivorKeySection);

        createParticularDefaultSurvivorshipSection(topComp);
        selectAlgorithmSection.setParticularDefaultSurvivorshipSection(particularDefaultSurvivorshipSection);

        createDefaultSurvivorshipSection(topComp);
        selectAlgorithmSection.setDefaultSurvivorshipDefinitionSection(defaultSurvivorshipDefinitionSection);
    }

    /**
     * DOC yyin Comment method "createMatchAndSurvivorKeySection".
     * 
     * @param topComp
     */
    private void createMatchAndSurvivorKeySection(Composite mainComp) {
        matchAndSurvivorKeySection = new MatchAndSurvivorKeySection(form, mainComp, toolkit);
        matchAndSurvivorKeySection.setMatchRuleDef(getCurrentModelElement());
        matchAndSurvivorKeySection.setAddColumn(!selectAlgorithmSection.isVSRMode());
        matchAndSurvivorKeySection.createContent();
        matchAndSurvivorKeySection.addPropertyChangeListener(this);
        matchAndSurvivorKeySection.changeSectionDisStatus(!selectAlgorithmSection.isVSRMode());
        matchAndSurvivorKeySection.getSection().setExpanded(true);
        registerSection(matchAndSurvivorKeySection.getSection());
    }

    /**
     * DOC zshen Comment method "createMatchingKeySection".
     * 
     * @param mainComp
     */
    private void createMatchingKeySection(Composite mainComp) {
        matchingKeyDefinitionSection = new MatchKeyDefinitionSection(form, mainComp, toolkit);
        matchingKeyDefinitionSection.setMatchRuleDef(getCurrentModelElement());
        matchingKeyDefinitionSection.setAddColumn(!selectAlgorithmSection.isVSRMode());
        matchingKeyDefinitionSection.createContent();
        matchingKeyDefinitionSection.addPropertyChangeListener(this);
        matchingKeyDefinitionSection.changeSectionDisStatus(selectAlgorithmSection.isVSRMode());
        matchingKeyDefinitionSection.getSection().setExpanded(true);
        registerSection(matchingKeyDefinitionSection.getSection());
    }

    /**
     * DOC HHB Comment method "createSurvivorshipSection".
     * 
     * @param mainComp
     * 
     * private void createSurvivorshipSection(Composite mainComp) { survivorshipDefinitionSection = new
     * SurvivorshipDefinitionSection(form, mainComp, toolkit);
     * survivorshipDefinitionSection.setMatchRuleDef(getCurrentModelElement());
     * survivorshipDefinitionSection.createContent(); survivorshipDefinitionSection.addPropertyChangeListener(this);
     * survivorshipDefinitionSection.changeSectionDisStatus(!selectAlgorithmSection.isVSRMode());
     * survivorshipDefinitionSection.getSection().setExpanded(true); }
     */

    private void createDefaultSurvivorshipSection(Composite mainComp) {
        defaultSurvivorshipDefinitionSection = new DefaultSurvivorshipDefinitionSection(form, mainComp, toolkit);
        defaultSurvivorshipDefinitionSection.setMatchRuleDef(getCurrentModelElement());
        defaultSurvivorshipDefinitionSection.createContent();
        defaultSurvivorshipDefinitionSection.addPropertyChangeListener(this);
        defaultSurvivorshipDefinitionSection.changeSectionDisStatus(!selectAlgorithmSection.isVSRMode());
        defaultSurvivorshipDefinitionSection.getSection().setExpanded(true);
        registerSection(defaultSurvivorshipDefinitionSection.getSection());
    }

    /**
     * Create section of ParticularDefaultSurvivorship table
     */
    private void createParticularDefaultSurvivorshipSection(Composite mainComp) {
        particularDefaultSurvivorshipSection = new ParticularDefSurshipMatchRuleSection(form, mainComp, toolkit);
        particularDefaultSurvivorshipSection.setMatchRuleDef(getCurrentModelElement());
        particularDefaultSurvivorshipSection.createContent();
        registerSection(particularDefaultSurvivorshipSection.getSection());
        particularDefaultSurvivorshipSection.addPropertyChangeListener(this);
        particularDefaultSurvivorshipSection.changeSectionDisStatus(!selectAlgorithmSection.isVSRMode());
        particularDefaultSurvivorshipSection.getSection().setExpanded(true);
    }

    /**
     * DOC zshen Comment method "createGenerationOfBlockingKey".
     * 
     * @param topComp
     */
    private void createGenerationOfBlockingKeySection(Composite mainComp) {
        blockingKeyDefinitionSection = new BlockingKeyDefinitionSection(form, mainComp, toolkit);
        blockingKeyDefinitionSection.setMatchRuleDef(getCurrentModelElement());
        blockingKeyDefinitionSection.createContent();
        blockingKeyDefinitionSection.addPropertyChangeListener(this);
        blockingKeyDefinitionSection.getSection().setExpanded(CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected());
        registerSection(blockingKeyDefinitionSection.getSection());
    }

    /**
     * DOC zshen Comment method "createSelectRecordLinkageSection".
     * 
     * @param topComp
     */
    private void createSelectRecordLinkageSection(Composite mainComp) {
        selectAlgorithmSection = new SelectAlgorithmSection(form, mainComp, toolkit);
        selectAlgorithmSection.setMatchRuleDef(getCurrentModelElement());
        selectAlgorithmSection.createChooseAlgorithmCom();
        selectAlgorithmSection.addPropertyChangeListener(this);
        selectAlgorithmSection.getSection().setExpanded(true);
        registerSection(selectAlgorithmSection.getSection());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {

        ReturnCode rc = canSave();
        if (!rc.isOk()) {
            MessageDialogWithToggle.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    DefaultMessagesImpl.getString("MatchRuleMasterDetailsPage.saveRule"), rc.getMessage()); //$NON-NLS-1$
            return;
        }
        // handle rename before saving it
        if (!handleRenameEvent()) {
            return;
        }
        //
        super.doSave(monitor);
        if (saveMatchRule()) {
            this.isDirty = false;
        }
    }

    private IMatchRuleChangeService getMatchRuleChangeService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMatchRuleChangeService.class)) {
            IMatchRuleChangeService service = (IMatchRuleChangeService) GlobalServiceRegister.getDefault().getService(
                    IMatchRuleChangeService.class);
            return service;
        }
        return null;
    }

    private boolean handleRenameEvent() {
        String oldName = (getCurrentModelElement()).getName();
        String newName = nameText.getText();
        if (oldName != null && newName != null && !oldName.equals(newName)) {
            IMatchRuleChangeService changeService = getMatchRuleChangeService();
            if (changeService != null) {
                return changeService.objectChange(getCurrentModelElement(), oldName, newName,
                        IMatchRuleChangeService.ChangeEvent.BEFORE_RENAME);
            }
        }
        return true;
    }

    /**
     * DOC zshen Comment method "saveMatchRule".
     * 
     * @return
     */
    private boolean saveMatchRule() {
        // algorithm
        getCurrentModelElement().setRecordLinkageAlgorithm(selectAlgorithmSection.getAlgorithmName());

        TDQMatchRuleItem matchRuleItem = (TDQMatchRuleItem) (item != null ? item : (TDQMatchRuleItem) getCurrentRepNode()
                .getObject().getProperty().getItem());
        ReturnCode rc = ElementWriterFactory.getInstance().createdMatchRuleWriter().save(matchRuleItem, Boolean.FALSE);
        return rc.isOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentModelElement()
     */
    @Override
    public MatchRuleDefinition getCurrentModelElement() {
        if (matchRuleDefiniton != null) {
            // when this page comes from MDM team
            return matchRuleDefiniton;
        }
        return (MatchRuleDefinition) ruleRepNode.getRule();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage#getCurrentRepNode()
     */
    @Override
    public RuleRepNode getCurrentRepNode() {
        // this can be null when this page comes from MDM team
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
        ruleRepNode = getMatchRuleRepNodeFromInput(currentEditor.getEditorInput());
    }

    /**
     * get PatternRepNode From editorInput
     * 
     * @param editorInput
     * @return
     */
    private RuleRepNode getMatchRuleRepNodeFromInput(IEditorInput editorInput) {
        if (editorInput instanceof FileEditorInput) {
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (file != null) {
                MatchRuleDefinition matchRuleDefinition = DQRuleResourceFileHelper.getInstance().findMatchRule(file);
                matchRuleDefinition = (MatchRuleDefinition) EObjectHelper.resolveObject(matchRuleDefinition);
                return RepositoryNodeHelper.recursiveFindMatcherRule(matchRuleDefinition);
            }
        } else if (editorInput instanceof BusinessRuleItemEditorInput) {
            return ((BusinessRuleItemEditorInput) editorInput).getRepNode();
        } else if (editorInput instanceof MatchRuleItemEditorInput) {
            // this is only comes from MDM team
            matchRuleDefiniton = (MatchRuleDefinition) ((MatchRuleItemEditorInput) editorInput).getMatchRule();
            item = ((MatchRuleItemEditorInput) editorInput).getItem();
        }
        return null;
    }

}
