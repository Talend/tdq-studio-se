// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.record.linkage.ui.composite.tableviewer;

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
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.cellEditor.jarFileCellEditor;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorDefinition;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorshipTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.filter.ColumnsDateFilter;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.AnaMatchKeyAndSurvLabelProvider;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchAndSurvivorCellModifer;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.MatchRule;

/**
 * The implementation specific to analysis editor.
 */
public class AnaMatchKeyAndSurvTableViewer extends MatchKeyAndSurvivorshipTableViewer {

    /**
     * DOC zhao AnaMatchKeyAndSurvTableViewer constructor comment.
     * 
     * @param parent
     * @param style
     * @param isAddColumn
     */
    public AnaMatchKeyAndSurvTableViewer(Composite parent, int style, boolean isAddColumn, MatchRule matchRule) {
        super(parent, style, isAddColumn, matchRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorshipTableViewer#
     * getTableLabelProvider()
     */
    @Override
    protected IBaseLabelProvider getTableLabelProvider() {
        return new AnaMatchKeyAndSurvLabelProvider(isAddColumn());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorshipTableViewer#
     * getCellEditor(java.util.List, java.util.List)
     */
    @Override
    protected CellEditor[] getCellEditor(List<String> headers, List<MetadataColumn> columnList) {
        CellEditor[] editors = new CellEditor[headers.size()];
        for (int i = 0; i < editors.length; ++i) {
            // used for MDM T-swoosh
            switch (i) {
            case 1:// MatchAnalysisConstant.INPUT_COLUMN
                editors[i] = createColumnCombEditor(columnList, null);
                break;
            case 2:// MatchAnalysisConstant.MATCHING_TYPE
                editors[i] = new ComboBoxCellEditor(innerTable, AttributeMatcherType.getAllTypes(), SWT.READ_ONLY);
                break;
            case 3:// MatchAnalysisConstant.CUSTOM_MATCHER
                editors[i] = new jarFileCellEditor(innerTable, SWT.READ_ONLY);
                break;
            case 4:// MatchAnalysisConstant.TOKENIZATION_TYPE
                editors[i] = new ComboBoxCellEditor(innerTable, TokenizedResolutionMethod.getAllTypes(), SWT.READ_ONLY);
                break;
            case 7:// MatchAnalysisConstant.HANDLE_NULL
                editors[i] = new ComboBoxCellEditor(innerTable, HandleNullEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            case 8:// MatchAnalysisConstant.FUNCTION
                editors[i] = new ComboBoxCellEditor(innerTable, SurvivorShipAlgorithmEnum.getAllTypes(), SWT.READ_ONLY);
                break;
            case 9:// MatchAnalysisConstant.REFERENCE_COLUMN show date type only
                editors[i] = createColumnCombEditor(columnList, new ColumnsDateFilter());
                break;
            default:// MatchAnalysisConstant.THRESHOLD MatchAnalysisConstant.CONFIDENCE_WEIGHT and
                    // MatchAnalysisConstant.PARAMETER
                editors[i] = new TextCellEditor(innerTable);
            }

        }
        return editors;
    }

    @Override
    protected AbstractMatchCellModifier<MatchKeyAndSurvivorDefinition> getTableCellModifier() {
        return new MatchAndSurvivorCellModifer(this);
    }

}
