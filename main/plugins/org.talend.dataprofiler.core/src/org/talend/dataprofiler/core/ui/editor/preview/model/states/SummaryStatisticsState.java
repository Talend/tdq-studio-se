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

import org.apache.log4j.Logger;
import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.dataset.CustomerDefaultBAWDataset;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.SummaryStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.utils.sql.Java2SqlType;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public class SummaryStatisticsState extends AbstractChartTypeStates {

    private static Logger log = Logger.getLogger(SummaryStatisticsState.class);

    // when all the summary indicators is selected, include the catalog, the number should be 8
    public static final int FULL_FLAG = 8;

    public static final int FULL_CHART = 6;

    private int sqltype;

    private SummaryStatisticsStateUtil summaryUtil;

    private boolean isSupportDynamicChart = false;

    private Map<IndicatorUnit, String> indicators2ValueMap = new HashMap<IndicatorUnit, String>();;

    /**
     * Sets the sqltype.
     * 
     * @param sqltype the sqltype to set
     */
    public void setSqltype(int sqltype) {
        this.sqltype = sqltype;
    }

    public SummaryStatisticsState(List<IndicatorUnit> units) {
        summaryUtil = new SummaryStatisticsStateUtil();
        if (units != null) {
            this.units.addAll(summaryUtil.check(units));
        }

        sqltype = summaryUtil.findSqlType(units);

    }

    public SummaryStatisticsState(List<IndicatorUnit> units, Map<IndicatorUnit, String> indicators2ValueMap) {
        this(units);
        this.indicators2ValueMap = indicators2ValueMap;

    }

    public Object getChart() {
        return getChart(getDataset());
    }

    @Override
    public Object getChart(Object dataset) {
        if (Java2SqlType.isDateInSQL(sqltype)) {
            return null;
        } else {
            if (isIntact() && !isSupportDynamicChart) {
                return TOPChartUtils.getInstance().createBoxAndWhiskerChart(
                        DefaultMessagesImpl.getString("SummaryStatisticsState.SummaryStatistics"), getDataset()); //$NON-NLS-1$
            } else {
                Object barChart = TOPChartUtils.getInstance().createBarChart(
                        DefaultMessagesImpl.getString("SummaryStatisticsState.Summary_Statistics"), dataset, false); //$NON-NLS-1$
                TOPChartUtils.getInstance().setDisplayDecimalFormatOfChart(barChart);
                return barChart;
            }
        }
    }

    public ICustomerDataset getCustomerDataset() {
        Map<IndicatorEnum, Double> map = new HashMap<IndicatorEnum, Double>();
        CustomerDefaultCategoryDataset customerdataset = new CustomerDefaultCategoryDataset();
        for (IndicatorUnit unit : units) {
            // MOD xqliu 2009-06-29 bug 7068
            String value = null;
            if (indicators2ValueMap.containsKey(unit)) {
                value = indicators2ValueMap.get(unit);
            } else {
                value = summaryUtil.getUnitValue(unit);
            }
            if (Java2SqlType.isNumbericInSQL(sqltype)) {
                try {
                    map.put(unit.getType(), Double.parseDouble(value));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            ChartDataEntity entity = summaryUtil.createDataEntity(unit, value);

            customerdataset.addDataEntity(entity);
        }

        if (isIntact() && !isSupportDynamicChart) {

            CustomerDefaultBAWDataset dataset = new CustomerDefaultBAWDataset(map.get(IndicatorEnum.MeanIndicatorEnum),
                    map.get(IndicatorEnum.MedianIndicatorEnum), map.get(IndicatorEnum.LowerQuartileIndicatorEnum),
                    map.get(IndicatorEnum.UpperQuartileIndicatorEnum), map.get(IndicatorEnum.MinValueIndicatorEnum),
                    map.get(IndicatorEnum.MaxValueIndicatorEnum), customerdataset);

            dataset.addDataEntity(customerdataset.getDataEntities());
            return dataset;
        } else {
            // MOD hcheng,Range indicator value should not appear in bar chart
            if (!isIntact()) {
                map.remove(IndicatorEnum.RangeIndicatorEnum);
                map.remove(IndicatorEnum.IQRIndicatorEnum);
            }
            for (IndicatorEnum indicatorEnum : map.keySet()) {
                customerdataset.addValue(map.get(indicatorEnum), indicatorEnum.getLabel(), indicatorEnum.getLabel());
            }
            return customerdataset;
        }
    }

    public DataExplorer getDataExplorer() {
        return summaryUtil.getDataExplorer(sqltype);
    }

    public Object getExampleChart() {
        // TODO Auto-generated method stub
        return null;
    }

    private boolean isIntact() {
        return units.size() == FULL_FLAG && summaryUtil.isMeaning();
    }

    public String getReferenceLink() {
        String url = null;

        if (isIntact()) {
            url = "http://en.wikipedia.org/wiki/Box_plot"; //$NON-NLS-1$
        } else {
            url = "http://en.wikipedia.org/wiki/Histogram"; //$NON-NLS-1$
        }
        return url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChart(org.talend.dataprofiler
     * .common.ui.editor.preview.ICustomerDataset)
     */
    public Object getChart(ICustomerDataset dataset) {
        return null;
    }

    /**
     * Sets the isSupportDynamicChart.
     * 
     * @param isSupportDynamicChart the isSupportDynamicChart to set
     */
    public void setSupportDynamicChart(boolean isSupportDynamicChart) {
        this.isSupportDynamicChart = isSupportDynamicChart;
    }

}
