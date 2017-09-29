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
package org.talend.dataprofiler.core.ui.grid.utils.events;

import java.util.HashMap;
import java.util.Map;

/**
 * created by talend on Jan 30, 2015 Detailled comment
 *
 */
public class ObserverEvent {

    public static final String ITEM_HEADER_WIDTH = "itemHeaderWith"; //$NON-NLS-1$

    public static final String HORIZONTAL_SCROLLBAR_MOVE = "hScrollMove"; //$NON-NLS-1$

    public static final String COLUMN_HEADER_RESIZE = "columnHeaderResize"; //$NON-NLS-1$

    public static final String COLUMN_HEADER_MOVE = "columnHeaderMove"; //$NON-NLS-1$
    
    public static final String COLUMN_HIGH_LIGHT = "columnHighlight"; //$NON-NLS-1$

    public static final String VERTICAL_SRCOLL_VISABLE = "VSrcollVisable"; //$NON-NLS-1$

    ObserverEventEnum eventType = null;

    Map<String, Object> dataMap = null;

    public ObserverEvent(ObserverEventEnum eventType) {
        this.eventType = eventType;
    }

    /**
     * Get the value by special key.
     * 
     * @return the dataMap
     */
    public Object getData(String key) {
        if (dataMap == null) {
            return null;
        }
        return dataMap.get(key);
    }

    /**
     * Set new value by special key.
     * 
     * @param dataMap the dataMap to set
     */
    public void putData(String key, Object value) {
        if (dataMap == null) {
            dataMap = new HashMap<String, Object>();
        }
        dataMap.put(key, value);
    }

    /**
     * Getter for eventType.
     * 
     * @return the eventType
     */
    public ObserverEventEnum getEventType() {
        return this.eventType;
    }

}
