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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;

/**
 * created by zhao on Aug 22, 2013 Detailled comment
 * 
 */
public class GroupStatisticsTableLabelProvider extends LabelProvider implements ITableLabelProvider {

    private static Logger log = Logger.getLogger(GroupStatisticsTableLabelProvider.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        // No implementation.
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        GroupStatisticsRow groupStatRow = (GroupStatisticsRow) element;
        switch (columnIndex) {
        case 0:
            return String.valueOf(groupStatRow.getGroupSize());
        case 1:
            return String.valueOf(groupStatRow.getGroupCount());
        case 2:
            return String.valueOf(groupStatRow.getRecordCount());
        case 3:
            return String.valueOf(groupStatRow.getRecordPercentage());

        }
        log.fatal(DefaultMessagesImpl.getString("DuplicateRecordTableLabelProvider.INCORRECT_COLUMN_IDX") + columnIndex); //$NON-NLS-1$
        return StringUtils.EMPTY;
    }

}
