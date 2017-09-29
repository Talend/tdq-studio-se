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
package org.talend.dataprofiler.core.ui.action.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.ui.actions.RenameFolderAction;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.editor.AbstractItemEditorInput;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.views.DQRespositoryView;
import org.talend.dataprofiler.core.ui.views.resources.IRepositoryObjectCRUDAction;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * rename tdq folder action.
 */
public class RenameTdqFolderAction extends RenameFolderAction {

    protected static Logger log = Logger.getLogger(RenameTdqFolderAction.class);

    private IRepositoryObjectCRUDAction repositoryObjectCRUD = RepNodeUtils.getRepositoryObjectCRUD();

    /**
     * @param node a folder
     */
    public RenameTdqFolderAction(RepositoryNode node) {
        super();
        this.repositoryNode = node;
    }

    /*
     * MOD 20130530 TDQ-7143, when any items are opened under the current folder, should not let it be renamed
     */
    @Override
    protected String getFirstOpenedChild(IRepositoryNode node) {
        if (node.hasChildren()) {
            IWorkbenchPage page = getActivePage();
            IEditorReference[] editorReferences = page.getEditorReferences();
            List<String> openEditor = new ArrayList<String>();
            List<IRepositoryNode> children = node.getChildren();
            for (IEditorReference tmpInput : editorReferences) {
                try {
                    IEditorInput editorInput = tmpInput.getEditorInput();

                    if (editorInput instanceof AbstractItemEditorInput) {
                        AbstractItemEditorInput einput = (AbstractItemEditorInput) editorInput;
                        openEditor.add(einput.getItem().getProperty().getId());
                    } else if (editorInput instanceof FileEditorInput) {
                        if (editorInput instanceof FileEditorInput) {
                            FileEditorInput fileInput = (FileEditorInput) editorInput;
                            for (IRepositoryNode currentNode : children) {
                                IFile nodeFile = RepositoryNodeHelper.getIFile(currentNode);
                                if (nodeFile != null
                                        && nodeFile.getFullPath().toString().equals(fileInput.getFile().getFullPath().toString())) {
                                    return currentNode.getObject().getLabel();
                                }
                            }
                        }
                    }
                } catch (PartInitException e) {
                    ExceptionHandler.process(e, Level.WARN);
                }
            }

            for (IRepositoryNode currentNode : children) {
                if (currentNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
                    if (openEditor.contains(currentNode.getObject().getId())) {
                        return currentNode.getObject().getLabel();
                    }
                } else if (currentNode.getType() == ENodeType.SIMPLE_FOLDER) {
                    String childOpen = getFirstOpenedChild(currentNode);
                    if (childOpen != null) {
                        return childOpen;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected Object getLabelOfNode(RepositoryNode node) {
        return node.getObject().getLabel();
    }// ~

    @Override
    protected void openFolderWizard(RepositoryNode node, ERepositoryObjectType objectType, IPath path) {
        if (repositoryNode.getObject().isDeleted()) {
            MessageDialog.openWarning(Display.getCurrent().getActiveShell(),
                    Messages.getString("RenameFolderAction.warning.cannotFind.title"), Messages //$NON-NLS-1$
                            .getString("RenameFolderAction.warning.cannotFind.message")); //$NON-NLS-1$
            return;
        }

        if (objectType != null) {
            super.openFolderWizard(node, objectType, path);

            // refresh the dq repository view
            if (this.repositoryNode != null && this.repositoryNode.getParent() != null) {
                CorePlugin.getDefault().refreshDQView(this.repositoryNode.getParent());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#getRepositorySelection()
     */
    @Override
    protected ISelection getRepositorySelection() {
        DQRespositoryView repositoryViewPart = null;
        for (IViewReference viewRef : getActivePage().getViewReferences()) {
            if (viewRef.getView(false) instanceof DQRespositoryView) {
                repositoryViewPart = (DQRespositoryView) viewRef.getView(false);
                break;
            }
        }

        if (repositoryViewPart == null) {
            return null;
        }
        return repositoryViewPart.getCommonViewer().getSelection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#getSelection()
     */
    @Override
    public ISelection getSelection() {
        repositoryObjectCRUD.refreshDQViewForRemoteProject();
        return repositoryObjectCRUD.getUISelection();
    }
}
