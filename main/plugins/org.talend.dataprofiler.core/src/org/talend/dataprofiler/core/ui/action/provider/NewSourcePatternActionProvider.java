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

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.dataprofiler.core.pattern.actions.CreatePatternAction;
import org.talend.dataprofiler.core.pattern.actions.ExportPatternsAction;
import org.talend.dataprofiler.core.pattern.actions.ImportPatternsAction;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;
import org.talend.resource.ResourceService;

/**
 * DOC qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class NewSourcePatternActionProvider extends AbstractCommonActionProvider {

    protected static Logger log = Logger.getLogger(NewSourcePatternActionProvider.class);

    public static final String EXTENSION_PATTERN = FactoriesUtil.PATTERN;

    public NewSourcePatternActionProvider() {
    }

    public void fillContextMenu(IMenuManager menu) {
        // MOD mzhao user readonly role on svn repository mode.
        if (!isShowMenu()) {
            return;
        }
        TreeSelection treeSelection = ((TreeSelection) this.getContext().getSelection());
        List<IFile> selectedFiles = new ArrayList<IFile>();
        if (treeSelection.size() == 1) {
            Object obj = treeSelection.getFirstElement();
            if (obj instanceof RepositoryNode) {
                RepositoryNode node = (RepositoryNode) obj;
                if (ENodeType.SYSTEM_FOLDER.equals(node.getType()) || ENodeType.SIMPLE_FOLDER.equals(node.getType())) {
                    try {
                        IFolder folder = WorkbenchUtils.getFolder(node);
                        ExpressionType type = null;

                        if (ResourceService.isSubFolder(ResourceManager.getPatternRegexFolder(), folder)) {
                            type = ExpressionType.REGEXP;
                        } else if (ResourceService.isSubFolder(ResourceManager.getPatternSQLFolder(), folder)) {
                            type = ExpressionType.SQL_LIKE;
                        }

                        if (type != null) {
                            menu.add(new CreatePatternAction(folder, type));
                            menu.add(new ImportPatternsAction(node, type));
                            menu.add(new ExportPatternsAction(node, false));
                            menu.add(new ExportPatternsAction(node, true));
                        }
                    } catch (Exception e) {
                        log.error(e, e);
                    }
                }

            }
            // else if (obj instanceof IFile) {
            // IFile file = (IFile) obj;
            // if (EXTENSION_PATTERN.equalsIgnoreCase(file.getFileExtension())) {
            // // menu.add(new RenameSqlFileAction((IFile) obj));
            // }
            // }
        }
        boolean isSelectFile = computeSelectedFiles(treeSelection, selectedFiles);
        if (!isSelectFile && !selectedFiles.isEmpty()) {
            // menu.add(new OpenSqlFileAction(selectedFiles));
            // menu.add(new DeletePatternAction(selectedFiles));
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
            Object obj = iterator.next();
            if (obj instanceof IFile) {
                IFile file = (IFile) obj;
                if (EXTENSION_PATTERN.equalsIgnoreCase(file.getFileExtension())) {
                    selectedFiles.add(file);
                }
            } else {
                isSelectFile = true;
                break;
            }
        }
        return isSelectFile;
    }
}
