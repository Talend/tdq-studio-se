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
package org.talend.dataprofiler.core.ui.views;

import java.util.Iterator;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.editor.dqrules.DQRuleEditor;
import org.talend.dq.nodes.DBColumnRepNode;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public final class WhereClauseDND {

    private WhereClauseDND() {
    }

    public static void installDND(final Text targetControl) {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        DQRespositoryView findView = (DQRespositoryView) activePage.findView(DQRespositoryView.ID);
        final CommonViewer commonViewer = findView.getCommonViewer();
        final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
        int operations = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { transfer };
        DropTarget dropTarget = new DropTarget(targetControl, operations);
        dropTarget.setTransfer(transfers);

        dropTarget.addDropListener(new DropTargetAdapter() {

            @Override
            public void dragEnter(DropTargetEvent event) {
                Object obj = ((IStructuredSelection) commonViewer.getSelection()).getFirstElement();
                if (!(obj instanceof DBColumnRepNode)) {
                    event.detail = DND.DROP_NONE;
                } else {
                    // event.detail = DND.DROP_MOVE;
                    event.feedback = DND.FEEDBACK_INSERT_AFTER;
                }
            }

            @Override
            public void dropAccept(DropTargetEvent event) {
            }

            @Override
            public void drop(DropTargetEvent event) {
                if (event.detail != DND.DROP_NONE) {
                    IStructuredSelection selection = (IStructuredSelection) commonViewer.getSelection();
                    if (!selection.isEmpty()) {
                        Iterator<DBColumnRepNode> it = selection.iterator();
                        IEditorPart currentActiveEditor = CorePlugin.getDefault().getCurrentActiveEditor();
                        if (currentActiveEditor instanceof DQRuleEditor) {
                            // DQRuleEditor editor = (DQRuleEditor) currentActiveEditor;
                            // FileEditorInput input = (FileEditorInput) editor.getEditorInput();
                            // WhereRule whereRule =
                            // DQRuleResourceFileHelper.getInstance().findWhereRule(input.getFile());
                            // String lang = whereRule.getSqlGenericExpression().get(0).getLanguage();
                            // // Analysis analysis = AnaResourceFileHelper.getInstance().findAnalysis(input.getFile());
                            while (it.hasNext()) {
                                // DbmsLanguage language = DbmsLanguageFactory.createDbmsLanguage(lang, null);
                                DBColumnRepNode next = it.next();
                                String name = next.getTdColumn().getName();
                                targetControl.insert(name);
                                targetControl.forceFocus();
                            }
                        }
                    }
                }
            }
        });
    }
}
