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
package org.talend.dataprofiler.core.ui.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.dataprofiler.core.ui.utils.WorkbenchUtils;
import org.talend.dataquality.properties.TDQFileItem;
import org.talend.dq.helper.FileUtils;
import org.talend.repository.model.RepositoryNode;
import org.talend.resource.ResourceManager;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public abstract class AbstractImportSourceFileAction extends AbstractImportFileAction {

    private static String historyFilePath = "./";

    /**
     * DOC bZhou AddSqlFileAction constructor comment.
     * 
     * @param folder
     */
    public AbstractImportSourceFileAction(RepositoryNode node) {
        super(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractImportFileAction#computeFilePath()
     */
    @Override
    public Map<File, IPath> computeFilePath() throws Exception {
        Map<File, IPath> resultMap = new HashMap<File, IPath>();
        FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());

        if (historyFilePath != null) {
            dialog.setFilterPath(historyFilePath);
        }

        if (getFilterExtensions() != null) {
            dialog.setFilterExtensions(getFilterExtensions());
        }
        String path = dialog.open();

        if (path != null) {
            File file = new File(path);
            historyFilePath = file.getParent();

            IFolder folder = WorkbenchUtils.getFolder(node);
            IPath filePath = folder.getFullPath().makeRelativeTo(ResourceManager.getSourceFileFolder().getFullPath());
            resultMap.put(file, filePath);
        }

        return resultMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.action.AbstractImportFileAction#initItem(java.io.File)
     */
    @Override
    public Item initItem(File srcFile) throws Exception {
        TDQFileItem fileItem = createTDQFileItem();

        fileItem.setExtension(FileUtils.getExtension(srcFile));
        fileItem.setName(FileUtils.getName(srcFile));
        ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
        byteArray.setInnerContentFromFile(srcFile);
        fileItem.setContent(byteArray);

        return fileItem;
    }

    protected abstract TDQFileItem createTDQFileItem();

    protected abstract String[] getFilterExtensions();
}
