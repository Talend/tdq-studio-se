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
package org.talend.cwm.management.mapdb;

import java.util.List;
import java.util.Map;

import org.talend.cwm.indicator.DataValidation;
import org.talend.dataprofiler.service.IMapDBService;
import org.talend.dataquality.indicators.mapdb.AbstractDB;
import org.talend.dataquality.indicators.mapdb.ColumnSetDBMap;

/**
 * created by xqliu on 2014-9-23 Detailled comment
 * 
 */
public class MapDBService implements IMapDBService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.IMapDBService#getColumnSetDBMapSubList(java.lang.Object, long, long,
     * java.util.Map, org.talend.cwm.indicator.DataValidation)
     */
    public List<Object[]> getColumnSetDBMapSubList(Object columnSetDBMap, long fromIndex, long toIndex,
            Map<Long, List<Object>> indexMap, DataValidation dataValiator) {
        return ((ColumnSetDBMap) columnSetDBMap).subList(fromIndex, toIndex, null, dataValiator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.service.IMapDBService#getDataSetDBMapSubList(java.lang.Object, long, long,
     * java.util.Map)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Object[]> getDataSetDBMapSubList(Object dbMap, long fromIndex, long toIndex, Map<Long, Object> indexMap) {
        return ((AbstractDB) dbMap).subList(fromIndex, toIndex, indexMap);
    }

}
