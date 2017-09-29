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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.talend.dataprofiler.common.ui.editor.preview.ICustomerDataset;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public abstract class AbstractChartTypeStates implements IChartTypeStates {

    protected List<IndicatorUnit> units = new ArrayList<IndicatorUnit>();

    public AbstractChartTypeStates() {

    }

    public AbstractChartTypeStates(List<IndicatorUnit> units) {

        if (units != null) {
            this.units.addAll(units);
        }
    }

    public List<Object> getChartList() {
        return null;
    }

    public Object getDataset() {
        return getCustomerDataset();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public Object getXYDataset() {
        if (getCustomerXYDataset() != null) {
            return getCustomerXYDataset();
        }

        return null;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public ICustomerDataset getCustomerXYDataset() {
        return null;
    }

    public Object getPieDataset() {
        return null;
    }

    public Object getChart(Object dataset) {
        return TOPChartUtils.getInstance().createBarChart(StringUtils.EMPTY, dataset, false);
    }

    public List<Object> getChartList(List<Object> datasets) {
        return null;
    }
}
