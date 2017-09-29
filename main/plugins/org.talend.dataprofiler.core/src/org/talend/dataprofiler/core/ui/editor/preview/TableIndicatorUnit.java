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

import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.IndicatorCommonUtil;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * It will be used for the Table Analysis
 */
public class TableIndicatorUnit extends IndicatorUnit {

    private TableIndicator parentTable;

    public TableIndicatorUnit(IndicatorEnum type, Indicator indicator, TableIndicator parentTable) {
        super(type, indicator);
        this.parentTable = parentTable;
    }

    /**
     * Getter for parentTable.
     * 
     * @return the parentTable
     */
    public TableIndicator getParentTable() {
        return this.parentTable;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    @Override
    public Object getValue() {
        return IndicatorCommonUtil.getIndicatorValue(indicator);
    }

    /**
     * Getter for children.
     * 
     * @return the children
     */
    @Override
    public TableIndicatorUnit[] getChildren() {
        return (TableIndicatorUnit[]) children;
    }

    /**
     * Sets the children.
     * 
     * @param children the children to set
     */
    public void setChildren(TableIndicatorUnit[] children) {
        this.children = children;
    }

    @Override
    public boolean isExcuted() {
        return !indicator.getInstantiatedExpressions().isEmpty();
    }

    /**
     * DOC xqliu Comment method "geIndicatorCount".
     * 
     * @return
     */
    public long geIndicatorCount() {
        long count = 0;
        if (IndicatorEnum.WhereRuleIndicatorEnum.equals(this.getType())) {
            count = this.getIndicator().getCount();
        }
        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit#getForms()
     */
    @Override
    public FormEnum[] getForms() {
        FormEnum[] forms = null;
        switch (this.getType()) {
        case RowCountIndicatorEnum:
        case WhereRuleIndicatorEnum:
            forms = new FormEnum[] { FormEnum.IndicatorThresholdsForm };
            break;
        default:
        }
        return forms;
    }
}
