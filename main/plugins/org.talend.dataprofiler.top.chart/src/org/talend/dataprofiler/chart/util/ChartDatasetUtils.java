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
package org.talend.dataprofiler.chart.util;

import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 * DOC Zqin class global comment. Detailled comment
 */
public final class ChartDatasetUtils {

    private static final double OUTLIER_FACTOR = 3;

    private ChartDatasetUtils() {

    }

    public static boolean isDatasetEmpty(CategoryDataset dataset) {
        return dataset.getColumnCount() == 0 && dataset.getRowCount() == 0;
    }

    public static DefaultCategoryDataset createDefaultDataset() {
        return new DefaultCategoryDataset();
    }

    public static DefaultBoxAndWhiskerCategoryDataset createBoxAndWhiskerDataset() {
        return new DefaultBoxAndWhiskerCategoryDataset();
    }

    public static BoxAndWhiskerItem createBoxAndWhiskerItem(Double mean, Double median, Double q1, Double q3,
            Double minRegularValue, Double maxRegularValue, List outliers) {
        // MOD scorreia 2008-06-05 automatic computation of outliers limits
        // see http://en.wikipedia.org/wiki/Box_plot
        Double xIQR = (q1 != null && q3 != null) ? OUTLIER_FACTOR * (q3 - q1) : null;
        Double minOutlier = xIQR != null ? q1 - xIQR : null;
        Double maxOutlier = xIQR != null ? q3 + xIQR : null;
        // enhance bounds of graphics when needed
        if (minOutlier != null && minRegularValue != null) {
            minOutlier = Math.min(minOutlier, minRegularValue);
        }
        if (maxOutlier != null && maxRegularValue != null) {
            maxOutlier = Math.max(maxOutlier, maxRegularValue);
        }

        BoxAndWhiskerItem item = new BoxAndWhiskerItem(mean, median, q1, q3, minRegularValue, maxRegularValue, minOutlier,
                maxOutlier, outliers);
        return item;
    }

}
