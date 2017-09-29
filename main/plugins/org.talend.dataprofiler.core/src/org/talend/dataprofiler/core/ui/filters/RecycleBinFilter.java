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
import org.eclipse.jface.viewers.Viewer;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.dq.helper.PropertyHelper;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class RecycleBinFilter extends AbstractViewerFilter {

    public static final int FILTER_ID = 5;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.filters.AbstractViewerFilter#getId()
     */
    @Override
    public int getId() {
        return FILTER_ID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof IFile) {
            IFile model = (IFile) element;
            Property property = PropertyHelper.getProperty(model);
            if (null != property && property.getItem().getState().isDeleted()) {
                return false;
            }
        }
        if (element instanceof RepositoryObject) {
            RepositoryObject obj = (RepositoryObject) element;
            Property property = obj.getProperty();
            if (null != property && property.getItem().getState().isDeleted()) {
                return false;
            }
        }
        return true;
    }

}
