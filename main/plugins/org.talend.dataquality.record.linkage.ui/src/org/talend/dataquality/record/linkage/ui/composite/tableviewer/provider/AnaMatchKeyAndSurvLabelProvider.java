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
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorDefinition;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition.MatchKeyAndSurvivorLabelProvider;

/**
 * The provider specific for anlaysis editor.
 * 
 */
public class AnaMatchKeyAndSurvLabelProvider extends MatchKeyAndSurvivorLabelProvider {

    /**
     * DOC zhao AnaMatchKeyAndSurvLabelProvider constructor comment.
     * 
     * @param isAddThresholdColumn
     */
    public AnaMatchKeyAndSurvLabelProvider(boolean isAddThresholdColumn) {
        super(isAddThresholdColumn);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider.definition.MatchKeyAndSurvivorLabelProvider
     * #getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof MatchKeyAndSurvivorDefinition) {
            MatchKeyAndSurvivorDefinition mkd = (MatchKeyAndSurvivorDefinition) element;
            switch (columnIndex) {
            case 0:// MatchAnalysisConstant.MATCH_KEY_NAME
                return mkd.getMatchKey().getName();
            case 1:// MatchAnalysisConstant.INPUT_COLUMN
                return mkd.getColumn();
            default:
                return super.getColumnText(element, columnIndex - 1);
            }
        }
        return StringUtils.EMPTY;

    }

}
