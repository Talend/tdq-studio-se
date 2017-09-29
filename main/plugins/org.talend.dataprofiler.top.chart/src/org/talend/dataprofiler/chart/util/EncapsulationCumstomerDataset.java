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
package org.talend.dataprofiler.chart.util;

import java.io.Serializable;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.util.PublicCloneable;

/**
 * DOC zshen  class global comment. Detailled comment
 */
public class EncapsulationCumstomerDataset extends DefaultCategoryDataset implements CategoryDataset, PublicCloneable,
        Serializable {

    private static final long serialVersionUID = 3783255021497439600L;

    private CategoryDataset dataset;

    private Object cusmomerDataset;

    public EncapsulationCumstomerDataset(CategoryDataset dataset, Object cusmomerDataset) {
        this.dataset = dataset;
        this.cusmomerDataset = cusmomerDataset;
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.KeyedValues2D#getRowKey(int)
     */
    @Override
    public Comparable getRowKey(int row) {
        return dataset.getRowKey(row);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.KeyedValues2D#getRowIndex(java.lang.Comparable)
     */
    @Override
    public int getRowIndex(Comparable key) {
        return dataset.getRowIndex(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.KeyedValues2D#getRowKeys()
     */
    @Override
    public List getRowKeys() {
        return dataset.getRowKeys();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.KeyedValues2D#getColumnKey(int)
     */
    @Override
    public Comparable getColumnKey(int column) {
        return dataset.getColumnKey(column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.KeyedValues2D#getColumnIndex(java.lang.Comparable)
     */
    @Override
    public int getColumnIndex(Comparable key) {
        return dataset.getColumnIndex(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.KeyedValues2D#getColumnKeys()
     */
    @Override
    public List getColumnKeys() {
        return dataset.getColumnKeys();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.KeyedValues2D#getValue(java.lang.Comparable, java.lang.Comparable)
     */
    @Override
    public Number getValue(Comparable rowKey, Comparable columnKey) {
        return dataset.getValue(rowKey, columnKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.Values2D#getRowCount()
     */
    @Override
    public int getRowCount() {
        return dataset.getRowCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.Values2D#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return dataset.getColumnCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.Values2D#getValue(int, int)
     */
    @Override
    public Number getValue(int row, int column) {
        return dataset.getValue(row, column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.general.Dataset#addChangeListener(org.jfree.data.general.DatasetChangeListener)
     */
    @Override
    public void addChangeListener(DatasetChangeListener listener) {
        dataset.addChangeListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.general.Dataset#removeChangeListener(org.jfree.data.general.DatasetChangeListener)
     */
    @Override
    public void removeChangeListener(DatasetChangeListener listener) {
        dataset.removeChangeListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.general.Dataset#getGroup()
     */
    @Override
    public DatasetGroup getGroup() {
        return dataset.getGroup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jfree.data.general.Dataset#setGroup(org.jfree.data.general.DatasetGroup)
     */
    @Override
    public void setGroup(DatasetGroup group) {
        dataset.setGroup(group);
    }

    /**
     * Getter for cusmomerDataset.
     * 
     * @return the cusmomerDataset
     */
    public Object getCusmomerDataset() {
        return this.cusmomerDataset;
    }


}
