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
package org.talend.dataprofiler.common.ui.editor.preview;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.dataprofiler.service.ITOPChartService;
import org.talend.dq.helper.AbstractOSGIServiceUtils;

/**
 * created by yyin on 2014-12-15 Detailled comment
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

    @Override
    public boolean isServiceInstalled() {
        initService(true);
        return this.chartService != null;
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
    protected String getMissingMessageName() {
        return "TOPChartUtil.missingTopChart"; //$NON-NLS-1$
    }

    @Override
    protected String getRestartMessageName() {
        return "TOPChartUtil.restartToLoadTopChart"; //$NON-NLS-1$
    }

    @Override
    protected void setService(BundleContext context, ServiceReference serviceReference) {
        if (serviceReference != null) {
            Object obj = context.getService(serviceReference);
            if (obj != null) {
                this.chartService = (ITOPChartService) obj;
            }
        }
    }

    // No need to download,only for check
    public boolean isTOPChartInstalled() {
        initService(false);
        return this.chartService != null;
    }

    public Object createDefaultCategoryDataset() {
        if (isTOPChartInstalled()) {
            return chartService.createDefaultCategoryDataset();
        }
        return null;
    }

    public void addValueToCategoryDataset(Object dataset, double value, String labelX, String labelY) {
        if (isTOPChartInstalled()) {
            chartService.addValueToCategoryDataset(dataset, value, labelX, labelY);
        }
    }

    public int getRowCount(Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.getRowCount(dataset);
        }
        return Integer.MIN_VALUE;
    }

    public int getColumnCount(Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.getColumnCount(dataset);
        }
        return Integer.MIN_VALUE;
    }

    public Number getValue(Object dataset, int row, int column) {
        if (isTOPChartInstalled()) {
            return chartService.getValue(dataset, row, column);
        }
        return Integer.MIN_VALUE;
    }

    public Comparable getRowKey(Object dataset, int row) {
        if (isTOPChartInstalled()) {
            return chartService.getRowKey(dataset, row);
        }
        return Integer.MIN_VALUE;
    }

    public int getRowIndex(Object dataset, Comparable key) {
        if (isTOPChartInstalled()) {
            return chartService.getRowIndex(dataset, key);
        }
        return Integer.MIN_VALUE;
    }

    public List getRowKeys(Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.getRowKeys(dataset);
        }
        return null;
    }

    public Comparable getColumnKey(Object dataset, int column) {
        if (isTOPChartInstalled()) {
            return chartService.getColumnKey(dataset, column);
        }
        return Integer.MIN_VALUE;
    }

    public int getColumnIndex(Object dataset, Comparable key) {
        if (isTOPChartInstalled()) {
            return chartService.getColumnIndex(dataset, key);
        }
        return Integer.MIN_VALUE;
    }

    public List getColumnKeys(Object dataset) {
        if (isTOPChartInstalled()) {
            return chartService.getColumnKeys(dataset);
        }
        return null;
    }

    public Number getValue(Object dataset, Comparable rowKey, Comparable columnKey) {
        if (isTOPChartInstalled()) {
            return chartService.getValue(dataset, rowKey, columnKey);
        }
        return Integer.MIN_VALUE;
    }

    public void clearDataset(Object dataset) {
        if (isTOPChartInstalled()) {
            chartService.clearDataset(dataset);
        }
    }

}
