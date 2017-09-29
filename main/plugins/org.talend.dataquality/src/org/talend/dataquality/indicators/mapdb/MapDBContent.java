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
package org.talend.dataquality.indicators.mapdb;

import org.apache.log4j.Logger;

import com.sun.management.OperatingSystemMXBean;

/**
 * created by talend on Oct 14, 2014 Detailled comment
 * 
 */
public class MapDBContent {

    private static final String CACHE_SIZE_PROPERTY_KEY = "talend.mapdb.cacheSize";//$NON-NLS-1$

    private static final String IS_MMAP_FILE_PROPERTY_KEY = "talend.mapdb.mmapFileEnable";//$NON-NLS-1$

    private static final String CLOSE_DBMAP_TIME_DELAY_PROPERTY_KEY = "talend.mapdb.closeDelayTime";//$NON-NLS-1$

    // The size which how many free memory can be used 1 mean that 1GB
    private static final String FREE_MEMORY_SIZE_PROPERTY_KEY = "talend.mapdb.freeMemoryLimitSize";//$NON-NLS-1$

    private static final String IS_VALUES_OUTSIDE_NODES_PROPERTY_KEY = "talend.mapdb.valuesOutsideNodesEnable";//$NON-NLS-1$

    private static Logger log = Logger.getLogger(MapDBContent.class);

    /**
     * the cache size used to create db.
     */
    private static final int DEFAULE_CACHE_SIZE = 8192;

    /**
     * close db after 5 minute (5 * 60 * 1000).
     */
    private static final int CLOSE_TIME_DELAY = 300000;

    /**
     * Waitting How long to close MapDB connect after execute analysis
     * 
     * @return The value which be set by vm parameter ,defaule it is 5 minute if no parameter be set
     */
    public static long getDelayTime() {
        Long delayTime = Long.getLong(MapDBContent.CLOSE_DBMAP_TIME_DELAY_PROPERTY_KEY);
        // if parameter has been defined and the value is number
        if (delayTime != null) {
            return delayTime;
        }
        // esle
        return CLOSE_TIME_DELAY;
    }

    /**
     * Judge the MapDB should ued mmapFileEnable or not Default it is true
     * 
     * @return
     */
    public static boolean isMmapFileEnable() {
        String property = System.getProperty(MapDBContent.IS_MMAP_FILE_PROPERTY_KEY);
        // if parameter has been defined
        if (property != null) {
            return Boolean.valueOf(property);
        }
        // else
        return true;
    }

    /**
     * Judge the MapDB should ued valuesOutsideNodesEnable or not Default it is false
     * 
     * @return
     */
    public static boolean isValuesOutsideNodesEnable() {
        String property = System.getProperty(MapDBContent.IS_VALUES_OUTSIDE_NODES_PROPERTY_KEY);
        // if parameter has been defined
        if (property != null) {
            return Boolean.valueOf(property);
        }
        // else
        return true;
    }

    /**
     * 
     * How many cache size can be used by MapDB
     * 
     * @return If the value has been set by vm parameter(cacheSize) the return it else return defaule value(1024)
     */
    public static Integer getCacheSize() {
        Integer cacheSize = Integer.getInteger(MapDBContent.CACHE_SIZE_PROPERTY_KEY);
        // if parameter has been defined and the value is number
        if (cacheSize != null) {
            return cacheSize;
        }
        // esle
        return MapDBContent.DEFAULE_CACHE_SIZE;
    }

    /**
     * 
     * Get the size of free memory which can be used by mapDB
     * 
     * @return Actual value if the parameter(freeMemoryLimitSize) is an double value esle return null.
     */
    public static Double getFreeMemorySize() {
        String propertyValue = System.getProperty(MapDBContent.FREE_MEMORY_SIZE_PROPERTY_KEY);
        // if parameter has been defined and the value is number
        try {
            if (propertyValue != null) {
                return Double.parseDouble(propertyValue);
            }
        } catch (NumberFormatException e) {
            log.error("The size of free memory size is " + propertyValue + " should be 1 or 1.5 and so on");
        }
        // esle
        OperatingSystemMXBean bean = (OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        long max = bean.getTotalPhysicalMemorySize();
        double formatMemoryInGB = max / 1024.0 / 1024.0 / 1024.0;
        return formatMemoryInGB;
    }
}
