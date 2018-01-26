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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.impl.ConnectionFactoryImpl;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.cwm.management.i18n.Messages;
import org.talend.dq.CWMPlugin;
import org.talend.utils.sugars.ReturnCode;

/**
 * DOC msjian class global comment. Detailled comment
 */
public class ConnectionUtilsTest {

    /**
     * Test method for
     * {@link org.talend.cwm.db.connection.ConnectionUtils#isGeneralJdbc(org.talend.core.model.metadata.builder.connection.Connection)}
     * .
     */
    @Test
    public void testIsGeneralJdbc() {
        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        EDatabaseTypeName generalJdbcType = EDatabaseTypeName.GENERAL_JDBC;
        createDatabaseConnection.setDatabaseType(generalJdbcType.getDisplayName());
        Assert.assertTrue(
                "Current DatabaseType of connection should be " + EDatabaseTypeName.GENERAL_JDBC.getDisplayName(), ConnectionUtils.isGeneralJdbc(createDatabaseConnection)); //$NON-NLS-1$
    }

    /**
     * Test method for
     * {@link org.talend.cwm.db.connection.ConnectionUtils#checkGeneralJdbcJarFilePathDriverClassName(org.talend.core.model.metadata.builder.connection.DatabaseConnection)}
     * . case 1 success case
     */
    @Test
    public void testCheckGeneralJdbcJarFilePathDriverClassNameCase1() {
        String driverClass = "om.mysql.jdbc.Driver"; //$NON-NLS-1$
        String driverName = "mysql-connector-java-5.1.12-bin.jar"; //$NON-NLS-1$
        CopyTheJarFile();

        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setDriverClass(driverClass);
        createDatabaseConnection.setDriverJarPath(driverName);
        try {
            ReturnCode rc = ConnectionUtils.checkJdbcJarFilePathDriverClassName(createDatabaseConnection);

            Assert.assertTrue("The driver " + driverName + " of conection can not be find", rc.isOk()); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.cwm.db.connection.ConnectionUtils#checkGeneralJdbcJarFilePathDriverClassName(org.talend.core.model.metadata.builder.connection.DatabaseConnection)}
     * . case 2 driverClass is null or empty
     */
    @Test
    public void testCheckGeneralJdbcJarFilePathDriverClassNameCase2() {
        String driverClass = StringUtils.EMPTY;
        String driverName = "mysql-connector-java-5.1.12-bin.jar"; //$NON-NLS-1$
        CopyTheJarFile();

        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setDriverClass(driverClass);
        createDatabaseConnection.setDriverJarPath(driverName);
        try {
            // driver class is empty case
            ReturnCode rc = ConnectionUtils.checkJdbcJarFilePathDriverClassName(createDatabaseConnection);

            Assert.assertFalse("The driver of conection is empty so that should not be found", rc.isOk()); //$NON-NLS-1$
            Assert.assertEquals(Messages.getString("ConnectionUtils.DriverClassEmpty"), rc.getMessage()); //$NON-NLS-1$

            // driver class is Null case
            driverClass = null;
            createDatabaseConnection.setDriverClass(driverClass);
            rc = ConnectionUtils.checkJdbcJarFilePathDriverClassName(createDatabaseConnection);

            Assert.assertFalse("The driver of conection is Null so that should not be found", rc.isOk()); //$NON-NLS-1$
            Assert.assertEquals(Messages.getString("ConnectionUtils.DriverClassEmpty"), rc.getMessage()); //$NON-NLS-1$

        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.cwm.db.connection.ConnectionUtils#checkGeneralJdbcJarFilePathDriverClassName(org.talend.core.model.metadata.builder.connection.DatabaseConnection)}
     * . case 3 driverName is null or empty
     */
    @Test
    public void testCheckGeneralJdbcJarFilePathDriverClassNameCase3() {
        String driverClass = "om.mysql.jdbc.Driver"; //$NON-NLS-1$
        String driverName = StringUtils.EMPTY;
        CopyTheJarFile();

        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setDriverClass(driverClass);
        createDatabaseConnection.setDriverJarPath(driverName);
        try {
            // driver name is empty case
            ReturnCode rc = ConnectionUtils.checkJdbcJarFilePathDriverClassName(createDatabaseConnection);

            Assert.assertFalse("The class of driver is empty so that should not be found", rc.isOk()); //$NON-NLS-1$
            Assert.assertEquals(Messages.getString("ConnectionUtils.DriverJarFileEmpty"), rc.getMessage()); //$NON-NLS-1$

            // driver name is Null case
            driverClass = null;
            createDatabaseConnection.setDriverJarPath(driverName);
            rc = ConnectionUtils.checkJdbcJarFilePathDriverClassName(createDatabaseConnection);

            Assert.assertFalse("The class of driver is Null so that should not be found", rc.isOk()); //$NON-NLS-1$
            Assert.assertEquals(Messages.getString("ConnectionUtils.DriverJarFileEmpty"), rc.getMessage()); //$NON-NLS-1$

        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.cwm.db.connection.ConnectionUtils#checkGeneralJdbcJarFilePathDriverClassName(org.talend.core.model.metadata.builder.connection.DatabaseConnection)}
     * . case 4 The jar can not be found case
     */
    @Test
    public void testCheckGeneralJdbcJarFilePathDriverClassNameCase4() {
        String driverClass = "om.mysql.jdbc.Driver"; //$NON-NLS-1$
        String driverName = "mysql-connector-java-5.1.12-bin111111.jar"; //$NON-NLS-1$
        CopyTheJarFile();

        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setDriverClass(driverClass);
        createDatabaseConnection.setDriverJarPath(driverName);
        try {
            ReturnCode rc = ConnectionUtils.checkJdbcJarFilePathDriverClassName(createDatabaseConnection);

            Assert.assertFalse("The driver is not exist so that should not be found", rc.isOk()); //$NON-NLS-1$
            Assert.assertEquals(Messages.getString("ConnectionUtils.JarFileCanNotBeFound"), rc.getMessage()); //$NON-NLS-1$
        } catch (MalformedURLException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Copy The jar file if it is not exist or not new one.
     * 
     * @throws URISyntaxException
     */
    private void CopyTheJarFile() {
        try {
            File sourceFile = null;
            URL fileURL = null;
            try {
                URL resource = CWMPlugin.getDefault().getBundle()
                        .getResource("jdbc" + Path.SEPARATOR + "mysql-connector-java-5.1.12-bin.jar"); //$NON-NLS-1$ //$NON-NLS-2$
                fileURL = FileLocator.toFileURL(resource);
                URI uri = fileURL.toURI();
                sourceFile = new File(uri);
            } catch (IllegalArgumentException e) {
                try {
                    Assert.fail("url is: " + fileURL.toString() + "URI is: " + fileURL.toURI().toString()); //$NON-NLS-1$ //$NON-NLS-2$
                } catch (URISyntaxException e1) {
                    Assert.fail(e1.getMessage());
                }
            } catch (URISyntaxException e) {
                Assert.fail(e.getMessage());
            }
            if (sourceFile == null) {
                Assert.fail("source file is not exist"); //$NON-NLS-1$
            }
            String tempLibPath = ExtractMetaDataUtils.getInstance().getJavaLibPath();
            File targetFile = new File(tempLibPath + "mysql-connector-java-5.1.12-bin.jar"); //$NON-NLS-1$
            FilesUtils.copyFile(sourceFile, targetFile);
        } catch (IOException e1) {
            Assert.fail(e1.getMessage());
        }
    }

    /**
     * Test method for
     * {@link org.talend.cwm.db.connection.ConnectionUtils#checkUsernameBeforeSaveConnection4Sqlite(org.talend.core.model.metadata.builder.connection.Connection)}
     * .
     */
    @Test
    public void testCheckUsernameBeforeSaveConnection4Sqlite() {
        DatabaseConnection sqliteConn = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        sqliteConn.setDatabaseType(SupportDBUrlType.SQLITE3DEFAULTURL.getDBKey());
        sqliteConn.setUsername(""); //$NON-NLS-1$
        sqliteConn.setContextMode(false);
        ConnectionUtils.checkUsernameBeforeSaveConnection4Sqlite(sqliteConn);
        Assert.assertTrue(JavaSqlFactory.DEFAULT_USERNAME.equals(sqliteConn.getUsername()));

        String username = "abc"; //$NON-NLS-1$
        sqliteConn.setUsername(username);
        ConnectionUtils.checkUsernameBeforeSaveConnection4Sqlite(sqliteConn);
        Assert.assertFalse(JavaSqlFactory.DEFAULT_USERNAME.equals(sqliteConn.getUsername()));
        Assert.assertTrue(username.equals(sqliteConn.getUsername()));
    }

    @Test
    public void testIsConnectionAvailable_generalJDBC_false() {
        String driverClass = "om.mysql.jdbc.Driver"; //$NON-NLS-1$
        String driverName = "mysql-connector-java-5.1.12-bin.jar"; //$NON-NLS-1$
        CopyTheJarFile();
        DatabaseConnection createDatabaseConnection = ConnectionFactoryImpl.eINSTANCE.createDatabaseConnection();
        createDatabaseConnection.setDriverClass(driverClass);
        createDatabaseConnection.setDriverJarPath(driverName);
        createDatabaseConnection.setUsername("root"); //$NON-NLS-1$
        createDatabaseConnection.setPassword("root"); //$NON-NLS-1$
        createDatabaseConnection.setDatabaseType("General JDBC"); //$NON-NLS-1$

        ReturnCode ret = ConnectionUtils.isConnectionAvailable(createDatabaseConnection);
        Assert.assertFalse(ret.isOk());

    }
}
