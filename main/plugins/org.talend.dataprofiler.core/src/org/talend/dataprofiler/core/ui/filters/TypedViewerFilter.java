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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Viewer filter used in selection dialogs.
 */
public class TypedViewerFilter extends ViewerFilter {

    private Class<Object>[] acceptedTypes;

    private Object[] rejectedElements;

    /**
     * Creates a filter that only allows elements of gives types.
     * 
     * @param acceptedTypes The types of accepted elements
     */
    public TypedViewerFilter(Class<Object>[] acceptedTypes) {
        this(acceptedTypes, null);
    }

    /**
     * Creates a filter that only allows elements of gives types, but not from a list of rejected elements.
     * 
     * @param acceptedTypes Accepted elements must be of this types
     * @param rejectedElements Element equals to the rejected elements are filtered out
     */
    public TypedViewerFilter(Class<Object>[] acceptedTypes, Object[] rejectedElements) {
        Assert.isNotNull(acceptedTypes);
        this.acceptedTypes = acceptedTypes;
        this.rejectedElements = rejectedElements;
    }

    /**
     * @see ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (rejectedElements != null) {
            for (int i = 0; i < rejectedElements.length; i++) {
                if (element.equals(rejectedElements[i])) {
                    return false;
                }
            }
        }
        for (int i = 0; i < acceptedTypes.length; i++) {
            if (acceptedTypes[i].isInstance(element)) {
                return true;
            }
        }
        return false;
    }

}
