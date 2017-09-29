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
package org.talend.dataprofiler.core.ui.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class EventManager {

    private static EventManager instance;

    // MultiMap<EventEnum,IEventReceiver>
    private Map<Object, MultiMap> ctxToReceiverQueueMap;

    private EventManager() {
        ctxToReceiverQueueMap = new HashMap<Object, MultiMap>();
    }

    /**
     * Getter for instance.
     * 
     * @return the instance
     */
    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    /**
     * register repository node's event receiver with its event type. e.g. when opening a editor, need to call register
     * to remember its receiver with some widgets.
     * 
     * @param context: the host of the event receiver
     * @param event: pointed event
     * @param receiver: event handler
     * @return
     */
    public boolean register(Object context, EventEnum event, IEventReceiver receiver) {
        MultiMap receverQueryMap = ctxToReceiverQueueMap.get(context);
        if (receverQueryMap == null) {
            receverQueryMap = new MultiValueMap();
            ctxToReceiverQueueMap.put(context, receverQueryMap);
        }
        receverQueryMap.put(event, receiver);

        return false;
    }

    /**
     * remove the registered receivers & event of some repository node when unnecessory. e.g. when closing some editor.
     * 
     * @param context: the host of the event receiver
     * @param event: pointed event
     * @param toBeUnRegistered: event handler
     * @return false: if the related receivers & event of some repository node is not registered yet.
     */
    public boolean unRegister(Object context, EventEnum event, IEventReceiver toBeUnRegistered) {
        MultiMap receverQueryMap = ctxToReceiverQueueMap.get(context);
        if (receverQueryMap == null) {
            return false;
        }
        if (receverQueryMap.containsValue(toBeUnRegistered)) {
            receverQueryMap.remove(event, toBeUnRegistered);
        }

        return true;
    }

    /**
     * clear all eventReceivers for the current event, for the current context
     * 
     * @param context
     * @param event
     * @return
     */
    public boolean clearEvent(Object context, EventEnum event) {
        MultiMap receverQueryMap = ctxToReceiverQueueMap.get(context);
        if (receverQueryMap == null) {
            return false;
        }
        receverQueryMap.remove(event);
        return true;
    }

    /**
     * call the registered event's related receiver to handle
     * 
     * @param context
     * @param event
     * @param data
     */
    public boolean publish(Object context, EventEnum event, Object data) {
        MultiMap receverQueryMap = ctxToReceiverQueueMap.get(context);
        if (receverQueryMap == null || receverQueryMap.isEmpty()) {
            return true;
        }
        List<IEventReceiver> receivers = (List<IEventReceiver>) receverQueryMap.get(event);
        if (receivers == null || receivers.size() == 0) {
            return true;
        }
        // Notify the receiver to handle the event.
        boolean handleResult = Boolean.TRUE;
        for (IEventReceiver receiver : receivers) {
            handleResult = receiver.handle(data);
            if (!handleResult) {
                break;
            }
        }
        return handleResult;
    }

    /**
     * find if there are some registered event for the context, if existed, return the index position in the event list.
     * 
     * @param context
     * @param event
     * @return
     */
    public IEventReceiver findRegisteredEvent(Object context, EventEnum event, int index) {
        MultiMap receverQueryMap = ctxToReceiverQueueMap.get(context);
        if (receverQueryMap == null || receverQueryMap.isEmpty()) {
            return null;
        }
        List<IEventReceiver> receivers = (List<IEventReceiver>) receverQueryMap.get(event);
        if (receivers == null || receivers.size() == 0 || receivers.size() < index) {
            return null;
        }
        return receivers.get(index);
    }
}
