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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.PieStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.utils.ComparatorsFactory;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.PluginConstant;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;
import org.talend.dq.indicators.preview.table.ChartDataEntity;

/**
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class PieStatisticsState extends AbstractChartTypeStates {

    private String title = PluginConstant.EMPTY_STRING;

    /**
     * @deprecated
     */
    @Deprecated
    public PieStatisticsState(List<IndicatorUnit> units) {
        super(units);
    }

    public PieStatisticsState(List<IndicatorUnit> units, String title) {
        super(units);
        this.title = title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChart()
     */
    public Object getChart() {
        return TOPChartUtils.getInstance().createPieChart(getTitle(), getPieDataset(), true, true, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getExampleChart()
     */
    public Object getExampleChart() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getDataExplorer()
     */
    public DataExplorer getDataExplorer() {
        return PieStatisticsStateUtil.getDataExplorer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getReferenceLink()
     */
    public String getReferenceLink() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPieDataset() {
        Map<String, Double> valueMap = new HashMap<String, Double>();
        for (IndicatorUnit unit : units) {
            if (unit.isExcuted()) {
                FrequencyExt[] frequencyExt = (FrequencyExt[]) unit.getValue();
                ComparatorsFactory.sort(frequencyExt, ComparatorsFactory.FREQUENCY_COMPARATOR_ID);
                int numOfShown = frequencyExt.length;
                for (int i = 0; i < numOfShown; i++) {
                    FrequencyExt freqExt = frequencyExt[i];
                    String keyLabel = PieStatisticsStateUtil.getkeyLabel(freqExt);
                    Double percent = freqExt.getFrequency();
                    valueMap.put(keyLabel, percent);
                }
            }
        }
        return TOPChartUtils.getInstance().createPieDataset(valueMap);
    }

    public ICustomerDataset getCustomerDataset() {
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();

        for (IndicatorUnit unit : units) {
            if (unit.isExcuted()) {
                FrequencyExt[] frequencyExt = (FrequencyExt[]) unit.getValue();

                int numOfShown = PieStatisticsStateUtil.getNumberOfShown(unit, frequencyExt);

                for (int i = 0; i < numOfShown; i++) {
                    FrequencyExt freqExt = frequencyExt[i];
                    String keyLabel = PieStatisticsStateUtil.getkeyLabel(freqExt);

                    customerdataset.addValue(freqExt.getValue(), PluginConstant.EMPTY_STRING, keyLabel);

                    ChartDataEntity entity = PieStatisticsStateUtil.createDataEntity(unit, freqExt, keyLabel);

                    customerdataset.addDataEntity(entity);
                }
            }
        }
        return customerdataset;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
