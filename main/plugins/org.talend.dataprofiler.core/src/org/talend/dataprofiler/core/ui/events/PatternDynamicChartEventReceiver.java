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
package org.talend.dataprofiler.core.ui.events;

import java.util.ArrayList;
import java.util.List;

import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.model.TableWithData;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.indicators.ext.PatternMatchingExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.PatternChartDataEntity;


/**
 * DOC zshen  class global comment. Detailled comment
 */
public class PatternDynamicChartEventReceiver extends DynamicChartEventReceiver {

    private List<Indicator> patternInticators = new ArrayList<Indicator>();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver#settingDatasetValue()
     */
    @Override
    protected void settingDatasetValue(Object indValue) {
        if (indValue instanceof PatternMatchingExt) {
            PatternMatchingExt patternExt = (PatternMatchingExt) indValue;
            TOPChartUtils.getInstance().setValue(dataset, patternExt.getNotMatchingValueCount(),
                    DefaultMessagesImpl.getString("PatternStatisticsState.NotMatching"), this.indicatorName);//$NON-NLS-1$
            TOPChartUtils.getInstance().setValue(dataset, patternExt.getMatchingValueCount(),
                    DefaultMessagesImpl.getString("PatternStatisticsState.Matching"), this.indicatorName);//$NON-NLS-1$
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver#refreshTable(java.lang.String)
     */
    @Override
    public void refreshTable(String value) {
        TableWithData input = (TableWithData) tableViewer.getInput();
        if (input != null) {
            ChartDataEntity[] dataEntities = input.getEnity();
            if (dataEntities != null && dataEntities.length > getIndexInDataset()) {
                ((PatternChartDataEntity) dataEntities[getIndexInDataset()]).setNumMatch(value);
                ((PatternChartDataEntity) dataEntities[getIndexInDataset()]).setNumNoMatch(value);
                tableViewer.getTable().clearAll();
                tableViewer.setInput(input);
            }
        }
    }

    private void refreshPatternTable(PatternMatchingExt matchExt) {
        TableWithData input = (TableWithData) tableViewer.getInput();
        if (input != null) {
            ChartDataEntity[] dataEntities = input.getEnity();
            if (dataEntities != null && dataEntities.length > getIndexInDataset()) {
                ((PatternChartDataEntity) dataEntities[getIndexInDataset()]).setNumMatch(String.valueOf(matchExt
                        .getMatchingValueCount()));
                ((PatternChartDataEntity) dataEntities[getIndexInDataset()]).setNumNoMatch(String.valueOf(matchExt
                        .getNotMatchingValueCount()));
                tableViewer.getTable().clearAll();
                tableViewer.setInput(input);
            }
        }
    }

    @Override
    public boolean handle(Object value) {
        Object indValue = value;
        if (value == null) {
            indValue = 0;
        }
        if (dataset != null) {
            settingDatasetValue(indValue);
        }
        if (tableViewer != null) {
            if (value != null && value instanceof PatternMatchingExt) {
                refreshPatternTable((PatternMatchingExt) value);
            } else {
                refreshTable(NAN_STRING);
            }
        }
        // need to refresh the parent composite of the chart to show the changes
        EventManager.getInstance().publish(chartComposite, EventEnum.DQ_DYNAMIC_REFRESH_DYNAMIC_CHART, null);

        return true;
    }

    private String[] getRowKeys() {
        // The value be definition at PatternStatisticsState line 55-57
        return new String[] { DefaultMessagesImpl.getString("PatternStatisticsState.NotMatching"), //$NON-NLS-1$
                DefaultMessagesImpl.getString("PatternStatisticsState.Matching") }; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.events.DynamicChartEventReceiver#clearDataset()
     */
    @Override
    protected void clearDataset() {
        for (String rowKey : getRowKeys()) {
            TOPChartUtils.getInstance().setValue(dataset, 0.0, rowKey, getColumnKey());
        }
    }


}
