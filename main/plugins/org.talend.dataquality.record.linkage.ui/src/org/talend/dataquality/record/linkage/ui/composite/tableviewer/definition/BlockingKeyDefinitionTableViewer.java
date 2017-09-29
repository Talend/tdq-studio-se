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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.BlockingKeyTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition.BlockingKeyTableDefinitionLabelProvider;
import org.talend.dataquality.record.linkage.utils.BlockingKeyAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPostAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPreAlgorithmEnum;

/**
 * created by zshen on Aug 26, 2013 Detailled comment
 * 
 */
public class BlockingKeyDefinitionTableViewer extends BlockingKeyTableViewer {

    /**
     * DOC zshen BlockingKeyDefinitionTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     * @param isAddColumn
     */
    public BlockingKeyDefinitionTableViewer(Composite parent, int style, boolean isAddColumn) {
        super(parent, style, isAddColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.BlockingKeyTableViewer#getCellEditor(java.util
     * .List)
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] editors = new CellEditor[headers.size()];
        for (int i = 0; i < editors.length; ++i) {
            switch (i) {
            case 1:
                editors[i] = new ComboBoxCellEditor(innerTable, BlockingKeyPreAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            case 3:
                editors[i] = new ComboBoxCellEditor(innerTable, BlockingKeyAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            case 5:
                editors[i] = new ComboBoxCellEditor(innerTable, BlockingKeyPostAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            default:
                editors[i] = new TextCellEditor(innerTable);
            }
        }
        return editors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.BlockingKeyTableViewer#getTableLabelProvider()
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        return new BlockingKeyTableDefinitionLabelProvider();
    }

}
