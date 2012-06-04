// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.dataprofiler.core.sql;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.PluginConstant;
import org.talend.dataprofiler.core.helper.WorkspaceResourceHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dq.helper.ProxyRepositoryManager;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z qzhang $
 * 
 */
public class RenameFolderAction extends Action {

    protected static Logger log = Logger.getLogger(RenameFolderAction.class);

    private IFolder obj;

    private RepositoryNode node;

    /**
     * DOC qzhang RenameFolderAction constructor comment.
     * 
     * @param obj
     */
    public RenameFolderAction(IFolder obj) {
        this.obj = obj;
        setText(DefaultMessagesImpl.getString("RenameFolderAction.renameFolder")); //$NON-NLS-1$
    }

    public RenameFolderAction(RepositoryNode node) {
        this.node = node;
        this.obj = WorkbenchUtils.getFolder(node);
        setText(DefaultMessagesImpl.getString("RenameFolderAction.renameFolder")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        // ADD xqliu 2012-05-24 TDQ-4831
        if (this.node != null && this.node instanceof SourceFileSubFolderNode) {
            SourceFileSubFolderNode folderNode = (SourceFileSubFolderNode) this.node;
            ReturnCode rc = WorkspaceResourceHelper.checkSourceFileSubFolderNodeOpening(folderNode);
            if (rc.isOk()) {
                WorkspaceResourceHelper.showSourceFilesOpeningWarnMessages(rc.getMessage());
                return;
            }
        }
        // ~ TDQ-4831
        InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(),
                DefaultMessagesImpl.getString("RenameFolderAction.renameFolderName"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("RenameFolderAction.inputNewFolderName"), null, new IInputValidator() { //$NON-NLS-1$

                    public String isValid(String newText) {
                        return null;
                    }

                });
        if (dialog.open() == InputDialog.OK) {
            String value2 = dialog.getValue();
            IFolder folder = obj.getParent().getFolder(new Path(value2));
            try {
                // close opend editors
                // List<SourceFileRepNode> sourceFileRepNodes = RepositoryNodeHelper.getSourceFileRepNodes(node, true);
                // RepNodeUtils.closeModelElementEditor(sourceFileRepNodes, true);
                List<IFile> files = FilesUtils.getFiles(this.obj, PluginConstant.SQL_STRING, true);
                RepNodeUtils.closeFileEditor(files, true);

                obj.move(folder.getFullPath(), true, null);
                ProxyRepositoryManager.getInstance().save();

                // refresh the dq repository view
                if (node != null && node.getParent() != null) {
                    CorePlugin.getDefault().refreshDQView(node.getParent());
                }
            } catch (CoreException e) {
                log.error(e, e);
            }
        }
    }

}
