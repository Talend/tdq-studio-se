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

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleEdge;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public class ToolTipChartComposite extends ChartComposite {

    public ToolTipChartComposite(Composite comp, int style, JFreeChart chart, boolean useBuffer) {
        super(comp, style, chart, useBuffer);
        // TODO Auto-generated constructor stub
    }

    /**
     * Returns a string for the tooltip.
     * 
     * @param e the mouse event.
     * 
     * @return A tool tip or <code>null</code> if no tooltip is available.
     */
    @Override
    public String getToolTipText(org.eclipse.swt.events.MouseEvent e) {
        // OK, we're using an SWT MouseEvent here, not much difference...
        String result = getTooltipAtPoint(new Point(e.x, e.y));
        if (result != null) {
            return result;
        }

        return super.getToolTipText();
    }

    private static int hotspontsize = 5;

    private HighLowItemLabelGenerator hiLoTips = new HighLowItemLabelGenerator();

    private StandardXYToolTipGenerator xyTips = new StandardXYToolTipGenerator();

    /**
     * This method attempts to get a tooltip by converting the screen X,Y into Chart Area X,Y and then looking for a
     * data point in a data set that lies inside a hotspot around that value.
     * 
     * @param point The Java 2D point
     * @return A string for the data at the point or null if no data is found.
     */
    protected String getTooltipAtPoint(Point point) {
        String result = null;

        Point2D translatedPoint = this.translateScreenToJava2D(point);
        Plot plot = this.getChart().getPlot();
        PlotRenderingInfo info = this.getChartRenderingInfo().getPlotInfo();
        if (plot instanceof CombinedDomainXYPlot) {
            int index = info.getSubplotIndex(translatedPoint);
            if (index < 0) {
                index = 0;
            }
            plot = (Plot) ((CombinedDomainXYPlot) plot).getSubplots().get(index);
            info = this.getChartRenderingInfo().getPlotInfo().getSubplotInfo(index);
        }
        if (plot != null && plot instanceof XYPlot) {
            XYPlot xyPlot = (XYPlot) plot;
            ValueAxis domainAxis = xyPlot.getDomainAxis();
            ValueAxis rangeAxis = xyPlot.getRangeAxis();
            // had to switch to SWT's rectangle here.
            Rectangle screenArea = this.scale(info.getDataArea());

            double hotspotSizeX = hotspontsize * this.getScaleX();
            double hotspotSizeY = hotspontsize * this.getScaleY();
            double x0 = point.getX();
            double y0 = point.getY();
            double x1 = x0 - hotspotSizeX;
            double y1 = y0 + hotspotSizeY;
            double x2 = x0 + hotspotSizeX;
            double y2 = y0 - hotspotSizeY;
            RectangleEdge xEdge = RectangleEdge.BOTTOM;
            RectangleEdge yEdge = RectangleEdge.LEFT;
            // Switch everything for horizontal charts
            if (xyPlot.getOrientation() == PlotOrientation.HORIZONTAL) {
                hotspotSizeX = hotspontsize * this.getScaleY();
                hotspotSizeY = hotspontsize * this.getScaleX();
                x0 = point.getY();
                y0 = point.getX();
                x1 = x0 + hotspotSizeX;
                y1 = y0 - hotspotSizeY;
                x2 = x0 - hotspotSizeX;
                y2 = y0 + hotspotSizeY;
                xEdge = RectangleEdge.LEFT;
                yEdge = RectangleEdge.BOTTOM;
            }

            // OK, here we have to get ourselves back into AWT land...
            Rectangle2D r2d = new Rectangle2D.Double();
            r2d.setRect(screenArea.x, screenArea.y, screenArea.width, screenArea.height);

            double ty0 = rangeAxis.java2DToValue(y0, r2d, yEdge);
            double tx1 = domainAxis.java2DToValue(x1, r2d, xEdge);
            double ty1 = rangeAxis.java2DToValue(y1, r2d, yEdge);
            double tx2 = domainAxis.java2DToValue(x2, r2d, xEdge);
            double ty2 = rangeAxis.java2DToValue(y2, r2d, yEdge);

            int datasetCount = xyPlot.getDatasetCount();
            for (int datasetIndex = 0; datasetIndex < datasetCount; datasetIndex++) {
                XYDataset dataset = xyPlot.getDataset(datasetIndex);
                int seriesCount = dataset.getSeriesCount();
                for (int series = 0; series < seriesCount; series++) {
                    int itemCount = dataset.getItemCount(series);
                    if (dataset instanceof OHLCDataset) {
                        // This could be optimized to use a binary search for x first
                        for (int item = 0; item < itemCount; item++) {
                            double xValue = dataset.getXValue(series, item);
                            double yValueHi = ((OHLCDataset) dataset).getHighValue(series, item);
                            double yValueLo = ((OHLCDataset) dataset).getLowValue(series, item);
                            // Check hi lo and swap if needed
                            if (yValueHi < yValueLo) {
                                double temp = yValueHi;
                                yValueHi = yValueLo;
                                yValueLo = temp;
                            }
                            // Check if the dataset 'X' value lies between the hotspot (tx1 < xValue < tx2)
                            if (tx1 < xValue && xValue < tx2) {
                                // Check if the cursor 'y' value lies between the high and low (low < ty0 < high)
                                if (yValueLo < ty0 && ty0 < yValueHi) {
                                    return hiLoTips.generateToolTip(dataset, series, item);
                                }
                            }
                        }
                    } else {
                        // This could be optimized to use a binary search for x first
                        for (int item = 0; item < itemCount; item++) {
                            double xValue = dataset.getXValue(series, item);
                            double yValue = dataset.getYValue(series, item);
                            // Check if the dataset 'X' value lies between the hotspot (tx1< xValue < tx2)
                            if (tx1 < xValue && xValue < tx2) {
                                // Check if the dataset 'Y' value lies between the hotspot (ty1 < yValue < ty2)
                                if (ty1 < yValue && yValue < ty2) {
                                    return xyTips.generateToolTip(dataset, series, item);
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public HighLowItemLabelGenerator getHiLoTips() {
        return this.hiLoTips;
    }

    public void setHiLoTips(HighLowItemLabelGenerator hiLoTips) {
        this.hiLoTips = hiLoTips;
    }

    public StandardXYToolTipGenerator getXyTips() {
        return this.xyTips;
    }

    public void setXyTips(StandardXYToolTipGenerator xyTips) {
        this.xyTips = xyTips;
    }
}
