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
package org.talend.dataprofiler.core.ui.views.context;

import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorPart;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.ui.context.view.AbstractContextView;
import org.talend.core.ui.context.view.Contexts;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.ui.editor.AbstractMetadataFormPage;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataprofiler.core.ui.editor.report.ReportItemEditorInput;
import org.talend.dq.helper.ProxyRepositoryManager;

/**
 * created by xqliu on Jul 25, 2013 Detailled comment
 * 
 */
public class TdqContextView extends AbstractContextView {

    private TDQItem currentItem;

    private AbstractMetadataFormPage currentPage;

    public TdqContextView() {
    }

    /**
     * Getter for currentItem.
     * 
     * @return the currentItem
     */
    public TDQItem getCurrentItem() {
        return this.currentItem;
    }

    /**
     * Set the currentItem.
     * 
     * @param currentItem
     */
    public void setCurrentItem(TDQItem currentItem) {
        this.currentItem = currentItem;
    }

    /**
     * Getter for currentPage.
     * 
     * @return the currentPage
     */
    public AbstractMetadataFormPage getCurrentPage() {
        return this.currentPage;
    }

    /**
     * Set the currentPage.
     * 
     * @param currentPage
     */
    public void setCurrentPage(AbstractMetadataFormPage currentPage) {
        this.currentPage = currentPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.context.view.AbstractContextView#createContextComposite(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContextComposite(Composite parent) {
        contextComposite = new TdqContextViewComposite(parent, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#swithToContextView()
     */
    @Override
    protected void swithToContextView() {
        Contexts.switchToCurContextsView(PluginConstant.PERSPECTIVE_ID, AbstractContextView.CTX_ID_TDQ);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.ui.context.view.AbstractContextView#handleDropContext(org.talend.core.model.properties.ContextItem
     * , java.util.Set, org.eclipse.ui.IEditorInput)
     */
    @Override
    protected boolean handleDropContext(ContextItem contextItem, Set<String> contextSet, IEditorInput editorInput) {
        // boolean created = false;
        // if (editorInput instanceof JobEditorInput) {
        // JobEditorInput jobInput = (JobEditorInput) editorInput;
        // IProcess2 process = jobInput.getLoadedProcess();
        // IContextManager contextManager = process.getContextManager();
        // // context group will reflect absolutely if no context variable in contextViewer
        // if (!ConnectionContextHelper.containsVariable(contextManager)) {
        // // for bug 15608
        // ConnectionContextHelper.addContextVarForJob(process, contextItem, contextManager);
        // // ConnectionContextHelper.checkAndAddContextsVarDND(contextItem,
        // // contextManager);
        // created = true;
        // } else {
        // Set<String> addedContext = ConnectionContextHelper.checkAndAddContextVariables(contextItem, contextSet,
        // contextManager, false);
        // if (addedContext != null && addedContext.size() > 0) {
        // ConnectionContextHelper.addContextVarForJob(process, contextItem, contextSet);
        // created = true;
        // }
        // }
        // }
        // return created;
        boolean created = false;
        if (editorInput instanceof ReportItemEditorInput) {

        }
        return created;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#getContextManager()
     */
    @Override
    protected IContextManager getContextManager() {
        return part == null ? null : ((SupportContextEditor) part).getContextManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#setEditorDirty(org.eclipse.ui.part.EditorPart)
     */
    @Override
    protected void setEditorDirty(EditorPart part) {
        ((SupportContextEditor) part).getMasterPage().setDirty(Boolean.TRUE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#getPart()
     */
    @Override
    protected void getPart() {
        final IEditorPart activeEditor = getSite().getPage().getActiveEditor();
        if (activeEditor instanceof SupportContextEditor) {
            part = (SupportContextEditor) activeEditor;
        } else {
            part = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#setCompositeReadonly(org.eclipse.ui.IEditorInput)
     */
    @Override
    protected void setCompositeReadonly(IEditorInput editorInput) {
        boolean modelIsNull = ProxyRepositoryManager.getInstance().isReadOnly();
        if (editorInput != null) {
            if (editorInput instanceof ReportItemEditorInput) {
                ReportItemEditorInput reportInput = (ReportItemEditorInput) editorInput;
                modelIsNull = reportInput.getModel() == null;
            } else if (editorInput instanceof AnalysisItemEditorInput) {
                AnalysisItemEditorInput analysisInput = (AnalysisItemEditorInput) editorInput;
                modelIsNull = analysisInput.getModel() == null;
            }
        }
        contextComposite.setReadOnly(modelIsNull || ProxyRepositoryManager.getInstance().isReadOnly());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#updateContextView()
     */
    @Override
    public void updateContextView() {
        super.updateContextView();
        if (part != null) {
            setEditorDirty(part);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#updateContextView(boolean, boolean, boolean)
     */
    @Override
    public void updateContextView(boolean isBuildIn, boolean isDisposeAll, boolean refreshView) {
        super.updateContextView(isBuildIn, isDisposeAll, refreshView);
        if (part != null) {
            setEditorDirty(part);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#updateContextView(boolean, boolean)
     */
    @Override
    public void updateContextView(boolean isBuildIn, boolean isDisposeAll) {
        super.updateContextView(isBuildIn, isDisposeAll);
        if (part != null) {
            setEditorDirty(part);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.view.AbstractContextView#updateContextView(boolean)
     */
    @Override
    public void updateContextView(boolean isBuildIn) {
        super.updateContextView(isBuildIn);
        if (part != null) {
            setEditorDirty(part);
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.currentItem = null;
        this.currentPage = null;
        if (contextComposite != null && contextComposite.getContextTableComposite() != null) {
            this.contextComposite.getContextTableComposite().refresh();
        }
    }

}
