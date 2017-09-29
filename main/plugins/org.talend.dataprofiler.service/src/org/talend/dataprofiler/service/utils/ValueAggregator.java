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
package org.talend.dataprofiler.service.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.talend.dataprofiler.service.ITOPChartService;
import org.talend.utils.collections.DoubleValueAggregate;
import org.talend.utils.collections.MultiMapHelper;
import org.talend.utils.collections.MultipleKey;

/**
 * created by yyin on 2014-12-17 Detailled comment
 * 
 */

public class ValueAggregator extends DoubleValueAggregate<MultipleKey> {

    private Map<String, List<String>> seriesKeyToLabel = new HashMap<String, List<String>>();

    /**
     * Method "getLabels". Must not be called before the {@link #addSeriesToXYZDataset(Object, String,ITOPChartService)}
     * method.
     * 
     * @param seriesKey
     * @return the label for each item of the dataset
     */
    public List<String> getLabels(String seriesKey) {
        return seriesKeyToLabel.get(seriesKey);
    }

    public Iterator<Entry<MultipleKey, Double[]>> getKeyValueMap() {
        return keyToVal.entrySet().iterator();
    }

    public Double[] getValueByKey(MultipleKey key) {
        return keyToVal.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.utils.collections.DoubleValueAggregate#addValue(java.lang.Object, java.lang.Double[]) Need to
     * compute average.
     */
    @Override
    public void addValue(MultipleKey key, Double[] values) {
        Double[] doubles = keyToVal.get(key);
        if (doubles == null) {
            doubles = new Double[values.length];
            Arrays.fill(doubles, 0.0);
        }

        // handle average which is an not additive variable
        assert values.length == 3;
        // specific code for CountAvgNullIndicator
        Double avg = values[0];
        Double count = values[1];
        Double nulls = values[2];
        if (count == null || avg == null || nulls == null) {
            nullResults.add(key);
            return;
        }
        // else add values
        Double previousCount = doubles[1];
        Double sumCount = previousCount + count;
        doubles[0] = (doubles[0] * previousCount + avg * count) / sumCount; // sum averages
        doubles[1] = sumCount;
        doubles[2] += nulls;

        keyToVal.put(key, doubles);
    }

    /**
     * Method "addSeriesToXYZDataset" adds a new series of data to the given dataset.
     * 
     * @param dataset a dataset
     * @param keyOfDataset the series key of the data series
     */
    public void addSeriesToXYZDataset(Object dataset, String keyOfDataset, ITOPChartService chartService) {

        final int size = keyToVal.size();
        double[] xDouble = new double[size];
        double[] yDouble = new double[size];
        double[] zDouble = new double[size];

        // get x,y,z for each key
        int i = 0;
        for (MultipleKey key : keyToVal.keySet()) {
            final Double[] doubles = keyToVal.get(key);
            xDouble[i] = doubles[0];
            yDouble[i] = doubles[1];
            zDouble[i] = doubles[2];
            MultiMapHelper.addUniqueObjectToListMap(keyOfDataset, key.toString(), this.seriesKeyToLabel);
            i++;
        }

        // array of data for jfreechart
        double[][] data = new double[][] { xDouble, yDouble, zDouble };
        chartService.addSeriesToDefaultXYZDataset(dataset, keyOfDataset, data);

    }

}
