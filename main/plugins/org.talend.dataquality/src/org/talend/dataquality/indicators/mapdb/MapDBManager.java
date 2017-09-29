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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.mapdb.DB;
import org.mapdb.StoreDirect;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.dataquality.analysis.Analysis;
import org.talend.resource.ResourceManager;

/**
 * created by talend on Aug 19, 2014 Detailled comment
 * 
 */
public class MapDBManager {

    private Map<File, DB> dbMap = new HashMap<File, DB>();

    // store the times the mapdb has been used.
    private Map<DB, Integer> dbRefCountMap = new HashMap<DB, Integer>();

    // store all of task which used to close db
    private Map<DB, CloseDBTimeTask> closeTaskMap = new HashMap<DB, CloseDBTimeTask>();

    private Timer timer = new Timer();

    private static MapDBManager instance = null;

    public static MapDBManager getInstance() {
        if (instance == null) {
            instance = new MapDBManager();
        }
        return instance;
    }

    public DB getDB(File filePath) {
        return dbMap.get(filePath);
    }

    public DB putDB(File filePath, DB db) {
        return dbMap.put(filePath, db);
    }

    public void removeDB(File filePath) {
        dbMap.remove(filePath);
    }

    /**
     * 
     * If MapDB file is not exist or has been closed then this method will not do anything else will create new task to
     * close it.
     * 
     * @param analysis
     */
    public void closeDB(File filePath) {
        DB db = dbMap.get(filePath);
        closeDB(db, filePath);
    }

    /**
     * 
     * If MapDB file is not exist or has been closed then this method will not do anything else will create new task to
     * close it.
     * 
     * @param analysis
     */
    public void closeDB(Analysis analysis) {
        File filePath = getMapDBFile(analysis);
        closeDB(filePath);
    }

    /**
     * 
     * Delete mapDB file
     * 
     * @param analysis
     */
    public void deleteDB(Analysis analysis) {
        File mapDBFile = getMapDBFile(analysis);
        DB db = this.getDB(mapDBFile);
        // if db is in used close it first
        if (db != null) {
            this.removeDB(mapDBFile);
            dbRefCountMap.remove(db);
            db.close();
            while (!db.isClosed()) {
                try {
                    Thread.currentThread().wait(1000);
                } catch (InterruptedException e) {
                }
            }
        }
        // delete data file and index file
        if (mapDBFile.exists()) {
            mapDBFile.delete();
            new File(mapDBFile.getAbsolutePath() + StoreDirect.DATA_FILE_EXT).delete();
        }
    }

    /**
     * Get MapDB file by analysis
     * 
     * @param analysis
     * @return
     */
    private File getMapDBFile(Analysis analysis) {
        String analysisUUID = null;
        if (analysis != null) {
            analysisUUID = ResourceHelper.getUUID(analysis);
        }
        if (analysisUUID == null) {
            return null;
        }
        return MapDBUtils.createPath(ResourceManager.getMapDBFilePath(), analysisUUID);
    }

    /**
     * 
     * If MapDB file is not exist or has been closed then this method will not do anything else will create new task to
     * close it.
     * 
     * @param analysis
     */
    public void closeDB(String parentPathStr, String fileName) {
        File filePath = MapDBUtils.createPath(parentPathStr, fileName);
        closeDB(filePath);
    }

    private void closeDB(DB db, File filePath) {
        if (db == null || db.isClosed()) {
            return;
        }
        scheduleCloseTask(db, filePath);
    }

    /**
     * Create a new close db task and schedule it. If same task has been schedule or the db is used by other drill down
     * editor then will do nothing
     * 
     * @param db
     */
    protected void scheduleCloseTask(DB db, File filePath) {
        if (hasBeenSchedule(db)) {
            // current db has been schedule
            return;
        }
        if (isUsedByDrillDown(db)) {
            // current db is in use by other drilldownEditor
            return;
        }
        CloseDBTimeTask closeDBTimeTask = new CloseDBTimeTask(db, filePath);
        timer.schedule(closeDBTimeTask, MapDBContent.getDelayTime());// close db after 5 minute 5*60*1000
        closeTaskMap.put(db, closeDBTimeTask);
    }

    /**
     * Whether the db has been contain by schduel
     * 
     * @param db
     * @return
     */
    private boolean hasBeenSchedule(DB db) {
        return closeTaskMap.containsKey(db);
    }

    /**
     * Whether the db is used by other drill down editor
     * 
     * @return
     */
    private boolean isUsedByDrillDown(DB db) {
        return dbRefCountMap.containsKey(db);
    }

    /**
     * 
     * Record one db have been used how many times
     * 
     * @param filePath
     */
    public void addDBRef(File filePath) {
        DB db = dbMap.get(filePath);
        if (db == null) {
            return;
        }
        cancelCloseTask(db);

        Integer refConut = dbRefCountMap.get(db);
        if (refConut == null) {
            refConut = 0;
        }
        refConut++;
        dbRefCountMap.put(db, refConut);
    }

    /**
     * Cancel the close db task if special db has a task exist
     * 
     * @param db
     */
    protected void cancelCloseTask(DB db) {
        CloseDBTimeTask closeDBTimeTask = closeTaskMap.get(db);
        if (closeDBTimeTask != null) {
            closeDBTimeTask.cancel();
            timer.purge();
            closeTaskMap.remove(db);
        }
    }

    /**
     * 
     * Reduce the times which one db be used. When the count of times is 0 we will close this db.
     * 
     * @param filePath
     */
    public void removeDBRef(File filePath) {
        DB db = dbMap.get(filePath);
        if (db == null) {
            return;
        }
        Integer refConut = dbRefCountMap.get(db);
        if (refConut == null) {
            return;
        }
        refConut--;
        if (refConut > 0) {
            dbRefCountMap.put(db, refConut);
        } else {
            dbRefCountMap.remove(db);
            closeDB(db, filePath);
        }
    }

}
