// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.urls.StandardXYZURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.PieDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.talend.dataprofiler.chart.ChartDecorator;
import org.talend.dataprofiler.chart.TalendBarRenderer;
import org.talend.dataprofiler.chart.i18n.Messages;
import org.talend.dataprofiler.chart.preview.DQRuleItemLabelGenerator;
import org.talend.dataprofiler.service.utils.ValueAggregator;

/**
 * @author scorreia
 * 
 * Chart factory adapted for TOP.
 */
public final class TopChartFactory {

    private static final int BASE_ITEM_LABEL_SIZE = 12;

    private static final int BASE_LABEL_SIZE = 12;

    private static final int BASE_TICK_LABEL_SIZE = 10;

    private static final int BASE_LEGEND_LABEL_SIZE = 10;

    private static final int BASE_TITLE_LABEL_SIZE = 14;

    public static final String NULL_FIELD = "<null>"; //$NON-NLS-1$

    public static final String NULL_FIELD2 = "Null field"; //$NON-NLS-1$

    public static final String EMPTY_FIELD = "Empty field"; //$NON-NLS-1$

    public static final Logger log = Logger.getLogger(TopChartFactory.class);

    private TopChartFactory() {
    }

    /**
     * Creates a bubble chart with default settings. The chart is composed of an {@link XYPlot}, with a {@link NumberAxis} for the
     * domain axis, a {@link NumberAxis} for the range axis, and an {@link XYBubbleRenderer} to draw the data items.
     * 
     * This method is copied from
     * {@link org.jfree.chart.ChartFactory#createBubbleChart(String, String, String, XYZDataset, PlotOrientation, boolean, boolean, boolean)}
     * 
     * @param title the chart title (<code>null</code> permitted).
     * @param xAxisLabel a label for the X-axis (<code>null</code> permitted).
     * @param yAxisLabel a label for the Y-axis (<code>null</code> permitted).
     * @param dataset the dataset for the chart (<code>null</code> permitted).
     * @param orientation the orientation (horizontal or vertical) (<code>null</code> NOT permitted).
     * @param legend a flag specifying whether or not a legend is required.
     * @param tooltips configure chart to generate tool tips?
     * @param urls configure chart to generate URLs?
     * 
     * @return A bubble chart.
     */
    public static JFreeChart createBubbleChart(String title, String xAxisLabel, String yAxisLabel, XYZDataset dataset,
            PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {

        if (orientation == null) {
            throw new IllegalArgumentException(Messages.getString("TopChartFactory.argument")); //$NON-NLS-1$
        }
        NumberAxis xAxis = new NumberAxis(xAxisLabel);
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis(yAxisLabel);
        yAxis.setAutoRangeIncludesZero(false);

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);

        XYItemRenderer renderer = new XYBubbleRenderer(XYBubbleRenderer.SCALE_ON_RANGE_AXIS) {

            @Override
            public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
                    XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item,
                    CrosshairState crosshairState, int pass) {

                // return straight away if the item is not visible
                if (!getItemVisible(series, item)) {
                    return;
                }

                PlotOrientation orientation = plot.getOrientation();

                // get the data point...
                double x = dataset.getXValue(series, item);
                double y = dataset.getYValue(series, item);
                double z = Double.NaN;
                if (dataset instanceof XYZDataset) {
                    XYZDataset xyzData = (XYZDataset) dataset;
                    z = xyzData.getZValue(series, item);
                }
                if (!Double.isNaN(z)) {
                    RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
                    RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
                    double transX = domainAxis.valueToJava2D(x, dataArea, domainAxisLocation);
                    double transY = rangeAxis.valueToJava2D(y, dataArea, rangeAxisLocation);

                    double transDomain = 0.0;
                    double transRange = 0.0;
                    double zero;

                    // MOD scorreia +2L avoid points: minimal size of circle must be 1
                    // z = z * transX + 1;

                    // ADD xqliu 2009-07-06 bug 8035
                    double zSize = getBubbleSize(z); // calculate the multiple of bubble's default size
                    z = 0; // use bubble's default size
                    // ~

                    switch (getScaleType()) {
                    case SCALE_ON_DOMAIN_AXIS:
                        zero = domainAxis.valueToJava2D(0.0, dataArea, domainAxisLocation);
                        transDomain = domainAxis.valueToJava2D(z, dataArea, domainAxisLocation) - zero;
                        transRange = transDomain;
                        break;
                    case SCALE_ON_RANGE_AXIS:
                        zero = rangeAxis.valueToJava2D(0.0, dataArea, rangeAxisLocation);
                        transRange = zero - rangeAxis.valueToJava2D(z, dataArea, rangeAxisLocation);
                        transDomain = transRange;
                        break;
                    default:
                        double zero1 = domainAxis.valueToJava2D(0.0, dataArea, domainAxisLocation);
                        double zero2 = rangeAxis.valueToJava2D(0.0, dataArea, rangeAxisLocation);
                        transDomain = domainAxis.valueToJava2D(z, dataArea, domainAxisLocation) - zero1;
                        transRange = zero2 - rangeAxis.valueToJava2D(z, dataArea, rangeAxisLocation);
                    }
                    transDomain = Math.abs(transDomain);
                    transRange = Math.abs(transRange);

                    // MODSCA 2008-11-27 enlarge ellipse by diag% of the total diagonal
                    double diag = Math.sqrt(dataArea.getHeight() * dataArea.getHeight() + dataArea.getWidth()
                            * dataArea.getWidth());
                    transDomain += diag / 100;
                    transRange += diag / 100;

                    Ellipse2D circle = null;

                    // ADD xqliu 2009-07-06 bug 8035
                    transDomain *= zSize;
                    transRange *= zSize;
                    // ~

                    if (orientation == PlotOrientation.VERTICAL) {
                        circle = new Ellipse2D.Double(transX - transDomain / 2.0, transY - transRange / 2.0, transDomain,
                                transRange);
                    } else if (orientation == PlotOrientation.HORIZONTAL) {
                        circle = new Ellipse2D.Double(transY - transRange / 2.0, transX - transDomain / 2.0, transRange,
                                transDomain);
                    }
                    g2.setPaint(getItemPaint(series, item));
                    g2.fill(circle);
                    g2.setStroke(getItemOutlineStroke(series, item));
                    g2.setPaint(getItemOutlinePaint(series, item));
                    g2.draw(circle);

                    if (isItemLabelVisible(series, item)) {
                        if (orientation == PlotOrientation.VERTICAL) {
                            drawItemLabel(g2, orientation, dataset, series, item, transX, transY, false);
                        } else if (orientation == PlotOrientation.HORIZONTAL) {
                            drawItemLabel(g2, orientation, dataset, series, item, transY, transX, false);
                        }
                    }

                    // add an entity if this info is being collected
                    EntityCollection entities = null;
                    if (info != null) {
                        entities = info.getOwner().getEntityCollection();
                        if (entities != null && circle.intersects(dataArea)) {
                            addEntity(entities, circle, dataset, series, item, circle.getCenterX(), circle.getCenterY());
                        }
                    }

                    int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
                    int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
                    updateCrosshairValues(crosshairState, x, y, domainAxisIndex, rangeAxisIndex, transX, transY, orientation);
                }

            }

            /**
             * DOC xqliu : calculate the size of bubble. for bug 8035 2009-07-06.
             * 
             * @param z multiple of bubble's default size
             * @return
             */
            private double getBubbleSize(double z) {
                if (z > 0 && z <= 10) {
                    return 2;
                } else if (z > 10 && z <= 100) {
                    return 3;
                } else if (z > 100) {
                    return 4;
                }
                return 1;
            }

        };
        if (tooltips) {
            renderer.setBaseToolTipGenerator(new StandardXYZToolTipGenerator());
        }
        if (urls) {
            renderer.setURLGenerator(new StandardXYZURLGenerator());
        }
        plot.setRenderer(renderer);
        plot.setOrientation(orientation);

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);

        return chart;

    }

    /**
     * Method "createBubbleChart".
     * 
     * @param indic the indicator
     * @param numericColumn the analyzed numeric column
     * @return the bubble chart
     */
    public static JFreeChart createBubbleChart(String chartName, Object dataset, Map<String, ValueAggregator> xyzDatasets) {
        final Map<String, ValueAggregator> xyzDatasetsFinal = xyzDatasets;
        JFreeChart chart = TopChartFactory
                .createBubbleChart(
                        chartName,
                        Messages.getString("TopChartFactory.average"), Messages.getString("TopChartFactory.count"), (DefaultXYZDataset) dataset, PlotOrientation.HORIZONTAL, //$NON-NLS-1$ //$NON-NLS-2$
                        true, true, true);
        final XYPlot plot = (XYPlot) chart.getPlot();
        final XYItemRenderer renderer = plot.getRenderer();
        renderer.setBaseToolTipGenerator(new StandardXYZToolTipGenerator() {

            private static final long serialVersionUID = 1L;

            /*
             * (non-Javadoc)
             * 
             * @see org.jfree.chart.labels.StandardXYZToolTipGenerator#createItemArray(org.jfree.data.xy.XYZDataset,
             * int, int)
             */
            @Override
            protected Object[] createItemArray(XYZDataset dset, int series, int item) {
                final Comparable<?> seriesKey = dset.getSeriesKey(series);
                final String seriesK = String.valueOf(seriesKey);
                String label = seriesK;
                if (xyzDatasetsFinal != null) {
                    ValueAggregator valueAggregator = xyzDatasetsFinal.get(seriesKey);
                    label = valueAggregator.getLabels(seriesK).get(item);
                }
                final Object[] itemArray = super.createItemArray(dset, series, item);
                itemArray[0] = label;// label;
                itemArray[1] = "avg=" + itemArray[1]; //$NON-NLS-1$
                itemArray[2] = "record count=" + itemArray[2]; //$NON-NLS-1$
                itemArray[3] = "null count=" + itemArray[3]; //$NON-NLS-1$
                return itemArray;
            }

        });
        return chart;
    }

    /**
     * 
     * DOC zhaoxinyi Comment method "createGanttChart".
     * 
     * @return
     */

    public static JFreeChart createGanttChart(String chartAxies, Object ganttDataset) {
        // ADD msjian TDQ-5112 2012-4-10: after upgrate to jfreechart-1.0.12.jar, change the default chart wallPaint
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // TDQ-5112~
        JFreeChart chart = ChartFactory.createGanttChart("", // chart title //$NON-NLS-1$
                Messages.getString("TopChartFactory.Categories"), // domain axis label //$NON-NLS-1$
                chartAxies, // range axis label
                (TaskSeriesCollection) ganttDataset, // data
                true, // include legend
                true, // tooltips
                false // urls
                );

        // ADD TDQ-5251 msjian 2012-7-31: do not display the shadow
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);
        // TDQ-5251~

        return chart;
    }

    /**
     * 
     * mzhao create bar chart with default bar render class.
     * 
     * @param titile
     * @param dataset
     * @param showLegend
     * @return
     */
    public static JFreeChart createBarChart(String titile, CategoryDataset dataset, boolean showLegend) {
        // MOD hcheng for 6965,Use 2D bar charts instead of 3D bar charts
        // ADD msjian TDQ-5112 2012-4-10: after upgrate to jfreechart-1.0.12.jar, change the default chart wallPaint
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // TDQ-5112~
        JFreeChart chart = ChartFactory.createBarChart(null, titile,
                Messages.getString("TopChartFactory.count"), dataset, PlotOrientation.VERTICAL, showLegend, //$NON-NLS-1$
                true, false);
        ChartDecorator.decorateBarChart(chart, new TalendBarRenderer(true, ChartDecorator.COLOR_LIST));
        return chart;
    }

    /**
     * create bar chart.
     * 
     * @param titile
     * @param dataset
     * @return
     */
    public static JFreeChart createBarChart(String title, CategoryDataset dataset) {
        return createBarChartByKCD(title, dataset, null);
    }

    /**
     * create bar chart.
     * 
     * @param titile
     * @param dataset
     * @return
     */
    public static JFreeChart createBarChartByKCD(String title, CategoryDataset dataset, Object cusmomerDataset) {
        // ADD msjian TDQ-5112 2012-4-10: after upgrate to jfreechart-1.0.12.jar, change the default chart wallPaint
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // TDQ-13378 add to vertical scroll bar. the scroll bar will be visible when the count >10
        SlidingCategoryDataset slideDataset = new SlidingCategoryDataset(dataset, 0, 10);
        // TDQ-5112~
        JFreeChart createBarChart = ChartFactory.createBarChart(null,
                                Messages.getString("TopChartFactory.Value"), title, slideDataset, PlotOrientation.HORIZONTAL, false, false, false); //$NON-NLS-1$

        CategoryPlot plot = createBarChart.getCategoryPlot();
        if (cusmomerDataset != null) {
            plot.setDataset(1, new EncapsulationCumstomerDataset(slideDataset, cusmomerDataset));
        }

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelPaint(NULL_FIELD, Color.RED);
        domainAxis.setTickLabelPaint(NULL_FIELD2, Color.RED);
        domainAxis.setTickLabelPaint(EMPTY_FIELD, Color.RED);

        // ADD TDQ-5251 msjian 2012-7-31: do not display the shadow
        BarRenderer renderer = new TalendBarRenderer(false, ChartDecorator.COLOR_LIST);
        renderer.setShadowVisible(false);
        // TDQ-5251~

        plot.setRenderer(renderer);
        return createBarChart;
    }

    /**
     * 
     * DOC zshen Comment method "createMatchRuleBarChart".
     * 
     * @param title
     * @param dataset
     * @return
     */
    public static JFreeChart createMatchRuleBarChart(String categoryAxisLabel, String valueAxisLabel, CategoryDataset dataset) {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart localJFreeChart = ChartFactory.createBarChart(null, categoryAxisLabel, valueAxisLabel, dataset,
                PlotOrientation.VERTICAL, false, true, false);

        localJFreeChart.addSubtitle(new TextTitle(Messages.getString(
                "DataChart.title", sumItemCount(dataset), sumGroupCount(dataset)))); //$NON-NLS-1$
        CategoryPlot plot = (CategoryPlot) localJFreeChart.getPlot();
        // get real color list from ChartDecorator.COLOR_LIST dataset.getColumnKeys()
        List<Color> currentColorList = null;
        try {
            currentColorList = getCurrentColorList(dataset.getColumnKeys());
        } catch (NumberFormatException e) {
            log.warn(e, e);
            currentColorList = ChartDecorator.COLOR_LIST;
        }
        BarRenderer barRenderer = new TalendBarRenderer(true, currentColorList);
        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barRenderer.setBaseItemLabelsVisible(true);
        // remove the shadow
        barRenderer.setShadowVisible(Boolean.FALSE);
        plot.setRenderer(barRenderer);

        CategoryAxis localCategoryAxis = plot.getDomainAxis();
        localCategoryAxis.setCategoryMargin(0.25D);
        localCategoryAxis.setUpperMargin(0.02D);
        localCategoryAxis.setLowerMargin(0.02D);

        NumberAxis localNumberAxis = (NumberAxis) plot.getRangeAxis();
        localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        localNumberAxis.setUpperMargin(0.1D);
        return localJFreeChart;
    }

    /**
     * DOC talend Comment method "getCurrentColorList".
     * 
     * @param columnKeys
     * @return
     */
    private static List<Color> getCurrentColorList(List<?> columnKeys) {
        List<Color> colorList = new ArrayList<>();
        int colorSize = ChartDecorator.COLOR_LIST.size();
        for (Object columnKey : columnKeys) {
            int groupSize = Integer.parseInt(columnKey.toString());
            colorList.add(ChartDecorator.COLOR_LIST.get(Math.abs((groupSize - 1) % colorSize)));
        }
        return colorList;
    }

    public static JFreeChart createBlockingBarChart(String title, HistogramDataset dataset) {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart chart = ChartFactory.createHistogram(null, title, "Key frequency", dataset, PlotOrientation.VERTICAL, false, //$NON-NLS-1$
                true, false);

        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setUpperMargin(0.08);
        // plot.getRangeAxis().setLowerBound(-0.08);
        decorateCategoryPlot(chart);
        plot.setRangeGridlinesVisible(true);

        XYBarRenderer renderer = new XYBarRenderer() {

            private static final long serialVersionUID = 4168794048090452033L;

            @Override
            public Paint getItemPaint(int row, int column) {
                return ChartDecorator.COLOR_LIST.get(0);
            }
        };
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setShadowVisible(Boolean.FALSE);
        plot.setRenderer(renderer);

        return chart;
    }

    private static void decorateCategoryPlot(JFreeChart chart) {

        XYPlot plot = chart.getXYPlot();
        XYItemRenderer render = plot.getRenderer();
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis valueAxis = plot.getRangeAxis();

        Font font = new Font("Tahoma", Font.BOLD, BASE_ITEM_LABEL_SIZE); //$NON-NLS-1$

        render.setBaseItemLabelFont(font);
        font = new Font("sans-serif", Font.BOLD, BASE_LABEL_SIZE); //$NON-NLS-1$
        domainAxis.setLabelFont(font);
        font = new Font("sans-serif", Font.BOLD, BASE_LABEL_SIZE); //$NON-NLS-1$
        valueAxis.setLabelFont(font);
        font = new Font("sans-serif", Font.PLAIN, BASE_TICK_LABEL_SIZE); //$NON-NLS-1$
        domainAxis.setTickLabelFont(font);
        valueAxis.setTickLabelFont(font);
        font = new Font("Tahoma", Font.PLAIN, BASE_LEGEND_LABEL_SIZE); //$NON-NLS-1$
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setItemFont(font);
        }
        font = new Font("sans-serif", Font.BOLD, BASE_TITLE_LABEL_SIZE); //$NON-NLS-1$
        TextTitle title = chart.getTitle();
        if (title != null) {
            title.setFont(font);
        }
        font = null;
        if (render instanceof BarRenderer) {
            int rowCount = chart.getCategoryPlot().getDataset().getRowCount();
            domainAxis.setUpperMargin(0.1);
            // domainAxis.setMaximumCategoryLabelLines(10);
            ((BarRenderer) render).setItemMargin(-0.40 * rowCount);
        }
        // set color
        int rowCount = chart.getXYPlot().getDataset().getSeriesCount();
        for (int i = 0; i < rowCount; i++) {
            plot.getRenderer().setSeriesPaint(i, Color.RED);
        }

    }

    private static double sumItemCount(CategoryDataset categorydataset) {
        double itemCount = 0;
        for (int i = 0; i < categorydataset.getColumnCount(); i++) {
            int columnKey = Integer.valueOf(categorydataset.getColumnKey(i).toString());
            itemCount += categorydataset.getValue(0, i).intValue() * columnKey;
        }
        return itemCount;
    }

    private static double sumGroupCount(CategoryDataset categorydataset) {
        double groupCount = 0.0;
        for (int i = 0; i < categorydataset.getColumnCount(); i++) {
            groupCount += categorydataset.getValue(0, i).doubleValue();
        }
        return groupCount;
    }

    /**
     * DOC Zqin Comment method "createBoxAndWhiskerChart".
     * 
     * @param title
     * @param dataset
     * @return
     */
    public static JFreeChart createBoxAndWhiskerChart(String title, BoxAndWhiskerCategoryDataset dataset) {
        // ADD msjian TDQ-5112 2012-4-10: after upgrate to jfreechart-1.0.12.jar, change the default chart wallPaint
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // TDQ-5112~
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(null, title,
                Messages.getString("TopChartFactory.Value"), dataset, false); //$NON-NLS-1$
        CategoryPlot plot = chart.getCategoryPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRange(false);

        double min = dataset.getMinRegularValue("0", "").doubleValue(); //$NON-NLS-1$ //$NON-NLS-2$
        double max = dataset.getMaxRegularValue("0", "").doubleValue(); //$NON-NLS-1$ //$NON-NLS-2$

        double unit = (max - min) / 10;
        rangeAxis.setRange(min - unit, max + unit);
        rangeAxis.setTickUnit(new NumberTickUnit(unit));

        BoxAndWhiskerRenderer renderer = (BoxAndWhiskerRenderer) plot.getRenderer();
        renderer.setArtifactPaint(ChartDecorator.COLOR_LIST.get(1));

        return chart;
    }

    /**
     * DOC Zqin Comment method "createStackedBarChart".
     * 
     * @param titile
     * @param dataset
     * @param orientation
     * @return
     */
    public static JFreeChart createStackedBarChart(String titile, CategoryDataset dataset, PlotOrientation orientation) {
        return createStackedBarChart(null, null, Messages.getString("TopChartFactory.Value"), dataset, orientation, //$NON-NLS-1$
                true, false, false);
    }

    /**
     * DOC xqliu Comment method "createStackedBarChart".
     * 
     * @param title
     * @param dataset
     * @param showLegend
     * @return
     */
    public static JFreeChart createStackedBarChart(String title, CategoryDataset dataset, boolean showLegend) {
        return createStackedBarChart(null, title, Messages.getString("TopChartFactory.Value"), dataset, //$NON-NLS-1$
                PlotOrientation.VERTICAL, showLegend, true, false);
    }

    /**
     * DOC xqliu Comment method "createStackedBarChart".
     * 
     * @param title
     * @param dataset
     * @param orientation
     * @param showLegend
     * @return
     */
    public static JFreeChart createStackedBarChart(String title, CategoryDataset dataset, PlotOrientation orientation,
            boolean showLegend) {
        return createStackedBarChart(null, null, title, dataset, orientation, showLegend, false, false);
    }

    /**
     * DOC xqliu Comment method "createStackedBarChart".
     * 
     * @param title
     * @param domainAxisLabel
     * @param rangeAxisLabel
     * @param dataset
     * @param orientation
     * @param legend
     * @param tooltips
     * @param urls
     * @return
     */
    public static JFreeChart createStackedBarChart(String title, String domainAxisLabel, String rangeAxisLabel,
            CategoryDataset dataset, PlotOrientation orientation, boolean legend, boolean tooltips, boolean urls) {
        // ADD msjian TDQ-5112 2012-4-10: after upgrate to jfreechart-1.0.12.jar, change the default chart wallPaint
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // TDQ-5112~
        final JFreeChart chart = ChartFactory.createStackedBarChart(title, domainAxisLabel, rangeAxisLabel, dataset, orientation,
                legend, tooltips, urls);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangeGridlinesVisible(true);

        StackedBarRenderer sbr = (StackedBarRenderer) plot.getRenderer();
        sbr.setBaseItemLabelsVisible(true);
        sbr.setRenderAsPercentages(true);
        sbr.setBaseItemLabelGenerator(new DQRuleItemLabelGenerator("{3}", NumberFormat.getIntegerInstance())); //$NON-NLS-1$
        sbr.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        // ADD xqliu 2010-03-10 feature 10834
        // sbr.setBaseToolTipGenerator(new DQRuleToolTipGenerator(ChartDecorator.NEW_TOOL_TIP_FORMAT_STRING,
        // NumberFormat
        // .getInstance()));
        // ~10834

        // ADD TDQ-5251 msjian 2012-7-31: do not display the shadow
        sbr.setShadowVisible(false);
        // TDQ-5251~

        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        axis.setUpperMargin(0.05f);
        axis.setLowerMargin(0.01f);

        return chart;
    }

    /**
     * DOC xqliu Comment method "createLineChart".
     * 
     * @param title
     * @param dataset
     * @param showLegend
     * @return
     */
    public static JFreeChart createLineChart(String title, XYDataset dataset, boolean showLegend) {
        // ADD msjian TDQ-5112 2012-4-10: after upgrate to jfreechart-1.0.12.jar, change the default chart wallPaint
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // TDQ-5112~
        final JFreeChart chart = ChartFactory.createXYLineChart(null, title,
                Messages.getString("TopChartFactory.Percent"), dataset, PlotOrientation.VERTICAL, //$NON-NLS-1$
                showLegend, false, false);

        final XYPlot plot = chart.getXYPlot();

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

    /**
     * 
     * DOC qiongli Comment method "createPieChart".
     * 
     * @param title
     * @param dataset
     * @param showLegend
     * @param toolTips,if show the toolTips.
     * @param urls
     * @return
     */
    public static JFreeChart createPieChart(String title, PieDataset dataset, boolean showLegend, boolean toolTips, boolean urls) {
        // ADD msjian TDQ-5112 2012-4-10: after upgrate to jfreechart-1.0.12.jar, change the default chart wallPaint
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        // TDQ-5112~
        return ChartFactory.createPieChart(title, dataset, showLegend, toolTips, urls);
    }

    public static JFreeChart createDuplicateRecordPieChart(String title, PieDataset dataset, boolean showLegend,
            boolean toolTips, boolean urls) {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart pieChart = ChartFactory.createPieChart(title, dataset, showLegend, toolTips, urls);
        ChartDecorator.decorateDuplicatePieChart(pieChart);
        return pieChart;
    }
}
