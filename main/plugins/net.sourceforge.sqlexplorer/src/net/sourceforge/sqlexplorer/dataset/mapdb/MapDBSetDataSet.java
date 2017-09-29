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

import java.util.List;
import java.util.Set;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dataset.DataSet;
import net.sourceforge.sqlexplorer.dataset.DataSetRow;
import net.sourceforge.sqlexplorer.service.MapDBUtils;

/**
 * created by talend on Aug 29, 2014 Detailled comment
 * 
 */
public class MapDBSetDataSet extends MapDBDataSet {

    protected Set<Object> dataSet = null;

    /**
     * DOC talend MapDBSetDataSet constructor comment.
     * 
     * @param columnLabels
     * @param data
     */
    public MapDBSetDataSet(String[] columnLabels, Comparable[][] data, int pageSize) {
        super(columnLabels, data, pageSize);
    }

    public MapDBSetDataSet(String[] columnLabels, Set<Object> imputDBSet, int pageSize) {
        super(columnLabels, new Comparable[0][0], pageSize);
        this.dataSet = imputDBSet;
        iterator = dataSet.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.sqlexplorer.dataset.DataSet#getRowCount()
     */
    @Override
    public int getRowCount() {
        if (dataSet != null) {
            return dataSet.size();
        } else {
            return super.getRowCount();
        }
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
            if (index < 0 || index >= dataSet.size()) {
                throw new IndexOutOfBoundsException(Messages.getString("DataSet.errorIndexOutOfRange") + index);
            }
            if (currentIndex > index) {
                iterator = dataSet.iterator();
                currentIndex = 0;
            }
            while (currentIndex < index) {
                iterator.next();
                currentIndex++;
            }
            Object currentData = iterator.next();
            currentIndex++;
            Comparable[] comparable = new Comparable[1];
            comparable[0] = (Comparable) currentData;
            returnDataSetRow = new DataSetRow(this, comparable);
            return returnDataSetRow;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.sqlexplorer.dataset.mapdb.TalendDataSet#getCurrentPageDataSet()
     */
    @Override
    public DataSet getCurrentPageDataSet() {
        Comparable[][] compareArray = new Comparable[(int) (endIndex - startIndex)][this.getColumns().length];
        List<Object[]> subList = MapDBUtils.getDefault().getDataSetDBMapSubList(this.dataSet, startIndex, endIndex, null);
        for (int i = 0; i < endIndex - startIndex; i++) {
            Object[] objArray = subList.get(i);
            for (int j = 0; j < this.getColumns().length; j++) {
                compareArray[i][j] = (Comparable) objArray[j];
            }
        }
        return new DataSet(this.columnHeads, compareArray);
    }

}
