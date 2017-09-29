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
package org.talend.algorithms;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author scorreia
 * 
 * Utility class.
 */
public final class AlgoUtils {

    private AlgoUtils() {
    }

    /**
     * Method "incrementValueCounts". Stores in map the object as key, and counts how many times it appears.
     * 
     * @param <T> the type of the key
     * @param value the value to store
     * @param valueToCount the map [object -> number of objects]
     * @return true if ok
     */
    public static <T> boolean incrementValueCounts(T value, final Map<T, Long> valueToCount) {
        Long freq = valueToCount.get(value);
        if (freq == null) {
            freq = 0L;
        }
        freq++;
        valueToCount.put(value, freq);
        return freq > 0;
    }

    /**
     * DOC scorreia Comment method "getMedian".
     * 
     * @param totalCount
     * @param valueToCount
     * @return
     */
    public static double getMedian(long totalCount, final TreeMap<Object, Long> valueToCount) {
        return getQuantile(totalCount, valueToCount, 1, 2);
    }

    /**
     * Method "getQuantile".
     * 
     * @param totalCount the total number of values
     * @param valueToCount the frequency table of each value
     * @param nthQuantile the nth q-quantile (e.g. 3 for the upper quartile)
     * @param qQuantile the q-quantile (e.g. 4 when we compute quartile)
     * @return the nth quantile
     */
    public static double getQuantile(long totalCount, final Map<Object, Long> valueToCount, int nthQuantile, int qQuantile) {
        double p = (double) nthQuantile / qQuantile;
        Set<Object> keys = valueToCount.keySet();
        Collection<Long> counts = valueToCount.values();

        double kthValue = totalCount * p;

        // TDQ-8185: set the default value is, becasue sometimes there is no data, 0 is not exactly
        double localMedian = Double.NaN;

        long sumCount = 0;
        Iterator<Object> keyIterator = keys.iterator();
        Number searchedKey = null;

        for (Long curValue : counts) {
            // MOD msjian 2011-12-16 TDQ-4164: there is something wrong about this compute
            searchedKey = new BigDecimal(String.valueOf(keyIterator.next()).trim()); // CAST here
            sumCount += curValue;
            // TDQ-4164 ~
            if (sumCount >= kthValue) {
                // compute median
                if (totalCount % qQuantile != 0) { // odd number of value, take the middle
                    localMedian = extracted(searchedKey).doubleValue();
                } else { // even number of values
                    localMedian = extracted(searchedKey).doubleValue();
                    if (sumCount - kthValue < 1) { // in case there are not many identical values
                        Number nextKey = (Number) keyIterator.next(); // CAST here
                        if (Double.isNaN(localMedian)) {
                            localMedian = (extracted(nextKey).doubleValue()) / 2; // with averaging
                        } else {
                            localMedian = (localMedian + extracted(nextKey).doubleValue()) / 2; // with averaging
                        }
                    }
                }
                break; // we got it.
            }
            // else loop

        }
        return localMedian;
    }

    private static Number extracted(Number searchedKey) {
        if (searchedKey != null) {
            return searchedKey;
        }
        return null;
    }

}
