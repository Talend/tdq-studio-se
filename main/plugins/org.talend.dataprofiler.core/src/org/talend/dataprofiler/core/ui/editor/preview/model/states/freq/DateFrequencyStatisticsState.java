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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.freq;

import java.util.List;

import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util.DateFrequencyStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dq.indicators.preview.EIndicatorChartType;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class DateFrequencyStatisticsState extends FrequencyStatisticsState {

    private EIndicatorChartType type;

    /**
     * DOC yyi DateFrequencyStatisticsState constructor comment.
     * 
     * @param units
     */
    public DateFrequencyStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    public DateFrequencyStatisticsState(List<IndicatorUnit> units, EIndicatorChartType type) {
        this(units);
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.FrequencyTypeStates#getCustomerDataset()
     */
    @Override
    public ICustomerDataset getCustomerDataset() {
        return DateFrequencyStateUtil.getCustomerDataset(units, ComparatorsFactory.FREQUENCY_COMPARATOR_ID);
    }

    @Override
    protected String getTitle() {
        return null == type ? DefaultMessagesImpl.getString("DateFrequencyStatisticsState.DateFrequencyStatistics") : type//$NON-NLS-1$
                .getLiteral();
    }
}
