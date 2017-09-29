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
package org.talend.dataprofiler.core.ui.action.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.dataprofiler.core.sql.AddSqlFileAction;
import org.talend.dataprofiler.core.sql.ImportSqlFileAction;
import org.talend.dataprofiler.core.sql.OpenSqlFileAction;
import org.talend.dataprofiler.core.sql.RenameSqlFileAction;
import org.talend.dataprofiler.core.ui.action.actions.RenameTdqFolderAction;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dq.nodes.SourceFileFolderRepNode;
import org.talend.dq.nodes.SourceFileRepNode;
import org.talend.dq.nodes.SourceFileSubFolderNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class NewSourceFileActionProvider extends AbstractCommonActionProvider {

    public NewSourceFileActionProvider() {
    }

    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        TreeSelection treeSelection = ((TreeSelection) this.getContext().getSelection());
        List<IFile> selectedFiles = new ArrayList<IFile>();
        if (treeSelection.size() == 1) {
            RepositoryNode node = (RepositoryNode) treeSelection.getFirstElement();
            if (node instanceof SourceFileSubFolderNode) {
                menu.add(new AddSqlFileAction(node));
                menu.add(new ImportSqlFileAction(node));
                menu.add(new RenameTdqFolderAction(node));
            } else if (node instanceof SourceFileFolderRepNode) {
                menu.add(new AddSqlFileAction(node));
                menu.add(new ImportSqlFileAction(node));
            } else if (node instanceof SourceFileRepNode) {
                // MOD klliu bug TDQ-4797 2012-02-29
                menu.add(new RenameSqlFileAction(node));
            }
            boolean isSelectFile = computeSelectedFiles(treeSelection, selectedFiles);
            if (isSelectFile && !selectedFiles.isEmpty()) {
                menu.add(new OpenSqlFileAction(selectedFiles));
            }
        }
    }

    /**
     * DOC qzhang Comment method "computeSelectedFiles".
     * 
     * @param treeSelection
     * @param selectedFiles
     * @return
     */
    public static boolean computeSelectedFiles(TreeSelection treeSelection, List<IFile> selectedFiles) {
        boolean isSelectFile = false;
        Iterator iterator = treeSelection.iterator();
        while (iterator.hasNext()) {
            RepositoryNode node = (RepositoryNode) iterator.next();
            IRepositoryViewObject viewObject = node.getObject();
            if (ERepositoryObjectType.TDQ_SOURCE_FILE_ELEMENT.equals(viewObject.getRepositoryObjectType())) {
                IPath append = WorkbenchUtils.getFilePath(node);
                IFile file = ResourceManager.getRootProject().getFile(append);
                selectedFiles.add(file);
                isSelectFile = true;
            }
        }
        return isSelectFile;
    }
}
