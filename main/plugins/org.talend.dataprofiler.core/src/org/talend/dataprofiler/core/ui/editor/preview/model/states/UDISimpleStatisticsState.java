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
package org.talend.dataprofiler.core.ui.editor.preview.model.states;

import java.util.List;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UDISimpleStatisticsState extends SimpleStatisticsState {

    @Override
    public Object getChart() {
        return getChart(getDataset());
    }

    @Override
    public Object getChart(Object dataset) {
        return TOPChartUtils.getInstance().createBarChart(
                DefaultMessagesImpl.getString("SimpleStatisticsState.UserIndicators"), dataset, false); //$NON-NLS-1$
    }

    /**
     * DOC xqliu UDISimpleStatisticsState constructor comment.
     * 
     * @param units
     */
    public UDISimpleStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

}
