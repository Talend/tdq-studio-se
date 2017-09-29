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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.dataset.CustomerXYSeriesCollection;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.CommonStateUtil;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.utils.WhereRuleStatisticsStateUtil;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.indicators.preview.table.WhereRuleChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.utils.format.StringFormatUtil;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class WhereRuleStatisticsStateTable extends AbstractChartTypeStatesTable {

    public static final String ROW_KEY_PASS = DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.match"); //$NON-NLS-1$

    public static final String ROW_KEY_NOT_PASS = DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.notMatch"); //$NON-NLS-1$

    private TableIndicator tableIndicator;

    private Long rowCount;

    public TableIndicator getTableIndicator() {
        return tableIndicator;
    }

    public Long getRowCount() {
        return rowCount;
    }

    public WhereRuleStatisticsStateTable(List<TableIndicatorUnit> units, TableIndicator tableIndicator) {
        super(units);
        this.tableIndicator = tableIndicator;
        this.rowCount = WhereRuleStatisticsStateUtil.initRowCount(tableIndicator);
    }

    @Override
    public Object getChart() {
        List<Object> chartList = getChartList();
        if (chartList != null && chartList.size() > 0) {
            return chartList.get(0);
        }
        return null;
    }

    public ICustomerDataset getCustomerDataset() {
        // MOD xqliu 2010-03-10 feature 10834
        CustomerDefaultCategoryDataset customerDataset = new CustomerDefaultCategoryDataset();
        for (TableIndicatorUnit unit : units) {
            addDataEntity2CustomerDataset(customerDataset, unit);
        }
        return customerDataset;
        // ~10834
    }

    /**
     * DOC xqliu Comment method "getCustomerDatasetRownCount".
     * 
     * @return
     */
    public ICustomerDataset getCustomerDatasetRownCount() {
        CustomerDefaultCategoryDataset customerDataset = new CustomerDefaultCategoryDataset();
        TableIndicatorUnit rownCountUnit = getRownCountUnit(units);
        if (rownCountUnit != null) {
            addRownCountDataEntity2CustomerDataset(customerDataset, rownCountUnit);
        }
        return customerDataset;
    }

    @Override
    public ICustomerDataset getCustomerXYDataset() {
        Map<Integer, Double> valueMap = new HashMap<Integer, Double>();

        int x = 0;
        for (TableIndicatorUnit unit : units) {
            x++;
            double y = 100 * (1 - (Double.parseDouble(unit.getValue().toString()) / getRowCount()));
            valueMap.put(x, y);
        }
        final CustomerXYSeriesCollection dataset = new CustomerXYSeriesCollection(valueMap);

        return dataset;
    }

    public DataExplorer getDataExplorer() {
        return WhereRuleStatisticsStateUtil.getDataExplorer();
    }

    public Object getExampleChart() {
        return null;
    }

    public String getReferenceLink() {
        return null;
    }

    private List<Object> tempDatasets;

    // better call this after getChartList()
    public List<Object> getTempDatasetList() {
        return tempDatasets;
    }

    @Override
    public List<Object> getChartList() {
        // MOD xqliu 2010-03-17 feature 10834
        tempDatasets = getOptimizeShowDataset();
        return getChartList(tempDatasets);
    }

    /**
     * DOC xqliu Comment method "getOptimizeShowDataset". ADD xqliu 2010-03-10 feature 10834
     * 
     * @return
     */
    private List<Object> getOptimizeShowDataset() {
        List<Object> result = new ArrayList<Object>();
        // get the page size
        int size = getSizeOfDQRulePerChart();

        // Add RowCountIndicator dataset
        CustomerDefaultCategoryDataset customerDatasetRownCount = new CustomerDefaultCategoryDataset();
        // MOD msjian TDQ-5119: fix a NPE
        if (units != null && units.size() > 0) {
            addRownCountDataEntity2CustomerDataset(customerDatasetRownCount, getRownCountUnit(units));
            result.add(customerDatasetRownCount);

            // MOD xqliu 2012-04-23 TDQ-5057: don't include RowCountUnit
            List<TableIndicatorUnit> whereRuleUnits = removeRowCountUnit(units);
            int totalNum = whereRuleUnits.size();
            int pageNum = totalNum % size == 0 ? totalNum / size : totalNum / size + 1;
            for (int i = 0; i < pageNum; i++) {
                CustomerDefaultCategoryDataset customerDataset = new CustomerDefaultCategoryDataset();
                for (int j = 0; j < size; ++j) {
                    int index = i * size + j;
                    if (index < totalNum) {
                        addDataEntity2CustomerDataset(customerDataset, whereRuleUnits.get(index));
                    } else {
                        break;
                    }
                }
                result.add(customerDataset);
            }
        }
        // TDQ-5119~

        return result;
    }

    /**
     * DOC yyin Comment method "getSizeOfDQRule".
     * 
     * @return
     */
    public static int getSizeOfDQRulePerChart() {
        String dqruleSize = EditorPreferencePage.getDQRuleSize();
        int maxSize = Integer.MAX_VALUE;
        int size = maxSize;
        try {
            size = Integer.parseInt(dqruleSize);
            if (size < 1) {
                size = maxSize;
            }
        } catch (NumberFormatException e) {
            size = maxSize;
        }
        return size;
    }

    public List<List<Indicator>> getPagedIndicators() {

        return WhereRuleStatisticsStateUtil.getPagedIndicators(units);
    }

    /**
     * DOC xqliu Comment method "removeRowCountUnit".
     * 
     * @param units1
     * @return
     */
    public List<TableIndicatorUnit> removeRowCountUnit(List<TableIndicatorUnit> units1) {
        return WhereRuleStatisticsStateUtil.removeRowCountUnit(units1);
    }

    /**
     * DOC xqliu Comment method "getRownCountUnit".
     * 
     * @param units1
     * @return
     */
    public TableIndicatorUnit getRownCountUnit(List<TableIndicatorUnit> units1) {
        return WhereRuleStatisticsStateUtil.getRownCountUnit(units1);
    }

    /**
     * DOC xqliu Comment method "addDataEntity2CustomerDataset". ADD xqliu 2010-03-10 feature 10834
     * 
     * @param customerDataset
     * @param unit
     */
    private void addDataEntity2CustomerDataset(CustomerDefaultCategoryDataset customerDataset, TableIndicatorUnit unit) {
        if (IndicatorEnum.WhereRuleIndicatorEnum.equals(unit.getType())) {
            String columnKey = unit.getIndicatorName();
            double value = WhereRuleStatisticsStateUtil.getMatchValue(unit.getValue());
            double valueNotM = WhereRuleStatisticsStateUtil.getNotMatchValue(unit.getValue(), value, unit.geIndicatorCount());
            customerDataset.addValue(valueNotM, ROW_KEY_NOT_PASS, columnKey);
            customerDataset.addValue(value, ROW_KEY_PASS, columnKey);

            WhereRuleChartDataEntity entity = WhereRuleStatisticsStateUtil
                    .createRuleDataEntity(unit, columnKey, value, valueNotM);

            customerDataset.addDataEntity(entity);
        }
    }

    /**
     * DOC xqliu Comment method "addRownCountDataEntity2CustomerDataset".
     * 
     * @param customerDataset
     * @param unit
     */
    private void addRownCountDataEntity2CustomerDataset(CustomerDefaultCategoryDataset customerDataset, TableIndicatorUnit unit) {
        if (IndicatorEnum.RowCountIndicatorEnum.equals(unit.getType())) {
            String value = CommonStateUtil.getUnitValue(unit.getValue(), StringFormatUtil.DOUBLE_NUMBER);
            String label = unit.getIndicatorName();
            customerDataset.addValue(Double.parseDouble(value), label, label);
            ChartDataEntity entityCount = CommonStateUtil.createDataEntity(unit, value, label);
            customerDataset.addDataEntity(entityCount);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChart(org.jfree.data.category
     * .CategoryDataset)
     */
    public Object getChart(Object dataset) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getChartList(java.util.List)
     */
    public List<Object> getChartList(List<Object> datasets) {
        List<Object> ret = new ArrayList<Object>();
        if (datasets == null || datasets.size() < 1) {
            return ret;
        }
        // first get the chart for the row count
        Object chart = TOPChartUtils.getInstance().createBarChart(
                DefaultMessagesImpl.getString("SimpleStatisticsState.SimpleStatistics"), datasets.get(0), false); //$NON-NLS-1$
        TOPChartUtils.getInstance().decorateChart(chart, false);
        ret.add(chart);

        // THEN get the charts for each paged rules
        for (int i = 1; i < datasets.size(); i++) {
            Object dataset = datasets.get(i);
            Object stackChart = TOPChartUtils.getInstance().createStackedBarChart(
                    DefaultMessagesImpl.getString("WhereRuleStatisticsStateTable.WhereRuleStatistics"), dataset, true); //$NON-NLS-1$

            ret.add(stackChart);
        }
        return ret;
    }
}
