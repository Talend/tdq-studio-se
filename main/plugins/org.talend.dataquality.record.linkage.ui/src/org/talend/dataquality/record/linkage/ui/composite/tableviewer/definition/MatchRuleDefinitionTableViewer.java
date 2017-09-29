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
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.constant.TokenizedResolutionMethod;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.cellEditor.jarFileCellEditor;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition.MatchRuleDefinitionLabelProvider;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;

/**
 * created by zshen on Aug 26, 2013 Detailled comment
 * 
 */
public class MatchRuleDefinitionTableViewer extends MatchRuleTableViewer {

    /**
     * DOC zshen MatchRuleDefinitionTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     * @param isAddColumn
     */
    public MatchRuleDefinitionTableViewer(Composite parent, int style, boolean isAddColumn) {
        super(parent, style, isAddColumn);
    }

    /*
     * (non-Javadoc)
     * headers.add(MatchAnalysisConstant.MATCH_KEY_NAME); // 14
     * headers.add(MatchAnalysisConstant.MATCHING_TYPE); // 12
     * headers.add(MatchAnalysisConstant.CUSTOM_MATCHER); // 20
     * headers.add(MatchAnalysisConstant.TOKENIZATION_TYPE); // 20
     * if (isAddColumn()) {
     * headers.add(MatchAnalysisConstant.THRESHOLD); // 14
     * }
     * headers.add(MatchAnalysisConstant.CONFIDENCE_WEIGHT); // 17
     * headers.add(MatchAnalysisConstant.HANDLE_NULL); // 11
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getCellEditor(java.util.List)
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] editors = new CellEditor[headers.size()];
        for (int i = 0; i < editors.length; ++i) {
            if (isAddColumn()) {
                switch (i) {
                case 1:// MatchAnalysisConstant.MATCHING_TYPE
                    editors[i] = new ComboBoxCellEditor(innerTable, AttributeMatcherType.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 2:// MatchAnalysisConstant.CUSTOM_MATCHER
                    editors[i] = new jarFileCellEditor(innerTable, SWT.READ_ONLY);
                    break;
                case 3:// MatchAnalysisConstant.TOKENIZATION_TYPE
                    editors[i] = new ComboBoxCellEditor(innerTable, TokenizedResolutionMethod.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 6:// MatchAnalysisConstant.HANDLE_NULL
                    editors[i] = new ComboBoxCellEditor(innerTable, HandleNullEnum.getAllTypes(), SWT.READ_ONLY);
                    break;
                default:
                    editors[i] = new TextCellEditor(innerTable);
                }
            } else {
                switch (i) {

                case 1:
                    editors[i] = new ComboBoxCellEditor(innerTable, AttributeMatcherType.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 2:
                    editors[i] = new jarFileCellEditor(innerTable, SWT.READ_ONLY);
                    break;
                case 3:// MatchAnalysisConstant.TOKENIZATION_TYPE
                    editors[i] = new ComboBoxCellEditor(innerTable, TokenizedResolutionMethod.getAllTypes(), SWT.READ_ONLY);
                    break;
                case 5:
                    editors[i] = new ComboBoxCellEditor(innerTable, HandleNullEnum.getAllTypes(), SWT.READ_ONLY);
                    break;
                default:
                    editors[i] = new TextCellEditor(innerTable);
                }

            }
        }
        return editors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataquality.record.linkage.ui.composite.tableviewer.MatchRuleTableViewer#getTableLabelProvider()
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        return new MatchRuleDefinitionLabelProvider(isAddColumn());
    }

}
