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

/**
 * Singleton class to manage memory when running analyses.
 */
public final class AnalysisThreadMemoryChangeNotifier extends AbstractMemoryChangeNotifier {

    private static AnalysisThreadMemoryChangeNotifier instance;

    /**
     * Method "getInstance".
     * 
     * @return the singleton instance.
     */
    public static synchronized AnalysisThreadMemoryChangeNotifier getInstance() {
        if (null == instance) {
            instance = new AnalysisThreadMemoryChangeNotifier();
        }
        // MOD yyi 2012-06-19 TDQ-4916: reload for each instance to guarantee the threshold has been setted to the jvm.
        // MOD TDQ-7674 scorreia 2013-09-10 avoid setting these values at each call (calls happen for every analyzed
        // data row)
        // instance.reloadPerference();

        return instance;
    }

    private AnalysisThreadMemoryChangeNotifier() {
        super();
    }

    /**
     * Method "convertToMB" convert bytes to MB.
     * 
     * @param numByte the number of bytes.
     * @return the number of MB.
     */
    public static int convertToMB(long numByte) {
        return Math.round(numByte / 1024 / 1024);
    }

}
