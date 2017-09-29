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
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.talend.dataquality.record.linkage.utils.BlockingKeyAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPostAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPreAlgorithmEnum;
import org.talend.dataquality.rules.BlockKeyDefinition;

/**
 * created by zshen on Aug 6, 2013 Detailled comment
 * 
 */
public class BlockingKeyTableLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof BlockKeyDefinition) {
            BlockKeyDefinition bkd = (BlockKeyDefinition) element;
            switch (columnIndex) {
            case 0:
                return bkd.getName();
            case 1:
                return bkd.getColumn();
            case 2:
                return BlockingKeyPreAlgorithmEnum.getTypeBySavedValue(bkd.getPreAlgorithm().getAlgorithmType()).getValue();
            case 3:
                return bkd.getPreAlgorithm().getAlgorithmParameters();
            case 4:
                return BlockingKeyAlgorithmEnum.getTypeBySavedValue(bkd.getAlgorithm().getAlgorithmType()).getValue();
            case 5:
                return bkd.getAlgorithm().getAlgorithmParameters();
            case 6:
                return BlockingKeyPostAlgorithmEnum.getTypeBySavedValue(bkd.getPostAlgorithm().getAlgorithmType()).getValue();
            case 7:
                return bkd.getPostAlgorithm().getAlgorithmParameters();
            }

        }
        return element == null ? StringUtils.EMPTY : element.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
     */
    @Override
    public Color getForeground(Object element, int columnIndex) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    @Override
    public Color getBackground(Object element, int columnIndex) {
        if (element instanceof BlockKeyDefinition) {
            BlockKeyDefinition bkd = (BlockKeyDefinition) element;
            boolean takeParameter = true;
            switch (columnIndex) {

            case 3:
                takeParameter = BlockingKeyPreAlgorithmEnum.getTypeBySavedValue(bkd.getPreAlgorithm().getAlgorithmType())
                        .isTakeParameter();
                break;

            case 5:
                takeParameter = BlockingKeyAlgorithmEnum.getTypeBySavedValue(bkd.getAlgorithm().getAlgorithmType())
                        .isTakeParameter();
                break;

            case 7:
                takeParameter = BlockingKeyPostAlgorithmEnum.getTypeBySavedValue(bkd.getPostAlgorithm().getAlgorithmType())
                        .isTakeParameter();
                break;
            }
            return getCellColor(takeParameter);
        }
        return null;
    }

    /**
     * DOC zshen Comment method "getCellColor".
     * 
     * @param takeParameter
     * @return
     */
    protected Color getCellColor(boolean takeParameter) {
        return Display.getDefault().getSystemColor(takeParameter ? SWT.COLOR_WHITE : SWT.COLOR_GRAY);
    }

}
