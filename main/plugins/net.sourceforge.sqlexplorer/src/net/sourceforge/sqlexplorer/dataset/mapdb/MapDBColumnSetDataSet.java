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
import java.util.Map;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dataset.DataSet;
import net.sourceforge.sqlexplorer.dataset.DataSetRow;
import net.sourceforge.sqlexplorer.service.MapDBUtils;

import org.talend.cwm.indicator.DataValidation;

/**
 * created by talend on Sep 1, 2014 Detailled comment
 * 
 */
public class MapDBColumnSetDataSet extends MapDBkeyListDataSet {

    /**
     * MapDBColumnSetDataSet constructor.
     * 
     * @param columnLabels
     * @param imputDBMap
     * @param size
     * @param dataItemValidator
     */
    public MapDBColumnSetDataSet(String[] columnLabels, Map<List<Object>, Long> imputDBMap, Long size,
            DataValidation dataItemValidator, int pageSize) {
        super(columnLabels, imputDBMap, size, dataItemValidator, pageSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.sqlexplorer.dataset.mapdb.MapDBkeyListDataSet#getRow(int)
     */
    @Override
    public DataSetRow getRow(int index) {
        DataSetRow returnDataSetRow = null;
        if (iterator == null) {
            return super.getRow(index);
        } else {
            if (index < 0 || index >= getRowCount()) {
                throw new IndexOutOfBoundsException(Messages.getString("DataSet.errorIndexOutOfRange") + index);
            }
            if (currentIndex > index) {
                iterator = dataMap.keySet().iterator();
                currentIndex = 0;
            }

            while (currentIndex < index && iterator.hasNext()) {

                List<Object> next = iterator.next();
                if (dataValidator.isValid(dataMap.get(next))) {
                    currentIndex++;
                }
            }

            List<Object> valueList = iterator.next();
            while (!dataValidator.isValid(dataMap.get(valueList)) && iterator.hasNext()) {

                valueList = iterator.next();
            }
            currentIndex++;
            Comparable[] comparable = new Comparable[valueList.size()];
            for (int i = 0; i < valueList.size(); i++) {
                comparable[i] = (Comparable) valueList.get(i);
            }
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
        List<Object[]> subList = MapDBUtils.getDefault().getColumnSetDBMapSubList(this.dataMap, startIndex, endIndex, null,
                dataValidator);
        for (int i = 0; i < endIndex - startIndex; i++) {
            Object[] objArray = subList.get(i);
            for (int j = 0; j < this.getColumns().length; j++) {
                compareArray[i][j] = (Comparable) objArray[j];
            }
        }
        return new DataSet(this.columnHeads, compareArray);
    }

}
