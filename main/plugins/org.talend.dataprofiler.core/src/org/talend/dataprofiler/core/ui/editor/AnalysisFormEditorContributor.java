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
package org.talend.dataprofiler.core.ui.editor;

import java.util.Hashtable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.talend.dataprofiler.core.ui.action.actions.RunAnalysisAction;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;

/**
 * DOC rli class global comment. Detailled comment
 */
public class AnalysisFormEditorContributor extends MultiPageEditorActionBarContributor {

    protected AnalysisEditor fEditor;

    protected IFormPage fPage;

    private Hashtable<String, Action> fGlobalActions = new Hashtable<String, Action>();

    public AnalysisFormEditorContributor() {
        super();
    }

    /**
     * DOC rli PDEFormEditorContributor class global comment. Detailled comment
     */
    class GlobalAction extends Action {

        private String id;

        public GlobalAction(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            fEditor.performGlobalAction(id);
        }
    }

    private void addGlobalAction(String id) {
        GlobalAction action = new GlobalAction(id);
        action.setActionDefinitionId(id);
        addGlobalAction(id, action);
    }

    private void addGlobalAction(String id, Action action) {
        fGlobalActions.put(id, action);
        getActionBars().setGlobalActionHandler(id, action);
    }

    @Override
    public void contributeToMenu(IMenuManager mm) {
    }

    @Override
    public void contributeToStatusLine(IStatusLineManager slm) {
    }

    @Override
    public void contributeToToolBar(IToolBarManager tbm) {
    }

    @Override
    public void contributeToCoolBar(ICoolBarManager cbm) {
    }

    public AnalysisEditor getEditor() {
        return fEditor;
    }

    public IAction getGlobalAction(String id) {
        return fGlobalActions.get(id);
    }

    public IStatusLineManager getStatusLineManager() {
        return getActionBars().getStatusLineManager();
    }

    protected void makeActions() {
        addGlobalAction(ActionFactory.DELETE.getId());
        // MOD mzhao bug 8710
        addGlobalAction(RunAnalysisAction.ID);
    }

    @Override
    public void setActiveEditor(IEditorPart targetEditor) {
        if (!(targetEditor instanceof AnalysisEditor)) {
            return;
        }

        fEditor = (AnalysisEditor) targetEditor;
        setActivePage(fEditor.getActiveEditor());

    }

    @Override
    public void setActivePage(IEditorPart newEditor) {
        if (fEditor == null) {
            return;
        }
        IFormPage oldPage = fPage;
        fPage = fEditor.getActivePageInstance();
        if (fPage != null) {
            if (oldPage != null && !oldPage.isEditor() && !fPage.isEditor()) {
                getActionBars().updateActionBars();
            }
        }
        // MOD mzhao bug 8710
        addGlobalAction(RunAnalysisAction.ID, fGlobalActions.get(RunAnalysisAction.ID));
    }

    @Override
    public void init(IActionBars bars) {
        super.init(bars);
        makeActions();
    }

}
