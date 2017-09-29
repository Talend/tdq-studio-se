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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.graphics.Color;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchRuleLabelProvider;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dq.helper.CustomAttributeMatcherHelper;

/**
 * created by zshen on Aug 27, 2013 Detailled comment
 * 
 */
public class MatchRuleDefinitionLabelProvider extends MatchRuleLabelProvider {

    boolean isAddThresholdColumn = false;

    public MatchRuleDefinitionLabelProvider(boolean isAddThresholdColumn) {
        this.isAddThresholdColumn = isAddThresholdColumn;
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
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.MatchRuleLabelProvider#getColumnText(
     * java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof MatchKeyDefinition) {
            MatchKeyDefinition mkd = (MatchKeyDefinition) element;
            if (isAddThresholdColumn) {
                switch (columnIndex) {
                case 0:// MatchAnalysisConstant.MATCH_KEY_NAME
                    return mkd.getName();
                case 1:// MatchAnalysisConstant.MATCHING_TYPE
                    return AttributeMatcherType.valueOf(mkd.getAlgorithm().getAlgorithmType()).getLabel();
                case 2:// MatchAnalysisConstant.CUSTOM_MATCHER
                    return CustomAttributeMatcherHelper.getClassName(mkd.getAlgorithm().getAlgorithmParameters());
                case 3:// MatchAnalysisConstant.TOKENIZATION_TYPE
                    return String.valueOf(mkd.getTokenizationType());
                case 4:// MatchAnalysisConstant.THRESHOLD
                    return String.valueOf(mkd.getThreshold());
                case 5:// MatchAnalysisConstant.CONFIDENCE_WEIGHT
                    return String.valueOf(mkd.getConfidenceWeight());
                case 6:// MatchAnalysisConstant.HANDLE_NULL
                    return HandleNullEnum.getTypeByValue(mkd.getHandleNull()).getLabel();
                }
            } else {
                switch (columnIndex) {
                case 0:// MatchAnalysisConstant.MATCH_KEY_NAME
                    return mkd.getName();
                case 1:// MatchAnalysisConstant.MATCHING_TYPE
                    return AttributeMatcherType.valueOf(mkd.getAlgorithm().getAlgorithmType()).getLabel();
                case 2:// MatchAnalysisConstant.CUSTOM_MATCHER
                    return CustomAttributeMatcherHelper.getClassName(mkd.getAlgorithm().getAlgorithmParameters());
                case 3:// MatchAnalysisConstant.TOKENIZATION_TYPE
                    return String.valueOf(mkd.getTokenizationType());
                case 4:// MatchAnalysisConstant.CONFIDENCE_WEIGHT
                    return String.valueOf(mkd.getConfidenceWeight());
                case 5:// MatchAnalysisConstant.HANDLE_NULL
                    return HandleNullEnum.getTypeByValue(mkd.getHandleNull()).getLabel();
                }
            }

        }
        return StringUtils.EMPTY;
    }

    /*
     * (non-Javadoc) differents with parent: lost one column: "column" , so columnIndex+1
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    @Override
    public Color getBackground(Object element, int columnIndex) {
        return super.getBackground(element, columnIndex + 1);
    }

}
