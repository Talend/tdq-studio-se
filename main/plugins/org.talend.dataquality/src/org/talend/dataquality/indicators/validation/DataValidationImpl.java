// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.indicators.validation;

import java.util.ArrayList;
import java.util.List;

import org.talend.cwm.indicator.DataValidation;

/**
 * Validation data type
 * 
 */
public class DataValidationImpl implements DataValidation {

    private boolean checkKey = false;

    protected List<Object[]> resultList = null;

    protected String[] propertiesStr;

    // the current sort information: the property name and direction of the
    // sort.
    protected String sortPropertyName;

    protected int sortDirection;

    /**
     * DOC zshen DataValidationImpl constructor comment.
     * 
     * @param checkKey
     */
    public DataValidationImpl() {
        initParameter();
    }

    /**
     * DOC zshen Comment method "initParameter".
     */
    protected void initParameter() {
        if (resultList == null) {
            resultList = new ArrayList<>();
        } else {
            resultList.clear();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.DataValidation#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid(Object inputData) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.DataValidation#isCheckKey()
     */
    @Override
    public boolean isCheckKey() {
        return checkKey;
    }

    /**
     * Sets the checkKey.
     * 
     * @param checkKey the checkKey to set
     */
    @Override
    public void setCheckKey(boolean checkKey) {
        this.checkKey = checkKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.IPropertyOrder#isWork()
     */
    @Override
    public boolean isWork() {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.IPropertyOrder#setWork(boolean)
     */
    @Override
    public void setWork(boolean isWork) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.IPropertyOrder#add(java.lang.Object[])
     */
    @Override
    public boolean add(Object[] element) {
        return resultList.add(element);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.cwm.indicator.IPropertyOrder#getResult()
     */
    @Override
    public List<Object[]> getResult() {
        return resultList;
    }

    /**
     * 
     * Get the index of column which need to sort
     * 
     * @return
     */
    public int getReorderIndex() {
        return 0;
    }

    /**
     * 
     * Setting properties array
     * 
     * @param propertiesStr
     */
    public void setProperties(String[] propertiesStr) {
        this.propertiesStr = propertiesStr;
    }

    /**
     * Getter for sortPropertyName.
     * 
     * @return the sortPropertyName
     */
    public String getSortPropertyName() {
        return this.sortPropertyName;
    }

    /**
     * Getter for sortDirection.
     * 
     * @return the sortDirection
     */
    public int getSortDirection() {
        return this.sortDirection;
    }

    /**
     * 
     * Setting information of sort
     * 
     * @param controller
     */
    public void synSortState(String sortPropertyName, int sortDirection) {
        this.sortDirection = sortDirection;
        this.sortPropertyName = sortPropertyName;
    }

}
