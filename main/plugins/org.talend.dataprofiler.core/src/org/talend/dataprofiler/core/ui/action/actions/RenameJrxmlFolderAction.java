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

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.talend.dataprofiler.core.ui.utils.RepNodeUtils;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.nodes.JrxmlTempleteRepNode;
import org.talend.repository.model.RepositoryNode;

/**
 * Update all reports if them used the jrxml files in this renamed folder.
 */
public class RenameJrxmlFolderAction extends RenameTdqFolderAction {

    private List<String> jrxmlFileNames = null;

    private List<JrxmlTempleteRepNode> jrxmlFileRepNodes = null;

    private String newNameOfFolder;

    private IPath oldPath;

    /**
     * DOC yyin RenameJrxmlFolderAction constructor comment.
     * 
     * @param node
     */
    public RenameJrxmlFolderAction(RepositoryNode node) {
        super(node);
    }

    /**
     * remember the old name, and use it to compare with the report, if it uses the jrxml, change its path to new named
     * one
     */
    @Override
    protected void doRun() {

        // IPath oldPath = RepositoryNodeHelper.getPath(repositoryNode);
        // find all jrxml files in this folder
        jrxmlFileRepNodes = RepositoryNodeHelper.getJrxmlFileRepNodes(repositoryNode, true);
        oldPath = RepositoryNodeHelper.getPath(repositoryNode);
        // remember the files name
        jrxmlFileNames = RepNodeUtils.getListOfJrxmlNameWithPath(oldPath, jrxmlFileRepNodes);

        super.doRun();

        newNameOfFolder = this.getNewNameOfFolder();
    }

    @Override
    public void run() {
        super.run();

        // means that the action is cancelled
        if (newNameOfFolder == null) {
            return;
        }

        // get the new folder name
        IPath newPath = oldPath.removeLastSegments(1).append(newNameOfFolder);

        // use two array :old file names and new file names, to call the method.
        List<String> jrxmlFileNamesAfterMove = RepNodeUtils.getListOfJrxmlNewNameWithPath(oldPath, newPath, jrxmlFileRepNodes);

        // update the depended reports
        RepNodeUtils.updateJrxmlRelatedReport(jrxmlFileNames, jrxmlFileNamesAfterMove);
    }
}
