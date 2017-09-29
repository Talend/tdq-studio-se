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
package net.sourceforge.sqlexplorer.dataset.mapdb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dataset.DataSetRow;

import org.talend.cwm.indicator.DataValidation;

/**
 * created by talend on Aug 28, 2014 Detailled comment
 * 
 */
public class MapDBkeyListDataSet extends TalendDataSet {

    protected Map<List<Object>, Long> dataMap = null;

    protected int currentIndex = 0;

    protected Iterator<List<Object>> iterator = null;

    /**
     * DOC talend MapDBValueListDataSet constructor comment.
     * 
     * @param columnLabels
     * @param imputDBMap
     */
    public MapDBkeyListDataSet(String[] columnLabels, Map<List<Object>, Long> imputDBMap, Long size,
            DataValidation dataItemValidator, int pageSize) {
        super(columnLabels, new Comparable[0][0], pageSize);
        dataMap = imputDBMap;
        iterator = imputDBMap.keySet().iterator();
        rowSize = size;
        dataValidator = dataItemValidator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.sqlexplorer.dataset.DataSet#getRowCount()
     */
    @Override
    public int getRowCount() {
        return (int) rowSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.sqlexplorer.dataset.DataSet#getRow(int)
     */
    @Override
    public DataSetRow getRow(int index) {
        DataSetRow returnDataSetRow = null;
        if (iterator == null) {
            return super.getRow(index);
        } else {
            if (index < 0 || index >= rowSize) {
                throw new IndexOutOfBoundsException(Messages.getString("DataSet.errorIndexOutOfRange") + index);
            }
            if (currentIndex > index) {
                iterator = dataMap.keySet().iterator();
                currentIndex = 0;
            }
            findNextOne(index);
            List<Object> valueList = iterator.next();
            currentIndex++;
            Object[] row = ((Object[]) valueList.get(0));
            Comparable[] comparable = new Comparable[valueList.size()];
            for (int i = 0; i < valueList.size(); i++) {
                comparable[i] = (Comparable) row[i];
            }
            returnDataSetRow = new DataSetRow(this, comparable);
            return returnDataSetRow;
        }
    }

    /**
     * DOC talend Comment method "findNextOne".
     * 
     * @param index
     */
    protected void findNextOne(int index) {
        while (currentIndex < index) {
            iterator.next();
            currentIndex++;
        }
    }
}
