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
package org.talend.dataprofiler.core.ui.editor.indicator;

import org.apache.log4j.Level;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.DefaultSaveAction;
import org.talend.dataprofiler.core.ui.editor.CommonFormEditor;
import org.talend.dataprofiler.core.ui.editor.TdEditorToolBar;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.indicators.definition.IndicatorsDefinitions;
import org.talend.dataquality.indicators.definition.userdefine.UDIndicatorDefinition;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;

/**
 * The Editor of Indicator Definitions, include system and user defined indicators.
 */
public class IndicatorEditor extends CommonFormEditor {

    private static final String ID = "Master Page";//$NON-NLS-1$

    private DefaultSaveAction saveAction;

    protected IndicatorsDefinitions indicatorDefinitions;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.editor.FormEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInputWithNotify(input);
        setPartName(getName(input));
    }

    private String getName(IEditorInput input) {
        if (input instanceof IndicatorEditorInput || input instanceof IndicatorDefinitionItemEditorInput) {
            return input.getName();
        } else if (input instanceof FileEditorInput) {
            return IndicatorResourceFileHelper.getInstance().findIndDefinition(((FileEditorInput) input).getFile()).getName();
        }
        return DefaultMessagesImpl.getString("IndicatorEditor.IndicatorEditor"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.CommonFormEditor#addPages()
     */
    @Override
    protected void addPages() {
        // TDQ-8453 according to the type to use IndicatorDefinition or UDI master page
        if (isSystemIndicator()) {
            masterPage = new IndicatorDefinitionMaterPage(this, ID, DefaultMessagesImpl.getString("IndicatorEditor.Indicator"));//$NON-NLS-1$
        } else {
            masterPage = new UDIMasterPage(this, ID, DefaultMessagesImpl.getString("IndicatorEditor.UserIndicator"));//$NON-NLS-1$
        }
        try {
            addPage(masterPage);
            setPartName(masterPage.getIntactElemenetName());
        } catch (PartInitException e) {
            ExceptionHandler.process(e, Level.ERROR);
        }

        TdEditorToolBar toolbar = getToolBar();
        if (toolbar != null && masterPage != null) {
            saveAction = new DefaultSaveAction(this);
            toolbar.addActions(saveAction);
            setSaveActionButtonState(false);
        }
    }

    /**
     * Judge if the current indicator definition is a system indicator
     * 
     * @return true if it is system indicator false if it is UDI
     */
    private boolean isSystemIndicator() {
        IEditorInput editorInput = this.getEditorInput();
        IndicatorDefinition definition = null;
        if (editorInput instanceof IndicatorEditorInput) {// from DQRespositoryView double click
            definition = ((IndicatorEditorInput) editorInput).getIndicatorDefinition();
        } else if (editorInput instanceof IndicatorDefinitionItemEditorInput) {// from OpenItemEditorAction
            definition = (IndicatorDefinition) ((IndicatorDefinitionItemEditorInput) editorInput).getModel();
        } else if (editorInput instanceof FileEditorInput) {
            // when open the indicator inside an analysis
            FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
            IFile file = fileEditorInput.getFile();
            if (FactoriesUtil.isUDIFile(file.getFileExtension())) {
                definition = IndicatorResourceFileHelper.getInstance().findIndDefinition(file);
            }
        }
        if (definition instanceof UDIndicatorDefinition) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
     */
    @Override
    protected void firePropertyChange(int propertyId) {
        setSaveActionButtonState(isDirty());

        super.firePropertyChange(propertyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.CommonFormEditor#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        masterPage.doSave(monitor);
        setPartName(masterPage.getIntactElemenetName());
        setEditorObject(((IndicatorDefinitionMaterPage) getMasterPage()).getIndicatorDefinitionRepNode());
        super.doSave(monitor);
    }

    /**
     * Sets the indicatorDefinitions.
     * 
     * @param indicatorDefinitions the indicatorDefinitions to set
     */
    public void setIndicatorDefinitions(IndicatorsDefinitions indicatorDefinitions) {
        this.indicatorDefinitions = indicatorDefinitions;
    }

    /**
     * Getter for indicatorDefinitions.
     * 
     * @return the indicatorDefinitions
     */
    public IndicatorsDefinitions getIndicatorDefinitions() {
        return indicatorDefinitions;
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
}
