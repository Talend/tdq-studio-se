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
package org.talend.dataprofiler.service;

import java.util.List;
import java.util.Map;

import org.talend.cwm.indicator.DataValidation;

/**
 * created by xqliu on 2014-9-23 Detailled comment
 * 
 */
public interface IMapDBService {

    public List<Object[]> getColumnSetDBMapSubList(Object columnSetDBMap, long fromIndex, long toIndex,
            Map<Long, List<Object>> indexMap, DataValidation dataValiator);

    public List<Object[]> getDataSetDBMapSubList(Object dbMap, long fromIndex, long toIndex, Map<Long, Object> indexMap);
}
