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
package org.talend.dataprofiler.core.ui.editor.indicator;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;

/**
 * DOC klliu class global comment. Add 13429: Feature request: require the ability to create java UDIs that accept
 * parameters
 */
public class IndicatorParametersCellModifier implements ICellModifier {

    // FIXME remove it.
    private TableViewer viewer;

    public IndicatorParametersCellModifier(TableViewer viewer) {
        this.viewer = viewer;
    }

    public boolean canModify(Object element, String property) {
        return true;
    }

    public Object getValue(Object element, String property) {

        return null;
    }

    public void modify(Object element, String property, Object value) {

    }

}
