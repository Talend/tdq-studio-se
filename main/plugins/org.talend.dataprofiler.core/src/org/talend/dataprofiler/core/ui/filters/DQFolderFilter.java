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
package org.talend.dataprofiler.core.ui.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.commons.utils.io.FilesUtils;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class DQFolderFilter extends ViewerFilter {

    private static final String XSD_FOLDER_NAME = ".xsd";//$NON-NLS-1$ 

    private static final String REPORTS_FOLDER_NAME = "Reports";//$NON-NLS-1$ 

    private boolean isShowFile;

    public DQFolderFilter() {
        this(false);
    }

    public DQFolderFilter(boolean isShowFile) {
        this.isShowFile = isShowFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof IFolder) {
            IFolder folder = (IFolder) element;
            // MOD mzhao feature 10238, filter xsd folder
            // MOD yyi 2010-02-09 11538, filter Reports folder
            return !FilesUtils.isSVNFolder(folder) && !folder.getName().endsWith(XSD_FOLDER_NAME)
                    && !folder.getName().endsWith(REPORTS_FOLDER_NAME);
        }

        if (element instanceof IFile && isShowFile) {
            IFile file = (IFile) element;
            return !file.getFileExtension().equals(FactoriesUtil.PROPERTIES_EXTENSION);
        }

        return isShowFile;
    }

}
