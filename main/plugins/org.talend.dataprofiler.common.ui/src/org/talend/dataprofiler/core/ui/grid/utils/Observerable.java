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
package org.talend.dataprofiler.core.ui.grid.utils;

/**
 * The interface of Observerable
 * 
 */
public interface Observerable<T> {

    /**
     * 
     * Add one observer
     * 
     * @param observer
     * @return
     */
    boolean addObserver(TDQObserver<T> observer);

    /**
     * 
     * Remove special observer
     * 
     * @param observer
     * @return
     */
    boolean removeObserver(TDQObserver<T> observer);

    /**
     * 
     * Clear all of observer
     */
    void clearObserver();

    /**
     * 
     * Notify all of Observers for current update
     */
    void notifyObservers();
}
