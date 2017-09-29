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

import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.dataprofiler.service.ITOPChartService;
import org.talend.dq.helper.AbstractOSGIServiceUtils;

/**
 * created by yyin on 2014-12-11 Detailled comment
 * 
 */
public class TOPChartUtil extends AbstractOSGIServiceUtils {

    private static TOPChartUtil instance;

    private ITOPChartService chartService;

    public static TOPChartUtil getInstance() {
        if (instance == null) {
            instance = new TOPChartUtil();
        }
        return instance;
    }

    public boolean isTOPChartInstalled() {
        initService(false);
        return this.chartService != null;
    }

    public Object createChartComposite(Object composite, int style, Object chart, boolean useBuffer) {
        if (isTOPChartInstalled()) {
            return chartService.createChartComposite(composite, style, chart, useBuffer);
        }
        return null;
    }

    public Object createConceptsChart(String title, Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.createConceptsChart(title, dataset);
        }
        return null;
    }

    public Object createDefaultCategoryDataset(List<String[]> inputData) {
        if (isTOPChartInstalled()) {
            return chartService.createDefaultCategoryDataset(inputData);
        }
        return null;
    }

    public Object createChartCompositeWithoutGrid(Object composite, int style, Object chart, boolean useBuffer) {
        if (isTOPChartInstalled()) {
            return chartService.createChartCompositeWithoutGrid(composite, style, chart, useBuffer);
        }
        return null;
    }

    public Object createMatchRuleBarChart(String categoryAxisLabel, String valueAxisLabel, Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.createMatchRuleBarChart(categoryAxisLabel, valueAxisLabel, dataset);
        }
        return null;
    }

    public void refrechChart(Object chartComp, Object chart) {
        if (isTOPChartInstalled()) {
            chartService.refrechChart(chartComp, chart);
        }
    }

    public Object createDatasetForMatchRule(Map<Object, Long> groupSize2GroupFrequency, List<String> groups, int times,
            String items) {
        if (isTOPChartInstalled()) {
            return chartService.createDatasetForMatchRule(groupSize2GroupFrequency, groups, times, items);
        }
        return null;
    }

    public Object createBlockingBarChart(String title, Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.createBlockingBarChart(title, dataset);
        }
        return null;
    }

    public Object createHistogramDataset(double[] valueArray, double maxValue, int bins) {
        if (isTOPChartInstalled()) {
            return chartService.createHistogramDataset(valueArray, maxValue, bins);
        }
        return null;
    }

    public Object createDuplicateRecordPieChart(String title, Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.createDuplicateRecordPieChart(title, dataset);
        }
        return null;
    }

    public Object createDatasetForDuplicateRecord(Map<String, Long> dupStats) {
        if (isTOPChartInstalled()) {
            return chartService.createDatasetForDuplicateRecord(dupStats);
        }
        return null;
    }

    @Override
    public String getPluginName() {
        return ITOPChartService.PLUGIN_NAME;
    }

    @Override
    public String getServiceName() {
        return ITOPChartService.class.getName();
    }

    @Override
    public boolean isServiceInstalled() {
        initService(true);
        return this.chartService != null;
    }

    @Override
    protected String getMissingMessageName() {
        return "TOPChartUtil.missingTopChart"; //$NON-NLS-1$
    }

    @Override
    protected String getRestartMessageName() {
        return "TOPChartUtil.restartToLoadTopChart"; //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setService(BundleContext context, ServiceReference serviceReference) {
        if (serviceReference != null) {
            Object obj = context.getService(serviceReference);
            if (obj != null) {
                this.chartService = (ITOPChartService) obj;
            }
        }
    }

}
