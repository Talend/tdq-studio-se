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
package org.talend.dataprofiler.core.helper;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.talend.core.ui.context.view.AbstractContextView;
import org.talend.core.ui.context.view.Contexts;
import org.talend.dataprofiler.core.ui.editor.SupportContextEditor;
import org.talend.dataprofiler.core.ui.views.context.TdqContextView;

/**
 * created by msjian on 2014-6-19 Detailled comment
 * 
 */
public final class ContextViewHelper {

    private ContextViewHelper() {
    }

    public static void updateContextView(IWorkbenchPart part) {
        IWorkbenchPart testedPart = part;
        if (!(part instanceof SupportContextEditor)) {
            testedPart = part.getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
        }
        // only for ReportEditror and AnalysisEditor
        if (testedPart instanceof SupportContextEditor) {
            SupportContextEditor currentEditor = (SupportContextEditor) testedPart;
            Contexts.setTitle(currentEditor.getTitle());
            currentEditor.updateContextView();
        }
    }

    public static void resetContextView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            IViewPart view = page.findView(AbstractContextView.CTX_ID_TDQ);
            if (view != null && view instanceof TdqContextView) {
                ((TdqContextView) view).reset();
            }
        }
    }

    public static void hideContextView(IWorkbenchPart part) {
        boolean currentEditorOpened = false;
        IWorkbenchPage page = part.getSite().getWorkbenchWindow().getActivePage();
        if (page == null) {
            return;
        }
        IEditorReference[] editorReferences = page.getEditorReferences();
        for (IEditorReference editorRef : editorReferences) {
            if (editorRef != null && editorRef.getEditor(false) != null) {
                if (editorRef.getEditor(false) instanceof SupportContextEditor) {
                    currentEditorOpened = true;
                    break;
                }
            }
        }
        if (!currentEditorOpened) {
            IViewPart ctxViewer = page.findView(AbstractContextView.CTX_ID_TDQ);
            if (ctxViewer != null) {
                page.hideView(ctxViewer);
            }
        }
    }
}
