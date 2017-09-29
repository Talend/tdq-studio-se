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

import java.util.List;

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
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisItemEditorInput;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.dbms.DbmsLanguage;
import org.talend.dq.dbms.DbmsLanguageFactory;
import org.talend.dq.nodes.DBColumnRepNode;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public final class DataFilterDND {

    private DataFilterDND() {
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
                    event.detail = DND.DROP_MOVE;
                    event.feedback = DND.FEEDBACK_INSERT_AFTER;
                }
            }

            @Override
            public void dropAccept(DropTargetEvent event) {
                // do nothing until now
            }

            @Override
            public void drop(DropTargetEvent event) {
                if (event.detail != DND.DROP_NONE) {
                    IStructuredSelection selection = (IStructuredSelection) commonViewer.getSelection();
                    if (!selection.isEmpty()) {
                        List list = selection.toList();
                        IEditorPart currentActiveEditor = CorePlugin.getDefault().getCurrentActiveEditor();
                        if (currentActiveEditor instanceof AnalysisEditor) {
                            AnalysisEditor editor = (AnalysisEditor) currentActiveEditor;
                            AnalysisItemEditorInput input = (AnalysisItemEditorInput) editor.getEditorInput();
                            for (Object object : list) {
                                DBColumnRepNode node = (DBColumnRepNode) object;
                                DbmsLanguage language = DbmsLanguageFactory.createDbmsLanguage((Analysis) input.getModel());
                                MetadataColumnRepositoryObject columnObject = (MetadataColumnRepositoryObject) node.getObject();
                                String name = columnObject.getTdColumn().getName();
                                targetControl.insert(language.quote(name));
                                // Focus text.
                                targetControl.forceFocus();
                            }
                        }
                    }
                }
            }
        });
    }
}
