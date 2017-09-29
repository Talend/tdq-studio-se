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

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.talend.utils.properties.PropertiesLoader;
import org.talend.utils.properties.TypedProperties;
import org.talend.utils.sql.ConnectionUtils;

/**
 * Example code from http://www.unix.org.ua/orelly/java-ent/jenut/ch02_09.htm.
 */
public class DBViewerMain {

    protected static Logger log = Logger.getLogger(DBViewerMain.class);

    public static void main(java.lang.String[] args) {

        System.out.println("--- Database Viewer ---");

        try {
            TypedProperties connectionParams = PropertiesLoader.getProperties(DBViewerMain.class, "db.properties");
            assertNotNull("No properties found", connectionParams);

            String driverClassName = connectionParams.getProperty("driver");
            String dbUrl = connectionParams.getProperty("url");
            Connection con = ConnectionUtils.createConnection(dbUrl, driverClassName, connectionParams);
            DatabaseMetaData dbmd = con.getMetaData();

            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Database Product: " + dbmd.getDatabaseProductName());
            System.out.println("SQL Keywords Supported:");
            StringTokenizer st = new StringTokenizer(dbmd.getSQLKeywords(), ",");
            while (st.hasMoreTokens())
                System.out.println(" " + st.nextToken());

            // Get a ResultSet that contains all of the tables in this database
            // We specify a table_type of "TABLE" to prevent seeing system tables,
            // views and so forth
            String[] tableTypes = { "TABLE" };
            ResultSet allTables = dbmd.getTables(null, null, null, tableTypes);
            while (allTables.next()) {
                String tableName = allTables.getString("TABLE_NAME");
                System.out.println("Table Name: " + tableName);
                System.out.println("Table Type: " + allTables.getString("TABLE_TYPE"));
                System.out.println("Indexes: ");

                // Get a list of all the indexes for this table
                ResultSet indexList = dbmd.getIndexInfo(null, null, tableName, false, false);
                while (indexList.next()) {
                    System.out.println(" Index Name: " + indexList.getString("INDEX_NAME"));
                    System.out.println(" Column Name:" + indexList.getString("COLUMN_NAME"));
                }
                indexList.close();
            }

            allTables.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load database driver class");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
        }
    }
}
