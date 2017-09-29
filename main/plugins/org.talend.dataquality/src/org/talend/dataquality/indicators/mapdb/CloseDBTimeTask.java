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
import java.util.TimerTask;

import org.mapdb.DB;

/**
 * created by talend on Sep 18, 2014 Detailled comment
 * 
 */
public class CloseDBTimeTask extends TimerTask {

    private DB db = null;

    private File dbFile = null;

    public CloseDBTimeTask(DB closeDb, File filePath) {
        this.db = closeDb;
        dbFile = filePath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        if (db != null && !db.isClosed()) {
            MapDBManager.getInstance().removeDB(dbFile);
            db.close();
        }
    }

}
