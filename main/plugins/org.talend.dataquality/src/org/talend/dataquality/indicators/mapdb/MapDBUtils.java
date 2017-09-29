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
package org.talend.dataquality.indicators.mapdb;

import java.io.File;
import java.util.List;

import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.impl.IndicatorImpl;
import org.talend.resource.ResourceManager;

/**
 * created by talend on Dec 11, 2014 Detailled comment
 * 
 */
public class MapDBUtils {

    /**
     * Make sure the path of DB file is valid
     * 
     * @param ParentPathStr the Path of parent folder
     * @param FileName The name of db File
     * @return If parentPathStr or fileName is null will return null else return the file of special path
     */
    public static File createPath(String parentPathStr, String fileName) {
        if (parentPathStr == null || fileName == null) {
            return null;
        }
        File parentFolder = new File(parentPathStr);
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }
        File file = new File(parentFolder.getPath() + File.separator + fileName);
        return file;
    }

    /**
     * 
     * Get the file of mapDB which used by indicator.
     * 
     * @param indicator
     * @return
     */
    public static File getMapDBFile(Indicator indicator) {
        return createPath(ResourceManager.getMapDBFilePath(), ResourceManager.getMapDBFileName(indicator));
    }

    /**
     * 
     * Get the map instance by dbName from indicator
     * 
     * @param dbName
     * @param indicator
     * @return
     */
    public static AbstractDB<Object> getMapDB(String dbName, Indicator indicator) {
        return ((IndicatorImpl) indicator).getMapDB(dbName);
    }

    /**
     * Store drill down data into file
     * 
     * @param masterObject
     * @param inputRowList
     */
    public static void handleDrillDownData(Object masterObject, List<Object> inputRowList, Indicator indicator) {
        ((IndicatorImpl) indicator).handleDrillDownData(masterObject, inputRowList);
    }
}
