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
package org.talend.dq.analysis.connpool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dq.analysis.AnalysisHandler;
import org.talend.dq.helper.EObjectHelper;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.hive.handler.HiveConnectionHandler;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.foundation.softwaredeployment.DataManager;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TdqAnalysisConnectionPool {

    private static Logger log = Logger.getLogger(TdqAnalysisConnectionPool.class);

    public static final int CONNECTIONS_PER_ANALYSIS_DEFAULT_LENGTH = 5;

    private static final int DEFAULT_WAIT_MILLISECOND = 5;

    private static final int DEFAULT_WAIT_TIMES = 10;

    private static final float DEFAULT_CONNECTION_NUMBER_OFFSET = 0.5f;

    private static final boolean SHOW_CONNECTIONS_INFO = Boolean.FALSE;

    public static final String NUMBER_OF_CONNECTIONS_PER_ANALYSIS = "NUMBER_OF_CONNECTIONS_PER_ANALYSIS"; //$NON-NLS-1$\

    /**
     * Map where key is the analysis and value is the connection pool of this analysis.
     */
    private static final Map<Analysis, TdqAnalysisConnectionPool> INSTANCE_ANA_TO_POOL_MAP = Collections
            .synchronizedMap(new HashMap<Analysis, TdqAnalysisConnectionPool>());

    /**
     * Keep a reference to analysis because the connection information from this analysis is required when create a
     * java.sql.connection.
     */
    private Analysis analysis = null;

    private Vector<PooledTdqAnalysisConnection> pConnections;

    private int driverMaxConnections = Integer.MAX_VALUE;

    private int maxConnections = CONNECTIONS_PER_ANALYSIS_DEFAULT_LENGTH;

    private String synchronizedFlag = ""; //$NON-NLS-1$\

    /**
     * Look up the conn pool from instance map, if there not have, creata a new one.
     * 
     * @param analysis
     * @return the specific connection pool by analylsis.
     */
    public static TdqAnalysisConnectionPool getConnectionPool(Analysis analysis) {
        TdqAnalysisConnectionPool connPoolTemp = INSTANCE_ANA_TO_POOL_MAP.get(analysis);
        if (connPoolTemp == null) {
            int maxConnNumberPerAnalysis = AnalysisHandler.createHandler(analysis).getNumberOfConnectionsPerAnalysis();
            connPoolTemp = new TdqAnalysisConnectionPool(analysis, maxConnNumberPerAnalysis);
            INSTANCE_ANA_TO_POOL_MAP.put(analysis, connPoolTemp);
        }
        return connPoolTemp;
    }

    /**
     * close the connection pool: 1) close all the connections belong to it; 2) remove it from the map.
     * 
     * @param analysis
     */
    public static void closeConnectionPool(Analysis analysis) {
        TdqAnalysisConnectionPool connectionPool = INSTANCE_ANA_TO_POOL_MAP.get(analysis);
        if (connectionPool != null) {
            connectionPool.closeConnectionPool();
        }

        deregisterHiveDriver(analysis);
    }

    /**
     * because in the HiveDriver class have done registerDriver, and sometimes caused the DriverManager to find the
     * error driver(e.g: need to find a hsql driver, but get a hive driver), so need to deregister it.
     * 
     * @param analysis
     */
    private static void deregisterHiveDriver(Analysis analysis) {
        org.talend.core.model.metadata.builder.connection.Connection tdDataProvider = (org.talend.core.model.metadata.builder.connection.Connection) analysis
                .getContext().getConnection();
        if (ConnectionHelper.isHive(tdDataProvider)) {
            Enumeration<Driver> drivers = java.sql.DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                String name = driver.getClass().getName();
                // if have other hive drivers, need to remove it here.
                if (name.equals(EDatabase4DriverClassName.HIVE.getDriverClass())
                        || name.equals(EDatabase4DriverClassName.HIVE2.getDriverClass())) {
                    try {
                        java.sql.DriverManager.deregisterDriver(driver);
                    } catch (SQLException e) {
                        log.error(e, e);
                    }
                }
            }
        }
    }

    /**
     * return the connection to the pool.
     * 
     * @param analysis
     * @param connection
     */
    public static void returnPooledConnection(Analysis analysis, java.sql.Connection connection) {
        TdqAnalysisConnectionPool connectionPool = INSTANCE_ANA_TO_POOL_MAP.get(analysis);
        if (connectionPool != null) {
            connectionPool.returnConnection(connection);
        }
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        if (maxConnections > 0) {
            this.maxConnections = maxConnections;
        }
    }

    public int getDriverMaxConnections() {
        return this.driverMaxConnections;
    }

    public void setDriverMaxConnections(int driverMaxConnections) {
        if (driverMaxConnections > 0) {
            this.driverMaxConnections = driverMaxConnections;
        }
    }

    /**
     * DOC xqliu TdqAnalysisConnectionPool constructor comment.
     * 
     * @param tConnection
     */
    public TdqAnalysisConnectionPool(Analysis analysis, int maxConnections) {
        this.analysis = analysis;
        this.setMaxConnections(maxConnections);
    }

    /**
     * DOC xqliu Comment method "getPConnections".
     * 
     * @return
     */
    public Vector<PooledTdqAnalysisConnection> getPConnections() {
        if (this.pConnections == null) {
            this.pConnections = new Vector<PooledTdqAnalysisConnection>();
        }
        return this.pConnections;
    }

    /**
     * DOC xqliu Comment method "setPConnections".
     * 
     * @param pConnections
     */
    public void setPConnections(Vector<PooledTdqAnalysisConnection> pConnections) {
        this.pConnections = pConnections;
    }

    /**
     * DOC xqliu Comment method "getConnection".
     * 
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = findFreeConnection();
        while (conn == null) {
            wait(DEFAULT_WAIT_MILLISECOND);
            conn = findFreeConnection();
            if (conn == null) {
                newConnection();
            }
        }
        showConnectionInfo();
        return conn;
    }

    /**
     * DOC xqliu Comment method "newConnection".
     * 
     * @return
     */
    private Connection newConnection() {
        Connection conn = null;
        if (isFull()) {
            return conn;
        }
        DataManager datamanager = analysis.getContext().getConnection();
        if (datamanager == null) {
            log.error(Messages.getString("AnalysisExecutor.DataManagerNull", analysis.getName())); //$NON-NLS-1$
            return null;
        }
        if (datamanager != null && datamanager.eIsProxy()) {
            datamanager = (DataManager) EObjectHelper.resolveObject(datamanager);
        }
        org.talend.core.model.metadata.builder.connection.Connection dataprovider = SwitchHelpers.CONNECTION_SWITCH
                .doSwitch(datamanager);

        TypedReturnCode<Connection> trcConn = null;

        IMetadataConnection metadataConnection = ConvertionHelper.convert(dataprovider);

        if (metadataConnection != null && EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConnection.getDbType())) {
            trcConn = new TypedReturnCode<Connection>(false);
            try {
                HiveConnectionHandler hiveConnHandler = HiveConnectionManager.getInstance().createHandler(metadataConnection);
                Connection hiveConnection = hiveConnHandler.createHiveConnection();
                if (hiveConnection != null) {
                    trcConn.setOk(true);
                    trcConn.setObject(hiveConnection);
                }
            } catch (ClassNotFoundException e) {
                trcConn.setOk(false);
                log.error(e);
            } catch (InstantiationException e) {
                trcConn.setOk(false);
                log.error(e);
            } catch (IllegalAccessException e) {
                trcConn.setOk(false);
                log.error(e);
            } catch (SQLException e) {
                trcConn.setOk(false);
                log.error(e);
            }
        } else {
            trcConn = JavaSqlFactory.createConnection(dataprovider);
        }
        if (trcConn != null && trcConn.isOk()) {
            conn = trcConn.getObject();
            synchronized (this.synchronizedFlag) {
                this.getPConnections().add(new PooledTdqAnalysisConnection(conn));
            }
        }

        if (conn != null) {
            try {
                if (metadataConnection != null
                        && EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataConnection.getDbType())) {
                    // don't set the max connection number if it is hive connection
                } else {
                    DatabaseMetaData metaData = conn.getMetaData();
                    int currentDriverMaxConnections = new Float(metaData.getMaxConnections() * DEFAULT_CONNECTION_NUMBER_OFFSET)
                            .intValue();
                    synchronized (this.synchronizedFlag) {
                        this.setDriverMaxConnections(currentDriverMaxConnections);
                    }
                }
            } catch (SQLException e) {
                log.debug(e, e);
            }
        }

        return conn;
    }

    /**
     * DOC xqliu Comment method "isFull".
     * 
     * @return
     */
    private synchronized boolean isFull() {
        boolean result = true;
        int topLimit = Math.min(this.getMaxConnections(), this.getDriverMaxConnections());
        if (topLimit < 1) {
            result = false;
        } else {
            result = !(this.getPConnections().size() < topLimit);
        }
        return result;
    }

    /**
     * DOC xqliu Comment method "findFreeConnection".
     * 
     * @return
     */
    private synchronized Connection findFreeConnection() {
        Connection conn = null;

        Iterator<PooledTdqAnalysisConnection> enumerate = this.getPConnections().iterator();
        while (enumerate.hasNext()) {
            PooledTdqAnalysisConnection pConn = enumerate.next();
            try {
                if (!pConn.isBusy()) {
                    Connection tempConn = pConn.getConnection();
                    if (tempConn.isClosed()) {
                        enumerate.remove();
                    } else {
                        conn = tempConn;
                        pConn.setBusy(true);
                        break;
                    }
                }
            } catch (Exception e) {
                log.debug(e);
            }
        }

        return conn;
    }

    /**
     * DOC xqliu Comment method "returnConnection".
     * 
     * @param conn
     */
    public synchronized void returnConnection(Connection conn) {
        if (conn == null) {
            return;
        }

        Enumeration<PooledTdqAnalysisConnection> enumerate = this.getPConnections().elements();
        while (enumerate.hasMoreElements()) {
            PooledTdqAnalysisConnection pConn = enumerate.nextElement();
            if (conn == pConn.getConnection()) {
                pConn.setBusy(false);
                break;
            }
        }

        showConnectionInfo();
    }

    /**
     * DOC xqliu Comment method "showConnectionInfo".
     */
    public void showConnectionInfo() {
        if (SHOW_CONNECTIONS_INFO) {
            int i = 0;
            Enumeration<PooledTdqAnalysisConnection> enumerate = this.getPConnections().elements();
            try {
                boolean hasElement = false;
                while (enumerate.hasMoreElements()) {
                    hasElement = true;
                    PooledTdqAnalysisConnection pConn = enumerate.nextElement();
                    i++;
                    log.error("pConn: id=[" + i + "] pid=[" + pConn.hashCode() + "] conn=[" + pConn.getConnection().toString() + "] [closed=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                            + pConn.getConnection().isClosed() + "] busy=[" + pConn.isBusy() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
                }
                if (!hasElement) {
                    log.error("the connection pool is empty!"); //$NON-NLS-1$
                }
            } catch (Exception e) {
                log.debug(e);
            }
        }
    }

    /**
     * DOC xqliu Comment method "refreshConnections".
     */
    public synchronized void refreshConnections() {
        Enumeration<PooledTdqAnalysisConnection> enumerate = this.getPConnections().elements();
        while (enumerate.hasMoreElements()) {
            PooledTdqAnalysisConnection pConn = enumerate.nextElement();
            int times = 0;
            busy: while (pConn.isBusy()) {
                try {
                    if (pConn.getConnection().isClosed()) {
                        break busy;
                    }
                } catch (Exception e) {
                    log.debug(e);
                }
                times++;
                wait(DEFAULT_WAIT_MILLISECOND);
                if (times > DEFAULT_WAIT_TIMES) {
                    break busy;
                }
            }

            closeConnection(pConn.getConnection());
            pConn.setConnection(newConnection());
            pConn.setBusy(false);
        }
    }

    /**
     * close all the connections belong to this pool, remove the pool from the map.
     */
    public void closeConnectionPool() {
        Enumeration<PooledTdqAnalysisConnection> enumerate = this.getPConnections().elements();
        while (enumerate.hasMoreElements()) {
            PooledTdqAnalysisConnection pConn = enumerate.nextElement();
            int times = 0;
            busy: if (pConn.isBusy()) {
                times++;
                wait(DEFAULT_WAIT_MILLISECOND);
                if (times > DEFAULT_WAIT_TIMES) {
                    break busy;
                }
            }
            closeConnection(pConn.getConnection());
        }
        getPConnections().removeAllElements();
        this.setPConnections(null);
        INSTANCE_ANA_TO_POOL_MAP.put(analysis, null);
    }

    /**
     * DOC xqliu Comment method "closeConnection".
     * 
     * @param conn
     */
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            showConnectionInfo();
        }
    }

    /**
     * DOC xqliu Comment method "removeConnection".
     * 
     * @param conn
     */
    public synchronized void removeConnection(Connection conn) {

        Iterator<PooledTdqAnalysisConnection> enumerate = this.getPConnections().iterator();

        while (enumerate.hasNext()) {
            PooledTdqAnalysisConnection pConn = enumerate.next();
            if (pConn.getConnection().equals(conn)) {
                getPConnections().remove(pConn);
                break;
            }
        }

        showConnectionInfo();
    }

    /**
     * DOC xqliu Comment method "wait".
     * 
     * @param mSeconds
     */
    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
            log.debug(e, e);
        }
    }

    /**
     * DOC xqliu TdqAnalysisConnectionPool class global comment. Detailled comment
     */
    class PooledTdqAnalysisConnection {

        Connection connection = null;

        boolean busy = false;

        public PooledTdqAnalysisConnection(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }

        public boolean isBusy() {
            return busy;
        }

        public void setBusy(boolean busy) {
            this.busy = busy;
        }
    }
}
