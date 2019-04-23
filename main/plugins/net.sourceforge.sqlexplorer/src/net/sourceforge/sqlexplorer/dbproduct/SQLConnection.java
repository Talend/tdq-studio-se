/*
 * Copyright (C) 2007 SQL Explorer Development Team
 * http://sourceforge.net/projects/eclipsesql
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.sqlexplorer.dbproduct;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

/**
 * Our SQLConnection, which adds the connection to our User object
 * 
 * @author John Spackman
 *
 */
public class SQLConnection extends net.sourceforge.squirrel_sql.fw.sql.SQLConnection {

    // The User that established this connection
    private User user;

    // The session that is using this connection
    private Session session;

    // When the connection was established
    private long createdTime;

    // Optional additional description
    private String description;

    /**
     * Constructor
     * 
     * @param user the User that the connection is for
     * @param connection JDBC Connection
     * @param description Optional additional description to appear in the connections view (EG process ID or server
     * connection ID)
     */
    public SQLConnection(User user, Connection connection, ManagedDriver driver, String description) {
        super(connection, null, driver.new SQLDriver());
        this.user = user;
        createdTime = System.currentTimeMillis();
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    /* package */ void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns true if this is a pooled connection
     * 
     * @return
     */
    public boolean isPooled() {
        return user.isInPool(this);
    }

    public long getCreatedTime() {
        return createdTime;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * for mysql case when no catalog setting then getCatalog() is empty
     * for mariraDB case when no catalog setting then getCatalog() is null
     * so that do switch from here to fix NPE
     */
    @Override
    public String getCatalog() throws SQLException {
        String catalog = super.getCatalog();
        return catalog == null ? StringUtils.EMPTY : catalog;
    }

}
