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

import java.util.ArrayList;
import java.util.List;

import org.talend.dataprofiler.common.ui.editor.preview.CustomerDefaultCategoryDataset;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.editor.preview.IndicatorUnit;
import org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.util.BenfordLawFrequencyStateUtil;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dq.analysis.explore.DataExplorer;
import org.talend.dq.indicators.ext.FrequencyExt;

/**
 * Show the Bar chart with the line chart together. bar: show the user data result, line point: show the benford law
 * standard values (formal value)
 */
public class BenfordLawFrequencyState extends FrequencyTypeStates {

    // 1~9,Numerically, the leading digits have the following distribution in Benford's law, add: 0, invalid with value
    // 0.0
    public static double[] formalValues = { 0.301, 0.176, 0.125, 0.097, 0.079, 0.067, 0.058, 0.051, 0.046, 0.0, 0.0 };

    private List<String> dotChartLabels = new ArrayList<String>();

    /**
     * DOC yyin BenfordLawFrequencyState constructor comment.
     * 
     * @param units
     */
    public BenfordLawFrequencyState(List<IndicatorUnit> units) {
        super(units);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.IChartTypeStates#getDataExplorer()
     */
    public DataExplorer getDataExplorer() {
        return BenfordLawFrequencyStateUtil.getDataExplorer();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.FrequencyTypeStates#sortIndicator(org.talend
     * .dq.indicators.ext.FrequencyExt[])
     */
    @Override
    protected void sortIndicator(FrequencyExt[] frequencyExt) {
        BenfordLawFrequencyStateUtil.sortIndicator(frequencyExt);
    }

    /**
     * using frequency insteadof value
     */
    @Override
    protected void setValueToDataset(CustomerDefaultCategoryDataset customerdataset, FrequencyExt freqExt, final String keyLabel) {
        customerdataset.addValue(freqExt.getFrequency(), "1", keyLabel); //$NON-NLS-1$
        dotChartLabels.add(keyLabel);
    }

    /**
     * create a bar chart with points(formalValues) on each bar.
     */
    @Override
    public Object getChart() {
        // Clear the dot category label first, so that the new category label will be added into list.
        dotChartLabels.clear();
        return createChart(getDataset());
    }

    /**
     * DOC yyin Comment method "createChart".
     * 
     * @param dataset
     * @return
     */
    private Object createChart(Object dataset) {
        String categoryAxisLabel = DefaultMessagesImpl.getString("BenfordLawFrequencyState.AxisY"); //$NON-NLS-1$
        String axisXLabel = DefaultMessagesImpl.getString("BenfordLawFrequencyState.value"); //$NON-NLS-1$
        return TOPChartUtils.getInstance().createBenfordChartByKCD(axisXLabel, categoryAxisLabel, dataset, dotChartLabels,
                formalValues, axisXLabel);
    }

    @Override
    public Object getChart(Object dataset) {
        // Clear the dot category label first, so that the new category label will be added into list.
        dotChartLabels.clear();
        return createChart(dataset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.editor.preview.model.states.freq.FrequencyTypeStates#getTitle()
     */
    @Override
    protected String getTitle() {
        return DefaultMessagesImpl.getString("FrequencyTypeStates.BenfordLawFrequencyStatistics"); //$NON-NLS-1$
    }

    /**
     * make it return true to show the frequency in the table. because this indicator has the frequency, no matter with
     * row count or not
     */
    @Override
    protected boolean isWithRowCountIndicator() {
        return true;
    }

}
