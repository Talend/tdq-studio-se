// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataprofiler.core.ui.utils;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

/**
 * DOC zshen class global comment. Detailled comment
 */
public class FixOrderList extends ArrayList<Object[]> {

    /**
     * 2017/1/17
     */
    private static final long serialVersionUID = 1L;

    private int LimitNumber = 30;

    private int orderIndex = -1;

    private String orderProperty = StringUtils.EMPTY;

    public FixOrderList() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.ArrayList#add(java.lang.Object)
     */
    @Override
    public boolean add(Object[] e) {
        // parameter is not init yet
        if (this.orderIndex < 0) {
            return false;
        }
        Object value = e[this.orderIndex];
        int index = -1;
        for (Object[] listValue : this) {

            // exceed limit case
            if (++index >= LimitNumber) {
                return false;
            }
            if (value == null) {
                this.add(0, e);
                break;
            }
            Object currentValue = listValue[this.orderIndex];
            if (currentValue == null) {
                continue;
            }
            if (value.toString().compareTo(currentValue.toString()) > 0) {
                continue;
            }
            this.add(index, e);
        }
        if (this.size() == 0) {
            this.add(0, e);
        }
        // remove no need element
        if (this.size() > this.LimitNumber) {
            this.removeRange(this.LimitNumber, this.size());
        }
        return true;
    }

    public boolean isWork() {
        return orderIndex > -1 && !orderProperty.equals(StringUtils.EMPTY);
    }

    /**
     * Getter for limitNumber.
     * 
     * @return the limitNumber
     */
    public int getLimitNumber() {
        return this.LimitNumber;
    }

    /**
     * Sets the limitNumber.
     * 
     * @param limitNumber the limitNumber to set
     */
    public void setLimitNumber(int limitNumber) {
        this.LimitNumber = limitNumber;
    }

    /**
     * Getter for orderIndex.
     * 
     * @return the orderIndex
     */
    public int getOrderIndex() {
        return this.orderIndex;
    }

    /**
     * Sets the orderIndex.
     * 
     * @param orderIndex the orderIndex to set
     */
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    /**
     * Getter for orderProperty.
     * 
     * @return the orderProperty
     */
    public String getOrderProperty() {
        return this.orderProperty;
    }

    /**
     * Sets the orderProperty.
     * 
     * @param orderProperty the orderProperty to set
     */
    public void setOrderProperty(String orderProperty) {
        this.orderProperty = orderProperty;
    }

    public void reset() {
        super.clear();
        LimitNumber = 30;
        orderIndex = -1;
        orderProperty = StringUtils.EMPTY;
    }

}
