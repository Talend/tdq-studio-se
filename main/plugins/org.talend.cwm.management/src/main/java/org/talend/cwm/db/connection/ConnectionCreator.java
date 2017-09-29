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
package org.talend.cwm.db.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * DOC xqliu class global comment. Detailled comment
 */
public class ConnectionCreator implements Runnable {

    private static Logger log = Logger.getLogger(ConnectionCreator.class);

    private Driver driver;

    private String url;

    private Properties props;

    private Connection connection;

    private SQLException execption;

    public SQLException getExecption() {
        return execption;
    }

    public void setExecption(SQLException execption) {
        this.execption = execption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ConnectionCreator(Driver driver, String url, Properties props) {
        this.driver = driver;
        this.url = url;
        this.props = props;
    }

    public void run() {
        try {
            setConnection(getDriver().connect(getUrl(), getProps()));
        } catch (SQLException e) {
            setExecption(e);
            log.debug(e);
        }
    }

}
