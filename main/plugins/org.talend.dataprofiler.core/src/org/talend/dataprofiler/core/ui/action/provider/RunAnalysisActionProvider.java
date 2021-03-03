// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.action.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.properties.Item;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.IRuningStatusListener;
import org.talend.dataprofiler.core.ui.action.actions.RunAnalysisAction;
import org.talend.dataprofiler.core.ui.editor.analysis.AnalysisEditor;
import org.talend.dataquality.properties.TDQAnalysisItem;
import org.talend.dq.nodes.ReportSubFolderRepNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC rli class global comment. Detailled comment
 */
public class RunAnalysisActionProvider extends AbstractCommonActionProvider {

    Map<TDQAnalysisItem, IRuningStatusListener> listenerMap = new HashMap<TDQAnalysisItem, IRuningStatusListener>();

    /**
     * Adds a submenu to the given menu with the name "New Component".
     */
    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        Object[] array = ((TreeSelection) this.getContext().getSelection()).toArray();
        ArrayList<TDQAnalysisItem> selectedItemsList = new ArrayList<TDQAnalysisItem>();

        List<AnalysisEditor> currentOpenAnalysisEditorList = CorePlugin.getDefault().getCurrentOpenAnalysisEditor();
        boolean analysisIsRuning = false;

        for (Object obj : array) {
            RepositoryNode node = (RepositoryNode) obj;
            RepositoryNode parent = node.getParent();
            if (!(parent instanceof ReportSubFolderRepNode)) {
                // IPath append = WorkbenchUtils.getFilePath(node);
                Item item = node.getObject().getProperty().getItem();
                if (item instanceof TDQAnalysisItem) {
                    selectedItemsList.add((TDQAnalysisItem) item);
                    AnalysisEditor findAnalysisEditor = findAnalysisEditor(node, currentOpenAnalysisEditorList);
                    if (findAnalysisEditor != null) {
                        analysisIsRuning = !checkRunStatuInEditor(findAnalysisEditor);
                        // break because of the menu should be disable when someone analysis just running
                        if (analysisIsRuning) {
                            break;
                        } else {
                            listenerMap.put((TDQAnalysisItem) item, findAnalysisEditor.getMasterPage());
                        }
                    }

                }
            }
        }

        if (!selectedItemsList.isEmpty()) {
            // IFile file = ResourceManager.getRootProject().getFile(append);
            RunAnalysisAction runAnalysisAction = new RunAnalysisAction();
            // runAnalysisAction.setSelectionFile(file);
            runAnalysisAction.setEnabled(!analysisIsRuning);
            runAnalysisAction.setListenerMap(listenerMap);
            runAnalysisAction.setAnalysisItems(selectedItemsList.toArray(new TDQAnalysisItem[selectedItemsList.size()]));
            menu.add(runAnalysisAction);
        }
    }

    private AnalysisEditor findAnalysisEditor(RepositoryNode node, List<AnalysisEditor> currentOpenAnalysisEditorList) {
        for (AnalysisEditor editor : currentOpenAnalysisEditorList) {
            IRepositoryNode editorRepNode = editor.getMasterPage().getCurrentRepNode();
            if (editorRepNode.getId().equals(node.getId())) {
                return editor;
            }
        }
        return null;
    }

    private boolean checkRunStatuInEditor(AnalysisEditor anaEditor) {
        return anaEditor.getRunAnalysisAction().isEnabled();
    }
}
