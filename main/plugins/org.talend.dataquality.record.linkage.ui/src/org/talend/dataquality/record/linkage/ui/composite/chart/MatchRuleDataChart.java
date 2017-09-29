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
package org.talend.dataquality.record.linkage.ui.composite.chart;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.dataquality.PluginConstant;
import org.talend.dataquality.record.linkage.ui.i18n.internal.DefaultMessagesImpl;

/**
 * DOC yyi class global comment. Detailled comment
 */
public class MatchRuleDataChart extends Composite {

    public static final int CHART_STANDARD_WIDHT = 600;

    public static final int CHART_STANDARD_HEIGHT = 275;

    private Object jfreeChartComp;

    // TDQ-9297: Set the default value of "hide groups less than" to 2 instead of 1
    private int times = PluginConstant.HIDDEN_GROUP_LESS_THAN_DEFAULT;

    private Map<Object, Long> groupSize2GroupFrequency = null;

    /**
     * DOC yyi DataChart constructor comment.
     * 
     * @param parent
     * @param style
     */
    public MatchRuleDataChart(Composite parent, Map<Object, Long> groupSize2GroupFrequency) {
        super(parent, SWT.NONE);
        this.groupSize2GroupFrequency = groupSize2GroupFrequency;
        this.setLayout(new FillLayout());
        // make the size of the chart full fill the area
        if (this.getParent().getLayout() instanceof GridLayout) {
            GridData data = new GridData(GridData.FILL_BOTH);
            this.setLayoutData(data);
        }

        initChartData();
    }

    public boolean initChartData() {
        if (this.jfreeChartComp == null) {
            createChart();
            return true;
        }
        return false;
    }

    /**
     * DOC yyi Comment method "createChart".
     */
    private void createChart() {
        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayout(new GridLayout(1, true));
        Object jfreechart = createChart(createDataset());
        this.jfreeChartComp = TOPChartUtil.getInstance().createChartComposite(composite, SWT.NONE, jfreechart, true);

    }

    private Object createChart(Object categorydataset) {
        Object localJFreeChart = TOPChartUtil.getInstance().createMatchRuleBarChart(DefaultMessagesImpl.getString("DataChart.0"), //$NON-NLS-1$
                "Group Count", categorydataset); //$NON-NLS-1$
        return localJFreeChart;
    }

    private Object createDataset() {
        String s = DefaultMessagesImpl.getString("DataChart.4"); //$NON-NLS-1$

        // sort dataset
        // ADD msjian : fix a NPE when the user didn't click the chart button
        if (groupSize2GroupFrequency != null) {
            String[] array = groupSize2GroupFrequency.keySet().toArray(new String[0]);
            List<String> groups = Arrays.asList(array);
            Collections.sort(groups, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    // return groupCounts.get(o2).compareTo(groupCounts.get(o1));
                    return Integer.parseInt(o1) - Integer.parseInt(o2);
                }
            });

            return TOPChartUtil.getInstance().createDatasetForMatchRule(groupSize2GroupFrequency, groups, times, s);
        } else {
            return TOPChartUtil.getInstance().createDatasetForMatchRule(groupSize2GroupFrequency, null, times, s);
        }
    }

    public Object getChartComp() {
        return jfreeChartComp;
    }

    public void refresh(Map<Object, Long> groupSize2GroupFrequencyNew) {
        this.groupSize2GroupFrequency = groupSize2GroupFrequencyNew;
        refresh();
    }

    /**
     * refresh the chart by old data
     * 
     */
    public void refresh() {
        initChartData();
        TOPChartUtil.getInstance().refrechChart(jfreeChartComp, createChart(createDataset()));
    }

    /**
     * DOC set spinner value
     * 
     * @param times
     */
    public void setTimes(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (jfreeChartComp != null) {
            ((Composite) jfreeChartComp).dispose();
        }
    }

    /**
     * Getter for groupSize2GroupFrequency.
     * 
     * @return the groupSize2GroupFrequency
     */
    public Map<Object, Long> getGroupSize2GroupFrequency() {
        return this.groupSize2GroupFrequency;
    }

    /**
     * 
     * create a chart with empty dataset,so as to clear the blocking key chart.
     */
    public void clearChart() {
        Object chart = createChart(TOPChartUtil.getInstance().createDatasetForMatchRule(null, null, times, StringUtils.EMPTY));
        TOPChartUtil.getInstance().refrechChart(jfreeChartComp, chart);
    }
}
