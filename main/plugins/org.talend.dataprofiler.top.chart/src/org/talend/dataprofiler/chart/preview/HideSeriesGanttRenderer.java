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
package org.talend.dataprofiler.chart.preview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 * A renderer for simple Gantt charts.
 */
public class HideSeriesGanttRenderer extends IntervalBarRenderer implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -4010349116350119512L;

    /** The paint for displaying the percentage complete. */
    private Paint completePaint;

    /** The paint for displaying the incomplete part of a task. */
    private Paint incompletePaint;

    /**
     * Controls the starting edge of the progress indicator (expressed as a percentage of the overall bar width).
     */
    private double startPercent;

    /**
     * Controls the ending edge of the progress indicator (expressed as a percentage of the overall bar width).
     */
    private double endPercent;

    /**
     * Creates a new renderer.
     */
    public HideSeriesGanttRenderer() {
        super();
        setIncludeBaseInRange(false);
        this.completePaint = Color.green;
        this.incompletePaint = Color.red;
        this.startPercent = 0.35;
        this.endPercent = 0.65;
    }

    /**
     * Returns the paint used to show the percentage complete.
     * 
     * @return The paint (never <code>null</code>.
     */
    public Paint getCompletePaint() {
        return this.completePaint;
    }

    /**
     * Sets the paint used to show the percentage complete and sends a {@link RendererChangeEvent} to all registered
     * listeners.
     * 
     * @param paint the paint (<code>null</code> not permitted).
     */
    public void setCompletePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument."); //$NON-NLS-1$
        }
        this.completePaint = paint;
        notifyListeners(new RendererChangeEvent(this));
    }

    /**
     * Returns the paint used to show the percentage incomplete.
     * 
     * @return The paint (never <code>null</code>).
     */
    public Paint getIncompletePaint() {
        return this.incompletePaint;
    }

    /**
     * Sets the paint used to show the percentage incomplete and sends a {@link RendererChangeEvent} to all registered
     * listeners.
     * 
     * @param paint the paint (<code>null</code> not permitted).
     */
    public void setIncompletePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument."); //$NON-NLS-1$
        }
        this.incompletePaint = paint;
        notifyListeners(new RendererChangeEvent(this));
    }

    /**
     * Returns the position of the start of the progress indicator, as a percentage of the bar width.
     * 
     * @return The start percent.
     */
    public double getStartPercent() {
        return this.startPercent;
    }

    /**
     * Sets the position of the start of the progress indicator, as a percentage of the bar width.
     * 
     * @param percent the percent.
     */
    public void setStartPercent(double percent) {
        this.startPercent = percent;
        notifyListeners(new RendererChangeEvent(this));
    }

    /**
     * Returns the position of the end of the progress indicator, as a percentage of the bar width.
     * 
     * @return The end percent.
     */
    public double getEndPercent() {
        return this.endPercent;
    }

    /**
     * Sets the position of the end of the progress indicator, as a percentage of the bar width.
     * 
     * @param percent the percent.
     */
    public void setEndPercent(double percent) {
        this.endPercent = percent;
        notifyListeners(new RendererChangeEvent(this));
    }

    /**
     * Draws the bar for a single (series, category) data item.
     * 
     * @param g2 the graphics device.
     * @param state the renderer state.
     * @param dataArea the data area.
     * @param plot the plot.
     * @param domainAxis the domain axis.
     * @param rangeAxis the range axis.
     * @param dataset the dataset.
     * @param row the row index (zero-based).
     * @param column the column index (zero-based).
     * @param pass the pass index.
     */
    @Override
    public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot,
            CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {

        if (dataset instanceof GanttCategoryDataset) {
            GanttCategoryDataset gcd = (GanttCategoryDataset) dataset;
            drawTasks(g2, state, dataArea, plot, domainAxis, rangeAxis, gcd, row, column);
        } else { // let the superclass handle it...
            super.drawItem(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column, pass);
        }

    }

    /**
     * Draws the tasks/subtasks for one item.
     * 
     * @param g2 the graphics device.
     * @param state the renderer state.
     * @param dataArea the data plot area.
     * @param plot the plot.
     * @param domainAxis the domain axis.
     * @param rangeAxis the range axis.
     * @param dataset the data.
     * @param row the row index (zero-based).
     * @param column the column index (zero-based).
     */
    protected void drawTasks(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot,
            CategoryAxis domainAxis, ValueAxis rangeAxis, GanttCategoryDataset dataset, int row, int column) {

        int count = dataset.getSubIntervalCount(row, column);
        if (count == 0 && getItemVisible(row, column)) {
            drawTask(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column);
        }

        for (int subinterval = 0; subinterval < count; subinterval++) {

            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();

            // value 0
            Number value0 = dataset.getStartValue(row, column, subinterval);
            if (value0 == null) {
                return;
            }
            double translatedValue0 = rangeAxis.valueToJava2D(value0.doubleValue(), dataArea, rangeAxisLocation);

            // value 1
            Number value1 = dataset.getEndValue(row, column, subinterval);
            if (value1 == null) {
                return;
            }
            double translatedValue1 = rangeAxis.valueToJava2D(value1.doubleValue(), dataArea, rangeAxisLocation);

            if (translatedValue1 < translatedValue0) {
                double temp = translatedValue1;
                translatedValue1 = translatedValue0;
                translatedValue0 = temp;
            }

            double rectStart = calculateBarW0(plot, plot.getOrientation(), dataArea, domainAxis, state, row, column);
            double rectLength = Math.abs(translatedValue1 - translatedValue0);
            double rectBreadth = state.getBarWidth();

            // DRAW THE BARS...
            Rectangle2D bar = null;

            if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                bar = new Rectangle2D.Double(translatedValue0, rectStart, rectLength, rectBreadth);
            } else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                bar = new Rectangle2D.Double(rectStart, translatedValue0, rectBreadth, rectLength);
            }

            Rectangle2D completeBar = null;
            Rectangle2D incompleteBar = null;
            Number percent = dataset.getPercentComplete(row, column, subinterval);
            double start = getStartPercent();
            double end = getEndPercent();
            if (percent != null) {
                double p = percent.doubleValue();
                if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                    completeBar = new Rectangle2D.Double(translatedValue0, rectStart + start * rectBreadth, rectLength * p,
                            rectBreadth * (end - start));
                    incompleteBar = new Rectangle2D.Double(translatedValue0 + rectLength * p, rectStart + start * rectBreadth,
                            rectLength * (1 - p), rectBreadth * (end - start));
                } else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                    completeBar = new Rectangle2D.Double(rectStart + start * rectBreadth,
                            translatedValue0 + rectLength * (1 - p), rectBreadth * (end - start), rectLength * p);
                    incompleteBar = new Rectangle2D.Double(rectStart + start * rectBreadth, translatedValue0, rectBreadth
                            * (end - start), rectLength * (1 - p));
                }

            }

            Paint seriesPaint = getItemPaint(row, column);
            g2.setPaint(seriesPaint);
            g2.fill(bar);
            if (completeBar != null) {
                g2.setPaint(getCompletePaint());
                g2.fill(completeBar);
            }
            if (incompleteBar != null) {
                g2.setPaint(getIncompletePaint());
                g2.fill(incompleteBar);
            }
            if (isDrawBarOutline() && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
                g2.setStroke(getItemStroke(row, column));
                g2.setPaint(getItemOutlinePaint(row, column));
                g2.draw(bar);
            }

            // collect entity and tool tip information...
            if (state.getInfo() != null) {
                EntityCollection entities = state.getEntityCollection();
                if (entities != null) {
                    String tip = null;
                    if (getToolTipGenerator(row, column) != null) {
                        tip = getToolTipGenerator(row, column).generateToolTip(dataset, row, column);
                    }
                    String url = null;
                    if (getItemURLGenerator(row, column) != null) {
                        url = getItemURLGenerator(row, column).generateURL(dataset, row, column);
                    }
                    CategoryItemEntity entity = new CategoryItemEntity(bar, tip, url, dataset, row, dataset.getColumnKey(column),
                            column);
                    entities.add(entity);
                }
            }
        }
    }

    /**
     * Draws a single task.
     * 
     * @param g2 the graphics device.
     * @param state the renderer state.
     * @param dataArea the data plot area.
     * @param plot the plot.
     * @param domainAxis the domain axis.
     * @param rangeAxis the range axis.
     * @param dataset the data.
     * @param row the row index (zero-based).
     * @param column the column index (zero-based).
     */
    protected void drawTask(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot,
            CategoryAxis domainAxis, ValueAxis rangeAxis, GanttCategoryDataset dataset, int row, int column) {

        PlotOrientation orientation = plot.getOrientation();

        RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();

        // Y0
        Number value0 = dataset.getEndValue(row, column);
        if (value0 == null) {
            return;
        }
        double java2dValue0 = rangeAxis.valueToJava2D(value0.doubleValue(), dataArea, rangeAxisLocation);

        // Y1
        Number value1 = dataset.getStartValue(row, column);
        if (value1 == null) {
            return;
        }
        double java2dValue1 = rangeAxis.valueToJava2D(value1.doubleValue(), dataArea, rangeAxisLocation);

        if (java2dValue1 < java2dValue0) {
            double temp = java2dValue1;
            java2dValue1 = java2dValue0;
            java2dValue0 = temp;
            Number tempNum = value1;
            value1 = value0;
            value0 = tempNum;
        }

        // count the number of non-null values
        int totalBars = countNonNullValues(dataset, column);
        if (totalBars == 0) {
            return;
        }
        // count non-null values up to but not including the current value
        int priorBars = countPriorNonNullValues(dataset, column, row);

        // double rectStart = calculateBarW0(plot, orientation, dataArea,
        // domainAxis, state, row, column);
        // double rectBreadth = state.getBarWidth();

        double rectBreadth = (domainAxis.getCategoryEnd(column, getColumnCount(), dataArea, plot.getDomainAxisEdge()) - domainAxis
                .getCategoryStart(column, getColumnCount(), dataArea, plot.getDomainAxisEdge())) / totalBars;
        double rectStart = domainAxis.getCategoryStart(column, getColumnCount(), dataArea, plot.getDomainAxisEdge())
                + rectBreadth * priorBars;
        double rectLength = Math.abs(java2dValue1 - java2dValue0);

        Rectangle2D bar = null;
        if (orientation == PlotOrientation.HORIZONTAL) {
            bar = new Rectangle2D.Double(java2dValue0, rectStart, rectLength, rectBreadth);
        } else if (orientation == PlotOrientation.VERTICAL) {
            bar = new Rectangle2D.Double(rectStart, java2dValue1, rectBreadth, rectLength);
        }

        Rectangle2D completeBar = null;
        Rectangle2D incompleteBar = null;
        Number percent = dataset.getPercentComplete(row, column);
        double start = getStartPercent();
        double end = getEndPercent();
        if (percent != null) {
            double p = percent.doubleValue();
            if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                completeBar = new Rectangle2D.Double(java2dValue0, rectStart + start * rectBreadth, rectLength * p, rectBreadth
                        * (end - start));
                incompleteBar = new Rectangle2D.Double(java2dValue0 + rectLength * p, rectStart + start * rectBreadth, rectLength
                        * (1 - p), rectBreadth * (end - start));
            } else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                completeBar = new Rectangle2D.Double(rectStart + start * rectBreadth, java2dValue1 + rectLength * (1 - p),
                        rectBreadth * (end - start), rectLength * p);
                incompleteBar = new Rectangle2D.Double(rectStart + start * rectBreadth, java2dValue1,
                        rectBreadth * (end - start), rectLength * (1 - p));
            }

        }

        Paint seriesPaint = getItemPaint(row, column);
        g2.setPaint(seriesPaint);
        g2.fill(bar);

        if (completeBar != null) {
            g2.setPaint(getCompletePaint());
            g2.fill(completeBar);
        }
        if (incompleteBar != null) {
            g2.setPaint(getIncompletePaint());
            g2.fill(incompleteBar);
        }

        // draw the outline...
        if (isDrawBarOutline() && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
            Stroke stroke = getItemOutlineStroke(row, column);
            Paint paint = getItemOutlinePaint(row, column);
            if (stroke != null && paint != null) {
                g2.setStroke(stroke);
                g2.setPaint(paint);
                g2.draw(bar);
            }
        }

        CategoryItemLabelGenerator generator = getItemLabelGenerator(row, column);
        if (generator != null && isItemLabelVisible(row, column)) {
            drawItemLabel(g2, dataset, row, column, plot, generator, bar, false);
        }

        // collect entity and tool tip information...
        if (state.getInfo() != null) {
            EntityCollection entities = state.getEntityCollection();
            if (entities != null) {
                String tip = null;
                CategoryToolTipGenerator tipster = getToolTipGenerator(row, column);
                if (tipster != null) {
                    tip = tipster.generateToolTip(dataset, row, column);
                }
                String url = null;
                if (getItemURLGenerator(row, column) != null) {
                    url = getItemURLGenerator(row, column).generateURL(dataset, row, column);
                }
                CategoryItemEntity entity = new CategoryItemEntity(bar, tip, url, dataset, row, dataset.getColumnKey(column),
                        column);
                entities.add(entity);
            }
        }

    }

    /**
     * Calculates the coordinate of the first "side" of a bar. This will be the minimum x-coordinate for a vertical bar,
     * and the minimum y-coordinate for a horizontal bar.
     * 
     * @param plot the plot.
     * @param orientation the plot orientation.
     * @param dataArea the data area.
     * @param domainAxis the domain axis.
     * @param state the renderer state (has the bar width precalculated).
     * @param row the row index.
     * @param column the column index.
     * 
     * @return The coordinate.
     */
    @Override
    protected double calculateBarW0(CategoryPlot plot, PlotOrientation orientation, Rectangle2D dataArea,
            CategoryAxis domainAxis, CategoryItemRendererState state, int row, int column) {
        // calculate bar width...
        double space = 0.0;
        if (orientation == PlotOrientation.HORIZONTAL) {
            space = dataArea.getHeight();
        } else {
            space = dataArea.getWidth();
        }
        double barW0 = domainAxis.getCategoryStart(column, getColumnCount(), dataArea, plot.getDomainAxisEdge());
        int seriesCount = getRowCount();
        int categoryCount = getColumnCount();
        if (seriesCount > 1) {
            double seriesGap = space * getItemMargin() / (categoryCount * (seriesCount - 1));
            double seriesW = calculateSeriesWidth(space, domainAxis, categoryCount, seriesCount);
            barW0 = barW0 + row * (seriesW + seriesGap) + (seriesW / 2.0) - (state.getBarWidth() / 2.0);
        } else {
            barW0 = domainAxis.getCategoryMiddle(column, getColumnCount(), dataArea, plot.getDomainAxisEdge())
                    - state.getBarWidth() / 2.0;
        }
        return barW0;
    }

    private int countNonNullValues(CategoryDataset dataset, int column) {
        return countPriorNonNullValues(dataset, column, dataset.getRowCount());
    }

    private int countPriorNonNullValues(CategoryDataset dataset, int column, int row) {
        if (row == 0) {
            return 0;
        }
        int count = 0;
        for (int r = 0; r < row; r++) {
            if (dataset.getValue(r, column) != null) {
                count++;
            }
        }
        return count;
    }

}
