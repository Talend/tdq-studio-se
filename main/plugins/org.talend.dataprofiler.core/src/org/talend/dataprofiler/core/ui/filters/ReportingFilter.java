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

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.Viewer;
import org.talend.resource.ResourceManager;

/**
 * DOC rli class global comment. Detailled comment
 */
public class ReportingFilter extends AbstractViewerFilter {

    protected static Logger log = Logger.getLogger(ReportingFilter.class);

    public static final int FILTER_ID = 2;

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

            if (ResourceManager.isReportsFolder(folder) || ResourceManager.isJRXMLFolder(folder)) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.views.filters.IFilter#getId()
     */
    public int getId() {
        return FILTER_ID;
    }

}
