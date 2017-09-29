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
package org.talend.dataprofiler.core.ui.editor.preview;

import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * It will be used for ColumnSet analysis and column correlation analysis.
 * 
 */
public class ColumnSetIndicatorUnit extends IndicatorUnit {

    /**
     * DOC talend ColumnSetIndicatorUnit constructor comment.
     * 
     * @param type
     * @param indicator
     */
    public ColumnSetIndicatorUnit(IndicatorEnum type, Indicator indicator) {
        super(type, indicator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit#getForms()
     */
    @Override
    public FormEnum[] getForms() {
        int sqlType = 0;
        DataminingType dataminingType = DataminingType.NOMINAL;
        IndicatorDefinition indicatorDefinition = this.getIndicator().getIndicatorDefinition();
        return FormEnum.getFormEnumArray(indicatorDefinition, sqlType, dataminingType, type, getExecutionLanguage());
    }

}
