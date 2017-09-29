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
import java.util.Collection;
import java.util.List;

import org.talend.dataprofiler.core.ui.editor.preview.ColumnIndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.analysis.explore.SummaryStastictisExplorer;
import org.talend.dq.indicators.IndicatorCommonUtil;
import org.talend.dq.indicators.preview.table.ChartDataEntity;
import org.talend.dq.nodes.indicator.type.IndicatorEnum;
import org.talend.utils.sql.Java2SqlType;

/**
 * created by yyin on 2014-12-2 Detailled comment
 * 
 */
public class SummaryStatisticsStateUtil {

    public SummaryStatisticsStateUtil() {

    }

    public DataExplorer getDataExplorer(int sqltype) {
        if (Java2SqlType.isDateInSQL(sqltype)) {
            return null;
        }
        return new SummaryStastictisExplorer();
    }

    // TDQ-9140 , if any values = NaN, isMeaning = false, and will not use BAW chart.
    private boolean isMeaning = true;

    /**
     * DOC bZhou Comment method "check".
     * 
     * @param units2
     * @return
     */
    public Collection<? extends IndicatorUnit> check(List<IndicatorUnit> parameterUnits) {
        List<IndicatorUnit> validUnitList = new ArrayList<IndicatorUnit>();

        for (IndicatorUnit unit : parameterUnits) {

            IndicatorEnum type = unit.getType();
            if (type != null && !unit.isExcuted()
                    && (type == IndicatorEnum.IQRIndicatorEnum || type == IndicatorEnum.RangeIndicatorEnum)) {
                IndicatorCommonUtil.getIndicatorValue(unit.getIndicator());
            }

            if (unit.getValue() == null) {
                isMeaning = false;
            }

            if (unit.getIndicator().getRealValue() != null && "null".equals(unit.getIndicator().getRealValue())) {//$NON-NLS-1$
                continue;
            } else {
                validUnitList.add(unit);
            }
        }

        return validUnitList;
    }

    /**
     * DOC yyin Comment method "findSqlType".
     * 
     * @param units
     */
    public int findSqlType(List<IndicatorUnit> units) {
        if (units != null && !units.isEmpty() && units.get(0) instanceof ColumnIndicatorUnit) {
            if (((ColumnIndicatorUnit) units.get(0)).getModelElementIndicator() != null) {
                return ((ColumnIndicatorUnit) units.get(0)).getModelElementIndicator().getJavaType();
            }
        }
        return Integer.MIN_VALUE;
    }

    public boolean isMeaning() {
        return isMeaning;
    }

    public void setMeaning(boolean meaning) {
        isMeaning = meaning;
    }

    /**
     * DOC yyin Comment method "getUnitValue".
     * 
     * @param unit
     * @return
     */
    public String getUnitValue(IndicatorUnit unit) {
        String value = null;
        if (unit.getValue() == null) {
            value = String.valueOf(Double.NaN);
            setMeaning(false);
        } else {
            value = unit.getValue().toString();
        }
        return value;
    }

    /**
     * DOC yyin Comment method "createDataEntity".
     * 
     * @param unit
     * @param value
     * @return
     */
    public ChartDataEntity createDataEntity(IndicatorUnit unit, String value) {
        ChartDataEntity entity = new ChartDataEntity();
        entity.setIndicator(unit.getIndicator());
        entity.setLabel(unit.getIndicatorName());
        entity.setValue(value);
        return entity;
    }

}
