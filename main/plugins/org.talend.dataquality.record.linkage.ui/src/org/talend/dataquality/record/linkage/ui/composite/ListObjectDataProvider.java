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
package org.talend.dataquality.record.linkage.ui.composite;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.talend.dataquality.PluginConstant;

/**
 * DOC yyin class global comment. Detailled comment
 *
 * @param <T>
 */
public class ListObjectDataProvider<T> extends ListDataProvider<T> {

    /**
     * DOC yyin ListObjectDataProvider constructor comment.
     *
     * @param list
     * @param columnAccessor
     */
    public ListObjectDataProvider(List<T> list, IColumnAccessor<T> columnAccessor) {
        super(list, columnAccessor);
    }

    @Override
    public Object getDataValue(int columnIndex, int rowIndex) {
        Object[] rowObj = (Object[]) list.get(rowIndex);
        if (columnIndex >= rowObj.length) {
            return null;
        }

        return rowObj[columnIndex] == null ? PluginConstant.NULL_STRING : rowObj[columnIndex];
    }
}
