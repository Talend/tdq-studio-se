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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.sorter;

import java.util.List;

import org.eclipse.jface.viewers.ViewerSorter;


/**
 * created by zshen on Aug 22, 2013
 * Detailled comment
 *
 */
public class KeyDefinitionTableViewerSorter<T> extends ViewerSorter {

    List<T> keyDefList = null;



    public KeyDefinitionTableViewerSorter(List<T> keyDefList) {
        this.keyDefList = keyDefList;
    }


    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
     */
    @Override
    public int category(Object element) {
        return keyDefList.indexOf(element);
    }

}
