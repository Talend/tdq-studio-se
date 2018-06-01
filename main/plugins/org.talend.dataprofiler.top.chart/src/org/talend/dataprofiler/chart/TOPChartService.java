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

import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.TextAnchor;
import org.talend.dataprofiler.chart.i18n.Messages;
import org.talend.dataprofiler.chart.preview.CustomHideSeriesGanttRender;
import org.talend.dataprofiler.chart.preview.RowColumPair;
import org.talend.dataprofiler.chart.util.ChartDatasetUtils;
import org.talend.dataprofiler.chart.util.ChartUtils;
import org.talend.dataprofiler.chart.util.EncapsulationCumstomerDataset;
import org.talend.dataprofiler.chart.util.HideSeriesChartDialog;
import org.talend.dataprofiler.chart.util.PluginConstant;
import org.talend.dataprofiler.chart.util.SlideChartComposite;
import org.talend.dataprofiler.chart.util.TalendChartComposite;
import org.talend.dataprofiler.chart.util.TopChartFactory;
import org.talend.dataprofiler.service.ITOPChartService;
import org.talend.dataprofiler.service.utils.ValueAggregator;

/**
 * created by yyin on 2014-11-28 Detailled comment
 * 
 */
public class TOPChartService implements ITOPChartService {

    public static final int CHART_STANDARD_WIDHT = 600;

    public static final int CHART_STANDARD_HEIGHT = 275;

    private static final Logger log = Logger.getLogger(TOPChartService.class);

    @Override
    public Object getDatasetFromChart(Object chart, int datasetIndex) {
        if (datasetIndex > -1) {
            return ((JFreeChart) chart).getCategoryPlot().getDataset(datasetIndex);
        }
        return ((JFreeChart) chart).getCategoryPlot().getDataset();
    }

    @Override
    public Object createTalendChartComposite(Object parentComponent, int style, Object chart, boolean useBuffer) {
        ChartComposite cc;
        if (style == SWT.VERTICAL) {
            cc = new SlideChartComposite((Composite) parentComponent, style, (JFreeChart) chart, useBuffer);
        } else {
            cc = new TalendChartComposite((Composite) parentComponent, style, (JFreeChart) chart, useBuffer);
        }
        GridData gd = new GridData();
        gd.widthHint = CHART_STANDARD_WIDHT;
        gd.heightHint = CHART_STANDARD_HEIGHT;
        cc.setLayoutData(gd);
        return cc;
    }

    @Override
    public Object createBarChart(String title, Object dataset, boolean showLegend) {
        return TopChartFactory.createBarChart(title, (CategoryDataset) dataset, false);
    }

    @Override
    public Object createBarChart(Object dataset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object createBarChart(String title, Object dataset) {
        return TopChartFactory.createBarChart(title, (CategoryDataset) dataset);
    }

    @Override
    public Object createBarChartByKCD(String title, Object dataset, Object customerDataset) {
        return TopChartFactory.createBarChartByKCD(title, (CategoryDataset) dataset, customerDataset);
    }

    @Override
    public Object createBenfordChart(String axisXLabel, String categoryAxisLabel, Object dataset, List<String> dotChartLabels,
            double[] formalValues, String title) {
        return createBenfordChartByKCD(axisXLabel, categoryAxisLabel, dataset, null, dotChartLabels, formalValues, title);
    }

    @Override
    public Object createBenfordChartByKCD(String axisXLabel, String categoryAxisLabel, Object dataset, Object customerDataset,
            List<String> dotChartLabels, double[] formalValues, String title) {
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        Object barChart = ChartFactory.createBarChart(null, axisXLabel, categoryAxisLabel, (CategoryDataset) dataset,
                PlotOrientation.VERTICAL, false, true, false);
        Object lineChart = ChartDecorator.decorateBenfordLawChartByKCD((CategoryDataset) dataset, customerDataset,
                (JFreeChart) barChart, title, categoryAxisLabel, dotChartLabels, formalValues);
        return lineChart;

    }

    @Override
    public void decorateChart(Object chart, boolean withPlot) {
        if (withPlot) {
            ChartDecorator.decorate((JFreeChart) chart, PlotOrientation.HORIZONTAL);
        } else {
            ChartDecorator.decorate((JFreeChart) chart, null);
        }

    }

    @Override
    public void decorateColumnDependency(Object chart) {
        ChartDecorator.decorateColumnDependency((JFreeChart) chart);
    }

    @Override
    public void decoratePatternMatching(Object chart) {
        ChartDecorator.decoratePatternMatching((JFreeChart) chart);
    }

    @Override
    public Object createChartComposite(Object composite, int style, Object chart, boolean useBuffer) {
        return createChartCompositeWithSpecialSize(composite, style, chart, useBuffer, CHART_STANDARD_HEIGHT,
                CHART_STANDARD_WIDHT);
    }

    @Override
    public Object createChartCompositeWithoutGrid(Object composite, int style, Object chart, boolean useBuffer) {
        return new ChartComposite((Composite) composite, style, (JFreeChart) chart, useBuffer);
    }

    @Override
    public Object createChartCompositeWithSpecialSize(Object composite, int style, Object chart, boolean useBuffer, int height,
            int width) {
        ChartComposite cc = new ChartComposite((Composite) composite, style, (JFreeChart) chart, useBuffer);

        GridData gd = new GridData();
        gd.widthHint = width;
        gd.heightHint = height;
        cc.setLayoutData(gd);
        return cc;
    }

    @Override
    public Object createChartCompositeWithFull(Object composite, Object chart) {
        ChartComposite chartComp = new ChartComposite((Composite) composite, SWT.NONE, (JFreeChart) chart, true);
        chartComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        return chartComp;
    }

    @Override
    public void setOrientation(Object chart, boolean isHorizontal) {
        if (isHorizontal) {
            ((JFreeChart) chart).getCategoryPlot().setOrientation(PlotOrientation.HORIZONTAL);
        } else {
            ((JFreeChart) chart).getCategoryPlot().setOrientation(PlotOrientation.VERTICAL);
        }
    }

    @Override
    public void setDisplayDecimalFormatOfChart(Object chart) {
        ChartDecorator.setDisplayDecimalFormat((JFreeChart) chart);
    }

    @Override
    public void addMouseListenerForChart(Object chartComposite, final Map<String, Object> menuMap, final boolean useRowFirst) {
        final ChartComposite chartComp = (ChartComposite) chartComposite;
        final ChartMouseListener listener = new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                boolean flag = event.getTrigger().getButton() != MouseEvent.BUTTON3;
                chartComp.setDomainZoomable(flag);
                chartComp.setRangeZoomable(flag);
                if (flag) {
                    return;
                }

                ChartEntity chartEntity = event.getEntity();
                if (chartEntity != null && chartEntity instanceof CategoryItemEntity) {
                    CategoryItemEntity cateEntity = (CategoryItemEntity) chartEntity;

                    Menu menu = getCurrentMenu(cateEntity);
                    if (menu != null) {
                        chartComp.setMenu(menu);
                        menu.setVisible(true);
                    }
                }
            }

            private Menu getCurrentMenu(CategoryItemEntity cateEntity) {
                if (useRowFirst) {
                    return findCurrentMenu(cateEntity.getRowKey(), cateEntity.getColumnKey());
                } else {
                    return findCurrentMenu(cateEntity.getColumnKey(), cateEntity.getRowKey());
                }
            }

            /**
             * DOC yyin Comment method "findCurrentMenu".
             * 
             * @param firstKey
             * @param secondKey
             * @return
             */
            private Menu findCurrentMenu(final Object firstKey, Object secondKey) {
                Object menu = menuMap.get(firstKey);
                if (menu != null) {
                    return (Menu) menu;
                }
                menu = menuMap.get(secondKey);
                if (menu != null) {
                    return (Menu) menu;
                }
                return null;
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                // no action here

            }

        };
        chartComp.addChartMouseListener(listener);
        chartComp.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                chartComp.removeChartMouseListener(listener);
                chartComp.dispose();
            }

        });
    }

    @Override
    public void addMouseListenerForConceptChart(Object chartComposite, final Map<String, Object> actionMap) {
        final ChartComposite chartComp = (ChartComposite) chartComposite;
        final ChartMouseListener listener = new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                boolean flag = event.getTrigger().getButton() == MouseEvent.BUTTON1;
                chartComp.setDomainZoomable(flag);
                chartComp.setRangeZoomable(flag);
                if (!flag) {
                    return;
                }

                ChartEntity chartEntity = event.getEntity();
                if (chartEntity != null && chartEntity instanceof CategoryItemEntity) {
                    CategoryItemEntity cateEntity = (CategoryItemEntity) chartEntity;
                    // highlight current selected bar
                    Plot plot = event.getChart().getPlot();
                    if (plot != null) {
                        // ((CategoryPlot) plot).getRenderer().setSeriesPaint(cateEntity.getSeries(), Green);
                        CustomConceptRenderer render = new CustomConceptRenderer(cateEntity.getCategoryIndex());
                        render.setShadowVisible(false);
                        render.setDrawBarOutline(false);
                        ((CategoryPlot) plot).setRenderer(render);
                        // ChartDecorator.decorateConceptChart(event.getChart(), PlotOrientation.HORIZONTAL);

                    }
                    Object action = getCurrentAction(cateEntity);
                    Class<? extends Object> actionClass = action.getClass();
                    try {
                        Method actionRunMethod = actionClass.getDeclaredMethod("run"); //$NON-NLS-1$
                        actionRunMethod.invoke(action);

                    } catch (NoSuchMethodException e) {
                        log.error(e, e);
                    } catch (SecurityException e) {
                        log.error(e, e);
                    } catch (IllegalAccessException e) {
                        log.error(e, e);
                    } catch (IllegalArgumentException e) {
                        log.error(e, e);
                    } catch (InvocationTargetException e) {
                        log.error(e, e);
                    }

                }
            }

            private Object getCurrentAction(CategoryItemEntity cateEntity) {
                return findCurrentAction(cateEntity.getColumnKey(), cateEntity.getRowKey());
            }

            /**
             * Find current action
             * 
             * @param firstKey
             * @param secondKey
             * @return
             */
            private Object findCurrentAction(final Object firstKey, Object secondKey) {
                Object action = actionMap.get(firstKey);
                if (action != null) {
                    return action;
                }
                action = actionMap.get(secondKey);
                if (action != null) {
                    return action;
                }
                return null;
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                // no action here

            }

        };
        chartComp.addChartMouseListener(listener);
        chartComp.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                chartComp.removeChartMouseListener(listener);
                chartComp.dispose();
            }

        });
    }

    @Override
    public Object createPieChart(String title, Object dataset, boolean showLegend, boolean toolTips, boolean urls) {
        return TopChartFactory.createPieChart(title, (PieDataset) dataset, showLegend, toolTips, urls);
    }

    @Override
    public Object createBoxAndWhiskerChart(String title, Object dataset) {
        return TopChartFactory.createBoxAndWhiskerChart(title, (BoxAndWhiskerCategoryDataset) dataset);
    }

    @Override
    public Object createStackedBarChart(String title, Object dataset, boolean showLegend) {
        JFreeChart stackedBarChart = TopChartFactory.createStackedBarChart(title, (CategoryDataset) dataset, showLegend);
        ChartDecorator.decorateStackedBarChart(stackedBarChart, null);
        return stackedBarChart;
    }

    @Override
    public Object createStackedBarChart(String title, Object dataset, boolean isHorizatal, boolean showLegend) {
        JFreeChart chart = null;
        if (isHorizatal) {
            chart = TopChartFactory.createStackedBarChart(title, (CategoryDataset) dataset, PlotOrientation.HORIZONTAL,
                    showLegend);
        } else {
            chart = TopChartFactory.createStackedBarChart(title, (CategoryDataset) dataset, PlotOrientation.VERTICAL, showLegend);
        }
        ChartDecorator.decorateStackedBarChart(chart, null);
        return chart;
    }

    @Override
    public void addListenerToChartComp(Object chartComposite, final String referenceLink, final String menuText) {
        final ChartComposite chartComp = (ChartComposite) chartComposite;
        final ChartMouseListener listener = new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent event) {
                if (event.getTrigger().getButton() == 1 && referenceLink != null) {
                    Menu menu = new Menu(chartComp.getShell(), SWT.POP_UP);
                    chartComp.setMenu(menu);

                    MenuItem item = new MenuItem(menu, SWT.PUSH);
                    item.setText(menuText);
                    item.addSelectionListener(new SelectionAdapter() {

                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            ChartUtils.openReferenceLink(referenceLink);
                        }
                    });

                    menu.setVisible(true);
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                // no need to implement
            }

        };
        chartComp.addChartMouseListener(listener);
        chartComp.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                chartComp.removeChartMouseListener(listener);
                chartComp.dispose();
            }
        });
    }

    @Override
    public Object createMatchRuleBarChart(String categoryAxisLabel, String valueAxisLabel, Object dataset) {
        return TopChartFactory.createMatchRuleBarChart(categoryAxisLabel, valueAxisLabel, (CategoryDataset) dataset);
    }

    @Override
    public void refrechChart(Object chartComp, Object chart) {
        ((ChartComposite) chartComp).setChart((JFreeChart) chart);
        ((ChartComposite) chartComp).forceRedraw();
    }

    @Override
    public Object createDatasetForMatchRule(Map<Object, Long> groupSize2GroupFrequency, List<String> groups, int times,
            String items) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        if (groups == null) {
            return defaultcategorydataset;
        }
        for (String count : groups) {
            if (Integer.parseInt(count) > times - 1) {
                defaultcategorydataset.addValue(groupSize2GroupFrequency.get(count), items, count);
            }
        }
        return defaultcategorydataset;
    }

    @Override
    public Object createBlockingBarChart(String title, Object dataset) {
        Object chart = TopChartFactory.createBlockingBarChart(title, (HistogramDataset) dataset);
        return chart;
    }

    @Override
    public Object createHistogramDataset(double[] valueArray, double maxValue, int bins) {
        HistogramDataset defaultcategorydataset = new HistogramDataset();
        if (valueArray == null) {
            return defaultcategorydataset;
        }
        defaultcategorydataset.addSeries("Key distribution", valueArray, bins, 0, maxValue); //$NON-NLS-1$
        return defaultcategorydataset;
    }

    @Override
    public Object createDatasetForDuplicateRecord(Map<String, Long> dupStats) {
        if (dupStats != null) {
            DefaultPieDataset dataset = new DefaultPieDataset();
            Iterator<String> iterator = dupStats.keySet().iterator();
            while (iterator.hasNext()) {
                String label = iterator.next();
                dataset.setValue(label, dupStats.get(label));
            }
            return dataset;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createDuplicateRecordPieChart(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public Object createDuplicateRecordPieChart(String title, Object dataset) {
        return TopChartFactory.createDuplicateRecordPieChart(title, (PieDataset) dataset, true, true, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createDefaultCategoryDataset()
     */
    @Override
    public Object createDefaultCategoryDataset() {
        return new DefaultCategoryDataset();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createDefaultCategoryDataset(java.util.List)
     */
    @Override
    public Object createDefaultCategoryDataset(List<String[]> inputData) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String[] dataArray : inputData) {
            dataset.addValue(Double.valueOf(dataArray[0]), dataArray[1], dataArray[2]);
        }
        // dataset.addValue(23192, "S1", "GNU General Public Licence");
        // dataset.addValue(3157, "S1", "GNU Lesser General Public Licence");
        // dataset.addValue(1506, "S1", "BSD Licence (Original)");
        // dataset.addValue(1283, "S1", "BSD Licence (Revised)");
        // dataset.addValue(738, "S1", "MIT/X Consortium Licence");
        // dataset.addValue(630, "S1", "Artistic Licence");
        // dataset.addValue(585, "S1", "Public Domain");
        // dataset.addValue(349, "S1", "Apache Licence 2.0");
        // dataset.addValue(317, "S1", "Apache Licence");
        // dataset.addValue(309, "S1", "Mozilla Public Licence");
        // dataset.addValue(918, "S1", "Other");

        return dataset;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createConceptsChart(java.lang.String,java.lang.Object)
     */
    @Override
    public Object createConceptsChart(String title, Object dataset) {
        // TODO some parameter should be get out
        // create the chart...
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        JFreeChart chart = ChartFactory.createBarChart(title, // chart title
                PluginConstant.EMPTY_STRING, // domain axis label
                PluginConstant.EMPTY_STRING, // range axis label
                (CategoryDataset) dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                false, // include legend
                true, // tooltips?
                false // URLs?
                );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        // BarRenderer renderer = (BarRenderer) plot.getRenderer();
        // default highlight the first bar
        CustomConceptRenderer renderer = new CustomConceptRenderer(0);
        plot.setRenderer(renderer);
        // disable bar outlines...
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        ChartDecorator.decorateConceptChart(chart, PlotOrientation.HORIZONTAL);
        return chart;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#addValueToCategoryDataset(double, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void addValueToCategoryDataset(Object dataset, double value, String labelX, String labelY) {
        ((DefaultCategoryDataset) dataset).addValue(value, labelX, labelY);

    }

    @Override
    public int getRowCount(Object dataset) {
        return ((DefaultCategoryDataset) dataset).getRowCount();
    }

    @Override
    public int getColumnCount(Object dataset) {
        return ((DefaultCategoryDataset) dataset).getColumnCount();
    }

    @Override
    public Number getValue(Object dataset, int row, int column) {
        return ((DefaultCategoryDataset) dataset).getValue(row, column);
    }

    @Override
    public Comparable getRowKey(Object dataset, int row) {
        return ((DefaultCategoryDataset) dataset).getRowKey(row);
    }

    @Override
    public int getRowIndex(Object dataset, Comparable key) {
        return ((DefaultCategoryDataset) dataset).getRowIndex(key);
    }

    @Override
    public List getRowKeys(Object dataset) {
        return ((DefaultCategoryDataset) dataset).getRowKeys();
    }

    @Override
    public Comparable getColumnKey(Object dataset, int column) {
        return ((DefaultCategoryDataset) dataset).getColumnKey(column);
    }

    @Override
    public int getColumnIndex(Object dataset, Comparable key) {
        return ((DefaultCategoryDataset) dataset).getColumnIndex(key);
    }

    @Override
    public List getColumnKeys(Object dataset) {
        return ((DefaultCategoryDataset) dataset).getColumnKeys();
    }

    @Override
    public Number getValue(Object dataset, Comparable rowKey, Comparable columnKey) {
        return ((DefaultCategoryDataset) dataset).getValue(rowKey, columnKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createPieDataset(java.util.Map)
     */
    @Override
    public Object createPieDataset(Map<String, Double> valueMap) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        if (valueMap != null) {
            Iterator<String> iterator = valueMap.keySet().iterator();
            while (iterator.hasNext()) {
                String label = iterator.next();
                dataset.setValue(label, valueMap.get(label));
            }
        }
        return dataset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createDefaultBoxAndWhiskerCategoryDataset(java.lang.Double,
     * java.lang.Double, java.lang.Double, java.lang.Double, java.lang.Double, java.lang.Double)
     */
    @Override
    public Object createDefaultBoxAndWhiskerCategoryDataset(Double mean, Double median, Double q1, Double q3,
            Double minRegularValue, Double maxRegularValue) {
        DefaultBoxAndWhiskerCategoryDataset dataset = ChartDatasetUtils.createBoxAndWhiskerDataset();
        BoxAndWhiskerItem item = ChartDatasetUtils.createBoxAndWhiskerItem(mean, median, q1, q3, minRegularValue,
                maxRegularValue, null);
        dataset.add(item, "0", ""); //$NON-NLS-1$ //$NON-NLS-2$

        @SuppressWarnings("rawtypes")
        List zerolist = new ArrayList();
        dataset.add(zerolist, "1", ""); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.add(zerolist, "2", ""); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.add(zerolist, "3", ""); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.add(zerolist, "4", ""); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.add(zerolist, "5", ""); //$NON-NLS-1$ //$NON-NLS-2$
        dataset.add(zerolist, "6", ""); //$NON-NLS-1$ //$NON-NLS-2$

        return dataset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createXYDataset(java.util.Map)
     */
    @Override
    public Object createXYDataset(Map<Integer, Double> valueMap) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Rules"); //$NON-NLS-1$
        if (valueMap != null) {
            Iterator<Integer> iterator = valueMap.keySet().iterator();
            while (iterator.hasNext()) {
                Integer x = iterator.next();
                series.add(x, valueMap.get(x));
                dataset.addSeries(series);
            }
        }
        return dataset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#setValue(java.lang.Object, java.lang.Comparable,
     * java.lang.Comparable)
     */
    @Override
    public void setValue(Object dataset, Number value, Comparable rowKey, Comparable columnKey) {
        ((DefaultCategoryDataset) dataset).setValue(value, rowKey, columnKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#clearDataset(java.lang.Object)
     */
    @Override
    public void clearDataset(Object dataset) {
        // the dataset must be DefaultCategoryDataset
        ((DefaultCategoryDataset) dataset).clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#clearDefaultBoxAndWhiskerCategoryDataset(java.lang.Object)
     */
    @Override
    public void clearDefaultBoxAndWhiskerCategoryDataset(Object dataset) {
        if (dataset instanceof DefaultBoxAndWhiskerCategoryDataset) {
            ((DefaultBoxAndWhiskerCategoryDataset) dataset).clear();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createTaskSeriesCollection()
     */
    @Override
    public Object createTaskSeriesCollection() {
        return new TaskSeriesCollection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createTaskSeries()
     */
    @Override
    public Object createTaskSeries(String keyOfDataset) {
        return new TaskSeries(keyOfDataset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#addTaskToTaskSeries(java.lang.String, java.util.Date[])
     */
    @Override
    public void addTaskToTaskSeries(Object taskSeries, String key, Date[] date) {
        ((TaskSeries) taskSeries).add(new Task(key, new org.jfree.data.time.SimpleTimePeriod(date[0], date[1])));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#addSeriesToCollection(java.lang.Object, java.lang.Object)
     */
    @Override
    public void addSeriesToCollection(Object taskSeriesCollection, Object taskSeries) {
        ((TaskSeriesCollection) taskSeriesCollection).add(((TaskSeries) taskSeries));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createGanttChart(java.lang.String, java.lang.Object)
     */
    @Override
    public Object createGanttChart(String chartAxies, Object ganttDataset) {
        return TopChartFactory.createGanttChart(chartAxies, ganttDataset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#addSeriesToDefaultXYZDataset(java.lang.Object, double[][])
     */
    @Override
    public void addSeriesToDefaultXYZDataset(Object dataset, String keyOfDataset, double[][] data) {
        ((DefaultXYZDataset) dataset).addSeries(keyOfDataset, data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createBubbleChart(java.lang.String, java.lang.Object)
     */
    @Override
    public Object createBubbleChart(String chartName, Object dataset, Map<String, ValueAggregator> xyzDatasets) {
        return TopChartFactory.createBubbleChart(chartName, dataset, xyzDatasets);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createDefaultXYZDataset()
     */
    @Override
    public Object createDefaultXYZDataset() {
        return new DefaultXYZDataset();
    }

    // used by HideSeriesChartComposite
    @Override
    public void createAnnotOnGantt(Object chart, List<Object[]> rowList, int multiDateColumn, int nominal) {
        Map<String, RowColumPair> hightlightSeriesMap = new HashMap<String, RowColumPair>();
        CategoryPlot xyplot = (CategoryPlot) ((JFreeChart) chart).getPlot();
        CategoryTextAnnotation an;
        for (int seriesCount = 0; seriesCount < ((TaskSeriesCollection) xyplot.getDataset()).getSeriesCount(); seriesCount++) {
            int indexOfRow = 0;
            int columnCount = 0;
            for (int itemCount = 0; itemCount < ((TaskSeriesCollection) xyplot.getDataset()).getSeries(seriesCount)
                    .getItemCount(); itemCount++, columnCount++) {
                Task task = ((TaskSeriesCollection) xyplot.getDataset()).getSeries(seriesCount).get(itemCount);
                String taskDescription = task.getDescription();
                String[] taskArray = taskDescription.split("\\|"); //$NON-NLS-1$
                boolean isSameTime = task.getDuration().getStart().getTime() == task.getDuration().getEnd().getTime();
                if (!isSameTime && (rowList.get(indexOfRow))[multiDateColumn - 3] != null
                        && (rowList.get(indexOfRow))[multiDateColumn - 2] != null
                        && !((rowList.get(indexOfRow))[multiDateColumn]).equals(new BigDecimal(0L))) {
                    RowColumPair pair = new RowColumPair();
                    pair.setRow(seriesCount);
                    pair.setColumn(columnCount);

                    hightlightSeriesMap.put(String.valueOf(seriesCount) + String.valueOf(columnCount), pair);

                    an = new CategoryTextAnnotation("#nulls = " + (rowList.get(indexOfRow))[multiDateColumn], //$NON-NLS-1$
                            taskDescription, task.getDuration().getStart().getTime());
                    an.setTextAnchor(TextAnchor.CENTER_LEFT);
                    an.setCategoryAnchor(CategoryAnchor.MIDDLE);
                    xyplot.addAnnotation(an);
                }
                if (taskArray.length == nominal) {
                    indexOfRow++;

                    if (rowList.size() != indexOfRow
                            && ((rowList.get(indexOfRow))[multiDateColumn - 3] == null || (rowList.get(indexOfRow))[multiDateColumn - 2] == null)) {
                        indexOfRow++;
                    }
                }
            }
        }
        CustomHideSeriesGanttRender renderer = new CustomHideSeriesGanttRender(hightlightSeriesMap);
        xyplot.setRenderer(renderer);
        renderer.setBaseToolTipGenerator(new CategoryToolTipGenerator() {

            @Override
            public String generateToolTip(CategoryDataset dataset, int row, int column) {
                TaskSeriesCollection taskSeriesColl = (TaskSeriesCollection) dataset;
                List<Task> taskList = new ArrayList<Task>();
                for (int i = 0; i < taskSeriesColl.getSeriesCount(); i++) {
                    for (int j = 0; j < taskSeriesColl.getSeries(i).getItemCount(); j++) {
                        taskList.add(taskSeriesColl.getSeries(i).get(j));
                    }
                }
                Task task = taskList.get(column);
                // Task task = taskSeriesColl.getSeries(row).get(column);
                String taskDescription = task.getDescription();

                Date startDate = task.getDuration().getStart();
                Date endDate = task.getDuration().getEnd();
                return taskDescription + ",     " + startDate + "---->" + endDate; //$NON-NLS-1$ //$NON-NLS-2$
                // return "this is a tooltip";
            }
        });
        xyplot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0f);

    }

    @Override
    public void showChartInFillScreen(Object chart, boolean isCountAvgNull, boolean isMinMaxDate) {
        try {
            new HideSeriesChartDialog(null, (JFreeChart) ((JFreeChart) chart).clone(), isCountAvgNull, isMinMaxDate).open();
        } catch (CloneNotSupportedException e) {
            log.error(e, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#addSpecifiedListenersForCorrelationChart(boolean, boolean,
     * java.lang.Object, java.util.List)
     */
    @Override
    public void addSpecifiedListenersForCorrelationChart(Object chartcomp, final Object chart, final boolean isAvg,
            final boolean isDate, final Map<Integer, Object> keyWithAdapter) {
        final ChartComposite chartComp = (ChartComposite) chartcomp;
        chartComp.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(ChartMouseEvent event) {

                chartComp.setRangeZoomable(event.getTrigger().getButton() == 1);
                chartComp.setDomainZoomable(event.getTrigger().getButton() == 1);

                if (event.getTrigger().getButton() != 3) {
                    return;
                }
                final Menu menu = new Menu(chartComp.getShell(), SWT.POP_UP);
                MenuItem itemShowInFullScreen = new MenuItem(menu, SWT.PUSH);
                itemShowInFullScreen.setText(Messages.getString("HideSeriesChartComposite.ShowInFullScreen")); //$NON-NLS-1$
                itemShowInFullScreen.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        Display.getDefault().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                showChartInFillScreen(chart, isAvg, isDate);
                            }
                        });
                    }
                });

                chartComp.setMenu(menu);
                ChartEntity chartEntity = event.getEntity();
                if (chartEntity != null) {
                    if (isAvg) {
                        addMenuOnBubbleChart(menu, chartEntity);
                    } else if (isDate) {
                        addMenuOnGantChart(menu, chartEntity);
                    }
                }
                menu.setVisible(true);
            }

            private void addMenuOnBubbleChart(Menu menu, ChartEntity chartEntity) {
                if (chartEntity instanceof XYItemEntity) {
                    XYItemEntity xyItemEntity = (XYItemEntity) chartEntity;
                    createMenuItem(menu, xyItemEntity.getItem());
                }
            }

            private void addMenuOnGantChart(Menu menu, ChartEntity chartEntity) {

                if (chartEntity instanceof CategoryItemEntity) {
                    CategoryItemEntity itemEntity = (CategoryItemEntity) chartEntity;
                    createMenuItem(menu, itemEntity.getCategoryIndex());
                }
            }

            private void createMenuItem(Menu menu, final int seriesK) {
                final SelectionAdapter selectionAdapter = (SelectionAdapter) keyWithAdapter.get(seriesK);
                MenuItem item;
                item = new MenuItem(menu, SWT.PUSH);
                item.setText(Messages.getString("HideSeriesChartComposite.ViewRow")); //$NON-NLS-1$
                item.addSelectionListener(selectionAdapter);
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent event) {
                // no need to implement
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#getSeriesCount(java.lang.Object)
     */
    @Override
    public int getSeriesCount(Object chart) {
        XYDataset dataset = ((JFreeChart) chart).getXYPlot().getDataset();
        return dataset.getSeriesCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#getSeriesRowCount(java.lang.Object)
     */
    @Override
    public int getSeriesRowCount(Object chart) {
        CategoryPlot plot = (CategoryPlot) ((JFreeChart) chart).getPlot();
        CategoryDataset dataset = plot.getDataset();
        return dataset.getRowCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createSelectionAdapterForButton()
     */
    @Override
    public Object createSelectionAdapterForButton(final Object chart, final boolean isCountAvg, final boolean isMinMax) {
        return new SelectionAdapter() {

            private static final String SERIES_KEY_ID = "SERIES_KEY"; //$NON-NLS-1$

            @Override
            public void widgetSelected(SelectionEvent e) {

                Button checkBtn = (Button) e.getSource();
                int seriesid = (Integer) checkBtn.getData(SERIES_KEY_ID);

                if (isCountAvg) {
                    XYPlot plot = ((JFreeChart) chart).getXYPlot();
                    XYItemRenderer xyRenderer = plot.getRenderer();
                    xyRenderer.setSeriesVisible(seriesid, checkBtn.getSelection());
                }

                if (isMinMax) {
                    CategoryPlot plot = (CategoryPlot) ((JFreeChart) chart).getPlot();
                    CategoryItemRenderer render = plot.getRenderer();
                    render.setSeriesVisible(seriesid, checkBtn.getSelection());
                }
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#getSeriesKeyOfBubbleChart(java.lang.Object, int)
     */
    @Override
    public String getSeriesKeyOfBubbleChart(Object chart, int index) {
        XYDataset dataset = ((JFreeChart) chart).getXYPlot().getDataset();

        return dataset.getSeriesKey(index).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#getSeriestKeyOfGanttChart(java.lang.Object, int)
     */
    @Override
    public String getSeriestKeyOfGanttChart(Object chart, int index) {
        CategoryPlot plot = (CategoryPlot) ((JFreeChart) chart).getPlot();
        CategoryDataset dataset = plot.getDataset();
        return dataset.getRowKey(index).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#createChartCompositeForCorrelationAna(java.lang.Object,
     * java.lang.Object, int)
     */
    @Override
    public Object createChartCompositeForCorrelationAna(Object parent, Object chart, int height) {
        ChartComposite chartComposite = new ChartComposite((Composite) parent, SWT.NONE);
        chartComposite.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_HAND));
        chartComposite.setToolTipText("sdfsdf"); //$NON-NLS-1$

        // the analysis.
        GridData gd = new GridData();
        gd.heightHint = height;
        gd.widthHint = 460;
        chartComposite.setLayoutData(gd);

        if (chart != null) {
            chartComposite.setChart((JFreeChart) chart);
        }

        // ~14173
        return chartComposite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#getCustomerDataset(java.lang.Object)
     */
    @Override
    public Object getCustomerDataset(Object dataset) {
        if (dataset == null) {
            return null;
        }
        if (dataset instanceof EncapsulationCumstomerDataset) {
            return ((EncapsulationCumstomerDataset) dataset).getCusmomerDataset();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.ITOPChartService#getChartFromChartComposite(java.lang.Object)
     */
    @Override
    public Object getChartFromChartComposite(Object chartComposite) {
        if (chartComposite == null) {
            return null;
        }
        if (chartComposite instanceof ChartComposite) {
            return ((ChartComposite) chartComposite).getChart();
        }
        return null;
    }

}
