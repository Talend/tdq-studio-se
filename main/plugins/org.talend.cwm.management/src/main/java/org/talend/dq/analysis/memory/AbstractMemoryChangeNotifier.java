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
package org.talend.dq.analysis.memory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;

/**
 * DOC yyi class global comment. Detailled comment
 */
public abstract class AbstractMemoryChangeNotifier {

    /**
     * Key of the preference related to the activation of the memory control.
     */
    public static final String ANALYSIS_AUTOMATIC_MEMORY_CONTROL = "ANALYSIS_AUTOMATIC_MEMORY_CONTROL";//$NON-NLS-1$

    /**
     * Key of the preference related to the value set by the user for the memory control.
     */
    public static final String ANALYSIS_MEMORY_THRESHOLD = "ANALYSIS_MEMORY_THRESHOLD";//$NON-NLS-1$

    private MemoryPoolMXBean tenuredGenPoll;

    private boolean isThresholdControl;

    private int userDefineThreshold;

    protected AbstractMemoryChangeNotifier() {
        initialize();
    }

    private MemoryPoolMXBean findTenuredGenPool() {
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getType() == MemoryType.HEAP && pool.isUsageThresholdSupported()) {
                return pool;
            }
        }
        return null;
    }

    protected boolean initialize() {
        tenuredGenPoll = findTenuredGenPool();
        this.initializeThresholdsFromPreferences();
        return null != tenuredGenPoll;
    }

    private void initializeThresholdsFromPreferences() {
        isThresholdControl = PlatformUI.getPreferenceStore().getBoolean(ANALYSIS_AUTOMATIC_MEMORY_CONTROL);
        userDefineThreshold = PlatformUI.getPreferenceStore().getInt(ANALYSIS_MEMORY_THRESHOLD);
        reloadPerference();

        PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                // MOD scorreia 2013-09-10 update also isThresholdControl
                if (event.getProperty() == ANALYSIS_AUTOMATIC_MEMORY_CONTROL) {
                    isThresholdControl = Boolean.valueOf(event.getNewValue().toString());
                    reloadPerference();
                } else if (event.getProperty() == ANALYSIS_MEMORY_THRESHOLD) {
                    userDefineThreshold = Integer.valueOf(event.getNewValue().toString());
                    isThresholdControl = true;
                    reloadPerference();
                }
            }
        });

    }

    private void reloadPerference() {
        if (isThresholdControl) {
            // The #userDefineThreshold changes to the free memory, if the free memory size less than
            // userDefineThreshold vlaue the analysis shoud be stop.
            setUsageThreshold(megaToByte(userDefineThreshold));
        } else {
            // Clear limit
            setUsageThreshold(0);
        }
    }

    // if threshold is greater than the maximum amount of memory for this memory pool if defined.
    private void setUsageThreshold(long threshold) {
        // MOD yyi 2012-04-12 TDQ-4916:The usage threshold crossing checking is disabled if it is set to zero.
        if (threshold <= 0) {
            tenuredGenPoll.setUsageThreshold(0);
        } else {
            // the threshold shoudle less than max memory
            tenuredGenPoll.setUsageThreshold(threshold);
        }
    }

    /**
     * Method "isUsageThresholdExceeded".
     * 
     * @return true when the memory control is activated and the used memory exceeds the threshold defined by the user
     * in the preference page.
     */
    public boolean isUsageThresholdExceeded() {
        if (!isThresholdControl) { // no control set in preference pages
            return false;
        }
        boolean isExceeded = tenuredGenPoll.isUsageThresholdExceeded();
        if (isExceeded) {
            ManagementFactory.getMemoryMXBean().gc();
            isExceeded = tenuredGenPoll.isUsageThresholdExceeded();
        }
        return isExceeded;
    }

    private static long megaToByte(int numMb) {
        return numMb * 1024 * 1024 - 512 * 1024;
    }

}
