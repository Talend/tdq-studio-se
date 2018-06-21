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
package org.talend.dataprofiler.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;
import org.talend.dataprofiler.chart.util.EncapsulationCumstomerDataset;
import org.talend.dataprofiler.chart.util.PluginConstant;

/**
 * DOC bzhou class global comment. Detailled comment
 */
public final class ChartDecorator {

    public static final String[] COLORS = { "#236192", "#C4D600", "#DB662A", "#F7A800", "#787121", "#00A9CE", "#A7A8AA",
            "#ECAB7C", "#B8B370", "#D4D3D3", "#83D3E6", "#FFD38B" };

    public static final List<Color> COLOR_LIST = new ArrayList<Color>();
    static {
        for (String color : COLORS) {
            COLOR_LIST.add(Color.decode(color));
        }
    }

    private static List<Color> PIE_COLOR_LIST = new ArrayList<Color>();
    static {
        PIE_COLOR_LIST.add(COLOR_LIST.get(0));
        PIE_COLOR_LIST.add(COLOR_LIST.get(1));
        PIE_COLOR_LIST.add(COLOR_LIST.get(3));
        PIE_COLOR_LIST.add(COLOR_LIST.get(2));
    }

    private static List<Color> DUPLICATE_PIE_COLOR_LIST = new ArrayList<Color>();
    static {
        DUPLICATE_PIE_COLOR_LIST.add(COLOR_LIST.get(2));
        DUPLICATE_PIE_COLOR_LIST.add(COLOR_LIST.get(1));
        DUPLICATE_PIE_COLOR_LIST.add(COLOR_LIST.get(0));
    }

    private static List<Color> STACK_BAR_COLOR_LIST = new ArrayList<Color>();
    static {
        STACK_BAR_COLOR_LIST.add(COLOR_LIST.get(2));
        STACK_BAR_COLOR_LIST.add(COLOR_LIST.get(1));
    }

    private static final int BASE_ITEM_LABEL_SIZE = 10;

    private static final int BASE_LABEL_SIZE = 12;

    private static final int BASE_TICK_LABEL_SIZE = 10;

    private static final int BASE_LEGEND_LABEL_SIZE = 10;

    private static final int BASE_TITLE_LABEL_SIZE = 14;

    /**
     * New format string. ADD yyi 2009-09-24 9243
     * */
    public static final String NEW_TOOL_TIP_FORMAT_STRING = "{0} = {2}"; //$NON-NLS-1$

    /**
     * Added TDQ-8673 yyin 20140415
     */
    private static final String DOUBLE_FORMAT = "0.00"; //$NON-NLS-1$

    // used for display double value in the chart
    private static final String PERCENT_FORMAT = "0.00%"; //$NON-NLS-1$

    /**
     * DOC bZhou ChartDecorator constructor comment.
     */
    private ChartDecorator() {
    }

    public static void decorateStackedBarChart(JFreeChart chart, PlotOrientation orientation) {
        if (chart != null) {
            Plot plot = chart.getPlot();
            if (plot instanceof CategoryPlot) {
                decorateCategoryPlot(chart, orientation);
                int rowCount = chart.getCategoryPlot().getDataset().getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    if (i >= STACK_BAR_COLOR_LIST.size()) {
                        STACK_BAR_COLOR_LIST.add(generateRandomColor(STACK_BAR_COLOR_LIST));
                    }
                    ((CategoryPlot) plot).getRenderer().setSeriesPaint(i, STACK_BAR_COLOR_LIST.get(i));
                }
            }
        }
    }

    /**
     * DOC bZhou Comment method "decorate".
     * 
     * @param chart
     */
    public static void decorate(JFreeChart chart, PlotOrientation orientation) {
        if (chart != null) {
            // TDQ-11522: Set white background on charts in the editors
            chart.setBackgroundPaint(Color.white);
            // TDQ-11522~
            Plot plot = chart.getPlot();
            if (plot instanceof CategoryPlot) {
                decorateCategoryPlot(chart, orientation);

                CategoryDataset dataset = chart.getCategoryPlot().getDataset();
                int rowCount = dataset != null ? dataset.getRowCount() : 20;
                for (int i = 0; i < rowCount; i++) {
                    // by zshen bug 14173 add the color in the colorList when chart need more the color than 8.
                    if (i >= COLOR_LIST.size()) {
                        COLOR_LIST.add(generateRandomColor(COLOR_LIST));
                    }
                    // ~14173
                    ((CategoryPlot) plot).getRenderer().setSeriesPaint(i, COLOR_LIST.get(i));
                }

            } else if (plot instanceof XYPlot) {
                decorateXYPlot(chart);

                int count = chart.getXYPlot().getDataset().getSeriesCount();
                for (int i = 0; i < count; i++) {
                    // by zshen bug 14173 add the color in the colorList when chart need the colors more than 8.
                    if (i >= COLOR_LIST.size()) {
                        COLOR_LIST.add(generateRandomColor(COLOR_LIST));
                    }
                    // ~14173
                    ((XYPlot) plot).getRenderer().setSeriesPaint(i, COLOR_LIST.get(i));
                }
            } else if (plot instanceof PiePlot) {
                decoratePiePlot(chart);

                // ADD msjian TDQ-8046 2013-10-17: add the color's control for pie chart
                PieDataset piedataset = ((PiePlot) plot).getDataset();
                for (int i = 0; i < piedataset.getItemCount(); i++) {
                    if (i >= PIE_COLOR_LIST.size()) {
                        PIE_COLOR_LIST.add(generateRandomColor(PIE_COLOR_LIST));
                    }
                    Comparable<?> key = piedataset.getKey(i);
                    ((PiePlot) plot).setSectionPaint(key, PIE_COLOR_LIST.get(i));
                }
                // TDQ-8046~
            }
        }
    }

    public static void decorateDuplicatePieChart(JFreeChart chart) {
        if (chart != null) {
            Plot plot = chart.getPlot();
            if (plot instanceof PiePlot) {
                decoratePiePlot(chart);

                // ADD msjian TDQ-8046 2013-10-17: add the color's control for pie chart
                PieDataset piedataset = ((PiePlot) plot).getDataset();
                for (int i = 0; i < piedataset.getItemCount(); i++) {
                    if (i >= DUPLICATE_PIE_COLOR_LIST.size()) {
                        DUPLICATE_PIE_COLOR_LIST.add(generateRandomColor(DUPLICATE_PIE_COLOR_LIST));
                    }
                    Comparable<?> key = piedataset.getKey(i);
                    ((PiePlot) plot).setSectionPaint(key, DUPLICATE_PIE_COLOR_LIST.get(i));
                }
                // TDQ-8046~
            }
        }
    }

    /**
     * 
     * decorate concept chart on semantic Analysis wizard.
     * 
     * @param chart
     * @param orientation
     */
    public static void decorateConceptChart(JFreeChart chart, PlotOrientation orientation) {
        if (chart == null) {
            return;
        }
        Plot plot = chart.getPlot();
        if (plot instanceof CategoryPlot) {
            decorateCategoryPlot(chart, orientation);

            int rowCount = chart.getCategoryPlot().getDataset().getRowCount();

            for (int i = 0; i < rowCount; i++) {
                ((CategoryPlot) plot).getRenderer().setSeriesPaint(i, PluginConstant.PRIMARY_BLUE_AWT);
            }

        }

    }

    /**
     * 
     * generate a Random Color.
     * 
     * @return a object of color which don't contain in list
     */
    private static Color generateRandomColor(List<Color> list) {
        Random rad = new Random();
        Color newColor = null;
        do {
            newColor = new Color(rad.nextInt(255), rad.nextInt(255), rad.nextInt(255));
        } while (list.contains(newColor) || Color.white.equals(newColor));
        return newColor;
    }

    /**
     * DOC xqliu Comment method "decorateColumnDependency".
     * 
     * @param chart
     */
    public static void decorateColumnDependency(JFreeChart chart) {
        decorate(chart, PlotOrientation.HORIZONTAL);
        CategoryItemRenderer renderer = ((CategoryPlot) chart.getPlot()).getRenderer();
        renderer.setSeriesPaint(0, COLOR_LIST.get(1));
        renderer.setSeriesPaint(1, COLOR_LIST.get(2));
    }

    /**
     * DOC msjian Comment method "decoratePatternMatching".
     * 
     * @param chart
     */
    public static void decoratePatternMatching(JFreeChart chart) {
        if (chart != null) {
            // TDQ-11522: Set white background on charts in the editors
            chart.setBackgroundPaint(Color.white);
            // TDQ-11522~
        }
    }

    /**
     * DOC bZhou Comment method "decorateCategoryPlot".
     * 
     * @param chart
     */
    public static void decorateCategoryPlot(JFreeChart chart, PlotOrientation orientation) {

        CategoryPlot plot = chart.getCategoryPlot();
        CategoryItemRenderer render = plot.getRenderer();
        CategoryAxis domainAxis = plot.getDomainAxis();
        // ADD msjian TDQ-5111 2012-4-9: set something look it well
        domainAxis.setCategoryMargin(0.1);
        domainAxis.setUpperMargin(0.05);
        domainAxis.setLowerMargin(0.05);
        domainAxis.setCategoryLabelPositionOffset(10);
        // TDQ-5111~

        ValueAxis valueAxis = plot.getRangeAxis();

        Font font = new Font("Tahoma", Font.BOLD, BASE_ITEM_LABEL_SIZE);//$NON-NLS-1$

        render.setBaseItemLabelFont(font);
        // MOD zshen 10998: change the font name 2010-01-16
        font = new Font("sans-serif", Font.BOLD, BASE_LABEL_SIZE);//$NON-NLS-1$
        domainAxis.setLabelFont(font);

        font = new Font("sans-serif", Font.BOLD, BASE_LABEL_SIZE);//$NON-NLS-1$
        valueAxis.setLabelFont(font);

        font = new Font("sans-serif", Font.PLAIN, BASE_TICK_LABEL_SIZE);//$NON-NLS-1$
        domainAxis.setTickLabelFont(font);
        valueAxis.setTickLabelFont(font);

        setLegendFont(chart);

        font = new Font("sans-serif", Font.BOLD, BASE_TITLE_LABEL_SIZE);//$NON-NLS-1$
        TextTitle title = chart.getTitle();
        if (title != null) {
            title.setFont(font);
        }

        font = null;

        if (render instanceof BarRenderer) {
            CategoryDataset dataset = chart.getCategoryPlot().getDataset();
            if (dataset != null) {
                int rowCount = dataset.getRowCount();
                List<?> columnKeys = dataset.getColumnKeys();
                if (!isContainCJKCharacter(columnKeys.toArray())) {
                    domainAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 10));//$NON-NLS-1$
                }
                ((BarRenderer) render).setItemMargin(-0.40 * rowCount);

                // TDQ-12621 add Tooltip for Lable
                for (Object colKey : columnKeys) {
                    domainAxis.addCategoryLabelToolTip(colKey.toString(), colKey.toString());
                }
            }
            domainAxis.setUpperMargin(0.1);
            // TDQ-12621 Only display in 1 line for the label, other chars will be displayed as "..."
            domainAxis.setMaximumCategoryLabelLines(1);

            // ADD msjian TDQ-5111 2012-4-9: set Bar Width and let it look well
            // not do this when the bar is horizontal Orientation
            if (orientation == null) {
                ((BarRenderer) render).setMaximumBarWidth(0.2);
            }
            // TDQ-5111~
        }
        // ~10998
    }

    /**
     * 
     * if it contians CJK, set Font to "Arial Unicode MS".Or else, the Font is "Tahoma".
     * 
     * @param chart
     */
    private static void setLegendFont(JFreeChart chart) {
        Font font;
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            font = new Font("Tahoma", Font.PLAIN, BASE_LEGEND_LABEL_SIZE);//$NON-NLS-1$
            // get legend label to judge if it contains CJK.
            LegendItemSource[] sources = legend.getSources();
            Set<String> itemLabels = new HashSet<String>();
            for (LegendItemSource source : sources) {
                LegendItemCollection legendItems = source.getLegendItems();
                for (int i = 0; i < legendItems.getItemCount(); i++) {
                    String label = legendItems.get(i).getLabel();
                    if (label != null) {
                        itemLabels.add(label);
                    }
                }
            }
            if (isContainCJKCharacter(itemLabels.toArray())) {
                font = getCJKFont(Font.PLAIN, BASE_LEGEND_LABEL_SIZE);
            }
            legend.setItemFont(font);
        }
    }

    /**
     * DOC bZhou Comment method "decorateXYPlot".
     * 
     * @param chart
     */
    private static void decorateXYPlot(JFreeChart chart) {

        Font font = null;
        XYPlot plot = chart.getXYPlot();
        XYItemRenderer render = plot.getRenderer();
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis valueAxis = plot.getRangeAxis();

        font = new Font("Tahoma", Font.BOLD, BASE_ITEM_LABEL_SIZE);//$NON-NLS-1$

        render.setBaseItemLabelFont(font);

        font = new Font("sans-serif", Font.BOLD, BASE_LABEL_SIZE);//$NON-NLS-1$
        domainAxis.setLabelFont(font);

        font = new Font("sans-serif", Font.BOLD, BASE_LABEL_SIZE);//$NON-NLS-1$
        valueAxis.setLabelFont(font);

        font = new Font("sans-serif", Font.PLAIN, BASE_TICK_LABEL_SIZE);//$NON-NLS-1$
        domainAxis.setTickLabelFont(font);
        valueAxis.setTickLabelFont(font);

        setLegendFont(chart);

        font = new Font("sans-serif", Font.BOLD, BASE_TITLE_LABEL_SIZE);//$NON-NLS-1$
        TextTitle title = chart.getTitle();
        if (title != null) {
            title.setFont(font);
        }

        font = null;
    }

    /**
     * 
     * DOC qiongli Comment method "decoratePiePlot".
     * 
     * @param chart
     */
    private static void decoratePiePlot(JFreeChart chart) {

        Font font = new Font("sans-serif", Font.BOLD, BASE_TITLE_LABEL_SIZE);//$NON-NLS-1$
        TextTitle textTitle = chart.getTitle();
        // MOD msjian TDQ-5213 2012-5-7: fixed NPE
        if (textTitle != null) {
            textTitle.setFont(font);
        }

        setLegendFont(chart);
        // TDQ-5213~
        PiePlot plot = (PiePlot) chart.getPlot();
        font = new Font("Monospaced", Font.PLAIN, 10);//$NON-NLS-1$
        plot.setLabelFont(font);
        plot.setNoDataMessage("No data available"); //$NON-NLS-1$
        StandardPieSectionLabelGenerator standardPieSectionLabelGenerator = new StandardPieSectionLabelGenerator(("{0}:{2}"),//$NON-NLS-1$
                NumberFormat.getNumberInstance(), new DecimalFormat(PERCENT_FORMAT));
        plot.setLabelGenerator(standardPieSectionLabelGenerator);
        plot.setLabelLinkPaint(Color.GRAY);
        plot.setLabelOutlinePaint(Color.WHITE);
        plot.setLabelGap(0.02D);
        plot.setOutlineVisible(false);
        plot.setMaximumLabelWidth(0.2D);
        plot.setCircular(false);
        // remove the shadow of the pie chart
        plot.setShadowXOffset(0);
        plot.setShadowYOffset(0);
    }

    /**
     * create bar chart with customized bar render class which can be adapted in JFreeChart class.
     * 
     * @param chart
     * @param barRenderer
     */
    public static void decorateBarChart(JFreeChart chart, BarRenderer barRenderer) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getRangeAxis().setUpperMargin(0.08);
        plot.setRangeGridlinesVisible(true);

        barRenderer.setBaseItemLabelsVisible(true);
        barRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        barRenderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        // MOD klliu 2010-09-25 bug15514: The chart of summary statistic indicators not beautiful
        barRenderer.setMaximumBarWidth(0.1);
        // renderer.setItemMargin(0.000000005);
        // renderer.setBase(0.04);
        // ADD yyi 2009-09-24 9243
        barRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(NEW_TOOL_TIP_FORMAT_STRING, NumberFormat
                .getInstance()));

        // ADD TDQ-5251 msjian 2012-7-31: do not display the shadow
        barRenderer.setShadowVisible(false);
        // TDQ-5251~

        // CategoryAxis domainAxis = plot.getDomainAxis();
        // domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        plot.setRenderer(barRenderer);
    }

    /**
     * Added TDQ-8673: set the display decimal format as: x.xx
     * 
     * @param chart
     */
    public static void setDisplayDecimalFormat(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();

        plot.getRenderer().setBaseItemLabelGenerator(
                new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat(DOUBLE_FORMAT))); //$NON-NLS-1$

    }

    /**
     * Decorate the benford law chart. in this method the line chart will be overlay on top of bar chart.
     * 
     * @param dataset
     * @param barChart
     * @param title
     * @param categoryAxisLabel
     * @param dotChartLabels
     * @param formalValues
     * @return JFreeChart
     */
    @SuppressWarnings("deprecation")
    public static JFreeChart decorateBenfordLawChart(CategoryDataset dataset, JFreeChart barChart, String title,
            String categoryAxisLabel, List<String> dotChartLabels, double[] formalValues) {
        return decorateBenfordLawChartByKCD(dataset, null, barChart, title, categoryAxisLabel, dotChartLabels, formalValues);
    }

    /**
     * Decorate the benford law chart. in this method the line chart will be overlay on top of bar chart.
     * 
     * @param dataset
     * @param barChart
     * @param title
     * @param categoryAxisLabel
     * @param dotChartLabels
     * @param formalValues
     * @return JFreeChart
     */
    @SuppressWarnings("deprecation")
    public static JFreeChart decorateBenfordLawChartByKCD(CategoryDataset dataset, Object customerDataset, JFreeChart barChart,
            String title, String categoryAxisLabel, List<String> dotChartLabels, double[] formalValues) {
        CategoryPlot barplot = barChart.getCategoryPlot();
        decorateBarChart(barChart, new BenfordLawLineAndShapeRenderer());
        // display percentage on top of the bar
        DecimalFormat df = new DecimalFormat(PERCENT_FORMAT);
        barplot.getRenderer().setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", df)); //$NON-NLS-1$
        barplot.getRenderer().setBasePositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
        // set the display of Y axis
        NumberAxis numAxis = (NumberAxis) barplot.getRangeAxis();
        numAxis.setNumberFormatOverride(df);

        CategoryDataset lineDataset = getLineDataset(dotChartLabels, formalValues);
        JFreeChart lineChart = ChartFactory.createLineChart(null, title, categoryAxisLabel, lineDataset,
                PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = lineChart.getCategoryPlot();
        if (customerDataset != null) {
            barplot.setDataset(2, new EncapsulationCumstomerDataset(dataset, customerDataset));
        }
        // show the value on the right axis of the chart(keep the comment)
        // NumberAxis numberaxis = new NumberAxis(DefaultMessagesImpl.getString("TopChartFactory.Value"));
        // plot.setRangeAxis(10, numberaxis);

        NumberAxis vn = (NumberAxis) plot.getRangeAxis();
        vn.setNumberFormatOverride(df);
        // set points format
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setPaint(COLOR_LIST.get(1));
        renderer.setSeriesShape(1, new Rectangle2D.Double(-1.5, -1.5, 3, 3));
        renderer.setShapesVisible(true); // show the point shape
        renderer.setBaseLinesVisible(false);// do not show the line

        // add the bar chart into the line chart
        CategoryItemRenderer barChartRender = barplot.getRenderer();
        barplot.setDataset(0, lineDataset);
        barplot.setRenderer(0, plot.getRenderer());
        barplot.setDataset(1, dataset);
        barplot.setRenderer(1, barChartRender);
        return barChart;
    }

    /**
     * 
     * created by mzhao on 2012-9-21 The customer render to paint bar color.
     * 
     */
    private static class BenfordLawLineAndShapeRenderer extends BarRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Paint getItemPaint(final int row, final int column) {
            return (column > 8) ? COLOR_LIST.get(2) : COLOR_LIST.get(0);
        }
    }

    /**
     * get the dataset of standard points.
     * 
     * @param dotChartLabels
     * @param formalValues
     * @return CategoryDataset
     */
    private static CategoryDataset getLineDataset(List<String> dotChartLabels, double[] formalValues) {
        DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
        for (int i = 0; i < dotChartLabels.size(); i++) {
            linedataset.addValue(formalValues[i], "Expected(%)", dotChartLabels.get(i)); //$NON-NLS-1$
        }
        return linedataset;
    }

    /**
     * Returns true if this string contains the chinese char values. DOC yyi Comment method
     * "isContainsChinese".2010-09-26:14692.
     * 
     * @param str
     * @return
     * @deprecated replace it with isContainCJKCharacter(String str)
     */
    @Deprecated
    private static boolean isContainsChineseColumn(JFreeChart chart) {
        Object[] columnNames = chart.getCategoryPlot().getDataset().getColumnKeys().toArray();
        String regEx = "[\u4e00-\u9fa5]";//$NON-NLS-1$
        Pattern pat = Pattern.compile(regEx);
        boolean flg = false;
        for (Object str : columnNames) {
            Matcher matcher = pat.matcher(str.toString());
            if (matcher.find()) {
                flg = true;
                break;
            }
        }
        return flg;
    }

    /**
     * 
     * if this String contain CJK array.
     * 
     * @param array
     * @return
     */
    private static boolean isContainCJKCharacter(Object[] array) {
        for (Object str : array) {
            if (str != null && isContainCJKCharacter(str.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * if this String contain CJK char.
     * 
     * @param str
     * @return
     */
    public static boolean isContainCJKCharacter(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.codePoints().count(); i++) {
            // TDQ-15079: change from charAt to codePointAt to support surrogate pair characters.
            int c = str.codePointAt(i);
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    // Korean characters
                    || ub == Character.UnicodeBlock.HANGUL_SYLLABLES || ub == Character.UnicodeBlock.HANGUL_JAMO
                    || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                    // Japanese 3 characters: hiragana,katakana,Katakana phonetic extensions
                    || ub == Character.UnicodeBlock.HIRAGANA || ub == Character.UnicodeBlock.KATAKANA
                    || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * font for Chinese Japanese and Korean .
     * 
     * @param Style
     * @param size
     * @return
     */
    public static Font getCJKFont(int Style, int size) {
        // Java Font "Monospaced" is enough to display CJK.no Need external font.
        return new Font("Monospaced", Style, size);
    }
}
