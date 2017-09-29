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
package org.talend.dataprofiler.core.ui.editor.preview.model.states.utils;

import java.util.ArrayList;
import java.util.List;

import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.model.TableIndicator;
import org.talend.dataprofiler.core.ui.editor.preview.TableIndicatorUnit;
import org.talend.dataprofiler.core.ui.pref.EditorPreferencePage;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.RowCountIndicator;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.analysis.explore.DQRuleExplorer;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.preview.table.WhereRuleChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * created by yyin on 2014-12-4 Detailled comment
 * 
 */
public class WhereRuleStatisticsStateUtil {

    public static DataExplorer getDataExplorer() {
        return new DQRuleExplorer();
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

    public static List<List<Indicator>> getPagedIndicators(List<TableIndicatorUnit> units) {
        int size = getSizeOfDQRulePerChart();

        List<List<Indicator>> indicatorList = new ArrayList<List<Indicator>>();
        // first , add row count indicator
        List<Indicator> rowInd = new ArrayList<Indicator>();
        rowInd.add(getRownCountUnit(units).getIndicator());
        indicatorList.add(rowInd);

        // then, add all where rules(one chart <--> one list)
        List<TableIndicatorUnit> whereRuleUnits = removeRowCountUnit(units);
        int totalNum = whereRuleUnits.size();
        int pageNum = totalNum % size == 0 ? totalNum / size : totalNum / size + 1;
        for (int i = 0; i < pageNum; i++) {
            List<Indicator> rules = new ArrayList<Indicator>();
            for (int j = 0; j < size; ++j) {
                int index = i * size + j;
                if (index < totalNum) {
                    rules.add(whereRuleUnits.get(index).getIndicator());
                } else {
                    break;
                }
            }
            indicatorList.add(rules);
        }
        return indicatorList;
    }

    /**
     * DOC xqliu Comment method "removeRowCountUnit".
     * 
     * @param units1
     * @return
     */
    public static List<TableIndicatorUnit> removeRowCountUnit(List<TableIndicatorUnit> units1) {
        List<TableIndicatorUnit> result = new ArrayList<TableIndicatorUnit>();
        for (TableIndicatorUnit tiu : units1) {
            if (!IndicatorEnum.RowCountIndicatorEnum.equals(tiu.getType())) {
                result.add(tiu);
            }
        }
        return result;
    }

    /**
     * DOC xqliu Comment method "getRownCountUnit".
     * 
     * @param units1
     * @return
     */
    public static TableIndicatorUnit getRownCountUnit(List<TableIndicatorUnit> units1) {
        for (TableIndicatorUnit tiu : units1) {
            if (IndicatorEnum.RowCountIndicatorEnum.equals(tiu.getType())) {
                return tiu;
            }
        }
        return null;
    }

    /**
     * DOC xqliu Comment method "getUnitToolTip". ADD xqliu 2010-03-10 feature 10834
     * 
     * @param unit
     * @return
     */
    public static String getUnitToolTip(TableIndicatorUnit unit) {
        if (unit != null) {
            if (unit.getIndicator() != null && unit.getIndicator().getIndicatorDefinition() != null) {
                IndicatorDefinition indicatorDefinition = unit.getIndicator().getIndicatorDefinition();
                TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DESCRIPTION,
                        indicatorDefinition.getTaggedValue());
                if (taggedValue != null) {
                    return taggedValue.getValue();
                }
            }
        }
        return null;
    }

    /**
     * DOC yyin Comment method "createRuleDataEntity".
     * 
     * @param unit
     * @param columnKey
     * @param value
     * @param valueNotM
     * @return
     */
    public static WhereRuleChartDataEntity createRuleDataEntity(TableIndicatorUnit unit, String columnKey, double value,
            double valueNotM) {
        WhereRuleChartDataEntity entity = new WhereRuleChartDataEntity();
        entity.setIndicator(unit.getIndicator());
        entity.setLabel(columnKey);
        entity.setNumMatch(String.valueOf(value));
        entity.setNumNoMatch(String.valueOf(valueNotM));
        // ADD xqliu 2010-03-10 feature 10834
        entity.setToolTip(WhereRuleStatisticsStateUtil.getUnitToolTip(unit));
        // ~
        return entity;
    }

    /**
     * get the MatchValue
     * 
     * @param value
     * @return
     */
    public static double getMatchValue(Object value) {
        return value == null ? Double.NaN : Double.parseDouble(value.toString());
    }

    public static double getNotMatchValue(Object value, double matchValue, long rowCount) {
        return value == null ? Double.NaN : rowCount - matchValue;
    }

    public static Long initRowCount(TableIndicator tableIndicator1) {
        Long result = 0L;
        if (tableIndicator1 != null) {
            TableIndicatorUnit[] tius = tableIndicator1.getIndicatorUnits();
            for (TableIndicatorUnit tiu : tius) {
                if (tiu.getIndicator() instanceof RowCountIndicator) {
                    RowCountIndicator rci = (RowCountIndicator) tiu.getIndicator();
                    result = rci.getCount();
                    break;
                }
            }
        }
        return result;
    }
}
