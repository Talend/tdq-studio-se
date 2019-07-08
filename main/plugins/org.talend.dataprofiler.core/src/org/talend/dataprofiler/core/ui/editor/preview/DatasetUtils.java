// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.editor.preview;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.utils.TOPChartUtils;
import org.talend.dataprofiler.service.utils.ValueAggregator;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.indicators.columnset.ColumnSetMultiValueIndicator;
import org.talend.dq.analysis.explore.MultiColumnSetValueExplorer;
import org.talend.dq.helper.SqlExplorerUtils;
import org.talend.utils.collections.MultiMapHelper;
import org.talend.utils.collections.MultipleKey;
import org.talend.utils.collections.ValueAggregate;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * created by yyin on 2014-12-17 Detailled comment
 *
 */
public class DatasetUtils {

    public static final String SPACE_STRING = " "; //$NON-NLS-1$

    public static Map<Integer, Object> getQueryMap(final Map<String, ValueAggregator> xyzMap,
            final ColumnSetMultiValueIndicator indicator, ModelElement column, Analysis analysis) {
        Map<Integer, Object> queryMap = new HashMap<Integer, Object>();
        EList<ModelElement> nominalList = indicator.getNominalColumns();
        Iterator<String> iterator = xyzMap.keySet().iterator();
        while (iterator.hasNext()) {
            final String seriesKey = iterator.next();
            final ValueAggregator valueAggregator = xyzMap.get(seriesKey);
            Iterator<Entry<MultipleKey, Double[]>> keyValueMap = valueAggregator.getKeyValueMap();
            int index = 0;
            while (keyValueMap.hasNext()) {
                Entry<MultipleKey, Double[]> next = keyValueMap.next();
                final String queryString = MultiColumnSetValueExplorer.getInstance().getQueryStirng(column, analysis,
                        nominalList, seriesKey, next.getKey().toString());
                queryMap.put(Integer.valueOf(index++), createSelectAdapter(queryString, column, analysis));
            }
        }

        return queryMap;
    }

    /**
     * DOC yyin Comment method "getGanttQueryMap".
     *
     * @param createGannttDatasets
     * @param indicator
     * @param column
     * @param analysis
     * @return
     */
    public static Map<Integer, Object> getGanttQueryMap(Map<String, DateValueAggregate> gannttMap,
            ColumnSetMultiValueIndicator indicator, ModelElement column, Analysis analysis) {
        Map<Integer, Object> ganttQueryMap = new HashMap<Integer, Object>();
        Iterator<String> iterator = gannttMap.keySet().iterator();
        EList<ModelElement> nominalList = indicator.getNominalColumns();
        int index = 0;
        while (iterator.hasNext()) {
            final String seriesKey = iterator.next();
            final DateValueAggregate valueAggregator = gannttMap.get(seriesKey);

            Iterator<MultipleKey> keyValueMap = valueAggregator.getKeyValueMap();
            while (keyValueMap.hasNext()) {
                MultipleKey next = keyValueMap.next();
                final String seriesLabel = next.toString();
                final String queryString = MultiColumnSetValueExplorer.getInstance().getQueryStirng(column, analysis,
                        nominalList, seriesKey, seriesLabel);
                ganttQueryMap.put(index++, createSelectAdapter(queryString, column, analysis));
            }
        }
        return ganttQueryMap;
    }

    private static SelectionAdapter createSelectAdapter(final String sql, final ModelElement column, final Analysis analysis) {
        return new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        Connection tdDataProvider = SwitchHelpers.CONNECTION_SWITCH.doSwitch(analysis.getContext()
                                .getConnection());
                        String query = sql;
                        @SuppressWarnings("deprecation")
                        String editorName = ColumnHelper.getColumnSetOwner(column).getName();
                        SqlExplorerUtils.getDefault().runInDQViewer(tdDataProvider, query, editorName);
                    }

                });
            }

        };
    }

    /**
     * DOC scorreia Comment method "createXYZDatasets".
     *
     * @param indicator
     * @param numericColumn a numeric column which is in the list of numeric column of the indicator
     * @return a series of datasets for the given numeric column
     */
    public static Map<String, ValueAggregator> createXYZDatasets(ColumnSetMultiValueIndicator indicator,
            ModelElement numericColumn) {

        final EList<ModelElement> nominalColumns = indicator.getNominalColumns();
        final EList<ModelElement> numericColumns = indicator.getNumericColumns();
        final EList<String> numericFunctions = indicator.getNumericFunctions();

        final int indexOfNumericCol = numericColumns.indexOf(numericColumn);
        assert indexOfNumericCol != -1;

        final int nbNumericFunctions = numericFunctions.size();
        assert nbNumericFunctions == 3 : DefaultMessagesImpl.getString("ChartDatasetFactory.expect"); //$NON-NLS-1$

        final List<Object[]> listRows = indicator.getListRows();

        final int nbNominalColumns = nominalColumns.size();

        return fillDataset(nominalColumns, listRows, nbNominalColumns + nbNumericFunctions * indexOfNumericCol);
    }

    public static Map<String, DateValueAggregate> createGanttDatasets(ColumnSetMultiValueIndicator indicator,
            ModelElement dateColumn) {

        final EList<ModelElement> nominalColumns = indicator.getNominalColumns();
        final EList<ModelElement> dateColumns = indicator.getDateColumns();
        final EList<String> dateFunctions = indicator.getDateFunctions();
        final int indexOfDateCol = dateColumns.indexOf(dateColumn);
        assert indexOfDateCol != -1;

        final int nbDateFunctions = dateFunctions.size();
        assert nbDateFunctions >= 2 : DefaultMessagesImpl.getString("ChartDatasetFactory.expect"); //$NON-NLS-1$

        final List<Object[]> listRows = indicator.getListRows();

        final int nbNominalColumns = nominalColumns.size();

        return fillGanttDataset(nominalColumns, listRows, nbNominalColumns + nbDateFunctions * indexOfDateCol);
    }

    /**
     * Method "fillDataset" fills in the data sets.
     *
     * @param nominalColumns the nominal columns
     * @param listRows the rows (=result set)
     * @param firstNumericColumnIdx the index of the first numeric column
     * @return a map [key -> aggregated values] where identifies a level of aggregation
     */
    private static Map<String, ValueAggregator> fillDataset(final List<ModelElement> nominalColumns,
            final List<Object[]> listRows, final int firstNumericColumnIdx) {
        Map<String, ValueAggregator> valueAggregators = new HashMap<String, ValueAggregator>();

        int xPos = firstNumericColumnIdx;
        int yPos = firstNumericColumnIdx + 1;
        int zPos = firstNumericColumnIdx + 2;

        for (int i = nominalColumns.size(); i > 0; i--) {
            String key = createKey(nominalColumns, i);
            // ADD msjian 2011-5-30 17479: Excel Odbc connection can not run well on the correlation analysis
            if (null != listRows) {
                for (Object[] row : listRows) {

                    final Object xobj = row[xPos];
                    final Double xValue = xobj != null ? Double.valueOf(String.valueOf(xobj)) : null;
                    final Object yobj = row[yPos];
                    final Double yValue = yobj != null ? Double.valueOf(String.valueOf(yobj)) : null;
                    final Object zobj = row[zPos];
                    final Double zValue = zobj != null ? Double.valueOf(String.valueOf(zobj)) : null;

                    ValueAggregator valueAggregator = valueAggregators.get(key);
                    if (valueAggregator == null) {
                        valueAggregator = new ValueAggregator();
                        valueAggregators.put(key, valueAggregator);
                    }
                    MultipleKey multipleKey = new MultipleKey(row, i);
                    valueAggregator.addValue(multipleKey, new Double[] { xValue, yValue, zValue });
                }
            }
        }

        return valueAggregators;

    }

    /**
     *
     */
    private static Map<String, DateValueAggregate> fillGanttDataset(final EList<ModelElement> nominalColumns,
            final List<Object[]> listRows, final int firstDateColumnIdx) {
        Map<String, DateValueAggregate> valueAggregators = new TreeMap<String, DateValueAggregate>();

        int minPos = firstDateColumnIdx;
        int maxPos = firstDateColumnIdx + 1;
        for (int i = nominalColumns.size(); i > 0; i--) {
            String key = createKey(nominalColumns, i);
            // ADD msjian 2011-5-30 17479: Excel Odbc connection can not run well on the correlation analysis
            if (null != listRows) {
                for (Object[] row : listRows) {
                    final Object minObj = row[minPos];
                    final Date minDate = minObj != null ? convert2Date(minObj) : null;
                    final Object maxobj = row[maxPos];
                    final Date maxDate = maxobj != null ? convert2Date(maxobj) : null;

                    DateValueAggregate valueAggregator = valueAggregators.get(key);
                    if (valueAggregator == null) {
                        valueAggregator = new DateValueAggregate();
                        valueAggregators.put(key, valueAggregator);
                    }
                    MultipleKey multipleKey = new MultipleKey(row, i);
                    valueAggregator.addValue(multipleKey, new Date[] { minDate, maxDate });
                }
            }
        }
        return valueAggregators;
    }

    /**
     * DOC msjian Comment method "getDate".
     * @param minObj
     * @return
     */
    private static java.util.Date convert2Date(Object obj) {
        if (obj instanceof String) {
            return java.sql.Date.valueOf(obj.toString());
        }
        // for Timestamp type data
        return (Date) obj;
    }

    /**
     * Method "createKey" creates a key with the concatenation of the values of the first n columns of the given list.
     *
     * @param nominalColumns
     * @param n
     * @return a key
     */
    private static String createKey(List<ModelElement> nominalColumns, int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(nominalColumns.get(i).getName()).append(SPACE_STRING);
        }
        return builder.toString();
    }

    /**
     * DOC scorreia ValueAggregator class global comment. Detailled comment
     */

    /**
     *
     */
    public static class DateValueAggregate extends ValueAggregate<MultipleKey, Date> {

        private Map<String, List<String>> seriesKeyToLabel = new HashMap<String, List<String>>();

        /**
         * Method "getLabels". Must not be called before the {@link #addSeriesToXYZDataset(DefaultXYZDataset, String)}
         * method.
         *
         * @param seriesKey
         * @return the label for each item of the dataset
         */
        public List<String> getLabels(String seriesKey) {
            return seriesKeyToLabel.get(seriesKey);
        }

        public Iterator<MultipleKey> getKeyValueMap() {
            TreeSet<MultipleKey> treeSet = new TreeSet<MultipleKey>(keyToVal.keySet());
            return treeSet.iterator();
        }

        public Date[] getValueByKey(MultipleKey key) {
            return keyToVal.get(key);
        }

        @Override
        public void addValue(MultipleKey key, Date[] values) {
            assert values.length == 2; // expect only min date and max date functions

            Date[] dates = keyToVal.get(key);
            if (dates == null) {
                dates = new Date[values.length];
                // fill in with nulls
                Arrays.fill(dates, null);
            }

            Date min = values[0];
            Date max = values[1];

            if (min == null || max == null) {
                nullResults.add(key);
                return;
            }

            Date prevMinDate = dates[0];
            Date prevMaxDate = dates[1];
            dates[0] = (prevMinDate == null || min.before(prevMinDate)) ? min : prevMinDate;
            dates[1] = (prevMaxDate == null || max.after(prevMaxDate)) ? max : prevMaxDate;

            keyToVal.put(key, dates);
        }

        /**
         * Method "addSeriesToXYZDataset" adds a new series of data to the given dataset.
         *
         * @param dataset a dataset
         * @param keyOfDataset the series key of the data series
         */
        public void addSeriesToGanttDataset(Object ganttDataset, String keyOfDataset) {
            Object series = TOPChartUtils.getInstance().createTaskSeries(keyOfDataset);
            int i = 0;
            for (MultipleKey key : new TreeSet<MultipleKey>(keyToVal.keySet())) {
                final Date[] date = keyToVal.get(key);
                TOPChartUtils.getInstance().addTaskToTaskSeries(series, key.toString(), date);

                MultiMapHelper.addUniqueObjectToListMap(keyOfDataset, key.toString(), this.seriesKeyToLabel);
            }
            TOPChartUtils.getInstance().addSeriesToCollection(ganttDataset, series);
        }

    }

}
