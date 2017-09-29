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
package net.sourceforge.sqlexplorer.service;

import java.util.List;
import java.util.Map;

import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.talend.cwm.indicator.DataValidation;
import org.talend.dataprofiler.service.IMapDBService;

/**
 * created by xqliu on 2014-9-23 Detailled comment
 * 
 */
public class MapDBUtils {

    private static MapDBUtils mapDBUtils;

    public static MapDBUtils getDefault() {
        if (mapDBUtils == null) {
            mapDBUtils = new MapDBUtils();
        }
        return mapDBUtils;
    }

    private IMapDBService mapDBService;

    public IMapDBService getMapDBService() {
        if (mapDBService == null) {
            BundleContext context = SQLExplorerPlugin.getDefault().getBundleContext();
            if (context != null) {
                ServiceReference serviceReference = context.getServiceReference(IMapDBService.class.getName());
                if (serviceReference != null) {
                    Object obj = context.getService(serviceReference);
                    if (obj != null) {
                        this.mapDBService = (IMapDBService) obj;
                    }
                }
            }
        }
        return this.mapDBService;
    }

    public List<Object[]> getColumnSetDBMapSubList(Object columnSetDBMap, long fromIndex, long toIndex,
            Map<Long, List<Object>> indexMap, DataValidation dataValiator) {
        if (getMapDBService() != null) {
            return getMapDBService().getColumnSetDBMapSubList(columnSetDBMap, fromIndex, toIndex, indexMap, dataValiator);
        }
        return null;
    }

    public List<Object[]> getDataSetDBMapSubList(Object dbMap, long fromIndex, long toIndex, Map<Long, Object> indexMap) {
        if (getMapDBService() != null) {
            return getMapDBService().getDataSetDBMapSubList(dbMap, fromIndex, toIndex, indexMap);
        }
        return null;
    }
}
