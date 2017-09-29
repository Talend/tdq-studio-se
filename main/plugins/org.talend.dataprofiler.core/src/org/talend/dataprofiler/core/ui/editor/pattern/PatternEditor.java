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

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.action.actions.DefaultSaveAction;
import org.talend.dataprofiler.core.ui.editor.CommonFormEditor;
import org.talend.dataprofiler.core.ui.editor.TdEditorToolBar;

/**
 * DOC rli class global comment. Detailled comment
 */
public class PatternEditor extends CommonFormEditor {

    // MOD xqliu 2009-07-02 bug 7687
    private DefaultSaveAction saveAction;

    // ~
    private static final String ID = "PatternEditor.masterPage";//$NON-NLS-1$

    @Override
    protected void addPages() {
        masterPage = new PatternMasterDetailsPage(this, ID, DefaultMessagesImpl.getString("PatternEditor.patternSettings")); //$NON-NLS-1$ 
        // MOD qiongli 2011-3-21,bug 19472.set method 'setPartName(...)' behind method 'addPage(...)'
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
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        if (masterPage.isDirty()) {
            masterPage.doSave(monitor);
            setPartName(masterPage.getIntactElemenetName());
        }
        setEditorObject(((PatternMasterDetailsPage) getMasterPage()).getCurrentRepNode());
        super.doSave(monitor);
    }

    @Override
    protected void firePropertyChange(final int propertyId) {
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
}
