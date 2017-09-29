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

import org.talend.dataprofiler.core.helper.ModelElementIndicatorHelper;
import org.talend.dataprofiler.core.model.ColumnIndicator;
import org.talend.dataprofiler.core.model.DelimitedFileIndicator;
import org.talend.dataprofiler.core.model.ModelElementIndicator;
import org.talend.dataprofiler.core.ui.wizard.indicator.forms.FormEnum;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.DataminingType;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;

/**
 * It will be used for column analysis
 */
public class ColumnIndicatorUnit extends IndicatorUnit {

    private ModelElementIndicator modelElementIndicator;

    public ColumnIndicatorUnit(IndicatorEnum type, Indicator indicator, ModelElementIndicator modelElementIndicator) {
        super(type, indicator);
        this.modelElementIndicator = modelElementIndicator;

    }

    /**
     * Getter for modelElementIndicator.
     * 
     * @return the modelElementIndicator
     */
    public ModelElementIndicator getModelElementIndicator() {
        return this.modelElementIndicator;
    }

    public boolean isColumn() {
        return this.modelElementIndicator instanceof ColumnIndicator;
    }

    public boolean isMetadataColumn() {
        return this.modelElementIndicator instanceof DelimitedFileIndicator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit#getForms()
     */
    @Override
    public FormEnum[] getForms() {
        int sqlType = null != modelElementIndicator ? modelElementIndicator.getJavaType() : 0;
        ColumnIndicator columnIndicator = ModelElementIndicatorHelper.switchColumnIndicator(this);
        DataminingType dataminingType = columnIndicator == null ? null : MetadataHelper.getDataminingType(columnIndicator
                .getTdColumn());
        if (dataminingType == null) {
            dataminingType = null != modelElementIndicator ? MetadataHelper.getDefaultDataminingType(modelElementIndicator
                    .getJavaType()) : DataminingType.NOMINAL;
        }

        return FormEnum.getFormEnumArray(this.getIndicator().getIndicatorDefinition(), sqlType, dataminingType, type,
                getExecutionLanguage());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (ColumnIndicatorUnit.class.isInstance(obj)) {
            if (!type.equals(((ColumnIndicatorUnit) obj).type)) {
                return false;
            }
            if (!indicator.equals(((ColumnIndicatorUnit) obj).indicator)) {
                return false;
            }
            if (!modelElementIndicator.equals(((ColumnIndicatorUnit) obj).modelElementIndicator)) {
                return false;
            }
            return true;
        }

        return super.equals(obj);
    }

}
