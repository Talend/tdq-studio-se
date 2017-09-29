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
package org.talend.dataprofiler.core.ui.editor.dqrules;

import java.util.List;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.DefaultSaveAction;
import org.talend.dataprofiler.core.ui.editor.CommonFormEditor;
import org.talend.dataprofiler.core.ui.editor.TdEditorToolBar;
import org.talend.dataprofiler.core.ui.editor.matchrule.MatchRuleItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.matchrule.MatchRuleMasterDetailsPage;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class DQRuleEditor extends CommonFormEditor {

    private ParserRuleMasterDetailsPage parserPage;

    private MatchRuleMasterDetailsPage matchPage;

    private static final String ID = "DQRuleEditor.masterPage";//$NON-NLS-1$

    private DefaultSaveAction saveAction;

    @Override
    protected void addPages() {
        ModelElement currentRuleModelElement = getCurrentModelElement();
        try {
            if (currentRuleModelElement != null) {
                if (currentRuleModelElement instanceof ParserRule) {
                    parserPage = new ParserRuleMasterDetailsPage(this, ID,
                            DefaultMessagesImpl.getString("DQRuleEditor.parserRuleSettings")); //$NON-NLS-1$
                    addPage(parserPage);
                    setPartName(parserPage.getIntactElemenetName());
                } else if (currentRuleModelElement instanceof MatchRuleDefinition) {
                    matchPage = new MatchRuleMasterDetailsPage(this);
                    addPage(matchPage);
                    setPartName(matchPage.getIntactElemenetName());
                    setTitleImage(ImageLib.getImage(ImageLib.MATCH_RULE_WHITE_ICON));
                } else {
                    masterPage = new DQRuleMasterDetailsPage(this, ID,
                            DefaultMessagesImpl.getString("DQRuleEditor.dqRuleSettings")); //$NON-NLS-1$
                    addPage(masterPage);
                    setPartName(masterPage.getIntactElemenetName());
                }
            }
        } catch (PartInitException e) {
            ExceptionHandler.process(e, Level.ERROR);
        }

        // ADD xqliu 2009-07-02 bug 7687
        TdEditorToolBar toolbar = getToolBar();
        // MOD msjian 2011-9-22 TDQ-3372: Add a "save" button in the parser rule editor
        if (toolbar != null && (masterPage != null || parserPage != null || matchPage != null)) {
            // TDQ-3372 ~
            saveAction = new DefaultSaveAction(this);
            saveAction.setEnabled(false);
            toolbar.addActions(saveAction);
        }
        // ~
    }

    /**
     * DOC zshen Comment method "getCurrentModelElement".
     */
    private ModelElement getCurrentModelElement() {
        IEditorInput editorInput = this.getEditorInput();
        if (editorInput instanceof FileEditorInput) {
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (FactoriesUtil.isDQRuleFile(file.getFileExtension())) {
                return DQRuleResourceFileHelper.getInstance().findIndicatorDefinition(file);
            }
        } else if (editorInput instanceof BusinessRuleItemEditorInput) {
            return ((BusinessRuleItemEditorInput) editorInput).getModel();
        } else if (editorInput instanceof MatchRuleItemEditorInput) {
            // this only used when this comes from MDM team
            return ((MatchRuleItemEditorInput) editorInput).getMatchRule();
        }
        return null;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        if (masterPage != null) {
            if (masterPage.isDirty()) {
                masterPage.doSave(monitor);
                setPartName(masterPage.getIntactElemenetName());
            }
            setEditorObject(((DQRuleMasterDetailsPage) getMasterPage()).getCurrentRepNode());
        } else if (parserPage != null) {
            if (parserPage.isDirty()) {
                parserPage.doSave(monitor);
                setPartName(parserPage.getIntactElemenetName());
            }
            setEditorObject(parserPage.getCurrentRepNode());
        } else if (matchPage != null) {
            if (matchPage.isDirty()) {
                matchPage.doSave(monitor);
                setPartName(matchPage.getIntactElemenetName());
            }
            // setEditorObject(matchPage.getRuleRepNode());
        }
        super.doSave(monitor);

    }

    @Override
    public void firePropertyChange(final int propertyId) {
        // ADD xqliu 2009-07-02 bug 7687
        setSaveActionButtonState(isDirty());
        // ~
        super.firePropertyChange(propertyId);
    }

    @Override
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
        // ADD xqliu 2009-07-02 bug 7686
        if (masterPage != null) {
            setSaveActionButtonState(false);
        } else if (parserPage != null) {
            setSaveActionButtonState(false);
        } else if (matchPage != null) {
            setSaveActionButtonState(false);
        }
    }

    /**
     * DOC xqliu 2009-07-02 bug 7687.
     * 
     * @param state
     */
    public void setSaveActionButtonState(boolean state) {
        if (saveAction != null) {
            saveAction.setEnabled(state);
        }
    }

    @Override
    public void setFocus() {
        super.setFocus();
        // don't invoke this method here, invoke it in IPartListener.partBroughtToTop()
        // WorkbenchUtils.autoChange2DataProfilerPerspective();
    }

    public ParserRuleMasterDetailsPage getParserPage() {
        return this.parserPage;
    }

    private boolean isMatchRuleEditor() {
        ModelElement currentModelElement = getCurrentModelElement();
        if (currentModelElement != null && currentModelElement instanceof MatchRuleDefinition) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.CommonFormEditor#getPreferredPerspectiveId()
     */
    @Override
    public List<String> getPreferredPerspectiveId() {
        // we will use this editor on the Di perspective and MDM perspective so return null to disable this function
        if (isMatchRuleEditor()) {
            return null;
        } else {
            return super.getPreferredPerspectiveId();
        }
    }
}
